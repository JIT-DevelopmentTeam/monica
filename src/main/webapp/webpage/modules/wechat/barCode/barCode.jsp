<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta charset="UTF-8">
    <title>条码追溯</title>
    <link rel="stylesheet" href="${ctxStatic}/css/wxqy/productFollow.css" charset="utf-8">
    <link rel="stylesheet" href="${ctxStatic}/css/wxqy/common.css" charset="utf-8">
    <link rel="stylesheet" href="${ctxStatic}/css/weui.css">
    <link rel="stylesheet" href="${ctxStatic}/css/jquery-weui.css">
    <script src="${ctxStatic}/js/jquery-2.1.4.js"></script>
    <script src="${ctxStatic}/js/jquery-weui.js"></script>
    <script src="${ctxStatic}/js/wechat/swiper.js"></script>
    <script src="${ctxStatic}/js/fastclick.js"></script>
    <script src="${ctxStatic}/js/wechat/jweixin-1.2.0.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue.js"></script>

</head>
<body>
<div class="productFollow">
    <div class="search-head">
        <div class="search-list">
            <img src="${ctxStatic}/image/wechat/icon-scan_gray.png" id="scanQRCode">
            <input class="search-input" type="text" name="" placeholder="请输入二维码或使用左侧扫码功能" id="QRCode">
            <img src="${ctxStatic}/image/wechat/icon-search_gray.png" id="search" onclick="searchCode()">
        </div>
    </div>
    <div class="proInfo-body">
        <div class="proInfo-cell_right">
            <div class="proInfo-list"><span>唯&nbsp;一&nbsp;码&nbsp;：</span><span id="primaryNo"></span></div>
            <div class="proInfo-list"><span>产品编号：</span><span id="itemNo"></span></div>
            <div class="proInfo-list"><span>产品名称：</span><span id="itemName"></span></div>
            <div class="proInfo-list"><span>规格型号：</span><span id="model"></span></div>
        </div>
        <div class="proImg-cell_left">
            <img src="${ctxStatic}/image/wechat/qr_code.png" title="图" id="picUrl">
            <p><a style="text-decoration:none;" id="bigCode">全屏看图</a></p>
        </div>
    </div>
    <div class="proValue-footer">
        <div class="proValue-list">
            <div class="proValue-list_item">
                <span>批&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</span>
                <span id="batchNo"></span>
            </div>
            <div class="proValue-list_item">
                <span>色&nbsp;&nbsp;&nbsp;号：</span>
                <span id="colorNo"></span>
            </div>
        </div>
        <div class="proValue-list">
            <div class="proValue-list_item">
                <span>生产日期：</span>
                <span id="proDate"></span>
            </div>
            <div class="proValue-list_item">
                <span>等&nbsp;&nbsp;&nbsp;级：</span>
                <span id="level"></span>
            </div>
        </div>
        <div class="proValue-list">
            <div class="proValue-list_item">
                <span>生产设备：</span>
                <span id="proEqu"></span>
            </div>
            <div class="proValue-list_item">
                <span>检查员：</span>
                <span id="checker"></span>
            </div>
        </div>
        <div class="proValue-list">
            <div class="proValue-list_item">
                <span>库存状态：</span>
                <span id="total"></span>
            </div>
        </div>
    </div>
    <div style="height: 40px"></div>
</div>

