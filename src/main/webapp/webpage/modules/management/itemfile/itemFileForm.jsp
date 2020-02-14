<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>上传商品图片管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">

        $(document).ready(function () {

        });

        function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if (!isValidate) {
                return false;
            } else {
                jp.loading();
                jp.post("${ctx}/management/itemfile/itemFile/save", $('#inputForm').serialize(), function (data) {
                    if (data.success) {
                        jp.getParent().refresh();
                        var dialogIndex = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                        parent.layer.close(dialogIndex);
                        jp.success(data.msg)

                    } else {
                        jp.error(data.msg);
                    }
                })
            }

        }
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="itemFile" class="form-horizontal">
    <form:hidden path="id"/>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td class="width-15 active"><label class="pull-right">商品名称：</label></td>
            <td class="width-35">
                <form:hidden path="itemId" htmlEscape="false" class="form-control "/>
                <form:input path="icitem.name" htmlEscape="false" class="form-control " readonly="true"/>
            </td>

            <td class="width-15 active"><label class="pull-right">文件原名称：</label></td>
            <td class="width-35">
                <form:input path="originalName" htmlEscape="false" class="form-control " readonly="true"/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">上传编码名称：</label></td>
            <td class="width-35">
                <form:input path="name" htmlEscape="false" class="form-control "/>
            </td>

            <td class="width-15 active"><label class="pull-right">文件大小：</label></td>
            <td class="width-35">
                <form:input path="size" htmlEscape="false" class="form-control " readonly="true"/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">文件类型：</label></td>
            <td class="width-35">
                <form:input path="type" htmlEscape="false" class="form-control " readonly="true"/>
            </td>

            <td class="width-15 active"><label class="pull-right">文件是否允许下载：</label></td>
            <td class="width-35">
                <form:select path="isDown" htmlEscape="false"    class="form-control ">
                    <form:option value="" label="请选择"/>
                    <form:option value="0" label="不允许"/>
                    <form:option value="1" label="允许"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">文件路径：</label></td>
            <td class="width-35">
                <form:input path="url" htmlEscape="false" class="form-control " readonly="true"/>
            </td>

            <td class="width-15 active"><label class="pull-right">文件预览图路径：</label></td>
            <td class="width-35">
                <form:input path="smallUrl" htmlEscape="false" class="form-control " readonly="true"/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">文件服务器地址：</label></td>
            <td class="width-35">
                <form:input path="server" htmlEscape="false" class="form-control "/>
            </td>

            <td class="width-15 active"><label class="pull-right">下载次数：</label></td>
            <td class="width-35">
                <form:input path="downCount" type="number" min="0" htmlEscape="false" class="form-control " readonly="true"/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">备注信息：</label></td>
            <td class="width-35" colspan="3">
                <form:textarea path="remarks" htmlEscape="false" rows="4" class="form-control "/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>