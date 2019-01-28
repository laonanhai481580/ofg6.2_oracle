<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page import="com.norteksoft.product.api.ApiFactory"%>

<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
	<script type="text/javascript" src="${ctx}/widgets/calendar/WdatePicker.js"></script>
	<link rel="stylesheet" href="${ctx}/widgets/kindeditor-4.1.7/themes/default/default.css" />
	<script charset="utf-8" src="${ctx}/widgets/kindeditor-4.1.7/kindeditor-min.js"></script>
	<script charset="utf-8" src="${ctx}/widgets/kindeditor-4.1.7/lang/zh_CN.js"></script>
	<jsp:include page="input-script.jsp" />
	<script type="text/javascript">
		var editor = null;
		$(document).ready(function(){
			KindEditor.ready(function(K) {
				$("#content").height($("#opt-content").height()-$("#content").position().top+30);
		 		editor = K.create('textarea[name="content"]', {
		 			pluginsPath : '${ctx}/widgets/kindeditor/plugins/',
		 			items : ['source','|','undo', 'redo', '|','fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
		 			        'strikethrough', 'lineheight', 'removeformat','|', 'justifyleft', 'justifycenter', 'justifyright','justifyfull','insertorderedlist',
							'insertunorderedlist', 'indent', 'outdent','|','table','|','image','link','|'/**,'fullscreen'*/]
		 		});
		 	});
			//可选参数
			initSymbols();
		});
		//设置可选参数
		function initSymbols(){
			$("#symbols").empty();
			var addObjs = ["当前用户名","当前时间","系统名称","版权"];
			for(var i=0;i<addObjs.length;i++){
				var obj = addObjs[i];
				var html = "<span style='padding:2px;margin-top:2px;border:1px solid blue;margin-left:4px;float:left;cursor:pointer;' class=caculateSpan>"+obj+"</span>";
				$("#symbols").append(html);
			}
			$("#symbols").find("span.caculateSpan").click(function(){
				if(editor){
					editor.insertHtml("{"+$(this).html()+"}");
				}
			});
		}
		function submitForm(){
			if($("#basicForm").valid()){
				var params = getParams();
				if(params.content){
					delete params.content;
				}
				//alert($.param(params));
				//内容超长时,分批保存,15000个字保存一次
				var contentHtml = params.showContentHtml;
				var contentArrs = [];
				var maxLen = contentHtml.length;
				var batchLen = 15000;//每一批传入的数据长度,15000个字保存一次
				var size = parseInt(maxLen/batchLen,10);
				if(maxLen%batchLen>0){
					size++;
				}
				for(var i=0;i<size;i++){
					var start = i * batchLen;
					var end = (i+1)*batchLen;
					if(end > maxLen){
						end = maxLen;
					}
					contentArrs.push(contentHtml.substring(start,end));
				}
				var batchSave = function(params,contentArrs,index){
					var tempParams = null;
					if(index+1==contentArrs.length){
						tempParams = params;
						tempParams.isEnd = true;
					}else{
						tempParams = {};
					}
					if(index==0){
						tempParams.isStart = true;
					}
					tempParams.showContentHtml = contentArrs[index];
					$.post('${emailtemplatectx}/template/save.htm',tempParams,function(result){
						if(result.error){
							$.showMessage(result.message,"custom");
						}else if(result.success){
							$.showMessage("<s:text name="info.保存成功"/>!","custom");
							$(".opt-btn .btn").removeAttr("disabled");
							$("#id").val(result.id);
						}else{
							batchSave(params,contentArrs,index+1);
						}
					},'json');  
				};
				$.showMessage("<s:text name="info.正在保存"/>,<s:text name="info.请稍候"/>... ...","custom");
				$(".opt-btn .btn").attr("disabled",true);
				batchSave(params,contentArrs,0);
			}else{
				var error = $("#basicForm").validate().errorList[0];
				$(error.element).focus();
			}
		}
		//获取参数
		function getParams(){
			var params = {};
			$("#basicForm :input").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name){
					params[obj.name] = jObj.val();
				}
			});	
			params['showContentHtml']=editor.html();
			return params;
		}
		function createAnnouncement(){
			window.location.href = '${emailtemplatectx}/template/input.htm';
		}
		function preview(){
			$.colorbox({
				href:'${emailtemplatectx}/template/view.htm?id='+$("#id").val(),
				iframe:true, 
				width:$(window).width()-300, 
				height:$(window).height(),
				overlayClose:false,
				title:"<s:text name="info.预览"/>",
				onClosed:function(){}
			});
		}
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
 	<script type="text/javascript">
		var secMenu="template_sec";
		var thirdMenu="template_list";
	</script>

		<%@ include file="/menus/header.jsp"%>
	<div id="secNav">
		<%@ include file="/menus/emailtemplate-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/emailtemplate-template-menu.jsp"%>
	</div> 
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="emailtemplate-template-input">
				<button class='btn' onclick="createTemplate();"><span><span><b class="btn-icons btn-icons-add"></b><s:text name="新建"/></span></span></button>
				</security:authorize>
	 			<button class='btn' type="button" onclick="submitForm();"><span><span><b class="btn-icons btn-icons-save"></b><s:text name="保存"/></span></span></button>
				<s:if test="id>0">
				<button  class='btn' type="button" onclick="preview()"><span><span><b class="btn-icons btn-icons-play"></b><s:text name="预览"/></span></span></button>
				</s:if>
				<span id="message" style="color:red;"></span>
			</div>
			<div id="opt-content" style="clear:both;">
				<form id="basicForm" name="basicForm" method="post" action="">
					<input type="hidden" name="id" id="id" value="${id}"></input>
					<table class="form-table-border-left" style="width:100%;">
						<tr>
							<td style="width:14%;">编码</td>
				  			<td style="width:19%;">
								<input name="code" id="code" value="${code}" class="{required:true,messages:{required:'必填!'}}" style="width:98%;"></input>
					  		</td>
							<td style="width:14%;">名称</td>
				  			<td style="width:52%;">
						  		<input name="name" id="name" value="${name}" class="{required:true,messages:{required:'必填!'}}" style="width:98%;"></input>
							</td>
						</tr>
						<tr>
							<td style="width:14%;">主题</td>
				  			<td colspan="3">
								<input name="title" id="title" value="${title}" class="{required:true,messages:{required:'必填!'}}" style="width:98.5%;"></input>
					  		</td>
						</tr>
				  		<tr>
				  			<td colspan="4">
				  				<fieldset style="margin-top:4px;">
									<legend>可选参数</legend>
									<div style="line-height:25px;" id="symbols">
										&nbsp;
									</div>
								</fieldset>
				  			</td>
						</tr>
						<tr>
							<td colspan="4"><textarea style="width:99.8%;border: 0;" rows="15" name="content" id="content">${showContentHtml}</textarea></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>

</html>