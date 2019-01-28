<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<table class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
<caption style="height: 25px"><h2>巡检报告</h2></caption>
<caption style="text-align:right;padding-bottom:4px;">编号:${inspectionNo}&nbsp;&nbsp;&nbsp;</caption>
<tr>
	<td style="width:115px;"><span style="color:red">*</span>厂区</td>
	<td>
		 <s:select list="processSections" 
 			  theme="simple"
			  listKey="value"  
 			  listValue="name" 
			  labelSeparator=""
 			  name="processSection" 
			  emptyOption="true" 
 			  cssClass="{required:true,messages:{required:'必填'}}"></s:select> 
		  <input type="hidden" name="businessUnitName" id="businessUnitName" value="${businessUnitName}" />
	</td>
	<td style="width:140px;"><span style="color:red">*</span>工厂</td>
	<td>
		 <s:select list="factorys" 
 			  theme="simple"
			  listKey="value"  
 			  listValue="name" 
			  labelSeparator=""
 			  name="factory" 
 			  id="factory"
			  emptyOption="true" 
 			  cssClass="{required:true,messages:{required:'必填'}}"></s:select> 
	</td>	
	<td style="width:140px;"><span style="color:red">*</span>机种</td>
	<td style="">
		<input name="machineNo" value="${machineNo}" id="machineNo"  class="{required:true,messages:{required:'必填'}}"></input>
	</td>
	<td><span style="color:red">*</span>工序</td>
	<td>
		<s:select list="mfg_process_bus" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  name="workProcedure"
			  id="workProcedure"
			  cssClass="{required:true,messages:{required:'必填'}}"
			  emptyOption="true"></s:select>
	</td>	
</tr>
<tr>
	<td>产品类别</td>
	<td>
		<s:select list="mfg_category" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  name="productModel"
			  emptyOption="false"></s:select>
	</td>
	<td>产品阶段</td>
	<td>
		<s:select list="productStages" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  name="productStage"
			  emptyOption="false"></s:select>
	</td>
	<td style="width:140px;">班别</td>
	<td style="">
		<s:select list="mfg_work_group_type" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  name="workGroupType"
			  emptyOption="false"></s:select>
	</td>
	<td style="width:140px;"><span style="color:red">*</span>生产机台</td>
	<td style="">
		<input name="produceMachine" value="${produceMachine}" id="produceMachine"  class="{required:true,messages:{required:'必填'}}"></input>
	</td>			
</tr>
<tr>
	<td>批次数量</td>
	<td>
		<input id="stockAmount" name="stockAmount" value="${stockAmount}" ></input>
		<s:select list="amountUnits" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  cssStyle="width:50px"
			  name="amountUnit"
			  id="amountUnit"></s:select>
	</td>				
	<td style="width:140px;">检验员</td>
	<td style="">
		<input id="inspector" name="inspector" value="${inspector}" />
	</td>
	<td ><span style="color:red">*</span>选择审核人</td>
	<td >
		<input type="hidden" id="choiceAuditLoinMan" name="choiceAuditLoinMan" value="${choiceAuditLoinMan}"/>
		<input  id="choiceAuditMan" name="choiceAuditMan" value="${choiceAuditMan}" onclick='selectObj("选择审核人","choiceAuditLoinMan","choiceAuditMan","loginName");' readonly="readonly"/>
	</td>
	<td colspan="2" buttd=true>
		<button class='btn' type="button" onclick="loadCheckItems();" name="loadButton">
			<span><span><b class="btn-icons btn-icons-search"></b>加载检验项目</span></span>
		</button>
	</td>
</tr>
<tr>
	<td colspan="8" style="padding:0px;" id="checkItemsParent">
		<div style="overflow-x:auto;overflow-y:hidden;">
			<%@ include file="check-items.jsp"%>
		</div>
	</td>
</tr>
<tr>
	<td>备注</td>
	<td colspan="2">
		<textarea  rows=2 id="remark" name="remark">${remark}</textarea>	
	</td>
	<td>上传附件</td>
	<td colspan="2" buttd=true>
	<button class='btn' type="button" onclick="uploadFiles('attachmentFilesShow','attachmentFiles');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
		           <span id='attachmentFilesShow'></span>
					<input type="hidden"  isFile="true" id="attachmentFiles" name="attachmentFiles" value="${attachmentFiles}"/>
	</td>
</tr>
<tr>
	<td>检验判定</td>
	<td style="border-top:0px;">
		<s:select list="iqc_okorng" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  id="inspectionConclusion"
			  name="inspectionConclusion"
			  emptyOption="false"></s:select>
	</td>
	<td>处理方式</td>
	<td>
		<s:select list="mfg_processing_result" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  id="processingResult"
			  name="processingResult"
			  emptyOption="true"></s:select>
	</td>
	<td>检验日期</td>
	<td>
		<s:date name="inspectionDate" format="yyyy-MM-dd HH:mm"/>
	</td>
	<td style="width:140px;">巡检状态</td>
	<td style="">
		${reportState}
	</td>		
</tr>
<tr>
	<td>审核日期</td>
	<td>
		<input type="hidden" id="auditTime" name="auditTime" value="<s:date name="auditTime" format="yyyy-MM-dd"/>" />
		<s:date name="auditTime" format="yyyy-MM-dd"/>
	</td>
		<td>
		审核意见<span id="auditTextSpan"  style="color:red;"></span>
	</td>
	<td colspan="5">
		<textarea lanuch="stage2" rows="2" cols="3" id="auditText" name="auditText">${auditText}</textarea>
	</td>
</tr>
</table>