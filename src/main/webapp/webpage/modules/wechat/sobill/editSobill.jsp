<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta charset="UTF-8">
    <title>编辑订单</title>
    <link rel="stylesheet" href="${ctxStatic}/css/weui.min.css">
    <link rel="stylesheet" href="${ctxStatic}/css/jquery-weui.min.css">
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
            height: 100%;
            box-sizing: border-box;
            border-right: 1px rgba(215, 215, 215, 0.6) solid;
            overflow: auto;
        }

        body {}

        ul {
            list-style: none;
            padding: 0px;
            margin: 0;
            width: 33vw;
            /*background: rgba(0, 0, 0, 0.6);*/
            /*定位 作为父级使用*/
            /*position: fixed;*/
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
            overflow: auto;
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
            height: 100%;
            text-align: center;
            /*justify-content: space-between;*/
            /*padding: 0px 10px 80px 10px;*/
            background: #FFFFFF;
            /*margin-bottom: 6.25rem;
            padding-bottom: 6.25rem;*/
            /* border: 1px red solid; */
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
        <input type="hidden" id="id" name="id" value="${sobill.id}"/>
        <%-- 表头 --%>
        <%--<div class="weui-cell">
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
                    <div class="weui-cell__ft">${sobill.cusName}</div>
                </div>

                <div class="weui-cell">
                    <div class="weui-cell__bd">
                        <p>销售员:</p>
                    </div>
                    <div class="weui-cell__ft">${sobill.empName}</div>
                </div>

                <div class="weui-cell">
                    <div class="weui-cell__bd">
                        <p>状态:</p>
                    </div>
                    <div class="weui-cell__ft"><c:if test="${sobill.status == 0}">草稿</c:if><c:if test="${sobill.status == 1}">提交</c:if></div>
                </div>
            </div>
        </div>--%>
        <div class="weui-cells weui-cells_form">
            <div class="weui-cell">
                <div class="weui-cell__hd"><label class="weui-label">编号:</label></div>
                <div class="weui-cell__bd">
                    ${sobill.billNo}
                </div>
            </div>

            <div class="weui-cell">
                <div class="weui-cell__hd"><label class="weui-label">客户:</label></div>
                <div class="weui-cell__bd">
                    ${sobill.cusName}
                </div>
            </div>

            <div class="weui-cell">
                <div class="weui-cell__hd"><label class="weui-label">销售员:</label></div>
                <div class="weui-cell__bd">
                    ${sobill.empName}
                </div>
            </div>

            <div class="weui-cell">
                <div class="weui-cell__hd"><label class="weui-label">状态:</label></div>
                <div class="weui-cell__bd">
                    <c:if test="${sobill.status == 0}">草稿</c:if><c:if test="${sobill.status == 1}">提交</c:if>
                </div>
            </div>
        </div>

        <%-- 表体 --%>
        <div id="detail">
            <c:forEach items="${sobill.sobillentryList}" var="var" varStatus="vs">
                <div class="weui-cells_checkbox">
                    <div id="${var.itemId}Detail">
                        <div style="float: left;width: 8%;">
                            <label class="weui-check__label" for="${var.itemId}Select">
                                <div class="weui-cell__hd">
                                    <input id="${var.itemId}Select" name="selectItems" type="checkbox" class="weui-check" value="${var.itemId}"/>
                                    <i class="weui-icon-checked"></i>
                                </div>
                            </label>
                        </div>
                        <div style="width: 92%;float: left;">
                            <div class="weui-cell__bd">
                                <div style="float: left;width: 100%;"><div style="float: left;"><span>编码:${var.number}</span></div></div>
                                <div style="float: left;width: 100%;"><div style="float: left;"><span>名称:${var.itemName}</span></div><div style="float:right;"><span>单位:${var.unit}</span></div></div>
                                <div style="float: left;width: 100%;"><div style="float: left;"><span>型号:${var.model}</span></div><div style="float: right;"><span>单价:</span></div></div>
                                <div style="float: left;width: 100%;"><div style="float: left;"><span>数量:<input type="number" id="${var.itemId}Qty" name="quantity" min="0" step="1" value="${var.auxqty}"/></span></div><div style="float: right;"><span>金额:<span name="amount">${var.amount}</span></span></div></div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div id="items" class="weui-popup__container" style="z-index: 501;"><%-- 覆盖底部导航 --%>
            <div class="weui-popup__overlay"></div>
            <div class="weui-popup__modal">
                <div class="all">
                    <!-- 搜索框 -->
                    <%--<div class="weui-search-bar" id="searchBar" style="height: 7%;">
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
                    </div>--%>
                    <!-- 滑动门 -->
                    <div class="bg">
                        <ul>
                            <li :id="itemClass.id" v-for="(itemClass,index) in icitemClassList">
                                <a v-on:click="screenClass(itemClass.id,index)">{{itemClass.name}}</a>
                                <div class="div2">
                                    <div class="banner" :id="index">

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

        <br><br><br>

        <div id="function" class="weui-tabbar" style="position:fixed;bottom: 0px;">
            <a class="weui-tabbar__item open-popup" data-target="#items">
                <div class="weui-tabbar__icon">
                    <img src="${ctxStatic}/image/wechat/icon-add.png" alt="">
                </div>
                <p class="weui-tabbar__label">{{add}}</p>
            </a>
            <a v-on:click="delItems" class="weui-tabbar__item open-popup">
                <div class="weui-tabbar__icon">
                    <img src="${ctxStatic}/image/wechat/icon-add.png" alt="">
                </div>
                <p class="weui-tabbar__label">{{del}}</p>
            </a>
            <a v-on:click="saveSob" class="weui-tabbar__item">
                <div class="weui-tabbar__icon">
                    <img src="${ctxStatic}/image/wechat/icon-add.png" alt="">
                </div>
                <p class="weui-tabbar__label">{{save}}</p>
            </a>
            <a v-on:click="submitSob" class="weui-tabbar__item">
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
<%--<script src="${ctxStatic}/js/jquery-2.1.4.js"></script>--%>
<script type="text/javascript">

    var itemIds = new Array();
    var itemIdsStr = '${itemIdsStr}';
    itemIds = itemIdsStr.split(",");

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
                $("#searchBar").addClass("weui-search-bar_focusing");
            },
            empty(){
                $("#searchInput").val('');
            },
            /* 提交商品 */
            submitItems(){
                var checkVals = [];
                var addVals = [];
                $("input[name='items']:checked").each(function () {
                    checkVals.push($(this).val());
                });
                var index;
                for (var i = 0; i < checkVals.length; i++) {
                    index = retrieveArrayIndex(checkVals[i]);
                    if (index == -1) {
                        itemIds.push(checkVals[i]);
                        addVals.push(checkVals[i]);
                    }
                }
                $.ajax({
                    async:false,
                    cache:false,
                    url:'${ctxf}/wechat/icitem/findItemListByIds',
                    data:{
                        idsStr:addVals.toString()
                    },
                    dataType: 'json',
                    success:function (res){
                        var template = '<div class="weui-cells_checkbox">';
                        var icitemList = res.body.icitemList;
                        for (var i = 0; i < icitemList.length; i++) {
                            template += '<div id="'+icitemList[i].id+'Detail">' +
                                '<div style="float: left;width: 8%;">'+
                                '<label class="weui-check__label" for="'+icitemList[i].id+'Select">' +
                                '<div class="weui-cell__hd">' +
                                '<input id="'+icitemList[i].id+'Select" name="selectItems" type="checkbox" class="weui-check" value="'+icitemList[i].id+'"/>' +
                                '<i class="weui-icon-checked"></i>' +
                                '</div>' +
                                '</label>'+
                                '</div>'+
                                '<div style="width: 92%;float: left;">'+
                                '<div class="weui-cell__bd">' +
                                '<div style="float: left;width: 100%;"><div style="float: left;"><span>编码:'+icitemList[i].number+'</span></div></div>' +
                                '<div style="float: left;width: 100%;"><div style="float: left;"><span>名称:'+icitemList[i].name+'</span></div><div style="float:right;"><span>单位:'+icitemList[i].unit+'</span></div></div>' +
                                '<div style="float: left;width: 100%;"><div style="float: left;"><span>型号:'+icitemList[i].model+'</span></div><div style="float: right;"><span>单价:</span></div></div>' +
                                '<div style="float: left;width: 100%;"><div style="float: left;"><span>数量:<input type="number" id="'+icitemList[i].id+'Qty" name="quantity" min="0" step="1"/></span></div><div style="float: right;"><span>金额:<span name="amount"></span></span></div></div>' +
                                '</div>'+
                                '</div>'+
                                '</div>';
                        }
                        template += '</div>';
                        $("#detail").append(template);
                    }
                });
            },
            /* 选择分类 */
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
                                '<div class="weui-cell__hd" style="float: left;">';
                            if(check){
                                template += '<input id="'+icitemList[i].id+'" v-on:click="selectItems(\''+icitemList[i].id+'\');" type="checkbox" checked class="weui-check" name="items" value="'+icitemList[i].id+'"/>';
                            } else {
                                template += '<input id="'+icitemList[i].id+'" v-on:click="selectItems(\''+icitemList[i].id+'\');" type="checkbox" class="weui-check" name="items" value="'+icitemList[i].id+'"/>';
                            }
                            template += '<i class="weui-icon-checked"></i>' +
                                '</div>' +
                                '<div class="weui-cell__bd">' +
                                '<p>编码:'+icitemList[i].number+'</p>'+
                                '<p>名称:'+icitemList[i].name+'</p>'+
                                '<p>单位:'+icitemList[i].unit+'</p>'+
                                '<p>型号:'+icitemList[i].model+'</p>'+
                                '<p>单价:</p>'+
                                '</div>' +
                                '</label>';
                        }
                        template += '</div>';
                        $("#"+index).append(template);
                    }
                });
            },
            /* 删除商品 */
            delItems:function () {
                var checkVals = [];
                $("input[name='selectItems']:checked").each(function () {
                    checkVals.push($(this).val());
                });
                if (checkVals.length == 0) {
                    $.alert("请选择要删除的商品!");
                    return;
                }
                var index;
                for (var i = 0; i < checkVals.length; i++) {
                    index = retrieveArrayIndex(checkVals[i]);
                    if (index != -1) {
                        itemIds.splice(index,1);
                        $("#"+checkVals[i]+"Detail").remove();
                    }
                }
            },
            /* 选中商品 */
            selectItems:function (){
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
            },
            /* 保存订单 */
            saveSob:function () {
                $.confirm("您确定要保存订单吗?", function() {
                    //点击确认后的回调函数
                    save(0);
                }, function() {
                    //点击取消后的回调函数
                });
            },
            submitSob:function () {
                $.confirm("您确定要提交订单吗?", function() {
                    //点击确认后的回调函数
                    save(1);
                }, function() {
                    //点击取消后的回调函数
                });
            }
        }
    });

    /* 检索数组元素下标 */
    function retrieveArrayIndex(val) {
        for (var i = 0; i < itemIds.length; i++) {
            if (itemIds[i] == val){
                return i;
            }
        }
        return -1;
    }

    /* 保存订单 type(0:保存草稿,1:提交) */
    function save(type) {
        if (itemIds.length == 0) {
            $.alert("请至少选择一个商品!");
            return;
        }
        var check = true;
        var json = '[';
        for (var i = 0; i < itemIds.length; i++) {
            if ($("#" + itemIds[i] + "Qty").val() == null || $("#" + itemIds[i] + "Qty").val() == '') {
                check = false;
            }
            json += '{"itemId":"'+itemIds[i]+'","auxqty":'+$("#"+itemIds[i]+"Qty").val()+"},";
        }
        json = json.substring(0,json.length-1);
        json += ']';
        if (!check){
            $.alert("请检查订单明细是否填写完整!");
            return
        }
        var id = $("#id").val();
        var data = {
            "id":id,
            "synStatus":0,
            "status":type,
            "cancellation":0,
            "checkStatus":0,
            "sobillentryList":json
        };
        $.ajax({
            async:false,
            cache:false,
            type:'post',
            url:'${ctxf}/wechat/sobill/saveSob',
            contentType: "application/json; charset=utf-8",
            data:JSON.stringify(data),
            processData : false,
            dataType:'json',
            success:function (res) {
                if (res.success) {
                    $.alert("保存成功!");
                    setTimeout(function(){ window.location.href = '${ctxf}/wechat/sobill/list' }, 3000);
                }
            },
            error:function () {
                $.alert("保存出错!");
            }
        });
    }

</script>
</body>
</html>
