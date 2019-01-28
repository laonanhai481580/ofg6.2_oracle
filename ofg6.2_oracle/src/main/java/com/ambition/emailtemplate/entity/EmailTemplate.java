package com.ambition.emailtemplate.entity;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ambition.product.base.IdEntity;

/**
 * 类名:邮件模板
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2017-3-20 发布
 */
@Entity
@Table(name="ET_EMAIL_TEMPLATE")
public class EmailTemplate extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String code;//编码
	private String name;//名称
	private String title;//标题
	private Long contentHtmlFileId;//正文内容附件ID
	/**
	 * 仅供显示的内容,不保存到数据库
	 */
	@Transient
	private String showContentHtml;//显示的内容HTML
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getContentHtmlFileId() {
		return contentHtmlFileId;
	}
	public void setContentHtmlFileId(Long contentHtmlFileId) {
		this.contentHtmlFileId = contentHtmlFileId;
	}
	public String getShowContentHtml() {
		return showContentHtml;
	}
	public void setShowContentHtml(String showContentHtml) {
		this.showContentHtml = showContentHtml;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
