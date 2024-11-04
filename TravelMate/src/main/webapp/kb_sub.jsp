<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>세부 계획</title>
    <link rel="stylesheet" href="kb_sub_style.css">
</head>
<body>
    <div class="container">
        <h1 id="schedule-title">일정 제목</h1>
    
        <!-- 기존 입력 폼들 -->
        <label for="schedule-type">일정 타입:</label>
        <select id="schedule-type">
            <option value="travel">이동</option>
            <option value="restaurant">맛집</option>
            <option value="accommodation">카페</option>
            <option value="hotplace">관광지</option>
            <option value="accommodation">숙소</option>
            <option value="etc">기타</option>
        </select>
    
        <label for="date">날짜:</label>
        <input type="date" id="date">
    
        <label for="location">장소:</label>
        <input type="text" id="location" placeholder="장소 입력" readonly>
    
        <label for="description">상세 설명:</label>
        <textarea id="description" rows="4" placeholder="상세 설명 입력"></textarea>
    
        <button id="save-btn">저장</button>
    
        <!-- 투표 섹션 -->
        <div id="voting-section">
            <h3>장소 투표</h3>
            <ol id="vote-options">
                <!-- 사용자 정의 투표 항목이 추가될 자리 -->
            </ol>
            <button onclick="addVoteOption()">항목 추가</button>
            <button onclick="startVoting()">투표 시작</button>
            <button id="complete-vote-btn" style="display: none;" onclick="completeVoting()">투표 완료</button>
        </div>
    </div>

    <script src="kb_sub_script.js"></script>
</body>
</html>
