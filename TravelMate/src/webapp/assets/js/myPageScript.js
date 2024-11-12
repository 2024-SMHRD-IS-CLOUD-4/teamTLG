// 프로필 이미지 업로드
document.getElementById("profile-image-upload").addEventListener("change", function(event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            document.getElementById("profile-image").src = e.target.result;
        };
        reader.readAsDataURL(file);
    }
});

// 모달 열기
function openModal() {
    document.getElementById("modal").style.display = "block";
}

// 모달 닫기
function closeModal() {
    document.getElementById("modal").style.display = "none";
}