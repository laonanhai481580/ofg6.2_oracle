<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="MFG_OQC_INSPECTION_LIST">
	<div id="_OQC_INSPECTION_LIST" class="west-notree" url="${mfgctx}/oqc/list.htm"  onclick="changeMenu(this);"><span>OQC检验台账</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_OQC_INSPECTION_HIDE_LIST">
	<div id="_OQC_INSPECTION_HIDE" class="west-notree" url="${mfgctx}/oqc/hide-list.htm"  onclick="changeMenu(this);"><span>OQC敏感数据台账</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_OQC_DELIVER_LIST">
	<div id="_OQC_DELIVER_LIST" class="west-notree" url="${mfgctx}/oqc/deliver-list.htm"  onclick="changeMenu(this);"><span>OQC出货报告</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_OQC_SIZE_LIST">
	<div id="_OQC_SIZE_LIST" class="west-notree" url="${mfgctx}/oqc/size-list.htm"  onclick="changeMenu(this);"><span>OQC尺寸检验数据</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_OQC_EXCEPTION_LIST">
	<div id="_OQC_EXCEPTION_LIST" class="west-notree" url="${mfgctx}/oqc/exception/list.htm"  onclick="changeMenu(this);"><span>OQC异常管理</span></div>
</security:authorize>
<script type="text/javascript" class="source">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>