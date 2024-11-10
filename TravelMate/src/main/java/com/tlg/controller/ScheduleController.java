package com.tlg.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tlg.model.KanbanCard;
import com.tlg.model.KanbanDAO;
import com.tlg.model.Schedule;
import com.tlg.model.ScheduleDAO;

@WebServlet("/ScheduleController")
public class ScheduleController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ScheduleDAO dao = new ScheduleDAO();

	KanbanDAO kbDao = new KanbanDAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 'id' 파라미터 가져오기 (카드 ID)
		String cardIdStr = request.getParameter("id");
		if (cardIdStr == null || cardIdStr.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "카드 ID가 필요합니다.");
			return;
		}

		try {
			int cardId = Integer.parseInt(cardIdStr);
			KanbanCard card = kbDao.getKanbanDataByCardId(cardId); // 카드 정보를 DAO에서 가져오기

			if (card == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "카드를 찾을 수 없습니다.");
				return;
			}

			// JSP 페이지로 카드 정보를 전달
			request.setAttribute("cardTitle", card.getCard_title());
			request.getRequestDispatcher("/kb_sub.jsp").forward(request, response);
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "카드 ID 형식이 잘못되었습니다.");
		}
	}
}
