<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta charset="UTF-8">
    <title>选择商品</title>
    <link rel="stylesheet" href="${ctxStatic}/css/weui.min.css">
    <script src="${ctxStatic}/css/jquery-weui.css"></script>
    <script src="${ctxStatic}/common/vue/js/vue.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue-resource.min.js"></script>
</head>
<body>
    <div id="page">
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
    </div>

    <script src="${ctxStatic}/js/jquery-weui.js"></script>
    <script src="${ctxStatic}/js/jquery-2.1.4.js"></script>
    <script type="text/javascript">
        var vm = new Vue({
           el:'#page',
           created: function () {
               this.$http.get('${ctx}/management/icitemclass/icitem/getItemsList',{}).then(function (res) {
                   this.icitemList = res.data.body.icitemList;
               });
           },
           data:{
               close:function(){
                 $("#searchBar").removeClass("weui-search-bar_focusing");
               },
               open:function(){
                   $("#searchBar").addClass("weui-search-bar_focusing")
               },
               empty:function(){
                 $("#searchInput").val('');
               },
               submit:function(){
                   window.history.go(-1);
               },
               icitemList:[]
           }
        });

        document.onkeydown = function (event) {
            var e = event || window.event;
            if (e && e.keyCode == 13) { //回车键的键值为13
                var name = $("#searchInput").val();
                $.ajax({
                   async:false,
                   cache:false,
                   url:'${ctx}/management/icitemclass/icitem/getListByName',
                   data:{
                       name:name
                   },
                   dataType:'json',
                   success:function (res) {
                        var icitemList = res.body.icitemList;
                        $("#items").empty();
                        var temple = '';
                       for (var i = 0; i < icitemList.length; i++) {
                           temple += '<div class="weui-cells weui-cells_checkbox">';
                           temple += '<label class="weui-cell weui-check__label" for="'+icitemList[i].id+'">';
                           temple += '<div class="weui-cell__hd">';
                           temple += '<input type="checkbox" class="weui-check" name="item" id="'+icitemList[i].id+'" value="'+icitemList[i].id+'">';
                           temple += '<i class="weui-icon-checked"></i>';
                           temple += '</div>';
                           temple += '<div class="weui-cell__bd">';
                           temple += '<span>名称:'+icitemList[i].name+'&emsp;分类:'+icitemList[i].itemClassName+'&emsp;单位:'+icitemList[i].unit+'</span>';
                           temple += '</div>'
                           temple += '</label>';
                           temple += '</div>';
                       }
                       $("#items").append(temple);
                   }
                });
            }
        };
    </script>
</body>
</html>
