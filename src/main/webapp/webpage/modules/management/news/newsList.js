<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#newsTable').bootstrapTable({

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
               url: "${ctx}/management/news/news/data",
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
                onLoadSuccess: function(data){
                    if(data["rows"] == undefined){
                        $("#newsTable").bootstrapTable("removeAll");
                        return;
                    }
                    $("#newsTable").bootstrapTable("load",data);
                },
               onContextMenuItem: function(row, $el){
                   if($el.data("item") == "edit"){
                   		edit(row.id);
                   }else if($el.data("item") == "view"){
                       view(row.id);
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该新闻公告记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/management/news/news/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#newsTable').bootstrapTable('refresh');
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
		        checkbox: true
		    }
			,{
		        field: 'title',
		        title: '标题',
                class:"text-nowrap",
		        sortable: true,
		        sortName: 'title'
		        ,formatter:function(value, row , index){
		        	value = jp.unescapeHTML(value);
				   <c:choose>
					   <c:when test="${fns:hasPermission('management:news:news:edit')}">
					      return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:when test="${fns:hasPermission('management:news:news:view')}">
					      return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:otherwise>
					      return value;
				      </c:otherwise>
				   </c:choose>
		         }

		    }
            ,{
                field: 'user.name',
                title: '发布人',
                sortable: true,
                sortName: 'authorid'

            },
		   {
			   field: 'office.name',
			   title: '发布人部门',
			   sortable: true,
			   sortName: 'deptid'

		   }
		   ,{
			   field: 'isPublic',
			   title: '是否发布',
			   sortable: true,
			   sortName: 'isPublic',
			   formatter:function(value, row , index){
				   var i = "-";
				   if(value == 0){
					   i = "否"
				   }else if(value == 1){
					   i = "是"
				   }
				   return i;
			   }

		   }
		   ,{
			   field: 'headline',
			   title: '是否设置为头条',
			   sortable: true,
			   sortName: 'headline',
			   formatter:function(value, row , index){
				   var i = "-";
				   if(value == 0){
					   i = "否"
				   }else if(value == 1){
					   i = "是"
				   }
				   return i;
			   }

		   }
			,{
		        field: 'describe',
		        title: '摘要',
                class:"text-nowrap",
		        sortable: true,
		        sortName: 'describe',
			    cellStyle: formatTableUnit,
			    formatter: paramsMatter

		    }
			,{
		        field: 'starttime',
		        title: '显示时间开始',
				class:"text-nowrap",
		        sortable: true,
		        sortName: 'starttime'

		    }
			,{
		        field: 'endtime',
		        title: '显示结束时间',
				class:"text-nowrap",
		        sortable: true,
		        sortName: 'endtime'

		    }
			,{
		        field: 'isPush',
		        title: '是否推送',
		        sortable: true,
		        sortName: 'isPush',
		        formatter:function(value, row , index){
                    var i = "-";
                    if(value == 0){
                        i = "否"
                    }else if(value == 1){
                        i = "是"
                    }
                    return i;
		        }
		    }
            ,{
                field: 'pushrule',
                title: '推送规则',
                sortable: true,
                sortName: 'pushrule',
                formatter:function(value, row , index){
                    var i = "-";
                    if(value == 0){
                        i = "全部推送"
                    }else if(value == 1){
                        i = "人员推送"
                    }else if(value == 2){
                        i = "部门推送"
                    }
                    return i;
                }

            }
			,{
		        field: 'push',
		        title: '推送时间',
                class:"text-nowrap",
		        sortable: true,
		        sortName: 'push'

		    },{
                field: 'readCount',
                title: '阅读次数',
                sortable: true,
                sortName: 'readCount'
            }
            ,{
                field: 'mainpic',
                title: '封面图片',
                sortable: true,
                sortName: 'mainpic',
                events: operateEvents,
                formatter: operateFormatter
            }
            ,{
                field: 'remarks',
                title: '操作',
                sortable: false,
                sortName: 'remarks',
                formatter:function(value, row, index) {
                    return [
                        '<div class="btn-group">',
                        '<button id="filesNewsDetail" type="button" class="btn btn-default" onclick="newsFilesDetail(\''+row.id+'\')"  singleSelected=true>附件详情</button>',
                        '</div>'
                    ].join('');
                }

            }
		     ]

		});


	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端
		  $('#newsTable').bootstrapTable("toggleView");
		}
	  
	  $('#newsTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#newsTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#newsTable').bootstrapTable('getSelections').length!=1);
            $.map($("#newsTable").bootstrapTable('getSelections'), function (row) {
                if(row.isPush == 1){
                    $('#edit').prop('disabled',"true");
                }
            });
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 2,
                area: [500, 200],
                auto: true,
			    title:"导入数据",
			    content: "${ctx}/tag/importExcel" ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  jp.downloadFile('${ctx}/management/news/news/import/template');
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/management/news/news/import', function (data) {
							if(data.success){
								jp.success(data.msg);
								refresh();
							}else{
								jp.error(data.msg);
							}
					   		jp.close(index);
						});//调用保存事件
						return false;
				  },
				 
				  btn3: function(index){ 
					  jp.close(index);
	    	       }
			}); 
		});
		
		
	 $("#export").click(function(){//导出Excel文件
	        var searchParam = $("#searchForm").serializeJSON();
	        searchParam.pageNo = 1;
	        searchParam.pageSize = -1;
            var sortName = $('#newsTable').bootstrapTable("getOptions", "none").sortName;
            var sortOrder = $('#newsTable').bootstrapTable("getOptions", "none").sortOrder;
            var values = "";
            for(var key in searchParam){
                values = values + key + "=" + searchParam[key] + "&";
            }
            if(sortName != undefined && sortOrder != undefined){
                values = values + "orderBy=" + sortName + " "+sortOrder;
            }

			jp.downloadFile('${ctx}/management/news/news/export?'+values);
	  })

		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#newsTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#newsTable').bootstrapTable('refresh');
		});
		
		
	});

    //绑定点击事件
    window.operateEvents = { 'click .picMainpic': function (e, value, row, index) {
        picMainpic(row.id);
    }
    };
        function operateFormatter(value, row, index) {
        return [
        '<button type="button" class="picMainpic btn btn-primary btn-sm"><i class="glyphicon glyphicon-file"></i>查看封面</button>'
        ].join('');
    }

