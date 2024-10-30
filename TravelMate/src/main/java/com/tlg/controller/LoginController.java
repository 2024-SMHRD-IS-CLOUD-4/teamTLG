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


@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		
		
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		
		
		TmMember loginMember = new TmMember(id, pw);
		

		MemberDAO dao = new MemberDAO();
		
		TmMember result = dao.login(loginMember);
		
		
		if(result == null) {
			// 로그인 실패
			response.sendRedirect("login.html");
		}else {
			// 세션에다가 회원 정보를 저장하고, index.jsp로 사용자를 보내주기!
			HttpSession session = request.getSession();
			session.setAttribute("member", result);
			response.sendRedirect("index.html");
		}
		
		
	
	}

}
