package com.ambition.emailtemplate.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.ambition.emailtemplate.entity.EmailTemplate;
import com.ambition.util.chart.entity.CustomMailInfo;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.common.DateUtil;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.useFile.entity.UseFile;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.acs.base.enumeration.MailboxDeploy;
import com.norteksoft.acs.entity.organization.MailDeploy;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.product.util.ContextUtils;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

/**
 * 类名:邮件模板发送附件工具类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2017-3-20 发布
 */
public class EmailTemplateUtils {
	/**
	  * 方法名:根据邮件模板和目标用户登录登录名发送邮件
	  * <p>功能说明：</p>
	  * @loginNames 目标用户登录名,多个用逗号(,)隔开
	  * @subject 主题,为空时会采用邮件模板的主题代替
	  * @emailTemplateCode 邮件模板编码
	  * @files 附件
	  * @customParamMap 自定义的参数
	  * @session Hibernate
	  * @return
	 */
	public static void sendEmailByEmailTemplateAndLoginName(String loginNames,
				String subject,
				String emailContentCode,
				List<File> files,
				Map<String,String> customParamMap,org.hibernate.Session session) throws Exception{
		EmailTemplate emailTemplate = findEmailTemplate(emailContentCode,session);
		if(emailTemplate == null){
			throw new AmbFrameException("编码为"+emailContentCode+"的邮件模板找不到!");
		}
		sendEmailByEmailTemplateAndLoginName(loginNames,subject,emailTemplate,files,customParamMap,session);
	}
	
	/**
	  * 方法名:根据邮件模板和目标用户登录登录名发送邮件
	  * <p>功能说明：</p>
	  * @loginNames 目标用户登录名,多个用逗号(,)隔开
	  * @subject 主题,为空时会采用邮件模板的主题代替
	  * @emailTemplate 邮件模板
	  * @files 附件
	  * @customParamMap 自定义的参数
	  * @session Hibernate
	  * @return
	 */
	public static void sendEmailByEmailTemplateAndLoginName(String loginNames,
				String subject,
				EmailTemplate emailTemplate,
				List<File> files,
				Map<String,String> customParamMap,org.hibernate.Session session) throws Exception{
		List<User> users = findUsers(loginNames, session);
		if(users.isEmpty()){
			throw new AmbFrameException("登录名为"+loginNames+"的用户找不到!");
		}
		sendEmailByEmailTemplateAndUsers(users,subject, emailTemplate, files, customParamMap, session);
	}
	
	/**
	  * 方法名:根据邮件模板和目标用户发送邮件
	  * <p>功能说明：</p>
	  * @toUsers 目标用户
	  * @subject 主题,为空时会采用邮件模板的主题代替
	  * @emailTemplateCode 邮件模板编码
	  * @files 附件
	  * @customParamMap 自定义的参数
	  * @session Hibernate
	  * @return
	 */
	public static void sendEmailByEmailTemplateAndUsers(List<User> toUsers,
				String subject,
				String emailContentCode,
				List<File> files,
				Map<String,String> customParamMap,
				org.hibernate.Session session) throws Exception{
		EmailTemplate emailTemplate = findEmailTemplate(emailContentCode,session);
		if(emailTemplate == null){
			throw new AmbFrameException("编码为"+emailContentCode+"的邮件模板找不到!");
		}
		sendEmailByEmailTemplateAndUsers(toUsers, subject, emailTemplate, files, customParamMap, session);
	}
	
	/**
	  * 方法名:根据邮件模板和目标用户发送邮件
	  * <p>功能说明：</p>
	  * @toUsers 目标用户
	  * @subject 主题,为空时会采用邮件模板的主题代替
	  * @emailTemplate 邮件模板
	  * @files 附件
	  * @customParamMap 自定义的参数
	  * @session Hibernate
	  * @return
	 */
	public static void sendEmailByEmailTemplateAndUsers(List<User> toUsers,
				String subject,EmailTemplate emailTemplate,
				List<File> files,Map<String,String> customParamMap,
				org.hibernate.Session session) throws Exception{
		String showContentHtml = getContentHtml(emailTemplate,session);
		if(StringUtils.isEmpty(showContentHtml)){
			throw new AmbFrameException("邮件内容读取失败!");
		}
		//默认按照传的参数来设置
		if(StringUtils.isEmpty(subject)){
			subject = emailTemplate.getTitle();
		}
		//格式化
		subject = getFormatHtml(subject, customParamMap);
		//格式化
		showContentHtml = getFormatHtml(showContentHtml,customParamMap);
		sendEmailByUsers(toUsers, subject,showContentHtml,files,session);
	}
	
