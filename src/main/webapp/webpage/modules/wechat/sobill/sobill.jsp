<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta charset="UTF-8">
    <title>订单管理</title>
    <link rel="stylesheet" href="${ctxStatic}/css/weui.min.css">
    <script src="${ctxStatic}/css/jquery-weui.css"></script>
    <script src="${ctxStatic}/common/vue/js/vue.js"></script>
    <script src="${ctxStatic}/common/vue/js/vue-resource.min.js"></script>
</head>
<body>
<div id="page" class="page">
    <div class="page__bd" style="height: 100%;">
        <div class="weui-tab">
            <div class="weui-navbar" style="position: fixed;top: 0px;">
                <div id="toAudit" class="weui-navbar__item weui-bar__item_on" onclick="changeStyle('toAudit');">
                    待审核订单
                </div>
                <div id="history" class="weui-navbar__item" onclick="changeStyle('history');">
                    历史订单
                </div>
            </div>
            <div class="weui-tab__panel">
                <div id="toAuditDetail">
                    <div class="weui-cells" v-for="toAudit in toAuditList">
                        <div class="weui-cells_radio">
                            <label class="weui-cell weui-check__label" :for="'toAudit'+toAudit.id">
                                <div class="weui-cell__bd">
                                    <div class="weui-cell">
                                        <div class="weui-cell__bd">
                                            <p>编号:</p>
                                        </div>
                                        <div class="weui-cell__ft">{{toAudit.billNo}}</div>
                                    </div>

                                    <div class="weui-cell">
                                        <div class="weui-cell__bd">
                                            <p>订单发货时间:</p>
                                        </div>
                                        <div class="weui-cell__ft">{{toAudit.needTime}}</div>
                                    </div>

                                    <div class="weui-cell">
                                        <div class="weui-cell__bd">
                                            <p>客户:</p>
                                        </div>
                                        <div class="weui-cell__ft">{{toAudit.cusName}}</div>
                                    </div>

                                    <div class="weui-cell">
                                        <div class="weui-cell__bd">
                                            <p>销售员:</p>
                                        </div>
                                        <div class="weui-cell__ft">{{toAudit.empName}}</div>
                                    </div>

                                    <div class="weui-cell">
                                        <div class="weui-cell__bd">
                                            <p>状态:</p>
                                        </div>
                                        <div class="weui-cell__ft" v-if="toAudit.status === 0">草稿</div>
                                        <div class="weui-cell__ft" v-else>提交</div>
                                    </div>
                                </div>
                                <div class="weui-cell__ft">
                                    <input type="radio" class="weui-check" name="toAudit" :id="'toAudit'+toAudit.id" :value="toAudit.id"/>
                                    <span class="weui-icon-checked"></span>
                                </div>
                            </label>
                        </div>
                    </div>

                    <div id="loadToAudit" class="weui-loadmore">
                        <i class="weui-loading"></i>
                        <span class="weui-loadmore__tips">正在加载...</span>
                    </div>

                    <div id="endToAudit" class="weui-loadmore weui-loadmore_line weui-loadmore_dot">
                        <span class="weui-loadmore__tips" style="background-color: #F6F6F6;">加载完毕</span>
                    </div>
                </div>

                <div id="historyDetail" style="display: none;">
                    <div class="weui-cells" v-for="history in historyList">
                        <div class="weui-cells_radio">
                            <label class="weui-cell weui-check__label" :for="'history'+history.id">
                                <div class="weui-cell__bd">
                                    <div class="weui-cell">
                                        <div class="weui-cell__bd">
                                            <p>编号:</p>
                                        </div>
                                        <div class="weui-cell__ft">{{history.billNo}}</div>
                                    </div>

                                    <div class="weui-cell">
                                        <div class="weui-cell__bd">
                                            <p>订单发货时间:</p>
                                        </div>
                                        <div class="weui-cell__ft">{{history.needTime}}</div>
                                    </div>

                                    <div class="weui-cell">
                                        <div class="weui-cell__bd">
                                            <p>客户:</p>
                                        </div>
                                        <div class="weui-cell__ft">{{history.cusName}}</div>
                                    </div>

                                    <div class="weui-cell">
                                        <div class="weui-cell__bd">
                                            <p>销售员:</p>
                                        </div>
                                        <div class="weui-cell__ft">{{history.empName}}</div>
                                    </div>

                                    <div class="weui-cell">
                                        <div class="weui-cell__bd">
                                            <p>状态:</p>
                                        </div>
                                        <div class="weui-cell__ft" v-if="history.status === 0">草稿</div>
                                        <div class="weui-cell__ft" v-else>提交</div>
                                    </div>
                                </div>
                                <div class="weui-cell__ft">
                                    <input type="radio" class="weui-check" name="history" :id="'history'+history.id" :value="history.id"/>
                                    <span class="weui-icon-checked"></span>
                                </div>
                            </label>
                        </div>
                    </div>

                    <div id="loadHistory" class="weui-loadmore">
                        <i class="weui-loading"></i>
                        <span class="weui-loadmore__tips">正在加载...</span>
                    </div>

                    <div id="endHistory" class="weui-loadmore weui-loadmore_line weui-loadmore_dot">
                        <span class="weui-loadmore__tips" style="background-color: #F6F6F6;">加载完毕</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <br><br><br>

    <div class="weui-tabbar" style="position:fixed;bottom: 0px;">
        <a v-bind:href="addHref" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/icon-add.png" alt="">
            </div>
            <p class="weui-tabbar__label">{{add}}</p>
        </a>
        <a v-bind:href="editHref" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/icon-edit2_blue.png" alt="">
            </div>
            <p class="weui-tabbar__label">{{edit}}</p>
        </a>
        <a v-on:click="delectById" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/icon-delete.png" alt="">
            </div>
            <p class="weui-tabbar__label">{{del}}</p>
        </a>
        <a v-on:click="checkSobill" class="weui-tabbar__item">
            <div class="weui-tabbar__icon">
                <img src="${ctxStatic}/image/wechat/icon-search.png" alt="">
            </div>
            <p class="weui-tabbar__label">{{check}}</p>
        </a>
    </div>
