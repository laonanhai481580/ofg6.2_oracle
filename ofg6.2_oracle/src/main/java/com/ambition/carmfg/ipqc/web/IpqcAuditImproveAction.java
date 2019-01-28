package com.ambition.carmfg.ipqc.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.carmfg.entity.IpqcAudit;
import com.ambition.carmfg.entity.IpqcAuditImprove;
import com.ambition.carmfg.entity.OqcProcedure;
import com.ambition.carmfg.factoryprocedure.service.OqcFactoryManager;
import com.ambition.carmfg.factoryprocedure.service.OqcProcedureManager;
import com.ambition.carmfg.ipqc.service.IpqcAuditImproveManager;
import com.ambition.carmfg.ipqc.service.IpqcAuditManager;
import com.ambition.carmfg.ort.service.OrtInspectionItemManager;
import com.ambition.emailtemplate.service.EmailTemplateManager;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.opensymphony.xwork2.ActionContext;
/**
 * 类名:IPQC稽核问题点改善Action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月9日 发布
 */
@Namespace("/carmfg/ipqc/ipqc-improve")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/carmfg/ipqc/ipqc-improve", type = "redirectAction") })
public class IpqcAuditImproveAction extends AmbWorkflowActionBase<IpqcAuditImprove>{

		/**
		  *IpqcAuditImproveAction.java2016年9月6日
		 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private String ids;
	private String nowTaskName;
	private IpqcAuditImprove ipqcAuditImprove;
	private String currentActivityName;
	@Autowired
	private IpqcAuditImproveManager ipqcAuditImproveManager;
	@Autowired
	private	IpqcAuditManager ipqcAuditManager;
	@Autowired
	private OrtInspectionItemManager ortInspectionItemManager;
	@Autowired
	private OqcFactoryManager oqcFactoryManager;
	@Autowired
	private OqcProcedureManager oqcProcedureManager;
	@Autowired
	private EmailTemplateManager emailTemplateManager;
	@Autowired
	private LogUtilDao logUtilDao;
	
	public String getIds() {
		return ids;
	}


	public void setIds(String ids) {
		this.ids = ids;
	}


	public String getNowTaskName() {
		return nowTaskName;
	}


	public void setNowTaskName(String nowTaskName) {
		this.nowTaskName = nowTaskName;
	}


	public IpqcAuditImprove getIpqcAuditImprove() {
		return ipqcAuditImprove;
	}


	public void setIpqcAuditImprove(IpqcAuditImprove ipqcAuditImprove) {
		this.ipqcAuditImprove = ipqcAuditImprove;
	}


	public String getCurrentActivityName() {
		return currentActivityName;
	}


	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}

	@Override
	protected AmbWorkflowManagerBase<IpqcAuditImprove> getAmbWorkflowBaseManager() {
		return ipqcAuditImproveManager;
	}
	/**
	 * 删除
	 */
	@Override
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		getLogger().info("删除记录");
		try {
			String str=ipqcAuditImproveManager.deleteEntity(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据成功!单号为："+str);
		} catch (Exception e) {
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据失败!");
			renderText("删除失败!"+ e.getMessage());
			getLogger().error("删除失败!", e);
		}
		return null;
	} 		
	
