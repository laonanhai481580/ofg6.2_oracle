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
		function setAllLogs(){
			var allLogs = "";
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
			var str = $("#currentActivityName").val();
			addBt();
			lcfj(a);
		}
		function isTestResult(obj){
			var result = $("#skillLeadStatus");
				if(result.val()==''){
					$("#finalStatus").val("");
				}else if(result.val()=='条件承认'||result.val()=='拒绝承认'){
					$("#finalStatus").val("拒绝承认");
				}else{
					$("#finalStatus").val("承认");
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
		function addBt(){
			var str = $("#currentActivityName").val();
			$("input:[stage='BT1']").each(function(index, obj) {
				$(obj).addClass("class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-button-text'");
				if(str=='会签'||str=='再次会签'||str=='SQE确认'){
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
					$(obj).parent().addClass("admit");
					$(obj).prev().prev().prev().attr("disabled",true);
					$(obj).prev().prev().prev().attr("display","none");
					$(obj).prev().prev().prev().attr("style","display:none");
				}else{
					$(obj).parent().removeClass();
				}
			});
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
			}else if(str=="会签"||str=="再次会签"){
				var name=$("#transactorName").val();
				var rd=$("#rdCheckerLog").val();
				var pm=$("#pmCheckerLog").val();
				var qs=$("#qsCheckerLog").val();
				var npi=$("#npiCheckerLog").val();
				var dqe=$("#dqeCheckerLog").val();
				var pro=$("#projectCheckerLog").val();
				var ee=$("#eeCheckerLog").val();
				var el=$("#elseCheckerLog").val();
				$("select:[stage='three']").each(function(index, obj) {
					$(obj).attr("disabled","disabled");
				});
				$("textarea:[stage='three']").each(function(index, obj) {
					$(obj).attr("disabled","disabled");
				});
				$("input:[stage='one']").each(function(index, obj) {
					$(obj).attr("disabled","disabled");
				});
				$("a:[addbtn='true']").each(function(index, obj) {
					$(obj).removeAttr('onclick');
				});
				$("a:[delbtn='true']").each(function(index, obj) {
					$(obj).removeAttr('onclick');
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
				if(name==ee){
					$("#eeStatus").removeAttr("disabled");
					$("#countersignEE").removeAttr("disabled");
					$("#eeStatus").addClass("{required:true, messages:{required:'必填'}}");
				}
				if(name==el){
					$("#elseStatus").removeAttr("disabled");
					$("#countersignElse").removeAttr("disabled");
					$("#elseStatus").addClass("{required:true, messages:{required:'必填'}}");
				}
			}
		}
		function hisReportInput(){
			var gpMaterialId=$("#gpMaterialId").val();
			if(gpMaterialId&&gpMaterialId!=""){
				window.open('${gpctx}/gpmaterial/input.htm?id='+gpMaterialId);
			};
		}
		var selectIndex="";
		function selectProject(obj){
			var materialSort ="";
			materialSort = obj.value;
			if(obj.value==""){
				materialSort = $("#materialSort").val();
			}
			if(materialSort==""){
				return ;
			}
			$("tr[zbtr2=true]").each(function(index,obj){				
				var trprefix=$(obj).attr("trprefix");
				if(trprefix=='a01'){
					$(obj).find(":input").each(function(index,o){
						$(o).val("");
					});
				}else{
					$(obj).remove();
				}				
			});
			$.colorbox({href:"${supplierctx}/base-info/admit-basics/select-list.htm?materialSort="+materialSort,
				iframe:true, 
				width:$(window).width()<700?$(window).width()-100:900,
				height:$(window).height()<400?$(window).height()-100:600,
					overlayClose:false,
					title:"选择科目"
			});
			
		}
		
		function setProjectValue(datas){
			var a=0;
			var b=1;
			for(var i=0;i<datas.length;i++){
				$("#a" + a + b + "_auditDepartment").val(datas[i].auditDepartment);
				$("#a" + a + b + "_materialName").val(datas[i].materialName);
				$("#a" + a + b + "_remark").val(datas[i].remark);
				selectIndex=$("#a" + a + b + "_auditDepartment")[0].parentNode;
				b++;
				if(i!=datas.length-1){
					addRowHtml(selectIndex,b);
				}
			}
			
		}
		//添加行
		function addRowHtml(aObj,b){
			var $tr = $(aObj).closest("tr");
			var clonetr = $tr.clone(false); 
			$tr.after(clonetr);
			var tableName = $tr.attr("tableName");
			var tableParams = _ambWorkflowFormObj.children[tableName];
			var maxIndex = b;
			tableParams['maxIndex'] = maxIndex;
			var flag = tableParams['inputNamePrefix'] + maxIndex;
			clonetr.attr("trPrefix",flag);
			//重置对象
			clonetr
				.find(":input[fieldName]")
				.unbind()
				.removeClass("hasDatepicker")
				.each(function(index ,input){
					$input = $(input);
					//清除值
					if(_ambWorkflowFormObj.childrenInitParams.addRowToClearValue){
						$input.val("");
						if($input.attr("title")!=""){
							$input.attr("title","");
						}
					}
					var id = flag + "_" + $input.attr("fieldName");
					$input.attr("name",id)
						  .attr("id",id);
					var hiddenInputName = $input.attr("hiddenInputName");
					if(hiddenInputName){
						$input.attr("hiddenInputId",flag + "_" + hiddenInputName);
					}
					var showInputName = $input.attr("showInputName");
					if(showInputName){
						$input.attr("showInputId",flag + "_" + showInputName);
					}
			});
			//移除自动添加的对象
			clonetr.find("[autoAppend]").remove();
			clonetr.find(":input[isFile]").removeAttr("showInputId");
			//初始化新增行的输入元素
			_initInputForScope(clonetr);
			//更新序号
			_updateRowNum(tableName);
			//检查是否有回调事件
			if($.isFunction(_ambWorkflowFormObj.childrenInitParams.afterAddRow)){
				_ambWorkflowFormObj.childrenInitParams.afterAddRow(tableName,clonetr);
			}
		}		
</script>