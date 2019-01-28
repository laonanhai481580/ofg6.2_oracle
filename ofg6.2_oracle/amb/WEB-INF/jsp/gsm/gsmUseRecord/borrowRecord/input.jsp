<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%-- <%@ page import="com.ambition.gsm.entity.BorrowRecord" %> --%>
<%@ page import="com.ambition.gsm.entity.BorrowRecordSublist" %>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@ include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"
	type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
<%-- 	<%@ include file="/common/common-js.jsp" %> --%>
	<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
	<!-- 表单和流程常用的方法封装 -->
	<script type="text/javascript" src="${ctx}/js/workflow-form-0.9.js"></script>
<!-- 	流程驳回 -->
	<script type="text/javascript" src="${ctx}/js/lcbh.js"></script>
	<c:set var="actionBaseCtx" value="${gsmctx}/gsmUseRecord/borrowRecord"/>
<%-- 	<%@ include file="input-script.jsp" %> --%>
	<script type="text/javascript">
		$(document).ready(function(){
			//初始化表单元素
			$initForm({
				webBaseUrl:'${ctx}',//后台执行的Action的前缀名,如:http://localhost:8080/amb/qrqc
				actionBaseUrl : '${actionBaseCtx}',//项目的前缀地址,如:http://localhost:8080/amb
				formId:'workflowForm',//表单ID
				objId:'${id}',//数据库对象的ID
				taskId:'${taskId}',//任务ID
				children:{
					borrowRecordSublists:{
						entityClass:"<%=BorrowRecordSublist.class.getName()%>"//子表1实体全路径
					}
				},
// 				beforeSaveCallback:function(){
// 					var value = checkLoginName();
// 					return value;
// 				},
				inputformortaskform:'inputform',//表单类型,taskform:流程办理界面,inputform:普通表单页面
				fieldPermissionStr:'${fieldPermission}',//字符串格式的字段权限
			});
// 			$('#preserverDept','workflowForm').datepicker({changeMonth:true,changeYear:true});
// 			$.clearMessage(3000);
		});
// 		customInit();
	
// 		function loadXMLDoc() {
// 			var xmlhttp;
// 			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
// 				xmlhttp = new XMLHttpRequest();
// 			} else {// code for IE6, IE5
// 				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
// 			}
// 			xmlhttp.onreadystatechange = function() {
// 				if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
// 					document.getElementById("myDiv").innerHTML = "";
// 				}
// 			}
// 			xmlhttp.open("GET", "input.htm", true);
// 			xmlhttp.send();
// 		}
	</script>
   <%@ include file="input-script.jsp" %>
</head>

<body onload="getContentHeight();">
	<script type="text/javascript">
		var secMenu="myUseRecord";
		var thirdMenu="borrowRecordInput";
 	</script>
 	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>
 	<div class="ui-layout-west">
	<%@ include file="/menus/gsm-use-record-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
			<div class="opt-btn">
					<s:if test="taskId>0">
						<wf:workflowButtonGroup taskId="${taskId}"></wf:workflowButtonGroup>
					</s:if>
					<s:else>
						<security:authorize ifAnyGranted="GSM_GSMUSERECORD_SAVE">
							<%--  <button class='btn' type="button" onclick="saveForm();"><span><span><b class="btn-icons btn-icons-save"></b><s:text name='保存'/></span></span></button>  --%>
							<button class='btn' type="button" onclick="saveForm();">
								<span><span><b class="btn-icons btn-icons-save"></b>
										<s:text name='暂存' /></span></span>
							</button>
						</security:authorize>
						<security:authorize ifAnyGranted="GSM_GSMUSERECORD_PROCESS">
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
						<security:authorize ifAnyGranted="gsm_gsmUseRecord_borrowRecord-export-form">
						<button class='btn' onclick="exportForm();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
						</security:authorize>
					</s:if>
					<s:if test="task.active==0&&returnableTaskMaps.size>0">
						<button class='btn' type="button" id="_task_button"
							onclick="showIdentifiersDiv();">
							<span><span><b class="btn-icons btn-icons-unbo"></b>驳回到</span></span>
						</button>
					</s:if>
					<button class='btn' onclick="history.back();">
						<span><span><b class="btn-icons btn-icons-undo"></b>返回</span></span>
					</button>
				</div>
			<div id="opt-content" class="form-bg">
				<div style="color:red;" id="message"><s:actionmessage theme="mytheme"/></div>
	
				<s:form  id="workflowForm" name="workflowForm" method="post" action="">
					<table class="form-table-without-border" style="width:100%;margin: auto;">
<!-- 						<caption><h1>欧菲光科技有限公司</h1></caption> -->
						<caption><h2>仪器借调申请单</h2></caption>
						<caption style="text-align:right;padding-bottom:4px;">OFG.NO:${formNo}</caption>
					</table>
						<jsp:include page="input-form.jsp" />	
					<c:if test="taskId>0">
						<%@ include file="process-form.jsp"%>
					</c:if>
				</s:form>
			</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>