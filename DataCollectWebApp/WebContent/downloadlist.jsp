<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    
    <title>下载列表</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
  </head>
  
  <body>	
	<table border="1" align="center">
		<tr>
			<th>序号</th>
			<th>文件名</th>
			<th>操作</th>
		</tr>
		<c:forEach var="en" items="${requestScope.fileNames}" varStatus="vs">
			<tr>
				<td>${vs.count }</td>
				<td>${en.value }</td>
				<td>
					<%--<a href="${pageContext.request.contextPath }/fileServlet?method=down&..">下载</a>--%>
					<!-- 构建一个地址  -->
					<c:url var="url" value="FileServlet">
						<c:param name="method" value="down"></c:param>
						<c:param name="fileName" value="${en.key}"></c:param>
					</c:url>
					<!-- 使用上面地址 -->
					<a href="${url }">下载</a>
				</td>
			</tr>
		</c:forEach>
	</table>  		
  </body>
</html>
