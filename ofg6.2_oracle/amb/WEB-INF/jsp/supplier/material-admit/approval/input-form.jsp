<%@page import="sun.java2d.pipe.SpanShapeRenderer.Simple"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page import="com.norteksoft.acs.service.AcsUtils"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar calendar = Calendar.getInstance();
	String dateStr = sdf.format(calendar.getTime());
	String user = ContextUtils.getUserName();
%> 
<table class="form-table-without-border" style="width:100%;margin: auto;">
		<caption><h2>材料承认申请</h2></caption>
		<input type="hidden" id="currentActivityName" name="currentActivityName" value="${currentActivityName }" />
		<input type="hidden" id="transactorName" name="transactorName" value="${transactorName}" />
		<input type="hidden" id="str" name="str" value="${str}" />
		<input  type="hidden" id="id" name="id" value="${id}" />
		<input name="taskId" id="taskId" value="${taskId}" type="hidden" />
</table>
<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
	<tbody>
		<tr>
			<td rowspan="4" style="text-align:center;width:5%"><h3>研发部发起承认</h3></td>
			<td style="width:16%">规格型号/版本</td>
			<td><input id="productVersion"   name="productVersion" value="${productVersion }"/></td>
			<td style="width:13%">物料名称</td>
			<td><input style="width:80%" id="materialName" name="materialName" value="${materialName }"/></td>
			<td style="width:13%">物料编号</td>
			<td><input style="width:80%" id="materialCode" name="materialCode"  style="float:left;" value="${materialCode }"/>
				<a class="small-button-bg" onclick="testSelect(this)" ><span class="ui-icon ui-icon-search"></span></a>
			</td>
		</tr>
		<tr>
			<td>申请日期</td>
			<td><input id="applyDate" name="applyDate" isDate="true" value="<s:date name='applyDate' format="yyyy-MM-dd"/>" /></td>
			<td>申请人</td>
			<td><input id="applicat" name="applicat" value="${applicat }"/></td>
			<td>申请编号</td>
			<td>${formNo }</td>
		</tr>
		<tr>
			<td>承认供应商</td>
			<td>
			<input style="width:200px;float:left;" id="supplierName" readonly=readonly name="supplierName"  value="${supplierName}"/>
			<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="supplierClick();"><span class="ui-icon ui-icon-search" style='cursor:pointer;' title="选择供应商"></span></a>
			</td>
			<td>供应商编码</td>
			<td><input id="supplierCode" name="supplierCode" value="${supplierCode }" style="width: 90%;border:none;background: none;" readonly="readonly"/></td>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td>应用项目</td>
			<td colspan="5">
				<textarea id="item" name="item">${item}</textarea>
			</td>
		</tr>
		<tr>
			<td rowspan="2" style="text-align:center;"><h3>采购部指定供应商</h3></td>
			<td>指定供应商</td>
			<td >
				 <input style="width:200px;float:left;" id="supplier" name="supplier"  hiddenInputId="supplierLoginName" value="${supplierName}" />
	             <input type="hidden" name="supplierLoginName" id="supplierLoginName"  value="${supplierLoginName}" />
              <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('supplier','supplierLoginName')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>   
			</td>
			<td>供应商邮箱</td>
			<td colspan="3">
				<input id="supplierEmail" name="supplierEmail" value="${supplierEmail}" style="width:60%;"/><span style="color:red;font-size:8px;" >*用于创建供应商QIS账号</span>
			</td>
		</tr>
		<tr>
			<td>采购经办人</td>
			<td>
				<span style="float:left;"> </span><input id="purchaseProcesser" name="purchaseProcesser" isUser="true"style="width:200px;float:left;" hiddenInputId="purchaseProcessLog"  value="${purchaseProcesser}"/>
	        	<input type="hidden"  name="purchaseProcessLog" id="purchaseProcessLog"  value="${purchaseProcessLog}" />
	        </td>
			<td>意见</td>
			<td colspan="3">
			<textarea id="opinionPurchase" name="opinionPurchase">${opinionPurchase}</textarea>
			</td>
		</tr>
		<tr>
			<td rowspan="8" style="text-align:center;"><h3>供应商上传承认资料</h3></td>
			<td>供应商料号</td>
			<td ><input id="vendorNo" name="vendorNo" value="${vendorNo }"/></td>
			<td>生产地址</td>
			<td colspan="3"><input style="width:300px;" id="address" name="address" value="${address }"/></td>
		</tr>
		<tr>
			<td>规格书</td>
			<td >
				<input type="hidden" stage="file" belongpart="RD"  id="specification" isFile="true"  name="specification" value="${specification }"/>
				<security:authorize ifAnyGranted="supplier-approval-delete">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${specificationBt}"/>
				</security:authorize>
				<input id="specificationBt" type="hidden" name="specificationBt" stage="BT" value="${specificationBt}" ></input>
			</td>
			<td>图纸</td>
			<td>
				<input type="hidden" stage="file" belongpart="RD" id="drawing"  isFile="true" name="drawing" value="${drawing }"/>
				<security:authorize ifAnyGranted="supplier-approval-delete">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${drawingBt}"/>
				</security:authorize>
					<input id="drawingBt" name="drawingBt" type="hidden" stage="BT" value="${drawingBt}" ></input>
			</td>
			<td>性能评估报告</td>
			<td>
				<input type="hidden" stage="file" belongpart="RD" id="evaluateReport" isFile="true" name="evaluateReport" value="${evaluateReport }"/>
				<security:authorize ifAnyGranted="supplier-approval-delete">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${evaluateReportBt}"></input>
				</security:authorize>
					<input id="evaluateReportBt" name="evaluateReportBt" type="hidden" stage="BT" value="${evaluateReportBt}" ></input>
				
			</td>
		</tr>
		<tr>
			<td>产品成分宣告</td>
			<td>
				<input type="hidden" class="fileCheck" hisId="productDeclare" id="productFileName" value="${productFileName }">
				<input type="hidden" stage="file" belongpart="QS" id="productDeclare" isFile="true" class="field" name="productDeclare" value="${productDeclare }"/>
				<security:authorize ifAnyGranted="supplier-approval-delete">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${productDeclareBt}"/>
				</security:authorize>
				<input id="productDeclareBt" name="productDeclareBt" type="hidden" stage="BT" value="${productDeclareBt}" ></input>
			</td>
			<td>测试报告</td>
			<td>
				<input type="hidden" stage="file" belongpart="QS" id="testReport" isFile="true" name="testReport" value="${testReport }"/>
				<security:authorize ifAnyGranted="supplier-approval-delete">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${testReportBt}"/>
				</security:authorize>	
				<input id="testReportBt" name="testReportBt" type="hidden" stage="BT" value="${testReportBt}" ></input>
				
			</td>
			<td>MSDS</td>
			<td>
				<input type="hidden" stage="file" belongpart="QS" id="msds" isFile="true" name="msds" value="${msds }"/>
				<security:authorize ifAnyGranted="supplier-approval-delete">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${msdsBt}"/>
				</security:authorize>
				<input id="msdsBt" name="msdsBt" type="hidden" stage="BT" value="${msdsBt}"></input>
			</td>
		</tr>
		<tr>
			<td>REACH SVHC(调查表)</td>
			<td>
				<input type="hidden" class="fileCheck" hisId="surveyFile" id="surveyFileNmae" value="${surveyFileNmae }">
				<input type="hidden" stage="file" belongpart="QS" id="surveyFile" isFile="true" class="field" name="surveyFile" value="${surveyFile }"/>
				<security:authorize ifAnyGranted="supplier-approval-delete">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${surveyFileBt}"/>
				</security:authorize>
				<input id="surveyFileBt" name="surveyFileBt" type="hidden" stage="BT" value="${surveyFileBt}" ></input>
				
			</td>
			<td>BOM</td>
			<td>
				<input type="hidden" stage="file" belongpart="QS" id="bomFile" isFile="true" name="bomFile" value="${bomFile }"/>
				<security:authorize ifAnyGranted="supplier-approval-delete">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${bomFileBt}"/>
				</security:authorize>
				<input id="bomFileBt" name="bomFileBt" type="hidden" stage="BT" value="${bomFileBt}" ></input>
				
			</td>
			<td>产品实物&拆解图</td>
			<td>
				<input type="hidden" stage="file" belongpart="QS" id="entityPhoto" isFile="true" name="entityPhoto" value="${entityPhoto }"/>
				<security:authorize ifAnyGranted="supplier-approval-delete">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${entityPhotoBt}"/>
				</security:authorize>
				<input id="entityPhotoBt" name="entityPhotoBt" type="hidden" stage="BT" value="${entityPhotoBt}" ></input>
			</td>
			
		</tr>
		<tr>
			<td>FAI报告</td>
			<td>
				<input type="hidden" stage="file" belongpart="SQE" id="faiReport" isFile="true" name="faiReport" value="${faiReport }"/>
				<security:authorize ifAnyGranted="supplier-approval-delete">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${faiReportBt}"/>
				</security:authorize>
				<input id="faiReportBt" name="faiReportBt" type="hidden" stage="BT" value="${faiReportBt}"></input>
				
			</td>
			<td>可靠性报告</td>
			<td>
				<input type="hidden" stage="file" belongpart="SQE" id="raReport" isFile="true" name="raReport" value="${raReport }"/>
				<security:authorize ifAnyGranted="supplier-approval-delete">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${raReportBt}"/>
				</security:authorize>
				<input id="raReportBt" name="raReportBt" type="hidden" stage="BT" value="${raReportBt}"></input>
				
			</td>
			<td>QC工程图</td>
			<td>
				<input type="hidden" stage="file" belongpart="SQE" id="qcPlan" isFile="true" name="qcPlan" value="${qcPlan }"/>
				<security:authorize ifAnyGranted="supplier-approval-delete">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${qcPlanBt}"/>
				</security:authorize>
				<input id="qcPlanBt" name="qcPlanBt" type="hidden" stage="BT" value="${qcPlanBt}" ></input>
			</td>
		</tr>
		<tr>
			<td>CPK报告</td>
			<td>
				<input type="hidden" stage="file" belongpart="SQE" id="cpkReport" isFile="true" name="cpkReport" value="${cpkReport }"/>
				<security:authorize ifAnyGranted="supplier-approval-delete">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${cpkReportBt}"></input>
				</security:authorize>
				<input id="cpkReportBt" name="cpkReportBt" type="hidden" stage="BT" value="${cpkReportBt}" ></input>
				
			</td>
			<td>FMEA</td>
			<td>
				<input type="hidden" stage="file" belongpart="SQE" id="fmea" isFile="true" name="fmea" value="${fmea }"/>
				<security:authorize ifAnyGranted="supplier-approval-delete">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${fmeaBt}"></input>
				</security:authorize>
				<input id="fmeaBt" name="fmeaBt" type="hidden" stage="BT" value="${fmeaBt}" ></input>
			</td>
			<td>代码说明</td>
			<td>
				<input type="hidden" stage="file" belongpart="SQE" id="codeExplain" isFile="true" name="codeExplain" value="${codeExplain }"/>
				<security:authorize ifAnyGranted="supplier-approval-delete">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${codeExplainBt}"></input>
				</security:authorize>	
					<input id="codeExplainBt" name="codeExplainBt" type="hidden" stage="BT" value="${codeExplainBt}"></input>
			</td>
		</tr>
		<tr>
			
			<td>包装规格</td>
			<td>
				<input type="hidden" stage="file" belongpart="SQE" id="packing" isFile="true" name="packing" value="${packing }"/>
				<security:authorize ifAnyGranted="supplier-approval-delete">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${packingBt}"/>
				</security:authorize>
				<input id="packingBt"  name="packingBt"type="hidden" stage="BT" value="${packingBt}" ></input>
			</td>
			<td>出货报告</td>
			<td>
				<input type="hidden" stage="file" belongpart="SQE" id="checkoutReport" isFile="true" name="checkoutReport" value="${checkoutReport }"/>
				<security:authorize ifAnyGranted="supplier-approval-delete">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${checkoutReportBt}"/>
				</security:authorize>	
					<input id="checkoutReportBt" name="checkoutReportBt" type="hidden" stage="BT" value="${checkoutReportBt}" ></input>
			</td>
			<td>其他</td>
			<td>
				<input type="hidden" id="others" belongpart="SQE" stage="file" isFile="true" name="others" value="${others }"/>
				<security:authorize ifAnyGranted="supplier-approval-delete">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${othersBt}"/>
				</security:authorize>	
					<input id="othersBt" name=othersBt type="hidden" stage="BT" value="${othersBt}" ></input>
			</td>
		</tr>
		<tr>
			<td>产品宣告表请在宣告表中上传</td>
			<td>
				<input id="showGpMaterialNo" name="showGpMaterialNo" value="${showGpMaterialNo}" type="hidden" style="width: 90%;border:none;background: none;" readonly="readonly"/>
				<input name="showGpMaterialId" id="showGpMaterialId" value="${showGpMaterialId}" type="hidden" style="width: 90%;border:none;background: none;" readonly="readonly"/>
				<%-- <span  onclick="hisReportInput(this);"><a style="text-decoration:underline;">${gpMaterialNo}</a>   </span> --%> 
				${showGpMaterialNo}
			</td>
			<td>
				<input name="showGpMaterialState" id="showGpMaterialState" value="${showGpMaterialState}" type="hidden" />
				<span >${showGpMaterialState}</span>
			</td>
			<td></td>
			<td></td>
		</tr>
