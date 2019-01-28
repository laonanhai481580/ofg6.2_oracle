<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="supplier-material-evaluate-list">
	<div id="evaluatelist" class="west-notree" url="${supplierctx }/material-admit/evaluate/list.htm"
		onclick="changeMenu(this);">
		<span>样件评估台帐</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-material-evaluate-input">
	<div id="evaluateInput" class="west-notree" url="${supplierctx }/material-admit/evaluate/input.htm"
		onclick="changeMenu(this);">
		<span>样件评估表</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-approval-list">
	<div id="approvalList" class="west-notree" url="${supplierctx }/material-admit/approval/list.htm"
		onclick="changeMenu(this);">
		<span>材料承认台帐</span>
	</div>
	<div id="approvalOkList" class="west-notree" url="${supplierctx }/material-admit/approval/list-ok.htm"
		onclick="changeMenu(this);">
		<span>材料承认已完成台帐</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-approval-add">
	<div id="approvalInput" class="west-notree" url="${supplierctx }/material-admit/approval/input.htm"
		onclick="changeMenu(this);">
		<span>材料承认申请表</span>
	</div>
</security:authorize>
<%-- <security:authorize ifAnyGranted="supplier-materials-file">
<div id="_supplier_materials" class="west-notree"
	url="${supplierctx}/material-admit/supplier-materials-file/list.htm" onclick="changeMenu(this);">
	<span>供应商附件</span>
</div>
</security:authorize> --%>
<security:authorize ifAnyGranted="supplier-admit-remake-list">
	<div id="admitList" class="west-notree" url="${supplierctx }/material-admit/admit-remake/list.htm"
		onclick="changeMenu(this);">
		<span>材料承认台帐</span>
	</div>
	<div id="admitOkList" class="west-notree" url="${supplierctx }/material-admit/admit-remake/list-ok.htm"
		onclick="changeMenu(this);">
		<span>材料承认已完成台帐</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-admit-lost-list">
	<div id="admitLostList" class="west-notree" url="${supplierctx }/material-admit/admit-remake/list-lost.htm"
		onclick="changeMenu(this);">
		<span>材料承认失效台帐</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-admit-remake-add">
	<div id="admitInput" class="west-notree" url="${supplierctx }/material-admit/admit-remake/input.htm"
		onclick="changeMenu(this);">
		<span>材料承认表单</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-admit-remake-list-state">
	<div id="admitListState" class="west-notree" url="${supplierctx }/material-admit/admit-remake/list-state.htm"
		onclick="changeMenu(this);">
		<span>材料承认清单</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-admit-all-list">
	<div id="admitAllList" class="west-notree" url="${supplierctx }/material-admit/admit-remake/all/list.htm"
		onclick="changeMenu(this);">
		<span>材料承认流程台帐汇总</span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="supplier-admit-all-state">
	<div id="admitAllState" class="west-notree" url="${supplierctx }/material-admit/admit-remake/all/list-state.htm"
		onclick="changeMenu(this);">
		<span>材料承认清单汇总</span>
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