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
			<security:authorize ifAnyGranted="IQC_TEST_FREQUENCY_SAVE">
				isRight =  true;
			</security:authorize>
			return isRight;
		}		
		
		var selRowId="";
		
		function $oneditfunc(rowId){
			selRowId = rowId;
			$("#" + rowId + "_checkBomCode").attr("disabled","disabled");
			$("#" + rowId + "_checkBomName").attr("disabled","disabled");
			$("#" + rowId + "_supplierName").attr("disabled","disabled");
			$("#" + rowId + "_supplierCode").attr("disabled","disabled");
			$("#" + rowId + "_businessUnitName").attr("disabled","disabled");
		}		
		function checkBomCodeClick(obj){
			selRowId=obj.rowid;	
			materialClick();
		}
		function checkBomNameClick(obj){
			selRowId=obj.rowid;	
			materialClick();
		}
	 	function  materialClick(){
	 		var url = '${iqcctx}/statistical-analysis/bom-code-select.htm';
	 		$.colorbox({href:url,iframe:true, 
	 			innerWidth:700, 
				innerHeight:500,
	 			overlayClose:false,
	 			title:"选择物料"
	 		});
	 	}
	 	
		//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setIqcBomValue(datas){
	 		$("#"+selRowId+"_checkBomCode").val(datas[0].code);
	 		$("#"+selRowId+"_checkBomName").val(datas[0].name);
	 	}
	 	
		function supplierNameClick(obj){
			selRowId=obj.rowid;	
			supplierClick();
		}
		function supplierCodeClick(obj){
			selRowId=obj.rowid;	
			supplierClick();
		}
	 	function  supplierClick(){
	 		var url = '${supplierctx}/archives/select-supplier.htm';
	 		$.colorbox({href:url,iframe:true, 
	 			innerWidth:700, 
				innerHeight:500,
	 			overlayClose:false,
	 			title:"选择供应商"
	 		});
	 	}
	 	
		//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setSupplierValue(datas){
	 		$("#"+selRowId+"_supplierCode").val(datas[0].code);
	 		$("#"+selRowId+"_supplierName").val(datas[0].name);
	 	}	 	
	 	
	 	var params="";
		//同步历史数据
	 	function addOld(obj){
			var title="同步历史供应商和物料";
			var max=150;
	 		var url='${iqcctx}/inspection-base/test-frequency/save-old.htm';
			if(confirm("确定要执行操作吗?")){
				$("#message").html("正在执行数据同步,请稍候... ...");
				$(".opt-btn button").attr("disabled",true);
				var progressWin = progressbar(title,max);
				$.post(encodeURI(url),{},function(result){
					$("#message").html("");
					$("button").attr("disabled","");
					progressWin.dialog("close");
					alert(result.message);
					$("#message").html("");
					$("#list").jqGrid("setGridParam").trigger("reloadGrid");
				},'json');
			}
	 		
	 	}	 	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="base";
		var thirdMenu="_test_frequency";
		var treeMenu = '_test_frequency';
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-inspection-base-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
				<div class="opt-btn">
					<%-- <security:authorize ifAnyGranted="IQC_TEST_FREQUENCY_SAVE">
						<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="IQC_TEST_FREQUENCY_DELETE">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize> --%>
					<button id="searchBtn" class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="IQC_TEST_FREQUENCY_EXPORT">
						<button class="btn" onclick="iMatrix.export_Data('${iqcctx}/inspection-base/test-frequency/export.htm?type=normal');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="IQC_TEST_FREQUENCY_ADDOLD">
						<button class="btn" onclick="addOld();"><span><span><b class="btn-icons btn-icons-play"></b>同步</span></span></button>
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
						<grid:jqGrid gridId="list" url="${iqcctx}/inspection-base/test-frequency/list-datas.htm" submitForm="defaultForm" code="IQC_TEST_FREQUENCY" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>