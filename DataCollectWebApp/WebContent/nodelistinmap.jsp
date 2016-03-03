<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>百度地图</title>
<!-- 
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.3"></script>  
<script type="text/javascript" src="lib/map/baidu/convertor.js"></script>  -->
</head>
<body>

	<div style="height:50px;text-align:right;">您好，<c:out value="${sessionScope.loginName}"/> &nbsp;&nbsp;<a href="${pageContext.request.contextPath }/UserServlet?method=logout">退出登陆</a></div>
	<div class="demo_main">
		<fieldset class="demo_title">百度地图API显示多个标注点带提示的代码</fieldset>
		<fieldset class="demo_content">
			<div style="min-height: 300px; width: 100%;" id="map"></div>

		</fieldset>
	</div>
	<table border="1" width="80%" align="center" cellpadding="5"
		cellspacing="0" id="table">
		<tr>
			<th>序号</th>
			<th>节点ID</th>
			<th>节点名</th>
			<th>节点经度</th>
			<th>节点纬度</th>
		</tr>

		<c:choose>
			<c:when test="${not empty requestScope.pageBean.pageData}">
				<c:forEach var="node" items="${requestScope.pageBean.pageData}"
					varStatus="vs">
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
				当前${requestScope.pageBean.currentPage }/${requestScope.pageBean.totalPage }页
				&nbsp;&nbsp; <a
				href="${pageContext.request.contextPath }/NodeListServlet?mode=map&currentPage=1">首页</a>
				<a
				href="${pageContext.request.contextPath }/NodeListServlet?mode=map&currentPage=${requestScope.pageBean.currentPage-1}">上一页
			</a> <a
				href="${pageContext.request.contextPath }/NodeListServlet?mode=map&currentPage=${requestScope.pageBean.currentPage+1}">下一页
			</a> <a
				href="${pageContext.request.contextPath }/NodeListServlet?mode=map&currentPage=${requestScope.pageBean.totalPage}">末页</a>
			</td>
		</tr>
	</table>

<script type="text/javascript">
    function map_init() {

    var table = document.getElementById("table");
    var rows = table.rows;
    var array = new Array();

    for (var index = 1; index < rows.length - 1; index++){
        var node = {
            count : '',
            node_id : '',
            name : '',
            longitude : 0,
            latitude : 0
        };
        for (var j = 0; j < rows[index].cells.length; j ++){
            var content = rows[index].cells[j];
            /* alert(content.innerHTML); */
            if(j == 0)
                node.count = content.innerHTML;
            if(j == 1)
                node.node_id = content.innerHTML;
            if(j == 2)
                node.name = content.innerHTML;
            if(j == 3)
                node.longitude = content.innerHTML;
            if(j == 4)
                node.latitude = content.innerHTML;
        }
        array.push(node);
    }

        var map = new BMap.Map("map"); // 创建Map实例
        var point = new BMap.Point(114.367, 30.5322); //地图中心点，广州市
        map.centerAndZoom(point, 13); // 初始化地图,设置中心点坐标和地图级别。
        map.enableScrollWheelZoom(true); //启用滚轮放大缩小
        //向地图中添加缩放控件
        var ctrlNav = new window.BMap.NavigationControl({
            anchor : BMAP_ANCHOR_TOP_LEFT,
            type : BMAP_NAVIGATION_CONTROL_LARGE
        });
        map.addControl(ctrlNav);

        //向地图中添加缩略图控件
        var ctrlOve = new window.BMap.OverviewMapControl({
            anchor : BMAP_ANCHOR_BOTTOM_RIGHT,
            isOpen : 1
        });
        map.addControl(ctrlOve);

        //向地图中添加比例尺控件
        var ctrlSca = new window.BMap.ScaleControl({
            anchor : BMAP_ANCHOR_BOTTOM_LEFT
        });
        map.addControl(ctrlSca);

        var point = new Array(); //存放标注点经纬信息的数组
        var marker = new Array(); //存放标注点对象的数组
        var info = new Array(); //存放提示信息窗口对象的数组

        for (var i = 0; i < array.length; i++) {

            var p0 = array[i].longitude;
            var p1 = array[i].latitude;

            point[i] = new window.BMap.Point(p0, p1); //循环生成新的地图点
            marker[i] = new window.BMap.Marker(point[i]); //按照地图点坐标生成标记
            /* point.push(new window.BMap.Point(p0, p1));
            marker.push(new window.BMap.Marker(point[i])); */
            map.addOverlay(marker[i]);
            /* marker[i].setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画  */
            var label = new window.BMap.Label(array[i].name, {
                offset : new window.BMap.Size(20, -10)
            });
            marker[i].setLabel(label);
            info[i] = new window.BMap.InfoWindow(
                            "<p style=’font-size:12px;lineheight:1.8em;’>"
                            + "</br>节点名：" + array[i].name
                            + "</br> 节点id：" + array[i].node_id 
                            + "</br> 经纬度：" + array[i].longitude + ' , ' + array[i].latitude + "</br></p>"); // 创建信息窗口对象 
        }
        
        marker[0].addEventListener("mouseover", function() {
    		this.openInfoWindow(info[0]);
			});
        marker[0].addEventListener("click", function() {
        	var url = '${pageContext.request.contextPath }/NodeDetailServlet?node_id=' + array[0].node_id + '&node_name=' + array[0].name;
        	window.location.href = url;
			});
        marker[1].addEventListener("mouseover", function() {
    		this.openInfoWindow(info[1]);
			});
        marker[2].addEventListener("mouseover", function() {
    		this.openInfoWindow(info[2]);
			});
        marker[3].addEventListener("mouseover", function() {
    		this.openInfoWindow(info[3]);
			});
    }
    //异步调用百度js
    function map_load() {
        var load = document.createElement("script");
        load.src = "http://api.map.baidu.com/api?v=1.4&callback=map_init";
        document.body.appendChild(load);
    }
    window.onload = map_load;
</script>

</body>
</html>
