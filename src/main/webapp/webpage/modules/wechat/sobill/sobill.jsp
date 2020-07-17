<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no,viewport-fit=cover">
    <meta charset="UTF-8">
    <title>订单管理</title>
    <link rel="stylesheet" href="${ctxStatic}/css/wxqy/orderList.css">
    <link rel="stylesheet" href="${ctxStatic}/css/weui.min.css">
    <link rel="stylesheet" href="${ctxStatic}/css/jquery-weui.min.css">
    <%-- 全面屏iphone适配 --%>
    <link rel="stylesheet" href="${ctxStatic}/css/wxqy/iphoneadaptation/iphoneadaptation.css">
    <script src="${ctxStatic}/common/vue/js/vue.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue-resource.min.js"></script>
    <style type="text/css">
        body {
            overflow: auto;
        }
    </style>
</head>
<body>
<div id="page" class="page">
    <div style="background: white;position: fixed;top: 0px;z-index: 10000;width: 100%;height: 5%;background: #f6f6f6;">
        <div style="float: left; width: 90%;">
            <input type="text" id="startTime" readonly class="weui-input"
                   placeholder="请选择开始时间" style="text-align: center;width:47%;"/>
            至
            <input type="text" id="endTime" readonly class="weui-input"
                   placeholder="请选择结束时间" style="text-align: center;width:45%;"/>
        </div>
        <div style="float: right;width: 10%;">
            <a v-on:click="filterDate">
                <img src="${ctxStatic}/image/wechat/icon-search_gray.png">
            </a>
        </div>
    </div>
    <div class="page__bd">
        <div class="weui-tab">
            <div class="weui-navbar" style="position: fixed;top: 4%;z-index: 10000;">
                <div id="inProgress" class="weui-navbar__item weui-bar__item_on" onclick="changeStyle('inProgress');">
                    执行中
                </div>
                <div id="toAudit" class="weui-navbar__item" onclick="changeStyle('toAudit');">
                    待审核
                </div>
                <div id="pending" class="weui-navbar__item" onclick="changeStyle('pending');">
                    待提交
                </div>
                <div id="closed" class="weui-navbar__item" onclick="changeStyle('closed');">
                    已关闭
                </div>
            </div>

            <%-- 执行中 --%>
            <div id="inProgressDetail" style="margin-top: 20%;">
                <div class="order-cell inProgressCard" :id="'inProgressCard'+inProgress.id" @click="selectCard(1,inProgress.id)" v-for="inProgress in inProgressList">
                    <div class="weui-cells_radio">
                        <label class="weui-cell weui-check__label" :for="'inProgress'+inProgress.id">
                            <div class="order-cell_left">
                                <div class="order-list">
                                    <div class="order-list_item"><span>客户：</span> {{inProgress.cusName}}</div>
                                    <input type="checkbox" class="weui-check" name="inProgress"
                                           :id="'inProgress'+inProgress.id"
                                           :value="inProgress.id"/>
                                    <span class="weui-icon-checked"></span>
                                </div>
                                <div class="order-cell_bg">
                                    <div class="order-list">
                                        <div class="order-list_item"><span>编号：</span> {{inProgress.billNo}}</div>
                                    </div>
                                    <div class="order-list">
                                        <div class="order-list_item"><span>跟单：</span> {{inProgress.followerName}}</div>
                                        <div class="order-list_item"><span>日期：</span> {{inProgress.needTimeStr}}</div>
                                    </div>
                                    <div class="order-list">
                                        <div class="order-list_item" v-if="inProgress.synStatus === 0"><span>同步：</span><span
                                                style="color: red;">未同步</span>
                                        </div>
                                        <div class="order-list_item" v-else><span>同步：</span><span
                                                style="color: green;">已同步</span></div>
                                        <div class="order-list_item" v-if="inProgress.status === 0">
                                            <span>状态：</span><span style="color: red;">草稿</span>
                                        </div>
                                        <div class="order-list_item" v-else><span>状态：</span><span
                                                style="color: green;">提交</span></div>
                                    </div>
                                </div>
                            </div>
                        </label>
                    </div>
                </div>
                <div id="loadInProgress" class="weui-loadmore">
                    <i class="weui-loading"></i>
                    <span class="weui-loadmore__tips">正在加载...</span>
                </div>

                <div id="endInProgress" class="weui-loadmore weui-loadmore_line weui-loadmore_dot">
                    <span class="weui-loadmore__tips" style="background-color: #F6F6F6;">加载完毕</span>
                </div>
            </div>

            <%-- 待审核 --%>
            <div id="toAuditDetail" style="margin-top: 20%;display: none;">
                <div class="order-cell" v-for="toAudit in toAuditList">
                    <div class="weui-cells_radio">
                        <label class="weui-cell weui-check__label" :for="'toAudit'+toAudit.id">
                            <div class="order-cell_left">
                                <div class="order-list">
                                    <div class="order-list_item"><span>客户：</span> {{toAudit.cusName}}</div>
                                    <input type="checkbox" class="weui-check" name="toAudit"
                                           :id="'toAudit'+toAudit.id"
                                           :value="toAudit.id"/>
                                    <span class="weui-icon-checked"></span>
                                </div>
                                <div class="order-cell_bg">
                                    <div class="order-list">
                                        <div class="order-list_item"><span>编号：</span> {{toAudit.billNo}}</div>
                                    </div>
                                    <div class="order-list">
                                        <div class="order-list_item"><span>跟单：</span> {{toAudit.followerName}}</div>
                                        <div class="order-list_item"><span>日期：</span> {{toAudit.needTimeStr}}</div>
                                    </div>
                                    <div class="order-list">
                                        <div class="order-list_item" v-if="toAudit.synStatus === 0"><span>同步：</span><span
                                                style="color: red;">未同步</span>
                                        </div>
                                        <div class="order-list_item" v-else><span>同步：</span><span
                                                style="color: green;">已同步</span></div>
                                        <div class="order-list_item" v-if="toAudit.status === 0">
                                            <span>状态：</span><span style="color: red;">草稿</span>
                                        </div>
                                        <div class="order-list_item" v-else><span>状态：</span><span
                                                style="color: green;">提交</span></div>
                                    </div>
                                </div>
                            </div>
                        </label>
                    </div>
                </div>

                <div id="loadToAudit" class="weui-loadmore">
                    <i class="weui-loading"></i>
                    <span class="weui-loadmore__tips">正在加载...</span>
                </div>

                <div id="endToAudit" class="weui-loadmore weui-loadmore_line weui-loadmore_dot">
                    <span class="weui-loadmore__tips" style="background-color: #F6F6F6;">加载完毕</span>
                </div>
            </div>

            <%-- 待提交 --%>
            <div id="pendingDetail" style="margin-top: 20%;display: none;">
                <div class="order-cell" v-for="pending in pendingList">
                    <div class="weui-cells_radio">
                        <label class="weui-cell weui-check__label" :for="'pending'+pending.id">
                            <div class="order-cell_left">
                                <div class="order-list">
                                    <div class="order-list_item"><span>客户：</span> {{pending.cusName}}</div>
                                    <input type="checkbox" class="weui-check" name="pending"
                                           :id="'pending'+pending.id"
                                           :value="pending.id"/>
                                    <span class="weui-icon-checked"></span>
                                </div>
                                <div class="order-cell_bg">
                                    <div class="order-list">
                                        <div class="order-list_item"><span>编号：</span> {{pending.billNo}}</div>
                                    </div>
                                    <div class="order-list">
                                        <div class="order-list_item"><span>跟单：</span> {{pending.followerName}}</div>
                                        <div class="order-list_item"><span>日期：</span> {{pending.needTimeStr}}</div>
                                    </div>
                                    <div class="order-list">
                                        <div class="order-list_item" v-if="pending.synStatus === 0"><span>同步：</span><span
                                                style="color: red;">未同步</span>
                                        </div>
                                        <div class="order-list_item" v-else><span>同步：</span><span
                                                style="color: green;">已同步</span></div>
                                        <div class="order-list_item" v-if="pending.status === 0">
                                            <span>状态：</span><span style="color: red;">草稿</span>
                                        </div>
                                        <div class="order-list_item" v-else><span>状态：</span><span
                                                style="color: green;">提交</span></div>
                                    </div>
                                </div>
                            </div>
                        </label>
                    </div>
                </div>
                <div id="loadPending" class="weui-loadmore">
                    <i class="weui-loading"></i>
                    <span class="weui-loadmore__tips">正在加载...</span>
                </div>

                <div id="endPending" class="weui-loadmore weui-loadmore_line weui-loadmore_dot">
                    <span class="weui-loadmore__tips" style="background-color: #F6F6F6;">加载完毕</span>
                </div>
            </div>

            <%-- 已关闭 --%>
            <div id="closedDetail" style="margin-top: 20%;display: none;">
                <div class="order-cell" v-for="closed in closedList">
                    <div class="weui-cells_radio">
                        <label class="weui-cell weui-check__label" :for="'closed'+closed.id">
                            <div class="order-cell_left">
                                <div class="order-list">
                                    <div class="order-list_item"><span>客户：</span> {{closed.cusName}}</div>
                                    <input type="checkbox" class="weui-check" name="closed"
                                           :id="'closed'+closed.id"
                                           :value="closed.id"/>
                                    <span class="weui-icon-checked"></span>
                                </div>
                                <div class="order-cell_bg">
                                    <div class="order-list">
                                        <div class="order-list_item"><span>编号：</span> {{closed.billNo}}</div>
                                    </div>
                                    <div class="order-list">
                                        <div class="order-list_item"><span>跟单：</span> </div>
                                        <div class="order-list_item"><span>日期：</span> {{closed.needTimeStr}}</div>
                                    </div>
                                    <div class="order-list">
                                        <div class="order-list_item" v-if="closed.synStatus === 0"><span>同步：</span><span
                                                style="color: red;">未同步</span>
                                        </div>
                                        <div class="order-list_item" v-else><span>同步：</span><span
                                                style="color: green;">已同步</span></div>
                                        <div class="order-list_item" v-if="closed.status === 0">
                                            <span>状态：</span><span style="color: red;">草稿</span>
                                        </div>
                                        <div class="order-list_item" v-else><span>状态：</span><span
                                                style="color: green;">提交</span></div>
                                    </div>
                                </div>
                            </div>
                        </label>
                    </div>
                </div>
                <div id="loadClosed" class="weui-loadmore">
                    <i class="weui-loading"></i>
                    <span class="weui-loadmore__tips">正在加载...</span>
                </div>

                <div id="endClosed" class="weui-loadmore weui-loadmore_line weui-loadmore_dot">
                    <span class="weui-loadmore__tips" style="background-color: #F6F6F6;">加载完毕</span>
                </div>
            </div>

        </div>
    </div>

    <br><br><br>

    <div id="footer" class="weui-tabbar" style="position:fixed;bottom: 0px;" v-if="errorCode !== '403'">
        <a v-on:click="backHome()" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/home.jpg" alt="">
            </div>
            <p class="weui-tabbar__label">{{ home }}</p>
        </a>
        <a v-bind:href="addHref" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/add.jpg" alt="">
            </div>
            <p class="weui-tabbar__label">{{add}}</p>
        </a>
        <a v-on:click="editHref" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/edit.jpg" alt="">
            </div>
            <p class="weui-tabbar__label">{{edit}}</p>
        </a>
        <a v-on:click="delectById" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/delect.jpg" alt="">
            </div>
            <p class="weui-tabbar__label">{{del}}</p>
        </a>
    </div>
