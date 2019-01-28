package com.ambition.carmfg.patrol.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import com.ambition.carmfg.entity.InspectionPointTypeEnum;
import com.ambition.carmfg.entity.MfgManufactureMessage;
import com.ambition.carmfg.entity.MfgPatrolDetail;
import com.ambition.carmfg.entity.MfgPatrolReport;
import com.ambition.carmfg.entity.MfgPlantParameterItem;
import com.ambition.carmfg.entity.MfgSupplierMessage;
import com.ambition.carmfg.factoryprocedure.service.OqcFactoryManager;
import com.ambition.carmfg.patrol.service.MfgPatrolReportManager;
import com.ambition.iqc.entity.CheckItem;
import com.ambition.iqc.entity.MfgToMes;
import com.ambition.iqc.samplestandard.service.SampleCodeLetterManager;
import com.ambition.iqc.samplestandard.service.SampleSchemeManager;
import com.ambition.supplier.utils.DateUtil;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.ambition.webservice.QisToBackService;
import com.ibm.icu.text.SimpleDateFormat;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.AsyncMailUtils;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.product.web.wf.WorkflowActionSupport;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.opensymphony.xwork2.ActionContext;

/**
 * 类名:首检、巡检、末检 流程ACTION
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  吴旭明
 * @version 1.00 2016-9-3 发布
 */
@Namespace("/carmfg/inspection/patrol")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "carmfg/inspection/patrol", type = "redirectAction") })
public class MfgPatrolReportAction extends  WorkflowActionSupport<MfgPatrolReport> {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private OqcFactoryManager oqcFactoryManager;
	@Autowired
	private LogUtilDao logUtilDao;
	private MfgPatrolReport mfgPatrolReport;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private Long id;
	private String deleteIds;// 删除的ids
    private Long taskId;//工作流任务id
    private TaskProcessingResult taskTransact; //点击的按钮
    private String fieldPermission; // 字段权限
    private WorkflowTask task;
    private String assignee;// 指派人
    private String workCode;
    private String currentActivityName;//当前流程环节名称
    @Autowired
	private AcsUtils acsUtils;
	@Autowired
    private MfgPatrolReportManager mfgPatrolReportManager;
	@Autowired
	private SampleCodeLetterManager sampleCodeLetterManager;
	@Autowired
	private SampleSchemeManager sampleSchemeManager;
	private Page<MfgPatrolReport> page;
	
	private Page<MfgManufactureMessage> mmPage;
	
	private Page<MfgSupplierMessage> smPage;
	@Autowired
	private QisToBackService qisToBackService;
	@Override
	public MfgPatrolReport getModel() {
		return mfgPatrolReport;
	}

	@Override
	public String abandonReceive() {
		return null;
	}

	@Override
	public String addSigner() {
		return null;
	}

	@Override
	public String completeInteractiveTask() {
		return null;
	}
	@Action("complete-task")
	@Override
	public String completeTask() {
		try{
			mfgPatrolReport.setAuditTime(new Date());
			mfgPatrolReportManager.completeTaskCode(mfgPatrolReport, taskId, taskTransact);
			task = mfgPatrolReportManager.getWorkflowTask(mfgPatrolReport.getWorkflowInfo().getFirstTaskId());
			fieldPermission = mfgPatrolReportManager.getFieldPermissionByTaskId(mfgPatrolReport.getWorkflowInfo().getFirstTaskId());
			ActionContext.getContext().put("fieldPermission", fieldPermission==null?"":fieldPermission);
			//意见处理
			List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(mfgPatrolReport);
			if(opinionParameters!=null && !opinionParameters.equals("") && opinionParameters.size()!=0){
				Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
			}else{
				Struts2Utils.getRequest().setAttribute("opinionParameters", new ArrayList<Opinion>());
			}
			addActionMessage("操作成功");
		}catch(Exception e){
			logUtilDao.debugLog("流程提交失败", "流程提交失败"+e.getMessage());
			addActionMessage("流程提交失败"+e.getMessage());
			mfgPatrolReport=mfgPatrolReportManager.getMfgPatrolReport(id);
			e.printStackTrace();
		}
		reInput();
		return "process-task";
	}
	 /**
     * 方法名:导出Excel文件
     * <p>
     * 功能说明：
     * </p>
     * 
     * @param incomingInspectionActionsReport
     * @throws IOException
     */
    @Action("download-report")
    @LogInfo(optType = "导出", message = "导出巡检报告单")
    public void exportReport() throws Exception {
        try {
            prepareModel();
            mfgPatrolReportManager.exportReport(mfgPatrolReport);
        } catch (Exception e) {
            e.printStackTrace();
            renderText("导出失败:" + e.getMessage());
        }
    }

