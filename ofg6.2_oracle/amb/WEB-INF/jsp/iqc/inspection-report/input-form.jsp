<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" %>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String changeView=(String)ActionContext.getContext().get("changeView");
%>
<table class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
		<caption style="height: 25px">
			<%
				String checkBomMaterialType = ActionContext.getContext().getValueStack().findString("checkBomMaterialType");
				String storageType = ActionContext.getContext().getValueStack().findString("storageType");
				String tableName = "";
				if(StringUtils.isNotEmpty(checkBomMaterialType)){
					tableName = checkBomMaterialType;
				}else if(StringUtils.isNotEmpty(storageType)){
					tableName = storageType;
				}
				if(tableName.endsWith("检验")){
					tableName = tableName.substring(0,tableName.length()-2);
				}
			%>
			<h2>
			<%=tableName%>检验报告
<%-- 			<%=ContextUtils.getSubCompanyName()==null?"":"("+ContextUtils.getSubCompanyName()+")"%> --%>
			</h2>
		</caption>
		<caption style="text-align:right;padding-bottom:4px;">编号:${inspectionNo}&nbsp;&nbsp;&nbsp;</caption>
		<tbody style="border-style:none;">
		<tr>
			<td colspan="8">
			<table  class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
			<tr>
		   <td colspan='8'>
		     <div style="float:left;">
		   <span style="font-size:30px;color:red;">上传检验数据</span>
		   <button class='btn' type="button" onclick="uploadFiles('iqcInspectionDatas','fileAll');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
		            <input type="hidden" name="hisAttachmentFilesAll" value='${fileAll}'></input>
		            <input type="hidden" name="fileAll" id="fileAll" value='${fileAll}'></input>
		           <span id='iqcInspectionDatas'></span>
		</div>
		   </td>
		</tr>
		<tr>
			<td style="width:140px;text-align: center;"><span style="color:red">*</span>厂区</td> 
			<td>
				 <s:select list="processSections" 
					  theme="simple"
					  listKey="value" 
					  listValue="name" 
					  labelSeparator=""
					  name="processSection"
					  id="processSection"
					  emptyOption="true"
					  onchange="loadCheckItems()"
					  cssClass="{required:true,messages:{required:'必填'}}"></s:select> 
				  <input type="hidden" name="businessUnitCode" id="businessUnitCode" value="${businessUnitCode}" />
			</td>
			<td style="width:140px;text-align: center;"><span style="color:red">*</span>物料编码</td>
			<td>
				<% if("true".equals(changeView)){ %>
					<input style="float:left;" name="checkBomCode"  id="checkBomCode" value="${checkBomCode}" hisValue="${checkBomCode}" class="{required:true,messages:{required:'物料编码不能为空'}}" onblur="loadCheckItems()" onchange="supplierValidate(this);"/>
				<%}else{ %>
					<span>${checkBomCode}</span>
					<input type="hidden" name="checkBomCode" id="checkBomCode" value="${checkBomCode}" />
				<%}%>
			</td>
			<td style="width:140px;text-align: center;"><span style="color:red">*</span>物料名称</td>
			<td>
				<% if("true".equals(changeView)){ %>
					<input name="checkBomName" id="checkBomName" value="${checkBomName}" />
				<%}else{ %>
						<input style="width: 98%;border:none;background: none;" type="text" name="checkBomName" id="checkBomName" value="${checkBomName}" />
				<%}%>
			</td>
			<td style="width:140px;text-align: center;"><span style="color:red">*</span>供应商编号</td>
			<td>
				<input style="float:left;" name="supplierCode" id="supplierCode" hisValue="${supplierCode}" value="${supplierCode}" class="{required:true,messages:{required:'不能为空'}}" onchange="supplierValidate(this);"/>
				<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="supplierClick()" href="javascript:void(0);" title="选择供应商"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>				
			</td>			
		</tr>
		<tr>
			<td style="width:140px;text-align: center;">供应商名称</td>
			<td>
				<input name="supplierName" value="${supplierName}" id="supplierName" title="${supplierName}"/>
			</td>
			<td style="width:140px;text-align: center;">Lot No</td>
			<td>
				<input id="lotNo" name="lotNo" value="${lotNo}"/>
			</td>
			<td style="width:140px;text-align: center;">订单号</td>
			<td>
				<% if("true".equals(changeView)){ %>
					<input id="orderNo" name="orderNo" value="${orderNo}"/>
				<%}else{ %>
					<span>${orderNo}</span>
				<%}%>
			</td>
			<td style="width:140px;text-align: center;">行号</td>
			<td>
				<% if("true".equals(changeView)){ %>
					<input id="entryId" name="entryId" value="${entryId}"/>
				<%}else{ %>
					<span>${entryId}</span>
				<%}%>				
			</td>			
		</tr>
		<tr>
			<td style="width:140px;text-align: center;">接受单号</td>
			<td>
				<% if("true".equals(changeView)){ %>
					<input id="erpInspectionNo" name="erpInspectionNo" value="${erpInspectionNo}"/>
				<%}else{ %>
					<span>${erpInspectionNo}</span>
				<%}%>
				<input type="hidden" id="acceptNo" name="acceptNo" value="${acceptNo}"/>				
			</td>		
			<td style="width:140px;text-align: center;"><span style="color:red">*</span>进料日期</td>
			<td>
			<% if("true".equals(changeView)){ %>
				<input name="enterDate" id="enterDate" value="<s:date name="enterDate" format="yyyy-MM-dd HH:mm"/>" readonly="readonly" class="line" />
			<%}else{%>
				<span><s:date name="enterDate" format="yyyy-MM-dd HH:mm"/></span>
			<%}%>
			</td>
			<td style="width:140px;text-align: center;"><span style="color:red">*</span>进料数量</td>
			<td>
				<%
					Double val = (Double)ActionContext.getContext().getValueStack().findValue("stockAmount");
					if(val == null){
						val = 0.0d;
					}
					DecimalFormat df = new DecimalFormat("#.#####");
				%>
				<input  name="stockAmount" id="stockAmount" value="${stockAmount}" onblur="loadCheckItems()" class="{required:true,number:true,messages:{required:'来料数量不能为空'}}"/>
			</td>
			<td style="width:140px;text-align: center;">单位</td>
			<td>
				<input name="units" id="units" value="${units}" />
			</td>
		</tr>
		<tr style="width:140px;">
			<td style="width:140px;text-align: center;">承认状态</td>
			<td>
				<input name="acknowledgeState" id="acknowledgeState" value="${acknowledgeState}"/>
			</td>
			<td style="width:140px;text-align: center;">承认书版本</td>
			<td>
				<input name="acknowledgeVersion" id="acknowledgeVersion" value="${acknowledgeVersion}" />
			</td>
			<td style="width:140px;text-align: center;"><span style="color:red">*</span>物料类别</td>
			<td>
				 <input name="checkBomMaterialType" id="checkBomMaterialType" style="float:left;" value="${checkBomMaterialType}" class="{required:true,messages:{required:'必填'}}" onclick="materialType()"/>
				 <a class="small-button-bg" style="margin-left:2px;float:left;" onclick="materialType()" href="javascript:void(0);" title="选择物料类别"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
			</td>
			<td style="width:140px;text-align: center;"><span style="color:red">*</span>产品阶段</td>
			<td>
				<s:select list="iqc_product_stage" 
					  theme="simple"
					  listKey="value" 
					  listValue="name" 
					  labelSeparator=""
					  id="productStage"
					  name="productStage"
					  emptyOption="true"
					  cssClass="{required:true,messages:{required:'必填'}}"></s:select>
			</td>			
		</tr>
		<tr>
			<td style="width:140px;text-align: center;">特殊检验状态</td>
			<td>
			<s:select list="sampleSchemeTypes" 
					  theme="simple"
					  listKey="name" 
					  listValue="name" 
					  labelSeparator=""
					  id="sampleSchemeType"
					  name="sampleSchemeType"
					  emptyOption="false"></s:select>
			</td>
			<td style="width:140px;text-align: center">客户代码</td>
			<td>
				<input name="customerCode" id="customerCode" value="${customerCode}" />
			</td>	
			<td style="width:140px;text-align: center">供应商状态</td>
			<td>
				<input name="supplierState" id="supplierState" value="${supplierState}" />
			</td>			
			<td style="width:140px;text-align: center;">驻厂检验</td>
			<td>
				<s:select list="isOks" 
				  theme="simple"
				  listKey="value" 
				  listValue="name" 
				  labelSeparator=""
				  id="isFactory"
				  name="isFactory"
				  emptyOption="false"></s:select>			
			</td>		
		</tr>
			</table>
			</td>
		</tr>
		
		<tr>
			<td colspan="6">
				<table  class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
					<tr>
						<td style="width:15%;text-align: center;">检验类别</td>
						<td style="width:20%;text-align: center;">检验内容</td>
						<td style="width:15%;text-align: center;">检验结果</td>
						<td style="width:30%;text-align: center;">描述</td>
						<td style="width:10%;text-align: center;">判定</td>
						<td style="width:10%;text-align: center;">录入员</td>
					</tr>
					<tr>
						<td rowspan="3" style="text-align: center;">包装/资料核对</td>
						<td style="text-align: ;">
							<s:iterator value="packing1" id="option">
								<input id="packingFir${option.name}"  fileName="packingFir" control="true" type="checkbox" onclick="getCheckedBoxVal();" <s:if test="%{packingFir.indexOf(#option.value)>-1}"> checked="checked" </s:if> value="${option.value}" />
								<label for="packingFir${option.name}">${option.name}</label>
						    </s:iterator> 
							<%-- <s:checkboxlist
								theme="simple" list="packing1" listKey="value" listValue="name"
								name="packingFir" value="packingFir">
							</s:checkboxlist> --%>

						</td>
						<td style="text-align: center;">
							<s:select list="iqc_okorng" 
							  theme="simple"
							  listKey="name" 
							  listValue="name" 
							  labelSeparator=""
							  name="packingFirResult"
							  emptyOption="false"></s:select>
						</td>
						<td rowspan="3" style="text-align: center;">
							<textarea name="packingText" id="packingText" rows="" cols="">${packingText}</textarea>
						</td>
						<td rowspan="3" style="text-align: center;">
							<s:select list="iqc_okorng" 
							  theme="simple"
							  listKey="value" 
							  listValue="name" 
							  labelSeparator=""
							  id="packingResult"
							  name="packingResult"
							  emptyOption="false"></s:select>
						</td>
						<td rowspan="3" style="text-align: center;">
							<input style="width:100px;" id="packingInspcetionMan"   name="packingInspcetionMan" value="${packingInspcetionMan}" />
						</td>
					</tr>
					<tr>
						<td style="text-align: ;">
							 <s:iterator value="packing2" id="option">
								<input id="packingSec${option.name}"   fileName="packingSec" onclick="getCheckedBoxVal();"  control="true" type="checkbox" <s:if test="%{packingSec.indexOf(#option.value)>-1}"> checked="checked" </s:if> value="${option.value}" />
								<label for="packingSec${option.name}">${option.name}</label>
						    </s:iterator> 
							<%-- <s:checkboxlist
								theme="simple" list="packing2" listKey="value" listValue="name"
								name="packingSec" value="packingSec">
							</s:checkboxlist> --%>
						</td>
						<td style="text-align: center;">
							<s:select list="iqc_okorng" 
							  theme="simple"
							  listKey="name" 
							  listValue="name" 
							  labelSeparator=""
							  name="packingSecResult"
							  emptyOption="false"></s:select>
						</td>
					</tr>
					<tr>
						<td style="text-align: ;">
