<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>商品分类管理管理</title>
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
	                jp.post("${ctx}/management/icitemclass/icitemClass/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="icitemClass" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">erp分类id：</label></td>
					<td class="width-35">
						<form:input path="erpId" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>编码：</label></td>
					<td class="width-35">
						<form:input path="number" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">erp备注：</label></td>
					<td class="width-35">
						<form:input path="erpNote" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">父级编号：</label></td>
					<td class="width-35">
						<sys:treeselect id="parent" name="parent.id" value="${icitemClass.parent.id}" labelName="parent.name" labelValue="${icitemClass.parent.name}"
						title="父级编号" url="/management/icitemclass/icitemClass/treeData" extId="${icitemClass.id}" cssClass="form-control " allowClear="true"/>
					</td>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>排序：</label></td>
					<td class="width-35">
						<form:input path="sort" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
		 	</tbody>
		</table>
		</form:form>
</body>
</html>