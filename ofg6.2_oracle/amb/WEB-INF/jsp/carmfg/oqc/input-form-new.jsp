<%@page import="com.opensymphony.xwork2.ActionContext"%>
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
		<caption style="font-size: 24px;">OQC数据录入表</caption>
		<input type="hidden" name= "itemData" id="itemData" value=""/>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table" partData="true">
		<tr>
			<td style="width:10%">厂区</td>
			<td>
				 <s:select list="businessUnits" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="businessUnit"
					name="businessUnit"
					onchange="selectBusinessUnit(this)"
					emptyOption="false"
					labelSeparator="">
				</s:select> 
			</td>
			<td style="width:10%">产品类别</td>
			<td>
				<s:select list="productTypes" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="productType"
					name="productType"
					onchange="selectBusinessUnit(this)"
					cssStyle="{required:true}"
					emptyOption="false">
				</s:select> 
			</td>
			<td style="width:10%">日期<span style="color:red">*</span></td>
			<td >
				<input name="inspectionDate" isDate=true id="inspectionDate" value="<s:date name="inspectionDate" format="yyyy-MM-dd" />" class="{required:true,messages:{required:'必填'}}" ></input>
			</td>
			<td style="width:10%">工厂</td>
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
			<td style="width:10%">工序</td>
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
			<td style="width:10%">班别</td>		
			<td>
				<s:select list="classGroups" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="classGroup"
					name="classGroup"
					cssStyle="{required:true}"
					emptyOption="false">
				</s:select> 
			</td>
			<td style="width:10%">客户</td>		
			<td>
				<%-- <input id="customer" name="customer" value="${customer}" /> --%>
				<s:select list="customers" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="customer"
					name="customer"
					emptyOption="true">
				</s:select>
			</td>
			<td style="width:10%">机种</td>		
			<td>
				<input id="model" name="model" value="${model}" onclick="modelClick(this);"/>
			</td>			
			<td style="width:10%">组/线别</td>
			<td>
				<s:select list="lineTypes" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="lineType"
					name="lineType"
					emptyOption="true">
				</s:select>
			</td>
			<td style="width:10%">备注</td>		
			<td>
				<input id="remark" name="remark" value="${remark}" />
			</td>
		</tr>		
	</table>
	<div style="overflow-x:auto;overflow-y:hidden;">
	<table style="width:10%;margin: auto;float: left;" class="form-table-border-left" id="default-table" style="border:0px;">
		<tr>
			<td colspan="2" style="text-align: center;height: 30px;">不良项目</td>		
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;height: 25px;">作业员</td>		
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;height: 25px;">检验员</td>		
		</tr>		
		<tr>
			<td colspan="2" style="text-align: center;height: 25px;">投入数</td>		
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;height: 25px;">抽检数</td>		
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;height: 25px;">不良数</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;height: 25px;">投入批</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;height: 25px;">合格批</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;height: 25px;">不良率</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;height: 25px;">批合格率</td>		
		</tr>
		<!-- <tr>
			<td colspan="2" style="text-align: center;">判定</td>		
		</tr> -->
		<% 	
		List<Map<String,String>> defectionList=(List<Map<String,String>>)ActionContext.getContext().get("defectionList");		
	    for (Map<String,String> map:defectionList) {
		   int size=map.size();
		%>		
<%-- 	<div class="badItem" style="width: 10%;mar">
		<ul>
			<li style="background-color:#00ffe5;"><%=map.get("typeName")%></li>
			 <% for (String itemCode:map.keySet()) {
				 if(!itemCode.equals("typeName")){
				 
			%>	
			<li>
				<div><%=map.get(itemCode)%></div>
				<div><input type="text" style="width: 55%;float: left;" vlrr=true name="<%=itemCode%>_itemName" id="<%=itemCode%>_itemName" class="text-inp" value="" onkeyup="mustNum(this);"><span id="<%=itemCode%>_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></div>				
			</li>
			<% 					
				 }}
			%>	
		</ul>
	</div>	 --%>
		<tr>
			<td rowspan="<%=size%>" ><%=map.get("typeName")%></td>		
		</tr>
		 <% for (String itemCode:map.keySet()) {			
			 if(!itemCode.equals("typeName")){				 
		%>
		<tr>
			<td style="width: 120px;text-align: center;height: 25px;"><input  value="<%=map.get(itemCode)%>" style="width: 90%;border:none;background: none;" readonly="readonly" title="<%=map.get(itemCode)%>"/></td>
		</tr>
		<% 					
			}			
		}
		%>				
	<% 					
		 }
	    int flag=1;
	%>	
	</table>
	<input type="hidden"  id="fir" name="fir" value="<%=flag+1%>"/>
	<input type="hidden" id="flagIds" name="flagIds" value="a_<%=flag%>"/>
	<table style="width:5%;margin: auto;float: left;" class="form-table-border-left" id="default-table" name="datasTable" itemData="true">
		<tr>
			<td colspan="2" style="text-align: center;height: 30px;">
				<a  class="small-button-bg" style="float:left;" name="add" onclick="addRowHtml(this)" title="添加"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
				<a  class="small-button-bg" style="margin-left:2px;float:left;" name="remove" onclick="removeRowHtml(this)" title="删除"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
			</td>		
		</tr>
		<tr>
			<td style="width: 80px;height: 25px;"><input type="text" fieldName="operator" name="a<%=flag%>_operator" id="a<%=flag%>_operator"  value=""  ></td>		
		</tr>
		<tr>
			<td style="width: 80px;height: 25px;"><input type="text" fieldName="inspectionMan" name="a<%=flag%>_inspectionMan" id="a<%=flag%>_inspectionMan"  value=""  ></td>		
		</tr>
		<tr>
			<td style="width: 80px;height: 25px;"><input type="text" fieldName="count" name="a<%=flag%>_count" id="a<%=flag%>_count"  value="" onkeyup="mustNum(this);" >
			<span id="a<%=flag%>_count_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></td>		
		</tr>
		<tr>
			<td style="width: 80px;height: 25px;"><input type="text" fieldName="samplingCount" name="a<%=flag%>_samplingCount" id="a<%=flag%>_samplingCount"  value="" onkeyup="mustNum(this);caculateBadRate(this)" >
			<span id="a<%=flag%>_samplingCount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></td>
		</tr>
		<tr>
			<td style="width: 80px;height: 25px;"><input type="text" fieldName="unQualityCount" name="a<%=flag%>_unQualityCount" id="a<%=flag%>_unQualityCount"  value="" onkeyup="mustNum(this);caculateBadRate(this);" >
			<span id="a<%=flag%>_unQualityCount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></td>
		</tr>
		<tr>
			<td style="width: 80px;height: 25px;"><input type="text" fieldName="stockBatch" name="a<%=flag%>_stockBatch" id="a<%=flag%>_stockBatch"  value="" onkeyup="mustNum(this);caculateBatchRate(this)">
			<span id="a<%=flag%>_stockBatch_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></td>	
			
		</tr>
		<tr>
			<td style="width: 80px;height: 25px;"><input type="text" fieldName="qualityBatch" name="a<%=flag%>_qualityBatch" id="a<%=flag%>_qualityBatch"  value="" onkeyup="mustNum(this);caculateBatchRate(this)">
			<span id="a<%=flag%>_qualityBatch_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></td>	
		</tr>
		<tr>
			<td style="width: 80px;height: 25px;"><input style="width: 90%;border:none;background: none;" readonly="readonly" type="text" fieldName="unQualityRate" name="a<%=flag%>_unQualityRate" id="a<%=flag%>_unQualityRate"  value="" >
		</tr>
		<tr>
			<td style="width: 80px;height: 25px;"><input style="width: 90%;border:none;background: none;" readonly="readonly" type="text" fieldName="qualityBatchRate" name="a<%=flag%>_qualityBatchRate" id="a<%=flag%>_qualityBatchRate"  value="" ></td>	
		</tr>
<%-- 		<tr>
			<td style="width: 80px;">
			<s:select list="judgeResults" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					fieldName="judgeResult"
					id="a<%=flag%>_judgeResult"
					name="a<%=flag%>_judgeResult"
					emptyOption="false">
				</s:select> 
			</td>	
		</tr> --%>
		<% 	
	    for (Map<String,String> map:defectionList) {
	    	for (String itemCode:map.keySet()) {			
				 if(!itemCode.equals("typeName")){
		%>	
		<tr>
			<td style="width: 80px;height: 25px;"><input type="text"  fieldName="<%=itemCode%>" name="a<%=flag%>_<%=itemCode%>" id="a<%=flag%>_<%=itemCode%>" isItem="true" value="" onkeyup="mustNum(this);caculateBadCount(this);">
			<span id="a<%=flag%>_<%=itemCode%>_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></td>	
		</tr>
		<% 					
				}			
			}
	    }
		%>	
	</table>
	</div>