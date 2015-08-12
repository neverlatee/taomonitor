<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Insert title here</title>
</head>
<body>
<h1 >获取节点信息</h1>
<select id="clusterSelector" onchange="javascript:location.href=this.value;" >
	<c:forEach var="zooKeeperCluster" items="${zooKeeperClusterMap}">
		<c:choose>
     			<c:when test="${ zooKeeperCluster.key eq clusterId }"><option value="zooKeeper.do?method=zooKeeperSettingsPAGE&clusterId=${zooKeeperCluster.key}"  selected>${zooKeeperCluster.value.clusterName}</option></c:when>
				<c:otherwise><option value="zooKeeper.do?method=zooKeeperSettingsPAGE&clusterId=${zooKeeperCluster.key}">${zooKeeperCluster.value.clusterName}</option></c:otherwise>
		</c:choose>
	</c:forEach>
</select>
<form name="createNode"  id="createNode"  action="nodeSetting.do?method=creatNode"  method="post">
	<table>
		 <tr >
		 	<td valign="middle">机器选择</td>
		 	<td valign="middle" ><input type="text" name="serverListString"  id="serverListString"  value="${zooKeeperClusterServerList}" size="100"/></td>
		 </tr>
		 <tr >
		 	<td valign="middle">节点Path</td>
		 	<td valign="middle" ><input type="text" name="path" id="path" value="${zooKeeperCluster.description}" size="100"/></td>
		 </tr>
		 <tr >
		 	<td valign="middle">节点内容(可以不填)</td>
		 	<td valign="middle" ><input type="text" name="data" id="data" value="${zooKeeperCluster.description}" size="100"/></td>
		 </tr>
	</table>
</form>
</body>
</html>