<%@page import="com.ambition.util.common.DateUtil"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"></script>
    <script type="text/javascript" src="${resourcesCtx}/widgets/validation/dynamic.validate.js"></script>
	<script type="text/javascript">
	var myLayout;
	$(document).ready(function(){
		myLayout = $('body').layout({
			north__paneSelector : '#header',
			north__size : 66,
			west__size : 250,
			north__spacing_open : 31,
			north__spacing_closed : 31,
			west__spacing_open : 6,
			west__spacing_closed : 6,
			center__minSize : 400,
			resizable : false,
			paneClass : 'ui-layout-panel',
			north__resizerClass : 'ui-layout-resizer',
			west__onresize : $.layout.callbacks.resizePaneAccordions,
			center__onresize : contentResize
		});
	});
	function click(cellvalue, options, rowObject){	
		return "<a href='${supplierctx}/evaluate/quarter/evaluate-total-table.htm?supplierId="+rowObject.supplierId+"&evaluateYear="+rowObject.evaluateYear+"&evaluateMonth="+rowObject.evaluateMonth+"&materialType="+rowObject.materialType+"'>"+cellvalue+"</a>";
	}
	function realTotalPointFormater(cellValue,objmodel,obj){
		return "<div style='color:red'>"+cellValue+"</div>";
	}
	
	function changeLocation(){
		var supplierName= $("#gys").val();
		var year= $("#year").val();
		var month= $("#month").val();
		var parentModelId= $("#parentModelId").val();
		var str = "?supplierName="+supplierName;
		str += "&year="+year; 
		str += "&month="+month; 
		str += "&parentModelId="+parentModelId; 
		$(document).mask();
		window.location = "${supplierctx}/evaluate/point-rank/performanceEvaluate-list.htm" + str;
	}
	
	//加载二级
	function onchangeGsmCodeSecRules(){
		var parentModelId = $("#parentModelId").val();
		$.post("${supplierctx}/evaluate/point-rank/change-model.htm?parentModelId="+parentModelId,function(result){
			$("#month").empty();
			if(result.months.length == 0){
				$("#month").attr("disabled","disabled");
				$("#month").css({background: "#eeeeee"});
				return false;
			}
			$("#month").attr("disabled","");
			$("#month").css({background: ""});
			for (var i = 0; i < result.months.length; i++) {
				var option = result.months[i];
				$("#month").append("<option value='"+option.value+"'>"+option.name+"</option>");
			}
		},'json');
	}
	//上传附件
	function showPicture(value,options,obj){
		var strs = "";
		strs = "<div style='width:100%;' title='上传附件' ><a class=\"small-button-bg\" onclick=\"attachmentFilesClick('"+obj.id+"');\" href=\"#\"><input type='hidden' id='"+obj.id+"_hiddenFiles' value='"+value+"'/><span id='"+obj.id+"_uploadBtn' class='ui-icon ui-icon-image uploadBtn' style='cursor:pointer;display:none;'></span></a><span style='text-align:left;' id='"+obj.id+"_showFiles'>"+$.getDownloadHtml(value)+"</span><div>";
		return strs;
	}
	function attachmentFilesClick(rowId){
		//上传附件 
		$.upload({   
			showInputId : rowId + "_showFiles",
			hiddenInputId : rowId + "_hiddenFiles",
			title:"上传附件",
			callback:function(files){
				params.attachmentFiles = $("#" + rowId + "_hiddenFiles").val();
			}
		}); 
	}
	function addNew(){
		iMatrix.addRow();
		$("#undefined_uploadBtn").show();
	}
	var params = {};
	function $oneditfunc(rowId){
		selRowId = rowId;
		$("#" + rowId + "_exemption").bind("click",function(){
			selectProject(selRowId);
		});
		$("#" + rowId + "_testReportExpire").attr("disabled","disabled");
		params.attachmentFiles = $("#" + rowId + "_hiddenFiles").val();
		$(".uploadBtn").hide();
		$("#undefined_uploadBtn").show();
		$("#" + rowId + "_uploadBtn").show();
	}
	function $afterrestorefunc(rowId){
		$("#" + rowId + "_uploadBtn").hide();
	}
	function $processRowData(data){
		for(var pro in params){
			data[pro] = params[pro];
		}
		return data;
	}
	$.extend($.jgrid.defaults,{
		loadComplete : function(obj){
			getGrid();
		}
	});
	function getGrid(cellvalue, options, rowObject) {
		 var ids = $("#list").getDataIDs();
        for(var i=0;i<=ids.length;i++){
            var rowData = $("#list").getRowData(ids[i]);
            if(rowData.grade=="D"){//
                $('#'+ids[i]).find("td[title=D]").addClass("SelectD");
            }else if(rowData.grade=="C"){//
                $('#'+ids[i]).find("td[title=D]").addClass("SelectC");
            }
        }
	}
	</script>
	<style type="text/css">
        .SelectD{
            background-color:#FF0000;
        }
        .SelectC{
            background-color:#FFFF00;
        }
            
    </style>
	<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
<script type="text/javascript">
		var secMenu="evaluate";
		var thirdMenu="total_improve_list";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west" id="west-ui">
		<%
			request.setAttribute("selLevel",1);
		%>
		<%@include file="left.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
		<aa:zone name="main">
			<div class="opt-btn">
				<table cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td valign="middle" style="padding: 0px;width:50%; margin: 0px;" id="btnTd">
							<button class="btn" onclick="iMatrix.showSearchDIV(this);onbindClick();"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
							<button  class='btn' onclick="iMatrix.export_Data('${supplierctx}/evaluate/quarter/export-all.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
						</td>
					</tr>
				</table>
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="list" url="${supplierctx}/evaluate/quarter/list-imporve.htm" code="SUPPLIER_EVALUATE_TOTAL_COPY"></grid:jqGrid>
				</form>
			</div>
		</aa:zone>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>