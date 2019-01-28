<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
		setAgainType();//首次
		setAgainAudit();//二次
	});
	function setAgainType(obj){
		var aa="";
		if(obj){
			aa=obj.value;
		}else{
			$("select:[name='auditorType']").each(function(index, obj) {
					aa=obj.value;
			});
		}
		if(aa=="现场"){
			$("tr[first=true]").show();
			$("input:[id='firstPersonLogin']").each(function(index, obj) {
				$(obj).addClass("{required:true, messages:{required:'必填'}}");
			});
		}else{
			$("tr[first=true]").hide();
			$("input:[id='firstPersonLogin']").each(function(index, obj) {
				$(obj).removeClass();
			});
		}
	}
	function setAgainAudit(obj){
		var aa="";
		if(obj){
			aa=obj.value;
		}else{
			$("input:[name='againAudit']").each(function(index, obj) {
				if(obj.checked){
					aa=obj.value;
				}
			});
		}
		if(aa=="是"){
			$("tr[twice=true]").show();
			$("input:[id='secondPersonLogin']").each(function(index, obj) {
				$(obj).addClass("{required:true, messages:{required:'必填'}}");
			});
		}else{
			$("tr[twice=true]").hide();
			$("input:[id='secondPersonLogin']").each(function(index, obj) {
				$(obj).removeClass();
			});
		}
	}
	function isTransaction(){
		var aa=$("#currentName");
	}
	function selectPerson1(obj, hidden) {
		var data = {
			treeNodeData : "loginName,name",
			chkboxType : "{'Y' : 'ps', 'N' : 'ps' }",
			departmentShow : ''
		};
		var multiple = "true";
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
	function copyPersonLoginName(showId, hidden, show, hiddenNames) {
		var names = $("#" + showId).val();
		var hiddenValue = $("#" + hidden).val();
		if (show != '') {
			var nameArry = names.split(",");
			var newNames = '';
			var hiddenArray = jstree.getLoginNames().split(",");
			var newHidden = '';
			for (var i = 0; i < nameArry.length; i++) {
				if (show.indexOf(nameArry[i]) < 0) {
					if (newNames == '') {
						newNames = nameArry[i];
					} else {
						newNames += "," + nameArry[i];
					}
				}
			}
			for (var i = 0; i < hiddenArray.length; i++) {
				if (hiddenValue.indexOf(hiddenArray[i]) < 0) {
					if (newHidden == '') {
						newHidden = hiddenArray[i];
					} else {
						newHidden += "," + hiddenArray[i];
					}
				}
			}
			$("#" + showId).val(show + "," + newNames);
			$('#' + hidden).attr("value", hiddenValue + "," + newHidden);
		} else {
			$('#' + hidden).attr("value", jstree.getLoginNames());
		}
	}
	function clearValue(showInputId, hiddenInputId) {
		$("#" + showInputId).val("");
		$("#" + hiddenInputId).val("");
	}
	function sendEmail(){
		var params1 = $("#checkPerson").val();
		var params = $("#supplierEmail").val();
		var formNo = $("#formNo").val();
		var str = $("#currentActivityName").val();
		var id = $("#id").val();
		var url = window.location.host+window.location.pathname+"?id="+id;
		if("供应商自评"==str){
			$.post("${supplierctx}/audit/year-inspect/release.htm",{par:params,par1:params1,formNo:formNo,url:url},function(data){
// 				console.log(data);
			},'json');
 		} 
	}
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
		$("#supplierLoginName").val(obj.code);
		$("#supplierEmail").val(obj.supplierEmail);
		//联系人
	} 
	function setAllLogs(){
		var allLogs = "";
		$("#checkerLog1 input[class=isCheckerLog2]").each(function(index,obj){
			if(obj.value!=""){
				if(allLogs.length==0){
					allLogs = obj.value;
				}else{
					allLogs += ","+obj.value;
				}
			}
		});
		if(allLogs.length!=0){
			$("#checkDeptMansLog1").val(allLogs);
		}		
	}
</script>