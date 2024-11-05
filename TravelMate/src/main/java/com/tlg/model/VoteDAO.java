package com.tlg.model;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class VoteDAO {
	
	SqlSessionFactory factory = com.tlg.database.SqlSessionManager.getSqlSessionFactory();

	public int insertVote(Vote vote) {
    	SqlSession session = factory.openSession(true);
		int result = session.insert("VoteMapper.insertVote",vote);
		session.close();
		return result;
    }
	
	 public Vote selectVote(Vote vote) {
	       SqlSession session = factory.openSession(true);
	       Vote result = session.selectOne("VoteMapper.insertVote",vote);
	       session.close();
	       return result;
	        
	    }
	 
	 // 투표항목보여주는 메소드
	 
	 public List<Vote> choice(Vote vote) {
	       SqlSession session = factory.openSession(true);
	       List<Vote> result = session.selectList("VoteMapper.choice",vote);
	       session.close();
	       return result;
	        
	    }
}
