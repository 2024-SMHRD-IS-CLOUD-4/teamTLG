package com.tlg.controller;

import com.tlg.model.KanbanDAO;
import com.tlg.model.ScheduleDAO;
import com.tlg.model.KanbanCard;
import com.tlg.model.KanbanColumn;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/KanbanController")
public class KanbanController extends HttpServlet {
	private static final long serialVersionUID = 2785127315253161912L;
	KanbanDAO kanbanDAO = new KanbanDAO();
	ScheduleDAO scDao = new ScheduleDAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String tr_idxStr = request.getParameter("tr_idx");
		System.err.println("tr_idxStr : " + tr_idxStr);

		if (tr_idxStr == null || tr_idxStr.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("{\"error\":\"Missing or invalid tr_idx parameter\"}");
			return;
		}

		try {
			int tr_idx = Integer.parseInt(tr_idxStr);
			System.out.println("tr_idx : " + tr_idx);

			// Kanban 데이터를 가져옵니다.
			List<KanbanColumn> kanbanData = kanbanDAO.getKanbanData(tr_idx);

			// JSON 형식으로 데이터를 반환합니다.
			String json = new Gson().toJson(kanbanData);
			response.setContentType("application/json; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			request.setCharacterEncoding("UTF-8");
			response.getWriter().write(json);

		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("{\"error\":\"Invalid tr_idx format\"}");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		StringBuilder sb = new StringBuilder();
		String line;

		try (BufferedReader reader = request.getReader()) {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		}

		String jsonData = sb.toString();
		Gson gson = new Gson();
		try {
			JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);

			System.out.println("받은 json 데이터 : " + jsonObject);

			if (jsonObject == null || !jsonObject.has("action")) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"error\":\"Missing or invalid action parameter\"}");
				return;
			}

			String action = jsonObject.get("action").getAsString();

			// 컬럼 추가
			if ("createColumn".equals(action)) {
				if (!jsonObject.has("tr_idx") || !jsonObject.has("col_title") || !jsonObject.has("col_order")) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write("{\"error\":\"Missing one or more required parameters\"}");
					return;
				}

				String col_title = jsonObject.get("col_title").getAsString();
				int tr_idx = jsonObject.get("tr_idx").getAsInt();
				int col_order = jsonObject.get("col_order").getAsInt();

				System.out.println("action: " + action);
				System.out.println("tr_idx: " + tr_idx);
				System.out.println("col_title: " + col_title);

				KanbanColumn column = new KanbanColumn();
				column.setCol_title(col_title);
				column.setTr_idx(tr_idx);
				column.setCol_order(col_order);

				int col_idx = kanbanDAO.insertColumn(column);

