<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<table class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
<caption style="height: 25px"><h2>制程报告</h2></caption>
<caption style="text-align:right;padding-bottom:4px;">编号:${inspectionNo}&nbsp;&nbsp;&nbsp;</caption>
<tr>
		   <td colspan='8'>
		     <div style="float:left;">
		   <span style="font-size:30px;color:red;">上传检验数据</span>
		   <button class='btn' type="button" onclick="uploadFiles('iqcInspectionDatas','fileAll');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
		            <input type="hidden" name="hisAttachmentFilesAll" value='${fileAll}'></input>
		            <input type="hidden" name="fileAll" id="fileAll" value='${fileAll}'></input>
		            <input type="hidden" name="inspectionNo" id="inspectionNo" value='${inspectionNo}'></input>
		           <span id='iqcInspectionDatas'></span>
		</div>
		   </td>
		</tr>
<tr>
 	<td>二维码</td>
	<td>
	    <textarea rows="2" cols="3" name="qrCode" id="qrCode">${qrCode}</textarea>
	</td>
	<td>流程卡</td>
	<td>
		<input name="processCard" id="processCard" value="${processCard}" />
		<button class='btn' type="button" onclick="submitProcessCard('${mfgctx}/inspection/made-inspection/get-process-card.htm');"><span><span><b class="btn-icons btn-icons-submit"></b>确定</span></span></button>
	</td> 
	<td style="width:140px;text-align: center;"><span style="color:red">*</span>检验</td>
	<td style="">
		<s:select list="inspection_models"
			  theme="simple"
			  listKey="name" 
			  listValue="value" 
			  labelSeparator=""
			  cssClass="{required:true,messages:{required:'必填'}}"
			  name="inspectionPointType"
			  emptyOption="false"></s:select>
	</td>
	<td style="width:140px;text-align: center;"><span style="color:red">*</span>厂区</td>
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
</tr>
<tr>		
	<td style="width:140px;text-align: center;"><span style="color:red">*</span>工厂</td>
	<td>
		 <s:select list="factorys" 
 			  theme="simple"
			  listKey="value"  
 			  listValue="name" 
			  labelSeparator=""
 			  name="factory" 
 			  id="factory"
			  emptyOption="true" 
 			 ></s:select> 
	</td>
	<td style="width:140px;text-align: center;"><span style="color:red">*</span>工序</td>
	<td>
		<s:select list="mfg_process_bus" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  onchange="loadCheckItems();"
			  name="workProcedure"
			  id="workProcedure"
			  cssClass="{required:true,messages:{required:'必填'}}"
			  emptyOption="true"></s:select>
	</td>	

	<td style="width:140px;text-align: center;">工站</td>
	<td>
		<input id="section" name="section" value="${section}"></input>
	</td>
	<td style="width:140px;text-align: center;">产品类别</td>
	<td>
		<s:select list="mfg_category" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  name="productModel"
			  emptyOption="false"></s:select>
	</td>	
</tr>
<tr>		
	<td style="width:140px;text-align: center;">产品阶段</td>
	<td>
		<s:select list="productStages" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  name="productStage"
			  emptyOption="false"></s:select>
	</td>
	<td style="width:140px;text-align: center;">班别</td>
	<td style="">
		<s:select list="mfg_work_group_type" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  name="workGroupType"
			  emptyOption="false"></s:select>
	</td>
	<td style="width:140px;text-align: center;"><span style="color:red">*</span>机种</td>
	<td style="">
		<input name="machineNo" value="${machineNo}" id="machineNo" onblur="loadCheckItems();" class="{required:true,messages:{required:'必填'}}"></input>
	</td>
	<td style="width:140px;text-align: center;">批次数量</td>
	<td>
		<input id="stockAmount" name="stockAmount" value="${stockAmount}" onchange="loadCheckItems();"  ></input>
		<s:select list="amountUnits" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  cssStyle="width:50px"
			  name="amountUnit"
			  id="amountUnit"></s:select>
	</td>
</tr>
<tr>	
	<td style="width:140px;text-align: center;"><span style="color:red">*</span>检验日期</td>
	<td>
		<%-- <input  name="inspectionDate" id="inspectionDate" value="<s:date name="inspectionDate" format="yyyy-MM-dd" />" onchange="loadCheckItems();" class="{required:true,messages:{required:'必填'}}"></input> --%>
		<input id="inspectionDate" name="inspectionDate" value="<s:date name="inspectionDate" format="yyyy-MM-dd HH:mm"/>" onchange="loadCheckItems();" class="{required:true,messages:{required:'必填'}}"/>
	</td>
	<td style="width:140px;text-align: center;">检验员</td>
	<td style="">
		<input id="inspector" name="inspector" value="${inspector}" />
	</td>		
	<td style="width:140px;text-align: center;"><span style="color:red">*</span>生产机台</td>
	<td style="">
		<input name="produceMachine" value="${produceMachine}" id="produceMachine"  class="{required:true,messages:{required:'必填'}}"></input>
	</td>
	<td style="width:140px;text-align: center;">生产线</td>
	<td style="">
		<s:select list="productionLines" 
			  theme="simple"
			  listKey="name" 
			  listValue="name" 
			  labelSeparator=""
			  name="productionLine"
			  id="productionLine"
			  emptyOption="false"></s:select>
	</td>