<%-- 						<s:iterator value="packing3" id="option">
								<input id="packingTre${option.id}"  control="true" type="checkbox" <s:if test="%{#option.value.equals(packingTre)}"> checked="checked" </s:if> value="${option.value}" name="packingTre"/>
								<label for="packingTre${option.id}">${option.name}</label>
						    </s:iterator> --%>
							<s:checkboxlist
								theme="simple" list="packing3" listKey="value" listValue="name"
								name="packingTre" value="packingTre">
							</s:checkboxlist>				  
						</td>
						<td style="text-align: center;">
							<s:select list="iqc_okorng" 
							  theme="simple"
							  listKey="name" 
							  listValue="name" 
							  labelSeparator=""
							  name="packingTreResult"
							  emptyOption="false"></s:select>
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">检验类别</td>
						<td style="text-align: center;">检验内容</td>
						<td style="text-align: center;">检验结果</td>
						<td style="text-align: center;">报告编号</td>
<%-- 						<td colspan="2"  style="text-align: center;">描述   <a class="small-button-bg" style="float:right;" onclick="testValidate(this)" href="#" title="测试验证"><span class="ui-icon btn-icons-search" style='cursor:pointer;'></span></a></td>
 --%>						<td colspan="2"  style="text-align: center;">描述<%-- <button class='btn' style="float:right;"  onclick="testValidate(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b></span></span></button> --%></td>
					</tr>
					<tr>
						<td style="text-align: center;"> HSF符合性检查</td>
						<td style="text-align: center;">
							<textarea rows="1" cols="2" name="hisInspectionItem">${hisInspectionItem}</textarea>
