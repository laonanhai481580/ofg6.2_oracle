<%@page import="com.ambition.carmfg.entity.MfgInspectingItem"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Date"%>
<%@page import="com.ambition.carmfg.entity.MfgPatrolDetailData"%>
<%@page import="com.ambition.carmfg.entity.MfgPatrolDetail"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.ambition.iqc.entity.SampleScheme"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.ambition.iqc.entity.InspectingItem"%>
<%@page import="com.ambition.iqc.entity.SampleCodeLetter"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.Map"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="java.util.List"%>
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
    DecimalFormat dec = new DecimalFormat("0.00000");
	SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm");
	String sampleSchemeType = SampleScheme.ORDINARY_TYPE;
	List<MfgPatrolDetail> checkItems = (List<MfgPatrolDetail>)request.getAttribute("checkItems");
	List<String> listDate = (List<String>)request.getAttribute("listDate");
	int max = 5,defaultColumns = 5;
	Map<Long,Map<String,Double>> resultMap = new HashMap<Long,Map<String,Double>>();
	long idFlag = 0;
	StringBuffer flagIds = new StringBuffer("");
	Set<Date> dateSet = new HashSet<Date>();
	for(MfgPatrolDetail checkItem : checkItems){
		if(checkItem.getId()==null){
			idFlag++;
			checkItem.setId(idFlag);
		}
		if(!MfgInspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
			Map<String,Double> tempMap = new HashMap<String,Double>();			
			int tempMax = 0;
			List<MfgPatrolDetailData> datas=checkItem.getMfgPatrolDetailDatas();
			for(MfgPatrolDetailData data:datas){
				Date inspectionDate=data.getInspectionDate();
				for(int i=1;i<=80;i++){
					Double value = (Double)PropertyUtils.getProperty(data,"result"+i);
					if(value != null&&inspectionDate!=null){
						dateSet.add(inspectionDate);
						tempMap.put(sdf.format(inspectionDate)+"result" + i, value);
						if(i>tempMax){
							tempMax=i;
						}
					}
				}
				if(tempMax>max){
					max = tempMax;
				}
				for(int i=1; i<=tempMax; i++){
					if(!tempMap.containsKey(inspectionDate==null?"":sdf.format(inspectionDate)+"result" + i)){
						tempMap.put(inspectionDate==null?"":sdf.format(inspectionDate)+"result" + i,null);
					}
				}				
			}
			resultMap.put(checkItem.getId(), tempMap);
		}else{
			List<MfgPatrolDetailData> datas=checkItem.getMfgPatrolDetailDatas();
			for(MfgPatrolDetailData data:datas){
				Date inspectionDate=data.getInspectionDate();
				if(inspectionDate!=null){
					dateSet.add(inspectionDate);
				}
			}
		}
	}
	
