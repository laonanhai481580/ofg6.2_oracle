package com.ambition.carmfg.ipqc.web;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.entity.IpqcAudit;
import com.ambition.carmfg.entity.OqcProcedure;
import com.ambition.carmfg.factoryprocedure.service.OqcFactoryManager;
import com.ambition.carmfg.factoryprocedure.service.OqcProcedureManager;
import com.ambition.carmfg.ipqc.service.IpqcAuditManager;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;
import com.sun.research.ws.wadl.Request;

@Namespace("/carmfg/data-acquisition/analyse")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/carmfg/data-acquisition/analyse", type = "redirectAction") })
public class StatisticalAnalysisAction extends CrudActionSupport<IpqcAudit> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private IpqcAudit ipqcAudit;
	private JSONObject params;
	private String	searchParams;
	private Page<IpqcAudit> page;
	private Long id;
	@Autowired
	private IpqcAuditManager ipqcAuditManager;
	@Autowired
	private OqcFactoryManager oqcFactoryManager;
	@Autowired
	private OqcProcedureManager oqcProcedureManager;

	public IpqcAudit getIpqcAudit() {
		return ipqcAudit;
	}

	public void setIpqcAudit(IpqcAudit ipqcAudit) {
		this.ipqcAudit = ipqcAudit;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public Page<IpqcAudit> getPage() {
		return page;
	}

	public void setPage(Page<IpqcAudit> page) {
		this.page = page;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSearchParams() {
		return searchParams;
	}

	public void setSearchParams(String searchParams) {
		this.searchParams = searchParams;
	}

	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	public IpqcAudit getModel() {
		return null;
	}

	@Override
	public String save() throws Exception {
		return null;
	}

	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {		
	}
	
	/**
	 * IPQC稽核遵守度统计图页面
	 */
	@Action("observe-rate")
	public String sizeSpreadchart() throws Exception {
		List<Option> businessUnits = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits");
		ActionContext.getContext().put("businessUnits",businessUnits);
		List<Option> factorys = oqcFactoryManager.listAllForOptions();
        ActionContext.getContext().put("factorys",factorys);
        List<OqcProcedure> procedures = oqcProcedureManager.getAllOqcProcedure();
        List<Option> proceduresOptions = oqcProcedureManager.converOqcProcedureToList(procedures);
        ActionContext.getContext().put("procedures",proceduresOptions);
        ActionContext.getContext().put("stations", ApiFactory.getSettingService().getOptionsByGroupCode("mfg_station"));
		return SUCCESS;
	}

	/**
	 * IPQC稽核遵守度统计图数据
	 */
	@Action("observe-rate-datas")
	@LogInfo(optType="数据",message="IPQC稽核遵守度统计图数据")
	public String sizeSpreadchartDatas() throws Exception {
		try {
			Map<String,Object> map=ipqcAuditManager.getObserveRateDatas(params);
			this.renderText(JSONObject.fromObject(map).toString());
		} catch (Exception e) {
			log.error("查询出错!",e);
		}
		return null;	
	}
	/**
	 * 尺寸分布趋势图页面
	 */
	@Action("observe-rate-show-detail")
	@LogInfo(optType="页面",message="查看详情")
	public String showDetail() throws Exception {
		searchParams=Struts2Utils.getParameter("searchParams");
		return SUCCESS;
	}	
	/**
	 * IPQC稽核遵守度统计图数据详情
	 */
	@Action("observe-rate-detail-datas")
	@LogInfo(optType="页面",message="查看详情")
	public String showDetailDatas() throws Exception {
		try {		
			searchParams=Struts2Utils.getParameter("searchParams");
			page=ipqcAuditManager.listDetail(page,searchParams);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		this.renderText(PageUtils.pageToJson(page));
		return null;
	}
}
