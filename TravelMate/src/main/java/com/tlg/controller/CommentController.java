package com.tlg.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tlg.model.Comment;
import com.tlg.model.CommentDAO;
import com.tlg.model.TmMember;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/CommentController")
public class CommentController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    CommentDAO commentDAO = new CommentDAO();
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	HttpSession session = request.getSession();
        TmMember member =  (TmMember)session.getAttribute("member");
		String user_id = member.getId();
        
        String tr_idxStr = request.getParameter("tr_idx");

        if (tr_idxStr == null || tr_idxStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Missing or invalid tr_idx parameter\"}");
            return;
        }

        try {
            int tr_idx = Integer.parseInt(tr_idxStr);

            // 댓글 목록 불러오기
            List<Comment> comments = commentDAO.getCommentsByTravelPlan(tr_idx);
            String json = new Gson().toJson(comments);
            System.out.println("댓글 가져올때 tr_idx 확인 : " +  tr_idx);
            System.out.println("댓글 가져올때 json 객체 확인 : " + json);
           
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(json);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid tr_idx format\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("member") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"로그인이 필요합니다.\"}");
            return;
        }

        TmMember member = (TmMember) session.getAttribute("member");
        String user_id = member.getId();
        System.out.println("user_id 뭐냐? " + user_id);

        StringBuilder sb = new StringBuilder();
        String line;

        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        String jsonData = sb.toString();
        System.out.println("json 데이터 확인! " + jsonData);
        
        Gson gson = new Gson();

        try {
            JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
            System.out.println("파싱된 json객체 : " + jsonObject.toString());
            if (jsonObject == null || !jsonObject.has("action")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Missing or invalid action parameter\"}");
                return;
            }

            String action = jsonObject.get("action").getAsString();

            // 댓글 추가
            if ("addComment".equals(action)) {
                if (!jsonObject.has("tr_idx") || !jsonObject.has("content")) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\":\"Missing tr_idx or content parameter\"}");
                    return;
                }

                int tr_idx = Integer.parseInt(jsonObject.get("tr_idx").getAsString());
                String content = jsonObject.get("cmt_content").getAsString();
                
                System.out.println("댓글추가할때 action은 뭔데 " + action.toString());
                System.out.println("tr_idx 뭔데 " + tr_idx);

                Comment comment = new Comment();
                comment.setTr_idx(tr_idx);
                comment.setCmt_content(content);
                comment.setId(user_id);  // 세션에서 가져온 user_id를 설정합니다.

                int comment_idx = commentDAO.insertComment(comment);

                if (comment_idx == 0) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("{\"error\":\"Failed to add comment\"}");
                    return;
                }

                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("comment_idx", comment_idx);
                jsonResponse.addProperty("content", content);
                response.getWriter().write(jsonResponse.toString());

            } else if ("deleteComment".equals(action)) {
                if (!jsonObject.has("comment_idx")) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\":\"Missing comment_idx parameter\"}");
                    return;
                }

                int comment_idx = jsonObject.get("comment_idx").getAsInt();
                int result = commentDAO.deleteComment(comment_idx);

                if (result == 0) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("{\"error\":\"Failed to delete comment\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\":\"Comment deleted successfully\"}");
                }

            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Invalid action\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Failed to process request\"}");
        }
    }
}
