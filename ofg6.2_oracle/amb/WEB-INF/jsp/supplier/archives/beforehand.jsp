<%@page import="com.ambition.supplier.entity.Supplier"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<%@ include file="/common/supplier-add.jsp" %>
<%@ include file="list-script.jsp" %>
<script type="text/javascript">
	//重写保存后的方法
	function $successfunc(response){
		var result = eval("(" + response.responseText + ")");
		if(result.error){
			alert(result.message);
			return false;
		}else{
			return true;
		}
	}
	
	//修改供应商信息
	function editInfo(id){
		$.colorbox({
			href:'${supplierctx}/archives/input.htm?state=<%=Supplier.STATE_BEFOREHAND%>&id='+id,
			iframe:true, 
			width:$(window).width()<900?$(window).width()-100:900, 
			height:$(window).height()<680?$(window).height()-100:680,
			overlayClose:false,
			title:"供应商信息",
			onClosed:function(){
				$("#suppliers").trigger("reloadGrid");
				makeEditable(true);
			}
		});
	}
	
	function editData(){
		var ids = $("#suppliers").jqGrid("getGridParam","selarrrow");
		if(ids.length>1){
			alert("只能选择一条进行编辑!");
			return;
		}
		var url = '${supplierctx}/audit/year/input.htm';
		var title = "编辑";
		var row = $("#suppliers").jqGrid('getRowData',ids[0]);
		if(row.id=="undefined"||row.id.length==0||row.id=="&nbsp;"){
			alert("该供应商不是合格供应商");
			return;
		}
		url += "?id="+ row.Id;
		$.colorbox({href:url,iframe:true, 
			innerWidth:$(window).width()<1100?$(window).width()-100:1100, 
			innerHeight:$(window).height()-80,
			overlayClose:false,
			title:title,
			onClosed:function(){
				$("#suppliers").trigger("reloadGrid");
			}
		});
	};
	//导出
	function exportSuppliers(){
		var state = '<%=Supplier.STATE_BEFOREHAND%>';
		iMatrix.export_Data("${supplierctx}/archives/exports.htm?state="+state);
	}
	//格式化
	function operateFormater(cellValue,options,rowObj){
		if(rowObj.id){
			var operations = "<div style='text-align:center;' title='修改详细信息'><a class=\"small-button-bg\" onclick=\"editInfo("+cellValue+");\" href=\"#\"><span class=\"ui-icon ui-icon-info\" style='cursor:pointer;'></span></a></div>";
			return operations;
		}else{
			return '';
		}
	}
	
	
	//导入
	function imports(){
		var url = '${supplierctx}/archives/import-form.htm';
		$.colorbox({href:url,iframe:true, 
			innerWidth:350, innerHeight:240,
			overlayClose:false,
			title:"导入供应商",
			onClosed:function(){
				$("#suppliers").trigger("reloadGrid");
				makeEditable(true);
			}
		});
	}
	//导出模板
	function downloadExcel(){
		$("#iframe").bind("readystatechange",function(){
			$("#iframe").unbind("readystatechange");
		}).attr("src","${supplierctx}/archives/download-excel.htm");
	}
	function sendCalcleSuppliers(){
		var ids=jQuery("#suppliers").getGridParam('selarrrow');
		if(ids.length<=0){
			alert("请选择要取消的供应商！");
			return;
		}else if(ids.length>1){
			alert("只能选一条！");
		}else{
			window.open("${supplierctx}/archives/supplier-cancle/input.htm?supplierId=" + ids[0]);	
		}
	}
	
	//上传附件
	function showPicture(value,options,obj){
		var strs = "";
		var colName = options.colModel.name;
		strs = "<div style='width:100%;' title='上传附件' ><a class=\"small-button-bg\" onclick=\"attachmentFilesClick('"+obj.id+"','"+colName+"');\" href=\"#\"><input type='hidden' id='"+obj.id+"_hiddenFiles"+colName+"' value='"+value+"'/><span id='"+obj.id+"_uploadBtn"+colName+"' class='ui-icon ui-icon-image uploadBtn' style='cursor:pointer;display:none;'></span></a><span style='text-align:left;' id='"+obj.id+"_showFiles"+colName+"'>"+$.getDownloadHtml(value)+"</span><div>";
		return strs;
	}
	function attachmentFilesClick(rowId,name){
		//上传附件 
		$.upload({   
			showInputId : rowId + "_showFiles"+name,
			hiddenInputId : rowId + "_hiddenFiles"+name,
			title:"上传附件",
			callback:function(files){
				params["attachmentFiles"+name] = $("#" + rowId + "_hiddenFiles"+name).val();
			}
		}); 
	}
	
	var params = {};
	function $oneditfunc(rowId){
		params = {};
		var obj = $("#" + rowId + "_qualityModelId");
		if(obj.length>0){
			params.qualityModelName = obj.bind("change",function(){
				params.qualityModelName = $(this).find("option:selected").html();
			}).find("option:selected").html();
		}
		obj = $("#" + rowId + "_estimateModelId");
		if(obj.length>0){
			params.estimateModelName = obj.bind("change",function(){
				params.estimateModelName = $(this).find("option:selected").html();
			}).find("option:selected").html();
		}
		params.attachmentFilesbaseInfoFile = $("#" + rowId + "_hiddenFilesbaseInfoFile").val();
		params.attachmentFilesthreePapersFile = $("#" + rowId + "_hiddenFilesthreePapersFile").val();
		params.attachmentFilesintegrityAgreementFile = $("#" + rowId + "_hiddenFilesintegrityAgreementFile").val();
		params.attachmentFilessampleEvaluateFile = $("#" + rowId + "_hiddenFilessampleEvaluateFile").val();
		params.attachmentFilesfactoryAuditFile = $("#" + rowId + "_hiddenFilesfactoryAuditFile").val();
		params.attachmentFilespurchasingFile = $("#" + rowId + "_hiddenFilespurchasingFile").val();
		params.attachmentFilessupplierAnalyzeFile = $("#" + rowId + "_hiddenFilessupplierAnalyzeFile").val();
		params.hisAttachmentFilebaseInfoFile = params.attachmentFilesbaseInfoFile;
		params.hisAttachmentFilesthreePapersFile = params.attachmentFilesthreePapersFile;
		params.hisAttachmentFilesintegrityAgreementFile = params.attachmentFilesintegrityAgreementFile;
		params.hisAttachmentFilessampleEvaluateFile = params.attachmentFilessampleEvaluateFile;
		params.hisAttachmentFilesfactoryAuditFile = params.attachmentFilesfactoryAuditFile;
		params.hisAttachmentFilespurchasingFile = params.attachmentFilespurchasingFile;
		params.hisAttachmentFilessupplierAnalyzeFile = params.attachmentFilessupplierAnalyzeFile;
		$(".uploadBtnbaseInfoFile").hide();
		$("#undefined_uploadBtnbaseInfoFile").show();
		$("#" + rowId + "_uploadBtnbaseInfoFile").show();
		$(".uploadBtnthreePapersFile").hide();
		$("#undefined_uploadBtnthreePapersFile").show();
		$("#" + rowId + "_uploadBtnthreePapersFile").show();
		$(".uploadBtnintegrityAgreementFile").hide();
		$("#undefined_uploadBtnintegrityAgreementFile").show();
		$("#" + rowId + "_uploadBtnintegrityAgreementFile").show();
		$(".uploadBtnsampleEvaluateFile").hide();
		$("#undefined_uploadBtnsampleEvaluateFile").show();
		$("#" + rowId + "_uploadBtnsampleEvaluateFile").show();
		$(".uploadBtnfactoryAuditFile").hide();
		$("#undefined_uploadBtnfactoryAuditFile").show();
		$("#" + rowId + "_uploadBtnfactoryAuditFile").show();
		$(".uploadBtnpurchasingFile").hide();
		$("#undefined_uploadBtnpurchasingFile").show();
		$("#" + rowId + "_uploadBtnpurchasingFile").show();
		$(".uploadBtnsupplierAnalyzeFile").hide();
		$("#undefined_uploadBtnsupplierAnalyzeFile").show();
		$("#" + rowId + "_uploadBtnsupplierAnalyzeFile").show();
	}	
	
	function $afterrestorefunc(rowId){
		$("#" + rowId + "_uploadBtnsupplierAnalyzeFile").hide();
		$("#" + rowId + "_uploadBtnbaseInfoFile").hide();
		$("#" + rowId + "_uploadBtnthreePapersFile").hide();
		$("#" + rowId + "_uploadBtnintegrityAgreementFile").hide();
		$("#" + rowId + "_uploadBtnsampleEvaluateFile").hide();
		$("#" + rowId + "_uploadBtnfactoryAuditFile").hide();
		$("#" + rowId + "_uploadBtnpurchasingFile").hide();
	}	
	function $processRowData(data){
		for(var pro in params){
			data[pro] = params[pro];
		}
		data.state = '<%=Supplier.STATE_BEFOREHAND%>';
		return data;
	}			
	
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="archives";
		var thirdMenu="_beforehand";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-supplier-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="archives-qualified-add">
				<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>				
				<security:authorize ifAnyGranted="archives-qualified-delete">
				<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button> 
				</security:authorize>				
				<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
				<security:authorize ifAnyGranted="archives-qualified-exports">
				<button class="btn" onclick="exportSuppliers();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>				
			    <security:authorize ifAnyGranted="archives-qualified-imports">
					<button class='btn' onclick="imports();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导入</span></span></button>
					<button class='btn' onclick="downloadExcel();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>下载模板</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="archives-send-audit-suppliers">
				    <button class="btn" onclick="sendAuditSuppliers();"><span><span><b class="btn-icons btn-icons-audit"></b>生成稽核计划</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="archives-qualified-create-accounts">
				    <button class="btn" onclick="createAccounts();"><span><span><b class="btn-icons btn-icons-audit"></b>创建供应商帐号</span></span></button>
				</security:authorize>
				<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
			</div>
			<div style="float:right;position:absolute; right:50px;top:6px;" >
			<security:authorize ifAnyGranted="supplier-add">					
				 <s:select list="systemCodes" 
							theme="simple"
							listKey="value" 
							listValue="name" 
							id="systemCode"
							onchange="systemCodeChange('archives/beforehand.htm')"
							cssStyle="width:60px"
							emptyOption="false"
							labelSeparator="">
				</s:select> 
			</security:authorize>						
			</div>				
			<div><iframe id="iframe" style="display:none;"></iframe></div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="suppliers"
						url="${supplierctx}/archives/beforehand-list-datas.htm" code="SUPPLIER_SUPPLIER_QUALIFIED"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>

</body>
</html>