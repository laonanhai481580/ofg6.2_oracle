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
		<caption style="font-size: 24px;">OBA数据录入表</caption>
		<input type="hidden" name= "itemData" id="itemData" value=""/>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table" partData="true">
		<tr>
			<td style="width:10%">厂区</td>
			<td>
				<s:select list="businessUnits" 
						  listKey="value" 
						  listValue="name"
						  theme="simple"
						  cssStyle="{required:true}"
						  name="businessUnitName"></s:select>
			</td>
			<td style="width:10%">客户名称</td>
			<td>
				<s:select list="customers" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="customerName"
					name="customerName"
					onchange="customerNameChange(this);"
					emptyOption="true">
				</s:select>
			</td>
			<td style="width:10%">欧菲机型</td>		
			<td>
				<input id="ofilmModel" name="ofilmModel" value="${ofilmModel}" onclick="modelClick(this);" />
			</td>
			<td style="width:10%">客户机型</td>		
			<td>
				<input id="customerModel" name="customerModel" value="${customerModel}" onclick="modelClick(this);"/>
			</td>
		</tr>
		<tr>
			<td style="width:10%">生产场地</td>
			<td>
				<s:select list="customerFactorys" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="place"
					name="place"
					emptyOption="true">
				</s:select>
			</td>		
			<td style="width:10%">项目</td>
			<td>
				<input id="itemName" name="itemName" value="${itemName}" />
			</td>
			<td style="width:10%">送检时间</td>		
			<td>
				<input name="sendTime" isDate=true id="sendTime" value="<s:date name="sendTime" format="yyyy-MM-dd" />"  ></input>
			</td>	
			<td style="width:10%">检验方式</td>
			<td>				
				<s:select list="inspectionMethods" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="inspectionMethod"
					name="inspectionMethod"
					emptyOption="true">
			</s:select>
			</td>		
		</tr>	
		<tr>
			<td style="width:10%">检验结论</td>
			<td>
			<s:select list="inspectionConclusions" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="inspectionConclusion"
					name="inspectionConclusion"
					emptyOption="true">
			</s:select>	
			</td>	
			<td style="width:10%">描述</td>
			<td colspan="5">
				<textarea name="remark" id="remark" rows="" cols="">${remark}</textarea>
			</td>		
		</tr>		
	</table>
	<div style="overflow-x:auto;overflow-y:hidden;">
	<table style="width:10%;margin: auto;float: left;" class="form-table-border-left" id="default-table" style="border:0px;">
		<tr>
			<td colspan="2" style="text-align: center;">不良项目</td>		
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">送检箱数</td>		
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">送检数</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">检验数</td>		
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
	<table style="width:8%;margin: auto;float: left;" class="form-table-border-left" id="default-table" name="datasTable" itemData="true">
		<tr>
			<td colspan="2" style="text-align: center;">
				<a  class="small-button-bg" style="float:left;" name="add" onclick="addRowHtml(this)" title="添加"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
				<a  class="small-button-bg" style="margin-left:2px;float:left;" name="remove" onclick="removeRowHtml(this)" title="删除"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
			</td>		
		</tr>
		<tr>
			<td style="width: 80px;"><input type="text" fieldName="sendBoxCount" name="a<%=flag%>_sendBoxCount" id="a<%=flag%>_sendBoxCount"  value="" onkeyup="mustNum(this);" >
			<span id="a<%=flag%>_sendBoxCount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></td>		
		</tr>
		<tr>
			<td style="width: 80px;"><input type="text" fieldName="sendCount" name="a<%=flag%>_sendCount" id="a<%=flag%>_sendCount"  value="" onkeyup="mustNum(this);caculateBadRate(this);" >
			<span id="a<%=flag%>_sendCount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></td>		
		</tr>				
		<tr>
			<td style="width: 80px;"><input type="text" fieldName="inspectionCount" name="a<%=flag%>_inspectionCount" id="a<%=flag%>_inspectionCount"  value="" onkeyup="mustNum(this);caculateBadRate(this);" >
			<span id="a<%=flag%>_inspectionCount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></td>		
		</tr>
		<tr>
			<td style="width: 80px;"><input type="text" fieldName="unqualifiedCount" name="a<%=flag%>_unqualifiedCount" id="a<%=flag%>_unqualifiedCount"  value="" onkeyup="mustNum(this);caculateBadRate(this);" >
			<span id="a<%=flag%>_unqualifiedCount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></td>
		</tr>
		<tr>
			<td style="width: 80px;"><input type="text" fieldName="unqualifiedRate" name="a<%=flag%>_unqualifiedRate" id="a<%=flag%>_unqualifiedRate"  value="" readonly="readonly"></td>	
		</tr>
		<% 	
	    for (Map<String,String> map:defectionList) {
	    	for (String itemCode:map.keySet()) {			
				 if(!itemCode.equals("typeName")){
		%>	
		<tr>
			<td style="width: 80px;"><input type="text"  fieldName="<%=itemCode%>" name="a<%=flag%>_<%=itemCode%>" id="a<%=flag%>_<%=itemCode%>"  value="" isItem="true" onkeyup="mustNum(this);caculateBadCount(this);">
			<span id="a<%=flag%>_<%=itemCode%>_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></td>	
		</tr>
		<% 					
				}			
			}
	    }
		%>	
	</table>
	</div>