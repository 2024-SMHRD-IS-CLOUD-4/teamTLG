document.addEventListener("DOMContentLoaded", function() {
    // URL에서 card_idx와 col_idx 가져오기
    const urlParams = new URLSearchParams(window.location.search);
    const card_idx = urlParams.get('id');
    const col_idx = urlParams.get('col_idx'); // col_idx 추가

    if (card_idx && col_idx) {
        loadCardDetails(card_idx, col_idx);
    }
});

function loadCardDetails(card_idx, col_idx) {
    fetch(`/TravelMate/ScheduleController?card_idx=${card_idx}&col_idx=${col_idx}`)
        .then(response => response.json())
        .then(data => {
            // card_title은 항상 설정
            document.getElementById("schedule-title").textContent = data.card_title || "일정 제목";

            // scheduleDetails가 존재하면 처리, 없으면 기본값 설정
            if (data.scheduleDetails) {
                const scheduleTypeSelect = document.getElementById("schedule-type");
                scheduleTypeSelect.value = data.scheduleDetails.sche_type || "";

                // formattedTime이 존재하면, 없으면 빈값 설정
                const formattedTime = data.scheduleDetails.sche_tm ? formatTime(data.scheduleDetails.sche_tm) : "";
                document.getElementById("time").value = formattedTime;

                document.getElementById("location").value = data.scheduleDetails.sche_place || "";
                document.getElementById("description").value = data.scheduleDetails.sche_desc || "";

                // 배경색 설정을 위해 change 이벤트 트리거
                scheduleTypeSelect.dispatchEvent(new Event('change'));
            } else {
                console.warn("scheduleDetails is undefined in response.");
                // 데이터 없을 때 기본값 설정
                document.getElementById("location").value = "";
                document.getElementById("description").value = "";
                document.getElementById("time").value = "";
            }
        })
        .catch(error => console.error('Error fetching card details:', error));
}

// 시간 포맷팅 함수
function formatTime(time) {
    const date = new Date(time);
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

// 업데이트된 일정 저장 함수
function updateSchedule() {
    const cardIdx = document.getElementById("card_idx").value;
    const colIdx = document.getElementById("col_idx").value;
    const scheType = document.getElementById("schedule-type").value;
    const schePlace = document.getElementById("location").value;
    const scheDesc = document.getElementById("description").value;
    const scheTime = document.getElementById("time").value;

    // 서버에 전송할 데이터를 동적으로 설정
    const data = {
        card_idx: cardIdx,
        col_idx: colIdx,
        sche_type: scheType,
        sche_place: schePlace,
        sche_desc: scheDesc,
        sche_tm: scheTime
    };

    // fetch로 요청 보내기
    fetch(`/TravelMate/ScheduleController`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json' // JSON 형식으로 전송
        },
        body: JSON.stringify(data) // 데이터를 JSON 형식으로 전송
    })
    .then(response => response.json())
    .then(data => {
		console.log('response data : ' , data);
        if (data.success) {
            alert("스케줄이 수정되었습니다!");
            window.close(); // 수정 후 창을 닫기
        } else {
            alert("스케줄 수정에 실패했습니다. 다시 시도해주세요.");
        }
    })
    .catch(error => console.error('Error updating schedule:', error));
}
