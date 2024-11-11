<%@page import="java.util.List"%>
<%@page import="com.tlg.model.TmMember"%>
<%@page import="com.tlg.model.TmBoard"%>
<%@page import="com.tlg.model.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Travel Mate Review</title>
    <link rel="stylesheet" href="assets/css/reviewListStyle.css">
</head>
<%
	BoardDAO dao = new BoardDAO();
	
	List<TmBoard> board = dao.getBoard();
	
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
    
    <nav>
        <ul>
        	<li><a href="mainPage.jsp">Home</a></li>
            <li><a href="notice.html">Notice</a></li>
            <li><a href="faq.html">FAQ</a></li>
            <li><a href="reviewList.jsp" class="active">Review</a></li> <!-- 활성화된 메뉴 -->
            <li><a href="https://www.japan.travel/ko/destinations/kanto/tokyo/" target="_blank">Info.Tokyo</a></li>
        </ul>
    </nav>

    <main>
	    
        <section class="announcement">
            <p>*최근공지사항*</p>
            <p>💢이거언제다하니...?</p>

        </section>
        
        <section class="best-reviews">
            <div class="review-card-container">
                <div class="review-card-title">best 공감 리뷰</div>
                <div class="review-card" style="background-image: url('img/bestReview.png');">
                    <p>P들의 4박5일 일본여행기</p>
                </div>
            </div>
            <div class="review-card-container">
                <div class="review-card-title">댓글이 가장많은 리뷰</div>
                <div class="review-card" style="background-image: url('img/여친.jpg');">
                    <p>여자친구랑 일주일간 여행</p>
                </div>
            </div>
            <div class="review-card-container">
                <div class="review-card-title">best 조회수 리뷰</div>
                <div class="review-card" style="background-image: url('img/이승기.jpg');">
                    <p>군대가기 전 마지막 여행..</p>
                </div>
            </div>
        </section>
        
	
        <section class="recent-posts">
            <h2>여행 게시글</h2>
	    	<input type="text" class="search" placeholder="search"><button class="glass">🔍</button>

            <table>
                <thead>
                    <tr>
                        <th>No.</th>
                        <th>제목</th>
                        <th>글쓴이</th>
                        <th>작성시간</th>
                    </tr>
                </thead>
                <tbody>
                    
                    <%for(int i = 0; i < board.size(); i++) {%>
                    <tr>
                    	<td><%=board.get(i).getC_idx()%></td>
                        <td><a href="postView.jsp?c_idx=<%=board.get(i).getC_idx()%>"><%=board.get(i).getC_title() %></a></td>
                        <td><%=board.get(i).getId() %></td>
                        <td><%=board.get(i).getCreated_at() %></td>
                    </tr>
                    <%} %>
                </tbody>
            </table>
            <%if(member != null) {%>
            <button onclick="location.href='reviewFormIndex.jsp'">게시글 작성하기</button>
            <%} else {%>
            <button onclick="needLogin()">게시글 작성하기</button>
            <%} %>
        </section>
    </main>

    
    <script src="assets/js/header.js"></script>
    <script>
	    function needLogin() {
	    	alert("로그인이 필요합니다!");
	    }
    </script>
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