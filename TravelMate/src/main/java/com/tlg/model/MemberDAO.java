package com.tlg.model;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

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
