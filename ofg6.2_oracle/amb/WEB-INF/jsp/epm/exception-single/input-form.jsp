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
		<tr>
			<td style="width:15%;">客户</td>
			<td ><input id="customerNo" name="customerNo" value="${customerNo }"/></td>
			<td colspan="2" style="width:18%;">机种</td>
			<td style="width:20%;"><input id="productNo" name="productNo" value="${productNo }"/></td>
			<td style="width:15%;">样品类别</td>
			<td ><input id="sampleType" name="sampleType" value="${sampleType }"/></td>
		</tr>
		<tr>
			<td>供应商</td>
			<td><input id="supplier" name="supplier" value="${supplier }"/></td>
			<td colspan="2">送测人</td>
			<td><input id="testSend" name="testSend" hiddenInputId="testSendLogin" style="float: left;" isUser="true" value="${testSend }"/>
			<input type="hidden" id="testSendLogin" name="testSendLogin" value="${testSendLogin }"></td>
			<td>送测部门</td>
			<td><input id="testDepartment" name="testDepartment" value="${testDepartment }"/></td>
		</tr>
		<tr>
			<td>产品阶段</td>
			<td><input id="productStage" name="productStage" value="${productStage }"/></td>
			<td colspan="2">规格型号</td>
			<td><input id="model" name="model" value="${model }"/></td>
			<td>批号</td>
			<td><input id="lotNo" name="lotNo" value="${lotNo }"/></td>
		</tr>
		<tr>
			<td>样品数量</td>
			<td><input id="quantity" name="quantity" value="${quantity }"/></td>
			<td colspan="2">严重度</td>
			<td>
			<s:select list="severitys"
					listKey="value" 
					listValue="value" 
					name="severity" 
					id="severity"
					emptyOption="true" 
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td>不良率</td>
			<td><input id="defection" name="defection" value="${defection }"/></td>
		</tr>
		<tr>
			<td>不良来源厂区</td>
			<td><input id="source" name="source" value="${source }"/></td>
			<td colspan="2">委托测试单号</td>
			<td>
				<input id="itemNo" value="${itemNo}" name="itemNo" type="hidden"></input>
				<input name="itemId" id="itemId" value="${itemId}" type="hidden" />
				<a title="查看检验单详情" href="javascript:void(0);exceptionInput('${itemId}','${itemNo}')">${itemNo}</a>
			</td>
			<td>测试员</td>
			<td><input id="testEngineer" name="testEngineer" hiddenInputId="testEngineerLogin" value="${testEngineer }"/>
			<input type="hidden" id="testEngineerLogin" name="testEngineerLogin" value="${testEngineerLogin }"></td>
		</tr>
		<tr>
			<td>日期</td>
			<td><input id="submitDate" name="submitDate" isDate="true" value="<s:date name='submitDate' format="yyyy-MM-dd"/>" /></td>
			<td colspan="2">管理编号</td>
			<td><input id="managerAssets" name="managerAssets" value="${managerAssets }"/></td>
			<td >不良数</td>
			<td><input id="defectNumber" name="defectNumber" value="${defectNumber }"/>
				<input type="hidden" id="entrustId" name="entrustId" value="${entrustId }">
			</td>
		</tr>
		<tr>
			<td colspan="7" >异常描述</td>
		</tr>
		<tr>
			<td colspan="5"><textarea id="exceptionDescr" name="exceptionDescr">${exceptionDescr }</textarea></td>
			<td>附件</td>
			<td><input type="hidden" isFile="true" name="exceptionFile" id="exceptionFile" value="${exceptionFile }"></td>
		</tr>
		<tr>
			<td >问题改善人</td>
			<td colspan="6"><input id="laboratory" name="laboratory" hiddenInputId="laboratoryLogin" style="float: left;" isUser="true"  value="${laboratory }"/>
			<input type="hidden" id="laboratoryLogin" name="laboratoryLogin" value="${laboratoryLogin }"></td>
		</tr>
		
		<tr>
			<td rowspan="2">临时对策</td>
			<td colspan="6"><textarea id="temporaryDisposal" name="temporaryDisposal">${temporaryDisposal }</textarea></td>
		</tr>
		<tr>
			<td>附件</td>
			<td colspan="2"><input type="hidden" isFile="true" name="temporaryFile" id="temporaryFile" value="${temporaryFile }"></td>
			<td colspan="3"></td>
		</tr>
		
		<tr>
			<td rowspan="4">原因分析</td>
			<td colspan="6"><textarea id="reasonAnalysis" name="reasonAnalysis">${reasonAnalysis }</textarea></td>
		</tr>
		<tr>
			<td>附件</td>
			<td colspan="2"><input type="hidden" isFile="true" name="rootCausesFile" id="rootCausesFile" value="${rootCausesFile }"></td>
			<td colspan="3"></td>
		</tr>
		<tr>
			<td>要求完成时间</td>
			<td colspan="2"><input id="completionTime" name="completionTime" isDate="true" value="<s:date name='completionTime' format="yyyy-MM-dd"/>" /></td>
			<td>实际完成时间</td>
			<td colspan="2"><input id="finishTime" name="finishTime" isDate="true" value="<s:date name='finishTime' format="yyyy-MM-dd"/>" /></td>
		</tr>
		<tr>
			<td >原因分析人</td>
			<td colspan="5"><input id="rootCauses" name="rootCauses" hiddenInputId="rootCausesLogin" style="float: left;" isUser="true"  value="${rootCauses }"/>
			<input type="hidden" id="rootCausesLogin" name="rootCausesLogin" value="${rootCausesLogin }"></td>
		</tr>
		<tr>
			<td >QE确认</td>
			<td colspan="6"><input id="temporary" name="temporary" hiddenInputId="temporaryLogin" style="float: left;" isUser="true"  value="${temporary }"/>
			<input type="hidden" id="temporaryLogin" name="temporaryLogin" value="${temporaryLogin }"></td>
		</tr>
		<tr>
			<td rowspan="4">改善对策</td>
			<td colspan="6"><textarea id="improvement" name="improvement">${improvement }</textarea></td>
		</tr>
		<tr>
			<td>附件</td>
			<td colspan="2"><input type="hidden" isFile="true" name="improveFile" id="improveFile" value="${improveFile }"></td>
			<td colspan="3"></td>
		</tr>
		<tr>
			<td>要求完成时间</td>
			<td colspan="2"><input id="completionDate" name="completionDate" isDate="true" value="<s:date name='completionDate' format="yyyy-MM-dd"/>" /></td>
			<td>实际完成时间</td>
			<td colspan="2"><input id="finishTimeDate" name="finishTimeDate" isDate="true" value="<s:date name='finishTimeDate' format="yyyy-MM-dd"/>" /></td>
		</tr>
		<tr>
			<td >改善对策人</td>
			<td colspan="5"><input id="improvePerson" name="improvePerson" hiddenInputId="improvePersonLogin" style="float: left;" isUser="true"  value="${improvePerson }"/>
			<input type="hidden" id="improvePersonLogin" name="improvePersonLogin" value="${improvePersonLogin }"></td>
		</tr>
		<tr>
			<td rowspan="2">问题确认</td>
			<td colspan="6"><textarea id="resultsText" name="resultsText">${resultsText }</textarea></td>
		</tr>
		<tr>
			<td >问题确认人</td>
			<td colspan="6"><input id="resultsPeople" name="resultsPeople" hiddenInputId="resultsPeopleLogin" style="float: left;" isUser="true"  value="${resultsPeople }"/>
			<input type="hidden" id="resultsPeopleLogin" name="resultsPeopleLogin" value="${resultsPeopleLogin }"></td>
		</tr>
		<tr>
			<td >结案人</td>
			<td colspan="6"><input id="labPeople" name="labPeople" hiddenInputId="labPeopleLogin" style="float: left;" isUser="true"  value="${labPeople }"/>
			<input type="hidden" id="labPeopleLogin" name="labPeopleLogin" value="${labPeopleLogin }"></td>
		</tr>
		
	</table>