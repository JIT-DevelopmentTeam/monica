<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>管理管理</title>
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
                jp.post("${ctx}/management/apiurl/apiUrl/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="apiUrl" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</td>
                    <td class="width-15 active"><label class="pull-right"><font color="red">*</font>用处：</label></td>
                    <td class="width-35">
                        <form:select path="usefulness" class="form-control required">
                            <form:option value="" label=""/>
                            <form:option value="1" label="物料分类同步"/>
                            <form:option value="2" label="物料信息同步"/>
                            <form:option value="3" label="客户资料同步"/>
                            <form:option value="4" label="订单信息同步"/>
                            <form:option value="5" label="库存列表查询"/>
                            <form:option value="6" label="库存列表总量查询"/>
                            <form:option value="7" label="库存详情查询"/>
                        </form:select>
                    </td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">域名：</label></td>
					<td class="width-35">
						<form:input path="domain" htmlEscape="false"    class="form-control"/>
					</td>
					<td class="width-15 active"><label class="pull-right">端口：</label></td>
					<td class="width-35">
						<form:input path="port" htmlEscape="false"    class="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>url：</label></td>
					<td class="width-35">
						<form:input path="url" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>端口协议：</label></td>
					<td class="width-35">
						<form:input path="protocol" htmlEscape="false"  class="form-control required"/>
					</td>
				</tr>
				<tr>
                    <td class="width-15 active"><label class="pull-right">端口编码：</label></td>
                    <td class="width-35">
                        <form:input path="number" htmlEscape="false"    class="form-control"/>
                    </td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>状态：</label></td>
					<td class="width-35">
                        <form:radiobuttons path="status" items="${fns:getDictList('apiurl_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否需要token：</label></td>
					<td class="width-35">
						<form:radiobuttons path="isToken" items="${fns:getDictList('apiurl_need_token')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
					</td>
                    <td class="width-15 active"><label class="pull-right">描述：</label></td>
                    <td class="width-35">
                        <form:input path="describe" htmlEscape="false"    class="form-control "/>
                    </td>
				</tr>
                <tr>
                    <td class="width-15 active"><label class="pull-right">备注信息：</label></td>
                    <td class="width-35" colspan="3">
                        <form:textarea path="remarks" htmlEscape="false" rows="4" cssStyle="resize: none;"   class="form-control "/>
                    </td>
                </tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>