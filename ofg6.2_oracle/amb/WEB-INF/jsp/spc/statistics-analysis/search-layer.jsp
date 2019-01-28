<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page import="com.norteksoft.product.web.struts2.Struts2Utils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.spc.entity.LayerType" %>
<%@ page import="com.ambition.spc.entity.LayerDetail" %>
<%@ include file="/common/taglibs.jsp"%>
<%
	List<LayerType> layerTypes=(List<LayerType>)ActionContext.getContext().getValueStack().findValue("layerTypes");
	int k=1;
%>
	<%if(layerTypes!=null){
	for(int i=0;i<layerTypes.size();i++){ 
		LayerType layerType=(LayerType)layerTypes.get(i);
		if("0".equals(layerType.getSampleMethod())){%>
<li style="display:none;" layerLi=true layerCode="<%=layerType.getTypeCode() %>" id="layerLi_<%=layerType.getTypeCode() %>">
	<span class="label"><%=layerType.getTypeName()%></span>
	<input type="text" name="params.<%=layerType.getTypeCode() %>"/>
</li>
		<%}else if("2".equals(layerType.getSampleMethod())||"1".equals(layerType.getSampleMethod())){%>
<li style="display:none;" layerLi=true layerCode="<%=layerType.getTypeCode() %>" id="layerLi_<%=layerType.getTypeCode() %>">
	<span class="label"><%=layerType.getTypeName()%></span>
	<select name="params.<%=layerType.getTypeCode() %>">
		<option value=""></option>
		<%List<LayerDetail> layerDetails=layerType.getLayerDetails();
		for(int j=0;j<layerDetails.size();j++){
			LayerDetail layerDetail=layerDetails.get(j);%>
		<option value="<%=layerDetail.getDetailCode()%>"><%=layerDetail.getDetailName()%></option>
		<%}%>
	</select>
</li>
<%}}}%>
<script type="text/javascript">
//更新查询条件
function updateSearchLayer(featureId){
	var url = "${spcctx}/statistics-analysis/get-layers-by-feature.htm";
	var params = {
		featureId : featureId	
	};
	$("li[layerLi=true]").hide();
	$.post(url,params,function(result){
		var codes = result;
		for(var i=0;i<codes.length;i++){
			var code = codes[i];
			var $li = $("#layerLi_"+code).show();
			$li.find("input[type=text]").val('');
			$li.find(":radio").removeAttr("checked");
			var select = $li.find("select");
			if(select.length>0){
				select.val('');
				var isMu = select.attr("multiselect");
				if(isMu=='true'){
					select.multiselect("uncheckAll");
				}
			}
		}
	},'json');
}
</script>
