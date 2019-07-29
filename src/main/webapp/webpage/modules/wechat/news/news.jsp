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
    <script src="${ctxStatic}/js/jquery-2.1.4.js"></script>
    <script src="${ctxStatic}/js/jquery-weui.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue.js"></script>

    <style type="text/css">
        .weui-media-box {
            padding: 0px 20px;
            position: relative;
        }
        .weui-panel:before {
            content: " ";
            position: absolute;
            left: 0;
            top: 0;
            right: 0;
            height: 1px;
            border-top: 0px solid #E5E5E5;
            color: #E5E5E5;
            -webkit-transform-origin: 0 0;
            transform-origin: 0 0;
            -webkit-transform: scaleY(0.5);
            transform: scaleY(0.5);
        }
    </style>
</head>
<body>
<div class="page">
    <div class="page__bd">
        <div class="weui-panel weui-panel_access">
            <div class="weui-panel__bd">
                <div style="background-color: #e2e2e2; border-radius: 30px;">
                    <span style="margin-left: 10px;">头条</span>
                </div>
                <div id="headLine" style="max-height: 306px; min-height: auto; overflow-y: auto; display: none; -webkit-overflow-scrolling:touch;">
                    <div v-for="item in items" v-show="item.headline === 1" style="border-bottom: 1px solid #f7f7f7;">
                        <a href="javascript:void(0);" @click="detail(item.id)" class="weui-media-box weui-media-box_appmsg">
                            <div class="weui-media-box__hd">
                                <img style="width: 70px; height: 70px;" class="weui-media-box__thumb" v-bind:src="path + item.mainpic" alt="">
                            </div>
                            <div class="weui-media-box__bd">
                                <p class="weui-media-box__title">{{ item.title }}</p>
                                <p class="weui-media-box__desc">{{ item.user.name }} {{ item.push }}</p>
                            </div>
                            <span class="weui-badge" style="margin: 1em;">头条</span>
                        </a>
                    </div>
                </div>
                <div style="background-color: #e2e2e2; border-radius: 30px;">
                    <span style="margin-left: 10px;"></span>
                </div>
                <div id="newList">
                    <div v-for="item in items" v-show="item.headline === 0" style="border-bottom: 1px solid #efefef;">
                        <a href="javascript:void(0);" @click="detail(item.id)" class="weui-media-box weui-media-box_appmsg">
                            <div class="weui-media-box__hd">
                                <img style="width: 70px; height: 70px;" class="weui-media-box__thumb" v-bind:src="path + item.mainpic" alt="">
                            </div>
                            <div class="weui-media-box__bd">
                                <p class="weui-media-box__title">{{ item.title }}</p>
                                <p class="weui-media-box__desc">{{ item.user.name }} {{ item.push }}</p>
                            </div>
                        </a>
                    </div>
                </div>

            </div>
            <%--<div class="weui-panel__ft">--%>
                <%--<a href="javascript:void(0);" class="weui-cell weui-cell_access weui-cell_link">--%>
                    <%--<div class="weui-cell__bd">查看更多</div>--%>
                    <%--<span class="weui-cell__ft"></span>--%>
                <%--</a>--%>
            <%--</div>--%>
        </div>
    </div>

    <%--<div class="weui-loadmore">--%>
        <%--<i class="weui-loading"></i>--%>
        <%--<span class="weui-loadmore__tips">正在加载</span>--%>
    <%--</div>--%>

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
        el: ".weui-panel__bd",
        data: {
            items: [],
            path: '${path}'
        },
        methods: {
            showData: function(){
                $.ajax({
                    type: "Post",
                    url: "${ctxf}/wechat/news/data",
                    data: {},
                    dataType: "json",
                    success: function(data) {
                        vm.items = data.newsList;
                    }
                });
            },
            detail: function (id) {
                window.location = "${ctxf}/wechat/news/form?id=" + id;
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