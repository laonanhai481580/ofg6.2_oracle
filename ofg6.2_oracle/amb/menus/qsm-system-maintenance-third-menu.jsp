<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
	<security:authorize ifAnyGranted="QSM_SYSTEM_MAINTENANCE_LIST">
		<div id="_system_maintenance" class="west-notree"  ><a href="${qsmctx}/system-maintenance/list.htm">体系维护台账</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="QSM_SYSTEM_EDUCATION_LIST">
		<div id="_system_maintenance_education" class="west-notree"  ><a href="${qsmctx}/system-maintenance/list-education.htm">培训教育</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="QSM_SYSTEM_LEGAL_LIST">
		<div id="_system_maintenance_legal" class="west-notree"  ><a href="${qsmctx}/system-maintenance/list-else.htm">法律法规</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="QSM_SYSTEM_RISK_LIST">
		<div id="_system_maintenance_risk" class="west-notree"  ><a href="${qsmctx}/system-maintenance/list-legal.htm">风险与机遇识别</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="QSM_SYSTEM_ELSE_LIST">
		<div id="_system_maintenance_else" class="west-notree"  ><a href="${qsmctx}/system-maintenance/list-risk.htm">其他</a></div>
	</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
</script>