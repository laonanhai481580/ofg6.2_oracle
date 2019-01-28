package com.ambition.qsm.inneraudit.web;

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
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.qsm.entity.Problems;
import com.ambition.qsm.inneraudit.service.ProblemsManager;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/qsm/inner-audit/problems")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/qsm/inner-audit/problems", type = "redirectAction") })
public class ProblemsAction extends AmbWorkflowActionBase<Problems>{

		/**
		  *ProblemsAction.java2017年5月11日
		 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private  ProblemsManager problemsManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private Logger log=Logger.getLogger(this.getClass());
	private File myFile;
	
	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	@Override
	protected AmbWorkflowManagerBase<Problems> getAmbWorkflowBaseManager() {
		// TODO Auto-generated method stub
		return problemsManager;
	}
	
	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public void initForm(){
		if(getId() == null&&getReport().getId()==null){
			getReport().setFormNo(formCodeGenerated.generateProblemsNo());
			getReport().setTaskCreatedTime(new Date());
			getReport().setTransactorName(ContextUtils.getUserName());
			getReport().setTransactor(ContextUtils.getLoginName());
			getReport().setConsignableDate(new Date());
			getReport().setConsignor(ContextUtils.getUserName());
			getReport().setDepartment(ContextUtils.getSubCompanyName());
			User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
			String subName=user.getSubCompanyName();
			getReport().setFactoryClassify(subName);
		}
		ActionContext.getContext().put("auditTypes",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_audit_type"));
		ActionContext.getContext().put("issuesTypes",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_issues_type"));
		ActionContext.getContext().put("degrees",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_degree"));
		ActionContext.getContext().put("closeStates",ApiFactory.getSettingService().getOptionsByGroupCode("qsm_close_state"));
	}
	@Action("import")
	public String imports() throws Exception {
		return SUCCESS;
	}
	
	@Action("import-datas")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(problemsManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
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
		Problems problems=problemsManager.getEntity(Long.valueOf(id));
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
		problems.setConsignorLogin(consignorLogin);
		problems.setConsignor(consignor);
		if(consignableDate!=null&&!consignableDate.equals("")){
			problems.setConsignableDate(sdf.parse(consignableDate));
		}
		problems.setDutyManLogin(dutyManLogin);
		problems.setDutyMan(dutyMan);
		if(improveDate!=null&&!improveDate.equals("")){
			problems.setImproveDate(sdf.parse(improveDate));
		}
		problems.setDutyManLeadLogin(dutyManLeadLogin);
		problems.setDutyManLead(dutyManLead);
		if(auditorDate!=null&&!auditorDate.equals("")){
			problems.setAuditorDate(sdf.parse(auditorDate));
		}		
		if(compliteDate!=null&&!compliteDate.equals("")){
			problems.setCompliteDate(sdf.parse(compliteDate));
		}
		if(actuallyDate!=null&&!actuallyDate.equals("")){
			problems.setActuallyDate(sdf.parse(actuallyDate));
		}		
		problems.setImproveEffect(improveEffect);
		problems.setCloseState(closeState);
		problems.setAuthorizer(authorizer);
		problems.setAuthorizerLogin(authorizerLogin);
		problems.setIsShouDong("是");
		problems.setShouDongLogin(ContextUtils.getLoginName());		
		JSONObject result = new JSONObject();
		try{
			problemsManager.saveEntity(problems);
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
					page = problemsManager.listState(page, type,null);
				}else{
					page = problemsManager.listState(page, type,subName);
				}
			}else{
				page = problemsManager.listState(page, type,null);
			}
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			log.error("查询失败!",e);
		}
		return null;
	}
	@Override
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		try {
			String str=problemsManager.deleteEntity(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据:单号:"+str);
		} catch (Exception e) {
			// TODO: handle exception
			renderText("删除失败:" + e.getMessage());
			log.error("删除数据信息失败",e);
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
	@LogInfo(optType="下载",message="下载内审问题模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/qsm-inner-audit-problems.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "内审问题模板.xls";
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
