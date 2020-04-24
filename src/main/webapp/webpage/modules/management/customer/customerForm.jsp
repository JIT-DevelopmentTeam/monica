<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>客户信息管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
		    $("textarea").css("resize","none");
		});
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/management/customer/customer/save",$('#inputForm').serialize(),function(data){
                    if(data.success){
                        jp.getParent().refresh();
                        var dialogIndex = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                        parent.layer.close(dialogIndex);
                        jp.success(data.msg);
                    }else{
                        jp.error(data.msg);
                    }
                })
			}

        }
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="customer" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>客户编码：</label></td>
					<td class="width-35">
						<form:input path="number" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>客户名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>部门归属：</label>
					</td>
					<td class="width-35">
							<sys:treeselect id="deptId" name="deptId" value="${customer.deptId}" labelName="office.name" labelValue="${customer.office.name}"
											title="部门" url="/sys/office/treeData?type=2" allowClear="true" cssClass="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>员工所属：</label></td>
					<td class="width-35">
						<sys:userselect id="emplId" name="emplId" value="${customer.emplId}" labelName="user.name" labelValue="${customer.user.name}"
										cssClass="form-control" isMultiSelected="false"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">结算币种：</label></td>
					<td class="width-35">
						<form:select path="currencyId" class="form-control ">
							<form:option value="" label="请选择"/>
							<form:options items="${fns:getDictList('sobill_currency')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">状态：</label></td>
					<td class="width-35">
						<form:radiobutton path="status" value="0" class="i-checks "/>停用
						<form:radiobutton path="status" value="1" class="i-checks "/>使用
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td colspan="3">
						<form:textarea path="remarks" htmlEscape="false" rows="4"  class="form-control "/>
					</td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>