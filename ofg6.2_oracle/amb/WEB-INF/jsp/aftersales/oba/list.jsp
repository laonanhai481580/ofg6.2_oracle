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
		window.location.href = encodeURI('${aftersalesctx}/oba/list.htm?businessUnit='+ obj.value);
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
	}
	var selRowId = null;
	function $oneditfunc(rowId){
		selRowId = rowId;
		$("#" + rowId + "_inputCount").keyup(caculateBadRate);
		$("#" + rowId + "_unqualifiedCount").keyup(caculateBadRate);
		$("#" + rowId + "_unqualifiedRate").attr("disabled","disabled");
		$("#" + rowId + "_customerName").change(function(){
			customerNameChange(rowId);
		});
	}
	function caculateBadRate(){
		var inputCount = $("#" + selRowId + "_inputCount").val();
		var unqualifiedCount = $("#" + selRowId + "_unqualifiedCount").val();
		if(isNaN(inputCount)){
			alert("投入数必须为整数！");
			$("#" + selRowId + "_inputCount").focus();
			return;
		}
		if(isNaN(unqualifiedCount)){
			alert("不良数必须为整数！");
			$("#" + selRowId + "_unqualifiedCount").focus();
			return;
		}

		if((inputCount-0)<(unqualifiedCount-0)){
			alert("不良数不能大于投入数！");
			return;
		}
		var rate=unqualifiedCount*100/inputCount;
		$("#" + selRowId + "_unqualifiedRate").val(rate.toFixed(2)+"%");
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
		var customerName=$("#"+selRowId+"_customerName").val();
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
 		$("#"+selRowId+"_customerModel").val(datas[0].value);
 		$("#"+selRowId+"_ofilmModel").val(datas[0].key);
 	}
	function edit(){
		var rowIds = $("#inspectionList").jqGrid("getGridParam","selarrrow");
		var editId=rowIds[0];
		if(rowIds.length==0){
			alert("请选择需要编辑的数据!");
			return false;
		}
		if(rowIds.length>1){
			alert("只能同时编辑一条数据!");
			return false;
		}		
		var businessUnit=$("#businessUnit").val();
		window.location="${aftersalesctx}/oba/input.htm?businessUnit="+businessUnit;
	}	
	function editAdd(){
		var businessUnit=$("#businessUnit").val();
		window.location="${aftersalesctx}/oba/input.htm?businessUnit="+businessUnit;
	}		
	function customerNameChange(rowid){
		selRowId=rowid;	
		var customerName=$("#"+selRowId+"_customerName").val();
		var url = "${aftersalesctx}/base-info/customer/place-select.htm?customerName="+customerName;
		$.post(encodeURI(url),{},function(result){
 			if(result.error){
 				alert(result.message);
 			}else{
				var places = result.places;
				var placeArr = places.split(",");
				var place = document.getElementById(selRowId+"_place");
				place.options.length=0;
				var opp1 = new Option("","");
				place.add(opp1);
 				for(var i=0;i<placeArr.length;i++){
 					var opp = new Option(placeArr[i],placeArr[i]);
 					place.add(opp);
 				}
 			}
 		},'json');
	}
	
 	//导入台账数据
	function importDatas(){
 		var businessUnit=$("#businessUnit").val();
		var url = encodeURI('${aftersalesctx}/oba/import.htm?businessUnit='+businessUnit);
		$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
			overlayClose:false,
			title:"导入台账数据",
			onClosed:function(){
				$("#inspectionList").trigger("reloadGrid");
			}
		});
	}
	function downloadTemplate(){
		window.location = '${aftersalesctx}/oba/download-template.htm';
	}		
	function inputNew(obj){
		var businessUnit=$("#businessUnit").val();
		window.location.href = encodeURI('${aftersalesctx}/oba/input-new.htm?businessUnit='+ businessUnit);
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<input type="hidden" id="businessUnit"  value="${businessUnit}"/>
	<script type="text/javascript">
		var secMenu="oba";
		var thirdMenu="oba_data";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/aftersales-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/aftersales-oba-third-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
			<div class="opt-btn">
			<security:authorize ifAnyGranted="AFS_OBA_DATA_SAVE">
				<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				<button class='btn' onclick="inputNew();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>表单新建</span></span></button>
			</security:authorize>
			<security:authorize ifAnyGranted="AFS_OBA_DATA_DELETE">
				<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
			</security:authorize>
				<button class='btn' onclick="showSearchDiv(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
			<security:authorize ifAnyGranted="AFS_OBA_DATA_EDIT">
				<button class='btn' onclick="edit(this);" type="button"><span><span><b class="btn-icons btn-icons-image"></b>编辑</span></span></button>
			</security:authorize>
			<security:authorize ifAnyGranted="AFS_OBA_DATA_IMPORT"> 
				<button class='btn' onclick="importDatas();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>
				<button class="btn" onclick="downloadTemplate();"><span><span><b class="btn-icons btn-icons-download"></b>下载导入模板</span></span></button>
			</security:authorize>					
			<security:authorize ifAnyGranted="AFS_OBA_DATA_EXPORT">
				<button class="btn" onclick="iMatrix.export_Data('${aftersalesctx}/oba/export.htm?businessUnit=${businessUnit }');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
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
				<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
			</div>
			<div style="display: none;" id="message"><font class=onSuccess><nobr>删除成功</nobr></font></div>
			<div id="opt-content">
				<input type="hidden" id="colCode"  name="colCode" value="${colCode}"/>
				<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="inspectionList" url="${aftersalesctx}/oba/list-datas.htm" code="AFS_OBA_DATA" pageName="dynamicPage" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
						<script type="text/javascript">
							$(document).ready(function(){
								$("#inspectionList").jqGrid('setGroupHeaders', {
									  useColSpanStyle: true, 
									  groupHeaders:${groupHeaders}
									});
							});
						</script>
				</form>
			</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>