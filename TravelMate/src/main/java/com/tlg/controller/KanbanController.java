// KanbanController.java
package com.tlg.controller;

import com.tlg.model.KanbanDAO;
import com.tlg.model.KanbanColumn;
import com.google.gson.Gson;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/KanbanController")
public class KanbanController extends HttpServlet {
	private KanbanDAO kanbanDAO;

	@Override
	public void init() throws ServletException {
		// ServletContext에서 SqlSessionFactory를 가져옵니다.
		SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) getServletContext().getAttribute("sqlSessionFactory");

		// SqlSessionFactory가 null이 아닌 경우에만 KanbanDAO를 초기화합니다.
		if (sqlSessionFactory != null) {
			this.kanbanDAO = new KanbanDAO(sqlSessionFactory);
		} else {
			throw new ServletException("SqlSessionFactory가 초기화되지 않았습니다.");
		}
	}
//	 

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int tr_idx = Integer.parseInt(request.getParameter("tr_idx"));
		System.out.println("tr_idx : " + tr_idx);

		// Kanban 데이터를 가져옵니다.
		List<KanbanColumn> kanbanData = kanbanDAO.getKanbanData(tr_idx);

		// JSON 형식으로 데이터를 반환합니다.
		String json = new Gson().toJson(kanbanData);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}
}
