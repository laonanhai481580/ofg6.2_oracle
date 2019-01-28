<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">	
<%
String companyName=ContextUtils.getCompanyName();
String webRoot="";
	if(companyName.equals("欧菲-TP")){
		webRoot="http://qis-tp.ofilm.com:9040/imatrix/task";
	}else if(companyName.equals("欧菲-CL事业群")){
		webRoot="http://qis-cl.ofilm.com:9050/imatrix/task";
	}else if(companyName.equals("欧菲-LCM")){
		webRoot="http://qis-lcm.ofilm.com:9060/imatrix/task";
	}else if(companyName.equals("欧菲科技-CCM")){
		webRoot="http://qis-ccm.ofilm.com:9010/imatrix/task";
	}else if(companyName.equals("欧菲科技-FPM")){
		webRoot="http://qis-fpm.ofilm.com:9020/imatrix/task";
	}else if(companyName.equals("欧菲科技-神奇工场")){
		webRoot="http://qis-sq.ofilm.com:9030/imatrix/task";
	}
	ActionContext.getContext().put("webRoot",webRoot);
%>
var webRoot='<%=webRoot%>';
function afterTB_Remove(){
	var strs = location.href.replace('#', '').split('/');
	var href0 = strs[strs.length-1];
	if(href0 === 'task!completedTasks.htm'){
		window.location = webRoot + "/task/task!completedTasks.htm";
	}else if(href0 === 'task.htm'){
		window.location = webRoot + "/task/task.htm";
	}
};
//function task_box(taskid, url){
//	url = processUrl(url+taskid);
//	var windowname = window.open(url,'windowname',"top=0,left=0,toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=no,width="+screen.availWidth+",height="+screen.availHeight);
//	$('body').everyTime('3s','timer'+taskid,function(){
//		if(windowname.closed){
//			$('body').stopTime('timer'+taskid);
//			taskList();
//		}
//	},0,true);
//}

function openInput(taskId){
	var screenWidth=screen.availWidth-12;
	var screenHeight=screen.availHeight-58;
	var win = window.open(webRoot+'/task/task!input.htm?id='+taskId,'win',"top=0,left=0,toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=false,resizable=no,width="+screenWidth+",height="+screenHeight);
	var time=(new Date()).getTime();
	$('body').everyTime('2s','timer'+time,function(){
		if(win.closed){
			$('body').stopTime('timer'+time);
			$("#taskTableId").trigger("reloadGrid");
			taskList();			
        }
	},0,true);
}

//function task_box_complete(taskid,url){
//	url = processUrl(url+taskid);
//	var childWin = window.open(url,'',"top=0,left=0,toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,width="+screen.availWidth+",height="+screen.availHeight);
//}

//根据系统code获取url
function processUrl(url){
	if(url.indexOf("http://")!=-1){
		return url;
	}else{
		var index = url.indexOf("/");
		var code = url.substring(0, index);
		var obj = $("#"+code).attr('id')!=undefined ? $("#"+code) : $("#"+code.toUpperCase());
		url = $($(obj).children()[0]).attr('href') + url.substring(index, url.length);;
		return url;
	}
}
function optSubMenu(obj, menu){
	if(menu == 'new'){
		window.location = webRoot + "/task/task.htm";
	}else if(menu == 'completed'){
		window.location = webRoot + "/task/task!completedTasks.htm";
	}
}


function showErrorPageNo(){};
function changePage(pageNo){};

function isUndoneTask(opts){
	if(opts.active==4 ||opts.active==0 ||opts.active==1)return true;
	return false;
}

