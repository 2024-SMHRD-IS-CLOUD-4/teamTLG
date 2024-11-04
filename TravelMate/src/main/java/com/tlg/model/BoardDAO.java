package com.tlg.model;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.tlg.database.SqlSessionManager;

public class BoardDAO {

	SqlSessionFactory factory = SqlSessionManager.getSqlSessionFactory();
	
	// 글작성 메소드
	public int write(TmBoard uploadBoard) {
		SqlSession session = factory.openSession(true);
		int result = session.insert("BoardMapper.write", uploadBoard);
		session.close();
		return result;
	}
	
	public List<TmBoard> getBoard() {
		SqlSession session = factory.openSession(true);
		List<TmBoard> result = session.selectList("BoardMapper.getBoard");
		session.close();
		return result;
	}
	
}