<script type="text/javascript">
    <%--var vm = new Vue({--%>
        <%--el: ".weui-panel__bd",--%>
        <%--data: {--%>
            <%--items: [],--%>
            <%--path: '${path}'--%>
        <%--},--%>
        <%--methods: {--%>
            <%--showData: function(){--%>
                <%--$.ajax({--%>
                    <%--type: "Post",--%>
                    <%--url: "${ctxf}/wechat/news/data",--%>
                    <%--data: {},--%>
                    <%--dataType: "json",--%>
                    <%--success: function(data) {--%>
                        <%--vm.items = data.newsList;--%>
                    <%--}--%>
                <%--});--%>
            <%--},--%>
            <%--detail: function (id) {--%>
                <%--window.location = "${ctxf}/wechat/news/form?id=" + id;--%>
            <%--},--%>
            <%--init: function () {--%>
                <%--if ($('#headLine').height() != 20) {--%>
                    <%--$('#headLine').css("display", "block");--%>
                    <%--$('#newList').css("border-top", "1px solid lightgrey");--%>
                <%--}--%>
            <%--}--%>
        <%--},--%>
        <%--mounted: function(){--%>
            <%--this.showData();--%>
        <%--},--%>
        <%--updated: function () {--%>
            <%--this.init();--%>
        <%--}--%>
    <%--});--%>
    $(function () {
        //查看全屏图
        $("#bigCode").click(function () {
            //获取放大的图片url
            var $picUrl = document.getElementById('picUrl');
            var pb = $.photoBrowser({
                items: [
                    $picUrl.src
                ],
                onSlideChange: function (index) {
                    //console.log(this, index);
                },
                onOpen: function () {
                    //console.log("onOpen", this);
                },
                onClose: function () {
                    //console.log("onClose", this);
                }
            });
            pb.open();
        });
        //配置调用手机微信功能
        $.ajax({
            url: "${ctxf}/wechat/barCode/config",
            data: {"url": encodeURIComponent(location.href.split('#')[0])},
            type: "get",
            dataType: "json",
            success: function (res) {
                if (res.errorCode === "403") {
                    $.toast(res.msg, "forbidden");
                } else {
                    var appId = res.body.config.appId;
                    var timestamp = res.body.config.timestamp;
                    var nonceStr = res.body.config.nonceStr;
                    var signature = res.body.config.signature;
                    // 通过config接口注入权限验证配置
                    wx.config({
                        debug: false, // 开启调试模式。
                        appId: appId, // 必填，公众号的唯一标识
                        timestamp: timestamp, // 必填，生成签名的时间戳
                        nonceStr: nonceStr, // 必填，生成签名的随机串
                        signature: signature,// 必填，签名
                        jsApiList: [
                            'checkJsApi',// 基础
                            'scanQRCode',// 微信扫一扫接口
                        ] // 必填，配置功能按钮
                    });
                }
            }
        });
        // 通过ready接口处理成功验证
        wx.ready(function () {
            wx.checkJsApi({  //判断当前客户端版本是否支持指定JS接口
                jsApiList: ['scanQRCode'],
                success: function (res) {// 以键值对的形式返回，可用true，不可用false。如：{"checkResult":{"scanQRCode":true},"errMsg":"checkJsApi:ok"}
                    if (res.checkResult.scanQRCode != true) {
                        alert('抱歉，当前客户端版本不支持扫一扫');
                    }
                }
            });
        });
        // 通过error接口处理失败验证
        wx.error(function (res) {
            alert(res.errMsg);
            //console.log("-->出错了<--：" + res.errMsg);//这个地方的好处就是wx.config配置错误，会弹出窗口哪里错误，然后根据微信文档查询即可。
        });
        //点击按钮扫描二维码
        $("#scanQRCode").click(function () {
            wx.scanQRCode({
                needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
                scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
                success: function (res) {
                    var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
                    let data = {
                        idCode: ''
                    }
                    if (result.indexOf("A.004") != -1) {
                        let arr = result.split(':');
                        data.idCode = arr[arr.length-1];
                    } else {
                        data.idCode = result;
                    }
                    //window.location.href = result;//因为我这边是扫描后有个链接，然后跳转到该页面
                    // alert("扫一扫返回来的数据:" + result);
                    $.ajax({
                        url: "${ctxf}/wechat/barCode/scanQRCode",
                        data: data,
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            if (data.errorCode === "403") {
                                $.toast(data.msg, "forbidden");
                            } else {
                                // alert("查询出来的数据：" + data.result.number);
                                $("#primaryNo").text(data.body.result[0].FIDCode);
                                $("#itemNo").text(data.body.result[0].FNumber);
                                $("#itemName").text(data.body.result[0].FName);
                                $("#model").text(data.body.result[0].FModel);
                                $("#batchNo").text(data.body.result[0].FBatchNo);
                                $("#colorNo").text(data.body.result[0].FColorNum);
                                $("#proDate").text(data.body.result[0].FBillDate);
                                $("#level").text(data.body.result[0].FGrade);
                                $("#proEqu").text(data.body.result[0].FDevice);
                                $("#checker").text(data.body.result[0].FChecker);
                                $("#total").text(data.body.result[0].FStockStatus);
                            }
                        }
                    });
                },
                error: function (res) {
                    alert(res.errMsg);
                    // console.log("异常");
                }
            });
        });
    })

    //搜索
    function searchCode() {
        var $QRCode = $("#QRCode").val();
        if ($QRCode != null && $QRCode != '') {
            $.ajax({
                url: "${ctxf}/wechat/barCode/searchCode",
                data: {"QRCode": $QRCode},
                type: "post",
                dataType: "json",
                success: function (data) {
                    if (data.errorCode === "403") {
                        $.toast(data.msg, "forbidden");
                    } else {
                        $("#primaryNo").text(data.body.result.erpId);
                        $("#itemNo").text(data.body.result.number);
                        $("#itemName").text(data.body.result.name);
                        $("#model").text(data.body.result.model);
                    }
                }
            });
        } else
            alert("请输入条码");
    }
</script>
</body>
</html>