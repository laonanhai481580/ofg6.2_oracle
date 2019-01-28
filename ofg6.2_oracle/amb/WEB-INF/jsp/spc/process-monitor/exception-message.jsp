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
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<style>
<!--
	#searchUl{
		margin:0px;
		padding:0px;
	}
	#searchUl li{
		float:left;
		width:260px;
		height:24px;
		line-height:24px;
		list-style:none;
		margin-top:2px;
	}
	#searchUl li select{
		width:170px;
	}
	#searchUl li input{
		width:170px;
	}
	.label{
		float:left;
		width:80px;
		text-align:right;
		padding-right:2px;
	}
	#groupUl{
		margin:0px;
		padding:0px;
	}
	#groupUl li{
		float:left;
		width:95px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#groupUl li.last{
		padding:0px;
		width:280px;
		margin-bottom:2px;
		text-align:right;
	}
-->
</style>
<script type="text/javascript">
	function reasonFormatter(value,options,rowData){
		var strs = '';
		<security:authorize ifAnyGranted="spc_process-monitor_reason-measure-input">
		strs = "<div style='text-align:center;'><a title='添加原因措施' class='small-button-bg' onclick='addReason("+rowData.id+")'><span class='ui-icon ui-icon-note' style='cursor:pointer;'></span></a></div>";
		</security:authorize>
		return strs;
	}
	
	function addReason(id){
		$.colorbox({href:"${spcctx}/process-monitor/reason-measure-input.htm?messageId="+id,
			iframe:true, 
			innerWidth:$(window).width()<1366?$(window).width()-124:$(window).width()-366, 
			innerHeight:$(window).height()<768?$(window).height()-68:$(window).height()-68,
			overlayClose:false,
			title:"添加原因措施",
			onClosed:function(){
				$("#exMessages").trigger("reloadGrid");
			}
		});
	}
	
	function numFormat(cellvalue, options, rowObject){
		var operations = '';
		if(cellvalue){
			<security:authorize ifAnyGranted="spc_process-monitor_subgroup-detail">
			operations = "<a onclick='callList("+rowObject.id+");' href='#'>"+cellvalue+"</a>";
			</security:authorize>
		}
		return operations;
	}
	
	function callList(id){
		$.colorbox({
			href:'${spcctx}/process-monitor/subgroup-detail.htm?messageId='+id,
			iframe:true, 
			innerWidth:$(window).width()<1366?$(window).width()-124:$(window).width()-366, 
			innerHeight:$(window).height()<768?$(window).height()-68:$(window).height()-68,
			overlayClose:false,
			title:"子组信息",
			onClosed:function(){
				$("#exMessages").trigger("reloadGrid");
			}
		});
	}
	
	function exportMessage(){
		//$("#contentForm").attr("action","${spcctx}/process-monitor/exports.htm");
		//$("#contentForm").submit();
		iMatrix.export_Data("${spcctx}/process-monitor/exports.htm");
	}
	var params = '';
	function contentResize(){
		var height = $(window).height() - $('#customerSearchDiv').height() - 200;
		$("#exMessages").jqGrid("setGridHeight",height);
	}

	$(document).ready(function(){
		params = getParams();
		$("#datepicker1").datepicker({changeMonth:true,changeYear:true});
		$("#datepicker2").datepicker({changeMonth:true,changeYear:true});
		contentResize();
	});
	//获取表单的值
	function getParams(){
		var params = {};
		$(":input","#customerSearchDiv").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		$("#customerSearchDiv input[type=hidden]").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		return params;
	}
	
	function search(){
		params = getParams();
		var date1 = $("#datepicker1").val();
		var date2 = $("#datepicker2").val();
		if(date1>date2){
			alert("日期前后选择有误,请重新设置!");
			$("#datepicker1").focus();
		}else{
			$('table.ui-jqgrid-btable').each(function(index,obj){
				obj.p.postData = params;
				$(obj).trigger("reloadGrid");
			});
		}
	}
	//选择质量特性
	function selectFeature(obj){
		$.colorbox({href:"${spcctx}/common/feature-bom-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
			overlayClose:false,
			title:"选择质量特性"
		});
	}
	
	function setFeatureValue(datas){
		$("#featureName").val(datas[0].value);
		$("#qualityFeature").val(datas[0].id);
	}
	//设置默认 参数
	function $addGridOption(jqGridOption){
		jqGridOption.postData = params;
	}
	function resetAll(){//重置表单
		$("#qualityFeature").val("");//重置隐藏域
		$('#contentForm')[0].reset();//重置显示域
	}
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="process_monitor";
		var thirdMenu="_messages";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/spc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/spc-process-monitor-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow: auto">
			<form id="contentForm" name="contentForm" method="post" action="">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="spc_process-monitor_info-delete">
						<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
