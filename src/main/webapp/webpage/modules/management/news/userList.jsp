<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>发布人管理列表</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script>
        $(function () {
            $('#userListTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
                /*$('').prop('disabled', !$('#userListTable').bootstrapTable('getSelections').length);*/
                //$('').prop('disabled', $('#userListTable').bootstrapTable('getSelections').length!=1)
            });
            intnDataList
        });
        function intnDataList() {
            $("#userListTable").bootstrapTable({
                //请求方法
                method: 'post',
                //类型json
                dataType: "json",
                contentType: "application/x-www-form-urlencoded",
                //显示检索按钮
                showSearch: true,
                //显示刷新按钮
                showRefresh: true,
                //显示切换手机试图按钮
                showToggle: true,
                //显示 内容列下拉框
                showColumns: true,
                //显示到处按钮
                showExport: true,
                //显示切换分页按钮
                showPaginationSwitch: true,
                //最低显示2行
                minimumCountColumns: 2,
                //是否显示行间隔色
                striped: true,
                //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                cache: false,
                //是否显示分页（*）
                pagination: true,
                //排序方式
                sortOrder: "asc",
                //初始化加载第一页，默认第一页
                pageNumber: 1,
                //每页的记录行数（*）
                pageSize: 10,
                //可供选择的每页的行数（*）
                pageList: [10, 25, 50, 100],
                //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
                url: "${ctx}",
                //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
                //queryParamsType:'',
                ////查询参数,每次调用是会带上这个参数，可自定义
                queryParams: function (params) {
                    var searchParam = $("#searchForm").serializeJSON();
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                //分页方式：client客户端分页，server服务端分页（*）
                sidePagination: "server",
                contextMenuTrigger: "right",//pc端 按右键弹出菜单
                contextMenuTriggerMobile: "press",//手机端 弹出菜单，click：单击， press：长按。
                contextMenu: '#context-menu',
                onContextMenuItem: function (row, $el) {

                },
                onClickRow: function (row, $el) {

                },
                columns: [{
                    checkbox: true
                }, {
                    field: 'priName',
                    title: '作者',
                    sortable: true,
                    sortName: 'priName'
                }, {
                    field: 'updateDate',
                    title: '部门',
                    sortable: true,
                    sortName: 'updateDate'
                }
                ]
            });
        }
        function getIdSelections() {
            return $.map($("#userListTable").bootstrapTable('getSelections'), function (row) {
                return row.id
            });
        }

        // 删除
    </script>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-body">
            <!-- 工具栏 -->
            <div id="toolbar">
                <form:form id="searchForm" modelAttribute="news" class="form form-horizontal well clearfix">
                    <div class="col-xs-12 col-sm-6 col-md-4">
                        发布人：<form:input path="smallnrl" htmlEscape="false"  class="form-control " placeholder="请输入发布人"/>
                    </div>
                    <div class="col-xs-12 col-sm-6 col-md-4">
                        <div style="margin-top:23px">
                            <a id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
                                    class="fa fa-search"></i> 查询</a>
                            <a id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
                                    class="fa fa-refresh"></i> 重置</a>
                        </div>
                    </div>
                </form:form>
            </div>
            <!-- 表格 -->
            <table id="userListTable" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>

<script type="text/javascript" charset="UTF-8">

</script>
</body>
</html>
