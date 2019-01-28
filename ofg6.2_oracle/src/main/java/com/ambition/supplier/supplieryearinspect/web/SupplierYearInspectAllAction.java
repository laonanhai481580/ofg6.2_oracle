package com.ambition.supplier.supplieryearinspect.web;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.entity.SupplierYearInspectAll;
import com.ambition.supplier.supplieryearinspect.services.SupplierYearInspectAllManager;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * 类名:供应商稽核报告2.0汇总
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  lpf
 * @version 1.00 2018年9月4日 发布
 */
@Namespace("/supplier/audit/year-inspect/all")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/supplier/audit/year-inspect/all", type = "redirectAction") })
public class SupplierYearInspectAllAction extends AmbWorkflowActionBase<SupplierYearInspectAll>{

	private static final long serialVersionUID = 1L;
	private SupplierYearInspectAll supplierYearInspect;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private AcsUtils acsUtils;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private Logger log=Logger.getLogger(this.getClass());
	private File myFile;
	private String currentActivityName;//当前流程环节名称
	@Autowired
	private SupplierYearInspectAllManager supplierYearInspectManager;
	@Override
	protected AmbWorkflowManagerBase<SupplierYearInspectAll> getAmbWorkflowBaseManager() {
		return supplierYearInspectManager;
	}
	public SupplierYearInspectAll getSupplierYearInspectAll() {
		return supplierYearInspect;
	}
	public void setSupplierYearInspectAll(SupplierYearInspectAll supplierYearInspect) {
		this.supplierYearInspect = supplierYearInspect;
	}
	public Logger getLog() {
		return log;
	}
	public void setLog(Logger log) {
		this.log = log;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	public String getCurrentActivityName() {
		return currentActivityName;
	}
	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}
	
	public void initForm(){
		if(getId() == null&&getReport().getId()==null){
			getReport().setFormNo(formCodeGenerated.generateCustomerAuditNo());
			getReport().setTaskCreatedTime(new Date());
			getReport().setTransactorName(ContextUtils.getUserName());
			getReport().setTransactor(ContextUtils.getLoginName());
			getReport().setInitiatorPerson(ContextUtils.getUserName());
			getReport().setInitiatorPersonLogin(ContextUtils.getLoginName());
		}else{
			if(getReport().getWorkflowInfo()!=null){
				currentActivityName=getReport().getWorkflowInfo().getCurrentActivityName();
			}
		}
		ActionContext.getContext().put("currentName",ContextUtils.getLoginName());
		ActionContext.getContext().put("checkResults",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-check-result"));
		ActionContext.getContext().put("supplierIssueType",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-issue-type"));
		ActionContext.getContext().put("supplierAuditorType",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-auditor-type"));
		ActionContext.getContext().put("auditSorts",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-year-inspect-category"));
		
		
	}
	
	@Action("delete")
	public String delete() throws Exception {
		return null;
	}
}
