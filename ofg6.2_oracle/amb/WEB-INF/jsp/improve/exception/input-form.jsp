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
		<caption style="font-size: 28px;padding-top: 10px;padding-bottom: 10px;">品质异常联络单</caption>
	<tr>
		<th colspan="4" style="text-align:right;padding-bottom:4px;">
			编号:${formNo}
		</th>
	</tr>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<tr>
			<td style="width:160px;text-align:center;">厂区</td>
			<td>
				 <s:select list="businessUnits" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="businessUnitName"
					name="businessUnitName"
					emptyOption="false"
					labelSeparator="">
				</s:select> 
			</td>		
			<td style="width:160px;text-align:center;">工厂</td>
			<td>
				<s:select list="factorys"
						theme="simple"
						listKey="value" 
						listValue="name" 
						name="factory" 
						id="factory" 
						emptyOption="true"
						onchange="factoryChange(this);">
					</s:select>	
			</td>
			<td style="width:160px;text-align:center;">工序</td>
			<td>
				<s:select list="procedures"
						theme="simple"
						listKey="value" 
						listValue="name" 
						name="procedure" 
						id="procedure" 
						emptyOption="true">
					</s:select>	
			</td>				
		</tr>	
		<tr>
			<td style="width:160px;text-align:center;">客户名称</td>
			<td>
				<%-- <input id="customerName" name="customerName" value="${customerName}" /> --%>
				<s:select list="customers" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="customerName"
					name="customerName"
					emptyOption="true">
				</s:select>
			</td>	
			<td style="width:160px;text-align:center;">欧菲机种</td>		
			<td>
				<input id="ofilmModel" name="ofilmModel" value="${ofilmModel}" onclick="modelClick(this);"/>
			</td>
			<td style="width:160px;text-align:center;">客户机种</td>		
			<td>
				<input id="customerModel" name="customerModel" value="${customerModel}" onclick="modelClick(this);"/>
			</td>				
		</tr>			
		<tr>
			<td style="width:160px;text-align:center;">异常日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="exceptionDate" name="exceptionDate" value="<s:date name='exceptionDate' format="yyyy-MM-dd"/>" />					
				<%-- <s:date name='exceptionDate' format="yyyy-MM-dd"/> --%>
			</td>
			<td style="width:160px;text-align:center;">异常项目</td>
			<td style="width:200px;">
				<input  name="exceptionItem" id="exceptionItem" value="${exceptionItem}"></input>
			</td>
			<td style="width:160px;text-align:center;">异常提报单位</td>
			<td style="width:200px;">
				<input  name="presentDept" id="presentDept" value="${presentDept}" isDept="true" style="float: left;"></input>
			</td>								
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">产品阶段</td>
			<td>
				<s:select list="productStages" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="productStage"
					name="productStage"
					emptyOption="false">
				</s:select> 
			</td>			
			<td style="width:160px;text-align:center;">流程卡号</td>
			<td style="width:200px;">
				<input  name="processCard" id="processCard" value="${processCard}"></input>
			</td>
			<td style="width:160px;text-align:center;">问题严重度</td>
			<td style="width:200px;">
				<s:select list="problemDegrees"
					listKey="value" 
					listValue="name" 
					name="problemDegree" 
					id="problemDegree" 
					cssStyle="width:140px;"
					onchange=""
					theme="simple">
				</s:select>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">班次</td>
			<td style="width:200px;">
				<s:select list="classGroups"
					listKey="value" 
					listValue="name" 
					name="classGroup" 
					id="classGroup" 
					cssStyle="width:140px;"
					onchange=""
					theme="simple">
				</s:select>			
			</td>		
			<td style="width:160px;text-align:center;">不良数/Lot数量</td>
			<td style="width:200px;">
				<input  name="lotNum" id="lotNum" value="${lotNum}"></input>
			</td>
			<td style="width:160px;text-align:center;">供应商名称</td>
			<td style="width:200px;">
				<input style="float:left;" id="supplierName" name="supplierName" readonly=readonly value="${supplierName}"/>
				<input type='hidden' id="supplierCode"  name="supplierCode" value="${supplierCode}"/>
	         <a class="small-button-bg" style="margin-left:2px;float:left;" onclick="supplierClick()" href="javascript:void(0);" title="选择供应商"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
			</td>			
		</tr>	
		<tr>
			<td style="width:160px;text-align:center;">物料类别</td>
			<td style="width:200px;">
				<s:select list="materialTypes"
					listKey="value" 
					listValue="name" 
					name="materialType" 
					id="materialType" 
					cssStyle="width:140px;"
					onchange=""
					emptyOption="true"
					theme="simple">
				</s:select>			
			</td>		
			<td style="width:160px;text-align:center;">问题归属</td>
			<td style="width:200px;">
				<s:select list="problemBelongs"
					listKey="value" 
					listValue="name" 
					name="problemBelong" 
					id="problemBelong"
					emptyOption="true" 
					theme="simple">
				</s:select>
			</td>
			<td style="width:160px;text-align:center;">管理编号</td>
			<td style="width:200px;">
				<input  name="managerNo" id="managerNo" value="${managerNo}" onchange="managerNoChange(this);"></input>
			</td>			
		</tr>			
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">异常描述</td>
		</tr>		
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="exceptionDescrible" name="exceptionDescrible"  >${exceptionDescrible}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">附件</td>
			<td style="width:200px;" colspan="3">
				<input type="hidden"  isFile="true" id="exceptionDescribleFile" name="exceptionDescribleFile" value="${exceptionDescribleFile}"/>
			</td>
			<td style="width:160px;text-align:center;">提报人</td>
			<td style="width:200px;">
				<input type="text" id="presentMan" isTemp="true" isUser="true" hiddenInputId="presentManLogin" style="float: left;"  name="presentMan" value="${presentMan}" />
				<input type="hidden" id="presentManLogin" name="presentManLogin" value="${presentManLogin}" />
			</td>					
		</tr>	
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">临时对策</td>
		</tr>		
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="emergencyMeasures" name="emergencyMeasures"  >${emergencyMeasures}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">附件</td>
			<td style="width:200px;">
				<input type="hidden"  isFile="true" id="emergencyFile" name="emergencyFile" value="${emergencyFile}"/>
			</td>	
			<td style="width:160px;text-align:center;">责任单位</td>
			<td style="width:200px;">
				<input type="text" id="dutyDept1" isTemp="true" isDept="true"  style="float: left;"  name="dutyDept1" value="${dutyDept1}" />
			</td>		
			<td style="width:160px;text-align:center;">作成</td>
			<td style="width:200px;">
				<input type="text" id="dutyMan1" isTemp="true" isUser="true" hiddenInputId="dutyMan1Login" style="float: left;"  name="dutyMan1" value="${dutyMan1}" />
				<input type="hidden" id="dutyMan1Login" name="dutyMan1Login" value="${dutyMan1Login}" />
			</td>	
		</tr>					
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">原因分析</td>
		</tr>
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="reasonAnalysis" name="reasonAnalysis"  >${reasonAnalysis}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">附件</td>
			<td style="width:200px;">
				<input type="hidden"  isFile="true" id="reasonAnalysisFile" name="reasonAnalysisFile" value="${reasonAnalysisFile}"/>
			</td>	
			<td style="width:160px;text-align:center;">责任单位</td>
			<td style="width:200px;">
				<input type="text" id="dutyDept2" isTemp="true" isDept="true"  style="float: left;"  name="dutyDept2" value="${dutyDept2}" />
			</td>		
			<td style="width:160px;text-align:center;">作成</td>
			<td style="width:200px;">
				<input type="text" id="dutyMan2" isTemp="true" isUser="true" hiddenInputId="dutyMan2Login" style="float: left;"  name="dutyMan2" value="${dutyMan2}" />
				<input type="hidden" id="dutyMan2Login" name="dutyMan2Login" value="${dutyMan2Login}" />
			</td>	
		</tr>				
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">改善对策</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">短期对策</td>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="shortMeasures" name="shortMeasures"  >${shortMeasures}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">长期对策</td>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="improvementMeasures" name="improvementMeasures"  >${improvementMeasures}</textarea>
			</td>
		</tr>		
		<tr>
			<td style="width:160px;text-align:center;">附件</td>
			<td style="width:200px;" colspan="5">
				<input type="hidden"  isFile="true" id="improvementMeasuresFile" name="improvementMeasuresFile" value="${improvementMeasuresFile}"/>
			</td>	
		</tr>		
		<tr>
			<td style="width:160px;text-align:center;">作成</td>
			<td style="width:200px;">
				<input type="text" id="dutyMan3" isTemp="true" isUser="true" hiddenInputId="dutyMan3Login" style="float: left;"  name="dutyMan3" value="${dutyMan3}" />
				<input type="hidden" id="dutyMan3Login" name="dutyMan3Login" value="${dutyMan3Login}" />
			</td>		
			<td style="width:160px;text-align:center;">预计完成日</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="planDate" name="planDate" value="<s:date name='planDate' format="yyyy-MM-dd"/>" />
			</td>
			<td style="width:160px;text-align:center;">实际完成日</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="actualDate" name="actualDate" value="<s:date name='actualDate' format="yyyy-MM-dd"/>" />
			</td>	
		</tr>		
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">效果确认</td>
		</tr>
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="effectConfirm" name="effectConfirm"  >${effectConfirm}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">确认人</td>
			<td style="width:200px;">
				<input type="text" id="dutyMan4" isTemp="true" isUser="true" hiddenInputId="dutyMan4Login" style="float: left;"  name="dutyMan4" value="${dutyMan4}" multiple="multiple"/>
				<input type="hidden" id="dutyMan4Login" name="dutyMan4Login" value="${dutyMan4Login}" />
			</td>
			<td  colspan="4">*会签模式，点击搜索图标人员可多选！</td>
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">是否结案</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">是否共享案例</td>
			<td colspan="3">
				<s:select list="isshares"
					listKey="value" 
					listValue="name" 
					name="isShare" 
					id="isShare" 
					cssStyle="width:140px;"
					onchange="shareChange(this);"
					theme="simple"> 
				</s:select>	
			</td>
			<td style="width:160px;text-align:center;">确认人</td>
			<td style="width:200px;">
				<input type="text" id="approvalMan4" isTemp="true" isUser="true" hiddenInputId="approvalMan4Login" style="float: left;"  name="approvalMan4" value="${approvalMan4}" />
				<input type="hidden" id="approvalMan4Login" name="approvalMan4Login" value="${approvalMan4Login}" />
			</td>
		</tr>
		<tr  name="share">
			<td style="width:160px;text-align:center;">共享原因</td>
			<td  colspan="5">
				<textarea rows="5"   id="shareText" name="shareText"  >${shareText}</textarea>
			</td>
		</tr>								
	</table>