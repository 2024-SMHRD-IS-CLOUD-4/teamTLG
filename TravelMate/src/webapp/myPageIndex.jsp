<%@page import="com.tlg.model.TmMember"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>마이페이지</title>
    <link rel="stylesheet" href="assets/css/myPageStyle.css">
</head>
<body>

	<%@ include file = "header.jsp" %>
	
    <main>
        <section class="profile-section">
            <label for="profile-image-upload" class="profile-image-label">
                <img id="profile-image" src="/img/profile_3135707.png" alt="프로필 이미지">
            </label>
            <input type="file" id="profile-image-upload" accept="image/*" style="display: none;">
            <h2 id="username"><%=member.getNick() %></h2>
            <button class="edit-info-button" onclick="openModal()">회원정보 수정</button>
            <button class="edit-info-button" onclick="openDeleteModal()">회원탈퇴</button>
        </section>
        
        <section class="travel-plan-list">
            <div class="list-header">
                <h3>이전 여행 계획 list</h3>
                <div class="list-buttons">
                    <button>전체 삭제</button>
                    <button>선택 삭제</button>
                </div>
            </div>
            <table>
                <tr><th>No.</th></tr>
                <!-- 여행 계획 항목 추가 예정 -->
            </table>
        </section>
    </main>

    <!-- 회원정보 수정 모달 -->
    <div id="modal" class="modal">
        <div class="modal-content">
            <h2>회원정보 수정</h2>
            <form action="UpdateController" method="post">
            <input type="hidden" name="id" value="<%=member.getId() %>">
            <input type="password" id="current-password" placeholder="기존 비밀번호를 입력하세요.">
            <input type="password" id="new-password" name="pw" placeholder="새로운 비밀번호를 입력하세요.">
            <input type="text" id="new-username" name="nick" placeholder="바꿀 닉네임을 입력하세요.">
            <div class="modal-buttons">
                <button>회원정보 수정</button>
                <button type="button" onclick="closeModal()">닫기</button>
            </div>
            </form>
                     
        </div>
    </div>
    
    <!-- 회원탈퇴 모달 -->
	<div id="delete-modal" class="modal">
		<div class="modal-content">
			<h2>회원탈퇴</h2>
			<form action="DeleteController" method="post">
			<input type="password" name="pw" placeholder="기존 비밀번호를 입력하세요.">
			
			<div class="modal-buttons">
				<button>회원탈퇴</button>
				<button type="button" onclick="closeDeleteModal()">닫기</button>
			</div>
			</form>
		</div>
	</div>

    <script src="assets/js/myPageScript.js"></script>
    <script>
    function openDeleteModal() {
        document.getElementById("delete-modal").style.display = "block";
    }
    function closeDeleteModal() {
        document.getElementById("delete-modal").style.display = "none";
    }
    </script>
</body>
</html>