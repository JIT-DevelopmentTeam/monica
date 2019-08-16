<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#icitemTable').bootstrapTable({

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
        //固定高度，达到固定表头
        height:$(window).height() * 0.8,
        //排序方式
        sortOrder: "asc",
        //初始化加载第一页，默认第一页
        pageNumber:1,
        //每页的记录行数（*）
        pageSize: 10,
        //可供选择的每页的行数（*）
        pageList: [10, 25, 50, 100],
        //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
        url: "${ctx}/management/icitemclass/icitem/data",
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
                jp.confirm('确认要删除该商品资料记录吗？', function(){
                    jp.loading();
                    jp.get("${ctx}/management/icitemclass/icitem/delete?id="+row.id, function(data){
                        if(data.success){
                            $('#icitemTable').bootstrapTable('refresh');
                            jp.success(data.msg);
                        }else{
                            jp.error(data.msg);
                        }
                    })

                });

            }
        },
        // 选中
        onCheck:function(row){
            var ids = getIdSelections();
            $("#ids").val(ids);
        },
        // 取消选中
        onUncheck:function(row) {
            var ids = getIdSelections();
            $("#ids").val(ids);
        },
        // 全选
        onCheckAll:function(rows){
            var ids = getIdSelections();
            $("#ids").val(ids);
        },
        // 取消全选
        onUncheckAll:function(rows){
            var ids = getIdSelections();
            $("#ids").val(ids);
        },
        onClickRow: function(row, $el){
        },
        onShowSearch: function () {
            $("#search-collapse").slideToggle();
        },
        columns: [{
            checkbox: true

        }
            ,{
                field: 'erpId',
                title: 'erp端id',
                sortable: true,
                sortName: 'erpId'
                ,formatter:function(value, row , index){
                    value = jp.unescapeHTML(value);
                    <c:choose>
                        <c:when test="${fns:hasPermission('management:icitemclass:icitem:edit')}">
                            return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
                        </c:when>
                        <c:when test="${fns:hasPermission('management:icitemclass:icitem:view')}">
                            return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
                        </c:when>
                        <c:otherwise>
                            return value;
                        </c:otherwise>
                    </c:choose>
                }

            }
            ,{
                field: '',
                title: '分类id',
                sortable: true,
                width:'15',
                sortName: ''

            }
            ,{
                field: 'number',
                title: '编号',
                sortable: true,
                sortName: 'number'

            }
            ,{
                field: 'name',
                title: '商品名称',
                sortable: true,
                sortName: 'name'

            }
            ,{
                field: 'model',
                title: '商品型号',
                sortable: true,
                sortName: 'model'

            }
            ,{
                field: 'unit',
                title: '商品计算单位',
                sortable: true,
                sortName: 'unit'

            }
            ,{
                field: 'erpclassId',
                title: 'erp端分类id',
                sortable: true,
                sortName: 'erpclassId'

            }
            ,{
                field: 'modifyTime',
                title: '同步时间戳',
                sortable: true,
                sortName: 'modifyTime'

            }
            ,{
                field: 'erpNote',
                title: 'erp备注',
                sortable: true,
                sortName: 'erpNote'

            }
            ,{
                field: 'status',
                title: '状态',
                sortable: true,
                sortName: 'status'

            }
            ,{
                field: 'remarks',
                title: '备注信息',
                sortable: true,
                sortName: 'remarks'

            }
            ,{
                field: 'remarks',
                title: '操作',
                sortable: false,
                sortName: 'remarks',
                formatter:function(value, row, index) {
                    return [
                        '<div class="btn-group">',
                            '<button id="picGroupDetail" type="button" class="btn btn-default" onclick="picGroupDetail(\''+row.id+'\')"  singleSelected=true>图片详情</button>',
                        '</div>'
                    ].join('');
                }

            }
        ]

    });
    /* $("#icitemTable").colResizable();*/

    if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {//如果是移动端


        $('#icitemTable').bootstrapTable("toggleView");
    }

    $('#icitemTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        $('#remove').prop('disabled', !$('#icitemTable').bootstrapTable('getSelections').length);
        $('#view,#edit,#uploadItemId').prop('disabled', $('#icitemTable').bootstrapTable('getSelections').length != 1);
    });

    $("#btnImport").click(function () {
        jp.open({
            type: 2,
            area: [500, 200],
            auto: true,
            title: "导入数据",
            content: "${ctx}/tag/importExcel",
            btn: ['下载模板', '确定', '关闭'],
            btn1: function (index, layero) {
                jp.downloadFile('${ctx}/management/icitemclass/icitem/import/template');
            },
            btn2: function (index, layero) {
                var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                iframeWin.contentWindow.importExcel('${ctx}/management/icitemclass/icitem/import', function (data) {
                    if (data.success) {
                        jp.success(data.msg);
                        refresh();
                    } else {
                        jp.error(data.msg);
                    }
                    jp.close(index);
                });//调用保存事件
                return false;
            },

            btn3: function (index) {
                jp.close(index);
            }
        });
    });


    $("#export").click(function () {//导出Excel文件
        var searchParam = $("#searchForm").serializeJSON();
        searchParam.pageNo = 1;
        searchParam.pageSize = -1;
        var sortName = $('#icitemTable').bootstrapTable("getOptions", "none").sortName;
        var sortOrder = $('#icitemTable').bootstrapTable("getOptions", "none").sortOrder;
        var values = "";
        for (var key in searchParam) {
            values = values + key + "=" + searchParam[key] + "&";
        }
        if (sortName != undefined && sortOrder != undefined) {
            values = values + "orderBy=" + sortName + " " + sortOrder;
        }

        jp.downloadFile('${ctx}/management/icitemclass/icitem/export?' + values);
    })

    $("#search").click("click", function () {// 绑定查询按扭
        $('#icitemTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#icitemTable').bootstrapTable('refresh');
    });


});

