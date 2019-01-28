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
	function selectBusinessUnit(obj){
		var customer=$("#customerStr").val();
		var yearInt=$("#yearInt").val();
		window.location.href = encodeURI('${aftersalesctx}/customer-evaluation/list.htm?customer='+ customer+"&yearInt="+yearInt);
	}	
	//重写(单行保存前处理行数据)
	function $processRowData(data){
		data.customer = $("#customerStr").val();
		data.yearInt = $("#yearInt").val();
		return data;
	}
	//后台返回错误信息
	function $successfunc(response){
		var jsonData = eval("(" + response.responseText+ ")");
		if(jsonData.error){
			alert(jsonData.message);
		}else{
			return true;
		}
	}
	function $addGridOption(jqGridOption){
		jqGridOption.postData.customer=$("#customerStr").val();
		jqGridOption.postData.yearInt=$("#yearInt").val();
	}
	var selRowId = null;
	function $oneditfunc(rowId){
		selRowId = rowId;
	}
 	//导入台账数据
	function importDatas(){
		var url = encodeURI('${aftersalesctx}/customer-evaluation/import.htm');
		$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
			overlayClose:false,
			title:"导入台账数据",
			onClosed:function(){
				$("#list").trigger("reloadGrid");
			}
		});
	}
	//选择提醒人员
	var selRowId = null;
	function dutyManClick(obj){	
		selRowId=obj.rowid;
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择人员',
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'loginName',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:'personId',
			showInputId:'personName',
			acsSystemUrl:acsSystemUrl,
			callBack:function(){
				setDutyMan();
			}
		});			
	}
	function qeManClick(obj){	
		selRowId=obj.rowid;
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择人员',
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'loginName',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:'personId',
			showInputId:'personName',
			acsSystemUrl:acsSystemUrl,
			callBack:function(){
				setqeMan();
			}
		});			
	}
	
	function setDutyMan(){
		var warmingMan=$("#personName").val();
		$("#"+selRowId+"_dutyMan").val(warmingMan);
		var warmingManLogin=$("#personId").val();
		$("#"+selRowId+"_dutyManLogin").val(warmingManLogin);
	}
	function setqeMan(){
		var warmingMan=$("#personName").val();
		$("#"+selRowId+"_qeMan").val(warmingMan);
		var warmingManLogin=$("#personId").val();
		$("#"+selRowId+"_qeManLogin").val(warmingManLogin);
	}
	function downloadTemplate(){
		window.location = '${aftersalesctx}/customer-evaluation/download.htm';
	}	

 	var params = {};
 	function hide(obj){
 		var id = $("#list").jqGrid("getGridParam","selarrrow");
 		if(id.length==0){
 			alert("请选择需要标记为敏感的数据！");
 			return ;
 		} 		
 		var url="${aftersalesctx}/customer-evaluation/hiddenState.htm?id="+id+"&&type=N";
		$.post(encodeURI(url),params,function(result){
			if(result.error){
				alert("操作失败,"+result.message);
			}else{
				alert(result.message);
			};
			$("#list").jqGrid("setGridParam").trigger("reloadGrid");
		},'json');
 	} 
	 function exportDatas(obj){
		 	var customer=$("#customerStr").val();
			var yearInt=$("#yearInt").val();
	 		iMatrix.export_Data("${aftersalesctx}/customer-evaluation/export.htm?customer="+customer+"&yearInt="+yearInt);
		}	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="customer";
		var thirdMenu="customer_evaluation";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/aftersales-sec-menu.jsp"%> 
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/aftersales-customer-third-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="AFS_CUSTOMER_EVALUATION_SAVE">
					<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>				
				<security:authorize ifAnyGranted="AFS_CUSTOMER_EVALUATION_DELETE">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<security:authorize ifAnyGranted="AFS_CUSTOMER_EVALUATION_EXPORT">
					<button class="btn" onclick="exportDatas(this);"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="AFS_CUSTOMER_EVALUATION_IMPORT">
				<button class='btn' onclick="importDatas();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>
				<button class="btn" onclick="downloadTemplate();"><span><span><b class="btn-icons btn-icons-download"></b>下载导入模板</span></span></button>
				</security:authorize>			
				客户：
				 <s:select list="customers" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="customerStr"
					name="customer"
					onchange="selectBusinessUnit(this)"
					cssStyle="width:100px"
					emptyOption="false"
					labelSeparator="">
				</s:select> 
				年度：
				 <s:select list="yearInts" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="yearInt"
					name="yearInt"
					onchange="selectBusinessUnit(this)"
					cssStyle="width:70px"
					emptyOption="false"
					labelSeparator="">
				</s:select> 
				<input type="hidden" name="personName"  id="personName" value=""/>
				<input type="hidden" name="personId"  id="personId" value=""/>				
			<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
			</div>
			<div style="display: none;" id="message"></div>
			<div id="opt-content" >
				<form id="contentForm" name="contentForm" method="post"  action=""  >
						<grid:jqGrid  gridId="list" url="${aftersalesctx}/customer-evaluation/list-datas.htm" submitForm="defaultForm" code="AFS_CUSTOMER_EVALUATION" ></grid:jqGrid>
				</form>
			</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>