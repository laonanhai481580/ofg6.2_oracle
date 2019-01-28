<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.supplier.entity.Supplier"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>	
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp"%>
	<%@ include file="/common/supplier-add.jsp" %>
	<%@ include file="/common/supplier-task.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
	function drawTask(){
		if(validateReadioTaskIds()){
			var id="";
			if(tree.getSelectNode()){
				id=tree.getSelectNode().id;
				if(id=='active_task'){
					id="";
				}
			}
			var taskType = $("#taskType").val();
			ajaxSubmit('task_form', encodeURI('${taskCtx}/task/task-receive.htm?typeName='+id+'&taskType='+taskType), 'product_task');
		}else{
			alert("请选择任务");
		}
	}
	function onClosedFun(){}
	function assign(){
		if(validateReadioTaskIds()){
			$.colorbox({href:webRoot+'/task/tree.htm',iframe:true, innerWidth:300, innerHeight:400,overlayClose:false,title:"选择"});
		}else{
			alert("请选择任务");
		}
	}
	function validateReadio(){
		var rds = jQuery("#taskTableId").getGridParam('selarrrow');
		
		for(var i=0;i<rds.length;i++){
			$("#task_id").attr("value", rds[i]);
			return true;
		}
		return false;
	}
	function validateReadioTaskIds(){
		var rds = jQuery("#taskTableId").getGridParam('selarrrow');
	    if(rds.length==0){
		    return false;
		}else{
			var taskIds = "";
			for(var i=0;i<rds.length;i++){
				taskIds+=rds[i]+",";
			}
			taskIds=taskIds.substring(0,taskIds.length-1);
			$("#task_ids").attr("value", taskIds);
		    return true;	
		}
	}
	
	function getTaskId(){
		return $("#task_id").attr("value");
	}

	function getTaskIds(){
		return $("#task_ids").attr("value");
	}
	
	function markColor(code){
			$("#flag").hide();
			if(validateReadioTaskIds()){
				var id="";
				if(tree.getSelectNode()){
					id=tree.getSelectNode().id;
					if(id=='active_task'){
						id="";
					}
				}
				$('#taskMark').attr('value', code.toUpperCase());
				var taskType = $("#taskType").val();
				ajaxSubmit("task_form", encodeURI('${taskCtx }/task/task-mark.htm?typeName='+id+'&taskType='+taskType), "product_task");
			}
	}
	
	//翻页
	function changePage(page){
		$("#pageNo").attr("value", page);
		ajaxSubmit('pageForm', '${taskCtx}/task/task.htm', 'product_task');
	}
	//错误页数
	function showErrorPageNo(){
		alert('<s:text name="page.errorPageNo"></s:text>');
	}
	
	function taskList(){
		setPageState();
		$("#searchBtn").find("span").find("span").html("查询");
		var typeName = $("#typeName").val();
		var currentId=typeName;
		if(currentId=="")currentId="active_task";
		activeTree("unhandle_bussiness_content",currentId,true,showTaskList);
		
	}

	function showTaskList(){
		var typeName = $("#typeName").val();
		var taskType = $("#taskType").val();
		ajaxSubmit('searchForm', encodeURI('${taskCtx}/task/task.htm?typeName='+typeName+'&taskType='+taskType), 'product_task');
	}
	
	function taskListAssgin(){
		var msg = "<font class=\"onSuccess\"><nobr>任务已成功指派</nobr></font>";
		$("#backMsg").html(msg);
		$("#backMsg").show();
		setTimeout('$("#backMsg").hide();',3000);
		taskList();
	}

	function changeTaskType(taskType){
		$("#typeName").attr("value","");
		$("#taskType").attr("value",taskType);
		ajaxSubmit('pageForm', '${taskCtx}/task/task.htm', 'product_task_type',changeTaskTypeCallback);
	}
	function changeTaskTypeCallback(){
		activeTree("unhandle_bussiness_content","active_task");
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide();$('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="task";
		var thirdMenu="_task";
 	</script>
 	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-task-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
	<div class="opt-body">
		<aa:zone name="product_task_all">
			<div class="opt-btn" >
				<button class="btn" onclick="$('#flag').hide();iMatrix.showSearchDIV(this);"  id="searchBtn"><span><span >查询</span></span></button>
				<!-- <button class="btn" onclick="drawTask();"><span><span>领取</span></span></button>
				<button class="btn" onclick="assign();"><span><span >指派</span></span></button> -->
				<s:select list="systemCodes" 
							theme="simple"
							listKey="value" 
							listValue="name" 
							id="systemCode"
							onchange="systemCodeChange('task/task.htm')"
							cssStyle="width:60px"
							emptyOption="false"
							labelSeparator="">
				</s:select> 
			</div>			
			<div id="opt-content" style="clear:both;">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="taskTableId"
						url="${supplierctx}/task/list-datas.htm" code="TASK_WF_TASK"></grid:jqGrid>
				</form>
			</div>
		</aa:zone>	
		</div>
	</div>
</body>
</html>
