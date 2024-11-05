package com.tlg.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tlg.model.MemberDAO;

@WebServlet("/DeleteController")
public class DeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String pw = request.getParameter("pw");
		
		MemberDAO dao = new MemberDAO();
		
		dao.delete(pw);
		
		HttpSession session = request.getSession();
		
		session.removeAttribute("member");
		
		response.sendRedirect("mainPage.jsp");
			
	}

}
