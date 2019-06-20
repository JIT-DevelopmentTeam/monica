<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta charset="UTF-8">
    <title>新增订单</title>
    <link rel="stylesheet" href="${ctxStatic}/css/weui.min.css">
    <link rel="stylesheet" href="https://cdn.bootcss.com/jquery-weui/1.2.1/css/jquery-weui.min.css">
    <script src="${ctxStatic}/common/vue/js/vue.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue-resource.min.js"></script>
    <style type="text/css">
        body {
            margin: 0;
            padding: 0;
            height: 1000%;
            /*Firefox*/
        -moz-calc(expression);
            /*chrome safari*/
        -webkit-calc(expression);
            /*Standard */
        calc();
        }

        .all {
            width: 100%;
            height: 100%;
        }

        .bg {
            width: 33vw;
            height: 93%;
            box-sizing: border-box;
            border-right: 1px rgba(215, 215, 215, 0.6) solid;
            overflow: hidden;
        }

        body {}

        ul {
            list-style: none;
            padding: 0px;
            margin: 0;
            width: 33vw;
            /* background: rgba(0, 0, 0, 0.6); */
            /*定位 作为父级使用*/
            position: fixed;

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
            font-weight: bold;
        }

        ul li a {
            text-decoration: none;
            font-size: 14px;
            color: black;
        }

        ul li:hover>a {
            color: black;
            font-weight: bold;
        }

        /*滑动门*/
        .div2 {
            width: 67vw;

            /* background: skyblue; */
            /*使用定位实现滑动门-------重要步骤*/
            position: absolute;
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
            height: 90vh;
            text-align: center;
            /*justify-content: space-between;*/
            padding: 0px 10px 80px 10px;
            background: #FFFFFF;
            margin-bottom: 6.25rem;
            padding-bottom: 6.25rem;
            /* border: 1px red solid; */
            overflow: auto;
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
            padding:0 1rem;
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
<body>
<div id="page">
    <form id="Form" method="post" action="${ctx}/management/sobillandentry/sobill/save">
        <input type="hidden" id="status" name="status" value="0"/>
        <input type="hidden" id="number" name="number"/>

        <div class="weui-cells">
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>编号:</p>
                </div>
                <div class="weui-cell__ft"></div>
            </div>

            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>客户:</p>
                </div>
                <div class="weui-cell__ft"></div>
            </div>

            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>销售员:</p>
                </div>
                <div class="weui-cell__ft"></div>
            </div>

            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>状态:</p>
                </div>
                <div class="weui-cell__ft">草稿</div>
            </div>
        </div>

        <div id="items" class="weui-popup__container" style="z-index: 501;"><%-- 覆盖底部导航 --%>
            <div class="weui-popup__overlay"></div>
            <div class="weui-popup__modal">
                <div class="all">
                    <!-- 搜索框 -->
                    <div class="weui-search-bar" id="searchBar" style="height: 7%;">
                        <div v-on:click="open" class="weui-search-bar__form">
                            <div class="weui-search-bar__box">
                                <i class="weui-icon-search"></i>
                                <input type="search" class="weui-search-bar__input" id="searchInput" placeholder="搜索" required="">
                                <a v-on:click="empty" class="weui-icon-clear" id="searchClear"></a>
                            </div>
                            <label class="weui-search-bar__label" id="searchText">
                                <i class="weui-icon-search"></i>
                                <span>搜索</span>
                            </label>
                        </div>
                        <a v-on:click="close" class="weui-search-bar__cancel-btn" id="searchCancel">取消</a>
                    </div>
                    <!-- 滑动门 -->
                    <div class="bg">
                        <ul style="height: 100%;">
                            <li v-on:click="screenClass(itemClass.id)" :id="itemClass.id" v-for="itemClass in icitemClassList">
                                <a href="#" class="first">{{itemClass.name}}</a>
                                <div class="div2">
                                    <div class="banner" style="width: 100%;">
                                        111
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <a v-on:click="submitItems" class="weui-btn weui-btn_primary close-popup" data-target="#items">{{saveItems}}</a>
                </div>
            </div>
        </div>

        <div style="position: fixed;bottom: 10%;width: 100%;">
            <a href="javascript:;" class="weui-btn weui-btn_primary">{{submit}}</a>
        </div>

        <div id="function" class="weui-tabbar" style="position:fixed;bottom: 0px;">
            <a class="weui-tabbar__item open-popup" data-target="#items">
                <div class="weui-tabbar__icon">
                    <img src="${ctxStatic}/image/wechat/icon-add.png" alt="">
                </div>
                <p class="weui-tabbar__label">{{add}}</p>
            </a>
            <a href="javascript:;" class="weui-tabbar__item">
                <div class="weui-tabbar__icon">
                    <img src="${ctxStatic}/image/wechat/icon-delete.png" alt="">
                </div>
                <p class="weui-tabbar__label">{{del}}</p>
            </a>
        </div>
    </form>
</div>
<script src="https://cdn.bootcss.com/jquery/1.11.0/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/jquery-weui/1.2.1/js/jquery-weui.min.js"></script>
<script src="${ctxStatic}/js/jquery-2.1.4.js"></script>
<script type="text/javascript">

    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth()+1;
    var day = date.getDate();
    var number = 'SOB'+year+'-'+month+'-'+day;
    $("#number").val(number);

    var vm = new Vue({
        el:'#page',
        created: function () {
            this.$http.get('${ctxf}/wechat/icitem/getItemClass',{}).then(function (res) {
                this.icitemClassList = res.data.body.icitemClassList;
            });
        },
        data:{
            number:number,
            itemDetail:'商品明细',
            submit:'提交订单',
            add:'新增商品',
            del:'删除商品',
            saveItems:'保存商品',
            icitemClassList:[]
        },
        methods:{
            close(){
                $("#searchBar").removeClass("weui-search-bar_focusing");
            },
            open(){
                $("#searchBar").addClass("weui-search-bar_focusing")
            },
            empty(){
                $("#searchInput").val('');
            },
            submitItems(){
            },
            screenClass(classId){

            }
        }
    });

    $(function () {
        //进入页面,默认选中第一种类
        $("ul li:first").children('div').addClass('play');
        $("ul li:first").addClass('playdiv1');
        $(".first").addClass("fir")

        $("li").click(function() {
            $("ul li:first").removeClass('playdiv1');
            $(this).children('a').addClass('fir');
            $("a:not(this)").removeClass("fir");
            $("li:not(this)").children('div').removeClass('play');
            $(this).children('div').addClass('play');
        });
    });

</script>
</body>
</html>
