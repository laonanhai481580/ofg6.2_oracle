<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>QMS-质量管理系统</title>
<%@include file="/common/meta.jsp"%>
<style type="text/css">
	.member{
		float:left;
		cursor:pointer;
		padding:10px;
		border:1px;
	}
</style>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/js/select-box.js"></script>
<link rel="stylesheet" href="${ctx}/widgets/kindeditor-4.1.7/themes/default/default.css" />
<script charset="utf-8" src="${ctx}/widgets/kindeditor-4.1.7/kindeditor-min.js"></script>
<script charset="utf-8" src="${ctx}/widgets/kindeditor-4.1.7/lang/zh_CN.js"></script>
<jsp:include page="input-script.jsp" />
	<script type="text/javascript">
		var editor = null;
		$(document).ready(function(){
			KindEditor.ready(function(K) {
				$("#content").height($("#opt-content").height()-$("#content").position().top+30);
		 		editor = K.create('textarea[name="content"]',{
		 			pluginsPath : '${ctx}/widgets/kindeditor-4.1.7/plugins/',
		 			items : ['source','|','undo', 'redo', '|','fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
		 			        'strikethrough', 'lineheight', 'removeformat','|', 'justifyleft', 'justifycenter', 'justifyright','justifyfull','insertorderedlist',
							'insertunorderedlist', 'indent', 'outdent','|','table','|','image','link','|'/**,'fullscreen'*/],
					htmlTags:{
					        font : ['color', 'size', 'face', '.background-color'],
					        span : [
					                '.color', '.background-color', '.font-size', '.font-family', '.background',
					                '.font-weight', '.font-style', '.text-decoration', '.vertical-align', '.line-height'
					        ],
					        div : [
					                'align', '.border', '.margin', '.padding', '.text-align', '.color',
					                '.background-color', '.font-size', '.font-family', '.font-weight', '.background',
					                '.font-style', '.text-decoration', '.vertical-align', '.margin-left'
					        ],
					        table: [
					                'border', 'cellspacing', 'cellpadding', 'width', 'height', 'align', 'bordercolor',
					                '.padding', '.margin', '.border', 'bgcolor', '.text-align', '.color', '.background-color',
					                '.font-size', '.font-family', '.font-weight', '.font-style', '.text-decoration', '.background',
					                '.width', '.height', '.border-collapse','style'
					        ],
					        'td,th': [
					                'align', 'valign', 'width', 'height', 'colspan', 'rowspan', 'bgcolor',
					                '.text-align', '.color', '.background-color', '.font-size', '.font-family', '.font-weight',
					                '.font-style', '.text-decoration', '.vertical-align', '.background', '.border','style'
					        ],
					        a : ['href', 'target', 'name'],
					        embed : ['src', 'width', 'height', 'type', 'loop', 'autostart', 'quality', '.width', '.height', 'align', 'allowscriptaccess'],
					        img : ['src', 'width', 'height', 'border', 'alt', 'title', 'align', '.width', '.height', '.border'],
					        'p,ol,ul,li,blockquote,h1,h2,h3,h4,h5,h6' : [
					                'align', '.text-align', '.color', '.background-color', '.font-size', '.font-family', '.background',
					                '.font-weight', '.font-style', '.text-decoration', '.vertical-align', '.text-indent', '.margin-left'
					        ],
					        pre : ['class'],
					        hr : ['class', '.page-break-after'],
					        'tr':['style'],
					        'br,tbody,strong,b,sub,sup,em,i,u,strike,s,del' : []
					}
		 		});
		 		if(window.parent.getEmailInitParams){
		 			$.showMessage("正在初始参数内容,请稍候... ...","custom");
		 			window.parent.getEmailInitParams(function(params){
		 				$.clearMessage();
		 				if(params.contentHtml){
		 					editor.html(params.contentHtml);
		 				}
		 				if(params.title){
		 					$("#title").val(params.title);
		 				}
		 				if(params.loginNames&&params.userNames){
		 					showUserDivInfos(params.loginNames.split(","),params.userNames.split(','));
		 				}
		 				//提交时的扩展参数
		 				if(params.extendParams){
		 					var extendParams = params.extendParams;
		 					for(var pro in extendParams){
		 						var $input = $("#basicForm").find(":input[name="+pro+"]");
		 						if($input.length==0){
		 							$input = $("<input type='hidden' name='"+pro+"'/>");
		 							$("#basicForm").append($input);
		 						}
		 						$input.attr("extend",true);
		 						$input.val(extendParams[pro]);
		 					}
		 				}
		 			});
		 		}
		 	});
// 			//添加人员事件
// 			addMemberEvent($("div.member"));
		});
		function addMemberEvent($members){
			$members.bind("mouseover",function(){
				$(this).css("background","yellow");
			}).bind("mouseout",function(){
				$(this).css("background","");
			}).bind("dblclick",function(){//alert($(this).attr("loginName"))
				$(this).remove();
		    	var hisNames = $(":input[name=names]").val().split(",");
		    	var hisLogins = $(":input[name=loginNames]").val().split(",");
		    	var uname="";
		    	var ulogin="";
		    	for(var i=0;i<hisNames.length;i++){
		    		var name = hisNames[i];
		    		var login = hisLogins[i];
		    		if($(this).html().indexOf(name)==-1){
		    			if(uname.length>0){
		    				uname += ",";
		    				ulogin+=",";
    	    			}
		    			uname += name;
		    			ulogin += login;
		    		}
		    	}$(":input[name=names]").val(uname);
		    	$(":input[name=loginNames]").val(ulogin);
			});
		}
		function testEmail(){
			if($("#basicForm").valid()){
				if(!confirm("确定要发送邮件吗?")){
					return;
				}
				var params = {};
				params.userNames = $(":input[name=names]").val();
				params.loginNames = $(":input[name=loginNames]").val();
				params.title = $(":input[name=title]").val();
				$(":input[extend][name]").each(function(index,obj){
					params[obj.name] = $(obj).val();
				});
				//内容超长时,分批保存,15000个字保存一次
				var contentHtml = editor.html();
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
					$.post('${emailtemplatectx}/template/send-email.htm',tempParams,function(result){
						if(result.error){
							$(".opt-btn .btn").removeAttr("disabled");
							$.showMessage(result.message,"custom");
						}else if(result.success){
							$(".opt-btn .btn").removeAttr("disabled");
							if(window.parent.afterSendEmail){
								window.parent.afterSendEmail({
									userNames:params.userNames,
									loginNames:params.loginNames
								});
							}else{
								alert("<s:text name="发送成功"/>!","custom");
								window.parent.$.colorbox.close();
							}
						}else{
							batchSave(params,contentArrs,index+1);
						}
					},'json');  
				};
				$.showMessage("<s:text name="正在发送"/>,<s:text name="请稍候"/>... ...","custom");
				$(".opt-btn .btn").attr("disabled",true);
				batchSave(params,contentArrs,0);
			}else{
				var error = $("#basicForm").validate().errorList[0];
				$(error.element).focus();
			}
		}
		//关闭
		function cancel(){
			<%
				String _from = request.getParameter("_from");
				if("portal".equals(_from)){
			%>
			window.close();
			<%}else{%>
			window.parent.$.colorbox.close();
			<%}%>
		}
		//选人后的回调函数
		function _selectUserCallback(){
	 		var loginNames = chkStyle == "checkbox"?ztree.getLoginNames().split(","):ztree.getLoginName().split(",");
   	    	var userNames = chkStyle == "checkbox"?ztree.getNames().split(","):ztree.getName().split(",");
   	    	showUserDivInfos(loginNames,userNames);
		}
		function showUserDivInfos(loginNames,userNames){
			var hisLoginNames = $(":input[name=loginNames]").val();
	    	var hisNames = $(":input[name=names]").val();
	    	for(var i=0;i<loginNames.length;i++){
	    		var loginName = loginNames[i],userName = userNames[i];
	    		if(!loginName){
	    			continue;
	    		}
	    		if($("#memberTd").find("div[loginName="+loginName+"]").length==0&&(","+hisLoginNames+",").indexOf(","+loginName+",")==-1){
	    			if(typeof userName != "undefined"){
		    			var html="<div title='双击移除用户' class='member' loginName='"+loginName+"' userName='"+userName+"'>";
		    			if(userNames[i]=="所有人员"){
		    				$("#memberTd").html("");
		    				html = html+userName;
		    				
		    				hisLoginNames = loginName;
	            			hisNames = userNames[i];
		    			}else{
		    				html = "<div title='双击移除用户' class='member' loginName='"+loginName+"' userName='"+userName+"'>";
		    				html += userName;
		    				
		    				if(hisLoginNames.length>0){
	    	    				hisLoginNames += ",";
	    	    				hisNames += ",";
	    	    			}
	        				hisLoginNames += loginName;
	            			hisNames += userNames[i];
		    			}
		    			html += "</div>";
		    			var $member = $(html);
		    			$("#memberTd").append($member);
		    			addMemberEvent($member);
	    			}
	    		}
	    		$(":input[name=names]").val(hisNames);
	    		$(":input[name=loginNames]").val(hisLoginNames);
	    	}
		}
		//清除所选所有成员
		function clearMember(){
			if(!confirm("确定要删除所有收件人吗?")){
				return;
			}
			$("#memberTd").html("");
			$(":input[name=names]").val("");
	    	$(":input[name=loginNames]").val("");
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="asd";
		var thirdMenu="aa";
	</script>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<button class='btn' onclick="testEmail();">
					<span><span><b class="btn-icons btn-icons-ok"></b><s:text name="发送邮件"/></span></span>
				</button>
				<button class='btn' onclick="cancel();">
					<span><span><b class="btn-icons btn-icons-cancel"></b><s:text name="关闭"/></span></span>
				</button>
				<span id="message" style="color:red;"></span>
			</div>
			<div id="opt-content" style="clear:both;" align="center">
				<form id="basicForm" name="basicForm" method="post" action="">
				<table style="width: 100%;">
					<tr style="height:30px;">
						<td style="width:90px;">
							<label style="float:left;">收件人</label>
							<a class="small-button-bg"  style="margin-left:2px;float:left;" onclick="_selectUser('names','loginNames','checkbox');" href="#" title="添加人员">
								<span class="ui-icon ui-icon-plusthick" style='cursor:pointer;'></span>
							</a>
							<a class="small-button-bg"  style="margin-left:2px;float:left;" onclick="clearMember()" href="#" title="清除全部">
								<span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span>
							</a>
						</td>
						<td id="memberTd" style='border:1px solid gray;background:white;line-height:3px;'>
							
						</td>
						<td>
							<input type="hidden" readonly="readonly" name="names" value="${names}" style="width:99.8%;" class="{required:true,messages:{required:'必填!'}}"/>
							<input type="hidden" name="loginNames" value="${loginNames}" style="width:99.8%;"/>
						</td>
					</tr>
					<tr style="height:30px;">
						<td>
							邮件主题&nbsp;&nbsp;
						</td>
						<td>
							<input type="text" id="title" name="title" value="${title}" style="width:99.8%;"/>
						</td>
					</tr>
					<tr>
						<td colspan="2"><textarea style="width:100%;border: 0;" rows="15" name="content" id="content">${showContentHtml}</textarea></td>
					</tr>
				</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>