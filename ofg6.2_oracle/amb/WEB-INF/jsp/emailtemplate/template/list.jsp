<%@page import="com.ambition.util.common.CommonUtil"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<style type="text/css">
	td {
		border-right-style:none;
		border-left-style:none;
		border-top-style:none;
		border-bottom-style:none;
	}
</style>
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>

<script type="text/javascript">
	//格式化
	function operateFormater(cellValue,options,rowObj){
		return operations = "<div style='text-align:left;' title='查看'><a class=\"small-button-bg\" onclick=\"editInfo("+cellValue+");\" href=\"#\"><span class=\"ui-icon ui-icon-info\" style='cursor:pointer;'></span></a></div>";
	}
	
	function formateAttachmentFiles(value,o,obj){
		return "<div>" +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
	}
	
	//查看
	function preview(){
		var ids = $("#list").jqGrid("getGridParam","selarrrow");
		if(ids.length==0){
			alert("请选择数据!");
			return;
		}
		$.colorbox({
			href:'${emailtemplatectx}/template/view.htm?id='+ids[0],
			iframe:true, 
			width:$(window).width()-300, 
			height:$(window).height(),
			overlayClose:false,
			title:"预览",
			onClosed:function(){
			}
		});
	}
	function createAnnouncement(){
		window.location.href = '${emailtemplatectx}/template/input.htm';
	}
	function editor(){
		var ids = $("#list").jqGrid("getGridParam","selarrrow");
		if(ids.length == 0||ids.length>1){
			alert("<s:text name="info.请选择一条数据"/>!");
			return;
		}
		window.location.href = '${emailtemplatectx}/template/input.htm?id='+ids;
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="template_sec";
		var thirdMenu="template_list";
	</script>
<%
		String _iframe = request.getParameter("_iframe");
		if(!"true".equalsIgnoreCase(_iframe)){
%>
                                                       
	<%@ include file="/menus/header.jsp" %>
	<div id="secNav">
		<%@ include file="/menus/emailtemplate-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/emailtemplate-template-menu.jsp"%>
	</div>
<% }%>

	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn" id="btnDiv">
					<security:authorize ifAnyGranted="emailtemplate-template-input">
					<button class='btn' onclick="createAnnouncement();"><span><span><b class="btn-icons btn-icons-add"></b><s:text name="新建"/></span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="emailtemplate-template-input">
					<button class='btn' onclick="editor();"><span><span><b class="btn-icons"></b><s:text name="编辑"/></span></span></button>
					</security:authorize>
					<button  class='btn' type="button" onclick="preview()"><span><span><b class="btn-icons btn-icons-play"></b><s:text name="预览"/></span></span></button>
					<security:authorize ifAnyGranted="emailtemplate-template-delete">
					<button class='btn' type="button" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b><s:text name="删除"/></span></span></button>
					</security:authorize>
					<button class='btn' type="button" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b><s:text name="查询"/></span></span></button>
					<span style="margin-left:6px;line-height:30px;color: red;" id="message"></span>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="list"  url="${emailtemplatectx}/template/list-datas.htm" code="ET_EMAIL_TEMPLATE" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>

</html>