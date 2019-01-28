<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
		function $successfunc(response){
			var result = eval("(" + response.responseText + ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="MFG_IPQC_AUDIT_AMOUNT_SAVE">
				isRight =  true;
			</security:authorize>
			return isRight;
		}
		//选择提醒人员
		var selRowId = null;
		function warmingManClick(obj){	
			selRowId=obj.rowid;
			var acsSystemUrl = "${ctx}";
			popTree({ title :'选择人员',
				innerWidth:'400',
				treeType:'MAN_DEPARTMENT_TREE',
				defaultTreeValue:'loginName',
				leafPage:'false',
				multiple:'true',
				hiddenInputId:'personId',
				showInputId:'personName',
				acsSystemUrl:acsSystemUrl,
				callBack:function(){
					setWarmingMan();
				}
			});			
		}		
		function setWarmingMan(){
			var warmingMan=$("#personName").val();
			$("#"+selRowId+"_warmingMan").val(warmingMan);
			var warmingManLogin=$("#personId").val();
			$("#"+selRowId+"_warmingManLogin").val(warmingManLogin);
		}	
		var params = {};
		var selRowId = null;
		function $oneditfunc(rowId){
			selRowId = rowId;
			$("#" + rowId + "_factory").change(function(){
				factoryChange(rowId);
			});
		}
		function factoryChange(rowid){
			selRowId=rowid;	
			var factory=$("#"+selRowId+"_factory").val();
			var url = "${mfgctx}/base-info/factory-procedure/procedure-select.htm?factory="+factory;
			$.post(encodeURI(url),{},function(result){
	 			if(result.error){
	 				alert(result.message);
	 			}else{
					var procedures = result.procedures;
					var procedureArr = procedures.split(",");
					var procedure = document.getElementById(selRowId+"_station");
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
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="audit_amount";
		var treeMenu = "_audit_amount";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-base-info-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="MFG_IPQC_AUDIT_AMOUNT_SAVE">
						<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="MFG_IPQC_AUDIT_AMOUNT_DELETE">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button id="searchBtn" class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="MFG_IPQC_AUDIT_AMOUNT_EXPORT">
						<button class="btn" onclick="iMatrix.export_Data('${mfgctx}/ipqc/audit-amount/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<input type="hidden" name="personName"  id="personName" value=""/>
					<input type="hidden" name="personId"  id="personId" value=""/>	
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<div id="message"><s:actionmessage theme="mytheme" /></div>	
				<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
				<div id="opt-content" >
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="list" url="${mfgctx}/ipqc/audit-amount/list-datas.htm" submitForm="defaultForm" code="MFG_IPQC_AUDIT_AMOUNT" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>