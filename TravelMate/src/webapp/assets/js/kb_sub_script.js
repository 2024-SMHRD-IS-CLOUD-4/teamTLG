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

// 항목 추가를 클릭하면 투표 선택지가 늘어나고 텍스트를 입력할 수 있게 구현
// 항목 추가를 클릭하면 투표 선택지에 체크박스와 입력 요소를 추가
function addVoteOption() {
	// 새로운 li 요소 생성
	const newOption = document.createElement('li');

	// 체크박스 생성
	const checkbox = document.createElement('input');
	checkbox.type = 'checkbox';
	checkbox.classList.add('vote-checkbox');
	checkbox.style.display = 'none'; // 초기에는 체크박스를 숨김
	checkbox.onclick = showCompleteVoteButton; // 체크박스 클릭 시 투표 완료 버튼 보이기

	// 입력 요소 생성
	const input = document.createElement('input');
	input.type = 'text';
	input.placeholder = '새로운 선택지를 입력하세요';
	input.classList.add('poll-option'); // 스타일 적용

	// 삭제 버튼 생성
	const deleteButton = document.createElement('button');
	deleteButton.textContent = '삭제';
	deleteButton.classList.add('delete-btn');
	deleteButton.onclick = function() {
		newOption.remove();  // 항목 삭제
		showCompleteVoteButton(); // 남아있는 체크박스 상태 확인 후 버튼 처리
	};

	// li에 체크박스, 입력 요소, 삭제 버튼 추가
	newOption.appendChild(checkbox);
	newOption.appendChild(input);
	newOption.appendChild(deleteButton);

	// vote-options에 새 항목 추가
	const voteOptionsList = document.getElementById('vote-options');
	if (voteOptionsList) {
		voteOptionsList.appendChild(newOption);
	} else {
		console.error('vote-options 요소를 찾을 수 없습니다.');
	}
}

// 체크박스 클릭 시 '투표 완료' 버튼을 보이게 함
function showCompleteVoteButton() {
	const checkboxes = document.querySelectorAll('.vote-checkbox');
	const completeVoteButton = document.getElementById('complete-vote-btn');

	// 체크된 항목이 있으면 '투표 완료' 버튼 표시
	const isAnyChecked = Array.from(checkboxes).some(checkbox => checkbox.checked);
	completeVoteButton.style.display = isAnyChecked ? 'inline-block' : 'none';
}

// 투표 완료 버튼 클릭 시 선택된 항목을 데이터에 반영
function completeVoting() {
	const checkboxes = document.querySelectorAll('.vote-checkbox');
	const selectedOptions = [];
	checkboxes.forEach((checkbox) => {
		if (checkbox.checked) {
			const optionText = checkbox.nextElementSibling.value;
			selectedOptions.push(optionText);
		}
	});

	if (selectedOptions.length > 0) {
		// 서버로 데이터 전송
		fetch('VoteController', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				selectedOptions: selectedOptions
			})
		})
			.then(response => response.text())
			.then(data => {
				alert("투표가 완료되었습니다.");
				console.log("서버 응답:", data);
			})
			.catch(error => {
				console.error("에러 발생:", error);
			});
	} else {
		alert("선택된 항목이 없습니다.");
	}

	checkboxes.forEach(checkbox => {
		checkbox.checked = false;
		checkbox.style.display = 'none';
	});
	document.getElementById('complete-vote-btn').style.display = 'none';
}


// 투표 취소 버튼 클릭 시 동작 정의
function cancelVoting() {
	const startButton = document.querySelector('button[onclick="startVoting()"]');
	const addButton = document.querySelector('button[onclick="addVoteOption()"]');
	const cancelVoteButton = document.getElementById('cancel-vote-btn');
	const deleteButtons = document.querySelectorAll('.delete-btn');
	const voteOptionsList = document.getElementById('vote-options');

	// 투표 취소 확인
	const isConfirmed = confirm("투표를 취소 하시겠습니까?");

	if (isConfirmed) {
		// 투표 취소 후 상태로 되돌리기
		startButton.textContent = '투표 시작';
		addButton.style.display = 'block'; // '항목 추가' 버튼 다시 보이기
		cancelVoteButton.style.display = 'none'; // '투표 취소' 버튼 숨기기

		deleteButtons.forEach(button => {
			button.style.display = 'inline-block';
		});

		// 모든 추가된 항목 삭제
		while (voteOptionsList.firstChild) {
			voteOptionsList.removeChild(voteOptionsList.firstChild);
		}

		// 체크박스 숨기기
		const checkboxes = document.querySelectorAll('.vote-checkbox');
		checkboxes.forEach(checkbox => {
			checkbox.style.display = 'none';
		});

		alert("투표가 취소되었습니다.");
	} else {
		// 취소 시 아무런 변화 없이 그대로 진행
		return;
	}
}


// 투표 시작 버튼을 클릭할 때의 동작 정의
function startVoting() {
	const startButton = document.querySelector('button[onclick="startVoting()"]');
	const addButton = document.querySelector('button[onclick="addVoteOption()"]');
	const cancelVoteButton = document.getElementById('cancel-vote-btn');
	const deleteButtons = document.querySelectorAll('.delete-btn');
	const optionInputs = document.querySelectorAll('.poll-option');
	const checkboxes = document.querySelectorAll('.vote-checkbox');

	if (startButton.textContent === '투표 시작') {
		startButton.textContent = '투표 종료';
		addButton.style.display = 'none'; // '항목 추가' 버튼 숨기기
		cancelVoteButton.style.display = 'inline-block'; // '투표 취소' 버튼 보이기

		// 모든 삭제 버튼 숨기기 및 입력 필드 비활성화
		deleteButtons.forEach(button => {
			button.style.display = 'none';
		});
		optionInputs.forEach(input => {
			input.disabled = true; // 입력 필드 비활성화
		});

		// 체크박스 보이기
		checkboxes.forEach(checkbox => {
			checkbox.style.display = 'inline-block';
		});

		alert("투표가 시작되었습니다.");
	} else {
		startButton.textContent = '투표 시작';
		addButton.style.display = 'block'; // '항목 추가' 버튼 보이기
		cancelVoteButton.style.display = 'none'; // '투표 취소' 버튼 숨기기

		// 모든 삭제 버튼 보이기 및 입력 필드 활성화
		deleteButtons.forEach(button => {
			button.style.display = 'inline-block';
		});
		optionInputs.forEach(input => {
			input.disabled = false; // 입력 필드 활성화
		});

		// 체크박스 숨기기
		checkboxes.forEach(checkbox => {
			checkbox.style.display = 'none';
		});

		alert("투표가 종료되었습니다.");
	}
}

function openDetailPage() {
	const url = "airTicket.jsp"; // 세부 페이지 URL
	const options = "width=350,height=200,top=50,left=1000"
	window.open(url, '_blank', options); // 새 창에서 열기
}