</div>

<!--BEGIN dialog1-->
<div class="js_dialog" id="iosDialog1" style="display: none;">
    <div class="weui-mask"></div>
    <div class="weui-dialog">
        <div class="weui-dialog__hd"><strong class="weui-dialog__title">操作反馈</strong></div>
        <div class="weui-dialog__bd">您确定要将该订单删除吗?</div>
        <div class="weui-dialog__ft">
            <a onclick="closeWindow('iosDialog1');" class="weui-dialog__btn weui-dialog__btn_default">取消</a>
            <a onclick="delect();" class="weui-dialog__btn weui-dialog__btn_primary">确定</a>
        </div>
    </div>
</div>
<!--END dialog1-->

<!--BEGIN dialog1-->
<div class="js_dialog" id="iosDialog3" style="display: none;">
    <div class="weui-mask"></div>
    <div class="weui-dialog">
        <div class="weui-dialog__hd"><strong class="weui-dialog__title">操作反馈</strong></div>
        <div class="weui-dialog__bd">您确定要审核该订单吗?</div>
        <div class="weui-dialog__ft">
            <a onclick="closeWindow('iosDialog3');" class="weui-dialog__btn weui-dialog__btn_default">取消</a>
            <a onclick="check();" class="weui-dialog__btn weui-dialog__btn_primary">确定</a>
        </div>
    </div>
</div>
<!--END dialog1-->

<!--BEGIN dialog2-->
<div class="js_dialog" id="iosDialog2" style="display: none;">
    <div class="weui-mask"></div>
    <div class="weui-dialog">
        <div class="weui-dialog__bd" id="title"></div>
        <div class="weui-dialog__ft">
            <a onclick="closeWindow('iosDialog2');" class="weui-dialog__btn weui-dialog__btn_primary">知道了</a>
        </div>
    </div>
