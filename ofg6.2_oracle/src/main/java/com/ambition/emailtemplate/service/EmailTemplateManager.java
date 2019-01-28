package com.ambition.emailtemplate.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.emailtemplate.dao.EmailTemplateDao;
import com.ambition.emailtemplate.entity.EmailTemplate;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.common.DateUtil;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.useFile.entity.UseFile;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.acs.base.enumeration.MailboxDeploy;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.wf.engine.client.FormFlowable;

/**
 * 类名:邮件模板业务类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2017-3-20 发布
 */
@Service
@Transactional
public class EmailTemplateManager {
	@Autowired
	private EmailTemplateDao emailTemplateDao;
	@Autowired
	private UseFileManager useFileManager;
	
	public EmailTemplate getEmailTemplate(Long id){
		return emailTemplateDao.get(id);
	}
	
	/**
	  * 方法名:根据模板编码获取邮件模板
	  * <p>功能说明：</p>
	  * @return
	 */
	public EmailTemplate getEmailTemplateByCode(String code){
		String hql = "from EmailTemplate e where e.code = ?";
		List<?> list = emailTemplateDao.createQuery(hql,code).list();
		if(list.isEmpty()){
			return null;
		}else{
			return (EmailTemplate)list.get(0);
		}
	}
	
	/**
	  * 方法名:根据模板编码获取邮件模板
	  * 不区分大小写
	  * <p>功能说明：</p>
	  * @return
	 */
	public EmailTemplate getEmailTemplateByIgnoreCaseCode(String code){
		String hql = "from EmailTemplate e where lower(e.code) = ?";
		List<?> list = emailTemplateDao.createQuery(hql,code.toLowerCase()).list();
		if(list.isEmpty()){
			return null;
		}else{
			return (EmailTemplate)list.get(0);
		}
	}
	
	public void saveEmailTemplate(EmailTemplate emailTemplate) throws Exception{
		//TODO:删除原来保存的文件
		if(emailTemplate.getContentHtmlFileId()!=null){
			UseFile useFile = useFileManager.findById(emailTemplate.getContentHtmlFileId());
			if(useFile != null){
				useFileManager.deleteFile(useFile);
			}
			emailTemplate.setContentHtmlFileId(null);
		}
		//保存内容到硬盘,以处理文件过大导致内容超出长度限制
		if(StringUtils.isNotEmpty(emailTemplate.getShowContentHtml())){
			File file = null;
			InputStream inputStream = null;
			OutputStream outputStream = null;
			try {
				String tempName = "邮件模板-" + UUID.randomUUID().toString()+".dat";
				file = new File(tempName);
				outputStream = new FileOutputStream(file);
				outputStream.write(emailTemplate.getShowContentHtml().getBytes("UTF-8"));
				outputStream.close();
				outputStream = null;
				
				inputStream = new FileInputStream(file);
				UseFile useFile = useFileManager.saveFileFile(file,tempName);
				emailTemplate.setContentHtmlFileId(useFile.getId());
			} finally{
				if(outputStream != null){
					outputStream.close();
				}
				if(inputStream != null){
					inputStream.close();
				}
				if(file != null && file.exists()){
					file.delete();
				}
			}
		}
		emailTemplate.setModifiedTime(new Date());
		emailTemplate.setModifier(ContextUtils.getLoginName());
		emailTemplate.setModifierName(ContextUtils.getUserName());
		emailTemplateDao.save(emailTemplate);
	}
	
	/**
	  * 方法名:获取文件内容
	  * <p>功能说明：
	  * 如果是保存到硬盘,从硬盘中读取
	  * 否则返回内容HTML
	  * </p>
	  * @return
	 * @throws Exception 
	 */
	public String getContentHtml(EmailTemplate emailTemplate) throws Exception{
		return EmailTemplateUtils.getContentHtml(emailTemplate,emailTemplateDao.getSession());
	}
	
