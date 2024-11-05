package com.tlg.model;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class ScheduleDAO {
	
	SqlSessionFactory factory = com.tlg.database.SqlSessionManager.getSqlSessionFactory();

    
    public int insertSchedule(Schedule schedule) {
    	SqlSession session = factory.openSession(true);
		int result = session.insert("ScheduleMapper.insertSchedule",schedule);
		session.close();
		return result;
        
    }

    public Schedule selectSchedule(String scheduleId) {
    	SqlSession session = factory.openSession(true);
		Schedule result = session.selectOne("ScheduleMapper.insertSchedule",scheduleId);
		session.close();
		return result;
    }
}
