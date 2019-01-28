<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
	<security:authorize ifAnyGranted="iqc-check-group-fpm">
		<div id="myInspectionReportInput_fpm" class="west-notree" url="${iqcctx }/inspection-report/fpm/input.htm?changeview=true" onclick="changeMenu(this);"><span>进货检验报告</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc-incominginspectionactionsreport-uncheck-fpm">
		<div id="unCheckInspectionReport_fpm" class="west-notree" url="${iqcctx }/inspection-report/fpm/un-check.htm" onclick="changeMenu(this);"><span>待检验台帐</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc-incominginspectionactionsreport-recheck-fpm">
		<div id="recheckInspectionReport_fpm" class="west-notree" url="${iqcctx }/inspection-report/fpm/recheck-list.htm" onclick="changeMenu(this);"><span>重检台帐</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc-wait-audit-fpm-list">
		<div id="waitAuditInspectionReport_fpm" class="west-notree" url="${iqcctx }/inspection-report/fpm/wait-audit.htm" onclick="changeMenu(this);"><span>待审核台帐</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc-inspection-report-fpm-oklist">
		<div id="myOkInspectionReport_fpm" class="west-notree" url="${iqcctx }/inspection-report/fpm/oklist.htm" onclick="changeMenu(this);"><span>审核完成合格台帐</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc-inspection-report-fpm-unlist">
		<div id="myUnInspectionReport_fpm" class="west-notree" url="${iqcctx }/inspection-report/fpm/unlist.htm" onclick="changeMenu(this);"><span>审核完成不合格台帐</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc-incominginspectionactionsreport-fpm-list">
		<div id="myInspectionReport_fpm" class="west-notree" url="${iqcctx }/inspection-report/fpm/list.htm" onclick="changeMenu(this);"><span>进货检验报告台帐</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc_incomingInspectionActionsReport_fpm_hide">
		<div id="inspection_y" class="west-notree" url="${iqcctx }/inspection-report/fpm/list-y.htm" onclick="changeMenu(this);"><span>隐藏数据台帐</span></div>
	</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>