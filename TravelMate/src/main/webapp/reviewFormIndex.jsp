<%@page import="com.tlg.model.TmMember"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Travel Mate - 리뷰 작성</title>
    <link rel="stylesheet" href="assets/css/reviewFormStyle.css">
</head>
<% TmMember member = (TmMember)session.getAttribute("member"); %>
<body>
    <header>
        <h1 id="logo" onclick="navigateToReviewList()">Travel Mate</h1>
        <h2>Review</h2>
    </header>
    
    <main>
        <form action="WriteController" method="post" enctype="multipart/form-data">
            <label for="title">제목을 입력하세요.</label>
            <input type="text" id="title" name="c_title" placeholder="제목을 입력하세요." required>
            
            <label for="id">아이디를 입력하세요</label>
            <input type="text" name="id" value="<%=member.getId() %>">

            <label for="content">내용을 입력하세요.</label>
            <textarea id="content" name="c_content" placeholder="내용을 입력하세요." required></textarea>

            <label for="receipt">사진 첨부</label>
            <input type="file" id="receipt" name="c_file" required>
            
            <label for="date">날짜 입력</label>
            <input type="date" id="date" name="created_at" required>
            
            <button type="submit">포스팅</button>      
		</form>
    </main>
</body>
</html>
