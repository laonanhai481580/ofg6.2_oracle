<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	function sendEmail(){
		var params = $("#copyMan").val();
		var str = $("#currentActivityName").val();
		if("计划核准"==str){
			$.post("${qsmctx}/inner-audit/audit-plan/release.htm",{par:params},function(data){
// 				console.log(data);
			},'json');
 		} 
	}
</script>