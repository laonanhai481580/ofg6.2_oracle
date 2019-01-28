<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul>
	<security:authorize ifAnyGranted="emailtemplate_template_list">
		<li id=template_sec><span><span>
			<a href="<grid:authorize code="emailtemplate_template_list" systemCode="emailtemplate"></grid:authorize>"><s:text name="邮件模板"/></a>
			</span>
		</span></li>
	</security:authorize>
</ul>
<div class="hid-header" onclick=headerChange(this); title="折叠/展开"></div>
<script>
	var topMenu='supplier';
	$('#'+secMenu).addClass('sec-selected');
</script>


