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
	    <tr><td style="text-align:center;background-color: CornflowerBlue;color: white;font-weight: bold;" colspan='6'>开单填写</td></tr>
	    <tr> 
	         <td>异常类型</td>
	         <td >
	           	 <s:select list="exceptionTypes" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="exceptionType"
				  id="exceptionType"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;">
				 </s:select>
	         </td>
	         <td>产品阶段</td>
	         <td >
	         <s:select list="productStages" 
				  theme="simple"
				  listKey="value" 
				  listValue="name" 
				  name="productStage"
				  id="productStage"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;"
				  ></s:select>
	         </td>
	        <td>事业部</td>
	         <td><input id="businessUnitName" name="businessUnitName" value="${businessUnitName}"/>
	         <input id="division" type="hidden" name="division" value="${division}"/>
	          <input id="sqeCompleteTime" type="hidden" name="sqeCompleteTime" value="${sqeCompleteTime}"/>
	           <input id="sqeReplyTime" type="hidden" name="sqeReplyTime" value="${sqeReplyTime}"/>
	           <input id="isSupplier" type="hidden" name="isSupplier" value="${isSupplier}"/>
	         <input id="isClosedAlaysis" type="hidden" name="isClosedAlaysis" value="${isClosedAlaysis}"/>
	         <input id="currentMan" type="hidden" name="currentMan" value="${currentMan}"/>
	         <input id="currentManLog" type="hidden" name="currentManLog" value="${currentManLog}"/>	         
	         </td>		         
	     </tr>
	    <tr>
	        <td>开单区域</td>
	         <td><input id="billingArea" name="billingArea" value="${billingArea}"/></td>
	          <td>供应商</td>
	         <td><input style="float:left;" id="supplierName" name="supplierName" readonly=readonly value="${supplierName}"/><input type='hidden' id="supplierCode"  name="supplierCode" value="${supplierCode}"/>
	         <a class="small-button-bg" style="margin-left:2px;float:left;" onclick="supplierClick()" href="javascript:void(0);" title="选择供应商"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
	         </td>
	          <td>检验日期</td>
	         <td><input id="inspectionDate" name="inspectionDate" isDate="true" value="<s:date name='inspectionDate' format="yyyy-MM-dd"/>" /></td>	          
	     </tr>
	     <tr>
	         <td>品名</td>
	         <td><input id="bomName" name="bomName" value="${bomName}"/></td>
	         <td>料号</td>
	         <td><input id="bomCode" name="bomCode" value="${bomCode}"/></td>
	         <td>物料类别</td>
	         <td>
				<input id="materialType" name="materialType" value="${materialType}"/>					  
			</td>
	     </tr>
	      <tr>
	         <td>进料数</td>
	         <td><input id="incomingAmount" name="incomingAmount" value="${incomingAmount}"/></td>
	         <td>抽检数</td>
	         <td><input id="checkAmount" name="checkAmount" value="${checkAmount}"/></td>
	          <td>单位</td>
	         <td><input id="units" name="units" value="${units}"/></td>
	     </tr>
	     <tr>
	     	<td>供应商邮箱地址</td>
	     	<td colspan='2'><textarea id="supplierEmail" name="supplierEmail" onchange="checkEmail(this);">${supplierEmail}</textarea></td>
	     	<td >
	     	<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">如需发多个邮箱请用";"分开.</span></span>	
	     	</td>
	     	<td>检验编号</td>
	     	<td >
	     	<input name="inspectionId" id="inspectionId" value="${inspectionId }" type="hidden">
	     	<input name="inspectionFormNo" id="inspectionFormNo" value="${inspectionFormNo }" type="hidden">
	     	<%-- <input name="formNo" id="formNo" value="${formNo}" type="hidden"> --%>
	     	<a title="查看检验单详情" href="javascript:void(0);openInspectionForm('${inspectionId}')">${inspectionFormNo}</a></td>
	     	
	     </tr>
	     <tr>
	         <td  style="text-align:center;">外观不良</td>
	         <td colspan='2' style="text-align:center;">外观不良率</td>
	         <td  style="text-align:center;"><input name="surfaceBadRate" id="surfaceBadRate" value="${surfaceBadRate }"/>%</td>
	     	 <td>检验状态</td>
	     	 <td>
	     	 	<s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="surfaceBadState"
				  id="surfaceBadState"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;">
				 </s:select>
	     	 </td>
	     </tr>
	      <tr>
	         <td  style="text-align:center;"> 功能不良</td>
	         <td colspan='2' style="text-align:center;">功能不良率</td>
	         <td style="text-align:center;"><input name="functionBadRate" id="functionBadRate" value="${functionBadRate }"/>%</td>
	      	 <td>检验状态</td>
	     	 <td>
	     	 	<s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="functionBadState"
				  id="functionBadState"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;">
				 </s:select>
	     	 </td>
	     </tr>
	      <tr>
	         <td style="text-align:center;">尺寸不良</td>
	         <td colspan='2' style="text-align:center;">尺寸不良率</td>
	         <td  style="text-align:center;"><input name="sizeBadRate" id="sizeBadRate" value="${sizeBadRate }"/>%</td>
	    	 <td>检验状态</td>
	     	 <td>
	     	 	<s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="sizeBadState"
				  id="sizeBadState"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;">
				 </s:select>
	     	 </td>
	     </tr>
	      <tr>
	         <td style="text-align:center;">特性不良</td>
	         <td colspan='2' style="text-align:center;">特性不良率</td>
	         <td  style="text-align:center;"><input name="featuresBadRate" id="featuresBadRate" value="${featuresBadRate }"/>%</td>
	    	 <td>检验状态</td>
	     	 <td>
	     	 	<s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="featuresBadState"
				  id="featuresBadState"
				  emptyOption="true"
				  labelSeparator=""
				  cssStyle="width:150px;">
				 </s:select>
	     	 </td> 
	     </tr>
	     <tr>
	        <td >异常描述</td>
	        <td colspan='3'><textarea rows="5" name="badDesc" style="width:98%;" id="badDesc" >${badDesc}</textarea></td>
	        <td>附件</td>
	        <td><input name="descFile" isFile="true"  type="hidden" id="descFile" value="${descFile }"/></td>
	     </tr>
	     <tr>
	     	 <td >
	         </td>
	         <td></td>
	         <td>发起日期</td>
	         <td>
             	<input id="sponsorDate" name="sponsorDate" isDate="true" value="<s:date name='sponsorDate' format="yyyy-MM-dd"/>" />	          
	         </td>	     
	         <td>发起人</td>
	         <td><input id="inspector" name="inspector" value="${inspector}"  /></td>	         	         
	     </tr>
	     <security:authorize ifAnyGranted="supplier-improve-conceal">
	     <tr><td style="text-align:center;background-color: CornflowerBlue;color: white;font-weight: bold;" colspan='6'>主管审核问题描述</td></tr>
	     <tr>
	         <td>主管意见</td>
	         <td colspan='5'><textarea rows="2" cols="3" id="reportCheckOpinion" name="reportCheckOpinion">${reportCheckOpinion}</textarea></td>	        
	     </tr>	     
	     <tr>
	    	 <td></td>
	         <td>
	         </td>
	    	 <td>审核日期</td>
	         <td>
	         	<input id="reportCheckDate" name="reportCheckDate" isDate="true" value="<s:date name='reportCheckDate' format="yyyy-MM-dd"/>" />
	         </td>
	      	 <td>异常主管</td>
	         <td>
	            <input id="reportChecker" name="reportChecker" value="${reportChecker}" isUser="true" style="float:left;" hiddenInputId="reportCheckerLog"/>
	            <input type="hidden" name="reportCheckerLog" id="reportCheckerLog"  value="${reportCheckerLog}" />
	          </td>
	     </tr>	
	     <tr><td style="text-align:center;background-color: CornflowerBlue;color: white;font-weight: bold;" colspan='6'>MQE确认异常</td></tr>
	     <tr>
	         <td>MQE意见</td>
	         <td colspan='5'><textarea rows="2" cols="3" id="mqeCheckOpinion" name="mqeCheckOpinion">${mqeCheckOpinion}</textarea></td>	        
	     </tr>	     
	     <tr>
	    	 <td></td>
	         <td>
	         </td>
	    	 <td>确认日期</td>
	         <td>
	         	<input id="mqeCheckDate" name="mqeCheckDate" isDate="true" value="<s:date name='mqeCheckDate' format="yyyy-MM-dd"/>" />
	         </td>
	      	 <td>MQE确认人</td>
	         <td>
	            <input id="mqeChecker" name="mqeChecker" value="${mqeChecker}" isUser="true" style="float:left;" hiddenInputId="mqeCheckerLog"/>
	            <input type="hidden" name="mqeCheckerLog" id="mqeCheckerLog"  value="${mqeCheckerLog}" />
	          </td>
	     </tr>		          
	     <tr><td style="text-align:center;background-color: CornflowerBlue;color: white;font-weight: bold;" colspan='6'>PMC经办</td></tr>
	     <tr>
	         <td>需求交期</td>
	         <td>
	         	<input id="demandDeliveryPeriod" name="demandDeliveryPeriod" isDate="true" value="<s:date name='demandDeliveryPeriod' format="yyyy-MM-dd"/>" />
	         </td>
	         <td>MRB申请</td>
	         <td>
	            <input type="radio" id="mrbApply1" name="mrbApply" value="需要" <s:if test="mrbApply=='需要'">checked="checked"</s:if> title="需要" checked="checked" onchange="mrbApplyChange(this);"/><label for="mrbApply1">需要</label>
	            <input type="radio" id="mrbApply2" name="mrbApply" value="不需要" <s:if test="mrbApply=='不需要'">checked="checked"</s:if> title="不需要"  onchange="mrbApplyChange(this);"/><label for="mrbApply2">不需要</label>
	         </td>
	         <td><span id="mrbReportNo_span" style="color:red;"></span>MRB单号</td>
	         <td><input id="mrbReportNo" name="mrbReportNo" value="${mrbReportNo }" /></td>
	     </tr>
	      <tr>
	         <td>PMC意见</td>
	          <td colspan='3'><textarea rows="2" cols="3" id="pmcOpinion" name="pmcOpinion">${pmcOpinion }</textarea></td>
	           <td>PMC办理人</td>
	         <td>
	            <input id="pmcChecker" name="pmcChecker" value="${pmcChecker}" isUser="true" style="float:left;" hiddenInputId="pmcCheckerLog"/>
	            <input type="hidden" name="pmcCheckerLog" id="pmcCheckerLog"  value="${pmcCheckerLog}" />
	          </td>
	     </tr>	     
	     <tr><td style="text-align:center;background-color: CornflowerBlue;color: white;font-weight: bold;" colspan='6'>MQE提供处理意见</td></tr>
	     <tr>
	         <td>MQE意见</td>
	          <td colspan='5'><textarea rows="2" cols="3" id="qualityOpinion" name="qualityOpinion">${qualityOpinion }</textarea></td>
	     </tr>
	     <tr>
	         <td>处理意见</td>
	         <td>
                <input type="radio" id="sqeProcessOpinion1"  name="sqeProcessOpinion" value="特采" <s:if test="sqeProcessOpinion=='特采'">checked="checked"</s:if> title="特采" checked="checked" onchange="sqeProcessOpinionChange(this);"/><label for="sqeProcessOpinion1">特采</label>
	            <input type="radio" id="sqeProcessOpinion2"  name="sqeProcessOpinion" value="退货" <s:if test="sqeProcessOpinion=='退货'">checked="checked"</s:if> title="退货" onchange="sqeProcessOpinionChange(this);"/><label for="sqeProcessOpinion2">退货</label>
             </td>
	         <td><span id="returnReportNo_span" style="color:red;"></span>退货通知单单号</td>
	         <td>
	          <input id="returnReportNo" name="returnReportNo" value="${returnReportNo }" />
	         </td>
	          <td><span id="sqeMrbReportNo_span" style="color:red;"></span>MRB单号</td>
	         <td><input id="sqeMrbReportNo" name="sqeMrbReportNo" value="${sqeMrbReportNo }" /></td>
	     </tr>
	     <tr><td style="text-align:center;background-color: CornflowerBlue;color: white;font-weight: bold;" colspan='6'>MQE主管审核处理意见</td></tr>
	     <tr>
	         <td>MQE主管意见</td>
	         <td colspan='5'><textarea rows="2" cols="3" id="mqeSupervisorOpinion" name="mqeSupervisorOpinion">${mqeSupervisorOpinion }</textarea></td>	        
	     </tr>	     
	     <tr>
	    	 <td></td>
	         <td>
	         </td>
	    	 <td>审核日期</td>
	         <td>
	         	<input id="mqeSupervisorDate" name="mqeSupervisorDate" isDate="true" value="<s:date name='mqeSupervisorDate' format="yyyy-MM-dd"/>" />
	         </td>
	      	 <td>MQE主管</td>
	         <td>
	            <input id="mqeSupervisor" name="mqeSupervisor" value="${mqeSupervisor}" isUser="true" style="float:left;" hiddenInputId="mqeSupervisorLog"/>
	            <input type="hidden" name="mqeSupervisorLog" id="mqeSupervisorLog"  value="${mqeSupervisorLog}" />
	          </td>
	     </tr>	     	     	     
	     </security:authorize>
	      <tr><td style="text-align:center;background-color: CornflowerBlue;color: white;font-weight: bold;" colspan='6'>供应商回复改善报告</td></tr>
	      <tr>
	         <td>暂定对策实施</td>
	         <td colspan='5'><textarea rows="5" cols="3" id="tempCountermeasures" name="tempCountermeasures">${tempCountermeasures }</textarea></td>
	      </tr>
	      <tr>
	          <td>真因定义及验证</td>
	          <td colspan='5'><textarea rows="5" cols="3" id="trueReasonCheck" name="trueReasonCheck">${trueReasonCheck }</textarea></td>
	      </tr>
	     <tr>
	         <td>永久对策实施</td>
	          <td colspan='5'><textarea rows="5" cols="3" id="countermeasures" name="countermeasures">${countermeasures }</textarea></td>
	     </tr>
	     <tr>
	         <td>预防再发生</td>
	         <td colspan='5'><textarea rows="5" cols="3" id="preventHappen" name="preventHappen">${preventHappen }</textarea></td>
	     </tr>
	     <tr>
	        <td>佐证资料</td>
	        <td colspan='3'>
	           <input type="hidden" name="supplierFile" id="supplierFile" isFile="true"  value="${supplierFile}" />
	        </td>
	         <td>经办</td>
	         <td colspan='5'>
	            <input id="supplierProcesser" name="supplierProcesser" value="${supplierName}"  />
	            <input type="hidden" name="supplierProcesserLog" id="supplierProcesserLog"  value="${supplierCode}" />
	         </td>
	     </tr>
	     <tr><td style="text-align:center;background-color: CornflowerBlue;color: white;font-weight: bold;" colspan='6'>SQE确认改善报告</td></tr>
	     <tr>
	         <td>SQE意见</td>
	         <td colspan='5'><textarea rows="2" cols="3" id="sqeComfirmOpinion" name="sqeComfirmOpinion">${sqeComfirmOpinion }</textarea></td>	        
	     </tr>	     
	     <tr>
	    	 <td></td>
	         <td>
	         </td>
	    	 <td>审核日期</td>
	         <td>
	         	<input id="sqeComfirmDate" name="sqeComfirmDate" isDate="true" value="<s:date name='sqeComfirmDate' format="yyyy-MM-dd"/>" />
	         </td>
	      	 <td>SQE确认人</td>
	         <td >
	            <input id="sqeChecker" name="sqeChecker" style="float:left;" hiddenInputId="sqeCheckerLog" isUser="true" value="${sqeChecker}"/>
	            <input type="hidden" name="sqeCheckerLog" id="sqeCheckerLog"  value="${sqeCheckerLog}" />
	         </td>
	     </tr>	
	     <tr><td style="text-align:center;background-color: CornflowerBlue;color: white;font-weight: bold;" colspan='6'>SQE主管审核</td></tr>
	     <tr>
	         <td>SQE主管意见</td>
	         <td colspan='5'><textarea rows="2" cols="3" id="sqeApprovalerOpinion" name="sqeApprovalerOpinion">${sqeApprovalerOpinion }</textarea></td>	        
	     </tr>	     
	     <tr>
	    	 <td></td>
	         <td>
	         </td>
	    	 <td>审核日期</td>
	         <td>
	         	<input id="sqeApprovalerDate" name="sqeApprovalerDate" isDate="true" value="<s:date name='sqeApprovalerDate' format="yyyy-MM-dd"/>" />
	         </td>
	      	 <td>SQE主管</td>
			  <td>
	             <input id="sqeApprovaler" name="sqeApprovaler" value="${sqeApprovaler}" isUser="true" style="float:left;" hiddenInputId="sqeApprovalerLog"/>
	             <input type="hidden" name="sqeApprovalerLog" id="sqeApprovalerLog"  value="${sqeApprovalerLog}" />
	          </td>
	     </tr>	          
	     <tr><td style="text-align:center;background-color: CornflowerBlue;color: white;font-weight: bold;" colspan='6'>MQE追踪改善效果</td></tr>
	     <tr>
	         <td>供应商改善效果确认</td>
	         <td colspan='5'><textarea rows="5" cols="3" id="checkResult" name="checkResult">${checkResult }</textarea></td>
	     </tr>		    
	     <tr>
	         <td>SQE追踪完成时间</td>
	         <td>
	           <input id="sqeFinishDate" name="sqeFinishDate" isDate="true" value="<s:date name='sqeFinishDate' format="yyyy-MM-dd"/>" />
	         </td>
	          <td>确认日期</td>
	          <td>  <input id="checkResultDate" name="checkResultDate" isDate="true" value="<s:date name='checkResultDate' format="yyyy-MM-dd"/>" /></td>           
	          <td>MQE确认人 </td>
			  <td>
			  	${mqeChecker}
	          </td>	         
	     </tr>
	</tbody> 	 
</table>
