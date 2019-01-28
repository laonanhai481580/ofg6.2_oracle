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
	$(document).ready(function(){
		$(":input[isDate=true]").datepicker({changeYear:true,changeMonth:true,showButtonPanel: true});
	});
	function selectBusinessUnit(obj){
		window.location.href = encodeURI('${aftersalesctx}/lar/item-input-new.htm');
	}
	function addRowHtml(obj){
		var table = $(obj).closest("table");
		var clonetr = table.clone(false);
		table.after(clonetr);
		var total = $("#fir");
		var num = total.val();
		clonetr.find(":input").each(function(index ,obj){
			obj=$(obj);
			var fieldName=obj.attr("fieldName");
			obj.attr("id","a"+num+"_"+fieldName).val("");
			obj.attr("name","a"+num+"_"+fieldName);
		});
		$("#fir").val(parseInt(num)+1);
		$("#flagIds").val($("#flagIds").val()+","+"a"+num);
	}
	function removeRowHtml(obj){
		var table = $(obj).closest("table");
		var pre = table.prev("table").attr("name");
		var next = table.next("table").attr("name");
		if (next != undefined) {
			table.remove();
		} else if (pre != undefined) {
			table.remove();
		} else {
			alert('至少要保留一列');
		}
	}
	function factoryChange(obj){
		var factory=$("#factory").val();
		var url = "${mfgctx}/base-info/factory-procedure/procedure-select.htm?factory="+factory;
		$.post(encodeURI(url),{},function(result){
 			if(result.error){
 				alert(result.message);
 			}else{
				var procedures = result.procedures;
				var procedureArr = procedures.split(",");
				var procedure = document.getElementById("procedure");
				procedure.options.length=0;
				var opp1 = new Option("","");
				procedure.add(opp1);
 				for(var i=0;i<procedureArr.length;i++){
 					var opp = new Option(procedureArr[i],procedureArr[i]);
 					procedure.add(opp);
 				}
 			}
 		},'json');
	}
	function caculateBadCount(obj){
		var id=obj.id;
		var flag=id.split("_")[0];		
		var badCount=0;
		var table = $(obj).closest("table");
		$(table).find(":input[isItem=true]").each(function(index,o){
			var count=o.value;
			if(count){
				badCount+=parseInt(count);
			};
		});
		if(badCount>0){
			$("#" + flag + "_badAmount").val(parseInt(badCount));
		};
		caculateBadRate(obj);
	}
	function caculateBatchRate(obj){
		var id=obj.id;
		var flag=id.split("_")[0];		
		var stockBatch = $("#" + flag + "_stockBatch").val();//投入批
		var qualityBatch = $("#" + flag + "_qualityBatch").val();//合格批
		if(isNaN(stockBatch)){
			$("#" + flag + "_stockBatch").focus();
			$("#" + flag + "_qualityBatchRate").val("");
			return;
		}
		if(isNaN(qualityBatch)){
			$("#" + flag + "_qualityBatch").focus();
			$("#" + flag + "_qualityBatchRate").val("");
			return;
		}
		if((stockBatch-0)<(qualityBatch-0)){
			alert("合格批不能大于投入批！");
			$("#" + flag + "_qualityBatchRate").val("");
			return;
		}
		var batchRate=qualityBatch*100/stockBatch;
		$("#" + flag + "_qualityBatchRate").val(batchRate.toFixed(2)+"%");
	}
	function caculateBadRate(obj){
		var id=obj.id;
		var flag=id.split("_")[0];		
		var inputAmount = $("#" + flag + "_inputAmount").val();//入料数
		var inspectionAmount = $("#" + flag + "_inspectionAmount").val();//抽检数
		var badAmount = $("#" + flag + "_badAmount").val();//不良数
		if(isNaN(inputAmount)){
			$("#" + flag + "_inputAmount").val("");
			$("#" + flag + "_inputAmount").focus();
			return;
		}
		if(isNaN(inspectionAmount)){
			$("#" + flag + "_inspectionAmount").val("");
			$("#" + flag + "_inspectionAmount").focus();
			return;
		}
		if(isNaN(badAmount)){
			$("#" + flag + "_badAmount").val("");
			$("#" + flag + "_badAmount").focus();
			return;
		}
		if((inputAmount-0)<(inspectionAmount-0)){
			alert("抽检数不能大于入料数！");
			return;
		}
		if((inspectionAmount-0)<(badAmount-0)){
			alert("不良数不能大于抽检数！");
			$("#" + flag + "_unQualifiedRate").val("");
			return;
		}
		if(inspectionAmount&&badAmount){
			var rate=badAmount*100/inspectionAmount;
			var qualifiedAmount=inspectionAmount-badAmount;
			var qualifiedrate=qualifiedAmount*100/inspectionAmount;
			$("#" + flag + "_qualifiedAmount").val(qualifiedAmount);
			$("#" + flag + "_unQualifiedRate").val(rate.toFixed(2)+"%");
			$("#" + flag + "_qualifiedRate").val(qualifiedrate.toFixed(2)+"%");
		}		
	}
	function saveForm() {
		var isRight = false;
		<security:authorize ifAnyGranted="mfg_oqc_save">
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
			var larId=$("#larId").val();
			var params = {};
			params["larId"] = larId;
			params["item"] = item;
			params["part"] = part;
			params["flagIds"] = flagIds;
			$("#message").html("正在保存，请稍候... ...");
			var url="${aftersalesctx}/lar/item-save-new.htm";
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
	function getItemDatas(){
		var infovalue="";
		$("table[itemData=true]").each(function(index,obj){
			infovalue += getTdItem(obj);
		});
		var item ="["+infovalue.substring(1)+"]";
		return item;
	}
	function getPartDatas(){
		var value="";
		$("table[partData=true]").find(":input").each(function(index,obj){
			iobj = $(obj);
			value += ",\""+iobj.attr("name")+"\":\""+iobj.val()+"\"";
		});
		return "[{"+value.substring(1)+"}]";
	}
	function getTdItem(obj){
		var value="";
		$(obj).find(":input").each(function(index,obj){
			iobj = $(obj);
		    value += ",\""+iobj.attr("name").split("_")[1]+"\":\""+iobj.val()+"\"";
		});
		return ",{"+value.substring(1)+"}";
   	}
	function mustNum(obj){
		var id=obj.id;
		var value=obj.value;
		if(value&&isNaN(value)){
			$("#"+id+"_span").html("*必须为数字!");	
		}else{
			$("#"+id+"_span").html("");
		}
	}
	
	function selectPerson(obj, hidden) {
		var data = {
			treeNodeData : "loginName,name",
			chkboxType : "{'Y' : 'ps', 'N' : 'ps' }",
			departmentShow : ''
		};
		var multiple = "false";
		if (multiple == "true") {
			data = {
				treeNodeData : "loginName,name",
				chkStyle : "checkbox",
				chkboxType : "{'Y' : 'ps', 'N' : 'ps' }",
				departmentShow : ''
			};
		}
		var zTreeSetting = {
			leaf : {
				enable : false,
				multiLeafJson : "[{'name':'用户树','type':'MAN_DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"loginName\":\"loginName\",\"code\":\"code\",\"cardNumber\":\"cardNumber\"}','showValue':'{\"name\":\"name\"}'}]"
			},
			type : {
				treeType : "MAN_DEPARTMENT_TREE",
				showContent : "[{'id':'id','code':'code','name':'name'}]",
				noDeparmentUser : false,
				onlineVisible : true
			},
			data : data,
			view : {
				title : "用户树",
				width : 300,
				height : 400,
				url : webRoot
			},
			feedback : {
				enable : true,
				showInput : "showInput",
				showThing : "{'name':'name'}",
				hiddenInput : "hiddenInput",
				hiddenThing : "{'loginName':'loginName'}",
				append : false
			},
			callback : {
				onClose : function(api) {
					if (multiple == "true") {
						$("#" + obj).val(ztree.getNames());
						$("#" + hidden).val(ztree.getLoginNames());
					} else {
						$("#" + showInputId).val(ztree.getName());
						$("#" + hiddenInputId).val(ztree.getLoginName());
					}
				}
			}
		};
		popZtree(zTreeSetting);
	}
	
 	function modelClick(){
		var customerName=$("#customerName").val();
		if(!customerName){
			alert("请先选择客户！");
			return;
		}
 		var url = '${aftersalesctx}/base-info/customer/model-select.htm?customerName='+customerName;
 		$.colorbox({href:url,iframe:true, 
 			innerWidth:700, 
			innerHeight:500,
 			overlayClose:false,
 			title:"选择机型"
 		});
 	}
 	function setProblemValue(datas){
 		$("#customerModel").val(datas[0].value);
 		$("#ofilmModel").val(datas[0].key);
 	}	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="lar";
		var thirdMenu="lar_data";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/aftersales-sec-menu.jsp"%> 
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/aftersales-lar-third-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="AFS_LAR_ITEM_SAVE">
					<button class='btn' onclick="saveForm();" type="button"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				</security:authorize>
				<span style="color:red;" id="message">
					<s:actionmessage theme="mytheme" />
				</span>
			</div>
			<div><iframe id="iframe" style="display:none;"></iframe></div>
			<div id="opt-content" style="text-align: center;">
				<form id="inputform" name="inputform" method="post" action="">
					 <%@ include file="input-form-new.jsp"%>
				</form>		
			</div>
		</div>
	</div>
</body>
</html>