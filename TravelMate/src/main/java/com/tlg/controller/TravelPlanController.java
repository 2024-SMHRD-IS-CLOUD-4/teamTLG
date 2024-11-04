package com.tlg.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tlg.model.Comment;
import com.tlg.model.CommentDAO;
import com.tlg.model.TravelPlan;
import com.tlg.model.TravelPlanDAO;

@WebServlet("/TravelPlanController")
public class TravelPlanController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 여행계획 정보 삽입
		
		String id = request.getParameter("id"); 
		String tr_title = request.getParameter("tr_title");
		String tr_st_dt = request.getParameter("tr_st_dt");
		String tr_ed_dt = request.getParameter("tr_ed_dt");
		String tr_desc = request.getParameter("tr_desc");
	
		// 댓글
		int c_idx =Integer.parseInt(request.getParameter("c_idx"));
		String cmt_content = request.getParameter("cmt_content");
		String create_at = request.getParameter("create_at");
		int cmt_heart = Integer.parseInt(request.getParameter("cmt_heart"));
		int tr_idx = Integer.parseInt(request.getParameter("tr_idx"));

		TravelPlan plan = new TravelPlan(id, tr_title, tr_st_dt, tr_ed_dt, tr_desc); // 여행계획데이터 묶음
		Comment comment = new Comment(c_idx, cmt_content, create_at, cmt_heart, id, tr_idx); // 동행자들의 댓글에 대한 묶음
		
		TravelPlanDAO tpDao = new TravelPlanDAO();
		CommentDAO cmDao = new CommentDAO();
		
		
		int insert_plan = tpDao.insertPlan(plan);
		
		if(insert_plan>0) {
			HttpSession session = request.getSession();
			session.setAttribute("plan",plan);
			response.sendRedirect("");
		}else {
			
		}
		
		int insert_comment = cmDao.insertComment(comment);

		if(insert_comment>0) {
			HttpSession session = request.getSession();
			session.setAttribute("plan_cmt",comment);
			response.sendRedirect("");
		}else {
			
		}
	}

}
