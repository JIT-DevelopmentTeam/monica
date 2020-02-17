<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta charset="UTF-8">
    <title>订单管理</title>
    <link rel="stylesheet" href="${ctxStatic}/css/wxqy/orderList.css">
    <link rel="stylesheet" href="${ctxStatic}/css/weui.min.css">
    <link rel="stylesheet" href="${ctxStatic}/css/jquery-weui.min.css">
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
    <div class="page__bd" style="height: 100%;">
        <div class="weui-tab">
            <div class="weui-navbar" style="position: fixed;top: 5%;z-index: 10000;">
                <div id="toAudit" class="weui-navbar__item weui-bar__item_on" onclick="changeStyle('toAudit');">
                    待审核订单
                </div>
                <div id="history" class="weui-navbar__item" onclick="changeStyle('history');">
                    历史订单
                </div>
            </div>

            <div id="order-cell1" style="padding-top: 25%;">
                <div id="toAuditDetail">
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
                                            <div class="order-list_item"><span>跟单：</span> </div>
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
            </div>

            <div id="historyDetail" style="display: none;">
                <div class="order-cell" v-for="history in historyList">
                    <div class="weui-cells_radio">
                        <label class="weui-cell weui-check__label" :for="'history'+history.id">
                            <div class="order-cell_left">
                                <div class="order-list">
                                    <div class="order-list_item"><span>客户：</span> {{history.cusName}}</div>
                                    <input type="checkbox" class="weui-check" name="history" :id="'history'+history.id"
                                           :value="history.id"/>
                                    <span class="weui-icon-checked"></span>
                                </div>
                                <div class="order-cell_bg">
                                    <div class="order-list">
                                        <div class="order-list_item"><span>编号：</span> {{history.billNo}}</div>
                                    </div>
                                    <div class="order-list">
                                        <div class="order-list_item"><span>跟单：</span> {{history.empName}}</div>
                                        <div class="order-list_item"><span>日期：</span> {{history.needTimeStr}}</div>
                                    </div>
                                    <div class="order-list">
                                        <div class="order-list_item" v-if="history.synStatus === 0">
                                            <span>同步：</span><span style="color: red;">未同步</span>
                                        </div>
                                        <div class="order-list_item" v-else><span>同步：</span><span style="color: green;">已同步</span>
                                        </div>
                                        <div class="order-list_item" v-if="history.status === 0"><span>状态：</span><span
                                                style="color: red;">草稿</span>
                                        </div>
                                        <div class="order-list_item" v-else><span>状态：</span><span style="color: green;">提交</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </label>
                    </div>
                </div>

                <div id="loadHistory" class="weui-loadmore">
                    <i class="weui-loading"></i>
                    <span class="weui-loadmore__tips">正在加载...</span>
                </div>

                <div id="endHistory" class="weui-loadmore weui-loadmore_line weui-loadmore_dot">
                    <span class="weui-loadmore__tips" style="background-color: #F6F6F6;">加载完毕</span>
                </div>
            </div>
        </div>
    </div>

    <br><br><br>

    <div class="weui-tabbar" style="position:fixed;bottom: 0px;">
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
        $("#loadToAudit").hide();
        $("#endToAudit").hide();
        $("#loadHistory").hide();
        $("#endHistory").hide();
    });

    var vm = new Vue({
        el: '#page',
        created: function () {
            // 待审核数据
            this.$http.get('${ctxf}/wechat/sobill/getSobillList', {
                params: {
                    checkStatus: 0,
                    startPage: 0,
                    endPage: 10,
                    qyUserId: '${qyUserId}'
                }
            }).then(function (res) {
                if (res.body.success) {
                    this.toAuditList = res.body.sobillList;
                }
            });

            // 历史数据
            this.$http.get('${ctxf}/wechat/sobill/getSobillList', {
                params: {
                    isHistory: 1,
                    startPage: 0,
                    endPage: 10,
                    qyUserId: '${qyUserId}'
                }
            }).then(function (res) {
                if (res.body.success) {
                    this.historyList = res.body.sobillList;
                }
            });
        },
        data: {
            add: '新增',
            addHref: '${ctxf}/wechat/sobill/goAdd?qyUserId=${qyUserId}',
            edit: '编辑',
            del: '删除',
            home:'首页',
            toAuditList: [],
            historyList: []
        },
        methods: {
            editHref: function () {
                var historyIds = [];
                $("input[name='history']:checked").each(function () {
                    historyIds.push($(this).val());
                });
                var toAuditIds = [];
                $("input[name='toAudit']:checked").each(function () {
                    toAuditIds.push($(this).val());
                });
                if (toAuditIds.length > 1 || historyIds.length > 1) {
                    $.alert("编辑操作仅允许单选!");
                    return;
                } else if (toAuditIds.length == 0 && historyIds.length == 0) {
                    $.alert("请至少选择一条数据!");
                    return;
                }
                var id = '';
                if (toAuditIds.length == 1) {
                    id = toAuditIds[0];
                } else if (historyIds.length == 1) {
                    id = historyIds[0];
                }
                $.ajax({
                    async: false,
                    cache: false,
                    url: '${ctxf}/wechat/sobill/getById',
                    type: 'post',
                    data: {
                        id: id
                    },
                    dataType: 'json',
                    success: function (res) {
                        var sobill = res.body.sobill;
                        if (sobill.checkStatus == 1 || sobill.status == 1) {
                            $.alert("该订单已审核或已提交,不允许编辑!");
                            return;
                        }
                        window.location.href = '${ctxf}/wechat/sobill/goEdit?id=' + id;
                    }
                });
            },
            /* 删除 */
            delectById: function () {
                var toAuditIds = [];
                $("input[name='toAudit']:checked").each(function () {
                    toAuditIds.push($(this).val());
                });
                var historyIds = [];
                $("input[name='history']:checked").each(function () {
                    historyIds.push($(this).val());
                });
                if (toAuditIds.length == 0 && historyIds.length == 0) {
                    $.alert("请至少选择一条数据!");
                    return;
                }
                var idsStr = '';
                if (toAuditIds.length > 0) {
                    idsStr = toAuditIds.toString();
                } else if (historyIds.length > 0) {
                    idsStr = historyIds.toString();
                }
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
                            if (res.success) {
                                $.alert(res.msg);
                                setTimeout(function () {
                                    window.location.reload();
                                }, 3000);
                            } else {
                                $.alert(res.msg);
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
                var detail = '';
                var head = '';
                var checkStatus;
                if ($("#toAudit").hasClass("weui-bar__item_on")) {
                    detail = 'toAuditDetail';
                    head = 'toAudit';
                    checkStatus = 0;
                } else {
                    detail = 'historyDetail';
                    head = 'history';
                }
                $.ajax({
                    async: false,
                    cache: false,
                    url: '${ctxf}/wechat/sobill/getSobillList',
                    type: 'post',
                    data: {
                        startTime: startTime,
                        endTime: endTime,
                        checkStatus: checkStatus,
                        qyUserId: '${qyUserId}'
                    },
                    dataType: 'json',
                    success: function (res) {
                        if (!res.success) {
                            return;
                        }
                        var sobillList = res.body.sobillList;
                        $("#"+detail).empty();
                        var template = '';
                        for (var i = 0; i < sobillList.length; i++) {
                            template += '<div class="order-cell">' +
                                '<div class="weui-cells_radio">' +
                                '<label class="weui-cell weui-check__label" for="'+ head + sobillList[i].id + '">' +
                                '<div class="order-cell_left">' +
                                '<div class="order-list">' +
                                '<div class="order-list_item"><span>客户：</span>  ' + (sobillList[i].cusName != null && sobillList[i].cusName != '' ? sobillList[i].cusName : "") + '</div>' +
                                '<input type="checkbox" class="weui-check" name="'+ head +'" id="'+ head + sobillList[i].id + '" value="' + sobillList[i].id + '"/>' +
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
                        $("#"+detail).append(template);
                    }
                });
            },
            backHome:function () {
                window.location.href = '${ctxf}/wechat/main/index';
            }
        }
    });

    // 滚动加载
    var loadingToAudit = false;  //状态标记
    var loadingHistory = false;
    $(document.body).infinite().on("infinite", function () {
        if ($("#toAuditDetail").css("display") == "block") {
            if (loadingToAudit) return;
            // 加载待审核数据
            $("#loadToAudit").show();
            setTimeout(function () {
                loadDatas('toAudit');
            }, 1500);   //模拟延迟
        } else if ($("#historyDetail").css("display") == "block") {
            if (loadingHistory) return;
            // 加载历史数据
            $("#loadHistory").show();
            setTimeout(function () {
                loadDatas('history');
            }, 1500);   //模拟延迟
        }

    });

    var startToAuditPage = 0;
    var endToAuditPage = 10;
    var startHistoryPage = 0;
    var endHistoryPage = 10;

    $("#startTime").calendar({
        dateFormat: "yyyy-mm-dd"
    });

    $("#endTime").calendar({
        dateFormat: "yyyy-mm-dd"
    });

    // 加载数据
    function loadDatas(Id) {
        switch (Id) {
            case 'toAudit':
                startToAuditPage = startToAuditPage + 10;
                endToAuditPage = endToAuditPage + 10;
                $.ajax({
                    async: false,
                    cache: false,
                    url: '${ctxf}/wechat/sobill/getSobillList',
                    data: {
                        checkStatus: 0,
                        startPage: startToAuditPage,
                        endPage: endToAuditPage,
                        qyUserId: '${qyUserId}'
                    },
                    dataType: 'json',
                    success: function (res) {
                        if (!res.success) {
                            return;
                        }
                        var sobillList = res.body.sobillList;
                        var template = '';
                        for (let i = 0; i < sobillList.length; i++) {
                            template += '<div class="order-cell">' +
                                '<div class="weui-cells_radio">' +
                                '<label class="weui-cell weui-check__label" for="toAudit' + sobillList[i].id + '">' +
                                '<div class="order-cell_left">' +
                                '<div class="order-list">' +
                                '<div class="order-list_item"><span>客户：</span>  ' + (sobillList[i].cusName != null && sobillList[i].cusName != '' ? sobillList[i].cusName : "") + '</div>' +
                                '<input type="checkbox" class="weui-check" name="toAudit" id="toAudit' + sobillList[i].id + '" value="' + sobillList[i].id + '"/>' +
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
                                '<div class="order-list_item"><span>状态：</span>  ' + (sobillList[i].status != null && sobillList[i].status == 1 ? "<span style='color: green;'>提交</span>" : "<span style='color:red;'>草稿</span>") + '</div>' +
                                '</div>' +
                                '</div>' +
                                '</div>' +
                                '</label>' +
                                '</div>' +
                                '</div>';
                        }
                        $("#loadToAudit").before(template);
                        if (sobillList.length < 10) {
                            loadingToAudit = true;
                            $("#loadToAudit").hide();
                            $("#endToAudit").show();
                        }
                    }
                });
                break;
            case 'history':
                startHistoryPage = startHistoryPage + 10;
                endHistoryPage = endHistoryPage + 10;
                $.ajax({
                    async: false,
                    cache: false,
                    url: '${ctxf}/wechat/sobill/getSobillList',
                    data: {
                        isHistory: 1,
                        startPage: startHistoryPage,
                        endPage: endHistoryPage,
                        qyUserId: '${qyUserId}'
                    },
                    dataType: 'json',
                    success: function (res) {
                        if (!res.success) {
                            return;
                        }
                        var sobillList = res.body.sobillList;
                        var template = '';
                        for (let i = 0; i < sobillList.length; i++) {
                            template += '<div class="order-cell">' +
                                '<div class="weui-cells_radio">' +
                                '<label class="weui-cell weui-check__label" for="history' + sobillList[i].id + '">' +
                                '<div class="order-cell_left">' +
                                '<div class="order-list">' +
                                '<div class="order-list_item"><span>客户：</span>  ' + (sobillList[i].cusName != null && sobillList[i].cusName != '' ? sobillList[i].cusName : "") + '</div>' +
                                '<input type="checkbox" class="weui-check" name="history" id="history' + sobillList[i].id + '" value="' + sobillList[i].id + '"/>' +
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
                            loadingHistory = true;
                            $("#loadHistory").hide();
                            $("#endHistory").show();
                        }
                        $("#loadHistory").before(template);
                    }
                });
                break;
        }
    }

    //切换导航
    function changeStyle(Id) {
        if (Id == 'toAudit') {
            if (!$("#" + Id).hasClass("weui-bar__item_on")) {
                $("#" + Id).addClass("weui-bar__item_on");
                $("#history").removeClass("weui-bar__item_on");
                $("#toAuditDetail").css("display", "block");
                $("#historyDetail").css("display", "none");
                $('input[type=checkbox][name="history"]:checked').prop("checked", false);
                $("#loadHistory").css("display", "none");
            }
        } else if (Id == 'history') {
            if (!$("#" + Id).hasClass("weui-bar__item_on")) {
                $("#" + Id).addClass("weui-bar__item_on");
                $("#toAudit").removeClass("weui-bar__item_on");
                $("#toAuditDetail").css("display", "none");
                $("#historyDetail").css("display", "block");
                $('input[type=checkbox][name="toAudit"]:checked').prop("checked", false);
                $("#loadToAudit").css("display", "none");
            }
        }
    }
</script>
</body>
</html>