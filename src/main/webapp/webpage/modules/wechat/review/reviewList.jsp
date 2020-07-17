<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no,viewport-fit=cover">
    <meta charset="UTF-8">
    <title>我的审批</title>
    <link rel="stylesheet" href="${ctxStatic}/css/weui.min.css">
    <link rel="stylesheet" href="${ctxStatic}/css/jquery-weui.min.css">
    <link rel="stylesheet" href="${ctxStatic}/css/wxqy/reviewList.css">
    <%-- 全面屏iphone适配 --%>
    <link rel="stylesheet" href="${ctxStatic}/css/wxqy/iphoneadaptation/iphoneadaptation.css">
    <script src="${ctxStatic}/common/vue/js/vue.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue-resource.min.js"></script>
</head>
<body>
    <div class="weui-tab" id="page">
        <!-- 搜索栏 -->
        <div class="weui-search-bar" id="searchBar">
            <div class="weui-search-bar__form">
                <div class="weui-search-bar__box">
                    <i class="weui-icon-search"></i>
                    <input v-on:blur="searchReview($event.currentTarget.value)" type="search" class="weui-search-bar__input" id="searchInput" placeholder="搜索" required="">
                    <a href="javascript:" class="weui-icon-clear" id="searchClear"></a>
                </div>
                <label class="weui-search-bar__label" id="searchText">
                    <i class="weui-icon-search"></i>
                    <span>{{search}}</span>
                </label>
            </div>
        </div>

        <div class="weui-tab__bd">

            <div id="myReview" class="weui-tab__bd-item weui-tab__bd-item--active">
                <div class="weui-navbar" style="position: static;">
                    <a id="unprocessedNavbar" class="weui-navbar__item weui-bar__item--on" v-on:click="switchData('unprocessed');">
                        {{unprocessed}}
                    </a>
                    <a id="processedNavbar" class="weui-navbar__item" v-on:click="switchData('processed');">
                        {{processed}}
                    </a>
                </div>

                <div id="unprocessedData" class="weui-tab__bd-item weui-tab__bd-item--active">
                    <div id="unprocessedDataList">
                        <div v-on:click="applicationDetail(unprocessed.sobillId.id,1)" style="border-radius: 5px" class="review-list" v-for="unprocessed in unprocessedList">
                            <div class="reviewInfo">
                                <p style="color: #b2b2b2;">销售名称：{{unprocessed.sobillId.empName}}</p>
                                <p style="color: #b2b2b2;">客户名称：{{unprocessed.sobillId.cusName}}</p>
                                <p style="color: #b2b2b2;">订单编号：{{unprocessed.sobillId.billNo}}</p>
                                <p style="color: #b2b2b2;" v-if="unprocessed.type === 1">申请类型：订单审批</p>
                                <p style="color: #b2b2b2;">审批状态：
                                    <i v-if="unprocessed.status === 0" style="font-size: 14px;line-height: 16px" class="weui-icon-waiting">审批中</i>
                                    <i v-else-if="unprocessed.status === 1" style="font-size: 14px;line-height: 16px" class="weui-icon-success">通过</i>
                                    <i v-else-if="unprocessed.status === 2" style="font-size: 14px;line-height: 16px" class="weui-icon-warn">拒绝</i>
                                </p>
                                <p style="color: #b2b2b2;">发起时间：{{unprocessed.createDateStr}}</p>
                            </div>
                        </div>
                    </div>
                    <div id="loadUnprocessed" class="weui-loadmore">
                        <i class="weui-loading"></i>
                        <span class="weui-loadmore__tips">正在加载...</span>
                    </div>

                    <div id="endUnprocessed" class="weui-loadmore weui-loadmore_line weui-loadmore_dot">
                        <span class="weui-loadmore__tips" style="background-color: #F6F6F6;">加载完毕</span>
                    </div>
                </div>
                <div id="processedData" class="weui-tab__bd-item">
                    <div id="processedDataList">
                        <div v-on:click="applicationDetail(processed.sobillId.id,2)" style="border-radius: 5px" class="review-list" v-for="processed in processedList">
                            <div class="reviewInfo">
                                <p style="color: #b2b2b2;">销售名称：{{processed.sobillId.empName}}</p>
                                <p style="color: #b2b2b2;">客户名称：{{processed.sobillId.cusName}}</p>
                                <p style="color: #b2b2b2;">订单编号：{{processed.sobillId.billNo}}</p>
                                <p style="color: #b2b2b2;" v-if="processed.type === 1">申请类型：订单审批</p>
                                <p style="color: #b2b2b2;">审批状态：
                                    <i v-if="processed.status === 0" style="font-size: 14px;line-height: 16px" class="weui-icon-waiting">审批中</i>
                                    <i v-else-if="processed.status === 1" style="font-size: 14px;line-height: 16px" class="weui-icon-success">通过</i>
                                    <i v-else-if="processed.status === 2" style="font-size: 14px;line-height: 16px" class="weui-icon-warn">拒绝</i>
                                </p>
                                <p style="color: #b2b2b2;">发起时间：{{processed.createDateStr}}</p>
                            </div>
                        </div>
                    </div>
                    <div id="loadProcessed" class="weui-loadmore">
                        <i class="weui-loading"></i>
                        <span class="weui-loadmore__tips">正在加载...</span>
                    </div>

                    <div id="endProcessed" class="weui-loadmore weui-loadmore_line weui-loadmore_dot">
                        <span class="weui-loadmore__tips" style="background-color: #F6F6F6;">加载完毕</span>
                    </div>
                </div>
            </div>
            <div id="submitted" class="weui-tab__bd-item">
                <div id="submittedList">
                    <div v-on:click="applicationDetail(submitted.id,2)" style="border-radius: 5px" class="review-list" v-for="submitted in submittedList">
                        <div class="reviewInfo">
                            <p style="color: #b2b2b2;">销售名称：{{submitted.empName}}</p>
                            <p style="color: #b2b2b2;">客户名称：{{submitted.cusName}}</p>
                            <p style="color: #b2b2b2;">订单编号：{{submitted.billNo}}</p>
                            <p style="color: #b2b2b2;" v-if="submitted.approveType === 1">申请类型：订单审批</p>
                            <p style="color: #b2b2b2;">审批状态：
                                <i v-if="(submitted.approveStatus === 1) && (submitted.isLast === 1)" style="font-size: 14px;line-height: 16px" class="weui-icon-success">通过</i>
                                <i v-else-if="submitted.approveStatus === 2" style="font-size: 14px;line-height: 16px" class="weui-icon-warn">拒绝</i>
                                <i v-else style="font-size: 14px;line-height: 16px" class="weui-icon-waiting">审批中</i>
                            </p>
                            <p style="color: #b2b2b2;">发起时间：{{submitted.initiateDateStr}}</p>
                        </div>
                    </div>
                </div>

                <div id="loadSubmitted" class="weui-loadmore">
                    <i class="weui-loading"></i>
                    <span class="weui-loadmore__tips">正在加载...</span>
                </div>

                <div id="endSubmitted" class="weui-loadmore weui-loadmore_line weui-loadmore_dot">
                    <span class="weui-loadmore__tips" style="background-color: #F6F6F6;">加载完毕</span>
                </div>
            </div>
        </div>

        <div id="footer" class="weui-tabbar" style="position:fixed;bottom: 0px;z-index: 10000;" v-if="errorCode !== '403'">
            <a v-on:click="backHome()" class="weui-tabbar__item">
                <div class="weui-tabbar__icon">
                    <img src="${ctxStatic}/image/wechat/home.jpg" alt="">
                </div>
                <p class="weui-tabbar__label">{{ home }}</p>
            </a>
            <a id="myReviewTabbar" v-on:click="switchContent('myReview')" class="weui-tabbar__item weui-bar__item--on">
                <div class="weui-tabbar__icon">
                    <img src="${ctxStatic}/image/wechat/search.jpg" alt="">
                </div>
                <p class="weui-tabbar__label">{{ myReview }}</p>
            </a>
            <a id="submittedTabbar" v-on:click="switchContent('submitted')" class="weui-tabbar__item">
                <div class="weui-tabbar__icon">
                    <img src="${ctxStatic}/image/wechat/upload.jpg" alt="">
                </div>
                <p class="weui-tabbar__label">{{ submitted }}</p>
            </a>
        </div>
    </div>

