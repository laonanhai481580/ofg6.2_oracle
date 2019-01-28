<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.spc.entity.AbnormalReason"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript">
	<%String isView = request.getParameter("isView");%>
	$(document).ready(function(){
		$("#tabs").tabs({});
		$("#dataTable").height($(window).height()-250);
		//计算一次
		caculateSigma();
		if('<%=isView%>'=='true'){
			$("#saveBtn").hide();
		}else{
			$("#dataTable").find(":input").removeAttr("disabled");
			$(":input[fieldName=DATA_VALUE]","#contentForm").bind("change",function(){
				caculateSigma();
			});
		}
	});
	
	function caculateSigma(){
		var val = 0,max=null,min=null;
		var count = 0;
		$(":input[fieldName=DATA_VALUE]","#contentForm").each(function(index,obj){
			count++;
			var value = parseFloat(obj.value);
			if(!isNaN(value)){
				val += value;
				
				if(max==null||value>max){
					max = value;
				}
				
				if(min==null||value<min){
					min = value;
				}
			}
		});
		if(count > 0){
			val = val/count;
		}
		//精度
		var precs = parseInt('${qualityFeature.precs}',10);
		if(isNaN(precs)){
			precs = 2;
		}
		$("#sigma").val(parseFloat(val.toFixed(precs)));
		var diff = 0;
		if(max != null){
			diff = max - min;
		}
		$("#maxValue").val(max);
		$("#minValue").val(min);
		$("#rangeDiff").val(parseFloat(diff.toFixed(precs)));
	}
	//获取表单的值
	function getParams(){
		var params = {};
		$(":input","#contentForm").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		//alert($.param(params));
		return params;
	}
	
	function submitForm(url){
		if(!$("#contentForm").valid()){
			var error = $("#contentForm").validate().errorList[0];
			var $obj = $(error.element);
			var message = error.message;
			var validateCustomMessage = $obj.attr("validateCustomMessage");
			if(validateCustomMessage){
				message = validateCustomMessage;
			}
			$.showMessage(message);
			return false;
		}
		var params = getParams();
		$.showMessage("正在保存,请稍候... ...","custom");
		$.post(url,params,function(result){
			if(result.error){
				$.showMessage(result.message);
			}else{
				$("#id").val(result);
				$.showMessage("保存成功!");
			}
		},'json');
	}	
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name = "main">
				<div class="opt-btn" id="btnDiv">
					<security:authorize ifAnyGranted="spc_data-acquisition_maintenance-update">
					<button class='btn' id="saveBtn" type="button" onclick="submitForm('${spcctx}/data-acquisition/maintenance-save.htm');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
					</security:authorize>
					<button class='btn' type="button" onclick="window.parent.$.colorbox.close();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
					<span style="color:red;" id="message"><s:actionmessage theme="mytheme"/></span>
				</div>
				<div id="opt-content">
				<form id="contentForm" method="post" action="" onsubmit="return false;">
					<input type="hidden" name="id" value="${spcSubGroup.id}"></input>
					<table style="width:100%;">
						<tr>
							<td style="width:20%;" align="left"><img src="${ctx}/images/stat/feature.png"/></td>
							<td style="width:80%;">
								<table style="width:100%;">
									<tr>
										<td align="right">质量特性:</td>
										<td colspan="3">
											<input name="featureName" style="width:88%;" value="${qualityFeature.name}" disabled="disabled"/>
											<input type="hidden" name="featureId" id="featureId" value="${qualityFeature.id }"/>
										</td>
									</tr>
									<tr>
										<td align="right">均值:</td>
										<td>
											<input name="sigma" id="sigma" value="" disabled="disabled"/>
										</td>
										<td align="right">极差:</td>
										<td><input name="rangeDiff" id="rangeDiff" value="" disabled="disabled"/></td>
									</tr>
									<tr>
										<td align="right">最大值:</td>
										<td>
											<input name="maxValue" id="maxValue" value="" disabled="disabled"/>
										</td>
										<td align="right">最小值:</td>
										<td><input name="minValue" id="minValue" value="" disabled="disabled"/></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<div id="tabs">
						<ul>
							<li><span><a href="#tabs1">子组信息</a></span></li>
						</ul>
						<div id="tabs1">
						<table class="form-table-border-left" style="width:100%;overflow-y:auto;" id="dataTable">
							<tr>
								<td style="width: 100%;vertical-align: top;">
									<table class="form-table-border-left">
										<thead>
											<tr style="height:25px;font-weight:bold;" class="ui-widget-content ui-state-default">
												<th style="width:10px;text-align: center;">序号</th>
												<th style="width:40px;text-align: center;">采集时间</th>
												<th style="width:20px;text-align: center;">采样值</th>
												<s:iterator value="featureLayers" var="layer">
												<th style="width:40px;text-align: center;">${detailName}</th>
												</s:iterator>
											</tr>
										</thead>
										<tbody>
											<%
												List<Map<String,Object>> dataMaps = (List<Map<String,Object>>)ActionContext.getContext().get("dataMaps");
												if(dataMaps==null){
													dataMaps = new ArrayList<Map<String,Object>>();
												}
												StringBuffer flags = new StringBuffer();
												int index = 0;
												for(Map<String,Object> dataMap : dataMaps){
													String preFix = "a"+index++;
													if(flags.length()>0){
														flags.append(",");
													}
													flags.append(preFix);
											%>
											<tr height="25">
												<td style="text-align:center;">
													X<%=index %>
													<input type="hidden" fieldName="ID" name="<%=preFix%>_ID" value="<%=dataMap.get("ID")%>"/>
												</td>
												<td style="text-align:center;">
													<%=dataMap.get("INSPECTION_DATE")%>
												</td>
												<td style="text-align:center;">
													<input type="text" fieldName="DATA_VALUE" name="<%=preFix%>_DATA_VALUE" value="<%=dataMap.get("DATA_VALUE")%>" class="{number:true,required:true}" style="width:98%;" disabled="">
												</td>
												<s:iterator value="featureLayers" var="layer">
												<%
													String detailCode = (String)ActionContext.getContext().getValueStack().findValue("detailCode");
													detailCode = detailCode==null?"":detailCode.toUpperCase();
													Object detailValue = dataMap.get(detailCode);
												%>
												<td style="text-align:center;">
													<input type="text" layerCode="${detailCode}" name="<%=preFix%>_${detailCode}" value="<%=detailValue==null?"":detailValue%>" style="width:98%;" disabled="">
												</td>
												</s:iterator>
											</tr>
											<%} %>
										</tbody>
									</table>
									<input type="hidden" name="flags" value="<%=flags %>"/>
								</td>
							</tr>
						</table>
						</div>
					</div>
				</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>