<%@ page contentType="text/html;charset=UTF-8" import="com.norteksoft.product.util.ContextUtils,java.text.SimpleDateFormat,java.util.Calendar,com.norteksoft.product.util.SystemUrls"%>
<%@ include file="/common/taglibs.jsp"%>
<%@page import="java.util.Locale"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%  
Locale currentLocale = ActionContext.getContext().getLocale();
String weekDay = null;
SimpleDateFormat fmt = null;
Calendar cal = Calendar.getInstance();
if(Locale.US.equals(currentLocale)){
	fmt = new SimpleDateFormat("yyyy-MM-dd");
	int day = cal.get(Calendar.DAY_OF_WEEK);
	switch(day){
		case Calendar.MONDAY: weekDay="Monday";break;
		case Calendar.TUESDAY: weekDay="Tuesday";break;
		case Calendar.WEDNESDAY: weekDay="Wednesday";break;
		case Calendar.THURSDAY: weekDay="Thursday";break;
		case Calendar.FRIDAY: weekDay="Friday";break;
		case Calendar.SATURDAY: weekDay="Saturday";break;
		case Calendar.SUNDAY: weekDay="Sunday";break;
	}
}else{
	fmt = new SimpleDateFormat("yyyy年MM月dd日");
	int day = cal.get(Calendar.DAY_OF_WEEK);
	switch(day){
		case Calendar.MONDAY: weekDay="星期一";break;
		case Calendar.TUESDAY: weekDay="星期二";break;
		case Calendar.WEDNESDAY: weekDay="星期三";break;
		case Calendar.THURSDAY: weekDay="星期四";break;
		case Calendar.FRIDAY: weekDay="星期五";break;
		case Calendar.SATURDAY: weekDay="星期六";break;
		case Calendar.SUNDAY: weekDay="星期日";break;
	}
}
%>

<div id="header" class="ui-north">
	<menu:firstMenu showNum="4"></menu:firstMenu>
	<div id="header-logo">
	</div>
	<div id="honorific">
		<%-- <span><span class="man">&nbsp;</span><%=ContextUtils.getHonorificTitle()%>, 您好!</span>
		<span><span class="day">&nbsp;</span><%=fmt.format(cal.getTime())+"  "+weekDay%></span>
		<span><span class="pemessage"></span><a href="#"  onclick="openMessage();" >个人消息</a></span>
		<span onclick="updatePassword();"><a href="#"><span class="password">&nbsp;</span>修改密码</a></span>  
		<span onclick="updateEmail();"><a href="#"><span class="password">&nbsp;</span>修改邮箱</a></span>
		<span onclick="changeStyle(event, this);"><a href="#"><span class="theme">&nbsp;</span>换肤</a></span>  --%>
		<%
		if(Locale.US.equals(currentLocale)){
		%> 
		<span><span class="man">&nbsp;</span><%=ContextUtils.getHonorificTitle()%>, hello!</span>
		<span><span class="day">&nbsp;</span><%=fmt.format(cal.getTime())+"  "+weekDay%></span>
		<%--<span><span class="pemessage"></span><a href="#"  onclick="openMessage();" >个人消息</a></span> --%>
		<span onclick="updatePassword();"><a href="#"><span class="password">&nbsp;</span>Change password</a></span>  
		<span onclick="updateEmail();"><a href="#"><span class="password">&nbsp;</span>Change E-mail</a></span>
		<span onclick="changeStyle(event, this);"><a href="#"><span class="theme">&nbsp;</span>Change skin</a></span> 
		<span onclick="changeLanague('zh_CN');"><a href="#" hidefocus="true"><span class="lanagueCn">&nbsp;</span>中文</a></span>
		<span ><a href="${ctx}/j_spring_security_logout"><span class="exit">&nbsp;</span>Exit</a></span> 
		<%}else{ %>
		<span><span class="man">&nbsp;</span><%=ContextUtils.getHonorificTitle()%>, 您好!</span>
		<span><span class="day">&nbsp;</span><%=fmt.format(cal.getTime())+"  "+weekDay%></span>
		<%--<span><span class="pemessage"></span><a href="#"  onclick="openMessage();" >个人消息</a></span> --%>
		<span onclick="updatePassword();"><a href="#"><span class="password">&nbsp;</span>修改密码</a></span>  
		<span onclick="updateEmail();"><a href="#"><span class="password">&nbsp;</span>修改邮箱</a></span>
		<span onclick="changeStyle(event, this);"><a href="#"><span class="theme">&nbsp;</span>换肤</a></span> 
		<span onclick="changeLanague('en_US');"><a href="#" hidefocus="true"><span class="lanagueEn">&nbsp;</span>English</a></span>
		<span ><a href="${ctx}/j_spring_security_logout"><span class="exit">&nbsp;</span>退出</a></span> 
		<%} %>
<%-- 		<span ><a href="${ctx}/j_spring_security_logout"><span class="exit">&nbsp;</span>退出</a></span>  --%>
	</div>
