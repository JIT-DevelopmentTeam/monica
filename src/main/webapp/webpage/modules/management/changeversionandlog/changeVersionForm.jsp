<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>变更管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
	        $('#date').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});

		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/management/changeversionandlog/changeVersion/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="changeVersion" action="${ctx}/management/changeversionandlog/changeVersion/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">变更人：</label></td>
					<td class="width-35">
						<form:input path="user" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">变更日期：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='date'>
							<input type='text'  name="date" class="form-control "  value="<fmt:formatDate value="${changeVersion.date}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
					<td class="width-15 active"><label class="pull-right">变更版本：</label></td>
					<td class="width-35">
						<form:input path="version" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">所属订单：</label></td>
					<td class="width-35">
						<form:input path="sobill" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">变更记录：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#changeLogList', changeLogRowIdx, changeLogTpl);changeLogRowIdx = changeLogRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th>备注信息</th>
						<th>字段名称</th>
						<th>变更前</th>
						<th>变更后</th>
						<th>变更模块</th>
						<th>所属版本</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="changeLogList">
				</tbody>
			</table>
			<script type="text/template" id="changeLogTpl">//<!--
				<tr id="changeLogList{{idx}}">
					<td class="hide">
						<input id="changeLogList{{idx}}_id" name="changeLogList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="changeLogList{{idx}}_delFlag" name="changeLogList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<textarea id="changeLogList{{idx}}_remarks" name="changeLogList[{{idx}}].remarks" rows="4"    class="form-control ">{{row.remarks}}</textarea>
					</td>
					
					
					<td>
						<input id="changeLogList{{idx}}_name" name="changeLogList[{{idx}}].name" type="text" value="{{row.name}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="changeLogList{{idx}}_beforeChange" name="changeLogList[{{idx}}].beforeChange" type="text" value="{{row.beforeChange}}"    class="form-control "/>
					</td>
					
					
					<td>
						<input id="changeLogList{{idx}}_afterChange" name="changeLogList[{{idx}}].afterChange" type="text" value="{{row.afterChange}}"    class="form-control "/>
					</td>
					
					
					<td>
						<c:forEach items="${fns:getDictList('change_log_module')}" var="dict" varStatus="dictStatus">
							<span><input id="changeLogList{{idx}}_module${dictStatus.index}" name="changeLogList[{{idx}}].module" type="radio" class="i-checks" value="${dict.value}" data-value="{{row.module}}"><label for="changeLogList{{idx}}_module${dictStatus.index}">${dict.label}</label></span>
						</c:forEach>
					</td>
					
					
					<td>
						<input id="changeLogList{{idx}}_changeVersion" name="changeLogList[{{idx}}].changeVersion" type="text" value="{{row.changeVersion}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#changeLogList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var changeLogRowIdx = 0, changeLogTpl = $("#changeLogTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(changeVersion.changeLogList)};
					for (var i=0; i<data.length; i++){
						addRow('#changeLogList', changeLogRowIdx, changeLogTpl, data[i]);
						changeLogRowIdx = changeLogRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		</form:form>
</body>
</html>