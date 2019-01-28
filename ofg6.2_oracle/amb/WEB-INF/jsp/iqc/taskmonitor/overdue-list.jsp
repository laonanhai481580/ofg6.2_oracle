<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript">
	//选择提醒人员
	var selRowId = null;
	function receiveManClick(obj){	
		selRowId=obj.rowid;
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择人员',
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'loginName',
			leafPage:'false',
			multiple:'true',
			hiddenInputId:'personId',
			showInputId:'personName',
			acsSystemUrl:acsSystemUrl,
			callBack:function(){
				setWarmingMan();
			}
		});			
	}		
	function setWarmingMan(){
		var warmingMan=$("#personName").val();
		$("#"+selRowId+"_receiveMan").val(warmingMan);
		var warmingManLogin=$("#personId").val();
		$("#"+selRowId+"_reveiveLoginMan").val(warmingManLogin);
	}
	
	//报检物料清单弹出框选择
	var rowId=null;
	function materielCodeClick(obj){
		rowId=obj.rowid;	
		$.colorbox({href:"${iqcctx}/inspection-base/inspection-bom/bom-inspection-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择物料"
 		});
 	}
	function setInspectionBomValue(datas){
		$("#"+rowId+"_materielCode").val(datas[0].materielCode);
 	}
 	var params="";
	//同步历史数据
 	function addOld(obj){
		var title="同步历史物料";
		var max=150;
 		var url='${iqcctx}/taskmonitor/save-old.htm';
		if(confirm("确定要执行操作吗?")){
			$("#message").html("正在执行数据同步,请稍候... ...");
			$(".opt-btn button").attr("disabled",true);
			var progressWin = progressbar(title,max);
			$.post(encodeURI(url),{},function(result){
				$("#message").html("");
				$("button").attr("disabled","");
				progressWin.dialog("close");
				alert(result.message);
				$("#message").html("");
				$("#dynamicInspection").jqGrid("setGridParam").trigger("reloadGrid");
			},'json');
		}
 		
 	}		
	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="taskMontior";
		var thirdMenu="_overdue_list";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-task-monitor-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn" id="btnDiv">
					<security:authorize ifAnyGranted="taskmonitor-overdue-list-add">
						<button class='btn' type="button" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="taskmonitor-overdue-list-delete">
						<button class='btn' type="button" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
						<button class='btn' type="button" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="taskmonitor-overdue-list-export">
						<button class='btn' type="button"  onclick="iMatrix.export_Data('${iqcctx}/taskmonitor/overdue-export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="taskmonitor-overdue-addold">
						<button class="btn" onclick="addOld();"><span><span><b class="btn-icons btn-icons-play"></b>同步</span></span></button>
					</security:authorize>	
					<input type="hidden" name="personName"  id="personName" value=""/>
					<input type="hidden" name="personId"  id="personId" value=""/>	
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="dynamicInspection"  url="${iqcctx}/taskmonitor/overdue-list-datas.htm" code="IQC_INSPECTION_OVERDUE_EMAIL" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>