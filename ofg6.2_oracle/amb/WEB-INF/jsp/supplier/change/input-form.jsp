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
<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-tabl">
	<tbody>
	<caption><h2>PCN 申请单</h2></caption>
	<caption style="text-align:left;padding-bottom:4px;">表单.NO:${formNo}</caption>
	     <tr >
	         <td style="width:130px;text-align:center;">申请公司</td>
	         <td style="width:110px;"><input id="applicant" name="applicant" value="${applicant }"/>
	         	<input type="hidden" name="sourceUnit" id="sourceUnit" value="${sourceUnit}" />
	         	<input type="hidden" id="currentActivityName" name="currentActivityName" value="${currentActivityName }" />
				<input type="hidden" id="transactorName" name="transactorName" value="${transactorName }" />
	         </td>
	         <td style="width:60px;text-align:center;">申请日期</td>
	         <td style="width:150px;"><input id="applyingDate" name="applyingDate" isDate="true" value="<s:date name='applyingDate' format="yyyy-MM-dd"/>" /></td>
	     	<td  style="width:150px;text-align:center;">厂区</td>
	        <td style="width:160px;">
				<%-- <s:select list="businessUnits"
					listKey="value" 
					listValue="name" 
					name="businessUnit" 
					id="businessUnit" 
					cssStyle="width:140px;"
					onchange=""
					theme="simple">
				</s:select> --%>
				<s:checkboxlist
					theme="simple" list="businessUnits" listKey="value" listValue="name"
					name="businessUnit" value="#request.businessUnitList">
				</s:checkboxlist>
			</td>
	     </tr>
	    <tr>
	         <td style="text-align:center;">材料名</td>
	         <td><input id="materialName" name="materialName" value="${materialName}"/></td>
	         <td style="text-align:center;">物料编码</td>
	         <td><input id="materialCode" name="materialCode" value="${materialCode}"/></td>
	         <td style="text-align:center;">适用机型</td>
	         <td><input id="applyingProject" name="applyingProject" value="${applyingProject }"/></td>
	     </tr>	     
	     <tr>
	         <td style="text-align:center;">申请事由</td>
	         <td colspan="5" ><textarea rows="3" id="reason" style="width:98%;" name="reason">${reason }</textarea></td>
	     </tr>
	     <tr>
	         <td style="text-align:center;">变更项目</td>
	         <td colspan="3">
	              <s:select list="levels" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="projectChange"
				  id="projectChange"
				  emptyOption="true"
				  labelSeparator=""
				  onChange="findKey(this);"
				  cssStyle="width:500px;"
				  ></s:select>
	           </td>
	         <td style="text-align:center;">变更等级</td>
	         <td colspan="2">
	             <input id="changeLevel" readonly="readonly" name="changeLevel" value="${changeLevel }" />
	         </td>
	     </tr>
	     <tr>
	         <td colspan="1" rowspan="3" style="text-align:center;" >变更内容</td>
	         <td colspan="2" style="text-align:center;">变更前</td>
	         <td colspan="3" style="text-align:center;">变更后</td>
	     </tr>
	     <tr>
	         <td colspan="2">
	         <textarea  id="contentBefore" style="width:95%;" name="contentBefore">${contentBefore }</textarea>
			</td> 
	         <td colspan="3">
	         <textarea  id="contentAfter" style="width:95%;" name="contentAfter">${contentAfter }</textarea>
			</td>
	     </tr>
	      <tr>
	         <td colspan="2" style="text-align:left;"><input id="beforeFile" type="hidden" isFile="true" name="beforeFile" value="${beforeFile }"/></td>
	         <td colspan="3" style="text-align:left;"><input id="afterFile" type="hidden" isFile="true" name="afterFile" value="${afterFile }"/></td>
	     </tr>
	     <tr>
	     	<td colspan="1" style="text-align:center;">评估报告</td>
	     	<td colspan="5" style="text-align:left;"><input id="reportFile" type="hidden" isFile="true" name="reportFile" value="${reportFile }"/></td>
	     </tr>
	      <tr>
	         <td  rowspan="2" style="text-align:center;">SQE办理</td>
	         <td colspan="2"><textarea id="sqeOpinion" name="sqeOpinion">${sqeOpinion }</textarea></td>
	         <td  rowspan="2" style="text-align:center;">SQE审核</td>
	         <td colspan="2" ><textarea id="sqeOpinion1" name="sqeOpinion1">${sqeOpinion1 }</textarea></td>
	     </tr>
	     <tr>
	     	 <td style="text-align:center;">经办人</td>
	         <td ><input id="sqeProcesser" style="float:left;" hiddenInputId="sqeProcesserLog" isUser="true" name="sqeProcesser" value="${sqeProcesser }"/>
	               <input type="hidden" name="sqeProcesserLog" id="sqeProcesserLog"  value="${sqeProcesserLog}" />
	         </td>
	         <td style="text-align:center;">审核人</td>
	         <td ><input id="sqeChecker" style="float:left;" hiddenInputId="sqeCheckerLog" isUser="true" name="sqeChecker" value="${sqeChecker }"/>
	               <input type="hidden" name="sqeCheckerLog" id="sqeCheckerLog"  value="${sqeCheckerLog}" />
	         </td>
	     </tr>
	     <tr><td colspan="6" style="text-align:center;"><h3>会签<h3></td></tr>
		 <tr>
			 <td style="text-align:center;">会签部门</td>	
			 <td style="text-align:center;">会签人</td>
			 <td colspan="2" style="text-align:center;">签核意见</td>
			 <td colspan="2" style="text-align:center;">上传附件</td>
			 <input  type="hidden" name="checkDeptMansLog" id="checkDeptMansLog" value="${checkDeptMansLog }"/>
		 </tr>
		 <tr id="checkerLog">		 	
			<td style="text-align:center;">采购</td>	
			<td>
				<input style="float:left;" isAsign="true" hiddenInputId="procurementProcesserLog"  isUser="true" id="procurementProcesser" name="procurementProcesser"  value="${procurementProcesser}"/>
	        	<input type="hidden" class="isCheckerLog" name="procurementProcesserLog" id="procurementProcesserLog"  value="${procurementProcesserLog}" />
            </td>
			<td colspan="2">
				<textarea style="width: 98%;" stage="three" id="procurementOpinion" name="procurementOpinion">${procurementOpinion }</textarea>
			</td>
			<td colspan="2">
			</td>
		</tr>	   
		<tr id="checkerLog">		 	
			<td style="text-align:center;">PMC</td>	
			<td>
				<input style="float:left;" isAsign="true" hiddenInputId="qualityProcesserLog"  isUser="true" id="qualityProcesser" name="qualityProcesser"  value="${qualityProcesser}"/>
	        	<input type="hidden" class="isCheckerLog" name="qualityProcesserLog" id="qualityProcesserLog"  value="${qualityProcesserLog}" />
            </td>
			<td colspan="2">
				<textarea style="width: 98%;" stage="three" id="qualityOpinion" name="qualityOpinion">${qualityOpinion }</textarea>
			</td>
			<td colspan="2">
			</td>
		</tr>  
		 <tr id="checkerLog">		 	
			<td style="text-align:center;">工程</td>	
			<td>
				<input style="float:left;" isAsign="true" hiddenInputId="projectProcesserLog"  isUser="true" id="projectProcesser" name="projectProcesser"  value="${projectProcesser}"/>
	        	<input type="hidden" class="isCheckerLog" name="projectProcesserLog" id="projectProcesserLog"  value="${projectProcesserLog}" />
            </td>
			<td colspan="2">
				<textarea style="width: 98%;" stage="three" id="projectOpinion" name="projectOpinion">${projectOpinion }</textarea>
			</td>
			<td colspan="2">
				<input type="hidden" id="projectFile" isFile="true" name="projectFile" value="${projectFile }"/>
			</td>			
		</tr>	     
		 <tr id="checkerLog">		 	
			<td style="text-align:center;">研发</td>	
			<td>
				<input style="float:left;" isAsign="true" hiddenInputId="developProcessLog"  isUser="true" id="developProcesser" name="developProcesser"  value="${developProcesser}"/>
	        	<input type="hidden" class="isCheckerLog" name="developProcessLog" id=developProcessLog  value="${developProcessLog}" />
            </td>
			<td colspan="2">
				<textarea style="width: 98%;" stage="three" id="developOpinion" name="developOpinion">${developOpinion }</textarea>
			</td>
			<td colspan="2">
				<input type="hidden" id="developFile" isFile="true" name="developFile" value="${developFile }"/>
			</td>
		</tr>	     
		 <tr id="checkerLog">		 	
			<td style="text-align:center;">CQE</td>	
			<td>
				<input style="float:left;" isAsign="true" hiddenInputId="qeProcesserLog"  isUser="true" id="qeProcesser" name="qeProcesser"  value="${qeProcesser}"/>
	        	<input type="hidden" class="isCheckerLog" name="qeProcesserLog" id="qeProcesserLog"  value="${qeProcesserLog}" />
            </td>
			<td colspan="2">
				<textarea style="width: 98%;" stage="three" id="qeOpinion" name="qeOpinion">${qeOpinion }</textarea>
			</td>
			<td style="text-align:center;">是否需要通知客户</td>
	     	<td>
	     	 	<s:select list="isMailCustomers" 
				  theme="simple"
				  listKey="value" 
				  listValue="name" 
				  name="isMailCustomer"
				  id="isMailCustomer"		
				  emptyOption="true">
				 </s:select>
	     	</td>			
		</tr>	
	     <tr>
	         <td style="text-align:center;">工程主管审核</td>
	         <td style="text-align:center;">核准人</td>
	         <td >
	            <input id="projectChecker" style="float:left;" hiddenInputId="projectCheckerLog" isUser="true" name="projectChecker" value="${projectChecker }"/>
	            <input type="hidden" name="projectCheckerLog" id="projectCheckerLog"  value="${projectCheckerLog}" />
	         </td>
	         <td style="text-align:center;">核准意见</td>
	         <td colspan="3" >
	         <textarea id="projectOpinion1" name="projectOpinion1">${projectOpinion1 }</textarea>
	        </td>
	     </tr>		
	     <tr>
	         <td style="text-align:center;">研发主管审核</td>
	         <td style="text-align:center;">核准人</td>
	         <td >
	            <input id="developChecker" style="float:left;" hiddenInputId="developCheckLog" isUser="true" name="developChecker" value="${developChecker }"/>
	            <input type="hidden" name="developCheckLog" id="developCheckLog"  value="${developCheckLog}" />
	         </td>
	         <td style="text-align:center;">核准意见</td>
	         <td colspan="3" >
	         <textarea id="developOpinion1" name="developOpinion1">${developOpinion1 }</textarea>
	        </td>
	     </tr>			     	     	    
	     <tr>
	         <td style="text-align:center;">品保核准</td>
	         <td style="text-align:center;">核准人</td>
	         <td >
	            <input id="qaProcesser" style="float:left;" hiddenInputId="qaProcesserLog" isUser="true" name="qaProcesser" value="${qaProcesser }"/>
	            <input type="hidden" name="qaProcesserLog" id="qaProcesserLog"  value="${qaProcesserLog}" />
	         </td>
	         <td style="text-align:center;">核准意见</td>
	         <td colspan="3" >
	         <textarea id="qaOpinion" name="qaOpinion">${qaOpinion }</textarea>
	        </td>
	     </tr>
	</tbody> 	 
</table>