	/**
	  * 方法名:根据邮件模板和目标邮件发送邮件
	  * <p>功能说明：</p>
	  * @toEmails 目标邮件
	  * @mailboxDeploy 发送邮件地址,内部还是外部,为null时为内部
	  * @subject 主题,为空时会采用邮件模板的主题代替
	  * @emailTemplateCode 邮件模板编码
	  * @files 附件
	  * @customParamMap 自定义的参数
	  * @session Hibernate
	  * @return
	 */
	public static void sendEmailByEmailTemplateAndToEmails(List<String> toEmails,MailboxDeploy mailboxDeploy,
				String subject,String emailContentCode,List<File> files,
				Map<String,String> customParamMap,org.hibernate.Session session) throws Exception{
		EmailTemplate emailTemplate = findEmailTemplate(emailContentCode,session);
		if(emailTemplate == null){
			throw new AmbFrameException("编码为"+emailContentCode+"的邮件模板找不到!");
		}
		sendEmailByEmailTemplateAndToEmails(toEmails,mailboxDeploy,subject, emailTemplate, files, customParamMap, session);
	}
	
	/**
	  * 方法名:根据邮件模板和目标邮件发送邮件
	  * <p>功能说明：</p>
	  * @toEmails 目标邮件
	  * @mailboxDeploy 发送邮件地址,内部还是外部,为null时为内部
	  * @subject 主题,为空时会采用邮件模板的主题代替
	  * @emailTemplate 邮件模板
	  * @files 附件
	  * @customParamMap 自定义的参数
	  * @session Hibernate
	  * @return
	 */
	public static void sendEmailByEmailTemplateAndToEmails(List<String> toEmails,MailboxDeploy mailboxDeploy,
				String subject,EmailTemplate emailTemplate,List<File> files,
				Map<String,String> customParamMap,org.hibernate.Session session) throws Exception{
		String showContentHtml = getContentHtml(emailTemplate,session);
		if(StringUtils.isEmpty(showContentHtml)){
			throw new AmbFrameException("邮件内容读取失败!");
		}
		//默认按照传的参数来设置
		if(StringUtils.isEmpty(subject)){
			subject = emailTemplate.getTitle();
		}
		//格式化
		subject = getFormatHtml(subject, customParamMap);
		//格式化
		showContentHtml = getFormatHtml(showContentHtml,customParamMap);
		//默认按照内部
		if(mailboxDeploy==null){
			mailboxDeploy = MailboxDeploy.INSIDE;
		}
		sendEmails(toEmails, mailboxDeploy, subject, showContentHtml, files, session);
	}
	
	/**
	  * 方法名: 发送附件的邮件
	  * <p>功能说明：</p>
	  * @param to
	  * @param subject
	  * @param content
	  * @param file
	  * @return
	  * @throws AddressException
	  * @throws MessagingException
	 * @throws UnsupportedEncodingException 
	 */
	public static void sendEmailByUsers(List<User> toUsers,String subject,
			String contentHtml,List<File> files,org.hibernate.Session session) throws AddressException, MessagingException, UnsupportedEncodingException{
        MailDeploy mailDeploy = findMailDeploy(session);
        if(mailDeploy == null){
        	return;
        }
		Map<String,List<String>> pathMap = new HashMap<String, List<String>>();
		Map<String,CustomMailInfo> mailInfoMap = new HashMap<String, CustomMailInfo>();
		for(User toUser : toUsers){
			if(StringUtils.isEmpty(toUser.getEmail())){
				throw new AmbFrameException("用户【"+toUser.getName()+"】的收件地址为空!");
			}
			CustomMailInfo info = getMailInfo(toUser.getMailboxDeploy(),mailDeploy);
			if(info != null){
				if(!pathMap.containsKey(info.getFrom())){
					pathMap.put(info.getFrom(),new ArrayList<String>());
					mailInfoMap.put(info.getFrom(),info);
				}
				pathMap.get(info.getFrom()).add(toUser.getEmail());
			}
		}
		Map<String,String> fileMap = new HashMap<String, String>();
		if(files != null){
			for(File file : files){
				fileMap.put(file.getName(),file.getAbsolutePath());
			}
		}
		for(String from : mailInfoMap.keySet()){
			CustomMailInfo info = mailInfoMap.get(from);
			sendMail(Boolean.valueOf(info.isAutheticate()), info.getProtocol(), info.getHost(), info.getPort(), info.getUser(), info.getPassword(), info.getFrom(),pathMap.get(from), subject, contentHtml, fileMap,session);
		}
	}
	
