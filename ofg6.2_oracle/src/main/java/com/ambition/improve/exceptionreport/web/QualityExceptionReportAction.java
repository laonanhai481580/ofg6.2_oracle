package com.ambition.improve.exceptionreport.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.aftersales.baseinfo.service.CustomerListManager;
import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.carmfg.checkinspection.service.MfgCheckInspectionReportManager;
import com.ambition.carmfg.entity.MfgCheckInspectionReport;
import com.ambition.carmfg.entity.OqcProcedure;
import com.ambition.carmfg.factoryprocedure.service.OqcFactoryManager;
import com.ambition.carmfg.factoryprocedure.service.OqcProcedureManager;
import com.ambition.emailtemplate.service.EmailTemplateManager;
import com.ambition.improve.entity.QualityExceptionReport;
import com.ambition.improve.exceptionreport.service.QualityExceptionReportManager;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.baseInfo.services.SupplierMaterialTypeGoalManager;
import com.ambition.supplier.entity.SupplierMaterialTypeGoal;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.tools.StringUtils;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.opensymphony.xwork2.ActionContext;
/**
 * 类名:品质异常联络单Action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年10月25日 发布
 */
@Namespace("/improve/exception")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/improve/exception", type = "redirectAction") })
public class QualityExceptionReportAction extends AmbWorkflowActionBase<QualityExceptionReport>{

	private static final long serialVersionUID = 1L;
	@Autowired
	private MfgCheckInspectionReportManager mfgCheckInspectionReportManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private String ids;
	private String nowTaskName;
	private QualityExceptionReport qualityExceptionReport;
	private String currentActivityName;
	@Autowired
	private OqcProcedureManager oqcProcedureManager;
	@Autowired
	private OqcFactoryManager oqcFactoryManager;
	@Autowired
	private CustomerListManager customerListManager;
	@Autowired
	private QualityExceptionReportManager qualityExceptionReportManager;
	@Autowired
    private SupplierMaterialTypeGoalManager supplierMaterialTypeGoalManager;
	@Autowired
	private EmailTemplateManager emailTemplateManager;
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

	public QualityExceptionReport getQualityExceptionReport() {
		return qualityExceptionReport;
	}


	public void setQualityExceptionReport(
			QualityExceptionReport qualityExceptionReport) {
		this.qualityExceptionReport = qualityExceptionReport;
	}


