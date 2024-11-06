<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="com.tlg.model.TravelPlanDAO"%>
<%@page import="com.tlg.model.TravelPlan"%>
<%@page import="com.tlg.model.TmMember"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Travel Mate</title>
<link rel="stylesheet" href="assets/css/mainStyle.css">
</head>
<%
TravelPlan plan = (TravelPlan) session.getAttribute("plan");
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
TravelPlanDAO tpDao = new TravelPlanDAO();
%>



<body>


	<%@ include file="header.jsp"%>

	<main>
		<div class="content">
			<a href="#" class="content-card fade-in-up"
				style="background-image: url('img/tokyoView.png');">
				<p class="card-text">도쿄 여행 꿀팁</p>
			</a> <a href="#" class="content-card fade-in-up"
				style="background-image: url('img/tako.png');">
				<p class="card-text">도쿄 맛집 list</p>
			</a> <a href="#" class="content-card fade-in-up"
				style="background-image: url('img/course.jpg');">
				<p class="card-text">추천 여행코스</p>
			</a> <a href="reviewList.jsp" class="content-card fade-in-up"
				style="background-image: url('img/bestReview.png');">
				<p class="card-text">여행 게시판</p>
			</a>
		</div>
		
		 <!-- 여행 계획 버튼 -->
	    <% if (member != null) { %>
	        <button onclick="openPlanForm()">여행 계획 짜기</button>
	    <% } else { %>
	        <button onclick="openModal('loginModal')">로그인 후 이용 가능</button>
	    <% } %>
		
		
		<div class="lists">
			<div class="list-item">최근 리뷰 게시글 출력해주기</div>
			<div class="list-item">
				나의 여행 계획 List
				<ul id="travel-plan-list">
					<%
					if (member != null) {
						List<TravelPlan> travelPlans = tpDao.selectPlans(member.getId()); // 사용자의 여행 계획 가져오기
						if (travelPlans != null && !travelPlans.isEmpty()) {
							for (TravelPlan plans : travelPlans) {
					%>
					<li>
						<a
							href="kb_index.jsp?id=<%=plans.getId()%>&travelIndex=<%=plans.getTr_idx()%>">
								<%=plans.getTr_title()%> - <%=dateFormat.format(plans.getTr_st_dt())%>
								~ <%=dateFormat.format(plans.getTr_ed_dt())%>
					</a>
						</li>
					<% } %>
                <% } else if (member != null) { %>
                    <li>여행 계획이 없습니다.</li>
                <% } else { %>
                    <li>로그인 후 여행 계획을 확인할 수 있습니다.</li>
                <% }}%>
            </ul>
        </div>
    </div>
	</main>

	<!-- 로그인 모달 -->
	<div id="loginModal" class="modal">
		<div class="modal-content">
			<button class="close-btn" onclick="closeModal('loginModal')">X</button>
			<div class="logo-placeholder">로고</div>
			<form action="LoginController" method="post">
				<input type="text" name="id" placeholder="아이디를 입력해주세요."> <input
					type="password" name="pw" placeholder="비밀번호를 입력해주세요.">
				<button>로그인</button>
			</form>
			<span class="register-link" onclick="openModal('registerModal')">회원가입
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
				<button type="submit">회원가입</button>
			</form>
		</div>
	</div>
	
	
	
	    <!-- 여행 계획 입력 폼 모달 -->
    <div id="planFormModal" class="modal" style="display: none;">
    <div class="modal-content">
        <button class="close-btn" onclick="closePlanForm()">X</button>
        <h2>새로운 여행 계획</h2>
        <form id="planForm" action="TravelPlanController" method="post" >
            <input type="text" name="tr_title" placeholder="여행 제목을 입력하세요" required>
            <label>시작 날짜:</label>
            <input type="date" name="tr_st_dt" required>
            <label>종료 날짜:</label>
            <input type="date" name="tr_ed_dt" required>
            <button type="submit">save</button>
        </form>
    </div>
   <!--  <div id="toastMessage">여행 계획이 저장되었습니다.</div> -->
</div>

	<!-- JavaScript 파일 연결 -->
	<script src="assets/js/mainScript.js"></script>
	<script>
        document.addEventListener("DOMContentLoaded", function() {
            const observer = new IntersectionObserver((entries) => {
                entries.forEach((entry) => {
                    if (entry.isIntersecting) {
                        entry.target.classList.add("visible");
                        observer.unobserve(entry.target);
                    }
                });
            });

            const fadeElements = document.querySelectorAll(".fade-in-up");
            fadeElements.forEach((el) => observer.observe(el));
        });
    </script>
</body>
</html>
