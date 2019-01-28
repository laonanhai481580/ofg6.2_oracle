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
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
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
	}
	function $processRowData(data){
		for(var pro in params){
			data[pro] = params[pro];
		}
		return data;
	}
	//修改供应商信息
	function editInfo(id){
		$.colorbox({
			href:'${supplierctx}/archives/input.htm?id='+id,
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
	//导出
	function exportSuppliers(){
		var state = '';
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
		var url = '${supplierctx}/archives/import-supplier-form.htm';
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
	//一次性导入供应商准入物料
	function initAdmittanceMaterial(){
		var url = '${supplierctx}/archives/import-supplier-material-form.htm';
		$.colorbox({href:url,iframe:true, 
			innerWidth:350, innerHeight:240,
			overlayClose:false,
			title:"导入供应商准入物料",
			onClosed:function(){
				$("#suppliers").trigger("reloadGrid");
				makeEditable(true);
			}
		});
	}
	
 	function $beforeEditRow(rowId,iRow,iCol,e){
		var isRight = false;
		<security:authorize ifAnyGranted="archives-input">
			isRight =  true;
		</security:authorize>
		return isRight;
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="archives";
		var thirdMenu="_archives_list_all";
	</script>


	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-supplier-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="archives-input">
				<button class='btn' onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="archives-delete">
				<button class='btn' onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
				<security:authorize ifAnyGranted="archives-exports">
				<button class="btn" onclick="exportSuppliers();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="archives-imports">
<!-- 				<button class="btn" onclick="initAdmittanceMaterial();"><span><span><b class="btn-icons btn-icons-export"></b>导入准入物料</span></span></button> -->
				</security:authorize>
			</div>
			<div style="float:right;position:absolute; right:50px;top:6px;" >
			<security:authorize ifAnyGranted="supplier-add">					
				 <s:select list="systemCodes" 
							theme="simple"
							listKey="value" 
							listValue="name" 
							id="systemCode"
							onchange="systemCodeChange('archives/list-all.htm')"
							cssStyle="width:60px"
							emptyOption="false"
							labelSeparator="">
				</s:select> 
			</security:authorize>						
			</div>				
			<div id="opt-content" style="clear:both;">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="suppliers"
						url="${supplierctx}/archives/list-datas.htm" code="SUPPLIER_SUPPLIER"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
</html>