<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="${ctx}/widgets/highcharts-4.0.3/highcharts.js"></script>
<script type="text/javascript" src="${ctx}/widgets/highcharts-4.0.3/highcharts-3d.js"></script>
<script type="text/javascript" src="${ctx}/widgets/highcharts-4.0.3/modules/exporting.js"></script>
<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<style>
<!--
	#searchUl{
		margin:0px;
		padding:0px;
	}
	.searchli{
		float:left;
		width:280px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	.searchli select{
		width:182px;
	}
	.input{
		width:178px;
	}
	.label{
		float:left;
		width:85px;
		text-align:right;
		padding-right:2px;
	}
	.tableHeader{
		font-weight:bold;
	}
	.totalItem{
		background:#FF9933;
	}
	.item{
		background:#FFFF00;
	}
	.itemDetail{
		background:##FCFCFC;
	}
	.itemTarget{
		background:#CCCCCC;
		color:#CC6600;
	}
-->
</style>
<script type="text/javascript">
function contentResize(){
	if(cacheResult != null){
		createReport(cacheResult);
		loadTable();
	}
}
var cacheResult = null;
$(document).ready(function(){
	contentResize();
	$(":input[isDate]").datepicker({"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
	//初始化多选框
	var ids = ['terrains'];
	for(var i=0;i<ids.length;i++){
		var $select = $("#" + ids[i]).attr("isMulti","true");
		$select.multiselect({
			noneSelectedText: "请选择...",
			checkAllText: "全选",
	        uncheckAllText: '全不选',
			selectedList:2
		});
		$select.multiselect("uncheckAll");
	}
	//处理多选框变形的问题
	var width = $(".ui-layout-center").find("button.ui-multiselect").width();
	$(".ui-layout-center").find("ul.ui-multiselect-checkboxes").width(width);
	//绑定滚动事件
	$("#opt-content").scroll(function(){
		updateTableWidth();
	});
	search();
});
var chart = null;
 //创建报告
function createReport(result){
	if(chart != null){
		chart.destroy();
		chart = null;
	}
	var width = $("#btnDiv").width()-10;
	chart = new Highcharts.Chart({
		colors: ['#FFCC00', '#99CC00', '#66CC99', '#f7a35c', '#8085e9', 
		         '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'],
		exporting : {
			enabled : false
		},
		chart: {
			renderTo: "reportDiv",
			width : width,
			type : 'column'
		},
		credits: {
	         enabled: false
		},
		title: {
			style : {
				"color": '#3E576F',
				"font-size": '16px'
			},
			text: result.title
		},
		xAxis: {
			categories: result.categories,
			gridLineDashStyle : 'ShortDashDot',
			gridLineWidth: 1
		},
		yAxis: [{
			title: {
				text: ''
			},
	        gridLineWidth : 0,
			min : 0
		}],
		legend: {
	         backgroundColor: '#FFFFFF',
	         x : 6,
	         y : 0
	    },
		plotOptions: {
			column: {
                stacking: 'normal',
                shadow : true
            },
	        spline : {
	            shadow : false,
	            dataLabels: {
	               enabled: true,
	               formatter : function(){
	            		   return this.y;
	               }
	            }
	        },series: {
                showInLegend: false
            }
	    },
		tooltip: {
	   		 formatter: function() {
				if (this.series.type=="column") {
					var html = '';
					if(this.x){
						html += '<b>'+ this.x +'</b><br/>';
					}
					html += this.series.name +': '+ this.y +'<br/>';
					html += '合计:'+ this.point.stackTotal;
					return html;
				}else{
					var html = "<b>" + this.series.name + ":</b>";
						html += this.y;
					return html;	
				}
			}
		},
		series:result.series
	});
} 
//确定的查询方法
function search(){
	var date1 = $("#datepicker1").val();
	var date2 = $("#datepicker2").val();
	if( date2!=""&&date1!=""&&date1>date2){
		alert("检验日期前后有误，请重新设置！");
	}else{
		reportByParams(getParams());
	}
}	
//根据参数获取数据
function reportByParams(params){
	$.showMessage("正在查询,请稍候... ...","custom");
	$.post("${mfgctx}/data-acquisition/analyse/analysis-list-datas.htm",params,function(result){
		$.clearMessage();
		if(result.error){
			alert(result.message);
		}else{
			createReport(result);
			loadTable();
			cacheResult = result;
		}
	},'json');
}
//获取表单的值
function getParams(){
	var params = {};
	$(".ui-layout-center :input[name]").each(function(index,obj){
		var jObj = $(obj);
		if(obj.type == 'radio'){
			if(obj.checked){
				params[obj.name] = jObj.val();
			}
		}else if(obj.type=='select-one'&&'true'==$(obj).attr("isMulti")){//
			var values = $(obj).multiselect("getChecked").map(function(){
			   	return this.value;	
			}).get();
			if(values&&values.length>0){
				value = values.join(",");
				params[obj.name] = value;
			}
		}else if(jObj.val()){
			params[obj.name] = jObj.val();
		}
	});	
	return params;
}
//处理子表浮动窗口位置
function updateTableWidth(){
	var contentP = $("#tableContent").position();
	if(!contentP){
		return;
	}
	$("#tableContent").css({
		"margin-top":($("#tableHeaderDiv").height()-1) + "px"
	});
	var p = $("#opt-content").position();
	var width = $(".ui-layout-center").width()-25;
	var top = contentP.top;
	if(top < 35){
		top = 35;
	}
	$("#tableHeaderDiv").css({
		"left":p.left+9,
		"top":top+"px",
		"width":width+"px"
	});
	//底部位zhi
	//底部与头部一致
	$("#tableFooter").width($("#tableHeader").width());
	var footerTop = $("#opt-content").height()+20;
	if(footerTop<top){
		$("#tableFooterDiv").hide();
	}else{
		$("#tableFooterDiv").show();
	}
	$("#tableFooterDiv").css({
		"left":p.left+9,
		"top":footerTop+"px",
		"width":width+"px"
	});
}
//加载表格
function loadTable(){
	var width = $(".ui-layout-center").width();
	$("#tableDiv").width(width-30);
	var url = "${mfgctx}/data-acquisition/analyse/oqc-trend-table.htm?width=" + (width-10);
	$("#tableDiv").html("正在加载表格数据,请稍候'... ...");
	$("#tableDiv").load(url,function(){
		//合并单元格
		customRowSpan();
		//绑定左右滚动事件
		$("#tableFooterDiv").scroll(function(){
			$("#tableHeaderDiv").scrollLeft($(this).scrollLeft()); 
			$("#tableDiv").scrollLeft($(this).scrollLeft()); 
		});
		updateTableWidth();
	});
}
//合并单元格
/*---------------------------------------------------------
函数名称:customRowSpan
参          数:
	gridId	表格的ID
	gridId	表格的ID
	gridId	表格的ID
功          能:自定义合并单元格
------------------------------------------------------------*/
function customRowSpan(){
	setTimeout(function(){
		var lastFlag = 0,rowSpanIds=[];
		var $trs = $("#tableContent").find("tr");
		$trs.each(function(index,obj){
			var flag = $(obj).find("td").first().html();
			var id = $(obj).attr("id");
			if(flag != lastFlag){
				_customRowspan(rowSpanIds);
				rowSpanIds = [];
			}
			rowSpanIds.push(id);
			lastFlag = flag;
		});
		_customRowspan(rowSpanIds);
	},100);
}

function factoryChange(obj){
	var factory=$("#factory").val();
	var url = "${mfgctx}/base-info/factory-procedure/procedure-select.htm?factory="+factory;
	$.post(encodeURI(url),{},function(result){
			if(result.error){
				alert(result.message);
			}else{
			var procedures = result.procedures;
			var procedureArr = procedures.split(",");
			var procedure = document.getElementById("procedure");
			procedure.options.length=0;
			var opp1 = new Option("","");
			procedure.add(opp1);
				for(var i=0;i<procedureArr.length;i++){
					var opp = new Option(procedureArr[i],procedureArr[i]);
					procedure.add(opp);
				}
			}
		},'json');
}

function _customRowspan(rowSpanIds){
	if(rowSpanIds.length<2){
		return;
	}
	var firstRow = $("#tableContent").find("tr[id="+rowSpanIds[0]+"]");
	firstRow.find("td").first().attr("rowSpan",rowSpanIds.length);
	for(var i=1;i<rowSpanIds.length;i++){
		var row = $("#tableContent").find("tr[id="+rowSpanIds[i]+"]");
		row.find("td").first().remove();
	}
}
//清除表格
function clearTable(){
	if(detailTable != null){
		detailTable.GridDestroy();
		detailTable = null;
	}
	$("#detailTableDiv_parent").html("<table id=\"detail_table\"></table>");
}
function clearAll(){
	$("#datepicker1").val("");
	$("#datepicker2").val("");
	document.myform.reset();
}
function addValue(api){
	var currentClickNodeData = api.single.getCurrentClickNodeData();
	var department = $.parseJSON(currentClickNodeData);
	$("#departmentCode").val(department.code);
	$("#departmentTo").val(department.name);
}
function pageSizeChange(obj){
	if(!obj.value||isNaN(obj.value)){
		obj.value = 10;
		alert("无效的数字!");
		return false;
	}
}
//导出趋势图到Exel
function exportCharts(){
	if(chart==null){
		alert("统计图不能为空!");
		return;
	}
	var svg = chart.getSVG();
	var url = "${mfgctx}/data-acquisition/analyse/export-charts.htm";
	$.showMessage("正在生成导出文件,请稍候... ...","custom");
	var params = {
		width : $("#reportDiv").width(),
		height : $("#reportDiv").height(),
		svg : svg
	};
	$.post(url,params,function(result){
		$.clearMessage();
		if(result.error){
			alert(result.message);
		}else{
			var flagStrs = result.flagStrs;
			var downloadUrl = "${ctx}/portal/export-data.action?fileName=" + flagStrs;
			window.location = encodeURI(downloadUrl);
		}
	},'json');
}
</script>
