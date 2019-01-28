package com.ambition.gsm.scrapList.web;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.gsm.scrapList.service.ScrapListManager;
import com.ambition.gsm.entity.ScrapList;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;

@Namespace("/gsm/scrap")
@ParentPackage("default")
@Results({@Result(name=CrudActionSupport.RELOAD, location = "/gsm/scrap", type = "redirectAction")})
public class ScrapListAction extends AmbWorkflowActionBase<ScrapList>{
	private static final long serialVersionUID = 1L;
	@Autowired
	private ScrapListManager scrapListManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	private String currentActivityName;//当前流程环节名称
	Logger log = Logger.getLogger(this.getClass());
	
	public String getCurrentActivityName() {
		return currentActivityName;
	}
	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}
	@Override
	protected AmbWorkflowManagerBase<ScrapList> getAmbWorkflowBaseManager() {
		// TODO Auto-generated method stub
		return scrapListManager;
	}
	public void initForm(){
		if(getId() == null){
			getReport().setFormNo(formCodeGenerated.generateGsmScrapNo());
			getReport().setTaskCreatedTime(new Date());
			getReport().setProposer(ContextUtils.getUserName());
			getReport().setProposerDate(new Date());
		}else{
			if(getReport().getWorkflowInfo()!=null){
				currentActivityName=getReport().getWorkflowInfo().getCurrentActivityName();
			}
		}
	}
	@Override
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		try {
			String str=scrapListManager.deleteEntity(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据:单号:"+str);
		} catch (Exception e) {
			// TODO: handle exception
			renderText("删除失败:" + e.getMessage());
			log.error("删除数据信息失败",e);
		}
		return null;
	}
}
