<%@page import="com.ambition.supplier.entity.Supplier"%>
<%@page import="com.ambition.supplier.entity.SupplyProduct"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
		isUsingComonLayout=false;
		var initiated = {
				'0':true,
				'1' : true,
				'2' : true
			};
		var id='${id}',idIndex = 0,updateParams = {},deleteParams={},lastSelect={},gridHeight = 0,gridWidth=0;
		$(document).ready(function(){
			$("#tabs").tabs({
				show: function(event, ui) {
					createProductGrid();
				}
			}).height($(document).height()-30);
			$(".content").height($(document).height()-105);
			gridHeight = $(document).height()-150;
			gridWidth = $(document).width() - 70;
			$("#form").validate();
		});
		//保存供应产品
		function saveProducts(){
			var params = {
				id : '${id}'
			};
			var pro = 'product_table';
			
			function priSaveProducts(){
				var idObj = deleteParams[pro];
				var ids = [];
				for(var id in idObj){
					if(id > 0){
						ids.push(id);
					}
				}
				params["params.deletes"] = ids.toString();
				idObj = updateParams[pro];
				var strs = [];
				for(var id in idObj){
					if(idObj[id]){
						var data = jQuery("#" + pro).jqGrid('getRowData',id);
						if(data){
							var str = '{';
							var flag = false;
							for(var per in data){
								if(per == 'act'){
									continue;
								}
								if(flag){
									str += ',';
								}
								str += '"' + per + '":' + '"' + data[per] + '\"';
								flag = true;
							}
							if(flag){
								str += '}';
								strs.push(str);
							}
						}
					}
				}
				params["params.updates"] = '['+ strs.toString() + ']';
				$(".opt-btn .btn").attr("disabled",true);
				$("#product-message").html("正在保存,请稍候... ...");
				$.post("${aftersalesctx}/lar/save-item.htm",params,function(result){
					$("#product-message").html(result.message);
					if(!result.error){
						//设置表格的更新后的ID
						var updateDetails = result.updateDetails;
						try{
							var tableId = pro;
							var updateIdObj = updateDetails[tableId];
							var grid = $("#" + tableId);
							var isUpdate = false;
							for(var hisId in updateIdObj){
								grid.jqGrid('setRowData',hisId,{id:updateIdObj[hisId]});
								var trs = $("#" + tableId + " #" + hisId);
								if(trs.length>0){
									trs[0].id = updateIdObj[hisId];
									isUpdate = true;
								}
							}
							if(isUpdate){
								addOperateMethod(tableId);
							}
						}catch(e){
							alert(e.message);
						}
						idObj = deleteParams[pro];
						for(var id in idObj){
							idObj[id] = false;
						}
						idObj = updateParams[pro];
						for(var id in idObj){
							idObj[id] = false;
						}
					}
					$(".opt-btn .btn").attr("disabled",false);
					setTimeout(function(){
						$("#product-message").html('');
					},1000);
				},'json');
			}
			//先保存最后一次编辑的记录
			if(lastSelect[pro]){
				var grid = $("#" + pro);
				var success = grid.jqGrid("saveRow",lastSelect[pro],{
					url:'clientArray'
				});
				if(!success){
					return;
				}
				addUpateParams(pro,lastSelect[pro]);
				lastSelect[pro] = null;
				makeEditable(pro,true);
			}
			priSaveProducts();
		}
		//获取参数
		function getParams(){
			var params = {};
			$("#form :input").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name){
					if(obj.type=='radio'){
						if(obj.checked){
							params[obj.name] = jObj.val();
						}
					}else{
						params[obj.name] = jObj.val();
					}
				}
			});
			var linkMans = "";
			$(".link-man").each(function(index,obj){
				var str = '';
				$(obj).find(":input[customName]").each(function(flag,item){
					var $item = $(item);
					if(str){
						str += ",";
					}
					str += "\""+$item.attr("customName")+"\":\"" + $item.val() + "\"";
				});
				if(linkMans){
					linkMans += ",";
				}
				linkMans += "{" + str + "}";
			});
			if(linkMans){
				params.linkMans = "[" + linkMans + "]";
			}
			return params;
		}
		//重置方法
		function setParams(params){
			$("#asendForm input").each(function(index,obj){
				if((obj.type=='text'||obj.type=='hidden')&&obj.name){
					if(params[obj.name]){
						$(obj).val(params[obj.name]);
					}else{
						$(obj).val('');
					}
				}
			});
			$("#asendForm select").each(function(index,obj){
				if(obj.name){
					if(params[obj.name]){
						$(obj).val(params[obj.name]);
					}else{
						$(obj).val('');
					}
				}
			});
		}
		function customDate(value,options){
			var el = document.createElement("input");
            el.type="text";
            el.value = value;
            setTimeout(function(){
            	$(el).datepicker();
            },100);
            return el;
		}
		function getCustomDateValue(elem) {
            return $(elem).val();
        }
		//创建供应的产品表格
		function createProductGrid(){
			$("#product_table").jqGrid({
				url : '${aftersalesctx}/lar/bad-item-datas.htm',
				postData : {id:'${id}'},
				height : gridHeight,
				width : gridWidth,
				datatype: "json",
				rownumbers:true,
				gridEdit: true,
				colNames : ${larItemGridColumnInfo.colNames},
				colModel : ${larItemGridColumnInfo.colModel},
				forceFit : true,
			   	shrinkToFit : true,
				multiselect : false,
			   	autowidth: true,
				viewrecords: true, 
				sortorder: "desc",
				gridComplete : function(){
					addOperateMethod("product_table");
				},
				ondblClickRow: function(rowId){
					editRow("product_table",rowId);
				},
				loadComplete : function(){
					var grid = $("#product_table");
					if(grid.jqGrid('getDataIDs').length == 0){
						grid.jqGrid("addRowData",--idIndex,{id:idIndex});
					};
				}
			});
		}
		function certificationFormatter(value,o,obj){
			var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title='上传证书'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
			return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(obj.certificationFiles) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(obj.certificationFiles?obj.certificationFiles:'')+"'></input></div>";
		}
		
		//添加参数的方法
		function addOperateMethod(tableId){
			var grid = jQuery("#" + tableId);
			var ids = grid.jqGrid('getDataIDs');
			for(var i=0;i < ids.length;i++){ 
				var cl = ids[i]; 
				var operations = "<a class=\"small-button-bg\" href=\"javascript:addRow('"+tableId+"',"+cl+");\" title=\"添加\"><span class=\"ui-icon ui-icon-plusthick\" style='cursor:pointer;'></span></a>"
					+"&nbsp;<a class=\"small-button-bg\" onclick=\"delRow('"+tableId+"',"+cl+",event);\" href=\"#\" title=\"删除\"><span class=\"ui-icon ui-icon-closethick\" style='cursor:pointer;'></span></a>";
					grid.jqGrid('setRowData',ids[i],{act:operations});
			}
		}
		function priAddRow(){
			var grid = $("#product_table");
			var tableId="product_table";
			var initdata = {};
			if (!editing) {
				var id = --idIndex;
				initdata.id = id;
				grid.jqGrid(
					'addRow',
					{
						rowID : id,
						position : "last",
						initdata : initdata,
						addRowParams : {
							keys:true,
							aftersavefunc : function(rowId, data) {
								afterSaveRow(tableId,rowId,data);
							},
							afterrestorefunc :function(rowId){
								makeEditable(tableId,true);
							},
							url : 'clientArray'
						}
					});
				lastSelect[tableId] = id;
				makeEditable(tableId,false);
				editFun(tableId,id);
			}
		}
		//添加方法
		function addRow() {
			var grid = $("#product_table");
			var tableId="product_table";
			function priAddRow(){
				var initdata = {};
				if (!editing) {
					var id = --idIndex;
					initdata.id = id;
					grid.jqGrid(
						'addRow',
						{
							rowID : id,
							position : "last",
							initdata : initdata,
							addRowParams : {
								keys:true,
								aftersavefunc : function(rowId, data) {
									afterSaveRow(tableId,rowId,data);
								},
								afterrestorefunc :function(rowId){
									makeEditable(tableId,true);
								},
								url : 'clientArray'
							}
						});
					lastSelect[tableId] = id;
					makeEditable(tableId,false);
					editFun(tableId,id);
					if(tableId="product_table"){
						$("#" + id + "_code").attr("readonly", "readonly").click(
								function() {
									supplyMaterialClick(id);
							});
					}
				}
			}
			if(editing&&lastSelect[tableId]){
				var obj = grid.jqGrid("getRowData",lastSelect[tableId]);
				if(obj.id){
					grid.jqGrid("saveRow",lastSelect[tableId],{
						url:'clientArray',
						aftersavefunc : function(rowId, data) {
							$("#" + rowId + " .upload").hide();
							addUpateParams(tableId,lastSelect[tableId]);
							priAddRow();
						}
					});
				}else{
					priAddRow();
				}
			}else{
				priAddRow();
			}
		}
		
		//添加修改的编号
		function addUpateParams(tableId,id){
			var obj = updateParams[tableId];
			if(!obj){
				obj = {};
				updateParams[tableId] = obj;
			}
			if(!obj[id]){
				obj[id] = true;
			}
		}
		
		//添加删除的编号
		function addDeleteParams(tableId,id){
			if(id > 0){
				var obj = deleteParams[tableId];
				if(!obj){
					deleteParams[tableId] = {};
					obj = deleteParams[tableId];
				}
				if(!obj[id]){
					obj[id] = true;
				}
			}
			var obj = updateParams[tableId];
			if(!obj){
				obj = {};
				updateParams[tableId] = obj;
			}
			if(obj[id]){
				obj[id] = false;
			}
		}
		var editing=false;
		function makeEditable(tableId,editable){
			if(editable){
				editing=false;
				jQuery("#"+tableId+" tbody").sortable('enable');
			}else{
				editing=true;
				jQuery("#"+tableId+" tbody").sortable('disable');
			}
		}
		function editNextRow(tableId,rowId){
			var grid = jQuery("#" + tableId);
			var ids=grid.jqGrid("getDataIDs");
			var index=grid.jqGrid("getInd",rowId);
			index++;
			if(index>ids.length){//当前编辑行是最后一行
				addRow(tableId,true);
			}else{
				editRow(tableId,ids[index-1]);
			}
		}
		function afterSaveRow(tableId,rowId,data){
			$("#" + rowId + " .upload").hide();
			addUpateParams(tableId,rowId);
			lastSelect[tableId] = null;
			editNextRow(tableId,rowId);
		}
		//编辑表格
		function editRow(tableId,rowId){
			var grid = $("#" + tableId);
			function priEditRow(){
				lastSelect[tableId]=rowId;
				grid.jqGrid("editRow",rowId,{
					keys:true,
					aftersavefunc : function(rowId, data) {
						afterSaveRow(tableId,rowId,data);
					},
					afterrestorefunc :function(rowId){
						makeEditable(tableId,true);
						$("#product-message").html("");
					},
					beforeEditCell :function(rowId){
						alert("11");
					},
					url : 'clientArray'
				});
				makeEditable(tableId,false);
				editFun(tableId,rowId);
			}
			if(lastSelect[tableId]&&rowId!=lastSelect[tableId]){
				var obj = grid.jqGrid("getRowData",lastSelect[tableId]);
				if(obj.id){
					grid.jqGrid("saveRow",lastSelect[tableId],{
						url:'clientArray',
						aftersavefunc : function(rowId, data) {
							addUpateParams(tableId,lastSelect[tableId]);
							$("#" + rowId + " .upload").hide();
							priEditRow();
						}
					});
				}else{
					priEditRow();
				}
			}else{
				priEditRow();
			}
		}
		function addBomClick(){
			alert();
		}
		//删除对象
		function delRow(tableId,rowId,event) {
			var grid = $("#" + tableId); 
			grid.jqGrid("delRowData",rowId);
			addDeleteParams(tableId,rowId);
			if(grid.jqGrid('getDataIDs').length == 0){
				idIndex--;
				grid.jqGrid("addRowData",idIndex,{id:idIndex,applyState:defaultState});
			};
			if(rowId == lastSelect['product_table']){
				lastSelect['product_table'] = null;
				makeEditable(tableId,true);
			}
			try {
				var evt = window.event || event;
				evt.cancelBubble=true;
			} catch (e) {}
		}
		var params = {};
		var editFumObj = {
				'product_table' : function(id){
					setTimeout(function(){
						$("#product-message").html("");
						var rowData = $("#product_table").jqGrid("getRowData",id);
						if(rowData){
							$("#" + id + "_code").click(function(){
							});
							$("#" + id + "_name").click(function(){
							});
							$("#" + id + "_importance").click(function(){
							});
							$("#" + id + "_materialType").click(function(){
							});
						}
					},100);
				},
			};
			function editFun(tableId,id){
				if(editFumObj[tableId]){
					editFumObj[tableId](id);
				}
			}
			var selRowId = null;
			function selectComponentCode(rowId){
				selRowId = rowId;
		 		var url = '${carmfgctx}/common/product-bom-select.htm?multiselect=true';
		 		$.colorbox({href:url,iframe:true, 
		 			innerWidth:700, innerHeight:400,
		 			overlayClose:false,
		 			title:"选择产品"
		 		});
		 	}
			
			function setFullBomValue(datas){
				$("#" + selRowId + "_code").val(datas[0].code);
				$("#" + selRowId + "_name").val(datas[0].name);
				$("#" + selRowId + "_materialType").val(datas[0].materialType);
				$("#" + selRowId + "_importance").val(datas[0].importance);
				if(datas.length>1){
					var lastId = selRowId;
					for(var i=1;i<datas.length;i++){
						var id = --idIndex;
						var data = {
							id : id,
							code : datas[i].code,
							name : datas[i].name,
							materialType : datas[i].materialType,
							importance : datas[i].importance,
							applyState : defaultState
						};
						$("#product_table").jqGrid("addRowData",id,data,'after',lastId);	
						lastId = id;
						addUpateParams("product_table",id);
					}	
				};
				
		 	}
			
	</script>
</head>