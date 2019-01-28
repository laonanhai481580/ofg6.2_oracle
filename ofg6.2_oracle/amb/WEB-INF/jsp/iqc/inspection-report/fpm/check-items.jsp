<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.ambition.iqc.entity.IncomingInspectionActionsReport"%>
<%@ include file="/common/taglibs.jsp"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@page import="com.ambition.iqc.entity.SampleScheme"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.ambition.iqc.entity.InspectingItem"%>
<%@page import="com.ambition.iqc.entity.SampleCodeLetter"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="java.util.List"%>
<%@page import="com.ambition.iqc.entity.CheckItem"%>
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String sampleSchemeType = "",schemeStartDateStr = "";
	List<CheckItem> checkItems = (List<CheckItem>)request.getAttribute("checkItems");
	int max = 5,defaultColumns = 5;
	Map<Long,Map<String,Double>> resultMap = new LinkedHashMap<Long,Map<String,Double>>();
	long idFlag = 0;
	Map<String,Integer> mapType=new LinkedHashMap<String,Integer>();
	for(CheckItem checkItem : checkItems){
		sampleSchemeType = checkItem.getSampleSchemeType();
		if("".equals(schemeStartDateStr)){
			schemeStartDateStr = checkItem.getSchemeStartDateStr();
		}
		if(SampleScheme.EXEMPTION_TYPE.equals(sampleSchemeType)){
			break;
		}
		if(checkItem.getId()==null){
			idFlag++;
			checkItem.setId(idFlag);
		}
		if(!InspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
			Map<String,Double> tempMap = new HashMap<String,Double>();
			int tempMax = defaultColumns;
			checkItem.getResult3();
			for(int i=1;i<=80;i++){
				Double value = (Double)PropertyUtils.getProperty(checkItem,"result"+i);
				if(value != null){
					tempMap.put("result" + i, value);
					if(i>tempMax){
						tempMax=i;
					}
				}
			}
			if(tempMax>max){
				max = tempMax;
			}
			for(int i=1; i<=tempMax; i++){
				if(!tempMap.containsKey("result" + i)){
					tempMap.put("result" + i,null);
				}
			}
			resultMap.put(checkItem.getId(), tempMap);
		}
		if(checkItem.getInspectionType().indexOf("尺寸")==-1){		
			String inspectionType	=checkItem.getInspectionType();
			if(mapType.containsKey(inspectionType)){
				mapType.put(inspectionType, mapType.get(inspectionType)+1);
			}else{
				mapType.put(inspectionType, 1);
			}
		}
	}
	StringBuffer flagIds = new StringBuffer("");
