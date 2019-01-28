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
 	   <style type="text/css">
    	.ui-jqgrid .ui-jqgrid-htable th div {
		    height:auto;
		    overflow:hidden;
		    padding-right:2px;
		    padding-top:2px;
		    position:relative;
		    vertical-align:text-top;
		    white-space:normal !important;
		}
		#flagExport{
			position:absolute; 
			display: none;
			z-index:999;
			background: #eee;
			border:3px solid #e1e1e1;
			box-shadow:0px 0px 4px #888;border-radius: 3px;margin: 0px;
		}
		#flagExport ul{ padding: 0px;margin: 0px;left: auto;width:160px;}
		#flagExport ul li{ padding: 2px 6px; list-style-type: none; width:150px;height: 20px;}
		#flagExport ul li:hover{background:#6CA412;}
		#flagExport ul li a {padding-left: 4px;color:#555;cursor:pointer}
		#flagExport ul li:hover a {padding-left: 5px;color:#fff;}
    </style> 
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
	function selectBusinessUnit(obj){
		var businessUnit=$("#businessUnit").val();
		var productType=$("#productType").val();
		window.location.href = encodeURI('${mfgctx}/oqc/list.htm?businessUnit='+ businessUnit+"&productType="+productType);
	}
	function inputNew(obj){
		var businessUnit=$("#businessUnit").val();
		var productType=$("#productType").val();
		window.location.href = encodeURI('${mfgctx}/oqc/input-new.htm?businessUnit='+ businessUnit+"&productType="+productType);
	}	
	//重写调整高度
