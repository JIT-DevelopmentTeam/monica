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
                   <div class="weui-cell weui-cell_access">
                        <div class="weui-cell__bd">
                            <label class="weui-label">当前库存：</label>
                        </div>
                        <div class="weui-cell__bd">
                            <div class="weui-cell__ft">说明</div>
                        </div>
                   </div>
                </span>
                <div class="weui-flex weui-cell">
                    <div class="weui-flex__item">
                        <div class="placeholder">等级A：60</div>
                    </div>
                    <div class="weui-flex__item">
                        <div class="placeholder">等级B：70</div>
                    </div>
                    <div class="weui-flex__item">
                        <div class="placeholder">等级C：80</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%--  --%>
    <div class="weui-flex">
        <div class="weui-flex__item">
            <div class="placeholder">仓库：C-E区</div>
        </div>
        <div class="weui-flex__item">
            <div class="placeholder">仓位：5-9A</div>
        </div>
    </div>
    <div class="weui-flex">
        <div class="weui-flex__item">
            <div class="placeholder">等级：A</div>
        </div>
        <div class="weui-flex__item">
            <div class="placeholder">色号：#88995</div>
        </div>
    </div>
    <div class="weui-flex">
        <div class="weui-flex__item">
            <div class="placeholder">批号：56GE855001</div>
        </div>
        <div class="weui-flex__item">
            <div class="placeholder">数量：80000</div>
        </div>
    </div>
</div>
</body>
</html>
