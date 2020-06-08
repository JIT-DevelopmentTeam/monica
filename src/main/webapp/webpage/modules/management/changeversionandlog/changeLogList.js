<%@ page contentType="text/html;charset=UTF-8" %>
    <script type="text/javascript">
    $(document).ready(function() {
        //表格初始化
        $('#table').bootstrapTable({

            //请求方法
            method: 'post',
            //类型json
            dataType: "json",
            contentType: "application/x-www-form-urlencoded",
            //是否显示行间隔色
            striped: true,
            //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            cache: false,
            //显示检索按钮
            showSearch: false,
            //显示刷新按钮
            showRefresh: false,
            //显示切换手机试图按钮
            showToggle: false,
            //显示 内容列下拉框
            showColumns: false,
            //显示切换分页按钮
            showPaginationSwitch: false,
            //是否显示分页（*）
            pagination: true,
            //排序方式
            sortOrder: "asc",
            //初始化加载第一页，默认第一页
            pageNumber:1,
            //每页的记录行数（*）
            pageSize: 10,
            //可供选择的每页的行数（*）
            pageList: [10, 25, 50, 100],
            //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
            url: "${ctx}/management/changeversionandlog/changeVersion/listChangeLog",
            //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
            //queryParamsType:'',
            ////查询参数,每次调用是会带上这个参数，可自定义
            queryParams : function(params) {
                var searchParam = $("#searchForm").serializeJSON();
                searchParam.pageNo = params.limit === undefined? "1" :params.offset/params.limit+1;
                searchParam.pageSize = params.limit === undefined? 10 : params.limit;
                searchParam.id = $("#changeVersionId").val();
                return searchParam;
            },
            onShowSearch: function () {
                $("#search-collapse").slideToggle();
            },
            //分页方式：client客户端分页，server服务端分页（*）
            sidePagination: "server",
            contextMenuTrigger:"right",//pc端 按右键弹出菜单
            contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。
            contextMenu: '#context-menu',
            onContextMenuItem: function(row, $el){
                if($el.data("item") == "edit"){
                    edit(row.id);
                } else if($el.data("item") == "delete"){
                    deleteAll(row.id);
                }
            },

            onClickRow: function(row, $el){
            },
            columns: [{
                radio: true

            }, {
                field: 'item.name',
                title: '商品名称',
            }, {
                field: 'originalQuantity',
                title: '原数量'
            }]

        });


        if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端
            $('#table').bootstrapTable("toggleView");
        }

        $('#table').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
            'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#table').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
        });

        $("#search").click("click", function() {// 绑定查询按扭
            $('#table').bootstrapTable('refresh');
        });
        $("#reset").click("click", function() {// 绑定查询按扭
            $("#searchForm  input").val("");
            $("#searchForm  select").val("");
            $('#table').bootstrapTable('refresh');
        });

        $('#beginInDate').datetimepicker({
            format: "YYYY-MM-DD HH:mm:ss"
        });
        $('#endInDate').datetimepicker({
            format: "YYYY-MM-DD HH:mm:ss"
        });

    });

function getRowSelections() {
    return $.map($("#table").bootstrapTable('getSelections'), function (row) {
        return row
    });
}
</script>