</div>
<!--END dialog2-->

<script src="${ctxStatic}/js/jquery-2.1.4.js"></script>
<script src="${ctxStatic}/js/fastclick.js"></script>
<script src="${ctxStatic}/js/jquery-weui.js"></script>

<script type="text/javascript">
    $(function () {
        $("#loadToAudit").hide();
        $("#endToAudit").hide();
        $("#loadHistory").hide();
        $("#endHistory").hide();
    });

    var vm = new Vue({
        el: '#page',
        created:function (){
            // 待审核数据
            this.$http.get('${ctxf}/wechat/sobill/getSobillListByCheckStatus',{params:{checkStatus:0,startPage:0,endPage:2}}).then(function (res) {
                this.toAuditList = res.data.body.sobillList;
            });

            // 历史数据
            this.$http.get('${ctxf}/wechat/sobill/getSobillListByCheckStatus',{params:{checkStatus:1,startPage:0,endPage:2}}).then(function (res) {
                this.historyList = res.data.body.sobillList;
            });
        },
        data: {
            add: '新增',
            addHref: '${ctxf}/wechat/sobill/goAdd',
            edit: '编辑',
            editHref: '${ctxf}/wechat/sobill/goEdit',
            del: '删除',
            delectById: function () {
                showWindow();
            },
            check: '审核',
            toAuditList:[],
            historyList:[],
            checkSobill:function () {
                var historyId = $("input[name='history']:checked").val();
                if (historyId != null && historyId != '') {
                    $("#title").text('该订单已审核,无需重复操作!');
                    $("#iosDialog2").fadeIn(200);
                    return;
                }
                var toAuditId = $("input[name='toAudit']:checked").val();
                if (toAuditId == null || toAuditId == '') {
                    $("#title").text('请至少选择一条数据!');
                    $("#iosDialog2").fadeIn(200);
                    return;
                }
                $("#iosDialog3").fadeIn(200);
            }
        }
    });

    //展示删除弹窗
    function showWindow() {
        var toAuditId = $("input[name='toAudit']:checked").val();
        var historyId = $("input[name='history']:checked").val();
        if (historyId != null && historyId != '') {
            $("#title").text('订单已审核或已提交不允许删除!');
            $("#iosDialog2").fadeIn(200);
            return;
        }
        if (toAuditId == null || toAuditId == '') {
            $("#title").text('请至少选择一条数据!');
            $("#iosDialog2").fadeIn(200);
            return;
        }
        $('#iosDialog1').fadeIn(200);
    }

    //关闭弹窗
    function closeWindow(Id) {
        $('#'+Id).fadeOut(200);
    }


    // 滚动加载
    var loadingToAudit = false;  //状态标记
    var loadingHistory = false;
    $(document.body).infinite().on("infinite", function () {
        if ($("#toAuditDetail").css("display") == "block"){
            if (loadingToAudit) return;
            // 加载待审核数据
            $("#loadToAudit").show();
            setTimeout(function () {
                loadDatas('toAudit');
            }, 1500);   //模拟延迟
        } else if ($("#historyDetail").css("display") == "block") {
            if (loadingHistory) return;
            // 加载历史数据
            $("#loadHistory").show();
            setTimeout(function () {
                loadDatas('history');
            }, 1500);   //模拟延迟
        }

    });

    var startToAuditPage = 0;
    var endToAuditPage = 2;
    var startHistoryPage = 0;
    var endHistoryPage = 2;
    // 加载数据
    function loadDatas(Id) {
        switch (Id) {
            case 'toAudit':
                startToAuditPage = startToAuditPage + 2;
                endToAuditPage = endToAuditPage + 2;
                $.ajax({
                   async:false,
                   cache:false,
                   url:'${ctxf}/wechat/sobill/getSobillListByCheckStatus',
                   data:{
                       checkStatus:0,
                       startPage:startToAuditPage,
                       endPage:endToAuditPage
                   },
                   dataType:'json',
                   success:function (res) {
                       var sobillList = res.body.sobillList;
                       var template = '';
                       for (let i = 0; i < sobillList.length; i++) {
                           template += '<div class="weui-cells">';
                           template += '<div class="weui-cells_radio">';
                           template += '<label class="weui-cell weui-check__label" for="toAudit'+sobillList[i].id+'">';
                           template += '<div class="weui-cell__bd">';

                           template += '<div class="weui-cell">';
                           template += '<div class="weui-cell__bd">';
                           template += '<p>编号:</p>';
                           template += '</div>';
                           template += '<div class="weui-cell__ft">'+sobillList[i].billNo+'</div>';
                           template += '</div>';

                           template += '<div class="weui-cell">';
                           template += '<div class="weui-cell__bd">';
                           template += '<p>订单发货时间:</p>';
                           template += '</div>';
                           template += '<div class="weui-cell__ft">'+sobillList[i].needTime+'</div>'
                           template += '</div>';

                           template += '<div class="weui-cell">';
                           template += '<div class="weui-cell__bd">';
                           template += '<p>客户:</p>';
                           template += '</div>';
                           template += '<div class="weui-cell__ft">'+sobillList[i].cusName+'</div>'
                           template += '</div>';

                           template += '<div class="weui-cell">';
                           template += '<div class="weui-cell__bd">';
                           template += '<p>销售员:</p>';
                           template += '</div>';
                           template += '<div class="weui-cell__ft">'+sobillList[i].empName+'</div>'
                           template += '</div>';

                           template += '<div class="weui-cell">';
                           template += '<div class="weui-cell__bd">';
                           template += '<p>状态:</p>';
                           template += '</div>';
                           template += '<div class="weui-cell__ft">'+(sobillList[i].status == 1 ? '提交' : '草稿')+'</div>'
                           template += '</div>';

                           template += '<div class="weui-cell__ft">';
                           template += '<input type="radio" class="weui-check" name="toAudit" id="toAudit'+sobillList[i].id+'" value="'+sobillList[i].id+'"/>';
                           template += '<span class="weui-icon-checked"></span>';
                           template += '</div>';
                           template += '</label>';
                           template += '</div>';
                           template += '</div>';
                       }
                       $("#loadToAudit").before(template);
                       if (sobillList.length < 2) {
                           loadingToAudit = true;
                           $("#loadToAudit").hide();
                           $("#endToAudit").show();
                       }
                   }
                });
                break;
            case 'history':
                startHistoryPage = startHistoryPage + 2;
                endHistoryPage = endHistoryPage + 2;
                $.ajax({
                    async:false,
                    cache:false,
                    url:'${ctxf}/wechat/sobill/getSobillListByCheckStatus',
                    data:{
                        checkStatus:1,
                        startPage:startHistoryPage,
                        endPage:endHistoryPage
                    },
                    dataType:'json',
                    success:function (res) {
                        var sobillList = res.body.sobillList;
                        var template = '';
                        for (let i = 0; i < sobillList.length; i++) {
                            template += '<div class="weui-cells">';
                            template += '<div class="weui-cells_radio">';
                            template += '<label class="weui-cell weui-check__label" for="history'+sobillList[i].id+'">';
                            template += '<div class="weui-cell__bd">';

                            template += '<div class="weui-cell">';
                            template += '<div class="weui-cell__bd">';
                            template += '<p>编号:</p>';
                            template += '</div>';
                            template += '<div class="weui-cell__ft">'+sobillList[i].billNo+'</div>';
                            template += '</div>';

                            template += '<div class="weui-cell">';
                            template += '<div class="weui-cell__bd">';
                            template += '<p>订单发货时间:</p>';
                            template += '</div>';
                            template += '<div class="weui-cell__ft">'+sobillList[i].needTime+'</div>'
                            template += '</div>';

                            template += '<div class="weui-cell">';
                            template += '<div class="weui-cell__bd">';
                            template += '<p>客户:</p>';
                            template += '</div>';
                            template += '<div class="weui-cell__ft">'+sobillList[i].cusName+'</div>'
                            template += '</div>';

                            template += '<div class="weui-cell">';
                            template += '<div class="weui-cell__bd">';
                            template += '<p>销售员:</p>';
                            template += '</div>';
                            template += '<div class="weui-cell__ft">'+sobillList[i].empName+'</div>'
                            template += '</div>';

                            template += '<div class="weui-cell">';
                            template += '<div class="weui-cell__bd">';
                            template += '<p>状态:</p>';
                            template += '</div>';
                            template += '<div class="weui-cell__ft">'+(sobillList[i].status == 1 ? '提交' : '草稿')+'</div>'
                            template += '</div>';

                            template += '<div class="weui-cell__ft">';
                            template += '<input type="radio" class="weui-check" name="history" id="history'+sobillList[i].id+'" value="'+sobillList[i].id+'"/>';
                            template += '<span class="weui-icon-checked"></span>';
                            template += '</div>';
                            template += '</label>';
                            template += '</div>';
                            template += '</div>';
                        }
                        if (sobillList.length < 2) {
                            loadingHistory = true;
                            $("#loadHistory").hide();
                            $("#endHistory").show();
                        }
                        $("#loadHistory").before(template);
                    }
                });
                break;
        }
    }

    //执行删除
    function delect() {
        var Id = $("input[name='toAudit']:checked").val();
        $.ajax({
           async:false,
           cache:false,
           url:'${ctxf}/wechat/sobill/delectById',
           data:{
               id:Id
           },
           dataType:'json',
           success:function (res){
               closeWindow('iosDialog1');
               if (res.success) {
                   $("#title").text(res.msg);
                   $("#iosDialog2").fadeIn(200);
                   setTimeout(function () {
                       window.location.reload();
                   },3000);
               } else {
                   $("#title").text(res.msg);
                   $("#iosDialog2").fadeIn(200);
               }
           }
        });
    }

    //切换导航
    function changeStyle(Id) {
        if (Id == 'toAudit') {
            if (!$("#" + Id).hasClass("weui-bar__item_on")) {
                $("#" + Id).addClass("weui-bar__item_on");
                $("#history").removeClass("weui-bar__item_on");
                $("#toAuditDetail").css("display", "block");
                $("#historyDetail").css("display", "none");
                $('input[type=radio][name="history"]:checked').prop("checked", false);
                $("#loadHistory").css("display", "none");
            }
        } else if (Id == 'history') {
            if (!$("#" + Id).hasClass("weui-bar__item_on")) {
                $("#" + Id).addClass("weui-bar__item_on");
                $("#toAudit").removeClass("weui-bar__item_on");
                $("#toAuditDetail").css("display", "none");
                $("#historyDetail").css("display", "block");
                $('input[type=radio][name="toAudit"]:checked').prop("checked", false);
                $("#loadToAudit").css("display", "none");
            }
        }
    }

    //执行审核
    function check() {
        var toAuditId = $("input[name='toAudit']:checked").val();
        $.ajax({
            async:false,
            cache:false,
            url:'${ctxf}/wechat/sobill/checkSobillById',
            data:{
                id:toAuditId
            },
            dataType:'json',
            success:function (res) {
                closeWindow('iosDialog3');
                if (res.success){
                    $("#title").text(res.msg);
                    $("#iosDialog2").fadeIn(200);
                    setTimeout(function () {
                        window.location.reload();
                    },3000);
                } else {
                    $("#title").text(res.msg);
                    $("#iosDialog2").fadeIn(200);
                    return;
                }
            }
        });
    }
</script>
</body>
</html>