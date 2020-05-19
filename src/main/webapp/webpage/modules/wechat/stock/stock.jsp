<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta charset="UTF-8">
    <title>库存查询</title>
    <link rel="stylesheet" href="${ctxStatic}/css/weui.min.css">
    <link rel="stylesheet" href="${ctxStatic}/css/jquery-weui.css">
    <script src="${ctxStatic}/common/vue/js/vue.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue-resource.min.js"></script>
    <style type="text/css">
        .weui-form-preview__bd {
            padding: 10px 15px 0px 15px;
            font-size: .9em;
            text-align: right;
            color: #999;
            line-height: 2;
        }
        .weui-label {
            display: block;
            width: 74px;
            word-wrap: break-word;
            word-break: break-all;
        }
        .weui-cell {
            padding: 3px 6px;
            position: relative;
            /* display: -webkit-box; */
            /* display: -webkit-flex; */
            display: flex;
            -webkit-box-align: center;
            /* -webkit-align-items: center; */
            align-items: center;
        }
        .weui-input {
            width: 100%;
        }
        a img {
            margin-top: 0.25rem;
            border: 0;
            width: 65%;
            height: 75%;
        }
        .fixed-body{
            position: absolute;
            width: 100%;
            height: 100vh;
            background: rgba(0, 0, 0, 0.32);
            top: 2.5rem;
            left: 0;
            z-index: 100;
            display: none;
        }
        .fixed-cell{
            margin-top: -1rem;
            background: #fff;
        }
        .fixed-cell_title{
            /*background: #c9def1;*/
            background: rgba(203, 203, 203, 0.25);
            padding: 5px 15px
        }
        .fixed-cell_item{
            text-align: center;
            padding-bottom: 2px;
            border-bottom:1px solid #f7f7f7;
        }
        .weui-cells_radio .weui-check+.weui-icon-checked{
            min-width: 0px;
        }
        .itemValue {
            position: absolute;
            left: 85px;
        }
    </style>
</head>
<body ontouchstart>
    <div id="app">
        <div class="weui-search-bar" id="searchBar">
            <a href="javascript:" class="weui-search-bar__box-btn" @click="showFixed()">
                <img src="${ctxStatic}/image/wechat/icon-screen_gray.png">
            </a>
            <%--<form class="weui-search-bar__form" action="#">
                <div class="weui-search-bar__box">
                    <i class="weui-icon-search"></i>
                    <input type="search" class="weui-search-bar__input" id="searchInput" placeholder="搜索" required="">
                    <a href="javascript:" class="weui-icon-clear" id="searchClear"></a>
                </div>
            </form>
            <a href="javascript:" class="weui-search-bar__cancel-btn" id="searchCancel">取消</a>--%>
        </div>
        <div id="list">
            <div v-for="(item, index) in itemList">
                <div class="weui-form-preview">
                    <div class="weui-form-preview__bd">
                        <div class="weui-form-preview__item">
                            <div class="weui-form-preview__label">
                                <img src="" style="width: 70px; height: 70px;"/>
                            </div>
                            <span class="weui-form-preview__value">
                                <div class="weui-cell">
                                    <div class="weui-cell__hd"><label class="weui-label">商品代码：</label></div>
                                    <div class="weui-cell__bd itemValue">
                                        {{item.commodityNumber}}
<%--                                      <input class="weui-input" type="text" placeholder="请输入商品代码" value=""/>--%>
                                    </div>
                                </div>
                                <div class="weui-cell">
                                    <div class="weui-cell__hd"><label class="weui-label">商品名称：</label></div>
                                    <div class="weui-cell__bd itemValue">
                                        {{item.commodityName}}
<%--                                      <input class="weui-input" type="text" placeholder="请输入商品名称" value=""/>--%>
                                    </div>
                                </div>
                                <div class="weui-cell">
                                    <div class="weui-cell__hd"><label class="weui-label">规格型号：</label></div>
                                    <div class="weui-cell__bd itemValue">
                                        {{item.specification}}
