<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%> 
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
<script type="text/javascript">
		jQuery.extend($.jgrid.defaults,{
			loadComplete : function(){
				if(scroolTop != null){
					$($("#evaluatingIndicatorList").closest(".ui-jqgrid-bdiv")[0]).scrollTop(scroolTop);
					scroolTop = null;
				}
			}
		});
		
		function showPicture(value,options,obj){
			var strs = "";
			strs = "<div style='width:100%;' title='上传检验项目图片' ><a class=\"small-button-bg\"  onclick=\"attachmentFilesClick('"+obj.id+"');\" href=\"#\"><input type='hidden' id='"+obj.id+"_hiddenFiles' value='"+value+"'/><span id='"+obj.id+"_uploadBtn' class='ui-icon ui-icon-image uploadBtn' style='cursor:pointer;display:none;'></span></a><span style='text-align:left;' id='"+obj.id+"_showFiles'>"+$.getDownloadHtml(value)+"</span><div>";
			return strs;
		}
		function attachmentFilesClick(rowId){
			//上传附件 
			$.upload({   
				//file_types:"*.jpg;*.bmp;*.png",
				showInputId : rowId + "_showFiles",
				hiddenInputId : rowId + "_hiddenFiles",
				title:"上传附件",
				callback:function(files){
					params.attachmentFiles = $("#" + rowId + "_hiddenFiles").val();
				}
			}); 
		}
		
		function actFormat(value){
			var strs = "";
			strs = "<div style='width:100%;text-align:center;' title='编辑检验项目' ><a class=\"small-button-bg\" onclick=\"editModelIndicator("+value+");\" href=\"#\"><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a><div>";
			return strs;
		}
		
		function indicatorAttachFormater(value,options,rowObj){
			if(rowObj.indicatorAttachId&&rowObj.indicatorAttachId!='&nbsp;'){
				return "<div style='width:100%;text-align:center;' title='下载检验标准' ><a style='margin-top:2px;margin-bottom:-3px;' class=\"small-button-bg\" onclick=\"downloadAttach("+rowObj.indicatorAttachId+");\" href=\"#\"><span class='ui-icon ui-icon-arrowthickstop-1-s' style='cursor:pointer;'></span></a><div>";
			}else{
				return "";
			}
		}
		
		function downloadAttach(id){
			window.location.href = '${iqcctx}/inspection-base/indicator/download-attach.htm?id=' + id;
		}
		
		/**
		* 刷新检验项目
		*/
		var scroolTop = null;
		function refreshEvaluatingIndicator(){
			scroolTop = $($("#evaluatingIndicatorList").closest(".ui-jqgrid-bdiv")[0]).scrollTop();
			$("#evaluatingIndicatorList").trigger("reloadGrid");
		}
		
		//选择物料BOM
	 	function selectBomValue(){
	 		var url = '${mfgctx}/common/product-bom-select.htm';
	 		$.colorbox({href:url,iframe:true,
	 			innerWidth:700, 
	 			innerHeight:400,
	 			overlayClose:false,
	 			title:"选择物料BOM"
	 		});
	 	}
		
		//选择之后的方法 data格式{key:'a',value:'a'}
		var selRowId = null;
	 	function setFullBomValue(datas){
			$("#" + selRowId + "_materielName").val(datas[0].name);
	 		$("#" + selRowId + "_materielCode").val(datas[0].code).focus();
	 	}
		var params = {};
		function $oneditfunc(rowId){
			selRowId = rowId;
/* 			$("#" + rowId + "_materielCode").attr("readonly","readonly")
			.click(function(){
				selectBomValue(rowId);
			});
			$("#" + rowId + "_materielName").attr("readonly","readonly")
			.click(function(){
				selectBomValue(rowId);
			}); */
			params.attachmentFiles = $("#" + rowId + "_hiddenFiles").val();
			params.hisAttachmentFiles = params.attachmentFiles;
			$(".uploadBtn").hide();
			$("#" + rowId + "_uploadBtn").show();
		}
		function $successfunc(response){
			var result = eval("(" + response.responseText + ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		function $afterrestorefunc(rowId){
			$("#" + rowId + "_uploadBtn").hide();
		}
		
		function $processRowData(data){
			for(var pro in params){
				data[pro] = params[pro];
			}
			return data;
		}
		//创建检验项目
		function createEvaluatingIndicator(){
			openEvaluatingIndicator();
		}
		//编辑检验项目
		function editEvaluatingIndicator(){
			var id = $("#evaluatingIndicatorList").jqGrid("getGridParam","selrow");
			if(!id){
				alert("请选择需要编辑的检验物料!");
				return;
			}
			openEvaluatingIndicator(id);
		}
		//打开检验项目
		function openEvaluatingIndicator(id){
			var parentId = null;
			var url='${iqcctx}/inspection-base/indicator/input.htm?1=1';
			if(id){
				url += '&id='+id;
			}else{
				parentId = $("#evaluatingIndicatorList").jqGrid("getGridParam","selrow");
				if(parentId){
					url += "&parentId=" + parentId;
				}
			}
			$.colorbox({href:url,iframe:true, innerWidth:800, innerHeight:600,
				overlayClose:false,
				onClosed:function(){
					refreshEvaluatingIndicator();
				},
				title:(id?"编辑":"添加") + "检验物料"
			});
		}
		//删除检验项目
		function deleteEvaluatingIndicator(){
			var ids = $("#evaluatingIndicatorList").jqGrid("getGridParam","selarrrow");
			if(!ids){
				alert("请选择要删除的检验物料!");
				return;
			}
			if(confirm("确定要删除选择的检验物料吗?")){
				$.post('${iqcctx}/inspection-base/indicator/delete.htm',{deleteIds:ids.join(",")},function(result){
					if(result.error){
						alert(result.message);
					}else{
						refreshEvaluatingIndicator();
					}
				},'json');
			}
		}
		//编辑检验项目
		function editModelIndicator(id){
			if(id&&id!=""&&id!=null){
				var url='${iqcctx}/inspection-base/indicator/edit-indicator.htm?indicatorId=' + id;
				$.colorbox({href:url,iframe:true,
					width:$(window).width()-100,
					height:$(window).height()-20,
					overlayClose:false,
					title:"设置检验项目"
				});
			}else{
				alert("对不起，请先选择需要维护检验标准的物料！");
			}
		}
		//导入
		function imports(){
			var url = '${iqcctx}/inspection-base/indicator/import-form.htm';
			$.colorbox({href:url,iframe:true, innerWidth:380, innerHeight:200,
				overlayClose:false,
				title:"导入抽样标准",
				onClosed:function(){
					$("#evaluatingIndicatorList").trigger("reloadGrid");
				}
			});
		}
		
		//复制抽样标准
		function copyInspectingIndicator(){
			var ids = $("#evaluatingIndicatorList").jqGrid("getGridParam","selarrrow");
			if(ids.length==0){
				alert("请先选择选择检验标准!");
			}else if(ids.length>1){
				alert("只能选择一条检验标准!");
			}else{
				var html = '<div id="auditBody" class="opt-body" style="overflow-y:auto;"><div class="opt-btn" style="line-height:30px;">'
					+'<button class="btn" type="button" onclick="processingResults();"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>'
					+'<span id="copyMessage" style="color:red;padding-left:4px;"></span></div>';
					html += '<form id="auditForm"><table class="form-table-border-left" style="margin:4px;width:98%;">';
					html += '<tr height=29><td style="width:20%;">物料编码</td><td  style="width:10%;"><input value="'+ids+'" type="hidden" name="id"></input><input name="materielCode" value="" class="{required:true,messages:{required:\'必填\'}}"></input></td>';
					html += '<td style="width:20%;">物料名称</td><td style="width:15%;"><input name="materielName" value="" class="{required:true,messages:{required:\'必填\'}}"></input></td></tr>';
					html += '<tr><td>零件类别</td><td><input name="materielType" value="" class="{required:true,messages:{required:\'必填\'}}"/></td>';
					html += '<td>图纸版本</td><td><input name="figureNumber" value=""  class="{required:true,messages:{required:\'必填\'}}"/></td></tr>';
					html += '</table></form>';
					var height = $(window).height()<450?$(window).height()-50:450;
					var width = $(window).width()-200>750?750:$(window).width();
					$.colorbox({
						title : '复制检验标准',
						html : html,
						iframe:false,
						height : height,
						width:width,
						onComplete:function(){
							$("#auditBody").height(height-50);
						}
					});
			}
		}
		function processingResults(sourceTaskId){
			if($("#auditForm").valid()){
				var params = {};
				$("#auditForm :input[name]").each(function(index,obj){
					if(obj.type=='radio'){
						if(obj.checked){
							params[obj.name] = obj.value;
						}
					}else{
						params[obj.name] = $(obj).val();
					}
				});
				$("#auditBody").find("button").attr("disabled","");
				$("#copyMessage").html("正在复制抽样标准,请稍候... ...");	
				$.post("${iqcctx}/inspection-base/indicator/copy-inspecting-indicator.htm",params,function(result){
					$("#auditBody").find("button").attr("disabled","");
					$("#copyMessage").html(result.message);	
					if(result.error){
						alert(result.message);		
					}else{
						$("#evaluatingIndicatorList").trigger("reloadGrid");
					};
					setTimeout(function(){
						$("#message").html("");
					},3000);
				},'json');
			};
		}
		
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="IQC_INSPECTION-BASE_INDICATOR_LIST_UPDATE">
				isRight =  true;
			</security:authorize>
			return isRight;
		}
		
		//一次性导入供应商准入物料
		function initAdmittanceMaterial(){
			var url = '${supplierctx}/archives/import-supplier-material-form.htm';
			$.colorbox({href:url,iframe:true, 
				innerWidth:350, innerHeight:240,
				overlayClose:false,
				title:"导入供应商准入物料",
				onClosed:function(){
					
				}
			});
		}
		
		function addPicture(obj){
			 return "";
		}
		function downloadTemplate(){
			window.location = '${iqcctx}/inspection-base/indicator/download-template.htm';
		}
		function viewHistory(){
			var ids = $("#evaluatingIndicatorList").jqGrid("getGridParam","selarrrow");
			if(ids.length==0){
				alert("请先选择选择检验标准!");
			}else if(ids.length>1){
				alert("只能选择一条检验标准!");
			}else{
				var rowData = $("#evaluatingIndicatorList").jqGrid("getRowData",ids[0]);
				var url = '${iqcctx}/inspection-base/indicator/list-history.htm?materielCode=' + rowData.materielCode;
				window.location = url;
			}
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="base";
		var thirdMenu="indicator";
		var treeMenu = 'indicator';
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-inspection-base-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn" style="line-height:30px;">
				<%-- <security:authorize ifAnyGranted="iqc-inspecting-indicator-save">
					<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize> --%>
				<security:authorize ifAnyGranted="iqc-inspecting-indicator-delete">
					<button class='btn' onclick="deleteEvaluatingIndicator();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="iqc-inspecting-indicator-copy">
					<button  class='btn' type="button" onclick="copyInspectingIndicator();"><span><span><b class="btn-icons btn-icons-paste"></b>复制检验标准</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="iqc-inspecting-indicator-imports">
					<button class='btn' onclick="imports();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导入检验标准</span></span></button>
					<button class="btn" onclick="downloadTemplate();"><span><span><b class="btn-icons btn-icons-download"></b>下载导入模板</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="iqc-inspecting-indicator-export">
					<button  class='btn' type="button" onclick="iMatrix.export_Data('${iqcctx}/inspection-base/indicator/export.htm');"><span><span><b class="btn-icons btn-icons-import"></b>导出</span></span></button>
				</security:authorize>
<!-- 				<button class='btn' type="button" onclick="viewHistory();"><span><span><b class="btn-icons btn-icons-search"></b>查看所有版本</span></span></button> -->
				<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<!-- <button class="btn" onclick="initAdmittanceMaterial();">
					<span><span><b class="btn-icons btn-icons-export"></b>导入准入物料</span></span>
				</button> -->
					<span id="message" style="color:red;"></span>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post" action="">
						<grid:jqGrid gridId="evaluatingIndicatorList" url="${iqcctx}/inspection-base/indicator/list-datas.htm" code="IQC_INSPECTING_INDICATOR" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>