<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.DAY_OF_MONTH,1);
	String startDateStr = sdf.format(calendar.getTime());
	
	calendar.add(Calendar.MONTH,1);
	calendar.add(Calendar.DATE,-1);
	String endDateStr = sdf.format(calendar.getTime());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp"%>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript">
		function loadByFeature(featureId,featureName){
			$("#groupIframe").get(0).contentWindow.loadByFeature(featureId,featureName);
		}
		function refreshFeaturs(){
			$("#featureIframe").get(0).contentWindow.reloadGrid();
		}
		function openColorbox(colorboxParams,onClosed){
// 			colorboxParams.onClosed = onClosed;
			$.colorbox(colorboxParams);
		}
		
		function batchMigration(groupIds){
			var progressWin = progressbar("正在准备迁移质量特性数据...");
			var url = "${spcctx}/data-acquisition/batch-migration.htm";
			$.post(url,{groupIds:groupIds},function(result){
				if(result.error){
					alert(result.message);
					progressWin.dialog("close");
				}else{
					var intervalFlag = null;
					var queryStatusFunc = function(executeFlag){
						url = "${spcctx}/data-acquisition/query-batch-migration-status.htm";
						$.post(url,{executeFlag:executeFlag},function(result){
							progressWin.find("div[content]").html(result.message);
							if(result.error){
								alert(result.message);
								progressWin.dialog("close");
								clearInterval(intervalFlag);
							}else if(result.complete){
								alert(result.message);
								progressWin.dialog("close");
								clearInterval(intervalFlag);
								refreshFeaturs();
							}
						},'json');
					};
					//2秒检查一次
					intervalFlag = setInterval(function(){
						queryStatusFunc(result.executeFlag);
					},2000);
				}
			},'json');
		}
		function progressbar(content){
			var windowId = "window" + (new Date()).getTime();
			var html = '<div style="border:0px;padding:3px;overflow-y:auto;" id="'+windowId+'" title="迁移进度">'
				+ '<div content=true style="">'+content+'</div>'
				+ '</div>';
			$(".ui-layout-center").append(html);
			var t = null;
			$("#" + windowId).dialog({
				width : 400,
				height : 110,
				resizable:false,
				modal : false,
				draggable : true,
				close : function(){
					$("#" + windowId).remove();
					clearInterval(t);
				}
			});
			//处理样式出错
			$("#" + windowId).parent().find(".ui-widget-header")
			.height(24)
			.css("padding-left","4px")
			.css("padding-top","7px")
			.css("border-top","0px")
			.css("border-left","0px")
			.css("border-right","0px");
			$("#" + windowId).parent().find(".ui-dialog-titlebar-close")
				.css("float","right")
				.css("margin-right","4px");
			var bottonPanel = $("#" + windowId).parent().find(".ui-dialog-buttonset")
				.css("height","35px")
				.css("padding-top","5px")
				.css("text-align","center");
			bottonPanel.parent().css("border-left","0px")
			.css("border-right","0px")
			.css("border-bottom","0px");
			return $("#"+windowId);
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="data_acq";
		var thirdMenu="_maintenance_data";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/spc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/spc-data-acquisition-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div id="opt-content" style="padding:0px;overflow:hidden;">
			<table style="width:100%;border:0px;height:100%;" cellpadding="0" cellspacing="0">
				<tr>
					<td style="width:40%;height:100%;">
						<iframe id="featureIframe" src="${spcctx}/data-acquisition/data-list-left.htm" style="width:100%;height:100%;"></iframe>
					</td>
					<td style="width:10px;">&nbsp;</td>
					<td style="width:60%;height:100%;">
						<iframe id="groupIframe" src="${spcctx}/data-acquisition/data-list-right.htm" style="width:100%;height:100%;"></iframe>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>