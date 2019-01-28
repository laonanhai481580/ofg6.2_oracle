<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ambition.carmfg.entity.*,java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>
	<security:authorize ifAnyGranted="mfg_patrol_input">
		<div id="patrol_input" class="west-notree" url="${mfgctx}/inspection/patrol/input.htm" onclick="changeMenu(this);"><span>检验报告</span></div>
	</security:authorize>
	<%-- <security:authorize ifAnyGranted="mfg_patrol_recheck_list">
		<div id="_recheck_list" class="west-notree" url="${mfgctx}/inspection/patrol/recheck-list.htm" onclick="changeMenu(this);"><span>重检台帐</span></div>
	</security:authorize>	 --%>
	<security:authorize ifAnyGranted="mfg_patrol_checking_list">
		<div id="_checking_list" class="west-notree" url="${mfgctx}/inspection/patrol/checking-list.htm" onclick="changeMenu(this);"><span>检验中台帐</span></div>
	</security:authorize>	
	<security:authorize ifAnyGranted="mfg_patrol_wait_audit_list">
		<div id="_wait_audit_list" class="west-notree" url="${mfgctx}/inspection/patrol/wait-audit-list.htm" onclick="changeMenu(this);"><span>待审核台账</span></div>
	</security:authorize>		
	<security:authorize ifAnyGranted="mfg_patrol_list">
		<div id="_patrol_list" class="west-notree" url="${mfgctx}/inspection/patrol/patrol-list.htm" onclick="changeMenu(this);"><span>巡检台帐</span></div>
	</security:authorize>
	<security:authorize ifAnyGranted="mfg_patrol_list_hide">
		<div id="_hide_list" class="west-notree" url="${mfgctx}/inspection/patrol/list-hid.htm" onclick="changeMenu(this);"><span>敏感数据台账</span></div>
	</security:authorize>
	<script type="text/javascript">
	$().ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	function changeMenu(obj){
		window.location = $(obj).attr('url');
	}
</script>