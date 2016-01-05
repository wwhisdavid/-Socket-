<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript">
	function go2reister(){
		window.location.href="register.jsp";
	}
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登陆</title>
</head>
<body style="background-image:url('http://news.cjn.cn/cjht1/201310/W020131019668788168194.jpg');">

	<form name="form2" action="${pageContext.request.contextPath }/UserServlet?method=login" method="post" >
		<div style="height:200px"></div>
		
		<table align="center" width="400px" bgcolor="white" >
			<tr>
				<th>用户名</th>
			<td align="center">
				<input type="text" name="username"/>
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
				<input type="submit" value="登陆"/> &nbsp; &nbsp; <input type="button" onclick="go2reister()" value="注册"/>
			</td>
		</tr>
		
	</table>	
	</form>
</body>
</html>