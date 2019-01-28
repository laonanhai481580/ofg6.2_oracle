<%@page import="com.norteksoft.product.util.PropUtils"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/widgets/highcharts-4.0.3/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/highcharts/modules/exporting.js"></script>
	<script type="text/javascript" src="${ctx}/js/hightchartsExport.js"></script>
	<script type="text/javascript" src="${ctx}/js/chart-view1.0.js"></script>
	<script type="text/javascript" src="${ctx}/js/chart-view-search-format1.0.js"></script>	
	<script type="text/javascript">
	<%
	String loginName=ContextUtils.getLoginName();
	String passWord=ContextUtils.getPassword();
	ActionContext.getContext().put("loginName",loginName);
	ActionContext.getContext().put("passWord",passWord);
	String cpmpanyName=PropUtils.getProp("companyName");
	ActionContext.getContext().put("cpmpanyName",cpmpanyName);
	%>	
	var cpmpanyName='<%=cpmpanyName%>';
	window.chartView.initInputEditType = function(){
		$(":input[isDate]").each(function(index,obj){
			var formatStr = $(obj).attr("dateFormat");
			if(!formatStr){
				formatStr = "yy-mm-dd";
			}
			$(obj).datepicker({changeYear:true,changeMonth:true,showButtonPanel: true,dateFormat:formatStr});
		});
		var btnDiv=$("#btnDiv");
		if(cpmpanyName=='CL'){
			$(btnDiv).append("<div style='float:right;margin-right:8px;'><select name='systemCode' style='width:80px;' onchange='systemCodeChange(this);'><option value='CL' selected='selected'>CL</option><option value='TP'>TP</option><option value='LCM' >LCM</option><option value='CCM'>CCM</option><option value='FPM'>FPM</option></select></div>");
		}else if(cpmpanyName=='LCM'){
			$(btnDiv).append("<div style='float:right;margin-right:8px;'><select name='systemCode' style='width:80px;' onchange='systemCodeChange(this);'><option value='LCM' selected='selected'>LCM</option><option value='TP'>TP</option><option value='CL' >CL</option><option value='CCM'>CCM</option><option value='FPM'>FPM</option></select></div>");
		}else if(cpmpanyName=='CCM'){
			$(btnDiv).append("<div style='float:right;margin-right:8px;'><select name='systemCode' style='width:80px;' onchange='systemCodeChange(this);'><option value='CCM' selected='selected'>CCM</option><option value='TP'>TP</option><option value='LCM' >LCM</option><option value='CL'>CL</option><option value='FPM'>FPM</option></select></div>");
		}else if(cpmpanyName=='FPM'){
			$(btnDiv).append("<div style='float:right;margin-right:8px;'><select name='systemCode' style='width:80px;' onchange='systemCodeChange(this);'><option value='FPM' selected='selected'>FPM</option><option value='TP'>TP</option><option value='LCM' >LCM</option><option value='CCM'>CCM</option><option value='CL'>CL</option></select></div>");
		}else{
			$(btnDiv).append("<div style='float:right;margin-right:8px;'><select name='systemCode' style='width:80px;' onchange='systemCodeChange(this);'><option value='TP' selected='selected'>TP</option><option value='CL'>CL</option><option value='LCM' >LCM</option><option value='CCM'>CCM</option><option value='FPM'>FPM</option></select></div>");
		}						
	};
	function systemCodeChange(){
		var url="";
		var systemCode=$("select[name=systemCode]").val();
		if(systemCode=='CL'){
			 url='http://qis-cl.ofilm.com:9051/amb/supplier/data-acquisition/incoming-close-rate-sqe.htm?type=auto&name=<%=loginName%>&pwd=<%=passWord%>';
		}else if(systemCode=='LCM'){
			url='http://qis-lcm.ofilm.com:9061/amb/supplier/data-acquisition/incoming-close-rate-sqe.htm?type=auto&name=<%=loginName%>&pwd=<%=passWord%>';
		}else if(systemCode=='CCM'){
			url='http://qis-ccm.ofilm.com:9011/amb/supplier/data-acquisition/incoming-close-rate-sqe.htm?type=auto&name=<%=loginName%>&pwd=<%=passWord%>';
		}else if(systemCode=='FPM'){
			url='http://qis-fpm.ofilm.com:9021/amb/supplier/data-acquisition/incoming-close-rate-sqe.htm?type=auto&name=<%=loginName%>&pwd=<%=passWord%>';
		}else{
			 url='http://qis-tp.ofilm.com:9041/amb/supplier/data-acquisition/incoming-close-rate-sqe.htm?type=auto&name=<%=loginName%>&pwd=<%=passWord%>';
		}
		window.location.href = encodeURI(url);
	}
	</script>	
</head>
<!-- 进料异常结案率推移图-->
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<chart:view 
		chartDefinitionCode="supplier-incoming-close-sqe"
		secMenu="analysis"
		secMenuJspPath="/menus/supplier-sec-menu.jsp"
		thirdMenu="incoming_close_sqe"
		thirdMenuJspPath="/menus/supplier-analysis-third-menu.jsp"/>
</body>
</html>	

