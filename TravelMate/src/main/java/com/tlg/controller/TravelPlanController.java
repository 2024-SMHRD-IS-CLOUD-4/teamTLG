package com.tlg.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.tlg.model.Comment;
import com.tlg.model.CommentDAO;
import com.tlg.model.TravelPlan;
import com.tlg.model.TravelPlanDAO;

@WebServlet("/TravelPlanController")
public class TravelPlanController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Gson gson = new Gson();

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Map<String, String> result = new HashMap<>();

		try {
			if ("POST".equalsIgnoreCase(request.getMethod())) {
				// JSON 데이터를 수신
				StringBuilder jsonBuilder = new StringBuilder();
				try (BufferedReader reader = request.getReader()) {
					String line;
					while ((line = reader.readLine()) != null) {
						jsonBuilder.append(line);
					}
				}

				String jsonData = jsonBuilder.toString();
				Map<String, Object> dataMap = gson.fromJson(jsonData, Map.class);

				// 데이터 타입에 따라 분기
				if (dataMap.containsKey("tr_title")) { // 일정 데이터인 경우
					handleTravelPlan(dataMap, result);
				} else if (dataMap.containsKey("cmt_content")) { // 댓글 데이터인 경우
					handleComment(dataMap, result);
				}

				// 응답 전송
				out.print(gson.toJson(result));
				out.flush();
			} else if ("GET".equalsIgnoreCase(request.getMethod())) {
				String action = request.getParameter("action");

				if ("getComments".equals(action)) {
					// 기존 댓글 목록 가져오기 로직
					String id = request.getParameter("id");
					String trIdxParam = request.getParameter("tr_idx");

					if (id == null || trIdxParam == null || trIdxParam.isEmpty()) {
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						result.put("error", "필수 파라미터가 누락되었습니다.");
						out.print(gson.toJson(result));
						return;
					}

					int tr_idx = Integer.parseInt(trIdxParam);
					Comment cmt = new Comment(id, tr_idx);
					CommentDAO cmDao = new CommentDAO();
					List<Comment> comments = cmDao.getKbComments(cmt);

					out.print(gson.toJson(comments));

				} else if ("getTravelPlans".equals(action)) {
					// 여행 기록 가져오기 로직
					String id = (String) request.getSession().getAttribute("id");

					if (id == null) {
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						result.put("error", "로그인이 필요합니다.");
						out.print(gson.toJson(result));
						return;
					}

					TravelPlanDAO tpDao = new TravelPlanDAO();
					List<TravelPlan> travelPlans = tpDao.selectPlans(id);
					out.print(gson.toJson(travelPlans));

				} else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					result.put("error", "유효하지 않은 action 파라미터입니다.");
					out.print(gson.toJson(result));
				}
			}
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			result.put("error", "유효하지 않은 숫자 형식입니다.");
			out.print(gson.toJson(result));
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result.put("error", "서버 오류가 발생했습니다.");
			out.print(gson.toJson(result));
		} finally {
			out.flush();
		}
	}

	private void handleTravelPlan(Map<String, Object> dataMap, Map<String, String> result) {
		String id = (String) dataMap.get("id");
		String tr_title = (String) dataMap.get("tr_title");

		// 날짜를 문자열로 가져오기
		String tr_st_dt_str = (String) dataMap.get("tr_st_dt");
		String tr_ed_dt_str = (String) dataMap.get("tr_ed_dt");
		String tr_desc = (String) dataMap.get("tr_desc");

		Date tr_st_dt = null;
		Date tr_ed_dt = null;

		// SimpleDateFormat을 사용하여 문자열을 Date로 변환
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // 날짜 포맷 설정
		try {
			tr_st_dt = dateFormat.parse(tr_st_dt_str); // 문자열을 Date로 변환
			tr_ed_dt = dateFormat.parse(tr_ed_dt_str); // 문자열을 Date로 변환
		} catch (Exception e) {
			e.printStackTrace(); // 예외 처리
			result.put("planResult", "fail");
			return; // 오류 발생 시 메소드 종료
		}

		TravelPlan plan = new TravelPlan(id, tr_title, tr_st_dt, tr_ed_dt, tr_desc);
		TravelPlanDAO tpDao = new TravelPlanDAO();

		int insertResult = tpDao.insertPlan(plan);
		result.put("planResult", insertResult > 0 ? "success" : "fail");
	}

	private void handleComment(Map<String, Object> dataMap, Map<String, String> result) {
		String id = (String) dataMap.get("id");
		String cmt_content = (String) dataMap.get("cmt_content");
		String create_at = (String) dataMap.get("create_at");
		int c_idx = ((Double) dataMap.get("c_idx")).intValue();
		int cmt_heart = ((Double) dataMap.get("cmt_heart")).intValue();
		int tr_idx = ((Double) dataMap.get("tr_idx")).intValue();

		Comment comment = new Comment(c_idx, cmt_content, create_at, cmt_heart, id, tr_idx);
		CommentDAO cmDao = new CommentDAO();

		int insertResult = cmDao.insertComment(comment);
		result.put("commentResult", insertResult > 0 ? "success" : "fail");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		String id = (String) request.getSession().getAttribute("id");

		Map<String, Object> data = new HashMap<>();
		data.put("id", id); // 실제 사용자 ID
		data.put("travelIndex", 1); // 실제 여행 인덱스 - 나중에 사용자가 선택한 여행지의 index로 변경

		PrintWriter out = response.getWriter();
		out.print(new Gson().toJson(data));
		out.flush();
	}

}
