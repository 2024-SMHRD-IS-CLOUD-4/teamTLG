package com.tlg.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tlg.model.Vote;
import com.tlg.model.VoteDAO;

@WebServlet("/VoteController")
public class VoteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		// 투표 입력
	request.setCharacterEncoding("utf-8");
	
	String id = request.getParameter("id");
	int vote_idx = Integer.parseInt(request.getParameter("vote_idx"));
	int sche_idx = Integer.parseInt(request.getParameter("sche_idx"));
	int vote_score = Integer.parseInt(request.getParameter("vote_score"));
	String create_at = request.getParameter("create_at");
	String vote_result = request.getParameter("vote_result");
	int tr_idx = Integer.parseInt(request.getParameter("tr_idx"));
	String[] vote_choice = request.getParameterValues("vote_choice");
	
	Vote inputVote = new Vote(vote_idx, sche_idx, vote_score, create_at, id, vote_result, tr_idx);
	
	VoteDAO vdao = new VoteDAO();
	
	int result = vdao.insertVote(inputVote);
	
	if(result>0) {
		HttpSession session = request.getSession();
		session.setAttribute("vote_result",inputVote);
		response.sendRedirect("kb_sub.jsp");
	}
	
	// 투표 출력
	// 필요한 정보 : 투표항목만 보여주면 되나? 결과는 끝나고 보여줄건데? 일단 결과도 보여준다고해야하나? ㅇㅋ 그럼 결과보여주고 나중에 어차피 비동기통신으로 바꿔야댐
		// 그럼 투표항목과 투표결과를 보여줘야함! vote_choice vote_result 보여줘야함!
	
	
	
	}

}
