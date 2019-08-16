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

</head>
<body>

<div class="header">
    <div>
        <img src="${ctxStatic}/common/images/flat.png" class="icon">
        <span>手机新闻</span>
    </div>
</div>

<div class="interval"></div>

<div class="body">
    <div class="headlines">
        <div class="label">
            <span>头条</span>
        </div>
        <div v-for="(item, index) in headlinesItems" v-if="index < 5">
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
        <div class="more">
            <a href="javascript:void(0)" @click="more('headlines')" class="mo-lk">
                进入头条 〉
            </a>
        </div>
    </div>

    <div class="interval"></div>

    <div class="ordinary">
        <div class="label">
            <span>新闻</span>
        </div>
        <div v-for="(item, index) in ordinaryItems" v-if="index < 5">
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
        <div class="more">
            <a href="javascript:void(0)" @click="more('ordinary')" class="mo-lk">
                进入新闻 〉
            </a>
        </div>
    </div>
</div>

<div class="footer">
    <span>©2019 JIT</span>
</div>
<%--<div class="page">
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
            &lt;%&ndash;<div class="weui-panel__ft">&ndash;%&gt;
                &lt;%&ndash;<a href="javascript:void(0);" class="weui-cell weui-cell_access weui-cell_link">&ndash;%&gt;
                    &lt;%&ndash;<div class="weui-cell__bd">查看更多</div>&ndash;%&gt;
                    &lt;%&ndash;<span class="weui-cell__ft"></span>&ndash;%&gt;
                &lt;%&ndash;</a>&ndash;%&gt;
            &lt;%&ndash;</div>&ndash;%&gt;
        </div>
    </div>

    &lt;%&ndash;<div class="weui-loadmore">&ndash;%&gt;
        &lt;%&ndash;<i class="weui-loading"></i>&ndash;%&gt;
        &lt;%&ndash;<span class="weui-loadmore__tips">正在加载</span>&ndash;%&gt;
    &lt;%&ndash;</div>&ndash;%&gt;

</div>--%>

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
        el: ".body",
        data: {
            headlinesItems: [],
            ordinaryItems: [],
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
                        for (let i = 0; i < data.newsList.length; i++) {
                            if (data.newsList[i].headline == '1') {
                                vm.headlinesItems.push(data.newsList[i]);
                            } else if (data.newsList[i].headline == '0') {
                                vm.ordinaryItems.push(data.newsList[i]);
                            }
                        }
                    }
                });
            },
            detail: function (id) {
                window.location = "${ctxf}/wechat/news/form?id=" + id;
            },
            more: function (type) {
                window.location = "${ctxf}/wechat/news/moreList?type=" + type;
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