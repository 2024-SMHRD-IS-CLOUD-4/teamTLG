<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>여행 일정 칸반 보드</title>
    <link rel="stylesheet" href="assets/css/kb_style.css">
     <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

</head>
<body>
    <div class="kanban-container">
        <div class="kanban-board" id="kanban-board">
            <!-- JavaScript에서 컬럼이 생성됩니다. -->
        </div>
        <div class="add-day-button" onclick="addNewDay()">+</div>
    </div>

    <!-- 칸반 보드 아래쪽에 댓글 섹션 -->
    <div id="comment-section" class="container">
        <h2>댓글</h2>
        <div id="comment-form">
            <textarea id="comment-content" placeholder="댓글을 입력하세요.."></textarea>
            <button id="add-comment">댓글 추가</button>
        </div>
        <div id="comments"></div> <!-- 댓글이 표시될 위치 -->
        <div id="pagination" class="pagination"></div>
    </div>
    <script type="text/javascript">
    	const tr_idx = "<%= request.getParameter("tr_idx") %>";
    </script>

   
    <script src="assets/js/kb_script.js"></script>
</body>
</html>
