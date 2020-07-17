<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no,viewport-fit=cover">
    <meta charset="UTF-8">
    <title>编辑订单</title>
    <link rel="stylesheet" href="${ctxStatic}/css/weui.min.css">
    <link rel="stylesheet" href="${ctxStatic}/css/jquery-weui.min.css">
    <link rel="stylesheet" href="${ctxStatic}/css/wxqy/editOrder.css">
    <link rel="stylesheet" href="${ctxStatic}/css/wxqy/saleOrder.css">
    <%-- 全面屏iphone适配 --%>
    <link rel="stylesheet" href="${ctxStatic}/css/wxqy/iphoneadaptation/iphoneadaptation.css">
    <script src="${ctxStatic}/common/vue/js/vue.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue-resource.min.js"></script>
    <style type="text/css">
        body {
            margin: 0;
            background: white;
        }

        .all {
            width: 100%;
            height: 100%;
        }

        .bg {
            width: 33vw;
            height: 100%;
            padding-bottom: 50px;
            box-sizing: border-box;
            border-right: 1px rgba(215, 215, 215, 0.6) solid;
            overflow: auto;
        }

        body {
        }

        ul {
            list-style: none;
            padding: 0px;
            margin: 0;
            width: 33vw;
            /*background: rgba(0, 0, 0, 0.6);*/
            /*定位 作为父级使用*/
            /*position: fixed;*/
        }

        ul li {
            height: 50px;
            line-height: 50px;
            list-style-type: none;
            text-align: center;
        }

        ul li:hover {
            background: #FFFFFF;
            color: black;
        }

        ul li a {
            text-decoration: none;
            font-size: 14px;
            color: black;
        }

        ul li:hover > a {
            color: black;
            font-weight: bold;
        }

        /*滑动门*/
        .div2 {
            width: 67vw;
            /* background: skyblue;*/
            /*使用定位实现滑动门-------重要步骤*/
            position: absolute;
            overflow: auto;
            top: 0;
            left: 33vw;
            display: none;
        }

        /*当鼠标悬停在内容上是显示对应的代码块*/
        .playdiv1 {
            /* background: rgb(27, 202, 173); */
            background: #FFFFFF;
            font-weight: bold;
            color: black;
        }

        .play {
            display: block;
            width: 66vw;
            height: 100%;
            text-align: center;
            /*justify-content: space-between;*/
            /*padding: 0px 10px 80px 10px;*/
            background: #FFFFFF;
            /*margin-bottom: 6.25rem;
            padding-bottom: 6.25rem;*/
            /* border: 1px red solid; */
            /* winphone8和android4+ */
            -webkit-overflow-scrolling: touch;
            /* ios5+ */
        }

        .cate {
            display: flex;
            flex-direction: row;
            flex-wrap: wrap;
            align-content: flex-start;
        }

        ul div a {
            padding: 0 1rem;
            color: black;
            height: 35px;
            line-height: 35px;
            border-radius: 50px;
            margin: 3px;

            -moz-box-shadow: 1px 1px 3px #333333;
            -webkit-box-shadow: 1px 1px 3px #333333;
            box-shadow: 1px 1px 3px #333333;
        }

        .second_title {
            text-align: left;
            color: black;
        }

        .second_title:after {
            content: ">>";
        }

        #search {
            width: 100%;
            margin: 0 auto;
            height: auto;
            padding-top: 10px;
            padding-bottom: 0;
            margin-bottom: 0;
        }
    </style>
