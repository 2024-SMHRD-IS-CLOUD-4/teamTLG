package com.tlg.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;  // Gson 라이브러리를 사용하는 경우
import com.tlg.model.Vote;

@WebServlet("/VoteController")
public class VoteController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        // JSON 데이터 읽기
        BufferedReader reader = request.getReader();
        StringBuilder jsonData = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonData.append(line);
        }

        // JSON 데이터를 Vote 객체로 변환
        Gson gson = new Gson();
        Vote inputVote = gson.fromJson(jsonData.toString(), Vote.class);

        // DB 연결 및 데이터 삽입
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "username", "password");
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO VOTE_CHOICE (CHOICE_IDX, TR_IDX, SCHE_IDX, VOTE_CONT, VOTE_SCORE) VALUES (?, ?, ?, ?, ?)")) {

            pstmt.setInt(1, inputVote.getChoiceIdx());
            pstmt.setInt(2, inputVote.getTrIdx());
            pstmt.setInt(3, inputVote.getScheIdx());
            pstmt.setString(4, inputVote.getVoteCont());
            pstmt.setInt(5, inputVote.getVoteScore());

            int rows = pstmt.executeUpdate();
            response.getWriter().write(rows > 0 ? "데이터 저장 성공" : "데이터 저장 실패");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("데이터베이스 오류 발생: " + e.getMessage());
        }
    }
}

