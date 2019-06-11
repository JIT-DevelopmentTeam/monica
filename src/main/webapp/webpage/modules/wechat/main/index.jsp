<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${ctxStatic}/css/weui.css">
    <script src="${ctxStatic}/js/jquery-weui.js"></script>
    <script src="${ctxStatic}/js/jquery-2.1.4.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue.js"></script>
    <title>莫尔卡·移动应用平台</title>
</head>
<body>
<div class="page">
    <div class="weui-grids">
        <a v-bind:href="newsHref" class="weui-grid">
            <div class="weui-grid__icon">
                <img src="${ctxStatic}/image/wechat/newspaper.png" alt="">
            </div>
            <p class="weui-grid__label">{{ news }}</p>
        </a>
        <a v-bind:href="orderHref" class="weui-grid">
            <div class="weui-grid__icon">
                <img src="${ctxStatic}/image/wechat/paste_plain.png" alt="">
            </div>
            <p class="weui-grid__label">{{ order }}</p>
        </a>
        <a v-bind:href="barCodeHref" class="weui-grid">
            <div class="weui-grid__icon">
                <img src="${ctxStatic}/image/wechat/qr-droid.png" alt="">
            </div>
            <p class="weui-grid__label">{{ barCode }}</p>
        </a>
        <a v-bind:href="stockHref" class="weui-grid">
            <div class="weui-grid__icon">
                <img src="${ctxStatic}/image/wechat/inventory-maintenance.png" alt="">
            </div>
            <p class="weui-grid__label">{{ stock }}</p>
        </a>
        <a v-bind:href="warningHref" class="weui-grid">
            <div class="weui-grid__icon">
                <img src="${ctxStatic}/image/wechat/appointment-missed.png" alt="">
            </div>
            <p class="weui-grid__label">{{ warning }}</p>
        </a>
        <a v-bind:href="dataAnalysisHref" class="weui-grid">
            <div class="weui-grid__icon">
                <img src="${ctxStatic}/image/wechat/chart_curve.png" alt="">
            </div>
            <p class="weui-grid__label">{{ dataAnalysis }}</p>
        </a>
        <a v-bind:href="checkInHref" class="weui-grid">
            <div class="weui-grid__icon">
                <img src="${ctxStatic}/image/wechat/map.png" alt="">
            </div>
            <p class="weui-grid__label">{{ checkIn }}</p>
        </a>
        <a v-bind:href="reportHref" class="weui-grid">
            <div class="weui-grid__icon">
                <img src="${ctxStatic}/image/wechat/page_white_edit.png" alt="">
            </div>
            <p class="weui-grid__label">{{ report }}</p>
        </a>
    </div>
</div>
</body>

<script>
    var vm = new Vue({
        el: ".weui-grids",
        data: {
            news: "新闻公告",
            newsHref: "${ctx}/wechat/news/list",
            order: "订单管理",
            orderHref: "${ctx}/wechat/sobill/list",
            barCode: "条码追溯",
            barCodeHref: "",
            stock: "库存查询",
            stockHref: "",
            warning: "预警提醒",
            warningHref: "",
            dataAnalysis: "数据分析",
            dataAnalysisHref: "",
            checkIn: "考勤签到",
            checkInHref: "",
            report: "工作报告",
            reportHref: ""
        }
    });
</script>
</html>