<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
	<security:authorize ifAnyGranted="AFS_CUSTOMER_RETURN_LIST">
		<div id="customer_return" class="west-notree"  ><a href="${aftersalesctx}/customer-return/list.htm">客退率台账</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="AFS_CUSTOMER_ISSUE_LIST">
		<div id="customer_issue" class="west-notree"  ><a href="${aftersalesctx}/customer-issue/list.htm">客户issue台账</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="AFS_CUSTOMER_EVALUATION_LIST">
		<div id="customer_evaluation" class="west-notree"  ><a href="${aftersalesctx}/customer-evaluation/list.htm">重要客户评价台账</a></div>
	</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
</script>