</tr>
<tr>			
	<td style="width:140px;text-align: center;">供应商编码</td>
	<td style="">
		<input style="float:left;" name="supplierCode" id="supplierCode" value="${supplierCode}" />
		<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="supplierClick()" href="javascript:void(0);" title="选择供应商"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>	
	</td>
	<td style="width:140px;text-align: center;">供应商名称</td>
	<td style="">
		<input name="supplierName" value="${supplierName}" id="supplierName" title="${supplierName}"/>
	</td>
	<td style="width:140px;text-align: center;">客户</td>
	<td style="">
		<s:select list="customers" 
			theme="simple"
			listKey="value" 
			listValue="name" 
			id="customerName"
			name="customerName"
			emptyOption="true">
		</s:select>
	</td>
	<td style="width:140px;text-align: center;">客户机种</td>
	<td style="">
		<input id="customerModel" name="customerModel" value="${customerModel}" onclick="modelClick(this);"/>
	</td>
</tr>
<tr>		
	<td style="width:140px;text-align: center;"><span style="color:red">*</span>选择审核人</td>
	<td style="">
		<input type="hidden" id="choiceAuditLoinMan" name="choiceAuditLoinMan" value="${choiceAuditLoinMan}"/>
		<input  id="choiceAuditMan" name="choiceAuditMan" value="${choiceAuditMan}" onclick='selectObj("选择审核人","choiceAuditLoinMan","choiceAuditMan","loginName");' readonly="readonly"/>
	</td>
	<td style="width:140px;text-align: center;"></td>
	<td style="">
		
	</td>
	<td style="width:140px;text-align: center;"></td>
	<td style="">
		
	</td>		
	<td colspan="2">
		<button class='btn' type="button" onclick="choiceWaitCheckItem();">
			<span><span><b class="btn-icons btn-icons-search"></b>领取待检验项目</span></span>
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
<%-- <tr>
	<td  colspan="6" style="text-align: center;">设备参数检验记录</td>
</tr>
<tr>
	<td colspan="6" style="padding:0px;" id="plantParameterItems">
		<div style="overflow-x:auto;overflow-y:hidden;">
			<%@ include file="plant-parameter-items.jsp"%>
		</div>
	</td>
</tr> --%>
<%-- <tr>
	<td colspan="6" style="text-align: center;font-weight: bold;font-size: 14px;">物料信息</td>
</tr>
<tr>
	<td colspan="6">
		<table  class="form-table-border-left" style="border:0px;table-layout:fixed;">
			<tr>
				<td style="width: 5%;text-align: center;">操作</td>
				<td style="text-align: center;">供应商代码</td>
				<td style="text-align: center;">供应商批次号</td>
				<td style="text-align: center;">物料号</td>
				<td style="text-align: center;">入料批次号</td>
				<td style="text-align: center;">供应商批次库存数</td>
				<td style="text-align: center;">备注</td>
			</tr>
				<%int bcount=0; %>
		        <s:iterator value="mfgSupplierMessages" var="mfgSupplierMessages" id="mfgSupplierMessages" status="st">
				    <tr class="mfgSupplierMessagesTr">
				    	<td>
				    		<div style="margin:0 auto;width: 42px;">
				      		<a class="small-button-bg" style="float:left;" onclick='addRowHtml(this,"objId")' title="添加不合格品"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
							<a class="small-button-bg" style="margin-left:2px;float:left;" onclick='removeRowHtml("mfgSupplierMessagesTr",this,"IDX")' title="删除不合格品"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
							</div>
				 		</td>
						<td style="text-align: center;">
							<input id="supplierCode-<%=bcount %>" name="supplierCode-<%=bcount %>" filedName="supplierCode" value="${supplierCode}" style="width:80%;"/>
							<input id="supplierName-<%=bcount %>" name="supplierName-<%=bcount %>" filedName="supplierName" value="${supplierName}" type="hidden"/>
						</td>
						<td style="text-align: center;">
							<input id="supplierBatchNo-<%=bcount %>" name="supplierBatchNo-<%=bcount %>" filedName="supplierBatchNo" value="${supplierBatchNo}" style="width:80%;"/>
						</td>
						<td style="text-align: center;">
							<input id="supplierBomCode-<%=bcount %>" name="supplierBomCode-<%=bcount %>" filedName="supplierBomCode" value="${supplierBomCode}" style="width:80%;"/>
						</td>
						<td style="text-align: center;">
							<input id="supplierBomBatchNo-<%=bcount %>" name="supplierBomBatchNo-<%=bcount %>" filedName="supplierBomBatchNo" value="${supplierBomBatchNo}" style="width:80%;"/>
						</td>
						<td style="text-align: center;">
							<input id="supplierBatchNum-<%=bcount %>" name="supplierBatchNum-<%=bcount %>" filedName="supplierBatchNum" value="${supplierBatchNum}" style="width:80%;" class="{digits:true,messages:{digits:'必须是整数!'}}"/>
						</td>
						<td style="text-align: center;">
							<textarea rows="" cols="" id="supplierRemark-<%=bcount %>" name="supplierRemark-<%=bcount %>" filedName="supplierRemark"  style="width:80%;">${supplierRemark}</textarea>
						</td>
                     </tr>
                     <%bcount++; %>
                     <input type="hidden" id="IDX" value="<%=bcount %>"/>
                     
		        </s:iterator>
		</table>
	</td>
