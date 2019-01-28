<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function() {
	$( "#accordion1" ).accordion({
		animated:false,
		collapsible:false,
		event:'click',
		fillSpace:true 
	});
	
});
</script>
	<script type="text/javascript" class="source">
		$(function () {
			if(thirdMenu=="levelScorelist"){
				$("#levelScorelist").jstree({ 
					"html_data" : {
						"data" :  
				          "<ul><li onclick='selectedNode(this)' id='levelScorelist'><a href='${supplierctx }/base-info/level-score/list.htm'>供应商等级与得分关系</a></li>"+
					      "<li onclick='selectedNode(this)' id='levelChangelist'><a href='${supplierctx }/base-info/level-change/list.htm'>供应商等级变更关系</a></li>"+
				          "<li onclick='selectedNode(this)' id='materialTypeGoallist'><a href='${supplierctx }/base-info/material-type-goal/list.htm'>物料类别目标</a></li>"+
				          "<li onclick='selectedNode(this)' id='_estimate_indicator'><a href='${supplierctx }/estimate/indicator/quarter/list.htm'>考评指标维护</a></li>"+
				          "<li onclick='selectedNode(this)' id='_estimate_model'><a href='${supplierctx }/estimate/model/quarter/list.htm'>考评模型维护</a></li>"+
				          "<li onclick='selectedNode(this)' id='_data_source'><a href='${supplierctx }/datasource/list.htm'>数据来源</a></li>"+
				          "</ul>"
					},
					"ui" : {
						"initially_select" : [ treeMenu ]
					},
					"plugins" : [ "themes", "html_data","ui" ]
				});
				$("#accordion1").accordion({active:0});
			}else if(thirdMenu=="defectionItem"){
				$("#defectionItem").jstree({ 
					"html_data" : {
						"data" :  
							 "<ul>"+
							 "<li onclick='selectedNode(this)' id='_defectionClass'><a href='${supplierctx}/base-info/defection-item/defection-class-list.htm'>物料类别</a></li>"+
// 					         "<li onclick='selectedNode(this)' id='_defectionItem'><a href='${supplierctx}/base-info/defection-item/list.htm'>不良项目</a></li>"+
					         "</ul>"
					},
					"ui" : {
						"initially_select" : [ treeMenu ]
					},
					"plugins" : [ "themes", "html_data","ui" ]
				});
				$("#accordion1").accordion({active:1});
			}else if(thirdMenu=="admitBasics"){
				$("#admitBasics").jstree({ 
					"html_data" : {
						"data" :  
							 "<ul>"+
							 "<li onclick='selectedNode(this)' id='_admitBasics'><a href='${supplierctx}/base-info/admit-basics/admit-class-list.htm'>文件维护</a></li>"+
					         "</ul>"
					},
					"ui" : {
						"initially_select" : [ treeMenu ]
					},
					"plugins" : [ "themes", "html_data","ui" ]
				});
				$("#accordion1").accordion({active:2});
			}
			
		});
		function selectedNode(obj) {
			window.location = $(obj).children('a').attr('href');
		}
	   function _change_menu(url){
			window.location=url;
		}
	</script>
<div id="accordion1" class="basic">
	<security:authorize ifAnyGranted="supplier-base-info-level-score-list">
		<h3><a id="_levelScorelist" onclick="_change_menu('${supplierctx}/base-info/level-score/list.htm');">供应商维护</a></h3>
		<div><div id="levelScorelist" class="demo">数据加载中......</div></div>
	</security:authorize>
	<security:authorize ifAnyGranted="SUPPLIER_BASEINFO_DEFECTION_LIST">
		<h3><a id="_defectionItem"  onclick="_change_menu('${supplierctx }/base-info/defection-item/defection-class-list.htm');">不良维护</a></h3>
		<div><div id="defectionItem" class="demo">数据加载中......</div></div>
	</security:authorize>
	<security:authorize ifAnyGranted="SUPPLIER_ADMIT_BASICS_LIST">
		<h3><a id="_admitBasics"  onclick="_change_menu('${supplierctx }/base-info/admit-basics/admit-class-list.htm');">材料承认维护</a></h3>
		<div><div id="admitBasics" class="demo">数据加载中......</div></div>
	</security:authorize>
</div>