function shouldBlod(opts){
	if(!opts.read&&isUndoneTask(opts))return true;
	return false;
}
/*---------------------------------------------------------
函数名称:fmtTitle
参          数:无
功          能:格式化列表中的“任务名称”字段
------------------------------------------------------------*/
function fmtTitle(ts1,cellval,opts,rwdat,_act){
	var name="";
	if(opts.taskMark=="CANCEL"){
		name="white";
	}else if(opts.taskMark=="PURPLE"){
		name="purple";
	}else if(opts.taskMark=="ORANGE"){
		name="orange";
	}else if(opts.taskMark=="GREEN"){
		name="green";
	}else if(opts.taskMark=="YELLOW"){
		name="yellow";
	}else if(opts.taskMark=="BLUE"){
		name="blue";
	}else if(opts.taskMark=="RED"){
		name="red";
	}
	var v ='';
	var value = replaceSpecialSymbol(ts1);
	if(opts.transferName!="&nbsp;"&&typeof(opts.transferName)!="undefined"&&opts.transferName!=""){//移交人不为空，表示是接收的任务
		if(opts.trustorName!="&nbsp;"&&typeof(opts.trustorName)!="undefined"&&opts.trustorName!=""){
			value = "<span style='color:#CC0000;'>（"+opts.transferName+"移交于"+opts.trustorName+","+opts.trustorName+"委托）"+value+"</span>";
		}else{
			value = "<span style='color:#CC0000;'>（"+opts.transferName+"移交）"+value+"</span>";
		}
	}else{
		if(opts.trustorName!="&nbsp;"&&typeof(opts.trustorName)!="undefined"&&opts.trustorName!=""){
			value = "<span style='color:#CC0000;'>（"+opts.trustorName+"委托）"+value+"</span>";
		}
	}
	if(opts.read == 'true'){
		v ="<a  href=\"#\" hidefocus=\"true\" onclick=\"_click_fun("+opts.id+");\">" +value + "</a>";
	}else if(opts.read == 'false'){
		v="<a  href=\"#\" hidefocus=\"true\" onclick=\"_click_fun("+opts.id+");\" style=\"font-weight:bold;\">" + value + "</a>";
	}
	return v;
}

/*---------------------------------------------------------
函数名称:fmtFinishedTitle
参          数:无
功          能:格式化已完成任务列表中的“任务名称”字段
------------------------------------------------------------*/
function fmtFinishedTitle(ts1,cellval,opts,rwdat,_act){
	var name="";
	if(opts.taskMark=="CANCEL"){
		name="white";
	}else if(opts.taskMark=="PURPLE"){
		name="purple";
	}else if(opts.taskMark=="ORANGE"){
		name="orange";
	}else if(opts.taskMark=="GREEN"){
		name="green";
	}else if(opts.taskMark=="YELLOW"){
		name="yellow";
	}else if(opts.taskMark=="BLUE"){
		name="blue";
	}else if(opts.taskMark=="RED"){
		name="red";
	}
	var value = replaceSpecialSymbol(ts1);
	if(opts.transferName!="&nbsp;"&&typeof(opts.transferName)!="undefined"&&opts.transferName!=""){//移交人不为空，表示是接收的任务
		if(opts.trustorName!="&nbsp;"&&typeof(opts.trustorName)!="undefined"&&opts.trustorName!=""){
			value = "<span style='color:#CC0000;'>（"+opts.transferName+"移交于"+opts.trustorName+","+opts.trustorName+"委托）"+value+"</span>";
		}else{
			value = "<span style='color:#CC0000;'>（"+opts.transferName+"移交）"+value+"</span>";
		}
	}else{
		if(opts.trustorName!="&nbsp;"&&typeof(opts.trustorName)!="undefined"&&opts.trustorName!=""){
			value = "<span style='color:#CC0000;'>（"+opts.trustorName+"委托）"+value+"</span>";
		}
	}
	 var v ="<a  href=\"#\" hidefocus=\"true\" onclick=\"_click_fun("+opts.id+");\">" +value + "</a>";
	return v;
}
/*---------------------------------------------------------
函数名称:fmtName
参          数:无
功          能:格式化列表中的“环节名称”字段
------------------------------------------------------------*/
function fmtName(ts1,cellval,opts,rwdat,_act){
	var v ="<a  href=\"#\" hidefocus=\"true\" onclick=\"_click_fun("+opts.id+");\">" + ts1 + "</a>";
	if(shouldBlod(opts)){
		v="<a  href=\"#\" hidefocus=\"true\" onclick=\"_click_fun("+opts.id+");\" style=\"font-weight:bold;\">" + ts1 + "</a>";
	}
	return v;
}
/*---------------------------------------------------------
函数名称:fmtCreateDate
参          数:无
功          能:格式化列表中的“创建时间”字段
------------------------------------------------------------*/
function fmtCreateDate(ts1,cellval,opts,rwdat,_act){
	var date=new Date(opts.createdTime.time);
	var v=_formate_date(date);
	if(shouldBlod(opts)){
		v="<font style=\"font-weight:bold;\">"+v+"</font>";
	}
	return v;
}
/*---------------------------------------------------------
函数名称:fmtCreatorName
参          数:无
功          能:格式化列表中的“发起人”字段
------------------------------------------------------------*/
function fmtCreatorName(ts1,cellval,opts,rwdat,_act){
	var name=opts.creatorName;
	var v=name;
	if(shouldBlod(opts)){
		v="<font style=\"font-weight:bold;\">"+v+"</font>";
	}
	return v;
}
/*---------------------------------------------------------
函数名称:fmtActive
参          数:无
功          能:格式化列表中的“任务状态”字段
------------------------------------------------------------*/
function fmtActive(ts1,cellval,opts,rwdat,_act){
	var active=opts.active;
	var v="";
	if(active==4){
		v="待领取";
	}else if(active==0){
		v="待办理";
	}else if(active==1){
		v="待指定办理人";
	}else if(active==2){
		v="已完成";
	}else if(active==3){
		v="已取消";
	}else if(active==5){
		v="已指派";
	}else{
		v="&nbsp;";
	}
	if(shouldBlod(opts)){
		v="<font style=\"font-weight:bold;\">"+v+"</font>";
	}
	return v;
}