</head>
<body ontouchstart>
<div id="page">
    <input type="hidden" id="id" name="id" value="${sobill.id}"/>
    <input type="hidden" id="custId" name="custId" value="${sobill.custId}"/>
    <input type="hidden" id="followerId" name="followerId" value="${sobill.followerId}"/>
    <input type="hidden" id="type" name="type" value="${sobill.type}"/>
    <input type="hidden" id="remark01" name="remark01" value="${sobill.remark01}"/>
    <input type="hidden" id="remark02" name="remark02" value="${sobill.remark02}"/>
    <input type="hidden" id="remark03" name="remark03" value="${sobill.remark03}"/>
    <input type="hidden" id="remark04" name="remark04" value="${sobill.remark04}"/>
    <input type="hidden" id="remark05" name="remark05" value="${sobill.remark05}"/>
    <input type="hidden" id="remark06" name="remark06" value="${sobill.remark06}"/>
    <input type="hidden" id="remark07" name="remark07" value="${sobill.remark07}"/>
    <input type="hidden" id="remark08" name="remark08" value="${sobill.remark08}"/>
    <%-- 表头 --%>
    <div class="order-panel">
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                编号
            </div>
            <div class="addOrder-list_ft">
                ${sobill.billNo}
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                <span>*</span>客户
            </div>
            <div class="addOrder-list_ft">
                <input type="text" id="cusName" readonly class="weui-input open-popup" value="${sobill.cusName}"
                       data-target="#customer" style="text-align: right;"/>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                <span>*</span>类型
            </div>
            <div class="addOrder-list_ft">
                <input class="weui-input" readonly id="typeSelect" type="text" placeholder="请选择类型"
                       style="text-align: right;"/>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                <span>*</span>发货日期
            </div>
            <div class="addOrder-list_ft">
                <input placeholder="请选择日期" readonly id="needTime" name="needTime" class="weui-input" type="text"
                       value="<fmt:formatDate value="${sobill.needTime}" pattern="yyyy-MM-dd" />"
                       style="text-align: right;"/>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                <span>*</span>跟单人员
            </div>
            <div class="addOrder-list_ft">
                <input placeholder="请选择跟单人员" type="text" id="followerName" readonly class="weui-input open-popup"
                       data-target="#follower" value="${sobill.followerName}" style="text-align: right;"/>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                单据日期
            </div>
            <div class="addOrder-list_ft">
                <fmt:formatDate value="${sobill.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                备注
            </div>
            <div class="addOrder-list_ft">
                <input placeholder="请输入备注" type="text" id="remarks" name="remarks" class="weui-input"
                       style="text-align: right;" value="${sobill.remarks}"/>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                包装
            </div>
            <div class="addOrder-list_ft">
                <input class="weui-input" id="remark01Select" readonly type="text" placeholder="请选择包装"
                       style="text-align: right;"/>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                镜面抛
            </div>
            <div class="addOrder-list_ft">
                <input class="weui-input" id="remark02Select" readonly type="text" placeholder="请选择镜面抛"
                       style="text-align: right;"/>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                胶水
            </div>
            <div class="addOrder-list_ft">
                <input class="weui-input" id="remark03Select" readonly type="text" placeholder="请选择胶水"
                       style="text-align: right;"/>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                标识要求
            </div>
            <div class="addOrder-list_ft">
                <input class="weui-input" id="remark04Select" readonly type="text" placeholder="请选择标识要求"
                       style="text-align: right;"/>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                喷码
            </div>
            <div class="addOrder-list_ft">
                <input class="weui-input" id="remark05Select" readonly type="text" placeholder="请选择喷码"
                       style="text-align: right;"/>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                客户验货
            </div>
            <div class="addOrder-list_ft">
                <input class="weui-input" id="remark06Select" readonly type="text" placeholder="请选择客户验货"
                       style="text-align: right;"/>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                跟柜物品
            </div>
            <div class="addOrder-list_ft">
                <input class="weui-input" id="remark07Select" readonly type="text" placeholder="请选择跟柜物品"
                       style="text-align: right;"/>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                物流
            </div>
            <div class="addOrder-list_ft">
                <input class="weui-input" id="remark08Select" readonly type="text" placeholder="请选择物流"
                       style="text-align: right;"/>
            </div>
        </div>
        <div class="addOrder-list big-list">
            <textarea class="addOrder-textarea" name="remark09" id="remark09" cols="30" rows="10" placeholder="其他特殊要求">${sobill.remark09}</textarea>
        </div>
        <div class="addOrder-list big-list">
            <textarea class="addOrder-textarea" name="remark10" id="remark10" cols="30" rows="10" placeholder="付款计划">${sobill.remark10}</textarea>
        </div>
    </div>

    <%-- 客户选择 --%>
    <div id="customer" class="weui-popup__container" style="z-index: 501;"><%-- 覆盖底部导航 --%>
        <div class="weui-popup__overlay"></div>
        <div class="weui-popup__modal" style="overflow: hidden;">
            <div class="weui-cells__title"><span style="font-size: 14px;">选择客户</span></div>
            <!-- 搜索框 -->
            <div class="weui-search-bar" id="customerSearchBar" style="height: 5%;">
                <div v-on:click="openCustomer" class="weui-search-bar__form">
                    <div class="weui-search-bar__box">
                        <i class="weui-icon-search"></i>
                        <input v-on:change="searchCustomer($event.currentTarget.value)" type="search" class="weui-search-bar__input" id="customerSearchInput" placeholder="搜索" required="">
                        <a v-on:click="emptyCustomer" class="weui-icon-clear" id="customerSearchClear"></a>
                    </div>
                    <label class="weui-search-bar__label" id="customerSearchText">
                        <i class="weui-icon-search"></i>
                        <span>搜索</span>
                    </label>
                </div>
                <a v-on:click="closeCustomer" class="weui-search-bar__cancel-btn" id="customerSearchCancel">取消</a>
            </div>

            <div id="customerList" class="weui-cells weui-cells_radio">
                <label :id="generateCustomerLabel(customer.id)" name="customerLabel" class="weui-cell weui-check__label" :for="customer.id" v-for="customer in customerList" style="overflow: hidden;">
                    <div class="weui-cell__bd">
                        <p>{{customer.name}}</p>
                    </div>
                    <div class="weui-cell__ft">
                        <input type="radio" class="weui-check" name="customer" :id="customer.id" :value="customer.id" v-on:click="selectCustomer(customer.id)"/>
                        <span class="weui-icon-checked"></span>
                    </div>
                </label>
            </div>
            <div class="btn-cell" style="z-index: 502;">
                <button v-on:click="saveCus" class="add-btn close-popup" data-target="#customer"><img src="${ctxStatic}/image/wechat/icon-add_white.png">{{saveCustomer}}</button>
                <button v-on:click="cancelCus" class="back-btn close-popup" data-target="#customer"><img src="${ctxStatic}/image/wechat/icon-back_white.png">{{cancelCustomer}}</button>
            </div>
        </div>
    </div>

    <%-- 跟单员选择 --%>
    <div id="follower" class="weui-popup__container" style="z-index: 501;"><%-- 覆盖底部导航 --%>
        <div class="weui-popup__overlay"></div>
        <div class="weui-popup__modal" style="overflow: hidden;">
            <div class="weui-cells__title"><span style="font-size: 14px;">选择跟单员</span></div>
            <!-- 搜索框 -->
            <div class="weui-search-bar" id="searchBar" style="height: 5%;">
                <div v-on:click="openFollower" class="weui-search-bar__form">
                    <div class="weui-search-bar__box">
                        <i class="weui-icon-search"></i>
                        <input v-on:change="searchFollower($event.currentTarget.value)" type="search" class="weui-search-bar__input" id="searchInput" placeholder="搜索" required="">
                        <a v-on:click="emptyFollower" class="weui-icon-clear" id="searchClear"></a>
                    </div>
                    <label class="weui-search-bar__label" id="searchText">
                        <i class="weui-icon-search"></i>
                        <span>搜索</span>
                    </label>
                </div>
                <a v-on:click="closeFollower" class="weui-search-bar__cancel-btn" id="searchCancel">取消</a>
            </div>

            <div id="followerList" class="weui-cells weui-cells_radio">
                <label class="weui-cell weui-check__label" :id="generateFollowerLabel(follower.id)" name="followerLabel" :for="follower.id" v-for="follower in followerList" style="overflow: hidden;">
                    <div class="weui-cell__bd">
                        <p>{{follower.name}}</p>
                    </div>
                    <div class="weui-cell__ft">
                        <input type="radio" class="weui-check" name="follower" :id="follower.id" :value="follower.id" v-on:click="selectFollower(follower.id)"/>
                        <span class="weui-icon-checked"></span>
                    </div>
                </label>
            </div>
            <div class="btn-cell" style="z-index: 502;">
                <button v-on:click="saveFol" class="add-btn close-popup" data-target="#follower"><img src="${ctxStatic}/image/wechat/icon-add_white.png">{{saveFollower}}</button>
                <button v-on:click="cancelFol" class="back-btn close-popup" data-target="#follower"><img src="${ctxStatic}/image/wechat/icon-back_white.png">{{cancelFollower}}</button>
            </div>
        </div>
    </div>

    <%-- 表体 --%>
    <div id="detail" style="background: white;">
        <c:forEach items="${sobill.sobillentryList}" var="var" varStatus="vs">
            <div id="${var.itemId}Detail" class="weui-cells_checkbox">
                <div class="pro-cell">
                    <div class="pro-list">
                        <div class="pro-item_left"><span>商品编码：</span> ${var.number}</div>
                        <label class="weui-check__label" for="${var.itemId}Select">
                            <input id="${var.itemId}Select" name="selectItems" type="checkbox" class="weui-check"
                                   value="${var.itemId}"/>
                            <i class="weui-icon-checked"></i>
                        </label>
                    </div>
                    <div class="pro-list">
                        <div class="pro-item_left"><span>商品名称：</span> ${var.itemName}</div>
                        <div class="pro-item_right"><span>单位： </span> ${var.unit}</div>
                    </div>
                    <div class="pro-list">
                        <div class="pro-item_left"><span>规格型号：</span> ${var.model}</div>
                        <div class="pro-item_right"><span>单价： </span> <span class="price"><%--<fmt:formatNumber value="${var.FPRICE}" pattern=".00" /></span>--%>元
                        </div>
                    </div>
                    <div class="pro-list">
                        <div class="pro-item_left"><span><span style="color: red;">*</span>数量：</span>
                            <input type="number" onchange="verificationNum('${var.itemId}Qty')" id="${var.itemId}Qty"
                                   name="quantity" min="0" step="1" value="${var.auxqty}" class="weui-input"
                                   placeholder="请输入数量"/>
                        </div>
                        <div class="pro-item_right"><span>金额： </span> <span class="total"><fmt:formatNumber
                                value="${var.amount}" pattern=".00"/></span>元
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <div id="items" class="weui-popup__container" style="z-index: 501;"><%-- 覆盖底部导航 --%>
        <div class="weui-popup__overlay"></div>
        <div class="weui-popup__modal">
            <div class="all">
                <!-- 滑动门 -->
                <div class="bg">
                    <ul>
                        <li :id="itemClass.id" v-for="(itemClass,index) in icitemClassList">
                            <a v-on:click="screenClass(itemClass.id,index)">{{itemClass.name}}</a>
                            <div class="div2">
                                <div class="banner" :id="index">

                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
                <div class="btn-cell">
                    <button v-on:click="submitItems" class="add-btn close-popup" data-target="#items"><img src="${ctxStatic}/image/wechat/icon-add_white.png">{{saveItems}}</button>
                    <button v-on:click="cancelItems" class="back-btn close-popup" data-target="#items"><img src="${ctxStatic}/image/wechat/icon-back_white.png">{{cancel}}</button>
                </div>
            </div>
        </div>
    </div>

    <br><br><br>

    <div id="footer" class="weui-tabbar" style="position:fixed;bottom: 0px;z-index: 500;">
        <a onclick="cleanSelect();" class="weui-tabbar__item open-popup" data-target="#items">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/add.jpg" alt="">
            </div>
            <p class="weui-tabbar__label">{{add}}</p>
        </a>
        <a v-on:click="delItems" class="weui-tabbar__item open-popup">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/delect.jpg" alt="">
            </div>
            <p class="weui-tabbar__label">{{del}}</p>
        </a>
        <a v-on:click="saveSob" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/save.jpg" alt="">
            </div>
            <p class="weui-tabbar__label">{{save}}</p>
        </a>
        <a v-on:click="submitSob" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/save.jpg" alt="">
            </div>
            <p class="weui-tabbar__label">{{submit}}</p>
        </a>
    </div>
