package com.tlg.model;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class ScheduleDAO {
	SqlSessionFactory factory = com.tlg.database.SqlSessionManager.getSqlSessionFactory();

	// KanbanCard 테이블에서 card_title 가져오는 메서드
	public String getCardTitle(int card_idx) {
		try (SqlSession session = factory.openSession(true)) {
			return session.selectOne("ScheduleMapper.getCardTitle", card_idx);
		}
	}

	// Schedule 테이블에서 추가 정보를 가져오는 메서드
	public Schedule getScheduleDetails(int card_idx) {
		try (SqlSession session = factory.openSession(true)) {
			Schedule result = session.selectOne("ScheduleMapper.getScheduleDetails", card_idx);
			if (result != null) {
				System.out.println("DAO에서 가져온 스케줄정보 : " + result.toString());
			}
			return result;
		}
	}

	// 특정 column_id에 속하는 스케줄 삭제
	public int deleteSchedulesByColumnId(int columnId) {
		try (SqlSession session = factory.openSession(true)) {
			return session.delete("ScheduleMapper.deleteSchedulesByColumnId", columnId);
		}
	}

	// 특정 card_id에 속하는 스케줄 삭제
	public int deleteSchedulesByCardId(int cardId) {
		try (SqlSession session = factory.openSession(true)) {
			return session.delete("ScheduleMapper.deleteSchedulesByCardId", cardId);
		}
	}

	public boolean saveSchedule(Schedule schedule) {
		try (SqlSession session = factory.openSession(true)) {
			int result = session.insert("ScheduleMapper.insertSchedule", schedule);
			return result > 0; // 성공적으로 삽입된 경우 true 반환
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateSchedule(Schedule schedule) {
		SqlSession session = factory.openSession();
		try {
			int result = session.update("updateSchedule", schedule);
			session.commit();
			return result > 0;
		} finally {
			session.close();
		}
	}

	// 새로운 스케줄 생성
	public boolean createSchedule(Schedule schedule) {
		try (SqlSession session = factory.openSession(true)) {
			int result = session.insert("ScheduleMapper.createSchedule", schedule);
			System.out.println(result);
			return result > 0; // 생성된 경우 true 반환
		} catch (Exception e) {
			e.printStackTrace();
			return false; // 예외 발생 시 false 반환
		}
	}

}
