<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"></script>
    <script type="text/javascript" src="${resourcesCtx}/widgets/validation/dynamic.validate.js"></script>
    <script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
		//添加/修改数据库配置
		function editInfo(isEdit,isCopy){
			var id = "";
			if(isEdit||isCopy){
				id = $("#list").jqGrid("getGridParam","selrow");
				if(!id){
					alert("请先选择数据!");
					return;
				}
			}
			var url = '${chartdesignctx}/custom-search/input.htm?id='+id;
			if(isCopy){
				url += '&isCopy=true';
			}
			$.colorbox({
				href:url,
				iframe:true, 
				width:$(window).width()<800?$(window).width()-100:800, 
				height:$(window).height()-40,
				overlayClose:false,
				title:"配置向导",
				onClosed:function(){
					$("#list").trigger("reloadGrid");
					makeEditable(true);
				}
			});
		}
		
		function operatorFormatter(val,o,rowObj){
			return "<div style='text-align:center;'><a href='javascript:void(0);showViewChart(\""+rowObj.code+"\")'>预览</a><div>";
		}
		
		function showViewChart(code){
			var url = '${chartdesignctx}/chart-view/view.htm?chartDefinitionCode='+code;
			window.open(url);
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="_custom_search";
		var thirdMenu="_list";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/chartdesign-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/chartdesign-customsearch-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="CHARTDESIGN-CUSTOMSEARCH-INPUT">
				<button class='btn' onclick="editInfo();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				<button class='btn' onclick="editInfo(true);"><span><span><b class="btn-icons btn-icons-edit"></b>修改</span></span></button>
				<button class='btn' onclick="editInfo(true,true);"><span><span><b class="btn-icons btn-icons-copy"></b>复制</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="CHARTDESIGN-CUSTOMSEARCH-DELETE">
				<button class='btn' onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
			</div>
			<div id="opt-content">
				<form id="contentForm" method="post" action="">
					<grid:jqGrid gridId="list" url="${chartdesignctx}/custom-search/list-datas.htm" code="CHARTDESIGN_CUSTOM_SEARCH"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
</html>