package com.tlg.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tlg.model.MemberDAO;
import com.tlg.model.TmMember;

@WebServlet("/TravelBoardJoinController")
public class TravelBoardJoinController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String name = request.getParameter("name");
		String nick = request.getParameter("nick");
		String gender = request.getParameter("gender");
		String email = request.getParameter("email");
		
		TmMember joinMember = new TmMember(id, pw, name, nick, gender, email);
		
		MemberDAO dao = new MemberDAO();
		
		int result = dao.join(joinMember);
		
		if(result > 0) {
			response.sendRedirect("TravelBoard.jsp");
		} else {
			response.sendRedirect("TravelBoard.jsp");
		}
		
	}

}