<script src="https://cdn.bootcss.com/jquery/1.11.0/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/jquery-weui/1.2.1/js/jquery-weui.min.js"></script>

<script type="text/javascript">

    $(function () {
        $("#loadUnprocessed").hide();
        $("#endUnprocessed").hide();
        $("#loadProcessed").hide();
        $("#endProcessed").hide();
        $("#loadSubmitted").hide();
        $("#endSubmitted").hide();
    });

    var startUnprocessedPage = 0;
    var endUnprocessedPage = 20;
    var startProcessedPage = 0;
    var endProcessedPage = 20;
    var startSubmittedPage = 0;
    var endSubmittedPage = 20;

    // 滚动加载
    var loadUnprocessed = false;  //状态标记
    var loadProcessed = false;
    var loadSubmitted = false;
    $(document.body).infinite().on("infinite", function () {
        // 非搜索状态下允许加载
        if($("#searchInput").val() == null || $("#searchInput").val() == ''){
            switch ($("#myReview").hasClass('weui-tab__bd-item--active')) {
                case true:
                    // 我的审核
                    switch ($("#unprocessedNavbar").hasClass('weui-bar__item--on')) {
                        case true:
                            // 待处理
                            if (loadUnprocessed) return;
                            $("#loadUnprocessed").show();
                            setTimeout(function () {
                                loadDatas('unprocessed');
                            }, 1500);   //模拟延迟
                            break;
                        case false:
                            // 已处理
                            if (loadProcessed) return;
                            $("#loadProcessed").show();
                            setTimeout(function () {
                                loadDatas('processed');
                            }, 1500);   //模拟延迟
                            break;
                    }
                    break;
                case false:
                    // 已提交
                    if (loadSubmitted) return;
                    $("#loadSubmitted").show();
                    setTimeout(function () {
                        loadDatas('submitted');
                    }, 1500);   //模拟延迟
                    break;
            }
        }
    });

    var vm = new Vue({
        el: '#page',
        created: function () {
            // 待处理
            this.$http.get('${ctxf}/wechat/review/myReviewList', {
                params: {
                    type: 1,
                    unprocessed:1,
                    startPage: 0,
                    endPage: 20,
                    qyUserId:'${qyUserId}'
                }
            }).then(function (res) {
                this.errorCode = res.errorCode;
                if (res.errorCode === "403") {
                    $.toast(res.msg, "forbidden");
                } else {
                    this.unprocessedList = res.body.body.myReviewList;
                }
            });

            // 已处理
            this.$http.get('${ctxf}/wechat/review/myReviewList', {
                params: {
                    type: 1,
                    processed:1,
                    startPage: 0,
                    endPage: 20,
                    qyUserId:'${qyUserId}'
                }
            }).then(function (res) {
                this.errorCode = res.errorCode;
                if (res.errorCode === "403") {
                    $.toast(res.msg, "forbidden");
                } else {
                    this.processedList = res.body.body.myReviewList;
                }
            });

            // 已提交
            this.$http.get('${ctxf}/wechat/sobill/submittedList', {
                params: {
                    startPage: 0,
                    endPage: 20,
                    qyUserId:'${qyUserId}'
                }
            }).then(function (res) {
                this.errorCode = res.errorCode;
                if (res.errorCode === "403") {
                    $.toast(res.msg, "forbidden");
                } else {
                    this.submittedList = res.body.body.submittedList;
                }
            });
        },
        data: {
            myReview:'我的审核',
            submitted:'已提交',
            search:'搜索',
            unprocessed:'待处理',
            processed:'已处理',
            home:'首页',
            unprocessedList:[],
            processedList:[],
            submittedList:[],
            errorCode: ''
        },
        methods: {
            // 切换内容
            switchContent:function (id) {
                switch (id) {
                    case 'myReview':
                        if (!$("#" + id).hasClass('weui-tab__bd-item--active')) {
                            $("#" + id).addClass('weui-tab__bd-item--active');
                            $("#submitted").removeClass('weui-tab__bd-item--active');
                        }
                        if (!$("#" + id + "Tabbar").hasClass('weui-bar__item--on')) {
                            $("#" + id + "Tabbar").addClass('weui-bar__item--on');
                            $("#submittedTabbar").removeClass('weui-bar__item--on');
                        }
                        break;
                    case 'submitted':
                        if (!$("#" + id).hasClass('weui-tab__bd-item--active')) {
                            $("#" + id).addClass('weui-tab__bd-item--active');
                            $("#myReview").removeClass('weui-tab__bd-item--active');
                        }
                        if (!$("#" + id + "Tabbar").hasClass('weui-bar__item--on')) {
                            $("#" + id + "Tabbar").addClass('weui-bar__item--on');
                            $("#myReviewTabbar").removeClass('weui-bar__item--on');
                        }
                        break;
                }
            },
            // 切换数据
            switchData:function (id) {
                switch (id) {
                    case 'unprocessed':
                        if (!$("#" + id + "Navbar").hasClass("weui-bar__item--on")) {
                            $("#" + id + "Navbar").addClass("weui-bar__item--on");
                            $("#processedNavbar").removeClass("weui-bar__item--on");
                            if (!$("#" + id + "Data").hasClass("weui-tab__bd-item--active")) {
                                $("#" + id + "Data").addClass("weui-tab__bd-item--active");
                                $("#processedData").removeClass("weui-tab__bd-item--active");
                            }
                        }
                        break;
                    case 'processed':
                        if (!$("#"+ id + "Navbar").hasClass("weui-bar__item--on")) {
                            $("#" + id + "Navbar").addClass("weui-bar__item--on");
                            $("#unprocessedNavbar").removeClass("weui-bar__item--on");
                            if (!$("#" + id + "Data").hasClass("weui-tab__bd-item--active")) {
                                $("#" + id + "Data").addClass("weui-tab__bd-item--active");
                                $("#unprocessedData").removeClass("weui-tab__bd-item--active");
                            }
                        }
                        break;
                }
            },
            searchReview:function (val) {
                var type;
                var url = '';
                var data;
                switch ($("#myReview").hasClass('weui-tab__bd-item--active')) {
                    case true:
                        // 我的审核
                        switch ($("#unprocessedNavbar").hasClass('weui-bar__item--on')) {
                            case true:
                                // 待处理
                                type = 1;
                                url = '${ctxf}/wechat/review/myReviewList';
                                if (val == null || val == ''){
                                    startUnprocessedPage = 0;
                                    endUnprocessedPage = 20;
                                    data = {type:1,unprocessed:1,startPage:startUnprocessedPage,endPage:endUnprocessedPage,qyUserId:'${qyUserId}'};
                                    loadUnprocessed = false;
                                } else{
                                    data = {type:1,unprocessed:1,cusName:val,qyUserId:'${qyUserId}'};
                                }
                                break;
                            case false:
                                // 已处理
                                type = 2;
                                url = '${ctxf}/wechat/review/myReviewList';
                                if (val == null || val == ''){
                                    startProcessedPage = 0;
                                    endProcessedPage = 20;
                                    data = {type:1,processed:1,startPage:startProcessedPage,endPage:endProcessedPage,qyUserId:'${qyUserId}'};
                                    loadProcessed = false;
                                } else{
                                    data = {type:1,processed:1,cusName:val,qyUserId:'${qyUserId}'};
                                }
                                break;
                        }
                        break;
                    case false:
                        // 已提交
                        type = 3;
                        url = '${ctxf}/wechat/sobill/submittedList';
                        if (val == null || val == ''){
                            startSubmittedPage = 0;
                            endSubmittedPage = 20;
                            data = {startPage:startSubmittedPage,endPage:endSubmittedPage,qyUserId:'${qyUserId}'};
                            loadSubmitted = false;
                        } else{
                            data = {cusName:val,qyUserId:'${qyUserId}'};
                        }
                        break;
                }
                $.ajax({
                    async:false,
                    cache:false,
                    url:url,
                    type:'get',
                    data:data,
                    dataType:'json',
                    success:function (res) {
                        this.errorCode = res.errorCode;
                        if (res.errorCode === "403") {
                            $.toast(res.msg, "forbidden");
                        } else {
                            var dataList = [];
                            var listId;
                            var templet = '';
                            switch (type) {
                                case 1:
                                    listId = 'unprocessedData';
                                    if (res.success) {
                                        dataList = res.body.myReviewList;
                                        $("#loadUnprocessed").hide();
                                        $("#endUnprocessed").hide();
                                    }
                                    break;
                                case 2:
                                    listId = 'processedData';
                                    if (res.success) {
                                        dataList = res.body.myReviewList;
                                        $("#loadProcessed").hide();
                                        $("#endProcessed").hide();
                                    }
                                    break;
                                case 3:
                                    listId = 'submitted';
                                    if (res.success) {
                                        dataList = res.body.submittedList;
                                        $("#loadSubmitted").hide();
                                        $("#endSubmitted").hide();
                                    }
                                    break;
                            }
                            $("#" + listId + "List").empty();
                            for (var i = 0; i < dataList.length; i++) {
                                templet += '<div style="border-radius: 5px" class="review-list">' +
                                    '<div class="reviewInfo">';
                                if (type == 1 || type == 2) {
                                    templet += '<p style="color: #b2b2b2;">销售名称：' + dataList[i].sobillId.empName + '</p>' +
                                        '<p style="color: #b2b2b2;">客户名称：' + dataList[i].sobillId.cusName + '</p>' +
                                        '<p style="color: #b2b2b2;">订单编号：' + dataList[i].sobillId.billNo + '</p>';
                                    if (dataList[i].type == 1) {
                                        templet += '<p style="color: #b2b2b2;">申请类型：订单审批</p>';
                                    }
                                    switch (dataList[i].status) {
                                        case 0:
                                            templet += '<p style="color: #b2b2b2;">审批状态：<i style="font-size: 14px;line-height: 16px" class="weui-icon-waiting">审批中</i></p>';
                                            break;
                                        case 1:
                                            templet += '<p style="color: #b2b2b2;">审批状态：<i style="font-size: 14px;line-height: 16px" class="weui-icon-success">通过</i></p>';

                                            break;
                                        case 2:
                                            templet += '<p style="color: #b2b2b2;">审批状态：<i style="font-size: 14px;line-height: 16px" class="weui-icon-warn">拒绝</i></p>';
                                            break;
                                    }
                                    templet += '<p style="color: #b2b2b2;">发起时间：' + dataList[i].createDateStr + '</p>'
                                } else {
                                    templet += '<p style="color: #b2b2b2;">销售名称：' + dataList[i].empName + '</p>' +
                                        '<p style="color: #b2b2b2;">客户名称：' + dataList[i].cusName + '</p>' +
                                        '<p style="color: #b2b2b2;">订单编号：' + dataList[i].billNo + '</p>';
                                    if (dataList[i].approveType == 1) {
                                        templet += '<p style="color: #b2b2b2;">申请类型：订单审批</p>';
                                    }
                                    if (dataList[i].approveStatus == 1 && dataList[i].isLast == 1) {
                                        templet += '<p style="color: #b2b2b2;">审批状态：<i style="font-size: 14px;line-height: 16px" class="weui-icon-success">通过</i></p>';
                                    } else if (dataList[i].approveStatus == 2) {
                                        templet += '<p style="color: #b2b2b2;">审批状态：<i style="font-size: 14px;line-height: 16px" class="weui-icon-warn">拒绝</i></p>';
                                    } else {
                                        templet += '<p style="color: #b2b2b2;">审批状态：<i style="font-size: 14px;line-height: 16px" class="weui-icon-waiting">审批中</i></p>';
                                    }
                                    templet += '<p style="color: #b2b2b2;">发起时间：' + dataList[i].initiateDateStr + '</p>';
                                }
                                templet += '</div>' +
                                    '</div>';
                            }
                            $("#" + listId + "List").append(templet);
                        }
                    }
                })
            },
            applicationDetail:function (sobillId,type) {
                if (type == 1) {
                    window.location.href = '${ctxf}/wechat/review/applicationDetail?id='+sobillId+"&isApproval=1&qyUserId=${qyUserId}";
                } else {
                    window.location.href = '${ctxf}/wechat/review/applicationDetail?id='+sobillId+"&qyUserId=${qyUserId}";
                }
            },
            backHome:function () {
                window.location.href = '${ctxf}/wechat/main/index';
            }
        }
    });

    // 加载数据
    function loadDatas(id) {
        var type;
        var url;
        var data;
        switch (id) {
            case 'unprocessed':
                type = 1;
                url = '${ctxf}/wechat/review/myReviewList';
                startUnprocessedPage += 20;
                endUnprocessedPage += 20;
                data = {type:1,unprocessed:1,startPage:startUnprocessedPage,endPage:endUnprocessedPage,qyUserId:'${qyUserId}'};
                break;
            case 'processed':
                type = 2;
                url = '${ctxf}/wechat/review/myReviewList';
                startProcessedPage += 20;
                endProcessedPage += 20;
                data = {type:1,processed:1,startPage:startProcessedPage,endPage:endProcessedPage,qyUserId:'${qyUserId}'};
                break;
            case 'submitted':
                type = 3;
                url = '${ctxf}/wechat/sobill/submittedList';
                startSubmittedPage += 20;
                endSubmittedPage += 20;
                data = {startPage:startSubmittedPage,endPage:endSubmittedPage,qyUserId:'${qyUserId}'};
                break;
        }
        $.ajax({
           async:false,
           cache:false,
           url:url,
           type:'get',
           data:data,
           dataType:'json',
           success:function(res) {
               vm.errorCode = res.errorCode;
               if (res.errorCode === "403") {
                   $.toast(res.msg, "forbidden");
               } else {
                   var dataList = [];
                   var listId;
                   var templet = '';
                   switch (type) {
                       case 1:
                           listId = 'unprocessedData';
                           if (res.success) {
                               dataList = res.body.myReviewList;
                               if (dataList.length < 20) {
                                   loadUnprocessed = true;
                                   $("#loadUnprocessed").hide();
                                   $("#endUnprocessed").show();
                               } else {
                                   $("#loadUnprocessed").hide();
                               }
                           } else {
                               $("#loadUnprocessed").hide();
                               $("#endUnprocessed").show();
                           }
                           break;
                       case 2:
                           listId = 'processedData';
                           if (res.success) {
                               dataList = res.body.myReviewList;
                               if (dataList.length < 20) {
                                   loadProcessed = true;
                                   $("#loadProcessed").hide();
                                   $("#endProcessed").show();
                               } else {
                                   $("#loadUnprocessed").hide();
                               }
                           } else {
                               $("#loadUnprocessed").hide();
                               $("#endUnprocessed").show();
                           }
                           break;
                       case 3:
                           listId = 'submitted';
                           if (res.success) {
                               dataList = res.body.submittedList;
                               if (dataList.length < 20) {
                                   loadSubmitted = true;
                                   $("#loadSubmitted").hide();
                                   $("#endSubmitted").show();
                               } else {
                                   $("#loadUnprocessed").hide();
                               }
                           } else {
                               $("#loadUnprocessed").hide();
                               $("#endUnprocessed").show();
                           }
                           break;
                   }
                   for (var i = 0; i < dataList.length; i++) {
                       templet += '<div style="border-radius: 5px" class="review-list">' +
                           '<div class="reviewInfo">';
                       if (type == 1 || type == 2) {
                           templet += '<p style="color: #b2b2b2;">销售名称：' + dataList[i].sobillId.empName + '</p>' +
                               '<p style="color: #b2b2b2;">客户名称：' + dataList[i].sobillId.cusName + '</p>' +
                               '<p style="color: #b2b2b2;">订单编号：' + dataList[i].sobillId.billNo + '</p>';
                           if (dataList[i].type == 1) {
                               templet += '<p style="color: #b2b2b2;">申请类型：订单审批</p>';
                           }
                           switch (dataList[i].status) {
                               case 0:
                                   templet += '<p style="color: #b2b2b2;">审批状态：<i style="font-size: 14px;line-height: 16px" class="weui-icon-waiting">审批中</i></p>';
                                   break;
                               case 1:
                                   templet += '<p style="color: #b2b2b2;">审批状态：<i style="font-size: 14px;line-height: 16px" class="weui-icon-success">通过</i></p>';

                                   break;
                               case 2:
                                   templet += '<p style="color: #b2b2b2;">审批状态：<i style="font-size: 14px;line-height: 16px" class="weui-icon-warn">拒绝</i></p>';
                                   break;
                           }
                           templet += '<p style="color: #b2b2b2;">发起时间：' + dataList[i].createDateStr + '</p>'
                       } else {
                           templet += '<p style="color: #b2b2b2;">销售名称：' + dataList[i].empName + '</p>' +
                               '<p style="color: #b2b2b2;">客户名称：' + dataList[i].cusName + '</p>' +
                               '<p style="color: #b2b2b2;">订单编号：' + dataList[i].billNo + '</p>';
                           if (dataList[i].approveType == 1) {
                               templet += '<p style="color: #b2b2b2;">申请类型：订单审批</p>';
                           }
                           if (dataList[i].approveStatus == 1 && dataList[i].isLast == 1) {
                               templet += '<p style="color: #b2b2b2;">审批状态：<i style="font-size: 14px;line-height: 16px" class="weui-icon-success">通过</i></p>';
                           } else if (dataList[i].approveStatus == 2) {
                               templet += '<p style="color: #b2b2b2;">审批状态：<i style="font-size: 14px;line-height: 16px" class="weui-icon-warn">拒绝</i></p>';
                           } else {
                               templet += '<p style="color: #b2b2b2;">审批状态：<i style="font-size: 14px;line-height: 16px" class="weui-icon-waiting">审批中</i></p>';
                           }
                           templet += '<p style="color: #b2b2b2;">发起时间：' + dataList[i].initiateDateStr + '</p>';
                       }
                       templet += '</div>' +
                           '</div>';
                   }
                   $("#" + listId + "List").before(templet);
               }
           }
        });

    }
</script>
</body>
</html>
