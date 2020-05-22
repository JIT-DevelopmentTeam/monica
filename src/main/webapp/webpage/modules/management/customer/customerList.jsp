<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>客户信息管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="/webpage/include/treeview.jsp" %>
    <%@include file="customerList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">客户信息列表</h3>
        </div>
        <div class="panel-body">

            <!-- 搜索 -->
            <div id="search-collapse" class="collapse">
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
            </div>

            <!-- 工具栏 -->
            <div id="toolbar">
                <%--<shiro:hasPermission name="management:customer:customer:add">--%>
                    <%--<button id="add" class="btn btn-primary" onclick="add()">--%>
                        <%--<i class="glyphicon glyphicon-plus"></i> 新建--%>
                    <%--</button>--%>
                <%--</shiro:hasPermission>--%>
                <shiro:hasPermission name="management:customer:customer:edit">
                    <button id="edit" class="btn btn-success" disabled onclick="edit()">
                        <i class="glyphicon glyphicon-edit"></i> 修改
                    </button>
                </shiro:hasPermission>
                <%--<shiro:hasPermission name="management:customer:customer:del">--%>
                    <%--<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">--%>
                        <%--<i class="glyphicon glyphicon-remove"></i> 删除--%>
                    <%--</button>--%>
                <%--</shiro:hasPermission>--%>
                <%--<shiro:hasPermission name="management:customer:customer:import">
                    <button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="management:customer:customer:export">
                    <button id="export" class="btn btn-warning">
                        <i class="fa fa-file-excel-o"></i> 导出
                    </button>
                </shiro:hasPermission>--%>
                <shiro:hasPermission name="management:customer:customer:view">
                    <button id="view" class="btn btn-default" disabled onclick="view()">
                        <i class="fa fa-search-plus"></i> 查看
                    </button>
                </shiro:hasPermission>
                <shiro:hasPermission name="management:customer:customer:synch">
                    <a id="sync" class="btn btn-success"
                       onclick="synch()"><i
                            class="glyphicon glyphicon-refresh"></i> 同步</a>
                </shiro:hasPermission>
            </div>

            <!-- 表格 -->
            <table id="customerTable" data-toolbar="#toolbar"></table>

            <!-- context menu -->
            <ul id="context-menu" class="dropdown-menu">
                <shiro:hasPermission name="management:customer:customer:view">
                    <li data-item="view"><a>查看</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="management:customer:customer:edit">
                    <li data-item="edit"><a>编辑</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="management:customer:customer:del">
                    <li data-item="delete"><a>删除</a></li>
                </shiro:hasPermission>
                <li data-item="action1"><a>取消</a></li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>