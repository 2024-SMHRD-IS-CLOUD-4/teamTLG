<%@page import="com.tlg.model.TmBoard"%>
<%@page import="com.tlg.model.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Post View</title>
    <link rel="stylesheet" href="/share/common.css"> <!-- 공통 스타일 -->
    <link rel="stylesheet" href="assets/css/postView.css"> <!-- 페이지별 스타일 -->
    <link rel="stylesheet" href="/share/header/header.css"> <!-- 헤더 스타일 추가 -->
</head>
<%

	int c_idx = Integer.parseInt(request.getParameter("c_idx"));
	
	BoardDAO dao = new BoardDAO();
	
	TmBoard board = dao.getBoardOne(c_idx);
	
%>
<body>

	<%@ include file = "header.jsp" %>

    <!-- 메인 컨텐츠 -->
    <main class="container">
        <table class="table table-bordered">
    		<tr>
    			<td>[번호]</td><td><%=board.getC_idx() %></td>
    		</tr>
    		<tr>
    			<td>[제목]</td><td><%=board.getC_idx() %></td>
    		</tr>
    		<tr>
    			<td>[내용]</td>
    			<td><img src="upload/<%=board.getC_file() %>"></td>
    			<br>
    			<td><%=board.getC_content() %></td>	
    		</tr>		
    	</table>

        <div class="comment-section">
            <h3>댓글 <span class="comment-count">0</span></h3>
            <textarea class="comment-input" placeholder="로그인 후 댓글을 작성하실 수 있습니다." maxlength="150"></textarea>
            <div class="comment-actions">
                <div class="comment-counter">0/150</div>
                <button class="submit-comment" onclick="submitComment()">댓글 작성</button>
            </div>
            <div class="comment-list"></div> <!-- 댓글이 추가될 영역 -->
        </div>
        
        <div>
        	<button class="btn btn-sm btn-info">수정</button>
    		<button class="btn btn-sm btn-warning">삭제</button>
    		<button class="btn btn-sm btn-success"><a href="reviewList.jsp">리스트</a></button>  
        </div>
    </main>

    <!-- JavaScript 파일 연결 -->
    <script src="assets/js/postView.js"></script>
   
</body>
</html>
