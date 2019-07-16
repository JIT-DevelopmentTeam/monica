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
            /*height: 1000%;*/
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
            background: rgba(0, 0, 0, 0.6);
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
            /* background: skyblue;*/
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
        <input type="hidden" id="number" name="number" value="${sobill.billNo}"/>

        <%-- 表头 --%>
        <div class="weui-cells">
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>编号:</p>
                </div>
                <div class="weui-cell__ft">${sobill.billNo}</div>
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

        <%-- 表体 --%>
        <strong>名称</strong>
        <div id="detail">

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
                        <ul>
                            <li :id="itemClass.id" v-for="(itemClass,index) in icitemClassList">
                                <a style="width: 100%;height: 100%;" v-on:click="screenClass(itemClass.id,index)">{{itemClass.name}}</a>
                                <div class="div2">
                                    <div class="banner" :id="index" style="width: 100%;">

                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div style="position: fixed;bottom: 0%;width: 100%;">
                        <div style="float: left;width: 48%;">
                            <a v-on:click="submitItems" class="weui-btn weui-btn_primary close-popup" data-target="#items">{{saveItems}}</a>
                        </div>
                        <div style="float: right;width: 48%;">
                            <a class="weui-btn weui-btn_warn close-popup" data-target="#items">{{cancel}}</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <br><br><br><br>
        <div style="position: fixed;bottom: 8%;width: 100%;">
            <div style="float: left;width: 48%;">
                <a class="weui-btn weui-btn_primary open-popup" data-target="#items">{{add}}</a>
            </div>
            <div style="float: right;width: 48%;">
                <a class="weui-btn weui-btn_warn">{{del}}</a>
            </div>
        </div>

        <div id="function" class="weui-tabbar" style="position:fixed;bottom: 0px;">
            <a class="weui-tabbar__item">
                <div class="weui-tabbar__icon">
                    <img src="${ctxStatic}/image/wechat/icon-add.png" alt="">
                </div>
                <p class="weui-tabbar__label">{{save}}</p>
            </a>
            <a href="javascript:;" class="weui-tabbar__item">
                <div class="weui-tabbar__icon">
                    <img src="${ctxStatic}/image/wechat/icon-add.png" alt="">
                </div>
                <p class="weui-tabbar__label">{{submit}}</p>
            </a>
        </div>
    </form>
</div>
<script src="https://cdn.bootcss.com/jquery/1.11.0/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/jquery-weui/1.2.1/js/jquery-weui.min.js"></script>
<script src="${ctxStatic}/js/jquery-2.1.4.js"></script>
<script type="text/javascript">

    var itemIds = new Array();

    var vm = new Vue({
        el:'#page',
        created: function () {
            this.$http.get('${ctxf}/wechat/icitem/getItemClass',{}).then(function (res) {
                this.icitemClassList = res.data.body.icitemClassList;
            });
        },
        data:{
            itemDetail:'商品明细',
            save:'保存',
            submit:'提交',
            add:'选择商品',
            del:'删除商品',
            saveItems:'保存',
            cancel:'取消',
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
                $("#detail").empty();
                $.ajax({
                   async:false,
                   cache:false,
                   url:'${ctxf}/wechat/icitem/findItemListByIds',
                   data:{
                      idsStr:itemIds.toString()
                   },
                   dataType: 'json',
                   success:function (res){
                        var template = '<div class="weui-cells weui-cells_checkbox">';
                        var icitemList = res.body.icitemList;
                       for (var i = 0; i < icitemList.length; i++) {
                           template += '<label class="weui-cell weui-check__label" for="'+icitemList[i].id+'Select">' +
                           '<div class="weui-cell__hd">' +
                           '<input id="'+icitemList[i].id+'Select" type="checkbox" class="weui-check"/>' +
                           '<i class="weui-icon-checked"></i>' +
                               '</div>' +
                               '<div class="weui-cell__bd">' +
                               '<p>'+icitemList[i].name+'</p>' +
                               '</div>' +
                               '</label>';
                       }
                        template += '</div>';
                       $("#detail").append(template);
                   }
                });
            },
            screenClass(classId,index){
                $("li:not("+classId+")").children('div').removeClass('play');
                $("#"+classId).children('div').addClass('play');
                $.ajax({
                   async:false,
                   cache:false,
                   url: '${ctxf}/wechat/icitem/getItemsListByClassId',
                   data:{
                       classId:classId
                   },
                   dataType:'json',
                   success:function (res) {
                        var icitemList = res.body.icitemList;
                        var template = '<div class="weui-cells weui-cells_checkbox">';
                        $("#"+index).empty();
                       for (var i = 0; i < icitemList.length; i++) {
                           var check = false;
                           for (var j = 0; j < itemIds.length; j++) {
                               if (itemIds[j] == icitemList[i].id){
                                   check = true;
                               }
                           }
                           template += '<label class="weui-cell weui-check__label" for="'+icitemList[i].id+'">' +
                               '<div class="weui-cell__hd">';
                               if(check){
                                   template += '<input id="'+icitemList[i].id+'" onclick="selectItems(\''+icitemList[i].id+'\');" type="checkbox" checked class="weui-check" name="items"/>';
                               } else {
                                   template += '<input id="'+icitemList[i].id+'" onclick="selectItems(\''+icitemList[i].id+'\');" type="checkbox" class="weui-check" name="items"/>';
                               }
                                template += '<i class="weui-icon-checked"></i>' +
                               '</div>' +
                               '<div class="weui-cell__bd">' +
                               '<p>'+icitemList[i].name+'</p>' +
                               '</div>' +
                               '</label>';
                       }
                       template += '</div>';
                       $("#"+index).append(template);
                   }
                });
            }
        }
    });

    /* 选中商品 */
    function selectItems(id) {
        var index;
        if (itemIds.indexOf(id) != -1) {
            if (!$("#"+id).prop("checked")) {
                index = retrieveArrayIndex(id);
                if (index != -1) {
                    itemIds.splice(index,1);
                }
            }
        } else {
            if ($("#"+id).prop("checked")) {
                itemIds.push(id);
            }
        }
    }

    /* 检索数组元素下标 */
    function retrieveArrayIndex(val) {
        for (var i = 0; i < itemIds.length; i++) {
            if (itemIds[i] == val){
                return i;
            }
        }
        return -1;
    }

</script>
</body>
</html>
