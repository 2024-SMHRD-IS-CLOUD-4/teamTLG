<%@page import="com.tlg.model.TmMember"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>header</title>
<link rel="stylesheet" href="assets/css/header.css">
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
            <span onclick="openCheckList()">나만의 여행가방</span>
            <%
            }
            %>
        </nav>
    </header>
<script src="assets/js/header.js"></script>
<script>
	function openCheckList() {
		// URL, 창 이름, 창 옵션 설정
		const url = "checkList.jsp";
		const name = "_blank"; // _blank는 새 창을 의미
		const options = "width=800,height=600,top=100,left=200";
	
		// 새 창 열기
		window.open(url, name, options);
	}
</script>
</body>
</html>
