<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>库存管理管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {

		});

		function save() {
	            var isValidate = jp.validateForm('#inputForm');//校验表单
	            if(!isValidate){
	                return false;
		    }else{
	                jp.loading();
	                jp.post("${ctx}/management/warehouse/warehouse/save",$('#inputForm').serialize(),function(data){
	                    if(data.success){
	                        jp.getParent().refreshTree();
	                        var dialogIndex = parent.layer.getFrameIndex(window.name); // 获取窗口索引
	                        parent.layer.close(dialogIndex);
	                        jp.success(data.msg)

	                    }else{
	                        jp.error(data.msg);
	                    }
	                })
		  }

	        }
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="warehouse" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>erpid：</label></td>
					<td class="width-35">
						<form:input path="erpid" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>编码：</label></td>
					<td class="width-35">
						<form:input path="number" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>仓库名称：</label></td>
					<td class="width-35">
						<form:input path="warehouseName" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">同步时间戳：</label></td>
					<td class="width-35">
						<form:input path="modifytime" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>状态：</label></td>
					<td class="width-35">
						<form:input path="status" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">父级编号：</label></td>
					<td class="width-35">
						<sys:treeselect id="parent" name="parent.id" value="${warehouse.parent.id}" labelName="parent.name" labelValue="${warehouse.parent.name}"
						title="父级编号" url="/management/warehouse/warehouse/treeData" extId="${warehouse.id}" cssClass="form-control " allowClear="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>排序：</label></td>
					<td class="width-35">
						<form:input path="sort" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
		</form:form>
</body>
</html>