<%--                                      <input class="weui-input" type="text" placeholder="请输入规格型号" value=""/>--%>
                                    </div>
                                </div>
                               <a class="weui-cell weui-cell_access" @click="item.total === 0 ? '' : goStockDetail(item.commodityNumber, item.batchNumber, item.warehouse)">
                                   <div class="weui-cell__hd"><label class="weui-label">当前库存：</label></div>
                                   <div class="weui-cell__bd itemValue">
                                        {{item.total}}
                                    </div>
                                    <div class="weui-cell__bd">
                                        <div class="weui-cell__ft">详情</div>
                                    </div>
                               </a>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
            <div class='demos-content-padded'>
                <div class="weui-loadmore" v-show="loading">
                    <i class="weui-loading"></i>
                    <span class="weui-loadmore__tips">正在加载</span>
                </div>
                <div class="weui-loadmore weui-loadmore_line" v-show="alled">
                    <span class="weui-loadmore__tips">暂无数据</span>
                </div>
            </div>
        </div>

        <div class="fixed-body" id="fixed" :style="fixedDisplay">
            <div class="fixed-cell">
                <div class="fixed-cell_item weui-cells">
                    <div class="fixed-cell_title">商品信息</div>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <input class="weui-input" v-model="item" type="text" placeholder="请输入名称、代码、规格，%号分隔" style="text-align: center;" />
                        </div>
                    </div>
                </div>
                <div class="fixed-cell_item weui-cells_checkbox">
                    <div class="fixed-cell_title">等级信息</div>
                    <div class="weui-flex">
                        <div class="weui-flex__item" v-for="(item, index) in levelList">
                            <label class="weui-cell weui-check__label" :for="index">
                                <div class="weui-cell__hd">
                                    <input type="checkbox" class="weui-check" name="checkbox" :value="item.FGrade" :id="index" :checked="item.checked" @change="changeState(item.FGrade)">
                                    <i class="weui-icon-checked"></i>
                                </div>
                                <div class="weui-cell__bd">
                                    <p>{{ item.FGrade }}</p>
                                </div>
                            </label>
                        </div>
                    </div>
                </div>
                <div class="fixed-cell_item">
                    <div class="fixed-cell_title">色号信息</div>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <input class="weui-input" v-model="color" type="text" placeholder="请输入色号" style="text-align: center;" />
                        </div>
                    </div>
                </div>
                <div class="fixed-cell_item">
                    <div class="fixed-cell_title">生产日期</div>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <input type="text" id="startTime" ref="startTime" readonly class="weui-input"
                                   placeholder="请选择开始时间" style="text-align: center;width:47%;"/>
                            至
                            <input type="text" id="endTime" ref="endTime" readonly class="weui-input"
                                   placeholder="请选择结束时间" style="text-align: center;width:45%;"/>
                        </div>
                    </div>
                </div>
                <div class="fixed-cell_item">
                    <div class="button_sp_area" style="text-align: center;">
                        <a href="javascript:;" class="weui-btn weui-btn_mini weui-btn_primary" id="open" @click="confirm()">确定</a>
                        <a href="javascript:;" class="weui-btn weui-btn_mini weui-btn_default" id="close" @click="cancel()">取消</a>
                    </div>
                </div>
            </div>
        </div>
        <div id="loadDiv" style="display: none;" class="weui-toast weui_loading_toast weui-toast--visible">
            <div class="weui_loading"><i class="weui-loading weui-icon_toast"></i></div>
            <p class="weui-toast_content">数据加载中</p>
        </div>
    </div>
<script src="${ctxStatic}/js/jquery-2.1.4.js"></script>
<script src="${ctxStatic}/js/fastclick.js"></script>
<script>
    $(function() {
        FastClick.attach(document.body);
    });
