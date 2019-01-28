<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
	<security:authorize ifAnyGranted="QSM_CUSTOMER_AUDIT_LIST">
		<div id="_customer_audit" class="west-notree"  ><a href="${qsmctx}/customer-audit/list.htm">客户审核履历</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="QSM_CUSTOMER_AUDIT_ISSUES_LIST">
		<div id="_customer_audit_issues-" class="west-notree"  ><a href="${qsmctx}/customer-audit/issues/list.htm">客户审核改善报告台账</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="QSM_CUSTOMER_AUDIT_ISSUES_INPUT">
		<div id="_customer_audit_issues-input" class="west-notree"  ><a href="${qsmctx}/customer-audit/issues/input.htm">客户审核改善报告</a></div>
	</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
</script>