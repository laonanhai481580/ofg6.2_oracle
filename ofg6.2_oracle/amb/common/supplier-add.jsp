<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">	
<%
String loginName=ContextUtils.getLoginName();
String passWord=ContextUtils.getPassword();
ActionContext.getContext().put("loginName",loginName);
ActionContext.getContext().put("passWord",passWord);
%>	
var tpurl='http://qis-tp.ofilm.com:9041/amb/supplier/';
var clurl='http://qis-cl.ofilm.com:9051/amb/supplier/';
var lcmurl='http://qis-lcm.ofilm.com:9061/amb/supplier/';
var ccmurl='http://qis-ccm.ofilm.com:9011/amb/supplier/';
var fpmurl='http://qis-fpm.ofilm.com:9021/amb/supplier/';
function systemCodeChange(urlMid){
	var url="";
	var urlBef='';
	var systemCode=$("#systemCode").val();
	if(systemCode=='CL'){
		urlBef=clurl;
	}else if(systemCode=='LCM'){
		urlBef=lcmurl;
	}else if(systemCode=='CCM'){
		urlBef=ccmurl;
	}else if(systemCode=='FPM'){
		urlBef=fpmurl;
	}else{
		urlBef=tpurl;
	}
	var urlAft='?type=auto&name=<%=loginName%>&pwd=<%=passWord%>';
	url=urlBef+urlMid+urlAft;
	window.location.href = encodeURI(url);
}
	
</script>