	/**
	  * 方法名: 初始化的参数在这里写
	  * <p>功能说明：</p>
	 */
	public void initForm(){
		if(getId()==null&&getReport().getFormNo()==null){
			getReport().setFormNo(formCodeGenerated.generateIpqcAuditImproveNo());
			getReport().setOperator(ContextUtils.getUserName());
			getReport().setOperatorLogin(ContextUtils.getLoginName());
		}	
		List<Option> factorys = oqcFactoryManager.listAllForOptions();
        ActionContext.getContext().put("factorys",factorys);
        List<OqcProcedure> procedures = oqcProcedureManager.getAllOqcProcedure();
        List<Option> customerPlaceOptions = oqcProcedureManager.converOqcProcedureToList(procedures);
        ActionContext.getContext().put("procedures",customerPlaceOptions);
        ActionContext.getContext().put("businessUnits", ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits"));
        ActionContext.getContext().put("problemDegrees", ApiFactory.getSettingService().getOptionsByGroupCode("mfg_ipqc_problem_degree"));
		ActionContext.getContext().put("classGroups", ApiFactory.getSettingService().getOptionsByGroupCode("mfg_class_group"));
		ActionContext.getContext().put("stations", ApiFactory.getSettingService().getOptionsByGroupCode("mfg_station"));
		ActionContext.getContext().put("auditTypes", ApiFactory.getSettingService().getOptionsByGroupCode("mfg_audit_type"));
		ActionContext.getContext().put("missing_items", ApiFactory.getSettingService().getOptionsByGroupCode("mfg_missing_items"));
	}
	
	/**
	 * 新建页面
	 */
	@Override
	@Action("input")
	public String input() throws Exception {
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		initForm();
		if (report != null && report.getWorkflowInfo() != null) {
			List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(report);
			if (!opinionParameters.isEmpty() && opinionParameters.size() != 0) {
				Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
			} else {
				Struts2Utils.getRequest().setAttribute("opinionParameters",	new ArrayList<Opinion>());
			}
		}
		String auditId=Struts2Utils.getParameter("auditId");
		if(auditId!=null&&auditId!=""){
			IpqcAudit ipqcAudit=ipqcAuditManager.getIpqcAudit(Long.valueOf(auditId));
			report.setBusinessUnitName(ipqcAudit.getBusinessUnitName());
			report.setProblemDegree(ipqcAudit.getProblemDegree());
			report.setAuditDate(ipqcAudit.getActualDate());
			report.setClassGroup(ipqcAudit.getClassGroup());
			report.setDepartment(ipqcAudit.getDepartment());
			report.setStation(ipqcAudit.getStation());	
			report.setFactory(ipqcAudit.getFactory());
			report.setAuditType(ipqcAudit.getAuditType());
			report.setMissingItems(ipqcAudit.getMissingItems());
			report.setPlanDate(ipqcAudit.getPlanDate());
			report.setProblemDescribe(ipqcAudit.getProblemDescribe());
			report.setAuditId(Long.valueOf(auditId));
		}
		return SUCCESS;
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
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "启动并提交流程成功!");
			addActionMessage("提交成功!");
			Long auditId=report.getAuditId();
			if(auditId!=null){
				IpqcAudit ipqcAudit=ipqcAuditManager.getIpqcAudit(Long.valueOf(auditId));
				ipqcAudit.setImproveId(report.getId().toString());
				ipqcAudit.setImproveNo(report.getFormNo());
			}	
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
		    	//ApiFactory.getFormService().fillEntityByTask(qrqcReport, taskId);
			}
			try {
				getAmbWorkflowBaseManager().updateDueDate(report);
			} catch (Exception e) {
				getLogger().error("更新催办期限失败!",e);
			}
		}
		emailTemplateManager.triggerTaskEmail(report,ipqcAuditImproveManager.getEntityInstanceClass(),null);
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
	 * 完成任务
	 */
	@Override
	@Action("complete-task")
	@LogInfo(optType="同意或者提交",message="完成任务")
	public String completeTask() {
		@SuppressWarnings("unused")
		CompleteTaskTipType completeTaskTipType=null;
		String errorMessage = null;
		try{
			beforeCompleteCallback();
			//子表信息
			Map<String, List<JSONObject>> childMaps = getChildrenInfos();
			completeTaskTipType =  getAmbWorkflowBaseManager().completeTask(report,childMaps,taskId, taskTransact);
			addActionMessage("流程处理成功!");
			
			afterCompleteCallback();
			//成功标志
			ActionContext.getContext().put("_successFlag",true);
		}catch(RuntimeException e){
			getLogger().error("流程处理失败！", e);
			addActionMessage("流程处理失败!");
			errorMessage = "处理失败," + e.getMessage();
			if(id != null){
				report = getAmbWorkflowBaseManager().getEntity(id);
			}else if(taskId != null){
				report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
				returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
			}
		} catch (Exception e) {
			getLogger().error("流程处理失败！设置特殊字段值出错", e);
			addActionMessage("流程处理失败!设置特殊字段值出错");
			errorMessage = "处理失败,设置特殊字段值出错," + e.getMessage();
			if(id != null){
				report = getAmbWorkflowBaseManager().getEntity(id);
			}else if(taskId != null){
				report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
				returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
			}
		}
		String currentActivityName=report.getWorkflowInfo().getCurrentActivityName();
		if(currentActivityName.equals("流程结束")){
			Long auditId=report.getAuditId();
			if(auditId!=null){
				IpqcAudit ipqcAudit=ipqcAuditManager.getIpqcAudit(Long.valueOf(auditId));
				ipqcAudit.setJiean("Close");
			}	
		}
		try {
			if(StringUtils.isEmpty(errorMessage)){
				getAmbWorkflowBaseManager().updateDueDate(report);
			}
		} catch (Exception e) {
			getLogger().error("更新催办期限失败!",e);
		}
		emailTemplateManager.triggerTaskEmail(report,ipqcAuditImproveManager.getEntityInstanceClass(),null);
		// 控制页面上的字段都不能编辑
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		initForm();
		
		List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(report);
		if(opinionParameters!=null && !opinionParameters.equals("") && opinionParameters.size()!=0){
			Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
		}else{
			Struts2Utils.getRequest().setAttribute("opinionParameters", new ArrayList<Opinion>());
		}
		//自定义返回页面时返自定义的地址
		String customInputTypeFormUrl = Struts2Utils.getParameter("customInputTypeFormUrl");
		if(StringUtils.isNotEmpty(customInputTypeFormUrl)){
			return customInputTypeFormUrl;
		}
		String returnurl = "inputform".equals(Struts2Utils.getParameter("inputformortaskform"))?"input":"process-task";
		return returnurl;
	}
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try {
			page = ipqcAuditImproveManager.searchPage(page);
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}	
	/**
	 * 列表页面
	 */
	@Action("hide-list")
	public String hideList() throws Exception {
		return SUCCESS;
	}	
	
	/**
	 * 列表数据
	 */
	@Action("hide-list-datas")
	@LogInfo(optType="查询",message="查询数据")
	public String getHideListDatas() throws Exception {
		try{
			page = ipqcAuditImproveManager.searchHide(page);
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			getLogger().error("查询失败!",e);
		}
		return null;
	}	
	@Action("hiddenState")
	@LogInfo(optType="隐藏")
	public String hiddenState(){
		JSONObject result = new JSONObject();
		String eid = Struts2Utils.getParameter("id");
		String type = Struts2Utils.getParameter("type");
		String formNo="";
		try {
			formNo=ipqcAuditImproveManager.hiddenState(eid,type);
			result.put("message", "操作成功!");
			result.put("error", false);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("error", true);
			result.put("message", e.getMessage());
		}	
		formNo=formNo.substring(0, formNo.length()-1);
		String str="";
		if("Y".equals(type)){
			str="IPQC改善报告取消敏感数据标记";
		}else{
			str="IPQC改善报告标记数据为敏感数据";
		}
		Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,str+"，稽核编号为:"+formNo);
		renderText(result.toString());
		return null;
	}	
	
	/**
	 * 导出台账
	 * @return
	 * @throws Exception
	 */
	@Action("export-hide")
	@LogInfo(optType="导出",message="导出敏感数据")
	public String exportHide() throws Exception {
		Page<IpqcAuditImprove> page = new Page<IpqcAuditImprove>(65535);
		page = ipqcAuditImproveManager.searchHide(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,getAmbWorkflowBaseManager().getEntityListCode()),getAmbWorkflowBaseManager().getWorkflowDefinitionName()));
		return null;
	}	
}
