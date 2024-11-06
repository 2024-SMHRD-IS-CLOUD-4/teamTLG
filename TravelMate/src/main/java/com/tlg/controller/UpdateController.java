package com.tlg.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tlg.model.MemberDAO;
import com.tlg.model.TmMember;

@WebServlet("/UpdateController")
public class UpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String nick = request.getParameter("nick");
		
		TmMember updateMember = new TmMember(id, pw, nick);
		
		MemberDAO dao = new MemberDAO();
		
		int result = dao.update(updateMember);
		
		if(result > 0) {
			HttpSession session = request.getSession();
			session.setAttribute("member", updateMember);
			response.sendRedirect("mainPage.jsp");
		} else {
			response.sendRedirect("myPageIndex.jsp");
		}
	}

}
