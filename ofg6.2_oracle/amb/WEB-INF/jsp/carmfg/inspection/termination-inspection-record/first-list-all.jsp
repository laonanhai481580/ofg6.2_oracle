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
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
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
		if(cellvalue){
			return "<a href='javascript:void(0);' onclick='callList("+rowObject.id+")'> "+cellvalue+"</a>";
		}else{
			return "";
		}			
	}
	function callList(id){
		myId = id;
		$.colorbox({href:'${mfgctx}/inspection/made-inspection/view-info.htm?id='+id,iframe:true,
			innerWidth:$(window).width()-100, 
			innerHeight:$(window).height()<800?$(window).height()-50:800,
			overlayClose:false,
			title:"报告详情"
		});
	}
	var dutySupplierId = null;
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
	function hide(obj){
 		var id = $("#dynamicInspection").jqGrid("getGridParam","selarrrow");
 		if(id.length==0){
 			alert("请选择一项！");
 			return ;
 		}
 		
 		$.post('${mfgctx}/inspection/made-inspection/hiddenState.htm?id='+id+"&&type=N",
 		function(data){
 			  window.location.reload(href='${mfgctx}/inspection/made-inspection/first-list-all.htm');
			  alert("修改成功");
 		});
	}
	
	function creatReport(obj){
 		var id = $("#dynamicInspection").jqGrid("getGridParam","selarrrow");
 		if(id.length==0){
 			alert("请选择一项！");
 			return ;
 		}
 		if(id.length>1){
			alert("只能选择一项!");
			return ;
		}
 		window.open('${mfgctx}/inspection/made-inspection/input.htm?copyId='+id);
	}	
	
	function SaveAsFile(){
		var id = $("#dynamicInspection").jqGrid("getGridParam","selarrrow");
		if(id.length==0){
 			alert("请选择一项！");
 			return ;
 		}
 		if(id.length>1){
			alert("只能选择一项!");
			return ;
		}
		window.location.href = '${mfgctx}/inspection/made-inspection/download-report.htm?id='+id;
		return;
		LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));  
		LODOP.PRINT_INIT("进货检验表单打印");
		var strBodyStyle="<style>table { border: 1 solid #000000;border-collapse:collapse }</style>";
		var strFormHtml=strBodyStyle+"<body>"+document.getElementById("inspectionForm").innerHTML+"</body>";

		LODOP.SET_PRINT_STYLE("FontSize",12);
		LODOP.SET_PRINT_STYLE("Bold",1);
		LODOP.ADD_PRINT_TABLE(0,0,"100%","100%",strFormHtml);

		LODOP.SET_SAVE_MODE("QUICK_SAVE",true);//快速生成（无表格样式,数据量较大时或许用到）
		LODOP.SAVE_TO_FILE("进货检验报告.xlsx");
	}	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu='data_list';
		var thirdMenu="firstInspectionRecord";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-inspection-list-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
			<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn" id="btnDiv">
					<security:authorize ifAnyGranted="termination-inspection-record-input">
					<button class='btn' onclick="window.location='${mfgctx}/inspection/made-inspection/input.htm'" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="termination-inspection-first-record-delete">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="MFG_INSPECTION_AUDIT">
					<button class='btn' onclick="reCheckList();" type="button"><span><span><b class="btn-icons btn-icons-cancel"></b>重新检验</span></span></button>
					</security:authorize>
					<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="termination-inspection-first-record-export">
					<button  class='btn' onclick="iMatrix.export_Data('${mfgctx}/inspection/termination-inspection-record/export-first-list-all.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出台账</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="mfg-inspection-made-inspection-export-report">
					  <button class='btn' onclick="SaveAsFile();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出表单</span></span></button>
					</security:authorize> 					
					<security:authorize ifAnyGranted="termination-inspection-record-input">
					<button class='btn' onclick="creatReport();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>生成检验报告</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="mfg_inspection_hide">
						<button class='btn' onclick="hide(this)" type="button">
							<span><span><b class="btn-icons btn-icons-undo"></b>隐藏</span></span>
						</button>
					</security:authorize>
					</div>
					<div id="opt-content">
						<form id="contentForm" name="contentForm" method="post" action="">
							<grid:jqGrid gridId="dynamicInspection"  url="${mfgctx}/inspection/termination-inspection-record/first-list-all-datas.htm" code="MFG_FIRST_INSPECTION_ALL" pageName="page"></grid:jqGrid>
						</form>
					</div>
				</aa:zone>
			</div>
	</div>
	
</body>
</html>