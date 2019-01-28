<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	function findKey(obj){
		var value = obj.value;
		var url = "${supplierctx}/change/select-level.htm?valueStr=" + value;
		$.post(encodeURI(url),{},function(result){
			if(result.error){
				alert(result.message);
			}else{
				$("#changeLevel").val(result.level);
			}
		},'json');
	}
	
	function setAllLogs(){
		var allLogs = "";
		$("#checkerLog input[class=isCheckerLog]").each(function(index,obj){
			if(obj.value!=""&&obj.value){
				if(allLogs.length==0){
					allLogs = obj.value;
				}else{
					allLogs += ","+obj.value;
				}
			}
		});
		$("#checkDeptMansLog").val(allLogs);
	}	

	function asignInit(a){
		var str = $("#currentActivityName").val();
		var name=$("#transactorName").val();
		var procurement=$("#procurementProcesserLog").val();
		var project=$("#projectProcesserLog").val();
		var develop=$("#developProcessLog").val();
		var qe=$("#qeProcesserLog").val();
		var quality=$("#qualityProcesserLog").val();
		if(str=="会签"){
			$("textarea:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("input:[isAsign='true']").each(function(index, obj) {
				var str=$(obj).next().next().val();
				if(name!=str){
					$(obj).attr("disabled","disabled");
					$(obj).next().removeAttr('onclick');
				}				
			});
			$("#projectFile").next().attr("style","display:none");
			$("#developFile").next().attr("style","display:none");
			$("#isMailCustomer").attr("disabled","disabled");
			var flag="";
			if(name==quality){
				$("#qualityOpinion").removeAttr("disabled");						
				flag=true;
			}
			if(name==procurement){
				$("#procurementOpinion").removeAttr("disabled");						
				flag=true;
			}
			if(name==project){
				$("#projectOpinion").removeAttr("disabled");	
				$("#projectFile").removeAttr("disabled");
				$("#projectFile").next().attr("style","display");
				flag=true;
			}
			if(name==develop){
				$("#developOpinion").removeAttr("disabled");
				$("#developFile").removeAttr("disabled");	
				$("#developFile").next().attr("style","display");
				flag=true;
			}
			if(name==qe){
				$("#qeOpinion").removeAttr("disabled");
				$("#isMailCustomer").removeAttr("disabled");
				flag=true;
			}			
			if(!flag){
				$("#qeOpinion").removeAttr("qeOpinion");
				$("#developChecker").removeAttr("disabled");
				$("#developFile").removeAttr("disabled");
				$("#projectOpinion").removeAttr("disabled");	
				$("#projectFile").removeAttr("disabled");
				$("#procurementOpinion").removeAttr("disabled");
			}
		}
	}	
	
</script>