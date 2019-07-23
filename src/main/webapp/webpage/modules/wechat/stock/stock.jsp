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
    <script src="${ctxStatic}/js/jquery-2.1.4.js"></script>
    <script src="${ctxStatic}/js/jquery-weui.js"></script>
    <script src="${ctxStatic}/js/fastclick.js"></script>
    <script src="${ctxStatic}/js/wechat/jweixin-1.2.0.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue.js"></script>
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
            width: 73px;
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
    </style>
</head>
<body><div id="app">
<div class="weui-search-bar" id="searchBar">
    <a href="javascript:" class="weui-search-bar__box-btn" onclick="showFixed();">
        <img src="${ctxStatic}/image/wechat/icon/icon-screen_gray.png">
    </a>
    <form class="weui-search-bar__form" action="#">
        <div class="weui-search-bar__box">
            <i class="weui-icon-search"></i>
            <input type="search" class="weui-search-bar__input" id="searchInput" placeholder="搜索" required="">
            <a href="javascript:" class="weui-icon-clear" id="searchClear"></a>
        </div>
    </form>
    <a href="javascript:" class="weui-search-bar__cancel-btn" id="searchCancel">取消</a>
</div>
<div class="weui-form-preview">
    <div class="weui-form-preview__bd">
        <div class="weui-form-preview__item">
            <div class="weui-form-preview__label">
                <img src="" style="width: 70px; height: 70px;"/>
            </div>
            <span class="weui-form-preview__value">
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="weui-label">商品代码：</label></div>
                        <div class="weui-cell__bd">
                          <input class="weui-input" type="text" placeholder="请输入商品代码"/>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="weui-label">商品名称：</label></div>
                        <div class="weui-cell__bd">
                          <input class="weui-input" type="text" placeholder="请输入商品名称"/>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="weui-label">规格型号：</label></div>
                        <div class="weui-cell__bd">
                          <input class="weui-input" type="text" placeholder="请输入规格型号"/>
                        </div>
                    </div>
                   <a class="weui-cell weui-cell_access" onclick="goStockDetail();">
                        <div class="weui-cell__bd">
                            <label class="weui-label">当前库存：</label>
                        </div>
                        <div class="weui-cell__bd">
                            <div class="weui-cell__ft">详情</div>
                        </div>
                   </a>
                </span>
        </div>
    </div>
</div>
<div class="weui-form-preview">
    <div class="weui-form-preview__bd">
        <div class="weui-form-preview__item">
            <div class="weui-form-preview__label">
                <img src="" style="width: 70px; height: 70px;"/>
            </div>
            <span class="weui-form-preview__value">
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="weui-label">商品代码：</label></div>
                        <div class="weui-cell__bd">
                          <input class="weui-input" type="number" pattern="[0-9]*" placeholder="请输入商品代码"/>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="weui-label">商品名称：</label></div>
                        <div class="weui-cell__bd">
                          <input class="weui-input" type="number" pattern="[0-9]*" placeholder="请输入商品名称"/>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="weui-label">规格型号：</label></div>
                        <div class="weui-cell__bd">
                          <input class="weui-input" type="number" pattern="[0-9]*" placeholder="请输入规格型号"/>
                        </div>
                    </div>
                   <a class="weui-cell weui-cell_access" onclick="goStockDetail();">
                        <div class="weui-cell__bd">
                            <label class="weui-label">当前库存：</label>
                        </div>
                        <div class="weui-cell__bd">
                            <div class="weui-cell__ft">详情</div>
                        </div>
                   </a>
                </span>
        </div>
    </div>
</div>

<div class="fixed-body" id="fixed" style="display: none;">
    <div class="fixed-cell">
        <div class="fixed-cell_item weui-cells weui-cells_radio">
            <div class="fixed-cell_title">等级信息</div>

            <label class="weui-cell weui-check__label" for="x11">
                <div class="weui-cell__bd">
                    <p>A</p>
                </div>
                <div class="weui-cell__ft">
                    <input type="radio" class="weui-check" name="radio1" id="x11">
                    <span class="weui-icon-checked"></span>
                </div>
            </label>
            <label class="weui-cell weui-check__label" for="x12">
                <div class="weui-cell__bd">
                    <p>B</p>
                </div>
                <div class="weui-cell__ft">
                    <input type="radio" name="radio1" class="weui-check" id="x12">
                    <span class="weui-icon-checked"></span>
                </div>
            </label>
            <label class="weui-cell weui-check__label" for="x13">
                <div class="weui-cell__bd">
                    <p>C</p>
                </div>
                <div class="weui-cell__ft">
                    <input type="radio" name="radio1" class="weui-check" id="x13">
                    <span class="weui-icon-checked"></span>
                </div>
            </label>
        </div>
        <div class="fixed-cell_item">
            <div class="fixed-cell_title">色号信息</div>
            <div class="weui-cell">
                <div class="weui-cell__hd weui-label"></div>
                <div class="weui-cell__bd">
                    <input class="weui-input" id="color" name="color" type="text" placeholder="请输入色号" />
                </div>
                <div class="weui-cell__hd weui-label"></div>
            </div>
        </div>
        <div class="fixed-cell_item">
            <div class="button_sp_area" style="text-align: center;">
                <a href="javascript:;" class="weui-btn weui-btn_mini weui-btn_default" id="close" onclick="cancel()">关闭</a>
                <a href="javascript:;" class="weui-btn weui-btn_mini weui-btn_primary" id="open" onclick="confirm()">确定</a>
            </div>
        </div>
    </div>
</div>
</div>
<script >
    var vm = new Vue({
        el: "#app",
        data: {

        },
        methods: {
            /*//打开筛选
            showFixed:function(){
                d.getElementById('fixed').style.cssText  = "display:block"
            },
            // 筛选取消
            cancel:function(){
                d.getElementById('fixed').style.cssText  = "display:none"
            },
            // 筛选确认
            confirm:function(){
                d.getElementById('fixed').style.cssText  = "display:none"
            },
            // 跳转详情页面
            goStockDetail:function() {
                console.log("445564")
                window.location ="${ctxf}/wechat/stock/detail";
            }*/
        },
        mounted: function(){

        }
    });

    var d = document;
    //打开筛选
    function showFixed(){
        d.getElementById('fixed').style.cssText  = "display:block"
    }
    // 筛选取消
    function cancel(){
        d.getElementById('fixed').style.cssText  = "display:none"
    }
    // 筛选确认
    function confirm(){
        d.getElementById('fixed').style.cssText  = "display:none"
    }

    // 跳转详情页面
    function goStockDetail() {
        console.log("445564")
        window.location ="${ctxf}/wechat/stock/detail";
    }


</script>
</body>
</html>