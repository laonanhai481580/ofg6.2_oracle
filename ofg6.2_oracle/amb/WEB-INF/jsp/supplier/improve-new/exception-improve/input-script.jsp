<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
		function supplierClick(){
			$.colorbox({href:"${supplierctx}/archives/select-supplier.htm",iframe:true,
				width:$(window).width()<1000?$(window).width()-100:1000,
				height:$(window).height()<600?$(window).height()-100:600,
				overlayClose:false,
				title:"选择供应商"
			});
		}
		function setSupplierValue(objs){
			var obj = objs[0];
			$("#supplierName").val(obj.name);
			$("#supplierCode").val(obj.code);
			$("#supplierEmail").val(obj.supplierEmail);
			//联系人
		} 
	function checkEmail(obj){
		var email = obj.value;
		if(email.indexOf(";")>-1){
			var a=email.split(";");
			for (var i = 0; i < a.length; i++) {
				if(a[i]&&!a[i].match(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/)){
					alert(a[i]+"邮箱格式不正确，请重新输入！");
					return;
				}
			}
		}else{
			if(!email.match(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/)){
				alert(email+"邮箱格式不正确，请重新输入！");
				return;
			}
		}
	}
	function mrbApplyChange(obj){
		var flag=$("#pmcOpinion").attr("disabled");
		if(!flag){
			var mrbApply=null;
			$("input[name=mrbApply]").each(function(index,obj){
				var check=$("#"+obj.id).attr("checked");
				if(check){
					mrbApply=$(obj).val();
				}
			});	
			if(mrbApply=="需要"){
				$("#mrbReportNo_span").html("*");	
				$("#mrbReportNo").attr("class","{required:true,messages:{required:'必填'}}");
				$("#mrbReportNo").attr("disabled","");
				$("#returnReportNo").val();
			}else{
				$("#mrbReportNo_span").html("");
				$("#mrbReportNo").val("");
				$("#mrbReportNo").attr("class","");
				$("#mrbReportNo").attr("disabled","disabled");
				$("label[for=mrbReportNo]").html("");
			}
		}
	}
	function sqeProcessOpinionChange(obj){
		var flag=$("#qualityOpinion").attr("disabled");
		if(!flag){
			var sqeProcessOpinion=null;
			var mrbApply=null;
			$("input[name=mrbApply]").each(function(index,obj){
				var check=$("#"+obj.id).attr("checked");
				if(check){
					mrbApply=$(obj).val();
				}
			});
			if(mrbApply=="需要"){
				sqeProcessOpinion="特采";
				$("#sqeProcessOpinion1").attr("checked","checked");
			}else{
				sqeProcessOpinion="退货";
				$("#sqeProcessOpinion2").attr("checked","checked");
			}
			var mrbReportNo=$("#mrbReportNo").val();
			if(sqeProcessOpinion=="特采"){
				$("#sqeMrbReportNo_span").html("*");	
				$("#sqeMrbReportNo").attr("class","{required:true,messages:{required:'必填'}}");
				$("#sqeMrbReportNo").attr("disabled","");
				$("#returnReportNo_span").html("");
				$("#returnReportNo").val("");
				$("#sqeMrbReportNo").val(mrbReportNo);
				$("#returnReportNo").attr("class","");
				$("#returnReportNo").attr("disabled","disabled");
				$("label[for=returnReportNo]").html("");
			}else if(sqeProcessOpinion=="退货"){
				$("#returnReportNo_span").html("*");	
				$("#returnReportNo").attr("class","{required:true,messages:{required:'必填'}}");
				$("#returnReportNo").attr("disabled","");
				$("#sqeMrbReportNo_span").html("");
				$("#sqeMrbReportNo").val("");
				$("#sqeMrbReportNo").attr("class","");
				$("#sqeMrbReportNo").attr("disabled","disabled");
				$("label[for=sqeMrbReportNo]").html("");
			}else{
				$("#sqeMrbReportNo_span").html("");
				$("#sqeMrbReportNo").val("");
				$("#sqeMrbReportNo").attr("class","");
				$("#sqeMrbReportNo").attr("disabled","disabled");
				$("label[for=sqeMrbReportNo]").html("");
				$("#returnReportNo_span").html("");
				$("#returnReportNo").val("");
				$("#returnReportNo").attr("class","");
				$("#returnReportNo").attr("disabled","disabled");
				$("label[for=returnReportNo]").html("");
			}
		}
	}	
	function clearValue(showInputId, hiddenInputId) {
		$("#" + showInputId).val("");
		$("#" + hiddenInputId).val("");
	}
	function copyValue(obj) {
		$("#unwithholdValue").val($("#" + obj.id).val());
	}
	function changeFlow(obj) {
		alert();
		if (obj.value.length != 0) {
			alert();
		}
	}
	function findKey(obj) {
		var value = obj.value;
		var url = "${supplierctx}/change/select-level.htm?valueStr=" + value;
		$.post(encodeURI(url), {}, function(result) {
			if (result.error) {
				alert(result.message);
			} else {
				$("#changeLevel").val(result.level);
			}
		}, 'json');
	}
	function setAllLogs() {
		var allLogs = "";
		$("#checkerLog input[class=isCheckerLog]").each(function(index, obj) {
			if (allLogs.length == 0) {
				allLogs = obj.value;
			} else {
				allLogs += "," + obj.value;
			}
		});
		$("#checkDeptMansLog").val(allLogs);

	}
	function openInspectionForm(id){
		var companyName=$("#companyName").val();
		var url="";
		if(companyName.indexOf("FPM")>-1){
			url= '${iqcctx}/inspection-report/fpm/input.htm?id=' + id;
		}else{
			url= '${iqcctx}/inspection-report/input.htm?id=' + id;
		}		
		window.open(url);
	}

	function openExceptionContact(id){
		var url= '${supplierctx}/improve-new/exception-contact/input.htm?id=' + id;
		window.open(url);
	}
	
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
			window.open('${supplierctx}/improve/input.htm?formId=${id}&&type=');	
		}
	}	
	
</script>