	/**
	 * 根据邮件地址发送邮件
	 * @toEmails 发送邮件的EMAIL
	 * @mailboxDeploy 内部还是外部的邮件类型
	 * @subject 主题
	 * @contentHtml 邮件内容
	 * */
	private static void sendEmails(List<String> toEmails,MailboxDeploy mailboxDeploy,String subject,String contentHtml,List<File> files,org.hibernate.Session session) throws AddressException, MessagingException, UnsupportedEncodingException{
		MailDeploy mailDeploy = findMailDeploy(session);
		if(mailDeploy == null){
			return;
		}
		CustomMailInfo info = getMailInfo(mailboxDeploy,mailDeploy);
		Map<String,String> fileMap = new HashMap<String, String>();
		if(files != null){
		   for(File file : files){
				fileMap.put(file.getName(),file.getAbsolutePath());
		   }
		}
		sendMail(Boolean.valueOf(info.isAutheticate()), info.getProtocol(), info.getHost(), info.getPort(), info.getUser(), info.getPassword(), info.getFrom(),toEmails, subject, contentHtml, fileMap,session);
	}
	
	private static void sendMail(Boolean isAutheticate, String protocol, String host, Integer port, String user, String password, String from, List<String> tos, 
            String subject, String contentHtml, Map<String,String> filePathMap,org.hibernate.Session session)
        throws AddressException, MessagingException, UnsupportedEncodingException
    {
        Properties p = new Properties();
        p.put("mail.smtp.auth", isAutheticate.toString());
        p.put("mail.transport.protocol", protocol);
        p.put("mail.smtp.host", host);
        p.put("mail.smtp.port", port);
        Session emailSession = Session.getInstance(p);
        Message msg = new MimeMessage(emailSession);
        msg.setFrom(new InternetAddress(from));
        InternetAddress[] address = new InternetAddress[tos.size()];
        for(int i=0;i<tos.size();i++){
        	address[i] = new InternetAddress(tos.get(i));
        }
        msg.setRecipients(javax.mail.Message.RecipientType.TO,address);
        msg.setSentDate(new Date());
        msg.setSubject(subject);
        
        if(contentHtml == null){
        	contentHtml = "";
        }
        Multipart mulp = new MimeMultipart();
        List<File> tempFiles = new ArrayList<File>();
        try {
        	//转换本地上传的图片为附件
        	contentHtml = convertImageToAttachfiles(mulp,contentHtml,tempFiles,session);
            MimeBodyPart remarkPart = new MimeBodyPart();
            remarkPart.setContent(contentHtml,"text/html;charset=utf-8");
            mulp.addBodyPart(remarkPart);
            msg.setContent(mulp);
            Transport tran = emailSession.getTransport(protocol);
            tran.connect(host, user, password);
            tran.sendMessage(msg, msg.getAllRecipients());
		} finally{
			for(File file : tempFiles){
				if(file.exists()){
					file.delete();
				}
			}
		}
    }
	
