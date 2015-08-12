<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Insert title here</title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-2.0.3.min.js"></script>
	<script type="text/javascript">
		$(function(){

			var flag = $("#flag").val();
			switch(flag){
				case "addNode": showAddNode();break;
				case "deleteNode": showDeleteNode(); break;
				case "showNode": showNode(); break;
			}
			function showAddNode(){
				$("#showNode").hide();
				$("#addNode").show();
				$("#deleteNode").hide();
			}
			function showDeleteNode(){
				$("#showNode").hide();
				$("#addNode").hide();
				$("#deleteNode").show();
			}
			function showNode(){
				$("#showNode").show();
				$("#addNode").hide();
				$("#deleteNode").hide();
			}

//
//			$("#showNode").click(function(){
//				$("#showNode").show();
//				$("#addNode").hide();
//				$("#deleteNode").hide();
//			});
//
//			$("#deleteNode").click(function(){
//				$("#showNode").hide();
//				$("#addNode").hide();
//				$("#deleteNode").show();
//			});

		})
	</script>
</head>
<body>
<select id="clusterSelector" onchange="javascript:location.href=this.value;" >
	<c:forEach var="zooKeeperCluster" items="${zooKeeperClusterMap}">
		<c:choose>
			<c:when test="${ zooKeeperCluster.key eq clusterId }"><option value="zooKeeper.do?method=zooKeeperSettingsPAGE&clusterId=${zooKeeperCluster.key}"  selected>${zooKeeperCluster.value.clusterName}</option></c:when>
			<c:otherwise><option value="zooKeeper.do?method=zooKeeperSettingsPAGE&clusterId=${zooKeeperCluster.key}">${zooKeeperCluster.value.clusterName}</option></c:otherwise>
		</c:choose>
	</c:forEach>
</select>
	<h1>ZooKeeper ½Úµã¹¦ÄÜÑ¡Ôñ</h1>
	
	<li><a href="<c:url value="/nodeSetting.do/getCluster?flag=addNode" />">´´½¨½Úµã</a></li>
	<li><a href="<c:url value="/nodeSetting.do/getCluster?flag=deleteNode" />">É¾³ý½Úµã</a></li>
	<li><a href="<c:url value="/nodeSetting.do/getCluster?flag=showNode" />">²é¿´½Úµã</a></li>
<input id="flag" type="hidden" value="${flag}"/>


<div id="showNode" style="display: none">
	<form action="/nodeSetting.do/method=createNodePAGE" method="post"> 
		<select id="clusterSelector2" name="zkAddress">
			<c:forEach var="zooKeeperCluster" items="${zooKeeperClusterMap}">
				<option value="${zooKeeperCluster.key}">${zooKeeperCluster.value.clusterName}</option>
			</c:forEach>
		</select>
		<p>选择节点名:<input type="text" name="path" /></p>
		<input type=submit value="ok"/>
	</form>
</div>
<div id="addNode" style="display: inline">
	<form action="/nodeSetting.do/method=createNodePAGE" method="post"> 
		<select id="clusterSelector3" name="zkAddress">
			<c:forEach var="zooKeeperCluster" items="${zooKeeperClusterMap}">
				<option value="${zooKeeperCluster.key}">${zooKeeperCluster.value.clusterName}</option>
			</c:forEach>
		</select>
		<p>创建节点名:<input type="text" name="path" /></p>
		<p>创建内容(可以不填):<input type="text" name="data" /></p>
		<input type=submit value="ok"/>
	</form>>
</div>
<div id="deleteNode" style="display: none">
	<form action="/nodeSetting.do/method=deleteNodePAGE" method="post"> 
		<select id="clusterSelector4" name="zkAddress" >
			<c:forEach var="zooKeeperCluster" items="${zooKeeperClusterMap}">
				<option value="${zooKeeperCluster.key}">${zooKeeperCluster.value.clusterName}</option>
			</c:forEach>
		</select>
		<p>删除节点名:<input type="text" name="path" /></p>
		<input type=submit value="ok"/>
	</form>
</div>

</body>
</html>