<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%@page import="com.ambition.supplier.entity.SupplierAdmitFile"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/common-js.jsp" %>
<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"
	type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
	<!-- 表单和流程常用的方法封装 -->
	<script type="text/javascript" src="${ctx}/js/workflow-form-0.9.js"></script>
<!-- 	流程驳回 -->
	<script type="text/javascript" src="${ctx}/js/lcbh.js"></script>
	<c:set var="actionBaseCtx" value="${supplierctx}/material-admit/admit-remake"/>
	<%@ include file="input-script.jsp" %>
	<script type="text/javascript">
		$(document).ready(function(){
			//初始化表单元素
			$initForm({
				webBaseUrl:'${ctx}',//后台执行的Action的前缀名,如:http://localhost:8080/amb/qrqc
				actionBaseUrl : '${actionBaseCtx}',//项目的前缀地址,如:http://localhost:8080/amb
				formId:'workflowForm',//表单ID
				objId:'${id}',//数据库对象的ID
				taskId:'${taskId}',//任务ID
				currentActivityName:'${currentActivityName}',//当前环节名称
				children:{
					supplierAdmitFiles:{
						entityClass:"<%=SupplierAdmitFile.class.getName()%>"//子表1实体全路径
					}
				},
				beforeSaveCallback:function(){
				},
				inputformortaskform:'inputform',//表单类型,taskform:流程办理界面,inputform:普通表单页面
				fieldPermissionStr:'${fieldPermission}',//字符串格式的字段权限
			});
			var a=$("#str").val();
			subControl(a);			
			setFinalStatus();
			setAllLogs();
		});
		
	</script>
	<style type="text/css">
        .admit{
            background-color:#A8A8A8;
        }
        .amend{
            background-color:#FF0000;
        }
        button[type='button']:disabled{ 
 		    pointer-events: none; 
 		    cursor: not-allowed; 
 		    opacity: .70; 
 			background-color:#f9f9f9;
 		    color:#aca899;
 			background: url(/amb/images/black.gif) repeat-x; 
 		} 
    </style>
</head>

<body onload="getContentHeight();">
	<script type="text/javascript">
		var secMenu="admit";
		var thirdMenu="admitInput";
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
				<s:if test="taskId>0">
						<wf:workflowButtonGroup taskId="${taskId}"></wf:workflowButtonGroup>
					</s:if>
					<s:else>
						<security:authorize ifAnyGranted="supplier-admit-remake-save">
							 <button class='btn' type="button" onclick="saveForm();">
								<span><span><b class="btn-icons btn-icons-save"></b>
									<s:text name='暂存' /></span></span>
							</button> 
							<button class='btn' type="button" onclick="submitForm();">
								<span><span><b class="btn-icons btn-icons-ok"></b>
									<s:text name='提交' /></span></span>
							</button>
						</security:authorize>	
					</s:else>
					<s:if test="taskId>0">
						<button class="btn" onclick="viewFormInfo()">
							<span><span><b class="btn-icons btn-icons-info"></b>
								<s:text name='流转历史' /></span></span>
						</button>
					</s:if>
					<!-- 					
					<s:if test="taskId>0&&currentActivityName!='会签'">
					 <button  class='btn' type="button" id="_task_button" onclick="showIdentifiersDiv();"><span><span><b class="btn-icons btn-icons-back"></b>驳回</span></span></button>
					</s:if>驳回按钮 -->
					<div id="flag" onmouseover='show_moveiIdentifiersDiv();' onmouseout='hideIdentifiersDiv();'>
						<ul style="width:300px;">
							 <s:iterator var="returnTaskName" value="returnableTaskNames">
								 <li onclick="backToTask(this,'${actionBaseCtx}','${taskId}');" style="cursor:pointer;" title="驳回到 ${returnTaskName}" taskName="${returnTaskName}">
								  ${returnTaskName}
								 </li>
							 </s:iterator>
						</ul>
				    </div>
				    <span style="color:red;" id="message1"></span>
				<button class='btn' onclick="history.back();"><span><span><b class="btn-icons btn-icons-undo"></b>返回</span></span></button>	
			</div>
			<div id="opt-content" class="form-bg">
				<div style="color:red;" id="message"><s:actionmessage theme="mytheme"/></div>
				<div>
					<form id="defaultForm1" name="defaultForm1" action="">
						<input type="hidden" name="id" id="id" value="${id}" />
						<input name="taskId" id="taskId" value="${taskId}" type="hidden" />
						<input id="selecttacheFlag" type="hidden" value="true" />
					</form>
				</div>
				<s:form  id="workflowForm" name="workflowForm" method="post" action="">
					<jsp:include page="input-form.jsp" />
					<%-- <s:if test="taskId>0">
						<%@ include file="process-form.jsp"%>
					</s:if> --%>
				</s:form>
			</div>
			</aa:zone>
		
		</div>
	</div>
</body>
</html>