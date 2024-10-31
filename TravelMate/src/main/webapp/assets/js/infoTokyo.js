function openGoogleSearch(event) {
    event.preventDefault();  // 기본 폼 제출 방지
    const query = document.getElementById('google-search-input').value;
    const googleUrl = `https://www.google.com/search?q=${encodeURIComponent(query)}`;
    window.open(googleUrl, 'googleWindow', 'width=1000,height=600');  // 새 창 크기 설정
}