<%-- 							${hisInspectionItem} --%>
						</td>
						<td style="text-align: center;">
							<s:select list="iqc_okorng" 
							  theme="simple"
							  listKey="name" 
							  listValue="name" 
							  labelSeparator=""
							  name="hisPackingResult"
							  emptyOption="false"></s:select>
<%-- 							${hisPackingResult} --%>
<%-- 							<input id="hisPackingResult" name="hisPackingResult" value="${hisPackingResult}"/> --%>
<%-- 							${hisPackingResult} --%>
						</td>
						<td style="text-align: center;">
							<input name="hisReportNo" id="hisReportNo" value="${hisReportNo}" type="hidden" />
							 <input name="hisReportId" id="hisReportId" value="${hisReportId}" type="hidden" />
							 <span style="text-decoration:underline;" id="hsfSpan1" onclick="hisReportInput(this);">${hisReportNo}</span>
							 <span id="hsfSpan2" ></span>
						</td>
						<td colspan="2" style="text-align: center;">
							<textarea rows="1" cols="2" id="hisText" name="hisText">${hisText}</textarea>
							<%-- ${hisText} --%>
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">可靠性测试</td>
						<td style="text-align: center;">
							<textarea rows="1" cols="2" name="ordInspectionItem">${ordInspectionItem}</textarea>