%>
<table class="form-table-border-left" style="border:0px;table-layout:fixed;">
	<thead>
		<tr>
			<td colspan="13"><span style="font-weight:bold;">尺寸测量(0收1退)</span></td>
		</tr>
		<tr>
			<td style="width:20px;text-align:center;border-top:0px;border-left:0px;">NO</td>
			<td style="width:220px;text-align:center;border-top:0px;">检查项目</td>			
			<td style="width:70px;text-align:center;border-top:0px;">标准值</td>
			<td style="width:70px;text-align:center;border-top:0px;">下限</td>
			<td style="width:70px;text-align:center;border-top:0px;">上限</td>
			<td colspan="4" style="width:250px;text-align:center;border-top:0px;">检验记录</td>
			<td style="width:80px;text-align: center;border-top:0px;">Min</td>
			<td style="width:80px;text-align: center;border-top:0px;">Max</td>
			<td style="width:80px;text-align: center;border-top:0px;">Avg</td>
			<td style="width:100px;text-align: center;border-top:0px;">单项判定</td>
			<td style="width:100px;text-align: center;border-top:0px;">判定</td>
		</tr>
	</thead>
	<tbody>
		<%
			List<Option> inspectionLevels = (List<Option>)request.getAttribute("inspectionLevels");
			if(inspectionLevels == null){
				inspectionLevels = new ArrayList<Option>();
			}
			Map<String,String> levelMap = new HashMap<String,String>();
			for(Option option : inspectionLevels){
				levelMap.put(option.getValue(),option.getName());
			}
			int i=0,flag = 0;
			boolean isLast = false;
			boolean isShow=false;							
			for(CheckItem checkItem : checkItems){
				if(checkItem.getInspectionType().indexOf("尺寸")==-1){
					continue;
				}
				flag++;
				if(flag==checkItems.size()){
					isLast = true;
					
				}
				if(checkItem.getItemStatus().equals("已领取")){
					isShow=true;
					i++;
				}else{
					isShow=false;
				}
				flagIds.append(",a" + flag);
		%>
			<tr name="checkItemCC">
				<td style="text-align:center;border-left:0px;" name="<%=checkItem.getCheckItemName()%>"  itemName="<%=checkItem.getCheckItemName()%>" tdShow="<%=isShow%>"><%=i %></td>
				<td style="text-align: left;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<input type="hidden" fieldName="itemStatus"  name="a<%=flag %>_itemStatus" itemStatus="<%=checkItem.getCheckItemName()%>" itemType="<%=checkItem.getInspectionType()%>" value="<%=checkItem.getItemStatus()==null?"":checkItem.getItemStatus()%>"/>
					<input type="hidden" fieldName="classification"  name="a<%=flag %>_classification" value="<%=checkItem.getClassification()==null?"":checkItem.getClassification()%>"/>
					<input type="hidden" fieldName="unit"  name="unit" value="<%=checkItem.getUnit()==null?"":checkItem.getUnit()%>"/>
					<input type="hidden" fieldName="minlimit"  name="a<%=flag %>_minlimit" value="<%=checkItem.getMinlimit()==null?"":checkItem.getMinlimit()%>"/>
					<input type="hidden" fieldName="maxlimit"  name="a<%=flag %>_maxlimit" value="<%=checkItem.getMaxlimit()==null?"":checkItem.getMaxlimit()%>"/>
					<input type="hidden" fieldName="parentRowSpan"  name="a<%=flag %>_parentRowSpan" value="<%=checkItem.getParentRowSpan()==null?"":checkItem.getParentRowSpan()%>"/>
					<input type="hidden" fieldName="parentItemName"  name="a<%=flag %>_parentItemName" value="<%=checkItem.getParentItemName()==null?"":checkItem.getParentItemName()%>"/>
					<input type="hidden" fieldName="checkItemName"  name="a<%=flag %>_checkItemName" value="<%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%>"/>
					<input type="hidden" fieldName="codeLetter"  name="a<%=flag %>_codeLetter" value="<%=checkItem.getCodeLetter()==null?"":checkItem.getCodeLetter()%>"/>
					<input type="hidden" fieldName="inspectionType"  name="a<%=flag %>_inspectionType" value="<%=checkItem.getInspectionType()==null?"":checkItem.getInspectionType()%>"/>
					<input type="hidden" fieldName="countType"  name="a<%=flag %>_countType" value="<%=checkItem.getCountType()==null?"":checkItem.getCountType()%>"/>
					<input type="hidden" fieldName="aql"  name="a<%=flag %>_aql" value="<%=checkItem.getAql()==null?"":checkItem.getAql()%>"/>
					<input type="hidden" fieldName="aqlAc"  name="a<%=flag %>_aqlAc" value="<%=checkItem.getAqlAc()==null?"":checkItem.getAqlAc()%>"/>
					<input type="hidden" fieldName="aqlRe"  name="a<%=flag %>_aqlRe" value="<%=checkItem.getAqlRe()==null?"":checkItem.getAqlRe()%>"/>
					<input type="hidden"  fieldName="inspectionAmount" hisParent="<%=checkItem.getInspectionType() %>" name="a<%=flag %>_inspectionAmount" value="<%=checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount()%>"/>
					<input type="hidden" fieldName="sampleSchemeType"  name="a<%=flag %>_sampleSchemeType" value="<%=checkItem.getSampleSchemeType()==null?"":checkItem.getSampleSchemeType()%>"/>
					<input type="hidden" fieldName="schemeStartDate_timestamp"  name="a<%=flag %>_schemeStartDate_timestamp" value="<%=checkItem.getSchemeStartDateStr()%>"/>
					<input type="hidden" fieldName="standardRemark"  name="a<%=flag %>_standardRemark" value="<%=checkItem.getStandardRemark()==null?"":checkItem.getStandardRemark()%>"/>
					<input type="hidden" fieldName="flag"  name="a<%=flag %>_flag" value="<%=checkItem.getFlag()==null?"":checkItem.getFlag()%>"/>
					<input type="hidden" fieldName="qualifiedAmount" name="a<%=flag %>_qualifiedAmount" value="<%=checkItem.getQualifiedAmount() %>" ></input>
					<input type="hidden" fieldName="unqualifiedAmount" name="a<%=flag %>_unqualifiedAmount" value="<%=checkItem.getUnqualifiedAmount() %>" ></input>
					<%if(checkItem.getConclusion()!=null && checkItem.getConclusion().equals("NG")) {%> 
						<font style="color:red"><%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%></font>
					<%}else{ %>
					 	<%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%>
					<%} %>
				</td>				
				<td style="text-align: center;overflow:hidden; word-wrap:break-word;word-break:break-all;"  tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>" title="<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications() %>">
					<input type="hidden"  fieldName="specifications" name="a<%=flag %>_specifications" value="<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications()%>"/>
					<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications() %>
				</td>
				<td style="text-align: center;overflow:hidden; word-wrap:break-word;word-break:break-all;"  tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>" title="<%=checkItem.getMinlimit()==null?"":checkItem.getMinlimit() %>">
					<input type="hidden"  fieldName="minlimit" name="a<%=flag %>_minlimit" value="<%=checkItem.getMinlimit()==null?"":checkItem.getMinlimit()%>"/>
					<%=checkItem.getMinlimit()==null?"":checkItem.getMinlimit() %>
				</td>
				<td style="text-align: center;overflow:hidden; word-wrap:break-word;word-break:break-all;"  tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>" title="<%=checkItem.getMaxlimit()==null?"":checkItem.getMaxlimit() %>">
					<input type="hidden"  fieldName="maxlimit" name="a<%=flag %>_maxlimit" value="<%=checkItem.getMaxlimit()==null?"":checkItem.getMaxlimit()%>"/>
					<%=checkItem.getMaxlimit()==null?"":checkItem.getMaxlimit() %>
				</td>
				<%-- <td style="text-align: center;overflow:hidden; word-wrap:break-word;word-break:break-all;"  tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>" title="<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications() %>">
					<input type="hidden"  fieldName="specifications" name="a<%=flag %>_specifications" value="<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications()%>"/>
					<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications() %>
				</td> --%>
				<%-- <td style="text-align: center;overflow:hidden; word-wrap:break-word;word-break:break-all;"  tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>" title="<%=checkItem.getStandardValue()==null?"":checkItem.getStandardValue() %>">
					<input type="text" onchange="standardValueChange(this);" style="width:90%;float:left;margin-left:2px;" fieldName="standardValue" name="a<%=flag %>_standardValue" value="<%=checkItem.getStandardValue()==null?"":checkItem.getStandardValue()>100000?new BigDecimal(checkItem.getStandardValue()):new BigDecimal(new Double(checkItem.getStandardValue()).toString())%>" class="{required:true,messages:{required:'必填'}}" />
				</td> --%>
				<%-- <td style="text-align: center;" tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>" class="checkItemsClass">
					<input style="width:95%;padding:0px;text-align:center;" fieldName="result1" name="a<%=flag %>_result1" value="<%=checkItem.getResult1()==null?"":checkItem.getResult1()%>" ></input>
					<% Map<String,Double> tempMap = resultMap.get(checkItem.getId());
						Double maxlimit = checkItem.getMaxlimit(),minlimit = checkItem.getMinlimit();
						Double value1 = tempMap.get("result1");
						Double value2 = tempMap.get("result2");
						Double value3 = tempMap.get("result3");
						Double value4 = tempMap.get("result4");
						Double value5 = tempMap.get("result5");	
						String color1 = "black",color2 = "black",color3 = "black",color4 = "black",color5 = "black";
						if(maxlimit!=null&&minlimit!=null){
							if(value1!=null&&(value1<minlimit||value1>maxlimit)){
								color1="red";
							}
							if(value2!=null&&(value2<minlimit||value2>maxlimit)){
								color2="red";
							}
							if(value3!=null&&(value3<minlimit||value3>maxlimit)){
								color3="red";
							}
							if(value4!=null&&(value4<minlimit||value4>maxlimit)){
								color4="red";
							}
							if(value5!=null&&(value5<minlimit||value5>maxlimit)){
								color5="red";
							}
						}
					%>
					<input isData="true" color="<%=color1 %>" results=true onchange="this.value=this.value.replace(' ','');" style="width:90%;float:left;margin-left:2px;color:<%=color1 %>;" fieldName="result1" name="a<%=flag %>_result1" value="<%=checkItem.getResult1()==null?"":checkItem.getResult1()>100000?new BigDecimal(checkItem.getResult1()):new BigDecimal(new Double(checkItem.getResult1()).toString())%>" class="{number:true}" ></input>
				</td>
				<td style="text-align: center;" tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>" class="checkItemsClass">
					<input style="width:95%;padding:0px;" fieldName="result2" name="a<%=flag %>_result2" value="<%=checkItem.getResult2()==null?"":checkItem.getResult2()%>" ></input>
					<input isData="true" color="<%=color2 %>" results=true onchange="this.value=this.value.replace(' ','');" style="width:90%;float:left;margin-left:2px;color:<%=color2 %>; " fieldName="result2" name="a<%=flag %>_result2" value="<%=checkItem.getResult2()==null?"":checkItem.getResult2()>100000?new BigDecimal(checkItem.getResult2()):new BigDecimal(new Double(checkItem.getResult2()).toString())%>" class="{number:true}" ></input>
				</td>
				<td style="text-align: center;" tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>" class="checkItemsClass">
					<input isData="true" color="<%=color3 %>" results=true onchange="this.value=this.value.replace(' ','');" style="width:90%;float:left;margin-left:2px;color:<%=color3 %>;" fieldName="result3" name="a<%=flag %>_result3" value="<%=checkItem.getResult3()==null?"":checkItem.getResult3()>100000?new BigDecimal(checkItem.getResult3()):new BigDecimal(new Double(checkItem.getResult3()).toString())%>" class="{number:true}" ></input>
					<input style="width:95%;padding:0px;" fieldName="result3" name="a<%=flag %>_result3" value="<%=checkItem.getResult3()==null?"":checkItem.getResult3()%>" ></input>
				</td>
				<td style="text-align: center;" tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>" class="checkItemsClass">
					<input style="width:95%;padding:0px;" fieldName="result4" name="a<%=flag %>_result4" value="<%=checkItem.getResult4()==null?"":checkItem.getResult4()%>" ></input>
					<input isData="true" color="<%=color4 %>" results=true onchange="this.value=this.value.replace(' ','');" style="width:90%;float:left;margin-left:2px;color:<%=color4 %>;" fieldName="result4" name="a<%=flag %>_result4" value="<%=checkItem.getResult4()==null?"":checkItem.getResult4()>100000?new BigDecimal(checkItem.getResult4()):new BigDecimal(new Double(checkItem.getResult4()).toString())%>" class="{number:true}" ></input>
				</td>
				<td style="text-align: center;" tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>" class="checkItemsClass">
					<input style="width:95%;padding:0px;" fieldName="result5" name="a<%=flag %>_result5" value="<%=checkItem.getResult5()==null?"":checkItem.getResult5()%>" ></input>
					<input isData="true" color="<%=color5 %>" results=true onchange="this.value=this.value.replace(' ','');" style="width:90%;float:left;margin-left:2px;color:<%=color5 %>;" fieldName="result5" name="a<%=flag %>_result5" value="<%=checkItem.getResult5()==null?"":checkItem.getResult5()>100000?new BigDecimal(checkItem.getResult5()):new BigDecimal(new Double(checkItem.getResult5()).toString())%>" class="{number:true}" ></input>
				</td> --%>
				<td colspan="4" id="check-items-td" style="padding:0px;"  tdShow="<%=isShow%>" class="checkItemsClass" itemName="<%=checkItem.getCheckItemName()%>">
					<%{ 
							Map<String,Double> tempMap = resultMap.get(checkItem.getId());
							int count=0;
								count = checkItem.getInspectionAmount()==null?tempMap.keySet().size():checkItem.getInspectionAmount();
// 							int count = checkItem.getInspectionAmount()==null?tempMap.keySet().size():checkItem.getInspectionAmount();
							Double maxlimit = checkItem.getMaxlimit(),minlimit = checkItem.getMinlimit();
							for(int j=0;j<count;j++){
								String name = "result"+(j+1);
								Double value = tempMap.get(name);
								String color = "black";
								if(maxlimit==null&&minlimit!=null&&value!=null){
									if(value<minlimit){
										color="red";
									}
								}
								if(maxlimit!=null&&minlimit==null&&value!=null){
									if(value>maxlimit){
										color="red";
									}
								}
								if(maxlimit!=null&&minlimit!=null&&value!=null){
									if(value<minlimit||value>maxlimit){
										color="red";
									}
								}
						%>
								<input    color="<%=color %>" isData="true" onchange="this.value=this.value.replace(' ','');" style="width:50px;float:left;margin-left:2px;color:<%=color %>" title="<%=checkItem.getCheckItemName() + "样品" + (j+1) %>" results=true fieldName="<%=name%>" name="a<%=flag %>_<%=name%>" value="<%=value==null?"":value>100000?new BigDecimal(value):new BigDecimal(new Double(value).toString())%>" class="{number:true}" ></input>
						<%
							} 
						%>
								<a class="small-button-bg"  style="margin-left:2px;float:left;" onclick="addResultInput(this,'<%=checkItem.getCheckItemName() %>')" href="#" title="添加检验项"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="removeResultInput(this)" href="#" title="删除最后的检验项"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
					<%} %>
				</td>
				<td style="text-align: center;" tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>">
					<input isData="true" style="width: 90%;border:none;text-align: center;background: none;" readonly="readonly" fieldName="minResult" name="a<%=flag %>_minResult" value="<%=checkItem.getMinResult()==null?"":checkItem.getMinResult()%>" ></input>
				</td>
				<td style="text-align: center;" tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>">
					<input isData="true" style="width: 90%;border:none;text-align: center;background: none;" readonly="readonly" fieldName="maxResult" name="a<%=flag %>_maxResult" value="<%=checkItem.getMaxResult()==null?"":checkItem.getMaxResult()%>" ></input>
				</td>
				<td style="text-align: center;" tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>">
					<input isData="true" style="width: 90%;border:none;text-align: center;background: none;" readonly="readonly" fieldName="avgResult" name="a<%=flag %>_avgResult" value="<%=checkItem.getAvgResult()==null?"":checkItem.getAvgResult()%>" ></input>
				</td>
				<td  tdShow="<%=isShow%>" style="text-align: center;border-right:0px;" itemName="<%=checkItem.getCheckItemName()%>">
					<input type="hidden" fieldName="conclusion" name="a<%=flag %>_conclusion" value="<%=checkItem.getConclusion()%>" itemName="<%=checkItem.getCheckItemName()%>" hisParent="<%=checkItem.getInspectionType() %>"/>
					<span name="conclusionSpan" fieldName="conclusionSpan">
					<%if(checkItem.getConclusion().equals("NG")) {%> 
					<font color="red">不合格</font><%}else{ %>
					 合格<%} %> </span> 
				</td>
				<%if(flag==1){ %>
				<td  mark="wgconclusionSpan" tdShow="<%=isShow%>" style="text-align: center;border-right:0px;" itemName="<%=checkItem.getCheckItemName()%>" rowspan="<%=checkItems.size()%>">
					<input type="hidden" name="wgconclusion" id="wgconclusion" value="${wgconclusion}" type="hidden"/>
					<span id="wgconclusionSpan" >
					<%if(checkItem.getConclusion().equals("NG")) {%> 
					<font color="red">不合格</font><%}else{ %>
					 合格<%} %> </span> 
				</td>
				<%} %>
			</tr>
		<%} %>
	</tbody>
