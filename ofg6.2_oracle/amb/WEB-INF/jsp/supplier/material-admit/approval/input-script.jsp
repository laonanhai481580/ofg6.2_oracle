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
			$("#supplierLoginName").val(obj.code);
			$("#supplierEmail").val(obj.supplierEmail);
			//联系人
		} 

		function clearValue(showInputId,hiddenInputId){
			$("#"+showInputId).val("");
			$("#"+hiddenInputId).val("");
		}
		function setAllLogs(){
			var allLogs = "";
			var allLogs2 = "";
			$("#checkerLog input[class=isCheckerLog]").each(function(index,obj){
				if(obj.value!=""){
					if(allLogs.length==0){
						allLogs = obj.value;
					}else{
						allLogs += ","+obj.value;
					}
				}
			});
			$("#checkDeptMansLog").val(allLogs);
			
			$("#checkerLog input[transact=y]").each(function(index,obj){
				if(obj.value!=""){
					if(allLogs2.length==0){
						allLogs2 = obj.value;
					}else{
						allLogs2 += ","+obj.value;
					}
				}
			});
			$("#checkDeptMansLog2").val(allLogs2);	
			
		}
		
		function showInput(){
			var chk = document.getElementById('p');
			if(chk.checked == true){
				$("#else").show();
			}else{
				$("#else").hide();
			}
		}
		function testSelect(){
			$.colorbox({href:"${mfgctx}/common/product-bom-select.htm",iframe:true,
				width:$(window).width()<1000?$(window).width()-100:1000,
				height:$(window).height()<600?$(window).height()-100:600,
				overlayClose:false,
				title:"选择物料"
			});
		}
		function setFullBomValue(objs){
			var obj = objs[0];
			$("#materialCode").val(obj.materielCode);
			$("#materialName").val(obj.materielName);
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
		function subControl(a){
			modify();
			setAllLogs();
			addBt();
			lcfj(a);
		}
		function modify(){
			var rd=$("#rdStatus").val();
			var pm=$("#pmStatus").val();
			var sqe=$("#sqeStatus").val();
			var qs=$("#qsStatus").val();
			var npi=$("#npiStatus").val();
			var dqe=$("#dqeStatus").val();
			var pro=$("#projectStatus").val();
			if("条件承认"==rd || "拒绝承认"==rd||''==rd){
				$("#rdCheckerLog").attr("transact","y");
				
			}else if("承认"==rd){
				$("input:[belongpart='RD']").each(function(index, obj) {
// 					$(obj).next().attr("disabled",true);
// 					$(obj).next().attr("display","none");
// 					$(obj).next().attr("style","display:none");
				});
			}
			if("条件承认"==pm || "拒绝承认"==pm||''==pm){
				$("#pmCheckerLog").attr("transact","y");
				
			}else if("承认"==pm){
				$("input:[belongpart='PM']").each(function(index, obj) {
// 					$(obj).next().attr("disabled",true);
// 					$(obj).next().attr("display","none");
// 					$(obj).next().attr("style","display:none");
				});
			}
			if("条件承认"==sqe || "拒绝承认"==sqe||''==sqe){
				$("#sqeCheckerLog").attr("transact","y");
			}else if("承认"==sqe){
				$("input:[belongpart='SQE']").each(function(index, obj) {
// 					$(obj).next().attr("disabled",true);
// 					$(obj).next().attr("display","none");
// 					$(obj).next().attr("style","display:none");
				});
			}
			if("条件承认"==qs || "拒绝承认"==qs||''==qs){
				$("#qsCheckerLog").attr("transact","y");
			}else if("承认"==qs){
				$("input:[belongpart='QS']").each(function(index, obj) {
// 					$(obj).next().attr("disabled",true);
// 					$(obj).next().attr("display","none");
// 					$(obj).next().attr("style","display:none");
				});
			}
			if("条件承认"==npi || "拒绝承认"==npi||''==npi){
				$("#npiCheckerLog").attr("transact","y");
			}else if("承认"==npi){
				$("input:[belongpart='NPI']").each(function(index, obj) {
// 					$(obj).next().attr("disabled",true);
// 					$(obj).next().attr("display","none");
// 					$(obj).next().attr("style","display:none");
				});
			}
			if("条件承认"==dqe || "拒绝承认"==dqe||''==dqe){
				$("#dqeCheckerLog").attr("transact","y");
			}else if("承认"==dqe){
				$("input:[belongpart='DQE']").each(function(index, obj) {
// 					$(obj).next().attr("disabled",true);
// 					$(obj).next().attr("display","none");
// 					$(obj).next().attr("style","display:none");
				});
			}
			if("条件承认"==pro || "拒绝承认"==pro||''==pro){
				$("#projectCheckerLog").attr("transact","y");
			}else if("承认"==pro){
				$("input:[belongpart='PRO']").each(function(index, obj) {
// 					$(obj).next().attr("disabled",true);
				});
			}
			
		}
		function amend(obj){
			var id=$(obj).next();
			var b = $(id).val();
			if(b=='NG'){
				$(id).val("OK");
				$(id).prev().val("OK");
				$(obj).parent().addClass("admit");
			}else{
				$(id).val("NG");
				$(id).prev().val("NG");
				$(obj).parent().removeClass();
			}
			return;
		}
		
		function lcfj(a){
			if(a=="流程结束"){
				return;
			}
			var str = $("#currentActivityName").val();
			
			if(str=="供应商上传资料"||str=="供应商修改资料"){
				$("a:[class='small-button-bg']").each(function(index, obj) {
					$(obj).removeAttr('onclick');
				});
				$("input:[stage='one']").each(function(index, obj) {
					$(obj).attr("disabled","disabled");
				});
			}else if(str=='各部门会签'||str=='部门再次会签'){
				var name=$("#transactorName").val();
				var rd=$("#rdCheckerLog").val();
				var pm=$("#pmCheckerLog").val();
				var qs=$("#qsCheckerLog").val();
				var sqe=$("#sqeCheckerLog").val();
				var npi=$("#npiCheckerLog").val();
				var dqe=$("#dqeCheckerLog").val();
				var pro=$("#projectCheckerLog").val();
				$("select:[stage='three']").each(function(index, obj) {
					$(obj).attr("disabled","disabled");
				});
				$("textarea:[stage='three']").each(function(index, obj) {
					$(obj).attr("disabled","disabled");
				});
				if(name==rd){
					$("#rdStatus").removeAttr("disabled");
					$("#countersignRD").removeAttr("disabled");
					$("#rdStatus").addClass("{required:true, messages:{required:'必填'}}");
				}
				if(name==pm){
					$("#pmStatus").removeAttr("disabled");
					$("#countersignPM").removeAttr("disabled");
					$("#pmStatus").addClass("{required:true, messages:{required:'必填'}}");
				}
				if(name==qs){
					$("#qsStatus").removeAttr("disabled");
					$("#countersignQS").removeAttr("disabled");
					$("#qsStatus").addClass("{required:true, messages:{required:'必填'}}");
				}
				if(name==sqe){
					$("#sqeStatus").removeAttr("disabled");
					$("#countersignSQE").removeAttr("disabled");
					$("#sqeStatus").addClass("{required:true, messages:{required:'必填'}}");
				}
				if(name==npi){
					$("#npiStatus").removeAttr("disabled");
					$("#countersignNpi").removeAttr("disabled");
					$("#npiStatus").addClass("{required:true, messages:{required:'必填'}}");
				}
				if(name==dqe){
					$("#dqeStatus").removeAttr("disabled");
					$("#countersignDqe").removeAttr("disabled");
					$("#dqeStatus").addClass("{required:true, messages:{required:'必填'}}");
				}
				if(name==pro){
					$("#projectStatus").removeAttr("disabled");
					$("#countersignProject").removeAttr("disabled");
					$("#projectStatus").addClass("{required:true, messages:{required:'必填'}}");
				}
			}else if(str=='文控'){				
				$("#rdStatus").removeAttr("disabled");
				$("#countersignRD").removeAttr("disabled");
				
				$("#pmStatus").removeAttr("disabled");
				$("#countersignPM").removeAttr("disabled");
				$("#qsStatus").removeAttr("disabled");
				$("#countersignQS").removeAttr("disabled");
				
				$("#sqeStatus").removeAttr("disabled");
				$("#countersignSQE").removeAttr("disabled");
				
				$("#npiStatus").removeAttr("disabled");
				$("#countersignNpi").removeAttr("disabled");
				
				$("#dqeStatus").removeAttr("disabled");
				$("#countersignDqe").removeAttr("disabled");
				
				$("#projectStatus").removeAttr("disabled");
				$("#countersignProject").removeAttr("disabled");			
			}
		}		
		
		function addBt(){
			var str = $("#currentActivityName").val();
			$("input:[stage='BT1']").each(function(index, obj) {
				$(obj).addClass("class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button-text'");
				if(str=='各部门会签'||str=='部门再次会签'||str=='研发核准上传'){
					
				}else{
					$(obj).attr("disabled",true);
					$(obj).attr("display","none");
					$(obj).attr("style","display:none");
				}
			});
			$("input:[stage='BT']").each(function(index, obj) {
				var str = obj.value;
				if(str==''){
					$(obj).val("NG");
				}
				if(str=='OK'){
// 					$(obj).val("OK");
					$(obj).parent().addClass("admit");
// 					$(obj).parent().find("td button").next().attr("disabled",true);
					$(obj).prev().prev().prev().attr("disabled",true);
					$(obj).prev().prev().prev().attr("display","none");
					$(obj).prev().prev().prev().attr("style","display:none");
				}else{
					$(obj).parent().removeClass();
				}
			});
		}
		function isTestResult(){
			var name = $("input[class='isCheckerLog']");
			for(var i=0; i<name.length; i++){
				if(name[i].value){
					$("input[class='isCheckerLog']").each(function(index, obj) {
						obj.name;
					});
				}
			}
			
			var result = $("select[stage='three']");
			for(var i=0; i<result.length; i++){
				if(result[i].value==''){
					$("#finalStatus").val("");
				}else if(result[i].value=='拒绝承认'||result[i].value=='条件承认'){
					$("#finalStatus").val("不承认");
					return;
				}else{
					$("#finalStatus").val("承认");
				}
			}
		}
		function hisReportInput(){
			var gpMaterialId=$("#gpMaterialId").val();
			if(gpMaterialId&&gpMaterialId!=""){
				window.open('${gpctx}/gpmaterial/input.htm?id='+gpMaterialId);
			};
		}
</script>