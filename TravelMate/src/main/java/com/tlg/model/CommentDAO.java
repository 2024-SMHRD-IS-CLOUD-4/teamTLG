package com.tlg.model;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class CommentDAO {
	
	SqlSessionFactory factory = com.tlg.database.SqlSessionManager.getSqlSessionFactory();

	// 여행계획 댓글 추가
	public int insertComment(Comment cmt) {
    	SqlSession session = factory.openSession(true);
		int result = session.insert("TravelPlanMapper.PlanComment_input",cmt);
		session.close();
		return result;
    }

	public List<Comment> getKbComments(Comment cmt) {
		SqlSession session = factory.openSession(true);
		List<Comment> result = session.selectOne("TravelPlanMapper.PlanComment_output",cmt);
		session.close();
		return result;
	}

}