	private static CustomMailInfo getMailInfo(MailboxDeploy mailboxDeploy,MailDeploy mailDeploy)
    {
        String protocol = "smtp";
        Integer port = Integer.valueOf(25);
        Boolean isAutheticate;
        String host;
        String userName;
        String password;
        String from;
        if(MailboxDeploy.INSIDE.equals(mailboxDeploy))
        {
            isAutheticate = getSmtpAuth(mailDeploy.getSmtpAuthInside());
            if(StringUtils.isNotEmpty(mailDeploy.getTransportProtocolInside()))
                protocol = mailDeploy.getTransportProtocolInside();
            Assert.notNull(mailDeploy.getSmtpHostInside(), "\u5185\u7F51\u914D\u7F6E\u4E2D\u7684[\u90AE\u4EF6\u670D\u52A1\u5668\u5730\u5740]\u4E0D\u80FD\u4E3A\u7A7A  ");
            host = mailDeploy.getSmtpHostInside();
            if(StringUtils.isNotEmpty(mailDeploy.getSmtpPortInside()))
                port = Integer.valueOf(NumberUtils.toInt(mailDeploy.getSmtpPortInside(), 25));
            Assert.notNull(mailDeploy.getHostUserInside(), "\u5185\u7F51\u914D\u7F6E\u4E2D\u7684[\u9ED8\u8BA4\u670D\u52A1\u5668\u7AEF\u7528\u6237\u540D]\u4E0D\u80FD\u4E3A\u7A7A  ");
            userName = mailDeploy.getHostUserInside();
            Assert.notNull(mailDeploy.getHostUserPasswordInside(), "\u5185\u7F51\u914D\u7F6E\u4E2D\u7684[\u9ED8\u8BA4\u670D\u52A1\u5668\u7528\u6237\u5BC6\u7801]\u4E0D\u80FD\u4E3A\u7A7A  ");
            password = mailDeploy.getHostUserPasswordInside();
            Assert.notNull(mailDeploy.getHostUserFromInside(), "\u5185\u7F51\u914D\u7F6E\u4E2D\u7684[\u9ED8\u8BA4\u4E3B\u673A\u5730\u5740]\u4E0D\u80FD\u4E3A\u7A7A  ");
            from = mailDeploy.getHostUserFromInside();
        } else
        {
            isAutheticate = getSmtpAuth(mailDeploy.getSmtpAuthExterior());
            if(StringUtils.isNotEmpty(mailDeploy.getTransportProtocolExterior()))
                protocol = mailDeploy.getTransportProtocolExterior();
            Assert.notNull(mailDeploy.getSmtpHostExterior(), "\u5916\u7F51\u914D\u7F6E\u4E2D\u7684[\u90AE\u4EF6\u670D\u52A1\u5668\u5730\u5740]\u4E0D\u80FD\u4E3A\u7A7A  ");
            host = mailDeploy.getSmtpHostExterior();
            if(StringUtils.isNotEmpty(mailDeploy.getSmtpPortExterior()))
                port = Integer.valueOf(NumberUtils.toInt(mailDeploy.getSmtpPortExterior(), 25));
            Assert.notNull(mailDeploy.getHostUserExterior(), "\u5916\u7F51\u914D\u7F6E\u4E2D\u7684[\u9ED8\u8BA4\u670D\u52A1\u5668\u7AEF\u7528\u6237\u540D]\u4E0D\u80FD\u4E3A\u7A7A  ");
            userName = mailDeploy.getHostUserExterior();
            Assert.notNull(mailDeploy.getHostUserPasswordExterior(), "\u5916\u7F51\u914D\u7F6E\u4E2D\u7684[\u9ED8\u8BA4\u670D\u52A1\u5668\u7528\u6237\u5BC6\u7801]\u4E0D\u80FD\u4E3A\u7A7A  ");
            password = mailDeploy.getHostUserPasswordExterior();
            Assert.notNull(mailDeploy.getHostUserFromExterior(), "\u5916\u7F51\u914D\u7F6E\u4E2D\u7684[\u9ED8\u8BA4\u4E3B\u673A\u5730\u5740]\u4E0D\u80FD\u4E3A\u7A7A  ");
            from = mailDeploy.getHostUserFromExterior();
        }
        return new CustomMailInfo(isAutheticate.booleanValue(), protocol, host, port, userName, password, from);
    }
	
