<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="integration-input">
	<div id="integration_third" class="west-notree" url="${integrationctx }/input.htm" onclick="changeMenu(this);"><span>手动同步ERP数据</span></div> 
</security:authorize>
<security:authorize ifAnyGranted="integration-update-time-list">
	<div id="update_time" class="west-notree" url="${integrationctx }/update-time-list.htm" onclick="changeMenu(this);"><span>手动更新时间</span></div> 
</security:authorize>
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>