package com.gura.spring07.cafe.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gura.spring07.cafe.dao.CafeCommentDao;
import com.gura.spring07.cafe.dao.CafeDao;
import com.gura.spring07.cafe.dto.CafeCommentDto;
import com.gura.spring07.cafe.dto.CafeDto;

@Service
public class CafeServiceImpl implements CafeService{
	@Autowired
	private CafeDao cafeDao;
	@Autowired
	private CafeCommentDao cafeCommentDao;
	//한 페이지에 나타낼 row 의 갯수 
	static final int PAGE_ROW_COUNT=5;
	//하단 디스플레이 페이지 갯수 
	static final int PAGE_DISPLAY_COUNT=3;
	
	@Override
	public void getList(HttpServletRequest request) {
		/*
		 *  request 에 검색 keyword 가 전달 될수도 있고 안될수도 있다.
		 *  1. 전달 안되는 경우 : home 에서 목록보기를 누른경우
		 *  2. 전달 되는 경우 : list.do 에서 검색어를 입력후 form 전송한
		 *     경우. 
		 *  3. 전달 되는 경우 : 이미 검색을 한 상태에서 페이지 링크를 
		 *     누른경우
		 */
		//검색과 관련된 파라미터를 읽어와 본다.
		String keyword=request.getParameter("keyword");
		String condition=request.getParameter("condition");
		//검색과 관련된 정보를 CafeDto 객체에 선택적으로 담는다.
		CafeDto dto=new CafeDto();
		if(keyword != null) {//검색 키워드가 전달된 경우
			if(condition.equals("titlecontent")) {//제목+내용 검색
				dto.setTitle(keyword);
				dto.setContent(keyword);
			}else if(condition.equals("title")) {//제목 검색
				dto.setTitle(keyword);
			}else if(condition.equals("writer")) {//작성자 검색
				dto.setWriter(keyword);
			}
			//request 에 검색 조건과 키워드 담기
			request.setAttribute("condition", condition);
			/*
			 *  검색 키워드에는 한글이 포함될 가능성이 있기 때문에
			 *  링크에 그대로 출력가능하도록 하기 위해 미리 인코딩을 해서
			 *  request 에 담아준다.
			 */
			String encodedKeyword=null;
			try {
				encodedKeyword=URLEncoder.encode(keyword, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//인코딩된 키워드와 인코딩 안된 키워드를 모두 담는다.
			request.setAttribute("encodedKeyword", encodedKeyword);
			request.setAttribute("keyword", keyword);
		}
		
		//보여줄 페이지의 번호
		int pageNum=1;
		//보여줄 페이지의 번호가 파라미터로 전달되는지 읽어와 본다.	
		String strPageNum=request.getParameter("pageNum");
		if(strPageNum != null){//페이지 번호가 파라미터로 넘어온다면
			//페이지 번호를 설정한다.
			pageNum=Integer.parseInt(strPageNum);
		}
		//보여줄 페이지 데이터의 시작 ResultSet row 번호
		int startRowNum=1+(pageNum-1)*PAGE_ROW_COUNT;
		//보여줄 페이지 데이터의 끝 ResultSet row 번호
		int endRowNum=pageNum*PAGE_ROW_COUNT;
		
		//전체 row 의 갯수를 읽어온다.
		int totalRow=cafeDao.getCount(dto);
		//전체 페이지의 갯수 구하기
		int totalPageCount=
				(int)Math.ceil(totalRow/(double)PAGE_ROW_COUNT);
		//시작 페이지 번호
		int startPageNum=
			1+((pageNum-1)/PAGE_DISPLAY_COUNT)*PAGE_DISPLAY_COUNT;
		//끝 페이지 번호
		int endPageNum=startPageNum+PAGE_DISPLAY_COUNT-1;
		//끝 페이지 번호가 잘못된 값이라면 
		if(totalPageCount < endPageNum){
			endPageNum=totalPageCount; //보정해준다. 
		}
		// startRowNum 과 endRowNum 을 CafeDto 객체에 담고 
		dto.setStartRowNum(startRowNum);
		dto.setEndRowNum(endRowNum);
		
		// startRowNum 과 endRowNum 에 해당하는 카페글 목록을 select 해 온다.
		List<CafeDto> list=cafeDao.getList(dto);
		
		//view 페이지에서 필요한 값을 request 에 담고 
		request.setAttribute("list", list);
		request.setAttribute("startPageNum", startPageNum);
		request.setAttribute("endPageNum", endPageNum);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("totalPageCount", totalPageCount);	
		//전체 글의 갯수도 request 에 담는다.
		request.setAttribute("totalRow", totalRow);		
	}

	@Override
	public void saveContent(CafeDto dto) {
		cafeDao.insert(dto);
	}

	@Override
	public void getDetail(HttpServletRequest request) {
		//글번호
		int num=Integer.parseInt(request.getParameter("num"));
		//검색과 관련된 파라미터를 읽어와 본다.
		String keyword=request.getParameter("keyword");
		String condition=request.getParameter("condition");
		//검색과 관련된 정보를 CafeDto 객체에 선택적으로 담는다.
		CafeDto dto=new CafeDto();
		if(keyword != null) {//검색 키워드가 전달된 경우
			if(condition.equals("titlecontent")) {//제목+내용 검색
				dto.setTitle(keyword);
				dto.setContent(keyword);
			}else if(condition.equals("title")) {//제목 검색
				dto.setTitle(keyword);
			}else if(condition.equals("writer")) {//작성자 검색
				dto.setWriter(keyword);
			}
			//request 에 검색 조건과 키워드 담기
			request.setAttribute("condition", condition);
			/*
			 *  검색 키워드에는 한글이 포함될 가능성이 있기 때문에
			 *  링크에 그대로 출력가능하도록 하기 위해 미리 인코딩을 해서
			 *  request 에 담아준다.
			 */
			String encodedKeyword=null;
			try {
				encodedKeyword=URLEncoder.encode(keyword, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//인코딩된 키워드와 인코딩 안된 키워드를 모두 담는다.
			request.setAttribute("encodedKeyword", encodedKeyword);
			request.setAttribute("keyword", keyword);
		}		
		//CafeDto 에 글번호도 담기
		dto.setNum(num);
		
		//글정보 얻어오기
		//CafeDto 객체를 getData() 의 인자로 전달해서 글정보를 얻어온다. 
		CafeDto resultDto=cafeDao.getData(dto);
		//글 조회수 올리기
		cafeDao.addViewCount(num);
		//request 에 글정보를 담고
		request.setAttribute("dto", resultDto);
		//댓글 목록을 얻어온다. (자세히 보기할 글번호가 댓글의 ref_group 번호)
		List<CafeCommentDto> commentList=cafeCommentDao.getList(num);
		//request 에 댓글 목록을 담는다.
		request.setAttribute("commentList", commentList);		
	}
	
}












