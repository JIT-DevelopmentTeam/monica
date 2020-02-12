<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta charset="UTF-8">
    <title>库存详情</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="stylesheet" href="${ctxStatic}/css/weui.min.css">
    <link rel="stylesheet" href="${ctxStatic}/css/jquery-weui.css">
    <script src="${ctxStatic}/js/jquery-2.1.4.js"></script>
    <script src="${ctxStatic}/js/jquery-weui.js"></script>
    <script src="${ctxStatic}/js/fastclick.js"></script>
    <script src="${ctxStatic}/js/wechat/jweixin-1.2.0.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue-resource.min.js"></script>
    <style type="text/css">
        body{
            background-color: #fff;
        }
        .weui-form-preview__bd {
            padding: 12px 16px 0px 16px;
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
        .placeholder {
            margin: 5px;
            padding: 0 10px;
            background-color: #e2e2e2;
            height: 2em;
            line-height: 2em;
            text-align: left;
            /*color: #cfcfcf;*/
            color: #8a8a8a;
        }
        .itemValue {
            position: absolute;
            left: 85px;
        }
    </style>
</head>
<body>
<div id="app">
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
                          {{stock.commodityNumber}}
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="weui-label">商品名称：</label></div>
                        <div class="weui-cell__bd itemValue">
                          {{stock.commodityName}}
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="weui-label">规格型号：</label></div>
                        <div class="weui-cell__bd itemValue">
                          {{stock.specification}}
                        </div>
                    </div>
                   <div class="weui-cell weui-cell_access">
                        <div class="weui-cell__hd"><label class="weui-label">当前库存：</label></div>
                       <div class="weui-cell__bd itemValue">
                          {{stock.total}}
                        </div>
                   </div>
                </span>
<%--                <div class="weui-flex weui-cell">--%>
<%--                    <div class="weui-flex__item">--%>
<%--                        <div class="placeholder">等级A：60</div>--%>
<%--                    </div>--%>
<%--                    <div class="weui-flex__item">--%>
<%--                        <div class="placeholder">等级B：70</div>--%>
<%--                    </div>--%>
<%--                    <div class="weui-flex__item">--%>
<%--                        <div class="placeholder">等级C：80</div>--%>
<%--                    </div>--%>
<%--                </div>--%>
            </div>
        </div>
    </div>
    <%--  --%>
    <div class="weui-flex">
        <div class="weui-flex__item">
            <div class="placeholder">仓库：{{stock.warehouse}}</div>
        </div>
        <div class="weui-flex__item">
            <div class="placeholder">仓位：{{stock.warehousePosition}}</div>
        </div>
    </div>
    <div class="weui-flex">
        <div class="weui-flex__item">
            <div class="placeholder">等级：{{stock.level}}</div>
        </div>
        <div class="weui-flex__item">
            <div class="placeholder">色号：{{stock.colorNumber}}</div>
        </div>
    </div>
    <div class="weui-flex">
        <div class="weui-flex__item">
            <div class="placeholder">批号：{{stock.batchNumber}}</div>
        </div>
        <div class="weui-flex__item">
            <div class="placeholder">数量：{{stock.total}}</div>
        </div>
    </div>
</div>
<script>
    var vm = new Vue({
        el: "#app",
        data: {
            stock: {}
        },
        methods: {

        },
        created: function(){
            this.$http.get('${ctxf}/wechat/stock/detailData', {
                params: {
                    commodityNumber: "${commodityNumber}"
                }
            }).then(res => {
                this.stock = res.body.body.data[0]
            })
        }
    });
</script>
</body>
</html>
