package com.ambition.carmfg.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * 类名:OQC异常管理台账
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2018年4月13日 发布
 */
@Entity
@Table(name = "MFG_OQC_EXCEPTION")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class OqcException extends IdEntity{

	private static final long serialVersionUID = 1L;
	private Date returnDate;//日期
	private String classGroup;//班别
	private String weekStr;//周别
	private String factory; //
	private String procedure;//工序
	private String ofilmModel;//欧菲机种
	private Integer standardCount;//标准数
	private Integer inputCount;//投入数
	private Integer unQualityCount;//不良数
	private String  unQualityRate;//不良率
	@Column(length=1000)
	private String problemDescrible;//不良现象
	private String dutyDept;//责任部门
	private String ecxeptionLevel;//异常等级
	private String ecxeptionType;//异常类型
	@Column(length=1000)
	private String reasonAnalysis;//原因分析
	@Column(length=1000)
	private String currentMeasures;//临时对策
	@Column(length=1000)
	private String counterMeasures;//永久对策
	private Date replyPeriod;//回复交期
	private String produceConmfirm;//生产确认
	private String projectConmfirm;//工程确认
	private String isClose;//结案
	private String remark;//备注
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public String getClassGroup() {
		return classGroup;
	}
	public void setClassGroup(String classGroup) {
		this.classGroup = classGroup;
	}
	public String getWeekStr() {
		return weekStr;
	}
	public void setWeekStr(String weekStr) {
		this.weekStr = weekStr;
	}
	public String getProcedure() {
		return procedure;
	}
	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}
	public String getOfilmModel() {
		return ofilmModel;
	}
	public void setOfilmModel(String ofilmModel) {
		this.ofilmModel = ofilmModel;
	}
	public Integer getStandardCount() {
		return standardCount;
	}
	public void setStandardCount(Integer standardCount) {
		this.standardCount = standardCount;
	}
	public Integer getInputCount() {
		return inputCount;
	}
	public void setInputCount(Integer inputCount) {
		this.inputCount = inputCount;
	}
	public Integer getUnQualityCount() {
		return unQualityCount;
	}
	public void setUnQualityCount(Integer unQualityCount) {
		this.unQualityCount = unQualityCount;
	}
	public String getUnQualityRate() {
		return unQualityRate;
	}
	public void setUnQualityRate(String unQualityRate) {
		this.unQualityRate = unQualityRate;
	}
	public String getProblemDescrible() {
		return problemDescrible;
	}
	public void setProblemDescrible(String problemDescrible) {
		this.problemDescrible = problemDescrible;
	}
	public String getDutyDept() {
		return dutyDept;
	}
	public void setDutyDept(String dutyDept) {
		this.dutyDept = dutyDept;
	}
	public String getEcxeptionLevel() {
		return ecxeptionLevel;
	}
	public void setEcxeptionLevel(String ecxeptionLevel) {
		this.ecxeptionLevel = ecxeptionLevel;
	}
	public String getEcxeptionType() {
		return ecxeptionType;
	}
	public void setEcxeptionType(String ecxeptionType) {
		this.ecxeptionType = ecxeptionType;
	}
	public String getReasonAnalysis() {
		return reasonAnalysis;
	}
	public void setReasonAnalysis(String reasonAnalysis) {
		this.reasonAnalysis = reasonAnalysis;
	}
	public String getCurrentMeasures() {
		return currentMeasures;
	}
	public void setCurrentMeasures(String currentMeasures) {
		this.currentMeasures = currentMeasures;
	}
	public String getCounterMeasures() {
		return counterMeasures;
	}
	public void setCounterMeasures(String counterMeasures) {
		this.counterMeasures = counterMeasures;
	}
	public Date getReplyPeriod() {
		return replyPeriod;
	}
	public void setReplyPeriod(Date replyPeriod) {
		this.replyPeriod = replyPeriod;
	}
	public String getProduceConmfirm() {
		return produceConmfirm;
	}
	public void setProduceConmfirm(String produceConmfirm) {
		this.produceConmfirm = produceConmfirm;
	}
	public String getProjectConmfirm() {
		return projectConmfirm;
	}
	public void setProjectConmfirm(String projectConmfirm) {
		this.projectConmfirm = projectConmfirm;
	}
	public String getIsClose() {
		return isClose;
	}
	public void setIsClose(String isClose) {
		this.isClose = isClose;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	
	
	
}
