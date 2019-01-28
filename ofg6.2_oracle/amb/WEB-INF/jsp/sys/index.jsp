<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
	<meta name="google" content="notranslate" />
	<title>系统开始菜单</title>
	<link rel="stylesheet" type="text/css" href="${ctx}/widgets/metro/css/jquery.gridly.css"/>
	<link rel="stylesheet" type="text/css" href="${ctx}/widgets/metro/css/sample.css"/>
	<script type="text/javascript" src="${ctx}/widgets/metro/js/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/metro/js/jquery.gridly.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/metro/js/sample.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/metro/js/rainbow.js"></script>
</head>

<body>
	<div class="content">
		<div id="head">
			<div>
				<span>欧菲光质量管理系统</span><a href="${ctx}/j_spring_security_logout"><img src="${ctx}/widgets/metro/img/logout.png" title="退出" alt="退出" /></a>
			</div>
		</div>
		<section class="example">
		<div class="gridly">
			<s:iterator value="_menus" id="menus" var="menu" status="status">
				<s:if test="'sys' != #menu.code">
					<div class="brick small" url="<s:property value="#menu.menuUrl" />">
			 			<img src="${ctx}/widgets/metro/img/src/<s:property value="#menu.code" />.png" title="【<s:property value="#menu.name" />】" alt="" /><s:property value="#menu.name" />
			 		</div>
				</s:if>
			</s:iterator>
		</div>
		</section>
	</div>
</body>
</html>