</div>

<script src="https://cdn.bootcss.com/jquery/1.11.0/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/jquery-weui/1.2.1/js/jquery-weui.min.js"></script>

<script type="text/javascript">
    $(function () {
        $("#loadInProgress").hide();
        $("#endInProgress").hide();
        $("#loadToAudit").hide();
        $("#endToAudit").hide();
        $("#loadPending").hide();
        $("#endPending").hide();
        $("#loadClosed").hide();
        $("#endClosed").hide();
    });

    // 滚动加载
    var loadingInProgress = false; //状态标记
    var loadingToAudit = false;
    var loadingPending = false;
    var loadingClosed = false;

    var vm = new Vue({
        el: '#page',
        created: function () {
            // 执行中数据
            this.$http.get('${ctxf}/wechat/sobill/getSobillList', {
                params: {
                    pageNo: 1,
                    pageSize: 10,
                    cancellation: 0,
                    qyUserId: '${qyUserId}'
                }
            }).then(function (res) {
                this.errorCode = res.body.errorCode;
                if (res.body.errorCode === "403") {
                    $.toast(res.body.msg, "forbidden");
                } else {
                    this.inProgressList = res.body.body.sobillList;
                    if (res.body.body.sobillList.length < 10) {
                        loadingInProgress = true;
                        $("#endInProgress").show();
                    }
                }
            });
            // 待审核数据
            this.$http.get('${ctxf}/wechat/sobill/getSobillList', {
                params: {
                    status: 1,
                    checkStatus: 0,
                    cancellation: 0,
                    pageNo: 1,
                    pageSize: 10,
                    qyUserId: '${qyUserId}'
                }
            }).then(function (res) {
                this.errorCode = res.body.errorCode;
                if (res.body.errorCode === "403") {
                    $.toast(res.body.msg, "forbidden");
                } else {
                    this.toAuditList = res.body.body.sobillList;
                    if (res.body.body.sobillList.length < 10) {
                        loadingToAudit = true;
                        $("#endToAudit").show();
                    }
                }
            });
            // 待提交数据
            this.$http.get('${ctxf}/wechat/sobill/getSobillList', {
                params: {
                    status: 0,
                    checkStatus: 2,
                    cancellation: 0,
                    pageNo: 1,
                    pageSize: 10,
                    qyUserId: '${qyUserId}'
                }
            }).then(function (res) {
                this.errorCode = res.body.errorCode;
                if (res.body.errorCode === "403") {
                    $.toast(res.body.msg, "forbidden");
                } else {
                    this.pendingList = res.body.body.sobillList;
                    if (res.body.body.sobillList.length < 10) {
                        loadingPending = true;
                        $("#endPending").show();
                    }
                }
            });
            // 已关闭数据
            this.$http.get('${ctxf}/wechat/sobill/getSobillList', {
                params: {
                    cancellation: 1,
                    pageNo: 1,
                    pageSize: 10,
                    qyUserId: '${qyUserId}'
                }
            }).then(function (res) {
                this.errorCode = res.body.errorCode;
                if (res.body.errorCode === "403") {
                    $.toast(res.body.msg, "forbidden");
                } else {
                    this.closedList = res.body.body.sobillList;
                    if (res.body.body.sobillList.length < 10) {
                        loadingClosed = true;
                        $("#endClosed").show();
                    }
                }
            });
        },
        data: {
            add: '新增',
            addHref: '${ctxf}/wechat/sobill/goAdd?qyUserId=${qyUserId}',
            edit: '编辑',
            del: '删除',
            home:'首页',
            inProgressList: [],
            toAuditList: [],
            pendingList: [],
            closedList: [],
            errorCode: ''
        },
        methods: {
            editHref: function () {
                var ids = [];
                if ($("#inProgress").hasClass("weui-bar__item_on")) {
                    $("input[name='inProgress']:checked").each(function () {
                        ids.push($(this).val());
                    });
                } else if ($("#toAudit").hasClass("weui-bar__item_on")) {
                    $("input[name='toAudit']:checked").each(function () {
                        ids.push($(this).val());
                    });
                } else if ($("#pending").hasClass("weui-bar__item_on")) {
                    $("input[name='pending']:checked").each(function () {
                        ids.push($(this).val());
                    });
                } else if ($("#closed").hasClass("weui-bar__item_on")) {
                    $("input[name='closed']:checked").each(function () {
                        ids.push($(this).val());
                    });
                }
                if (ids.length == 0) {
                    $.alert("请至少选择一条数据!");
                    return;
                }
                if (ids.length > 1) {
                    $.alert("编辑操作仅允许单选!");
                    return;
                }
                var id = ids[0];
                $.ajax({
                    async: false,
                    cache: false,
                    url: '${ctxf}/wechat/sobill/getById',
                    type: 'get',
                    data: {
                        id: id
                    },
                    dataType: 'json',
                    success: function (res) {
                        this.errorCode = res.body.errorCode;
                        if (res.body.errorCode === "403") {
                            $.toast(res.body.msg, "forbidden");
                        } else {
                            var sobill = res.body.sobill;
                            window.location.href = '${ctxf}/wechat/sobill/goEdit?id=' + id;
                        }
                    }
                });
            },
            /* 删除 */
            delectById: function () {
                var ids = [];
                if ($("#inProgress").hasClass("weui-bar__item_on")) {
                    $("input[name='inProgress']:checked").each(function () {
                        ids.push($(this).val());
                    });
                } else if ($("#toAudit").hasClass("weui-bar__item_on")) {
                    $("input[name='toAudit']:checked").each(function () {
                        ids.push($(this).val());
                    });
                } else if ($("#pending").hasClass("weui-bar__item_on")) {
                    $("input[name='pending']:checked").each(function () {
                        ids.push($(this).val());
                    });
                } else if ($("#closed").hasClass("weui-bar__item_on")) {
                    $("input[name='closed']:checked").each(function () {
                        ids.push($(this).val());
                    });
                }
                if (ids.length == 0) {
                    $.alert("请至少选择一条数据!");
                    return;
                }
                var idsStr = ids.toString();
                $.confirm("您确定要删除选中订单吗?", function () {
                    //点击确认后的回调函数
                    $.ajax({
                        async: false,
                        cache: false,
                        url: '${ctxf}/wechat/sobill/delectByIds',
                        data: {
                            idsStr: idsStr
                        },
                        dataType: 'json',
                        success: function (res) {
                            this.errorCode = res.body.errorCode;
                            if (res.body.errorCode === "403") {
                                $.toast(res.body.msg, "forbidden");
                            } else {
                                $.alert(res.body.msg);
                                setTimeout(function () {
                                    window.location.reload();
                                }, 3000);
                            }
                        }
                    });
                }, function () {
                    //点击取消后的回调函数
                });
            },
            filterDate: function () {
                var startTime = $("#startTime").val();
                var endTime = $("#endTime").val();
                if ((startTime == null || startTime == '') && (endTime == null || endTime == '')) {
                    $.alert("请至少选择一个日期!");
                    return;
                }
                var data = {qyUserId: '${qyUserId}',startTime:startTime,endTime:endTime};
                var detail = '';
                var head = '';
                if ($("#inProgress").hasClass("weui-bar__item_on")) {
                    detail = 'inProgressDetail';
                    head = 'inProgress';
                    data.cancellation = 0;
                    loadingInProgress = true;
                } else if ($("#toAudit").hasClass("weui-bar__item_on")) {
                    detail = 'toAuditDetail';
                    head = 'toAudit';
                    data.checkStatus = 0;
                    data.status = 1;
                    data.cancellation = 0;
                    loadingToAudit = true;
                } else if ($("#pending").hasClass("weui-bar__item_on")) {
                    detail = 'pendingDetail';
                    head = 'pending';
                    data.status = 0;
                    data.checkStatus = 2;
                    data.cancellation = 0;
                    loadingPending = true;
                } else if ($("#closed").hasClass("weui-bar__item_on")) {
                    detail = 'closedDetail';
                    head = 'closed';
                    data.cancellation = 1;
                    loadingClosed = true;
                }
                $.ajax({
                    async: false,
                    cache: false,
                    url: '${ctxf}/wechat/sobill/getSobillList',
                    type: 'post',
                    data: data,
                    dataType: 'json',
                    success: function (res) {
                        $("#"+detail).empty();
                        this.errorCode = res.errorCode;
                        if (res.errorCode === "403") {
                            $.toast(res.msg, "forbidden");
                        } else {
                            var sobillList = res.body.sobillList;
                            var template = '';
                            for (var i = 0; i < sobillList.length; i++) {
                                template += '<div class="order-cell">' +
                                    '<div class="weui-cells_radio">' +
                                    '<label class="weui-cell weui-check__label" for="' + head + sobillList[i].id + '">' +
                                    '<div class="order-cell_left">' +
                                    '<div class="order-list">' +
                                    '<div class="order-list_item"><span>客户：</span>  ' + (sobillList[i].cusName != null && sobillList[i].cusName != '' ? sobillList[i].cusName : "") + '</div>' +
                                    '<input type="checkbox" class="weui-check" name="' + head + '" id="' + head + sobillList[i].id + '" value="' + sobillList[i].id + '"/>' +
                                    '<span class="weui-icon-checked"></span>' +
                                    '</div>' +

                                    '<div class="order-cell_bg">' +
                                    '<div class="order-list">' +
                                    '<div class="order-list_item"><span>编号：</span>  ' + (sobillList[i].billNo != null && sobillList[i].billNo != '' ? sobillList[i].billNo : "") + '</div>' +
                                    '</div>' +
                                    '<div class="order-list">' +
                                    '<div class="order-list_item"><span>跟单：</span>  ' + (sobillList[i].followerName != null && sobillList[i].followerName != '' ? sobillList[i].followerName : "") + '</div>' +
                                    '<div class="order-list_item"><span>日期：</span>  ' + (sobillList[i].needTimeStr != null && sobillList[i].needTimeStr != '' ? sobillList[i].needTimeStr : "") + '</div>' +
                                    '</div>' +
                                    '<div class="order-list">' +
                                    '<div class="order-list_item"><span>同步：</span>  ' + (sobillList[i].synStatus != null && sobillList[i].synStatus == 1 ? "<span style='color: green;'>同步</span>" : "<span style='color: red;'>未同步</span>") + '</div>' +
                                    '<div class="order-list_item"><span>状态：</span>  ' + (sobillList[i].status != null && sobillList[i].status == 1 ? "<span style='color: green;'>提交</span>" : "<span style='color: red;'>草稿</span>") + '</div>' +
                                    '</div>' +
                                    '</div>' +
                                    '</div>' +
                                    '</label>' +
                                    '</div>' +
                                    '</div>';
                            }
                            $("#" + detail).append(template);
                        }
                    }
                });
            },
            backHome:function () {
                window.location.href = '${ctxf}/wechat/main/index';
            },
            selectCard(type,id) {
                switch (type) {
                    case 1:
                        $("#"+type+id).css("background-color","yellow");
                        break;
                }
            }
        }
    });

    $(document.body).infinite().on("infinite", function () {
        if ($("#inProgressDetail").css("display") == "block") {
            if (loadingInProgress) return;
            // 加载执行中数据
            $("#loadInProgress").show();
            setTimeout(function () {
                loadDatas('inProgress');
            }, 1500);   //模拟延迟
        } else if ($("#toAuditDetail").css("display") == "block") {
            if (loadingToAudit) return;
            // 加载待审核数据
            $("#loadToAudit").show();
            setTimeout(function () {
                loadDatas('toAudit');
            }, 1500);   //模拟延迟
        } else if ($("#pendingDetail").css("display") == "block") {
            if (loadingPending) return;
            $("#loadPending").show();
            setTimeout(function () {
                loadDatas('pending');
            }, 1500);   //模拟延迟
        } else if ($("#closedDetail").css("display") == "block") {
            if (loadingClosed) return;
            $("#loadClosed").show();
            setTimeout(function () {
                loadDatas('closed');
            }, 1500);   //模拟延迟
        }
    });

    var inProgressPageNo = 1;
    var toAuditPageNo = 1;
    var pendingPageNo = 1;
    var closedPageNo = 1;

    $("#startTime").calendar({
        dateFormat: "yyyy-mm-dd"
    });

    $("#endTime").calendar({
        dateFormat: "yyyy-mm-dd"
    });

    // 加载数据
    function loadDatas(Id) {
        var data = {qyUserId:'${qyUserId}',pageSize:10};
        switch (Id) {
            case 'inProgress':
                inProgressPageNo++;
                data.cancellation = 0;
                data.pageNo = inProgressPageNo;
                break;
            case 'toAudit':
                // 待审核
                toAuditPageNo++;
                data.status = 1;
                data.checkStatus = 0;
                data.cancellation = 0;
                data.pageNo = toAuditPageNo;
                break;
            case 'pending':
                // 待提交(草稿)
                pendingPageNo++;
                data.status = 0;
                data.checkStatus = 2;
                data.cancellation = 0;
                data.pageNo = pendingPageNo;
                break;
            case "closed":
                closedPageNo++;
                data.cancellation = 1;
                data.pageNo = closedPageNo;
                break;
        }
        $.ajax({
            async: false,
            cache: false,
            url: '${ctxf}/wechat/sobill/getSobillList',
            data: data,
            dataType: 'json',
            success: function (res) {
                this.errorCode = res.body.errorCode;
                if (res.body.errorCode === "403") {
                    $.toast(res.body.msg, "forbidden");
                } else {
                    var sobillList = res.body.sobillList;
                    var template = '';
                    for (let i = 0; i < sobillList.length; i++) {
                        template += '<div class="order-cell">' +
                            '<div class="weui-cells_radio">' +
                            '<label class="weui-cell weui-check__label" for="' + Id + sobillList[i].id + '">' +
                            '<div class="order-cell_left">' +
                            '<div class="order-list">' +
                            '<div class="order-list_item"><span>客户：</span>  ' + (sobillList[i].cusName != null && sobillList[i].cusName != '' ? sobillList[i].cusName : "") + '</div>' +
                            '<input type="checkbox" class="weui-check" name="' + Id + '" id="' + Id + sobillList[i].id + '" value="' + sobillList[i].id + '"/>' +
                            '<span class="weui-icon-checked"></span>' +
                            '</div>' +

                            '<div class="order-cell_bg">' +
                            '<div class="order-list">' +
                            '<div class="order-list_item"><span>编号：</span>  ' + (sobillList[i].billNo != null && sobillList[i].billNo != '' ? sobillList[i].billNo : "") + '</div>' +
                            '</div>' +
                            '<div class="order-list">' +
                            '<div class="order-list_item"><span>跟单：</span>  ' + (sobillList[i].followerName != null && sobillList[i].followerName != '' ? sobillList[i].followerName : "") + '</div>' +
                            '<div class="order-list_item"><span>日期：</span>  ' + (sobillList[i].needTimeStr != null && sobillList[i].needTimeStr != '' ? sobillList[i].needTimeStr : "") + '</div>' +
                            '</div>' +
                            '<div class="order-list">' +
                            '<div class="order-list_item"><span>同步：</span>  ' + (sobillList[i].synStatus != null && sobillList[i].synStatus == 1 ? "<span style='color: green;'>同步</span>" : "<span style='color: red;'>未同步</span>") + '</div>' +
                            '<div class="order-list_item"><span>状态：</span>  ' + (sobillList[i].status != null && sobillList[i].status == 1 ? "<span style='color: green;'>提交</span>" : "<span style='color: red;'>草稿</span>") + '</div>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '</label>' +
                            '</div>' +
                            '</div>';
                    }
                    if (sobillList.length < 10) {
                        switch (Id) {
                            case 'inProgress':
                                loadingInProgress = true;
                                $("#loadInProgress").hide();
                                $("#endInProgress").show();
                                $("#loadInProgress").before(template);
                                break;
                            case 'toAudit':
                                loadingToAudit = true;
                                $("#loadToAudit").hide();
                                $("#endToAudit").show();
                                $("#loadToAudit").before(template);
                                break;
                            case 'pending':
                                loadingPending = true;
                                $("#loadPending").hide();
                                $("#endPending").show();
                                $("#loadPending").before(template);
                                break;
                            case "closed":
                                loadingClosed = true;
                                $("#loadClosed").hide();
                                $("#endClosed").show();
                                $("#loadClosed").before(template);
                                break;
                        }
                    }
                }
            }
        });
    }

    //切换导航
    function changeStyle(Id) {
        switch (Id) {
            case 'inProgress':
                if (!$("#" + Id).hasClass("weui-bar__item_on")) {
                    $("#" + Id).addClass("weui-bar__item_on");
                    $("#toAudit").removeClass("weui-bar__item_on");
                    $("#pending").removeClass("weui-bar__item_on");
                    $("#closed").removeClass("weui-bar__item_on");
                    $("#toAuditDetail").removeClass("weui-bar__item_on");
                    $("#inProgressDetail").css("display","block");
                    $("#toAuditDetail").css("display", "none");
                    $("#pendingDetail").css("display", "none");
                    $("#closedDetail").css("display", "none");
                    $('input[type=checkbox][name="toAudit"]:checked').prop("checked", false);
                    $('input[type=checkbox][name="pending"]:checked').prop("checked", false);
                    $('input[type=checkbox][name="closed"]:checked').prop("checked", false);
                    $("#loadToAudit").css("display", "none");
                }
                break;
            case 'toAudit':
                if (!$("#" + Id).hasClass("weui-bar__item_on")) {
                    $("#" + Id).addClass("weui-bar__item_on");
                    $("#inProgress").removeClass("weui-bar__item_on");
                    $("#pending").removeClass("weui-bar__item_on");
                    $("#closed").removeClass("weui-bar__item_on");
                    $("#toAuditDetail").css("display", "block");
                    $("#inProgressDetail").css("display","none");
                    $("#pendingDetail").css("display","none");
                    $("#closedDetail").css("display","none");
                    $('input[type=checkbox][name="inProgress"]:checked').prop("checked", false);
                    $('input[type=checkbox][name="pending"]:checked').prop("checked", false);
                    $('input[type=checkbox][name="closed"]:checked').prop("checked", false);
                    $("#loadHistory").css("display", "none");
                }
                break;
            case 'pending':
                if (!$("#" + Id).hasClass("weui-bar__item_on")) {
                    $("#" + Id).addClass("weui-bar__item_on");
                    $("#inProgress").removeClass("weui-bar__item_on");
                    $("#toAudit").removeClass("weui-bar__item_on");
                    $("#closed").removeClass("weui-bar__item_on");
                    $("#toAuditDetail").css("display", "none");
                    $("#inProgressDetail").css("display","none");
                    $("#closedDetail").css("display","none");
                    $("#pendingDetail").css("display", "block");
                    $('input[type=checkbox][name="toAudit"]:checked').prop("checked", false);
                    $('input[type=checkbox][name="inProgress"]:checked').prop("checked", false);
                    $('input[type=checkbox][name="closed"]:checked').prop("checked", false);
                    $("#loadToAudit").css("display", "none");
                }
                break;
            case 'closed':
                if (!$("#" + Id).hasClass("weui-bar__item_on")) {
                    $("#" + Id).addClass("weui-bar__item_on");
                    $("#inProgress").removeClass("weui-bar__item_on");
                    $("#toAudit").removeClass("weui-bar__item_on");
                    $("#pending").removeClass("weui-bar__item_on");
                    $("#toAuditDetail").css("display", "none");
                    $("#inProgressDetail").css("display","none");
                    $("#pendingDetail").css("display","none");
                    $("#closedDetail").css("display", "block");
                    $('input[type=checkbox][name="inProgress"]:checked').prop("checked", false);
                    $('input[type=checkbox][name="toAudit"]:checked').prop("checked", false);
                    $('input[type=checkbox][name="pending"]:checked').prop("checked", false);
                    $("#loadToAudit").css("display", "none");
                }
                break;
        }
    }
</script>
</body>
</html>