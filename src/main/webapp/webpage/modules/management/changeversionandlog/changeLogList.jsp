<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>ERP用户</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="changeLogList.js" %>
</head>
<body>
<input type="hidden" id="changeVersionId" value="${changeVersionId}"/>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-body">
            <!-- 表格 -->
            <table id="table" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>