	public void deleteEmailTemplate(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id: ids){
			EmailTemplate emailTemplate = emailTemplateDao.get(Long.valueOf(id));
			deleteEmailTemplate(emailTemplate);
		}
	}
	
	public void deleteEmailTemplate(EmailTemplate emailTemplate){
		emailTemplateDao.delete(emailTemplate);
		if(emailTemplate.getContentHtmlFileId() != null){
			UseFile useFile = useFileManager.findById(emailTemplate.getContentHtmlFileId());
			if(useFile != null){
				useFileManager.deleteFile(useFile);
			}
		}
	}
	public Page<EmailTemplate> search(Page<EmailTemplate> page) {
		return emailTemplateDao.search(page);
	}
	
	/**
	  * 方法名:发送邮件内容
	  * <p>功能说明：</p>
	  * @return
	 */
	public void sendEmailTemplate(Long templateId,String names,String loginNames) throws Exception{
		EmailTemplate emailTemplate = emailTemplateDao.get(templateId);
		EmailTemplateUtils.sendEmailByEmailTemplateAndLoginName(loginNames,
					null,emailTemplate,null,null,emailTemplateDao.getSession());
	}
	
	
	/**
	  * 方法名:触发邮件提醒
	  * <p>功能说明：</p>
	  * @return
	 */
	public void triggerTaskEmail(FormFlowable formFlowable,Class<?> cla,Map<String,Map<String,String>> optionMap){
		if(formFlowable.getWorkflowInfo() == null){
			return;
		}
		//自动转换对象的属性
		List<String> fieldNames = CommonUtil.getEditFieldNames(cla);
		Map<String,String> customParamMap = new HashMap<String, String>();
		for(String fieldName : fieldNames){
			try {
				Class<?> proClass = PropertyUtils.getPropertyType(formFlowable,fieldName);
				if(proClass == null){
					continue;
				}
				Object value = PropertyUtils.getProperty(formFlowable,fieldName);
				if(value == null){
					value = "";
				}else{
					if(value instanceof Date){
						value = DateUtil.formateDateStr((Date)value);
					}else{
						//处理类似选项组的问题
						if(optionMap!=null&&optionMap.containsKey(fieldName)){
							Map<String,String> valueMap = optionMap.get(fieldName);
							if(valueMap.containsKey(value+"")){
								value = valueMap.get(value.toString());
							}
						}
					}
				}
				customParamMap.put(fieldName,value+"");
			} catch (Exception e) {
				throw new AmbFrameException("自动获取属性值失败!",e);
			}
		}
		triggerTaskEmail(formFlowable,customParamMap);
	}
	
	/**
	  * 方法名:触发邮件提醒
	  * <p>功能说明：</p>
	  * @return
	 */
	public void triggerTaskEmail(FormFlowable formFlowable,Map<String,String> customParamMap){
		if(formFlowable.getWorkflowInfo() == null){
			return;
		}
		//查询待通知的任务
		String sql = "select p.id,i.process_code,t.code,p.name,p.title,p.transactor,p.transactor_name,"
				 + " u.email,u.mailbox_deploy,s.path,p.url from workflow_task t "
				 + " inner join product_task p on t.id = p.id"
				 + " inner join wf_instance i on t.process_instance_id = i.process_instance_id"
				 + " inner join acs_business_system s on i.system_id = s.id"
				 + " inner join acs_user u on p.transactor_id = u.id"
				 + " where t.process_instance_id = ? and p.transact_date is null"
				 + " and (p.email_flag is null or p.email_flag < 1) ";
		List<?> list = emailTemplateDao.getSession().createSQLQuery(sql)
					.setParameter(0,formFlowable.getWorkflowInfo().getWorkflowId())
					.list();
		if(customParamMap==null){
			customParamMap = new HashMap<String, String>();
		}
		for(Object obj : list){
			Object[] objs = (Object[])obj;
			//任务ID
			Long taskId = Long.valueOf(objs[0].toString());
			//流程定义ID
			String processCode = objs[1]+"";
			//环节Code
			//String taskCode = objs[2].toString();
			
			//模板Code
			String emailTemplateCode = processCode;
			final EmailTemplate emailTemplate = getEmailTemplateByIgnoreCaseCode(emailTemplateCode);
			if(emailTemplate != null){
				String taskName = objs[3]+"";
				String taskTitle = objs[4]+"";
				String transactorLoginName = objs[5]+"";
				String transactorName = objs[6]+"";
				final List<String> toEmails = new ArrayList<String>();
				//登录名时,按照联系人的邮箱来处理
				if(CommonUtil.checkIsSupplierByLoginName(transactorLoginName)){
					String supplierTempCode = CommonUtil.getSupplierTempCodeByLoginName(transactorLoginName);
					toEmails.addAll(EmailTemplateUtils.querySupplierEmailsByTempCode(supplierTempCode, emailTemplateDao.getSession()));
				}else{
					String email = objs[7]==null?"":objs[7].toString();
					if(StringUtils.isNotEmpty(email)){
						toEmails.add(email);
					}
				}
				if(toEmails.isEmpty()){
					continue;
				}
				Integer mailboxDeployIndex = objs[8]==null?0:Integer.valueOf(objs[8].toString());
				String systemPath = objs[9]+"";
				String taskUrl = objs[10]+"";
				//处理URL
				String url = systemPath + taskUrl.substring(taskUrl.indexOf("/")) + taskId;
				customParamMap.put("taskUrl",url);
				customParamMap.put("transactorName",transactorName);
				customParamMap.put("transactorLoginName",transactorLoginName);
				customParamMap.put("taskTitle", taskTitle);
				customParamMap.put("taskName", taskName);
				
				//默认是内部
				MailboxDeploy mailboxDeploy = MailboxDeploy.INSIDE;
				if(mailboxDeployIndex>0){
					mailboxDeploy = MailboxDeploy.EXTERIOR;
				}
				final Session session = emailTemplateDao.getSessionFactory().openSession();
				try {
					final Map<String,String> paramMap = customParamMap;
					final MailboxDeploy useDeploy = mailboxDeploy;
					Thread thread = new Thread(new Runnable() {
						public void run() {
							try {
								EmailTemplateUtils.sendEmailByEmailTemplateAndToEmails(toEmails,useDeploy,null,emailTemplate,null,paramMap,session);
							} catch (Exception e) {
								Logger.getLogger(EmailTemplateManager.class).error("邮件提醒失败!",e);
							}finally{
								if(session != null){
									session.close();
								}
							}
						}
					});
					thread.start();
				} catch (Exception e) {
					if(session != null){
						session.close();
					}
				}
			}
			//更新邮件通知标识
			updateTaskEmailFlagToNotice(taskId);
		}
	}
	/**
	  * 方法名:更新标志为邮件已处理
	  * <p>功能说明：</p>
	  * @return
	 */
	private void updateTaskEmailFlagToNotice(Long taskId){
		String sql = "update product_task t set t.email_flag = 1 where t.id = ?";
		emailTemplateDao.getSession().createSQLQuery(sql).setParameter(0,taskId).executeUpdate();
	}
	
	/**
	  * 方法名:发送邮件内容
	  * <p>功能说明：</p>
	  * @return
	 */
	public void sendEmailTemplate(String loginNames,String title,String contentHtml) throws Exception{
		//判断是否有需要执行的接口
		String sendEmailDoingService = Struts2Utils.getParameter("sendEmailDoingService");
		if(StringUtils.isNotEmpty(sendEmailDoingService)){
			SendEmailDoingSerivce sendEmailDoingSerivce = (SendEmailDoingSerivce)ContextUtils.getBean(sendEmailDoingService);
			if(sendEmailDoingSerivce != null){
				sendEmailDoingSerivce.doingWhenSendEmail();
			}
		}
		//执行发送邮件的动作
		String hql = "from User u where u.deleted = 0 and  u.loginName in ('" + loginNames.replace(",","','") + "')";
		List<User> users = emailTemplateDao.createQuery(hql).list();
		EmailTemplateUtils.sendEmailByUsers(users, title, contentHtml,null, emailTemplateDao.getSession());
	}
}			
				
	
