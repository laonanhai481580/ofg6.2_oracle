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
	function click(cellvalue, options, rowObject){	
		return "<a href='javascript:void(0);' onclick='creatInput("+rowObject.id+");'>"+cellvalue+"</a>";
		//return "<a href='${iqcctx}/inspection-report/input.htm?id="+rowObject.id+"'>"+cellvalue+"</a>";
	}
	function creatInput(id){
		if(id){
			window.open('${iqcctx}/inspection-report/input.htm?id='+id);
		}		
	}
 	function exceptionConfirm(obj){
 		var rowIds = $("#dynamicInspection").jqGrid("getGridParam","selarrrow");
		if(rowIds.length==0){
			alert("请选择数据!");
			return;
		}
		$.post("${iqcctx}/inspection-report/exception-confirm.htm",{ids:rowIds.join(",")},function(result){
			if(result.error){
				alert(result.message);		
			}else{
				alert(result.message);
				$("#dynamicInspection").jqGrid("setGridParam").trigger("reloadGrid");
			};
		},'json');
	}
	function returnAudit(obj){
 		var rowIds = $("#dynamicInspection").jqGrid("getGridParam","selarrrow");
		if(rowIds.length==0){
			alert("请选择数据!");
			return;
		}
		$.post("${iqcctx}/inspection-report/return-audit.htm",{ids:rowIds.join(",")},function(result){
			if(result.error){
				alert(result.message);		
			}else{
				alert(result.message);
				$("#dynamicInspection").jqGrid("setGridParam").trigger("reloadGrid");
			};
		},'json');
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="inspectionReport";
		var thirdMenu="exception_confirm";
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
					<security:authorize ifAnyGranted="iqc-incomingInspectionActionsReport-delete">
						<button class='btn' type="button" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="iqc_incomingInspectionActionsReport_exception_confirm">
						<button class='btn' onclick="exceptionConfirm(this)" type="button">
							<span><span><b class="btn-icons btn-icons-submit"></b>异常确认</span></span>
						</button>
					</security:authorize>
					<security:authorize ifAnyGranted="iqc_incomingInspectionActionsReport_return">
						<button class='btn' onclick="returnAudit(this)" type="button">
							<span><span><b class="btn-icons btn-icons-back"></b>取回</span></span>
						</button>
					</security:authorize>
						<button class='btn' type="button" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="IQC_INSPECTION_REPORT_LIST_EXPORT">
						<button class='btn' type="button" onclick="iMatrix.export_Data('${iqcctx}/inspection-report/export.htm?state=exception');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="dynamicInspection"  url="${iqcctx}/inspection-report/exception-confirm-list-datas.htm" code="IQC_UNIIR" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>