// 图片详情
function picGroupDetail(id) {
    jp.openViewDialog('商品图片详情', "${ctx}/management/itemfile/itemFile/list?itemId=" + id, '1100px', '600px');
}

function synIcitemClass() {
    jp.post("${ctx}/management/icitemclass/icitemClass/synIcitemClass", null, function (callbackData) {
        console.log(callbackData);
    });
}

function synIcitem() {
    jp.post("${ctx}/management/icitemclass/icitem/synIcitem", null, function (callbackData) {
        console.log(callbackData);
    });
}

function getIdSelections() {
    return $.map($("#icitemTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {

    jp.confirm('确认要删除该商品资料记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/management/icitemclass/icitem/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#icitemTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })

    })
}

//刷新列表
function refresh() {
    $('#icitemTable').bootstrapTable('refresh');
}

function add() {
    jp.openSaveDialog('新增商品资料', "${ctx}/management/icitemclass/icitem/form", '800px', '500px');
}


function edit(id) {//没有权限时，不显示确定按钮
    if (id == undefined) {
        id = getIdSelections();
    }
    jp.openSaveDialog('编辑商品资料', "${ctx}/management/icitemclass/icitem/form?id=" + id, '800px', '500px');
}

function view(id) {//没有权限时，不显示确定按钮
    if (id == undefined) {
        id = getIdSelections();
    }
    jp.openViewDialog('查看商品资料', "${ctx}/management/icitemclass/icitem/form?id=" + id, '800px', '500px');
}

function save(parentObject) {
    $("#ids").val(getIdSelections());
}

function submitItems() {
    $("#ids").val(getIdSelections());
}

// 商品图片，附件上传
function icItemUpload(id) {
    if (id == undefined) {
        id = getIdSelections();
    }
    jp.openViewDialog('上传商品图片', "${ctx}/management/itemfile/itemFile/toUpload/" + id, '800px', '500px');
}
</script>