/*---------------------------------------------------------
函数名称:fmtCompleteActive
参          数:无
功          能:格式化列表中的“任务状态”字段
------------------------------------------------------------*/
function fmtCompleteActive(ts1,cellval,opts,rwdat,_act){
	var active=opts.active;
	var v="";
	if(active==2){
		v="已完成";
	}else if(active==3){
		v="已取消";
	}else if(active==5){
		v="已指派";
	}else{
		v="&nbsp;";
	}
	if(shouldBlod(opts)){
		v="<font style=\"font-weight:bold;\">"+v+"</font>";
	}
	return v;
}
/*---------------------------------------------------------
函数名称:fmtGroupName
参          数:无
功          能:格式化列表中的“流程名称”字段
------------------------------------------------------------*/
function fmtGroupName(ts1,cellval,opts,rwdat,_act){
	var name=opts.groupName;
	var v=name;
	if(shouldBlod(opts)){
		v="<font style=\"font-weight:bold;\">"+v+"</font>";
	}
	return v;
}
/*---------------------------------------------------------
函数名称:fmtTransactDate
参          数:无
功          能:格式化列表中的“办理日期”字段
------------------------------------------------------------*/
function fmtTransactDate(ts1,cellval,opts,rwdat,_act){
	var v="";
	if(opts.transactDate!=null){
		var date=new Date(opts.transactDate.time);
		v=_formate_date(date);
		if(shouldBlod(opts)){
			v="<font style=\"font-weight:bold;\">"+v+"</font>";
		}
	}else{
		v="&nbsp;";
	}
	return v;
}
/*---------------------------------------------------------
函数名称:_click_fun
参          数:无
功          能:进入办理任务页面
------------------------------------------------------------*/
function _click_fun(taskId){
	openInput(taskId);
}

/*---------------------------------------------------------
函数名称:_formate_date
参          数:无
功          能:格式化列表中的时间
------------------------------------------------------------*/
function _formate_date(date){
	var minute=date.getMinutes();
	var hour=date.getHours();
	if(minute<10){
		minute="0"+minute;
	}
	if(hour<10){
		hour="0"+hour;
	}
	return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+hour+":"+minute ; 
}

/*---------------------------------------------------------
函数名称:showIdentifiersDiv
参          数:
功          能:标识为（下拉选）
------------------------------------------------------------*/
function showIdentifiersDiv(){
	if($("#flag").css("display")=='none'){
		removeSearchBox();
		$("#flag").show();
		var position = $("#_task_button").position();
		$("#flag").css("left",position.left+15);
		$("#flag").css("top",position.top+28);
	}else{
		$("#flag").hide();
	}
}

var identifiersDiv;
function hideIdentifiersDiv(){
	identifiersDiv = setTimeout('$("#flag").hide()',300);
}

