<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
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
	function inputNew(obj){
		var larId=$("#larId").val();
		window.location.href = encodeURI('${aftersalesctx}/lar/item-input-new.htm?larId='+larId);
	}	
	//后台返回错误信息
	function $successfunc(response){
		var jsonData = eval("(" + response.responseText+ ")");
		if(jsonData.error){
			alert(jsonData.message);
		}else{
			return true;
		}
	}
	
	function $processRowData(data){
		data.larId = $("#larId").val();
		return data;
	}	
	var selRowId = null;
	function $oneditfunc(rowId){
		selRowId = rowId;
		$("#" + rowId + "_inputAmount").keyup(caculateBadRate);
		$("#" + rowId + "_inspectionAmount").keyup(caculateBadRate);
		$("#" + rowId + "_badAmount").keyup(caculateBadRate);
		$("#" + rowId + "_unQualifiedRate").attr("disabled","disabled");
		$(":input[name]").each(function(index,obj){
			var id=obj.id;
			var str=id.split("_a")[1];
			if(str){
				$(obj).keyup(caculateBadCount);
			}
		});
	}
	
	function customerModelClick(obj){
		selRowId=obj.rowid;	
		modelClick();
	}
	function ofilmModelClick(obj){
		selRowId=obj.rowid;	
		modelClick();
	}
 	function modelClick(){
		var customer=$("#customer").val();
 		var url = '${aftersalesctx}/base-info/customer/model-select.htm?customerName='+customer;
 		$.colorbox({href:url,iframe:true, 
 			innerWidth:700, 
			innerHeight:500,
 			overlayClose:false,
 			title:"选择机型"
 		});
 	}
 	
	//选择之后的方法 data格式{key:'a',value:'a'}
 	function setProblemValue(datas){
 		$("#"+selRowId+"_customerModel").val(datas[0].value);
 		$("#"+selRowId+"_ofilmModel").val(datas[0].key);
 	}	
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
			$("#" + selRowId + "_badAmount").val(parseInt(badCount));
		}
		caculateBadRate();
	}
	function caculateBadRate(){
		var inputAmount = $("#" + selRowId + "_inputAmount").val();
		var inspectionAmount = $("#" + selRowId + "_inspectionAmount").val();
		var badAmount = $("#" + selRowId + "_badAmount").val();
		if(isNaN(inputAmount)){
			alert("入料数必须为整数！");
			$("#" + selRowId + "_inputAmount").val("");
			$("#" + selRowId + "_inputAmount").focus();
			return;
		}
		if(isNaN(inspectionAmount)){
			alert("抽检数必须为整数！");
			$("#" + selRowId + "_inspectionAmount").val("");
			$("#" + selRowId + "_inspectionAmount").focus();
			return;
		}
		if(isNaN(badAmount)){
			alert("不良数必须为整数！");
			$("#" + selRowId + "_badAmount").val("");
			$("#" + selRowId + "_badAmount").focus();
			return;
		}
		if((inputAmount-0)<(inspectionAmount-0)){
			alert("抽检数不能大于入料数！");
			$("#" + selRowId + "_inspectionAmount").val("");
			return;
		}
		if((inspectionAmount-0)<(badAmount-0)){
			alert("不良数不能大于抽检数量！");
			$("#" + selRowId + "_badAmount").val("");
			return;
		}
		if(badAmount&&inspectionAmount){
			var rate=badAmount*100/inspectionAmount;
			$("#" + selRowId + "_unQualifiedRate").val(rate.toFixed(2)+"%");
		}		
	}
 	//导入台账数据
	function importDatas(){
		var url = encodeURI('${aftersalesctx}/lar/item-import.htm');
		$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
			overlayClose:false,
			title:"导入台账数据",
			onClosed:function(){
				$("#inspectionList").trigger("reloadGrid");
			}
		});
	}
	var selRowId = null;
	function downloadTemplate(){
		window.location = '${aftersalesctx}/lar/item-download-template.htm';
	}	
	/*---------------------------------------------------------
	函数名称:showIdentifiersDiv
	参          数:
	功          能:标识为（下拉选）
	------------------------------------------------------------*/
	function showIdentifiersDiv(idName,buttonId){
		if($("#"+idName).css("display")=='none'){
			removeSearchBox();
			$("#"+idName).show();
			var position = $("#"+buttonId).position();
			$("#"+idName).css("left",position.left+15);
			$("#"+idName).css("top",position.top+28);
		}else{
			$("#"+idName).hide();
		}
	}
	function show_moveiIdentifiersDiv(idName){
		clearTimeout(identifiersDiv);
	}
	var identifiersDiv;
	function hideIdentifiersDiv(idName){
		identifiersDiv = setTimeout('$("#"+idName+")".hide()',300);
	}
	 function exportDatas(obj){
		var larId=$("#larId").val();
 		iMatrix.export_Data("${aftersalesctx}/lar/item-export.htm?larId="+larId);		
	} 

	/*---------------------------------------------------------
	函数名称:customSave
	参          数:
		gridId	表格的ID
	功          能:自定义保存方法
	------------------------------------------------------------*/
	function customSave(gridId){		
		if(lastsel==undefined||lastsel==null){
			alert("当前没有可编辑的行!");
			return;
		}
		var $grid = $("#" + gridId);
		var o = getGridSaveParams(gridId);
		if ($.isFunction(gridBeforeSaveFunc)) {
			gridBeforeSaveFunc.call($grid);
		}
		$grid.jqGrid("saveRow",lastsel,o);
	}	
	function editInput(){
		var id = jQuery("#inspectionList").jqGrid('getGridParam','selarrrow');
		if(id.length==0){
			alert("请选择数据!");
			return;
		}
		if(id.length>1){
			alert("只能选择一条数据");
			return;
		}
		$.colorbox({href:"${aftersalesctx}/lar/item-input.htm?id=" + id,iframe:true,
			innerWidth:$(window).width()<1200?$(window).width()-50:1200, 
			innerHeight:$(window).height()<700?$(window).height()-100:$(window).height(),
 			overlayClose:false,
			title:"编辑信息",
			onClosed:function(){
				$("#inspectionList").trigger("reloadGrid");
			}
		});
	}
	function factoryChange(rowid){
		selRowId=rowid;	
		var factory=$("#"+selRowId+"_factory").val();
		var url = "${mfgctx}/base-info/factory-procedure/procedure-select.htm?factory="+factory;
		$.post(encodeURI(url),{},function(result){
 			if(result.error){
 				alert(result.message);
 			}else{
				var procedures = result.procedures;
				var procedureArr = procedures.split(",");
				var procedure = document.getElementById(selRowId+"_procedure");
				procedure.options.length=0;
				var opp1 = new Option("","");
				procedure.add(opp1);
 				for(var i=0;i<procedureArr.length;i++){
 					var opp = new Option(procedureArr[i],procedureArr[i]);
 					procedure.add(opp);
 				}
 			}
 		},'json');
	}	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	
	<script type="text/javascript">
		var secMenu="lar";
		var thirdMenu="lar_data";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/aftersales-sec-menu.jsp"%> 
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/aftersales-lar-third-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="AFS_LAR_ITEM_SAVE">
					<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					<button class='btn' onclick="inputNew();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>表单新建</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="AFS_LAR_ITEM_SAVE">
				<button class='btn' onclick="customSave('inspectionList');" type="button"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				</security:authorize>					
				<security:authorize ifAnyGranted="AFS_LAR_ITEM_DELETE">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<button class='btn' onclick="showSearchDiv(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<security:authorize ifAnyGranted="AFS_LAR_ITEM_EXPORT">
				<button class="btn" onclick="exportDatas(this);"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="AFS_LAR_ITEM_IMPORT">
				<!-- <button class='btn' onclick="importDatas();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button> -->
				<!-- <button class="btn" onclick="downloadTemplate();"><span><span><b class="btn-icons btn-icons-download"></b>下载导入模板</span></span></button> -->
				</security:authorize>			
				<input type="hidden" name="larId"  id="larId" value="${larId}"/>
				<input type="hidden" name="customer"  id="customer" value="${customer}"/>
				<input type="hidden" name="personName"  id="personName" value=""/>
				<input type="hidden" name="personId"  id="personId" value=""/>				
			<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
			</div>
			<div style="display: none;" id="message"><font class=onSuccess><nobr>删除成功</nobr></font></div>
			<div id="opt-content" >
				<input type="hidden" id="colCode"  name="colCode" value="${colCode}"/>
				<form id="contentForm" name="contentForm" method="post"  action=""  >
						<grid:jqGrid  gridId="inspectionList" url="${aftersalesctx}/lar/item-list-datas.htm?larId=${larId}" code="AFS_LAR_ITEM" pageName="dynamicPage" dynamicColumn="${dynamicColumn}" ></grid:jqGrid>
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
									if(ariaDescribedby&&ariaDescribedby=="inspectionList_unQualityRate"){
										var title = $(obj).attr("title");	
										if(!title){
											var samplingCount=	$(obj).parent().find("td[aria-describedby=inspectionList_samplingCount]").attr("title");
											var unQualityCount=	$(obj).parent().find("td[aria-describedby=inspectionList_unQualityCount]").attr("title");
											if(samplingCount&&unQualityCount){
												var rate=unQualityCount*100/samplingCount;
												$(obj).html(rate.toFixed(2)+"%");
											}
										}
									}
									if(ariaDescribedby&&ariaDescribedby=="inspectionList_qualityBatchRate"){
										var title = $(obj).attr("title");	
										if(!title){
											var stockBatch=	$(obj).parent().find("td[aria-describedby=inspectionList_stockBatch]").attr("title");
											var qualityBatch=	$(obj).parent().find("td[aria-describedby=inspectionList_qualityBatch]").attr("title");
											if(stockBatch&&qualityBatch){
												var rate=qualityBatch*100/stockBatch;
												$(obj).html(rate.toFixed(2)+"%");
											}
										}
									}
								});
							}
						</script>
				</form>
			</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>