<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>ERP部门列表</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="/webpage/include/treeview.jsp" %>
    <%@include file="erpDeptList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-body">

            <!-- 搜索 -->
            <%--<div id="search-collapse" class="collapse">
                <div class="accordion-inner">
                    <form:form id="searchForm" modelAttribute="customer" class="form form-horizontal well clearfix">
                        <div class="col-xs-12 col-sm-6 col-md-1" style="margin-top:4px">
                            <label class="label-item single-overflow pull-right" title="客户信息：">客户信息：</label>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-2">
                            <form:input path="info" htmlEscape="false" maxlength="100" class=" form-control" placeholder="编码或名称"/>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4">
                            <a id="search" class="btn btn-primary "><i
                                    class="fa fa-search"></i> 查询</a>
                            <a id="reset" class="btn btn-danger "><i
                                    class="fa fa-refresh"></i> 重置</a>
                        </div>
                    </form:form>
                </div>
            </div>--%>

            <!-- 表格 -->
            <input type="hidden" id="deptId" value="${id}">
            <table id="erpDeptTable" ></table>

        </div>
    </div>
</div>
</body>
</html>