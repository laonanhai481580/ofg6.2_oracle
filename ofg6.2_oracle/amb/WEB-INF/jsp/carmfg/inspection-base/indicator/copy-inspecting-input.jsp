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
		$("#message").html("正在复制检验标准,请稍候... ...");	
		$.post(encodeURI(url),params,function(result){
			if(result.error){
				$("#message").html(result.message);
			}else{
				$("#message").html(result.message);
			};
		},'json');
	}
</script>
</head>

<body>
	<div class="opt-body">
		<aa:zone name="main">
			<div class="opt-btn">
				<button class='btn'
					onclick="submitForm('${mfgctx}/inspection-base/indicator/copy-inspecting-indicator.htm')">
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
							<td >机种<span style="color:red">*</span></td>
							<td>
								<input type="text" id="model" name="model" value="" class="{required: true,messages:{required:'必填'}}"/>
							</td>
							<td >机种名称<span style="color:red">*</span></td>
							<td>
								<input style="width: 120px;" name="modelName" id="modelName"  value="" class="{required: true,messages:{required:'必填'}}"></input>
							</td>
						</tr>
						<tr>
							<td >工序 <span style="color:red">*</span></td>
							<td>
								<s:select list="workingProcedures"
									listKey="value" 
									listValue="name" 
									name="workingProcedure" 
									id="workingProcedure" 
									emptyOption="true" 
									onchange=""
									cssStyle="width:150px"
									cssClass="{required: true,messages:{required:'必填'}}"
									theme="simple">
								</s:select>
							</td>
							<td >版本号</td>
							<td>
								<input style="width: 120px;" name="standardVersion" id="standardVersion"  value="" ></input>
							</td>
						</tr>
																												
					</table>
				</form>
			</div>
		</aa:zone>
	</div>
</body>
</html>