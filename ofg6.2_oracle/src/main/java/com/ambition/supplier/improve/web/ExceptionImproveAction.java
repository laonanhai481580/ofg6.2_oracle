package com.ambition.supplier.improve.web;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.iqc.inspectionreport.service.IncomingInspectionActionsReportManager;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.baseInfo.services.SupplierMaterialTypeGoalManager;
import com.ambition.supplier.develop.services.SupplierDevelopManager;
import com.ambition.supplier.entity.ExceptionContact;
import com.ambition.supplier.entity.ExceptionImprove;
import com.ambition.supplier.improve.services.ExceptionContactManager;
import com.ambition.supplier.improve.services.ExceptionImproveManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.tools.StringUtils;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;
import com.opensymphony.xwork2.ActionContext;

/**    
 * 物料异常矫正单
 * @authorBy LPF
 *
 */
@Namespace("/supplier/improve-new/exception-improve")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/improve-new/exception-improve", type = "redirectAction")})
public class ExceptionImproveAction extends AmbWorkflowActionBase<ExceptionImprove>{
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SupplierDevelopManager supplierDevelopManager;
	@Autowired
	private ExceptionImproveManager exceptionImproveManager;
	@Autowired
	private ExceptionContactManager exceptionContactManager;
	@Autowired
	private IncomingInspectionActionsReportManager iiarManager;
	private String ids;
    @Autowired
    private FormCodeGenerated formCodeGenerated;
    @Autowired
    private AcsUtils acsUtils;
    boolean isCurrent = false;
    private String nowTaskName; 
    private String selectType;
    private String assignee; //指派人
    private boolean onlyView=false;//
    private ExceptionImprove exceptionImprove;
	@Autowired
	private SupplierMaterialTypeGoalManager supplierMaterialTypeGoalManager;
	@Override
	protected AmbWorkflowManagerBase<ExceptionImprove> getAmbWorkflowBaseManager() {
		return exceptionImproveManager;
	}
	public String getNowTaskName() {
		 nowTaskName = report.getWorkflowInfo().getCurrentActivityName();  
	     return nowTaskName;
	}
	public void setNowTaskName(String nowTaskName) {
		this.nowTaskName = nowTaskName;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}	
	public boolean isOnlyView() {
		return onlyView;
	}
	public void setOnlyView(boolean onlyView) {
		this.onlyView = onlyView;
	}

	private List<String> returnableTaskNames = new ArrayList<String>();

	public List<String> getReturnableTaskNames() {
		returnableTaskNames = ApiFactory.getTaskService()
				.getReturnableTaskNames(taskId);
		return returnableTaskNames;
	}

	public void setReturnableTaskNames(List<String> returnableTaskNames) {
		this.returnableTaskNames = returnableTaskNames;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public boolean isCurrent() {
		return isCurrent;
	}
	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}
	