<!-- 		<tr> -->
<!-- 			<td style="text-align:center;"><h3>供应商上传材料承认</h3></td> -->
<!-- 			<td>承认书</td> -->
<%-- 			<td colspan="5"><input type="hidden" id="materialBook" isFile="true" stage='two' name="materialBook" value="${materialBook }"/></td> --%>
<!-- 		</tr> -->
		<tr><td colspan="7" style="text-align:center;"><h3>会签<h3></td></tr>
		<tr>
			<td style="text-align:center;">评审部门</td>	
			<td style="text-align:center;">评审人</td>
			<td style="text-align:center;">结论</td>
			<td colspan="2" style="text-align:center;">签核意见</td>
			<td colspan="2" style="text-align:center;">上传附件</td>
		</tr>
		<tr id="checkerLog">
		 	<input  type="hidden" name="checkDeptMansLog" id="checkDeptMansLog" value="${checkDeptMansLog }"/>
		 	<input  type="hidden" name="checkDeptMansLog2" id="checkDeptMansLog2" value="${checkDeptMansLog2 }"/>
			<td>RD核准:</td>	
			<td>
				<input style="float:left;" hiddenInputId="rdCheckerLog"  isUser="true" id="rdChecker" name="rdChecker"  value="${rdChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="rdCheckerLog" id="rdCheckerLog" transact="s" value="${rdCheckerLog}" />
            </td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="rdStatus" 
					id="rdStatus" 
					emptyOption="true"
					onchange=""
					stage="three"
					theme="simple">
				</s:select>
			</td>
			<td colspan="2">
				<textarea style="width: 98%;" stage="three" id="countersignRD" name="countersignRD">${countersignRD }</textarea>
			</td>
			<td colspan="2">
				<s:if test="consignorDept!='供应商'">
					<input type="hidden" id="fileRD" isFile="true" name="fileRD" value="${fileRD }"/>
				</s:if>
			</td>
		</tr>
		<tr id="checkerLog">
			<td>PM核准:</td>	
			<td>
				<input style="float:left;" hiddenInputId="pmCheckerLog"  isUser="true" id="pmChecker" name="pmChecker" value="${pmChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="pmCheckerLog" id="pmCheckerLog" transact="s" value="${pmCheckerLog}" />
              	<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('pmChecker','pmCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
			</td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="pmStatus" 
					id="pmStatus" 
					emptyOption="true"
					onchange=""
					stage="three"
					theme="simple">
				</s:select>
			</td>
			<td colspan="2">
				<textarea style="width: 98%;" stage="three" id="countersignPM" name="countersignPM">${countersignPM }</textarea>
			</td>
			<td colspan="2"></td>
		</tr>
		<tr id="checkerLog">
			<td>SQE核准:</td>	
			<td>
			<input style="float:left;" hiddenInputId="sqeCheckerLog"  isUser="true" id="sqeChecker" name="sqeChecker" value="${sqeChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="sqeCheckerLog" transact="s" id="sqeCheckerLog"  value="${sqeCheckerLog}" />
              	<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('sqeChecker','sqeCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
			</td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="sqeStatus" 
					id="sqeStatus" 
					emptyOption="true"
					onchange=""
					stage="three"
					theme="simple">
				</s:select>
			</td>
			<td colspan="2">
				<textarea style="width: 98%;" stage="three" id="countersignSQE" name="countersignSQE">${countersignSQE }</textarea>
			</td>
			<td colspan="2"></td>
		</tr>
		<tr id="checkerLog">
		<td>QS核准:</td>	
			<td>
			<input style="float:left;" hiddenInputId="qsCheckerLog"  isUser="true" id="qsChecker" name="qsChecker" value="${qsChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="qsCheckerLog" id="qsCheckerLog" transact="s"  value="${qsCheckerLog}" />
              	<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('qsChecker','qsCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
			</td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="qsStatus" 
					id="qsStatus" 
					emptyOption="true"
					onchange=""
					stage="three"
					theme="simple">
				</s:select>
			</td>
			<td colspan="2">
				<textarea style="width: 98%;" stage="three" id="countersignQS" name="countersignQS">${countersignQS }</textarea>
			</td>
			<td colspan="2"></td>
		</tr>
		<% if(!"欧菲科技-CCM".equals(ContextUtils.getCompanyName())&&!"欧菲科技-神奇工场".equals(ContextUtils.getCompanyName())){ %>
		<tr id="checkerLog">
		<td >NPI核准:</td>	
			<td>
				<input style="float:left;" hiddenInputId="npiCheckerLog"  isUser="true" id="npiChecker" name="npiChecker" value="${npiChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="npiCheckerLog" id="npiCheckerLog" transact="s" value="${npiCheckerLog}" />
              	<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('npiChecker','npiCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
			</td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="npiStatus" 
					id="npiStatus" 
					emptyOption="true"
					onchange=""
					stage="three"
					theme="simple">
				</s:select>
			</td>
			<td colspan="2">
				<textarea style="width: 98%;" stage="three"  id="countersignNpi" name="countersignNpi">${countersignNpi }</textarea>
			</td>
			<td colspan="2"></td>
		</tr>
		<tr id="checkerLog">
		<td>DQE核准:</td>	
			<td>
			<input style="float:left;" hiddenInputId="dqeCheckerLog"  isUser="true" id="dqeChecker" name="dqeChecker" value="${dqeChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="dqeCheckerLog" id="dqeCheckerLog" transact="s" value="${dqeCheckerLog}" />
              	<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('dqeChecker','dqeCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
			</td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="dqeStatus" 
					id="dqeStatus" 
					emptyOption="true"
					onchange=""
					stage="three"
					theme="simple">
				</s:select>
			</td>
			<td colspan="2">
				<textarea style="width: 98%;"  stage="three" id="countersignDqe" name="countersignDqe">${countersignDqe }</textarea>
			</td>
			<td colspan="2"></td>
		</tr>
		<tr id="checkerLog">
		<td>工程核准:</td>
			<td>
			<input style="float:left;" hiddenInputId="projectCheckerLog"  isUser="true" id="projectChecker" name="projectChecker" value="${projectChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="projectCheckerLog" id="projectCheckerLog" transact="s" value="${projectCheckerLog}" />
              	<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('projectChecker','projectCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
			</td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="projectStatus" 
					id="projectStatus" 
					emptyOption="true"
					onchange=""
					stage="three"
					theme="simple">
				</s:select>
			</td>
			<td colspan="2">
				<textarea style="width: 98%;" stage="three" id="countersignProject" name="countersignProject">${countersignProject }</textarea>
			</td>
			<td colspan="2"></td>
		</tr>
		<% } %>
		<tr>
			<td rowspan="2" style="text-align:center;"><h3>文控归档</h3></td>
			<td>
				<span style="float:left;"> </span><input id="docControl" name="docControl" isUser="true"style="width:200px;float:left;" hiddenInputId="docControlLoging"  value="${docControl}"/>
	        	<input type="hidden"  name="docControlLoging" id="docControlLoging"  value="${docControlLoging}" />
			</td>
<!-- 			<td>提交时间</td> -->
			<td><span>提交时间: <input id="docControlDate" name="docControlDate" isDate="true" value="<s:date name='docControlDate' format="yyyy-MM-dd"/>" /></span></td>
			<td colspan="2"><span>最终状态: <input  id="finalStatus" name="finalStatus" value="${finalStatus }" style="width: 70%;border:none;background: none;" readonly="readonly"/></span></td>
<!-- 	         <td > -->
<%-- 	            <input style="float:left;width:200px" name="copyMan" id="copyMan" value="${copyMan}" /> --%>
<%-- 	          	<input style="float:left;" type='hidden' name="copyLoginMan" id="copyLoginMan" value="${copyLoginMan}" /> --%>
<!-- 	           	<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="selectPerson1('copyMan','copyLoginMan');"><span class="ui-icon ui-icon-search"  ></span></a> -->
<%-- 	           	<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('copyMan','copyLoginMan')" href="javascript:void(0);" title="<s:text name='清空'/>"> --%>
<!-- 	           	<span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a> -->
<!-- 	         </td> -->
	         	<td colspan="3"></td>
				
		</tr>
		<tr>
		<td>文控意见</td>
	         <td colspan="3"><textarea style="width: 98%;" id="opiniondoc" name="opiniondoc">${opiniondoc }</textarea></td>
			
			<td colspan="2"></td>
		</tr>
		
	</tbody>
</table>
