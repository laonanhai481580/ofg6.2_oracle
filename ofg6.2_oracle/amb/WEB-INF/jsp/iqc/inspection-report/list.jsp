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
	<script type="text/javascript">
	var batchNo = "<%=request.getParameter("batchNo")==null?"":request.getParameter("batchNo")%>";
	jQuery.extend($.jgrid.defaults,{
		beforeRequest : function(){
			if(batchNo){
				return false;
			}
			return true;
		}
	});
	
	function click(cellvalue, options, rowObject){	
		return "<a href='javascript:void(0);' onclick='creatInput("+rowObject.id+");'>"+cellvalue+"</a>";
		//return "<a href='${iqcctx}/inspection-report/input.htm?id="+rowObject.id+"'>"+cellvalue+"</a>";
	}
	function creatInput(id){
		if(id){
			window.open('${iqcctx}/inspection-report/input.htm?id='+id);
		}		
	}
	var dutySupplierId = null;
	function supplierNameClick(obj){
		dutySupplierId = obj.currentInputId;
		var url='${supplierctx}';
		$.colorbox({href:url+"/archives/select-supplier.htm",
			iframe:true, 
			width:$(window).width()<1000?$(window).width()-100:1000, 
			height:$(window).height()<600?$(window).height()-100:600,
			overlayClose:false,
			title:"选择供应商"
		});
	}
	
	function setSupplierValue(objs){
		var obj = objs[0];
		$("#" + dutySupplierId).val(obj.name);
		params.supplierId = obj.id;
	}
	
	var materialNameId = null;
	function checkBomCodeClick(obj){
		var url = '${mfgctx}';
		materialNameId = obj.currentInputId;
		$.colorbox({href:url+"/common/product-bom-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:700, 
			innerHeight:$(window).height()<400?$(window).height()-100:400,
 			overlayClose:false,
 			title:"选择物料BOM"
 		});
 	}
	
	function checkBomNameClick(obj){
		var url = '${mfgctx}';
		materialNameId = obj.currentInputId;
		$.colorbox({href:url+"/common/product-bom-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:700, 
			innerHeight:$(window).height()<400?$(window).height()-100:400,
 			overlayClose:false,
 			title:"选择物料BOM"
 		});
 	}
	
	function setBomValue(datas){
		var rowId = materialNameId.split("_")[0];
		$("#" + rowId + "_checkBomCode").val(datas[0].key);
		$("#" + rowId + "_checkBomName").val(datas[0].value);
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
	
	function $processRowData(data){
		for(var pro in params){
			data[pro] = params[pro];
		}
		data.isLedger = true;
		return data;
	}
	
	//业务判断
	function checkAmount(value,colname){
		var curId = $("#dynamicInspection").jqGrid('getGridParam','selrow');
		var inspectionAmount = $('#'+curId+'_inspectionAmount','#dynamicInspection').val();
		var qualifiedAmount = $('#'+curId+'_qualifiedAmount','#dynamicInspection').val();
		var unqualifiedAmount = $('#'+curId+'_unqualifiedAmount','#dynamicInspection').val();
		if(parseInt(inspectionAmount)!=parseInt(qualifiedAmount)+parseInt(unqualifiedAmount)){
			return [false,"不正确，检验数量不等于合格数和不良数之和！"];
		}else{
			return [true,""];
		}
	}
	
	//业务判断
	function checkStockAmount(value,colname){
		var curId = $("#dynamicInspection").jqGrid('getGridParam','selrow');
		var inspectionAmount = $('#'+curId+'_inspectionAmount','#dynamicInspection').val();
		var stockAmount = $('#'+curId+'_stockAmount','#dynamicInspection').val();
		if(parseInt(inspectionAmount)>parseInt(stockAmount)){
			return [false,"不正确，检验数不能大于来料数！"];
		}else{
			return [true,""];
		}
	}
	
	function inspectorClick(obj){
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择人员',
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:obj.rowid+"_inspector",
			showInputId:obj.currentInputId,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}});
	} 
	
	//导入
	function imports(){
		var url = '${iqcctx}/inspection-report/imports-form.htm';
		$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
			overlayClose:false,
			title:"导入",
			onClosed:function(){
			}
		});
	}

	function $afterrestorefunc(rowId){
		if(rowId > 0){
			$("#" + rowId + " .upload").hide();
			var rowData = $("#dynamicInspection").jqGrid("getRowData",rowId);
			var rate = rowData.qualifiedAmount/(rowData.inspectionAmount*1.0);
			$("#dynamicInspection").jqGrid("setRowData",rowId,{qualifiedRate:rate,inspectionConclusion:rate<100?"NG":"OK"});
		}
	}
	
	//重写(给单元格绑定事件)
	var params = {};
	function $oneditfunc(rowid){
		params = {
			hisAttachmentFiles : $("#" + rowid + "_hiddenAttachmentFiles").val()
		};
		$("#" + rowid + " .upload").show();

		jQuery('#'+rowid+'_qualifiedAmount','#dynamicInspection').change(function(){
			var inspectionAmount = jQuery('#'+rowid+'_inspectionAmount','#dynamicInspection').val();
			var val = this.value;
			if(inspectionAmount&&!isNaN(inspectionAmount)&&val&&!isNaN(val)){
				if(val>inspectionAmount){
					val = inspectionAmount;
					this.value = val;
				}
				jQuery('#'+rowid+'_unqualifiedAmount','#dynamicInspection').val(inspectionAmount - val);
				caclute(rowid);
			}
		});
		jQuery('#'+rowid+'_unqualifiedAmount','#dynamicInspection').change(function(){
			var inspectionAmount = jQuery('#'+rowid+'_inspectionAmount','#dynamicInspection').val();
			var val = this.value;
			if(inspectionAmount&&!isNaN(inspectionAmount)&&val&&!isNaN(val)){
				if(val>inspectionAmount){
				}
				jQuery('#'+rowid+'_qualifiedAmount','#dynamicInspection').val(inspectionAmount - val);
				caclute(rowid);
			}
		});
		jQuery('#'+rowid+'_inspectionAmount','#dynamicInspection').change(function(){
			var unqualifiedAmount = jQuery('#'+rowid+'_unqualifiedAmount','#dynamicInspection').val();
			var qualifiedAmount = jQuery('#'+rowid+'_qualifiedAmount','#dynamicInspection').val();
			var val = this.value;
			if(unqualifiedAmount&&!isNaN(unqualifiedAmount)&&val&&!isNaN(val)&&qualifiedAmount&&!isNaN(qualifiedAmount)){
				if(val<unqualifiedAmount){
					unqualifiedAmount = val;
					jQuery('#'+rowid+'_unqualifiedAmount','#dynamicInspection').val(qualifiedAmount);
				}
				jQuery('#'+rowid+'_qualifiedAmount','#dynamicInspection').val(val - unqualifiedAmount);
				caclute(rowid);
			}
		});
		//获取编号
		if(rowid == 0){
			$("#dynamicInspection").jqGrid("setRowData",rowid,{inspectionNo:'正在生成编号...'});
			$.post("${iqcctx}/inspection-report/get-inspection-no.htm",{},function(result){
				$("#dynamicInspection").jqGrid("setRowData",rowid,{inspectionNo:result});
			});
		}
	}
	
	//系统计算合格率和不良值
	function caclute(rowid){
		var inspectionAmount = jQuery('#'+rowid+'_inspectionAmount','#dynamicInspection').val();
		var qualifiedAmount = jQuery('#'+rowid+'_qualifiedAmount','#dynamicInspection').val();
		var qualifiedRate = (qualifiedAmount/inspectionAmount)*100;
		jQuery('#'+rowid+'_qualifiedRate','#dynamicInspection').val(qualifiedRate.toFixed(1));
		$("#" + rowid + " .rate").html(qualifiedRate.toFixed(1) + "%");
		if(qualifiedRate<100){
			$("#dynamicInspection").jqGrid("setRowData",rowid,{inspectionConclusion:"NG"});
			params.inspectionConclusion = "NG";
		}else{
			$("#dynamicInspection").jqGrid("setRowData",rowid,{inspectionConclusion:"OK"});
			params.inspectionConclusion = "OK";
		}
	}
	
	function formatRate(cellvalue, options, obj){
		if(cellvalue){
			var rate = cellvalue*100;
			if(obj.inspectionAmount != undefined && obj.qualifiedAmount != undefined){
				rate = obj.qualifiedAmount*100.0/obj.inspectionAmount;
			}
			if(rate.toFixed){
				rate = rate.toFixed(2);
			}
			var operations = "<div style='text-align:left;' class='rate'>"+rate+"%</div>";
			return operations;
		}else{
			return "<div style='text-align:left;' class='rate'></div>";
		}
	}
	
	function formateAttachmentFiles(value,o,obj){
		var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title='上传附件'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
		return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
	}
	
	function beginUpload(rowId){
		$.upload({
			showInputId : rowId + "_showAttachmentFiles",
			hiddenInputId : rowId + "_hiddenAttachmentFiles",
			callback : function(files){
				params.attachmentFiles = $("#" + rowId + "_hiddenAttachmentFiles").val();
			}
		});
	}
	
	function $beforeEditRow(rowId,iRow,iCol,e){
		var isRight = false;
		<security:authorize ifAnyGranted="iqc-save">
			isRight =  true;
		</security:authorize>
		return isRight;
	}
	
	function createReport(){
		window.location='${iqcctx}/inspection-report/input.htm';
	}
	function updateReWrite(){
		if(!confirm("确定吗?")){
			return;
		}
		var str = prompt("起始日期:");
		if(!str){
			alert("请输入日期!");
			return;
		}
		var params = {
			updateDate:str
		};
		$.post("${iqcctx}/inspection-report/re-write.htm",params,function(result){
			if(result.error){
				alert(result.message);
			}else{
				alert("成功!");
			}
		},'json');
	}
	//导入
	function importsJqgrid(){
		var url = '${iqcctx}/inspection-report/import-form.htm';
		$.colorbox({href:url,iframe:true, innerWidth:380, innerHeight:200,
			overlayClose:false,
			title:"导入附件",
			onClosed:function(){
				$("#dynamicInspection").trigger("reloadGrid");
			}
		});
	}
	function show_moveiIdentifiersDiv(idName){
		clearTimeout(identifiersDiv);
	}
	var identifiersDiv;
	function hideIdentifiersDiv(idName){
		identifiersDiv = setTimeout('$("#"+idName+")".hide()',300);
	}
	//修改对内对外状态
 	function updateState(obj){
 		hideIdentifiersDiv();
 		var type="";
 		var title = $(obj).attr("title");
 		if(title=="对内"){
 			type="in";
 		}else if(title=="对外"){
 			type="out";
 		}
 		var url='${iqcctx}/inspection-report/update-supplier-all.htm?&type='+type;
		$("#message").html("正在修改状态,请稍候... ...");	
		$.post(encodeURI(url),params,function(result){
			if(result.error){
				alert("修改失败"+result.message);
			}else{
				alert(result.message);
			};
			showIdentifiersDiv("flag","_update_state");
			$("#dynamicInspection").jqGrid("setGridParam").trigger("reloadGrid");
		},'json');
 		
 	}
 	function hide(obj){
 		var id = $("#dynamicInspection").jqGrid("getGridParam","selarrrow");
 		if(id.length==0){
 			alert("请选择一项！");
 			return ;
 		}
 		
 		$.post('${iqcctx}/inspection-report/hiddenState.htm?id='+id+"&&type=N",
 		function(data){
 			  window.location.reload(href='${iqcctx}/inspection-report/list.htm');
			  alert("修改成功");
 		});
	}
	/*
	下载删除附件
	*/
 	function downloadExcelFormat(){
		window.location = '${iqcctx}/inspection-report/download-excel-format.htm';
	}
 	/*---------------------------------------------------------
	函数名称:showIdentifiersDiv
	参          数:
	功          能:标识为（下拉选）
	------------------------------------------------------------*/
	function showIdentifiersDiv(idName,buttonId){
		if($("#"+idName).css("display")=='none'){
			removeSearchBox();
			$("#"+idName).show();
			var position = $("#"+buttonId).position();
			$("#"+idName).css("left",position.left+15);
			$("#"+idName).css("top",position.top+28);
		}else{
			$("#"+idName).hide();
		}
	}
	<jsp:include page="audit-method.jsp" />
	<jsp:include page="recheck-method.jsp" />
	//手动修改供应商
	function updateSupplier(){
		var rowIds = $("#dynamicInspection").jqGrid("getGridParam","selarrrow");
		if(rowIds.length==0){
			alert("请选择需要修改的数据!");
			return;
		}
		var url='${iqcctx}/inspection-report/update-supplier-input.htm?&id='+rowIds;
		$.colorbox({href:url,iframe:true, innerWidth:700, innerHeight:500,
			overlayClose:false,
			onClosed:function(){
				$("#dynamicInspection").jqGrid("setGridParam").trigger("reloadGrid");
			},
		});
	}		
	
	//手动修改物料版本
	function updateBomVersion(){
		var rowIds = $("#dynamicInspection").jqGrid("getGridParam","selarrrow");
		if(rowIds.length==0){
			alert("请选择需要修改的数据!");
			return;
		}
		var url='${iqcctx}/inspection-report/update-bom-input.htm?&id='+rowIds;
		$.colorbox({href:url,iframe:true, innerWidth:700, innerHeight:500,
			overlayClose:false,
			onClosed:function(){
				$("#dynamicInspection").jqGrid("setGridParam").trigger("reloadGrid");
			},
		});
	}
		
	</script>
	<style type="text/css">
		#flagExport{
			position:absolute; 
			display: none;
			z-index:999;
			background: #eee;
			border:3px solid #e1e1e1;
			box-shadow:0px 0px 4px #888;border-radius: 3px;margin: 0px;
		}
		#flagExport ul{ padding: 0px;margin: 0px;left: auto;width:160px;}
		#flagExport ul li{ padding: 2px 6px; list-style-type: none; width:150px;height: 20px;}
		#flagExport ul li:hover{background:#6CA412;}
		#flagExport ul li a {padding-left: 4px;color:#555;cursor:pointer}
		#flagExport ul li:hover a {padding-left: 5px;color:#fff;}
	</style>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="inspectionReport";
		var thirdMenu="myInspectionReport";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-inspection-report-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn" id="btnDiv">
					<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_INPUT_ADD">
						<button class='btn' type="button" onclick="createReport();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="iqc-delete">
						<button class='btn' type="button" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button class='btn' type="button" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="IQC_INSPECTION-REPORT_LIST_EXPORT">
						<button class='btn' type="button" onclick="iMatrix.export_Data('${iqcctx}/inspection-report/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="iqc_incomingInspectionActionsReport_update_supplier">
						<button class='btn' onclick="updateSupplier(this)" type="button">
							<span><span><b class="btn-icons btn-icons-edit"></b>修改供应商</span></span>
						</button>
					</security:authorize>
					<security:authorize ifAnyGranted="iqc_incomingInspectionActionsReport_update_bom">
						<button class='btn' onclick="updateBomVersion(this)" type="button">
							<span><span><b class="btn-icons btn-icons-edit"></b>修改物料版本</span></span>
						</button>
					</security:authorize>
					<security:authorize ifAnyGranted="IQC_UPDATE_STATE">
						<button class='btn' type="button" id="_update_state" onclick='showIdentifiersDiv("flag","_update_state");'><span><span><b class="btn-icons btn-icons-sync"></b>修改状态</span></span></button>
					</security:authorize>
					 <security:authorize ifAnyGranted="iqc_incomingInspectionActionsReport_hide">
						<button class='btn' onclick="hide(this)" type="button">
							<span><span><b class="btn-icons btn-icons-undo"></b>隐藏</span></span>
						</button>
					</security:authorize>					
				</div>
				<div id="opt-content">
					<div id="flag" onmouseover='show_moveiIdentifiersDiv("flag");' onmouseout='hideIdentifiersDiv("flag");'>
						<ul style="width:130px;">
							 <li onclick="updateState(this);" style="cursor:pointer;" title="对内">对内</li>
							 <li onclick="updateState(this);" style="cursor:pointer;" title="对外">对外</li>
						</ul>
					</div>
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="dynamicInspection"  url="${iqcctx}/inspection-report/list-datas.htm" code="IQC_IIAR" pageName="page"></grid:jqGrid>
					</form>
					<script type="text/javascript">
						$(document).ready(function(){
							if(batchNo){
								$(":input[name=batchNo]","#parameter_Table").each(function(index,obj){
									if(obj.name=='batchNo'){
										$(obj).val(batchNo);
										return false;
									}
								});
								batchNo = false;
								//showSearchDIV($("#searchBtn")[0]);
								fixedSearchSubmit('dynamicInspection', '${iqcctx}/inspection-report/list-datas.htm');
							}
						});
					</script>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
<%-- <script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script> --%>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>