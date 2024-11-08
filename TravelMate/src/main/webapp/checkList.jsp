<%@page import="com.tlg.model.CheckList"%>
<%@page import="java.util.List"%>
<%@page import="com.tlg.model.CheckListDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>CheckList</title>
<link rel="stylesheet" href="assets/css/checkListStyle.css">
<style>
	a {
		text-decoration: none;
		color: white;
	}
</style>
</head>
<%
	CheckListDAO dao = new CheckListDAO();

	List<CheckList> checkList = dao.getItem();
%>
<body>
	
	<div class="checklist-container">
	
        <h1>개인 준비물</h1>
        
        <form action="CheckListController" method="post">
	        <input type="text" id="taskInput" name="item" placeholder="필요한 준비물을 추가하세요" required>
	        <button>+</button>
	        <ul id="taskList">
	        	<%for(int i = 0; i< checkList.size(); i++) {%>
	        	<li>
	        		<input type="checkbox" name="checkBox">
	        		<%=checkList.get(i).getItem() %>
	        			<a href="CheckListDeleteController?item_idx=<%=checkList.get(i).getItem_idx() %>">
	        				<button type="button">X</button>
	        			</a>
	        	</li>        	
	        	<%} %>
	        </ul>
        </form>
    </div>
    
    <script src="assets/js/checkListScript.js"></script>
</body>
</html>