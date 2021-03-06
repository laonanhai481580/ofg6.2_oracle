<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="bom";
		var treeMenu="bomCost";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-base-info-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name = "main">
				<div class="opt-btn">
				<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button" style="float:left;margin-left:4px;"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<security:authorize ifAnyGranted="MFG_BASE-INFO_BOM-COST_EXPORT">
				<button class='btn' type="button" onclick="iMatrix.export_Data('${mfgctx}/base-info/bom-cost/export.htm');" style="float:left;margin-left:4px;"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>
				</div>
				<div style="margin-top:15px; margin-left: 10px;">
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="bomList"  url="${mfgctx}/base-info/bom-cost/list-datas.htm" code="MFG_MATERIAL_COST_PRICE" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>