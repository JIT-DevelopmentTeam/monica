<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>新闻公告管理</title>
	<meta name="decorator" content="ani"/>
	<link rel="stylesheet" href="${ctxStatic}/plugin/bootstrapselect/bootstrap-select.min.css">
	<!-- SUMMERNOTE -->
	<script src="${ctxStatic}/plugin/wangEditor/js/wangEditor.js"></script>

    <script src="${ctxStatic}/plugin/bootstrapselect/bootstrap-select.min.js"></script>
    <script src="${ctxStatic}/plugin/bootstrapselect/defaults-zh_CN.min.js"></script>
	<style>
		.text{
			font-size: 14px;
		}
	</style>

	<script type="text/javascript">

		$(document).ready(function() {
            UE.getEditor('editor',{
                initialFrameWidth :window.innerWidth*0.8,//设置编辑器宽度
                initialFrameHeight:window.innerHeight*0.7,//设置编辑器高度
                scaleEnabled:true//设置不自动调整高度
                //scaleEnabled {Boolean} [默认值：false]//是否可以拉伸长高，(设置true开启时，自动长高失效)
            });
		    $("#objId").selectpicker({
                'width': '337px',
                'title': '请选择'
			});

            $("textarea").css("resize","none");

            $("#starttime").datetimepicker({
                format:"YYYY-MM-DD",
            }).on('dp.changeDate',function(ev){
                var starttime=$("#starttime").val();
                $("#endtime").datetimepicker('setStartDate',starttime);
                $("#starttime").datetimepicker('hide');
            });

            $("#endtime").datetimepicker({
                format:"YYYY-MM-DD",
            }).on('dp.changeDate',function(ev) {
                var endtime = $("#endtime").val();
                $("#starttime").datetimepicker('setEndDate', endtime);
                $("#endttime").datetimepicker('hide');
            });



	        $('#push').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });

	        $("#pullStarttime").click(function () {
                $("#starttime").find("div").css("z-index",
					"99999");
            });

	        $("#pullEndtime").click(function () {
                $("#endtime").find("div").css("z-index","99999");
            });

	        $("#pushTime").click(function () {
                $("#push").find("div").css("z-index","99999");
            });

	        $("input[name='isPush']").each(function () {
				if ($("input[name='isPush']:checked").val() == '0') {
					$("input[name='push']").attr("disabled", "true");
					$("#pushrule").attr("disabled", "true");
					$("#objId").attr("disabled", "true");
				} else {
					$("input[name='sendType']").each(function () {
						if ($("input[name='sendType']:checked").val() == '0') {
							$("select[name='pushrule']").html(
									"<option value=''>请选择</option>" +
									"<option value='1'>人员推送</option>")
						} else if ($("input[name='sendType']:checked").val() == '1') {
							$("select[name='pushrule']").html(
									"<option value=''>请选择</option>" +
									"<option value='0'>全部推送</option>" +
									"<option value='1'>人员推送</option>" +
									"<option value='2'>部门推送</option>")
						}
					});
				}
			});

	        // 是否推送 = 1  时，则可以选择推送类型
            /*console.log("---->>  "+$("input[name='isPush']").val());
            if(${news.isPush eq 1}){
                var $sendType = $("[name='sendType']");
                $sendType.removeAttr("disabled");

                var $sendType_div =$("#sendType-div div");
                $sendType_div.attr("class","iradio_square-blue ");
            }*/

			$("input[name='sendType']").on("ifChecked", function() {
				if (this.value == '0') {
					$("select[name='pushrule']").html(
							"<option value=''>请选择</option>" +
							"<option value='1'>人员推送</option>")
				} else if (this.value == '1') {
					$("select[name='pushrule']").html(
							"<option value=''>请选择</option>" +
							"<option value='0'>全部推送</option>" +
							"<option value='1'>人员推送</option>" +
							"<option value='2'>部门推送</option>")
				}
			});


            // 判断是否推送，当前选中值
            $("input[name='isPush']").on('ifChecked', function () {
                if (this.value == "0") {
                    var push= $("input[name='push']");
                    push.attr("disabled", "true");
                    push.attr("class","form-control ");

                    $("#pushrule").attr("disabled", "true");
                    $("#objId").attr("disabled", "true");
                    $("[name='push']").val("");
                    var $sendType = $("[name='sendType']");
                    $sendType.attr("disabled","true");
                    var $sendType_div =$("#sendType-div div");
                    $sendType_div.attr("class","iradio_square-blue disabled");
                } else if (this.value == "1") {
                    // 推送时间
                    var push = $("[name='push']");
                    push.removeAttr("disabled");
                    push.attr("class","form-control  required");
                    // 推送规则
                    var pushrule = $("#pushrule");
                    pushrule.removeAttr("disabled");
                    pushrule.attr("class","form-control  required");
                    // 推送对象
                    var  objId = $("#objId");
                    objId.removeAttr("disabled");
                    objId.attr("class","selectpicker required show-tick ");

                    var $sendType = $("[name='sendType']");
                    $sendType.removeAttr("disabled");

                    var $sendType_div =$("#sendType-div div");
                    $sendType_div.attr("class","iradio_square-blue ");
                }
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
        function getUserName() {
            console.log("获取发布人/作者");
            jp.openSaveDialog('选择发布人', "${ctx}/management/news/news/newsUserList", '900px', '500px');
        }

        /**
         * 新闻推送规则
         */
        function newsPushRule(val) {
        	let sendType = $("input[name='sendType']:checked").val();
            if(val == ""){
                return;
            }
            check(val);
            var option="";
            jp.post("${ctx}/management/news/news/userOrOffice",{pushrule:val, sendType: sendType},function (data) {
            	//微信人员列表
				if (data.wxUserListInfo != null) {
					if (data.wxUserListInfo.length > 0) {
						$.each(data.wxUserListInfo, function (index, value) {
							option +="<option  value=\""+value.id+"\"   data-tokens=\""+ value.nickName+"\">"+value.nickName+"</option>";
						})
					}
				}
                //人员列表
                if(data.userListInfo != null){
                    if(data.userListInfo.length > 0){
                        $.each(data.userListInfo, function (index, value) {
                            //console.log(value.id+":"+value.name);
                            option +="<option  value=\""+value.id+"\"   data-tokens=\""+ value.name+"\">"+value.name+"</option>";
                        });
                        //console.log(option);
                    }
                }
                //部门列表
                if(data.officeListInfo !=null){
                    if(data.officeListInfo.length > 0){
                        $.each(data.officeListInfo, function (index, value) {
                            console.log(value.id+":"+value.name);
                            option +="<option  value=\""+value.id+"\">"+value.name+"</option>";
                        });
                        // console.log(""+option);
                    }
                }
                $("#objId").html(option);
                $('#objId').selectpicker('refresh');//刷新
				$('div[class="dropdown-menu open"]').css("z-index", "99999");
            });
        }
        // 检验规则，必填项
        function check(val) {
            var  objId = $("#objId");
            if(val == '0'){
                objId.attr("disabled","true");
                objId.attr("class","selectpicker  show-tick ");
            }else if(val == '1' || val == '2'){
                objId.removeAttr("disabled");
                objId.attr("class","selectpicker required  show-tick ");
            }
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
						<form:hidden path="smallnrl" htmlEscape="false"  class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>发布人：</label></td>
					<td class="width-35">
                        <div class="input-group">
                            <div class="input-icon-group">
                                    <input type="text" class="form-control required" value="${fns:getUser().name}" data-clearbtn="true"/>
                             </div>
                            <span class="input-group-btn">
                                 <button class="btn btn-primary" type="button" onclick="getUserName();"><i class="fa fa-search" aria-hidden="true"></i></button>
                            </span>
                        </div>
						<form:hidden path="authorid" htmlEscape="false" class="form-control required"  value="${fns:getUser().id}"/>
					</td>
					<td class="width-15 active"><label class="pull-right">发布人部门：</label></td>
					<td class="width-35">
						<input  class="form-control required" value="${fns:getUser().office.name}" id="officeName" readonly="true"/>
						<form:hidden path="deptid" htmlEscape="false" class="form-control" value="${fns:getUser().office.id}"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否发布：</label></td>
					<td class="width-35">
						<form:radiobutton path="isPublic"   itemLabel="label" value="0" htmlEscape="false" class="i-checks required" checked="${checked}"/>否
						<form:radiobutton path="isPublic"   itemLabel="label" value="1" htmlEscape="false" class="i-checks required"/>是
                        <label class="error" for="isPublic" id="isPublic"></label>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否设置为头条：</label></td>
					<td class="width-35">
						<form:radiobutton path="headline" itemLabel="label" value="0" htmlEscape="false" class="i-checks required" checked="${checked}"/>否
						<form:radiobutton path="headline" itemLabel="label" value="1" htmlEscape="false" class="i-checks required" />是
                        <label class="error" for="headline" id="headline"></label>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">显示时间开始：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='starttime'>
							<input type='text'  name="starttime" class="form-control "  value="<fmt:formatDate value="${news.starttime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon" id="pullStarttime">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>
					<td class="width-15 active"><label class="pull-right">显示结束时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='endtime'>
							<input type='text'  name="endtime" class="form-control "  value="<fmt:formatDate value="${news.endtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<div id="pushtimepicker" class="bootstrap-datetimepicker-widget dropdown-menu usetwentyfour bottom pull-right"></div>
							<span class="input-group-addon" id="pullEndtime">
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
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>是否推送：</label></td>
					<td class="width-35">
						<form:radiobutton path="isPush" itemLabel="label" value="0" htmlEscape="false" class="i-checks required" checked="${checked}"/>否
						<form:radiobutton path="isPush" itemLabel="label" value="1" htmlEscape="false" class="i-checks required"/>是
                        <label class="error" for="isPush" id="isPush"></label>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>推送类型：</label></td>
					<td class="width-35" id="sendType-div">
						<form:radiobutton path="sendType" itemLabel="label" value="0" htmlEscape="false" class="i-checks required"  disabled="${news.id == undefined ? 'true': 'false'}"/>服务号
						<form:radiobutton path="sendType" itemLabel="label" value="1" htmlEscape="false" class="i-checks required"  disabled="${news.id == undefined ? 'true': 'false'}"/>企业微信
						<label class="error" for="sendType" id="sendType"></label>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">推送时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='push'>
							<input type='text'  name="push" class="form-control "  value="<%--<fmt:formatDate value="${news.push}" pattern="yyyy-MM-dd HH:mm:ss"/>--%>"/>
							<span class="input-group-addon" id="pushTime">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
					</td>

					<td class="width-15 active"><label class="pull-right">推送规则：</label></td>
					<td class="width-35">
                        <form:select path="pushrule" htmlEscape="false" class="form-control " onchange="newsPushRule(this.value)">
<%--                            <form:option value="">请选择</form:option>--%>
<%--                            <form:option value="0">全部推送</form:option>--%>
<%--                            <form:option value="1">人员推送</form:option>--%>
<%--                            <form:option value="2">部门推送</form:option>--%>
                        </form:select>
				</tr>
				<tr>
                    <td  class="width-15 active"><label class="pull-right">推送对象：</label></td>
                    <td class="width-35">
						<div>
							<select id="objId" name="objId" class="selectpicker required show-tick " multiple  data-live-search="true" data-actions-box="true" data-width="auto">

							</select>
						</div>

                    </td>

                    </td>
                    <td class="width-15 active"><label class="pull-right">阅读次数：</label></td>
                    <td class="width-35">
                        <form:input path="readCount" readonly="true" htmlEscape="false" type="number" min="0" step="1"  class="form-control " />
                    </td>
                </tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td colspan="3">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
		  		</tr>
                <tr>
					<td class="width-15 active"><label class="pull-right">文本内容：</label></td>
					<td colspan="3">
                        <script type="text/plain" id="editor" name="content" htmlEscape="false" >
                            ${fns:unescapeHtml(news.content)}
                        </script>
					</td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
    <%
        //获取application域中的count的值
        Integer count = (Integer)application.getAttribute("count");
        //判断count是否为空  https://zhidao.baidu.com/question/1754793497933447348.html 如何解决Ueditor Flash初始化失败
        if(count!=null){
            //不等于空
            count++;
            //存入到application域中
            application.setAttribute("count", count);
        }else{
            //等于空
            count=1;
            //存入到application域中
            application.setAttribute("count", count);
        }
    %>
        <!-- 百度富文本编辑框-->
        <script type="text/javascript" charset="utf-8">window.UEDITOR_HOME_URL = "${ctxStatic}/plugin/ueditor/";</script>
        <script type="text/javascript" charset="utf-8" src="${ctxStatic}/plugin/ueditor/ueditor.config.js"></script>
        <script type="text/javascript" charset="utf-8" src="${ctxStatic}/plugin/ueditor/ueditor.all.js"></script>
</body>
</html>