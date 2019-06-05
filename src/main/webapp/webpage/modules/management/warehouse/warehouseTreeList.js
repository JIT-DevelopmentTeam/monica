<%@ page contentType="text/html;charset=UTF-8" %>
	<script>
		$(document).ready(function() {
			$('div[class="col-sm-10"]').attr('class', 'col-sm-8');
			$('i[class="fa fa-plus"]').attr('class', 'glyphicon glyphicon-refresh');
			$('i[class="glyphicon glyphicon-refresh"]').text('同步仓库');
			$('button[class="btn btn-default btn-sm"]').removeAttr('onclick');
			$('button[class="btn btn-default btn-sm"]').click(function () {
				jp.post("${ctx}/management/warehouse/warehouse/synWareHouse", {}, function (data) {
					refreshTree();
				});
			});

			var to = false;
			$('#search_q').keyup(function () {
				if(to) { clearTimeout(to); }
				to = setTimeout(function () {
					var v = $('#search_q').val();
					$('#warehousejsTree').jstree(true).search(v);
				}, 250);
			});
			$('#warehousejsTree').jstree({
				'core' : {
					"multiple" : false,
					"animation" : 0,
					"themes" : { "variant" : "large", "icons":true , "stripes":true},
					'data' : {
						"url" : "${ctx}/management/warehouse/warehouse/treeData",
						"dataType" : "json" 
					}
				},
				"conditionalselect" : function (node, event) {
					return false;
				},
				'plugins': ["contextmenu", 'types', 'wholerow', "search"],
				"contextmenu": {
					"items": function (node) {
						var tmp = $.jstree.defaults.contextmenu.items();
						delete tmp.create.action;
						delete tmp.rename.action;
						tmp.rename = null;
						tmp.create = {
							"label": "添加下级库存管理",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								jp.openSaveDialog('添加下级库存管理', '${ctx}/management/warehouse/warehouse/form?parent.id=' + obj.id + "&parent.name=" + obj.text, '800px', '500px');
							}
						};
						tmp.remove = {
							"label": "删除库存管理",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								jp.confirm('确认要删除库存管理吗？', function(){
									jp.loading();
									$.get("${ctx}/management/warehouse/warehouse/delete?id="+obj.id, function(data){
										if(data.success){
											$('#warehousejsTree').jstree("refresh");
											jp.success(data.msg);
										}else{
											jp.error(data.msg);
										}
									})

								});
							}
						}
						tmp.ccp = {
							"label": "编辑库存管理",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								var parentId = inst.get_parent(data.reference);
								var parent = inst.get_node(parentId);
								jp.openSaveDialog('编辑库存管理', '${ctx}/management/warehouse/warehouse/form?id=' + obj.id, '800px', '500px');
							}
						}
						return tmp;
					}

				},
				"types":{
					'default' : { 'icon' : 'fa fa-folder' },
					'1' : {'icon' : 'fa fa-home'},
					'2' : {'icon' : 'fa fa-umbrella' },
					'3' : { 'icon' : 'fa fa-group'},
					'4' : { 'icon' : 'fa fa-file-text-o' }
				}

			}).bind("activate_node.jstree", function (obj, e) {
				var node = $('#warehousejsTree').jstree(true).get_selected(true)[0];
				var opt = {
					silent: true,
					query:{
						'warehouseId.id':node.id
					}
				};
				$("#warehouseIdId").val(node.id);
				$("#warehouseIdName").val(node.text);
				$('#stockTable').bootstrapTable('refresh',opt);
			}).on('loaded.jstree', function() {
				$("#warehousejsTree").jstree('open_all');
			});
		});

		function refreshTree() {
	            $('#warehousejsTree').jstree("refresh");
	        }
	</script>