<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta charset="UTF-8">
    <title>新增订单</title>
    <link rel="stylesheet" href="${ctxStatic}/css/weui.min.css">
    <script src="${ctxStatic}/css/jquery-weui.css"></script>
    <script src="${ctxStatic}/common/vue/js/vue.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue-router.min.js"></script>
</head>
<body>
<div id="page">
    <form id="Form" method="post" action="${ctx}/management/sobillandentry/sobill/save">
        <input type="hidden" id="status" name="status" value="0"/>
        <input type="hidden" id="number" name="number"/>
        <p>
            <router-link to="/home">home</router-link>
            <router-view></router-view>
        </p>

        <div class="weui-cells">
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>编号:</p>
                </div>
                <div class="weui-cell__ft">{{number}}</div>
            </div>

            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>客户:</p>
                </div>
                <div class="weui-cell__ft">{{cusName}}</div>
            </div>

            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>销售员:</p>
                </div>
                <div class="weui-cell__ft">{{empName}}</div>
            </div>

            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>状态:</p>
                </div>
                <div class="weui-cell__ft">草稿</div>
            </div>
        </div>


        <div style="position: fixed;bottom: 10%;width: 100%;">
            <a id="submit" href="javascript:;" class="weui-btn weui-btn_primary">{{submit}}</a>
        </div>

        <div id="function" class="weui-tabbar" style="position:fixed;bottom: 0px;">
            <a v-bind:href="selectItems" class="weui-tabbar__item">
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
<script src="${ctxStatic}/js/jquery-weui.js"></script>
<script src="${ctxStatic}/js/jquery-2.1.4.js"></script>
<script type="text/javascript">
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth()+1;
    var day = date.getDate();
    var number = 'SOB'+year+'-'+month+'-'+day;
    $("#number").val(number);

    const Home = { template:
            `<template id="template">
        <div class="weui-search-bar" id="searchBar" style="position: fixed;top: 0px;width: 100%;z-index: 2;">
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

        <br><br>

        <div id="items">
            <div class="weui-cells weui-cells_checkbox" v-for="item in icitemList">
                <label class="weui-cell weui-check__label" :for="item.id">
                    <div class="weui-cell__hd">
                        <input type="checkbox" class="weui-check" name="item" :id="item.id" :value="item.id">
                        <i class="weui-icon-checked"></i>
                    </div>
                    <div class="weui-cell__bd">
                        <span>名称:{{item.name}}&emsp;分类:{{item.itemClassName}}&emsp;单位:{{item.unit}}</span>
                    </div>
                </label>
            </div>
        </div>

        <br><br><br>

        <div style="position: fixed;bottom: 0px;z-index: 2;width: 100%;">
            <a v-on:click="submit" class="weui-btn weui-btn_primary">提交</a>
        </div>
    </template>` };

    const routes = [
        { path: '/', redirect: '/home' },
        {
            path: '/home',
            component: Home,
        },
    ]

    const router = new VueRouter({
        routes // （缩写）相当于 routes: routes
    })

    const app = new Vue({
        router
    }).$mount('#page')

    var vm = new Vue({
        el:'#page',
        data:{
            number:number,
            itemDetail:'商品明细',
            submit:'提交订单',
            add:'新增商品',
            selectItems:'${ctxf}/wechat/sobill/selectItems',
            del:'删除商品',

            icitemList:[]
        },
        methods:{
            close(){
                //$("#searchBar").removeClass("weui-search-bar_focusing");
            },
            open(){
                //$("#searchBar").addClass("weui-search-bar_focusing")
            },
            empty(){
                //$("#searchInput").val('');
            },
            submit(){
               // window.history.back('111');
            }
        }
    })


</script>
</body>
</html>
