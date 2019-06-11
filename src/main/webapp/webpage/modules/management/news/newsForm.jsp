<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>新闻公告管理</title>
	<meta name="decorator" content="ani"/>
	<!-- SUMMERNOTE -->
	<%@include file="/webpage/include/summernote.jsp" %>
	<script type="text/javascript">

		$(document).ready(function() {
            $("textarea").css("resize","none");

					//富文本初始化
			$('#content').summernote({
				height: 300,
                lang: 'zh-CN',
                callbacks: {
                    onChange: function(contents, $editable) {
                        $("input[name='content']").val($('#content').summernote('code'));//取富文本的值
                    }
                }
            });
	        $('#starttime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#endtime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#push').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/management/news/news/save",$('#inputForm').serialize(),function(data){
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
        // 上传附件
        function upload() {
            jp.openViewDialog('上传附件', "${ctx}/management/news/news/uploadFile/${projust.id}", '600px', '500px');
        }
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="news" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>标题：</label></td>
					<td class="width-35">
						<form:input path="title" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">封面图片：</label></td>
					<td class="width-35">
						<button id="uploadFile" class="btn btn-primary" onclick="upload();return false;">
							<i class="glyphicon glyphicon-upload"></i> 上传附件
						</button>
						<form:hidden path="mainpic" htmlEscape="false"  class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>发布人：</label></td>
					<td class="width-35">
						<input id="authorid" htmlEscape="false"  class="form-control required" value="${fns:getUser().name}" readonly="true"/>
						<form:hidden path="authorid" value="${fns:getUser().id}"/>
					</td>
					<td class="width-15 active"><label class="pull-right">发布人部门：</label></td>
					<td class="width-35">
						<input id="office.name" value="${fns:getUser().office.name}" class="form-control " readonly="readonly"/>
						<form:hidden path="deptid" htmlEscape="false" class="form-control " value="${fns:getUser().office.id}"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否发布：</label></td>
					<td class="width-35">
						<form:radiobutton path="isPublic"   itemLabel="label" value="0" htmlEscape="false" class="i-checks required"/>否
						<form:radiobutton path="isPublic"   itemLabel="label" value="1" htmlEscape="false" class="i-checks required"/>是
					</td>

					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否设置为头条：</label></td>
					<td class="width-35">
						<form:radiobutton path="headline" itemLabel="label" value="0" htmlEscape="false" class="i-checks required"/>否
						<form:radiobutton path="headline" itemLabel="label" value="1" htmlEscape="false" class="i-checks required"/>是
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">显示时间开始：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='starttime'>
							<input type='text'  name="starttime" class="form-control "  value="<fmt:formatDate value="${news.starttime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
					<td class="width-15 active"><label class="pull-right">显示结束时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='endtime'>
							<input type='text'  name="endtime" class="form-control "  value="<fmt:formatDate value="${news.endtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>摘要：</label></td>
					<td colspan="3">
						<form:textarea path="describe" htmlEscape="false" rows="4"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>内容：</label></td>
					<td colspan="3">
                        <input type="hidden" name="content" value=" ${news.content}"/>
						<div id="content">
                          ${fns:unescapeHtml(news.content)}
                        </div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否推送：</label></td>
					<td class="width-35">
						<form:radiobutton path="isPush" itemLabel="label" value="0" htmlEscape="false" class="i-checks required"/>否
						<form:radiobutton path="isPush" itemLabel="label" value="1" htmlEscape="false" class="i-checks required"/>是
					</td>
					<td class="width-15 active"><label class="pull-right">推送时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='push'>
							<input type='text'  name="push" class="form-control "  value="<fmt:formatDate value="${news.push}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">推送规则：</label></td>
					<td class="width-35">
						<form:input path="pushrule" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">阅读次数：</label></td>
					<td class="width-35">
						<form:input path="readCount" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td colspan="3">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>