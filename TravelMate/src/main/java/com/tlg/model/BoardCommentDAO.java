package com.tlg.model;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.tlg.database.SqlSessionManager;

public class BoardCommentDAO {
	
	SqlSessionFactory factory = SqlSessionManager.getSqlSessionFactory();
	
	public int cmt_content(BoardComment writeComment) {
		SqlSession sqlSession = factory.openSession(true);
		int result = sqlSession.insert("BoardCommentMapper.cmt_content", writeComment);
		sqlSession.close();
		return result;
	}
	
	public BoardComment getComment (int c_idx) {
		SqlSession session = factory.openSession(true);
		BoardComment result = session.selectOne("BoardCommentMapper.getComment", c_idx);
		session.close();
		return result;
	}
	
	public List<BoardComment> listComment() {
		SqlSession session = factory.openSession(true);
		List<BoardComment> result = session.selectList("BoardCommentMapper.listComment");
		session.close();
		return result;
	}
	
	public List<BoardComment> listCommentByPostId(int c_idx) {
	    SqlSession session = factory.openSession(true);
	    List<BoardComment> result = session.selectList("BoardCommentMapper.listCommentByPostId", c_idx);
	    session.close();
	    return result;
	}
	
}
