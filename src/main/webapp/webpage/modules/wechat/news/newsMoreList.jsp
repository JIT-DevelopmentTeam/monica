<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta charset="UTF-8">
    <title>新闻公告</title>
    <link rel="stylesheet" href="${ctxStatic}/css/weui.css">
    <link rel="stylesheet" href="${ctxStatic}/css/wxqy/news/news.css">
    <script src="${ctxStatic}/js/jquery-2.1.4.js"></script>
    <script src="${ctxStatic}/js/jquery-weui.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue.js"></script>

    <style>
        .header {
            background: #F5F5F5;
            text-align: center;
            border-bottom: 1px solid #edf5fa;
        }
        .header span {
            color: #277de2;
            margin-left: 0;
            font-style: normal;
            font-weight: bold;
            font-size: larger;
        }
    </style>
</head>
<body>

<div id="all">
    <div class="header">
        <span>{{ head }}</span>
    </div>

    <%--<div class="interval"></div>--%>

    <div class="body">
        <div class="headlines" id="list">
            <%--<div class="label">
                <span>{{ head }}</span>
            </div>--%>
            <div v-for="(item, index) in items">
                <div class="lists">
                    <div class="list">
                        <a href="javascript:void(0)" @click="detail(item.id)" class="a-lk">
                            <div class="title">
                                <strong>{{ item.title }}</strong>
                                <div class="info">
                                    <span class="resource">{{ item.user.name }}</span>
                                </div>
                            </div>
                            <div class="u-img">
                                <div class="photo">
                                    <img v-bind:src="path + item.mainpic">
                                </div>
                            </div>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <%--<div class="weui-loadmore">--%>
        <%--<i class="weui-loading"></i>--%>
        <%--<span class="weui-loadmore__tips">正在加载</span>--%>
    <%--</div>--%>
    <%--<div class="footer">
        <span>©2019 JIT</span>
    </div>--%>
</div>

<script type="text/javascript">
    // var loading = false;  //状态标记
    // $(document.body).infinite().on("infinite", function() {
    //     if(loading) return;
    //     loading = true;
    //     setTimeout(function() {
    //         $("#list").append("<p> 我是新加载的内容 </p>");
    //         loading = false
    //     }, 1500);   //模拟延迟
    // });

    var vm = new Vue({
        el: "#all",
        data: {
            items: [],
            path: '${path}',
            head: '${head}'
        },
        methods: {
            showData: function(){
                $.ajax({
                    type: "Post",
                    url: "${ctxf}/wechat/news/moreListData?type=${type}",
                    data: {},
                    dataType: "json",
                    success: function(data) {
                        vm.items = data.newsList;
                        /*for (let i = 0; i < data.newsList.length; i++) {
                            if (data.newsList[i].headline == vm.type) {
                                vm.headlinesItems.push(data.newsList[i]);
                            } else {
                                vm.ordinaryItems.push(data.newsList[i]);
                            }
                        }*/
                    }
                });
            },
            detail: function (id) {
                window.location = "${ctxf}/wechat/news/form?id=" + id;
            },
            back: function() {
                window.history.back();
            },
            init: function () {
                if ($('#headLine').height() != 20) {
                    $('#headLine').css("display", "block");
                }
            }
        },
        mounted: function(){
            this.showData();
        },
        updated: function () {
            this.init();
        }
    });
</script>
</body>
</html>