	private static Boolean getSmtpAuth(String smtpAuth)
    {
        Boolean isAutheticate;
        if(StringUtils.isEmpty(smtpAuth))
            isAutheticate = Boolean.valueOf(true);
        else
            isAutheticate = Boolean.valueOf(smtpAuth);
        return isAutheticate;
    }
	
	private static String convertImageToAttachfiles(Multipart mulp,String contentHtml,List<File> tempFiles,org.hibernate.Session session){
        MimeBodyPart filePart = new MimeBodyPart();
        String pattern = "<img [^<|>]*/>";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(contentHtml);
		List<String> imgHtmlList = new ArrayList<String>();
		while(m.find()){
			String paramStr = m.group(0);
			//包含download.htm表示是amb编辑的数据
			if(paramStr.indexOf("download.htm?id=")>-1&&paramStr.indexOf("src=")>-1){
				imgHtmlList.add(paramStr);
			}
		}
		try {
			String srcPatternRegex = "src=[\"|'][^\"|']*[\"|']";
			Pattern urlPattern = Pattern.compile(srcPatternRegex);
			int pictureIndex = 0;
			for(String imgHtml : imgHtmlList){
				//格式如: <img src="/download.htm?id=aaaa" />;
				Matcher matcher = urlPattern.matcher(imgHtml);
				if(matcher.find()){
					String groupStr = matcher.group();
					String useFileIdStr = groupStr.split("id=")[1];
					if(useFileIdStr.endsWith("\"")||useFileIdStr.endsWith("'")){
						useFileIdStr = useFileIdStr.substring(0,useFileIdStr.length()-1);
					}
					if(CommonUtil.isInteger(useFileIdStr)){
						UseFile useFile = findUsefile(Long.valueOf(useFileIdStr),session);
						if(useFile != null){
							File file = readFromUsefile(useFile);
							if(file != null){
								//缓存起来,用完删除
								tempFiles.add(file);
								
								DataSource ds3 = new FileDataSource(file);
						    	DataHandler dataHandler3 = new DataHandler(ds3);
						    	filePart.setDataHandler(dataHandler3);
						    	filePart.setFileName(MimeUtility.encodeText(useFile.getFileName()));
						    	String contentId = "picture" + pictureIndex++;
						    	filePart.setContentID(contentId);
						    	mulp.addBodyPart(filePart);
						    	
						    	String newSrc = "src=\"cid:" + contentId + "\"";
						    	//替换旧的路径为附件路径
						    	contentHtml = contentHtml.replace(groupStr,newSrc);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Logger.getLogger(EmailTemplateUtils.class).error("发送邮件时转换图片路径失败!",e);
		}
		return contentHtml;
	}
	
	/**
	  * 方法名:查询文件对象
	  * <p>功能说明：</p>
	  * @return
	 * @throws Exception 
	 */
	private static UseFile findUsefile(Long useIfleId,org.hibernate.Session session){
		String hql = "from UseFile u where u.id = ?";
		List<?> useFiles = session.createQuery(hql).setParameter(0,useIfleId).list();
		if(useFiles.isEmpty()){
			return null;
		}else{
			return (UseFile)useFiles.get(0);
		}
	}
	/**
	  * 方法名:读文件
	  * <p>功能说明：</p>
	  * @return
	 */
	private static File readFromUsefile(UseFile useFile) throws Exception{
		UseFileManager useFileManager = (UseFileManager)ContextUtils.getBean("useFileManager");
		File file = null;
		OutputStream out = null;
		try {
			file = new File(UUID.randomUUID().toString()+".bat");
			out = new FileOutputStream(file);
			useFileManager.writeFromUseFile(useFile, out);
			return file;
		} catch (Exception e) {
			if(file != null 
					&& file.exists()){
				file.delete();
			}
			return null;
		}finally{
			if(out != null){
				out.close();
			}
		}
	}
	
	/**
	  * 方法名:查询邮箱设置对象
	  * <p>功能说明：</p>
	  * @return
	 */
	private static MailDeploy findMailDeploy(org.hibernate.Session session){
		String hql = "from MailDeploy m";
	    List<?> list = session.createQuery(hql).list();
	    if(list.isEmpty()){
	    	return null;
	    }else{
	    	return (MailDeploy)list.get(0);
	    }
	}
	
	/**
	  * 方法名:查询邮件模块
	  * <p>功能说明：</p>
	  * @return
	 */
	private static EmailTemplate findEmailTemplate(String emailTemplateCode,org.hibernate.Session session){
		String hql = "from EmailTemplate m where m.code = ?";
	    List<?> list = session.createQuery(hql).setParameter(0,emailTemplateCode).list();
	    if(list.isEmpty()){
	    	return null;
	    }else{
	    	return (EmailTemplate)list.get(0);
	    }
	}
	
	/**
	  * 方法名:根据登录名查询用户
	  * <p>功能说明：</p>
	  * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<User> findUsers(String loginNames,org.hibernate.Session session){
		String hql = "from User u where u.deleted = 0 and u.loginName in ('" + loginNames.replace(",","','") + "')";
		return session.createQuery(hql).list();
	}
	
	/**
	  * 方法名:根据供应商临时编码查询邮箱
	  * <p>功能说明：</p>
	  * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> querySupplierEmailsByTempCode(String supplierTempCode,org.hibernate.Session session){
		String sql = "select m.email from SUPPLIER_SUPPLIER_LINK_MAN m inner join supplier_supplier s on m.FK_SUPPLIER_ID = s.id ";
		sql += "where s.temp_Code = ? and m.email is not null";
		return session.createSQLQuery(sql)
					.setParameter(0,supplierTempCode)
					.list();
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
	public static String getContentHtml(EmailTemplate emailTemplate,org.hibernate.Session session) throws Exception{
		if(emailTemplate.getContentHtmlFileId()!=null){
			UseFile useFile = findUsefile(emailTemplate.getContentHtmlFileId(), session);
			if(useFile != null){
				ByteOutputStream byteOutputStream = null;
				try {
					byteOutputStream = new ByteOutputStream();
					UseFileManager useFileManager = (UseFileManager)ContextUtils.getBean("useFileManager");
					useFileManager.writeFromUseFile(useFile, byteOutputStream);
					return new String(byteOutputStream.getBytes(),"UTF-8");
				}catch(Exception ex){
					Logger.getLogger(EmailTemplateUtils.class).error("读取文件出错!",ex);
					return null;
				} finally{
					if(byteOutputStream != null){
						byteOutputStream.close();
					}
				}
			}
		}
		//默认返回字段的内容
		return null;
	}
	
	/**
	  * 方法名:获取文件内容
	  * <p>功能说明：
	  * 否则返回内容HTML
	  * </p>
	  * @return
	 * @throws Exception 
	 */
	public static String getContentHtml(EmailTemplate emailTemplate,Map<String,String> customParamMap,org.hibernate.Session session) throws Exception{
		String contentHtml = getContentHtml(emailTemplate,session);
		return getFormatHtml(contentHtml,customParamMap);
	}
	
	/**
	  * 方法名:格式化邮件发送内容
	  * <p>功能说明：</p>
	  * @return
	 */
	public static String getFormatHtml(String contentHtml,Map<String,String> customParamMap){
		Map<String,String> paramMap = getStaticParamMap();
		if(customParamMap != null){
			for(String key : customParamMap.keySet()){
				paramMap.put(key,customParamMap.get(key));
			}
		}
		//替换
		for(String paramName : paramMap.keySet()){
			String value = paramMap.get(paramName);
			if(value == null){
				value = "";
			}
			contentHtml = contentHtml.replace("{" + paramName + "}",value);
		}
		return contentHtml;
	}
	
	/**
	  * 方法名:获取邮件可选参数 
	  * <p>功能说明：</p>
	  * @return
	 */
	private static Map<String,String> getStaticParamMap(){
		Map<String,String> staticMap = new HashMap<String, String>();
		staticMap.put("当前用户名",ContextUtils.getUserName());
		staticMap.put("当前时间",DateUtil.formateTimeStr(Calendar.getInstance()));
		staticMap.put("系统名称","QIS");
		staticMap.put("版权","Copyright©2018 南昌欧菲光科技光科技有限公司");
		return staticMap;
	}
}
