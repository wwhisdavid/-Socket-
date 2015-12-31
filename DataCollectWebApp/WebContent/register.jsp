<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
	table
  	{
  		border-collapse:collapse;
  		margin: auto;
  	}

	table, td, th
  	{
 		 border:1px solid black;
  	}
  	div {
  		background
  	}
</style>
<title>注册</title>
</head>
<body style="background-image:url('http://news.cjn.cn/cjht1/201310/W020131019668788168194.jpg');">

	<form name="form1" action="${pageContext.request.contextPath }/UserServlet?method=register" method="post" >
		<div style="height:200px"></div>
		
		<table align="center" width="400px" bgcolor="white" >
			<tr>
				<th>用户名</th>
			<td align="center">
				<input type="text" name="userName"/>
				${requestScope.message}
			</td>
		</tr>
			
		<tr>
			<th>
				密码
			</th>
			<td align="center">
				<input type="password" name="password"/>
			</td>
		</tr>
		
		<tr>
			<td colspan="2" align="center">
				<input type="submit" value="注册"/>
			</td>
		</tr>
	</table>	
	</form>
</body>
</html>