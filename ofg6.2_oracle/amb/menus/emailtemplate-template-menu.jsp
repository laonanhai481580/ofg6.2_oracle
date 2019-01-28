<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="emailtemplate_template_list">
<div id="template_list" class="west-notree" url="${emailtemplatectx}/template/list.htm"
	onclick="changeMenu(this);">
	<span>模板清单</span>
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