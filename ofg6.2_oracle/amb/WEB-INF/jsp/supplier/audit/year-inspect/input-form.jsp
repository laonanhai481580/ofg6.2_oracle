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
	<caption><h2>供应商稽核报告</h2></caption>
	<input type="hidden" id="currentActivityName" name="currentActivityName" value="${currentActivityName }"/>
	<input type="hidden" id="currentName" name="currentName" value="${currentName}"/>
	<input type="hidden" name="id" id="id" value="${id}" />
	<input type="hidden" id="inspectId" name="inspectId" value="${inspectId}" />
	<input type="hidden" id="creationYear" name="creationYear" value="${creationYear}" />
	<input type="hidden" id=formNo name="formNo" value="${formNo}" />
	<input name="taskId" id="taskId" value="${taskId}" type="hidden" />
	<caption style="text-align:right;padding-bottom:4px;">表单.NO:${formNo}</caption>
	<input type="hidden" name="year" id="year" value="${year}" />
	<input  type="hidden" name="checkDeptMansLog1" id="checkDeptMansLog1" value="${checkDeptMansLog1 }"/>
</table>
<table class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
		<tr>
	  		<td colspan='6' style="text-align:center;"><h3>发起审核</h3></td>
	  	</tr>
	    <tr>
	      <td style="text-align:center;">供应商名称</td>
	      <td style="width:20%;">
	          <input name="supplierName" id="supplierName" value="${supplierName}" hiddenInputId="supplierLoginName" onclick="supplierClick();" readonly="readonly"/>
	          <input  name="supplierLoginName" id="supplierLoginName" value="${supplierLoginName }" type="hidden"/>
	      </td>
	      <td style="text-align:center;">供货事业部</td>
	        <td style="width:20%;">
	          <input name="shareGroup" id="shareGroup" value="${shareGroup}" />
	      </td>
	      <td style="text-align:center;">供应厂区</td>
	      <td style="width:20%;">
	         <input  name="supplyFactory" id="supplyFactory" value="${supplyFactory}" />
	      </td>
	    </tr>
	    <tr>
	    <td style="text-align:center;">供应编码</td>
	      <td>
	         <input name="supplierCode" id="supplierCode" value="${supplierCode}" readonly="readonly"/>
	      </td>
	      <td style="text-align:center;">审核类型</td>
	      <td>
	      		<s:select list="supplierAuditorType" style="width:100px;"
					listKey="value" 
					listValue="value" 
					name="auditorType" 
					id="auditorType"
					emptyOption="true" 
					onchange="setAgainType(this)"
					theme="simple">
				</s:select>
	      </td>
	      <td style="text-align:center;">审核类别</td>
	        <td>
	        	<s:select list="auditSorts" style="width:100px;"
					listKey="value" 
					listValue="value" 
					name="auditSort" 
					id="auditSort"
					emptyOption="true" 
					onchange=""
					theme="simple">
				</s:select>
	      </td>
	    </tr>
	    <tr>
	    	<td style="text-align:center;">供应物料</td>
	      <td>
	         <input name="supplyMaterial" id="supplyMaterial" value="${supplyMaterial}" />
	      </td>
	    	<td style="text-align:center;">发起人</td>
	    	<td>
	       		<input  name="initiatorPerson" id="initiatorPerson" value="${initiatorPerson }" style="float:left;" hiddenInputId="initiatorPersonLogin" isUser="true"/>
	        	<input  name="initiatorPersonLogin" id="initiatorPersonLogin" value="${initiatorPersonLogin }" type="hidden"/>
	       	</td>
	    	<td style="text-align:center;">审核小组成员</td>
	      	<td>