%>
 <table class="form-table-border-left" style="border:0px;table-layout:fixed;">
	<thead>
		<tr id="checkItemsHeader" itemTr=true>
			<td style="width:20px;text-align:center;border-top:0px;border-left:0px;">NO</td>
			<td style="width:150px;text-align:center;border-top:0px;">检查项目</td>
			<td style="width:150px;text-align:center;border-top:0px;">检查方法</td>
			<td style="width:45px;text-align:center;border-top:0px;">检验数量</td>
			<td style="width:100px;text-align:center;border-top:0px;">规格</td>
			<td style="width:45px;text-align:center;border-top:0px;">上限</td>
			<td style="width:45px;text-align:center;border-top:0px;">下限</td>
			<td style="width:45px;text-align:center;border-top:0px;">单位</td>
			<% if(dateSet.size()==0){  %>
			<td style="width:270px;border-top:0px;text-align: center;" tdFlag="1"  records="true" >
				<input name="a_inspectionDate" isDate=true type="text"  style="width:150px;"   value="" onchange="inspectionDateChange(this);"/>
				<a class="small-button-bg" style="float:right;" onclick="deleteRecord(this)" href="#" title="删除检验记录"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
			</td>
			<%}else{ 
				for(Date date:dateSet){
					int i=1;
			%>
			<td style="width:270px;padding:0px;border-top:0px;text-align: center;" tdFlag="<%=i %>"  records="true" >
				<input name="a_inspectionDate_<%=sdf.format(date)%>" isDate=true type="text"  style="width:150px;"  value="<%=sdf.format(date)%>" onchange="inspectionDateChange(this);"/>
				<a class="small-button-bg" style="float:right;" onclick="deleteRecord(this)" href="#" title="删除检验记录"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
			</td>
			<% i++;}} %>
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
			int i=1,flag = 0;
			boolean isLast = false;
			boolean isShow=false;
			for(MfgPatrolDetail checkItem : checkItems){
				flag++;
				if(flag==checkItems.size()){
					isLast = true;
				}
				if(checkItem.getItemStatus().equals("已领取")){
					isShow=true;
				}else{
					isShow=false;
				}
				flagIds.append(",a" + flag);
		%>
				<tr id="checkItemTr" name="checkItemTr" itemTr=true>
					<td style="text-align: center;border-left:0px;" name="<%=checkItem.getCheckItemName()%>"  itemName="<%=checkItem.getCheckItemName()%>" tdShow="<%=isShow%>"><%=i++ %></td>
					<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">			
					<input type="hidden" fieldName="itemStatus"  name="a<%=flag %>_itemStatus" itemStatus="<%=checkItem.getCheckItemName()%>" value="<%=checkItem.getItemStatus()==null?"":checkItem.getItemStatus()%>"/>
					<input type="hidden" fieldName="inspector" name="a<%=flag %>_inspector" inspector="<%=checkItem.getCheckItemName()%>" value="<%=checkItem.getInspector()==null?"":checkItem.getInspector()%>"/>
					<input type="hidden" fieldName="unit" name="a<%=flag %>_unit" value="<%=checkItem.getUnit()==null?"":checkItem.getUnit()%>"/>
					<input type="hidden" fieldName="minlimit" name="a<%=flag %>_minlimit" value="<%=checkItem.getMinlimit()==null?"":checkItem.getMinlimit()%>"/>
					<input type="hidden" fieldName="maxlimit" name="a<%=flag %>_maxlimit" value="<%=checkItem.getMaxlimit()==null?"":checkItem.getMaxlimit()%>"/>
					<input type="hidden" fieldName="parentRowSpan" name="a<%=flag %>_parentRowSpan" value="<%=checkItem.getParentRowSpan()==null?"":checkItem.getParentRowSpan()%>"/>
					<input type="hidden" fieldName="parentItemName" name="a<%=flag %>_parentItemName" value="<%=checkItem.getParentItemName()==null?"":checkItem.getParentItemName()%>"/>
					<input type="hidden" fieldName="checkItemName" name="a<%=flag %>_checkItemName" value="<%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%>"/>
					<input type="hidden" fieldName="codeLetter" name="a<%=flag %>_codeLetter" value="<%=checkItem.getCodeLetter()==null?"":checkItem.getCodeLetter()%>"/>
					<input type="hidden" fieldName="inspectionType" name="a<%=flag %>_inspectionType" value="<%=checkItem.getInspectionType()==null?"":checkItem.getInspectionType()%>"/>
					<input type="hidden" fieldName="countType" name="a<%=flag %>_countType" value="<%=checkItem.getCountType()==null?"":checkItem.getCountType()%>"/>
					<input type="hidden" fieldName="aql" name="a<%=flag %>_aql" value="<%=checkItem.getAql()==null?"":checkItem.getAql()%>"/>
					<input type="hidden" fieldName="aqlAc" name="a<%=flag %>_aqlAc" value="<%=checkItem.getAqlAc()==null?"":checkItem.getAqlAc()%>"/>
					<input type="hidden" fieldName="aqlRe" name="a<%=flag %>_aqlRe" value="<%=checkItem.getAqlRe()==null?"":checkItem.getAqlRe()%>"/>
					<input type="hidden" name="spcSampleIds" value="<%=checkItem.getSpcSampleIds()==null?"":checkItem.getSpcSampleIds()%>"/>
					<input type="hidden" fieldName="remark" name="a<%=flag %>_remark" value="<%=checkItem.getRemark()==null?"":checkItem.getRemark()%>"/>
					<input type="hidden" fieldName="isJnUnit" name="a<%=flag %>_isJnUnit" value="<%=checkItem.getIsJnUnit()==null?"":checkItem.getIsJnUnit()%>"/>
					<input type="hidden" fieldName="equipmentNo" name="a<%=flag %>_equipmentNo" value="<%=checkItem.getEquipmentNo()==null?"": checkItem.getEquipmentNo()%>"/>
					 <%=checkItem.getCheckItemName()==null?"":checkItem.getCheckItemName().replaceAll("\n","")%>			
				</td>
				<td style="text-align:center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<input type="hidden" fieldName="checkMethod" name="a<%=flag %>_checkMethod" value="<%=checkItem.getCheckMethod()==null?"":checkItem.getCheckMethod()%>"/>
					<%=checkItem.getCheckMethod()==null?"":checkItem.getCheckMethod() %>
				</td>
				<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<input type="hidden" fieldName="inspectionAmount" name="a<%=flag %>_inspectionAmount" value="<%=checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount()%>"/>
					<%=checkItem.getInspectionAmount()==null?"":checkItem.getInspectionAmount() %>
				</td>
				<td style="text-align: center;word-wrap:break-word;word-break:break-all;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<input type="hidden" fieldName="specifications" name="a<%=flag %>_specifications" value="<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications()%>"/>
					<%=checkItem.getSpecifications()==null?"":checkItem.getSpecifications() %>
				</td>
				<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<%=checkItem.getMaxlimit()==null?"":checkItem.getMaxlimit() %>
				</td>
				<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<%=checkItem.getMinlimit()==null?"":checkItem.getMinlimit() %>
				</td>
				<td style="text-align: center;" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>">
					<%=checkItem.getUnit()==null?"":checkItem.getUnit() %>
				</td>
				<td style="text-align: center;display: none;">
					<input type="hidden" fieldName="featureId" name="a<%=flag %>_featureId" value="<%=checkItem.getFeatureId()==null?"":checkItem.getFeatureId()%>"/>
					<%=checkItem.getFeatureId()==null?"":checkItem.getFeatureId() %>
				</td>
				<% 
				 if(dateSet.size()==0){  %>		
				 <td style="padding:0px;" class="checkItemsClass" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>"  tdFlag="1" records="true">
				
						<%
							if(InspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
						%>
							<textarea style="width:98%;height:100%;" rows=3 result=true fieldName="results" name="a<%=flag %>_results" class="{maxlength:1000}"></textarea>
						<%
						}else{ 
							Map<String,Double> tempMap = resultMap.get(checkItem.getId());						 	
							int spaceCount=5;
							int count = checkItem.getInspectionAmount()==null?spaceCount:checkItem.getInspectionAmount();
							Double maxlimit = checkItem.getMaxlimit(),minlimit = checkItem.getMinlimit();
							for(int j=0;j<count;j++){
								String name = "result"+(j+1);
						%>
								<input results=true onchange="this.value=this.value.replace(' ','');" result=true canSee="<%=checkItem.getCheckItemName()%>" color="" style="width:38px;float:left;margin-left:2px;color:" title="<%=checkItem.getCheckItemName() + "样品" + (j+1) %>" fieldName="<%=name%>" name="a<%=flag %>_<%=name%>" value="" ></input>
						<%	}
							if(count<30){
						%>
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="addResultInput(this,'<%=checkItem.getCheckItemName() %>')" href="#" title="添加检验项"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="removeResultInput(this)" href="#" title="删除最后的检验项"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
						<%} 
						}%>
				</td>
				<%					
				 }else{						
					for(Date date:dateSet){ 
					int m=1;
					List<MfgPatrolDetailData> datas=checkItem.getMfgPatrolDetailDatas();
					//System.out.println(datas.size());
					MfgPatrolDetailData data=null;
						for(MfgPatrolDetailData mfgPatrolDetailData:datas){
							if(sdf.format(mfgPatrolDetailData.getInspectionDate()).equals(sdf.format(date))){
								data=mfgPatrolDetailData;
							}
						}					
				%>
				<td style="padding:0px;" class="checkItemsClass" itemName="<%=checkItem.getCheckItemName()%>"  tdShow="<%=isShow%>"  tdFlag="<%=m %>" records="true">
				
					<%
						if(InspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
					%>
						<textarea style="width:98%;height:100%;" rows=3 result=true fieldName="results" name="a<%=flag %>_results_<%=sdf.format(date)%>" class="{maxlength:1000}"><%=data.getResults()==null?"":data.getResults() %></textarea>	
					<%		
					}else{ 
							Map<String,Double> tempMap = resultMap.get(checkItem.getId());						 	
							int spaceCount=0;
							/* if(tempMap!=null&&tempMap.keySet().size()>0&&dateSet.size()>0){
								System.out.println(tempMap.keySet());
								spaceCount=tempMap.keySet().size()/dateSet.size();
							} */ 
							//System.out.println("ss"+tempMap.size());
							for(String str:tempMap.keySet()){
								if(str.indexOf(sdf.format(date))>-1){
									spaceCount++;
								}
							}
							//System.out.println(spaceCount);
							int count = spaceCount;
							if(count==0){
								count=checkItem.getInspectionAmount();
							}
							Double maxlimit = checkItem.getMaxlimit(),minlimit = checkItem.getMinlimit();
							for(int j=0;j<count;j++){
								String name = "result"+(j+1);
								Double value = tempMap.get(date==null?"":sdf.format(date)+name);
								String color = "black";
								if(maxlimit!=null&&minlimit!=null&&value!=null){
									if(value<minlimit||value>maxlimit){
										color="red";
									}
								}
						%>
							<input results=true onchange="this.value=this.value.replace(' ','');" result=true canSee="<%=checkItem.getCheckItemName()%>" color="<%=color %>" style="width:38px;float:left;margin-left:2px;color:<%=color %>" title="<%=checkItem.getCheckItemName() + "样品" + (j+1) %>" fieldName="<%=name%>" name="a<%=flag %>_<%=name%>_<%=sdf.format(date)%>" value="<%=value==null?"":new BigDecimal(new Double(value).toString())%>" ></input>
						<%		
							}
							if(count<30){
						%>
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="addResultInput(this,'<%=checkItem.getCheckItemName() %>')" href="#" title="添加检验项"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="removeResultInput(this)" href="#" title="删除最后的检验项"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
						<%} %>
					<%} %>
				</td>
				<% m++;} 
				}%>
			</tr>
		<%} %>
	</tbody>
</table> 
<input type="hidden" name="flagIds" value="<%=flagIds%>">
<script type="text/javascript">
	$("td[tdShow=true]").show();
	/* $("td[tdShow=false]").hide(); */
</script>