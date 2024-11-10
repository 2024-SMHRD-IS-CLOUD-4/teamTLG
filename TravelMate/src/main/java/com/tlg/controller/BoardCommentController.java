package com.tlg.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tlg.model.BoardComment;
import com.tlg.model.BoardCommentDAO;

@WebServlet("/BoardCommentController")
public class BoardCommentController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		int c_idx = Integer.parseInt(request.getParameter("c_idx"));
		String cmt_content = request.getParameter("cmt_content");
		String id = request.getParameter("id");
		String create_at = request.getParameter("created_at");
		
		BoardCommentDAO dao = new BoardCommentDAO();
		
		BoardComment writeComment = new BoardComment(c_idx, cmt_content, id, create_at);
		
		dao.cmt_content(writeComment);
		
		response.sendRedirect("postView.jsp?c_idx=" + c_idx);
		
	}

}
