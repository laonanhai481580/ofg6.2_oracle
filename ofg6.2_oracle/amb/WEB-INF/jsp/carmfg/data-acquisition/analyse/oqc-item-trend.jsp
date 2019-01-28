<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<jsp:include page="oqc-trend-script.jsp" />
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="analyse";
		var thirdMenu="_oqc_bad_trend";
	</script>
	<%
		String _iframe = request.getParameter("_iframe");
		if(!"true".equalsIgnoreCase(_iframe)){
	%>
	<%@ include file="/menus/header.jsp"%>
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp"%>
	</div>
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-data-acquisition-menu.jsp"%>
	</div>
<% }%>
	<div class="ui-layout-center" style="overflow:hidden;">
		<form onsubmit="return false;" name="myform">
			<div class="opt-body" style="overflow-y:auto;">
				<div class="opt-btn" id="btnDiv">
					<button class='btn' type="button" onclick="search();"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
					<button class='btn' type="button" onclick="exportCharts();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					<button class='btn' type="button" onclick="clearAll();"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
					<span style="margin-left:4px;color:red;" id="message"></span>
				</div>
				<div id="opt-content">
				<jsp:include page="oqc-trend-center.jsp" />
				</div>
			</div>
		</form>
	</div>
</body>
</html>
