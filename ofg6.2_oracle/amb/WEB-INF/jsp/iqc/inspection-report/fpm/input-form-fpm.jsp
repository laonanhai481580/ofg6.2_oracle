<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" %>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String changeView=(String)ActionContext.getContext().get("changeView");
%>
<table class="form-table-border-left" style="width:98%;margin: auto;border:0px;">
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
			<%=tableName%>进料检验报告
			</h2>
		</caption>
		<caption style="text-align:right;padding-bottom:4px;">编号:${inspectionNo}&nbsp;&nbsp;</caption>
		<tr>
		   <td colspan='6'>
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
			<td ><span style="color:red">*</span>厂区</td> 
			<td>
				 <s:select list="businessUnits" 
					  theme="simple"
					  listKey="value" 
					  listValue="name" 
					  labelSeparator=""
					  name="processSection"
					  emptyOption="true"
					  cssClass="{required:true,messages:{required:'必填'}}"></s:select> 
				  <input type="hidden" name="businessUnitCode" id="businessUnitCode" value="${businessUnitCode}" />
			</td>		
			<td style="width: 200px;"><span style="color:red">*</span>物料编码</td>
			<td>
				<% if("true".equals(changeView)){ %>
					<input style="float:left;" name="checkBomCode"  id="checkBomCode" value="${checkBomCode}" hisValue="${checkBomCode}" class="{required:true,messages:{required:'物料编码不能为空'}}" onblur="loadCheckItems()"/>
				<%}else{ %>
					<span>${checkBomCode}</span>
					<input type="hidden" name="checkBomCode" id="checkBomCode" value="${checkBomCode}" />
				<%}%>
			</td>
			<td><span style="color:red">*</span>物料名称</td>
			<td>
				<% if("true".equals(changeView)){ %>
					<input name="checkBomName" id="checkBomName" value="${checkBomName}" />
				<%}else{ %>
					<span>
						${checkBomName}
						<input type="hidden" name="checkBomName" id="checkBomName" value="${checkBomName}" />
					</span>
				<%}%>
			</td>
		</tr>
		<tr>
			<td ><span style="color:red">*</span>供应商编号</td>
			<td>
				<% if("true".equals(changeView)){ %>
					<input style="float:left;" name="supplierCode" id="supplierCode" hisValue="${supplierCode}" value="${supplierCode}" class="{required:true,messages:{required:'不能为空'}}"/>
					<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="supplierClick(this)" href="javascript:void(0);" title="选择供应商"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
				<%}else{ %>
					<span>
						${supplierCode}
						<input style="float:left;" type="hidden" name="supplierCode" id="supplierCode" hisValue="${supplierCode}" value="${supplierCode}"/>
					</span>
				<%}%>
			</td>		
			<td >供应商名称</td>
			<td>
				<% if("true".equals(changeView)){ %>
					<input name="supplierName" value="${supplierName}" id="supplierName"/>
				<%}else{ %>
					<span>
						${supplierName}
						<input type="hidden" name="supplierName" value="${supplierName}" id="supplierName"/>	
					</span>
				<%}%>
			</td>
			<td><span style="color:red">*</span>进料日期</td>
			<td>
			<% if("true".equals(changeView)){ %>
				<input name="enterDate" id="enterDate" value="<s:date name="enterDate" format="yyyy-MM-dd HH:mm"/>" readonly="readonly" class="line" />
			<%}else{%>
				<span><s:date name="enterDate" format="yyyy-MM-dd HH:mm"/></span>
			<%}%>
			</td>					
		</tr>
		<tr>
			<td><span style="color:red">*</span>进料数量</td>
			<td>
				<%
					Double val = (Double)ActionContext.getContext().getValueStack().findValue("stockAmount");
					if(val == null){
						val = 0.0d;
					}
					DecimalFormat df = new DecimalFormat("#.#####");
				%>
				<% if("true".equals(changeView)){ %>
					<input  name="stockAmount" id="stockAmount" value="${stockAmount}" onblur="loadCheckItems()"/>
				<%}else{ %>
					<%=df.format(val) %>${units}
					<input type="hidden" name="stockAmount" id="stockAmount" value="<%=df.format(val) %>"  class="{required:true,number:true,messages:{required:'来料数量不能为空'}}"></input>
				<%}%> 				
			</td>
			<td><span style="color:red">*</span>Lot No</td>
			<td>
				<input id="lotNo" name="lotNo" value="${lotNo}" class="{required:true,messages:{required:'必填'}}"/>
			</td>			
			<td><span style="color:red">*</span>检验日期</td>
			<td>
				<input name="inspectionDate" id="inspectionDate" value="<s:date name="inspectionDate" format="yyyy-MM-dd HH:mm"/>" readonly="readonly" class="line"/>
				<input type="hidden" name="schemeStartDate" value="${schemeStartDateStr}"/>
			</td>			

		</tr>
		<tr>
			<td><span style="color:red">*</span>型号规格</td>
			<td>
				<input name="modelSpecification" id="modelSpecification" value="${modelSpecification}" class="{required:true,messages:{required:'必填'}}"/>
			</td>		
			<td><span style="color:red">*</span>产品阶段</td>
			<td>
				<s:select list="iqc_product_stage" 
					  theme="simple"
					  listKey="value" 
					  listValue="name" 
					  labelSeparator=""
					  id="productStage"
					  name="productStage"
					  emptyOption="false"
					  cssClass="{required:true,messages:{required:'必填'}}"></s:select>
			</td>
			<td><span style="color:red">*</span>物料类别</td>
			<td>
				 <input name="checkBomMaterialType" id="checkBomMaterialType" style="float:left;" value="${checkBomMaterialType}" class="{required:true,messages:{required:'必填'}}" />
				 <a class="small-button-bg" style="margin-left:2px;float:left;" onclick="materialType()" href="javascript:void(0);" title="选择物料类别"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
			</td>
		</tr>
		<tr>
			<td><span style="color:red">*</span>客户代码</td>
			<td>
				<input name="customerCode" id="customerCode" value="${customerCode}" class="{required:true,messages:{required:'必填'}}"/>
			</td>		
			<td>组织名称</td>
			<td>
				<% if("true".equals(changeView)){ %>
					<input  name="businessUnitName"  id="businessUnitName" value="${businessUnitName}"  />
				<%}else{ %>
					<span>${businessUnitName}</span>
					<input type="hidden" name="businessUnitName" id="businessUnitName" value="${businessUnitName}" />
				<%}%>
			</td>
			<td>原厂供应商</td>
			<td>
				<input style="float:left;" name="supplierNameTrue" id="supplierNameTrue"  value="${supplierNameTrue}" />
				<a class="small-button-bg" style="margin-left:2px;float:left;" id="supplierTrue" onclick="supplierClick(this)" href="javascript:void(0);" title="选择供应商"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
				<input type="hidden" name="supplierCodeTrue" id="supplierCodeTrue" value="${supplierCodeTrue}" />
			<td>
		</tr>	
		<tr>
			<td style="width:140px;">驻厂检验</td>
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
			<td></td>
			<td>
			</td>
			<td></td>
			<td>
			<td>
		</tr>			
		<tr>
			<td colspan="6">
				<table  class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
					<tr>
						<td style="width:15%;text-align: center;">检验项目</td>
						<td style="width:30%;text-align: center;">检验内容</td>
						<td style="width:10%;text-align: center;">检验结果</td>
						<td style="width:10%;text-align: center;">判定</td>
						<td style="width:20%;text-align: center;">备注</td>
					</tr>
					<tr>
						<td rowspan="2" style="text-align: center;">包装/资料核对</td>
						<td style="text-align: center;">
							<%-- <s:checkboxlist
								theme="simple" list="packing1" listKey="value" listValue="name"
								name="packingFir" value="packingFir">
							</s:checkboxlist> --%>
							<s:iterator value="packing1" id="option">
								<input id="packingFir${option.name}"  fileName="packingFir" control="true" type="checkbox" onclick="getCheckedBoxVal();" <s:if test="%{packingFir.indexOf(#option.value)>-1}"> checked="checked" </s:if> value="${option.value}" />
								<label for="packingFir${option.name}">${option.name}</label>
						    </s:iterator> 
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
						<td rowspan="2" style="text-align: center;">
							<s:select list="iqc_okorng" 
							  theme="simple"
							  listKey="value" 
							  listValue="name" 
							  labelSeparator=""
							  id="packingResult"
							  name="packingResult"
							  emptyOption="false"></s:select>
						</td>
						<td rowspan="2" style="text-align: center;">
							<textarea  rows="2"  id="packingText" name="packingText">${packingText}</textarea>
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">
							<%-- <s:checkboxlist
								theme="simple" list="packing2" listKey="value" listValue="name"
								name="packingSec" value="packingSec">
							</s:checkboxlist> --%>
							 <s:iterator value="packing2" id="option">
								<input id="packingSec${option.name}"   fileName="packingSec" onclick="getCheckedBoxVal();"  control="true" type="checkbox" <s:if test="%{packingSec.indexOf(#option.value)>-1}"> checked="checked" </s:if> value="${option.value}" />
								<label for="packingSec${option.name}">${option.name}</label>
						    </s:iterator> 
						</td >
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
				</table>
			</td>
		</tr>
		<tr>
			<td>检验类型</td>
			<td colspan="3">
				<%-- <s:checkboxlist
					theme="simple" list="appearanceInspections" listKey="value" listValue="name"
					name="appearanceInspection" value="appearanceInspection">
				</s:checkboxlist> --%>				
	            <input type="radio" id="appearanceInspection2" name="appearanceInspection" value="正常检验" <s:if test="appearanceInspection=='正常检验'">checked="true"</s:if> checked="checked" title="正常检验" onchange="uncheckNoChange(this);"/><label for="appearanceInspection2">正常检验</label>
	            <input type="radio" id="appearanceInspection3" name="appearanceInspection" value="加严检验" <s:if test="appearanceInspection=='加严检验'">checked="true"</s:if> title="加严检验" onchange="uncheckNoChange(this);"/><label for="appearanceInspection3">加严检验</label>
	            <input type="radio" id="appearanceInspection4" name="appearanceInspection" value="放宽检验" <s:if test="appearanceInspection=='放宽检验'">checked="true"</s:if> title="放宽检验" onchange="uncheckNoChange(this);"/><label for="appearanceInspection4">放宽检验</label>
				<input type="radio" id="appearanceInspection5" name="appearanceInspection" value="单片抽检" <s:if test="appearanceInspection=='单片抽检'">checked="true"</s:if> title="单片抽检" onchange="uncheckNoChange(this);"/><label for="appearanceInspection5">单片抽检</label>
				<input type="radio" id="appearanceInspection1" name="appearanceInspection" value="全检" <s:if test="appearanceInspection=='全检'">checked="true"</s:if> title="全检" onchange="uncheckNoChange(this);"/><label for="appearanceInspection1">全检</label>
				<input type="radio" id="appearanceInspection6" name="appearanceInspection" value="免检" <s:if test="appearanceInspection=='免检'">checked="true"</s:if> title="免检" onchange="uncheckNoChange(this);"/><label for="appearanceInspection6">免检</label>
				&nbsp;<span id="uncheckNo_span" style="color:red;"></span>免检单号:<input name="uncheckNo" id="uncheckNo" value="${uncheckNo}" />
			</td>
			<td colspan="2" style="text-align: center;">
				<button class='btn' type="button" onclick="choiceWaitCheckItem();">
					<span><span><b class="btn-icons btn-icons-search"></b>领取待检验项目</span></span>
				</button>
			</td>			
		</tr>
		<tr>
			<td colspan="6">
				<table  class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
					<tr>
						<td style="width:200px;text-align: center;" rowspan="2">抽样计划</td>
						<td style="width:200px;text-align: center;" colspan="2" rowspan="2">抽样水准</td>
						<td style="width:180px;;text-align: center;" rowspan="2">抽样等级</td>
						<td style="width:300px;text-align: center;" colspan="3">样本数</td>
						<td style="width:100px;text-align: center;" rowspan="2">不良数</td>
						<td style="width:250px;text-align: center;" rowspan="2">不良项目</td>
						<td style="width:140px;text-align: center;" rowspan="2">判定</td>
					</tr>
					<tr>
						<td style="width:50px;text-align: center;" >N</td>
						<td style="width:50px;;text-align: center;" >Ac</td>
						<td style="width:50px;text-align: center;" >Re</td>
					</tr>					
					<tr>
						<td rowspan="2" style="text-align: center;">
							<!-- <input type="checkbox" name="samplingPlan" value="Z1.4-2003" <s:if test="%{samplingPlan.indexOf('Z1.4-2003')>=0}">checked="checked"</s:if> />Z1.4-2003<br>
							<input type="checkbox" name="samplingPlan" value="MIL-STD-1916" <s:if test="%{samplingPlan.indexOf('MIL-STD-1916')>=0}">checked="checked"</s:if> />MIL-STD-1916<br>
							<input type="checkbox" name="samplingPlan" value="C=0抽样方案" <s:if test="%{samplingPlan.indexOf('C=0抽样方案')>=0}">checked="checked"</s:if> />C=0抽样方案<br>
							<input type="checkbox" name="samplingPlan" value="MIL-STD-105E" <s:if test="%{samplingPlan.indexOf('MIL-STD-105E')>=0}">checked="checked"</s:if> />MIL-STD-105E -->
							<input type="hidden" style="width: 98%;" name="samplingPlan" id="samplingPlan" value="${samplingPlan}" type="text"/>	
							<span id="samplingPlanSpan" >${samplingPlan}</span>
						</td>
						<td >
							<%-- <input style="width: 98%;" name="samplingStandard1" id="samplingStandard1" value="${samplingStandard1}" type="text"/> --%>	
							<s:select list="samplingStandards" 
							  theme="simple"
							  listKey="name" 
							  listValue="name" 
							  labelSeparator=""
							  id="samplingStandard1"
							  name="samplingStandard1"
							  emptyOption="false"></s:select>	
						</td>
						<td>
							<%-- <input style="width: 98%;" name="samplingStandard2" id="samplingStandard2" value="${samplingStandard2}" type="text"/> --%>
							<s:select list="aqls" 
							  theme="simple"
							  listKey="name" 
							  listValue="name" 
							  labelSeparator=""
							  id="samplingStandard2"
							  name="samplingStandard2"
							  emptyOption="true"></s:select>		
						</td>						
						<td rowspan="2">
							<%-- <input style="width: 98%;" name="samplingLevel" id="samplingLevel" value="${samplingLevel}" type="text"/> --%>
							<s:select list="samplingLevels" 
							  theme="simple"
							  listKey="name" 
							  listValue="name" 
							  labelSeparator=""
							  id="samplingLevel"
							  name="samplingLevel"
							  emptyOption="false"></s:select>
						</td>
						<td>
							<input style="width: 100px; " name="samplingN1" id="samplingN1" value="${samplingN1}" type="text" onkeyup="mustNum(this);jisuanBadRate(this);"/>
							<span id="samplingN1_span" style="color:red;padding-top:0.2rem;text-align: center;"></span>
						</td>
						<td>
							<input style="width: 100px;" name="samplingAc1" id="samplingAc1" value="${samplingAc1}" type="text"/>
						</td>
						<td>
							<input style="width: 100px;" name="samplingRe1" id="samplingRe1" value="${samplingRe1}" type="text"/>
						</td>
						<td>
							<input name="samplingBadCount1" id="samplingBadCount1" value="${samplingBadCount1}" type="text" onkeyup="mustNum(this);jisuanBadRate(this);"/>
							<span id="samplingBadCount1_span" style="color:red;padding-top:0.2rem;text-align: center;"></span>
							<input name="badRate1" id="badRate1" value="${badRate1}" type="hidden"/>
						</td>
						<td>
							<textarea id="samplingBadItem1" name="samplingBadItem1">${samplingBadItem1}</textarea>
						</td>
						<td rowspan="2" style="text-align:center;">
							<s:select list="iqc_okorng" 
							  theme="simple"
							  listKey="name" 
							  listValue="name" 
							  labelSeparator=""
							  name="determine"
							  id="determine"
							  cssStyle="width:90%;"
							  onchange="caculateTotalAmount(this);"
							  emptyOption="false"></s:select>
						</td>																																				
					</tr>
					<tr>
						<td>
							<%-- <input style="width: 98%;" name="samplingStandard3" id="samplingStandard3" value="${samplingStandard3}" type="text"/> --%>
							<s:select list="samplingStandards" 
							  theme="simple"
							  listKey="name" 
							  listValue="name" 
							  labelSeparator=""
							  id="samplingStandard3"
							  name="samplingStandard3"
							  emptyOption="false"></s:select>
						</td>
						<td>
							<%-- <input  style="width: 98%;" name="samplingStandard4" id="samplingStandard4" value="${samplingStandard4}" type="text"/> --%>
							<s:select list="aqls" 
							  theme="simple"
							  listKey="name" 
							  listValue="name" 
							  labelSeparator=""
							  id="samplingStandard4"
							  name="samplingStandard4"
							  emptyOption="true"></s:select>
						</td>
						<td>
							<input style="width: 100px;" name="samplingN2" id="samplingN2" value="${samplingN2}" type="text" onkeyup="mustNum(this);jisuanBadRate(this);"/>
							<span id="samplingN2_span" style="color:red;padding-top:0.2rem;text-align: center;"></span>
						</td>
						<td>
							<input style="width: 100px;" name="samplingAc2" id="samplingAc2" value="${samplingAc2}" type="text"/>
						</td>
						<td>
							<input style="width: 100px;" name="samplingRe2" id="samplingRe2" value="${samplingRe2}" type="text"/>
						</td>
						<td>
							<input style="width: 98%;" name="samplingBadCount2" id="samplingBadCount2" value="${samplingBadCount2}" type="text" onkeyup="mustNum(this);jisuanBadRate(this);"/>
							<span id="samplingBadCount2_span" style="color:red;padding-top:0.2rem;text-align: center;"></span>
							<input name="badRate2" id="badRate2" value="${badRate2}" type="hidden"/>
						</td>
						<td>
							<textarea id="samplingBadItem2" name="samplingBadItem2">${samplingBadItem2}</textarea>
						</td>
					</tr>
				</table>
			</td>		
		<tr>
			<td colspan="6" style="padding:0px; " id="checkItemsParent">
				<div style="overflow-x:auto;overflow-y:hidden;padding-bottom:18px;">
					<s:if test="%{sampleSchemeType == '免检'}">
						<span style="padding:10px 0px 10px 6px;font-weight:bold;">
							免检物料,没有检验项目
						</span>
					</s:if>
					<s:else>
					   <%@ include file="check-items.jsp"%>
					</s:else>
				</div>
			</td>
		</tr>
		<tr>
			<td>尺寸抽检数</td>
			<td>
				<input id="sizeAmount" name="sizeAmount" value="${sizeAmount}"  onkeyup="mustNum(this);jisuanTotalRate(this);"/>
				<span id="sizeAmount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span>
			</td>
			<td>尺寸不良数</td>
			<td>
				<input id="sizeUnAmount" name="sizeUnAmount" value="${sizeUnAmount}"  onkeyup="mustNum(this);jisuanTotalRate(this);"/>
				<span id="sizeUnAmount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span>
			</td>
			<td>尺寸不良率</td>
			<td>
				<input id="sizeUnAmountRate" name="sizeUnAmountRate" value="${sizeUnAmountRate}" />%
				<input type="hidden" id="sizeAmountRate" name="sizeAmountRate" value="${sizeAmountRate}" />
			</td>
		</tr>		
		<tr>
			<td>特性抽检数</td>
			<td>
				<input id="qualifiedAmount" name="qualifiedAmount" value="${qualifiedAmount}" onkeyup="mustNum(this);jisuanTotalRate(this);"/>
				<span id="qualifiedAmount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span>
				<input type="hidden" id="functionAmount" name="functionAmount" value="${functionAmount}" />
				<input type="hidden" id="appearanceAmount" name="appearanceAmount" value="${appearanceAmount}" />
			</td>
			<td>特性不良数</td>
			<td>
				<input id="unqualifiedAmount" name="unqualifiedAmount" value="${unqualifiedAmount}" onkeyup="mustNum(this);jisuanTotalRate(this);"/>
				<span id="unqualifiedAmount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span>
				<input type="hidden" id="functionUnAmount" name="functionUnAmount" value="${functionUnAmount}" />
				<input type="hidden" id="appearanceUnAmount" name="appearanceUnAmount" value="${appearanceUnAmount}" />
			</td>
			<td>特性不良率</td>
			<td>
				<input id="qualifiedUnRate" name="qualifiedUnRate" value="${qualifiedUnRate}" />%
				<input type="hidden" id="qualifiedRate" name="qualifiedRate" value="${qualifiedRate}" />
				<input type="hidden" id="functionAmountRate" name="functionAmountRate" value="${functionAmountRate}" />
				<input type="hidden" id="functionUnAmountRate" name="functionUnAmountRate" value="${functionUnAmountRate}" />
				<input type="hidden" id="appearanceAmountRate" name="appearanceAmountRate" value="${appearanceAmountRate}" />
				<input type="hidden" id="wgBadRate" name="wgBadRate" value="${wgBadRate}" />
				<input type="hidden" id="checkAmount" name="checkAmount" value="${checkAmount}" />
				<input type="hidden" id="wgBadDescrible" name="wgBadDescrible" value="${wgBadDescrible}"/>
				<input type="hidden" id="gnBadDescrible" name="gnBadDescrible" value="${gnBadDescrible}"/>
				<input type="hidden" id="ccBadDescrible" name="ccBadDescrible" value="${ccBadDescrible}"/>
			</td>
		</tr>					
		<%-- <tr>
			<td >检验依据</td>
			<td colspan="3" style="text-align: center;"><input  name="inspectionBasis" id="inspectionBasis" value="${inspectionBasis}" type="text"/>进料检验规范</td>
			<td style="text-align: center;">版本</td>
			<td ><input name="version" id="version" value="${version}" type="text"/></td>
		</tr> --%>
		<tr>
			<td rowspan="2">HSF符合性</td>
			<td rowspan="2">RoHS六项&卤素(Br、Cl)</td>
			<td colspan="2" style="text-align: center;">检验方法</td>
			<td style="text-align: center;">报告编号</td>
			<td style="text-align: center;">判定</td>
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">
				<s:checkboxlist
					theme="simple" list="hsfCheckMethods" listKey="value" listValue="name"
					name="hsfCheckMethod" value="hsfCheckMethod">
				</s:checkboxlist> &nbsp;&nbsp;依据《HSF检测作业规范》			
			</td>
			<td style="text-align: center;"><input type="text" id="hsfFormNo" name="hsfFormNo" value="${hsfFormNo}" /></td>
			<td style="text-align: center;">
				<s:select list="iqc_okorng" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  labelSeparator=""
				  name="hsfDetermine"
				  id="hsfDetermine"
				  emptyOption="false"></s:select>			
			</td>
		</tr>			
		<tr>
			<td colspan="6">
				<table  class="form-table-border-left" style="width:100%;margin: auto;border:0px;">			
					<tr>
						<td style="text-align: center;width:15%;">检验类别</td>
						<td style="text-align: center;width:25%;">检验内容</td>
						<td style="text-align: center;width:15%;">检验结果</td>
						<td style="text-align: center;width:25%;">报告编号</td>
						<td colspan="2"  style="text-align: center;width:20%;">描述</td>
					</tr>
					<tr>
						<td>HSF符合性检查</td>
						<td>
							<textarea rows="2" cols="2" name="hisInspectionItem">${hisInspectionItem}</textarea>