</table>
<table class="form-table-border-left" style="border:0px;">
	<thead>
		<tr>
		<td colspan="7"><span style="font-weight:bold;">特性检验(0收1退)</span></td>
		</tr>
		<tr>
			<td style="width:80px;text-align:center;border-top:0px;">NO</td>
			<td style="width:80px;text-align:center;border-top:0px;">检查类别</td>
			<td style="width:150px;text-align:center;border-top:0px;">检查项目</td>
			<td style="width:100px;text-align:center;border-top:0px;">检验工具</td>
			<td style="width:200px;text-align:center;border-top:0px;">规格</td>
			<td style="width:100px;text-align:center;border-top:0px;">抽样计划</td>
			<td style="width:200px;text-align:center;border-top:0px;">检验结果</td>
			<td style="width:80px;text-align:center;border-top:0px;">判定</td>
		</tr>
	</thead>
	<tbody>
		<%
			int j=0;
			isLast = false;
			isShow=false;
			for(String str:mapType.keySet()){
			boolean isFirst=true;
			for(CheckItem checkItem : checkItems){
				if(checkItem.getInspectionType().indexOf("尺寸")!=-1){
					continue;
				}
				if(!checkItem.getInspectionType().equals(str)){	
					
					continue;
				}				
				flag++;
				if(flag==checkItems.size()){
					isLast = true;					
				}
				if(checkItem.getItemStatus().equals("已领取")){
					isShow=true;
					j++;					
				}else{
					isShow=false;
				}
				//System.out.println(checkItem.getInspectionType()+":"+checkItem.getCheckItemName()+j);
				flagIds.append(",a" + flag);
		%>
			<tr name="checkItemTX">
				<td style="text-align:center;border-left:0px;" name="<%=checkItem.getCheckItemName()%>"  itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>"><%=j %></td>
				<% if(isFirst){  isFirst=false;%>
				<td rowspan="<%=mapType.get(str)%>" itemName="<%=checkItem.getCheckItemName()%>" tdShow="<%=isShow%>"><%=checkItem.getInspectionType()==null?"":checkItem.getInspectionType() %></td>
				<% } %>
				<td style="text-align: left;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<input type="hidden" fieldName="itemStatus"  name="a<%=flag %>_itemStatus" itemStatus="<%=checkItem.getCheckItemName()%>" itemType="<%=checkItem.getInspectionType()%>" value="<%=checkItem.getItemStatus()==null?"":checkItem.getItemStatus()%>"/>
					<input type="hidden" fieldName="classification"  name="a<%=flag %>_classification" value="<%=checkItem.getClassification()==null?"":checkItem.getClassification()%>"/>
					<input type="hidden" fieldName="unit"  name="unit" value="<%=checkItem.getUnit()==null?"":checkItem.getUnit()%>"/>
					<input type="hidden" fieldName="minlimit"  name="a<%=flag %>_minlimit" value="<%=checkItem.getMinlimit()==null?"":checkItem.getMinlimit()%>"/>
					<input type="hidden" fieldName="maxlimit"  name="a<%=flag %>_maxlimit" value="<%=checkItem.getMaxlimit()==null?"":checkItem.getMaxlimit()%>"/>
					<input type="hidden" fieldName="parentRowSpan"  name="a<%=flag %>_parentRowSpan" value="<%=checkItem.getParentRowSpan()==null?"":checkItem.getParentRowSpan()%>"/>
					<input type="hidden" fieldName="parentItemName"  name="a<%=flag %>_parentItemName" value="<%=checkItem.getParentItemName()==null?"":checkItem.getParentItemName()%>"/>
					<input type="hidden" fieldName="checkItemName"  name="a<%=flag %>_checkItemName" value="<%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%>"/>
					<input type="hidden" fieldName="codeLetter"  name="a<%=flag %>_codeLetter" value="<%=checkItem.getCodeLetter()==null?"":checkItem.getCodeLetter()%>"/>
					<input type="hidden" fieldName="inspectionType"  name="a<%=flag %>_inspectionType" value="<%=checkItem.getInspectionType()==null?"":checkItem.getInspectionType()%>"/>
					<input type="hidden" fieldName="countType"  name="a<%=flag %>_countType" value="<%=checkItem.getCountType()==null?"":checkItem.getCountType()%>"/>
					<input type="hidden" fieldName="aql"  name="a<%=flag %>_aql" value="<%=checkItem.getAql()==null?"":checkItem.getAql()%>"/>
					<input type="hidden" fieldName="aqlAc"  name="a<%=flag %>_aqlAc" value="<%=checkItem.getAqlAc()==null?"":checkItem.getAqlAc()%>"/>
					<input type="hidden" fieldName="aqlRe"  name="a<%=flag %>_aqlRe" value="<%=checkItem.getAqlRe()==null?"":checkItem.getAqlRe()%>"/>
					<input type="hidden" fieldName="sampleSchemeType"  name="a<%=flag %>_sampleSchemeType" value="<%=checkItem.getSampleSchemeType()==null?"":checkItem.getSampleSchemeType()%>"/>
					<input type="hidden" fieldName="schemeStartDate_timestamp"  name="a<%=flag %>_schemeStartDate_timestamp" value="<%=checkItem.getSchemeStartDateStr()%>"/>
					<input type="hidden" fieldName="standardRemark"  name="a<%=flag %>_standardRemark" value="<%=checkItem.getStandardRemark()==null?"":checkItem.getStandardRemark()%>"/>
					<input type="hidden" fieldName="flag"  name="a<%=flag %>_flag" value="<%=checkItem.getFlag()==null?"":checkItem.getFlag()%>"/>
					<input type="hidden" fieldName="qualifiedAmount" name="a<%=flag %>_qualifiedAmount" value="<%=checkItem.getQualifiedAmount() %>" ></input>
					<input type="hidden" fieldName="unqualifiedAmount" name="a<%=flag %>_unqualifiedAmount" value="<%=checkItem.getUnqualifiedAmount() %>" ></input>
					<%if(checkItem.getConclusion()!=null && checkItem.getConclusion().equals("NG")) {%> 
						<font style="color:red"><%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%></font>
					<%}else{ %>
					 	<%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%>
					<%} %>
				</td>
				<td style="text-align: center;" tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>">
					<input type="hidden" style="width:95%;padding:0px;text-align:center;" fieldName="checkMethod" name="a<%=flag %>_checkMethod" value="<%=checkItem.getCheckMethod()==null?"":checkItem.getCheckMethod() %>" ></input>
					<%=checkItem.getCheckMethod()==null?"":checkItem.getCheckMethod() %>
				</td>				
				<td style="text-align: center;overflow:hidden; word-wrap:break-word;word-break:break-all;"  tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>" title="<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications() %>">
					<input type="hidden" fieldName="specifications" name="a<%=flag %>_specifications" value="<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications()%>"/>
					<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications() %>
				</td>
				<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<input type="hidden"  fieldName="inspectionAmount" hisParent="<%=checkItem.getInspectionType() %>" name="a<%=flag %>_inspectionAmount" value="<%=checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount()%>"/>
					n=<%=checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount() %>,Ac/Re=<%=checkItem.getAqlAc()==null?"":checkItem.getAqlAc()%>/<%=checkItem.getAqlRe()==null?"":checkItem.getAqlRe()%>
				</td>				
				<td style="text-align: center;" tdShow="<%=isShow%>" itemName="<%=checkItem.getCheckItemName()%>" class="checkItemsClass">
					<%
						if(InspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
					%> <textarea style="width:95%;height:95%;" rows=3 fieldName="results" name="a<%=flag %>_results"  class="{maxlength:1000}"><%=checkItem.getResults()==null||checkItem.getResults()==""?"":checkItem.getResults() %></textarea>						
					<%}else{ 
							Map<String,Double> tempMap = resultMap.get(checkItem.getId());
							int count = tempMap.keySet().size();							
							Double maxlimit = checkItem.getMaxlimit(),minlimit = checkItem.getMinlimit();
							for(int k=0;k<count;k++){
								String name = "result"+(k+1);
								Double value = tempMap.get(name);
								String color = "black";
								if(maxlimit==null&&minlimit!=null&&value!=null){
									if(value<minlimit){
										color="red";
									}
								}
								if(maxlimit!=null&&minlimit==null&&value!=null){
									if(value>maxlimit){
										color="red";
									}
								}
								if(maxlimit!=null&&minlimit!=null&&value!=null){
									if(value<minlimit||value>maxlimit){
										color="red";
									}
								}
						%>
								<input    color="<%=color %>" isData="true" onchange="this.value=this.value.replace(' ','');" style="width:40px;float:left;margin-left:2px;color:<%=color %>" title="<%=checkItem.getCheckItemName() + "样品" + (j+1) %>" results=true fieldName="<%=name%>" name="a<%=flag %>_<%=name%>" value="<%=value==null?"":value>100000?new BigDecimal(value):new BigDecimal(new Double(value).toString())%>" class="{number:true}" ></input>
						<%
							} 
						%>
								<a class="small-button-bg"  style="margin-left:2px;float:left;" onclick="addResultInput(this,'<%=checkItem.getCheckItemName() %>')" href="#" title="添加检验项"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="removeResultInput(this)" href="#" title="删除最后的检验项"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
					<%} %>					
				</td>
				<td  tdShow="<%=isShow%>" style="text-align: center;border-right:0px;" itemName="<%=checkItem.getCheckItemName()%>">
<%-- 					<input type="hidden" fieldName="conclusion" name="a<%=flag %>_conclusion" value="<%=checkItem.getConclusion()%>" itemName="<%=checkItem.getCheckItemName()%>" hisParent="<%=checkItem.getInspectionType() %>"/>
 --%>					 <%-- <s:select list="conclusions" 
							  theme="simple"
							  listKey="value" 
							  listValue="name" 
							  labelSeparator=""
							  fieldName="conclusion"
							  name="a<%=flag %>_conclusion"
							  itemName="<%=checkItem.getCheckItemName()%>"
							  hisParent="<%=checkItem.getInspectionType() %>"
							  emptyOption="false"></s:select> --%>
							  <select name="a<%=flag %>_conclusion"   itemName="<%=checkItem.getCheckItemName()%>"  hisParent="<%=checkItem.getInspectionType() %>" fieldname="conclusion" onchange="caculateTotalAmount(this);">
								    <option  <%if(checkItem.getConclusion().equals("OK")){%> selected="selected" <%}%> value="OK">合格</option>
								    <option  <%if(checkItem.getConclusion().equals("NG")){%> selected="selected" <%}%> value="NG" >不合格</option>
							  </select>
				</td>
			</tr>
		<%}} %>
	</tbody>
</table>
<input type="hidden" name="flagIds" value="<%=flagIds%>">
<script type="text/javascript">
	$("td[tdShow=true]").show();
	$("td[tdShow=false]").hide();
	$(document).ready(
	);
</script>