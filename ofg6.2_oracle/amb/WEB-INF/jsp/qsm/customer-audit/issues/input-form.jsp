<%@page import="sun.java2d.pipe.SpanShapeRenderer.Simple"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar calendar = Calendar.getInstance();
	String dateStr = sdf.format(calendar.getTime());
	String user = ContextUtils.getUserName();
	
%> 
	<table class="form-table-without-border" style="width:100%;margin: auto;">
		<caption><h2>客户审核改善报告</h2></caption>
		<input type="hidden" id="currentActivityName" name="currentActivityName" value="${currentActivityName }" />
		<input  type="hidden" id="id" name="id" value="${id}" />
		<input type="hidden" id=formNo name="formNo" value="${formNo}" />
<%-- 	<caption style="text-align:left;padding-bottom:4px;">表单.NO:${formNo}</caption> --%>
		<caption style="text-align:right;padding-bottom:4px;">管理编号:<input id="manageNo" name="manageNo" style="width:120px;"value="${manageNo}" /></caption>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<tr>
			<td style="width:10%;">厂区</td>
			<td style="width:200px;">
				<s:select list="enterpriseGroups"
					listKey="value" 
					listValue="value" 
					name="enterpriseGroup" 
					id="enterpriseGroup"
					emptyOption="true" 
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td style="width:12%;">审核类型</td>
			<td style="width:200px;">
				<s:select list="auditTypes"
					listKey="value" 
					listValue="name" 
					name="auditType" 
					id="auditType" 
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td style="width:10%;">审核日期</td>
			<td><input  type="text" isDate="true" id="auditDate" name="auditDate" value="<s:date name='auditDate' format="yyyy-MM-dd"/>" /></td>
			<td></td>
			<td><input id="customerId" name="customerId" value="${customerId }"/></td>
		</tr>
		<tr>
			<td>客户</td>
			<td><input id="customer" name="customer" value="${customer }"/></td>
			<td>问题点类型</td>
			<td style="width:200px;">
				<s:select list="issuesTypes"
					listKey="value" 
					listValue="name" 
					name="issuesType" 
					id="issuesType" 
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td>问题严重度</td>
			<td style="width:200px;">
				<s:select list="degrees"
					listKey="value" 
					listValue="name" 
					name="degree" 
					id="degree" 
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td>责任部门</td>
			<td><input id="department" isDept="true" name="department" style="float: left;" value="${department }"/></td>
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="8" >问题描述</td>
		</tr>	
		<tr>
			<td colspan="8"><textarea id="problemDescrible" name="problemDescrible" rows="5">${problemDescrible }</textarea></td>
		</tr>
		<tr>
			<td>问题提交人</td>
			<td>
				<input type="text" id="consignor" isTemp="true" isUser="true"  hiddenInputId="consignorLogin" style="float: left;"  name="consignor" value="${consignor}" />
				<input type="hidden" id="consignorLogin" name="consignorLogin" value="${consignorLogin}" />
			</td>
			<td>问题提交时间</td>
			<td><input  type="text" isDate="true" id="consignableDate" name="consignableDate" value="<s:date name='consignableDate' format="yyyy-MM-dd"/>" />
			<td>问题描述附件</td>
			<td><input type="hidden"  isFile="true" id="problemFile" name="problemFile" value="${problemFile}"/></td>
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="8">原因分析</td>
		</tr>	
		<tr>
			<td colspan="8"><textarea id="reasonAnalysis" name="reasonAnalysis" rows="5">${reasonAnalysis }</textarea></td>
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="8">改善对策</td>
		</tr>	
		<tr>
			<td colspan="8"><textarea id="improveMeasures" name="improveMeasures" rows="5">${improveMeasures }</textarea></td>
		</tr>
		<tr>
			<td>改善佐证</td>
			<td><input type="hidden"  isFile="true" id="improveFile" name="improveFile" value="${improveFile}"/></td>
			<td>计划完成时间</td>
			<td><input  type="text" isDate="true" id="compliteDate" name="compliteDate" value="<s:date name='compliteDate' format="yyyy-MM-dd"/>" /></td>
			<td>责任人</td>
			<td>
				<input type="text" id="dutyMan" isTemp="true" isUser="true"  hiddenInputId="dutyManLogin" style="float: left;"  name="dutyMan" value="${dutyMan}" />
				<input type="hidden" id="dutyManLogin" name="dutyManLogin" value="${dutyManLogin}" />
			</td>
			<td>改善提交时间</td>
			<td><input  type="text" isDate="true" id="improveDate" name="improveDate" value="<s:date name='improveDate' format="yyyy-MM-dd"/>" /></td>
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="8">原因分析及改善对策审核意见</td>
		</tr>
		<tr>
			<td colspan="8"><textarea id="auditingOpinion" name="auditingOpinion" rows="5">${auditingOpinion }</textarea></td>
		</tr>
		<tr>
			<td colspan="2">责任人主管</td>
			<td colspan="2">
				<input type="text" id="dutyManLead" isTemp="true" isUser="true"  hiddenInputId="dutyManLeadLogin" style="float: left;"  name="dutyManLead" value="${dutyManLead}" />
				<input type="hidden" id="dutyManLeadLogin" name="dutyManLeadLogin" value="${dutyManLeadLogin}" />
			</td>
			<td >审核时间</td>	
			<td colspan="4"><input  type="text" isDate="true" id="auditorDate" name="auditorDate" value="<s:date name='auditorDate' format="yyyy-MM-dd"/>" /></td>
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="8">问题改善确认</td>
		</tr>
		<tr>
			<td colspan="8"><textarea id="improveEffect" name="improveEffect" rows="5">${improveEffect }</textarea></td>
		</tr>
		<tr>
			<td>关闭状态</td>
			<td style="width:200px;">
				<s:select list="closeStates"
					listKey="value" 
					listValue="name" 
					name="closeState" 
					id="closeState" 
					emptyOption="true" 
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td>实际完成时间</td>
			<td><input  type="text" isDate="true" id="actuallyDate" name="actuallyDate" value="<s:date name='actuallyDate' format="yyyy-MM-dd"/>" /></td>	
			<td>备注</td>
			<td colspan="3"><textarea id="remark" name="remark">${remark }</textarea></td>
		</tr>
		<tr>
			<td colspan="2">确认人</td>
			<td><input type="text" id="authorizer" isTemp="true" isUser="true"  hiddenInputId="authorizerLogin" style="float: left;"  name="authorizer" value="${authorizer}" />
			<input type="hidden" id="authorizerLogin" name="authorizerLogin" value="${authorizerLogin}" /></td>	
			<td >确认时间</td>	
			<td colspan="4"><input  type="text" isDate="true" id="authorizerDate" name="authorizerDate" value="<s:date name='authorizerDate' format="yyyy-MM-dd"/>" /></td>	
		</tr>
	</table>