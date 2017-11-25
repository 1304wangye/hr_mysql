<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	//删除登录标识
	session.removeAttribute("empLoginName");
%>
<script>
	window.top.location.href = 'login.html';
</script>