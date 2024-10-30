<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="JoinController" method="post">
		<h1>Travel Mate 회원가입</h1>
		<input type="text" name="id" placeholder="아이디를 입력해주세요."><br>
		<input type="text" name="pw" placeholder="비밀번호를 입력해주세요."><br>
		<input type="text" name="name" placeholder="이름을 입력해주세요"><br>
		<input type="text" name="nick" placeholder="닉네임을 입력해주세요"><br>
		성별 체크
		남<input type="radio" name="gender" value="m">
		여<input type="radio" name="gender" value="f"><br>
		<input type="text" name=email placeholder="이메일을 입력해주세요"><br>
		<input type="submit" value="회원가입">
	</form>
</body>
</html>