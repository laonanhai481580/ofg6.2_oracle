package com.ambition.gsm.nonconformity.web;

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

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.gsm.entity.NonconformityDispose;
import com.ambition.gsm.nonconformity.service.NonconformityDisposeManager;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;


@Namespace("/gsm/nonconformity")
@ParentPackage("default")
@Results({@Result(name=CrudActionSupport.RELOAD, location = "/gsm/nonconformity", type = "redirectAction")})
public class NonconformityDisposeAction extends AmbWorkflowActionBase<NonconformityDispose>{
	private static final long serialVersionUID = 1L;
	@Autowired
	private NonconformityDisposeManager noconformityDisposeManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private String ids;
	private String nowTaskName;
	private NonconformityDispose nonconformityDispose;
	private String currentActivityName;//当前流程环节名称
	Logger log = Logger.getLogger(this.getClass());
	
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
	public NonconformityDispose getNonconformityDispose() {
		return nonconformityDispose;
	}
	public void setNonconformityDispose(NonconformityDispose nonconformityDispose) {
		this.nonconformityDispose = nonconformityDispose;
	}
	
	public String getCurrentActivityName() {
		return currentActivityName;
	}
	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}
	@Override
	protected AmbWorkflowManagerBase<NonconformityDispose> getAmbWorkflowBaseManager() {
		// TODO Auto-generated method stub
		return noconformityDisposeManager;
	}
	public void initForm(){
		if(getId() == null){
			getReport().setFormNo(formCodeGenerated.generateGsmNonconformityNo());
			getReport().setTaskCreatedTime(new Date());
//			getReport().setTransactorName(ContextUtils.getUserName());
//			getReport().setTransactor(ContextUtils.getLoginName());
			getReport().setLaboratory(ContextUtils.getUserName());
			
		}else{
			if(getReport().getWorkflowInfo()!=null){
				currentActivityName=getReport().getWorkflowInfo().getCurrentActivityName();
			}
		}
		ActionContext.getContext().put("nonconformityDispose", nonconformityDispose);
		ActionContext.getContext().put("businessUnitNames",ApiFactory.getSettingService().getOptionsByGroupCode("_businessDivision"));
		

	}
	/**
	 * 导出表单
	 * */
	@Action("export-form")
	@LogInfo(optType="导出",message="导出不合格台帐")
	public String exportForm() {
		try{
			noconformityDisposeManager.exportReport(id);
		}catch(Exception e){
			createErrorMessage("导出表单失败:" + e.getMessage());
			log.error("导出失败："+e.getMessage());
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
	@Override
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		try {
			String str=noconformityDisposeManager.deleteEntity(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据:单号:"+str);
		} catch (Exception e) {
			// TODO: handle exception
			renderText("删除失败:" + e.getMessage());
			log.error("删除数据信息失败",e);
		}
		return null;
	}
}
