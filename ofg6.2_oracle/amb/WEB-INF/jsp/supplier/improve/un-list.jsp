 <%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<c:set var="actionBaseCtx" value="${supplierctx}/improve"/>
	<script type="text/javascript">
	function createForm(){
		window.location="${actionBaseCtx}/input.htm";	
	}
	function click(cellvalue, options, rowObject){	
		return "<a href='${actionBaseCtx}/input.htm?id="+rowObject.id+"'>"+cellvalue+"</a>";
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
	function selectTypeChange(obj){
		var selectType=$("#selectType").val();
		window.location.href = encodeURI('${actionBaseCtx}/list.htm?selectType='+ selectType);
	}
	var params = {};
 	function hide(obj){
 		var id = $("#dynamicComplain").jqGrid("getGridParam","selarrrow");
 		if(id.length==0){
 			alert("请选择需要标记为敏感的数据！");
 			return ;
 		} 		
 		var url="${actionBaseCtx}/hiddenState.htm?id="+id+"&&type=N";
 		$("#message").html("正在操作,请稍候... ...");	
		$.post(encodeURI(url),params,function(result){
			if(result.error){
				alert("操作失败,"+result.message);
			}else{
				alert(result.message);
			};
			$("#dynamicComplain").jqGrid("setGridParam").trigger("reloadGrid");
		},'json');
 	}
	
	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="improve";
		var thirdMenu="improveUnList";
 	</script>
 	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-improve-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="supplier-improve-add">	
					<security:authorize ifAnyGranted="supplier-improve-conceal">		
						<button class="btn" onclick="createForm();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					</security:authorize>
					<security:authorize ifAnyGranted="supplier-improve-delete">
					    <button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="supplier-improve-hide">
						<button class='btn' onclick="hide(this)" type="button">
							<span><span><b class="btn-icons btn-icons-undo"></b>标记敏感</span></span>
						</button>
					</security:authorize>
					<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="supplier-improve-export">
					    <button  class='btn' onclick="iMatrix.export_Data('${actionBaseCtx}/export.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>		
					</security:authorize> 
					<div style="float:right;position:absolute; right:50px;top:6px;" >
					 <s:select list="selectTypes" 
								theme="simple"
								listKey="value" 
								listValue="name" 
								id="selectType"
								name="selectType"
								onchange="selectTypeChange(this)"
								cssStyle="width:60px"
								emptyOption="false"
								labelSeparator="">
					</s:select> 
					</div>
 				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="" >
						<grid:jqGrid gridId="dynamicComplain" url="${actionBaseCtx}/un-list-datas.htm?selectType=${selectType}" code="SUPPLIER_IMPROVE" pageName="page" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>