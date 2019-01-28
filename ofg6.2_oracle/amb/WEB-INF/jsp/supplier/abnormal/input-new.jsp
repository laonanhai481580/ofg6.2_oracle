<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript">
	
	function saveForm() {
		var isRight = false;
		<security:authorize ifAnyGranted="supplier-abnormal-save">
			isRight =  true;
		</security:authorize>
		if(!isRight){
			alert("你没有权限保存！");
			return ;
		}
		if($("#inputform").valid()){
			var item=getItemDatas();
			var part=getPartDatas();
			var flagIds=$("#flagIds").val();
			var params = {};
			params["item"] = item;
			params["part"] = part;
			params["flagIds"] = flagIds;
			$("#message").html("正在保存，请稍候... ...");
			var url="${supplierctx}/abnormal/save-new.htm";
			$.post(encodeURI(url),params,function(result){
				if(result.error){
					$("#message").html("保存失败"+result.message);
				}else{
					$("#message").html(result.message);
					var i=0;
					$("table[itemData=true]").each(function(index,obj){
						if(i==0){
							$(obj).find(":input").each(function(index1,o){
								$(o).val("");
							});
						}else{
							$(obj).remove();
						}
						i++;
					});
				};
			},'json');
		}		
	}
	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="abnormal";
		var thirdMenu="abnormalList";
	</script>

	<div id="header" class="ui-north">
			<%@ include file="/menus/header.jsp" %>
		</div>
		
		<div id="secNav">
			<%@ include file="/menus/supplier-sec-menu.jsp" %>
		</div>
		
		<div class="ui-layout-west">
			<%@ include file="/menus/supplier-abnormal-menu.jsp" %>
		</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="supplier-abnormal-save">
					<button class='btn' onclick="saveForm();" type="button"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				</security:authorize>
				<span style="color:red;" id="message">
					<s:actionmessage theme="mytheme" />
				</span>
			</div>
			<div><iframe id="iframe" style="display:none;"></iframe></div>
			<div id="opt-content" style="text-align: center;">
				<form id="inputform" name="inputform" method="post" action="">
					 <%@ include file="input-form.jsp"%>
				</form>		
			</div>
		</div>
	</div>
</body>
</html>