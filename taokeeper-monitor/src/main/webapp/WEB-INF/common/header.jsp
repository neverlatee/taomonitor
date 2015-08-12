<%@ page language="java" pageEncoding="GBK"%><%@ include
	file="/WEB-INF/common/taglibs.jsp"%>
<div id="navcolumn" style="height: 100px;">
	<ul>

		<li>Monitor
			<ul>
				<li><a
					href="<c:url value="/zooKeeper.do?method=zooKeeperSettingsPAGE" />">¼¯ÈºÅäÖÃ</a></li>
				<li><a
					href="<c:url value="/zooKeeperStatus.do?method=showZooKeeperStatusPAGE" />">¼¯Èº¼à¿Ø</a></li>
				<!-- »úÆ÷¼à¿ØÏÈÔÝÊ±²»ÓÃÁË
				<li><a
					href="<c:url value="/hostPerformance.do?method=showHostPerformancePAGE" />">»úÆ÷¼à¿Ø</a></li>
				 -->
				<li><a
					href="<c:url value="/alarmSettings.do?method=alarmSettingsPAGE" />">±¨¾¯ÉèÖÃ</a></li>
				<li><a href="<c:url value="/nodeSetting.do/getCluster?flag=addNode" />">节点配置</a></li>
			</ul>
		</li>

		<br>
		<li>Admin
			<ul>
				<!-- ±¨¾¯¿ª¹Ø·Ö¼¯Èº
				<li><a
					href="<c:url value="admin.do?method=switchOfNeedAlarmPAGE" />">±¨¾¯¿ª¹Ø</a></li>
					 -->
				<li><a
					href="<c:url value="admin.do?method=setSystemConfigPAGE" />">ÏµÍ³ÉèÖÃ</a></li>
			</ul>
		</li>

		<br>
		<!-- 
	<li>Reports
		<ul>
			<li><a href="">ÈÕ±¨</a></li>
			<li><a href="">ÖÜ±¨</a></li>
			<li><a href="">Ç÷ÊÆ</a></li>
		</ul>
	</li>
-->




	</ul>
</div>
