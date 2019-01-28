<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="MFG_IPQC_AUDIT_LIST">
	<div id="_IPQC_AUDIT_LIST" class="west-notree" url="${mfgctx}/ipqc/ipqc-audit/list.htm"  onclick="changeMenu(this);"><span>IPQC稽核台账</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_IPQC_AUDIT_MISS_LIST">
	<div id="_IPQC_AUDIT_MISS" class="west-notree" url="${mfgctx}/ipqc/ipqc-audit/miss-list.htm"  onclick="changeMenu(this);"><span>IPQC稽核缺失件数维护</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_IPQC_AUDIT_HIDELIST">
	<div id="_IPQC_AUDIT_HIDE" class="west-notree" url="${mfgctx}/ipqc/ipqc-audit/hide-list.htm"  onclick="changeMenu(this);"><span>IPQC稽核敏感数据台账</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_IPQC_IMPROVE_INPUT">
	<div id="_IPQC_IMPROVE_INPUT" class="west-notree" url="${mfgctx}/ipqc/ipqc-improve/input.htm"  onclick="changeMenu(this);"><span>IPQC稽核问题点改善报告</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_IPQC_IMPROVE_LIST">
	<div id="_IPQC_IMPROVE_LIST" class="west-notree" url="${mfgctx}/ipqc/ipqc-improve/list.htm"  onclick="changeMenu(this);"><span>IPQC稽核问题点改善报告台账</span></div>
</security:authorize>
<security:authorize ifAnyGranted="MFG_IPQC_IMPROVE_HIDELIST">
	<div id="_IPQC_IMPROVE_HIDE" class="west-notree" url="${mfgctx}/ipqc/ipqc-improve/hide-list.htm"  onclick="changeMenu(this);"><span>IPQC改善报告敏感数据台账</span></div>
</security:authorize>
 <security:authorize ifAnyGranted="MFG_IPQC_LOSS_LIST">	
	<div id="_ipqc_loss" class="west-notree" url="${mfgctx}/ipqc/ipqc-audit/ipqc-loss.htm" onclick="changeMenu(this);"><span>IPQC稽核类别柏拉图</span></div>
 </security:authorize>	
 <security:authorize ifAnyGranted="MFG_IPQC_CLOSE_LIST">	
	<div id="_ipqc_close" class="west-notree" url="${mfgctx}/ipqc/ipqc-audit/ipqc-close.htm" onclick="changeMenu(this);"><span>IPQC结案率推移图</span></div>
 </security:authorize>	
 <security:authorize ifAnyGranted="MFG_IPQC_OBSERVE_LIST">	
	<div id="_ipqc_observe" class="west-notree" url="${mfgctx}/ipqc/ipqc-audit/observe-rate.htm" onclick="changeMenu(this);"><span>IPQC遵守度统计图</span></div>
 </security:authorize>	
<script type="text/javascript" class="source">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>