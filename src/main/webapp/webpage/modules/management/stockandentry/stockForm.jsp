<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>库存查询管理</title>
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
                jp.post("${ctx}/management/stockandentry/stock/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="stock" action="${ctx}/management/stockandentry/stock/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">仓库id：</label></td>
					<td class="width-35">
						<form:input path="warehouseId" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">仓位：</label></td>
					<td class="width-35">
						<form:input path="warehousePosition" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品代码：</label></td>
					<td class="width-35">
						<form:input path="commodityNumber" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">商品名称：</label></td>
					<td class="width-35">
						<form:input path="commodityName" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">规格型号：</label></td>
					<td class="width-35">
						<form:input path="specification" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">单位：</label></td>
					<td class="width-35">
						<form:input path="unit" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">库存总数：</label></td>
					<td class="width-35">
						<form:input path="total" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">子库存查询表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#stockentryList', stockentryRowIdx, stockentryTpl);stockentryRowIdx = stockentryRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>批号</th>
						<th>等级</th>
						<th>色号</th>
						<th>库存数量</th>
						<th>备注信息</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="stockentryList">
				</tbody>
			</table>
			<script type="text/template" id="stockentryTpl">//<!--
				<tr id="stockentryList{{idx}}">
					<td class="hide">
						<input id="stockentryList{{idx}}_id" name="stockentryList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="stockentryList{{idx}}_delFlag" name="stockentryList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="stockentryList{{idx}}_batchNumber" name="stockentryList[{{idx}}].batchNumber" type="text" value="{{row.batchNumber}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="stockentryList{{idx}}_level" name="stockentryList[{{idx}}].level" type="text" value="{{row.level}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="stockentryList{{idx}}_colorNumber" name="stockentryList[{{idx}}].colorNumber" type="text" value="{{row.colorNumber}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="stockentryList{{idx}}_number" name="stockentryList[{{idx}}].number" type="text" value="{{row.number}}"    class="form-control "/>
					</td>
					
					
					<td>
						<textarea id="stockentryList{{idx}}_remarks" name="stockentryList[{{idx}}].remarks" rows="4"    class="form-control ">{{row.remarks}}</textarea>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#stockentryList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var stockentryRowIdx = 0, stockentryTpl = $("#stockentryTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(stock.stockentryList)};
					for (var i=0; i<data.length; i++){
						addRow('#stockentryList', stockentryRowIdx, stockentryTpl, data[i]);
						stockentryRowIdx = stockentryRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		</form:form>
</body>
</html>