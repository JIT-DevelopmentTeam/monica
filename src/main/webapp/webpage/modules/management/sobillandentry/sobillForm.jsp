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
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
			$(list+idx).find(".form_datetime").each(function(){
				 $(this).datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
			    });
			});
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="sobill" action="${ctx}/management/sobillandentry/sobill/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="emplId" value="${fns:getUser().id}"/>
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
						<form:input path="billNo" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>客户：</label></td>
					<td class="width-35">
						<sys:treeselect id="custId" name="custId.id" value="${role.office.id}" labelName="custId.name" labelValue="${role.office.name}"
										title="客户" url="/sys/office/treeData" allowClear="true" cssClass="form-control required"/>
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
                        <form:input path="emplName" htmlEscape="false" readonly="true" class="form-control"/>
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
			<a class="btn btn-white btn-sm" onclick="addRow('#sobillentryList', sobillentryRowIdx, sobillentryTpl);sobillentryRowIdx = sobillentryRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>商品</th>
						<th><font color="red">*</font>商品单位</th>
						<th>批号</th>
						<th><font color="red">*</font>单价</th>
						<th><font color="red">*</font>数量</th>
						<th><font color="red">*</font>总额</th>
						<th><font color="red">*</font>行序号</th>
						<th>备注</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="sobillentryList">
				</tbody>
			</table>
			<script type="text/template" id="sobillentryTpl">//<!--
				<tr id="sobillentryList{{idx}}">
					<td class="hide">
						<input id="sobillentryList{{idx}}_id" name="sobillentryList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="sobillentryList{{idx}}_delFlag" name="sobillentryList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td>
						<sys:treeselect id="custId" name="custId.id" value="${role.office.id}" labelName="custId.name" labelValue="${role.office.name}"
											title="客户" url="/sys/office/treeData" allowClear="true" cssClass="form-control required"/>
					</td>
					
					
					<td>
						<input id="sobillentryList{{idx}}_unit" name="sobillentryList[{{idx}}].unit" type="text" value="{{row.unit}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="sobillentryList{{idx}}_batchNo" name="sobillentryList[{{idx}}].batchNo" type="text" value="{{row.batchNo}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="sobillentryList{{idx}}_price" name="sobillentryList[{{idx}}].price" type="text" value="{{row.price}}" onchange="countItemsAmount('sobillentryList{{idx}}');"  class="form-control required isFloatGteZero"/>
					</td>
					
					
					<td>
						<input id="sobillentryList{{idx}}_auxqty" name="sobillentryList[{{idx}}].auxqty" type="text" value="{{row.auxqty}}" onchange="countItemsAmount('sobillentryList{{idx}}');" class="form-control required isFloatGteZero"/>
					</td>
					
					
					<td>
						<input id="sobillentryList{{idx}}_amount" name="sobillentryList[{{idx}}].amount" type="text" value="{{row.amount}}"    class="form-control required isFloatGteZero"/>
					</td>
					
					
					<td>
						<input id="sobillentryList{{idx}}_rowId" name="sobillentryList[{{idx}}].rowId" type="text" value="{{row.rowId}}"    class="form-control required isIntGteZero"/>
					</td>

					<td>
						<input id="sobillentryList{{idx}}_remarks" name="sobillentryList[{{idx}}].remarks" rows="4" value="{{row.remarks}}" class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#sobillentryList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>

				</tr>//-->
			</script>
			<script type="text/javascript">
				var sobillentryRowIdx = 0, sobillentryTpl = $("#sobillentryTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(sobill.sobillentryList)};
					for (var i=0; i<data.length; i++){
						addRow('#sobillentryList', sobillentryRowIdx, sobillentryTpl, data[i]);
						sobillentryRowIdx = sobillentryRowIdx + 1;
					}
				});

                function countItemsAmount(Id) {
                    var price = ($("#"+Id+"_price").val() != null && $("#"+Id+"_price").val() != '' ? parseFloat($("#"+Id+"_price").val()) : 0);
                    var auxqty = ($("#"+Id+"_auxqty").val() != null && $("#"+Id+"_auxqty").val() != '' ? parseFloat($("#"+Id+"_auxqty").val()) : 0);
                    $("#"+Id+"_amount").val((price * auxqty).toFixed(2));
                }
			</script>
			</div>
		</div>
		</div>
		</form:form>
</body>
</html>