function show_moveiIdentifiersDiv(){
	clearTimeout(identifiersDiv);
}

//给字符串替换尖括号
function replaceSpecialSymbol(str){
	 if(str.indexOf('<')!=-1){
		 str = replaceSpecialSymbol(str.replace('<','&lt'));
	 }else if(str.indexOf('>')!=-1){
		 str = replaceSpecialSymbol(str.replace('>','&gt'));
	 }
	 return str;
}

/**下拉按钮效果 ****/
function initBtnGroup(){//默认按钮效果  
	$("#parentFlowBtn")
		.button()
			.click(function() {
				removeSearchBox();
				showFlowBtnDiv();
				})
		.next()
			.button( {
				text: false,
				icons: {
					primary: "ui-icon-triangle-1-s"
				}
			})
			.click(function() {
				removeSearchBox();
				showFlowBtnDiv();
			})
			.parent()
			.buttonset();
	}

function showFlowBtnDiv(){//显示更多按钮效果位置  
	if($("#flowbtn").css("display")=='none'){
		$("#flowbtn").show();
		var position = $("#_flowbtn").position();
		$("#flowbtn").css("left",position.left+0);
		$("#flowbtn").css("top",position.top+24);
	}else{
		$("#flowbtn").hide();
	};
	$("#flowbtn").hover(
		function (over ) {
			$("#flowbtn").show();
		},
		function (out) {
			 $("#flowbtn").hide();
		}
	); 

}

function activeTree(treeid,currentId,refreshable,onAsynSuccess){
	if(refreshable==undefined)refreshable=false;
	$.ajaxSetup({cache:false});
	var mycurrentId=currentId;
	var url=webRoot+"/task/task-type-tree.htm";
	
	if(treeid=="handle_bussiness_content" ){
		url=webRoot+"/task/task-type-tree.htm?taskCategory=complete";
	}else if(treeid=="canceled_bussiness_content"){
		url=webRoot+"/task/task-type-tree.htm?taskCategory=cancel";
	}
	if(typeof($("#taskType").val())!='undefined'&&$("#taskType").val()!=""){
		if(url.indexOf("?")<0){
			url=url+"?taskType="+$("#taskType").val();
		}else{
			url=url+"&taskType="+$("#taskType").val();
		}
	}
	//treeId:,url:,data(静态树才需要该参数):,multiple:,callback:
	tree.initTree({treeId:treeid,
		url:url,
		type:"ztree",
		initiallySelect:mycurrentId,
		initiallySelectFirstChild:true,
		callback:{
				onClick:selectZNode,
				onAsyncSuccess:onAsynSuccess
			}});
	
}

function selectZNode(){
	//if(!refreshable){//如果不是办理任务后刷新页面
		var currentId = tree.getSelectNodeId();
		 selectNode(currentId,tree.treeId);
	// }
	 //refreshable=false;
}

function selectNode(currentId,treeid){
	treechange(currentId,treeid);
}
function treechange(currentId,treeid){
	var url="";
	if(currentId=="complete_task"||currentId=="active_task"||currentId=="cancel_task")currentId="";
	$("#typeName").attr("value",currentId);
	if(treeid=="unhandle_bussiness_content"){
		url=webRoot+"/task/task.htm";
		ajaxSubmit('pageForm', url, 'product_task_type');
	}else if(treeid=="handle_bussiness_content"){
		url=webRoot+"/task/task-completed-list.htm";
		ajaxSubmit('pageForm', url, 'pageTablelist_all');
	}else if(treeid=="canceled_bussiness_content"){
		url=webRoot+"/task/task-canceled-list.htm";
		ajaxSubmit('pageForm', url, 'pageTablelist_all');
	}
}

