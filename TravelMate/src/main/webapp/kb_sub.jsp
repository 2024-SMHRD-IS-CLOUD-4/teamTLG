<%@page import="com.tlg.model.KanbanColumn"%>
<%@page import="com.tlg.model.TmMember"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>세부 계획</title>
<link rel="stylesheet" href="assets/css/kb_sub_style.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
</head>
<body>

	<div class="container">
		<!-- 카드 제목 표시 -->
		<h1 id="schedule-title">
			<%=request.getAttribute("sche_title") != null ? request.getAttribute("sche_title") : "일정 제목"%>
		</h1>




		<%
		TmMember member = (TmMember) session.getAttribute("member");
		String userId = member != null ? member.getId() : "guest";
		%>

		<!-- 스케줄 타입 -->
		<label for="schedule-type">일정 타입:</label> <select id="schedule-type">
			<option value="이동"
				<%="travel".equals(request.getAttribute("sche_type")) ? "selected" : ""%>>이동</option>
			<option value="맛집"
				<%="restaurant".equals(request.getAttribute("sche_type")) ? "selected" : ""%>>맛집</option>
			<option value="숙소"
				<%="accommodation".equals(request.getAttribute("sche_type")) ? "selected" : ""%>>숙소</option>
			<option value="관광지"
				<%="hotplace".equals(request.getAttribute("sche_type")) ? "selected" : ""%>>관광지</option>
			<option value="기타"
				<%="etc".equals(request.getAttribute("sche_type")) ? "selected" : ""%>>기타</option>
		</select>

		<!-- 시간 입력 -->
		<label for="time">시간:</label> <input type="time" id="time"
			value="<%=request.getAttribute("sche_tm") != null ? request.getAttribute("sche_tm").toString().substring(11, 16) : ""%>">

		<!-- 장소 입력 -->
		<label for="location">장소:</label> <input type="text" id="location"
			placeholder="장소 입력"
			value="<%=request.getAttribute("sche_place") != null ? request.getAttribute("sche_place") : ""%>">

		<!-- 상세 설명 입력 -->
		<label for="description">상세 설명:</label>
		<textarea id="description" rows="4"><%=request.getAttribute("sche_desc") != null ? request.getAttribute("sche_desc") : ""%></textarea>

		<!-- 저장 버튼 -->
		<button id="save-btn" onclick="updateSchedule()">저장</button>

		<!-- 투표 섹션 -->
		<div id="voting-section">
			<h3>장소 투표</h3>
			<ol id="vote-options">
				<!-- 사용자 정의 투표 항목이 추가될 자리 -->
			</ol>
			<button onclick="addVoteOption()">항목 추가</button>
			<button onclick="startVoting()">투표 시작</button>
			<button id="complete-vote-btn" style="display: none;"
				onclick="completeVoting()">투표 완료</button>
		</div>
	</div>
	<script>
		const urlParams = new URLSearchParams(window.location.search);
	    const cardIdx = urlParams.get('id'); // cardId 가져오기
	    const colIdx = urlParams.get('col_idx'); // col_idx 가져오기

	    console.log("Card ID:", cardIdx);
	    console.log("Column ID:", colIdx);
	</script>

	<script>
    // 알림 박스를 표시하는 함수
    function showAlert(message) {
        const alertBox = document.getElementById("alert-box");
        alertBox.textContent = message;
        alertBox.style.display = "block";
        alertBox.style.opacity = "1";

        // 일정 시간 후 알림 숨기기
        setTimeout(() => {
            alertBox.style.opacity = "0";
            setTimeout(() => {
                alertBox.style.display = "none";
            }, 500); // 알림을 숨길 때의 transition 시간과 맞추기 위해 설정
        }, 2000); // 알림이 표시되는 시간 (2초)
    }

    // 저장 버튼 클릭 이벤트
    document.getElementById("save-btn").onclick = function() {
        let timeValue = document.getElementById("time").value;
        let placeValue = document.getElementById("location").value;

        if (!timeValue || !placeValue) {
            showAlert("모든 필드를 채워주세요!");
            return;
        }

        // 저장 로직 수행
        // ...
    };
</script>

	<script src="assets/js/kb_sub_script.js"></script>

	<script>
		// DOM 로드 후 실행
		document.addEventListener("DOMContentLoaded",
				function() {
					const scheduleTypeSelect = document
							.getElementById('schedule-type');
					const scheduleTitle = document
							.getElementById('schedule-title');
					const container = document.querySelector('.container');

					// 일정 타입 변경 시 배경색 변경
					scheduleTypeSelect.addEventListener('change', function() {
						switch (this.value) {
						case '이동':
							scheduleTitle.style.backgroundColor = '#FFEB3B'; // 이동: 노란색
							container.style.backgroundColor = '#FFF8DC'; // 밝은 노란색
							break;
						case '맛집':
							scheduleTitle.style.backgroundColor = '#FF7043'; // 맛집: 주황색
							container.style.backgroundColor = '#FFE0B2'; // 밝은 주황색
							break;
						case '숙소':
							scheduleTitle.style.backgroundColor = '#4DB6AC'; // 숙소: 청록색
							container.style.backgroundColor = '#E0F2F1'; // 밝은 청록색
							break;
						case '관광지':
							scheduleTitle.style.backgroundColor = '#81C784'; // 관광지: 연녹색
							container.style.backgroundColor = '#E8F5E9'; // 밝은 녹색
							break;
						case '기타':
							scheduleTitle.style.backgroundColor = '#B39DDB'; // 기타: 연보라색
							container.style.backgroundColor = '#EDE7F6'; // 밝은 보라색
							break;
						default:
							scheduleTitle.style.backgroundColor = '#FFEB3B'; // 기본: 노란색
							container.style.backgroundColor = '#FFF8DC'; // 밝은 노란색
							break;
						}
					});
				});
	</script>
</body>
</html>
