<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
	<security:authorize ifAnyGranted="AFS_VLRR_DATA_LIST">
		<div id="vlrr_data" class="west-notree"  ><a href="${aftersalesctx}/vlrr/list.htm">VLRR明细表</a></div>
	</security:authorize>
	<security:authorize ifAnyGranted="AFS_VLRR_DATA_LIST_HIDE">
		<div id="vlrr_data_hide" class="west-notree"  ><a href="${aftersalesctx}/vlrr/hide-list.htm">VLRR敏感数据</a></div>
	</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
</script>