<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	function isEmail(str) {
		var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
		return reg.test(str);
	}
	//停用
	function stopSupplier(){
		var ids=jQuery("#suppliers").getGridParam('selarrrow');
		if(ids.length<=0){
			alert("请选择要停用的供应商！");
			return;
		}else if(ids.length>0){
			if(confirm("确定要停用所选的供应商吗？")){
				$.post("${supplierctx}/archives/update-stop.htm?deleteIds="+ids.join(","), {}, function(result) {
					if(result.error){
						alert(result.message);
					}else{
						jQuery("#suppliers").clearGridData();
						jQuery("#suppliers").trigger("reloadGrid");
					}
				});
			}
		}
		
	}
	//禁用
	function disableSupplier(){
		var ids=jQuery("#suppliers").getGridParam('selarrrow');
		if(ids.length<=0){
			alert("请选择要禁用的供应商！");
			return;
		}else if(ids.length>0){
			if(confirm("确定要禁用所选的供应商吗？")){
				$.post("${supplierctx}/archives/update-disable.htm?deleteIds="+ids.join(","), {}, function(result) {
					if(result.error){
						alert(result.message);
					}else{
						jQuery("#suppliers").clearGridData();
						jQuery("#suppliers").trigger("reloadGrid");
					}
				});
			}
		}
	}
	//恢复
	function restoreSupplier(){
		var ids=jQuery("#suppliers").getGridParam('selarrrow');
		if(ids.length<=0){
			alert("请选择要恢复的供应商！");
			return;
		}else if(ids.length>0){
			if(confirm("确定要恢复所选的供应商吗？")){
				$.post("${supplierctx}/archives/update-restore.htm?deleteIds="+ids.join(","), {}, function(result) {
					if(result.error){
						alert(result.message);
					}else{
						jQuery("#suppliers").clearGridData();
						jQuery("#suppliers").trigger("reloadGrid");
					}
				});
			}
		}
	}
	//淘汰供应商
	function washOutSupplier(){
		var ids=jQuery("#suppliers").getGridParam('selarrrow');
		if(ids.length<=0){
			alert("请选择要淘汰的供应商！");
			return;
		}else if(ids.length>0){
			$.post("${supplierctx}/archives/wash-out-supplier.htm?deleteIds="+ids.join(","), {}, function(result) {
				if(result.error){
					alert(result.message);
				}else{
					jQuery("#suppliers").trigger("reloadGrid");
				}
			});
		}
	}
	function editInfo(id){
		$.colorbox({
			href:'${supplierctx}/archives/input.htm?id='+id,
			iframe:true, 
			width:$(window).width()<1500?$(window).width()-100:1500, 
			height:$(window).height()<1380?$(window).height()-100:1380,
			overlayClose:false,
			title:"供应商信息",
			onClosed:function(){
				$("#suppliers").trigger("reloadGrid");
				makeEditable(true);
			}
		});
	}
	
	//创建帐号
	function createAccounts(){
		var ids=jQuery("#suppliers").getGridParam('selarrrow');
		
		if(ids.length<=0){
			alert("请选择要创建的供应商！");
			return;
		}else {
			$.post('${supplierctx}/archives/create-accounts.htm?userId='+ids,
				function(result){
// 					if(result.error){
// 						console.log(result);
// 						console.log(result.message);
// 						alert(result.message);		
// 					}else{
// 						alert(result.message);
// 	// 					$("#list").jqGrid("setGridParam").trigger("reloadGrid");
// 					};
// 					var orderId="";
// 					orderId = result.split(",")[0];
					alert(result);
// 					console.log(data);
// 					alert(data);
// 					window.location.href='${epmctx}/entrust-ort/input.htm?id='+obj+"&&str="+str;
			});
		}
	}
	
	function sendEvaluateSuppliers(){
		var ids=jQuery("#suppliers").getGridParam('selarrrow');
		var row = $("#suppliers").jqGrid('getRowData',ids[0]);
		var estimateModelId = row.estimateModelId;
		var materialType = row.materialType;
// 		if(estimateModelId.length==0){
// 			alert("请先设置评价模型");
// 			return;
// 		}
		if(ids.length<=0){
			alert("请选择要评价的供应商！");
			return;
		}else if(ids.length>1){
			alert("只能选一条！");
		}else{
			window.open("${supplierctx}/evaluate/quarter/add.htm?supplierId=" + ids[0]+"&estimateModelId="+estimateModelId+"&materialType="+materialType);	
		}
	}
	//生成审核计划
	function sendAuditSuppliers(){
		var ids=jQuery("#suppliers").getGridParam('selarrrow');
		if(ids.length<=0){
			alert("请选择要生成的供应商！");
			return;
		}else {
			$.post('${supplierctx}/audit/year-inspect-plan/sendAuditSuppliers.htm?userId='+ids,
				function(result){
					if(result){
						alert(result);
					}else if(result.message){
						alert(result.message);
					}
				});
		}
	}
	var rowId=null;
	function supplyMaterialClick(obj){
		rowId=obj.rowid;
		$.colorbox({href:"${iqcctx}/inspection-base/inspection-bom/bom-inspection-mulit-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择物料"
 		});
	}
	function setInspectionBomValue(datas){
		var materielName = "";
		for(var i=0;i<datas.length;i++){
			if(materielName.length==0){
				materielName = datas[i].materielName;
			}else{
				materielName += ";" + datas[i].materielName;
			}
		}
		$("#"+rowId+"_supplyMaterial").val(materielName);
 	}
	//隐藏供应商数据
	function hiddenSupplier(){
		var ids=jQuery("#suppliers").getGridParam('selarrrow');
		if(ids.length<=0){
			alert("请选择要隐藏的供应商！");
			return;
		}else if(ids.length>1){
			alert("只能选一条！");
		}else {
			$.post('${supplierctx}/archives/hidden-supplier.htm?userId='+ids,
				function(result){
					if(result){
						alert(result);
					}else{
						alert("修改成功!");
	// 					$("#list").jqGrid("setGridParam").trigger("reloadGrid");
					};
			});
		}
	}
</script>