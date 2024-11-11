package com.tlg.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tlg.model.Schedule;
import com.tlg.model.ScheduleDAO;

@WebServlet("/ScheduleController")
public class ScheduleController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ScheduleDAO scDao = new ScheduleDAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 요청에서 card_idx 파라미터 가져오기
		int cardIdx = Integer.parseInt(request.getParameter("card_idx"));
		System.out.println("요청에서 가져온 cardIdx : " + cardIdx);

		// DAO를 통해 KanbanCard 테이블에서 card_title 가져오기
		String cardTitle = scDao.getCardTitle(cardIdx);

		// Schedule 테이블에서 추가 정보 가져오기
		Schedule scheduleDetails = scDao.getScheduleDetails(cardIdx);

		System.out.println("카드 타이틀 정보 : " + cardTitle);
		System.out.println("스케줄 정보 : " + scheduleDetails);

		// 시간을 올바른 형식으로 변환 (DATE 타입을 HH:mm 형식으로 변환)
		String formattedTime = null;
		if (scheduleDetails != null && scheduleDetails.getSche_tm() != null) {
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			formattedTime = timeFormat.format(scheduleDetails.getSche_tm()); // HH:mm 형식으로 시간 변환
		}

		// JSON 응답 설정
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		// JSON 객체로 합쳐서 반환
		Gson gson = new Gson();
		JsonObject jsonResponse = new JsonObject();
		jsonResponse.addProperty("card_title", cardTitle);
		jsonResponse.add("scheduleDetails", gson.toJsonTree(scheduleDetails));
		jsonResponse.addProperty("formattedTime", formattedTime); // 변환된 시간 문자열 추가

		response.getWriter().write(gson.toJson(jsonResponse));
	}

	  @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        // JSON 데이터 읽기
	        StringBuilder jsonData = new StringBuilder();
	        String line;
	        BufferedReader reader = request.getReader();
	        while ((line = reader.readLine()) != null) {
	            jsonData.append(line);
	        }

	        // Gson을 사용해 JSON을 객체로 변환
	        Gson gson = new Gson();
	        JsonObject jsonObject = gson.fromJson(jsonData.toString(), JsonObject.class);

	        String action = jsonObject.get("action").getAsString();

	        switch (action) {
	            case "updateSchedule":
	                updateSchedule(jsonObject, response);
	                break;
	            default:
	                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	                break;
	        }
	    }
	    // 기존 스케줄 업데이트
	    private void updateSchedule(JsonObject jsonObject, HttpServletResponse response) throws IOException {
	        int scheduleId = jsonObject.get("schedule_id").getAsInt();
	        String scheType = jsonObject.get("sche_type").getAsString();
	        String schePlace = jsonObject.get("sche_place").getAsString();
	        String scheDesc = jsonObject.get("sche_desc").getAsString();
	        String scheTimeStr = jsonObject.get("sche_tm").getAsString(); // 시간 문자열
	        
	        System.out.println("Received schedule ID: " + scheduleId);
	        System.out.println("Received scheType: " + scheType);


	        // 시간을 DATE 타입으로 변환
	        Date scheTime = convertToDate(scheTimeStr);

	        Schedule schedule = new Schedule(scheduleId, scheType, schePlace, scheDesc, scheTime);
	        boolean result = scDao.updateSchedule(schedule); // DB 업데이트

	        // 응답 설정
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        JsonObject jsonResponse = new JsonObject();
	        jsonResponse.addProperty("success", result); // 성공 여부 반환

	        response.getWriter().write(new Gson().toJson(jsonResponse)); // 응답 JSON 전송
	    }

	    // 시간을 DATE 타입으로 변환하는 메서드
	    private Date convertToDate(String timeStr) {
	        try {
	            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	            return timeFormat.parse(timeStr);
	        } catch (ParseException e) {
	            e.printStackTrace();
	            return null; // 예외 처리
	        }
	    }
}
