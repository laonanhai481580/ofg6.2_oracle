<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java"  pageEncoding="UTF-8"%>
<table class="form-table-border-left" style="border:0px; table-layout: fixed;">
	<tbody>
			<tr >
              	<td  style="width:30px;text-align:center;border-top:0px;border-left:0px;">操作</td>
				<td style="width:40px;text-align:center;border-top:0px;">审核部门</td>
				<td style="width:50px;text-align:center;border-top:0px;">资料名称</td>
				<td style="width:120px;text-align:center;border-top:0px;">资料文件</td>
				<td style="width:80px;text-align:center;border-top:0px;">备注</td>
			</tr>
			<s:iterator value="_supplierAdmitFiles" id="item" var="item">
			<tr class="supplierAdmitFiles" zbtr2=true>
				<td style="text-align: center;"><a class="small-button-bg"
					addBtn="true" onclick="addRowHtml(this)" href="#" title="添加"> <span
						class="ui-icon ui-icon-plus" style='cursor: pointer;'></span></a> 
						 <security:authorize ifAnyGranted="supplier-improve-conceal">
						<a class="small-button-bg" delBtn="true" onclick="removeRowHtml(this)"
					href="#" title="删除"> <span class="ui-icon ui-icon-minus"
						style='cursor: pointer;'></span></a> </security:authorize>
						</td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="auditDepartment" value="${auditDepartment}" title="${auditDepartment}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="materialName" value="${materialName}" title="${materialName}"/></td>
				<td ><input style="width: 95%;" type="hidden" isFile="true" name="materialFile" value="${materialFile}"/>
					<security:authorize ifAnyGranted="supplier-admit-remake-button">
					<input type="button" stage="BT1" style="width:40px;float:right;" onclick="amend(this)" value="${fileBT}"></input>
					</security:authorize>
					<input id="fileBT" name="fileBT" type="hidden" stage="BT" value="${fileBT}" ></input>
				</td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="remark" value="${remark}"/></td>
			</tr>
		</s:iterator>
	</tbody>
</table>