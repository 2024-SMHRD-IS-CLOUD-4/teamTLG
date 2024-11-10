package com.tlg.model;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class KanbanDAO {

	// SqlSessionFactory 인스턴스를 가져옵니다.
	SqlSessionFactory factory = com.tlg.database.SqlSessionManager.getSqlSessionFactory();

	// 특정 여행 계획(tr_idx)에 속한 Kanban 컬럼과 카드 정보 조회 메소드
	public List<KanbanColumn> getKanbanData(int tr_idx) {
		SqlSession session = factory.openSession(); // select도 true로 설정해야하나..?
		List<KanbanColumn> kanbanData = session.selectList("KanbanMapper.getKanbanData", tr_idx);
		session.close();

		// 디버깅용 출력
		System.out.println("==== Kanban Data ====");
		for (KanbanColumn column : kanbanData) {
			System.out.println("Column Title: " + column.getCol_title());
			System.out.println("Column Order: " + column.getCol_order());
			System.out.println("Column ID: " + column.getCol_idx());
			System.out.println("Cards: ");
			for (KanbanCard card : column.getCards()) {
				System.out.println("  Card Title: " + card.getCard_title());
				System.out.println("  Card Order: " + card.getCard_order());
				System.out.println("  Card ID: " + card.getCard_idx());
			}
		}
		System.out.println("==== End of Kanban Data ====");

		return kanbanData;
	}

	// Kanban 컬럼 추가 메서드
	public int insertColumn(KanbanColumn column) {
		SqlSession session = factory.openSession(false); // 자동 커밋 해제
		try {
			session.insert("KanbanMapper.insertColumn", column);
			session.commit(); // 성공 시 커밋
			System.out.println("db에서 받아온 col_idx : " + column.getCol_idx());
			return column.getCol_idx();
		} catch (Exception e) {
			session.rollback(); // 오류 발생 시 롤백
			throw e; // 예외를 다시 던져서 호출한 곳에서 처리할 수 있게 함
		} finally {
			session.close(); // 세션 닫기
		}
	}

	// 카드 추가
	public int insertCard(KanbanCard card) {
		SqlSession session = factory.openSession(false); // 자동 커밋 해제
		try {
			session.insert("KanbanMapper.insertCard", card);
			session.commit(); // 성공 시 커밋
			System.out.println("db에서 받아온 card_idx : " + card.getCol_idx());
			return card.getCard_idx();
		} catch (Exception e) {
			session.rollback(); // 오류 발생 시 롤백
			throw e; // 예외를 다시 던져서 호출한 곳에서 처리할 수 있게 함
		} finally {
			session.close(); // 세션 닫기
		}
	}

	// 컬럼 순서 업데이트 메서드
	public void updateColumnOrder(List<KanbanColumn> columns) {
		SqlSession session = factory.openSession(false); // 자동 커밋 해제
		try {
			for (KanbanColumn column : columns) {
				session.update("KanbanMapper.updateColumnOrder", column);
			}
			session.commit(); // 모든 업데이트가 성공하면 커밋
		} catch (Exception e) {
			session.rollback(); // 오류 발생 시 롤백
			throw e;
		} finally {
			session.close(); // 세션 닫기
		}
	}

	// 카드 순서 업데이트 메서드
	public void updateCardOrder(List<KanbanCard> cards) {
		SqlSession session = factory.openSession(false); // 자동 커밋 해제
		try {
			for (KanbanCard card : cards) {
				session.update("KanbanMapper.updateCardOrder", card);
			}
			session.commit(); // 모든 업데이트가 성공하면 커밋
		} catch (Exception e) {
			session.rollback(); // 오류 발생 시 롤백
			throw e;
		} finally {
			session.close(); // 세션 닫기
		}
	}

	public void deleteCard(int card_idx) {
		SqlSession session = factory.openSession(false);
		try {
			session.delete("KanbanMapper.deleteCard", card_idx);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}
	
	public int updateCardTitle(KanbanCard card) {
	    SqlSession session = factory.openSession(false); // 자동 커밋 해제
	    try {
	        int result = session.update("KanbanMapper.updateCardTitle", card);
	        session.commit(); // 성공 시 커밋
	        return result;
	    } catch (Exception e) {
	        session.rollback(); // 오류 발생 시 롤백
	        throw e; // 예외를 다시 던져서 호출한 곳에서 처리할 수 있게 함
	    } finally {
	        session.close(); // 세션 닫기
	    }
	}

	public int updateColumnTitle(KanbanColumn column) {
	    SqlSession session = factory.openSession(false); // 자동 커밋 해제
	    try {
	        int result = session.update("KanbanMapper.updateColumnTitle", column);
	        session.commit(); // 성공 시 커밋
	        return result;
	    } catch (Exception e) {
	        session.rollback(); // 오류 발생 시 롤백
	        throw e; // 예외를 다시 던져서 호출한 곳에서 처리할 수 있게 함
	    } finally {
	        session.close(); // 세션 닫기
	    }
	}

	public void deleteCardsByColumn(int col_idx) {
	    SqlSession session = factory.openSession();
	    try {
	        session.delete("deleteCardsByColumn", col_idx);
	        session.commit();
	    } finally {
	        session.close();
	    }
	}

	public int deleteColumn(int col_idx) {
	    SqlSession session = factory.openSession();
	    try {
	        int result = session.delete("deleteColumn", col_idx);
	        session.commit();
	        return result;
	    } finally {
	        session.close();
	    }
	}

	public KanbanCard getKanbanDataByCardId(int cardId) {
		SqlSession session = factory.openSession();
		try {
			
			return session.selectOne("KanbanMapper.getCardById",cardId);
		} finally {
			session.close();
		}
	}


}
