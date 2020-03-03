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
                <div class="col-xs-12 col-sm-6 col-md-1">
                    <label class="label-item single-overflow pull-right" title="编码：">编码：</label>
                </div>
                <div class="col-xs-12 col-sm-6 col-md-2">
                    <form:input path="billNo" htmlEscape="false" maxlength="100"  class=" form-control"/>
                </div>

                <div class="col-xs-12 col-sm-6 col-md-1">
                    <label class="label-item single-overflow pull-right" title="客户：">客户：</label>
                </div>
                <div class="col-xs-12 col-sm-6 col-md-2">
                    <form:input path="cusName" htmlEscape="false" maxlength="64"  class=" form-control"/>
                </div>

                <div class="col-xs-12 col-sm-6 col-md-1">
                    <label class="label-item single-overflow pull-right" title="归属员工：">归属员工：</label>
                </div>
                <div class="col-xs-12 col-sm-6 col-md-2">
                    <form:input path="empName" htmlEscape="false" maxlength="64"  class=" form-control"/>
                </div>

                <div class="col-xs-12 col-sm-6 col-md-1">
                    <label class="label-item single-overflow pull-right" title="审核状态：">审核状态：</label>
                </div>
                <div class="col-xs-12 col-sm-6 col-md-2">
                    <form:select path="checkStatus"  class="form-control m-b">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('sobill_checkStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </div>

                <br><br>
                <div class="col-xs-12 col-sm-6 col-md-1">
                    <label class="label-item single-overflow pull-right" title="发货时间：">&nbsp;发货时间：</label>
                </div>
                <div class="col-xs-12 col-sm-6 col-md-4">
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

		 <div class="col-xs-12 col-sm-6 col-md-4">
			<div>
			  <a  id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
			  <a  id="reset" class="btn btn-danger" ><i class="fa fa-refresh"></i> 重置</a>
			 </div>
	    </div>	
	</form:form>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div id="toolbar">
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

			<%--<shiro:hasPermission name="management:sobillandentry:sobill:synchronization">
				<button id="synchronization" class="btn btn-success" onclick="">
					<i class="glyphicon glyphicon-refresh"></i> 同步
				</button>
			</shiro:hasPermission>--%>
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

        <%-- 导航 --%>
        <div class="row">
            <div class="tabs-container" style="margin-left: 0.8%;margin-right: 0.8%;">
                <!-- 导航标签 -->
                <ul class="nav nav-tabs" role="tablist">
                    <li role="presentation" class="active"><a href="#sobillentry" aria-controls="sobillentry" role="tab" data-toggle="tab">订单明细</a></li>
                    <li role="presentation" class=""><a href="#review" aria-controls="review" role="tab" data-toggle="tab">审批明细</a></li>
                </ul>
                <!-- 标签窗格 -->
                <div class="tab-content">
                    <!-- 订单明细 -->
                    <div role="tabpanel" class="tab-pane fade in active" id="sobillentry">
                        <table id="sobillentryListTable" ></table>
                    </div>

                    <!-- 审核明细 -->
                    <div role="tabpanel" class="tab-pane fade in" id="review">
                        <table id="reviewListTable" ></table>
                    </div>
                </div>
            </div>
        </div>
	</div>
	</div>
</body>
</html>