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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json"); // 응답의 컨텐츠 타입
		response.setCharacterEncoding("UTF-8"); // 인코딩

		PrintWriter out = response.getWriter();
		Map<String, String> result = new HashMap<>(); // result 객체는 클라이언트에 보낼 json데이터를 저장할 목적으로 생성. 키-값 형태로 저장할 수 있음.

		try { // try는 예외처리를 위한 구조
				// JSON 데이터를 수신
			StringBuilder jsonBuilder = new StringBuilder(); // 클라이언트가 전송한 json데이터를 저장한 준비
			try (BufferedReader reader = request.getReader()) {
				String line; // 클라이언트가 전송한 데이터를 줄다윈로 읽어옴
				while ((line = reader.readLine()) != null) {
					jsonBuilder.append(line); // 반복문으로 한줄씩 읽어와 jsonBuilder에 추가. 나중에는 전체가 jsonBuilder에 저장됨.
				}
			}

			String jsonData = jsonBuilder.toString();
			Map<String, Object> dataMap = gson.fromJson(jsonData, Map.class);

			// 세션에서 사용자 ID를 가져옵니다.
			String userId = (String) request.getSession().getAttribute("id");

			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized 상태코드 : 클라이어늩가 인증되지 않알을 때
																			// 서버가 응답으로 보냄.
				result.put("error", "로그인이 필요합니다.");
				out.print(gson.toJson(result));
				return;
			}

			// 데이터 타입에 따라 분기
			// 데이터맵의 키값을 확인하여 요청이 어떤 데이터인지 판별하기 => 각 메서드에는 dataMap , result, userId 가 전달됨.
			if (dataMap.containsKey("tr_title")) { // tr_title키가 있으면! 여행 계획 데이터를 처리함 => handleTravelPlan메서드
				handleTravelPlan(dataMap, result, userId);
			} else if (dataMap.containsKey("cmt_content")) { // cmt_content 키가 있으면 댓글 데이터 처리! => handleComment 메서드
				handleComment(dataMap, result, userId);
			}

			// 응답 전송 => result맵을 json형식으로 변환 -> 클리아이너트에 전송. 여기에서 클라이언트는 서버로부터 json 형식의 응답을
			// 받게 됨.
			out.print(gson.toJson(result));

		} catch (NumberFormatException e) { // 400 에러코드 발생. 숫자형식 변환이 잘못되었을 때 발생가능
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			result.put("error", "유효하지 않은 숫자 형식입니다.");
			out.print(gson.toJson(result));

		} catch (Exception e) { // 500 에러코드 발생. 모든 다른 예외처리
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result.put("error", "서버 오류가 발생했습니다.");
			out.print(gson.toJson(result));

		} finally { // 예외와는 상관없이 항상실행, 여기서는 out.flush()함수로 모든 출력이 클라이언트로 전송되도록 보장함.
			out.flush();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		PrintWriter out = response.getWriter();

		Map<String, Object> responseData = new HashMap<>();

		String action = request.getParameter("action");

		try {
			// 세션에서 사용자 ID를 가져옵니다.
			String userId = (String) request.getSession().getAttribute("id");

			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				responseData.put("error", "로그인이 필요합니다.");
				out.print(gson.toJson(responseData));
				return;
			}

			if ("getTravelPlans".equals(action)) {
				// 여행 계획 목록을 가져오는 로직
				TravelPlanDAO tpDao = new TravelPlanDAO();
				List<TravelPlan> travelPlans = tpDao.selectPlans(userId);

				Map<String, Object> travelPlanData = new HashMap<>();
				travelPlanData.put("days", travelPlans.size()); // 예시로 여행 계획의 일수를 반환
				travelPlanData.put("cards", travelPlans); // 카드 데이터를 담아 반환

				out.print(gson.toJson(travelPlanData));
			} else if ("getComments".equals(action)) {
				// 기존 댓글 목록 가져오기 로직

				String trIdxParam = request.getParameter("tr_idx");
				// tr_idx가 null값이라면 integer.parseInt 함수에서 오류가 남. 따라서 null인지 아닌지 확인하기위해 우선 문자열로
				// 받고 검증 후 진행

				if (trIdxParam == null || trIdxParam.isEmpty()) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					responseData.put("error", "필수 파라미터가 누락되었습니다.");
					out.print(gson.toJson(responseData));
					return;
				}

				int tr_idx = Integer.parseInt(trIdxParam);

				Comment cmt = new Comment(userId, tr_idx);
				CommentDAO cmDao = new CommentDAO();
				List<Comment> comments = cmDao.getKbComments(cmt);

				out.print(gson.toJson(comments));

			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				responseData.put("error", "유효하지 않은 action 파라미터입니다.");
				out.print(gson.toJson(responseData));
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseData.put("error", "서버 오류가 발생했습니다.");
			out.print(gson.toJson(responseData));
		} finally {
			out.flush();
		}
	}

	private void handleTravelPlan(Map<String, Object> dataMap, Map<String, String> result, String userId) {
		String tr_title = (String) dataMap.get("tr_title");
		String tr_st_dt_str = (String) dataMap.get("tr_st_dt");
		String tr_ed_dt_str = (String) dataMap.get("tr_ed_dt");

//		String partner_name = (String) dataMap.get("partner_name"); // 동행자 이름 추가

		Date tr_st_dt = null;
		Date tr_ed_dt = null;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			tr_st_dt = dateFormat.parse(tr_st_dt_str);
			tr_ed_dt = dateFormat.parse(tr_ed_dt_str);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("planResult", "fail");
			return;
		}

		// 여행 계획 저장
		TravelPlan plan = new TravelPlan(userId, tr_title, tr_st_dt, tr_ed_dt);
		TravelPlanDAO tpDao = new TravelPlanDAO();
		int res = tpDao.insertPlan(plan); //

		if (res > 0) {
			result.put("planResult", "success");
		} else {
			result.put("planResult", "fail");
		}

	}

	private void handleComment(Map<String, Object> dataMap, Map<String, String> result, String userId) {
		String cmt_content = (String) dataMap.get("cmt_content");
		String create_at = (String) dataMap.get("create_at");
		int c_idx = ((Double) dataMap.get("c_idx")).intValue();
		int cmt_heart = ((Double) dataMap.get("cmt_heart")).intValue();
		int tr_idx = ((Double) dataMap.get("tr_idx")).intValue();

		Comment comment = new Comment(c_idx, cmt_content, create_at, cmt_heart, userId, tr_idx);
		CommentDAO cmDao = new CommentDAO();

		int insertResult = cmDao.insertComment(comment);
		result.put("commentResult", insertResult > 0 ? "success" : "fail");
	}
}