<%-- 							${ordInspectionItem} --%>
						</td>
						<td style="text-align: center;">
<%-- 							<input id="ordPackingResult" name="ordPackingResult" value="${ordPackingResult}"/> --%>
<%-- 							${ordPackingResult} --%>
							<s:select list="iqc_okorng" 
							  theme="simple"
							  listKey="name" 
							  listValue="name" 
							  labelSeparator=""
							  name="ordPackingResult"
							  emptyOption="false"></s:select>
						</td>
						<td style="text-align: center;">
							<input name="ordReportNo" id="ordReportNo" value="${ordReportNo}" type="hidden" />
							<input name="ordReportId" id="ordReportId" value="${ordReportId}" type="hidden" />
							<span style="text-decoration:underline;" id="ortSpan1" onclick="ordReportInput(this);">${ordReportNo}</span>
							<span id="ortSpan2" ></span>
						</td>
						<td colspan="2" style="text-align: center;">
							<textarea rows="1" cols="2" id="ordText" name="ordText">${ordText}</textarea>
						</td>
					</tr>
					<security:authorize ifAnyGranted="iqc-ort-test">
					<tr>
						<td style="text-align: center;">可靠性测试(1月)</td>
						<td>
							<input name="ordReportNo1" id="ordReportNo1" value="${ordReportNo1}" type="hidden" />
							<input name="ordReportId1" id="ordReportId1" value="${ordReportId1}" type="hidden" />
							<span style="text-decoration:underline;" id="ort1Span1" onclick="ordReportInput1(this);">${ordReportNo1}</span>
							<span id="ort1Span2" ></span>
						</td>
						<td style="text-align: center;">可靠性测试(3月)</td>
						<td>
							<input name="ordReportNo3" id="ordReportNo3" value="${ordReportNo3}" type="hidden" />
							<input name="ordReportId3" id="ordReportId3" value="${ordReportId3}" type="hidden" />
							<span style="text-decoration:underline;" id="ort3Span1" onclick="ordReportInput3(this);">${ordReportNo3}</span>
							<span id="ort3Span2" ></span>
						</td>
						<td colspan="2" style="text-align: center;">
							<textarea rows="2" cols="2" id="ordTextSpecial" name="ordTextSpecial">${ordTextSpecial}</textarea>
						</td>						
					</tr>	
					</security:authorize>				
				</table>
			</td>
		</tr>
		<tr>
			<td style="width:150px;text-align: center;"><span style="color:red;">*</span>检验日期</td>
			<td>
				<input name="inspectionDate" id="inspectionDate" value="<s:date name="inspectionDate" format="yyyy-MM-dd HH:mm"/>" readonly="readonly" class="line"/>
				<input type="hidden" name="schemeStartDate" value="${schemeStartDateStr}"/>
			</td>
			<td style="text-align: center;"><span style="color:red;">*</span>审核人</td>
			<td>
				<input type="hidden" id="auditLoginMan" name="auditLoginMan" value="${auditLoginMan}"/>
				<input id="auditMan" name="auditMan" value="${auditMan}" onclick='selectObj("选择审核人","auditLoginMan","auditMan","loginName");' readonly="readonly" class="{required:true,messages:{required:'必填'}}"/>
			</td>
			<td style="text-align: center;">MQE确认人</td>
			<td>
				<input type="hidden" id="lastStateLoginMan" name="lastStateLoginMan" value="${lastStateLoginMan}"/>
				<input style="float:left;" id="lastStageMan" name="lastStageMan" value="${lastStageMan}" onclick='selectObj("选择上级审核人","lastStateLoginMan","lastStageMan","loginName");' readonly="readonly" />
				<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="clearValue('lastStageMan','lastStateLoginMan')" href="javascript:void(0);" title="清空"><span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
			</td>
		</tr>
		<tr>
			<td style="text-align: center;">备注</td>
			<td colspan="4">
				<textarea name="remark" rows="2" cols="2">${remark}</textarea>
			</td>
			<td style="text-align: center;">
				<button class='btn' type="button" onclick="choiceWaitCheckItem();">
					<span><span><b class="btn-icons btn-icons-search"></b>领取待检验项目</span></span>
				</button>
			</td>
		</tr>
		<tr>
			<td colspan="6" style="padding:0px;" id="checkItemsParent">
				<div style="overflow-x:auto;overflow-y:hidden;padding-bottom:5px;border-bottom-style: none;">
					<%-- <s:if test="%{sampleSchemeType == '免检'}">
						<span style="padding:10px 0px 10px 6px;font-weight:bold;">
							免检物料,没有检验项目
						</span>
					</s:if>
					<s:else>
					   <%@ include file="check-items.jsp"%>
					</s:else> --%>
					<%@ include file="check-items.jsp"%>
				</div>
			</td>
		</tr>
		<tr>
		<td colspan="6">
		<table  class="form-table-border-left" style="width:100%;margin: auto;border:0px;">		
		<tr>
			<td style="text-align: center;">外观合格数</td>
			<td >
				<input id="appearanceAmount" name="appearanceAmount" value="${appearanceAmount}" onkeyup="mustNum(this);caculateBadRate(this)"/>
				<span id="appearanceAmount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span>
			</td>
			<td style="text-align: center;">外观不良数</td>
			<td >
				<input id="appearanceUnAmount" name="appearanceUnAmount" value="${appearanceUnAmount}" onkeyup="mustNum(this);caculateBadRate(this)"/>
				<span id="appearanceUnAmount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span>
			</td>
			<td style="text-align: center;">外观合格率</td>
			<td >
				<input id="appearanceAmountRate" name="appearanceAmountRate" value="${appearanceAmountRate}"  />%
				<input type="hidden" id="wgBadRate" name="wgBadRate" value="${wgBadRate}" />
			</td>
			<td style="text-align: center;">外观检验员</td>
			<td>
				<input id="wgInspector" name="wgInspector" value="${wgInspector}" />
			</td>			
		</tr>
		<tr>	
			<td style="text-align: center;">特性合格数</td>
			<td >
				<input id="qualifiedAmount" name="qualifiedAmount" value="${qualifiedAmount}" onkeyup="mustNum(this);caculateBadRate(this)"/>
				<input type="hidden" id="functionAmount" name="functionAmount" value="${functionAmount}" />
				<span id="qualifiedAmount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span>
			</td>
			<td style="text-align: center;">特性不良数</td>
			<td >
				<input id="unqualifiedAmount" name="unqualifiedAmount" value="${unqualifiedAmount}" onkeyup="mustNum(this);caculateBadRate(this)"/>
				<input type="hidden" id="functionUnAmount" name="functionUnAmount" value="${functionUnAmount}" />
				<span id="unqualifiedAmount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span>
			</td>
			<td style="text-align: center;">特性合格率</td>
			<td >
				<input id="qualifiedRate" name="qualifiedRate" value="${qualifiedRate}" />%
				<input type="hidden" id="qualifiedUnRate" name="qualifiedUnRate" value="${qualifiedUnRate}" />
				<input type="hidden" id="functionAmountRate" name="functionAmountRate" value="${functionAmountRate}" />
				<input type="hidden" id="functionUnAmountRate" name="functionUnAmountRate" value="${functionUnAmountRate}" />				
				<input type="hidden" id="checkAmount" name="checkAmount" value="${checkAmount}" />
				<input type="hidden" id="wgBadDescrible" name="wgBadDescrible" value="${wgBadDescrible}"/>
				<input type="hidden" id="gnBadDescrible" name="gnBadDescrible" value="${gnBadDescrible}"/>
				<input type="hidden" id="ccBadDescrible" name="ccBadDescrible" value="${ccBadDescrible}"/>
			</td>
			<td style="text-align: center;">特性检验员</td>
			<td >
				<input id="qualifiedInspector" name="qualifiedInspector" value="${qualifiedInspector}" />
			</td>			
		</tr>
		<tr>
			<td style="text-align: center;">尺寸合格数</td>
			<td >
				<input id="sizeAmount" name="sizeAmount" value="${sizeAmount}"  onkeyup="mustNum(this);caculateBadRate(this)"/>
				<span id="sizeAmount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span>
			</td>
			<td style="text-align: center;">尺寸不良数</td>
			<td >
				<input id="sizeUnAmount" name="sizeUnAmount" value="${sizeUnAmount}" onkeyup="mustNum(this);caculateBadRate(this)"/>
				<span id="sizeUnAmount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span>
			</td>
			<td style="text-align: center;">尺寸合格率</td>
			<td >
				<input id="sizeAmountRate" name="sizeAmountRate" value="${sizeAmountRate}"  />%
				<input type="hidden" id="sizeUnAmountRate" name="sizeUnAmountRate" value="${sizeUnAmountRate}" />
			</td>
			<td style="text-align: center;">尺寸检验员</td>
			<td >
				<input id="sizeInspector" name="sizeInspector" value="${sizeInspector}" />
			</td>			
		</tr>
		<tr>				
			<td style="text-align: center;">综合判定</td>
			<td >
				<span>${inspectionConclusion}</span>
				<input name="inspectionConclusion" id="inspectionConclusion" value="${inspectionConclusion}" type="hidden" />
			</td>
			<td style="text-align: center;">处理方式</td>
			<td >
				<input name="processingResult" id="processingResult" value="${processingResult}" type="text" style="width: 90%;border:none;text-align: center;background: none;" readonly="readonly"/>
			</td>
			<td style="text-align: center;">管理编号</td>
			<td>
				<input name="managerNo" id="managerNo" value="${managerNo}" type="text" />
			</td>
			<td style="text-align: center;">异常单号</td>
			<td style="text-align: ;"><input name="exceptionNo" id="exceptionNo" value="${exceptionNo}" type="hidden" />
				 <input name="exceptionId" id="exceptionId" value="${exceptionId}" type="hidden" />
				 <span style="text-decoration:underline;" onclick="exceptionInput(this);">${exceptionNo}</span>
			</td>			
		</tr>
		<tr>
			<td style="text-align: center;">Mrb单号</td>
			<td style="text-align: center;">
				<input id="sqeMrbReportNo" name="sqeMrbReportNo" value="${sqeMrbReportNo}" style="width: 90%;border:none;background: none;" readonly="readonly"/>
			</td>
			<td style="text-align: center;">退货单号</td>
			<td>
				<input id="returnReportNo" name="returnReportNo" value="${returnReportNo}" style="width: 90%;border:none;background: none;" readonly="readonly"/>
			</td>		
		<%if("欧菲-TP".equals(ContextUtils.getCompanyName())){%>
			<td style="text-align: center;">异常回复时间</td>
			<td style="text-align: center;">				
				<input id="sqeReplyTime" name="sqeReplyTime" value="<s:date name="sqeReplyTime" format="yyyy-MM-dd HH:mm"/>" style="width: 90%;border:none;background: none;" readonly="readonly"/>
			</td>
			<td style="text-align: center;">追踪完成时间</td>
			<td style="text-align: center;">				
				<input id="sqeCompleteTime" name="sqeCompleteTime" value="<s:date name="sqeCompleteTime" format="yyyy-MM-dd HH:mm"/>" style="width: 90%;border:none;background: none;" readonly="readonly"/>
			</td>
		<%}else{%>	
			<td ></td>
			<td ></td>
			<td ></td>
			<td ></td>
		<%}%>
		</tr>
		<tr>
		<td style="text-align: center;">上传附件</td>		
		   <td colspan='7'>
	   			<button class='btn' type="button" onclick="uploadFiles('showAttachmentFiles','attachmentFiles');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
	            <input type="hidden" name="hisAttachmentFiles" value='${attachmentFiles}'></input>
	            <input type="hidden" name="attachmentFiles" id="attachmentFiles" value='${attachmentFiles}'></input>
	           <span id='showAttachmentFiles'></span>
		   </td>		
		</tr>
		<tr>
			<td style="text-align: center;">审核时间</td>
			<td><s:date name="auditTime" format="yyyy-MM-dd HH:mm"/></td>
			<td style="text-align: center;">审核意见</td>
			<td colspan="5">
			 <input type="hidden" id="auditText" name="auditText" value='${auditText}'></input>
			 ${auditText}</td>
		</tr>
		<tr>
			<td style="text-align: center;">MQE确认</td>
			<td><s:date name="lastStateTime" format="yyyy-MM-dd HH:mm"/></td>
			<td style="text-align: center;">MQE确认意见</td>
			<td colspan="5">
			 <input type="hidden" id="lastStateText" name="lastStateText" value='${lastStateText}'></input>
			${lastStateText}</td>
		</tr>
		<tr>
			<td style="text-align: center;">重检时间</td>
			<td><s:date name="recheckTime" format="yyyy-MM-dd HH:mm"/></td>
			<td style="text-align: center;">重检意见</td>
			<td colspan="5">
			<input type="hidden" id="recheckText" name="recheckText" value='${recheckText}'></input>
			${recheckText}</td>
		</tr>								
	</table>
	</td>
	</tr>
	</tbody>										
</table>
