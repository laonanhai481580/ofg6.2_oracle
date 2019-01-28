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
		<caption style="font-size: 28px;padding-top: 10px;padding-bottom: 10px;">管理评审表</caption>
		<input type="hidden" id="currentActivityName" name="currentActivityName" value="${currentActivityName }" />
<%-- 	<tr>
		<th colspan="4" style="text-align:right;padding-bottom:4px;">
			编号:${formNo}
		</th>
	</tr> --%>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<tr>
			<td style="width:160px;text-align:center;">公司名称</td>
			<td style="width:200px;">
				<s:select list="companyNames"
					listKey="value" 
					listValue="name" 
					name="companyName" 
					id="companyName" 
					cssStyle="width:180px;"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td style="width:160px;text-align:center;">日期</td>
			<td style="width:200px;">
			<input  type="text" isDate="true" id="riqi" name="riqi" value="<s:date name='riqi' format="yyyy-MM-dd"/>" />
			</td>
			<td style="width:160px;text-align:center;">编号</td>
<%-- 			<td style="width:200px;">${formNo}</td>	 --%>
			<td>No.<input id="manageNo" name="manageNo" type="text" value="${manageNo}" /></td>		
		</tr>
		<tr >
			<td style="width:160px;text-align:center;">体系名称</td>
			<td style="width:200px;" colspan="5">
				<s:checkboxlist
					theme="simple" list="systemNames" listKey="value" listValue="name"
					name="systemName" value="#request.systemNameList">
				</s:checkboxlist>
			</td>
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">管理评审计划</td>
		</tr>
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="2"   id="remark1" name="remark1"  >${remark1}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">附件上传</td>
			<td style="width:200px;" colspan="5">
				<input type="hidden"  isFile="true" id="attachment1" name="attachment1" value="${attachment1}"/>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">提交人</td>
			<td style="width:200px;">
				${submitter1}
			</td>
			<td style="width:160px;text-align:center;">审核</td>
			<td style="width:200px;">
				<input type="text" id="auditMan1" isTemp="true" isUser="true" hiddenInputId="auditMan1Login" style="float: left;"  name="auditMan1" value="${auditMan1}" />
				<input type="hidden" id="auditMan1Login" name="auditMan1Login" value="${auditMan1Login}" />
			</td>
			<td style="width:160px;text-align:center;">核准</td>
			<td style="width:200px;">
				<input type="text" id="approvedMan1" isTemp="true" isUser="true" hiddenInputId="approvedMan1Login" style="float: left;"  name="approvedMan1" value="${approvedMan1}" />
				<input type="hidden" id="approvedMan1Login" name="approvedMan1Login" value="${approvedMan1Login}" />
			</td>
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">评审输出</td>
		</tr>
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="2"   id="remark2" name="remark2"  >${remark2}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">实际评审日期</td>
			<td style="width:200px;">
			<input  type="text" isDate="true" id="actualReviewDate" name="actualReviewDate" value="<s:date name='actualReviewDate' format="yyyy-MM-dd"/>" />
			</td>
			<td style="width:160px;text-align:center;">附件上传</td>
			<td style="width:200px;" colspan="3">
				<input type="hidden"  isFile="true" id="attachment2" name="attachment2" value="${attachment2}"/>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">提交人</td>
			<td style="width:200px;">
				${submitter2}
				<input type="hidden" id="submitter2"    name="submitter2" value="${submitter2}" />
				<input type="hidden" id="submitter2Login" name="submitter2Login" value="${submitter2Login}" />
			</td>
			<td style="width:160px;text-align:center;">审核</td>
			<td style="width:200px;">
				<input type="text" id="auditMan2" isTemp="true" isUser="true" hiddenInputId="auditMan2Login" style="float: left;"  name="auditMan2" value="${auditMan2}" />
				<input type="hidden" id="auditMan2Login" name="auditMan2Login" value="${auditMan2Login}" />
			</td>
			<td style="width:160px;text-align:center;">核准</td>
			<td style="width:200px;">
				<input type="text" id="approvedMan2" isTemp="true" isUser="true" hiddenInputId="approvedMan2Login" style="float: left;"  name="approvedMan2" value="${approvedMan2}" />
				<input type="hidden" id="approvedMan2Login" name="approvedMan2Login" value="${approvedMan2Login}" />
			</td>
		</tr>
		<tr >
			<td style="width:160px;text-align:center;">发布部门</td>
	         <td colspan="3">
	            <input style="float:left;width:70%;border:none;text-align: center;background: none;" readonly="readonly" name="copyMan" id="copyMan" value="${copyMan}" />
	          	<input style="float:left;" type='hidden' name="copyLoginMan" id="copyLoginMan" value="${copyLoginMan}" />
	           	<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="selectPerson1('copyMan','copyLoginMan');"><span class="ui-icon ui-icon-search"  ></span></a>
	           	<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('copyMan','copyLoginMan')" href="javascript:void(0);" title="<s:text name='清空'/>">
	           	<span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
	         </td>
	         <td colspan="2"></td>
		</tr>
	</table>