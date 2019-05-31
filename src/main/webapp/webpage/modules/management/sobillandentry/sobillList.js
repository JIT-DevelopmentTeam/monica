<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#sobillTable').bootstrapTable({

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
    	       //显示详情按钮
    	       detailView: true,
    	       	//显示详细内容函数
	           detailFormatter: "detailFormatter",
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
               url: "${ctx}/management/sobillandentry/sobill/data",
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
                        jp.confirm('确认要删除该订单记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/management/sobillandentry/sobill/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#sobillTable').bootstrapTable('refresh');
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
                       field: 'billNo',
                       title: '编码',
                       sortable: true,
                       sortName: 'billNo'
                       ,formatter:function(value, row , index){
                           value = jp.unescapeHTML(value);
                       <c:choose>
                           <c:when test="${fns:hasPermission('management:sobillandentry:sobill:edit')}">
                           return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
                       </c:when>
                           <c:when test="${fns:hasPermission('management:sobillandentry:sobill:view')}">
                           return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
                       </c:when>
                           <c:otherwise>
                           return value;
                       </c:otherwise>
                           </c:choose>
                       }
                   }
			,{
		        field: 'type',
		        title: '类型',
		        sortable: true,
		        sortName: 'type',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('sobill_type'))}, value, "-");
		        }

		    }
			,{
		        field: 'synStatus',
		        title: '同步状态',
		        sortable: true,
		        sortName: 'synStatus',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('syn_status'))}, value, "-");
		        }

		    }
			,{
		        field: 'custName',
		        title: '客户',
		        sortable: true,
		        sortName: 'custName'

		    }
			,{
		        field: 'deptName',
		        title: '归属部门',
		        sortable: true,
		        sortName: 'deptName'

		    }
			,{
		        field: 'emplName',
		        title: '归属员工',
		        sortable: true,
		        sortName: 'emplName'

		    }
			,{
		        field: 'needTime',
		        title: '发货时间',
				class: 'text-nowrap',
		        sortable: true,
		        sortName: 'needTime'

		    }
			,{
		        field: 'status',
		        title: '状态',
		        sortable: true,
		        sortName: 'status',
			    formatter:function(value, row , index){
			 	   return jp.getDictLabel(${fns:toJson(fns:getDictList('sobill_status'))}, value, "-");
			    }
		    }
			,{
		        field: 'cancellation',
		        title: '是否取消',
		        sortable: true,
		        sortName: 'cancellation',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('sobill_cancellation'))}, value, "-");
		        }

		    }
			,{
		        field: 'checkerName',
		        title: '审核人',
		        sortable: true,
		        sortName: 'checkerName'

		    }
			,{
		        field: 'checkTime',
		        title: '审核时间',
		        sortable: true,
		        sortName: 'checkTime'

		    }
			,{
		        field: 'checkStatus',
		        title: '审核状态',
		        sortable: true,
		        sortName: 'checkStatus',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('sobill_checkStatus'))}, value, "-");
		        }

		    }
		   ,{
			   field: 'currencyId',
			   title: '币别',
			   sortable: true,
			   sortName: 'currencyId',
			   formatter:function(value, row , index){
				   return jp.getDictLabel(${fns:toJson(fns:getDictList('sobill_currency'))}, value, "-");
			   }

		   }
			,{
		        field: 'amount',
		        title: '总金额',
		        sortable: true,
		        sortName: 'amount'

		    }
                   ,{
                       field: 'remarks',
                       title: '备注',
                       sortable: true,
                       sortName: 'remarks'
                   }
		     ]

		});


	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端


		  $('#sobillTable').bootstrapTable("toggleView");
		}

	  $('#sobillTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#sobillTable').bootstrapTable('getSelections').length);
            $('#view,#edit,#check,#cancelCheck,#synchronization').prop('disabled', $('#sobillTable').bootstrapTable('getSelections').length!=1);
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
					  jp.downloadFile('${ctx}/management/sobillandentry/sobill/import/template');
				  },
			    btn2: function(index, layero){
						var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/management/sobillandentry/sobill/import', function (data) {
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
            var sortName = $('#sobillTable').bootstrapTable("getOptions", "none").sortName;
            var sortOrder = $('#sobillTable').bootstrapTable("getOptions", "none").sortOrder;
            var values = "";
            for(var key in searchParam){
                values = values + key + "=" + searchParam[key] + "&";
            }
            if(sortName != undefined && sortOrder != undefined){
                values = values + "orderBy=" + sortName + " "+sortOrder;
            }

			jp.downloadFile('${ctx}/management/sobillandentry/sobill/export?'+values);
	  })
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#sobillTable').bootstrapTable('refresh');
		});

	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#sobillTable').bootstrapTable('refresh');
		});

				$('#beginNeedTime').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endNeedTime').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});

	});

  function getIdSelections() {
        return $.map($("#sobillTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }

  function deleteAll(){

		jp.confirm('确认要删除该订单记录吗？', function(){
			jp.loading();
			jp.get("${ctx}/management/sobillandentry/sobill/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#sobillTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})

		})
  }

    //刷新列表
  function refresh() {
      $('#sobillTable').bootstrapTable('refresh');
  }
  function add(){
	  jp.openSaveDialog('新增订单', "${ctx}/management/sobillandentry/sobill/form", window.innerWidth * 0.9+'px', window.innerWidth * 0.8+'px');
  }

   function edit(id){//没有权限时，不显示确定按钮
       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑订单', "${ctx}/management/sobillandentry/sobill/form?id=" + id, window.innerWidth * 0.9+'px', window.innerWidth * 0.8+'px');
  }


 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
        jp.openViewDialog('查看订单', "${ctx}/management/sobillandentry/sobill/form?id=" + id, window.innerWidth * 0.9+'px', window.innerWidth * 0.8+'px');
 }



  function detailFormatter(index, row) {
	  var htmltpl =  $("#sobillChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/management/sobillandentry/sobill/detail?id="+row.id, function(sobill){
    	var sobillChild1RowIdx = 0, sobillChild1Tpl = $("#sobillChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  sobill.sobillentryList;
		for (var i=0; i<data1.length; i++){
			data1[i].dict = {};
			data1[i].dict.synStatus = jp.getDictLabel(${fns:toJson(fns:getDictList(''))}, data1[i].synStatus, "-");
			addRow('#sobillChild-'+row.id+'-1-List', sobillChild1RowIdx, sobillChild1Tpl, data1[i]);
			sobillChild1RowIdx = sobillChild1RowIdx + 1;
		}


      })

        return html;
    }

	function addRow(list, idx, tpl, row){
		$(list).append(Mustache.render(tpl, {
			idx: idx, delBtn: true, row: row
		}));
	}

	function checkOrder(){
        jp.confirm('您确定要审核该订单吗？', function(){
            jp.post("${ctx}/management/sobillandentry/sobill/checkOrder?id="+getIdSelections(),null, function(data){
                if(data.success){
                    jp.success(data.msg);
                    refresh();
                }else{
                    jp.error(data.msg);
                }
            });
        });
	}

function cancelCheckOrder(){
    jp.confirm('您确定要反审核该订单吗？', function(){
        jp.post("${ctx}/management/sobillandentry/sobill/cancelCheckOrder?id="+getIdSelections(),null, function(data){
            if(data.success){
                jp.success(data.msg);
                refresh();
            }else{
                jp.error(data.msg);
            }
        });
    });
}
</script>
<script type="text/template" id="sobillChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">订单明细</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>商品</th>
								<th>商品单位</th>
								<th>批号</th>
								<th>单价</th>
								<th>数量</th>
								<th>总额</th>
    							<th>备注</th>
							</tr>
						</thead>
						<tbody id="sobillChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="sobillChild1Tpl">//<!--
				<tr>
					<td>
						{{row.itemName}}
					</td>
					<td>
						{{row.unit}}
					</td>
					<td>
						{{row.batchNo}}
					</td>
					<td>
						{{row.price}}
					</td>
					<td>
						{{row.auxqty}}
					</td>
					<td>
						{{row.amount}}
					</td>
					<td>
						{{row.remarks}}
					</td>
				</tr>//-->
	</script>
