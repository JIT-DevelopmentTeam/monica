<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>新闻封面</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">

        $(document).ready(function () {

        });
    </script>
</head>
<body class="bg-white">
    <div class="row">
        <img src="${news.mainpic}" id="img" class="img-polaroid" style="width: 505px;height: 400px;" title="${ news.title}" alt="无封面"/>
    </div>
</body>
</html>