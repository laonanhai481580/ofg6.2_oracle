<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<%@ include file="/common/supplier-add.jsp" %>
<c:set var="actionBaseCtx" value="${supplierctx}/audit/year-inspect" />
<script type="text/javascript">
	function createqualiy() {
		window.location = "${actionBaseCtx}/input.htm";
	}

	function click(cellvalue, options, rowObject) {
		return "<a  href='${actionBaseCtx}/input.htm?id="
				+ rowObject.id + "'>" + cellvalue + "</a>";
	}
	//节点
	function stageFormatter(value, options, rowObject) {
		var launchState = rowObject.launchState;
		var changeWorkFlowColor = rowObject.changeWorkFlowColor;
		if (changeWorkFlowColor == 'red') {
			return "<div style='text-align:center;margin-left:-10px;'><img src='"+webRoot+"/images/red.gif'/></div>";
		} else {
			if (launchState) {
				var colName = options.colModel.name;
				if (launchState.indexOf(colName) > -1) {
					var reg = new RegExp(colName + "$");
					if (reg.test(launchState)) {
						return "<div style='text-align:center;margin-left:-10px;color:green;'>办理中...</div>";
					} else {
						return "<div style='text-align:center;margin-left:-10px;'><img src='"+webRoot+"/images/green.gif'/></div>";
					}
				} else {
					return '';
				}
			} else {
				return "";
			}
		}
	}
	//流转历史
	function viewProcessInfo(value,o,obj){
		var strs = "";
		strs = "<div style='width:100%;text-align:center;' title='查看流转历史' ><a class=\"small-button-bg\"  onclick=\"_viewProcessInfo("+obj.id+");\" href=\"#\"><span class='ui-icon ui-icon-info' style='cursor:pointer;text-align:right;'></span></a><div>";
		return strs;
	}
	function _viewProcessInfo(formId){
		$.colorbox({href:'${actionBaseCtx}/view-info.htm?id='+formId,iframe:true,
			innerWidth:$(window).width()<1100?$(window).width()-50:1100, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"表单"
			
		});
		$(document).ready(function(){
			contentResize();
		});
	}
	/*
	//自定义保存方法
	function customSave(gridId) {
			console.log(gridId);
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
	 */
	function $oneditfunc(rowid) {
		params = {};
		myId = rowid;
		// 	 		更改回车事件为下一单元格
		enterKeyToNext("customerGrid", rowid, function() {

		});
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu = "audit";
		var thirdMenu = "auditYearInspectList";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-audit-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="supplier-year-inspect-add">
						<button class="btn" onclick="createqualiy();">
							<span><span><b class="btn-icons btn-icons-add"></b>新建</span></span>
						</button>
					</security:authorize>
				<security:authorize ifAnyGranted="supplier-year-inspect-delete">
					<button class='btn' onclick="iMatrix.delRow();">
						<span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span>
					</button>
				</security:authorize>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);">
					<span><span><b class="btn-icons btn-icons-search"></b>查询</span></span>
				</button>
					<button class="btn" onclick="iMatrix.settingColumns();"><span><span>配置字段</span></span></button>
				<security:authorize ifAnyGranted="SUPPLIER_HIDE">
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
								onchange="systemCodeChange('audit/year-inspect/list.htm')"
								cssStyle="width:60px"
								emptyOption="false"
								labelSeparator="">
					</s:select> 
				</security:authorize>						
				</div> 			
			<div id="opt-content" style="clear: both;">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="suppliers" url="${supplierctx}/audit/year-inspect/list-state.htm"
						code="SUPPLIER_YEAR_INSPECT"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>