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

// 여행 계획 제출 및 저장 성공 시 메시지 표시
function handleSubmit(event) {
    event.preventDefault(); // 폼의 기본 제출 방식을 막음

    const formData = new FormData(document.getElementById("planForm"));

    fetch("TravelPlanController", {
        method: "POST",
        body: formData
    })
    .then(response => {
        if (response.ok) {
            showToastMessage(); // 저장 성공 시 메시지 표시
            setTimeout(closePlanForm, 2000); // 2초 후에 모달 닫기
        } else {
            alert("저장 실패! 다시 시도해 주세요.");
        }
    })
    .catch(error => {
        console.error("Error:", error);
        alert("오류가 발생했습니다.");
    });
}


// 저장 성공 메시지 표시
function showToastMessage() {
    const toast = document.getElementById("toastMessage");
    toast.classList.add("show");
    setTimeout(() => {
        toast.classList.remove("show");
    }, 2000); // 2초 동안 메시지를 표시
}