	public String getCurrentActivityName() {
		return currentActivityName;
	}


	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}

	@Override
	protected AmbWorkflowManagerBase<QualityExceptionReport> getAmbWorkflowBaseManager() {
		return qualityExceptionReportManager;
	}
	/**
	  * 方法名: 初始化的参数在这里写
	  * <p>功能说明：</p>
	 */
	public void initForm(){
		if(getId()==null&&getReport().getFormNo()==null){
			getReport().setFormNo(formCodeGenerated.generateQualityExceptionReportNo());
			/*getReport().setExceptionDate(new Date());*/
			getReport().setPresentMan(ContextUtils.getUserName());
			getReport().setPresentManLogin(ContextUtils.getLoginName());
			getReport().setDutyMan4(ContextUtils.getUserName());
			getReport().setDutyMan4Login(ContextUtils.getLoginName());
		}
       if(getId()==null){
    	   addImprove();
		}
		ActionContext.getContext().put("businessUnits", ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits"));
		ActionContext.getContext().put("problemDegrees", ApiFactory.getSettingService().getOptionsByGroupCode("imp_problem_degree"));
		ActionContext.getContext().put("productStages", ApiFactory.getSettingService().getOptionsByGroupCode("imp_product_phase"));
		ActionContext.getContext().put("isshares",ApiFactory.getSettingService().getOptionsByGroupCode("ecm_yes_or_no"));
		ActionContext.getContext().put("classGroups",ApiFactory.getSettingService().getOptionsByGroupCode("mfg_class_group"));
		List<Option> factorys = oqcFactoryManager.listAllForOptions();
        ActionContext.getContext().put("factorys",factorys);
        List<Option> customers=customerListManager.listAllForOptions();
        ActionContext.getContext().put("customers",customers);
        List<OqcProcedure> procedures = oqcProcedureManager.getAllOqcProcedure();
        List<Option> procedureOptions = oqcProcedureManager.converOqcProcedureToList(procedures);
        ActionContext.getContext().put("procedures",procedureOptions);
        List<SupplierMaterialTypeGoal> list = supplierMaterialTypeGoalManager.getAllType();
        List<Option> materialTypes = supplierMaterialTypeGoalManager.converExceptionLevelToList(list);
        ActionContext.getContext().put("materialTypes",materialTypes);
        ActionContext.getContext().put("problemBelongs", ApiFactory.getSettingService().getOptionsByGroupCode("exception_problem_belong"));
	}

	public void addImprove(){
		String formNo=Struts2Utils.getParameter("formId");
		if(formNo!=null&&!"".equals(formNo)){
			MfgCheckInspectionReport report=mfgCheckInspectionReportManager.getMfgCheckInspectionReport(Long.valueOf(formNo));
			if(report!=null){
				getReport().setBusinessUnitName(report.getProcessSection());
				getReport().setFactory(report.getFactory());
				getReport().setProcedure(report.getWorkProcedure());
				getReport().setProductStage(report.getProductStage());
				getReport().setProcessCard(report.getProcessCard());
				getReport().setClassGroup(report.getWorkGroupType());
				getReport().setExceptionDate(report.getInspectionDate());
				getReport().setMfgReportNo(report.getInspectionNo());
				getReport().setMfgReportId(report.getId().toString());
			}
		}
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		ActionContext.getContext().getValueStack().push(getReport());
	}	
	
	
	/**
	 * 导出表单
	 * */
	@Action("export-report")
	@LogInfo(optType="导出表单",message="品质异常联络单")
	public String exportReport() {
		try{
			qualityExceptionReportManager.exportReport(id);
		}catch(Exception e){
			createMessage("导出表单失败:" + e.getMessage());
		}
		return null;
	}
	
	@Action("return-to-task")
	@Override
	public String returnToTask() throws Exception {
		JSONObject result = new JSONObject();
		try {
			String returnTaskName = Struts2Utils.getParameter("returnTaskName");
			String opinion = Struts2Utils.getParameter("opinion");
			//记录操作日志
			WorkflowTask task = ApiFactory.getTaskService().getTask(taskId);
			//保存记录
			Opinion opinionParameter = new Opinion();
	        opinionParameter.setCustomField("驳回");
	        opinionParameter.setOpinion(opinion);
	        opinionParameter.setTransactor(ContextUtils.getLoginName());
	        opinionParameter.setTransactorName(ContextUtils.getUserName());
	        opinionParameter.setTaskName(task.getName());
	        opinionParameter.setTaskId(taskId);
	        opinionParameter.setAddOpinionDate(new Date());
	        ApiFactory.getOpinionService().saveOpinion(opinionParameter);
	        //驳回操作
			ApiFactory.getTaskService().returnTaskTo(taskId,returnTaskName);
		} catch (Exception e) {
			//e.printStackTrace();
			result.put("error","驳回任务出错!请联系系统管理员!");
			 logger.error("驳回任务出错!",e);
		}
		renderText(result.toString());
		return null;
	}	
    /**
     * 创建返回消息
     * @param error
     * @param message
     * @return
     */
    public void createMessage(String message){
        Map<String,Object> map = new HashMap<String, Object>();
        //map.put("error",false);
        map.put("message",message);
        renderText(JSONObject.fromObject(map).toString());
    }
	/**
	 * 删除
	 */
	@Override
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		try {
			String str=qualityExceptionReportManager.deleteEntity(deleteIds);
			String str1=str.split("~")[0];
			String str2=str.split("~")[1];
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"删除异常矫正单,编号为:"+str1);
			createMessage(str2);			
		} catch (Exception e) {
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据失败!");
			renderText("删除失败!"+ e.getMessage());
			getLogger().error("删除失败!", e);
		}
		return null;
	}  

	@Action("share-list")
	public String shareList() throws Exception {
		return SUCCESS;
	}	
	
	@Action("share-list-datas")
	public String getOkListDatas() throws Exception {
		try {
			page = qualityExceptionReportManager.searchSharePage(page);
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	@Override
	@Action("list")
	public String list() throws Exception {
		return SUCCESS;
	}	
	
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try {
			page = qualityExceptionReportManager.searchPage(page);
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	@Action("share-export")
	@LogInfo(optType="导出",message="异常单共享案例")
	public String shareExports() throws Exception {
		try {
			Page<QualityExceptionReport> page = new Page<QualityExceptionReport>(65535);
			page = qualityExceptionReportManager.searchSharePage(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"异常单共享案例"));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		Page<QualityExceptionReport> page = new Page<QualityExceptionReport>(65535);
		page = qualityExceptionReportManager.searchHide(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,getAmbWorkflowBaseManager().getEntityListCode()),getAmbWorkflowBaseManager().getWorkflowDefinitionName()));
		return null;
	}
	@Action("cancle")
	@LogInfo(optType="数据",message="取消三地共享")
	public String recycleBack() throws Exception {
		JSONObject obj = new JSONObject();
		try {			
			String deleteIds=Struts2Utils.getParameter("ids");	
			qualityExceptionReportManager.cancle(deleteIds);
			obj.put("message","取消成功！");
		} catch (Exception e) {
			obj.put("error",true);
			obj.put("message","取消失败"+e.getMessage());
		}
		this.renderText(obj.toString());
		return null;
	}
	
	@Action("manager-no-select")
	public String managerNoSelect() throws Exception {
		JSONObject obj = new JSONObject();
		String formId=Struts2Utils.getParameter("formId");	
		String managerNo=Struts2Utils.getParameter("managerNo");	
		boolean flag=qualityExceptionReportManager.managerNoSelect(formId,managerNo);
		if(flag){
			obj.put("error",true);
		}else{
			obj.put("error",false);
		}
		this.renderText(obj.toString());
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
	public String getHideListDatas() throws Exception {
		try{
			page = qualityExceptionReportManager.searchHide(page);
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
			formNo=qualityExceptionReportManager.hiddenState(eid,type);
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
			str="标记数据为敏感数据";
		}else{
			str="取消敏感数据标记";
		}
		Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,str+"，编号为:"+formNo);
		renderText(result.toString());
		return null;
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
			//回写异常单号到IQC报告
			if(report.getMfgReportId()!=null){
				MfgCheckInspectionReport iiar=mfgCheckInspectionReportManager.getMfgCheckInspectionReport(Long.valueOf(report.getMfgReportId()));
				if(iiar!=null){
					iiar.setExceptionNo(report.getFormNo());
					iiar.setExceptionId(report.getId().toString());				
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
			emailTemplateManager.triggerTaskEmail(report,qualityExceptionReportManager.getEntityInstanceClass(),null);
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
			try {
				if(StringUtils.isEmpty(errorMessage)){
					getAmbWorkflowBaseManager().updateDueDate(report);
				}
			} catch (Exception e) {
				getLogger().error("更新催办期限失败!",e);
			}
			emailTemplateManager.triggerTaskEmail(report,qualityExceptionReportManager.getEntityInstanceClass(),null);
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
}
