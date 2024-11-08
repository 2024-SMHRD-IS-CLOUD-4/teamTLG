package com.tlg.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tlg.model.CheckListDAO;

@WebServlet("/CheckListDeleteController")
public class CheckListDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		int item_idx =  Integer.parseInt(request.getParameter("item_idx"));
		
		CheckListDAO dao = new CheckListDAO();
		
		dao.delete(item_idx);
		
		response.sendRedirect("checkList.jsp");
	}

}
