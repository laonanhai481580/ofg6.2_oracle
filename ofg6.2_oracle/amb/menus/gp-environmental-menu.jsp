<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div style="display: block; height: 10px;"></div>

<security:authorize ifAnyGranted="gp-average-list_ok">
	<div id="averageList_ok" class="west-notree" url="${gpctx }/averageMaterial/list-ok.htm"
		onclick="changeMenu(this);">
		<span><s:text name="gp.form.均质材料库"/></span>
	</div>
</security:authorize>

<security:authorize ifAnyGranted="gp-gpmaterial-add">
<div id="gpmaterialInput" class="west-notree" url="${gpctx }/gpmaterial/input.htm"
	onclick="changeMenu(this);">
	<span><s:text name="gp.form.产品成分宣告表单"/></span>
</div>
</security:authorize>
<security:authorize ifAnyGranted="gp-gpmaterial-list">
<div id="gpmaterialList" class="west-notree" url="${gpctx }/gpmaterial/list.htm"
	onclick="changeMenu(this);">
	<span><s:text name="gp.form.产品成分宣告表台账"/></span>
</div>
</security:authorize>
<security:authorize ifAnyGranted="gp-average-list_qualified">
	<div id="averageList_qualified" class="west-notree" url="${gpctx }/averageMaterial/list-qualified.htm"
		onclick="changeMenu(this);">
		<span><s:text name="gp.form.过期均质材料库"/></span>
	</div>
</security:authorize>
<security:authorize ifAnyGranted="gp-gpSurvey-list">
	<div id="gpSurveyList" class="west-notree" url="${gpctx }/gpmaterial/gpSurvey/list.htm"
		onclick="changeMenu(this);">
		<span><s:text name="gp.form.供应商调查表台账"/></span>
	</div>
</security:authorize>

<script type="text/javascript" class="source">
		$(document).ready(function(){
			$('#'+thirdMenu).addClass('west-notree-selected');
		});
		function changeMenu(obj){
			window.location = $(obj).attr('url');
		}
	</script>