<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@page import="com.ambition.iqc.entity.IncomingInspectionActionsReport"%>
<%@page import="com.ambition.util.useFile.entity.UseFile"%>
<%@page import="org.apache.struts2.ServletActionContext"%>
<%@page import="com.ambition.util.useFile.dao.UseFileDao"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script language="javascript" src="${ctx}/widgets/lodop/LodopFuncs.js"></script>
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="${ctx}/widgets/lodop/install_lodop.exe"></embed>
	</object>
	<%
		String changeView=(String)ActionContext.getContext().get("changeView");
		String isNewData=(String)ActionContext.getContext().get("isNewData");
	%>
	<script type="text/javascript">
	 var LODOP; //声明打印的全局变量 
	function getScrollTop(){
		 return $("#opt-content").height()+23;
	}
	$(document).ready(function(){
		var methods1 = '${packingFir}'.split(",");
		var flag1=false;
		if(methods1==""){
			flag1=true;
		}
		$(":input[filename=packingFir]").each(function(index,obj){
			var checked = false;
			for(var i=0;i<methods1.length;i++){
				if($.trim(obj.value)==$.trim(methods1[i])||flag1){
					checked = true;
					break;
				}
			}
			if(checked){
				$(obj).attr("checked","checked");
			}else{
				$(obj).removeAttr("checked");
				flag=false;
			}
		});
		var methods2 = '${packingSec}'.split(",");
		var flag2=false;
		if(methods2==""){
			flag2=true;
		}
		$(":input[filename=packingSec]").each(function(index,obj){
			var checked = false;
			for(var i=0;i<methods2.length;i++){
				if($.trim(obj.value)==$.trim(methods2[i])||flag2){
					checked = true;
					break;
				}
			}
			if(checked){
				$(obj).attr("checked","checked");
			}else{
				$(obj).removeAttr("checked");
			}
		});
		autoSave();
		uncheckNoChange();
		$.parseDownloadPath({
			showInputId : 'showAttachmentFiles',
			hiddenInputId : 'attachmentFiles'
		});
		var attachments=[];
		$("td[name=attachmentSon").each(function(index,obj){
			attachments.push({showInputId:'a'+(index+1)+'_showAttachment',hiddenInputId:'a'+(index+1)+'_attachmentFiles'});//子表上传附件
		});
		attachments.push({showInputId:'iqcInspectionDatas',hiddenInputId:'fileAll'});
		parseDownloadFiles(attachments);
		var methods3 = '${packingTre}'.split(",");
		$(":input[name=packingTre]").each(function(index,obj){
			var checked = false;
			for(var i=0;i<methods3.length;i++){
				if($.trim(obj.value)==$.trim(methods3[i])){
					checked = true;
					break;
				}
			}
			if(checked){
				$(obj).attr("checked","checked");
			}else{
				$(obj).removeAttr("checked");
			}
		});
		var id=$("#id").val();
		$.post("${supplierctx}/improve/is-exist-improve.htm",{id:id},function(result){
			if(result.error){
				//$("#isImprove").attr("disabled","");
				$("#isImprove").val("true");
			}
		},'json');
		
		$.post("${supplierctx}/improve-new/exception-contact/is-exist-improve.htm",{id:id},function(result){
			if(result.error){
				//$("#isImprove").attr("disabled","");
				$("#isImprove").val("true");
			}
		},'json');
		//测试验证
		if("${inspectionState}"=="待检验"||"${inspectionState}"== "暂存"){
			testValidate();
		}		
		$.clearMessage(3000);
	 });
	
	function autoSave(){
		setTimeout(function(){
			/* submitForm('${mfgctx}/inspection/made-inspection/save.htm',false); */
			alert("为防止数据丢失，请先暂存表单！");
			autoSave();
		},1000*60*15);
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
	function uncheckNoChange(obj){
		var appearanceInspection=null;
		$("input[name=appearanceInspection]").each(function(index,obj){
			var check=$("#"+obj.id).attr("checked");
			if(check){
				appearanceInspection=$(obj).val();
			}
		});
		if(appearanceInspection=="免检"){
			$("#uncheckNo_span").html("*");	
			$("#uncheckNo").attr("class","{required:true,messages:{required:'必填'}}");
			$("#uncheckNo").attr("disabled","");
		}else{
			$("#uncheckNo_span").html("");
			$("#uncheckNo").val("");
			$("#uncheckNo").attr("class","");
			$("#uncheckNo").attr("disabled","disabled");
			$("label[for=uncheckNo]").html("");
		}
	}
	function jisuanBadRate(){
		var samplingBadCount1 = $("#samplingBadCount1").val();//
		var samplingN1 = $("#samplingN1").val();//
		var samplingBadCount2 = $("#samplingBadCount2").val();//
		var samplingN2 = $("#samplingN2").val();//
		if(isNaN(samplingBadCount1)||isNaN(samplingN1)){
			$("#badRate1").val("");
			return;
		}
		if(parseInt(samplingBadCount1)>parseInt(samplingN1)){			
			$("#badRate1").val(0);
			$("#samplingBadCount1").val("");
			alert("不良数不能大于样本数！");
			return;
		}
		if(isNaN(samplingBadCount2)||isNaN(samplingN2)){
			$("#badRate2").val("");
			return;
		}
		if(parseInt(samplingBadCount2)>parseInt(samplingN2)){			
			$("#badRate2").val(0);
			$("#samplingBadCount2").val("");
			alert("不良数不能大于样本数！");
			return;
		}
		if(samplingBadCount1&&samplingN1){
			var badRate1=samplingBadCount1*100/samplingN1;
			$("#badRate1").val(badRate1.toFixed(2));
		}else{
			$("#badRate1").val(0.00);
		}	
		if(samplingBadCount2&&samplingN2){
			var badRate2=samplingBadCount2*100/samplingN2;
			$("#badRate2").val(badRate2.toFixed(2));
		}else{
			$("#badRate2").val(0.00);
		}
	}
	function jisuanTotalRate(){
		var sizeAmount = $("#sizeAmount").val();//
		var sizeUnAmount = $("#sizeUnAmount").val();//
		var qualifiedAmount = $("#qualifiedAmount").val();//
		var unqualifiedAmount = $("#unqualifiedAmount").val();//
		if(isNaN(sizeAmount)||isNaN(sizeUnAmount)){
			$("#sizeAmountRate").val(100);
			$("#sizeUnAmountRate").val(0);
			return;
		}
		if(parseInt(sizeUnAmount)>parseInt(sizeAmount)){			
			$("#sizeAmountRate").val(100);
			$("#sizeUnAmountRate").val(0);
			$("#sizeUnAmount").val("");
			alert("不良数不能大于抽检数！");
			return;
		}
		if(isNaN(qualifiedAmount)||isNaN(unqualifiedAmount)){
			$("#qualifiedRate").val(100);
			$("#qualifiedUnRate").val(0);
			return;
		}
		if(parseInt(unqualifiedAmount)>parseInt(qualifiedAmount)){			
			$("#qualifiedRate").val(100);
			$("#qualifiedUnRate").val(0);
			$("#unqualifiedAmount").val("");
			alert("不良数不能大于抽检数！");
			return;
		}
		if(sizeAmount&&sizeUnAmount){
			var sizeAmountRate=sizeUnAmount*100/sizeAmount;
			$("#sizeUnAmountRate").val(sizeAmountRate.toFixed(2));
			$("#sizeAmountRate").val(100-sizeAmountRate.toFixed(2));
		}else{
			$("#sizeAmountRate").val(100);
			$("#sizeUnAmountRate").val(0);
		}	
		if(unqualifiedAmount&&qualifiedAmount){
			var qualifiedRate=unqualifiedAmount*100/qualifiedAmount;
			$("#qualifiedUnRate").val(qualifiedRate.toFixed(2));
			$("#qualifiedRate").val(100-qualifiedRate.toFixed(2));
		}else{
			$("#qualifiedUnRate").val(0);
			$("#qualifiedRate").val(100);
		}	
	}
	//上传附件
	function uploadFiles(showId,hiddenId){
		$.upload({
			appendTo : '#opt-content',
			showInputId : showId,
			hiddenInputId : hiddenId
		});
	}
	//加载附件
	function parseDownloadFiles(arrs){
		for(var i=0;i<arrs.length;i++){
			var hiddenInputId = arrs[i].hiddenInputId;
			var showInputId = arrs[i].showInputId;
			var files = $("#"+hiddenInputId).val();
			if(files&&files.length>0){
		 		$.parseDownloadPath({
					showInputId : showInputId,
					hiddenInputId : hiddenInputId
				});
		 		/* $("#" + hiddenInputId + "_uploadBtn").hide(); */
			}
		}
	}
	function checkScrollDiv(){
		var scrollTop = getScrollTop();
		var tableTop = $("#checkItemsParent").position().top + $("#checkItemsParent").height()-18;
		if(tableTop<scrollTop){
			$("#scroll").hide();
		}else{
			$("#scroll").show();
		}
	}
	function initScrollDiv(){
		var width = $(".ui-layout-center").width()-30;
		var offset = $("#checkItemsParent").find("div").width(width).offset();
		var contentWidth =  $("#checkItemsParent").find("table").width();
		$("#scroll").width(width).css("top",getScrollTop() + "px").find("div").width(contentWidth);
	}
	function contentResize(){
		initScrollDiv();
		checkScrollDiv();
	}
	var topMenu ='';
	var processingResult="";
	var $inputs;
	function enterkey(){
		 $inputs=$("input:text,textarea","#inspectionForm");   
		 $inputs.bind("keypress",keypress);
	}
	
	function keypress(e){
		var key = e.which;
		if(key==13){
			e.preventDefault();
			var index=$inputs.index(this); 
			if(index==$inputs.length-1){
				submitForm('${iqcctx}/inspection-report/fpm/save.htm');
				return false;
			}
			 var $input=$inputs.eq(index++);  
			  $input.change();
			  $input=$inputs.eq(index); 
			 if($input.attr("readonly")==true&&$input.attr("name").indexOf("Date")<0){
				 $input=$inputs.eq(++index);
			 }
			 $input.focus();
			 $input.select(); 
			return false;
		} 
	}
	$(document).bind("keydown",function(e){
		var key=e.which;
		if(e.ctrlKey==true&&key==83){//ctrl+s
			e.preventDefault();
			submitForm('${iqcctx}/inspection-report/fpm/save.htm');
		}
		/* else if(key==9){//tab键
			e.preventDefault(); 
			$obj=$("td[id=check-items-td]"); 
			if($obj.length>0){
				var active=this.activeElement;
				if(active){ 
				  var input;
				   $obj.find(":input").each(function(i,obj){  
					if(obj.name==active.name){
					  input=obj;
					  return ;
					} 
				   });  
				   if(input){
					   input=$(input).change().parent(); 
					var index=$obj.index($(input))+1; 
					if(index<$obj.length){ 
						input=$obj.eq(index).find(":input").first();  
					}else{
						input=$obj.first().find(":input").first();
					}
					}else{
					   input=$obj.first().find(":input").first();
					} 
					input.focus();
					input.select();
				}
				//$obj.find(":input:first").focus();
			} 
		} */
		else if(e.ctrlKey==true&&key==61){//ctrl+=
			e.preventDefault();
			 
		}else if(e.ctrlKey==true&&key==173){//ctrl+-
			e.preventDefault();
			 
		}     
	}).ready(
	function(){
		enterkey();
		$("#scroll").bind("scroll",function(){
			$("#checkItemsParent").find("div").scrollLeft($("#scroll").scrollLeft());
		});
		$("#checkItemsParent").find("div").bind("scroll",function(){
			$("#scroll").scrollLeft($("#checkItemsParent").find("div").scrollLeft());
		});
		$("#opt-content").bind("scroll",function(){
			checkScrollDiv();
		});
 		caculateTotalAmount();
		contentResize();		
		if("${inspectionState}" != "<%=IncomingInspectionActionsReport.INPECTION_STATE_DEFAULT%>"&&"${inspectionState}" != "<%=IncomingInspectionActionsReport.INPECTION_STATE_RE_CHECK%>"){
			$("button").attr("disabled","disabled");
			$(":input").attr("disabled","disabled");
			$("#requestCheckNo").attr("readonly","").focus();
			$("a").removeAttr("onClick");
			if("${failflag}"!="failed"){
				$("#message").html("<font color=red>检验状态【${inspectionState}】,不能修改!</font>");
			}else{
				$("#message").html("<font color=red>${failmessage}</font>");
				alert('${inspectionState}');
				alert('${failmessage}');
			}
			
			$(".opt-btn").find("button").attr("disabled","");
			return;
		}
		var samplingPlan=$("#samplingPlan").val();
		if(samplingPlan=="C=0"){
			document.getElementById("samplingLevel").disabled=true;
		}
		var inspector = $("#inspector").val();
		if(!inspector){
			$("#inspector").val("<%=ContextUtils.getUserName()%>");
			$("#inspectorLoginName").val("<%=ContextUtils.getLoginName()%>");
		}
		$('#enterDate').datepicker({changeYear:'true',changeMonth:'true'});
		$('#planIncomingDate').datepicker({changeYear:'true',changeMonth:'true'});
		$('#inspectionDate').datetimepicker({changeYear:'true',changeMonth:'true'});
		bindCustomEvent();
		$.parseDownloadPath({
			showInputId : 'showAttachmentFiles',
			hiddenInputId : 'attachmentFiles'
		});
		setTimeout(function(){
			$("#message").html("");
		},3000);
	});
	function choiceWaitCheckItem(){
		var appearanceInspection=$("input[name='appearanceInspection']:checked").val();
		if(appearanceInspection=="免检"){
			alert("免检物料不能领取检验项目！");
			return;
		}
		var stockAmountobj = $("#stockAmount").val();
		var inspectionDate = $("#inspectionDate").val();
		var businessUnitCode=null;
		var checkBomMaterialType=$("#checkBomMaterialType").val();
		businessUnitCode = $(":input[name=businessUnitCode]").val();
		var checkBomCode = $("#checkBomCode").val();
		var supplierCode = $("#supplierCode").val();
		if(checkBomCode==''||inspectionDate==''||stockAmountobj==''){
			$("#message").html("<font color=red>物料编码、检验日期、进料数量不能为空！</font>");
			setTimeout(function(){
				$("#message").html("");
			},3000);
			return;
		}
		var url="id=${id}&businessUnitCode="+businessUnitCode+"&checkBomCode="+checkBomCode+"&inspectionDate="+inspectionDate+"&supplierCode="+supplierCode+"&stockAmount="+stockAmountobj+"&checkBomMaterialType="+checkBomMaterialType;
		$.colorbox({href:"${iqcctx}/inspection-report/fpm/wait-checked-items.htm?"+url,
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择检验项目"
 		});
	}
	function exceptionInput(){
		var exceptionId=$("#exceptionId").val();
		var exceptionNo=$("#exceptionNo").val();
		if(exceptionId&&exceptionId!=""){
			if(exceptionNo&&exceptionNo.indexOf("EC")>-1){
				window.open('${supplierctx}/improve-new/exception-contact/input.htm?id='+exceptionId);
			}else{
				window.open('${supplierctx}/improve/input.htm?id='+exceptionId);
			};			
		};		
	}
	function hisReportInput(){
		var hisReportId=$("#hisReportId").val();
		if(hisReportId&&hisReportId!=""){
			window.open('${epmctx}/entrust-hsf/input.htm?id='+hisReportId);
		};		
	}
	function ordReportInput(){
		var ordReportId=$("#ordReportId").val();
		if(ordReportId&&ordReportId!=""){
			window.open('${epmctx}/entrust-ort/input.htm?id='+ordReportId);
		};		
	}	
	function getTempItem(){
		var itemName="";
		//根据是否有检验员，来判定该项目是否有提交过
		$(":input[fieldName=inspectionMan]").each(function(index,obj){
			var objval=$(obj).val();
			if(objval==""){
				$(obj).prev().val("未领取");
				$("td[tdShow=false]").hide();
			}else{
				itemName+=$(obj).attr("inspectionman")+",";
			}
		});
		return itemName;
	}
	function saveBadDescrible(){
		var wgBad="OK",gnBad="OK",ccBad="OK";
		$("#checkItemsParent").find("tr[name='checkItemCC']").each(function(index,obj){
			var conclusion=$(obj).find(":input[fieldName='conclusion']").val();
			if(conclusion=="NG"){
				var parent=$(obj).find(":input[fieldName='conclusion']").attr("hisParent");
				var itemName=$(obj).find(":input[fieldName='conclusion']").attr("itemName");
				var results=$(obj).find(":input[fieldName='results']").val();
				var specifications=$(obj).find(":input[fieldName='specifications']").val();
					var badValues="";
					$(obj).find(":input[isData='true']").each(function(index,o){
						var color=$(o).attr("color");
						if(color=="red"){
							if(badValues==""){
							  	badValues=$(o).val();
							}else{
								badValues=badValues+"、"+$(o).val();
							}
						}
					});					
					if(ccBad=="OK"){
						ccBad=itemName+":不合格，规格："+specifications+"不良数据："+badValues;
					}else{
						ccBad=ccBad+";"+itemName+":不合格，规格："+specifications+",不良数据："+badValues;;
					}
				}
		});
		$("#checkItemsParent").find("tr[name='checkItemTX']").each(function(index,obj){
			var conclusion=$(obj).find(":input[fieldName='conclusion']").val();
			if(conclusion=="NG"){
				var itemName=$(obj).find(":input[fieldName='conclusion']").attr("itemName");
				var results=$(obj).find(":input[fieldName='results']").val();
				var str=itemName+":"+results;
					if(wgBad=="OK"){
						wgBad=str;
					}else{
						wgBad=wgBad+";"+str;
					}
					//$(obj).find(":input[fieldName='conclusion']").val("NG");
			}
			//$(obj).find(":input[fieldName='conclusion']").val("OK");
		});		
		$("#wgBadDescrible").val(wgBad);
		$("#ccBadDescrible").val(ccBad);
	}	
	function submitForm(url){
		 if($("#inspectionForm").valid()){
			 saveBadDescrible();
			$('#inspectionForm').attr('action',url);
			$("#message").html("<b>数据保存中,请稍候... ...</b>");
			$(".opt-btn .btn").attr("disabled",true);
			$('#inspectionForm').submit();
		}else{
			var error = $("#inspectionForm").validate().errorList[0];
			$(error.element).focus();
		}
	}
	function saveFormTemp(url){
		$("#inspectionForm").attr("action",url);
		saveBadDescrible();
		if($("#inspectionForm").valid()){
			$(".opt-btn .btn").attr("disabled",true);
			$("#message").html("<b>数据保存中,请稍候... ...</b>");
			$('#inspectionForm').submit();
		}else{
			var error = $("#inspectionForm").validate().errorList[0];
			$(error.element).focus();
		}		
	}
	function showCheckedBox(){
		var $packingFirStr=$(":input[fileName=packingFir]");
		if('${packingFir}'!=null||'${packingFir}'!=''){
			$($packingFirStr).each(function(index,obj){
				if('${packingFir}'.indexOf($(obj).val())>=0){
					$(obj).attr("checked","checked");
				}			
			});
		}
		var $packingSecStr=$(":input[fileName=packingSec]");
		if('${packingSec}'!=null||'${packingSec}'!=''){
			$($packingSecStr).each(function(index,obj){
				if('${packingSec}'.indexOf($(obj).val())>=0){
					$(obj).attr("checked","checked");
				}			
			});
		}
	}
	function getCheckedBoxVal(){
		var $packingFirStr=$(":input[fileName=packingFir]:checked");
		$("#packingFir").val("");
		$("#packingSec").val("");
		var packingFir=",";
		$($packingFirStr).each(function(index,obj){
			packingFir+=$(obj).val()+",";
		});
		$("#packingFir").val(packingFir.substring(1, packingFir.length-1));
		
		var $packingSecStr=$(":input[fileName=packingSec]:checked");
		var packingSec=",";
		$($packingSecStr).each(function(index,obj){
			packingSec+=$(obj).val()+",";
		});
		$("#packingSec").val(packingSec.substring(1, packingSec.length-1));
	}
	function setBomValue(code,name,model){
		var checkBomName=$("#checkBomName");
		var checkBomCode=$("#checkBomCode");
		//var checkBomMaterialType=$("#modelSpecification"); 
		checkBomName.val(name);
		checkBomName.closest("td").find("span").html(name);
		checkBomCode.val(code);
		//checkBomMaterialType.val(model);
		//checkBomMaterialType.closest("td").find("span").html(model);
	}
	function checkBomClick(obj){
		var supplierCode=$("#supplierCode").val(); 
		if(supplierCode==''){
			alert('请选择供应商');
			return ;
		}
		$.colorbox({href:"${mfgctx}/common/product-bom-select.htm?supplierCode="+supplierCode,
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择物料"
 		});
 	}
	function setFullBomValue(datas){
		var his = $("#checkBomCode").val();
		setBomValue(datas[0].code, datas[0].name, datas[0].model);
		if(datas[0].code != his){
			loadCheckItems();
		}
 	}
	var supplierId="";
	function supplierClick(obj){
		supplierId=obj.id;
		$.colorbox({href:"${supplierctx}/archives/select-supplier.htm?state=合格",iframe:true,
			width:$(window).width()<1000?$(window).width()-100:1000,
			height:$(window).height()<600?$(window).height()-100:600,
			overlayClose:false,
			title:"选择供应商"
		});
	}
	
	function setSupplierValue(objs){
		var obj = objs[0];
		if(supplierId=="supplierTrue"){
			$("#supplierNameTrue").val(obj.name);
			$("#supplierCodeTrue").val(obj.code);
		}else{
			
			$("#supplierName").val(obj.name);
			$("#supplierName").closest("td").find("span").html(obj.name);
			$("#supplierCode").val(obj.code);
			$("#supplierId").val(obj.id);
		}

	} 

	
	function setSupplierProduct(result){
		var checkBomCode = $("#checkBomCode").val();
		if(result.length<=0){
			alert("对不起，该供方没有准入物料！");
			return false;
		}else{
			var isSelected = false;
			for(var i=0;i<result.length;i++){
				if(checkBomCode!=""&&checkBomCode!=null&&checkBomCode==result[i].code){
					$("#selectBomCode").append("<option value="+result[i].code+" selected='selected'>"+result[i].code+"</option>");
					isSelected = true;
				}else{
					$("#selectBomCode").append("<option value="+result[i].code+">"+result[i].code+"</option>");
				}
			}
			if(!isSelected){
				$("#checkBomName").val(result[0].name);
				$("#checkBomCode").val(result[0].code);
				$("#checkBomMaterialType").val(result[0].materialType);
			}			
		}		
	}
	function getBomNameByCode(){
		var code = $("#selectBomCode").val();
		$("#checkBomCode").val(code);
		$.post("${mfgctx}/common/search-product-boms.htm",{searchParams:'{"code":"'+code+'"}',label:'code'},function(result){
			autoSetBomNam(result);
		},'json');
	}
	function autoSetBomNam(result){
		$("#checkBomName").val(result[0].value);
		$("#checkBomMaterialType").val(result[0].materialType);
	}
	
	function callback(){
		showMsg();
	}
	
	 //选择检验人员
 	function selectObj(title,hiddenInputId,showInputId,treeValue){
 		var data = {
 				treeNodeData: "loginName,name",
 				chkboxType:"{'Y' : 'ps', 'N' : 'ps' }",
 				departmentShow:''
 			};
 		var zTreeSetting={
 				leaf: {
 					enable: false,
 					multiLeafJson:  "[{'name':'用户树','type':'MAN_DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"loginName\":\"loginName\",\"code\":\"code\",\"cardNumber\":\"cardNumber\"}','showValue':'{\"name\":\"name\"}'}]"
 				},
 				type: {
 					treeType: "MAN_DEPARTMENT_TREE",
 					showContent:"[{'id':'id','code':'code','name':'name'}]",
 					noDeparmentUser:false,
 					onlineVisible:true
 				},
 				data: data,
 				view: {
 					title: "用户树",
 					width: 300,
 					height:400,
 					url:webRoot
 				},
 				feedback:{
 					enable: true,
 			                showInput:"showInput", 
 			                showThing:"{'name':'name'}",
 			                hiddenInput:"hiddenInput",
 			                hiddenThing:"{'loginName':'loginName'}",
 			                append:false
 				},
 				callback: {
 					onClose:function(api){
 							$("#"+hiddenInputId).val(ztree.getLoginName());
 							$("#"+showInputId).val(ztree.getName());							
 					}
 				}	
			};
		    popZtree(zTreeSetting);
	}
	var isLoadItems = false;
	function loadCheckItems(){
		if(isLoadItems){
			return;
		}
		var stockAmountobj = $("#stockAmount");
		var stockAmount1=stockAmountobj.val();
		 var hisNum=stockAmountobj.attr("hisNum");
		 var stockAmount=0;
		 try{
				stockAmount=parseFloat(stockAmount1);
				if(isNaN(stockAmount)){
					stockAmount="";
		        }
				if(stockAmount!=stockAmount1){
					stockAmountobj.val(stockAmount);
				}
			}catch(err){ 
				stockAmountobj.val("");
			} 
		 if(stockAmount<=0){
			 stockAmountobj.val(hisNum);
			 return ; 
		 } 
		 
		 if(stockAmount>50000000){
			 alert("来料数不能大于50000000");
			 return ;
		 }
		var inspectionDate = $("#inspectionDate").val();
		var businessUnitCode=null;
		var checkBomMaterialType=$("#checkBomMaterialType").val();
		businessUnitCode = $(":input[name=businessUnitCode]").val();
		var checkBomCode = $("#checkBomCode").val();
		var supplierCode = $("#supplierCode").val();
		if(!businessUnitCode||!checkBomCode||!stockAmount||!inspectionDate){ 
			return ;
		}else{
			var params = {
				businessUnitCode : businessUnitCode,
				supplierCode : supplierCode,
				checkBomCode : checkBomCode,
				stockAmount : stockAmount,
				checkBomMaterialType:checkBomMaterialType,
				inspectionDate : inspectionDate,
				flagIds : $(":input[name=flagIds]").val()
			};
			$("#checkItemsParent").find(":input[fieldName=countType]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("#checkItemsParent").find(":input[fieldName=checkItemName]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("#checkItemsParent").find(":input[fieldName=results]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("#checkItemsParent").find(":input[results=true]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("#checkItemsParent").find(":input[fieldName=qualifiedAmount]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("#checkItemsParent").find(":input[fieldName=unqualifiedAmount]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("#scroll").hide();
			$("#checkItemsParent").find("div").html("<div style='padding:4px;'><b>检验项目加载中,请稍候... ...</b></div>");
			var url = "${iqcctx}/inspection-report/fpm/check-items.htm";
			isLoadItems = true;
			$("#checkItemsParent").find("div"). load(url,params,function(){
				isLoadItems = false;
// 				$("#inspectionForm").validate({});
				bindCustomEvent();
				//更新不合格数和合格数
				$(".checkItemsClass").each(function(index,obj){
					caculateUqualifiedAmount(obj);
				});
				var contentWidth =  $("#checkItemsParent").find("table").width();
				$("#scroll").find("div").width(contentWidth);
				checkScrollDiv();  
				enterkey();
			});
			stockAmountobj.attr("hisNum",stockAmount);
// 			computeMinQualified();	
			updateSamplingPlan(checkBomCode);
			}	
	}
	 function updateSamplingPlan(checkBomCode){
			var url = "${iqcctx}/inspection-report/fpm/update-sampling-plan.htm?checkBomCode="+checkBomCode;
			$.post(encodeURI(url),{},function(result){
	 			if(result.error){
	 				alert(result.message);
	 			}else{
	 				var baseType = result.baseType;
	 				$("#samplingPlan").val(baseType);
	 				var specification = result.specification;
	 				$("#inspectionBasis").val(specification);
	 				$("span[id=samplingPlanSpan]").html(baseType);
					var places = result.samplingStandards;
					var placeArr = places.split(",");
					var place = document.getElementById("samplingLevel");
					place.options.length=0;
					var opp1 = new Option("","");
					place.add(opp1);
	 				for(var i=0;i<placeArr.length;i++){
	 					var opp = new Option(placeArr[i],placeArr[i]);
	 					place.add(opp);
	 				}	 				
	 				if(places.length==0||places==""){
	 					document.getElementById("samplingLevel").disabled=true;
	 				}
	 			}
	 		},'json');
	 }
	function addResultInput(obj,checkItemName){
		var tdLen = $(obj).parent().find("input").length;
		if(tdLen==80){
			alert("最多能添加80项！");
		}else{
			var countTypeName = $(obj).closest("tr").find(":input[fieldName=countType]").attr("name");
			var flagIndex = countTypeName.split("_")[0];
			var html = "<input results=\"true\" color=\"black\" style=\"width:33px;float:left;margin-left:2px;\" title=\""+checkItemName+"样品"+(tdLen+1)+"\" fieldName=\"result"+(tdLen+1)+"\" name=\""+flagIndex+"_result"+(tdLen+1)+"\"  class=\"{number:true}\"></input>";
			if(tdLen%10==0){
				html+="<br/>";
			}
			$(obj).before(html); 
			//更新顶部的宽度
			updateCheckItemsHeaderWidth();
			$lastinput=$(obj).parent().find("input").last();
			$lastinput.bind("keypress",keypress)
			.bind("change",resultChange);
			$inputs=$("input:text,textarea","#inspectionForm");
		}
		
	}
	function removeResultInput(obj){ 
		$children=$(obj).parent().find("input"); 
		if($children.length==1){
			return ;
		}
		$children.last().remove(); 
		$inputs=$("input:text,textarea","#inspectionForm");
		//更新顶部的宽度
		updateCheckItemsHeaderWidth();
		//初始化滚动条
		initScrollDiv();
		
	}
	//更新顶部的宽度
	function updateCheckItemsHeaderWidth(){
		var maxLen = 10;
// 		$(".checkItemsClass").each(function(index,obj){
// 			var len = $(obj).find("input").length;
// 			if(len>maxLen){
// 				maxLen = len;
// 			}
// 		});
		//$("#checkItemsHeader").width(maxLen*38+70);
		//初始化滚动条
		initScrollDiv();
	}
	

	function addBadNum(obj){
		var qualifiedAmount=$(obj).closest("tr").find(":input[fieldName=qualifiedAmount]");
		var value=qualifiedAmount.val();
		if(isNaN(value)){
			value = 0;
		}else{
			value = parseFloat(value);
		}
		if(value<1){
			return ;
		}  
		value--;
		qualifiedAmount.val(value); 
		qualifiedAmount.change();
	}
	
	function removeBadNum(obj){
		var unqualifiedAmount=$(obj).closest("tr").find(":input[fieldName=unqualifiedAmount]");
		var value=unqualifiedAmount.val();
		if(isNaN(value)){
			value = 0;
		}else{
			value = parseFloat(value);
		}
		if(value<1){
			return ;
		}  
		var qualifiedAmount=$(obj).closest("tr").find(":input[fieldName=qualifiedAmount]");
		var hisVal = qualifiedAmount.val();
		qualifiedAmount.val(parseFloat(hisVal)+1); 
		qualifiedAmount.change();
	}
	
	//添加各种事件
	function bindCustomEvent(){
		//不合格数
		$("#checkItemsParent").find(":input[fieldName=unqualifiedAmount]").bind("change",function(){
			unqualifiedAmountChange(this);
		});
		$("#packingResult").bind("change",function(){
			caculateTotalAmount();
		})
		//合格数
		$("#checkItemsParent").find(":input[fieldName=qualifiedAmount]").bind("change",function(){
			var value=parseInt(this.value); 
			var hisamount=$(this).attr("hisamount");
			if(value>hisamount){
				value=hisamount;
			}
			this.value=value;
			 var unqualifiedAmount=$(this).parent().next().find(":input[fieldName=unqualifiedAmount]");
			 var value=hisamount-value; 
			 unqualifiedAmount.val(value);
			 unqualifiedAmount.change();
			//this.parent().next();
			if(!isNaN(this.value)&&this.value.indexOf(".")==-1){
				caculateTotalAmount();
			}
		});
		//计量的值不符合范围时的事件
		$(".checkItemsClass input").bind("change",resultChange);
	}
	function unqualifiedAmountChange(obj){
		if(!isNaN(obj.value)&&obj.value.indexOf(".")==-1){
			var parentTr = $(obj).parent().parent();
			var re = parentTr.find(":input[fieldName=aqlRe]").val();
			if(isNaN(re)){
				parentTr.find("span[name=conclusionSpan]").html("合格");
				parentTr.find(":input[fieldName=conclusion]").val("OK");
			}else{
				 if(parseInt(obj.value)<parseInt(re)){
					parentTr.find("span[name=conclusionSpan]").html("合格");
					parentTr.find(":input[fieldName=conclusion]").val("OK");
				}else{
					parentTr.find("span[name=conclusionSpan]").html("<font color='red'>不合格</font>");
					parentTr.find(":input[fieldName=conclusion]").val("NG");
				} 
				/* if(parseInt(obj.value)>0){
					parentTr.find("span[name=conclusionSpan]").html("<font color='red'>不合格</font>");
					parentTr.find(":input[fieldName=conclusion]").val("NG");
				}else{
					parentTr.find("span[name=conclusionSpan]").html("合格");
					parentTr.find(":input[fieldName=conclusion]").val("OK");
				} */
			}
			caculateTotalAmount();
		}
	}
	function resultChange(){
		if(!isNaN(this.value)){
			var parentTr = $(this).parent().parent();
			var maxlimit = parentTr.find(":input[fieldName=maxlimit]").val(),minlimit = parentTr.find(":input[fieldName=minlimit]").val();
			if(maxlimit&&!isNaN(maxlimit)||minlimit&&!isNaN(minlimit)){
				if(!this.value){
					$(this).css("color","black").attr("color","black");
				}else{
					var val =parseFloat(this.value);
					if(!maxlimit||isNaN(maxlimit)){//只有下限
						if(val>=parseFloat(minlimit)){
							$(this).css("color","black").attr("color","black");
						}else{
							$(this).css("color","red").attr("color","red");
						}
					}else if(!minlimit||isNaN(minlimit)){//只有上限
						if(val<=parseFloat(maxlimit)){
							$(this).css("color","black").attr("color","black");
						}else{
							$(this).css("color","red").attr("color","red");
						}
					}else{//有上限和下限parseFloat(minlimit)parseFloat(maxlimit)
						if(val>=parseFloat(minlimit)&&val<=parseFloat(maxlimit)){
							$(this).css("color","black").attr("color","black");
						}else{
							$(this).css("color","red").attr("color","red");
						}
					}
				}
				$(this).parent().each(function(index,obj){
					caculateUqualifiedAmount(obj);
				});				
			}
		}else{
			$(this).val("");
			$(this).focus();
			alert("必须填数字！");
		}
	}
	function standardValueChange(obj){
		var value=obj.value;
		if(isNaN(value)){
			$(obj).val("");
			$(obj).focus();
			alert("必须填数字！");
		}
	}
	//最低合格率，合格数，不合格数
	var qualifiedRateMin=0;
	//计算总的检验数量
function caculateTotalRate(){
		//计算外观合格数
		var wgInspectionAmount = 0, functionInspectionAmount = 0,sizeInspectionAmount = 0,speInspectionAmount = 0;
		$("#checkItemsParent").find(":input[hisParent]").each(function(index,obj){
			var parent=$(obj).attr("hisParent");
			if(parent=="外观"||parent=="外观检验"){
				if(obj.getAttribute("fieldName")=="inspectionAmount"){
					if(obj.value>wgInspectionAmount){
						wgInspectionAmount = obj.value;
					}
				}
			}
			if(parent=="尺寸"||parent=="尺寸量测"||parent=="尺寸检验"){
				if(obj.getAttribute("fieldName")=="inspectionAmount"){
					if(obj.value>sizeInspectionAmount){
						sizeInspectionAmount = obj.value;
					}
				}
			}
			if(parent=="特性"||parent=="特性检验"){	
				if(obj.getAttribute("fieldName")=="inspectionAmount"){
					if(obj.value>speInspectionAmount){
						speInspectionAmount = obj.value;
					}
				}
			}
			if(parent=="功能"){
				if(obj.getAttribute("fieldName")=="inspectionAmount"){
					if(obj.value>functionInspectionAmount){
						functionInspectionAmount = obj.value;
					}
				}
			}
		});
		$("#appearanceAmount").val(wgInspectionAmount);
		$("#functionAmount").val(functionInspectionAmount);
		$("#sizeAmount").val(sizeInspectionAmount);
		$("#qualifiedAmount").val(speInspectionAmount);
		var wgQualifiedAmount = 0,wgUnqualifiedAmount = 0;
		var functionQualifiedAmount = 0,functionUnqualifiedAmount = 0,functionQualifiedHeJi=0;
		var sizeQualifiedAmount = 0,sizeUnqualifiedAmount = 0,sizeQualifiedHeJi = 0;
		var speQualifiedAmount = 0,speUnqualifiedAmount = 0,speQualifiedHeJi = 0;
		$("#checkItemsParent").find(":input[hisParent]").each(function(index,obj){
			var parent=$(obj).attr("hisParent");
			if(parent=="外观"||parent=="外观检验"){
				if(obj.getAttribute("fieldName")=="qualifiedAmount"){
					wgQualifiedAmount = parseInt(wgQualifiedAmount)+parseInt(obj.value);
					if(wgQualifiedAmount>wgInspectionAmount){
						wgQualifiedAmount = wgInspectionAmount;
					}
				}
				if(obj.getAttribute("fieldName")=="unqualifiedAmount"){
					wgUnqualifiedAmount = parseInt(wgUnqualifiedAmount)+parseInt(obj.value);
					if(wgUnqualifiedAmount>wgInspectionAmount){
						wgUnqualifiedAmount = wgInspectionAmount;
					}
				}
			}
			if(parent=="尺寸"||parent=="尺寸量测"){
				if(obj.getAttribute("fieldName")=="qualifiedAmount"){
					sizeQualifiedAmount = parseInt(sizeQualifiedAmount)+parseInt(obj.value);
					if(sizeQualifiedAmount>sizeInspectionAmount){
						sizeQualifiedAmount = sizeInspectionAmount;
					}
				}
				if(obj.getAttribute("fieldName")=="unqualifiedAmount"){
					sizeUnqualifiedAmount = parseInt(sizeUnqualifiedAmount)+parseInt(obj.value);
					if(sizeUnqualifiedAmount>sizeInspectionAmount){
						sizeUnqualifiedAmount = sizeInspectionAmount;
					}
					if(parseInt(obj.value)>0){
						var qualifiedAmount=$(obj).parent().parent().find("input[fieldName='inspectionAmount']").val();
						sizeQualifiedHeJi=parseInt(sizeQualifiedHeJi)+parseInt(qualifiedAmount);
					}
				}
			}
			if(parent=="特性"||parent=="特性检验"){	
				if(obj.getAttribute("fieldName")=="qualifiedAmount"){
					speQualifiedAmount = parseInt(speQualifiedAmount)+parseInt(obj.value);
					if(speQualifiedAmount>speInspectionAmount){
						speQualifiedAmount = speInspectionAmount;					
					}
				}
				if(obj.getAttribute("fieldName")=="unqualifiedAmount"){
					speUnqualifiedAmount = parseInt(speUnqualifiedAmount)+parseInt(obj.value);
					if(speUnqualifiedAmount>speInspectionAmount){
						speUnqualifiedAmount = speInspectionAmount;
					}
					if(parseInt(obj.value)>0){
						var qualifiedAmount=$(obj).parent().parent().find("input[fieldName='inspectionAmount']").val();
						speQualifiedHeJi=parseInt(speQualifiedHeJi)+parseInt(qualifiedAmount);
					}
				}
			}
			if(parent=="功能"){
				if(obj.getAttribute("fieldName")=="qualifiedAmount"){
					functionQualifiedAmount = parseInt(functionQualifiedAmount)+parseInt(obj.value);
					if(functionQualifiedAmount>functionInspectionAmount){
						functionQualifiedAmount = functionInspectionAmount;
					}
				}
				if(obj.getAttribute("fieldName")=="unqualifiedAmount"){
					functionUnqualifiedAmount = parseInt(functionUnqualifiedAmount)+parseInt(obj.value);
					if(functionUnqualifiedAmount>functionInspectionAmount){
						functionUnqualifiedAmount = functionInspectionAmount;
					}
					if(parseInt(obj.value)>0){
						var qualifiedAmount=$(obj).parent().parent().find("input[fieldName='inspectionAmount']").val();
						functionQualifiedHeJi=parseInt(functionQualifiedHeJi)+parseInt(qualifiedAmount);
					}
				}
			}
		});
		//外观
		var wgqua = parseInt(wgInspectionAmount)-parseInt(wgUnqualifiedAmount);
		if(wgqua<0){
			wgqua=0;
		}
		$("#appearanceAmount").val(wgqua);
		if(wgUnqualifiedAmount>wgInspectionAmount){
			wgUnqualifiedAmount = wgInspectionAmount;
		}
		$("#appearanceUnAmount").val(wgUnqualifiedAmount);
		var wgHege = $("#appearanceAmount").val();
		if(wgHege==0){
			$("#appearanceAmountRate").val(0);
		}else{
			var wgrate = wgHege*100/wgInspectionAmount;
			$("#appearanceAmountRate").val(wgrate.toFixed(2));
		} 
		$("#checkAmount").val(wgInspectionAmount);
		//功能
		if(parseInt(functionQualifiedHeJi)<parseInt(functionInspectionAmount)){
			functionQualifiedHeJi=functionInspectionAmount;
		}
		var functionqua = parseInt(functionInspectionAmount)-parseInt(functionUnqualifiedAmount);
		if(functionqua<0){
			functionqua=0;
		}
		$("#functionAmount").val(functionqua);
		if(functionUnqualifiedAmount>functionInspectionAmount){
			functionUnqualifiedAmount = functionInspectionAmount;
		}
		$("#functionUnAmount").val(functionUnqualifiedAmount);
		var functionHege = $("#functionAmount").val();
		if(functionHege==0){
			$("#functionAmountRate").val(0);
		}else{
			var functionrate = functionHege*100/functionQualifiedHeJi;
			$("#functionAmountRate").val(functionrate.toFixed(2));
		}
		//尺寸
		if(parseInt(sizeQualifiedHeJi)<parseInt(sizeInspectionAmount)){
			sizeQualifiedHeJi=sizeInspectionAmount;
		}
		var sizequa = parseInt(sizeQualifiedHeJi)-parseInt(sizeUnqualifiedAmount);
		if(sizequa<0){
			sizequa=0;
		}
		$("#sizeAmount").val(sizequa);
		if(sizeUnqualifiedAmount>sizeInspectionAmount){
			sizeUnqualifiedAmount = sizeInspectionAmount;
		}
		/* $("#sizeUnAmount").val(sizeUnqualifiedAmount);
		var sizeHege = $("#sizeAmount").val();
		if(sizeHege==0){
			$("#sizeAmountRate").val(0);
		}else{
			var sizerate = sizeHege*100/sizeQualifiedHeJi;
			$("#sizeAmountRate").val(sizerate.toFixed(2));
		} */
		//特性
		if(parseInt(speQualifiedHeJi)<parseInt(speInspectionAmount)){
			speQualifiedHeJi=speInspectionAmount;
		}
		var qua = parseInt(speQualifiedHeJi)-parseInt(speUnqualifiedAmount);
		if(qua<0){
			qua=0;
		}		
		$("#qualifiedAmount").val(qua);
		/* if(speUnqualifiedAmount>speQualifiedHeJi){
			speUnqualifiedAmount = speQualifiedHeJi;
		}
		$("#unqualifiedAmount").val(speUnqualifiedAmount);
		var texingHege = $("#qualifiedAmount").val();
		if(texingHege==0){
			$("#qualifiedRate").val(0);
		}else{
			var rate = texingHege*100/speQualifiedHeJi;
			$("#qualifiedRate").val(rate.toFixed(2));
		} */
	}	
	function caculateTotalAmount(){
		//总的判定
		var conclusionshow = '合格';
		var conclusion='OK';
		if($("#packingResult").val()=="NG"){
			conclusion ='NG';
			conclusionshow='<font color="red">不合格</font>';
		}
		var wgconclusion='OK';
		var wgconclusionshow = '合格';
		$("#checkItemsParent").find("tr[name='checkItemCC']").each(function(index,obj){
			var str=$(obj).find(":input[fieldName='conclusion']").val();
			if(str=="NG"){
				wgconclusion ='NG';
				wgconclusionshow='<font color="red">不合格</font>';				
			}
		});
		$("input[name=wgconclusion]").val(wgconclusion);
		$("input[name=wgconclusion]").parent().find("span").html(wgconclusionshow);
		$("#checkItemsParent").find(":input[fieldName=conclusion]").each(function(index,obj){
			//if(obj.value!='合格'){alert(":"+obj.value);}
			if(obj.value=='NG'){
				conclusion ='NG';
				conclusionshow='<font color="red">不合格</font>';
				return false;
			}
		});
		var determine=$("#determine").val();
		if(determine=="不合格"){
			conclusion ='NG';
			conclusionshow='<font color="red">不合格</font>';
		}
		$("input[name=inspectionConclusion]").val(conclusion);
		$("input[name=inspectionConclusion]").parent().find("span").html(conclusionshow);
		//检验数量
// 		var inspectionAmount = 0;
// 		$("#checkItemsParent").find(":input[fieldName=inspectionAmount]").each(function(index,obj){
// 			if(obj.value){
// 				if(parseInt(obj.value)>inspectionAmount){
// 					inspectionAmount = parseInt(obj.value);
// 				}
// 			}
// 		});
// 		$("#inspectionAmount").val(inspectionAmount);
// 		$("#inspectionAmount").parent().find("span").html(inspectionAmount);
		//不合格数量 
// 		var unqualifiedAmount = 0;
// 		$("#checkItemsParent").find(":input[fieldName=unqualifiedAmount]").each(function(index,obj){
// 			if(obj.value&&!isNaN(obj.value)){
// 				unqualifiedAmount += parseInt(obj.value);
// 			}
// 		});
// 		if(unqualifiedAmount>inspectionAmount){
// 			unqualifiedAmount = inspectionAmount;
// 		}
// 		$("#unqualifiedAmount").val(unqualifiedAmount);
// 		$("#unqualifiedAmount").parent().find("span").html(unqualifiedAmount);
// 		//合格数量 
// 		var qualifiedAmount = inspectionAmount - unqualifiedAmount;
// 		if(qualifiedAmount<0){
// 			qualifiedAmount = 0;
// 		}
// 		$("#qualifiedAmount").val(qualifiedAmount);
// 		$("#qualifiedAmount").parent().find("span").html(qualifiedAmount);

// 		var qualifiedRate = 1;
// 		if(inspectionAmount>0){
// 			qualifiedRate = qualifiedAmount/(inspectionAmount*1.0);
// 		}
// 		$("#qualifiedRate").val(qualifiedRate); 
// 		if(unqualifiedAmount>0){
// 			//processingResult[1].checked=true;
// 			$("#processingResult").val("待审核");
// 		}
// 		try {
// 			$("#qualifiedRate").parent().find("span").html((qualifiedRate*100.0).toFixed(1) + "%");
// 		} catch (e) {
// 			$("#qualifiedRate").parent().find("span").html((qualifiedRate*100.0) + "%");
// 		}
// 		computeMinQualified();
	}
	//计算不合格数量和合格数 
	function caculateUqualifiedAmount(obj){
		var count = 0;
		var min=null,max=null,sum=0,num=0,avg=0;
		$(obj).parent().find("input[results=true]").each(function(index,o){
			if($(o).attr("color")=='red'){
				count++;
			}			
			var value=$(o).val();
			if(num==0&&value){
				min=value;
				max=value;
			}
			if(value&&parseFloat(value)>=max){
				max=value;
			}
			if(value&&parseFloat(value)<=min){
				min=value;
			}
			if(value){
				sum+=parseFloat(value);
				num++;
			}			
		});
		if(num>0){
			avg=sum/num;
		}
		$(obj).parent().find("input[fieldName='minResult']").val(min);
		$(obj).parent().find("input[fieldName='maxResult']").val(max);
		if(avg==0){
			$(obj).parent().find("input[fieldName='avgResult']").val("");
		}else{
			$(obj).parent().find("input[fieldName='avgResult']").val(avg.toFixed(3));
		}		
		var $unqualifiedAmount = $(obj).parent().find(":input[fieldName=unqualifiedAmount]");
		$unqualifiedAmount.val(count);
		$unqualifiedAmount.parent().find("span").html(count);
		
		var qualifiedAmount = 0;
		var inspectionAmount = $(obj).parent().find(":input[fieldName=inspectionAmount]").val();
		if(inspectionAmount&&!isNaN(inspectionAmount)){
			qualifiedAmount = parseInt(inspectionAmount)-count;
			if(qualifiedAmount<1){
				qualifiedAmount=0;
			}
		}
		
		var $qualifiedAmount = $(obj).parent().find(":input[fieldName=qualifiedAmount]");
		$qualifiedAmount.val(qualifiedAmount);
		$qualifiedAmount.parent().find("span").html(qualifiedAmount);
		
		if($unqualifiedAmount.length>0){
			unqualifiedAmountChange($unqualifiedAmount[0]);
		}
		caculateTotalRate();
	}
	//判断是否发起过改进
	function isHasSubmitImprove(){
		var id = $("#id").val();
		var sourceNo = '${inspectionNo}';
		var processingResult = $("#processingResult").val();;
		if(id=="undefined"||id==""){
			alert("请先执行单据保存操作！");
			return false;
		}else if(processingResult=="接收"){
			alert("检验合格，无需发起异常处理！");
			return false;
		}else{
			$.post("${improvectx}/correction-precaution/is-submit-improve.htm",{sourceNo:sourceNo},function(result){
				submitImprove(result);
			},'json');
		}

	}
	function submitImprove(result){
		var id = $("#id").val();
 		var url='${improvectx}/correction-precaution/called-input.htm?iqcInspectionReportId='+id;
 		if(result.error){
 			if(confirm(result.message)){
 				$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
 		 			overlayClose:false,
 		 			title:"异常处理",
 		 			onClosed:function(){
 		 			}
 		 		});
 			}
 		}else{
 			$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
		 			overlayClose:false,
		 			title:"异常处理",
		 			onClosed:function(){
		 			}
		 		});
 		}
 		
 	}
