<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>项目列表</title>
</head>
<body>
	<c:out value="${requestScope.loginName}"></c:out>
	<div style="height:10px">${requestScope.loginName}</div>
	<table border="1" width="80%" align="center" cellpadding="5" cellspacing="0">
		<tr>
			<th>序号</th>
			<th>项目ID</th>
			<th>项目名</th>
			<th>项目简介</th>
			<th>选项</th>
		</tr>
		<c:choose>
			<c:when test="${not empty requestScope.pageBean.pageData}">
				<c:forEach var="node" items="${requestScope.pageBean.pageData}" varStatus="vs">
					<tr>
  						<td>${vs.count }</td>
  						<td>${node.project_id }</td>
  						<td>${node.name }</td>
  						<td>${node.description }</td>
  						<td><a href="${pageContext.request.contextPath }/NodeListServlet?table=${node.child_table}">查看数据</a></td>
  					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
  				<tr>
  					<td colspan="5">对不起，没有你要找的数据</td>
  				</tr>
  			</c:otherwise>
		</c:choose>
		<tr>
  			<td colspan="5" align="center">
  				当前${requestScope.pageBean.currentPage }/${requestScope.pageBean.totalPage }页     &nbsp;&nbsp;
  				
  				<a href="${pageContext.request.contextPath }/ProjectListServlet?method=login&currentPage=1">首页</a>
  				<a href="${pageContext.request.contextPath }/ProjectListServlet?method=login&currentPage=${requestScope.pageBean.currentPage-1}">上一页 </a>
  				<a href="${pageContext.request.contextPath }/ProjectListServlet?method=login&currentPage=${requestScope.pageBean.currentPage+1}">下一页 </a>
  				<a href="${pageContext.request.contextPath }/ProjectListServlet?method=login&currentPage=${requestScope.pageBean.totalPage}">末页</a>
  			</td>
  		</tr>
	</table>
</body>
</html>