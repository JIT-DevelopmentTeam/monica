<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>库存数据权限管理</title>
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
                jp.post("${ctx}/management/jurisdiction/jurisdiction/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="jurisdiction" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">客户id：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/management/customer/customer/data" id="client" name="client.id" value="${jurisdiction.client.id}" labelName="" labelValue="${jurisdiction.}"
							 title="选择客户id" cssClass="form-control " fieldLabels="客户名称" fieldKeys="name" searchLabels="客户名称" searchKeys="name" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">仓库id：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/management/warehouse/warehouse/data" id="warehouse" name="warehouse.id" value="${jurisdiction.warehouse.id}" labelName="" labelValue="${jurisdiction.}"
							 title="选择仓库id" cssClass="form-control " fieldLabels="仓库名称" fieldKeys="name" searchLabels="仓库名称" searchKeys="name" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品id：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/management/icitemclass/icitem/data" id="item" name="item.id" value="${jurisdiction.item.id}" labelName="" labelValue="${jurisdiction.}"
							 title="选择商品id" cssClass="form-control " fieldLabels="商品名称" fieldKeys="name" searchLabels="商品名称" searchKeys="name" ></sys:gridselect>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>