// 	//获取对应项目的最低合格率，不合格数，合格数
	function computeMinQualified(){
		var qualifiedRate=1,qualifiedAmount=0,unqualifiedAmount=0,qualifiedRateTemp=0.0;
		$("tr[name=checkItemTr]").each(function(index,obj){
				var inspectionAmount=$(obj).find(":input[fieldName=inspectionAmount]").val();
				var qualifiedCheckedAmount=$(obj).find(":input[fieldName=qualifiedAmount]").val();
				var unqualifiedCheckedAmount=inspectionAmount-qualifiedCheckedAmount;
				if(inspectionAmount>0){
					qualifiedRateTemp = qualifiedCheckedAmount/(inspectionAmount*1.0);
				}
				if(qualifiedRateTemp<qualifiedRate){
					qualifiedRate=qualifiedRateTemp;
					qualifiedAmount=qualifiedCheckedAmount;
					unqualifiedAmount=unqualifiedCheckedAmount;
				}else if(qualifiedRateTemp==qualifiedRate){
					if(unqualifiedCheckedAmount>unqualifiedAmount){
						unqualifiedAmount=unqualifiedCheckedAmount;
						qualifiedAmount=qualifiedCheckedAmount;
					}
				}
			}
		);
		$("#qualifiedRate").val(qualifiedRate);
		$("#qualifiedAmount").val(qualifiedAmount);
		$("#unqualifiedAmount").val(unqualifiedAmount);
		try {
			$("#qualifiedAmount").parent().find("span").html(qualifiedAmount);
			$("#unqualifiedAmount").parent().find("span").html(unqualifiedAmount);
			$("#qualifiedRate").parent().find("span").html((qualifiedRate*100.0).toFixed(1) + "%");
		} catch (e) {
			$("#qualifiedAmount").parent().find("span").html(qualifiedAmount);
			$("#unqualifiedAmount").parent().find("span").html(unqualifiedAmount);
			$("#qualifiedRate").parent().find("span").html((qualifiedRate*100.0) + "%");
		}
	}
 	//发起不合格品处理流程
	function beginDefectiveGoodsProcessForm(){
 		var inspectionConclusion=$("#inspectionConclusion").val();
 		if(inspectionConclusion=="OK"){
 			alert("检验判定为合格时，不能触发不合格处理流程");
			return ;
 		}
		window.location.href = '${mfgctx}/defective-goods/ledger/input.htm?qualityTestingReportNo=${inspectionNo}&sourceType=iqc';
	}
	
	function goback(){
		window.location="${iqcctx}/inspection-report/fpm/list.htm";
	}
	
	function showImprove(id){
		$.colorbox({href:'${improvectx}/correction-precaution/view-info.htm?id='+id,iframe:true,
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"页面详情"
		});
	}
	function showPicture(name){
		var src="${iqcctx}/inspection-report/show-indicator-picture.htm?mid="+name;
		window.open(src,'','toolbar=no,resizable=yes,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,top=50,left=50,width=950,height=550') ;  
	}
	<jsp:include page="audit-method.jsp" />
	<jsp:include page="last-audit-method.jsp" />
	<jsp:include page="recheck-method.jsp" />
	function quickSearch(){
		$.colorbox({href:"${iqcctx}/inspection-report/select-report.htm",iframe:true,
			width:$(window).width()<1000?$(window).width()-100:1000,
			height:$(window).height()<600?$(window).height()-100:600,
			overlayClose:false,
			title:"选择待检验的报告"
		});
	}
	
	function setReportValue(objs){
		window.location.href = '${iqcctx}/inspection-report/fpm/input.htm?id=' + objs[0].id;
	} 
	function showDefectiveGoodsProcessForm(formNo){
		$.colorbox({href:'${mfgctx}/defective-goods/ledger/view-info.htm?formNo='+formNo,iframe:true,
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"不合格品处理单详情"
		});
	}
	function SaveAsFile(){
		var id = '${id}';
		if(!id){
			alert("请先保存!");
			return;
		}
		window.location.href = '${iqcctx}/inspection-report/fpm/export-report.htm?id=${id}';
	}
	
	function completeItem(datas,parentColspan){
		$("td[tdShow=false]").hide();
		var index1=0,index2=0;
		for(var pro in datas){												
			$("td[itemName="+datas[pro]+"]").show();
			$(":input[itemstatus="+datas[pro]+"]").val("已领取");
			var type=$(":input[itemstatus="+datas[pro]+"]").attr("itemType");
			if(type&&type.indexOf("尺寸")!=-1){
				index1++;
				$("td[name="+datas[pro]+"]").html(index1);
			}else{
				index2++;
				$("td[name="+datas[pro]+"]").html(index2);
			}
		}
		$("td[mark=wgconclusionSpan]").attr("rowspan",index1);
	}
	
	/* function addImprove(){
		var id=$("#id").val();
		if(id==''){
			alert("请先保存");
			return;
		} 
		var isimprove=$("#isImprove").val();		
		if(isimprove=="true"){
			alert("该检验单已经发起过改进！");
		}else{
			window.open('${supplierctx}/improve/input.htm?formId=${id}&&type=');	
		}
	} */
	
	 function addImprove(){
			var id=$("#id").val();
			if(id==''){
				alert("请先保存");
				return;
			} 
			var isimprove=$("#isImprove").val();		
			if(isimprove=="true"){
				alert("该检验单已经发起过改进！");
			}else{
				window.open('${supplierctx}/improve-new/exception-contact/input.htm?formId=${id}&&type=');	
			}
	} 
	
	function ReadExcel() {  
		var filePath=$("#filePath").val(); //要读取的xls
	    var sheet_id=1; //读取第1个表
	    var row_start=1; //从第1行开始读取
	    var tempStr='';
	    try{
	        var oXL = new ActiveXObject("Excel.application"); //创建Excel.Application对象
	    }catch(err){
	        alert(err);
	    }
	    var oWB = oXL.Workbooks.open(filePath);
	    oWB.worksheets(sheet_id).select();
	    var oSheet = oWB.ActiveSheet;
	    var colcount=oXL.Worksheets(sheet_id).UsedRange.Cells.Rows.Count ;
	    for(var i=row_start;i<=colcount;i++){
	       var tdName = $.trim(oSheet.Cells(i,1).value);//获取第一个格子的内容
	       for(var j=1;j>0;j++){
	    	   var tdValue = $.trim(oSheet.Cells(i,j+1).value);
	    	   if(tdValue.length==0||tdValue=="undefine"){
	    		   break;
	    	   }
	    	   $("td[itemname="+tdName+"]").find(":input[fieldname=result"+j+"]").val(parseFloat(tdValue));
	    	   resultChangeSelf($("td[itemname="+tdName+"]").find(":input[fieldname=result"+j+"]"),tdValue);
	    	   unqualifiedAmountChange($("td[itemname="+tdName+"]").find(":input[fieldname=result"+j+"]")[0]);
	       }
// 	        tempStr+=($.trim(oSheet.Cells(i,2).value)+" "+$.trim(oSheet.Cells(i,4).value)+" "+$.trim(oSheet.Cells(i,6).value.toString())+" "+temp_time+"\n");
	        //读取第2、4、6、8列内容
	    }
	    return tempStr; //返回
	    oXL.Quit();
	    CollectGarbage(); 
     }  
	function resultChangeSelf(obj,value){
		if(!isNaN(value)){
			var parentTr = $(obj).parent().parent();
			var maxlimit = parentTr.find("input[fieldName=maxlimit]").val(),minlimit = parentTr.find("input[fieldName=minlimit]").val();
			if(maxlimit&&!isNaN(maxlimit)||minlimit&&!isNaN(minlimit)){
				if(!value){
					$(obj).css("color","black").attr("color","black");
				}else{
					//判断是按照大于还是大于等于的方式来进行判断,onlyGreater=true表示仅按照>或小于的方式来判断,默认按照>=和<=的方式来进行
					var specifications = $(obj).closest("tr").find(":input[fieldName=specifications]").val()+"";
					var onlyGreater = false;
					if(specifications.indexOf(">")>-1||specifications.indexOf("<")>-1){
						onlyGreater = true;
					}
					var val = parseFloat(value);
					if(!maxlimit||isNaN(maxlimit)){//只有下限
						if(onlyGreater&&val>parseFloat(minlimit)){
							$(obj).css("color","black").attr("color","black");
						}else if(!onlyGreater&&val>=parseFloat(minlimit)){
							$(obj).css("color","black").attr("color","black");
						}else{
							$(obj).css("color","red").attr("color","red");
						}
					}else if(!minlimit||isNaN(minlimit)){//只有上限
						if(onlyGreater&&val<parseFloat(maxlimit)){
							$(obj).css("color","black").attr("color","black");
						}else if(!onlyGreater&&val<=parseFloat(maxlimit)){
							$(obj).css("color","black").attr("color","black");
						}else{
							$(obj).css("color","red").attr("color","red");
						}
					}else{//有上限和下限
						if(onlyGreater&&val>parseFloat(minlimit)&&val<parseFloat(maxlimit)){
							$(obj).css("color","black").attr("color","black");
						}else if(!onlyGreater&&val>=parseFloat(minlimit)&&val<=parseFloat(maxlimit)){
							$(obj).css("color","black").attr("color","black");
						}else{
							$(obj).css("color","red").attr("color","red");
						}
					}
				}
				$(obj).parent().each(function(index,obj){
					caculateUqualifiedAmount(obj);
				});
			}
		}else{
				$(obj).val("");
				alert("必须填数字！");
		}
	}
	//导入
	function imports(){
		var url = "${iqcctx}/inspection-report/fpm/imports.htm"
		$.colorbox({href:url,iframe:true, innerWidth:380, innerHeight:200,
			overlayClose:false,
			title:"导入检验数据"
		});
	}
	function setResultValues(results){
		for (var key in results) { 
            alert("results["+key+"]"); 
        } 
	}
	function showIdentifiersDiv(){
		if($("#flag").css("display")=='none'){
			removeSearchBox();
			$("#flag").show();
			var position = $("#_task_button").position();
			$("#flag").css("left",position.left+15);
			$("#flag").css("top",position.top+28);
		}else{
			$("#flag").hide();
		}
	}
	
	var identifiersDiv;
	function hideIdentifiersDiv(){
		identifiersDiv = setTimeout('$("#flag").hide()',300);
	}
	
	function show_moveiIdentifiersDiv(){
		clearTimeout(identifiersDiv);
	}
	function addEntrusts(obj){
		var title = $(obj).attr("title");
		if(title=='HSF'){
			addHsf();
			return;
		}
		if(title='ORT'){
			addOrt();
			return;
		}
	}
	function addHsf(){
		if('${id}'==''){
			alert("请先保存");
			return;
		}
		window.open('${epmctx}/entrust-hsf/input.htm?formId=${id}');
	}
	function addOrt(){
		if('${id}'==''){
			alert("请先保存");
			return;
		}
		window.open('${epmctx}/entrust-ort/input.htm?formId=${id}');
	}
	function countRate(type){
		if("外观"==type){
			var appearanceAmount = $("#appearanceAmount").val();
			var appearanceUnAmount = $("#appearanceUnAmount").val();
			var all = parseInt(appearanceAmount) + parseInt(appearanceUnAmount);
			if(isNaN(all)){
				return;
			}
			var rate = appearanceAmount/all*100;
			$("#appearanceAmountRate").val(rate.toFixed(2));
		}
		if("特性"==type){
			var qualifiedAmount = $("#qualifiedAmount").val();
			var unqualifiedAmount = $("#unqualifiedAmount").val();
			var all = parseInt(qualifiedAmount) + parseInt(unqualifiedAmount);
			if(isNaN(all)){
				return;
			}
			var rate = qualifiedAmount/all*100;
			$("#qualifiedRate").val(rate.toFixed(2));
		}
	}
	function materialType(){
		$.colorbox({href:"${supplierctx}/base-info/material-type-goal/select-material-type-list.htm",iframe:true,
			width:$(window).width()<1000?$(window).width()-100:1000,
			height:$(window).height()<600?$(window).height()-100:600,
			overlayClose:false,
			title:"选择物料类别"
		});
	}
	
	function setMaterialTypeValue(objs){
		var obj = objs[0];
		$("#checkBomMaterialType").val(obj.materialType);
	} 
	//测试验证(自动验证)
	function testValidate(obj){
		var supplierCode=$("#supplierCode").val(); 
		var checkBomCode=$("#checkBomCode").val();
		if(!supplierCode||!checkBomCode){
			return;
		}		
		var params = {
				supplierCode : supplierCode,
				checkBomCode : checkBomCode
			};		
			var url = "${iqcctx}/inspection-base/test-frequency/test-validate.htm";
			$.post(encodeURI(url),params,function(result){
				if(!result.testFrequency){
					alert("该物料测试频率没有维护，请及时处理！");
					return;
				}	
				if(result.ortEmpty){
					if(result.isOrt){
						$("#ortSpan2").html("ORT测试频率没有维护!");
						$("#ortSpan2").attr("style","color:red");
					}					
				}else{
					if(result.ortNeed){
						if(result.ortFirst){
							$("#ortSpan2").html("首批报检，需要送检可靠性试验!");
							$("#ortSpan2").attr("style","color:red");
						}else{
							$("#ortSpan2").html("该物料距离上次送检时间已超期，需要送检可靠性试验!");
							$("#ortSpan2").attr("style","color:red");
						}
					}else{
						$("#ortSpan1").html(result.lastOrtReportNo);
						$("#ordReportId").val(result.lastOrtReportId);
						$("#ordReportNo").val(result.lastOrtReportNo);
					}
				}
				if(result.hsfEmpty){
					if(result.isHsf){
						$("#hsfSpan2").html("HSF测试频率没有维护!");
						$("#hsfSpan2").attr("style","color:red");
					}					
				}else{
					if(result.hsfNeed){
						if(result.hsfFirst){
							$("#hsfSpan2").html("该物料首批报检，需要送检HSF试验!");
							$("#hsfSpan2").attr("style","color:red");
						}else{
							$("#hsfSpan2").html("该物料距离上次送检时间已超期，需要送检HSF试验!");
							$("#hsfSpan2").attr("style","color:red");
						}
					}else{						
							$("#hsfSpan1").html(result.lastHsfReportNo);
							$("#hisReportId").val(result.lastHsfReportId);
							$("#hisReportNo").val(result.lastHsfReportNo);
					}
				}	
				
			},'json');						
 	}		
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
<input type="hidden" id="id" value="${id}"/>
	<script type="text/javascript">
		var secMenu="inspectionReport_fpm";
		var thirdMenu="myInspectionReportInput_fpm";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-inspection-report-fpm-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<table style="width:100%;margin-top:-2px;" cellpadding="0" cellspacing="0">
					<tr>
						<td valign="middle">
							<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_INPUT_ADD">
								<button class='btn' onclick="javascript:window.location='${iqcctx}/inspection-report/fpm/input.htm?changeview=true';" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
							</security:authorize>
							<c:if test="${inspectionState=='待检验'||inspectionState=='重新检验'}">
								<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_INPUT_FPM_SAVE">
									<button class='btn' type="button" onclick="saveFormTemp('${iqcctx}/inspection-report/fpm/save.htm');"><span><span><b class="btn-icons btn-icons-save"></b>暂存</span></span></button>
									<button class='btn' type="button" onclick="submitForm('${iqcctx}/inspection-report/fpm/save.htm?isSubmit=true',true);"><span><span><b class="btn-icons btn-icons-submit"></b>提交</span></span></button>
									<button class='btn' type="button" onclick="loadCheckItems();"><span><span><b class="btn-icons btn-icons-search"></b>同步检验项目</span></span></button>
								</security:authorize>
							</c:if>
							<c:if test="${inspectionState!='待检验'&&inspectionState!='重新检验'}">
								<%-- <security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_INPUT_RECHECK">
									<button class='btn' type="button" onclick="reCheck('${id}');"><span><span><b class="btn-icons btn-icons-cancel"></b>重新检验</span></span></button>			
								</security:authorize> --%>
								<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_INPUT_FPM_RECHECK">
									<button class='btn' type="button" onclick="reCheck({id:'${id}',inspectionState:'${inspectionState}',checkBomName:'${checkBomName}',supplierName:'${supplierName}',stockAmount:'${stockAmount}',inspectionConclusion:'${inspectionConclusion}',appearanceAmount:'${appearanceAmount}',appearanceUnAmount:'${appearanceUnAmount}',appearanceAmountRate:'${appearanceAmountRate}',qualifiedAmount:'${qualifiedAmount}',unqualifiedAmount:'${unqualifiedAmount}',qualifiedRate:'${qualifiedRate}'});"><span><span><b class="btn-icons btn-icons-cancel"></b>重新检验</span></span></button>			
								</security:authorize>
							</c:if>
							<c:if test="${inspectionState=='待审核'&&isAudit==true}">
								<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_LIST_FPM_AUDIT">
									<button class='btn' type="button" onclick="auditIqc({id:'${id}',inspectionState:'${inspectionState}',processSection:'${processSection}',productStage:'${productStage}',checkBomMaterialType:'${checkBomMaterialType}',checkBomName:'${checkBomName}',supplierName:'${supplierName}',stockAmount:'${stockAmount}',inspectionConclusion:'${inspectionConclusion}',appearanceAmount:'${appearanceAmount}',appearanceUnAmount:'${appearanceUnAmount}',appearanceAmountRate:'${appearanceAmountRate}',qualifiedAmount:'${qualifiedAmount}',unqualifiedAmount:'${unqualifiedAmount}',qualifiedRate:'${qualifiedRate}'});"><span><span><b class="btn-icons btn-icons-audit"></b>审核</span></span></button>			
								</security:authorize>
							</c:if>
							<c:if test="${inspectionState=='上级待审核'&&isLastAudit==true}">
								<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_LIST_FPM_AUDIT">
									<button class='btn' type="button" onclick="lastAuditIqc({id:'${id}',inspectionState:'${inspectionState}',checkBomName:'${checkBomName}',supplierName:'${supplierName}',stockAmount:'${stockAmount}',inspectionConclusion:'${inspectionConclusion}',appearanceAmount:'${appearanceAmount}',appearanceUnAmount:'${appearanceUnAmount}',appearanceAmountRate:'${appearanceAmountRate}',qualifiedAmount:'${qualifiedAmount}',unqualifiedAmount:'${unqualifiedAmount}',qualifiedRate:'${qualifiedRate}'});"><span><span><b class="btn-icons btn-icons-audit"></b>审核</span></span></button>			
								</security:authorize>
							</c:if>
							<c:if test="${id>0}">
								<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_INPUT_UN-HANDLING">
									<s:if test="defectiveGoodsProcessingFormNo==null||defectiveGoodsProcessingFormNo==''">
										<button class="btn" onclick="beginDefectiveGoodsProcessForm()"><span><span><b class="btn-icons btn-icons-alarm"></b>不合格品处理</span></span></button>
									</s:if>
									<s:else>
										<button class="btn" onclick="showDefectiveGoodsProcessForm('${defectiveGoodsProcessingFormNo}')"><span><span><b class="btn-icons btn-icons-info"></b>查看不合格品处理单</span></span></button>
									</s:else>
								</security:authorize>
								<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_INPUT_EX-HANDLING">