</script>
<script src="${ctxStatic}/js/jquery-weui.js"></script>
<script>
    var loading = false;
    $(document.body).infinite().on("infinite", function() {
        if(loading) return;
        loading = true;
        if (vm.itemList.length === vm.total && $("#loadDiv").is(":hidden")) {
            vm.alled = true
        }
        if (!vm.alled) {
            vm.pageNo += 1;
            vm.loading = true;
            setTimeout(function() {
                loadList();
                loading = false;
            }, 2000);
        }
    });
    
    function loadList() {
        var result = [];
        var checked = [];
        for (let i = 0; i < vm.levelList.length; i++) {
            if (vm.levelList[i].checked) {
                checked.push(vm.levelList[i].FGrade)
            }
        }
        vm.$http.get('${ctxf}/wechat/stock/listData', {
            params: {
                pageNo: vm.pageNo,
                item: vm.item,
                levelArr: checked,
                color: vm.color,
                startTime: vm.$refs.startTime.value,
                endTime: vm.$refs.endTime.value
            }
        }).then(res => {
            vm.loading = false;
            result = res.body.body.stockList;
            for (let i = 0; i < result.length; i++) {
                vm.itemList.push(result[i]);
            }
        });
    }
</script>
<script >
    var vm = new Vue({
        el: "#app",
        data: {
            pageNo: 1,
            itemList: [],
            loading: false,
            alled: false,
            total: 0,
            levelList: [],
            item: '',
            color: '',
            fixedDisplay: {display: 'block'}
        },
        methods: {
            // 跳转详情页面
            goStockDetail:function(commodityNumber, batchNumber, warehouse) {
                window.location ="${ctxf}/wechat/stock/detail?commodityNumber=" + commodityNumber + "&batchNumber=" + batchNumber + "&warehouse=" + warehouse;
            },
            // 条件筛选显示与否
            showFixed: function () {
                if (this.fixedDisplay.display == 'none') {
                    this.fixedDisplay.display = 'block';
                } else {
                    this.fixedDisplay.display = 'none';
                }
            },
            // 取消按钮
            cancel: function () {
                this.fixedDisplay.display = 'none';
            },
            // 确认按钮
            confirm: function () {
                this.fixedDisplay.display = 'none';
                this.itemList = [];
                var checked = [];
                for (let i = 0; i < this.levelList.length; i++) {
                    if (this.levelList[i].checked) {
                        checked.push(this.levelList[i].FGrade)
                    }
                }

                $("#loadDiv").show();
                this.$http.get('${ctxf}/wechat/stock/listData', {
                    params: {
                        pageNo: this.pageNo,
                        item: this.item,
                        levelArr: checked,
                        color: this.color,
                        startTime: this.$refs.startTime.value,
                        endTime: this.$refs.endTime.value
                    }
                }).then(res => {
                    $("#loadDiv").hide();
                    console.log("itemList.size: " + res.body.body.stockList.length + "\ntotal: " + res.body.body.total)
                    this.itemList = res.body.body.stockList;
                    console.log(this.itemList)
                    this.total = res.body.body.total;
                })
            },
            changeState: function (value) {
                for (let i = 0; i < this.levelList.length; i++) {
                    if (this.levelList[i].FGrade == value) {
                        if (this.levelList[i].checked) {
                            this.levelList[i].checked = false;
                            console.log('未选中')
                        } else {
                            this.levelList[i].checked = true;
                            console.log('选中')
                        }
                    }
                }
            }
        },
        created: function(){
            this.$http.get('${ctxf}/wechat/stock/getLeval')
                .then(res => {
                    var dataList = res.data.body.data
                    for (let i = 0; i < dataList.length; i++) {
                        dataList[i].checked = false;
                    }
                    this.levelList = dataList
                })
        }
    });

    $("#startTime").calendar({
        dateFormat: "yyyy-mm-dd"
    });
    $("#endTime").calendar({
        dateFormat: "yyyy-mm-dd"
    });

</script>
</body>
</html>