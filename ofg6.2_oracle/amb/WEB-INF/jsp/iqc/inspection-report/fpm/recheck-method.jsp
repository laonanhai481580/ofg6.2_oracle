<%@page import="java.util.Date"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@page import="com.ambition.iqc.entity.IncomingInspectionActionsReport"%>
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
function reCheck(obj){
	if(!obj){
		var ids = $("#dynamicInspection").jqGrid("getGridParam","selarrrow");
		if(ids.length==0){
			alert("请选择需要重检的数据!");
			return;
		}
		if(ids.length>1){
			alert("一次只能重检一条数据!");
			return;
		}
		obj = $("#dynamicInspection").jqGrid("getRowData",ids[0]);
		obj.id = ids[0];
	}
	if(obj.inspectionState=='待检验'){
		alert("检验报告尚未提交，请先提交！");
		return;
	}
	var results = ${processingResults};
	var reportId = obj.id;
	var checkBomName = obj.checkBomName;
	var supplierName=obj.supplierName;
	var appearanceAmount=obj.appearanceAmount;
	var appearanceUnAmount=obj.appearanceUnAmount;
	var appearanceAmountRate=obj.appearanceAmountRate;
	var qualifiedAmount=obj.qualifiedAmount;
	var unqualifiedAmount = obj.unqualifiedAmount;
	var qualifiedRate = obj.qualifiedRate;
	var stockAmount = obj.stockAmount;
	var html = '<div id="auditBody" class="opt-body" style="overflow-y:auto;"><div class="opt-btn" style="line-height:30px;">'
	+'<button style="margin-left:4px;" class="btn" type="button" onclick="reCheckResult();"><span><span><b class="btn-icons btn-icons-cancel"></b>重新检验</span></span></button>'
	+'<span id="copyMessage" style="color:red;padding-left:4px;"></span></div>';
	html += '<form id="auditForm"><table class="form-table-border-left" style="margin:4px;width:98%;">';
	html += '<tr height=29><td style="width:20%;">检验物料</td><td  style="width:10%;"><input value="'+reportId+'" type="hidden" name="id"></input><input value="'+checkBomName+'" disabled="disabled"></input></td>';
	html += '<td style="width:20%;">供应商</td><td style="width:15%;"><input name="supplierName" value="'+supplierName+'" disabled="disabled"></input></td>';
	html += '<td style="width:20%;">进料数量</td><td style="width:15%;"><input name="stockAmount" value="'+stockAmount+'" disabled="disabled"></input></td></tr>';
	html += '<tr><td>外观合格数</td><td><input name="appearanceAmount" value="'+appearanceAmount+'"  disabled="disabled"/></td>';
	html += '<td>外观不良数</td><td><input name="appearanceUnAmount" value="'+appearanceUnAmount+'"  disabled="disabled"/></td>';
	html += '<td>外观合格率</td><td><input name="appearanceAmountRate" value="'+appearanceAmountRate+'"  disabled="disabled"/></td></tr>';
	html += '<tr><td>特性合格数</td><td><input name="qualifiedAmount" value="'+qualifiedAmount+'" disabled="disabled"/></td>';
	html += '<td>特性不良数</td><td><input name="unqualifiedAmount" value="'+unqualifiedAmount+'" disabled="disabled"/></td>';
	html += '<td>特性合格率</td><td><input name="qualifiedRate" value="'+qualifiedRate+'" disabled="disabled"/></td></tr>';
	html += '<tr><td>重检意见</td><td colspan="5"><textarea id="recheckText" name="recheckText" rows="3" cols="3"></textarea></td></tr>';
	html += '</table></form>';
	html += '提示:<font style="color:blue;">接收数量+拒收数量<b>\<</b>来料数量时将拆分成两张入库单!</font></div>';
	var height = $(window).height()<450?$(window).height()-50:450;
	var width = $(window).width()-200>750?750:$(window).width();
	$.colorbox({
		title : '审核检验单',
		html : html,
		iframe:false,
		height : height,
		width:width,
		onComplete:function(){
			$("#auditBody").height(height-50);
			$("#auditForm").validate({});
			$("#auditForm :input[name=receiveQty]").bind("keyup",function(){
				var stockAmount = parseFloat($("#auditForm :input[name=stockAmount]").val());
				var receiveQty = 0;
				if(!isNaN(this.value)){
					receiveQty = parseFloat(this.value);
				}
				$("#auditForm :input[name=badQty]").val(stockAmount-receiveQty);
			});
		}
	});
}
function reCheckResult(){
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
		var str = '';
		$("#auditBody").find("button").attr("disabled","");
		var recheckText=$("#recheckText").val();
		if(!recheckText){
			alert("重检意见不能为空！");
			return;
		}
		if(confirm("确定要重新检验吗?")){
		$.colorbox.close();
			$("#message").html("正在提交请求,请稍候... ...");
			$.post("${iqcctx}/inspection-report/fpm/re-check.htm",params,function(result){
				if(result.error){
					$("#message").html("<b style='color:red;'>"+result.message+"</b>");
					alert(result.message);				
				}else{
					$("#message").html("<b style='color:red;'>操作成功!</b>");
					if($("#dynamicInspection").length>0){
						$("#dynamicInspection").trigger("reloadGrid");
					}else{
						window.location.href = '${iqcctx}/inspection-report/fpm/input.htm?id=${id}';
					}
				}
			},'json');
		}
	};
}