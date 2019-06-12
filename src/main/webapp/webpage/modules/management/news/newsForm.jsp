<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>新闻公告管理</title>
	<meta name="decorator" content="ani"/>
	<!-- SUMMERNOTE -->
	<script src="${ctxStatic}/plugin/wangEditor/js/wangEditor.js"></script>

	<script type="text/javascript">

		$(document).ready(function() {
            $("textarea").css("resize","none");


            //富文本初始化
            var E = window.wangEditor;
            var editor = new E('#content');
            // 配置服务器端地址
            editor.customConfig.uploadImgServer = '${ctx}/management/news/news/uploadEditorPic';
            // 自定义菜单配置
            editor.customConfig.menus = [
                'head',  // 标题
                'bold',  // 粗体
                'fontSize',  // 字号
                'fontName',  // 字体
                'italic',  // 斜体
                'underline',  // 下划线
                'strikeThrough',  // 删除线
                'foreColor',  // 文字颜色
                'backColor',  // 背景颜色
                'link',  // 插入链接
                'list',  // 列表
                'justify',  // 对齐方式
                'quote',  // 引用
                'emoticon',  // 表情
                'image',  // 插入图片
                'table',  // 表格
                'video',  // 插入视频
                'code',  // 插入代码
                'undo',  // 撤销
                'redo'  // 重复
            ];

            // 将图片大小限制为 3M
            editor.customConfig.uploadImgMaxSize = 3 * 1024 * 1024;
            // 限制一次最多上传 5 张图片
            editor.customConfig.uploadImgMaxLength = 5;
            // 将 timeout 时间改为 3s
            editor.customConfig.uploadImgTimeout = 50000;
            //自定义文件名
            editor.customConfig.uploadFileName = 'fileName';
            editor.customConfig.uploadImgHooks = {
                before: function (xhr, editor, files) {
                    // 图片上传之前触发
                    // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，files 是选择的图片文件

                    // 如果返回的结果是 {prevent: true, msg: 'xxxx'} 则表示用户放弃上传
                    // return {
                    //     prevent: true,
                    //     msg: '放弃上传'
                    // }
                    // alert("前奏");
                },
                success: function (xhr, editor, result) {
                    // 图片上传并返回结果，图片插入成功之后触发
                    // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，result 是服务器端返回的结果
                    // var url = result.data.url;
                    // alert(JSON.stringify(url));
                    // editor.txt.append(url);
                    // alert("成功");
                },
                fail: function (xhr, editor, result) {
                    // 图片上传并返回结果，但图片插入错误时触发
                    // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，result 是服务器端返回的结果
                    alert("失败");
                },
                error: function (xhr, editor) {
                    // 图片上传出错时触发
                    // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象
                    // alert("错误");
                },
                // 如果服务器端返回的不是 {errno:0, data: [...]} 这种格式，可使用该配置
                // （但是，服务器端返回的必须是一个 JSON 格式字符串！！！否则会报错）
                customInsert: function (insertImg, result, editor) {
                    // 图片上传并返回结果，自定义插入图片的事件（而不是编辑器自动插入图片！！！）
                    // insertImg 是插入图片的函数，editor 是编辑器对象，result 是服务器端返回的结果
                    // 举例：假如上传图片成功后，服务器端返回的是 {url:'....'} 这种格式，即可这样插入图片：
                    var url = result.url
                    console.log(url);
                    //var jsonStrings = encodeURIComponent(url);
                    insertImg(url);
                    // result 必须是一个 JSON 格式字符串！！！否则报错
                }
            };
            editor.customConfig.onchange = function (html) {
                // 监控变化，同步更新到 textarea
                $("[name=\"content\"]").val(html)
            }
            editor.create();
            // 初始化 textarea 的值
            $("[name=\"content\"]").val(editor.txt.html());

			/*$('#content').summernote({
				height: 300,
                lang: 'zh-CN',
                callbacks: {
                    onChange: function(contents, $editable) {
                        $("input[name='content']").val($('#content').summernote('code'));//取富文本的值
                    }
                }
            });*/
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
						<input htmlEscape="false" class="form-control required" value="${fns:getUser().name}" readonly="true"/>
						<form:hidden path="authorid" value="${fns:getUser().id}"/>
					</td>
					<td class="width-15 active"><label class="pull-right">发布人部门：</label></td>
					<td class="width-35">
						<input  class="form-control " readonly="readonly"/>
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
                        <input type="hidden" name="content" value="${news.content}"/>
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