<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
	<security:authorize ifAnyGranted="iqc_batch_pass_rate">
		<div id="_batch_pass_rate" class="west-notree" url="${iqcctx}/statistical-analysis/iqc-batch-pass-rate.htm" onclick="changeMenu(this);"><span>IQC批次合格率推移图</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc_bad_rate">
		<div id="_bad_rate" class="west-notree" url="${iqcctx}/statistical-analysis/iqc-bad-rate.htm" onclick="changeMenu(this);"><span>IQC检验不良率推移图</span></div>
	</security:authorize>	
	<security:authorize ifAnyGranted="iqc_sampling_bad">
		<div id="_sampling_bad" class="west-notree" url="${iqcctx}/statistical-analysis/iqc-sampling-bad.htm" onclick="changeMenu(this);"><span>不良项目柏拉图</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc_wg_bad_rate">
		<div id="_wg_bad_rate" class="west-notree" url="${iqcctx}/statistical-analysis/iqc-wg-bad-rate.htm" onclick="changeMenu(this);"><span>外观抽检不良率推移图</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="iqc_size_spread_rate">
		<div id="_size_spread" class="west-notree" url="${iqcctx}/statistical-analysis/size-spread-rate.htm" onclick="changeMenu(this);"><span>尺寸分布趋势图</span></div>
	</security:authorize>
<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>