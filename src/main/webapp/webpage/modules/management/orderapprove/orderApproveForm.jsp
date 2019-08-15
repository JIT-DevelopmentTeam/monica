<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>订单流程管理</title>
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
                jp.post("${ctx}/management/orderapprove/orderApprove/save",$('#inputForm').serialize(),function(data){
                    if(data.success){
                        jp.getParent().refresh();
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
		<form:form id="inputForm" modelAttribute="orderApprove" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>流程类型：</label></td>
					<td class="width-35">
						<form:select path="type" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('order_Approve_Type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
                    <td class="width-15 active"><label class="pull-right"><font color="red">*</font>节点名称：</label></td>
                    <td class="width-35">
                        <form:input path="name" htmlEscape="false"  class="form-control required"/>
                    </td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>流程节点用户：</label></td>
					<td class="width-35">
						<sys:userselect id="approvalEmplId" name="approvalEmplId.id" value="${orderApprove.approvalEmplId.id}" labelName="approvalEmplId.name" labelValue="${orderApprove.approvalEmplId.name}"
							    cssClass="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>排序：</label></td>
					<td class="width-35">
						<form:input path="index" htmlEscape="false"  class="form-control required isIntGtZero"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">审批状态：</label></td>
					<td class="width-35">
						<form:radiobuttons path="status" items="${fns:getDictList('order_Approve_Status')}" disabled="true" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks "/>
					</td>
                    <td class="width-15 active"><label class="pull-right">审核时间：</label></td>
                    <td class="width-35">
                        <form:input path="date" htmlEscape="false" readonly="true"   class="form-control "/>
                    </td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">是否进入待审核状态：</label></td>
					<td class="width-35">
						<form:radiobuttons path="isToapp" items="${fns:getDictList('order_Approve_IsToapp')}" disabled="true" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks "/>
					</td>
					<td class="width-15 active"><label class="pull-right">是否为最后节点：</label></td>
					<td class="width-35">
						<form:radiobuttons path="isLast" items="${fns:getDictList('order_Approve_IsLast')}" disabled="true" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks "/>
					</td>
				</tr>
                <tr>
                    <td class="width-15 active"><label class="pull-right">审核评议：</label></td>
                    <td class="width-35" colspan="3">
                        <form:textarea path="remark" htmlEscape="false" rows="4"  cssStyle="resize: none;"  class="form-control "/>
                    </td>
                </tr>
                <tr>
                    <td class="width-15 active"><label class="pull-right">备注信息：</label></td>
                    <td class="width-35" colspan="3">
                        <form:textarea path="remarks" htmlEscape="false" rows="4"  cssStyle="resize: none;"  class="form-control "/>
                    </td>
                </tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>