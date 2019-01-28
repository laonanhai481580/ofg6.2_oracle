<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@ include file="/common/meta.jsp" %>
<%-- 	<%@ include file="/common/common-js.jsp" %> --%>
<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
<!-- 表单和流程常用的方法封装 -->
<script type="text/javascript" src="${ctx}/js/workflow-form-0.9.js"></script>
<!-- 	流程驳回 -->
<script type="text/javascript" src="${ctx}/js/lcbh.js"></script>
<c:set var="actionBaseCtx" value="${supplierctx}/audit/year-inspect/all"/>
<script type="text/javascript">
		$(document).ready(function(){
			//初始化表单元素
			$initForm({
				webBaseUrl:'${ctx}',//后台执行的Action的前缀名,如:http://localhost:8080/amb/qrqc
				actionBaseUrl : '${actionBaseCtx}',//项目的前缀地址,如:http://localhost:8080/amb
				formId:'workflowForm',//表单ID
				objId:'${id}',//数据库对象的ID
				taskId:'${taskId}',//任务ID
				beforeSaveCallback:function(){
					sendEmail();
				},
				inputformortaskform:'inputform',//表单类型,taskform:流程办理界面,inputform:普通表单页面
				fieldPermissionStr:'${fieldPermission}',//字符串格式的字段权限
			});

		});
	</script>
</head>

<body onload="getContentHeight();">
	<script type="text/javascript">
		var secMenu="audit";
		var thirdMenu="yearInspectAll";
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
			<aa:zone name="main">
			<div class="opt-btn">
				<button class='btn' onclick="history.back();"><span><span><b class="btn-icons btn-icons-undo"></b>返回</span></span></button>		
			</div>
			<div id="opt-content" class="form-bg">
				<div style="color:red;" id="message"><s:actionmessage theme="mytheme"/></div>				
				<s:form  id="workflowForm" name="workflowForm" method="post" action="">
					<jsp:include page="input-form.jsp" />
				</s:form>
			</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>