<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
    $('#erpDeptTable').bootstrapTable({

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
        pageNumber:1,
        //每页的记录行数（*）
        pageSize: 10,
        //可供选择的每页的行数（*）
        pageList: [10, 25, 50, 100],
        //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
        url: "${ctx}/sys/office/erpDeptData",
        //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
        //queryParamsType:'',
        ////查询参数,每次调用是会带上这个参数，可自定义
        queryParams : function(params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.pageNo = params.limit === undefined? "1" :params.offset/params.limit+1;
            searchParam.pageSize = params.limit === undefined? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined? "" : params.sort+ " "+  params.order;
            return searchParam;
        },
        //分页方式：client客户端分页，server服务端分页（*）
        sidePagination: "server",
        contextMenuTrigger:"right",//pc端 按右键弹出菜单
        contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。
        contextMenu: '#context-menu',
        onContextMenuItem: function(row, $el){
            if($el.data("item") == "edit"){
                edit(row.id);
            }else if($el.data("item") == "view"){
                view(row.id);
            } else if($el.data("item") == "delete"){
                jp.confirm('确认要删除该客户信息记录吗？', function(){
                    jp.loading();
                    jp.get("${ctx}/management/customer/customer/delete?id="+row.id, function(data){
                        if(data.success){
                            $('#erpDeptTable').bootstrapTable('refresh');
                            jp.success(data.msg);
                        }else{
                            jp.error(data.msg);
                        }
                    })

                });

            }
        },

        onClickRow: function(row, $el){
        },
        onShowSearch: function () {
            $("#search-collapse").slideToggle();
        },
        columns: [{
            radio: true

        }
            ,{
                field: 'erpNumber',
                title: 'ERP部门编码',
                sortable: true,
                sortName: 'erpNumber'

            }
            ,{
                field: 'erpName',
                title: 'ERP部门名称',
                sortable: true,
                sortName: 'erpName'

            }
        ]

    });


    if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端


        $('#erpDeptTable').bootstrapTable("toggleView");
    }

    $('#erpDeptTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        $('#remove').prop('disabled', ! $('#erpDeptTable').bootstrapTable('getSelections').length);
        $('#view,#edit').prop('disabled', $('#erpDeptTable').bootstrapTable('getSelections').length!=1);
    });





    $("#search").click("click", function() {// 绑定查询按扭
        $('#erpDeptTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function() {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#erpDeptTable').bootstrapTable('refresh');
    });


});

function getRowSelections() {
    return $.map($("#erpDeptTable").bootstrapTable('getSelections'), function (row) {
        return row
    });
}

//刷新列表
function refresh(){
    $('#erpDeptTable').bootstrapTable('refresh');
}

function save() {
    let row = getRowSelections();
    let data = {
        id: $("#deptId").val(),
        number: row[0].erpNumber,
        name: row[0].erpName
    }
    jp.loading();
    jp.post("${ctx}/sys/office/updateErp",data,function(data){
        if(data.success){
            jp.getParent().refresh();
            jp.success(data.msg);
            var dialogIndex = parent.layer.getFrameIndex(window.name); // 获取窗口索引
            parent.layer.close(dialogIndex);
        }else {
            jp.error(data.msg);
        }
    })
}

</script>