</tr>
<tr>
	<td colspan="6" style="text-align: center;font-weight: bold;font-size: 14px;">生产信息</td>
</tr>
<tr>
	<td colspan="6">
		<table  class="form-table-border-left" style="border:0px;table-layout:fixed;">
			<tr>
				<td style="width: 5%;text-align: center;">操作</td>
				<td style="text-align: center;">二维码</td>
				<td style="text-align: center;">机台</td>
				<td style="text-align: center;">作业员</td>
				<td style="text-align: center;">生产时间</td>
				<td style="text-align: center;">工单</td>
				<td style="text-align: center;">工站</td>
			</tr>
			<tr>
				<%int ccount=0; %>
		        <s:iterator value="manufactureMessages" var="manufactureMessages" id="manufactureMessages" status="st">
				    <tr class="manufactureMessagesTr">
				    	<td>
				    		<div style="margin:0 auto;width: 42px;">
				      		<a class="small-button-bg" style="float:left;" onclick='addRowHtml(this,"IDXA")' title="添加不合格品"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
							<a class="small-button-bg" style="margin-left:2px;float:left;" onclick='removeRowHtml("manufactureMessagesTr",this,"IDXA")' title="删除不合格品"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
							</div>
				 		</td>
						<td style="text-align: center;">
							<input type="hidden" id="qcCode-<%=ccount %>" name="qcCode-<%=ccount %>" filedName="qcCode" value="${qcCode}"  style="width:80%;"/>
							${qcCode}
						</td>
						<td style="text-align: center;">
							<input id="marchineNo-<%=ccount %>" name="marchineNo-<%=ccount %>"  filedName="marchineNo" value="${marchineNo}"  style="width:80%;"/>
						</td>
						<td style="text-align: center;">
							<input id="operator-<%=ccount %>" name="operator-<%=ccount %>"  filedName="operator" value="${operator}"  style="width:80%;"/>
						</td>
						<td style="text-align: center;">
							<input id="manufactureTime-<%=ccount %>" name="manufactureTime-<%=ccount %>"  filedName="manufactureTime" value="${manufactureTime}"  style="width:80%;"/>
						</td>
						<td style="text-align: center;">
							<input id="workOrderNo-<%=ccount %>" name="workOrderNo-<%=ccount %>"  filedName="workOrderNo" value="${workOrderNo}"  style="width:80%;"/>
						</td>
						<td style="text-align: center;">
							<input id="section-<%=ccount %>" name="section-<%=ccount %>"  filedName="section" value="${section}"  style="width:80%;"/>
						</td>
                     </tr>
                     <%ccount++; %>
                     <input type="hidden" id="IDXA" value="<%=ccount %>"/>
                     
		        </s:iterator>
				
			</tr>
		</table>
	</td>
</tr> --%>
<tr>
	<td style="width:140px;text-align: center;">检验判定</td>
	<td style="border-top:0px;">
		<span><s:if test="inspectionConclusion=='OK'">合格</s:if><s:if test="inspectionConclusion=='NG'">不合格</s:if></span>
		<input name="inspectionConclusion" id="inspectionConclusion" value="${inspectionConclusion}" type="hidden"></input>
	</td>
	<td style="width:140px;text-align: center;">处理方式</td>
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
	<td style="width:140px;text-align: center;">责任人</td>
	<td style="border-top:0px;">
		<input type="hidden" id="dutyManLogin" name="dutyManLogin" value="${dutyManLogin}"/>
		<input  id="dutyMan" name="dutyMan" value="${dutyMan}" onclick='selectObj("选择审核人","dutyManLogin","dutyMan","loginName");' readonly="readonly"/>
	</td>
	<td >异常单号</td>
	<td style="text-align: ;"><input name="exceptionNo" id="exceptionNo" value="${exceptionNo}" type="hidden" />
		 <input name="exceptionId" id="exceptionId" value="${exceptionId}" type="hidden" />
		 <span style="text-decoration:underline;" onclick="exceptionInput(this);">${exceptionNo}</span>
	</td>	
</tr>
<tr>
	<td style="width:140px;text-align: center;">
		审核意见<span id="auditTextSpan"  style="color:red;"></span>
	</td>
	<td colspan="7">
		<textarea lanuch="stage2" rows="2" cols="3" id="auditText" name="auditText">${auditText}</textarea>
	</td>
</table>