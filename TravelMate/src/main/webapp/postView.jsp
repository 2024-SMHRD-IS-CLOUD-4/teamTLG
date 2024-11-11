<%@page import="java.util.List"%>
<%@page import="com.tlg.model.BoardComment"%>
<%@page import="com.tlg.model.BoardCommentDAO"%>
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
    <style>
		.BoardComment-input {
			width: 100%;
			padding: 10px;
			border: 1px solid #ddd;
			border-radius: 4px;
			resize: none;
			box-sizing: border-box; /* padding과 border를 너비에 포함 */
		}
		
		table {
			margin: 0 auto;
			text-align: center;
			border-collapse: collapse;
			width: 100%;
		}
		
		/* 테이블과 셀 테두리 스타일 */
		table, th, td {
			padding: 8px;
		}
		
		td.title{
			font-size: 2rem;
			font-weight: bold;
			margin: 0;
			/* display: flex;
			margin-left: auto; */
		}
		
		/* 헤더 스타일 */
		th {
			background-color: #f2f2f2;	
		}
    </style>
</head>
<%
	int c_idx = Integer.parseInt(request.getParameter("c_idx"));
	
	BoardDAO dao = new BoardDAO();
	
	TmBoard board = dao.getBoardOne(c_idx);
%>
<%
	BoardCommentDAO dao_comment = new BoardCommentDAO();
	List<BoardComment> commentList = dao_comment.listCommentByPostId(c_idx);
%>
<body>

	<%@ include file = "header.jsp" %>

    <!-- 메인 컨텐츠 -->
    <main class="container">
        <table class="table table-bordered">
        	<%-- <tr>
    			<td class="uploader"><%=member.getNick() %></td>
    		</tr> --%>
    		<tr>
    			<td class="title"><%=board.getC_title() %></td>
    		</tr>
    		<tr>
    			<td><img src="upload/<%=board.getC_file() %>"><div><%=board.getC_content() %></div></td>
    		</tr>		
    	</table>

        <div class="comment-section">
        	<form action="BoardCommentController" method="post">
            <textarea id="comment-input" class="BoardComment-input" name="cmt_content" placeholder="로그인 후 댓글을 작성하실 수 있습니다." maxlength="150"></textarea>
            <input type="hidden" name="id" value="<%=member.getId() %>">
            <input type="hidden" name="c_idx" value="<%=board.getC_idx() %>">     
            <div class="comment-actions">
                <%if(member != null) {%>
                <button class="submit-comment" type="button" onclick="submitComment()">댓글 작성</button>
                <%} else {%>
                <button class="submit-comment" type="button" onclick="needLogin()">댓글 작성</button>
                <%} %> 
            </div>
            <div class="comment-list">
            </div> <!-- 댓글이 추가될 영역 -->
            	<table>
            		<tr>
                    	<th>작성자</th>
                    	<th>내용</th>
                    	<th>작성 시간</th>
                	</tr>
		            <%for(int i = 0; i < commentList.size(); i++) {%>
		            <tr>
		            	<td><%=member.getNick() %></td>
		            	<td><%=commentList.get(i).getCmt_content() %></td>
		            	<td><%=commentList.get(i).getCreated_at() %></td>
		            <%} %>
		            </tr>
            	</table>
            </form>
        </div>
      
        <div>
        	<button class="btn btn-sm btn-info">수정</button>
    		<button class="btn btn-sm btn-warning">삭제</button>
    		<button class="btn btn-sm btn-success"><a href="reviewList.jsp">리스트</a></button>  
        </div>
    </main>

    <!-- JavaScript 파일 연결 -->
    <script src="assets/js/postView.js"></script>
    <script type="text/javascript">
    function needLogin() {
    	alert("로그인 후 댓글을 작성하실 수 있습니다!");
    }
    function submitComment() {
        var commentInput = document.getElementById("comment-input").value.trim();

        // `textarea` 값이 비어 있는지 확인
        if (commentInput === "") {
            alert("댓글 내용을 입력해 주세요.");
            return false; // 폼 제출 방지
        }

        // 내용이 있으면 폼 제출 진행
        document.querySelector("form").submit();
    }
    </script>
</body>
</html>
