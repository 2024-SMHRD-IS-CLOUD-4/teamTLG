package com.tlg.model;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class CheckListDAO {
	
	SqlSessionFactory factory = com.tlg.database.SqlSessionManager.getSqlSessionFactory();
	
	public int item(String item) {
		SqlSession sqlSession = factory.openSession(true);
		int result = sqlSession.insert("CheckListMapper.item", item);
		sqlSession.close();
		return result;
	}
	
	public List<CheckList> getItem() {
		SqlSession session = factory.openSession(true);
		List<CheckList> result = session.selectList("CheckListMapper.getItem");
		session.close();
		return result;
	}

	public int delete(int item_idx) {
		SqlSession sqlSession = factory.openSession(true);
		int result = sqlSession.delete("CheckListMapper.delete", item_idx);
		sqlSession.close();
		return result;
	}

}