<!-- 									<button class="btn" onclick="isHasSubmitImprove()"><span><span><b class="btn-icons btn-icons-alarm"></b>异常处理</span></span></button> -->
								</security:authorize>
							</c:if>
							<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_INPUT_PRINT">
								<button class='btn' type="button" onclick="prn1_preview();"><span><span><b class="btn-icons btn-icons-print"></b>打印</span></span></button>	
							</security:authorize>
							<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_EXPORT_REPORT">
								<button class='btn' onclick="SaveAsFile();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
							</security:authorize>
							<c:if test="${correctionPrecautionReportId!=null}">
								<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_INPUT_SHOW-IMPROVE">
<%-- 									<button class="btn" onclick="showImprove('${correctionPrecautionReportId}')"><span><span><b class="btn-icons btn-icons-info"></b>查看异常报告</span></span></button> --%>
								</security:authorize>
							</c:if>
							<security:authorize ifAnyGranted="experimental-delegation-top-input">
								<button class='btn' type="button" id="_task_button" onclick="showIdentifiersDiv();"><span><span><b class="btn-icons btn-icons-add"></b>发起实验委托</span></span></button>	
							</security:authorize>
							<c:if test="${canImprove==true}">
							<security:authorize ifAnyGranted="supplier-improve-input">
								<button class='btn' type="button" onclick="addImprove();"><span><span><b class="btn-icons btn-icons-add"></b>发起改进</span></span></button>	
							</security:authorize>	
							</c:if>		
							<c:if test="${remark!=null}">
								<span style="color:red">${remark}</span>
							</c:if>				
							<span style="margin-left:6px;line-height:30px;" id="message"><s:actionmessage theme="mytheme" cssStyle="color:red;" /></span>
						</td>
						<td style="text-align:right;">
							<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_INPUT_QUICK-SEARCH">
								<button class='btn' type="button" onclick="quickSearch();">
									<span><span><b class="btn-icons btn-icons-search"></b>快速检索</span></span>
								</button>
							</security:authorize>
						</td>
					</tr>
				</table>
			</div>
			<div id="opt-content" style="text-align: center;">
				<div id="scroll" style="position:absolute;top:0px;left:15px;overflow-y:hidden;overflow-x:auto;height:35px;line-height:35px;display:block;z-index:2;">
					<div style="">&nbsp;</div>
				</div>
				<form action="" method="post" id="inspectionForm" name="inspectionForm">
					<input type="hidden" name="id" value="${id}"/>
					<input type="hidden" name="canImprove" value="${canImprove}"/>
					<input type="hidden" name="isAudit" id="isAudit" value="${isAudit}"/>
					<input type="hidden" name="isLastAudit" id="isLastAudit" value="${isLastAudit}"/>
					<input type="hidden" id="packingFir" name="packingFir" value="${packingFir}"/>
					<input type="hidden" id="packingSec" name="packingSec" value="${packingSec}"/>
					<input type="hidden" id="isImprove" name="isImprove" value=""/>
					<!-- 检验工序 -->
					<input type="hidden" name="workingProcedure" value="${workingProcedure}"/>
					<jsp:include page="input-form-fpm.jsp" />					
				</form>
			</div>
			<div id="flag" onmouseover='show_moveiIdentifiersDiv();' onmouseout='hideIdentifiersDiv();'>
				<ul style="width:100px;">
					<li onclick="addEntrusts(this);" style="cursor:pointer;" title="HSF">HSF委托</li>
					<li onclick="addEntrusts(this);" style="cursor:pointer;" title="ORT">可靠性委托</li>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>