	public String getSelectType() {
		return selectType;
	}
	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}	
	
	public ExceptionImprove getExceptionImprove() {
		return exceptionImprove;
	}
	public void setExceptionImprove(ExceptionImprove exceptionImprove) {
		this.exceptionImprove = exceptionImprove;
	}
	/**
     * 方法名: 初始化的参数在这里写
     * <p>功能说明：</p>
    */
   public void initForm(){
       if(getId()==null){
    	   report.setCreatedTime(new Date());
    	   report.setFormNo(formCodeGenerated.getExceptionImproveCode());
    	   report.setCreator(ContextUtils.getLoginName());
    	   report.setCreatorName(ContextUtils.getUserName());
    	   report.setInspector(ContextUtils.getUserName());
    	   report.setBusinessUnitName(ContextUtils.getCompanyName());
    	   report.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
    	   report.setHappenSpace("进料检验");
    	   report.setSourceUnit(PropUtils.getProp("companyName"));
    	   report.setSponsorDate(new Date());
    	   onlyView=false;
       }
       if(getId()==null){
    	   addImprove();
		}
       if(task!=null){
    	   report.setCurrentManLog(task.getTransactor());
    	   report.setCurrentMan(task.getTransactorName());
			if(ContextUtils.getLoginName().equals(task.getTransactor())){
				isCurrent = true;				
				ActionContext.getContext().put("isCurrent",isCurrent);
			}
			 List<String[]> list=getAmbWorkflowBaseManager().getTaskHander(report);
			 if(list.size()>0){
				 Object objs[] = (Object[])list.get(0);
				 report.setCurrentManLog(objs[0].toString());
				 report.setCurrentMan(objs[1].toString());
			 }
		}
       HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
       if(report.getExceptionType()!=null){
    	   String[] exceptionTypeArr = report.getExceptionType().split(",");
    	   List<String> list = new ArrayList<String>();  
    	   for(int i = 0;i < exceptionTypeArr.length; i++){  
    	       list.add(exceptionTypeArr[i].trim());  
    	   }  
    	   request.setAttribute("exceptionTypeList", list); 
       }
     ActionContext.getContext().put("isImproves",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-is-improve"));
     ActionContext.getContext().put("mrbApplys",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-improve-mrb-applys"));
     ActionContext.getContext().put("dealOpinions",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-improve-deal-opinions"));
     ActionContext.getContext().put("happenSpaces",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-improve-happenSpaces"));
     ActionContext.getContext().put("productStages",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-product-stage"));
     ActionContext.getContext().put("labTestResults",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-lab-test-result"));
     ActionContext.getContext().put("exceptionTypes",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-exception_type_new"));
     ActionContext.getContext().put("exceptionStages",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-exception_stage"));
     ActionContext.getContext().put("exceptionDegrees",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-exception_degree"));
   }
	/**
	 * 判定是否发起过改进
	 */
	@Action("is-exist-improve")
	public String isExistImprove() throws Exception {
		JSONObject obj = new JSONObject();
		String formNo=Struts2Utils.getParameter("id");	
		boolean flag=false;
		if(formNo!=null&&!"".equals(formNo)){
			flag=exceptionImproveManager.isExistExceptionContact(Long.valueOf(formNo));
		}
		if(flag){
			obj.put("error",true);
		}		
		this.renderText(obj.toString());
		return null;
	}	
	public void addImprove(){
		String formNo=Struts2Utils.getParameter("formId");
		if(formNo!=null&&!"".equals(formNo)){
			ExceptionContact report=exceptionContactManager.getEntity(Long.valueOf(formNo));
			if(report!=null){
				getReport().setBusinessUnitName(report.getBusinessUnitName());
				getReport().setHappenSpace(report.getHappenSpace());
				getReport().setBillingArea(report.getBillingArea());
				getReport().setInspector((ContextUtils.getUserName()));
				getReport().setProductStage(report.getProductStage());
				getReport().setSupplierName(report.getSupplierName());
				getReport().setSupplierCode(report.getSupplierCode());
				getReport().setExceptionContactId(report.getId().toString());
				getReport().setExceptionContactNo(report.getFormNo());
				getReport().setInspectionId(report.getInspectionId());
				getReport().setInspectionFormNo(report.getInspectionFormNo());
				getReport().setSupplierEmail(report.getSupplierEmail());
				getReport().setBomName(report.getBomName());
				getReport().setBomCode(report.getBomCode());
				getReport().setInspectionDate(report.getInspectionDate());
				getReport().setCheckAmount(report.getCheckAmount());	
				getReport().setIncomingAmount(report.getIncomingAmount());
				getReport().setMaterialType(report.getMaterialType());
				getReport().setUnits(report.getUnits());
				getReport().setExceptionStage(report.getExceptionStage());
				getReport().setExceptionType(report.getExceptionType());
				getReport().setExceptionDegree(report.getExceptionDegree());
				getReport().setSurfaceBadRate(report.getSurfaceBadRate());
				getReport().setSurfaceBad(report.getSurfaceBad());
				getReport().setSurfaceBadState(report.getSurfaceBadState());
				getReport().setSponsorDate(report.getSponsorDate());
				getReport().setDescFile(report.getDescFile());
				if(StringUtils.isNotEmpty(report.getReturnReportNo())||StringUtils.isNotEmpty(report.getSqeMrbReportNo())){
					getReport().setSqeProcessOpinion(report.getSqeProcessOpinion());
					getReport().setReturnReportNo(report.getReturnReportNo());
					getReport().setSqeMrbReportNo(report.getSqeMrbReportNo());
				}
				getReport().setMqeChecker(report.getMqeChecker());
				getReport().setMqeCheckerLog(report.getMqeCheckerLog());
				
				getReport().setSizeBadRate(report.getSizeBadRate());
				getReport().setSizeBad(report.getSizeBad());
				getReport().setSizeBadState(report.getSizeBadState());
				
				getReport().setFunctionBadRate(report.getFunctionBadRate());
				getReport().setFunctionBad(report.getFunctionBad());
				getReport().setFunctionBadState(report.getFunctionBadState());
				
				getReport().setFeaturesBadRate(report.getFeaturesBadRate());
				getReport().setFeaturesBad(report.getFeaturesBad());
				getReport().setFeaturesBadState(report.getFeaturesBadState());
				getReport().setBadDesc(report.getBadDesc());
			}
		}
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		ActionContext.getContext().getValueStack().push(getReport());
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
		//emailTemplateManager.triggerTaskEmail(report,exceptionImproveManager.getEntityInstanceClass(),null);
		initForm();
		// 控制页面上的字段都不能编辑
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
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

   /**
    * 创建返回消息
    * @param error
    * @param message
    * @return
    */
   public void createErrorMessage(String message){
       Map<String,Object> map = new HashMap<String, Object>();
       map.put("error",true);
       map.put("message",message);
       renderText(JSONObject.fromObject(map).toString());
   }
   /**
    * 创建返回消息
    * @param error
    * @param message
    * @return
    */
   public void createMessage(String message){
       Map<String,Object> map = new HashMap<String, Object>();
       map.put("error",false);
       map.put("message",message);
       renderText(JSONObject.fromObject(map).toString());
   }
	/**
	 * 新建页面
	 */
	@Override
	@Action("input")
	public String input() throws Exception {
		initForm();
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		if (report != null && report.getWorkflowInfo() != null) {
			List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(report);
			if(!opinionParameters.isEmpty() && opinionParameters.size() != 0) {
				Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
			} else {
				Struts2Utils.getRequest().setAttribute("opinionParameters",	new ArrayList<Opinion>());
			}
		}
		return SUCCESS;
	} 
	/**
	 * 保存
	 */
	@Override
	@Action("save")
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
			}
			addActionMessage("保存成功!");
		} catch (Exception e) {
			getLogger().error("保存"+getAmbWorkflowBaseManager().getWorkflowDefinitionName()+"失败!",e);
			addActionMessage("保存"+getAmbWorkflowBaseManager().getWorkflowDefinitionName()+"失败," + e.getMessage());
			if(id != null){
				report = getAmbWorkflowBaseManager().getEntity(id);
			}else if(taskId != null){
				report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
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
		//判断是否手机端发起的请求
		return returnurl;
	}
	/**
	 * 办理任务页面
	 * @return
	 */
	@Action("process-task")
	@LogInfo(optType="办理",message="办理任务")
	public String task() throws Exception {
		Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "办理任务");
		task=getAmbWorkflowBaseManager().getWorkflowTask(taskId);
		if(task==null){
			HttpServletResponse response= ServletActionContext.getResponse();
			response.setContentType("text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			PrintWriter out = response.getWriter();
			out.println("<script language='javascript'>");
			out.println("alert('该任务已经失效!')");
			out.println("window.location.href='"+PropUtils.getProp("taskAdress")+"';");
			out.println("</script>");
			out.flush();
			out.close();
		}
		initForm();
		getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
		//办理前自动填写域设值
		if(taskId!=null){
			ApiFactory.getFormService().fillEntityByTask(report, taskId);
			returnableTaskNames = ApiFactory.getTaskService().getReturnableTaskNames(taskId);
		}
		List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(report);
		if(!opinionParameters.isEmpty() && opinionParameters.size()!=0){
			Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
		}else{
			Struts2Utils.getRequest().setAttribute("opinionParameters", new ArrayList<Opinion>());
		}
  		return SUCCESS;
	}		
	
	/**
	 * 生成部门用户
	 * @return
	 */
	public StringBuffer generateDepartmentUserTree(){
		List<com.norteksoft.acs.entity.organization.Department> allDepartments = supplierDevelopManager.queryAllDepartments();
		List<User> allUsers = supplierDevelopManager.queryAllUsers();
		StringBuffer userHtml = new StringBuffer();
		List<com.norteksoft.acs.entity.organization.Department> parentDepts = queryChildrens(allDepartments,null);
		for(com.norteksoft.acs.entity.organization.Department dept : parentDepts){
			generateHtml(userHtml, dept, allDepartments, allUsers);
		}
		return userHtml;
	}
	private List<com.norteksoft.acs.entity.organization.Department> queryChildrens(List<com.norteksoft.acs.entity.organization.Department> allDepartments,Long parentId){
		List<com.norteksoft.acs.entity.organization.Department> children = new ArrayList<com.norteksoft.acs.entity.organization.Department>();
		for(com.norteksoft.acs.entity.organization.Department d : allDepartments){
			if(parentId==null){
				if(d.getParent()==null){
					children.add(d);
				}
			}else{
				if(d.getParent()!=null&&d.getParent().getId().equals(parentId)){
					children.add(d);
				}
			}
		}
		return children;
	}
	private void generateHtml(StringBuffer html,com.norteksoft.acs.entity.organization.Department dept,
			List<com.norteksoft.acs.entity.organization.Department> allDepartments,
			List<User> allUsers){
		List<User> users = queryUsersByDeptId(allUsers,dept==null?null:dept.getId());
		List<com.norteksoft.acs.entity.organization.Department> children = queryChildrens(allDepartments,dept==null?null:dept.getId());
		if(users.isEmpty()&&children.isEmpty()){
			//html.append("<p>"+dept.getName()+"</p>");
		}else{
			html.append("<li style=\"margin-left:20px;\">");
			html.append("<label><a href=\"javascript:;\" onclick=\"showdiv('"+(dept==null?"noId":dept.getName())+"')\" >"+(dept==null?"无部门用户":dept.getName())+"</a></label>");
			html.append("<div style=\"display:none;\" id="+(dept==null?"noId":dept.getName())+"><ul class=\"two\" style=\"margin-left:30px;\">");
			for(User user : users){
				html.append("<li><label><input  type=\"checkbox\" name='"+user.getName()+"' deptName="+(dept==null?"noId":dept.getName())+"  value='"+user.getLoginName()+"'><a  href=\"javascript:;\" >"+user.getName()+"</a></label></li>");
				
			}
			for(com.norteksoft.acs.entity.organization.Department child : children){
				generateHtml(html,child,allDepartments,allUsers);
			}
			html.append("</ul></div>");
			html.append("</li>");
		}
	}
	private List<User> queryUsersByDeptId(List<User> allUsers,Long deptId){
		List<User> users = new ArrayList<User>();
		for(User u : allUsers){
			if(deptId==null){
				if(u.getMainDepartmentId()==null){
					users.add(u);
				}
			}else{
				if(u.getMainDepartmentId()!=null&&u.getMainDepartmentId().equals(deptId)){
					users.add(u);
				}
			}
		}
		return users;
	}
   /**
    * 启动并提交流程
    */
   @Override
   @Action("submit-process")
   @LogInfo(optType="启动并提交流程")
   public String submitProcess() {
       boolean hasError = false;
       try{
           beforeSubmitCallback();
           if(report.getFormNo()==null){
           	  report.setFormNo(formCodeGenerated.getExceptionImproveCode());
			}
           //子表信息
           Map<String, List<JSONObject>> childMaps = getChildrenInfos();
           CompleteTaskTipType completeTaskTipType =  getAmbWorkflowBaseManager().submitProcess(report,childMaps);
           submitResult = getAmbWorkflowBaseManager().getCompleteTaskTipType(completeTaskTipType,report);
           Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "启动并提交流程成功!");
           addActionMessage("提交成功!");
           id = report.getId();
       }catch(RuntimeException e){
           hasError = true;
           Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "启动并提交流程失败!");
           addActionMessage("提交失败!");
           getLogger().error("启动并提交流程失败!", e);
           if(id != null){
               report = getAmbWorkflowBaseManager().getEntity(id);
           }else if(taskId != null){
               report = getAmbWorkflowBaseManager().getEntityByTaskId(taskId);
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
      // emailTemplateManager.triggerTaskEmail(report,exceptionImproveManager.getEntityInstanceClass(),null);
       initForm();
       // 控制页面上的字段都不能编辑
       getRight(taskId,getAmbWorkflowBaseManager().getWorkflowDefinitionCode());
       return "input";
   }
 
	/**
	 * 指派
	 * @return
	 */
	@Action("assign")
	@LogInfo(optType="指派",message="指派任务")
	public String assign(){
		try {
			getAmbWorkflowBaseManager().assign(taskId, assignee);
			getReport().setCurrentMan(ContextUtils.getUserName());
	        getReport().setCurrentManLog(ContextUtils.getLoginName());
			renderText("指派完成");
		} catch (Exception e) {
			renderText("指派失败!");
			getLogger().error("指派办理人员失败!",e);
		}
		return null;
	}  
   

   @Action("return-to-task")
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
   @Action("monitor-list")
   public String listMonitor() throws Exception {
       return SUCCESS;
   }
   @Action("monitor-list-datas")
   public String listMonitorDatas() throws Exception {
       try{
           page = getAmbWorkflowBaseManager().search(page);
           renderText(PageUtils.pageToJson(page));
       }catch(Exception e){
           getLogger().error("流程跟踪查询失败!",e);
       }
       return null;
   }
	/**
	 * 列表页面
	 */
	@Override
	@Action("list")
	public String list() throws Exception {
		selectType = Struts2Utils.getParameter("selectType");
		List<Option> selectTypes = ApiFactory.getSettingService().getOptionsByGroupCode("iqc-select-type");
		if(StringUtils.isEmpty(selectType)){
			if(selectTypes.size()>0){
				selectType = selectTypes.get(0).getValue();
			}
		}
		String department=CommonUtil.getMainDepartMent();
		if(department!=null&&"供应商".equals(department)){
			selectType="single";
		}
		ActionContext.getContext().put("selectTypes",selectTypes);
		ActionContext.getContext().put("systemCodes", ApiFactory.getSettingService().getOptionsByGroupCode("supplier-system-code"));
		return SUCCESS;
	}

	/**
	 * 列表页面
	 */
	@Action("hide-list")
	public String hideList() throws Exception {
		ActionContext.getContext().put("systemCodes", ApiFactory.getSettingService().getOptionsByGroupCode("supplier-system-code"));
		return SUCCESS;
	}	
	
	/**
	 * 列表数据
	 */
	@Action("hide-list-datas")
	public String getHideListDatas() throws Exception {
		try{
			page = exceptionImproveManager.searchHide(page);
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
			formNo=exceptionImproveManager.hiddenState(eid,type);
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
	 * 列表页面
	 */
	@Action("list-all")
	public String listAll() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 列表数据
	 */
	@Action("list-datas-all")
	public String getListDatasAll() throws Exception {
		try{
			page = exceptionImproveManager.searchAll(page);					
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			getLogger().error("查询失败!",e);
		}
		return null;
	}	
	
	/**
	 * 列表数据
	 */
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try{
			selectType = Struts2Utils.getParameter("selectType");
			String department=CommonUtil.getMainDepartMent();
			if(department!=null&&"供应商".equals(department)){
				selectType="single";
				page = exceptionImproveManager.searchSupplierSingle(page);
			}else{
				if(selectType!=null&&selectType.equals("single")){
					page = exceptionImproveManager.searchSingle(page);	
				}else{
					page = exceptionImproveManager.search(page);
				}
			}						
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			getLogger().error("查询失败!",e);
		}
		return null;
	}
	
	/**
	 * 导出表单
	 * */
	@Action("export-report")
	public String exportReport() {
		try{
			exceptionImproveManager.exportReport(id);
		}catch(Exception e){
			createMessage("导出表单失败:" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 导出台账
	 * @return
	 * @throws Exception
	 */
	@Action("export")
	@LogInfo(optType="导出",message="导出数据")
	public String export() throws Exception {
		Page<ExceptionImprove> page = new Page<ExceptionImprove>(65535);
		String department=CommonUtil.getMainDepartMent();
		if(department!=null&&"供应商".equals(department)){
			selectType="single";
			page = exceptionImproveManager.searchSupplierSingle(page);
		}else{
			if(selectType!=null&&selectType.equals("single")){
				page = exceptionImproveManager.searchSingle(page);	
			}else{
				page = exceptionImproveManager.search(page);
			}
		}
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,getAmbWorkflowBaseManager().getEntityListCode()),getAmbWorkflowBaseManager().getWorkflowDefinitionName()));
		return null;
	}
	
	/**
	 * 导出台账
	 * @return
	 * @throws Exception
	 */
	@Action("export-all")
	@LogInfo(optType="导出",message="导出汇总台账数据")
	public String exportAll() throws Exception {
		Page<ExceptionImprove> page = new Page<ExceptionImprove>(65535);
		page = exceptionImproveManager.searchAll(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"SUPPLIER_EXCEPTION_IMPROVE_ALL"),"物料异常矫正单汇总台账"));
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
		Page<ExceptionImprove> page = new Page<ExceptionImprove>(65535);
		page = exceptionImproveManager.searchHide(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,getAmbWorkflowBaseManager().getEntityListCode()),getAmbWorkflowBaseManager().getWorkflowDefinitionName()));
		return null;
	}
	@Override
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		try {
			String str=exceptionImproveManager.deleteEntity(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据:单号:"+str);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除数据信息失败",e);
		}
		return null;
	}		
}
