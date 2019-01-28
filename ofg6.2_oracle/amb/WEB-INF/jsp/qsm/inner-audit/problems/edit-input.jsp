<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
	var isUsingComonLayout=false;
	$(function(){
		//添加验证
		editFormValidate();
		$(":input[isDate=true]").datepicker({changeYear:true,changeMonth:true,showButtonPanel: true});
	});
	function editFormValidate(){
		$("#editForm").validate({
			submitHandler: function() {
				$(".opt_btn").find("button.btn").attr("disabled","disabled");
				aa.submit('editForm','','main',showMsg);
			}
		});
	}
	function submitForm(url){
		var params = {};
		if($("#editForm").valid()){			
			$("#editForm :input[name]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
		}else{
			return;
		};
		$("#message").html("正在保存处理结果,请稍候... ...");	
		$.post(encodeURI(url),params,function(result){
			if(result.error){
				$("#message").html("保存失败"+result.message);
			}else{
				$("#message").html(result.message);
			};
		},'json');
	}
	function callback(){
		addFormValidate('${fieldPermission}','editForm');
		editFormValidate();
		showMsg();
	}
	function checkedClick(obj){
		$("#readonly").val(obj.checked);
	}
	function checkManClick(title,showInputId,hiddenInputId,multiple,defaultTreeValue){
		var acsSystemUrl = '${ctx}';
		popZtree({
	        leaf: {
	            enable: false,
	            multiLeafJson:"[{'name':'用户树','type':'MAN_DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"loginName\":\"loginName\"}','showValue':'{\"name\":\"name\"}'}]"
	        },
	        type: {
	            treeType: "MAN_DEPARTMENT_TREE",
	            showContent:"[{'id':'id','loginName':'loginName','name':'name'}]",
	            noDeparmentUser:true,
	            onlineVisible:true
	        },
	        data: {
	            treeNodeData:"id,loginName,name",
	            chkStyle:"",
	            chkboxType:"{'Y':'ps','N':'ps'}",
	            departmentShow:""
	        },
	        view: {
	            title: title,
	            width: 400,
	            height:400,
	            url:acsSystemUrl
	        },
	        feedback:{
	            enable: true,
	            showInput:showInputId,
	            showThing:"{'name':'name'}",
	            hiddenInput:hiddenInputId,
	            hiddenThing:"{'id':'id'}",
	            append:false
	        },
	        callback: {
	            onClose:function(api){
	                if(hiddenInputId){
	                    var currentClickNodeData = api.single.getCurrentClickNodeData();
	                    var user = $.parseJSON(currentClickNodeData);
	                    $("#"+hiddenInputId).val(user.loginName);
	                }
	            }
	        }
	    });
	}
</script>
</head>

<body>
	<div class="opt-body">
		<aa:zone name="main">
			<div class="opt-btn">
				<button class='btn'
					onclick="submitForm('${qsmctx}/inner-audit/problems/edit-save.htm')">
					<span><span><b class="btn-icons btn-icons-save"></b>保存
					</span>
					</span>
				</button>
				<span style="color:red;" id="message">
					<s:actionmessage theme="mytheme" />
				</span>
			</div>
			<div id="opt-content">
				<form id="editForm" name="editForm" method="post" action="">
					<div id="hiddenDiv">
						<input type="hidden" name="id" id="id" value="${id }" />
					</div>
					<table style="width: 90%;">
						<tr>
							<td >问题提交人</td>
							<td>
								<input type="hidden" id="consignorLogin" name="consignorLogin" value="${consignorLogin}" />
								<input style="width: 120px;float:left;" name="consignor" id="consignor" value="${consignor}" ></input>	
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="checkManClick('选择审核人','consignor','consignorLogin',false,'loginName')" href="javascript:void(0);" title="选择人员"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>						
							</td>
							<td >提交时间</td>
							<td>
								<input style="width: 120px;" name="consignableDate" id="consignableDate" isDate=true value="<s:date name='consignableDate' format="yyyy-MM-dd"/>" ></input>
							</td>
						</tr>
						<tr>
							<td >责任人</td>
							<td>
								<input type="hidden" id="dutyManLogin" name="dutyManLogin" value="${dutyManLogin}" />
								<input style="width: 120px;float: left;" name="dutyMan" id="dutyMan" value="${dutyMan}" ></input>
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="checkManClick('选择审核人','dutyMan','dutyManLogin',false,'loginName')" href="javascript:void(0);" title="选择人员"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>		
							</td>
							<td >改善提交时间</td>
							<td>
								<input style="width: 120px;" name="improveDate" id="improveDate" isDate=true value="<s:date name='improveDate' format="yyyy-MM-dd"/>" ></input>
							</td>
						</tr>
						<tr>
							<td >责任人主管</td>
							<td>
								<input type="hidden" id="dutyManLeadLogin" name="dutyManLeadLogin" value="${dutyManLeadLogin}" />
								<input style="width: 120px;float: left;" name="dutyManLead" id="dutyManLead" value="${dutyManLead}" ></input>	
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="checkManClick('选择责任人主管','dutyManLead','dutyManLeadLogin',false,'loginName')" href="javascript:void(0);" title="选择人员"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>		
							</td>
							<td >审核时间</td>
							<td>
								<input style="width: 120px;" name="auditorDate" id="auditorDate" isDate=true value="<s:date name='auditorDate' format="yyyy-MM-dd"/>" class="{required: true,messages:{required:'必填'}}" ></input>
							</td>
						</tr>	
						<tr>
							<td >计划完成时间<span style="color:red">*</span></td>
							<td>
								<input style="width: 120px;" name="compliteDate" id="compliteDate" isDate=true value="<s:date name='compliteDate' format="yyyy-MM-dd"/>" class="{required: true,messages:{required:'必填'}}"></input>
							</td>
							<td >实际完成时间<span style="color:red">*</span></td>
							<td>
								<input style="width: 120px;" name="actuallyDate" id="actuallyDate" isDate=true value="<s:date name='actuallyDate' format="yyyy-MM-dd"/>" class="{required: true,messages:{required:'必填'}}"></input>
							</td>
						</tr>	
						<tr>
							<td >改善确认</td>
							<td colspan="3"><textarea style="width:440px;" id="improveEffect" name="improveEffect" rows="3">${improveEffect }</textarea></td>
						</tr>
						<tr>
							<td >关闭状态</td>
							<td>
								<s:select list="closeStates"
									listKey="value" 
									listValue="name" 
									name="closeState" 
									id="closeState" 
									emptyOption="false" 
									onchange=""
									cssStyle="width:120px"
									cssClass="{required: true,messages:{required:'必填'}}"
									theme="simple">
								</s:select>
							</td>
							<td >核准人</td>
							<td>
								<input type="hidden" id="authorizerLogin" name="authorizerLogin" value="${authorizerLogin}" />
								<input style="width: 120px;float: left;" name="authorizer" id="authorizer" value="${authorizer}"></input>	
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="checkManClick('选择核准人','authorizer','authorizerLogin',false,'loginName')" href="javascript:void(0);" title="选择人员"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>		
							</td>
						</tr>																								
					</table>
				</form>
			</div>
		</aa:zone>
	</div>
</body>
</html>