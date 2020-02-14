<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>新闻公告附件管理</title>
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
                jp.post("${ctx}/management/newsfile/newsFile/save",$('#inputForm').serialize(),function(data){
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
        function getNewsList() {
            jp.openSaveDialog('选择新闻', "${ctx}/management/news/news/list", '900px', '500px');
        }
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="newsFile" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">所属新闻：</label></td>
					<td class="width-35">
						<form:input path="newsId" htmlEscape="false"    class="form-control "/>
					</td>
					<%--<td class="width-35">--%>
						<%--<div class="input-group">--%>
							<%--<div class="input-icon-group">--%>
								<%--<input type="text" class="form-control required" value="" data-clearbtn="true"/>--%>
							<%--</div>--%>
							<%--<span class="input-group-btn">--%>
                                 <%--<button class="btn btn-primary" type="button" onclick="getNewsList();"><i class="fa fa-search" aria-hidden="true"></i></button>--%>
                            <%--</span>--%>
						<%--</div>--%>
						<%--<form:hidden path="newsId" htmlEscape="false" class="form-control required"  value=""/>--%>
					<%--</td>--%>
					<td class="width-15 active"><label class="pull-right">文件原名称：</label></td>
					<td class="width-35">
						<form:input path="originalName" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">上传编码名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">文件大小：</label></td>
					<td class="width-35">
						<form:input path="size" type="number" min="0"  htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">文件类型：</label></td>
					<td class="width-35">
						<form:input path="type" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">文件是否允许下载：</label></td>
					<td class="width-35">
						<form:select path="isDown" htmlEscape="false"    class="form-control ">
							<form:option value="" label="请选择"/>
							<form:option value="0" label="否"/>
							<form:option value="1" label="是"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">文件路径：</label></td>
					<td class="width-35">
						<form:input path="url" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">文件预览图路径：</label></td>
					<td class="width-35">
						<form:input path="smallUrl" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">文件服务器地址：</label></td>
					<td class="width-35">
						<form:input path="server" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">下载次数：</label></td>
					<td class="width-35">
						<form:input path="downCount" type="number" min="0" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
		   		<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35" colspan="3">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>