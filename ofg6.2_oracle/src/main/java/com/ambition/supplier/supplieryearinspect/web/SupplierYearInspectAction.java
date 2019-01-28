package com.ambition.supplier.supplieryearinspect.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
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
import com.ambition.supplier.entity.SupplierYearInspect;
import com.ambition.supplier.entity.SupplierYearInspectPlan;
import com.ambition.supplier.supplieryearinspect.services.SupplierYearInspectManager;
import com.ambition.supplier.supplieryearinspect.services.SupplierYearInspectPlanManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * 类名:供应商稽核报告2.0
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  xjn
 * @version 1.00 2016年9月23日 发布
 */
@Namespace("/supplier/audit/year-inspect")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/supplier/audit/year-inspect", type = "redirectAction") })
public class SupplierYearInspectAction extends AmbWorkflowActionBase<SupplierYearInspect>{

	private static final long serialVersionUID = 1L;
	private SupplierYearInspect supplierYearInspect;
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
	private SupplierYearInspectManager supplierYearInspectManager;
	@Autowired
	private SupplierYearInspectPlanManager supplierYearInspectPlanManager;
	@Override
	protected AmbWorkflowManagerBase<SupplierYearInspect> getAmbWorkflowBaseManager() {
		return supplierYearInspectManager;
	}
	public SupplierYearInspect getSupplierYearInspect() {
		return supplierYearInspect;
	}
	public void setSupplierYearInspect(SupplierYearInspect supplierYearInspect) {
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
	public SupplierYearInspectManager getSupplierYearInspectManager() {
		return supplierYearInspectManager;
	}
	public void setSupplierYearInspectManager(
			SupplierYearInspectManager supplierYearInspectManager) {
		this.supplierYearInspectManager = supplierYearInspectManager;
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
			editAuditSuppliers();
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
	public void editAuditSuppliers() {
		String userId = Struts2Utils.getParameter("userId");
		if(userId!=null&&!"".equals(userId)){
			SupplierYearInspectPlan supplierYearInspectPlan = supplierYearInspectPlanManager.getSupplierYearInspectPlan(Long.valueOf(userId));
			getReport().setInspectId(userId);
			getReport().setCreationYear(supplierYearInspectPlan.getCreationYear());
			getReport().setSupplierName(supplierYearInspectPlan.getSupplierName());//名称
			getReport().setSupplierCode(supplierYearInspectPlan.getSupplierCode());//编码
			getReport().setSupplierLoginName(supplierYearInspectPlan.getSupplierCode());//供应商登录名
			getReport().setAddress(supplierYearInspectPlan.getAddress());//供应商地址
			getReport().setSupplyFactory(supplierYearInspectPlan.getSupplyFactory());//供应商厂区
			getReport().setSupplierEmail(supplierYearInspectPlan.getSupplierEmail());//供应商邮箱
			getReport().setSupplyMaterial(supplierYearInspectPlan.getSupplyMaterial());//供应物料
			getReport().setMaterialType(supplierYearInspectPlan.getMaterialType());//材料类别
			getReport().setAuditorType(supplierYearInspectPlan.getAuditorType());//审核类型
			getReport().setAuditSort(supplierYearInspectPlan.getAuditSort());//审核类别
		}
	}
	
	/**
	 * 保存
	 */
	@Override
	@Action("save")
	@LogInfo(optType="保存",message="保存数据")
	public String save() throws Exception {
		//设置
		try {
			beforeSaveCallback();
			//子表信息
			Map<String, List<JSONObject>> childMaps = getChildrenInfos();
 			getAmbWorkflowBaseManager().saveEntity(report,childMaps);
			if(taskId == null && report.getWorkflowInfo() != null){
				taskId = report.getWorkflowInfo().getFirstTaskId();
				task = getAmbWorkflowBaseManager().getWorkflowTask(taskId);
				returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
			}
			supplierYearInspectManager.setSupplierYearInspect(report);
			addActionMessage("保存成功!");
			
			afterSaveCallback();
			//成功标志
			ActionContext.getContext().put("_successFlag",true);
			//只是保存
			ActionContext.getContext().put("_isSave",true);
		} catch (Exception e) {
			getLogger().error("保存"+getAmbWorkflowBaseManager().getWorkflowDefinitionName()+"失败!",e);
			addActionMessage("保存"+getAmbWorkflowBaseManager().getWorkflowDefinitionName()+"失败," + e.getMessage());
			if(id != null){
				report = getAmbWorkflowBaseManager().getEntity(id);
			}else if(taskId != null){
				report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
				returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
			}
		}
		initForm();
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		String returnurl = Struts2Utils.getParameter("inputformortaskform").equals("inputform")?"input":"process-task";
		if("process-task".equals(returnurl)){
			List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(report);
			if(opinionParameters!=null && !opinionParameters.equals("") && opinionParameters.size()!=0){
				Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
			}else{
				Struts2Utils.getRequest().setAttribute("opinionParameters", new ArrayList<Opinion>());
			}
		}
		//自定义返回页面时返自定义的地址
		String customInputTypeFormUrl = Struts2Utils.getParameter("customInputTypeFormUrl");
		if(StringUtils.isNotEmpty(customInputTypeFormUrl)){
			return customInputTypeFormUrl;
		}
		return returnurl;
	}	
	
	/**
	 * 启动并提交流程
	 */
	@Override
	@Action("submit-process")
	@LogInfo(optType="启动并提交",message="启动并提交流程")
	public String submitProcess() {
		boolean hasError = false;
		try{
			beforeSubmitCallback();
			//子表信息
			Map<String, List<JSONObject>> childMaps = getChildrenInfos();
			CompleteTaskTipType completeTaskTipType =  getAmbWorkflowBaseManager().submitProcess(report,childMaps);
			submitResult = getAmbWorkflowBaseManager().getCompleteTaskTipType(completeTaskTipType,report);
			supplierYearInspectManager.setSupplierYearInspect(report);
			createSupplierUser(report);
			isCountersign(report);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "启动并提交流程成功!");
			addActionMessage("提交成功!");
			afterSubmitCallback();
			//成功标志
			ActionContext.getContext().put("_successFlag",true);
		}catch(AmbFrameException e){
			hasError = true;
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "启动并提交流程失败!");
			addActionMessage("提交失败!" + e.getMessage());
			getLogger().error("启动并提交流程失败!", e);
			if(id != null){
				report = getAmbWorkflowBaseManager().getEntity(id);
			}else if(taskId != null){
				report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
				returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
			}
		} catch (Exception e) {
			hasError = true;
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "启动并提交流程失败!设置特殊字段值出错");
			addActionMessage("提交失败!设置特殊字段值出错");
			getLogger().error("启动并提交流程失败!设置特殊字段值出错", e);
			if(id != null){
				report = getAmbWorkflowBaseManager().getEntity(id);
			}else if(taskId != null){
				report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
			}
		}
		if(!hasError){
			task = getAmbWorkflowBaseManager().getActiveTaskByTransactorId(report,ContextUtils.getUserId());
			if(task!=null)taskId = task.getId();
			if(task==null&&report.getWorkflowInfo()!=null){
				taskId = report.getWorkflowInfo().getFirstTaskId();
				task = getAmbWorkflowBaseManager().getWorkflowTask(taskId);
			}
			try {
				getAmbWorkflowBaseManager().updateDueDate(report);
			} catch (Exception e) {
				getLogger().error("更新催办期限失败!",e);
			}
		}
		// 控制页面上的字段都不能编辑
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		initForm();
		//自定义返回页面时返自定义的地址
		String customInputTypeFormUrl = Struts2Utils.getParameter("customInputTypeFormUrl");
		if(StringUtils.isNotEmpty(customInputTypeFormUrl)){
			return customInputTypeFormUrl;
		}
		return "input";
	}
	
	/**
	 * 列表数据
	 */
	@Action("list-state")
	@LogInfo(optType="查询",message="查询数据")
	public String getListStates() throws Exception {
		String type = Struts2Utils.getParameter("type");
		User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
		String subName=user.getSubCompanyName();
		Integer weight =user.getWeight();
		String dept =acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName();
		String code = ContextUtils.getLoginName();
		try{
			if(type==null){
				type="N";
			}
			if("供应商".equals(dept)){
				page = supplierYearInspectManager.listState(page, type,null,code);
			}else if(weight==3){
				page = supplierYearInspectManager.listState(page, type,null,null);
			}else{
				page = supplierYearInspectManager.listState(page, type,subName,null);
			}
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			log.error("查询失败!",e);
		}
		return null;
	}
	@Action("seeYearInspect")
	public String seeYearInspect(){
		String userId = Struts2Utils.getParameter("id");
		Boolean bl=supplierYearInspectManager.auditSuppliers(userId);
		String ble="";
		if(bl){
			ble="Y";
		}
		return renderHtml(ble);
	}
	public void isCountersign(SupplierYearInspect obj){
		String y="Y";
		if(obj.getRdCheckerLog()!=null){
			obj.setRdValue(y);
		}
		if(obj.getProjectCheckerLog()!=null){
			obj.setProValue(y);
		}
		if(obj.getSqeCheckerLogin()!=null){
			obj.setSqeValue(y);
		}
		if(obj.getPurchaseProcesserLogin()!=null){
			obj.setPurValue(y);
		}
		supplierYearInspectManager.saveEntity(obj);
	}
	@Action("release")
	public String release() throws Exception {
		String params = Struts2Utils.getParameter("par");
		String params1 = Struts2Utils.getParameter("par1");
		String formNo = Struts2Utils.getParameter("formNo");
		String url =Struts2Utils.getParameter("url");
		try {
			if(params!=null&&!"".equals(params)){
				supplierYearInspectManager.releaseEmail(params,url);
			}
			if(params1!=null&&!"".equals(params1)){
				supplierYearInspectManager.releaseEmail1(params1,url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//创建供应商
	private void createSupplierUser(SupplierYearInspect report) {
	   //查出供应商部门
	   Department dept = supplierYearInspectManager.searchSupplierDept();
	   //检查是否存在供应商，不存在就插入
	   supplierYearInspectManager.saveUser(report,dept);
				
	}
	@Override
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		try {
			String str=supplierYearInspectManager.deleteEntity(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据:单号:"+str);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除数据信息失败",e);
		}
		return null;
	}
	
	/**
	 * 列表页面
	 */
	@Override
	@Action("list")
	public String list() throws Exception {			
		ActionContext.getContext().put("systemCodes", ApiFactory.getSettingService().getOptionsByGroupCode("supplier-system-code"));
		return SUCCESS;
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
