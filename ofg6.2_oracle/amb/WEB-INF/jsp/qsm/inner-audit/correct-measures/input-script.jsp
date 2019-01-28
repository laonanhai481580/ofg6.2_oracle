<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">

var orderId="";
var selectIndex1="";
function selectProject(obj){
	orderId = obj.id;
	selectIndex1=obj.parentNode;
	$.colorbox({href:"${qsmctx}/defection-clause/select-list.htm",
		iframe:true, 
		width:$(window).width()<700?$(window).width()-100:900,
		height:$(window).height()<400?$(window).height()-100:600,
			overlayClose:false,
			title:"选择条款"
	});
	
}
function setProjectValue(datas){
	var idFirst = orderId.split("_")[0];
	var a=idFirst.slice(0,2);
	var b=idFirst.slice(2);
	for(var i=0;i<datas.length;i++){
		$("#" + a + b + "_clauseName").val(datas[i].clauseName);
		$("#" + a + b +  "_systemName").val(datas[i].systemName);
		b++;
		if(i!=datas.length-1){
			addRowHtml(selectIndex1);
		}
	}
}
</script>