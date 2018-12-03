package com.gura.spring07.file.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gura.spring07.file.service.FileService;

@Controller
public class FileController {
	@Autowired
	private FileService service;
	
	@RequestMapping("/file/list")
	public String getList(HttpServletRequest request) {
		//FileService 객체의 getList() 메소드에 HttpServletRequest 객체의 
		//참조값을 전달해서 view 페이지에 필요한 값이 request 영역에 담기게 한다. 
		service.getList(request);
		// view 페이지로 forward 이동해서 파일 목록 출력하기 
		return "file/list";
	}
}








