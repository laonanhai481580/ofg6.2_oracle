<%@page import="com.ambition.spc.dataacquisition.service.SpcDataManager"%>
<%@page import="com.ambition.util.common.CommonUtil"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.ambition.spc.jlanalyse.entity.JLResult"%>
<%@page import="com.ambition.spc.entity.CPKMoudle"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<table style="width: 98%; margin-left: 2px; margin-top: 0px;" class="form-table-border-left">
<%
	String featurePrecs = (String)ActionContext.getContext().get("featurePrecs");
	Integer precs = null;
	if(CommonUtil.isInteger(featurePrecs)){
		precs = Integer.valueOf(featurePrecs);
	}
	ValueStack valueStack=ActionContext.getContext().getValueStack();
	JLResult jlResult =(JLResult) valueStack.findValue("jLResult");
	CPKMoudle cpkMoudle=(CPKMoudle)valueStack.findValue("cpkMoudle");
	if(jlResult==null){
		jlResult = new JLResult();
	}
	if(cpkMoudle==null){
		cpkMoudle = new CPKMoudle();
	}
%>
	<tr ><td colspan="2" style="text-align: center"><strong>统计量</strong></td></tr>
	<tr  align="left">
	<td width="40%" ><strong>Mean</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(jlResult.getAverage(),precs) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Max</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(jlResult.getMax(),precs) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Min</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(jlResult.getMin(),precs) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Range</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(jlResult.getR(),precs) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>StdDev</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(jlResult.getS(),precs) %></td>
	</tr>
	<%-- <tr  align="left">
	<td width="40%" ><strong>Sigma</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getSigma(),precs) %></td>
	</tr> --%>
	<tr  align="left">
	<td width="40%" ><strong>Skewness</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(jlResult.getSkewness(),precs) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Kurtosis</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(jlResult.getKurtosis(),precs) %></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Cp</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getCp(),precs)%></td>
	</tr>
	<%-- <tr  align="left">
	<td width="40%" ><strong>Cr</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getCr(),precs)%></td>
	</tr> --%>
	<tr  align="left">
	<td width="40%" ><strong>K</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getK(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Cpu</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getCpu(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Cpl</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getCpl(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Cpk</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getCpk(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Cpm</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getCpm(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Zu_Cap</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getZu_cap(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Zl_Cap</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getZl_cap(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Fpu_Cap</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getFpu_cap(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Fpl_Cap</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getFpl_cap(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Fp_Cap</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getFp_cap(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Pp</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getPp(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Pr</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getPr(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Ppu</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getPpu(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Ppl</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getPpl(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Ppk</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getPpk(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Ppm</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getPpm(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Zu_Perf</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getZu_pref(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Zl_Perf</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getZl_pref(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Fpu_Perf</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getFpu_pref(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Fpl_Perf</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getFpl_pref(),precs)%></td>
	</tr>
	<tr  align="left">
	<td width="40%" ><strong>Fp_Perf</strong></td>
	<td width="*%" align="right"><%=SpcDataManager.formatValue(cpkMoudle.getFp_pref(),precs)%></td>
	</tr>
</table>