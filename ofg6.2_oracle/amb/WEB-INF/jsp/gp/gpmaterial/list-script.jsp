<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function findSupplierMaterial(){
	var id = $("#dynamicComplain").jqGrid("getGridParam","selarrrow");
	if(id.length==0){
			alert("请选择一项！");
			return ;
		}
	var url="${gpctx}/gpmaterial/find-supplier-material.htm?ids="+id;
	$.post(url,function(result){
		if(result.error){
			$("#message").html("操作失败"+result.message);
		}else{
			$("#message").html(result.message);
		};
		$("#dynamicComplain").jqGrid("setGridParam").trigger("reloadGrid");
	},'json');
}
</script>