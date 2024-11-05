package com.tlg.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tlg.model.Schedule;
import com.tlg.model.ScheduleDAO;

@WebServlet("/ScheduleController")
public class ScheduleController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ScheduleDAO dao = new ScheduleDAO();

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	request.setCharacterEncoding("utf-8");
	
	// 여행기록
	
	int sche_idx = Integer.parseInt(request.getParameter("sche_idx")); 
	int tr_idx = Integer.parseInt(request.getParameter("tr_idx")); 
	String sche_type = request.getParameter("sche_type"); 
	String sche_dt = request.getParameter("sche_dt"); 
	String sche_tm = request.getParameter("sche_tm"); 
	String id = (request.getParameter("id")); 
	int sche_order = Integer.parseInt(request.getParameter("sche_order")); 
	String vote_result = request.getParameter("vote_result"); 
	
	
	
	
	
	
	}
}
