<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
		function supplierClick3(){
			$.colorbox({href:"${supplierctx}/archives/select-supplier.htm",iframe:true,
				width:$(window).width()<1000?$(window).width()-100:1000,
				height:$(window).height()<600?$(window).height()-100:600,
				overlayClose:false,
				title:"选择供应商"
			});
		}
		function setSupplierValue(objs){
			var obj = objs[0];
			$("#dutySupplier").val(obj.name);
			$("#dutySupplierCode").val(obj.code);
			$("#supplierEmail").val(obj.supplierEmail);
			//联系人
		} 

		function clearValue(showInputId,hiddenInputId){
			$("#"+showInputId).val("");
			$("#"+hiddenInputId).val("");
		}
		function copyValue(obj){
			$("#unwithholdValue").val($("#"+obj.id).val());
		}
		function changeFlow(obj){
			alert();
			if(obj.value.length!=0){
				alert();
			}
		}
</script>