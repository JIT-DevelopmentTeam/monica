<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>商品资料管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>

	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="icitemClassTreeList.js" %>
    <%@include file="icitemList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">商品资料列表</h3>
	</div>
	<div class="panel-body">
        <input type="hidden" id="ids"/>
		<div class="row">
				<div class="col-sm-4 col-md-2" >
					<div class="form-group">
                        <c:if test="${icitem.isSelect == null}">
                            <div class="row">
                                <div class="col-sm-6" >
                                    <div class="input-search">
                                        <button type="submit" class="input-search-btn">
                                            <i class="fa fa-search" aria-hidden="true"></i></button>
                                        <input   id="search_q" type="text" class="form-control input-sm" name="" placeholder="查找...">

                                    </div>
                                </div>
                                <div class="col-sm-4" >
                                    <button  class="btn btn-default btn-sm"  onclick="synIcitemClass()">
                                        <i class="glyphicon glyphicon-refresh">同步</i>
                                    </button>
                                </div>
                                <%--<div class="col-sm-2" >
                                    <button  class="btn btn-default btn-sm"  onclick="jp.openSaveDialog('新建商品分类', '${ctx}/management/icitemclass/icitemClass/form','800px', '500px')">
                                        <i class="fa fa-plus"></i>
                                    </button>
                                </div>--%>
                            </div>
                        </c:if>
					</div>
					<div id="icitemClassjsTree" style="overflow-x:auto; border:0px; overflow-y: auto; height: 600px;"></div>
				</div>
				<div  class="col-sm-8 col-md-10">
	
	<!-- 搜索 -->
	<div id="search-collapse" class="collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="icitem" class="form form-horizontal well clearfix">
                <div class="col-xs-12 col-sm-6 col-md-1" style="margin-top:4px">
                    <label class="label-item single-overflow pull-right" title="商品信息：">商品信息：</label>
                </div>
                <div class="col-xs-12 col-sm-6 col-md-2">
                    <form:input path="info" htmlEscape="false" maxlength="100" class=" form-control" placeholder="编号、名称或型号"/>
                </div>
		 <div class="col-xs-12 col-sm-6 col-md-4">
			  <a  id="search" class="btn btn-primary "><i class="fa fa-search"></i> 查询</a>
			  <a  id="reset" class="btn btn-danger " ><i class="fa fa-refresh"></i> 重置</a>
	    </div>
	</form:form>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div id="toolbar">
            <c:if test="${icitem.isSelect == null}">
                <%--<shiro:hasPermission name="management:icitemclass:icitem:add">--%>
                    <%--<button id="add" class="btn btn-primary" onclick="add()">--%>
                        <%--<i class="glyphicon glyphicon-plus"></i> 新建--%>
                    <%--</button>--%>
                <%--</shiro:hasPermission>--%>
                <shiro:hasPermission name="management:icitemclass:icitem:edit">
                    <button id="edit" class="btn btn-success" disabled onclick="edit()">
                        <i class="glyphicon glyphicon-edit"></i> 修改
                    </button>
                </shiro:hasPermission>
                <shiro:hasPermission name="management:icitemclass:icitem:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
                        <i class="glyphicon glyphicon-remove"></i> 删除
                    </button>
                </shiro:hasPermission>
                <%--<shiro:hasPermission name="management:icitemclass:icitem:import">
                    <button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="management:icitemclass:icitem:export">
                    <button id="export" class="btn btn-warning">
                        <i class="fa fa-file-excel-o"></i> 导出
                    </button>
                </shiro:hasPermission>--%>
                <shiro:hasPermission name="management:icitemclass:icitem:view">
                    <button id="view" class="btn btn-default" disabled onclick="view()">
                        <i class="fa fa-search-plus"></i> 查看
                    </button>
                </shiro:hasPermission>
                <button id="uploadItemId"  class="btn btn-primary"  onclick="icItemUpload()">
                    <i class="fa fa-picture-o" aria-hidden="true"></i> 上传商品图片</i>
                </button>
            </c:if>
		    </div>

	<!-- 表格 -->
	<table id="icitemTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="management:icitemclass:icitem:view">
        <li data-item="view"><a>查看</a></li>
        </shiro:hasPermission>
    	<shiro:hasPermission name="management:icitemclass:icitem:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="management:icitemclass:icitem:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
	</div>
</div>
</body>
</html>