 <%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<c:set var="actionBaseCtx" value="${improvectx}/exception" />
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
	var params = {};
 	function hide(obj){
 		var id = $("#dynamicComplain").jqGrid("getGridParam","selarrrow");
 		if(id.length==0){
 			alert("请选择需要取消敏感标记的数据！");
 			return ;
 		} 		
 		var url="${actionBaseCtx}/hiddenState.htm?id="+id+"&&type=Y";
 		//$("#message").html("正在操作,请稍候... ...");	
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
		var secMenu="8d_report";
		var thirdMenu="exception_report_hide";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/improve-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/improve-8d-report-third-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="IMP_EXCEPTION_REPORT_EXPORT_HIDE">
					    <button  class='btn' onclick="iMatrix.export_Data('${actionBaseCtx}/export-hide.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>		
					</security:authorize> 
					<security:authorize ifAnyGranted="IMP_EXCEPTION_REPORT_HIDE">
						<button class='btn' onclick="hide(this)" type="button">
							<span><span><b class="btn-icons btn-icons-undo"></b>取消标记</span></span>
						</button>
					</security:authorize>
 				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="" >
						<grid:jqGrid gridId="dynamicComplain" url="${actionBaseCtx}/hide-list-datas.htm" code="IMP_QUALITY_EXCEPTION_REPORT" pageName="page" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>