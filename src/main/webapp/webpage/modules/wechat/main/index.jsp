<%--suppress ALL --%>
<%@ page import="com.jeeplus.modules.wxapi.api.wxuser.user.JwUserAPI" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${ctxStatic}/css/weui.css">
    <script src="${ctxStatic}/js/jquery-2.1.4.js"></script>
    <script src="${ctxStatic}/js/jquery-weui.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue.js"></script>
    <title>莫尔卡·移动应用平台</title>
</head>
<body>
<%
    Map<String,Object> wxuser = new HashMap<>();
    //请求身份认证信息,如果为空或失效,则启用公共回调页面
    String code = request.getParameter("code");
    String agentId = request.getParameter("agentId");
    if(code == null || code == "") {
        String pageCurr = request.getRequestURL().toString() ;  //  获取当前页为设置为回调页面
        if(agentId != null && agentId != "")
            pageCurr = pageCurr + "?agentId=" + agentId;
        pageCurr = java.net.URLEncoder.encode(pageCurr); //  url编码
        String url ="https://open.weixin.qq.com/connect/oauth2/authorize?appid=ww49c384af1f4dac63&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        url = url.replace("REDIRECT_URI",pageCurr);
        response.sendRedirect(url); //  调用回调
    } else {
        //  得知CODE值,则解析出身份信息
        wxuser = JwUserAPI.getWxuserInfo(code);
    }
%>
<input type="hidden" id="qyUserId" value="<%= wxuser.get("UserId") != null ? wxuser.get("UserId").toString() : null %>"/>
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
        <a v-bind:href="reviewHref" class="weui-grid">
            <div class="weui-grid__icon">
                <img src="${ctxStatic}/image/wechat/page_white_edit.png" alt="">
            </div>
            <p class="weui-grid__label">{{ review }}</p>
        </a>
    </div>
</div>
</body>

<script>
    var vm = new Vue({
        el: ".weui-grids",
        data: {
            news: "新闻公告",
            newsHref: "${ctxf}/wechat/news/list",
            order: "订单管理",
            orderHref: "${ctxf}/wechat/sobill/list?qyUserId="+$("#qyUserId").val(),
            barCode: "条码追溯",
            barCodeHref: "${ctxf}/wechat/barCode/list",
            stock: "库存查询",
            stockHref: "${ctxf}/wechat/stock/list",
            warning: "预警提醒",
            warningHref: "",
            review: "订单审核",
            reviewHref: "${ctxf}/wechat/review/list?qyUserId="+$("#qyUserId").val()
        }
    });
</script>
</html>