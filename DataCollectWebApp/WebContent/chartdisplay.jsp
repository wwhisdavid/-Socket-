<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	function checkDay(){
		var inputNode = document.getElementById("day");
		if(inputNode > 0 && inputNode < 32)
			return true;
		else 
			return false;
	}
	
	function checkHours(){
		var inputNode = document.getElementById("hours");
		if(inputNode >= 0 && inputNode < 25)
			return true;
		else 
			return false;
	}
	
	function checkAll(){
		var day = checkDay();	
		var hours = checkHours();
		if(day&&hours){
			return true;
		}else{
			return false;
		}
	}
	
/*
 表单提交的时候是会触发onsubmit事件的，如果onsubmit事件的方法返回是true，那么该表单允许提交，如果返回的是false，该表单不允许提交。

*/
</script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>图表展示</title>
<script type="text/javascript">
	/* var text = '${pageContext.request.contextPath }/NodeDetailServlet?node_id=${requestScope.node_id}&currentPage=1'; */
</script>
</head>
<body>
	<%-- <c:out value="${requestScope.node_name}"></c:out> --%>
	<form action="${pageContext.request.contextPath }/FilterDataServlet" method="get"  onsubmit="return checkAll()"> <!--如果表单提交时候触发的方法返回是false,那么该表单不允许提交，如果返回的是true允许提交 -->
			<table border="1px" width="50%" align="center" cellspacing="0px" cellpadding="3px">
				
	
				
				<tr>
					<td>处理方式</td><td>
						<input type="radio" checked="ture" name="download" id="download" value="download"/>
					下载数据文件
						<input type="radio" name="transfer" value="transfer"/>
					传输至客户端</td>
					<td>
						<input type="hidden" name="node_id" value="${requestScope.node_id}" />
					</td>
				</tr>
				<tr>
					<td>数据字段</td><td>
						<input type="checkbox"  name="temperature" />
					温度
						<input type="checkbox" name="humidity" />
					湿度
						<input type="checkbox" name="stress-x"/>
					应力-x  
						<input type="checkbox" name="stress-y"/>
					应力-y
						<input type="checkbox" name="stress-z"/>
					应力-z
                    <span id="hobbySpan"></span>
                    </td>
				</tr>
				<tr>
					<td>查询时间（起始）</td><td>
					<select name="year" id="year">
						<option value=""> 请选择</option>
						<option value="2013"> 2013 </option>
						<option value="2014"> 2014 </option>
						<option value="2015"> 2015 </option>
					</select> 年
					<select name="month" id="month">
						<option value=""> 请选择</option>
						<option value="1"> 1 </option>
						<option value="2"> 2 </option>
						<option value="3"> 3 </option>
						<option value="4"> 4 </option>
						<option value="5"> 5 </option>
						<option value="6"> 6 </option>
						<option value="7"> 7 </option>
						<option value="8"> 8 </option>
						<option value="9"> 9 </option>
						<option value="10"> 10 </option>
						<option value="11"> 11 </option>
						<option value="12"> 12 </option>
					</select> 月
					<input type="text" name="day1" id="day" onblur="checkDay()"/> 日
					<input type="text" name="hours1" id="hours" onblur="checkHours()"/> 时
                    
                    </td>
				</tr>
				<tr>
					<td>查询时间（终止）</td><td>
					<select name="year" id="year">
						<option value=""> 请选择</option>
						<option value="2013"> 2013 </option>
						<option value="2014"> 2014 </option>
						<option value="2015"> 2015 </option>
					</select> 年
					<select name="month" id="month">
						<option value=""> 请选择</option>
						<option value="1"> 1 </option>
						<option value="2"> 2 </option>
						<option value="3"> 3 </option>
						<option value="4"> 4 </option>
						<option value="5"> 5 </option>
						<option value="6"> 6 </option>
						<option value="7"> 7 </option>
						<option value="8"> 8 </option>
						<option value="9"> 9 </option>
						<option value="10"> 10 </option>
						<option value="11"> 11 </option>
						<option value="12"> 12 </option>
					</select> 月
					<input type="text" name="day2" id="day" onblur="checkDay()"/> 日
					<input type="text" name="hours2" id="hours" onblur="checkHours()"/> 时
                    
                    </td>
				</tr>
				<tr align="center">
					<td colspan="2"><!--提交按钮-->
					<input type="submit" value="生成数据并传输至PC"/>（请确保客户端已经打开）
					
					</td>
				</tr>
			</table>
		</form>
</body>
	
	<img src="ChartServlet"/> <br>
	<table border="1" width="80%" align="center" cellpadding="5"
		cellspacing="0" id="table">
		<tr>
			<th>序号</th>
			<th>时间</th>
			<th>温度</th>
			<th>湿度</th>
			<th>应力-x</th>
			<th>应力-y</th>
			<th>应力-z</th>
		</tr>

		<c:choose>
			<c:when test="${not empty requestScope.pageBean.pageData}">
				<c:forEach var="node" items="${requestScope.pageBean.pageData}"
					varStatus="vs">
					<tr>
						<td>${vs.count }</td>
						<td>${node.record_time }</td>
						<td>${node.temperature }</td>
						<td>${node.humidity }</td>
						<td>${node.stress_x }</td>
						<td>${node.stress_y }</td>
						<td>${node.stress_z }</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="7">对不起，没有你要找的数据</td>
				</tr>
			</c:otherwise>
		</c:choose>
		<tr>
			<td colspan="7" align="center">
				当前${requestScope.pageBean.currentPage }/${requestScope.pageBean.totalPage }页
				&nbsp;&nbsp; 
				<a href='${pageContext.request.contextPath }/NodeDetailServlet?node_id=${requestScope.node_id}&currentPage=1'>首页</a> 
				<a href="${pageContext.request.contextPath }/NodeDetailServlet?node_id=${requestScope.node_id}&currentPage=${requestScope.pageBean.currentPage-1}">上一页</a> 
				<a href="${pageContext.request.contextPath }/NodeDetailServlet?node_id=${requestScope.node_id}&currentPage=${requestScope.pageBean.currentPage+1}">下一页</a> 
				<a href="${pageContext.request.contextPath }/NodeDetailServlet?node_id=${requestScope.node_id}&currentPage=${requestScope.pageBean.totalPage}">末页</a>
			</td>
		</tr>
	</table>
</body>
</html>