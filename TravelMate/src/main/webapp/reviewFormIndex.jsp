<%@page import="com.tlg.model.TmMember"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Travel Mate - 리뷰 작성</title>
    <link rel="stylesheet" href="assets/css/reviewForm_style.css">
</head>
<%
	TmMember member = (TmMember) session.getAttribute("member");
%>
<body>
    <header class="header-container">
        <h1 onclick="goToMain()" class="header-title">Travel Mate</h1>
        <nav class="header-icons">
            <%
            if (member == null) {
            %>
            <span onclick="checkLoginStatus()">로그인</span>
            <%
            } else {
            %>
            <span onclick="openModal('alarmModal')">🔔<span>알람</span></span>
            <span>👤<a href="myPageIndex.jsp"><span>my page</span></a></span>
            <span><a id="logout" href="LogoutController">로그아웃</a></span>
            <span onclick="openChecklist()">나만의 여행가방</span>
            <%
            }
            %>
        </nav>
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
<script src="assets/js/mainScript.js"></script>
</body>
</html>
