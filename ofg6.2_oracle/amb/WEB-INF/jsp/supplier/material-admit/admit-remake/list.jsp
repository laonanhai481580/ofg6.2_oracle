 <%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<%@ include file="/common/supplier-add.jsp" %>
	<c:set var="actionBaseCtx" value="${supplierctx}/material-admit/admit-remake"/>
	<script type="text/javascript">
	function createForm(){
		window.location="${actionBaseCtx}/input.htm";	
	}
	function click(cellvalue, options, rowObject){	
		return "<a href='javascript:void(0);' onclick='creatInput("+rowObject.id+");'>"+cellvalue+"</a>";
	}
	function creatInput(id){
		if(id){
			window.open('${actionBaseCtx}/input.htm?id='+id);
		}		
	}
	function stageFormatter(value,options, rowObject){
		var launchState = rowObject.launchState; 
		var changeWorkFlowColor = rowObject.changeWorkFlowColor;
		if(changeWorkFlowColor=='red'){
			return "<div style='text-align:center;margin-left:-10px;'><img src='"+webRoot+"/images/red.gif'/></div>";
		}else{
			if(launchState){
				var colName = options.colModel.name;
				if(launchState.indexOf(colName)>-1){
					var reg = new RegExp(colName + "$");
					if(reg.test(launchState)){
						return "<div style='text-align:center;margin-left:-10px;color:green;'>办理中...</div>";
					}else{
						return "<div style='text-align:center;margin-left:-10px;'><img src='"+webRoot+"/images/green.gif'/></div>";					
					}
				}else{
					return '';
				}
			}else{
				return "";
			}
		}
	}
	

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
	}
	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="admit";
		var thirdMenu="admitList";
 	</script>
 	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-material-admit-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
				<security:authorize ifAnyGranted="supplier-admit-remake-add">			
					<button class="btn" onclick="createForm();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="supplier-admit-remake-delete">
				    <button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<security:authorize ifAnyGranted="supplier-admit-remake-export">
				    <button  class='btn' onclick="iMatrix.export_Data('${actionBaseCtx}/export.htm?adminState=FDL');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>		
				</security:authorize> 
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
								onchange="systemCodeChange('material-admit/admit-remake/list.htm')"
								cssStyle="width:60px"
								emptyOption="false"
								labelSeparator="">
					</s:select> 
				</security:authorize>						
				</div>  				
				<div id="opt-content">
					<form id="contentForm" method="post" action="" >
						<grid:jqGrid gridId="dynamicComplain" url="${actionBaseCtx}/list-datas.htm" code="SUPPLIER_SUPPLIER_ADMIT" pageName="page" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>