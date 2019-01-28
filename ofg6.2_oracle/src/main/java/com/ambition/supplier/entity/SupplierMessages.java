package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 类名:通告/下载
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月26日 发布
 */
@Entity
@Table(name = "SUPPLIER_MESSAGES")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class SupplierMessages extends IdEntity{

		/**
		  *SupplierMessages.java2018年9月14日
		 */
	private static final long serialVersionUID = 1L;
	private Date relaseDate;//发布日期
	private String messageType;//类型
	private String messageCategory;//类别
	private String fileName;//文件名
	private String fileVersion;//版本
	private String files;//附件
	private Boolean isNewVersion;//是否为新版本
	private Boolean release=false;//发布状态，true为要发布的内容，false为不需要发布的
	private Boolean isDelete=false;//是否被删除
	private String deleteMan;//删除人员
	private String deleteManLogin;//删除人登录名
	private Date  deleteDate;//删除日期
	
	//用于集成判断
	private  Long sourceId;//集成过来原ID
	private String sourceUnit;//集成的事业部	
	public Boolean getIsNewVersion() {
		return isNewVersion;
	}
	public void setIsNewVersion(Boolean isNewVersion) {
		this.isNewVersion = isNewVersion;
	}
	public Boolean getRelease() {
		return release;
	}
	public void setRelease(Boolean release) {
		this.release = release;
	}
	public Date getRelaseDate() {
		return relaseDate;
	}
	public void setRelaseDate(Date relaseDate) {
		this.relaseDate = relaseDate;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getMessageCategory() {
		return messageCategory;
	}
	public void setMessageCategory(String messageCategory) {
		this.messageCategory = messageCategory;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileVersion() {
		return fileVersion;
	}
	public void setFileVersion(String fileVersion) {
		this.fileVersion = fileVersion;
	}
	public String getFiles() {
		return files;
	}
	public void setFiles(String files) {
		this.files = files;
	}
	public Boolean getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	public String getDeleteMan() {
		return deleteMan;
	}
	public void setDeleteMan(String deleteMan) {
		this.deleteMan = deleteMan;
	}
	public String getDeleteManLogin() {
		return deleteManLogin;
	}
	public void setDeleteManLogin(String deleteManLogin) {
		this.deleteManLogin = deleteManLogin;
	}
	public Date getDeleteDate() {
		return deleteDate;
	}
	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}
	public Long getSourceId() {
		return sourceId;
	}
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}
	public String getSourceUnit() {
		return sourceUnit;
	}
	public void setSourceUnit(String sourceUnit) {
		this.sourceUnit = sourceUnit;
	}
	
	
}