</div>
<script src="https://cdn.bootcss.com/jquery/1.11.0/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/jquery-weui/1.2.1/js/jquery-weui.min.js"></script>
<script type="text/javascript">

    var itemIds = new Array();
    var itemIdsStr = '${itemIdsStr}';
    itemIds = itemIdsStr.split(",");

    var vm = new Vue({
        el: '#page',
        created: function () {
            this.$http.get('${ctxf}/wechat/icitem/getItemClass', {}).then(function (res) {
                if (res.body.errorCode === "403") {
                    $.toast(res.body.msg, "forbidden");
                } else {
                    this.icitemClassList = res.data.body.icitemClassList;
                }
            });
            this.$http.get('${ctxf}/wechat/customer/getCustomerListByEmpId?emplId=${sobill.emplId}', {}).then(function (res) {
                if (res.body.errorCode === "403") {
                    $.toast(res.body.msg, "forbidden");
                } else {
                    this.customerList = res.body.body.customerList;
                }
            });
            this.$http.get('${ctxf}/wechat/user/getUserList', null).then(function (res) {
                if (res.body.errorCode === "403") {
                    $.toast(res.body.msg, "forbidden");
                } else {
                    this.followerList = res.body.body.userList;
                }
            });
            /* 订单类型 */
            this.$http.get('${ctxf}/wechat/sys/dict/listData?type=sobill_type', {}).then(function (res) {
                var dictList = res.body.rows;
                for (var i = 0; i < dictList.length; i++) {
                    if (dictList[i].value == '${sobill.type}') {
                        $("#typeSelect").val(dictList[i].label);
                    }
                }
                var data = [];
                for (var i = 0; i < dictList.length; i++) {
                    var info = {"title": dictList[i].label, "value": dictList[i].value};
                    data.push(info);
                }

                $("#typeSelect").select({
                    title: "选择类型",
                    items: data,
                    onChange: function (result) {//选中触发事件
                        $("#type").val(result.values);
                    }
                });
            });
            /* 包装 */
            this.$http.get('${ctxf}/wechat/sys/dict/listData?type=sobill_remark01', {}).then(function (res) {
                var dictList = res.body.rows;
                for (var i = 0; i < dictList.length; i++) {
                    if (dictList[i].value == '${sobill.remark01}') {
                        $("#remark01Select").val(dictList[i].label);
                    }
                }
                var data = [];
                for (var i = 0; i < dictList.length; i++) {
                    var info = {"title": dictList[i].label, "value": dictList[i].value};
                    data.push(info);
                }
                $("#remark01Select").select({
                    title: "选择包装",
                    items: data,
                    onChange: function (result) {//选中触发事件
                        $("#remark01").val(result.values);
                    }
                });
            });
            /* 	镜面抛 */
            this.$http.get('${ctxf}/wechat/sys/dict/listData?type=sobill_remark02', {}).then(function (res) {
                var dictList = res.body.rows;
                for (var i = 0; i < dictList.length; i++) {
                    if (dictList[i].value == '${sobill.remark02}') {
                        $("#remark02Select").val(dictList[i].label);
                    }
                }
                var data = [];
                for (var i = 0; i < dictList.length; i++) {
                    var info = {"title": dictList[i].label, "value": dictList[i].value};
                    data.push(info);
                }
                $("#remark02Select").select({
                    title: "选择镜面抛",
                    items: data,
                    onChange: function (result) {//选中触发事件
                        $("#remark02").val(result.values);
                    }
                });
            });
            /* 	胶水 */
            this.$http.get('${ctxf}/wechat/sys/dict/listData?type=sobill_remark03', {}).then(function (res) {
                var dictList = res.body.rows;
                for (var i = 0; i < dictList.length; i++) {
                    if (dictList[i].value == '${sobill.remark03}') {
                        $("#remark03Select").val(dictList[i].label);
                    }
                }
                var data = [];
                for (var i = 0; i < dictList.length; i++) {
                    var info = {"title": dictList[i].label, "value": dictList[i].value};
                    data.push(info);
                }
                $("#remark03Select").select({
                    title: "选择胶水",
                    items: data,
                    onChange: function (result) {//选中触发事件
                        $("#remark03").val(result.values);
                    }
                });
            });
            /* 标识要求 */
            this.$http.get('${ctxf}/wechat/sys/dict/listData?type=sobill_remark04', {}).then(function (res) {
                var dictList = res.body.rows;
                for (var i = 0; i < dictList.length; i++) {
                    if (dictList[i].value == '${sobill.remark04}') {
                        $("#remark04Select").val(dictList[i].label);
                    }
                }
                var data = [];
                for (var i = 0; i < dictList.length; i++) {
                    var info = {"title": dictList[i].label, "value": dictList[i].value};
                    data.push(info);
                }
                $("#remark04Select").select({
                    title: "选择标识要求",
                    items: data,
                    onChange: function (result) {//选中触发事件
                        $("#remark04").val(result.values);
                    }
                });
            });
            /* 	喷码 */
            this.$http.get('${ctxf}/wechat/sys/dict/listData?type=sobill_remark05', {}).then(function (res) {
                var dictList = res.body.rows;
                for (var i = 0; i < dictList.length; i++) {
                    if (dictList[i].value == '${sobill.remark05}') {
                        $("#remark05Select").val(dictList[i].label);
                    }
                }
                var data = [];
                for (var i = 0; i < dictList.length; i++) {
                    var info = {"title": dictList[i].label, "value": dictList[i].value};
                    data.push(info);
                }
                $("#remark05Select").select({
                    title: "选择喷码",
                    items: data,
                    onChange: function (result) {//选中触发事件
                        $("#remark05").val(result.values);
                    }
                });
            });
            /* 客户验货 */
            this.$http.get('${ctxf}/wechat/sys/dict/listData?type=sobill_remark06', {}).then(function (res) {
                var dictList = res.body.rows;
                for (var i = 0; i < dictList.length; i++) {
                    if (dictList[i].value == '${sobill.remark06}') {
                        $("#remark06Select").val(dictList[i].label);
                    }
                }
                var data = [];
                for (var i = 0; i < dictList.length; i++) {
                    var info = {"title": dictList[i].label, "value": dictList[i].value};
                    data.push(info);
                }
                $("#remark06Select").select({
                    title: "选择客户验货",
                    items: data,
                    onChange: function (result) {//选中触发事件
                        $("#remark06").val(result.values);
                    }
                });
            });
            /* 	跟柜物品 */
            this.$http.get('${ctxf}/wechat/sys/dict/listData?type=sobill_remark07', {}).then(function (res) {
                var dictList = res.body.rows;
                for (var i = 0; i < dictList.length; i++) {
                    if (dictList[i].value == '${sobill.remark07}') {
                        $("#remark07Select").val(dictList[i].label);
                    }
                }
                var data = [];
                for (var i = 0; i < dictList.length; i++) {
                    var info = {"title": dictList[i].label, "value": dictList[i].value};
                    data.push(info);
                }
                $("#remark07Select").select({
                    title: "选择跟柜物品",
                    items: data,
                    onChange: function (result) {//选中触发事件
                        $("#remark07").val(result.values);
                    }
                });
            });
            /* 	物流 */
            this.$http.get('${ctxf}/wechat/sys/dict/listData?type=sobill_remark08', {}).then(function (res) {
                var dictList = res.body.rows;
                for (var i = 0; i < dictList.length; i++) {
                    if (dictList[i].value == '${sobill.remark08}') {
                        $("#remark08Select").val(dictList[i].label);
                    }
                }
                var data = [];
                for (var i = 0; i < dictList.length; i++) {
                    var info = {"title": dictList[i].label, "value": dictList[i].value};
                    data.push(info);
                }
                $("#remark08Select").select({
                    title: "选择物流",
                    items: data,
                    onChange: function (result) {//选中触发事件
                        $("#remark08").val(result.values);
                    }
                });
            });
        },
        data: {
            itemDetail: '商品明细',
            save: '保存',
            submit: '提交',
            add: '选择商品',
            del: '删除商品',
            saveItems: '保存',
            cancel: '取消',
            saveCustomer:'提交',
            cancelCustomer:'取消',
            saveFollower:'提交',
            cancelFollower:'取消',
            icitemClassList: [],
            customerList: [],
            followerList: [],
            remark01:[],
            remark02:[],
            remark03:[],
            remark04:[],
            remark05:[],
            remark06:[],
            remark07:[],
            remark08:[]
        },
        methods: {
            closeCustomer() {
                $("#customerSearchBar").removeClass("weui-search-bar_focusing");
            },
            openCustomer() {
                $("#customerSearchBar").addClass("weui-search-bar_focusing");
            },
            emptyCustomer() {
                $("#customerSearchInput").val('');
            },
            closeFollower() {
                $("#followerSearchBar").removeClass("weui-search-bar_focusing");
            },
            openFollower() {
                $("#followerSearchBar").addClass("weui-search-bar_focusing");
            },
            emptyFollower() {
                $("#followerSearchInput").val('');
            },
            /* 提交商品 */
            submitItems() {
                var checkVals = [];
                var addVals = [];
                $("input[name='items']:checked").each(function () {
                    checkVals.push($(this).val());
                });
                var index;
                for (var i = 0; i < checkVals.length; i++) {
                    index = retrieveArrayIndex(checkVals[i]);
                    if (index == -1) {
                        itemIds.push(checkVals[i]);
                        addVals.push(checkVals[i]);
                    }
                }
                $.ajax({
                    async: false,
                    cache: false,
                    url: '${ctxf}/wechat/icitem/findItemListByIds',
                    data: {
                        idsStr: addVals.toString()
                    },
                    dataType: 'json',
                    success: function (res) {
                        if (res.body.errorCode === "403") {
                            $.toast(res.body.msg, "forbidden");
                        } else {
                            var template = '';
                            var icitemList = res.body.icitemList;
                            for (var i = 0; i < icitemList.length; i++) {
                                template += '<div id="' + icitemList[i].id + 'Detail" class="weui-cells_checkbox">' +
                                    '<div class="pro-cell">' +
                                    '<div class="pro-list">' +
                                    '<div class="pro-item_left">' +
                                    '<span>商品编号：</span>  ' + icitemList[i].number +
                                    '</div>' +
                                    '<label class="weui-check__label" for="' + icitemList[i].id + 'Select">' +
                                    '<div class="weui-cell__hd">' +
                                    '<input id="' + icitemList[i].id + 'Select" name="selectItems" type="checkbox" class="weui-check" value="' + icitemList[i].id + '"/>' +
                                    '<i class="weui-icon-checked"></i>' +
                                    '</div>' +
                                    '</label>' +
                                    '</div>' +
                                    '<div class="pro-list">' +
                                    '<div class="pro-item_left">' +
                                    '<span>商品名称：</span>  ' + icitemList[i].name +
                                    '</div>' +
                                    '<div class="pro-item_right">' +
                                    '<span>单位：</span>  ' + icitemList[i].unit +
                                    '</div>' +
                                    '</div>' +
                                    '<div class="pro-list">' +
                                    '<div class="pro-item_left">' +
                                    '<span>规格型号：</span>  ' + icitemList[i].model +
                                    '</div>' +
                                    '<div class="pro-item_right">' +
                                    '<span>单价：</span>  ' +
                                    '</div>' +
                                    '</div>' +
                                    '<div class="pro-list">' +
                                    '<div class="pro-item_left">' +
                                    '<span><span style="color: red;">*</span>数量：</span><input type="number" onchange="verificationNum(\'' + icitemList[i].id + 'Qty\')" id="' + icitemList[i].id + 'Qty" name="quantity" min="0" step="1" placeholder="请输入数量" class="weui-input"/>' +
                                    '</div>' +
                                    '<div class="pro-item_right">' +
                                    '<span>金额：</span>  <span class="total"></span>元' +
                                    '</div>' +
                                    '</div>' +
                                    '</div>' +
                                    '</div>';
                            }
                            $("#detail").append(template);
                        }
                    }
                });
            },
            /* 选择分类 */
            screenClass(classId, index) {
                $("li:not(" + classId + ")").children('div').removeClass('play');
                $("#" + classId).children('div').addClass('play');
                $.ajax({
                    async: false,
                    cache: false,
                    url: '${ctxf}/wechat/icitem/getItemsListByClassId',
                    data: {
                        classId: classId
                    },
                    dataType: 'json',
                    success: function (res) {
                        if (res.body.errorCode === "403") {
                            $.toast(res.body.msg, "forbidden");
                        } else {
                            var icitemList = res.body.icitemList;
                            var template = '<div class="weui-cells weui-cells_checkbox">';
                            $("#" + index).empty();
                            for (var i = 0; i < icitemList.length; i++) {
                                var check = false;
                                for (var j = 0; j < itemIds.length; j++) {
                                    if (itemIds[j] == icitemList[i].id) {
                                        check = true;
                                    }
                                }
                                template += '<div class="pro-cell">' +
                                    '<div class="pro-list" style="padding: 0px;font-size: 12px;">' +
                                    '<label class="weui-cell weui-check__label" for="' + icitemList[i].id + '" style="padding:0%;margin-bottom: 2%;">';
                                if (check) {
                                    template += '<input id="' + icitemList[i].id + '" v-on:click="selectItems(\'' + icitemList[i].id + '\');" type="checkbox" checked class="weui-check" name="items" value="' + icitemList[i].id + '"/>';
                                } else {
                                    template += '<input id="' + icitemList[i].id + '" v-on:click="selectItems(\'' + icitemList[i].id + '\');" type="checkbox" class="weui-check" name="items" value="' + icitemList[i].id + '"/>';
                                }
                                template += '<i class="weui-icon-checked"></i>' +
                                    '<div class="weui-cell__bd">' +
                                    '<p>编码:' + icitemList[i].number + '</p>' +
                                    '<p>名称:' + icitemList[i].name + '</p>' +
                                    '<p>单位:' + icitemList[i].unit + '</p>' +
                                    '<p>型号:' + icitemList[i].model + '</p>' +
                                    '<p>单价:</p>' +
                                    '</div>' +
                                    '</label>' +
                                    '</div>' +
                                    '</div>';
                            }
                            template += '</div>';
                            template += '</div>';
                            $("#" + index).append(template);
                        }
                    }
                });
            },
            /* 删除商品 */
            delItems: function () {
                var checkVals = [];
                $("input[name='selectItems']:checked").each(function () {
                    checkVals.push($(this).val());
                });
                if (checkVals.length == 0) {
                    $.alert("请选择要删除的商品!");
                    return;
                }
                $.confirm("您确定要删除选中商品吗?", "提醒", function () {
                    /* 清理选择 */
                    cleanSelect();
                    var index;
                    for (var i = 0; i < checkVals.length; i++) {
                        index = retrieveArrayIndex(checkVals[i]);
                        if (index != -1) {
                            itemIds.splice(index, 1);
                            $("#" + checkVals[i] + "Detail").remove();
                        }
                    }
                }, function () {

                });
            },
            /* 选中商品 */
            selectItems: function () {
                var index;
                if (itemIds.indexOf(id) != -1) {
                    if (!$("#" + id).prop("checked")) {
                        index = retrieveArrayIndex(id);
                        if (index != -1) {
                            itemIds.splice(index, 1);
                        }
                    }
                } else {
                    if ($("#" + id).prop("checked")) {
                        itemIds.push(id);
                    }
                }
            },
            /* 保存订单 */
            saveSob: function () {
                $.confirm("您确定要保存订单吗?", function () {
                    //点击确认后的回调函数
                    save(0);
                }, function () {
                    //点击取消后的回调函数
                });
            },
            submitSob: function () {
                $.confirm("您确定要提交订单吗?", function () {
                    //点击确认后的回调函数
                    save(1);
                }, function () {
                    //点击取消后的回调函数
                });
            },
            cancelItems: function () {

            },
            saveCus: function () {
                var id = $("input[name='customer']:checked").val();
                $("#custId").val(id);
                $.ajax({
                    async:false,
                    cache:false,
                    url:'${ctxf}/wechat/customer/getCustomerById',
                    type:'post',
                    data:{
                        id:id
                    },
                    dataType:'json',
                    success:function (res) {
                        if (res.body.errorCode === "403") {
                            $.toast(res.body.msg, "forbidden");
                        } else {
                            $("#cusName").val(res.body.customer.name);
                        }
                    }
                });
            },
            cancelCus: function () {
            },
            searchCustomer:function (val) {
                $.ajax({
                    async:false,
                    cache:false,
                    url:'${ctxf}/wechat/customer/getCustomerListByEmpId',
                    type:'post',
                    data:{
                        name:val
                    },
                    dataType:'json',
                    success:function (res) {
                        $("#customerList").empty();
                        if (res.body.errorCode === "403") {
                            $.toast(res.body.msg, "forbidden");
                        } else {
                            var templet = '';
                            var customerList = res.body.customerList;
                            for (var i = 0; i < customerList.length; i++) {
                                templet += '<label class="weui-cell weui-check__label" id="customer' + customerList[i].id + '" name="customerLabel" for="' + customerList[i].id + '">' +
                                    '<div class="weui-cell__bd">' +
                                    '<p>' + customerList[i].name + '</p>' +
                                    '</div>' +
                                    '<div class="weui-cell__ft">' +
                                    '<input type="radio" class="weui-check" onclick="selectCustomer(\'' + customerList[i].id + '\')" name="customer" id="' + customerList[i].id + '"  value="' + customerList[i].id + '">' +
                                    '<span class="weui-icon-checked"></span>' +
                                    '</div>' +
                                    '</label>';
                            }
                            $("#customerList").append(templet);
                        }
                    }
                });
            },
            searchFollower:function (val) {
                $.ajax({
                    async:false,
                    cache:false,
                    url:'${ctxf}/wechat/user/getUserList',
                    type:'post',
                    data:{
                        name:val
                    },
                    dataType:'json',
                    success:function (res) {
                        $("#followerList").empty();
                        if (res.body.errorCode === "403") {
                            $.toast(res.body.msg, "forbidden");
                        } else {
                            var templet = '';
                            var followerList = res.body.userList;
                            for (var i = 0; i < followerList.length; i++) {
                                templet += '<label class="weui-cell weui-check__label" id="follower' + followerList[i].id + '" name="followerLabel" for="' + followerList[i].id + '">' +
                                    '<div class="weui-cell__bd">' +
                                    '<p>' + followerList[i].name + '</p>' +
                                    '</div>' +
                                    '<div class="weui-cell__ft">' +
                                    '<input type="radio" class="weui-check"  onclick="selectFollower(\'' + followerList[i].id + '\')" name="follower" id="' + followerList[i].id + '" value="' + followerList[i].id + '">' +
                                    '<span class="weui-icon-checked"></span>' +
                                    '</div>' +
                                    '</label>';
                            }
                            $("#followerList").append(templet);
                        }
                    }
                });
            },
            saveFol: function () {
                var id = $("input[name='follower']:checked").val();
                $("#followerId").val(id);
                $.ajax({
                    async:false,
                    cache:false,
                    url:'${ctxf}/wechat/user/getUserById',
                    type:'post',
                    data:{
                        id:id
                    },
                    dataType:'json',
                    success:function (res) {
                        if (res.body.errorCode === "403") {
                            $.toast(res.body.msg, "forbidden");
                        } else {
                            $("#followerName").val(res.body.user.name);
                        }
                    }
                });
            },
            cancelFol: function () {
            },
            generateCustomerLabel: function (val) {
                return 'customer'+val;
            },
            generateFollowerLabel: function (val) {
                return 'follower'+val;
            },
            selectCustomer: function (val) {
                $("[name='customerLabel']").css('background','');
                $("#customer"+val).css('background','radial-gradient(#58b0fb, transparent)');
            },
            selectFollower: function (val) {
                $("[name='followerLabel']").css('background','');
                $("#follower"+val).css('background','radial-gradient(#58b0fb, transparent)');
            }
        }
    });

    $("#needTime").calendar({
        dateFormat: "yyyy-mm-dd"
    });

    /* 检索数组元素下标 */
    function retrieveArrayIndex(val) {
        for (var i = 0; i < itemIds.length; i++) {
            if (itemIds[i] == val) {
                return i;
            }
        }
        return -1;
    }

    /* 验证数量 */
    function verificationNum(id) {
        var value = $("#" + id).val();
        if (value <= 0) {
            $.alert("请输入合法数字!");
            $("#" + id).val('');
        }
    }

    /* 保存订单 type(0:保存草稿,1:提交) */
    function save(status) {
        var id = $("#id").val();
        var custId = $("#custId").val();
        var followerId = $("#followerId").val();
        var needTime = $("#needTime").val();
        var type = $("#type").val();
        var remarks = $("#remarks").val();
        var remark01 = $("#remark01").val();
        var remark02 = $("#remark02").val();
        var remark03 = $("#remark03").val();
        var remark04 = $("#remark04").val();
        var remark05 = $("#remark05").val();
        var remark06 = $("#remark06").val();
        var remark07 = $("#remark07").val();
        var remark08 = $("#remark08").val();
        var remark09 = $("#remark09").val();
        var remark10 = $("#remark10").val();
        if (custId == null || custId == '') {
            $.alert("请选择客户!");
            return;
        }
        if (type == null || type == '') {
            $.alert("请选择类型!");
            return;
        }
        if (needTime == null || needTime == '') {
            $.alert("请选择发货日期!");
            return;
        }
        if (itemIds.length == 0) {
            $.alert("请至少选择一个商品!");
            return;
        }
        var check = true;
        var json = '[';
        for (var i = 0; i < itemIds.length; i++) {
            if ($("#" + itemIds[i] + "Qty").val() == null || $("#" + itemIds[i] + "Qty").val() == '') {
                check = false;
            }
            json += '{"itemId":"' + itemIds[i] + '","auxqty":' + $("#" + itemIds[i] + "Qty").val() + "},";
        }
        json = json.substring(0, json.length - 1);
        json += ']';
        if (!check) {
            $.alert("请检查订单明细是否填写完整!");
            return
        }
        $.showLoading("数据加载中");
        var data = {
            "id": id,
            "custId": custId,
            "status": status,
            "needTime": needTime,
            "type": type,
            "followerId":followerId,
            "remarks":remarks,
            "remark01":remark01,
            "remark02":remark02,
            "remark03":remark03,
            "remark04":remark04,
            "remark05":remark05,
            "remark06":remark06,
            "remark07":remark07,
            "remark08":remark08,
            "remark09":remark09,
            "remark10":remark10,
            "sobillentryList": json
        };
        $.ajax({
            async: false,
            cache: false,
            type: 'post',
            url: '${ctxf}/wechat/sobill/saveSob',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data),
            processData: false,
            dataType: 'json',
            success: function (res) {
                if (res.body.errorCode === "403") {
                    $.toast(res.body.msg, "forbidden");
                } else {
                    setTimeout(function () {
                        $.hideLoading();
                        $.toast(res.body.msg);
                        window.location.href=document.referrer;
                    }, 3000);
                }
            },
            error: function () {
                $.toast("保存出错!");
            }
        });
    }

    /* 选择客户切换背景色 */
    function selectCustomer(val) {
        $("[name='customerLabel']").css('background','');
        $("#customer"+val).css('background','radial-gradient(#58b0fb, transparent)');
    }

    /* 选择跟单员切换背景色 */
    function selectFollower(val) {
        $("[name='followerLabel']").css('background','');
        $("#follower"+val).css('background','radial-gradient(#58b0fb, transparent)');
    }

    /* 清理选择 */
    function cleanSelect() {
        for (var i = 0; i < itemIds.length; i++) {
            $("#" + itemIds[i]).attr("checked", false);
        }
    }

</script>
</body>
</html>
