package com.tlg.model;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class TravelPlanDAO {
    
	SqlSessionFactory factory = com.tlg.database.SqlSessionManager.getSqlSessionFactory();

	public int insertPlan(TravelPlan plan) {
    	SqlSession session = factory.openSession(true);
		int result = session.insert("TravelPlanMapper.insertPlan",plan);
		session.close();
		return result;
    }
	

    public TravelPlan selectPlan(String planId) {
       SqlSession session = factory.openSession(true);
       TravelPlan result = session.selectOne("TravlePlanMapper.selectPlan", planId);
       session.close();
       return result;
        
    }
}
