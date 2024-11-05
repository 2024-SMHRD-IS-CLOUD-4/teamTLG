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
		// 세션 영역 안에 있는 사용자의 id를 가져오기!
		TmMember member = (TmMember)session.getAttribute("member");
		TravelPlan plan = (TravelPlan)session.getAttribute("plan");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		TravelPlanDAO tpDao = new TravelPlanDAO();
	    
	%>
<body>

    <!-- Header -->
    <header>
       <h1 id="logo" onclick="goToMain()">Travel Mate</h1>
        <div class="header-icons">
            <span onclick="openModal('alarmModal')">알람</span>
            <%if(member == null) {%>
            <span onclick="checkLoginStatus()">로그인</span>
            <%} else {%>
            <span>마이페이지</span>
            <span><a id="logout" href="LogoutController">로그아웃</a></span>
            <%} %>
            <span onclick="openModal('travelBagModal')">나만의 여행가방</span>
        </div>
    </header>

    <!-- 메인 컨텐츠 -->
    <main>
        <div class="content">
            <a href="#" class="content-card" style="background-image: url('img/tokyoView.png');">
                <p class="card-text">도쿄 여행 꿀팁</p>
            </a>
            <a href="#" class="content-card" style="background-image: url('img/tako.png');">
                <p class="card-text">도쿄 맛집 list</p>
            </a>
            <a href="#" class="content-card" style="background-image: url('img/course.jpg');">
                <p class="card-text">추천 여행코스</p>
            </a>
            <a href="reviewList.jsp" class="content-card" style="background-image: url('img/bestReview.png');">
                <p class="card-text">여행 게시판</p>
            </a>
        </div>
        <div class="lists">
            <div class="list-item">최근 리뷰 게시글</div>
				<div class="list-item">나의 여행 계획 List
                <ul id="travel-plan-list">
                    <%
                        if (member != null) {
                            List<TravelPlan> travelPlans = tpDao.selectPlans(member.getId()); // 사용자의 여행 계획 가져오기
                            if (travelPlans != null && !travelPlans.isEmpty()) {
                                for (TravelPlan plans : travelPlans) {
                    %>
	                   <li>
						    <a href="kb_index.html?id=<%= plans.getId() %>&travelIndex=<%= plans.getTr_idx() %>">
						        <%= plans.getTr_title() %> - 
						        <%= dateFormat.format(plans.getTr_st_dt()) %> ~ 
						        <%= dateFormat.format(plans.getTr_ed_dt()) %>
						    </a>
						</li>
                    <%
                                }
                            } else {
                    %>
                    <li>여행 계획이 없습니다.</li>
                    <%
                            }
                        } else {
                    %>
                    <li>로그인 후 여행 계획을 확인할 수 있습니다.</li>
                    <%
                        }
                    %>
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
            <input type="text" name="id" placeholder="아이디를 입력해주세요.">
            <input type="text" name="pw" placeholder="비밀번호를 입력해주세요.">
            <button>로그인</button>
            </form>
            <a href="#" class="google-btn">google로 로그인하기</a>
            <span class="register-link" onclick="openModal('registerModal')">회원가입 하기</span>
        </div>
    </div>

    <!-- 회원가입 모달 -->
    <div id="registerModal" class="modal">
        <div class="modal-content">
            <button class="close-btn" onclick="closeModal('registerModal')">X</button>
            <form action="JoinController" method="post">
            <h2>Travel Mate 회원가입</h2>
            <input type="text" name="id" placeholder="아이디를 입력해주세요.">
            <input type="password" name="pw" placeholder="비밀번호를 입력해주세요.">
            <input type="text" name="name" placeholder="이름을 입력해주세요.">
            <input type="text" name="nick" placeholder="닉네임을 입력해주세요.">
            <div class="gender-check">
                <span>성별 체크</span>
                <input type="radio" name="gender" value="M"> 남
                <input type="radio" name="gender" value="F"> 여
            </div>
            <div class="email-container">
                <input type="text" name="email" placeholder="이메일을 입력해주세요.">            
            </div>
            <button type="submit">회원가입</button>
            </form>
        </div>
    </div>

    <!-- JavaScript 파일 연결 -->
    <script src="assets/js/mainScript.js"></script>
</body>
</html>
    