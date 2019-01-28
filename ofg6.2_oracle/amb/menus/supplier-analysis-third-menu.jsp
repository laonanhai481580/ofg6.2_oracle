<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
<security:authorize ifAnyGranted="SUPPLIER_INCOMING_CLOSE_LIST">
	<div id="incoming_close_rate" class="west-notree" ><a href="${supplierctx}/data-acquisition/incoming-close-rate.htm">进料异常结案率推移图</a></div>
</security:authorize>
<security:authorize ifAnyGranted="SUPPLIER_INCOMING_CLOSE_SQE">
	<div id="incoming_close_sqe" class="west-notree" ><a href="${supplierctx}/data-acquisition/incoming-close-rate-sqe.htm">进料异常结案率推移图(SQE)</a></div>
</security:authorize>
<security:authorize ifAnyGranted="SUPPLIER_INCOMING_CLOSE_MQE">
	<div id="incoming_close_mqe" class="west-notree" ><a href="${supplierctx}/data-acquisition/incoming-close-rate-mqe.htm">进料异常结案率推移图(MQE)</a></div>
</security:authorize>
<security:authorize ifAnyGranted="SUPPLIER_ABNORMAL_CHAR_LIST">
	<div id="abnormal_char_rate" class="west-notree" ><a href="${supplierctx}/data-acquisition/abnormal-char-rate.htm">上线异常推移图</a></div>
</security:authorize>
<security:authorize ifAnyGranted="SUPPLIER_ABNORMAL_CHAR_TOTAL">
	<div id="abnormal_char_total" class="west-notree" ><a href="${supplierctx}/data-acquisition/abnormal-char-rate-total.htm">上线异常推移图(汇总)</a></div>
</security:authorize>
<security:authorize ifAnyGranted="SUPPLIER_SENT-OUT-RECORD_LIST">
	<div id="sent-out-record" class="west-notree" ><a href="${supplierctx}/data-acquisition/sent-out-record.htm">发料记录推移图</a></div>
</security:authorize>
<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');	
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>