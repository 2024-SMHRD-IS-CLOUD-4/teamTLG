
// 로그인 여부를 확인하는 변수
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

document.getElementById("login").addEventListener('click', login);

// 회원가입 함수
function signup() {
    // 회원가입 로직 구현 가능 (예: ID/PW 저장)
    closeModal('signupModal');
    alert("회원가입 성공!");
}

// 모달 열기
function openModal(modalId) {
    document.getElementById(modalId).style.display = 'flex';
}

// 모달 닫기
function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

// 메인으로 이동
function goToMain() {
	location.href="index.jsp"
}
