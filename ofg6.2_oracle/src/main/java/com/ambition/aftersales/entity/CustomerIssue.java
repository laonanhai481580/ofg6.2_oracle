package com.ambition.aftersales.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * 类名:客户issue台账
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2018年4月12日 发布
 */
@Entity
@Table(name = "AFS_CUSTOMER_ISSUE")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class CustomerIssue extends IdEntity{

	private static final long serialVersionUID = 1L;
	private Date noticeDate;//通报日期
	private String customer;//客户
	private String customerCode;//客户编码
	private String ofilmModel;//欧菲机种
	private String customerPlace;//客户地区
	private String productStage;//产品阶段
	private String badRate;//不良率
	private String ecxeptionType;//异常类型
	private String ecxeptionLevel;//异常等级
	@Column(length=1000)
	private String problemDescrible;//不良现象
	private String noticeMan;//通报人
	private String noticeManLogin;//通报人登录名
	private String sampleState;//试料状况
	private String qeMan;//QE担当
	private String qeManLogin;//QE担当登录名
	@Column(length=1000)
	private String currentMeasures;//临时对策
	@Column(length=1000)
	private String reasonAnalysis;//原因分析
	private String dutyDept;//责任部门
	@Column(length=1000)
	private String counterMeasures;//永久对策
	private Date improvePeriod;//改善交期
	private Date replyPeriod;//回复交期
	private String isClose;//结案
	public Date getNoticeDate() {
		return noticeDate;
	}
	public void setNoticeDate(Date noticeDate) {
		this.noticeDate = noticeDate;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getOfilmModel() {
		return ofilmModel;
	}
	public void setOfilmModel(String ofilmModel) {
		this.ofilmModel = ofilmModel;
	}
	public String getCustomerPlace() {
		return customerPlace;
	}
	public void setCustomerPlace(String customerPlace) {
		this.customerPlace = customerPlace;
	}
	public String getBadRate() {
		return badRate;
	}
	public void setBadRate(String badRate) {
		this.badRate = badRate;
	}
	public String getEcxeptionType() {
		return ecxeptionType;
	}
	public void setEcxeptionType(String ecxeptionType) {
		this.ecxeptionType = ecxeptionType;
	}
	public String getEcxeptionLevel() {
		return ecxeptionLevel;
	}
	public void setEcxeptionLevel(String ecxeptionLevel) {
		this.ecxeptionLevel = ecxeptionLevel;
	}
	public String getProblemDescrible() {
		return problemDescrible;
	}
	public void setProblemDescrible(String problemDescrible) {
		this.problemDescrible = problemDescrible;
	}
	public String getNoticeMan() {
		return noticeMan;
	}
	public void setNoticeMan(String noticeMan) {
		this.noticeMan = noticeMan;
	}
	public String getNoticeManLogin() {
		return noticeManLogin;
	}
	public void setNoticeManLogin(String noticeManLogin) {
		this.noticeManLogin = noticeManLogin;
	}
	public String getSampleState() {
		return sampleState;
	}
	public void setSampleState(String sampleState) {
		this.sampleState = sampleState;
	}
	public String getQeMan() {
		return qeMan;
	}
	public void setQeMan(String qeMan) {
		this.qeMan = qeMan;
	}
	public String getQeManLogin() {
		return qeManLogin;
	}
	public void setQeManLogin(String qeManLogin) {
		this.qeManLogin = qeManLogin;
	}
	public String getCurrentMeasures() {
		return currentMeasures;
	}
	public void setCurrentMeasures(String currentMeasures) {
		this.currentMeasures = currentMeasures;
	}
	public String getReasonAnalysis() {
		return reasonAnalysis;
	}
	public void setReasonAnalysis(String reasonAnalysis) {
		this.reasonAnalysis = reasonAnalysis;
	}
	public String getDutyDept() {
		return dutyDept;
	}
	public void setDutyDept(String dutyDept) {
		this.dutyDept = dutyDept;
	}
	public String getCounterMeasures() {
		return counterMeasures;
	}
	public void setCounterMeasures(String counterMeasures) {
		this.counterMeasures = counterMeasures;
	}
	public Date getImprovePeriod() {
		return improvePeriod;
	}
	public void setImprovePeriod(Date improvePeriod) {
		this.improvePeriod = improvePeriod;
	}
	public Date getReplyPeriod() {
		return replyPeriod;
	}
	public void setReplyPeriod(Date replyPeriod) {
		this.replyPeriod = replyPeriod;
	}
	public String getIsClose() {
		return isClose;
	}
	public void setIsClose(String isClose) {
		this.isClose = isClose;
	}
	public String getProductStage() {
		return productStage;
	}
	public void setProductStage(String productStage) {
		this.productStage = productStage;
	}
	
	
}