	@Override
	public String drawTask() {
		return null;
	}

	@Override
	public String fillOpinion() {
		return null;
	}

	@Override
	public String processEmergency() {
		return null;
	}

	@Override
	public String removeSigner() {
		return null;
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

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public TaskProcessingResult getTaskTransact() {
		return taskTransact;
	}

	public void setTaskTransact(TaskProcessingResult taskTransact) {
		this.taskTransact = taskTransact;
	}

	public String getFieldPermission() {
		return fieldPermission;
	}

	public void setFieldPermission(String fieldPermission) {
		this.fieldPermission = fieldPermission;
	}

	public WorkflowTask getTask() {
		return task;
	}

	public void setTask(WorkflowTask task) {
		this.task = task;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getCurrentActivityName() {
		return currentActivityName;
	}

	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}

	public Page<MfgPatrolReport> getPage() {
		return page;
	}

	public void setPage(Page<MfgPatrolReport> page) {
		this.page = page;
	}

	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	public Page<MfgManufactureMessage> getMmPage() {
		return mmPage;
	}

	public void setMmPage(Page<MfgManufactureMessage> mmPage) {
		this.mmPage = mmPage;
	}

	public Page<MfgSupplierMessage> getSmPage() {
		return smPage;
	}

	public void setSmPage(Page<MfgSupplierMessage> smPage) {
		this.smPage = smPage;
	}

	@Action("retrieve")
	@Override
	public String retrieveTask() {
		String msg = mfgPatrolReportManager.retrieve(taskId);
		this.renderText(msg);
		return null;
	}
	@Action("history")
	@Override
	public String showHistory() {
        try {
        	mfgPatrolReport = mfgPatrolReportManager.getEntityByTaskId(taskId);
            task = mfgPatrolReportManager.getWorkflowTask(taskId);
            opinions = ApiFactory.getOpinionService().getOpinions(mfgPatrolReport);
            ActionContext.getContext().getValueStack().push(mfgPatrolReport);
        } catch (Exception e) {
        	logUtilDao.debugLog("查看流转历史出错", e.getMessage());
        }
        return SUCCESS;
	}
    public void prepareReceiveTask() throws Exception {
        prepareModel();
    }
    public void prepareCompleteTask() throws Exception {
        prepareModel();
        
    }
    public void prepareProcessTask() throws Exception {
        prepareModel();
    }
    public void prepareProcessEmergency() throws Exception {
        prepareModel();
    }
    
    public void prepareDistributeTask() throws Exception {
        prepareModel();
    }
    public void prepareSubmitProcess() throws Exception {
        prepareModel();
    }
	
	/** 
	  * 方法名:获取请求参数 
	  * @return
	 */
	public static List<JSONObject> getRequestCheckItems(){
		String flagIds = Struts2Utils.getParameter("flagIds");
		if(StringUtils.isEmpty(flagIds)){
			return null;
		}
		String[] flags = flagIds.split(",");
		Map<String,JSONObject> flagMaps = new HashMap<String, JSONObject>();
		for(String flag : flags){
			if(StringUtils.isNotEmpty(flag)){
				JSONObject obj = new JSONObject();
				obj.put("flagIndex",flag.substring(1));
				flagMaps.put(flag,obj);	
			}
		}
		Map<String,String> paramMap = Struts2Utils.getRequest().getParameterMap();
		for(String key : paramMap.keySet()){
			if(key.indexOf("_")>0){
				String flag = key.substring(0,key.indexOf("_"));
				String fieldName = key.substring(key.indexOf("_")+1);
				if(flagMaps.containsKey(flag)){
					flagMaps.get(flag).put(fieldName,Struts2Utils.getParameter(key));
				}
			}
		}
		List<JSONObject> arrays = new ArrayList<JSONObject>();
		for(JSONObject json : flagMaps.values()){
			arrays.add(json);
		}
		Collections.sort(arrays,new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject o1, JSONObject o2) {
				if(o1.getInt("flagIndex")<o2.getInt("flagIndex")){
					return 0;
				}else if(o1.getInt("flagIndex")==o2.getInt("flagIndex")){
					return -1;
				}else{
					return 1;
				}
			}
		});
		return arrays;
	}
	@Action("submit-process")
	@Override
	public String submitProcess() {
        if(id != null){
        	mfgPatrolReport.setLastModifiedTime(new Date());
        	mfgPatrolReport.setLastModifier(ContextUtils.getUserName());       	
        }
        mfgPatrolReport.setInspectionDate(new Date());
        try {
			List<JSONObject> checkItems =getRequestCheckItems();
            mfgPatrolReportManager.submitProcess(mfgPatrolReport,checkItems,InspectionPointTypeEnum.STORAGEINSPECTION);
            if( mfgPatrolReport.getWorkflowInfo()!=null){
            	 taskId = mfgPatrolReport.getWorkflowInfo().getFirstTaskId();
                 task = ApiFactory.getTaskService().getTask(taskId);
            }            
            	addActionMessage("提交成功!");
            	//邮件提醒责任人
            	sendEmail(mfgPatrolReport);          		           
            id=mfgPatrolReport.getId();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("流程提交失败"+e.getMessage(),e);
            addActionMessage("提交失败:" + e.getMessage());
        }
        reInput();
        getRight(taskId,MfgPatrolReportManager.MFG_CODE);
        return "input";
	}
	public void sendEmail(MfgPatrolReport mfgPatrolReport) {
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String inspectionDate = sdf.format(mfgPatrolReport.getInspectionDate());
		String message = "*最新预警信息:【"+inspectionDate+"、"+mfgPatrolReport.getProcessSection()+"】、" +"机种为【"+mfgPatrolReport.getMachineNo()+"】的巡检单判定为NG，请注意！单号："+mfgPatrolReport.getInspectionNo();
		if(mfgPatrolReport.getDutyManLogin()!=null&&!"".equals(mfgPatrolReport.getDutyManLogin())&&mfgPatrolReport.getInspectionConclusion().equals("NG")){
			String email = ApiFactory.getAcsService().getUserByLoginName(mfgPatrolReport.getDutyManLogin()).getEmail();
			if(StringUtils.isNotEmpty(email)){
				AsyncMailUtils.sendMail(email,"制造过程异常预警",message);
			}
    	}
	}
	
	@Action("delete")
	@LogInfo(optType = "删除")
	@Override
	public String delete() throws Exception {
		try{
			String message = mfgPatrolReportManager.deleteMfgPatrolReport(deleteIds);
			String str1=message.split("~")[0];
			String str2=message.split("~")[1];
			renderText(str2);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"删除巡检报告，编号为:"+str1);
        }catch(Exception e){
        	e.printStackTrace();
            renderText("删除失败:" + e.getMessage());
        }
		return null;
	}
	@Action("input")
	@Override
	public String input() throws Exception {
	    if(mfgPatrolReport.getWorkflowInfo()==null||mfgPatrolReport.getWorkflowInfo().getFirstTaskId()==null){
            taskId = null;
        }else{
            taskId = mfgPatrolReport.getWorkflowInfo().getFirstTaskId();
            currentActivityName=mfgPatrolReport.getWorkflowInfo().getCurrentActivityName();
        }
	    List<MfgPatrolDetail> checkItems = null;
	    Struts2Utils.getRequest().setAttribute("checkItems",checkItems);
	    reInput();
        getRight(taskId,MfgPatrolReportManager.MFG_CODE);
        return SUCCESS;
	}
	@Action("input-spc")
	public String inputSpc() throws Exception {
		JSONObject result = new JSONObject();
	    String spcSampleId = Struts2Utils.getParameter("spcSampleId");
	    try{
	    	 String reportId = mfgPatrolReportManager.getReportIdBySpcSmapleId(spcSampleId);
	    	 result.put("error", false);
	    	 result.put("reportId", reportId);
	    }catch(Exception e){
	    	result.put("error", true);
	    	result.put("message", "找不到对应表单");
	    }
	    renderText(result.toString());
        return null;
	}
	 /**
		 * 抄送
		 * @return
		 */
	@Action("copy-tasks")
	public String copyTasks(){
		mfgPatrolReportManager.createCopyTasks(taskId,Arrays.asList(assignee.split(",")), null, null);
		renderText("已抄送");
		return null;
	}
	/**
     * 指派
     * @return
     */
    @Action("assign")
    public String assign(){
    	try {
    		mfgPatrolReportManager.assign(taskId, assignee);
		} catch (Exception e) {
			renderText("指派失败!" + e.getMessage());
			logUtilDao.debugLog("指派失败!",e.getMessage());
		}
        return null;
    }
	@SuppressWarnings("unchecked")
	@Action("check-items")
	public String getCheckItems() throws Exception {
		try {
			String workProcedure = Struts2Utils.getParameter("workProcedure");
			String checkBomCode = Struts2Utils.getParameter("checkBomCode");
			String str = Struts2Utils.getParameter("stockAmount");
			Integer stockAmount =null;
			if(str!=null&&!"".equals(str)){
				 stockAmount = Integer.valueOf(str);
			}			
			List<JSONObject> checkItemArrays = MfgPatrolReportAction.getRequestCheckItems();
			String inspectionPointType=Struts2Utils.getParameter("inspectionPointType");
			Map<String,Object> resultMap = mfgPatrolReportManager.getCheckItems(workProcedure,checkBomCode,stockAmount,checkItemArrays, inspectionPointType);
			
			List<MfgPatrolDetail> checkItems = (List<MfgPatrolDetail>)resultMap.get("checkItems");
			Struts2Utils.getRequest().setAttribute("checkItems",checkItems);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	/**
	  * 方法名: 待检验的项目
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("wait-checked-items")
	public String getWaitCheckedItems() throws Exception{
		List<MfgPatrolDetail> checkItems=null;
			String workProcedure = Struts2Utils.getParameter("workProcedure");
			String checkBomCode = Struts2Utils.getParameter("checkBomCode");
			Integer stockAmount =null;
			String str = Struts2Utils.getParameter("stockAmount");
			if(str!=null&&!"".equals(str)){
				 stockAmount = Integer.valueOf(str);
			}	
			List<JSONObject> checkItemArrays = MfgPatrolReportAction.getRequestCheckItems();
			String inspectionPointType=Struts2Utils.getParameter("inspectionPointType");
			Map<String,Object> resultMap = mfgPatrolReportManager.getCheckItems(workProcedure,checkBomCode,stockAmount,checkItemArrays, inspectionPointType);
			checkItems = (List<MfgPatrolDetail>)resultMap.get("checkItems");
//		}
		if(checkItems.size()!=0){
			Struts2Utils.getRequest().setAttribute("checkItems",checkItems);
		}else{
			Struts2Utils.getRequest().setAttribute("checkItems",new ArrayList<CheckItem>());
		}
		return SUCCESS;
	}
	
	/**
	  * 方法名: 验证项目状态
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("validate-item-status")
	public String getItemStatus(){
		String items=Struts2Utils.getParameter("items");
		String  itemNameStr="";
		String reportId=Struts2Utils.getParameter("reportId");
		id=reportId==""?null:Long.valueOf(reportId);
		if(id!=null){
			mfgPatrolReport=mfgPatrolReportManager.getMfgPatrolReport(id);
			if(mfgPatrolReport!=null&&mfgPatrolReport.getCheckItems()!=null){
				for(MfgPatrolDetail item:mfgPatrolReport.getCheckItems()){
					if(item.getCheckItemName().indexOf(items)>=0){
						itemNameStr+=item.getCheckItemName()+",";
					}
				}
			}
		}
		JSONObject jsonObj=new JSONObject();
		jsonObj.put("itemNameStr", itemNameStr);
		renderText(jsonObj.toString());
		return null;
	}	
	
    /**
     * 任务办理界面
     * @return
     * @throws Exception
     */
    @Action("process-task")
    public String processTask() throws Exception {
        if(mfgPatrolReport != null){
            List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(mfgPatrolReport);
            Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
        }else if(taskId != null){
        	mfgPatrolReportManager.deleteFormFlowable(taskId);
        }
        workCode=task.getCode();
        reInput();
        this.getRight(taskId,MfgPatrolReportManager.MFG_CODE);
        return SUCCESS;
    }
	@Override
	public String list() throws Exception {
		return null;
	}
	@Action("wait-audit-list")
	public String waitAudit(){
		return SUCCESS;
	}
	@Action("wait-audit-list-datas")
	public String waitAuditListData(){
		page = mfgPatrolReportManager.waitAuditList(page);
		this.renderText(PageUtils.pageToJson(page));
		return null;
	}	

	@Action("recheck-list")
	public String recheck(){
		return SUCCESS;
	}
	@Action("recheck-list-datas")
	public String recheckListData(){
		page = mfgPatrolReportManager.recheckList(page);
		this.renderText(PageUtils.pageToJson(page));
		return null;
	}
	
	@Action("checking-list")
	public String checkingList(){
		return SUCCESS;
	}
	@Action("checking-list-datas")
	public String checkingListData(){
		page = mfgPatrolReportManager.checkingList(page);
		this.renderText(PageUtils.pageToJson(page));
		return null;
	} 	
	/**
	  * 方法名:重检导出 
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("wait-audit-export")
	@LogInfo(optType = "导出", message = "导出巡检报告台账")
	public String waitAuditExport() throws Exception {
		try {
			Page<MfgPatrolReport> page = new Page<MfgPatrolReport>(100000);
			page = mfgPatrolReportManager.recheckList(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_PATROL_REPORT"),"检验报告"));
		} catch (Exception e) {
			logUtilDao.debugLog("重检导出失败", e.getMessage());
		}
		return null;
	}	
	/**
	  * 方法名:待审核导出 
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("recheck-export")
	@LogInfo(optType = "导出", message = "导出巡检报告台账")
	public String recheckExport() throws Exception {
		try {
			Page<MfgPatrolReport> page = new Page<MfgPatrolReport>(100000);
			page = mfgPatrolReportManager.waitAuditList(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_PATROL_REPORT"),"检验报告"));
		} catch (Exception e) {
			logUtilDao.debugLog("待审核导出失败", e.getMessage());
		}
		return null;
	}	
	
	/**
	  * 方法名:检验中导出 
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("checking-export")
	@LogInfo(optType = "导出", message = "导出巡检报告台账")
	public String checkingExport() throws Exception {
		try {
			Page<MfgPatrolReport> page = new Page<MfgPatrolReport>(100000);
			page = mfgPatrolReportManager.checkingList(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_PATROL_REPORT"),"检验报告"));
		} catch (Exception e) {
			logUtilDao.debugLog("待审核导出失败", e.getMessage());
		}
		return null;
	}	
	@Action("patrol-list")
	public String patrolList(){
		return SUCCESS;
	}
	@Action("patrol-list-datas")
	public String patrolListData(){
		page = mfgPatrolReportManager.list(page);
		this.renderText(PageUtils.pageToJson(page));
		return null;
	}
	/**
	  * 方法名:巡检导出 
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("patrol-exports")
	@LogInfo(optType = "导出", message = "导出巡检报告台账")
	public String patrolExport() throws Exception {
		try {
			Page<MfgPatrolReport> page = new Page<MfgPatrolReport>(100000);
			page = mfgPatrolReportManager.list(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_PATROL_INSPECTION_REPORT"),"巡检报告"));
		} catch (Exception e) {
			logUtilDao.debugLog("巡检导出失败", e.getMessage());
		}		
		return null;
		
	}
	@Override
	protected void prepareModel() throws Exception {
		if(taskId!=null){
			mfgPatrolReport = mfgPatrolReportManager.getMfgPatrolReportByTaskId(taskId);
            task = mfgPatrolReportManager.getWorkflowTask(taskId);
            opinions = mfgPatrolReportManager.getOpinions(mfgPatrolReport);
            ApiFactory.getFormService().fillEntityByTask(mfgPatrolReport, taskId);
        }else if(id!=null){
        	mfgPatrolReport=mfgPatrolReportManager.getMfgPatrolReport(id);
            task = mfgPatrolReportManager.getMyTask(mfgPatrolReport,ContextUtils.getLoginName());
            if(task!=null)taskId = task.getId();
            if(task==null) taskId = mfgPatrolReport.getWorkflowInfo().getFirstTaskId();
            if(mfgPatrolReport.getWorkflowInfo()!=null) 
            opinions = mfgPatrolReportManager.getOpinions(mfgPatrolReport);   
        }else if(id==null){
        	mfgPatrolReport=new MfgPatrolReport();
        	mfgPatrolReport.setCompanyId(ContextUtils.getCompanyId());
        	mfgPatrolReport.setCreatedTime(new Date());
        	mfgPatrolReport.setCreator(ContextUtils.getUserName());
        	mfgPatrolReport.setInspector(ContextUtils.getUserName());
        	mfgPatrolReport.setInspectionNo(formCodeGenerated.generatePatrolCode());
        	mfgPatrolReport.setBusinessUnitName(ContextUtils.getSubCompanyName());
        	mfgPatrolReport.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));       	
        }
	}
	@Action("save")
	@Override
	public String save() throws Exception {
		try {
			List<JSONObject> checkItems = MfgPatrolReportAction.getRequestCheckItems();
			InspectionPointTypeEnum inEnum=InspectionPointTypeEnum.PATROLINSPECTION;
			mfgPatrolReportManager.saveMadeInspection(mfgPatrolReport,checkItems,inEnum);
			if(Struts2Utils.getParameter("isLedger") != null){
				renderText(JsonParser.getRowValue(mfgPatrolReport));
				return null;
			}else{
				addActionMessage("保存成功!");
				Struts2Utils.getRequest().setAttribute("accordionMenu","input");
				Struts2Utils.getRequest().setAttribute("inspectionLevels",sampleCodeLetterManager.getInspectionLevelOptions(sampleSchemeManager.getUseBaseType().getBaseType()));
				reInput();
				getRight(taskId,MfgPatrolReportManager.MFG_CODE);
				return "input";
			}
		} catch (Exception e) {
			logUtilDao.debugLog("保存失败",e.getMessage());
			mfgPatrolReport.setId(id);
			addActionMessage("保存失败:" + e.getMessage());
			reInput();
			return "input";
		}
	}
	@Action("view-info")
    public String viewInfo(){
        if(id != null){ 
        	mfgPatrolReport = mfgPatrolReportManager.getMfgPatrolReport(id);
            if(mfgPatrolReport.getWorkflowInfo()!=null){
                taskId=mfgPatrolReport.getWorkflowInfo().getFirstTaskId();
            }
        }else{
            addActionMessage("没有符合条件的检验报告单!");
        }
        getRight(taskId,MfgPatrolReportManager.MFG_CODE);
        reInput();
        fieldPermission = "[{request:\"false\",readonly:\"true\",controlType:\"allReadolny\"}]";
        if(mfgPatrolReport != null && mfgPatrolReport.getWorkflowInfo()!=null){
            List<Opinion> opinionParameters = ApiFactory.getOpinionService().getOpinions(mfgPatrolReport);
            if(opinionParameters!=null && !opinionParameters.equals("") && opinionParameters.size()!=0){
                Struts2Utils.getRequest().setAttribute("opinionParameters", opinionParameters);
            }else{
                Struts2Utils.getRequest().setAttribute("opinionParameters", new ArrayList<Opinion>());
            }
        }
        ActionContext.getContext().getValueStack().push(mfgPatrolReport);
        return SUCCESS;
    }
    /**
     * 获取权限
     * @param taskId
     * @param defCode
     */
    private void getRight(Long taskId,String defCode) {
        if(taskId==null){
            fieldPermission = mfgPatrolReportManager.getFieldPermission(defCode);//禁止或必填字段
        }else{
            fieldPermission = mfgPatrolReportManager.getFieldPermissionByTaskId(taskId);//禁止或必填字段
            taskPermission = mfgPatrolReportManager.getActivityPermission(taskId); 
        }
    }
    private void reInput(){
    	//事业部
    	ActionContext.getContext().put("businessUnits",ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits"));
    	//处理方式
    	ActionContext.getContext().put("mfg_processing_result",ApiFactory.getSettingService().getOptionsByGroupCode("mfg_processing_result"));
    	//检验类型
    	ActionContext.getContext().put("inspection_models",ApiFactory.getSettingService().getOptionsByGroupCode("inspection_models"));
    	// 产品类别
    	ActionContext.getContext().put("mfg_category",ApiFactory.getSettingService().getOptionsByGroupCode("mfg_category"));
    	ActionContext.getContext().put("processSections",ApiFactory.getSettingService().getOptionsByGroupCode("processSections"));
    	//工序
    	ActionContext.getContext().put("mfg_process_bus",ApiFactory.getSettingService().getOptionsByGroupCode("mfg_work_procedure"));
    	//批次数量单位
    	ActionContext.getContext().put("amountUnits",ApiFactory.getSettingService().getOptionsByGroupCode("mfg_amount_unit"));
    	//楼层
    	ActionContext.getContext().put("floors",ApiFactory.getSettingService().getOptionsByGroupCode("mfg-floor"));
    	//生产线
    	ActionContext.getContext().put("productionLines",ApiFactory.getSettingService().getOptionsByGroupCode("mfg_productionLine"));
    	//班别
    	ActionContext.getContext().put("mfg_work_group_type",ApiFactory.getSettingService().getOptionsByGroupCode("mfg_work_group_type"));
    	//检验结果
    	ActionContext.getContext().put("iqc_okorng",ApiFactory.getSettingService().getOptionsByGroupCode("iqc_okorng"));
    	//产品阶段
    	ActionContext.getContext().put("productStages",ApiFactory.getSettingService().getOptionsByGroupCode("iqc_product_stage"));
    	List<Option> factorys = oqcFactoryManager.listAllForOptions();
        ActionContext.getContext().put("factorys",factorys);
    	List<MfgPatrolDetail> mfgCheckItems=null;
    	if(mfgPatrolReport.getCheckItems()==null){
			mfgCheckItems = new ArrayList<MfgPatrolDetail>();
		}else{
			mfgCheckItems=mfgPatrolReport.getCheckItems();
		}
    	List<String> listDate=null;
    	if(mfgCheckItems.size()>0){
			MfgPatrolDetail mfgPatrolDetail=mfgCheckItems.get(0);
			listDate=mfgPatrolReportManager.getListDate(mfgPatrolDetail);
		}
    	
	    Struts2Utils.getRequest().setAttribute("checkItems",mfgCheckItems);
	    Struts2Utils.getRequest().setAttribute("listDate",listDate);
	    List<MfgPlantParameterItem> parameterItems=null;
	    Struts2Utils.getRequest().setAttribute("plantDetails",parameterItems);
    }

    @Action("get-process-card")
    public String getReportInfomation(){
    	String processCard=Struts2Utils.getParameter("processCard");
    	String workProcedure=Struts2Utils.getParameter("workProcedure");
    	MfgToMes mes= new MfgToMes();
    	mes.setProcessCard(processCard);
    	mes.setWorkProcedure(workProcedure);
    	QisToBackService qbs=new QisToBackService();
    	String returnMessage="";
    	JSONObject obj=new JSONObject();
    	try {
    		returnMessage=mfgPatrolReportManager.mfgToBarch(qbs,mes);
    		obj.put("message", returnMessage);
		} catch (Exception e) {
			logUtilDao.debugLog("获取数据失败", returnMessage);
		}
    	renderText(obj.toString());
    	return null;
    }
    @Action("hiddenState")
    @LogInfo(optType="隐藏")
	public String hiddenState(){
		String eid = Struts2Utils.getParameter("id");
		String type = Struts2Utils.getParameter("type");
		String str=mfgPatrolReportManager.hiddenState(eid,type);
		if("Y".equals(type)){
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"巡检报告取消隐藏,编号为:"+str);
		}else{
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"隐藏巡检报告,编号为:"+str);
		}
		return null;
	}
	@Action("list-hid")
	public String listHid(){
		return SUCCESS;
	}
    @Action("list-hide-datas")
	@LogInfo(optType="查询",message="巡检报告隐藏数据")
	public String getListHid() throws Exception {
		try {
			page = mfgPatrolReportManager.listHid(page);
		} catch (Exception e) {
			logUtilDao.debugLog("获取数据失败!",e.getMessage());
		}
		this.renderText(PageUtils.pageToJson(page));
		return null;
	}
	/**
	  * 方法名:巡检导出 
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("patrol-exports-hide")
	@LogInfo(optType = "导出", message = "导出巡检报告隐藏台账")
	public String patrolExportHide() throws Exception {
		try {
			Page<MfgPatrolReport> page = new Page<MfgPatrolReport>(65535);
			page = mfgPatrolReportManager.listHid(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_PATROL_INSPECTION_REPORT"),"巡检报告"));
		} catch (Exception e) {
			logUtilDao.debugLog("巡检报告隐藏台账导出失败", e.getMessage());
		}		
		return null;
		
	}  
    
}
