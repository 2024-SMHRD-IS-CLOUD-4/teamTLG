<%@page import="com.tlg.model.TmMember"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<%
	TmMember member = (TmMember) session.getAttribute("member");
%>
<body>
    <form action="http://192.168.219.48:5000/result" method="post" enctype="multipart/form-data">
        <fieldset style="width: 150px;">
            <div>
                <input type="hidden" name="id" value="<%=member.getId()%>">
            </div>
            <div>
                img:<input type="file" name="ocr_img" required>
            </div>
            <div>
                date:<input type="date" name="created_at" required>
            </div>
            <input type="submit" value="제출">
        </fieldset>
    </form>
</body>
</html>