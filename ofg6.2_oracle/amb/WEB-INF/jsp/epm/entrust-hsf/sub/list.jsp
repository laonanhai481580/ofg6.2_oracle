<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<c:set var="actionBaseCtx" value="${epmctx}/entrust-hsf/sub" />
<script type="text/javascript">
	function createqualiy() {
		window.location = "${epmctx}/entrust-hsf/input.htm";
	}
	
	function click(cellvalue, options, rowObject) {
		return "<a target='_blank' href='${epmctx}/entrust-hsf/input.htm?sunId=" + rowObject.id + "'>"
				+ cellvalue + "</a>";
	}
	//节点
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
	//流转历史
	function viewProcessInfo(value,o,obj){
		var strs = "";
		strs = "<div style='width:100%;text-align:center;' title='查看流转历史' ><a class=\"small-button-bg\"  onclick=\"_viewProcessInfo("+obj.id+");\" href=\"#\"><span class='ui-icon ui-icon-info' style='cursor:pointer;text-align:right;'></span></a><div>";
		return strs;
	}
	function _viewProcessInfo(formId){
		$.colorbox({href:'${epmctx}/entrust-hsf/view-info.htm?sunId='+formId,iframe:true,
			innerWidth:$(window).width()<1100?$(window).width()-50:1100, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"表单"
		});
	}
	
	function showPicture(value,options,obj){
		var strs = "";
		strs = "<div style='width:100%;' title='上传附件' ><a class=\"small-button-bg\" onclick=\"attachmentFilesClick('"+obj.id+"');\" href=\"#\"><input type='hidden' id='"+obj.id+"_hiddenFiles' value='"+value+"'/><span id='"+obj.id+"_uploadBtn' class='ui-icon ui-icon-image uploadBtn' style='cursor:pointer;display:none;'></span></a><span style='text-align:left;' id='"+obj.id+"_showFiles'>"+$.getDownloadHtml(value)+"</span><div>";
		return strs;
	}
	function attachmentFilesClick(rowId){
		//上传附件 
		$.upload({   
			showInputId : rowId + "_showFiles",
			hiddenInputId : rowId + "_hiddenFiles",
			title:"上传附件",
			callback:function(files){
				params.foreignFile = $("#" + rowId + "_hiddenFiles").val();
			}
		}); 
	}
	function addNew(){
		iMatrix.addRow();
		$("#undefined_uploadBtn").show();
	}
	var params = {};
	function $oneditfunc(rowId){
		selRowId = rowId;
		params.attachmentFiles = $("#" + rowId + "_hiddenFiles").val();
		params.hisAttachmentFiles = params.attachmentFiles;
		$(".uploadBtn").hide();
		$("#undefined_uploadBtn").show();
		$("#" + rowId + "_uploadBtn").show();
		
	}
	function $afterrestorefunc(rowId){
		$("#" + rowId + "_uploadBtn").hide();
	}
	
	function $oneditfunc(rowid) {
		params = {};
		myId = rowid;
		// 	 		更改回车事件为下一单元格
		enterKeyToNext("customerGrid", rowid, function() {

		});
	}
	 function viewChildTableInputForm(value,o,obj){
			var strs = "";
			strs = "<div style='width:100%;text-align:center;' title='查看子表详情' ><a class=\"small-button-bg\"  onclick=\"_viewChildTableInputForm("+obj.id+");\" href=\"#\"><span class='ui-icon ui-icon-info' style='cursor:pointer;text-align:right;'></span></a><div>";
			return strs;
		}

		function addExceptionSingle(){
	 		var id = $("#list").jqGrid("getGridParam","selarrrow");
	 		if(id.length>1){
	 			alert("只能选择一项！");
	 			return ;
	 		}else if(id.length==0){
	 			alert("请选择一项！");
	 			return ;
	 		}
			window.open('${epmctx}/exception-single/input.htm?formId='+id+"&&type=hsf");
		}
		function alterException(obj){
			var id = $("#list").jqGrid("getGridParam","selarrrow");
			console.log(id);
			if(id.length>1){
	 			alert("只能选择一项！");
	 			return ;
	 		}else if(id.length==0){
	 			alert("请选择一项！");
	 			return ;
	 		}
			$.post('${epmctx}/exception-single/alter-exception.htm?entrustId='+id+"&&type=hsf", null,
					   function(data){
					     window.location.reload(href='${epmctx}/entrust-ort/sub/list.htm');
					     alert("修改成功");
					   });
// 			window.location.href='${epmctx}/exception-single/alter-exception.htm?entrustId='+id+"&&type=hsf";
// 			window.location.reload(href='${epmctx}/entrust-ort/sub/list.htm');
		}
		function epmHide(obj){
	 		var id = $("#list").jqGrid("getGridParam","selarrrow");
// 	 		if(id.length==0){
// 	 			alert("请选择一项！");
// 	 			return ;
// 	 		}
	 		$.post('${epmctx}/entrust-hsf/sub/epmHide.htm?id='+id,
	 		function(data){
// 	 			  window.location.reload(href='${epmctx}/entrust-hsf/sub/list.htm');
				  alert("修改成功");
	 		});
		}
		function showIdentifiersDiv(idName,buttonId){
			if($("#"+idName).css("display")=='none'){
				removeSearchBox();
				$("#"+idName).show();
				var position = $("#"+buttonId).position();
				$("#"+idName).css("left",position.left+15);
				$("#"+idName).css("top",position.top+28);
			}else{
				$("#"+idName).hide();
			}
		}
		function show_moveiIdentifiersDiv(idName){
			clearTimeout(identifiersDiv);
		}
		var identifiersDiv;
		function hideIdentifiersDiv(idName){
			identifiersDiv = setTimeout('$("#"+idName+")".hide()',300);
		}
		//修改对内对外状态
	 	function updateState(obj){
	 		hideIdentifiersDiv();
	 		var type="";
	 		var title = $(obj).attr("title");
	 		if(title=="对内"){
	 			type="in";
	 		}else if(title=="对外"){
	 			type="out";
	 		}
	 		var url='${epmctx}/entrust-hsf/sub/update-all.htm?&type='+type;
			$("#message").html("正在修改状态,请稍候... ...");	
			$.post(encodeURI(url),params,function(result){
				if(result.error){
					alert("修改失败"+result.message);
				}else{
					alert(result.message);
				};
				showIdentifiersDiv("flag","_update_state");
				$("#list").jqGrid("setGridParam").trigger("reloadGrid");
			},'json');
	 		
	 	}
	 	//手动修改结果
		function updateAfter(){
			var rowIds = $("#list").jqGrid("getGridParam","selarrrow");
			if(rowIds.length==0){
				alert("请选择需要修改的数据!");
				return;
			}
			var url='${epmctx}/entrust-hsf/sub/update-input.htm?&id='+rowIds;
			$.colorbox({href:url,iframe:true, innerWidth:700, innerHeight:500,
				overlayClose:false,
				onClosed:function(){
					$("#list").jqGrid("setGridParam").trigger("reloadGrid");
				},
			});
		}
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu = "entrust";
		var thirdMenu = "entrustListSub";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>
	<div id="secNav">
		<%@ include file="/menus/epm-sec-menu.jsp"%>
	</div>
	<div class="ui-layout-west">
		<%@ include file="/menus/epm-entrust-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="epm_entrustHsf_save">
						<button class="btn" onclick="createqualiy();">
							<span><span><b class="btn-icons btn-icons-add"></b>新建</span></span>
						</button>
					</security:authorize>
					<button class='btn' onclick="iMatrix.showSearchDIV(this);"
						type="button">
						<span><span><b class="btn-icons btn-icons-search"></b>查询</span></span>
					</button>
					<security:authorize ifAnyGranted="epm_entrustOrt_exceptionOrt">
						<button class='btn' onclick="epmHide(this)"
							type="button">
							<span><span><b class="btn-icons btn-icons-export1"></b>修改异常结果</span></span>
						</button>
					</security:authorize>
					<security:authorize ifAnyGranted="epm_entrust-update">
						<button class='btn' onclick="updateAfter(this)" type="button">
							<span><span><b class="btn-icons btn-icons-edit"></b>修改结果</span></span>
						</button>
						<button class='btn' type="button" id="_update_state" onclick='showIdentifiersDiv("flag","_update_state");'><span><span><b class="btn-icons btn-icons-sync"></b>修改状态</span></span></button>
					</security:authorize>
					<div id="flag" onmouseover='show_moveiIdentifiersDiv("flag");' onmouseout='hideIdentifiersDiv("flag");'>
						<ul style="width:130px;">
							 <li onclick="updateState(this);" style="cursor:pointer;" title="对内">对内</li>
							 <li onclick="updateState(this);" style="cursor:pointer;" title="对外">对外</li>
						</ul>
					</div>
					<security:authorize ifAnyGranted="epm_entrustHsf_export">
						<button class='btn' onclick="iMatrix.export_Data('${actionBaseCtx}/export.htm');"
							type="button">
							<span><span><b class="btn-icons btn-icons-export"></b>导出</span></span>
						</button>
					</security:authorize>
					<button class="btn" onclick="iMatrix.settingColumns();"><span><span>配置字段</span></span></button>
					<security:authorize ifAnyGranted="epm_entrustHsf_hide">
					<button class="btn" myType='settingData' onclick="iMatrix.setDataVisible(this);"><span><span>配置数据显示</span></span></button>
					</security:authorize>	
				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="">
						<grid:jqGrid gridId="list" url="${actionBaseCtx}/list-state.htm"
							code="EPM_ENTRUST_HSF_SUBLIST" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>