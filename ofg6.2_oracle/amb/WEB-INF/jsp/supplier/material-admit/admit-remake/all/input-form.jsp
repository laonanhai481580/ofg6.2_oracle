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
		<input type="hidden" id="transactorName" name="transactorName" value="${transactorName }" />
		<input type="hidden" id="str" name="str" value="${str}" />
		<input  type="hidden" id="id" name="id" value="${id}" />
		<input name="taskId" id="taskId" value="${taskId}" type="hidden" />
</table>
<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
	<tbody>
		<tr>
			<td rowspan="4" style="text-align:center;width:5%"><h3>研发/工程部发起承认</h3></td>
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
			<td>材料类别</td>
			<td>
				<s:select list="materialSorts"
					listKey="value" 
					listValue="value" 
					name="materialSort" 
					id="materialSort" 
					emptyOption="true"
					stage="three"
					theme="simple">
				</s:select>
			</td>
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
			<td rowspan="3" style="text-align:center;"><h3>供应商上传承认资料</h3></td>
			<td>供应商料号</td>
			<td ><input id="vendorNo" name="vendorNo" value="${vendorNo }"/></td>
			<td>生产地址</td>
			<td colspan="3"><input style="width:300px;" id="address" name="address" value="${address }"/></td>
		</tr>
		<tr>
			<td colspan="6" style="padding:0px;" id="checkItemsParent">
<!-- 				<div style="overflow-x:auto;overflow-y:hidden;"> -->
					<%@ include file="admit-file.jsp" %>
