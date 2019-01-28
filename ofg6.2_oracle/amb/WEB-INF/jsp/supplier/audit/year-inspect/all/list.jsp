<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
<c:set var="actionBaseCtx" value="${supplierctx}/audit/year-inspect/all" />
<script type="text/javascript">
	function click(cellvalue, options, rowObject) {
		return "<a  href='${actionBaseCtx}/input.htm?id="
				+ rowObject.id + "'>" + cellvalue + "</a>";
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu = "audit";
		var thirdMenu = "yearInspectAll";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-audit-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<button class='btn' onclick="iMatrix.showSearchDIV(this);">
					<span><span><b class="btn-icons btn-icons-search"></b>查询</span></span>
				</button>
					<security:authorize ifAnyGranted="supplier-year-inspect-export-all">
					    <button  class='btn' onclick="iMatrix.export_Data('${actionBaseCtx}/export.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>		
					</security:authorize>
			</div>
			<div id="opt-content" style="clear: both;">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="suppliers" url="${actionBaseCtx}/list-datas.htm"
						code="SUPPLIER_YEAR_INSPECT_ALL"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
</html>