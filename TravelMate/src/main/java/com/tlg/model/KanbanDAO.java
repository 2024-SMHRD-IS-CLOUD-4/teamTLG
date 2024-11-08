package com.tlg.model;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class KanbanDAO {
	private final SqlSessionFactory sqlSessionFactory;

	// 생성자에서 SqlSessionFactory를 초기화합니다.
	public KanbanDAO(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;

		// 매핑이 로드되었는지 테스트 (테스트 완료 후 삭제해도 됩니다)
		try (SqlSession session = sqlSessionFactory.openSession()) {
			session.getConfiguration().getMappedStatement("KanbanMapper.getKanbanData");
			System.out.println("KanbanMapper.getKanbanData 매핑을 찾았습니다.");
		} catch (Exception e) {
			System.out.println("KanbanMapper.getKanbanData 매핑을 찾지 못했습니다.");
			e.printStackTrace();
		}
	}

	// 특정 여행 계획(tr_idx)에 속한 Kanban 컬럼과 카드 정보 조회 메소드
	public List<KanbanColumn> getKanbanData(int tr_idx) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			List<KanbanColumn> kanbanData = session.selectList("KanbanMapper.getKanbanData", tr_idx);

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
	}

	/*
	 * // 특정 컬럼의 정보를 조회하는 메소드 (필요한 경우) public KanbanColumn getColumn(int col_idx) {
	 * SqlSession session = factory.openSession(); KanbanColumn result =
	 * session.selectOne("KanbanMapper.getColumn", col_idx); session.close(); return
	 * result; }
	 */
	/*
	 * // 특정 카드의 정보를 조회하는 메소드 (필요한 경우) public KanbanCard getCard(int card_idx) {
	 * SqlSession session = factory.openSession(true); KanbanCard result =
	 * session.selectOne("KanbanMapper.getCard", card_idx); session.close(); return
	 * result; }
	 */
}
