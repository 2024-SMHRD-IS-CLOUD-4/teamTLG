<%@page import="com.tlg.model.TmMember"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<%
	TmMember member = (TmMember) session.getAttribute("member");
%>
<body>
	<form action="http://192.168.219.48:5000/result" method="post">
        <fieldset style="width: 150px;">
            <input type="hidden" name="id" value="<%=member.getId()%>">
            <div>
                img:<input type="file" name="ocr_img">
            </div>
            <div>
                date:<input type="date" name="created_at">
            </div>
            <input type="submit" value="제출">
        </fieldset>
    </form>
</body>
</html>