<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<%@ include file="/common/supplier-add.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/js/CodeCombobox.js"></script>
	<script type="text/javascript" src="${ctx}/js/CodeMultiCombobox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">	
		//材料代码列表
		$(function(){
			$("#customCombobox").CodeCombobox({
				value : {id:1,code:'asd',name:'add'}
			});
			$("#customCombobox1").CodeMultiCombobox({
				value : [{id:1,code:'asd',name:'add'}]
			});
		});
		function $successfunc(response){
			var result = eval("(" + response.responseText	+ ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		function click(cellvalue, options, rowObject){	
			if(rowObject.id){
				return "<div style='text-align:center;'><a href='javascript:void(0);' onclick='creatInput("+rowObject.id+");'>添加材料项目</a>&nbsp;"+""+"&nbsp;"
		              +"</div>";
			}else{
				return "";
			}
		}
		function creatInput(id){
			if(id){
				window.open('${supplierctx}/base-info/admit-basics/list.htm?admitClassId='+id);
			}		
		}
		function delMyRow(rowId) {
			if(editing){
				alert("请先完成编辑！");
				return;
			}
			var ids = jQuery("#admitClassList").getGridParam('selarrrow');
			if(ids.length < 1){
				alert("请选中需要删除的记录！");
				return;
			}
			if(ids.length > 1){
				alert("记录可能含有材料代码，请逐条删除！");
				return;
			}
			if(confirm("确定要删除所选中的记录？")){
				var ret = $("#admitClassList").jqGrid('getRowData',ids);
				$.post("${supplierctx}/base-info/admit-basics/search-subs.htm",{admitClassId : ret.id},function(result){
					if(result == "have"){
						alert("还有材料项目不能删除，请先删除其下材料项目！");
						return;
					}else{
						$.post("${supplierctx}/base-info/admit-basics/admit-class-delete.htm", {
							deleteIds : ids.join(',')
						}, function(data) {
							//ids数组的长度是会自动变小的(实际是jqgrid内部的一个数组)
							while (ids.length>0) {
								jQuery("#admitClassList").jqGrid('delRowData', ids[0]);
							}
						});
					}
				});
			}			
		}
		function deleteSubs(rowId){
			var ret = jQuery("#admitClassList").jqGrid('getRowData',rowId);
			if(confirm("确定要删除"+ret.supplierAdmitClass+"下所有的材料项目吗？")){
				$.post("${supplierctx}/base-info/admit-basics/delete-subs.htm", {admitClassId : ret.id}, function(){});
			};
		}
		
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="SUPPLIER_DEFECTION_CLASS_SAVE">
			  isRight =  true;
			</security:authorize>
			return isRight;
		}
		//重写(单行保存前处理行数据)
		function $processRowData(data){
			//data.businessUnit = $("#businessUnit").val();
			return data;
		}
		function selectBusinessUnit(obj){
			window.location.href = encodeURI('${supplierctx}/base-info/admit-basics/admit-class-list.htm?businessUnit='+ obj.value);
		}
		function $addGridOption(jqGridOption){
			//jqGridOption.postData.businessUnit=$("#businessUnit").val();
		}
		//导入
		function imports(){
			var url = '${supplierctx}/base-info/admit-basics/imports.htm';
			$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
				overlayClose:false,
				title:"导入",
				onClosed:function(){
					$("#admitClassList").trigger("reloadGrid");
				}
			});
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="admitBasics";
		var treeMenu="_admitBasics";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-base-info-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="supplier_admit_basics_save">
					<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="supplier_admit_basics_delete">
					<button class='btn' onclick="delMyRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<div style="float:right;position:absolute; right:50px;top:6px;" > 
				<security:authorize ifAnyGranted="supplier-add">					
					 <s:select list="systemCodes" 
								theme="simple"
								listKey="value" 
								listValue="name" 
								id="systemCode"
								onchange="systemCodeChange('base-info/admit-basics/admit-class-list.htm')"
								cssStyle="width:60px"
								emptyOption="false"
								labelSeparator="">
					</s:select> 
				</security:authorize>						
				</div> 				
				<div id="opt-content">
					<form id="contentForm" method="post" action="" >
						<grid:jqGrid gridId="admitClassList" url="${supplierctx}/base-info/admit-basics/admit-class-list-datas.htm" code="SUPPLIER_ADMIT_CLASS"></grid:jqGrid>	
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>