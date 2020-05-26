<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>微信用户管理</title>
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
                jp.post("${ctx}/management/wxuser/wxUser/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="wxUser" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">昵称：</label></td>
					<td class="width-35">
						<form:input path="nickName" htmlEscape="false"    class="form-control " readonly="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">性别：</label></td>
					<td class="width-35">
						<form:radiobuttons path="sex" items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks " disabled="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">国家：</label></td>
					<td class="width-35">
						<form:input path="country" htmlEscape="false"    class="form-control " readonly="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">省份：</label></td>
					<td class="width-35">
						<form:input path="province" htmlEscape="false"    class="form-control " readonly="true"/>
					</td>

				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">城市：</label></td>
					<td class="width-35">
						<form:input path="city" htmlEscape="false"    class="form-control " readonly="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">所属客户：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/management/customer/customer/data" id="client" name="client.id" value="${wxUser.client.id}" labelName="client.name" labelValue="${wxUser.client.name}"
										title="选择所属客户" cssClass="form-control " fieldLabels="客户名称" fieldKeys="name" searchLabels="客户名称" searchKeys="name" ></sys:gridselect>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>