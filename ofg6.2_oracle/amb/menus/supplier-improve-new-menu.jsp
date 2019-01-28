<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="supplier-exception-contact-list">
	<div id="contactList" class="west-notree" url="${supplierctx }/improve-new/exception-contact/list.htm"
		onclick="changeMenu(this);">
		<span>物料异常联络单台账</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-exception-improve-list">
	<div id="improveList" class="west-notree" url="${supplierctx }/improve-new/exception-improve/list.htm"
		onclick="changeMenu(this);">
		<span>物料异常矫正单台账</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-exception-contact-list-all">
	<div id="contactListAll" class="west-notree" url="${supplierctx }/improve-new/exception-contact/list-all.htm"
		onclick="changeMenu(this);">
		<span>物料异常联络单汇总台账</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-exception-improve-list-all">
	<div id="improveListAll" class="west-notree" url="${supplierctx }/improve-new/exception-improve/list-all.htm"
		onclick="changeMenu(this);">
		<span>物料异常矫正单汇总台账</span>
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