<!-- 				</div> -->
			</td>
		</tr>
		<tr>
			<td>产品宣告表:</td>
			<td >
			<input id="showGpMaterialNo" name="showGpMaterialNo" value="${showGpMaterialNo }" type="hidden"/>
			<input name="showGpMaterialId" id="showGpMaterialId" value="${showGpMaterialId}" type="hidden" />
			${showGpMaterialNo}
			</td>
			<td>
				<input name="showGpMaterialState" id="showGpMaterialState" value="${showGpMaterialState}" type="hidden" />
				<span >${showGpMaterialState}</span>
			</td>
			<td></td>
			<td></td>
		</tr>
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
			<td>RD:</td>	
			<td>
				<input style="float:left;" hiddenInputId="rdCheckerLog"  isUser="true" id="rdChecker" name="rdChecker"  value="${rdChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="rdCheckerLog" id="rdCheckerLog"  value="${rdCheckerLog}" />
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
			<td>PM:</td>	
			<td>
				<input style="float:left;" hiddenInputId="pmCheckerLog"  isUser="true" id="pmChecker" name="pmChecker" value="${pmChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="pmCheckerLog" id="pmCheckerLog" transact="s" value="${pmCheckerLog}" />
              	<%-- <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('pmChecker','pmCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span> --%>
			</td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="pmStatus" 
					id="pmStatus" 
					emptyOption="true"
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
		<td>QS:</td>	
			<td>
			<input style="float:left;" hiddenInputId="qsCheckerLog"  isUser="true" id="qsChecker" name="qsChecker" value="${qsChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="qsCheckerLog" id="qsCheckerLog" transact="s"  value="${qsCheckerLog}" />
              	<%-- <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('qsChecker','qsCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span> --%>
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
		<% if(!"欧菲科技-CCM".equals(ContextUtils.getCompanyName())){ %>
		<tr id="checkerLog">
		<% if("欧菲-TP".equals(ContextUtils.getCompanyName())){ %>
		<td >EE:</td>	
		<% } else{%>
		<td >NPI:</td>	
		<% } %>
			<td>
				<input style="float:left;" hiddenInputId="npiCheckerLog"  isUser="true" id="npiChecker" name="npiChecker" value="${npiChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="npiCheckerLog" id="npiCheckerLog" transact="s" value="${npiCheckerLog}" />
              	<%-- <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('npiChecker','npiCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span> --%>
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
				<textarea style="width: 98%;" stage="three" id="countersignNpi" name="countersignNpi">${countersignNpi }</textarea>
			</td>
			<td colspan="2"></td>
		</tr>
		<tr id="checkerLog">
		<td>DQE:</td>	
			<td>
			<input style="float:left;" hiddenInputId="dqeCheckerLog"  isUser="true" id="dqeChecker" name="dqeChecker" value="${dqeChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="dqeCheckerLog" id="dqeCheckerLog" transact="s" value="${dqeCheckerLog}" />
              	<%-- <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('dqeChecker','dqeCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span> --%>
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
				<textarea style="width: 98%;" stage="three" id="countersignDqe" name="countersignDqe">${countersignDqe }</textarea>
			</td>
			<td colspan="2"></td>
		</tr>
		<tr id="checkerLog">
		<td>工程:</td>
			<td>
			<input style="float:left;" hiddenInputId="projectCheckerLog"  isUser="true" id="projectChecker" name="projectChecker" value="${projectChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="projectCheckerLog" id="projectCheckerLog" transact="s" value="${projectCheckerLog}" />
              	<%-- <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('projectChecker','projectCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span> --%>
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
		<% if("欧菲科技-TP".equals(ContextUtils.getCompanyName())){ %>
		<tr id="checkerLog">
		<td>EE:</td>
			<td>
			<input style="float:left;" hiddenInputId="eeCheckerLog"  isUser="true" id="eeChecker" name="eeChecker" value="${eeChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="eeCheckerLog" id="eeCheckerLog" transact="s" value="${eeCheckerLog}" />
              	<%-- <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('eeChecker','eeCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span> --%>
			</td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="eeStatus" 
					id="eeStatus" 
					emptyOption="true"
					onchange=""
					stage="three"
					theme="simple">
				</s:select>
			</td>
			<td colspan="2">
				<textarea style="width: 98%;" stage="three" id="countersignEE" name="countersignEE">${countersignEE }</textarea>
			</td>
			<td colspan="2"></td>
		</tr>
		<tr id="checkerLog">
		<td><input style="width: 90%;" id="elseText" name="elseText" value="${elseText }" /></td>
			<td>
			<input style="float:left;" hiddenInputId="elseCheckerLog"  isUser="true" id="elseChecker" name="elseChecker" value="${elseChecker}"/>
	        	<input type="hidden" class="isCheckerLog" name="elseCheckerLog" id="elseCheckerLog" transact="s" value="${elseCheckerLog}" />
              	<%-- <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('elseChecker','elseCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span> --%>
			</td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="elseStatus" 
					id="elseStatus" 
					emptyOption="true"
					onchange=""
					stage="three"
					theme="simple">
				</s:select>
			</td>
			<td colspan="2">
				<textarea style="width: 98%;" stage="three" id="countersignElse" name="countersignElse">${countersignElse }</textarea>
			</td>
			<td colspan="2"></td>
		</tr>
		<% } %>
		<tr>
			<td rowspan="2">SQE:</td>
			<td>
			<input style="float:left;" hiddenInputId="sqeCheckerLog"  isUser="true" id="sqeChecker" name="sqeChecker" value="${sqeChecker}"/>
	        	<input type="hidden" name="sqeCheckerLog" transact="s" id="sqeCheckerLog"  value="${sqeCheckerLog}" />
              	<%-- <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('sqeChecker','sqeCheckerLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span> --%>
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
				<textarea style="width: 98%;" id="countersignSQE" name="countersignSQE">${countersignSQE }</textarea>
			</td>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td>会签人员</td>
			<td colspan="3">
				<input style="float:left;width:200px" name="copyMan" id="copyMan" value="${copyMan}" />
				<input style="float:left;" type='hidden' name="copyManLogin" id="copyManLogin" value="${copyManLogin}" />
				<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="selectPerson1('copyMan','copyManLogin');"><span class="ui-icon ui-icon-search"  ></span></a>
				<%-- <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('copyMan','copyManLogin')" href="javascript:void(0);" title="<s:text name='清空'/>"> --%>
				<span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
			</td>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td style="text-align:center;"><h3>SQE主管审核</h3></td>
			<td>
			<input style="float:left;" hiddenInputId="sqeLeadLog"  isUser="true" id="sqeLead" name="sqeLead" value="${sqeLead}"/>
	        	<input type="hidden" name="sqeLeadLog" transact="s" id="sqeLeadLog"  value="${sqeLeadLog}" />
              	<%-- <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('sqeLead','sqeLeadLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span> --%>
			</td>
			<td>承认结论</td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="sqeLeadStatus" 
					id="sqeLeadStatus" 
					emptyOption="true"
					onchange=""
					stage="three"
					theme="simple">
				</s:select>
			</td>
			<td>意见</td>
			<td colspan="2">
				<textarea style="width: 98%;" id="countersignSqeLead" name="countersignSqeLead">${countersignSqeLead }</textarea>
			</td>
		</tr>
		<tr>
			<td style="text-align:center;"><h3>主导部门审核</h3></td>
			<td>
			<input style="float:left;" hiddenInputId="skillLeadLog"  isUser="true" id="skillLead" name="skillLead" value="${skillLead}"/>
	        	<input type="hidden" name="skillLeadLog" transact="s" id="skillLeadLog"  value="${skillLeadLog}" />
              	<%-- <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('skillLead','skillLeadLog')" href="javascript:void(0);" title="<s:text name='清空'/>"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span> --%>
			</td>
			<td>承认结论</td>
			<td>
				<s:select list="admitProjects"
					listKey="value" 
					listValue="value" 
					name="skillLeadStatus" 
					id="skillLeadStatus" 
					emptyOption="true"
					onchange=""
					stage="three"
					theme="simple">
				</s:select>
			</td>
			<td>意见</td>
			<td colspan="2">
				<textarea style="width: 98%;" id="countersignSkill" name="countersignSkill">${countersignSkill }</textarea>
			</td>
		</tr>
		<%--
		<tr>
			<td rowspan="2" style="text-align:center;"><h3>文控归档</h3></td>
			<td>
				<span style="float:left;"> </span><input id="docControl" name="docControl" isUser="true"style="width:200px;float:left;" hiddenInputId="docControlLoging"  value="${docControl}"/>
	        	<input type="hidden"  name="docControlLoging" id="docControlLoging"  value="${docControlLoging}" />
			</td>
			<td><span>提交时间: <input id="docControlDate" name="docControlDate" isDate="true" value="<s:date name='docControlDate' format="yyyy-MM-dd"/>" /></span></td>  
		--%>
<%-- 			<td colspan="2"><span>最终状态: <input  id="finalStatus" name="finalStatus" value="${finalStatus }" style="width: 70%;border:none;background: none;" readonly="readonly"/></span></td> --%>
	 <%--       <td colspan="3"></td>
		</tr>
		<td>文控意见</td>
	         <td colspan="3"><textarea style="width: 98%;" id="opiniondoc" name="opiniondoc">${opiniondoc }</textarea></td>
			
			<td colspan="2"></td>
		</tr>
	--%>
	</tbody>
</table>
