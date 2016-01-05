<%@ page language="java" contentType="text/html; charset=UTF-8"  
    pageEncoding="UTF-8"%>  
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
<html>  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
<title>百度地图</title>  
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.3"></script>  
<script type="text/javascript" src="lib/map/baidu/convertor.js"></script>  
</head>  
<body>  
  
  
<div style="width:800px;height:600px;border:1px solid gray" id="container"></div>
<div id="r-result">
		经度: <input id="longitude" type="text" style="width:100px; margin-right:10px;" />
		纬度: <input id="latitude" type="text" style="width:100px; margin-right:10px;" />
		<input type="button" value="查询" onclick="theLocation()" />
</div>  
<table border="1" width="80%" align="center" cellpadding="5" cellspacing="0">
		<tr>
			<th>序号</th>
			<th>节点ID</th>
			<th>节点名</th>
			<th>节点经度</th>
			<th>节点纬度</th>
		</tr>
		<c:choose>
			<c:when test="${not empty requestScope.pageBean.pageData}">
				<c:forEach var="node" items="${requestScope.pageBean.pageData}" varStatus="vs">
					<tr>
  						<td>${vs.count }</td>
  						<td>${node.node_id }</td>
  						<td>${node.name }</td>
  						<td>${node.logitude }</td>
  						<td>${node.latitude }</td>
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
  				
  				<a href="${pageContext.request.contextPath }/NodeListServlet?mode=map&currentPage=1">首页</a>
  				<a href="${pageContext.request.contextPath }/NodeListServlet?mode=map&currentPage=${requestScope.pageBean.currentPage-1}">上一页 </a>
  				<a href="${pageContext.request.contextPath }/NodeListServlet?mode=map&currentPage=${requestScope.pageBean.currentPage+1}">下一页 </a>
  				<a href="${pageContext.request.contextPath }/NodeListServlet?mode=map&currentPage=${requestScope.pageBean.totalPage}">末页</a>
  			</td>
  		</tr>
	</table>
<script type="text/javascript">  
var map = new BMap.Map("container");  // 创建地图实例  
//var longitude=getUrlParam("longitude");//经度  
//var latitude=getUrlParam("latitude");//纬度  
var longitude="114.3666";//经度  
var latitude="30.532207";//纬度  
var point = new BMap.Point(longitude, latitude);  // 创建点坐标  
map.centerAndZoom(point, 15);  // 初始化地图，设置中心点坐标和地图级别  
map.addControl(new BMap.NavigationControl()); //添加平移缩放控件  
map.addControl(new BMap.ScaleControl());  //添加放大、缩小控件  
map.enableScrollWheelZoom();//允许鼠标滑轮操作  
  
  
//坐标转换完之后的回调函数  
  
translateCallback = function (point){  
  
    var marker = new BMap.Marker(point);  
  
    map.addOverlay(marker);  
  
    //根据坐标得到地址描述    
    var myGeo = new BMap.Geocoder();    
    myGeo.getLocation(point, function(result){    
        if (result){    
            var label = new BMap.Label(result.address,{offset:new BMap.Size(20,-10)});  
            marker.setLabel(label);  
        }});    
      
    // 将标注添加到地图中  
    map.addOverlay(marker);  
      
    //将坐标设置为地图中心位置  
    map.setCenter(point);  
}  
  
  
  
setTimeout(function(){  
    BMap.Convertor.translate(new BMap.Point(longitude, latitude),0,translateCallback);     //真实经纬度转成百度坐标  
}, 2000);  
  
//用经纬度设置地图中心点
function theLocation(){
	if(document.getElementById("longitude").value != "" && document.getElementById("latitude").value != ""){
		map.clearOverlays(); 
		var new_point = new BMap.Point(document.getElementById("longitude").value,document.getElementById("latitude").value);
		var new_point2 = new BMap.Point(document.getElementById("longitude").value+0.001,document.getElementById("latitude").value);
		var marker = new BMap.Marker(new_point);  // 创建标注
		var mark2 = new BMap.Marker(new_point2);
		map.addOverlay(marker); // 将标注添加到地图中/*
		map.addOverlay(mark2);
		map.panTo(new_point);       
	}
}
  
</script>  

</body>  
</html>  