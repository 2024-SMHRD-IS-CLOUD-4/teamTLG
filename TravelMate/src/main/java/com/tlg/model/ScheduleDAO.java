package com.tlg.model;

import com.tlg.database.SqlSessionManager;
import org.apache.ibatis.session.SqlSession;

public class ScheduleDAO {
    
    public void insertSchedule(Schedule schedule) {
        try (SqlSession session = SqlSessionManager.getSqlSessionFactory().openSession()) {
            session.insert("TravelScheduleMapper.insertSchedule", schedule);
            session.commit();
        }
    }

    public Schedule selectSchedule(String scheduleId) {
        try (SqlSession session = SqlSessionManager.getSqlSessionFactory().openSession()) {
            return session.selectOne("TravelScheduleMapper.selectSchedule", scheduleId);
        }
    }
}
