<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>订单管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

        var itemIds = [];

        if (${sobill.id != null && sobill.id != ''}) {
            var itemIdsStr = '${itemIdsStr}';
            itemIds = itemIdsStr.split(",");
        }

        $(document).ready(function() {
	        $('#needTime').datetimepicker({
				 format: "YYYY-MM-DD"
		    });
		});

        $.fn.serializeObject = function()
        {
            var o = {};
            var a = this.serializeArray();
            $.each(a, function() {
                if (o[this.name]) {
                    if (!o[this.name].push) {
                        o[this.name] = [o[this.name]];
                    }
                    o[this.name].push(this.value || '');
                } else {
                    o[this.name] = this.value || '';
                }
            });
            return o;
        };

        function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                if (itemIds.length == 0) {
                    jp.error("请至少选择一个商品!");
                    return;
                }
                var headerData = $('#inputForm').serializeObject() ;
                headerData = JSON.stringify(headerData);
                var bodyData = '"sobillentryList":[';
                for (var i = 0; i < itemIds.length; i++) {
                    bodyData += '{"itemId":"'+itemIds[i]+'","batchNo":"'+$("#"+itemIds[i]+"BatchNo").val()+'",' +
                        '"auxqty":"'+$("#"+itemIds[i]+"Auxqty").val()+'","rowId":"'+$("#"+itemIds[i]+"RowId").val()+'",' +
                        '"remarks":"'+$("#"+itemIds[i]+"Remarks").val()+'"' +
                        '},';
                }
                bodyData = bodyData.substring(0,bodyData.length-1);
                bodyData += ']';
                var allData = headerData.slice(0,headerData.length-1)+","+bodyData+headerData.slice(headerData.length-1);
                jp.loading();
                $.ajax({
                    async:false,
                    cache:false,
                    type:'post',
                    url:'${ctx}/management/sobillandentry/sobill/save',
                    contentType: "application/json; charset=utf-8",
                    data:JSON.stringify(allData),
                    processData : false,
                    dataType:'json',
                    success:function (res) {
                        if(res.success){
                            jp.getParent().refresh();
                            var dialogIndex = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                            parent.layer.close(dialogIndex);
                            jp.success(res.msg)

                        }else{
                            jp.error(res.msg);
                        }
                    },
                    error:function () {
                        jp.error("保存出错!");
                    }
                });
			}

        }
		function addItems(){
            jp.open({
                type: 2,
                area: [window.innerWidth * 0.9 + "px", window.innerHeight * 1 + 'px'],//弹框大小
                title:"选择商品",//标题
                content: "${ctx}/management/icitemclass/icitem/list?isSelect=1" ,//Controller地址
                btn: ['确定', '关闭'],
                yes: function(index, layero){//回调函数
                    var body = top.layer.getChildFrame('body', index);
                    var ids = body.find('#ids').val().substring(0,body.find('#ids').val().length-1);//子页面的某个id，通过这个 父页面 可以获取子页面的值，并进行下一步的操作
                    // 需要新增
                    var addIds = [];
                    if (itemIds.length == 0){
                        itemIds = ids.split(",");
                        addIds = ids.split(",");
                    } else {
                        // 选择
                        var newIds = ids.split(",");
                        for (var i = 0; i < newIds.length; i++) {
                            var check = true;
                            for (var j = 0; j < itemIds.length; j++) {
                                if (itemIds[j] == newIds[i]) {
                                    check = false;
                                }
                            }
                            if (check) {
                                itemIds.push(newIds[i]);
                                addIds.push(newIds[i]);
                            }
                        }
                    }
                    if (addIds.length > 0) {
                        $.ajax({
                           async:false,
                           cache:false,
                           url:'${ctx}/management/icitemclass/icitem/getListByIds',
                           type:'post',
                           data:{
                               idsStr:addIds.toString()
                           },
                           dataType:'json',
                           success:function (res) {
                               var icitemList = res.body.icitemList;
                               var templet = '';
                               for (var i = 0; i < icitemList.length; i++) {
                                   templet += '<tr id="'+icitemList[i].id+'Tr">'+
                                                '<td class="hide"></td>'+
                                                '<td>'+icitemList[i].name+'</td>'+
                                                '<td><input type="text" id="'+icitemList[i].id+'BatchNo" class="form-control"/></td>'+
                                                '<td></td>'+
                                                '<td><input type="number" id="'+icitemList[i].id+'Auxqty" value="1.0" min="0" class="form-control required"/></td>'+
                                                '<td></td>'+
                                                '<td><input type="number" id="'+icitemList[i].id+'RowId" min="0" step="1" class="form-control required"/></td>'+
                                                '<td><input type="text" id="'+icitemList[i].id+'Remarks" class="form-control"/></td>'+
                                                '<td class="text-center" width="10"><span class="close" onclick="delRow(\''+icitemList[i].id+'\')" title="删除">&times;</span></td>'
                                           '<tr>';
                               }
                               $("#sobillentryList").append(templet);
                           }
                        });
                    } else {
                        jp.error("请至少选择一个商品!");
                        return;
                    }
                    jp.close(index);//关闭弹框。
                },
                cancel: function(index){
                }
            });
		}

		function delRow(Id) {
            $("#"+Id+"Tr").remove();
            var index = retrieveArrayIndex(Id);
            if(index != -1){
                itemIds.splice(index,1);
            }
        }

        /* 检索数组元素下标 */
        function retrieveArrayIndex(val) {
            for (var i = 0; i < itemIds.length; i++) {
                if (itemIds[i] == val){
                    return i;
                }
            }
            return -1;
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
							<input type='text'  name="needTime" class="form-control required"  value="<fmt:formatDate value="${sobill.needTime}" pattern="yyyy-MM-dd"/>"/>
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
					<td class="width-35" colspan="3">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
        </form:form>
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
						<th width="20%;">批号</th>
						<th><font color="red">*</font>单价</th>
						<th width="10%;"><font color="red">*</font>数量</th>
						<th><font color="red">*</font>总额</th>
						<th width="10%;"><font color="red">*</font>行序号</th>
						<th>备注</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="sobillentryList">
                    <c:forEach items="${sobill.sobillentryList}" varStatus="vs" var="var">
                        <tr id="${var.itemId}Tr">
                            <td class="hide"></td>
                            <td>${var.itemName}</td>
                            <td><input type="text" id="${var.itemId}BatchNo" value="${var.batchNo}" class="form-control"/></td>
                            <td>${var.price}</td>
                            <td><input type="number" id="${var.itemId}Auxqty" value="${var.auxqty}" min="0" class="form-control required"/></td>
                            <td>${var.amount}</td>
                            <td><input type="number" id="${var.itemId}RowId" value="${var.rowId}" min="0" step="1" class="form-control required"/></td>
                            <td><input type="text" id="${var.itemId}Remarks" value="${var.remarks}" class="form-control"/></td>
                            <td class="text-center" width="10">
                                <span class="close" onclick="delRow('${var.itemId}')" title="删除">&times;</span>
                            </td>
                        </tr>
                    </c:forEach>
				</tbody>
			</table>
			</div>
		</div>
		</div>
</body>
</html>