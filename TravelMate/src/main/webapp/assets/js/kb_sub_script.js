// URL에서 id 파라미터 추출
function getQueryParameter(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

// 세부 정보 불러오기
document.addEventListener("DOMContentLoaded", () => {
    const id = getQueryParameter("id");

    if (id) {
        // 서버로부터 데이터 가져오기
        $.ajax({
            url: `/TravelMate/ScheduleController`, // 이 부분은 실제 서버 URL로 바꿔야 해
            type: 'GET',
            data: { id: id },
            dataType: 'json',
            success: function(scheduleData) {
                if (scheduleData) {
                    // 기존 정보 표시
                    document.getElementById("schedule-title").textContent = scheduleData.title;
                    document.getElementById("schedule-type").value = scheduleData.type;
                    document.getElementById("date").value = scheduleData.date;
                    document.getElementById("location").value = scheduleData.location;
                    document.getElementById("description").value = scheduleData.description;
                } else {
                    document.getElementById("schedule-title").textContent = "일정을 찾을 수 없습니다.";
                }
            },
            error: function(xhr, status, error) {
                console.error("일정을 불러오는 중 오류가 발생했습니다:", error);
            }
        });
    }

    // 저장 버튼 클릭 시 세부 정보 저장
    document.getElementById("save-btn").onclick = function() {
        const updatedData = {
            id: id,
            title: document.getElementById("schedule-title").textContent,
            type: document.getElementById("schedule-type").value,
            date: document.getElementById("date").value,
            location: document.getElementById("location").value,
            description: document.getElementById("description").value
        };

        // 서버로 데이터 저장 요청
        $.ajax({
            url: `/TravelMate/ScheduleController`, // 실제 서버 URL로 바꿔야 해
            type: 'POST',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify(updatedData),
            success: function(response) {
                alert("일정이 저장되었습니다!");
            },
            error: function(xhr, status, error) {
                console.error("일정을 저장하는 중 오류가 발생했습니다:", error);
            }
        });
    };
});
