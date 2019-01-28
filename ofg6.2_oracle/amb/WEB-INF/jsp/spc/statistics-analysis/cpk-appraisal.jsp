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
<link href="${ctx}/widgets/multiselect/jquery.multiselect.css" rel="stylesheet" type="text/css"/>
<script src="${ctx}/widgets/multiselect/jquery.multiselect.js" type="text/javascript"></script>
<script src="${ctx}/widgets/multiselect/jquery.multiselect.zh-cn.js" type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
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
	$(document).ready(function(){
		$("#datepicker1").datepicker({changeMonth:true,changeYear:true});
		$("#datepicker2").datepicker({changeMonth:true,changeYear:true});
		$("li[layerLi]").show();
		//$("#_qualityFeature").multiselect({selectedList:2});
	});

	function search(){
		var url = '${spcctx }/statistics-analysis/cpk-appraisal-table.htm?nowtime=' + (new Date()).getTime();
		var params = getParams();
		if(!params['params.qualityFeatures']){
			$.showMessage("请选择质量特性","custom");
			return;
		}else{
			$.showMessage("正在统计,请稍候... ...","custom");
			$("#cpkAppraisalTable").load(url,params,function(){
				$.clearMessage();
			});
		}
	}
	
	function getParams(){
		var params = {};
		$(":input","#customerSearchDiv").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&obj.type=='select-one'){
				if(obj.id&&!obj.disabled){
					var values = $("#" + obj.id).multiselect("getChecked").map(function(){
					   return this.value;	
					}).get();
					if(values){
						var val = values.toString();
						if(val){
							params[obj.name] = val;
						}
					}else{
						if(jObj.val()){
							params[obj.name] = jObj.val();
						}
					}
				}
			}else if(obj.type == 'radio'){
				if(obj.checked){
					params[obj.name] = jObj.val();
				}
			}else if(obj.name&&jObj.val()&&jObj.val()!=""){
					params[obj.name] = jObj.val();
			}
		});
		return params;
	}
	//选择质量特性
	function selectFeature(obj){
		$.colorbox({/* href:"${spcctx}/common/feature-bom-select.htm", */
			href:"${spcctx}/common/feature-bom-multi-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
			overlayClose:false,
			title:"选择质量特性"
		});
	}
	
	function setFeatureValue(datas){
		/* $("#featureName").val(datas[0].value);
		$("#_qualityFeature").val(datas[0].id); */
		var ids="",values="";
		for ( var data in datas) {
			if(datas.length > 1){
				values="当前已选择"+(parseInt(data)+1)+"项质量特性!";
			}else{
				values += datas[data].value;
			}
			if(data == (datas.length-1)){
				ids += datas[data].id;
				//values += datas[data].value;
				break;
			}
			ids += datas[data].id+",";
			//values += datas[data].value+",";
		}
		$("#featureName").val(values);
		$("#_qualityFeature").val(ids);
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="stat_analyse";
		var thirdMenu="_cpk_table";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/spc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/spc-statistics-analysis-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center" style="overflow: auto;">
		<div class="opt-body" style="overflow: auto;">
			<aa:zone name = "main">
				<form id="contentForm" name="contentForm" method="post" action="">
					<div class="opt-btn">
						<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
						<span style="font-weight: bold;color:red;" id="message"><s:actionmessage theme="mytheme" /></span>
<%-- 						<div id="message" style="color:red;"><s:actionmessage theme="mytheme" /></div>	 --%>
					</div>
					<div id="customerSearchDiv" style="display:block;padding:4px;">						
						<table class="form-table-outside-border"  style="width:100%;padding:4px;">
							<tr>
								<td style="padding-left:6px;padding-bottom:4px;">
									<ul id="searchUl">
							 		<li>
							 			<span class="label">检验日期</span>
							 			<input id="datepicker1" type="text" readonly="readonly" style="width:68px;" value="<%=startDateStr%>" name="params.startDate_ge_date"/>
							 			至
							 			<input id="datepicker2" type="text" readonly="readonly" style="width:68px;" value="<%=endDateStr%>" name="params.endDate_le_date"/>
							 		</li>
							 		<li>
							 			<span class="label">质量特性</span>
										<input style="border:1px solid blue;color: black;font-weight:bold" name="params.featureName" id="featureName" value="${qualityFeatures[0].name}" onclick="selectFeature(this);" readonly="readonly"/>
										<input name="params.qualityFeatures" id="_qualityFeature" value="${qualityFeatures[0].value}" class="targerSelect" type="hidden"/>
							 		</li>
							 		<jsp:include page="search-layer.jsp" />
							 	</ul>
							 </td>
		 				 </tr>
						</table>	
					</div>
				</form>
			</aa:zone>
			<div id="cpkAppraisalTable" style="overflow: auto;">
			<%@ include file="cpk-appraisal-table.jsp" %>
			</div>
		</div>
	</div>
</body>
</html>