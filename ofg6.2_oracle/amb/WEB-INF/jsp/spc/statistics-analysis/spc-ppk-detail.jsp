<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		var params = ${params};
		var postData = {};
		$(document).ready(function(){
			for(var pro in params){
				postData['params.' + pro] = params[pro];
			}
		});
		//设置默认 参数
		function $addGridOption(jqGridOption){
			jqGridOption.postData = postData;
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div style="margin:6px auto;text-align:center;font-size:16px;font-weight:bold;" id='titleDiv'>
				${title}
			</div>
			<div id="opt-content">
				<table style="width:99.7%;">
					<tr>
						<td>
							<grid:jqGrid gridId="list" url="${spcctx}/statistics-analysis/spc-ppk-detail-datas.htm" code="SPC_QUALITY_FEATURE_DETAIL" pageName="page"></grid:jqGrid>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>
</html>