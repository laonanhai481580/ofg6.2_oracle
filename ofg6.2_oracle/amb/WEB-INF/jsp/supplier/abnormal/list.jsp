<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.supplier.entity.Supplier"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<%@ include file="/common/supplier-add.jsp" %>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
		var isFirst = true;
		$.extend($.jgrid.defaults,{
			beforeRequest : function(){
				if(isFirst){
					isFirst = false;
					contentResize();
				}
			},
		});
		function contentResize(){
			var gridWidth = $(".ui-layout-center").width()-20;
			var boxHeight = 0;
			if($("#search_box").is(":visible")){
				boxHeight = $("#search_box").height();
			}
			$("#inspectionList").jqGrid("setGridWidth",gridWidth);
			var gridHeight = $("#opt-content").height() - boxHeight - $(".ui-jqgrid-hbox").height() - 55;
			$("#inspectionList").jqGrid("setGridHeight",gridHeight);
		}	
	    function showSearchDiv(obj){
			iMatrix.showSearchDIV(obj);
			if($("#search_box").is(":visible")){
				var p = $("#search_box").position();
				var postData = $("#inspectionList").jqGrid("getGridParam","postData");
				//默认查询条件为今天
				if(postData.searchParameters==undefined){
				}
			}else{
				$("#search_box").css("top",40+"px");
				$("#gbox_inspectionList").css("top","0px");
			}
			contentResize();
		}	
		function $successfunc(response){
			var jsonData = eval("(" + response.responseText+ ")");
			if(jsonData.error){
				alert(jsonData.message);
			}else{
				return true;
			}
		}
		function customSave(gridId){
			if(lastsel==undefined||lastsel==null){
				alert("当前没有可编辑的行!");
				return;
			}
			var $grid = $("#" + gridId);
			var o = getGridSaveParams(gridId);
			if ($.isFunction(gridBeforeSaveFunc)){
				gridBeforeSaveFunc.call($grid);
			}
			$grid.jqGrid("saveRow",lastsel,o);
		}
		//重写(给单元格绑定事件)
		var params = {};
		var selRowId = null;
		function $oneditfunc(rowid){
			params = {
				hisAttachmentFiles : $("#" + rowid + "_hiddenAttachmentFiles").val()
			};
			selRowId = rowid;
			$("#" + rowid + "_unqualifiedRate").attr("disabled","disabled");
			$("#" + rowid + "_unqualifiedAmount").attr("disabled","disabled");
			$(":input[name]").each(function(index,obj){
				var id=obj.id;
				var str=id.split("_a")[1];
				if(str){
					$(obj).keyup(caculateBadCount);
				}
			});
		};
		function caculateBadCount(){
			var badCount=0;
			$(":input[name]").each(function(index,obj){
				var id=obj.id;
				var str=id.split("_a")[1];
				if(str){
					var count=obj.value;
					if(count!=""){
						badCount+=parseInt(count);
					}				
				}
			});
			if(badCount>0){
				$("#" + selRowId + "_unqualifiedAmount").val(parseInt(badCount));
				 caculateBadRate();
			}
		}
		function caculateBadRate(){
			var inputAmount = $("#" + selRowId + "_inputAmount").val();
			var unqualifiedAmount = $("#" + selRowId + "_unqualifiedAmount").val();
			if(isNaN(inputAmount)){
				alert("投入数必须为整数！");
				$("#" + selRowId + "_inputAmount").val("");
				$("#" + selRowId + "_inputAmount").focus();
				$("#" + selRowId + "_unqualifiedRate").val("");
				return;
			}

			if((inputAmount-0)<(unqualifiedAmount-0)){
				alert("不良数不能大于投入数！");
				$("#" + selRowId + "_unqualifiedRate").val("");
				return;
			}
			if(inputAmount&&unqualifiedAmount){
				var rate=unqualifiedAmount*100/inputAmount;
				$("#" + selRowId + "_unqualifiedRate").val(rate.toFixed(2)+"%");
			}else{
				$("#" + selRowId + "_unqualifiedRate").val("");
			}		
		}
		function supplierCodeClick(){
			$.colorbox({href:"${supplierctx}/archives/select-supplier.htm",iframe:true,
				width:$(window).width()<1000?$(window).width()-100:1000,
				height:$(window).height()<600?$(window).height()-100:600,
				overlayClose:false,
				title:"选择供应商"
			});
		}
		function supplierNameClick(){
			$.colorbox({href:"${supplierctx}/archives/select-supplier.htm",iframe:true,
				width:$(window).width()<1000?$(window).width()-100:1000,
				height:$(window).height()<600?$(window).height()-100:600,
				overlayClose:false,
				title:"选择供应商"
			});
		}
		var rowId=null;
		function itemNumberClick(obj){
			rowId=obj.rowid;
			$.colorbox({href:"${iqcctx}/inspection-base/inspection-bom/bom-inspection-select.htm",
				iframe:true, 
				width:$(window).width()<700?$(window).width()-100:900,
				height:$(window).height()<400?$(window).height()-100:600,
	 			overlayClose:false,
	 			title:"选择物料"
	 		});
		}
		function materialTypeClick(obj){
			rowId=obj.rowid;
			$.colorbox({href:"${supplierctx}/base-info/material-type-goal/select-material-type-list.htm",iframe:true,
				width:$(window).width()<1000?$(window).width()-100:1000,
				height:$(window).height()<600?$(window).height()-100:600,
				overlayClose:false,
				title:"选择物料类别"
			});
		}	
		function setInspectionBomValue(datas){
			$("#"+rowId+"_itemNumber").val(datas[0].materielCode);
	 	}	
		function setMaterialTypeValue(datas){
			$("#"+rowId+"_materialType").val(datas[0].materialType);
	 	}	
		function setSupplierValue(objs){
			var obj = objs[0];
			$("#" + selRowId + "_supplierName").val(obj.name);
			$("#" + selRowId + "_supplierCode").val(obj.code);
			//联系人
		} 
		function $processRowData(data){
			for(var pro in params){
				data[pro] = params[pro];
			}
			if(data.id){
				$("#" + data.id).find(":input[name]").each(function(index,obj){
					data[obj.name] = $(obj).val();
				});
			}
			data.businessUnit = $("#businessUnit").val();
			data.productType = $("#productType").val();
			return data;
		}
		
		//上传附件
		function formateMessageFile(value,o,obj){
			var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title='上传附件'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
			return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
		}
		function beginUpload(rowId){
			$.upload({
				showInputId : rowId + "_showAttachmentFiles",
				hiddenInputId : rowId + "_hiddenAttachmentFiles",
				callback : function(files){
					params.reportFile = $("#" + rowId + "_hiddenAttachmentFiles").val();
				}
			});
		}
		function $addGridOption(jqGridOption){
			jqGridOption.postData.businessUnit=$("#businessUnit").val();
			jqGridOption.postData.productType=$("#productType").val();
		}
		function inputNew(obj){
			var businessUnit=$("#businessUnit").val();
			window.location.href = encodeURI('${supplierctx}/abnormal/input-new.htm?businessUnit='+ businessUnit);
		}	
		function selectBusinessUnit(obj){
			var businessUnit=$("#businessUnit").val();
			var productType=$("#productType").val();
			window.location.href = encodeURI('${supplierctx}/abnormal/list.htm?businessUnit='+ businessUnit+"&productType="+productType);
		}
		function downloadTemplate(){
			window.location = '${supplierctx}/abnormal/download-template.htm';
		}
		 function exportDatas(obj){
				var businessUnit=$("#businessUnit").val();
				var productType=$("#productType").val();
		 		iMatrix.export_Data("${supplierctx}/abnormal/export.htm?businessUnit="+businessUnit+"&productType="+productType);
			}
		//导入台账数据
		function importDatas(){
	 		var businessUnit=$("#businessUnit").val();
			var url = encodeURI('${supplierctx}/abnormal/import.htm?businessUnit='+businessUnit);
			$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
				overlayClose:false,
				title:"导入台账数据",
				onClosed:function(){
					$("#inspectionList").trigger("reloadGrid");
				}
			});
		}
	</script>
	</head>

	<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
		<script type="text/javascript">
			var secMenu="abnormal";
			var thirdMenu="abnormalList";
		</script>


		<div id="header" class="ui-north">
			<%@ include file="/menus/header.jsp" %>
		</div>
		
		<div id="secNav">
			<%@ include file="/menus/supplier-sec-menu.jsp" %>
		</div>
		
		<div class="ui-layout-west">
			<%@ include file="/menus/supplier-abnormal-menu.jsp" %>
		</div>
		<div class="ui-layout-center">
			<div class="opt-body">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="supplier-abnormal-save">
					    <button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="supplier-abnormal-delete">
					    <button class='btn' onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button class='btn' onclick="showSearchDiv(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="supplier-abnormal-export">
				      <button class="btn" onclick="exportDatas(this);"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>	
				    </security:authorize> 
					<security:authorize ifAnyGranted="supplier-abnormal-import">
						<button class='btn' onclick="importDatas();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>
						<button class="btn" onclick="downloadTemplate();"><span><span><b class="btn-icons btn-icons-download"></b>下载导入模板</span></span></button>
					</security:authorize>	
					<button class="btn" onclick="iMatrix.settingColumns();"><span><span>配置字段</span></span></button>
							<security:authorize ifAnyGranted="SUPPLIER_HIDE">
							<button class="btn" myType='settingData' onclick="iMatrix.setDataVisible(this);"><span><span>配置数据显示</span></span></button>
							</security:authorize>					  	
					  	  厂区：
								 <s:select list="businessUnits" 
									theme="simple"
									listKey="value" 
									listValue="name" 
									id="businessUnit"
									name="businessUnit"
									onchange="selectBusinessUnit(this)"
									cssStyle="width:100px"
									emptyOption="false"
									labelSeparator="">
								</s:select> 
						  物料类别：
						 <s:select list="productTypes" 
							theme="simple"
							listKey="value" 
							listValue="name" 
							id="productType"
							name="productType"
							onchange="selectBusinessUnit(this)"
							cssStyle="width:100px"
							emptyOption="false"
							labelSeparator="">
						</s:select> 
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>	
					<security:authorize ifAnyGranted="supplier-add">					
						<div style="float:right;position:absolute; right:50px;top:6px;" >
						 <s:select list="systemCodes" 
									theme="simple"
									listKey="value" 
									listValue="name" 
									id="systemCode"
									onchange="systemCodeChange('abnormal/list.htm')"
									cssStyle="width:60px"
									emptyOption="false"
									labelSeparator="">
						</s:select> 
						</div>					
					</security:authorize>			
				</div>
				<div id="opt-content" style="clear:both;">
					<form id="contentForm" name="contentForm" method="post" action="">
						<grid:jqGrid gridId="inspectionList"
							url="${supplierctx}/abnormal/list-datas.htm" code="SUPPLIER_ABNORMAL" pageName="dynamicPage" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
							<script type="text/javascript">
							$(document).ready(function(){
								$("#inspectionList").jqGrid('setGroupHeaders', {
									  useColSpanStyle: true, 
									  groupHeaders:${groupHeaders}
									});
							});
							function $gridComplete(){
								$("td[role=gridcell]").each(function(index,obj){
									var ariaDescribedby = $(obj).attr("aria-describedby");
									if(ariaDescribedby&&ariaDescribedby=="inspectionList_unqualifiedRate"){
										var title = $(obj).attr("title");	
										if(!title){
											var inputCount=	$(obj).parent().find("td[aria-describedby=inspectionList_inputCount]").attr("title");
											var unqualifiedCount=	$(obj).parent().find("td[aria-describedby=inspectionList_unqualifiedCount]").attr("title");
											if(inputCount&&unqualifiedCount){
												var rate=unqualifiedCount*100/inputCount;
												$(obj).html(rate.toFixed(2)+"%");
											}
										}
									}
								});
							}
						</script>
					</form>
				</div>
			</div>
		</div>
	</body>
	</html>