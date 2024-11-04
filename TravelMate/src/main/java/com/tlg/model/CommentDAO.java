package com.tlg.model;

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

}
