<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp"%>
	<script type="text/javascript">
		$.extend($.jgrid.defaults,{
			loadComplete : function(){
				$(this).find("tr[role=row]").bind("dblclick",function(){
					var id = $(this).attr("id");
					var data = $("#list").jqGrid("getRowData",id);
					window.parent.loadByFeature(data.id,data.name);
				});
			}
		});
		function contentResize(){
			var height = $(window).height()-140;
			$("#list").jqGrid("setGridHeight",height);
			$("#list").jqGrid("setGridWidth",$(window).width()-20);
		}
		$(document).ready(function(){
			$( "#tabs" ).tabs({
				show: function(event, ui) {}
			});
		});
		
		
		function search(){
			var params = {
				featureName : $("#featureName").val(),
				featureId : $("#featureId").val()
			};
				$('table.ui-jqgrid-btable').each(function(index,obj){
					obj.p.postData = params;
					$(obj).trigger("reloadGrid");
				});
		}
		
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			return isRight;
		}
		
		function batchMigration(){
			var ids = $("#list").jqGrid("getGridParam","selarrrow");
			if(ids.length==0){
				if(!confirm("将检查和迁移所有质量特性数据,需要消耗时间较长,确定要继续吗?")){
					return;
				}
			}else{
				if(!confirm("确定要迁移选择的质量特性数据吗?")){
					return;
				}
			}
			window.parent.batchMigration(ids.join(","));
		}
		function reloadGrid(){
			$("#list").trigger("reloadGrid");
		}
		var featureId='';
		//选择质量特性
		function selectFeature(obj){
			$.colorbox({href:"${spcctx}/common/feature-bom-select.htm",
				iframe:true, 
				width:$(window).width()<700?$(window).width()-100:1000,
				height:$(window).height()<400?$(window).height()-100:600,
				overlayClose:false,
				title:"选择质量特性"
			});
		}
		//设置质量特性
		function setFeatureValue(datas){
			$("#featureName").val(datas[0].value);
			$("#featureId").val(datas[0].id);
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
					<a href="#tabs-1">质量特性列表</a>
				</li>
			</ul>
			<div id="tabs-1" style="padding:0px;">
				<div class="opt-body" style="overflow: auto">
					<div class="opt-btn">
								<span>
										<strong style="vertical-align: middle;">质量特性</strong>
										<input type="text" id="featureName" name="featureName" value="" style="width:120px;height:19px;margin-top:-10px;"/>
										<input type="hidden" id="featureId" name="featureId" value="" style="width:120px;height:19px;margin-top:-10px;"/>
										<a class="small-button-bg" style="vertical-align: middle;" onclick="selectFeature(this);" href="javascript:void(0);" title="选择质量特性">
										<span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
                            	</span>
								
								<span style="color:red;" id="message"><s:actionmessage theme="mytheme"/></span>
						<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-search"></b>确定</span></span></button>		
						<button style="float:right;margin-right:4px;" class='btn' onclick="batchMigration();" type="button"><span><span><b class="btn-icons btn-icons-upload"></b>数据迁移</span></span></button>
						<span style="color:red;" id="message"></span>
					</div>
					
					<div id="opt-content">
						<grid:jqGrid gridId="list" url="${spcctx}/data-acquisition/feature-list-datas.htm" code="SPC_QUALITY_FEATURE_LIST" pageName="featurePage"></grid:jqGrid>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>