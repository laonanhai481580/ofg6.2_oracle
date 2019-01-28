<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>

<security:authorize ifAnyGranted="supplier-audit-year-list1">
	<div id="auditYearList" class="west-notree" url="${supplierctx }/audit/year-view/list.htm"
		onclick="changeMenu(this);">
		<span>供应商年度稽核计划</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-audit-year-inspect-plan">
	<div id="auditYearInspectPlan" class="west-notree" url="${supplierctx }/audit/year-inspect-plan/list.htm"
		onclick="changeMenu(this);">
		<span>供应商稽核计划台帐</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-year-inspect-add">
	<div id="auditYearInspectInput" class="west-notree" url="${supplierctx }/audit/year-inspect/input.htm"
		onclick="changeMenu(this);">
		<span>供应商稽核表单</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-audit-year-inspect-list">
	<div id="auditYearInspectList" class="west-notree" url="${supplierctx }/audit/year-inspect/list.htm"
		onclick="changeMenu(this);">
		<span>供应商稽核台帐</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-audit-year-history-list1">
	<div id="audtiHistoryList" class="west-notree" url="${supplierctx }/audit/year/list.htm"
		onclick="changeMenu(this);">
		<span>供应商稽核历史台帐</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-year-inspect-plan-all">
	<div id="yearInspectPlanAll" class="west-notree" url="${supplierctx }/audit/year-inspect-plan/all/list.htm"
		onclick="changeMenu(this);">
		<span>供应商稽核计划汇总</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-year-inspect-all">
	<div id="yearInspectAll" class="west-notree" url="${supplierctx }/audit/year-inspect/all/list.htm"
		onclick="changeMenu(this);">
		<span>供应商稽核报告汇总</span>
	</div>
</security:authorize>
<script type="text/javascript" class="source">
		$(document).ready(function(){
			$('#'+thirdMenu).addClass('west-notree-selected');
		});
		function changeMenu(obj){
			window.location = $(obj).attr('url');
		}
	</script>