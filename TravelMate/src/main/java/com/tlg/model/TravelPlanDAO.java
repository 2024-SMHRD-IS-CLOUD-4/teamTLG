package com.tlg.model;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class TravelPlanDAO {

	SqlSessionFactory factory = com.tlg.database.SqlSessionManager.getSqlSessionFactory();

	public int insertPlan(TravelPlan plan) {
		SqlSession session = factory.openSession(true);
		int result = session.insert("TravelPlanMapper.insertPlan", plan);
		session.close();
		return result;
	}

	public List<TravelPlan> selectPlans(String id) {
	    SqlSession session = factory.openSession();
	    List<TravelPlan> result = null;
	    try {
	        result = session.selectList("com.tlg.database.TravelPlanMapper.selectPlan", id);
	    } catch (Exception e) {
	        e.printStackTrace(); // 오류 로그 출력
	    } finally {
	        session.close();
	    }
	    return result;
	}
}
