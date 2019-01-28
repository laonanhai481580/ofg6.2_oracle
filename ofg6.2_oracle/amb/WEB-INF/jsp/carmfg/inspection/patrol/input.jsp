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
	<script language="javascript" src="${ctx}/widgets/lodop/LodopFuncs.js"></script>
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="${ctx}/widgets/lodop/install_lodop.exe"></embed>
	</object> 
	<script type="text/javascript">
	var LODOP; //声明打印的全局变量 
	function getScrollTop(){
		 return $("#opt-content").height()+23;
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
	$(document).ready(function(){
		$("#scroll").bind("scroll",function(){
			$("#checkItemsParent").find("div").scrollLeft($("#scroll").scrollLeft());
		});
		$("#checkItemsParent").find("div").bind("scroll",function(){
			$("#scroll").scrollLeft($("#checkItemsParent").find("div").scrollLeft());
		});
		$("#opt-content").bind("scroll",function(){
			checkScrollDiv();
		});
		$("input[isDate=true").each(function(index,obj){
			$(obj).datetimepicker({changeYear:'true',changeMonth:'true'});
		});
		autoSave();
		initForm();				
		bindCustomEvent();
		contentResize();
		$.parseDownloadPath({
			showInputId : 'attachmentFilesShow',
			hiddenInputId : 'attachmentFiles'
		});
		var attachments=[];
		attachments.push({showInputId:'attachmentFilesShow',hiddenInputId:'attachmentFiles'});
		parseDownloadFiles(attachments);
		if("${reportState}" != "<%=MfgPatrolReport.STATE_DEFAULT%>"
			&&"${reportState}" != "<%=MfgPatrolReport.STATE_RECHECK%>"){
			$("button").attr("disabled","disabled");
			$(":input").attr("disabled","disabled");
			$("a").removeAttr("onClick");
			$("#message").html("<font color=red>检验状态为【${reportState}】,不能修改!</font>");
			$(".opt-btn").find("button").attr("disabled","");
			$("td[buttd=true]").find("button").each(function(index,obj){
				$(obj).attr("disabled","disabled");
				$(obj).attr("display","none");
				$(obj).attr("style","display:none");
			});
			return;
		}else{
			tdFlagChange();
			dateOrder();
		}
		var machineNo=$("#machineNo").val();
		var workProcedure=$("#workProcedure").val();
		if("${id}"&&machineNo&&workProcedure){
			$("#machineNo").attr("disabled","disabled");
			$("#workProcedure").attr("disabled","disabled");
			$("button[name=loadButton]").attr("display","none");
			$("button[name=loadButton]").attr("style","display:none");
		}
		setTimeout(function(){
			$("#message").html("");
		},3000);
		//自动填写,物料编码
		$("#machineNo")
		.bind("blur",function(){
			if(!$(this).data("autocomplete").menu.element.is(":visible")){
				var hisSearch = $(this).attr("hisSearch");
				if(this.value != hisSearch){
					$(this).attr("hisSearch",this.value);
				}
			}
		})
		.autocomplete({
			minLength: 1,
			delay : 200,
			source: function( request, response ) {
				response([{label:'数据加载中...',value:''}]);
				$.post("${mfgctx}/common/search-machine-no.htm",{searchParams:'{"machineNo":"'+request.term+'"}',label:'machineNo'},function(result){
					response(result);
				},'json');
			},
			focus: function() {
				return false;
			},
			select: function( event, ui ) {
				if(ui.item.value){
					var his = $(":input[name=machineNo]").attr("hisValue");
					$(":input[name=machineNo]").val(ui.item.code);
					$(":input[name=machineNo]").val(ui.item.code).attr("hisValue",ui.item.code);
					$(":input[name=machineName]").val(ui.item.value);
					$(":input[name=machineName]").val(ui.item.value).attr("hisValue",ui.item.value);
				}else{
					$(":input[name=machineNo]").val("");
					$(":input[name=machineName]").val("");
				}
				return true;
			},
			close : function(event,ui){
				var val = $(":input[name=machineNo]").val();
				if(val){
					var hisValue = $(":input[name=machineNo]").attr("hisValue");
					if(val != hisValue){
						$(":input[name=machineNo]").val(hisValue);
					}
				}else{
					$(":input[name=machineNo]").val("").attr("hisValue","").attr("hisSearch",'');
					$(":input[name=machineName]").val("");
				}
			}
		});
	});

	function autoSave(){
		setTimeout(function(){
			alert("为防止数据丢失，请先暂存表单！");
			autoSave();
		},1000*60*15);
	}
	function initForm(){
		$("input[name^='inspectDate']").each(function(index,obj){
			$(obj).datepicker({changeMonth:true,changeYear:true});
		});
		addFormValidate('${fieldPermission}', 'defectiveGoodsForm');
		var attachmentMap = {
			reviewAttachment : true,
			inspectorAttachment :true,
			qualityEngineerAttachment:true,
		};
		var fieldPermission = ${fieldPermission};
		 if(fieldPermission.length==1&&fieldPermission[0].controlType=='allReadolny'){
			$("a").removeAttr("onclick");
			$("button[uploadBtn=true]").hide();
			$(":input[name]").attr("disabled","disabled");
		} 
		if("${reportState}" == "<%=MfgPatrolReport.STATE_DEFAULT%>"||"${reportState}" == "<%=MfgPatrolReport.STATE_RECHECK%>"){
			$("#processingResult").attr("disabled","disabled");
			$("#auditText").attr("disabled","disabled");
		}
		for(var i=0;i<fieldPermission.length;i++){
			var obj=fieldPermission[i];
			if(obj.readonly=='true'){
				var $obj = $(":input[name="+obj.name+"]");
				if($obj.attr("type") != 'hidden'){
					$obj.attr("disabled","disabled");
				}else{
					if(attachmentMap[obj.name]){
						$obj.closest("td").find("button[uploadBtn=true]").hide();
					}
				}
				if(obj.name=="supplierMessageStr"){
					$(".mfgSupplierMessagesTr").find(":input").attr("disabled","disabled");
					$(".mfgSupplierMessagesTr").find("a").removeAttr("onclick");
				}else if(obj.name=="checkedItemStr"){
					$("#checkItemsParent").find(":input").attr("disabled","disabled");
					$("#checkItemsParent").find("a").removeAttr("onclick");
				}
			}
		};
	}
	function choiceWaitCheckItem(){
		var workProcedure = $("#workProcedure").val().replace("+","%2B");
		var businessUnitCode=$(":input[name=businessUnitName]").val();
		var checkBomCode = $("#machineNo").val();
		var inspectionPointType=$("#inspectionPointType").val();
		var url="id=${id}&businessUnitCode="+businessUnitCode+"&checkBomCode="+checkBomCode+"&workProcedure="+workProcedure+"&inspectionPointType=" +inspectionPointType;
		if(checkBomCode==''||workProcedure==''){
			return;
		}
		$.colorbox({href:"${mfgctx}/inspection/patrol/wait-checked-items.htm?"+url,
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择检验项目"
 		});
	}
	function getTempItem(){
		var itemName="";
		//根据是否有检验员，来判定该项目是否有提交过
		$(":input[fieldName=inspector]").each(function(index,obj){
			var objval=$(obj).val();
			if(objval==""){
				$(obj).prev().val("未领取");
				$("td[tdShow=false]").hide();
			}else{
				itemName+=$(obj).attr("inspector")+",";
			}
		});
		return itemName;
	}
	function completeItem(datas,parentColspan){
		$("td[tdShow=false]").hide();
		$("td[tdShow=false]").hide();
		var index=0;
		for(var pro in datas){
			index++;
			$("td[name="+datas[pro]+"]").html($("td[name][tdShow=true]").length+index);
			$("td[itemName="+datas[pro]+"]").show();
			$(":input[itemstatus="+datas[pro]+"]").val("已领取");
		}
	}
	function submitForm(url,b){
		var dateList="";
		var flag=false;
		var empty=false;
		$("#checkItemsParent").find("input[isDate=true]").each(function(index,obj){
			var date=$(obj).val();
			if(dateList.indexOf(date)>-1){
				flag=true;				
			}else{
				dateList+=date+",";
			}
		});
		if(flag||dateList==""){
			alert("检验日期不能重复且不能为空!");
			return;
		}
		if(b){
			if($("#inspectionForm").valid()){
				$('#inspectionForm').attr('action',url);
				$("#message").html("<b>数据保存中,请稍候... ...</b>");
				$(".opt-btn .btn").attr("disabled",true);
				$('#inspectionForm').submit();
			}else{
				var error = $("#inspectionForm").validate().errorList[0];
				$(error.element).focus();
			}
		}else{
			$('#inspectionForm').attr('action',url);
			$("#message").html("<b>数据保存中,请稍候... ...</b>");
			$(".opt-btn .btn").attr("disabled",true);
			$('#inspectionForm').submit();
		}
		
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
 					width: 400,
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
	function loadCheckItems(){
		if(!confirm("加载检验项目会导致已有的记录丢失，是否确认加载!")){
			return;
		}
		var workProcedure = $("#workProcedure").val();
		var checkBomCode = $("#machineNo").val();
		var inspectionPointType=$("#inspectionPointType").val();
	if(!checkBomCode||!workProcedure){
			
		}else{
			var params = {
				workProcedure : workProcedure,
				checkBomCode : checkBomCode,
				inspectionPointType:inspectionPointType,
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
			var url = "${mfgctx}/inspection/patrol/check-items.htm";
			$("#checkItemsParent").find("div").load(url,params,function(){
				bindCustomEvent();
				var contentWidth =  $("#checkItemsParent").find("table").width();
				$("#scroll").find("div").width(contentWidth);
				checkScrollDiv();
				$("input[isDate=true]").each(function(index,obj){
					$(obj).datetimepicker({changeYear:'true',changeMonth:'true'});
				});
			});
		}
	}
	//删除检验记录
	function deleteRecord(obj){
		var attr=$(obj).parent().attr("tdFlag");
		var i=0;
		$("#checkItemsHeader").find("td[records=true]").each(function(index,obj){
			i++;
		});
		if(i>1){
			$("#checkItemsParent").find("td[tdFlag="+attr+"]").remove();				
		}else{
			alert("至少保留一列！");
		};
		tdFlagChange();
	}
	//定义编号
	function tdFlagChange(){
		$("#checkItemsParent").find("tr[itemTr=true]").each(function(index,obj){
			var i=1;
			$(obj).find("td[records=true]").each(function(index,o){
				$(o).attr("tdFlag",i);
				i++;
			});
		});	
	}
	//日期改变事件
	function inspectionDateChange(str){
		var date=$(str).val();
		var tdFlag=$(str).parent().attr("tdFlag");		
		$("#checkItemsParent").find("td[tdFlag="+tdFlag+"]").each(function(index,obj){
			$(obj).find("input").each(function(index,o){
				var name=$(o).attr("name");
				var m = name.length;
				var n=name.replace("_", "").replace("_", "").length;
				if(m-n==1){
					$(o).attr("name",name+"_"+date);
					var b=name.split("_")[1];
					if(b!="inspectionDate"){
						$(o).attr("id",name+"_"+date);
					}					
				}else if(m-n==2){
					var a=name.split("_")[0];
					var b=name.split("_")[1];
					$(o).attr("name",a+"_"+b+"_"+date);
					if(b!="inspectionDate"){
						$(o).attr("id",name+"_"+date);
					}
				}				
			});
			$(obj).find("textarea").each(function(index,o){
				var name=$(o).attr("name");
				if(name.endsWith("results")){
					$(o).attr("name",name+"_"+date);
					$(o).attr("id",name+"_"+date);
				}else{
					var a=name.split("_")[0];
					var b=name.split("_")[1];
					$(o).attr("name",a+"_"+b+"_"+date);
					$(o).attr("id",a+"_"+b+"_"+date);
				}
			});
		});	
	}
	//表头日期排序
	function dateOrder(str){
		$("#checkItemTr").find("td[records=true]").each(function(index,obj){
			var flag =$(obj).attr("tdFlag");
			var name=$(obj).find("input").last().attr("name");
			if(!name){
				name=$(obj).find("textarea").last().attr("name");
			}
			var date=name.split("_")[2];
			var input=$("#checkItemsHeader").find("td[tdFlag="+flag+"]").find("input");
			$(input).attr("name","a_inspectionDate_"+date);
			//$(input).attr("id","a_inspectionDate_"+date);
			$(input).attr("value",date);
			$(input).val(date);
		});
	}
	
	
	//增加检验记录
	function addItems(){
		var td =$("#checkItemsHeader").find("td").last();
		var inspectionDate=$(td).find("input[isDate=true]").val();
		if(!inspectionDate){
			alert("请先完成之前的记录！");
			return;
		}
		$("#checkItemsParent").find("tr[itemTr=true]").each(function(index,obj){
			var td =$(obj).find("td").last();
			var clonetr = td.clone(false);
			$(clonetr).find("input").each(function(index,o){
				$(o).val("");				
				var attr=$(o).attr("isDate");
				if(attr=="true"){
					var name=$(o).attr("name");
					$(o).attr("id",name+"_a");
					$(o).attr("value","");
					$(o).removeAttr("class","hasDatepicker");
					$(o).datetimepicker({changeYear:true,changeMonth:true});
				};	
				var results=$(o).attr("results");
				if(results=="true"){
					$(o).removeAttr("color");
					$(o).bind("change",resultChange);
				};	
			});
			$(clonetr).find("textarea").each(function(index,o){
				$(o).val("");
			});
			td.after(clonetr);
		});
		tdFlagChange();
	}
	function addResultInput(obj,checkItemName){
		var tdLen = $(obj).parent().find("input").length;
		if(tdLen==80){
			alert("最多能添加80项！");
		}else{
			var countTypeName = $(obj).closest("td").find(":input[result=true]").attr("name");
			var flagIndex = countTypeName.split("_")[0];
			var date = countTypeName.split("_")[2];
			var html="";
			if(tdLen%10==0){
				html+="<br/>";
			}
			html+= "<input color=\"black\" style=\"width:38px;float:left;margin-left:2px;\" title=\""+checkItemName+"样品"+(tdLen+1)+"\" name=\""+flagIndex+"_result"+(tdLen+1)+"_"+date+"\"  class=\"{number:true,min:0}\"></input>";
			$(obj).before(html);
			//更新顶部的宽度
			updateCheckItemsHeaderWidth();
			$(obj).parent().find("input").last().bind("change",resultChange);
		}
	}
	function removeResultInput(obj){
		$(obj).parent().find("input:last").remove();
		//更新顶部的宽度
		updateCheckItemsHeaderWidth();
		//初始化滚动条
		initScrollDiv();
	}
	//更新顶部的宽度
	function updateCheckItemsHeaderWidth(){
		var maxLen = 10;
		$("#checkItemsHeader").width(maxLen*38+70);
		//初始化滚动条
		initScrollDiv();
	}
	//添加各种事件
	function bindCustomEvent(){
		//计量的值不符合范围时的事件
		$(".checkItemsClass input").bind("change",resultChange);
	}
	function resultChange(){
		if(!isNaN(this.value)){
			var parentTr = $(this).parent().parent();
			var maxlimit = parentTr.find(":input[fieldName=maxlimit]").val(),minlimit = parentTr.find(":input[fieldName=minlimit]").val();
			if(maxlimit&&!isNaN(maxlimit)||minlimit&&!isNaN(minlimit)){
				if(!this.value){
					$(this).css("color","black").attr("color","black");
				}else{
					var val = parseFloat(this.value);
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
					}else{//有上限和下限
						if(val>=parseFloat(minlimit)&&val<=parseFloat(maxlimit)){
							$(this).css("color","black").attr("color","black");
						}else{
							$(this).css("color","red").attr("color","red");
						}
					}
				}
			}
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
			}
		}
	}
	<jsp:include page="audit-method.jsp" />
	function getTdItem(obj){
		var value="";
		$(obj).find(":input[name]").each(function(index,obj){
			iobj = $(obj);
		    value += ",\""+iobj.attr("filedName")+"\":\""+iobj.val()+"\"";
		});
		return ",{"+value.substring(1)+"}";
   	}
	//导出
	function exportForm(){
		var id = '${id}';
		var current = 0;
		var dd = setInterval(function(){
			current++;
			var str = '';
			for(var i=0;i<(current%3);i++){
				str += "...";
			}
		}, 500);
		$("#iframe").bind("readystatechange",function(){
			clearInterval(dd);
			$("#message").html("");
			$("#iframe").unbind("readystatechange");
			printCertification();
		}).attr("src","${mfgctx}/inspection/made-inspection/download-report.htm?id="+id);
	}
	function setAuditMan(obj){
		var loginName = obj.value;
		var selectIndex = obj.selectedIndex;
		var name = obj.options[selectIndex].innerHTML;
		$("#choiceAuditLoinMan").val(loginName);
		$("#choiceAuditMan").val(name);
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu='patrol';
		var thirdMenu="patrol_input";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-patrol-inspection-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
			<div class="opt-body">
					<div class="opt-btn" style="padding:0px;height:30px;">
						<table style="width:100%;" cellpadding="0" cellspacing="0">
							<tr>
								<td style="padding-left:4px;">
									<security:authorize ifAnyGranted="mfg_patrol_input">
									<button class='btn' onclick="window.location='${mfgctx}/inspection/patrol/input.htm'" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
									</security:authorize>
									<c:if test="${taskId==null}">
										<security:authorize ifAnyGranted="mfg_patrol_save,mfg_patrol_submit">
										<button class='btn' type="button" onclick="submitForm('${mfgctx}/inspection/patrol/save.htm',false);"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
 										<button class='btn' type="button" onclick="submitForm('${mfgctx}/inspection/patrol/submit-process.htm',true);"><span><span><b class="btn-icons btn-icons-submit"></b>提交</span></span></button>
										</security:authorize>
									</c:if>
									<c:if test="${reportState=='检验中'}">
										<button class='btn' type="button" onclick="addItems();"><span><span><b class="btn-icons btn-icons-add"></b>增加检验记录</span></span></button>
									</c:if>
									<button class='btn' type="button" onclick="javascript:window.history.back();"><span><span><b class="btn-icons btn-icons-undo"></b>返回</span></span></button>
									<span style="margin-left:6px;line-height:30px;" id="message"><s:actionmessage theme="mytheme" cssStyle="color:red;" /></span>
								</td>
							</tr>
						</table>
					</div>
					<div><iframe id="iframe" style="display:none;"></iframe></div>
					<div id="opt-content" style="text-align: center;">
					<div id="scroll" style="position:absolute;top:0px;left:15px;overflow-y:hidden;overflow-x:auto;height:35px;line-height:35px;display:block;z-index:2;">
						<div style="">&nbsp;</div>
					</div>
					<form action="" method="post" id="inspectionForm" name="inspectionForm" enctype="multipart/form-data">
						<input type="hidden" name="inspectionPoint" value="${inspectionPoint}"/>
						<input type="hidden" name="id" value="${id}"></input>
						<input type="hidden" name="checkItemStrs" value="${checkItemStrs}"/>
						<input type="hidden" name="params.savetype" value="input"></input>
						<input type="hidden" name="acquisitionMethod" value="on"></input>
						<jsp:include page="input-form.jsp" />
					</form>
				</div>
			</div>
	</div>
</body>
</html>