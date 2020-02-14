<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#itemFileTable').bootstrapTable({

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
               url: "${ctx}/management/itemfile/itemFile/data",
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
                        $("#itemFileTable").bootstrapTable("removeAll");
                        return;
                    }
                    $("#itemFileTable").bootstrapTable("load",data);
                },
               onContextMenuItem: function(row, $el){
                   if($el.data("item") == "edit"){
                   		edit(row.id);
                   }else if($el.data("item") == "view"){
                       view(row.id);
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该上传商品图片记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/management/itemfile/itemFile/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#itemFileTable').bootstrapTable('refresh');
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

		    },{
                   field: 'icitem.name',
                   title: '商品名称',
                   sortable: true,
                   class:"text-nowrap",
                   sortName: 'icitem.name'

               }
			,{
		        field: 'originalName',
		        title: '文件原名称',
		        sortable: true,
		        sortName: 'originalName'
		        ,formatter:function(value, row , index){
		        	value = jp.unescapeHTML(value);
				   <c:choose>
					   <c:when test="${fns:hasPermission('management:itemfile:itemFile:edit')}">
					      return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:when test="${fns:hasPermission('management:itemfile:itemFile:view')}">
					      return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:otherwise>
					      return value;
				      </c:otherwise>
				   </c:choose>
		         }

		    }
			,{
		        field: 'name',
		        title: '上传编码名称',
		        sortable: true,
		        sortName: 'name'

		    }
			,{
		        field: 'size',
		        title: '文件大小',
		        sortable: true,
		        sortName: 'size',
                formatter:function (value,row,index) {
                    if(value != '' || value != null ){
                        return value+"K";
                    }else{
                        return "-";
                    }
                }
		    }
			,{
		        field: 'type',
		        title: '文件类型',
		        sortable: true,
		        sortName: 'type'
		    }
			,{
		        field: 'isDown',
		        title: '是否允许下载',
		        sortable: true,
		        sortName: 'isDown',
                formatter:function (value,row,index) {
                    if(value == '1'){
                        return "<a id='isDown_a' href='#' class='green' style='color: green;'>允许</a>";
                    }else{
                        return "<a id='isDown_a' href='#' class='red' style='color: red;'>不允许</a>";
                    }
                }
		    }
			/*,{
		        field: 'url',
		        title: '文件路径',
		        sortable: true,
		        sortName: 'url'

		    }
			,{
		        field: 'smallUrl',
		        title: '文件预览图路径',
		        sortable: true,
		        sortName: 'smallUrl'

		    }*/
			,{
		        field: 'server',
		        title: '文件服务器地址',
		        sortable: true,
		        sortName: 'server'

		    }
			,{
		        field: 'downCount',
		        title: '下载次数',
		        sortable: true,
		        sortName: 'downCount'

		    }
			,{
		        field: 'remarks',
		        title: '备注信息',
		        sortable: true,
		        sortName: 'remarks'

		    }
		    ,{
                field: 'download',
                title: "下载",
                sortable: true,
                formatter:function (value,row,index) {
                    return "<a id='' class='' href='${ctx}/management/itemfile/itemFile/download/template/"+row.id+"'>下载</a>";
                }
            }
		     ]

		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#itemFileTable').bootstrapTable("toggleView");
		}
	  
	  $('#itemFileTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#itemFileTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#itemFileTable').bootstrapTable('getSelections').length!=1);
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
					  jp.downloadFile('${ctx}/management/itemfile/itemFile/import/template');
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/management/itemfile/itemFile/import', function (data) {
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
            var sortName = $('#itemFileTable').bootstrapTable("getOptions", "none").sortName;
            var sortOrder = $('#itemFileTable').bootstrapTable("getOptions", "none").sortOrder;
            var values = "";
            for(var key in searchParam){
                values = values + key + "=" + searchParam[key] + "&";
            }
            if(sortName != undefined && sortOrder != undefined){
                values = values + "orderBy=" + sortName + " "+sortOrder;
            }

			jp.downloadFile('${ctx}/management/itemfile/itemFile/export?'+values);
	  })

		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#itemFileTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#itemFileTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#itemFileTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该上传商品图片记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/management/itemfile/itemFile/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#itemFileTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }

    //刷新列表
  function refresh(){
  	$('#itemFileTable').bootstrapTable('refresh');
  }
  
   function add(){
	  jp.openSaveDialog('新增上传商品图片', "${ctx}/management/itemfile/itemFile/form",'800px', '500px');
  }


  
   function edit(id){//没有权限时，不显示确定按钮
       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑上传商品图片', "${ctx}/management/itemfile/itemFile/form?id=" + id, '800px', '500px');
  }

 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
        jp.openViewDialog('查看商品图片', "${ctx}/management/itemfile/itemFile/showPic?id="+id, '500px', '500px');
 }



</script>