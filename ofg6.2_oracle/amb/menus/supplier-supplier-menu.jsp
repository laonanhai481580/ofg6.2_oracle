<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>

<security:authorize ifAnyGranted="archives-list">
	<div id="_archives_list" class="west-notree" url="${supplierctx }/archives/list.htm"
		onclick="changeMenu(this);">
		<span>零部件供应商</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="archives-list-all">
	<div id="_archives_list_all" class="west-notree" url="${supplierctx}/archives/list-all.htm"
		onclick="changeMenu(this);">
		<span>供应商台账</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="archives-qualified">
<div id="_qualified" class="west-notree" url="${supplierctx }/archives/qualified.htm"
	onclick="changeMenu(this);">
	<span>合格供应商名录</span>
</div>
</security:authorize>
<security:authorize ifAnyGranted="archives-assign">
<div id="_assign" class="west-notree" url="${supplierctx }/archives/assign.htm"
	onclick="changeMenu(this);">
	<span>条件合格供应商名录</span>
</div>
</security:authorize>
<security:authorize ifAnyGranted="archives-beforehand">
<div id="_beforehand" class="west-notree" url="${supplierctx }/archives/beforehand.htm"
	onclick="changeMenu(this);">
	<span>潜在供应商名录</span>
</div>
</security:authorize>
<security:authorize ifAnyGranted="archives-qualified-cancle-list">
<div id="_cancleList" class="west-notree" url="${supplierctx }/archives/supplier-cancle/list.htm"
	onclick="changeMenu(this);">
	<span>合格供应商取消申请台账</span>
</div>
</security:authorize>
<%-- <security:authorize ifAnyGranted="archives-qualified-cancle-input1">
<div id="_cancleInput" class="west-notree" url="${supplierctx }/archives/supplier-cancle/input.htm"
	onclick="changeMenu(this);">
	<span>合格供应商取消申请单</span>
</div>
</security:authorize> --%>
<security:authorize ifAnyGranted="archives-eliminated">
<div id="_eliminated" class="west-notree" url="${supplierctx }/archives/eliminated.htm"
	onclick="changeMenu(this);">
	<span>已淘汰供应商名录</span>
</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-all-list">
<div id="allList" class="west-notree" url="${supplierctx }/archives/all/list.htm"
	onclick="changeMenu(this);">
	<span>供应商名录事业群汇总</span>
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