				if (col_idx == 0) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().write("{\"error\":\"Failed to create column\"}");
					return;
				}

				JsonObject jsonResponse = new JsonObject();
				jsonResponse.addProperty("col_idx", col_idx);
				jsonResponse.addProperty("col_order", col_order);
				response.getWriter().write(jsonResponse.toString());
				return;
			}

			// 카드 추가
			else if ("createCard".equals(action)) {
				if (!jsonObject.has("card_title") || !jsonObject.has("col_idx")) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write("{\"error\":\" card_title, col_idx중에 하나 없음\"}");
					return;
				}

				int col_idx = jsonObject.get("col_idx").getAsInt();
				String card_title = jsonObject.get("card_title").getAsString();
				int card_order = jsonObject.get("card_order").getAsInt();

				KanbanCard card = new KanbanCard();
				card.setCard_title(card_title);
				card.setCol_idx(col_idx);
				card.setCard_order(card_order);

				int card_idx = kanbanDAO.insertCard(card);

				if (card_idx == 0) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().write("{\"error\":\"Failed to create card\"}");
					return;
				}

				JsonObject jsonResponse = new JsonObject();
				jsonResponse.addProperty("card_idx", card_idx);
				response.getWriter().write(jsonResponse.toString());
				return;
			} else if ("deleteColumn".equals(action)) {
				if (!jsonObject.has("col_idx")) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write("{\"error\":\"Missing col_idx parameter\"}");
					return;
				}

				int col_idx = jsonObject.get("col_idx").getAsInt();

				try {
					// 1. 해당 컬럼에 속한 카드들에 연결된 모든 스케줄 삭제
					scDao.deleteSchedulesByColumnId(col_idx);

					// 2. 해당 컬럼에 속한 모든 카드 삭제
					kanbanDAO.deleteCardsByColumnId(col_idx);

					// 3. 컬럼 삭제
					kanbanDAO.deleteColumn(col_idx);

					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().write("{\"message\":\"Column and related data deleted successfully\"}");
				} catch (Exception e) {
					e.printStackTrace();
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().write("{\"error\":\"Failed to delete column and related data\"}");
				}
				return;
			}

			// 카드 삭제
			else if ("deleteCard".equals(action)) {
				if (!jsonObject.has("card_idx")) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write("{\"error\":\"Missing card_idx parameter\"}");
					return;
				}

				int card_idx = jsonObject.get("card_idx").getAsInt();

				try {
					// 1. 해당 카드에 연결된 스케줄 삭제
					int deletedSchedulesCount = scDao.deleteSchedulesByCardId(card_idx);
					System.out.println("삭제된 스케줄 수: " + deletedSchedulesCount);

					// 2. 카드 삭제
					int deletedCardCount = kanbanDAO.deleteCard(card_idx);
					if (deletedCardCount > 0) {
						response.setStatus(HttpServletResponse.SC_OK);
						response.getWriter().write("{\"message\":\"Card and related schedules deleted successfully\"}");
					} else {
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						response.getWriter().write("{\"error\":\"Failed to delete card\"}");
					}
				} catch (Exception e) {
					e.printStackTrace();
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().write("{\"error\":\"An error occurred while deleting card and schedules\"}");
				}
				return;

			} else if ("updateColumnOrder".equals(action)) {
				// 컬럼 순서 업데이트
				if (!jsonObject.has("columns")) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write("{\"error\":\"Missing columns parameter\"}");
					return;
				}

				List<KanbanColumn> columns = gson.fromJson(jsonObject.get("columns"),
						new TypeToken<List<KanbanColumn>>() {
						}.getType());
				kanbanDAO.updateColumnOrder(columns);

				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"message\":\"Column order updated successfully\"}");
				return;
			}

			// 카드 순서 업데이트
			else if ("updateCardOrder".equals(action)) {
				if (!jsonObject.has("cards")) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write("{\"error\":\"Missing cards parameter\"}");
					return;
				}

				List<KanbanCard> cards = gson.fromJson(jsonObject.get("cards"), new TypeToken<List<KanbanCard>>() {
				}.getType());
				kanbanDAO.updateCardOrder(cards);

				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"message\":\"Card order updated successfully\"}");
				return;

			} else if ("updateCardTitle".equals(action)) {
				if (!jsonObject.has("card_idx") || !jsonObject.has("card_title")) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write("{\"error\":\"Missing card_idx or card_title parameter\"}");
					return;
				}

				String card_idxStr = jsonObject.get("card_idx").getAsString();

				// 임시 카드 ID인 경우 업데이트 요청을 무시하고 로그를 남깁니다.
				if (card_idxStr != null && card_idxStr.startsWith("temp-")) {
					System.out.println("임시 카드 ID는 업데이트하지 않습니다: " + card_idxStr);
					return;
				}

				int card_idx;
				try {
					card_idx = Integer.parseInt(card_idxStr);
				} catch (NumberFormatException e) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write("{\"error\":\"Invalid card_idx format\"}");
					return;
				}

				String card_title = jsonObject.get("card_title").getAsString();

				KanbanCard card = new KanbanCard();
				card.setCard_idx(card_idx);
				card.setCard_title(card_title);

				int result = kanbanDAO.updateCardTitle(card);

				if (result == 0) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().write("{\"error\":\"Failed to update card title\"}");
					return;
				}

				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"message\":\"Card title updated successfully\"}");
				return;
			} else if ("updateColumnTitle".equals(action)) {
				if (!jsonObject.has("col_idx") || !jsonObject.has("col_title")) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write("{\"error\":\"Missing col_idx or col_title parameter\"}");
					return;
				}

				int col_idx = jsonObject.get("col_idx").getAsInt();
				String col_title = jsonObject.get("col_title").getAsString();

				KanbanColumn column = new KanbanColumn();
				column.setCol_idx(col_idx);
				column.setCol_title(col_title);

				int result = kanbanDAO.updateColumnTitle(column);

				if (result == 0) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().write("{\"error\":\"Failed to update column title\"}");
					return;
				}

				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("{\"message\":\"Column title updated successfully\"}");
				return;
			}

			// 잘못된 action 요청
			else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"error\":\"Invalid action\"}");
				return;
			}

		} catch (JsonSyntaxException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("{\"error\":\"Invalid JSON format\"}");
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"error\":\"Failed to save data\"}");
		}
	}
}
