<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="MFG_OQC_EXCEPTION_SAVE">
				isRight =  true;
			</security:authorize>
			return isRight;
		}		
		//导入台账数据
		function importDatas(){
			var url = encodeURI('${mfgctx}/oqc/exception/import.htm');
			$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
				overlayClose:false,
				title:"导入台账数据",
				onClosed:function(){
					$("#list").trigger("reloadGrid");
				}
			});
		}
		var selRowId = null;
		function $oneditfunc(rowId){
			selRowId = rowId;
			$("#" + rowId + "_inputCount").keyup(caculateBadRate);
			$("#" + rowId + "_unQualityCount").keyup(caculateBadRate);
			$("#" + rowId + "_unQualityRate").attr("disabled","disabled");
		}
		function caculateBadRate(){
			var inputCount = $("#" + selRowId + "_inputCount").val();
			var unQualityCount = $("#" + selRowId + "_unQualityCount").val();
			if(isNaN(inputCount)){
				alert("投入数必须为整数！");
				$("#" + selRowId + "_inputCount").focus();
				$("#" + selRowId + "_unQualityRate").val("");
				return;
			}
			if(isNaN(unQualityCount)){
				alert("不良数必须为整数！");
				$("#" + selRowId + "_unQualityCount").focus();
				$("#" + selRowId + "_unQualityRate").val("");
				return;
			}

			if((inputCount-0)<(unQualityCount-0)){
				alert("不良数不能大于投入数！");
				$("#" + selRowId + "_unQualityRate").val("");
				return;
			}
			if(unQualityCount&&inputCount){
				var rate=unQualityCount*100/inputCount;
				$("#" + selRowId + "_unQualityRate").val(rate.toFixed(2)+"%");
			}		
		}		
	 	function modelClick(){
	 		selRowId=obj.rowid;
			var customerName=$("#"+selRowId+"_customer").val();
	 		var url = '${aftersalesctx}/base-info/customer/model-select.htm?customerName='+customerName;
	 		$.colorbox({href:url,iframe:true, 
	 			innerWidth:700, 
				innerHeight:500,
	 			overlayClose:false,
	 			title:"选择客户机型"
	 		});
	 	}
		function downloadTemplate(){
			window.location = '${mfgctx}/oqc/exception/download.htm';
		}		
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="oqc_list";
		var thirdMenu="_OQC_EXCEPTION_LIST";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-oqc-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="MFG_OQC_EXCEPTION_SAVE">
						<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="MFG_OQC_EXCEPTION_DELETE">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button id="searchBtn" class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="MFG_OQC_EXCEPTION_EXPORT">
						<button class="btn" onclick="iMatrix.export_Data('${mfgctx}/oqc/exception/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="MFG_OQC_EXCEPTION_IMPORT">
						<button class='btn' onclick="importDatas();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>
						<button class="btn" onclick="downloadTemplate();"><span><span><b class="btn-icons btn-icons-download"></b>下载导入模板</span></span></button>
					</security:authorize>					
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<div style="float:right;position:absolute; right:50px;top:6px;" >
					<span style="color:red;" id="message"></span>
				</div>					
				<div id="message"><s:actionmessage theme="mytheme" /></div>	
				<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
				<div id="opt-content" >
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="list" url="${mfgctx}/oqc/exception/list-datas.htm" submitForm="defaultForm" code="MFG_OQC_EXCEPTION" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>	
</body>
</html>