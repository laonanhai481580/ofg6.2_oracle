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
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<caption style="font-size: 24px;">IPQC稽核问题点改善报告</caption>
			<tr>
		<th colspan="4" style="text-align:right;padding-bottom:4px;">
			编号:${formNo}
			<input type="hidden" name="formNo" id="formNo" value="${formNo}" />
			<input type="hidden" name="auditId" id="auditId" value="${auditId}" />
		</th>
	</tr>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<tr>	
			<td style="width:160px;text-align: center;">厂区</td>
			<td>
				<s:select list="businessUnits"
						theme="simple"
						listKey="value" 
						listValue="name" 
						name="businessUnitName" 
						id="businessUnitName" 
						emptyOption="true">
					</s:select>	
			</td>				
			<td style="width:160px;text-align: center;">工厂</td>
			<td>
				<s:select list="factorys"
						theme="simple"
						listKey="value" 
						listValue="name" 
						name="factory" 
						id="factory" 
						emptyOption="true"
						onchange="factoryChange(this);">
					</s:select>	
			</td>		
			<td style="width:160px;text-align: center;">工序</td>
			<td style="width:200px;">
				<s:select list="procedures" 
				  listKey="value" 
				  listValue="name"
				  theme="simple"
				  emptyOption="true"
				  id="station"
				  name="station"
				 ></s:select>
			</td>		
		</tr>		
		<tr>
			<td style="width:160px;text-align:center;">稽核日期</td>
			<td style="width:200px;">
				<input   type="text" isDate="true" id="auditDate" name="auditDate" value="<s:date name='auditDate' format="yyyy-MM-dd"/>" />
			</td>
			<td style="width:160px;text-align:center;">班次</td>
			<td style="width:200px;">
				<s:select list="classGroups" 
				  listKey="value" 
				  listValue="name"
				  theme="simple"
				  emptyOption="true"
				  id="classGroup"
				  name="classGroup"
				 ></s:select>				
			</td>
			<td style="width:160px;text-align:center;">责任单位</td>
			<td style="width:200px;">
				<input   type="text"name="department" id="department" value="${department}" isDept="true" style="float: left;"></input>
			</td>	
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">稽核员</td>
			<td style="width:200px;">
				<input type="text" id="operator" isTemp="true" isUser="true" hiddenInputId="operatorLogin" style="float: left;"  name="operator" value="${operator}" />
				<input type="hidden" id="operatorLogin" name="operatorLogin" value="${operatorLogin}" />
			</td>		
			<td style="width:160px;text-align:center;">作业员</td>
			<td style="width:200px;">
				<input   type="text" name="worker" id="worker" value="${worker}" ></input>
			</td>
			<td style="width:160px;text-align:center; ">稽核类别</td>
			<td style="width:200px;">
				<s:select list="auditTypes" 
				  listKey="value" 
				  listValue="name"
				  theme="simple"
				  emptyOption="true"
				  id="auditType"
				  name="auditType"
				 ></s:select>				
			</td>		
		</tr>
		<tr>			
			<td style="width:160px;text-align: center;">问题严重度</td>
			<td>
				<s:select list="problemDegrees"
						theme="simple"
						listKey="value" 
						listValue="name" 
						name="problemDegree" 
						id="problemDegree" 
						emptyOption="true"	>
					</s:select>	
			</td>				
			<td style="width:160px;text-align:center; ">预计完成日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="planDate" name="planDate" value="<s:date name='planDate' format="yyyy-MM-dd"/>" />
			</td>
			<td style="width:160px;text-align:center; "></td>
			<td style="width:200px;">
			</td>
		</tr>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">		
		<tr>
			<td style="width:160px;text-align:center">问题描述</td>
			<td colspan="7">
				<textarea rows="2"   id="problemDescribe" name="problemDescribe"  >${problemDescribe}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center" rowspan="2">原因分析</td>
			<td colspan="7">
				<textarea rows="2"   id="reasonAnalysis" name="reasonAnalysis"  >${reasonAnalysis}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:200px;">
			</td>
			<td style="width:160px;text-align:center; "></td>
			<td style="width:200px;">
			</td>
			<td style="width:160px;text-align:center" >责任人</td>
			<td style="width:200px;">
				<input type="text" id="reasontMan" isTemp="true" isUser="true" hiddenInputId="reasontManLogin" style="float: left;"  name="reasontMan" value="${reasontMan}" />
				<input type="hidden" id="reasontManLogin" name="reasontManLogin" value="${reasontManLogin}" />				
			</td>
			<td style="width:160px;text-align:center; ">分析时间</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="reasontDate" name="reasontDate" value="<s:date name='reasontDate' format="yyyy-MM-dd"/>" />
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center" rowspan="2">改善对策</td>
			<td colspan="7">
				<textarea rows="2"   id="improveMeasures" name="improveMeasures"  >${improveMeasures}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:200px;" colspan="3">
				<input type="hidden"  isFile="true" id="improveFile" name="improveFile" value="${improveFile}"/>
			</td>
			<td style="width:160px;text-align:center" >责任人</td>
			<td style="width:200px;">
				<input type="text" id="departmentMan" isTemp="true" isUser="true" hiddenInputId="departmentManLogin" style="float: left;"  name="departmentMan" value="${departmentMan}" />
				<input type="hidden" id="departmentManLogin" name="departmentManLogin" value="${departmentManLogin}" />	
				<input type="hidden" id="isDuice" name="isDuice" value="${isDuice}" />				
			</td>
			<td style="width:160px;text-align:center; ">分析时间</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="departmentDate" name="departmentDate" value="<s:date name='departmentDate' format="yyyy-MM-dd"/>" />
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center" rowspan="2">确认</td>
			<td colspan="7">
				<textarea rows="2"   id="comfirmComment" name="comfirmComment"  >${comfirmComment}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:200px;">
			</td>
			<td style="width:160px;text-align:center; "></td>
			<td style="width:200px;">
			</td>
			<td style="width:160px;text-align:center" >核准人</td>
			<td style="width:200px;">
				<input type="text" id="approveMan" isTemp="true" isUser="true" hiddenInputId="approveManLogin" style="float: left;"  name="approveMan" value="${approveMan}" />
				<input type="hidden" id="approveManLogin" name="approveManLogin" value="${approveManLogin}" />				
			</td>
			<td style="width:160px;text-align:center; ">结案时间</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="jieanDate" name="jieanDate" value="<s:date name='jieanDate' format="yyyy-MM-dd"/>" />
			</td>
		</tr>		
	</table>