<%@page import="com.ambition.util.common.CommonUtil"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	Map<String,Object> cacheDataMap = (Map<String,Object>)request.getAttribute("cacheDataMap");
	JSONArray categories = (JSONArray)cacheDataMap.get("categories");
	JSONArray showCategories = (JSONArray)cacheDataMap.get("showCategories");
	List<Map<String,Object>> tableDatas = (List<Map<String,Object>>)cacheDataMap.get("tableDatas");
	Integer tableWidth = categories.size()*50 + 220;
%>
<div style="position:absolute;overflow:hidden;" id="tableHeaderDiv">
<table class="form-table-border-left" style="border:0px;table-layout:fixed;" class="tableDetail" id="tableHeader">
	<tr style="background:#99CCFF;height:30px;">
		<td style="width:80px;">&nbsp;</td>
		<td style="width:100px;">&nbsp;</td>
		<%
			for(Object obj : showCategories){
		%>
			<td style="width:180px;text-align:center;" class="tableHeader">
				<%=obj %>
			</td>		
		<%} %>
	</tr>
</table>
</div>
<div style="position:absolute;overflow-x:auto;" id="tableFooterDiv">
	<div id="tableFooter">&nbsp;</div>
</div>
<table class="form-table-border-left" style="border:0px;table-layout:fixed;margin-bottom:10px;" class="tableDetail" id="tableContent">
	<%
	int idFlag = 1;
	for(Map<String,Object> tableData : tableDatas){
	%>
	<tr id=<%=idFlag++ %>>
		<td style="width:80px;"><%=tableData.get("type") %></td>
		<td style="width:100px;"><%=tableData.get("name") %></td>
		<%
			for(Object obj : categories){
				Object value = tableData.get(obj);
				if(value != null && value instanceof Number){
					value = CommonUtil.toThousands((Number)value,2);
				}
		%>
			<td style="width:180px;text-align:center;">
				<%=value==null?"":value %>
			</td>		
		<%} %>
	</tr>
	<%} %>
</table>
