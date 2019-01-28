<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.DAY_OF_MONTH,1);
	String startDateStr = sdf.format(calendar.getTime());
	
	calendar.add(Calendar.MONTH,1);
	calendar.add(Calendar.DATE,-1);
	String endDateStr = sdf.format(calendar.getTime());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp"%>
	<script type="text/javascript">
		var selFeatureId=null;
		$.extend($.jgrid.defaults,{
			beforeRequest : function(){
				var p = $(this).jqGrid("getGridParam","postData");
				if(p&&p.featureId!=undefined){
					return true;
				}else{
					contentResize();
					return false;
				}
			}
		});
		function contentResize(){
			var height = $(window).height()-140-40;
			$("#list").jqGrid("setGridHeight",height);
			$("#list").jqGrid("setGridWidth",$(window).width()-20);
		}
		$(document).ready(function(){
			$(":input[isDate=true]").datepicker({changeMonth:true,changeYear:true});
			$( "#tabs" ).tabs({
				show: function(event, ui) {}
			});
		});
		//获取表单的值
		function getParams(){
			var params = {};
			$(":input","#customerSearchDiv").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name&&jObj.val()){
					params[obj.name] = jObj.val();
				}
			});
			$("#customerSearchDiv input[type=hidden]").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name&&jObj.val()){
					params[obj.name] = jObj.val();
				}
			});
			return params;
		}
		
		function search(){
			params = getParams();
			var date1 = $("#datepicker1").val();
			var date2 = $("#datepicker2").val();
			if(date1>date2){
				alert("日期前后选择有误,请重新设置!");
				$("#datepicker1").focus();
			}else{
				$("#list").jqGrid("setGridParam",{postData:params});
				$("#list").jqGrid("setGridParam",{page:1});
				$("#list").trigger("reloadGrid");
			}
		}
		function loadByFeature(featureId,featureName){
			$.clearMessage();
			$("#tabTitle").html(featureName+"-采集数据");
			$("#list").jqGrid("setGridParam",{postData:{featureId:featureId}});
			search();
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			return false;
		}
		function delRows(){
			var ids = $("#list").jqGrid("getGridParam","selarrrow");
			if(ids.length==0){
				alert("请选择需要删除的数据!");
				return;
			}
			if(!confirm("确定要删除吗?")){
				return false;
			}
			var groupIds=[],dataIds=[];
			for(var i=0;i<ids.length;i++){
				var data = $("#list").jqGrid("getRowData",ids[i]);
				if(data.dataIds){
					dataIds.push(data.dataIds);
				}else{
					groupIds.push(ids[i]);
				}
			}
			var featureId = $("#list").jqGrid("getGridParam","postData").featureId;
			$.showMessage("正在删除，请稍候... ...","custom");
			$("button").attr("disabled","disabled");
			var url = "${spcctx}/data-acquisition/maintenance-delete.htm";
			$.post(url,{groupIds:groupIds.join(","),dataIds:dataIds.join(","),featureId:featureId},function(result){
				$.clearMessage();
				$("button").removeAttr("disabled");
				if(result.error){
					alert(result.message);
				}else{
					$.showMessage("删除成功!");
					$("#list").trigger("reloadGrid");
				}
			},'json');
		}
		function exports(){
			var params = getParams();
			var p = $("#list").jqGrid("getGridParam","postData");
			if(!p||!p.featureId){
				alert("请选择左边质量特性!");
				return;
			}
			params['_list_code'] = p._list_code;
			params['featureId'] = p.featureId;
			$.showMessage("正在导出,请稍候... ...",'custom');
			$.post('${spcctx}/data-acquisition/export-maintenance-list.htm',params,function(result){
				$.clearMessage();
				if(result.error){
					alert(result.message);
				}else{
					var url = "${ctx}/portal/export-data.action?fileName=" + result.fileName;
					window.location = encodeURI(url);
				}
			},'json');
		}
		function updateData(isView){
			var ids = $("#list").jqGrid("getGridParam","selarrrow");
			if(ids.length==0){
				alert("请选择数据!");
				return;
			}
			var groupId="",dataIds="";
			var data = $("#list").jqGrid("getRowData",ids[0]);
			if(data.dataIds){
				dataIds = data.dataIds;
			}else{
				groupId = ids[0];
			}
			openInfo(groupId,dataIds,isView);
		}
		function openInfo(groupId,dataIds,isView){
			var featureId = $("#list").jqGrid("getGridParam","postData").featureId;
			var params = {
				href:'${spcctx}/data-acquisition/maintenance-input.htm?groupId='+(groupId?groupId:"")+"&dataIds="+(dataIds?dataIds:"")+"&isView="+isView+"&featureId="+featureId,
				iframe:true,
				innerWidth:$(window).width()<715?$(window).width()-10:715,
				innerHeight:$(window).height()<512?$(window).height()-10:512,
				overlayColse:false,
				title:"子组信息"
			};
			if(!isView){
				params.onClosed = function(){
					$("#list").trigger("reloadGrid");
				};
			}
			window.parent.openColorbox(params,function(){
				$("#list").trigger("reloadGrid");
			});
		}
		function reloadGrid(){
			$("#list").trigger("reloadGrid");
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="data_acq";
		var thirdMenu="_maintenance_data";
	</script>
	<div class="ui-layout-center">
		<div id="tabs" >
			<ul>
				<li>
					<a href="#tabs-1" id="tabTitle">质量特性-采集数据</a>
				</li>
			</ul>
			<div id="tabs-1" style="padding:0px;">
				<div class="opt-body" style="overflow: auto">
					<div class="opt-btn">
						<security:authorize ifAnyGranted="spc_data-acquisition_maintenance-update">
							<button class='btn' onclick="updateData(false);" type="button"><span><span><b class="btn-icons btn-icons-edit"></b>修改</span></span></button>
						</security:authorize>
						<security:authorize ifAnyGranted="spc_data-acquisition_maintenance-delete">
							<button class='btn' onclick="delRows();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
						</security:authorize>
						<button class='btn' onclick="updateData(true);" type="button"><span><span><b class="btn-icons btn-icons-info"></b>查看详情</span></span></button>
						<security:authorize ifAnyGranted="spc_data-acquisition_export-maintenance-list">
							<button class='btn' onclick="exports();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
						</security:authorize>
						<span style="color:red;" id="message">请双击左边质量特性查看采集数据.</span>
					</div>
					<div style="line-height:40px;padding-left:10px;" id="customerSearchDiv">
						采集时间:&nbsp;
						<input id="datepicker1" isDate=true type="text" readonly="readonly" style="width:72px;height:20px;" name="params.startDate_ge_date" value="<%=startDateStr%>"/>
						至
					    <input id="datepicker2" isDate=true type="text" readonly="readonly" style="width:72px;height:20px;" name="params.endDate_le_date" value="<%=endDateStr%>"/>
						<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-search"></b>确定</span></span></button>
					</div>
					<div id="opt-content">
						<grid:jqGrid gridId="list" url="${spcctx}/data-acquisition/spcgroup-list-datas.htm" code="SPC_SUB_GROUP" pageName="page"></grid:jqGrid>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>