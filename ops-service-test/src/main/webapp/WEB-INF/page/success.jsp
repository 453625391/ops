<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>开发平台测试工具类</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link type="text/css" href="css/page.css" rel="stylesheet" />
<script type="text/javascript"
	src="http://apps.bdimg.com/libs/jquery/2.1.1/jquery.js"></script>
</head>
<body>
	啊哈
	<%
		String user_token = (String)request.getSession().getAttribute("user_token");
	%>
	<p><%=user_token %></p>
</body>
</html>

