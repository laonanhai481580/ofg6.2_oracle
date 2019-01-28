<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
			<div id="search" style="display:block;">
				<table class="form-table-outside-border" style="width:100%;margin-top:4px;">
					<tr>
						<td>
							<ul id="searchUl">
								<li class='searchli'>
									<span class='label'>日期</span>
									<input id="datepicker1" isDate=true type="text" readonly="readonly" style="width:75px;" class="line" name="params.totalDate_start" value="${defaultDate_start}"/>
									至
									<input id="datepicker2" isDate=true type="text" readonly="readonly" style="width:75px;" class="line" name="params.totalDate_end" value="${defaultDate_end}"/>
								</li>
								<li class='searchli'>
									<span class='label'>厂区</span>
									<s:select list="businessUnits"
										listKey="value" 
										listValue="name" 
										name="params.businessUnitName" 
										id="businessUnitName"
										emptyOption="true"
										theme="simple"
										onchange=""></s:select>
								</li>
								<li class='searchli'>
									<span class='label'>工厂</span>
									<s:select list="factorys"
										listKey="value" 
										listValue="name" 
										name="params.factory" 
										id="factory"
										emptyOption="true"
										theme="simple"
										onchange="factoryChange(this);"></s:select>
								</li>
								<li class='searchli'>
									<span class='label'>工序</span>
									<s:select list="procedures"
										listKey="value" 
										listValue="name" 
										name="params.procedure" 
										id="procedure"
										emptyOption="true"
										theme="simple"
										onchange=""></s:select>
								</li>
								<li class='searchli'>
									<span class='label'>产品类别</span>
									<s:select list="productTypes"
										listKey="value" 
										listValue="name" 
										name="params.productType" 
										id="productType"
										emptyOption="true"
										theme="simple"
										onchange=""></s:select>
								</li>
								<li class='searchli'>
									<span class='label'>客户</span>
									<input type="text" id="customer" class="input" name="params.customer" value="${customer}"/>
								</li>
								<li class='searchli'>
									<span class='label'>机种</span>
									<input type="text" id="model" class="input" name="params.model" value="${model}"/>
								</li>
								<li class='searchli'>
									<span class='label'>班别</span>
									<s:select list="classGroups"
										listKey="value" 
										listValue="name" 
										name="params.classGroup" 
										id="classGroup"
										emptyOption="true"
										theme="simple"
										onchange=""></s:select>
								</li>
								<li class='searchli'>
									<span class='label'>作业员</span>
									<input type="text" id="operator" class="input" name="params.operator" value="${operator}"/>
									<input type="hidden" id="order" class="input" name="params.order" value="asc"/>
								</li>
							</ul>
						</td>
					</tr>		
				</table>
				<table class="form-table-outside-border" style="width:100%;">
						<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;padding-left: 10px;">统计分组</caption>
						<tr style="height:40px;">
							<td style="padding:0px;margin:0px;padding-bottom:2px;">
								<input type="radio" name="params.groupValue" id="group1" value="day" title="日" checked="checked"/><label for="group1">日</label>
 								<input type="radio" name="params.groupValue" id="group2" value="week" title="周"/><label for="group2">周</label>
								<input type="radio" name="params.groupValue" id="group3" value="month" title="月"/><label for="group3">月</label>
								<input type="radio" name="params.groupValue" id="group4" value="quarter" title="季"/><label for="group4">季</label>
								<input type="radio" name="params.groupValue" id="group5" value="year" title="年"/><label for="group5">年</label>
								<input type="radio" name="params.groupValue" id="group6" value="businessUnitName" title="厂区"/><label for="group6">厂区</label>
								<input type="radio" name="params.groupValue" id="group7" value="factory" title="工厂"/><label for="group7">工厂</label>
								<input type="radio" name="params.groupValue" id="group8" value="procedure" title="工序"/><label for="group8">工序</label>
								<input type="radio" name="params.groupValue" id="group9" value="customer" title="客户"/><label for="group9">客户</label>
								<input type="radio" name="params.groupValue" id="group10" value="model" title="机种"/><label for="group10">机种</label>
								<input type="radio" name="params.groupValue" id="group11" value="lineType" title="组/线别"/><label for="group11">组/线别</label>
								<input type="radio" name="params.groupValue" id="group12" value="operator" title="作业员"/><label for="group12">作业员</label>								
								<div style="float:right;margin-right:8px;">
									<!--  <select name="params.order" style="width:80px;">
										<option value="desc">降序</option>
										<option value="asc">升序</option>
									</select> -->									
									显示分组前&nbsp;<input type="text" size="5" value="10" style="width:40px;text-align: center;" name="params.pageSize" id="pageSize" onchange="pageSizeChange(this)"/>&nbsp;项
								</div>
							</td>
						</tr>
				</table>
				 <table class="form-table-outside-border" style="width:100%">
						<caption style="font-weight: bold;text-align: left;padding-top:4px;padding-bottom:4px;padding-left: 10px;">统计维度Y轴</caption>
						<tr style="height:40px;">
							<td style="padding:0px;margin:0px;padding-bottom:2px;">
								<input type="radio" name="params.target" id="target1"  checked="checked" value="itemPPM" title="不良PPM"/><label for="target1">不良PPM</label>								
							</td>
						</tr>
				</table> 
			</div>
			<div>
				<table style="width:100%;">
					<tr>
						<td id="reportDiv_parent">
							<div id='reportDiv'></div>
						</td>
					</tr>
				</table>
			</div>
			<div id="tableDiv" style="overflow:hidden;">
				
			</div>
		
