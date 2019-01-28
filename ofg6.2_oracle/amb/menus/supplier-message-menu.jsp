<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="supplier-message-list">
<div id="_messageList" class="west-notree" url="${supplierctx }/archives/supplier-messages/list.htm"
	onclick="changeMenu(this);">
	<span>通告/下载栏</span>
</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-file-list">
<div id="_fileList" class="west-notree" url="${supplierctx }/archives/supplier-file/list.htm"
	onclick="changeMenu(this);">
	<span>供应商汇报资料</span>
</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-file-list-all">
<div id="_fileListAll" class="west-notree" url="${supplierctx }/archives/supplier-file/list-all.htm"
	onclick="changeMenu(this);">
	<span>供应商汇报资料汇总</span>
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