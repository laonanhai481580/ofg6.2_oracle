<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>全面品质管理系统</title>
<%@include file="/common/meta.jsp"%>
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
	 			readonlyMode : true,
	 			items : []
	 		});
	 	});
	});
	function testEmail(){
		if($("#basicForm").valid()){
			if(!confirm("确定要发送测试邮件吗?")){
				return;
			}
			var names = $(":input[name=names]").val();
        	var loginNames = $(":input[name=loginNames]").val();
        	var url = '${emailtemplatectx}/template/test-email.htm';
        	$("button").attr("disabled","disabled");
        	$.showMessage("正在发送邮件,请稍候... ...","custom");
        	var params = {
        		names : names,
        		loginNames : loginNames,
        		templateId : '${id}'
        	};
        	$.post(url,params,function(result){
        		$.clearMessage();
        		$("button").removeAttr("disabled");
        		if(result.error){
        			alert(result.message);
        		}else{
        			$.showMessage("发送成功!");
        		}
        	},'json');
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
	//选择用户
	function _selectUser(){
		var acsSystemUrl = '${ctx}';
		popZtree({
	        leaf: {
	            enable: false,
	            multiLeafJson:"[{'name':'"+ambLocale["用户树"]+"','type':'MAN_DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"loginName\":\"loginName\"}','showValue':'{\"name\":\"name\"}'}]"
	        },
	        type: {
	            treeType: "MAN_DEPARTMENT_TREE",
	            showContent:"[{'id':'id','loginName':'loginName','name':'name'}]",
	            noDeparmentUser:true,
	            onlineVisible:true
	        },
	        data: {
	            treeNodeData:"id,loginName,name",
	            chkStyle:"checkbox",
	            chkboxType:"{'Y':'ps','N':'ps'}",
	            departmentShow:""
	        },
	        view: {
	            title:'选择用户',
	            width: 400,
	            height:400,
	            url:acsSystemUrl
	        },
	        feedback:{
	            enable: true,
// 	            showInput:showInputId,
	            showThing:"{'name':'name'}",
// 	            hiddenInput:hiddenInputId,
	            hiddenThing:"{'id':'id'}",
	            append:false
	        },
	        callback: {
	            onClose:function(api){
	            	var loginNames = ztree.getLoginNames().split(",");
	            	var names = ztree.getNames().split(",");
	            	var hisLoginNames = $(":input[name=loginNames]").val();
	            	var hisNames = $(":input[name=names]").val();
	            	for(var i=0;i<loginNames.length;i++){
	            		var loginName = loginNames[i];
	            		if((","+hisLoginNames+",").indexOf(","+loginName+",")==-1){
	            			if(hisLoginNames.length>0){
	            				hisLoginNames += ",";
	            				hisNames += ",";
	            			}
	            			hisLoginNames += loginName;
	            			hisNames += names[i];
	            		}
	            	}
	            	$(":input[name=names]").val(hisNames);
	            	$(":input[name=loginNames]").val(hisLoginNames);
	            }
	        }
	    });
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
				<span><span><b class="btn-icons btn-icons-ok"></b><s:text name="button.发送邮件"/></span></span>
				</button>
				<button class='btn' onclick="cancel();">
				<span><span><b class="btn-icons btn-icons-cancel"></b><s:text name="button.关闭"/></span></span>
				</button>
				<span id="message" style="color:red;"></span>
			</div>
			<div id="opt-content" style="clear:both;" align="center">
				<form id="basicForm" name="basicForm" method="post" action="">
				<table style="width: 100%;">
					<tr style="height:30px;">
						<td style="width:70px;">
							<label style="float:left;">收件人</label>
							<a class="small-button-bg"  style="margin-left:2px;float:left;" onclick="_selectUser(this)" href="#" title="添加"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
						</td>
						<td>
							<input type="text" readonly="readonly" name="names" value="${names}" style="width:99.8%;" class="{required:true,messages:{required:'必填!'}}"/>
							<input type="hidden" name="loginNames" value="${loginNames}" style="width:99.8%;"/>
						</td>
					</tr>
					<tr style="height:30px;">
						<td>
							主&nbsp;&nbsp;&nbsp;题
						</td>
						<td>
							<input type="text" name="title" value="${title}" style="width:99.8%;"/>
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