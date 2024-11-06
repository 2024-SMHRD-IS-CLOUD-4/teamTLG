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
%>
<body>
    
    <%@ include file="header.jsp" %>
    
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

    <footer>
        <input type="text" placeholder="search">
        <button>🔍</button>
    </footer>

    <script src="assets/js/reviewListScript.js" defer></script>
    <script>
	    function needLogin() {
	    	alert("로그인이 필요합니다!");
	    }
    </script>
</body>
</html>