<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div id="tabs-2">
	<div class="opt-btn" style="line-height: 40px;">
		<button class='btn' type="button" onclick="saveProducts();">
			<span><span><b class="btn-icons btn-icons-save"></b>保存</span></span>
		</button>
		<span id="product-message"
			style="color: red; padding-left: 4px; font-weight: bold;"></span>
	</div>
	<table id="product_table">
	</table>
	<script type="text/javascript">
	var rowId = null;
	function supplyMaterialClick(obj){
		rowId=obj;
		$.colorbox({href:"${mfgctx}/common/product-bom-select.htm",iframe:true,
			width:$(window).width()<1000?$(window).width()-100:1000,
			height:$(window).height()<600?$(window).height()-100:600,
			overlayClose:false,
			title:"选择物料"
		});
	}
	function setFullBomValue(datas){
		var materielName = "";
		var materielCode = ""; 
		var materielType = "";
		for(var i=0;i<datas.length;i++){
			if(materielName.length==0){
				materielName = datas[i].materielName;
				materielCode = datas[i].materielCode;
				materielType = datas[i].materialType;
			}else{
				materielName += ";" + datas[i].materielName;
				materielCode += ";" + datas[i].materielCode;
				materielType += ";" + datas[i].materialType;
			}
		}
		$("#"+rowId+"_code").val(materielCode);
		$("#"+rowId+"_name").val(materielName);
		$("#"+rowId+"_materialType").val(materielType);
 	}
	</script>
</div>
<!-- <div id="tabs-3">
	<div class="opt-btn" style="line-height: 40px;">
		<button class='btn' onclick="saveCertificates();">
			<span><span><b class="btn-icons btn-icons-save"></b>保存</span></span>
		</button>
		<span id="certificate-message" style="color: red; padding-left: 4px; font-weight: bold;"></span>
	</div>
	<table id="certificate_table"></table>
</div> -->