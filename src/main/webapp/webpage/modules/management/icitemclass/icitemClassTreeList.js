<%@ page contentType="text/html;charset=UTF-8" %>
	<script>
		$(document).ready(function() {
			var to = false;
			$('#search_q').keyup(function () {
				if(to) { clearTimeout(to); }
				to = setTimeout(function () {
					var v = $('#search_q').val();
					$('#icitemClassjsTree').jstree(true).search(v);
				}, 250);
			});
			$('#icitemClassjsTree').jstree({
				'core' : {
					"multiple" : false,
					"animation" : 0,
					"themes" : { "variant" : "large", "icons":true , "stripes":true},
					'data' : {
						"url" : "${ctx}/management/icitemclass/icitemClass/treeData",
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
							"label": "添加下级商品分类管理",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								jp.openSaveDialog('添加下级商品分类管理', '${ctx}/management/icitemclass/icitemClass/form?parent.id=' + obj.id + "&parent.name=" + obj.text, '800px', '500px');
							}
						};
						tmp.remove = {
							"label": "删除商品分类管理",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								jp.confirm('确认要删除商品分类管理吗？', function(){
									jp.loading();
									$.get("${ctx}/management/icitemclass/icitemClass/delete?id="+obj.id, function(data){
										if(data.success){
											$('#icitemClassjsTree').jstree("refresh");
											jp.success(data.msg);
										}else{
											jp.error(data.msg);
										}
									})

								});
							}
						}
						tmp.ccp = {
							"label": "编辑商品分类管理",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								var parentId = inst.get_parent(data.reference);
								var parent = inst.get_node(parentId);
								jp.openSaveDialog('编辑商品分类管理', '${ctx}/management/icitemclass/icitemClass/form?id=' + obj.id, '800px', '500px');
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
				var node = $('#icitemClassjsTree').jstree(true).get_selected(true)[0];
				var opt = {
					query:{
						'classId.id':node.id
					}
				};
				$("#classIdId").val(node.id);
				$("#classIdName").val(node.text);
				$('#icitemTable').bootstrapTable('removeAll');
				$('#icitemTable').bootstrapTable('refresh',opt);
			}).on('loaded.jstree', function() {
				$("#icitemClassjsTree").jstree('open_all');
			});
		});

		function refreshTree() {
            $('#icitemClassjsTree').jstree("refresh");
        }

        // 同步分类
        function synIcitemClass() {
            jp.confirm("您确定要同步商品分类和商品吗?",function () {
                jp.loading("正在同步,请稍后...");
                jp.get("${ctx}/management/icitemclass/icitemClass/synIcitem_Class", function (res) {
                    if (res.success) {
                        refreshTree();
						$('#icitemTable').bootstrapTable('refresh');
                        jp.success(res.msg);
                    } else {
                        jp.error(res.msg);
                    }
                });
            });

        }
	</script>