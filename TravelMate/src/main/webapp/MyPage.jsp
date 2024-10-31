<%@page import="com.tlg.model.TmMember"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Travel Mate</title>
<link rel="stylesheet" href="assets/css/TravelBoardStyle.css">
</head>
<%
// 세션 영역 안에 있는 사용자의 id를 가져오기!
TmMember member = (TmMember) session.getAttribute("member");
%>
<body>

	<!-- Header -->
	<header>
		<h1 id="logo" onclick="goToMain()">Travel Mate</h1>
		<div class="header-icons">
			<span onclick="openModal('alarmModal')">알람</span>
			<%
			if (member == null) {
			%>
			<span onclick="checkLoginStatus()">로그인</span>
			<%
			} else {
			%>
			<span>마이페이지</span> <span><a id="logout"
				href="LogoutController">로그아웃</a></span>
			<%
			}
			%>

			<span onclick="openModal('travelBagModal')">나만의 여행가방</span>
		</div>
	</header>

	<!-- 메인 컨텐츠 -->
	<main>
		<span onclick="openModal('EMIModal')"><a id="EMI">회원정보 수정</a></span>
	</main>

	<!-- 로그인 모달 -->
	<div id="loginModal" class="modal">
		<div class="modal-content">
			<button class="close-btn" onclick="closeModal('loginModal')">X</button>
			<div class="logo-placeholder">로고</div>
			<form action="LoginController" method="post">
				<input type="text" name="id" placeholder="아이디를 입력해주세요."> <input
					type="password" name="pw" placeholder="비밀번호를 입력해주세요."> <input
					type="submit" value="로그인">
			</form>
			<a href="#" class="google-btn">google로 로그인하기</a> <span
				class="register-link" onclick="openModal('registerModal')">회원가입
				하기</span>
		</div>
	</div>

	<!-- 회원가입 모달 -->
	<div id="registerModal" class="modal">
		<div class="modal-content">
			<button class="close-btn" onclick="closeModal('registerModal')">X</button>
			<form action="JoinController" method="post">
				<h2>Travel Mate 회원가입</h2>
				<input type="text" name="id" placeholder="아이디를 입력해주세요."> <input
					type="password" name="pw" placeholder="비밀번호를 입력해주세요."> <input
					type="text" name="name" placeholder="이름을 입력해주세요."> <input
					type="text" name="nick" placeholder="닉네임을 입력해주세요.">
				<div class="gender-check">
					<span>성별 체크</span> <input type="radio" name="gender" value="M">
					남 <input type="radio" name="gender" value="F"> 여
				</div>
				<div class="email-container">
					<input type="text" name="email" placeholder="이메일을 입력해주세요.">

				</div>
				<input type="submit" value="회원가입">
			</form>
		</div>
	</div>

	<!-- 회원 정보 수정(Edit Member Information) 모달 -->
	<div id="EMIModal" class="modal">
		<div class="modal-content">
			<button class="close-btn" onclick="closeModal('EMIModal')">X</button>
			<div><h2>회원정보 수정</h2></div>
			<input type="password" name="currentpw" placeholder="기존 비밀번호를 입력하세요.">
			<input type="password" name="newpw" placeholder="새로운 비밀번호를 입력하세요.">
			<input type="text" name="nick" placeholder="바꿀 닉네임을 입력하세요.">
			<input type="submit" value="회원정보 수정">
		</div>
	</div>


	<!-- JavaScript 파일 연결 -->
	<script src="assets/js/MainScript.js"></script>
</body>
</html>