function historyTree(treeid,currentId,refreshable){
	if(refreshable==undefined)refreshable=false;
	$.ajaxSetup({cache:false});
	var mycurrentId=currentId;
	var url=webRoot+"/task/history-task-type-tree.htm";
	
	if(treeid=="completed_tree_id"){
		url=url+"?taskCategory=complete";
	}else if(treeid=="canceled_tree_id"){
		url=url+"?taskCategory=cancel";
	}
	if(typeof($("#taskType").val())!='undefined'&&$("#taskType").val()!=""){
		if(url.indexOf("?")<0){
			url=url+"?taskType="+$("#taskType").val();
		}else{
			url=url+"&taskType="+$("#taskType").val();
		}
	}
	//treeId:,url:,data(静态树才需要该参数):,multiple:,callback:
	tree.initTree({treeId:treeid,
		url:url,
		type:"ztree",
		initiallySelect:mycurrentId,
		initiallySelectFirstChild:true,
		callback:{
				onClick:historySelectZNode
			}});
	
}

function historySelectZNode(){
	//if(!refreshable){//如果不是办理任务后刷新页面
		var currentId = tree.getSelectNodeId();
		historySelectNode(currentId,tree.treeId);
	// }
	 //refreshable=false;
}

function historySelectNode(currentId,treeid){
	historyTreechange(currentId,treeid);
}
function historyTreechange(currentId,treeid){
	var url="";
	if(currentId=="complete_task"||currentId=="active_task"||currentId=="cancel_task")currentId="";
	$("#typeName").attr("value",currentId);
	if(treeid=="completed_tree_id"){
		url=webRoot+"/task/history-workflow-task.htm";
		ajaxSubmit('pageForm', url, 'pageTablelist_all');
	}else if(treeid=="canceled_tree_id"){
		url=webRoot+"/task/history-workflow-task-canceled.htm";
		ajaxSubmit('pageForm', url, 'pageTablelist_all');
	}
}

/*---------------------------------------------------------
函数名称:fmtFinishedTitle
参          数:无
功          能:格式化历史已完成任务列表中的“任务名称”字段
------------------------------------------------------------*/
function historyFmtFinishedTitle(ts1,cellval,opts,rwdat,_act){
	var name="";
	if(opts.taskMark=="CANCEL"){
		name="white";
	}else if(opts.taskMark=="PURPLE"){
		name="purple";
	}else if(opts.taskMark=="ORANGE"){
		name="orange";
	}else if(opts.taskMark=="GREEN"){
		name="green";
	}else if(opts.taskMark=="YELLOW"){
		name="yellow";
	}else if(opts.taskMark=="BLUE"){
		name="blue";
	}else if(opts.taskMark=="RED"){
		name="red";
	}
	var value = replaceSpecialSymbol(ts1);
	if(opts.transferName!="&nbsp;"&&typeof(opts.transferName)!="undefined"&&opts.transferName!=""){//移交人不为空，表示是接收的任务
		if(opts.trustorName!="&nbsp;"&&typeof(opts.trustorName)!="undefined"&&opts.trustorName!=""){
			value = "<span style='color:#CC0000;'>（"+opts.transferName+"移交于"+opts.trustorName+","+opts.trustorName+"委托）"+value+"</span>";
		}else{
			value = "<span style='color:#CC0000;'>（"+opts.transferName+"移交）"+value+"</span>";
		}
	}else{
		if(opts.trustorName!="&nbsp;"&&typeof(opts.trustorName)!="undefined"&&opts.trustorName!=""){
			value = "<span style='color:#CC0000;'>（"+opts.trustorName+"委托）"+value+"</span>";
		}
	}
	 var v ="<img src=\""+webRoot+"/images/"+name+".gif\"/><a  href=\"#\" hidefocus=\"true\" onclick=\"history_click_fun("+opts.id+");\">" +value + "</a>";
	return v;
}

/*---------------------------------------------------------
函数名称:history_click_fun
参          数:无
功          能:进入办理任务页面
------------------------------------------------------------*/
function history_click_fun(taskId){
	historyOpenInput(taskId);
}

function historyOpenInput(taskId){
	var screenWidth=screen.availWidth-12;
	var screenHeight=screen.availHeight-58;
	var win = window.open(webRoot+'/task/history-workflow-task-input.htm?id='+taskId,'win',"top=0,left=0,toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=false,resizable=no,width="+screenWidth+",height="+screenHeight);
	var time=(new Date()).getTime();
	$('body').everyTime('2s','timer'+time,function(){
		if(win.closed){
			$('body').stopTime('timer'+time);
			taskList();
        }
	},0,true);
}
	
</script>