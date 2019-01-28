<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.supplier.entity.Supplier"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<%@ include file="/common/supplier-add.jsp" %>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript">
<%
String companyName=ContextUtils.getCompanyName();
ActionContext.getContext().put("companyName",companyName);
%>
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
 	function $beforeEditRow(rowId,iRow,iCol,e){
		var isRight = false;
		<security:authorize ifAnyGranted="supplier-file-save">
			isRight =  true;
		</security:authorize>
		var companyName =$(e.currentTarget).find("td[title=TP]").html();
		if(companyName&&companyName.indexOf('TP')>-1){
			isRight =  true;
		}else{
			isRight = false;
		}
		return isRight;
	}
	var params = {};
	function $oneditfunc(rowId){
		selRowId = rowId;
		params.attachmentFiles = $("#" + rowId + "_hiddenFiles").val();
		params.hisAttachmentFiles = params.attachmentFiles;
		$(".uploadBtn").hide();
		$("#undefined_uploadBtn").show();
		$("#" + rowId + "_uploadBtn").show();
		
	}
	//上传附件
	function formateMessageFile(value,o,obj){
		var strs = "";
		strs = "<div style='width:100%;' title='上传附件' ><a class=\"small-button-bg\" onclick=\"attachmentFilesClick('"+obj.id+"');\" href=\"#\"><input type='hidden' id='"+obj.id+"_hiddenFiles' value='"+value+"'/><span id='"+obj.id+"_uploadBtn' class='ui-icon ui-icon-image uploadBtn' style='cursor:pointer;display:none;'></span></a><span style='text-align:left;' id='"+obj.id+"_showFiles'>"+$.getDownloadHtml(value)+"</span><div>";
		return strs;
	}
	function attachmentFilesClick(rowId){
		//上传附件 
		$.upload({   
			showInputId : rowId + "_showFiles",
			hiddenInputId : rowId + "_hiddenFiles",
			title:"上传附件",
			callback:function(files){
				params.attachmentFiles = $("#" + rowId + "_hiddenFiles").val();
			}
		}); 
	}
	function $afterrestorefunc(rowId){
		$("#" + rowId + "_uploadBtn").hide();
	}
 	//重写(单行保存前处理行数据)
	function $processRowData(data){
		data.attachmentFiles =params.attachmentFiles;
		return data;
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="message";
		var thirdMenu="_fileList";
	</script>


	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-message-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="supplier-file-save">
				<button class='btn' onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="supplier-file-delete">
				<button class='btn' onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button" ><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<security:authorize ifAnyGranted="supplier-file-export">
					<button  class='btn' onclick="iMatrix.export_Data('${supplierctx}/archives/supplier-file/exports.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>		
				</security:authorize>
			</div>
			<div style="float:right;position:absolute; right:50px;top:6px;" >
			<security:authorize ifAnyGranted="supplier-add">					
				 <s:select list="systemCodes" 
							theme="simple"
							listKey="value" 
							listValue="name" 
							id="systemCode"
							onchange="systemCodeChange('archives/supplier-file/list.htm')"
							cssStyle="width:60px"
							emptyOption="false"
							labelSeparator="">
				</s:select> 
			</security:authorize>						
			</div>			
			<div id="opt-content" style="clear:both;">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="suppliers"
						url="${supplierctx}/archives/supplier-file/list-datas.htm" code="SUPPLIER_FILE"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
</html>