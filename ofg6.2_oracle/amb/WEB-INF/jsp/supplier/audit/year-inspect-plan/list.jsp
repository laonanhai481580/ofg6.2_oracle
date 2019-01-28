<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<%@ include file="/common/supplier-add.jsp" %>
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
			<security:authorize ifAnyGranted="supplier-year-inspect-edit">
				isRight =  true;
			</security:authorize>
			return isRight;
		}	
		function customSave(gridId) {
			if (lastsel == undefined || lastsel == null) {
				alert("当前没有可编辑的行!");
				return;
			}
			var $grid = $("#" + gridId);
			var o = getGridSaveParams(gridId);
			if ($.isFunction(gridBeforeSaveFunc)) {
				gridBeforeSaveFunc.call($grid);
			}
			$grid.jqGrid("saveRow", lastsel, o);
		}
		//生成审核计划
		function editAuditSuppliers(){
			var ids=jQuery("#list").getGridParam('selarrrow');
			if(ids.length<=0){
				alert("请选择要发起的供应商！");
				return;
			}else if(ids.length>1){
				alert("只能选一条！");
			}else{
				var inspectId = $("#list").jqGrid("getRowData",ids).inspectId;
				if(inspectId){
					alert("已经生成计划");
					return ;
				}else{
					window.open('${supplierctx}/audit/year-inspect/input.htm?userId='+ids);
				};
			}
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="audit";
		var thirdMenu="auditYearInspectPlan";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-audit-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="supplier-year-inspect-delete">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button id="searchBtn" class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="supplier-year-inspect-export">
						<button class="btn" onclick="iMatrix.export_Data('${supplierctx}/audit/year-inspect-plan/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="archives-send-audit-suppliers">
				    <button class="btn" onclick="editAuditSuppliers();"><span><span><b class="btn-icons btn-icons-audit"></b>发起稽核</span></span></button>
				</security:authorize>				
				<security:authorize ifAnyGranted="SUPPLIER_HIDE">
				<button class="btn" onclick="iMatrix.settingColumns();"><span><span>配置字段</span></span></button>
				<button class="btn" myType='settingData' onclick="iMatrix.setDataVisible(this);"><span><span>配置数据显示</span></span></button>
				</security:authorize>	
 				</div>
				<div style="float:right;position:absolute; right:50px;top:6px;" > 
				<security:authorize ifAnyGranted="supplier-add">					
					 <s:select list="systemCodes" 
								theme="simple"
								listKey="value" 
								listValue="name" 
								id="systemCode"
								onchange="systemCodeChange('audit/year-inspect-plan/list.htm')"
								cssStyle="width:60px"
								emptyOption="false"
								labelSeparator="">
					</s:select> 
				</security:authorize>						
				</div>   				
				<div id="message"><s:actionmessage theme="mytheme" /></div>	
				<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
				<div id="opt-content" >
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="list" url="${supplierctx}/audit/year-inspect-plan/list-datas.htm" submitForm="defaultForm" code="SUPPLIER_YEAR_INSPECT_PLAN" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>	
</body>
</html>