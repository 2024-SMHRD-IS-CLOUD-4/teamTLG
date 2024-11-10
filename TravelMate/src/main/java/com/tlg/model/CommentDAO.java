package com.tlg.model;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class CommentDAO {

    SqlSessionFactory factory = com.tlg.database.SqlSessionManager.getSqlSessionFactory();

    // 여행계획 댓글 추가
    public int insertComment(Comment cmt) {
        try (SqlSession session = factory.openSession(true)) {
            return session.insert("CommentMapper.PlanComment_input", cmt);
        }
    }

    // 여행계획 댓글 가져오기
    public List<Comment> getCommentsByTravelPlan(int tr_idx) {
        try (SqlSession session = factory.openSession()) {
            return session.selectList("CommentMapper.getComment", tr_idx);
        }
    }

    // 여행계획 댓글 삭제
    public int deleteComment(int comment_idx) {
        try (SqlSession session = factory.openSession()) {
            int result = session.delete("CommentMapper.deleteComment", comment_idx);
            session.commit();
            return result;
        }
    }
}
