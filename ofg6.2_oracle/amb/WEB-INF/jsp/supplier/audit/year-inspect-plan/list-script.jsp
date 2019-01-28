<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	//生成审核计划
	function editAuditSuppliers(){
		var ids=jQuery("#suppliers").getGridParam('selarrrow');
		if(ids.length<=0){
			alert("请选择要发起的供应商！");
			return;
		}else if(ids.length>1){
			alert("只能选一条！");
		}else{
			$.post('${supplierctx}/audit/year-inspect/seeYearInspect.htm?id='+ids,
		 		function(result){
		 			if(result=="Y"){
						alert("已经生成计划");
					}else{
						window.open('${supplierctx}/audit/year-inspect/input.htm?userId='+ids);
					};
			 });
		}
	}
</script>