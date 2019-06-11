<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta charset="UTF-8">
    <title>新增/编辑订单</title>
    <link rel="stylesheet" href="${ctxStatic}/css/weui.min.css">
    <script src="${ctxStatic}/css/jquery-weui.css"></script>
</head>
<body>
<form id="Form" method="post" action="${ctx}/management/sobillandentry/sobill/save">
    <input type="hidden" id="billNo" name="billNo"/>
    <div class="weui-cells">
        <div class="weui-cell">
            <div class="weui-cell__bd">
                <p>编号:</p>
            </div>
            <div class="weui-cell__ft" id="number"></div>
        </div>

        <div class="weui-cell">
            <div class="weui-cell__bd">
                <p>客户:</p>
            </div>
            <div class="weui-cell__ft"></div>
        </div>

        <div class="weui-cell">
            <div class="weui-cell__bd">
                <p>销售员:</p>
            </div>
            <div class="weui-cell__ft"></div>
        </div>

        <div class="weui-cell">
            <div class="weui-cell__bd">
                <p>状态:</p>
            </div>
            <div class="weui-cell__ft"></div>
        </div>
    </div>

    <div class="weui-cells__title">单选列表项</div>
    <div class="weui-cells weui-cells_radio">
        <label class="weui-cell weui-check__label" for="x11">
            <div class="weui-cell__bd">
                <p>cell standard</p>
            </div>
            <div class="weui-cell__ft">
                <input type="radio" class="weui-check" name="radio1" id="x11"/>
                <span class="weui-icon-checked"></span>
            </div>
        </label>
        <label class="weui-cell weui-check__label" for="x12">

            <div class="weui-cell__bd">
                <p>cell standard</p>
            </div>
            <div class="weui-cell__ft">
                <input type="radio" name="radio1" class="weui-check" id="x12" checked="checked"/>
                <span class="weui-icon-checked"></span>
            </div>
        </label>
    </div>

    <div style="position: fixed;bottom: 2%;width: 100%;">
        <a href="javascript:;" class="weui-btn weui-btn_primary">提交</a>
    </div>

    <div id="function" class="weui-tabbar" style="position:fixed;bottom: 0px;">
        <a v-bind:href="addHref" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/icon-add.png" alt="">
            </div>
            <p class="weui-tabbar__label">{{add}}</p>
        </a>
        <a href="javascript:;" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/icon-delete.png" alt="">
            </div>
            <p class="weui-tabbar__label">删除</p>
        </a>
    </div>
</form>

<script src="${ctxStatic}/js/jquery-weui.js"></script>
<script src="${ctxStatic}/js/jquery-2.1.4.js"></script>
</body>
</html>
