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
			int col_idx = session.insert("KanbanMapper.insertColumn", column);
			session.commit(); // 성공 시 커밋
			return col_idx;
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
			int card_idx = session.insert("KanbanMapper.insertCard", card);
			session.commit(); // 성공 시 커밋
			return card_idx;
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
}
	
	/*
	 * // 특정 컬럼(col_idx)에 속한 카드 중 가장 큰 card_order를 가져오는 메서드 public int
	 * getMaxCardOrder(int col_idx) { SqlSession session = factory.openSession();
	 * try { int maxCardOrder = session.selectOne("KanbanMapper.getMaxCardOrder",
	 * col_idx); return maxCardOrder; } finally { session.close(); } }
	 */

	/*
	 * // 컬럼의 order중에 가장 큰 값을 가져옴. public int getMaxColOrder(int tr_idx) {
	 * SqlSession session = factory.openSession(); int maxColOrder =
	 * session.selectOne("KanbanMapper.getMaxColOrder", tr_idx); session.close();
	 * return maxColOrder; }
	 */

