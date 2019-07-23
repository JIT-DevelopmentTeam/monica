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
            padding-top: 15%;
            padding-bottom: 12%;
        }
    </style>
</head>
<body>
<div id="page" class="page">
    <div class="page__bd" style="height: 100%;">
        <div class="weui-tab">
            <div class="weui-navbar" style="position: fixed;top: 0px;">
                <div id="toAudit" class="weui-navbar__item weui-bar__item_on" onclick="changeStyle('toAudit');">
                    待审核订单
                </div>
                <div id="history" class="weui-navbar__item" onclick="changeStyle('history');">
                    历史订单
                </div>
            </div>

            <div id="order-cell1">
                <div id="toAuditDetail">
                    <div class="order-cell" v-for="toAudit in toAuditList">
                        <div class="weui-cells_radio">
                            <label class="weui-cell weui-check__label" :for="'toAudit'+toAudit.id">
                                <div class="order-cell_left">
                                    <div class="order-list">
                                        <div class="order-list_item"><span>客户：</span> {{toAudit.cusName}}</div>
                                        <input type="radio" class="weui-check" name="toAudit" :id="'toAudit'+toAudit.id"
                                               :value="toAudit.id"/>
                                        <span class="weui-icon-checked"></span>
                                    </div>
                                    <div class="order-cell_bg">
                                        <div class="order-list">
                                            <div class="order-list_item"><span>编号：</span> {{toAudit.billNo}}</div>
                                        </div>
                                        <div class="order-list">
                                            <div class="order-list_item"><span>跟单：</span> {{toAudit.empName}}</div>
                                            <div class="order-list_item"><span>日期：</span> {{toAudit.needTime}}</div>
                                        </div>
                                        <div class="order-list">
                                            <div class="order-list_item" v-if="toAudit.synStatus === 0"><span>同步：</span>未同步
                                            </div>
                                            <div class="order-list_item" v-else><span>同步：</span>已同步</div>
                                            <div class="order-list_item" v-if="toAudit.status === 0"><span>状态：</span>草稿
                                            </div>
                                            <div class="order-list_item" v-else><span>状态：</span>提交</div>
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
                                    <input type="radio" class="weui-check" name="history" :id="'history'+history.id"
                                           :value="history.id"/>
                                    <span class="weui-icon-checked"></span>
                                </div>
                                <div class="order-cell_bg">
                                    <div class="order-list">
                                        <div class="order-list_item"><span>编号：</span> {{history.billNo}}</div>
                                    </div>
                                    <div class="order-list">
                                        <div class="order-list_item"><span>跟单：</span> {{history.empName}}</div>
                                        <div class="order-list_item"><span>日期：</span> {{history.needTime}}</div>
                                    </div>
                                    <div class="order-list">
                                        <div class="order-list_item" v-if="history.synStatus === 0"><span>同步：</span>未同步
                                        </div>
                                        <div class="order-list_item" v-else><span>同步：</span>已同步</div>
                                        <div class="order-list_item" v-if="history.status === 0"><span>状态：</span>草稿
                                        </div>
                                        <div class="order-list_item" v-else><span>状态：</span>提交</div>
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
        <a v-bind:href="addHref" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/icon-add.png" alt="">
            </div>
            <p class="weui-tabbar__label">{{add}}</p>
        </a>
        <a v-on:click="editHref" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/icon-edit2_blue.png" alt="">
            </div>
            <p class="weui-tabbar__label">{{edit}}</p>
        </a>
        <a v-on:click="delectById" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/icon-delete.png" alt="">
            </div>
            <p class="weui-tabbar__label">{{del}}</p>
        </a>
        <a v-on:click="checkSobill" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/icon-search.png" alt="">
            </div>
            <p class="weui-tabbar__label">{{check}}</p>
        </a>
    </div>
</div>