<%-- 							${hisInspectionItem} --%>
						</td>
						<td>
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
						<td>
							<input name="hisReportNo" id="hisReportNo" value="${hisReportNo}" type="hidden" />
							 <input name="hisReportId" id="hisReportId" value="${hisReportId}" type="hidden" />
							 <span style="text-decoration:underline;" id="hsfSpan1" onclick="hisReportInput(this);">${hisReportNo}</span>
							 <span id="hsfSpan2" ></span>
						</td>
						<td colspan="2">
							<textarea rows="2" cols="2" name="hisText">${hisText}</textarea>
							<%-- ${hisText} --%>
						</td>
					</tr>
					<tr>
						<td>可靠性测试</td>
						<td>
							<textarea rows="2" cols="2" name="ordInspectionItem">${ordInspectionItem}</textarea>
<%-- 							${ordInspectionItem} --%>
						</td>
						<td>
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
						<td>
							<input name="ordReportNo" id="ordReportNo" value="${ordReportNo}" type="hidden" />
							<input name="ordReportId" id="ordReportId" value="${ordReportId}" type="hidden" />
							<span style="text-decoration:underline;" id="ortSpan1" onclick="ordReportInput(this);">${ordReportNo}</span>
							<span id="ortSpan2" ></span>
						</td>
						<td colspan="2">
							<textarea rows="2" cols="2" name="ordText">${ordText}</textarea>
						</td>
					</tr>	
				</table>
			</td>
		</tr>				
		<tr>				
			<td>综合判定</td>
			<td style="border-top:0px;" colspan="2">
				<span>${inspectionConclusion}</span>
				<input name="inspectionConclusion" id="inspectionConclusion" value="${inspectionConclusion}" type="hidden"/>
			</td>
			<td>处理方式</td>
			<td colspan="2">
				<s:checkboxlist
					theme="simple" list="dealMethods" listKey="value" listValue="name"
					name="processingResult" value="processingResult">
				</s:checkboxlist>
			</td>
			<td  colspan="2"></td>
		</tr>
		<tr>
			<td>异常单号</td>
			<td ><input name="exceptionNo" id="exceptionNo" value="${exceptionNo}" type="hidden" />
				 <input name="exceptionId" id="exceptionId" value="${exceptionId}" type="hidden" />
				 <span style="text-decoration:underline;" onclick="exceptionInput(this);">${exceptionNo}</span>
			</td>
			<td>Mrb单号</td>
			<td>
				<input id="sqeMrbReportNo" name="sqeMrbReportNo" value="${sqeMrbReportNo}" style="width: 90%;border:none;background: none;" readonly="readonly"/>
			</td>
			<td>退货单号</td>
			<td>
				<input id="returnReportNo" name="returnReportNo" value="${returnReportNo}" style="width: 90%;border:none;background: none;" readonly="readonly"/>
			</td>
		</tr>
		<tr>
			<td>上传附件</td>		
		   <td colspan='3'>
	   			<button class='btn' type="button" onclick="uploadFiles('showAttachmentFiles','attachmentFiles');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
	            <input type="hidden" name="hisAttachmentFiles" value='${attachmentFiles}'></input>
	            <input type="hidden" name="attachmentFiles" id="attachmentFiles" value='${attachmentFiles}'></input>
	           <span id='showAttachmentFiles'  style="display:inline-block;width:60%;word-wrap:break-word;white-space:normal;width:700px;"></span>
		   </td>	
		   	<td>IQC检验员</td>
			<td><input type="hidden" id="inspectorLoginName" name="inspectorLoginName" value="${inspectorLoginName}"/>
				<input id="inspector" name="inspector" value="${inspector}" onclick='selectObj("选择检验员","inspectorLoginName","inspector","loginName");' readonly="readonly" class="{required:true,messages:{required:'必填'}}"/>			
			</td>	
		</tr>
		<tr>
			<td>IQC录入员</td>
			<td><input type="text" id="inputMan" name="inputMan" value="${inputMan}" class="{required:true,messages:{required:'必填'}}"/></td>
			<td>IQC班长</td>
			<td>
				<input type="hidden" id="auditLoginMan" name="auditLoginMan" value="${auditLoginMan}"/>
				<input id="auditMan" name="auditMan" value="${auditMan}" onclick='selectObj("选择审核人","auditLoginMan","auditMan","loginName");' readonly="readonly" class="{required:true,messages:{required:'必填'}}"/>
			</td>
			<td>审核时间</td>
			<td><s:date name="auditTime" format="yyyy-MM-dd HH:mm"/></td>
		</tr>		
	</table>
