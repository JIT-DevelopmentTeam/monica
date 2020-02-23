<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>商品资料管理</title>
	<meta name="decorator" content="ani"/>
	<script src="${ctxStatic}/plugin/wangEditor/js/wangEditor.js"></script>
	<script type="text/javascript">

		$(document).ready(function() {
			UE.getEditor('editor',{
				initialFrameWidth :window.innerWidth*0.8,//设置编辑器宽度
				initialFrameHeight:window.innerHeight*0.7,//设置编辑器高度
				scaleEnabled:true//设置不自动调整高度
				//scaleEnabled {Boolean} [默认值：false]//是否可以拉伸长高，(设置true开启时，自动长高失效)
			});

		});

		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/management/icitemclass/icitem/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="icitem" class="form-horizontal">
		<form:hidden path="id"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">编号：</label></td>
					<td class="width-35">
						<form:input path="number" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">商品名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品型号：</label></td>
					<td class="width-35">
						<form:input path="model" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">商品计算单位：</label></td>
					<td class="width-35">
						<form:input path="unit" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">erp备注：</label></td>
					<td class="width-35">
						<form:input path="erpNote" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">状态：</label></td>
					<td class="width-35">
						<form:input path="status" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">销售价格（元）：</label></td>
					<td class="width-35" >
						<input type="number" id="salePrice" name="salePrice" value="${icitem.salePrice}" htmlEscape="false"    class="form-control " onKeyPress="if((event.keyCode<48 || event.keyCode>57) && event.keyCode!=46 || /\.\d\d$/.test(value))event.returnValue=false"/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
		  		</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">商品描述：</label></td>
					<td colspan="3">
						<script type="text/plain" id="editor" name="describe" htmlEscape="false" >
                            ${fns:unescapeHtml(icitem.describe)}
                        </script>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>

		<!-- 百度富文本编辑框-->
		<script type="text/javascript" charset="utf-8">window.UEDITOR_HOME_URL = "${ctxStatic}/plugin/ueditor/";</script>
		<script type="text/javascript" charset="utf-8" src="${ctxStatic}/plugin/ueditor/ueditor.config.js"></script>
		<script type="text/javascript" charset="utf-8" src="${ctxStatic}/plugin/ueditor/ueditor.all.js"></script>
</body>
</html>