<%--<script src="${ctxStatic}/js/jquery-2.1.4.js"></script>--%>
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
            this.$http.get('${ctxf}/wechat/sobill/getSobillListByCheckStatus', {
                params: {
                    checkStatus: 0,
                    startPage: 0,
                    endPage: 10
                }
            }).then(function (res) {
                this.toAuditList = res.data.body.sobillList;
            });

            // 历史数据
            this.$http.get('${ctxf}/wechat/sobill/getSobillListByCheckStatus', {
                params: {
                    checkStatus: 1,
                    startPage: 0,
                    endPage: 10
                }
            }).then(function (res) {
                this.historyList = res.data.body.sobillList;
            });
        },
        data: {
            add: '新增',
            addHref: '${ctxf}/wechat/sobill/goAdd',
            edit: '编辑',
            del: '删除',
            check: '审核',
            toAuditList: [],
            historyList: []
        },
        methods: {
            editHref: function () {
                var historyId = $("input[name='history']:checked").val();
                if (historyId != null && historyId != '') {
                    $.alert("审核订单不允许编辑操作!");
                    return;
                }
                var toAuditId = $("input[name='toAudit']:checked").val();
                if (toAuditId == null || toAuditId == '') {
                    $.alert("请至少选择一条数据!");
                    return;
                }
                window.location.href = '${ctxf}/wechat/sobill/goEdit?id=' + toAuditId;
            },
            /* 删除 */
            delectById: function () {
                var toAuditId = $("input[name='toAudit']:checked").val();
                var historyId = $("input[name='history']:checked").val();
                if (historyId != null && historyId != '') {
                    $.alert("订单已审核不允许删除!");
                    return;
                }
                if (toAuditId == null || toAuditId == '') {
                    $.alert("请至少选择一条数据!");
                    return;
                }
                $.confirm("您确定要删除该订单吗?", function () {
                    //点击确认后的回调函数
                    $.ajax({
                        async: false,
                        cache: false,
                        url: '${ctxf}/wechat/sobill/delectById',
                        data: {
                            id: toAuditId
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
            checkSobill: function () {
                var historyId = $("input[name='history']:checked").val();
                if (historyId != null && historyId != '') {
                    $.alert("该订单已审核,无需重复操作!");
                    return;
                }
                var toAuditId = $("input[name='toAudit']:checked").val();
                if (toAuditId == null || toAuditId == '') {
                    $.alert("请至少选择一条数据!");
                    return;
                }
                $.confirm("您确定要审核该订单吗?", function () {
                    //点击确认后的回调函数
                    $.ajax({
                        async: false,
                        cache: false,
                        url: '${ctxf}/wechat/sobill/checkSobill',
                        type: 'post',
                        data: {
                            id: toAuditId
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

    // 加载数据
    function loadDatas(Id) {
        switch (Id) {
            case 'toAudit':
                startToAuditPage = startToAuditPage + 10;
                endToAuditPage = endToAuditPage + 10;
                $.ajax({
                    async: false,
                    cache: false,
                    url: '${ctxf}/wechat/sobill/getSobillListByCheckStatus',
                    data: {
                        checkStatus: 0,
                        startPage: startToAuditPage,
                        endPage: endToAuditPage
                    },
                    dataType: 'json',
                    success: function (res) {
                        var sobillList = res.body.sobillList;
                        var template = '';
                        for (let i = 0; i < sobillList.length; i++) {
                            template += '<div class="order-cell">' +
                                '<div class="weui-cells_radio">' +
                                '<label class="weui-cell weui-check__label" for="' + sobillList[i].id + '">' +
                                '<div class="order-cell_left">' +
                                '<div class="order-list">' +
                                '<div class="order-list_item"><span>客户：</span>  ' + (sobillList[i].cusName != null && sobillList[i].cusName != '' ? sobillList[i].cusName : "") + '</div>' +
                                '<input type="radio" class="weui-check" name="toAudit" id="toAudit' + sobillList[i].id + '" value="' + sobillList[i].id + '"/>' +
                                '<span class="weui-icon-checked"></span>' +
                                '</div>' +

                                '<div class="order-cell_bg">' +
                                '<div class="order-list">' +
                                '<div class="order-list_item"><span>编号：</span>  ' + (sobillList[i].billNo != null && sobillList[i].billNo != '' ? sobillList[i].billNo : "") + '</div>' +
                                '</div>' +
                                '<div class="order-list">' +
                                '<div class="order-list_item"><span>跟单：</span>  ' + (sobillList[i].empName != null && sobillList[i].empName != '' ? sobillList[i].empName : "") + '</div>' +
                                '<div class="order-list_item"><span>日期：</span>  ' + (sobillList[i].needTime != null && sobillList[i].needTime != '' ? sobillList[i].needTime : "") + '</div>' +
                                '</div>' +
                                '<div class="order-list">' +
                                '<div class="order-list_item"><span>同步：</span>  ' + (sobillList[i].synStatus != null && sobillList[i].synStatus == 1 ? "同步" : "未同步") + '</div>' +
                                '<div class="order-list_item"><span>状态：</span>  ' + (sobillList[i].status != null && sobillList[i].synStatus == 1 ? "提交" : "草稿") + '</div>' +
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
                    url: '${ctxf}/wechat/sobill/getSobillListByCheckStatus',
                    data: {
                        checkStatus: 1,
                        startPage: startHistoryPage,
                        endPage: endHistoryPage
                    },
                    dataType: 'json',
                    success: function (res) {
                        var sobillList = res.body.sobillList;
                        var template = '';
                        for (let i = 0; i < sobillList.length; i++) {
                            template += '<div class="order-cell">' +
                                '<div class="weui-cells_radio">' +
                                '<label class="weui-cell weui-check__label" for="' + sobillList[i].id + '">' +
                                '<div class="order-cell_left">' +
                                '<div class="order-list">' +
                                '<div class="order-list_item"><span>客户：</span>  ' + (sobillList[i].cusName != null && sobillList[i].cusName != '' ? sobillList[i].cusName : "") + '</div>' +
                                '<input type="radio" class="weui-check" name="toAudit" id="toAudit' + sobillList[i].id + '" value="' + sobillList[i].id + '"/>' +
                                '<span class="weui-icon-checked"></span>' +
                                '</div>' +

                                '<div class="order-cell_bg">' +
                                '<div class="order-list">' +
                                '<div class="order-list_item"><span>编号：</span>  ' + (sobillList[i].billNo != null && sobillList[i].billNo != '' ? sobillList[i].billNo : "") + '</div>' +
                                '</div>' +
                                '<div class="order-list">' +
                                '<div class="order-list_item"><span>跟单：</span>  ' + (sobillList[i].empName != null && sobillList[i].empName != '' ? sobillList[i].empName : "") + '</div>' +
                                '<div class="order-list_item"><span>日期：</span>  ' + (sobillList[i].needTime != null && sobillList[i].needTime != '' ? sobillList[i].needTime : "") + '</div>' +
                                '</div>' +
                                '<div class="order-list">' +
                                '<div class="order-list_item"><span>同步：</span>  ' + (sobillList[i].synStatus != null && sobillList[i].synStatus == 1 ? "同步" : "未同步") + '</div>' +
                                '<div class="order-list_item"><span>状态：</span>  ' + (sobillList[i].status != null && sobillList[i].synStatus == 1 ? "提交" : "草稿") + '</div>' +
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
                $('input[type=radio][name="history"]:checked').prop("checked", false);
                $("#loadHistory").css("display", "none");
            }
        } else if (Id == 'history') {
            if (!$("#" + Id).hasClass("weui-bar__item_on")) {
                $("#" + Id).addClass("weui-bar__item_on");
                $("#toAudit").removeClass("weui-bar__item_on");
                $("#toAuditDetail").css("display", "none");
                $("#historyDetail").css("display", "block");
                $('input[type=radio][name="toAudit"]:checked').prop("checked", false);
                $("#loadToAudit").css("display", "none");
            }
        }
    }
</script>
</body>
</html>