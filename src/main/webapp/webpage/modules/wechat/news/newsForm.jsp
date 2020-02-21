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
    <header class='demos-header'>
        <h2 class="demos-title">{{ title }}</h2>
    </header>
    <article class="weui-article">
        <span style="color: grey; font-weight: bold; font-size: smaller;">{{ author }} {{ pushTime }}</span>
        <h5 style="float: right; color: grey; font-weight: bold; font-size: smaller;">{{ readCount }}</h5>
        <section style="clear: both;">
            <section>
                <%--<h3>1.1 节标题</h3>--%>
                <p>
                    <span v-html="content">{{ content }}</span>
                </p>
                <%--<p>--%>
                    <%--<img src="./images/pic_article.png" alt="">--%>
                    <%--<img src="./images/pic_article.png" alt="">--%>
                <%--</p>--%>
            </section>
        </section>
    </article>

    <%--<div class="weui-loadmore">--%>
        <%--<i class="weui-loading"></i>--%>
        <%--<span class="weui-loadmore__tips">正在加载</span>--%>
    <%--</div>--%>

    <%--<div class="weui-footer">
        <p class="weui-footer__text">{{ copyright }}</p>
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
        el: ".page",
        data: {
            title: "",
            author: "",
            pushTime: "",
            content: "",
            readCount: ""
        },
        methods: {
            showData: function(){
                var id = '${id}';
                $.ajax({
                    type: "Post",
                    url: "${ctxf}/wechat/news/formData",
                    data: {id : id},
                    dataType: "json",
                    success: function(data) {
                        console.log(data);
                        vm.title = data.news.title;
                        vm.author = "—— " + data.news.user.name;
                        vm.pushTime = data.news.push + " ——";
                        vm.content = vm.decode(data.news.content);
                        vm.readCount = "阅读次数：" + data.news.readCount;
                    }
                });
            },
            decode: function (text) {
                return text.replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&amp;/g, "&").replace(/&quot;/g, '"').replace(/&apos;/g, "'");
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