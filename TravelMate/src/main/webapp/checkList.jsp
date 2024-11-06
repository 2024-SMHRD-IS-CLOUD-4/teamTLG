<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>CheckList</title>
<link rel="stylesheet" href="assets/css/checkListStyle.css">
</head>
<body>
	<div class="checklist-container">
        <h1>개인 준비물</h1>
        <input type="text" id="taskInput" placeholder="필요한 준비물을 추가하세요" />
        <button onclick="addTask()">+</button>
        

        <ul id="taskList"></ul>
        <button id="clearAllBtn" onclick="clearAllTasks()">전체 삭제</button>
    </div>
    <script src="assets/js/checkListScript.js"></script>
</body>
</html>