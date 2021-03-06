package com.gura.spring07.users.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gura.spring07.users.dto.UsersDto;
/*
 *  users 에 관련된 DB 에 
 *  INSERT, UPDATE, DELETE, SELECT 작업을 할 객체 
 */
@Repository
public class UsersDaoImpl implements UsersDao{
	//의존객체를 주입 받기 위해 
	@Autowired
	private SqlSession session;

	@Override
	public void insert(UsersDto dto) {
		/*
		 *  mapper namespace : users
		 *  sql id : insert
		 *  parameterType : UsersDto 
		 */
		session.insert("users.insert", dto);
	}

	@Override
	public boolean isValid(UsersDto dto) {
		/*
		 *  parameterType : UsersDto
		 *  resultType : String
		 */
		String id=session.selectOne("users.getId", dto);
		if(id == null) { //아이디 혹은 비밀번호가 틀리면 null 이다. 
			return false;
		}else { // null 이 아니면 유효한 정보이다. 
			return true; 
		}
	}
	//인자로 전달된 아이디가 존재하는지 여부를 리턴하는 메소드 
	@Override
	public boolean isExist(String inputId) {
		String selectedId=session.selectOne("users.isExist", inputId);
		if(selectedId==null) {// null 이면 해당아이디는 존재 하지 않는것이다.
			return false;
		}else {
			return true;
		}
	}

	@Override
	public UsersDto getData(String id) {
		
		return session.selectOne("users.getData", id);
	}

	@Override
	public void update(UsersDto dto) {
		session.update("users.update", dto);
	}

	@Override
	public void delete(String id) {
		session.delete("users.delete", id);
	}

	@Override
	public String getPwdHash(String id) {
		//비밀번호 hash 값을 select 해서 리턴해준다.
		return session.selectOne("users.getPwd", id);
	}

	@Override
	public void updatePwd(UsersDto dto) {
		//비밀번호만 수정하기 
		session.update("users.updatePwd", dto);
	}
	
}











