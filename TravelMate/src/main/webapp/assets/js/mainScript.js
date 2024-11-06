/*// 로그인 여부를 확인하는 변수
let isLoggedIn = false;

// 로그인 상태를 확인하여 모달 열기
function checkLoginStatus() {
    if (isLoggedIn) {
        openModal('myPageModal');
    } else {
        openModal('loginModal');
    }
}

// 로그인 함수
function login() {
    // 로그인 로직 구현 가능 (예: ID/PW 확인)
	if(isLoggedIn = true) {
		alert("로그인 성공!");
	}
}

// 회원가입 함수
function signup() {
    // 회원가입 로직 구현 가능 (예: ID/PW 저장)
    closeModal('signupModal');
    alert("회원가입 성공!");
}*/

// 모달 열기
function openModal(modalId) {
    document.getElementById(modalId).style.display = 'flex';
}

// 모달 닫기
function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

// 회원가입 함수
function signup() {
	// 회원가입 로직 구현 가능 (예: ID/PW 저장)
	closeModal('signupModal');
	alert("회원가입 성공!");
}

function openPlanForm() {
    const planFormModal = document.getElementById("planFormModal");
    planFormModal.style.display = "block"; // 여행 계획 모달 열기
}

function closePlanForm() {
    const planFormModal = document.getElementById("planFormModal");
    planFormModal.style.display = "none"; // 여행 계획 모달 닫기
}


// 메인으로 이동
function goToMain() {
	location.href="mainPage.jsp"
}

// 여행 계획 제출
function submitTravelPlan() {
	const tr_title = document.querySelector('input[name="tr_title"]').value;
	const tr_st_dt = document.querySelector('input[name="tr_st_dt"]').value;
	const tr_ed_dt = document.querySelector('input[name="tr_ed_dt"]').value;
	/*const partner_name = document.querySelector('textarea[name="partner_name"]').value;*/

	const travelPlanData = {
		tr_title: tr_title,
		tr_st_dt: tr_st_dt,
		tr_ed_dt: tr_ed_dt,
		/*partner_name: partner_name*/
	};

	fetch('/TravelPlanController', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(travelPlanData)
	})
		.then(response => response.json())
		.then(data => {
			if (data.planResult === 'success') {
				alert('여행 계획이 성공적으로 저장되었습니다.');
				closePlanForm();
				location.reload();
			} else {
				alert('여행 계획 저장에 실패했습니다.');
			}
		})
		.catch(error => console.error('Error:', error));
}




