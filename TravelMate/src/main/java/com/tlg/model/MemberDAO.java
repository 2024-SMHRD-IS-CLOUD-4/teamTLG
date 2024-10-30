package com.tlg.model;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.tlg.model.TmMember;
import com.tlg.database.SqlSessionManager;

public class MemberDAO {

	SqlSessionFactory factory = com.tlg.database.SqlSessionManager.getSqlSessionFactory();
	
	public int join(TmMember joinMember) {
		SqlSession sqlSession = factory.openSession(true);
		int result = sqlSession.insert("MemberMapper.insert", joinMember);
		sqlSession.close();
		return result;
	}
	
	public TmMember login(TmMember loginMember) {
		SqlSession session = factory.openSession(true);
		TmMember result = session.selectOne("MemberMapper.login", loginMember);
		session.close();
		return result;
	}

	
}
