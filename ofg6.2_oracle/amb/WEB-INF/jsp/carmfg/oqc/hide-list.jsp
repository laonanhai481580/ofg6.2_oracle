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
	function selectBusinessUnit(obj){
		var businessUnit=$("#businessUnit").val();
		var productType=$("#productType").val();
		window.location.href = encodeURI('${mfgctx}/oqc/hide-list.htm?businessUnit='+ businessUnit+"&productType="+productType);
	}
	function inputNew(obj){
		var businessUnit=$("#businessUnit").val();
		var productType=$("#productType").val();
		window.location.href = encodeURI('${mfgctx}/oqc/input-new.htm?businessUnit='+ businessUnit+"&productType="+productType);
	}	
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
	 function exportDatas(obj){
		var businessUnit=$("#businessUnit").val();
		var productType=$("#productType").val();
 		var title = $(obj).attr("title");
 		if(title=="全部导出"){
 			iMatrix.export_Data("${mfgctx}/oqc/export-hide.htm?businessUnit="+businessUnit);
 		}else{
 			iMatrix.export_Data("${mfgctx}/oqc/export-hide.htm?businessUnit="+businessUnit+"&productType="+productType);
 		}
	} 		
	var params = {};
 	function hide(obj){
 		var id = $("#inspectionList").jqGrid("getGridParam","selarrrow");
 		if(id.length==0){
 			alert("请选择需要取消敏感标记的数据！");
 			return ;
 		} 		
 		var url="${mfgctx}/oqc/hiddenState.htm?id="+id+"&&type=Y";
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
		var thirdMenu="_OQC_INSPECTION_HIDE";
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
				<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<security:authorize ifAnyGranted="MFG_OQC_EXPORT_HIDE">
				<button class="btn" onclick="exportDatas(this);"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="MFG_OQC_HIDE">
					<button class='btn' onclick="hide(this)" type="button">
						<span><span><b class="btn-icons btn-icons-undo"></b>取消标记</span></span>
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
						<grid:jqGrid  gridId="inspectionList" url="${mfgctx}/oqc/hide-list-datas.htm" code="MFG_OQC_INSPECTION_HIDE" pageName="dynamicPage" dynamicColumn="${dynamicColumn}" ></grid:jqGrid>
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