<%-- 	         <input name="supplyMaterial" id="supplyMaterial" value="${supplyMaterial}" readonly="readonly"/> --%>
	      			<input style="float:left;width:200px" name="checkPerson" id="checkPerson" value="${checkPerson}" />
  	 				<input style="float:left;" type='hidden' name="checkPersonLogin" id="checkPersonLogin" value="${checkPersonLogin}" />
   					<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="selectPerson1('checkPerson','checkPersonLogin');"><span class="ui-icon ui-icon-search"  ></span></a>
   					<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('checkPerson','checkPersonLogin')" href="javascript:void(0);" title="<s:text name='清空'/>">
   					<span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
	      	</td>
	   
	    </tr>
	    <tr>
	  		<td colspan='6' style="text-align:center;"><h3>供应商自评</h3></td>
	  	</tr>
	    <tr>
	    	<td style="text-align:center;">自评日期</td>
	    	<td>
	    		<input isDate="true" name="selfAssessmentDate" id="selfAssessmentDate" value="<s:date name='selfAssessmentDate' format="yyyy-MM-dd"/>"/>
	    	</td>
	    	<td style="text-align:center;">自评结果</td>
	    	<td>
	    		<s:select list="checkResults" style="width:100px;"
					listKey="value" 
					listValue="value" 
					name="selfAssessmentResult" 
					id="selfAssessmentResult"
					emptyOption="true" 
					onchange=""
					theme="simple">
				</s:select>
	    	</td>
	    		<td style="text-align:center;">自评表</td>
	    	<td colspan=''>
	    		<input type="hidden" isFile=true name="selfAssessmentFile" id="selfAssessmentFile" value='${selfAssessmentFile}'/>
	    	</td>
	    	
	    </tr>
	    <tr>
	    	<td style="text-align:center;">自评分数</td>
	    	<td>
	    		<input name="supplierGrade" id="supplierGrade" value="${supplierGrade}" />
	    	</td>
	    	<td style="text-align:center;">AEO</td>
	    	<td>
	    		<input name="aeo" id="aeo" value="${aeo}" />
	    	</td>
	    	<td style="text-align:center;">供应商邮箱<a style="font-family:verdana;color:red;font-size:10px;">(*多个邮箱请用;隔开)</a></td>
	      	<td><textarea id="supplierEmail" name="supplierEmail">${supplierEmail }</textarea></td>
	    </tr>
	    <security:authorize ifAnyGranted="supplier-improve-conceal">
	    <tr first=true>
	        <td colspan='6' style="text-align:center;"><h3>首次</h3></td>
	    </tr>
	    <tr first=true>
	        <td style="text-align:center;">首次计划日期</td>
	        <td>
	        	<input isDate="true" name="firstCheckPlanDate" id="firstCheckPlanDate" value="<s:date name='firstCheckPlanDate' format="yyyy-MM-dd"/>" />
	        </td>
	        <td style="text-align:center;">首次实际日期</td>
	        <td>
	        	<input isDate="true" name="firstCheckDesignDate" id="firstCheckDesignDate" value="<s:date name='firstCheckDesignDate' format="yyyy-MM-dd"/>"/>
	        </td>
	        <td style="text-align:center;">首次稽核结果</td>
	        <td>
	        	<s:select list="checkResults" style="width:100px;"
					listKey="value" 
					listValue="value" 
					name="firstCheckResult" 
					id="firstCheckResult"
					emptyOption="true" 
					onchange=""
					theme="simple">
				</s:select>
	        </td>
	    </tr>
	    <tr first=true>
	    	<td style="text-align:center;">首次评分</td>
	    	<td>
	    		<input name="firstGrade" id="firstGrade" value="${firstGrade}" />
	    	</td>
	    	<td style="text-align:center;">AEO</td>
	    	<td>
	    		<input name="firstAeo" id="firstAeo" value="${firstAeo}" />
	    	</td>
	    	<td style="text-align:center;">是否再次审核</td>
	       	<td>
				<input  type="radio" id="d1" name="againAudit" value="是" onclick="setAgainAudit(this)" <s:if test='%{againAudit=="是"}'>checked="true"</s:if> title="是"/><label for="d1">是</label>
				<input  type="radio" id="d2" name="againAudit" value="否" onclick="setAgainAudit(this)" <s:if test='%{againAudit=="否"}'>checked="true"</s:if> title="否"/><label for="d2">否</label>
			</td>
	    </tr>
	    <tr first=true>
	    	<td colspan='2'></td>
	    	<td style="text-align:center;">首次审核人员</td>
	    	<td>
	    		<input  name="firstPerson" id="firstPerson" value="${firstPerson }" style="float:left;" hiddenInputId="firstPersonLogin" isUser="true"/>
	        	<input  name="firstPersonLogin" id="firstPersonLogin" value="${firstPersonLogin }" type="hidden"/>
	    	</td>
	    	<td style="text-align:center;">办理日期</td>
	    	<td>
	    		<input isDate="true" name="firstOperationDate" id="firstOperationDate" value="<s:date name='firstOperationDate' format="yyyy-MM-dd"/>"/>
	    	</td>
	    </tr>
	    <tr first=true>
	       <td style="text-align:center;">首次稽核报告</td>
	       <td colspan='5'>
	       	<input type="hidden" isFile=true name="firstCheckFile" id="firstCheckFile" value='${firstCheckFile}'/>
	       </td>
	    </tr>
	    <tr>
	    </tr>
	     <tr twice=true>
	        <td colspan='6' style="text-align:center;"><h3>二次</h3></td>
	    </tr>
	     <tr twice=true>
	        <td style="text-align:center;">二次计划日期</td>
	        <td>
	        	<input isDate="true" name="secondCheckPlanDate" id="secondCheckPlanDate" value="<s:date name='secondCheckPlanDate' format="yyyy-MM-dd"/>" />
	        </td>
	        <td style="text-align:center;">二次实际日期</td>
	        <td>
	        	<input isDate="true" name="secondCheckDesignDate" id="secondCheckDesignDate" value="<s:date name='secondCheckDesignDate' format="yyyy-MM-dd"/>" />
	        </td>
	        	<td style="text-align:center;">二次稽核结果</td>
	       <td>
	       	<s:select list="checkResults" style="width:100px;"
					listKey="value" 
					listValue="value" 
					name="secondCheckResult" 
					id="secondCheckResult"
					emptyOption="true" 
					onchange=""
					theme="simple">
				</s:select>
	       </td>
	    </tr>
	    <tr twice=true>
	    	<td style="text-align:center;">二次评分</td>
	    	<td>
	    		<input name="secondrade" id="secondrade" value="${secondrade}" />
	    	</td>
	    	<td style="text-align:center;">AEO</td>
	    	<td>
	    		<input name="secondAeo" id="secondAeo" value="${secondAeo}" />
	    	</td>
	    </tr>
	    <tr twice=true>
	    	<td colspan='2'></td>
	    	<td style="text-align:center;">二次审核人员</td>
	    	<td>
	    		<input  name="secondPerson" id="secondPerson" value="${secondPerson }" style="float:left;" hiddenInputId="secondPersonLogin" isUser="true"/>
	        	<input  name="secondPersonLogin" id="secondPersonLogin" value="${secondPersonLogin }"  type="hidden"/>
	    	</td>
	    	<td style="text-align:center;">办理日期</td>
	    	<td>
	    		<input isDate="true" name="secondOperationDate" id="secondOperationDate" value="<s:date name='secondOperationDate' format="yyyy-MM-dd"/>" />
	    	</td>
	    </tr>
	     <tr twice=true>
	       <td style="text-align:center;">二次稽核报告</td>
	       <td colspan='5'> 
	       	<input type="hidden" isFile=true name="secondCheckFile" id="secondCheckFile" value='${secondCheckFile}'></input>
	       </td>
	    </tr>
	   	</security:authorize>
	  	<tr>
	  		<td colspan='6' style="text-align:center;"><h3>不符合项跟进表</h3></td>
	  	</tr>
	    <tr>
	    	<td style="text-align:center;">不符合项跟进表</td>
	    	<td colspan='3'>
	    		<input type="hidden" isFile=true name="improveReportFile" id="improveReportFile" value='${improveReportFile}'/>
	    	</td>
	    	<td style="text-align:center;">改善交期</td>
	    	<td>
	    		<input isDate="true" name="ncrDate" id="ncrDate" value="<s:date name='ncrDate' format="yyyy-MM-dd"/>" />
	    	</td>
	    </tr>	   
	    <tr>
	  		<td colspan='6' style="text-align:center;"><h3>不符合项改善效果确认</h3></td>
	  	</tr>
	    <tr>
			<td style="text-align:center;">审核人</td> 
	       	<td> 
 	       		<input  name="affirmPerson" id="affirmPerson" value="${affirmPerson }" style="float:left;" hiddenInputId="affirmPersonLogin" isUser="true"/> 
 	        	<input  name="affirmPersonLogin" id="affirmPersonLogin" value="${affirmPersonLogin }" type="hidden"/>
	       	</td>
            <td colspan="3">
				<textarea style="width: 98%;" id="countersignSQE" name="countersignSQE">${countersignSQE}</textarea>
			</td>
			<td>
	       		<s:select list="supplierIssueType" style="width:100px;"
					listKey="value" 
					listValue="value" 
					name="closeStateSQE" 
					id="closeStateSQE"
					emptyOption="true" 
					onchange=""
					theme="simple">
				</s:select>
	       	</td>	       		       
	    </tr>
 		<security:authorize ifAnyGranted="supplier-improve-conceal">	    
	    <tr>
	  		<td colspan='6' style="text-align:center;"><h3>最终稽核结果</h3></td>
	  	</tr>
	    <tr>
	       	<td style="text-align:center;">审核人主管</td>
	       	<td>
	       		<input  name="auditorPerson" id="auditorPerson" value="${auditorPerson }" style="float:left;" hiddenInputId="auditorPersonLogin" isUser="true"/>
	        	<input  name="auditorPersonLogin" id="auditorPersonLogin" value="${auditorPersonLogin }" type="hidden"/>
	       	</td>
	       	<td style="text-align:center;">审核日期</td>
	       	<td><input  name="auditorDate" isDate="true" id="auditorDate" value="<s:date format='yyyy-mm-dd' name='auditorDate'/>" /></td>
	    	<td style="text-align:center;">关闭日期</td>
	       	<td><input  name="closeDate" isDate="true" id="closeDate" value="<s:date format='yyyy-mm-dd' name='closeDate'/>" /></td>
	    </tr>
	    <tr>
	    	<td style="text-align:center;">最终稽核结果</td>
	       	<td >
	       		<s:select list="checkResults" style="width:100px;"
					listKey="value" 
					listValue="value" 
					name="finalCheckResult" 
					id="finalCheckResult"
					emptyOption="true" 
					onchange=""
					theme="simple">
				</s:select>
	       	</td>
	       	<td style="text-align:center;">备注</td>
	       	<td colspan="3">
	    		<textarea name="auditorRemark" id="auditorRemark">${auditorRemark }</textarea>
	    	</td>
		</tr>
		</security:authorize>
</table>