/*  	function contentResize(){
		var tableHeight=$('.ui-layout-center').height()-210;
		var tableWidth=_getTableWidth();
		jQuery("#inspectionList").jqGrid('setGridHeight',tableHeight);
		jQuery("#inspectionList").jqGrid('setGridWidth',tableWidth);
	
	}  */
	//重写(单行保存前处理行数据)
	function $processRowData(data){
		data.businessUnit = $("#businessUnit").val();
		data.productType = $("#productType").val();
		return data;
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
	function $addGridOption(jqGridOption){
		jqGridOption.postData.businessUnit=$("#businessUnit").val();
		jqGridOption.postData.productType=$("#productType").val();
	}
	var selRowId = null;
	function $oneditfunc(rowId){
		selRowId = rowId;
		$("#" + rowId + "_count").keyup(caculateBadRate);
		$("#" + rowId + "_samplingCount").keyup(caculateBadRate);
		$("#" + rowId + "_unQualityCount").keyup(caculateBadRate);
		$("#" + rowId + "_unQualityRate").attr("disabled","disabled");
		$("#" + rowId + "_qualityBatchRate").attr("disabled","disabled");
		$("#" + rowId + "_qeManLogin").attr("disabled","disabled");
		$("#" + rowId + "_dutyManLogin").attr("disabled","disabled");
		$("#" + rowId + "_stockBatch").keyup(caculateBatchRate);
		$("#" + rowId + "_qualityBatch").keyup(caculateBatchRate);
		$("#" + rowId + "_factory").change(function(){
			factoryChange(rowId);
		});
		$(":input[name]").each(function(index,obj){
			var id=obj.id;
			var str=id.split("_a")[1];
			if(str){
				$(obj).keyup(caculateBadCount);
			}
		});
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
			$("#" + selRowId + "_unQualityCount").val(parseInt(badCount));
			 caculateBadRate();
		}
	}
	function caculateBatchRate(){
		var stockBatch = $("#" + selRowId + "_stockBatch").val();
		var qualityBatch = $("#" + selRowId + "_qualityBatch").val();
		if(isNaN(stockBatch)){
			alert("投入批数数必须为整数！");
			$("#" + selRowId + "_stockBatch").val("");
			$("#" + selRowId + "_stockBatch").focus();
			$("#" + selRowId + "_qualityBatchRate").val("");
			return;
		}
		if(isNaN(qualityBatch)){
			alert("检验批数数必须为整数！");
			$("#" + selRowId + "_qualityBatch").val("");
			$("#" + selRowId + "_qualityBatch").focus();
			$("#" + selRowId + "_qualityBatchRate").val("");
			return;
		}
		if((stockBatch-0)<(qualityBatch-0)){
			alert("检验批数不能大于投入批数！");
			$("#" + selRowId + "_qualityBatchRate").val("");
			return;
		}
		if(qualityBatch&&stockBatch){
			var rate=qualityBatch*100/stockBatch;
			$("#" + selRowId + "_qualityBatchRate").val(rate.toFixed(2)+"%");
		}else{
			$("#" + selRowId + "_qualityBatchRate").val("");
		}		
	}
	function caculateBadRate(){
		var count = $("#" + selRowId + "_count").val();
		var samplingCount = $("#" + selRowId + "_samplingCount").val();
		var unQualityCount = $("#" + selRowId + "_unQualityCount").val();
		if(isNaN(count)){
			alert("检验数必须为整数！");
			$("#" + selRowId + "_count").val("");
			$("#" + selRowId + "_count").focus();
			$("#" + selRowId + "_unQualityRate").val("");
			return;
		}
		if(isNaN(unQualityCount)){
			alert("不良数必须为整数！");
			$("#" + selRowId + "_unQualityCount").val("");
			$("#" + selRowId + "_unQualityCount").focus();
			$("#" + selRowId + "_unQualityRate").val("");
			return;
		}
		if(isNaN(samplingCount)){
			alert("抽检数必须为整数！");
			$("#" + selRowId + "_samplingCount").val("");
			$("#" + selRowId + "_samplingCount").focus();
			$("#" + selRowId + "_unQualityRate").val("");
			return;
		}
		if((count-0)<(samplingCount-0)){
			alert("抽检数不能大于检验数量！");
			$("#" + selRowId + "_unQualityRate").val("");
			return;
		}
		if((samplingCount-0)<(unQualityCount-0)){
			alert("不良数不能大于抽检数量！");
			$("#" + selRowId + "_unQualityRate").val("");
			return;
		}
		if(unQualityCount&&samplingCount){
			var rate=unQualityCount*100/samplingCount;
			$("#" + selRowId + "_unQualityRate").val(rate.toFixed(2)+"%");
		}else{
			$("#" + selRowId + "_unQualityRate").val("");
		}		
	}
	function partsCodeClick(){
 		var url = '${carmfgctx}/common/product-bom-select-for-improve.htm';
 		$.colorbox({href:url,iframe:true, 
 			innerWidth:700, 
			innerHeight:500,
 			overlayClose:false,
 			title:"选择"
 		});
 	}
 	//选择之后的方法 data格式{key:'a',value:'a'}
 	function setBomValue(datas){
 		$('#'+selRowId+'_partCode').val(datas[0].key);
 		$('#'+selRowId+'_partName').val(datas[0].value);
 	}
 	
 	//导入台账数据
	function importDatas(){
 		var businessUnit=$("#businessUnit").val();
		var url = encodeURI('${mfgctx}/oqc/import.htm?businessUnit='+businessUnit);
		$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
			overlayClose:false,
			title:"导入台账数据",
			onClosed:function(){
				$("#inspectionList").trigger("reloadGrid");
			}
		});
	}
	//选择提醒人员
	var selRowId = null;
	function dutyManClick(obj){	
		selRowId=obj.rowid;
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择人员',
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'loginName',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:'personId',
			showInputId:'personName',
			acsSystemUrl:acsSystemUrl,
			callBack:function(){
				setDutyMan();
			}
		});			
	}
	function qeManClick(obj){	
		selRowId=obj.rowid;
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择人员',
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'loginName',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:'personId',
			showInputId:'personName',
			acsSystemUrl:acsSystemUrl,
			callBack:function(){
				setqeMan();
			}
		});			
	}
	
	function setDutyMan(){
		var warmingMan=$("#personName").val();
		$("#"+selRowId+"_dutyMan").val(warmingMan);
		var warmingManLogin=$("#personId").val();
		$("#"+selRowId+"_dutyManLogin").val(warmingManLogin);
	}
	function setqeMan(){
		var warmingMan=$("#personName").val();
		$("#"+selRowId+"_qeMan").val(warmingMan);
		var warmingManLogin=$("#personId").val();
		$("#"+selRowId+"_qeManLogin").val(warmingManLogin);
	}
	function downloadTemplate(){
		window.location = '${mfgctx}/oqc/download-template.htm';
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
		//hideIdentifiersDiv();
		var businessUnit=$("#businessUnit").val();
		var productType=$("#productType").val();
 		var title = $(obj).attr("title");
 		if(title=="全部导出"){
 			iMatrix.export_Data("${mfgctx}/oqc/export.htm?businessUnit="+businessUnit);
 		}else{
 			iMatrix.export_Data("${mfgctx}/oqc/export.htm?businessUnit="+businessUnit+"&productType="+productType);
 		}
	} 
	function copy(){
		var rowIds = $("#inspectionList").jqGrid("getGridParam","selarrrow");
		var copyId=rowIds[0];
		if(rowIds.length==0){
			alert("请选择需要复制的数据!");
			return false;
		}
		if(rowIds.length>1){
			alert("只能选择一条数据!");
			return false;
		}		
		$.post("${mfgctx}/oqc/copy.htm?id="+ copyId, null,function(data) {
			if (data.error) {
				alert(data.message);
				return false;
			}
			alert(data.message);
			$("#inspectionList").trigger("reloadGrid");
		}, "json");
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
		$.colorbox({href:"${mfgctx}/oqc/input.htm?id=" + id,iframe:true,
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
	
 	function modelClick(){
		var customerName=$("#"+selRowId+"_customer").val();
		if(!customerName){
			alert("请先选择客户！");
		}
 		var url = '${aftersalesctx}/base-info/customer/model-select.htm?customerName='+customerName;
 		$.colorbox({href:url,iframe:true, 
 			innerWidth:700, 
			innerHeight:500,
 			overlayClose:false,
 			title:"选择机型"
 		});
 	}
	//选择之后的方法 data格式{key:'a',value:'a'}
 	function setProblemValue(datas){
 		$("#"+selRowId+"_model").val(datas[0].value);
 	}
 	var params = {};
 	function hide(obj){
 		var id = $("#inspectionList").jqGrid("getGridParam","selarrrow");
 		if(id.length==0){
 			alert("请选择需要标记为敏感的数据！");
 			return ;
 		} 		
 		var url="${mfgctx}/oqc/hiddenState.htm?id="+id+"&&type=N";
		$.post(encodeURI(url),params,function(result){
			if(result.error){
				alert("操作失败,"+result.message);
			}else{
				alert(result.message);
			};
			$("#inspectionList").jqGrid("setGridParam").trigger("reloadGrid");
		},'json');
 	} 	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	
	<script type="text/javascript">
		var secMenu="oqc_list";
		var thirdMenu="_OQC_INSPECTION_LIST";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-oqc-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="mfg_oqc_save">
					<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					<button class='btn' onclick="inputNew();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>表单新建</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="mfg_oqc_save">
				<button class='btn' onclick="customSave('inspectionList');" type="button"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				</security:authorize>					
				<security:authorize ifAnyGranted="mfg_oqc_delete">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
<!-- 				<button class='btn' onclick="editInput();" type="button"><span><span><b class="btn-icons btn-icons-edit"></b>编辑</span></span></button> -->
				<button class='btn' onclick="showSearchDiv(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<security:authorize ifAnyGranted="MFG_OQC_EXPORT">
				<button class="btn" onclick="exportDatas(this);"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				<%-- <button class="btn" onclick="iMatrix.export_Data('${mfgctx}/oqc/export-most.htm?businessUnit=${businessUnit }');"><span><span><b class="btn-icons btn-icons-export"></b>合并导出</span></span></button> --%>
				<!--  <button class='btn' type="button" id="_export_button" onclick='showIdentifiersDiv("flagExport","_export_button");'><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>  -->
				</security:authorize>
				<security:authorize ifAnyGranted="MFG_OQC_IMPORT">
				<button class='btn' onclick="importDatas();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>
				<button class="btn" onclick="downloadTemplate();"><span><span><b class="btn-icons btn-icons-download"></b>下载导入模板</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="MFG_OQC_COPY">
				<button class="btn" onclick="copy();"><span><span><b class="btn-icons btn-icons-import"></b>复制</span></span></button>
				</security:authorize>	
				<security:authorize ifAnyGranted="MFG_OQC_HIDE">
					<button class='btn' onclick="hide(this)" type="button">
						<span><span><b class="btn-icons btn-icons-undo"></b>标记敏感</span></span>
					</button>
				</security:authorize>				
						
				厂区：
				 <s:select list="businessUnits" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="businessUnit"
					name="businessUnit"
					onchange="selectBusinessUnit(this)"
					cssStyle="width:120px"
					emptyOption="false"
					labelSeparator="">
				</s:select> 
				产品类别：
				 <s:select list="productTypes" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="productType"
					name="productType"
					onchange="selectBusinessUnit(this)"
					cssStyle="width:120px"
					emptyOption="false"
					labelSeparator="">
				</s:select> 
				<input type="hidden" name="personName"  id="personName" value=""/>
				<input type="hidden" name="personId"  id="personId" value=""/>				
			<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
			</div>
			<div style="display: none;" id="message"><font class=onSuccess><nobr>删除成功</nobr></font></div>
			<div id="opt-content" >
				<input type="hidden" id="colCode"  name="colCode" value="${colCode}"/>
				<div id="flagExport" onmouseover='show_moveiIdentifiersDiv("flagExport");' onmouseout='hideIdentifiersDiv("flagExport");'>
						<ul style="width:160px;">
							 <li onclick="exportDatas(this);" style="cursor:pointer;" title="全部导出">全部导出</li>
							 <li onclick="exportDatas(this);" style="cursor:pointer;" title="分开导出">分开导出</li>
						</ul>
					</div>
				<form id="contentForm" name="contentForm" method="post"  action=""  >
						<grid:jqGrid  gridId="inspectionList" url="${mfgctx}/oqc/list-datas.htm" code="MFG_OQC_INSPECTION" pageName="dynamicPage" dynamicColumn="${dynamicColumn}" ></grid:jqGrid>
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