</div>
<script>
function updatePassword(){
	window.open("<%=SystemUrls.getBusinessPath("acs")%>/organization/user!updateUserPassword.action",'',"top=300,left=400,toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,width=400,height=300");
}
function updateEmail(){
	var url = "<%=SystemUrls.getBusinessPath("amb")%>/carmfg/common/update-user-email.htm";
	$.colorbox({
		href:url,
		iframe:true, 
		width:400, 
		height:400,
		overlayClose:false,
		title:"修改邮箱"
	});
}
function changeLanague(a){
	//跨域调用平台的切换语言
	var imatrixUrl = '${imatrixCtx}/acs/organization/organization-change-lanague.htm';
	imatrixUrl+="?lanague="+a;
	imatrixUrl += "&url=";
	$.getScript(imatrixUrl);
	//调用amb的替换
	var b='<form id="saveLanguageForm" name="saveThemeForm" action="${supplierctx}/common/gateway/organization-change-lanague.htm" method="post"><input type="hidden" id="lanague" name="lanague" value="'+a+'"/><input type="hidden" id="_language_url" name="url" value="'+window.location.href+'"/></form>';
	$("#header").before(b);
	$("#saveLanguageForm").submit();
}
</script>
<html>
<head>
  <meta charset="utf-8" />
  <style type="text/css">
      html, body {
          margin: 0px;
		  
      }
	  #div_quality {
			position:absolute;
			width:430px;
			height:45px;
			z-index:100; //如果被东西遮挡 就在把这个值设置高
			left: 400px;
			top: 10px;
		
		}
   .quality{
        margin:auto;
        font-family:"'Microsoft YaHei','微軟正黑體',SimSun";
        font-size: 16pt;
        font-weight:bold;
        color:red;
        width:450px;
        height:50px;
        line-height:50px;
        vertical-align:middle;
        text-align:center;
		
         /*
        border:1px solid lightgray;
        -moz-border-radius:5px;
        -webkit-border-radius:5px;
        border-radius: 5px;

        -moz-box-shadow: 3px 3px 4px #808080;
        -webkit-box-shadow: 3px 3px 4px #808080;
        box-shadow: 3px 3px 4px #808080;
        */
        text-shadow:3px 2px 3px lightgray;

	}
	
  </style>
</head>
<body onload="javascript: showTip();">
<marquee direction="left" scrollamount="6" onmouseover="this.stop()" onmouseout="this.start()"
  <div id="div_quality" class="quality" style="float:left;left:15%">
      质量方针--追求卓越品质，提供完美服务，持续改进，客户100%满意！      社会责任安全方针--遵守社会责任承诺，关怀员工健康安全，维护职工合法权益，节能防污持续改善！
  </div>
</marquee>

<script type="text/javascript">
  function showTip()
  {
    var statmentArr= new Array();
   /*  statmentArr[0]="市场如水，企业如舟，质量象舵，人是舵手";
    statmentArr[1]="未来的成功属于质量领先者";
    statmentArr[2]="马虎是追求品质最大的障碍";
    statmentArr[3]="深化质量管理，提高产品质量";
    statmentArr[4]="质量up，成本down，占领市场有希望"; */
    statmentArr[0]="质量方针--追求卓越品质，提供完美服务，持续改进，客户100%满意！      社会责任安全方针--遵守社会责任承诺，关怀员工健康安全，维护职工合法权益，节能防污持续改善！";
	//var index = (new Date()).getDay();
	//var theDate = new Date(2017, 5, 22);
	var theDate = new Date();
	//每周显示不同的内容
	var index = theDate.getWeekOfMonth();
	if (index < statmentArr.length){
        var x = document.getElementById("div_quality");
        x.innerHTML = statmentArr[index];
	}
  };
  
  // 计算当前日期在本年度的周数  
  Date.prototype.getWeekOfYear = function(weekStart) { // weekStart：每周开始于周几：周日：0，周一：1，周二：2 ...，默认为周日  
    weekStart = (weekStart || 0) - 0;  
    if(isNaN(weekStart) || weekStart > 6)  
        weekStart = 0;    
  
    var year = this.getFullYear();  
    var firstDay = new Date(year, 0, 1);  
    var firstWeekDays = 7 - firstDay.getDay() + weekStart;  
    var dayOfYear = (((new Date(year, this.getMonth(), this.getDate())) - firstDay) / (24 * 3600 * 1000)) + 1;  
    return Math.ceil((dayOfYear - firstWeekDays) / 7) + 1;  
	} ; 
  
  // 计算当前日期在本月份的周数  
  Date.prototype.getWeekOfMonth = function(weekStart) {  
    weekStart = (weekStart || 0) - 0;  
    if(isNaN(weekStart) || weekStart > 6)  
        weekStart = 0;  
  
    var dayOfWeek = this.getDay();  
    var day = this.getDate();  
    return Math.ceil((day - dayOfWeek - 1) / 7) + ((dayOfWeek >= weekStart) ? 1 : 0);  
};  

</script>
</body>
</html>