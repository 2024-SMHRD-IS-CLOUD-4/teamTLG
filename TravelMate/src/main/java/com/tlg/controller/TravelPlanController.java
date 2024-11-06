package com.tlg.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tlg.model.TmMember;
import com.tlg.model.TravelPlan;
import com.tlg.model.TravelPlanDAO;

@WebServlet("/TravelPlanController")
public class TravelPlanController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		
		System.out.println("TravelPlanController");
		// 세션에서 사용자 ID를 가져오기
		TmMember member =  (TmMember)request.getSession().getAttribute("member");
		String id = member.getId();
		System.out.println(id);
		if (id == null) {
			response.sendRedirect("mainPage.jsp"); // 로그인 페이지로 리디렉션
			return;
		}

		// 여행 계획 정보 저장을 위한 파라미터 수집
		String tr_title = request.getParameter("tr_title");
		String tr_st_dt_str = request.getParameter("tr_st_dt");
		String tr_ed_dt_str = request.getParameter("tr_ed_dt");
		
		System.out.println("tr_title: " + request.getParameter(tr_ed_dt_str));
		System.out.println("tr_st_dt: " + tr_st_dt_str);
		System.out.println("tr_ed_dt: " + tr_ed_dt_str);
		

		Date tr_st_dt = null;
		Date tr_ed_dt = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			// 날짜 형식 변환
			tr_st_dt = dateFormat.parse(tr_st_dt_str);
			tr_ed_dt = dateFormat.parse(tr_ed_dt_str);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "날짜 형식이 올바르지 않습니다.");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}

		// 여행 계획 객체 생성 후 데이터베이스에 저장
		TravelPlan plan = new TravelPlan(id, tr_title, tr_st_dt, tr_ed_dt);
		TravelPlanDAO tpDao = new TravelPlanDAO();
		System.out.println(plan.toString());
		int res = tpDao.insertPlan(plan);

		// 결과에 따라 페이지 이동
		if (res > 0) {
			response.sendRedirect("mainPage.jsp"); // 성공 시 여행 계획 리스트 페이지로 이동
		} else {
			request.setAttribute("error", "여행 계획을 저장하지 못했습니다.");
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}
}