<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta charset="UTF-8">
    <title>订单管理</title>
    <link rel="stylesheet" href="${ctxStatic}/css/weui.css">
    <script src="${ctxStatic}/js/jquery-weui.js"></script>
    <script src="${ctxStatic}/js/jquery-2.1.4.js"></script>
</head>
<body>
<div class="page">
    <div class="page__bd" style="height: 100%;">
        <div class="weui-tab">
            <div class="weui-navbar">
                <div id="toAudit" class="weui-navbar__item weui-bar__item_on" onclick="changeStyle('toAudit');">
                    待审核订单
                </div>
                <div id="history" class="weui-navbar__item" onclick="changeStyle('history');">
                    历史订单
                </div>
            </div>
            <div class="weui-tab__panel">
                <div id="toAuditDetail">
                    <c:forEach items="${toAuditList}" var="var" varStatus="vs">
                        <div class="weui-cell">
                            <div class="weui-cell__bd">
                                <p>编号:</p>
                            </div>
                            <div class="weui-cell__ft">${var.billNo}</div>
                        </div>

                        <div class="weui-cell">
                            <div class="weui-cell__bd">
                                <p>订单发货时间:</p>
                            </div>
                            <div class="weui-cell__ft"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${var.needTime}"/></div>
                        </div>

                        <div class="weui-cell">
                            <div class="weui-cell__bd">
                                <p>客户:</p>
                            </div>
                            <div class="weui-cell__ft">${var.cusName}</div>
                        </div>

                        <div class="weui-cell">
                            <div class="weui-cell__bd">
                                <p>销售员:</p>
                            </div>
                            <div class="weui-cell__ft">${var.empName}</div>
                        </div>

                        <div class="weui-cell">
                            <div class="weui-cell__bd">
                                <p>状态:</p>
                            </div>
                            <div class="weui-cell__ft"><c:if test="${var.status == 0}">草稿</c:if><c:if test="${var.status == 1}">提交</c:if></div>
                        </div>
                    </c:forEach>
                </div>

                <div id="historyDetail" style="display: none;">
                    <c:forEach items="${historyList}" var="var" varStatus="vs">
                        <div class="weui-cell">
                            <div class="weui-cell__bd">
                                <p>编号:</p>
                            </div>
                            <div class="weui-cell__ft">${var.billNo}</div>
                        </div>

                        <div class="weui-cell">
                            <div class="weui-cell__bd">
                                <p>订单发货时间:</p>
                            </div>
                            <div class="weui-cell__ft"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${var.needTime}"/></div>
                        </div>

                        <div class="weui-cell">
                            <div class="weui-cell__bd">
                                <p>客户:</p>
                            </div>
                            <div class="weui-cell__ft">${var.cusName}</div>
                        </div>

                        <div class="weui-cell">
                            <div class="weui-cell__bd">
                                <p>销售员:</p>
                            </div>
                            <div class="weui-cell__ft">${var.empName}</div>
                        </div>

                        <div class="weui-cell">
                            <div class="weui-cell__bd">
                                <p>状态:</p>
                            </div>
                            <div class="weui-cell__ft"><c:if test="${var.status == 0}">草稿</c:if><c:if test="${var.status == 1}">提交</c:if></div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>

    <div class="weui-loadmore">
        <i class="weui-loading"></i>
        <span class="weui-loadmore__tips">正在加载</span>
    </div>

    <br><br><br>

    <div class="weui-tabbar" style="position:fixed;bottom: 0px;">
        <a href="${ctx}/wechat/sobill" class="weui-tabbar__item weui-bar__item--on">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/icon-add.png" alt="">
            </div>
            <p class="weui-tabbar__label">新增</p>
        </a>
        <a href="javascript:;" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/icon-edit2_blue.png" alt="">
            </div>
            <p class="weui-tabbar__label">编辑</p>
        </a>
        <a href="javascript:;" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/icon-delete.png" alt="">
            </div>
            <p class="weui-tabbar__label">删除</p>
        </a>
        <a href="javascript:;" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/icon-search.png" alt="">
            </div>
            <p class="weui-tabbar__label">审核</p>
        </a>
    </div>
</div>

<script type="text/javascript">
    var loading = false;  //状态标记
    $(document.body).infinite().on("infinite", function() {
        if(loading) return;
        loading = true;
        setTimeout(function() {
            $("#list").append("<p> 我是新加载的内容 </p>");
            loading = false
        }, 1500);   //模拟延迟
    });

    function changeStyle(Id) {
        if (Id == 'toAudit') {
            if (!$("#" + Id).hasClass("weui-bar__item_on")) {
                $("#" + Id).addClass("weui-bar__item_on");
                $("#history").removeClass("weui-bar__item_on");
                $("#toAuditDetail").css("display", "block");
                $("#historyDetail").css("display", "none");
            }
        } else if (Id == 'history') {
            if (!$("#" + Id).hasClass("weui-bar__item_on")) {
                $("#" + Id).addClass("weui-bar__item_on");
                $("#toAudit").removeClass("weui-bar__item_on");
                $("#toAuditDetail").css("display", "none");
                $("#historyDetail").css("display", "block");
            }
        }
    }
</script>
</body>
</html>