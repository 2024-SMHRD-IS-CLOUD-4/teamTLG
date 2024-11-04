// URL에서 id 파라미터 추출
function getQueryParameter(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

// 세부 정보 불러오기
document.addEventListener("DOMContentLoaded", () => {
    const id = getQueryParameter("id");

    if (id) {
        const scheduleData = JSON.parse(localStorage.getItem(id));

        if (scheduleData) {
            // 기존 정보 표시
            document.getElementById("schedule-title").value = scheduleData.title;
            document.getElementById("schedule-type").value = scheduleData.type;
            document.getElementById("date").value = scheduleData.date;
            document.getElementById("location").value = scheduleData.location;
            document.getElementById("description").value = scheduleData.description;
        } else {
            document.getElementById("schedule-title").textContent = "일정을 찾을 수 없습니다.";
        }
    }

    // 저장 버튼 클릭 시 세부 정보 저장
    document.getElementById("save-btn").onclick = function() {
        const updatedData = {
            title: document.getElementById("schedule-title").value,
            type: document.getElementById("schedule-type").value,
            date: document.getElementById("date").value,
            location: document.getElementById("location").value,
            description: document.getElementById("description").value
        };

        // 로컬 저장소에 업데이트된 데이터 저장
        localStorage.setItem(id, JSON.stringify(updatedData));
        alert("일정이 저장되었습니다!");
    };
});
