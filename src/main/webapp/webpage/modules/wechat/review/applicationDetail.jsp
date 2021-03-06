<%@ page import="com.jeeplus.modules.sys.utils.DictUtils" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no,viewport-fit=cover">
    <meta charset="UTF-8">
    <title>申请详细</title>
    <link rel="stylesheet" href="${ctxStatic}/css/weui.min.css">
    <link rel="stylesheet" href="${ctxStatic}/css/jquery-weui.min.css">
    <link rel="stylesheet" href="${ctxStatic}/css/wxqy/editOrder.css">
    <link rel="stylesheet" href="${ctxStatic}/css/wxqy/saleOrder.css">
    <link rel="stylesheet" href="${ctxStatic}/css/wxqy/reviewList.css">
    <%-- 全面屏iphone适配 --%>
    <link rel="stylesheet" href="${ctxStatic}/css/wxqy/iphoneadaptation/iphoneadaptation.css">
    <script src="${ctxStatic}/common/vue/js/vue.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue-resource.min.js"></script>
    <style type="text/css">
        li{
            list-style-type :none;
        }
    </style>
</head>
<body ontouchstart style="background-color: white;">
<div id="page">
    <input type="hidden" id="sobillId" value="${sobill.id}"/>
    <%-- 表头 --%>
    <div class="order-panel">
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                编号
            </div>
            <div class="addOrder-list_ft">
                ${sobill.billNo}
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                <span>*</span>客户
            </div>
            <div class="addOrder-list_ft">
                ${sobill.cusName}
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                <span>*</span>类型
            </div>
            <div class="addOrder-list_ft">
                <c:forEach items="${sobillTypeList}" varStatus="vs" var="var">
                    <c:if test="${sobill.type == var.value}">${var.label}</c:if>
                </c:forEach>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                <span>*</span>发货日期
            </div>
            <div class="addOrder-list_ft">
                <fmt:formatDate value="${sobill.needTime}" pattern="yyyy-MM-dd"/>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                <span>*</span>跟单人员
            </div>
            <div class="addOrder-list_ft">
                ${sobill.followerName}
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                单据日期
            </div>
            <div class="addOrder-list_ft">
                <fmt:formatDate value="${sobill.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                备注
            </div>
            <div class="addOrder-list_ft">
                <input type="text" readonly class="weui-input"
                       style="text-align: right;" value="${sobill.remarks}"/>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                包装
            </div>
            <div class="addOrder-list_ft">
                <c:forEach items="${sobillRemark01List}" varStatus="vs" var="var">
                    <c:if test="${sobill.remark01 == var.value}">${var.label}</c:if>
                </c:forEach>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                镜面抛
            </div>
            <div class="addOrder-list_ft">
                <c:forEach items="${sobillRemark02List}" varStatus="vs" var="var">
                    <c:if test="${sobill.remark02 == var.value}">${var.label}</c:if>
                </c:forEach>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                胶水
            </div>
            <div class="addOrder-list_ft">
                <c:forEach items="${sobillRemark03List}" varStatus="vs" var="var">
                    <c:if test="${sobill.remark03 == var.value}">${var.label}</c:if>
                </c:forEach>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                标识要求
            </div>
            <div class="addOrder-list_ft">
                <c:forEach items="${sobillRemark04List}" varStatus="vs" var="var">
                    <c:if test="${sobill.remark04 == var.value}">${var.label}</c:if>
                </c:forEach>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                喷码
            </div>
            <div class="addOrder-list_ft">
                <c:forEach items="${sobillRemark05List}" varStatus="vs" var="var">
                    <c:if test="${sobill.remark05 == var.value}">${var.label}</c:if>
                </c:forEach>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                客户验货
            </div>
            <div class="addOrder-list_ft">
                <c:forEach items="${sobillRemark06List}" varStatus="vs" var="var">
                    <c:if test="${sobill.remark06 == var.value}">${var.label}</c:if>
                </c:forEach>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                跟柜物品
            </div>
            <div class="addOrder-list_ft">
                <c:forEach items="${sobillRemark07List}" varStatus="vs" var="var">
                    <c:if test="${sobill.remark07 == var.value}">${var.label}</c:if>
                </c:forEach>
            </div>
        </div>
        <div class="addOrder-list">
            <div class="addOrder-list_bd">
                物流
            </div>
            <div class="addOrder-list_ft">
                <c:forEach items="${sobillRemark08List}" varStatus="vs" var="var">
                    <c:if test="${sobill.remark08 == var.value}">${var.label}</c:if>
                </c:forEach>
            </div>
        </div>
        <div class="addOrder-list big-list">
            <textarea readonly class="addOrder-textarea" cols="30" rows="10" placeholder="其他特殊要求">${sobill.remark09}</textarea>
        </div>
        <div class="addOrder-list big-list">
            <textarea readonly class="addOrder-textarea" cols="30" rows="10" placeholder="付款计划">${sobill.remark10}</textarea>
        </div>
    </div>

    <%-- 表体 --%>
    <div id="detail" style="background: white;">
        <c:forEach items="${sobill.sobillentryList}" var="var" varStatus="vs">
            <div class="weui-cells_checkbox">
                <div class="pro-cell">
                    <div class="pro-list">
                        <div class="pro-item_left"><span>商品编码：</span> ${var.number}</div>
                    </div>
                    <div class="pro-list">
                        <div class="pro-item_left"><span>商品名称：</span> ${var.itemName}</div>
                        <div class="pro-item_right"><span>单位： </span> ${var.unit}</div>
                    </div>
                    <div class="pro-list">
                        <div class="pro-item_left"><span>规格型号：</span> ${var.model}</div>
                        <div class="pro-item_right"><span>单价： </span> <span class="price"><%--<fmt:formatNumber value="${var.amount}" pattern=".00" />--%></span>元
                        </div>
                    </div>
                    <div class="pro-list">
                        <div class="pro-item_left"><span><span style="color: red;">*</span>数量：</span>
                            ${var.auxqty}
                        </div>
                        <div class="pro-item_right"><span>金额： </span> <span class="total"><fmt:formatNumber
                                value="${var.amount}" pattern=".00"/></span>元
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <div id="spman"  >
        <div class="review-list">
            <div class="reviewInfo">
                <p style="color: #b2b2b2;">流程审批： </p>
                <c:forEach items="${orderApproveList}" var="var">
                    <div style="background-color: rgb(238, 238, 238);width: 100%;padding: 5px">
                        <li style="margin-left: 8%">
                            <p style="height: 32px;width: 100%;line-height: 32px;font-size: 10px;color: #353535" class="center">
                                <strong>${var.approvalEmpName}
                                    <c:if test="${var.status == 0 && var.isToapp == 1}">
                                        ○  <span style="color:blue;">审批中</span>
                                    </c:if>
                                    <c:if test="${var.status == 1}">
                                        ○  <span style="color: green;">已通过</span>
                                    </c:if>
                                    <c:if test="${var.status == 2}">
                                        ○  <span style="color:red;">已驳回</span>
                                    </c:if>
                                </strong>
                            </p>
                            <p style="height: 32px;width: 100%;line-height: 32px;font-size: 10px;color: #353535" class="center">
                                <strong>审核评议:</strong>
                                    ${var.remark}
                            </p>
                        </li>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <c:forEach items="${orderApproveList}" var="var">
        <c:if test="${(var.approvalEmplId.qyUserId eq qyUserId && (var.status == 0 && var.isToapp == 1))}">
            <br><br><br>

            <div id="footer" class="weui-tabbar" style="position:fixed;bottom: 0px;z-index: 500;">
                <a v-on:click="applicationApproved" class="weui-tabbar__item">
                    <div class="weui-tabbar__icon">
                        <img src="${ctxStatic}/image/wechat/receive.jpg" alt="">
                    </div>
                    <p class="weui-tabbar__label">{{pass}}</p>
                </a>
                <a v-on:click="rejectApplication" class="weui-tabbar__item">
                    <div class="weui-tabbar__icon">
                        <img src="${ctxStatic}/image/wechat/reject.jpg" alt="">
                    </div>
                    <p class="weui-tabbar__label">{{reject}}</p>
                </a>
            </div>
        </c:if>
    </c:forEach>
