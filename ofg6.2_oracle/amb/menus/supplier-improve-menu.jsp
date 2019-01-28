<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>

<%-- <security:authorize ifAnyGranted="supplier-improve-list">
	<div id="improveUnList" class="west-notree" url="${supplierctx }/improve/un-list.htm"
		onclick="changeMenu(this);">
		<span>未完成台账</span>
	</div>
</security:authorize> --%>
<security:authorize ifAnyGranted="supplier-improve-list">
	<div id="improveList" class="west-notree" url="${supplierctx }/improve/list.htm"
		onclick="changeMenu(this);">
		<span>进料异常纠正措施单台账</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-improve-input">
<security:authorize ifAnyGranted="supplier-improve-conceal">
	<div id="improveInput" class="west-notree" url="${supplierctx }/improve/input.htm"
		onclick="changeMenu(this);">
		<span>进料异常纠正措施单</span>
	</div>
</security:authorize>
</security:authorize>
<security:authorize ifAnyGranted="supplier-improve-hide-list">
	<div id="hideList" class="west-notree" url="${supplierctx }/improve/hide-list.htm"
		onclick="changeMenu(this);">
		<span>敏感数据台账</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-exception-contact-list">
	<div id="contactList" class="west-notree" url="${supplierctx }/improve-new/exception-contact/list.htm"
		onclick="changeMenu(this);">
		<span>物料异常联络单台账</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-exception-improve-list">
	<div id="improveList1" class="west-notree" url="${supplierctx }/improve-new/exception-improve/list.htm"
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