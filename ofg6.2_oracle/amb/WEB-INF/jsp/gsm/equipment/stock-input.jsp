<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();
	String dateStr = sdf.format(calendar.getTime());
	String dateStr1 = sdf1.format(calendar.getTime());
	String ids=(String)ActionContext.getContext().get("ids");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>盘点计划页面</title>
	<%@include file="/common/meta.jsp" %>
	
	<script type="text/javascript">
	$(document).ready(function(){
		$("#stockPlanTime").datepicker({changeMonth:'true',changeYear:'true'});
		$("#measurementForm").validate({
			rules: {
				useDept:{required: true},
				borrower:{required: true},
				borrowDate:{required: true}
			}
		});
		gridHeight = $(document).height() - 230;
		gridWidth = $("#btmDiv").width() - 10;
		$("#gsm_table").jqGrid({
			url : '${gsmctx}/equipment/inventory-input-datas.htm?ids=<%=ids%>',
			height : gridHeight,
			width : gridWidth,
			prmNames:{
				rows:'page.pageSize',
				page:'page.pageNo',
				sort:'page.orderBy',
				order:'page.order'
			},
			datatype: "json",
			rowNum: 15,
			rownumbers:true,
			gridEdit: true,
			pager : '#pager',
			colNames:['',"责任人","使用部门","仪器管理编号","仪器名称","规格/型号","状态","校验方式"], 
			colModel:[{name:'id',index:'id',width:1,hidden:true,editable:false},
			          {name:'dutyMan',index:'dutyMan',align:'center',width:80,editable:false}, 
			          {name:'devName',index:'devName',align:'center',width:80,editable:false}, 
					  {name:'managerAssets',index:'managerAssets',align:'center',width:160,editable:false},
					  {name:'equipmentName',index:'equipmentName',align:'center',width:125,editable:false},
					  {name:'equipmentModel',index:'equipmentModel',align:'center',editable:false},
					  {name:'measurementState',index:'measurementState',align:'center',width:90,editable:false},
					  {name:'checkMethod',index:'checkMethod',align:'center',width:80,editable:false}
					  ], 
			multiselect : true,
		   	autowidth: true,
			viewrecords: true, 
			sortorder: "desc",
			ondblClickRow: function(rowId){
				editRow(rowId);
			},
			gridComplete : function(){}
		});
	});
	function submitForm(){
		var rowId = jQuery("#gsm_table").getGridParam('selarrrow');
		var stockPlanPerson =$("#stockPlanPerson").val();
		var stockPlanTime =$("#stockPlanTime").val();
		var type="P";
		var url='${gsmctx}/equipment/update-stock-datas.htm?row='+rowId;
		var par = {stockPlanPerson:stockPlanPerson,stockPlanTime:stockPlanTime,type:type};
		if(rowId.length<1){
			alert("请选择要盘点的计量器具！");
			return;
		}
		$.post(url,par,function(result){
			if(result){
				var obj=eval('('+result+')');
				alert(obj.message);
				closeInput();
			}
		});
	}
	//获取表单的值
	function getParams(){
		var params = {};
		$("#opt-content form input[type=text]").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		return params;
	}
	function closeInput(){
		window.parent.$.colorbox.close();
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" id="btmDiv">
<!-- 				<button class='btn' onclick="addGsm();"><span><span><b class="btn-icons btn-icons-find"></b>选择器具</span></span></button> -->
				<button class='btn' onclick="submitForm()"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				<button class='btn' onclick="closeInput();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>				
			</div>
			<div style="display:block;" id="message"><s:actionmessage theme="mytheme" cssStyle="color:red;" /></div>
			<div style="margin-left:5px;overflow-y:auto;"  id="opt-content">
				<form action="" method="post" id="measurementForm" name="measurementForm" >
					<fieldSet  style="padding:6px;">
						<table class="form-table-border-left" style="width:100%;margin: auto;">
							<tr>
								<td style="width:15%;">盘点计划人</td>
								<td style="width:15%;">
									<input type="hidden" id="stockPlanPerson" name="stockPlanPerson" value="<%=ContextUtils.getUserName() %>"/>
									<%=ContextUtils.getUserName() %>
								</td>
								<td style="width:15%;">计划盘点时间</td>
								<td style="width:20%;">
									<input id="stockPlanTime" name="stockPlanTime" isDate="true" value="<%=dateStr1 %>"/>
								</td>
							</tr>
						</table>
					</fieldSet>
					<fieldSet  style="padding:6px;">
						<div id="tabs-1" style="width: 100%;">
							<table id="gsm_table" ></table>
							<div id="pager"></div>
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</body>
</html>