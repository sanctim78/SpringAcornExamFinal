package com.gura.spring07.file.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gura.spring07.file.dto.FileDto;

@Repository
public class FileDaoImpl implements FileDao{
	@Autowired
	private SqlSession session;

	@Override
	public int getCount(FileDto dto) {
		
		return session.selectOne("file.getCount", dto);
	}

	@Override
	public List<FileDto> getList(FileDto dto) {
		
		return session.selectList("file.getList", dto);
	}
	
}




