<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>订单管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="sobillList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">订单列表</h3>
	</div>
	<div class="panel-body">
    <input type="hidden" id="sobillId" name="sobillId" class="form-control "/>
	
	<!-- 搜索 -->
	<div id="search-collapse" class="collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="sobill" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="编码：">编码：</label>
				<form:input path="billNo" htmlEscape="false" maxlength="100"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="同步状态：">同步状态：</label>
				<form:select path="synStatus"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('syn_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="客户：">客户：</label>
				<form:input path="cusName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="归属员工：">归属员工：</label>
				<form:input path="empName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="发货时间：">&nbsp;发货时间：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginNeedTime' style="left: -10px;" >
					                   <input type='text'  name="beginNeedTime" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endNeedTime' style="left: -10px;" >
					                   <input type='text'  name="endNeedTime" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="是否已经取消：">是否已经取消：</label>
				<form:select path="cancellation"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('sobill_cancellation')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="审核状态：">审核状态：</label>
				<form:select path="checkStatus"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('sobill_checkStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		 <div class="col-xs-12 col-sm-6 col-md-4">
			<div style="margin-top:26px">
			  <a  id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
			  <a  id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
			 </div>
	    </div>	
	</form:form>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div id="toolbar">
			<shiro:hasPermission name="management:sobillandentry:sobill:add">
				<button id="add" class="btn btn-primary" onclick="add()">
					<i class="glyphicon glyphicon-plus"></i> 新建
				</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="management:sobillandentry:sobill:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="management:sobillandentry:sobill:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="management:sobillandentry:sobill:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="management:sobillandentry:sobill:export">
	        		<button id="export" class="btn btn-warning">
					<i class="fa fa-file-excel-o"></i> 导出
				</button>
			 </shiro:hasPermission>

             <shiro:hasPermission name="management:sobillandentry:sobill:view">
				<button id="view" class="btn btn-default" disabled onclick="view()">
					<i class="fa fa-search-plus"></i> 查看
				</button>
			</shiro:hasPermission>

            <shiro:hasPermission name="management:sobillandentry:sobill:check">
                <button id="check" class="btn btn-primary" onclick="checkOrder()">
                    <i class="glyphicon glyphicon-search"></i> 审核
                </button>

				<button id="cancelCheck" class="btn btn-danger" onclick="cancelCheckOrder()">
					<i class="glyphicon glyphicon-remove"></i> 反审核
				</button>
            </shiro:hasPermission>

			<shiro:hasPermission name="management:sobillandentry:sobill:synchronization">
				<button id="synchronization" class="btn btn-success" onclick="">
					<i class="glyphicon glyphicon-refresh"></i> 同步
				</button>
			</shiro:hasPermission>
		    </div>
		
	<!-- 表格 -->
	<table id="sobillTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="management:sobillandentry:sobill:view">
        <li data-item="view"><a>查看</a></li>
        </shiro:hasPermission>
    	<shiro:hasPermission name="management:sobillandentry:sobill:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="management:sobillandentry:sobill:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>

    <%-- 订单明细 --%>
    <div class="row">
        <div class="panel panel-default col-sm-12 col-md-12">
            <div class="panel-heading"  style="background-color: rgba(128,128,128,0.07);">
                <h3 class="panel-title"><label>订单明细</label></h3>
            </div>
        </div>
        <div class="col-sm-12 col-md-11">
            <div class="panel-heading">
            </div>
        </div>
        <div class="col-sm-12 col-md-11">
            <table id="sobillentryListTable" ></table>
        </div>
    </div>
	</div>
	</div>
</body>
</html>