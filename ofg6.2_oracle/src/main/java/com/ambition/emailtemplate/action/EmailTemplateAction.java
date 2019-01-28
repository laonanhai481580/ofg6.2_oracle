package com.ambition.emailtemplate.action;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.emailtemplate.entity.EmailTemplate;
import com.ambition.emailtemplate.service.EmailTemplateManager;
import com.ambition.emailtemplate.service.EmailTemplateUtils;
import com.ambition.product.BaseAction;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 类名:邮件模板Action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2017-3-20 发布
 */
@Namespace("/emailtemplate/template")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "emailtemplate/template", type = "redirectAction") })
public class EmailTemplateAction extends BaseAction<EmailTemplate> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private JSONObject params;
	private EmailTemplate emailTemplate;
	@Autowired
	private EmailTemplateManager emailTemplateManager;
	private Page<EmailTemplate> page;
	
	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setPage(Page<EmailTemplate> page) {
		this.page = page;
	}
	
	public Page<EmailTemplate> getPage() {
		return page;
	}
	
	public EmailTemplate getModel() {
		return emailTemplate;
	}
	
	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			emailTemplate=new EmailTemplate();
			emailTemplate.setCreatedTime(new Date());
			emailTemplate.setCompanyId(ContextUtils.getCompanyId());
			emailTemplate.setCreator(ContextUtils.getLoginName());
			emailTemplate.setCreatorName(ContextUtils.getUserName());
		}else {
			emailTemplate=emailTemplateManager.getEmailTemplate(id);
		}
	}
	
	
	@Action("save")
	@Override
	public String save() throws Exception {
		JSONObject result = new JSONObject();
		String cacheKey = "email_template_content";
		try {
			String isStart = Struts2Utils.getParameter("isStart");
			String showContentHtml = null;
			if("true".equals(isStart)){
				showContentHtml = "";
			}else{
				showContentHtml = (String)Struts2Utils.getSession().getAttribute(cacheKey);
			}
			if(showContentHtml==null){
				showContentHtml="";
			}
			showContentHtml += Struts2Utils.getParameter("showContentHtml");
			
			String isEnd = Struts2Utils.getParameter("isEnd");
			if("true".equals(isEnd)){
				emailTemplate.setShowContentHtml(showContentHtml);
				emailTemplateManager.saveEmailTemplate(emailTemplate);
				result.put("success",true);
				result.put("id",emailTemplate.getId());
				//移除缓存
				Struts2Utils.getSession().removeAttribute(cacheKey);
			}else{
				Struts2Utils.getSession().setAttribute(cacheKey,showContentHtml);
			}
		} catch (Exception e) {
			log.error("保存失败!",e);
			result.put("error",true);
			result.put("message",com.norteksoft.product.web.struts2.Struts2Utils.getText("emailtemplate.form.保存失败")+"," + e.getMessage());
			//移除缓存
			Struts2Utils.getSession().removeAttribute(cacheKey);
		}
		renderText(result.toString());
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="删除邮件模板")
	public String delete() throws Exception {
		try {
			emailTemplateManager.deleteEmailTemplate(deleteIds);
		} catch (Exception e) {
			renderText("删除失败," + e.getMessage());
			log.error("删除邮件模板失败",e);
		}
		return null;
	}
	

	@Action("input")
	@Override
	public String input() throws Exception {
		//显示内容
		try {
			emailTemplate.setShowContentHtml(emailTemplateManager.getContentHtml(emailTemplate));
		} catch (Exception e) {
			log.error("显示内容处理失败!",e);
		}
		return SUCCESS;
	}
	
	@Action("view")
	public String view() throws Exception {
		if(id!=null){
			EmailTemplate emailTemplate=emailTemplateManager.getEmailTemplate(id);
			try {
				String showContentHtml = emailTemplateManager.getContentHtml(emailTemplate);
				showContentHtml = EmailTemplateUtils.getFormatHtml(showContentHtml,null);
				emailTemplate.setShowContentHtml(showContentHtml);
			} catch (Exception e) {
				log.error("读取内容失败!",e);
			}
			ActionContext.getContext().getValueStack().push(emailTemplate);
			//默认当前用户
			String loginName = ContextUtils.getLoginName();
			String userName = ContextUtils.getUserName();
			ActionContext.getContext().put("loginNames",loginName);
			ActionContext.getContext().put("names",userName);
		}
		return SUCCESS;
	}
	
	@Action("test-email")
	public String testEmail() throws Exception {
		JSONObject result = new JSONObject();
		try {
			String templateIdStr = Struts2Utils.getParameter("templateId");
			Long templateId = Long.valueOf(templateIdStr);
			String userNames = Struts2Utils.getParameter("names");
			String loginNames = Struts2Utils.getParameter("loginNames");
			emailTemplateManager.sendEmailTemplate(templateId, userNames, loginNames);
		} catch (Exception e) {
			log.error("测试失败!",e);
			result.put("error",true);
			result.put("message","发送失败," + e.getMessage());
		}
		renderText(result.toString());
		return null;
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception {
		try{
			page = emailTemplateManager.search(page);
			renderText(PageUtils.pageToJson(page,Struts2Utils.getParameter("_list_code")));
		}catch (Exception e) {
			log.error("查询失败!",e);
		}
		return null;
	}

	@Action("send-email-form")
	public String sendEmailForm() throws Exception {
		return SUCCESS;
	}
	@Action("send-email")
	public String sendEmail() throws Exception {
		JSONObject result = new JSONObject();
		String cacheKey = "send_email_template_content";
		try {
			String isStart = Struts2Utils.getParameter("isStart");
			String showContentHtml = null;
			if("true".equals(isStart)){
				showContentHtml = "";
			}else{
				showContentHtml = (String)Struts2Utils.getSession().getAttribute(cacheKey);
			}
			if(showContentHtml==null){
				showContentHtml="";
			}
			showContentHtml += Struts2Utils.getParameter("showContentHtml");
			
			String isEnd = Struts2Utils.getParameter("isEnd");
			if("true".equals(isEnd)){
				String loginNames = Struts2Utils.getParameter("loginNames");
				String title = Struts2Utils.getParameter("title");
				emailTemplateManager.sendEmailTemplate(loginNames,title, showContentHtml);
				result.put("success",true);
				//移除缓存
				Struts2Utils.getSession().removeAttribute(cacheKey);
			}else{
				Struts2Utils.getSession().setAttribute(cacheKey,showContentHtml);
			}
		} catch (Exception e) {
			log.error("测试失败!",e);
			result.put("error",true);
			result.put("message","发送失败," + e.getMessage());
			//移除缓存
			Struts2Utils.getSession().removeAttribute(cacheKey);
		}
		renderText(result.toString());
		return null;
	}
	
	public EmailTemplate getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(EmailTemplate emailTemplate) {
		this.emailTemplate = emailTemplate;
	}
}
