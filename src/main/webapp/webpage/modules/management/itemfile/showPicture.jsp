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
    <div class="row">
        <img src="${cxt }${ itemFile.url}" id="img" class="img-polaroid" style="width: 505px;height: 400px;" title="${ itemFile.name}" alt="无图"/>
    </div>
</body>
</html>