<!-- 					<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button> -->
					<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					
					<security:authorize ifAnyGranted="spc_process-monitor_exports">
						<button class='btn' onclick="exportMessage();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<button class="btn" onclick="resetAll();" type="button"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
					<span style="color:red;" id="message"></span>
				</div>
				<div id="customerSearchDiv" style="display:block;padding:4px;">
					<table class="form-table-outside-border" style="width:99.7%;padding:4px;">
						<tr>
							<td>
								<ul id="searchUl">
							 		<li>
							 			<span class="label">发生时间</span>
							 			<input id="datepicker1" type="text" readonly="readonly" style="width:68px;" name="params.startDate_ge_date" value="<%=startDateStr%>"/>
							 			至
							 			<input id="datepicker2" type="text" readonly="readonly" style="width:68px;" name="params.endDate_le_date" value="<%=endDateStr%>"/>
							 		</li>
							 		<li>
							 			<span class="label">处理单号</span>
							 			<input type="text" name="params.num" id="num"></input>
							 		</li>
							 		<li>
							 			<span class="label">处理状态</span>
							 			<select  style="height:22px;" name="params.priState" id="priState">
											<option value="">请选择</option>
											<option value="1">已处理</option>
											<option value="2">未处理</option>
										</select>
							 		</li>
							 		<li>
							 			<span class="label">质量特性</span>
										<input Sname="featureName" id="featureName" onclick="selectFeature(this);" readonly="readonly"/>
										<input name="params.qualityFeatures" id="qualityFeature" type="hidden"/>
							 		</li>
<!-- 							 		<li> -->
<!-- 							 			<span class="label">最后子组数</span> -->
<!-- 							 			<input type="text" name="lastAmout" id="lastAmout"></input> -->
<!-- 							 		</li> -->
<%-- 							 		<jsp:include page="search-layer.jsp" /> --%>
						 		</ul>
							</td>
						</tr>
<!-- 						<tr> -->
<!-- 							<td style="width:280px;" colspan="2"> -->
<!-- 								<span style="font-weight: bold;color:red;float: right;"> -->
<!-- 									该行查询框可模糊查询: -->
<!-- 								</span> -->
<!-- 							</td> -->
<!-- 							<td>工厂名称 -->
<!-- 								<input  type="text" name="params.OneName" id="OneName"/> -->
<!-- 								&nbsp; -->
<!-- 								质量特性模块 -->
<!-- 								<input  type="text" name="params.TwoName" id="TwoName"/> -->
<!-- 								&nbsp;质量特性总称 -->
<!-- 								<input style="width:145px;"  type="text" name="params.ThreeName" id="ThreeName"/> -->
<!-- 							</td> -->
<!-- 						</tr> -->
					</table>
				</div>
				<div>
					<table style="width:99.7%;">
						<tr>
							<td>
								<grid:jqGrid gridId="exMessages" url="${spcctx}/process-monitor/list-datas.htm" code="SPC_ABNORMAL_INFO" pageName="page"></grid:jqGrid>
							</td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div>
</body>
</html>