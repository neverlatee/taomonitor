<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resources/css/admin/main.css"/>
  <script type="text/javascript" src="<%=request.getContextPath() %>/resources/js/jquery-1.7.2.min.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath() %>/resources/js/core/jquery.cms.core.js"></script>
  <script type="text/javascript">
    $(function(){
      $("#listTable").trColorChange();
      $(".delete").confirmOperator({msg:"删除后无法恢复，您确定吗？"});
    })
  </script>
</head>
<body>
<div id="content">
  <h3 class="admin_link_bar">
    <jsp:include page="add.jsp"></jsp:include>
  </h3>
  <table width="800" cellspacing="0" cellPadding="0" id="listTable">
    <thead>
    <tr>
      <td>server</td>
      <td>path</td>
      <td>目标节点数</td>
      <td>报警节点差值数</td>
      <td>报警时间</td>
      <td>用户操作</td>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${PathAlarmSettings }" var="setting">
      <tr>
        <td>${setting.address}&nbsp;</td>
        <td>${setting.path}</td>
        <td>${setting.targetNodes}&nbsp;</td>
        <td>${setting.nodeDif}</td>
        <td>${setting.alarmLimits}</td>
        <td>
          <a href="deleteAlarm?address=${setting.address}" title="${user.id }" class="list_op delete">删除</a>
          <a href="update/${user.id }" class="list_op">更新</a>
          <a href="<%=request.getContextPath() %>/admin/channel/userchannels/${user.id }" class="list_op">管理栏目</a>
          &nbsp;
        </td>
      </tr>
    </c:forEach>
    </tbody>
    <tfoot>
    <tr>
      <td colspan="6" style="text-align:right;margin-right:10px;">
        <jsp:include page="/jsp/pager.jsp">
          <jsp:param value="${datas.total }" name="totalRecord"/>
          <jsp:param value="users" name="url"/>
        </jsp:include>
      </td>
    </tr>
    </tfoot>
  </table>
</div>
</body>
</html>