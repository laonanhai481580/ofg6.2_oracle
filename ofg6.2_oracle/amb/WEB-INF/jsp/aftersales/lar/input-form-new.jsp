<%@page import="com.ambition.aftersales.entity.LarData"%>
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
	LarData larData=(LarData)ActionContext.getContext().get("larData");
%> 
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<caption style="font-size: 24px;">LAR数据录入表</caption>
		<input type="hidden" name= "itemData" id="itemData" value=""/>
		<input type="hidden" name= "larId" id="larId" value="${larId}"/>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table" partData="true">
		<tr>
			<td style="width:9%;text-align: center;">年</td>
			<td >
				<input style="width: 90%;border:none;background: none;" readonly="readonly" type="text"  value="<%=larData.getYearInt()%>" />
			</td>
			<td style="width:9%;text-align: center;">月</td>
			<td>
				<input style="width: 90%;border:none;background: none;" readonly="readonly" type="text"  value="<%=larData.getMonthInt()%>" />
			</td>
			<td style="width:9%;text-align: center;">厂区</td>
			<td >
				<input style="width: 90%;border:none;background: none;" readonly="readonly" type="text"  value="<%=larData.getBusinessUnitName()%>" />
			</td>
			<td style="width:9%;text-align: center;">客户</td>
			<td>
				<input style="width: 90%;border:none;background: none;" readonly="readonly" name="customerName" id="customerName" type="text"  value="<%=larData.getCustomer()%>" />
			</td>
			<td style="width:9%;text-align: center;">客户机种</td>
			<td>
				<input style="width: 90%;"  type="text" name="customerModel" id="customerModel" value="" onclick="modelClick(this);"/>
			</td>
			<td style="width:9%;text-align: center;">欧菲机种</td>
			<td>
				<input style="width: 90%;"  type="text" name="ofilmModel" id="ofilmModel" value="" onclick="modelClick(this);"/>
			</td>
		</tr>		
	</table>
	<div style="overflow-x:auto;overflow-y:hidden;">
	<table style="width:9%;margin: auto;float: left;" class="form-table-border-left" id="default-table" style="border:0px;">
		<tr>
			<td colspan="2" style="text-align: center;">不良项目</td>		
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;"><span style="color:red">*</span>日期</td>		
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;"><span style="color:red">*</span>料号</td>		
		</tr>			
		<tr>
			<td colspan="2" style="text-align: center;"><span style="color:red">*</span>入料数</td>		
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;"><span style="color:red">*</span>抽检数</td>		
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">不良数</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">不良率</td>	
		</tr>
		<% 	
		List<Map<String,String>> defectionList=(List<Map<String,String>>)ActionContext.getContext().get("defectionList");		
	    for (Map<String,String> map:defectionList) {
		   int size=map.size();
		%>		
		<tr>
			<td rowspan="<%=size%>"><%=map.get("typeName")%></td>		
		</tr>
		 <% for (String itemCode:map.keySet()) {			
			 if(!itemCode.equals("typeName")){				 
		%>
		<tr>
			<td style="width: 100px;"><input  value="<%=map.get(itemCode)%>" style="width: 90%;border:none;background: none;" readonly="readonly" title="<%=map.get(itemCode)%>"/></td>
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
			<td colspan="2" style="text-align: center;">
				<a  class="small-button-bg" style="float:left;" name="add" onclick="addRowHtml(this)" title="添加"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
				<a  class="small-button-bg" style="margin-left:2px;float:left;" name="remove" onclick="removeRowHtml(this)" title="删除"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
			</td>		
		</tr>
		<tr>
			<td style="width: 80px;"><input type="text" isDate="true" fieldName="larDate" name="a<%=flag%>_larDate" id="a<%=flag%>_larDate"  value=""  class="{required:true,messages:{required:'必填'}}"/></td>		
		</tr>
		<tr>
			<td style="width: 80px;"><input type="text"  fieldName="materialNo" name="a<%=flag%>_materialNo" id="a<%=flag%>_materialNo"  value=""  class="{required:true,messages:{required:'必填'}}"/></td>		
		</tr>		
		<tr>
			<td style="width: 80px;"><input type="text" fieldName="inputAmount" name="a<%=flag%>_inputAmount" id="a<%=flag%>_inputAmount"  value="" onkeyup="mustNum(this);caculateBadRate(this)" class="{required:true,messages:{required:'必填'}}"/>
			<span id="a<%=flag%>_inputAmount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></td>				
		</tr>
		<tr>
			<td style="width: 80px;"><input type="text" fieldName="inspectionAmount" name="a<%=flag%>_inspectionAmount" id="a<%=flag%>_inspectionAmount"  value="" onkeyup="mustNum(this);caculateBadRate(this)" class="{required:true,messages:{required:'必填'}}"/>
			<span id="a<%=flag%>_inspectionAmount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></td>				
		</tr>		
		<tr>
			<td style="width: 80px;"><input style="width: 90%;border:none;background: none;" readonly="readonly" type="text" fieldName="badAmount" name="a<%=flag%>_badAmount" id="a<%=flag%>_badAmount"  value="" />
									<input style="width: 90%;border:none;background: none;" readonly="readonly" type="hidden" fieldName="qualifiedAmount" name="a<%=flag%>_qualifiedAmount" id="a<%=flag%>_qualifiedAmount"  value="" />
									<input style="width: 90%;border:none;background: none;" readonly="readonly" type="hidden" fieldName="qualifiedRate" name="a<%=flag%>_qualifiedRate" id="a<%=flag%>_qualifiedRate"  value="" />
		</tr>
		<tr>
			<td style="width: 80px;"><input style="width: 90%;border:none;background: none;" readonly="readonly" type="text" fieldName="unQualifiedRate" name="a<%=flag%>_unQualifiedRate" id="a<%=flag%>_unQualifiedRate"  value="" /></td>	
		</tr>
		<% 	
	    for (Map<String,String> map:defectionList) {
	    	for (String itemCode:map.keySet()) {			
				 if(!itemCode.equals("typeName")){
		%>	
		<tr>
			<td style="width: 80px;"><input type="text"  fieldName="<%=itemCode%>" name="a<%=flag%>_<%=itemCode%>" id="a<%=flag%>_<%=itemCode%>" isItem="true" value="" onkeyup="mustNum(this);caculateBadCount(this);"/>
			<span id="a<%=flag%>_<%=itemCode%>_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></td>	
		</tr>
		<% 					
				}			
			}
	    }
		%>	
	</table>
	</div>