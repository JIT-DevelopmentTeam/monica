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
    <script src="${ctxStatic}/js/jquery-weui.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue.js"></script>
    <script src="${ctxStatic}/js/jquery-2.1.4.js"></script>

</head>
<body>
<div class="page">
    <div class="page__bd">
        <div class="weui-panel weui-panel_access">
            <div class="weui-panel__bd">
                <div v-for="item in items">
                    <span class="weui-badge" style="margin-left: 5px;" v-if="item.headline === 1">头条</span>
                    <a href="javascript:void(0);" @click="detail(item.id)" class="weui-media-box weui-media-box_appmsg">
                        <div class="weui-media-box__hd">
                            <img class="weui-media-box__thumb" v-bind:src="item.mainpic" alt="">
                        </div>
                        <div class="weui-media-box__bd">
                            <h4 class="weui-media-box__title">{{ item.title }}</h4>
                            <%--<p class="weui-media-box__desc">由各种物质组成的巨型球状天体，叫做星球。星球有一定的形状，有自己的运行轨道。</p>--%>
                        </div>
                    </a>
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
            items: []
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
                        console.log(vm.items);
                    }
                });
            },
            detail: function (id) {
                window.location = "${ctxf}/wechat/news/form?id=" + id;
            }
        },
        mounted: function(){
            this.showData();
        }
    });

    function changeStyle(Id) {
        if (Id == 'toAudit') {
            if (!$("#" + Id).hasClass("weui-bar__item_on")) {
                $("#" + Id).addClass("weui-bar__item_on");
                $("#history").removeClass("weui-bar__item_on");
                $("#toAuditDetail").css("display", "block");
                $("#historyDetail").css("display", "none");
            }
        } else if (Id == 'history') {
            if (!$("#" + Id).hasClass("weui-bar__item_on")) {
                $("#" + Id).addClass("weui-bar__item_on");
                $("#toAudit").removeClass("weui-bar__item_on");
                $("#toAuditDetail").css("display", "none");
                $("#historyDetail").css("display", "block");
            }
        }
    }
</script>
</body>
</html>