</div>
<script src="https://cdn.bootcss.com/jquery/1.11.0/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/jquery-weui/1.2.1/js/jquery-weui.min.js"></script>
<script type="text/javascript">


    var vm = new Vue({
        el: '#page',
        data: {
            pass:'通过',
            reject:'驳回'
        },
        methods: {
            applicationApproved:function () {
                reviewOrder(1);
            },
            rejectApplication:function () {
                reviewOrder(2);
            }
        }
    });

    // 执行审核订单
    function reviewOrder(status) {
        var confirmText = '';
        if (status == 1) {
            confirmText = '您确定通过该订单吗?';
        } else {
            confirmText = '您确定驳回该订单吗?';
        }
        $.confirm(confirmText, function() {
            //点击确认后的回调函数
            $.prompt("请输入审核评议!", function(text) {
                //点击确认后的回调函数
                $.showLoading("数据加载中");
                $.ajax({
                    async:false,
                    cache:false,
                    url:'${ctxf}/wechat/review/reviewOrder',
                    type:'post',
                    data:{
                        sobillId:$("#sobillId").val(),
                        status:status,
                        qyUserId:'${qyUserId}',
                        remark:text
                    },
                    dataType:'json',
                    success:function (res) {
                        if (res.errorCode === "403") {
                            $.toast(res.msg, "forbidden");
                        } else {
                            setTimeout(function () {
                                $.hideLoading();
                                $.toast(res.msg);
                                window.location.href=document.referrer;
                            }, 3000);
                        }
                    },
                    error:function () {
                        $.toast("操作出错!","cancel");
                    }
                });
            }, function() {
                //点击取消后的回调函数
            });
        }, function() {
            //点击取消后的回调函数
        });
    }

</script>
</body>
</html>
