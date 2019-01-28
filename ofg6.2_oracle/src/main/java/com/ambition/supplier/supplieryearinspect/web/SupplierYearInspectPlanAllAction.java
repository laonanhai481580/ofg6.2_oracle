package com.ambition.supplier.supplieryearinspect.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.entity.SupplierYearInspectPlanAll;
import com.ambition.supplier.supplieryearinspect.services.SupplierYearInspectPlanAllManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * 类名:供应商稽核计划2.0汇总
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  lpf
 * @version 1.00 2018年9月4日 发布
 */
@Namespace("/supplier/audit/year-inspect-plan/all")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location ="/supplier/audit/year-inspect-plan/all", type = "redirectAction") })
public class SupplierYearInspectPlanAllAction extends CrudActionSupport<SupplierYearInspectPlanAll>{

	private static final long serialVersionUID = 1L;
	@Autowired
	private SupplierYearInspectPlanAllManager yearInspectPlanAllManager;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log = Logger.getLogger(this.getClass());
	private SupplierYearInspectPlanAll yearInspectPlanAll;
	private Long id;
	private String ids;
	private String deleteIds;
	private Page<SupplierYearInspectPlanAll> page;
	private String processSection;
	
	@Override
	public SupplierYearInspectPlanAll getModel() {
		return yearInspectPlanAll;
	}
	
	@Action("save")
	@Override
	public String save() throws Exception {
		return null;
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		return null;
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		ActionContext.getContext().put("systemCodes", ApiFactory.getSettingService().getOptionsByGroupCode("supplier-system-code"));
		ActionContext.getContext().put("auditYears",ApiFactory.getSettingService().getOptionsByGroupCode("supplier_audit_year"));
		ActionContext.getContext().put("supplierAuditorType",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-auditor-type"));
		return SUCCESS;
	}
	@Action("list-datas")
	public String listDates(){
		try {
			page = yearInspectPlanAllManager.list(page);
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			log.error("台账获取例表失败", e);
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			yearInspectPlanAll = new SupplierYearInspectPlanAll();
			yearInspectPlanAll.setCompanyId(ContextUtils.getCompanyId());
			yearInspectPlanAll.setCreatedTime(new Date());
			yearInspectPlanAll.setCreator(ContextUtils.getUserName());
			yearInspectPlanAll.setModifiedTime(new Date());
			yearInspectPlanAll.setModifier(ContextUtils.getUserName());
			yearInspectPlanAll.setBusinessUnitName(ContextUtils.getSubCompanyName());
			yearInspectPlanAll.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			yearInspectPlanAll = yearInspectPlanAllManager.getSupplierYearInspectPlanAll(id);
		}
	}
	@Action("export")
	@LogInfo(optType="导出",message="稽核计划")
	public String export() throws Exception {
		Page<SupplierYearInspectPlanAll> page = new Page<SupplierYearInspectPlanAll>(100000);
		page = yearInspectPlanAllManager.list(page);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"SUPPLIER_YEAR_INSPECT_PLAN_ALL"),"稽核计划"));
		logUtilDao.debugLog("导出", "稽核计划");
		return null;
	}
	//创建返回消息
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public SupplierYearInspectPlanAll getYearInspectPlanAll() {
		return yearInspectPlanAll;
	}

	public void setYearInspectPlanAll(SupplierYearInspectPlanAll yearInspectPlanAll) {
		this.yearInspectPlanAll = yearInspectPlanAll;
	}

	public Page<SupplierYearInspectPlanAll> getPage() {
		return page;
	}

	public void setPage(Page<SupplierYearInspectPlanAll> page) {
		this.page = page;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getProcessSection() {
		return processSection;
	}

	public void setProcessSection(String processSection) {
		this.processSection = processSection;
	}
	
	
}
