<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>库存查询管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="itemClassTreeList.js" %>
	<%@include file="stockList.js" %>
	
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">库存查询列表</h3>
	</div>
	<div class="panel-body">
		<div class="row">
				<div class="col-sm-4 col-md-2" >
					<div class="form-group">
						<div class="row">
							<div class="col-sm-12" >
								<div class="input-search">
									<button type="submit" class="input-search-btn">
										<i class="fa fa-search" aria-hidden="true"></i></button>
									<input   id="search_q" type="text" class="form-control input-sm" name="" placeholder="查找...">

								</div>
							</div>
							<%--<div class="col-sm-4" >
								<button  class="btn btn-default btn-sm"  onclick="synWareHouse()">
									<i class="glyphicon glyphicon-refresh">同步仓库</i>
								</button>
							</div>--%>
							<%--<div class="col-sm-2" >
								<button  class="btn btn-default btn-sm"  onclick="jp.openSaveDialog('新建仓库管理表', '${ctx}/management/warehouse/warehouse/form','800px', '500px')">
									<i class="fa fa-plus"></i>
								</button>
							</div>--%>
						</div>
					</div>
					<div id="icitemClassjsTree" style="overflow-x:auto; border:0px; overflow-y: auto; height: 650px;"></div>
				</div>
				<div  class="col-sm-8 col-md-10">
	
	<!-- 搜索 -->
	<div id="search-collapse" class="collapse" style="display: block;">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="stock" class="form form-horizontal well clearfix">
				<input type="hidden" id="itemClassNumber" name="itemClassNumber">
				<div class="col-xs-12 col-sm-6 col-md-1" style="margin-top:4px">
					<label class="label-item single-overflow pull-right" title="产品信息：">产品信息：</label>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-2">
					<input id="item" name="item" htmlEscape="false" maxlength="64"  class=" form-control" placeholder="名称或规格型号">
				</div>
				<div class="col-xs-12 col-sm-6 col-md-1" style="margin-top:4px">
					<label class="label-item single-overflow pull-right" title="批号：">批号：</label>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-2">
					<form:input path="batchNumber" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-1" style="margin-top:4px">
					<label class="label-item single-overflow pull-right" title="等级：">等级：</label>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-1">
					<form:input path="level" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-1" style="margin-top:4px">
					<label class="label-item single-overflow pull-right" title="色号：">色号：</label>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-1">
					<form:input path="colorNumber" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-1" style="margin-top:4px">
					<label class="label-item single-overflow pull-right" title="仓库：">仓库：</label>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-1">
					<form:input path="warehouse" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
		 <div class="col-xs-12 col-sm-6 col-md-3">
			<div style="margin-top:15px">
			  <a  id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
			  <a  id="reset" class="btn btn-danger" ><i class="fa fa-refresh"></i> 重置</a>
			 </div>
	    </div>
	</form:form>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div id="toolbar">
			<shiro:hasPermission name="management:warehouse:stock:export">
	        		<button id="export" class="btn btn-warning">
					<i class="fa fa-file-excel-o"></i> 导出
				</button>
			 </shiro:hasPermission>
	</div>
		
	<!-- 表格 -->
	<table id="stockTable"   data-toolbar="#toolbar"></table>

	</div>
	</div>
	</div>
	</div>
</div>
</body>
</html>