// 表格超出宽度鼠标悬停显示td内容
function paramsMatter(value, row, index, field) {
	var span = document.createElement('span');
	span.setAttribute('title', value);
	span.innerHTML = value;
	return span.outerHTML;
}
// td宽度以及内容超过宽度隐藏
function formatTableUnit(value, row, index) {
	return {
		css: {
			"white-space": 'nowrap',
			"text-overflow": 'ellipsis',
			"overflow": 'hidden',
			"max-width": '150px'
		}
	}
}

  function getIdSelections() {
        return $.map($("#newsTable").bootstrapTable('getSelections'), function (row) {
                if(row.isPush == 1){
                $('#edit').prop('disabled',"true");
            }

            return row.id
        });
    }

  function deleteAll(){

		jp.confirm('确认要删除该新闻公告记录吗？', function(){
			jp.loading();
			jp.get("${ctx}/management/news/news/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){

         	  			$('#newsTable').bootstrapTable('refresh',"${ctx}/management/news/news/data");
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})

		})
  }

    //刷新列表
  function refresh(){
  	$('#newsTable').bootstrapTable('refresh');
  }
  
   function add(){
	  jp.openSaveDialog('新增新闻公告', "${ctx}/management/news/news/form",'1050px', '850px');
  }


  
   function edit(id){//没有权限时，不显示确定按钮
       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑新闻公告', "${ctx}/management/news/news/form?id=" + id, '1050px', '850px');
  }
  
 function view(id) {  //没有权限时，不显示确定按钮
     if (id == undefined) {
         id = getIdSelections();
     }
     jp.openViewDialog('查看新闻公告', "${ctx}/management/news/news/form?id=" + id, '1050px', '850px');
 }
 function picMainpic(mainpic) {
     jp.openViewDialog('查看新闻公告封面',"${ctx}/management/news/news/picMainpic?id="+mainpic, '500px', '500px');
 }
// 附件详情
function newsFilesDetail(id) {
    console.log(id);
    jp.openViewDialog('新闻附件详情', "${ctx}/management/newsfile/newsFile/list?newsId="+ id, '1100px', '600px');
}


</script>