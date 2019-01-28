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
		<caption style="font-size: 24px;">上线异常数据录入表</caption>
		<input type="hidden" name= "itemData" id="itemData" value=""/>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table" partData="true">
		<tr>
			<td style="width:10%">厂区<span style="color:red">*</td>
			<td>
				<s:select list="businessUnit" 
						  listKey="value" 
						  listValue="name"
						  theme="simple"
						  cssStyle="{required:true}"
						  id="businessUnitName"
						  name="businessUnitName"></s:select>
			</td>
			<td style="width:10%">日期<span style="color:red">*</span></td>
			<td style="width:25%">
				<input name="vlrrDate" isDate=true id="vlrrDate" value="<s:date name="vlrrDate" format="yyyy-MM-dd" />" class="{required:true,messages:{required:'必填'}}" ></input>
			</td>
			<td style="width:10%">周期<span style="color:red">*</span></td>
			<td style="width:25%">
				<input name="vlrrDate" isDate=true id="vlrrDate" value="<s:date name="vlrrDate" format="yyyy-MM-dd" />" class="{required:true,messages:{required:'必填'}}" ></input>
			</td>
			<td style="width:10%">班别<span style="color:red">*</span></td>
			<td>
				<s:select list="squads" 
						  listKey="value" 
						  listValue="name"
						  theme="simple"
						  cssStyle="{required:true}"
						  id="squad"
						  name="squad"></s:select>
			</td>
		</tr>
		<tr>
			<td style="width:10%">厂商<span style="color:red">*</span></td>
			<td>
				<input id="ofilmModel" name="ofilmModel" value="${ofilmModel}" onclick="modelClick(this);"/>
			</td>
			<td style="width:10%">工厂<span style="color:red">*</span></td>
			<td>
				<s:select list="factorys" 
						  listKey="value" 
						  listValue="name"
						  theme="simple"
						  cssStyle="{required:true}"
						  id="factory"
						  name="factory"></s:select>
			</td>
			<td style="width:10%">欧菲光机种<span style="color:red">*</span></td>
			<td>
				<input id="ofilmModel" name="ofilmModel" value="${ofilmModel}" onclick="modelClick(this);"/>
			</td>
			<td style="width:10%">筛选方式 <span style="color:red">*</span></td>
			<td>
				<s:select list="filtrates" 
						  listKey="value" 
						  listValue="name"
						  theme="simple"
						  cssStyle="{required:true}"
						   id="filtrate"
						  name="filtrate"></s:select>
			</td>
			
		</tr>
		
	</table>
	<table style="width:10%;margin: auto;float: left;" class="form-table-border-left" id="default-table" style="border:0px;">
		<tr>
			<td colspan="2" style="text-align: center;">不良项目</td>		
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">投入数</td>		
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">不良数</td>		
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">复判良品数</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">误判率 </td>		
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">复判后不良数</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">不良率</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">组合投入数</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">复判不良数</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">自责</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">签退数</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">投入数</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">总不良数</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">良品数</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">不良率</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">生产</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">厂商</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">SQM</td>	
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
			<td style="width: 80px;"><input type="text" fieldName="inputCount" name="a<%=flag%>_inputCount" id="a<%=flag%>_inputCount"  value="" onkeyup="mustNum(this);caculateBadRate(this);" >
			<span id="a<%=flag%>_inputCount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></td>		
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