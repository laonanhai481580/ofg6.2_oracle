<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript">
 	function hide(obj){
 		var id = $("#dynamicInspection").jqGrid("getGridParam","selarrrow");
 		if(id.length==0){
 			alert("请选择一项！");
 			return ;
 		}
 		
 		$.post('${iqcctx}/inspection-report/hiddenState.htm?id='+id+"&&type=Y",
 		function(data){
 			  window.location.reload(href='${iqcctx}/inspection-report/list-y.htm');
			  alert("修改成功");
 		});
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="inspectionReport";
		var thirdMenu="inspection_y";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-inspection-report-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn" id="btnDiv">
					<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_INPUT_ADD">
						<button class='btn' type="button" onclick="createReport();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="iqc-delete">
						<button class='btn' type="button" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
						<button class='btn' type="button" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_LIST_EXPORT">
						<button class='btn' type="button" onclick="iMatrix.export_Data('${iqcctx}/inspection-report/export.htm?state=hide');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					 <security:authorize ifAnyGranted="iqc_incomingInspectionActionsReport_hide">
						<button class='btn' onclick="hide(this)" type="button">
							<span><span><b class="btn-icons btn-icons-undo"></b>取消隐藏</span></span>
						</button>
					</security:authorize>
				</div>
				<div id="opt-content">
					<!-- <div id="flagExport" onmouseover='show_moveiIdentifiersDiv("flagExport");' onmouseout='hideIdentifiersDiv("flagExport");'>
						<ul style="width:160px;">
							 <li onclick="backToTask(this,'flagExport');" style="cursor:pointer;" title="导出检验单">导出检验单</li>
							 <li onclick="backToTask(this,'flagExport');" style="cursor:pointer;" title="导出异常检验单">导出异常检验单</li>
						</ul>
					</div> -->
					<div id="flag" onmouseover='show_moveiIdentifiersDiv("flag");' onmouseout='hideIdentifiersDiv("flag");'>
						<ul style="width:130px;">
							 <li onclick="backToTask(this,'flag');" style="cursor:pointer;" title="导入EXCEL删除">导入EXCEL删除</li>
							 <li onclick="backToTask(this,'flag');" style="cursor:pointer;" title="下载EXCEL格式">下载附件格式</li>
						</ul>
					</div>
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="dynamicInspection"  url="${iqcctx}/inspection-report/list-hid.htm" code="IQC_IIAR" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
<%-- <script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script> --%>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>