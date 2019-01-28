package com.ambition.qsm.customeraudit.web;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.emailtemplate.service.EmailTemplateManager;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.qsm.customeraudit.service.CustomerAuditIssuesManager;
import com.ambition.qsm.entity.CustomerAuditIssues;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * 类名:客户审核问题点履历action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Namespace("/qsm/customer-audit/issues")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/qsm/customer-audit/issues", type = "redirectAction") })
public class CustomerAuditIssuesAction extends AmbWorkflowActionBase<CustomerAuditIssues>{

	private static final long serialVersionUID = 1L;
	private CustomerAuditIssues customerAuditIssues;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private Logger log=Logger.getLogger(this.getClass());
	private File myFile;
	private String currentActivityName;//当前流程环节名称
	@Autowired
	private CustomerAuditIssuesManager customerAuditIssuesManager;
	@Autowired
	private EmailTemplateManager emailTemplateManager;
	@Override
	protected AmbWorkflowManagerBase<CustomerAuditIssues> getAmbWorkflowBaseManager() {
		// TODO Auto-generated method stub
		return customerAuditIssuesManager;
	}
	public CustomerAuditIssues getCustomerAuditIssues() {
		return customerAuditIssues;
	}
	public void setCustomerAuditIssues(CustomerAuditIssues customerAuditIssues) {
		this.customerAuditIssues = customerAuditIssues;
	}
	public LogUtilDao getLogUtilDao() {
		return logUtilDao;
	}
	public void setLogUtilDao(LogUtilDao logUtilDao) {
		this.logUtilDao = logUtilDao;
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
	public CustomerAuditIssuesManager getCustomerAuditIssuesManager() {
		return customerAuditIssuesManager;
	}
	public void setCustomerAuditIssuesManager(
			CustomerAuditIssuesManager customerAuditIssuesManager) {
		this.customerAuditIssuesManager = customerAuditIssuesManager;
	}
	public String getCurrentActivityName() {
		return currentActivityName;
	}
	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(taskId!=null){
	    	report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
	    	id=report.getId();
	    	task = getAmbWorkflowBaseManager().getWorkflowTask(taskId);
	    	ApiFactory.getFormService().fillEntityByTask(report, taskId);
	    }else if(id!=null){
	    	report = getAmbWorkflowBaseManager().getEntity(id);
	    	task = getAmbWorkflowBaseManager().getActiveTaskByTransactorId(report, ContextUtils.getUserId());
			if(task==null&&report.getWorkflowInfo()!=null){
				task = getAmbWorkflowBaseManager().getWorkflowTask(report.getWorkflowInfo().getFirstTaskId());
			}
			if(task!=null){
				taskId = task.getId();
			}
			if(taskId != null){
				ApiFactory.getFormService().fillEntityByTask(report, taskId);
			}
	    }else if(id==null){
	    	report = getAmbWorkflowBaseManager().getEntityInstanceClass().newInstance();
	    	report.setCompanyId(ContextUtils.getCompanyId());
	    	report.setSubCompanyId(ContextUtils.getSubCompanyId());
	    	report.setDepartmentId(ContextUtils.getDepartmentId());
	    	report.setCreatedTime(new Date());
	    	report.setCreator(ContextUtils.getLoginName());
	    	report.setCreatorName(ContextUtils.getUserName());
	    	report.setBusinessUnitName(ContextUtils.getSubCompanyName());
	    	report.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
	    	User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
			String subName=user.getSubCompanyName();
			report.setFactoryClassify(subName);
	    	
	    }
		//未办理,并且办理人为当前用户才可以驳回
		if(task != null && task.getTransactDate() == null && task.getTransactor().equals(ContextUtils.getLoginName())){
			returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
		}
	}
	public void initForm(){
		if(getId() == null&&getReport().getId()==null){
			getReport().setFormNo(formCodeGenerated.generateCustomerAuditNo());
			getReport().setTaskCreatedTime(new Date());
			getReport().setTransactorName(ContextUtils.getUserName());
			getReport().setTransactor(ContextUtils.getLoginName());
			getReport().setConsignableDate(new Date());
//			getReport().setBusinessUnitName(ContextUtils.getSubCompanyName());
//			getReport().setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			if(getReport().getWorkflowInfo()!=null){
				currentActivityName=getReport().getWorkflowInfo().getCurrentActivityName();
			}
		}
		ActionContext.getContext().put("enterpriseGroups",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_enterprise_group"));
		ActionContext.getContext().put("auditTypes",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_audit_type"));
		ActionContext.getContext().put("issuesTypes",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_issues_type"));
		ActionContext.getContext().put("degrees",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_degree"));
		ActionContext.getContext().put("closeStates",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_close_state"));
	}
	@Action("export")
	@LogInfo(optType="导出", message="客户审核问题点履历")
	public String export() throws Exception {
		try {
			Page<CustomerAuditIssues> page = new Page<CustomerAuditIssues>(65535);
			page = customerAuditIssuesManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"客户审核问题点履历台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出客户审核问题点履历失败",e);
		}
		return null;
	}
	@Action("import")
	public String imports() throws Exception {
		return SUCCESS;
	}
	@Action("edit-input")
	public String editInput() throws Exception {
		ActionContext.getContext().put("closeStates",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_close_state"));
		return SUCCESS;
	}	
	@Action("edit-save")
	public String editSave() throws Exception {
		String id=Struts2Utils.getParameter("id");
		String consignorLogin=Struts2Utils.getParameter("consignorLogin");
		String consignor=Struts2Utils.getParameter("consignor");
		String consignableDate=Struts2Utils.getParameter("consignableDate");
		String dutyManLogin=Struts2Utils.getParameter("dutyManLogin");
		String dutyMan=Struts2Utils.getParameter("dutyMan");
		String improveDate=Struts2Utils.getParameter("improveDate");
		String dutyManLeadLogin=Struts2Utils.getParameter("dutyManLeadLogin");
		String dutyManLead=Struts2Utils.getParameter("dutyManLead");
		String auditorDate=Struts2Utils.getParameter("auditorDate");
		String compliteDate=Struts2Utils.getParameter("compliteDate");
		String actuallyDate=Struts2Utils.getParameter("actuallyDate");
		String improveEffect=Struts2Utils.getParameter("improveEffect");
		String closeState=Struts2Utils.getParameter("closeState");
		String authorizer=Struts2Utils.getParameter("authorizer");
		String authorizerLogin=Struts2Utils.getParameter("authorizerLogin");
		customerAuditIssues=customerAuditIssuesManager.getEntity(Long.valueOf(id));
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
		customerAuditIssues.setConsignorLogin(consignorLogin);
		customerAuditIssues.setConsignor(consignor);
		if(consignableDate!=null&&!consignableDate.equals("")){
			customerAuditIssues.setConsignableDate(sdf.parse(consignableDate));
		}
		customerAuditIssues.setDutyManLogin(dutyManLogin);
		customerAuditIssues.setDutyMan(dutyMan);
		if(improveDate!=null&&!improveDate.equals("")){
			customerAuditIssues.setImproveDate(sdf.parse(improveDate));
		}
		customerAuditIssues.setDutyManLeadLogin(dutyManLeadLogin);
		customerAuditIssues.setDutyManLead(dutyManLead);
		if(auditorDate!=null&&!auditorDate.equals("")){
			customerAuditIssues.setAuditorDate(sdf.parse(auditorDate));
		}		
		if(compliteDate!=null&&!compliteDate.equals("")){
			customerAuditIssues.setCompliteDate(sdf.parse(compliteDate));
		}
		if(actuallyDate!=null&&!actuallyDate.equals("")){
			customerAuditIssues.setActuallyDate(sdf.parse(actuallyDate));
		}		
		customerAuditIssues.setImproveEffect(improveEffect);
		customerAuditIssues.setCloseState(closeState);
		customerAuditIssues.setAuthorizer(authorizer);
		customerAuditIssues.setAuthorizerLogin(authorizerLogin);
		customerAuditIssues.setIsShouDong("是");
		customerAuditIssues.setShouDongLogin(ContextUtils.getLoginName());	
		JSONObject result = new JSONObject();
		try{
			customerAuditIssuesManager.saveEntity(customerAuditIssues);
			logUtilDao.debugLog("保存", customerAuditIssues.toString());
			result.put("message", "保存成功!");
			result.put("error", false);
		}catch(Exception e){
			result.put("error", true);
			result.put("message", e.getMessage());			
		}
		renderText(result.toString());
		ActionContext.getContext().put("closeStates",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_close_state"));
		return null;
	}	
	@Action("import-datas")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(customerAuditIssuesManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	/**
	  * 方法名: 下载检设备参数模板
	  * <p>功能说明：下载检验标准的模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download-template")
	@LogInfo(optType="下载",message="下载客户审核问题点履历模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/qsm-customer-issues.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "客户审核问题点履历模板.xls";
			byte byname[] = fileName.getBytes("gbk");
			fileName = new String(byname, "8859_1");
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append(fileName).append("\"")
					.toString());
			book.write(response.getOutputStream());
		}catch (Exception e) {
			log.error("导出失败!",e);
		} finally{
			if(inputStream != null){
				inputStream.close();
			}
		}
		return null;
	}	
	/**
	 * 列表数据
	 */
	@Action("list-state")
	@LogInfo(optType="查询",message="查询数据")
	public String getListStates() throws Exception {
		String type = Struts2Utils.getParameter("type");
		String companyName =PropUtils.getProp("companyName");
//		String processSection = Struts2Utils.getParameter("processSection");
		User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
		String subName=user.getSubCompanyName();
		Integer weight =user.getWeight();
		try{
			if(type==null){
				type="N";
			}
			if("TP".equals(companyName)){
				if(weight==3){
					page = customerAuditIssuesManager.listState(page, type,null);
				}else{
					page = customerAuditIssuesManager.listState(page, type,subName);
				}
			}else{
				page = customerAuditIssuesManager.listState(page, type,null);
			}
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			log.error("查询失败!",e);
		}
		return null;
	}
	/**
	  * 方法名: 提交后
	  * <p>功能说明：</p>
	 * @throws Exception 
	 */
	public void afterSubmitCallback() throws Exception{
		emailTemplateManager.triggerTaskEmail(report,customerAuditIssuesManager.getEntityInstanceClass(),null);
	}
	/**
	  * 方法名: 完成后
	  * <p>功能说明：</p>
	 * @throws Exception 
	 */
	public void afterCompleteCallback() throws Exception{
		emailTemplateManager.triggerTaskEmail(report,customerAuditIssuesManager.getEntityInstanceClass(),null);
	}
	@Override
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		try {
			String str=customerAuditIssuesManager.deleteEntity(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据:单号:"+str);
		} catch (Exception e) {
			// TODO: handle exception
			renderText("删除失败:" + e.getMessage());
			log.error("删除数据信息失败",e);
		}
		return null;
	}
	/**
	 * 创建返回消息
	 * 
	 * @param error
	 * @param message
	 * @return
	 */
	public void createErrorMessage(String message) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("error", true);
		map.put("message", message);
		renderText(JSONObject.fromObject(map).toString());
	}
}
