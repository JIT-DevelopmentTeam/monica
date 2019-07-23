<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>订单管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
	        $('#needTime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});

		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/management/sobillandentry/sobill/save",$('#inputForm').serialize(),function(data){
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
		function addItems(){
            jp.openDialog("选择商品", "${ctx}/management/icitemclass/icitem/list", window.innerWidth * 0.9 + "px", window.innerHeight * 0.8 + 'px', null ,function (iframeWin) {
            });
		}
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="sobill" action="${ctx}/management/sobillandentry/sobill/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="emplId" value="${sobill.emplId}"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>类型：</label></td>
					<td class="width-35">
						<form:select path="type" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('sobill_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>

					<td class="width-15 active"><label class="pull-right">状态：</label></td>
					<td class="width-35">
						<input type="text" readonly class="form-control" value="<c:if test="${sobill.status == 0 || sobill.status == null}">草稿</c:if><c:if test="${sobill.status == 1}">审核提交</c:if>"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>编码：</label></td>
					<td class="width-35">
						<form:input path="billNo" htmlEscape="false"  readonly="true"  class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>客户：</label></td>
					<td class="width-35">
						<sys:gridselect id="customer" title="选择客户" url="${ctx}/management/customer/customer/data"
						cssClass="form-control required" fieldKeys="number|name" fieldLabels="编号|名称" labelName="customer.name"
						labelValue="${sobill.cusName}"  name="custId" searchKeys="number|name" searchLabels="编号|名称" value="${sobill.custId}"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">同步状态：</label></td>
					<td class="width-35">
						<input type="text" readonly class="form-control" value="<c:if test="${sobill.synStatus == 0 || sobill.synStatus == null}">未同步</c:if><c:if test="${sobill.synStatus == 1}">已同步</c:if>"/>
					</td>
					<td class="width-15 active"><label class="pull-right">同步时间：</label></td>
					<td class="width-35">
						<form:input path="synTime" htmlEscape="false"  readonly="true"  class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">归属部门：</label></td>
					<td class="width-35">
                        <form:input path="deptName" htmlEscape="false" readonly="true" class="form-control"/>
					</td>
					<td class="width-15 active"><label class="pull-right">归属员工：</label></td>
					<td class="width-35">
                        <form:input path="empName" htmlEscape="false" readonly="true" class="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>币别：</label></td>
					<td class="width-35">
                        <form:select path="currencyId" class="form-control required">
                            <form:option value="" label=""/>
                            <form:options items="${fns:getDictList('sobill_currency')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                        </form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>发货时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='needTime'>
							<input type='text'  name="needTime" class="form-control required"  value="<fmt:formatDate value="${sobill.needTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">审核人：</label></td>
					<td class="width-35">
						<form:input path="checkerName" htmlEscape="false"  readonly="true" class="form-control"/>
					</td>
					<td class="width-15 active"><label class="pull-right">审核状态：</label></td>
					<td class="width-35">
						<input type="text" value="<c:if test="${sobill.checkStatus == 0 || sobill.checkStatus == null}">待审核</c:if><c:if test="${sobill.checkStatus == 1}">已审核</c:if>" readonly class="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">审核时间：</label></td>
					<td class="width-35">
						<input type="text" readonly value="<fmt:formatDate value="${sobill.checkTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" class="form-control"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>总金额：</label></td>
					<td class="width-35">
						<form:input path="amount" htmlEscape="false" class="form-control required isFloatGteZero"/>
					</td>
				</tr>

		   		<tr>
					<td class="width-15 active"><label class="pull-right">备注：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">订单明细</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addItems();" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>商品</th>
						<th>批号</th>
						<th><font color="red">*</font>单价</th>
						<th width="10%;"><font color="red">*</font>数量</th>
						<th><font color="red">*</font>总额</th>
						<th width="10%;"><font color="red">*</font>行序号</th>
						<th>备注</th>
						<th width="10">&nbsp;</th>
					</tr>
                    <c:forEach items="${sobill.sobillentryList}" varStatus="vs" var="var">
                        <tr>
                            <td class="hide"></td>
                            <td>${var.itemName}</td>
                            <td>${var.batchNo}</td>
                            <td>${var.price}</td>
                            <td><input type="number" id="${var.id}Qty" value="${var.auxqty}" min="0" class="form-control required"/></td>
                            <td>${var.amount}</td>
                            <td><input type="number" id="${var.id}Row" value="${var.rowId}" min="0" step="1" class="form-control required"/></td>
                            <td>${var.remarks}</td>
                            <td class="text-center" width="10">
                                <span class="close" onclick="delRow()" title="删除">&times;</span>
                            </td>
                        </tr>
                    </c:forEach>
				</thead>
				<tbody id="sobillentryList">
				</tbody>
			</table>
			</div>
		</div>
		</div>
		</form:form>
</body>
</html>