package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;

/**
 * 类名:供应商变更
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月12日 发布
 */
@Entity
@Table(name = "SUPPLIER_CHANGE")
public class SupplierChange extends  WorkflowIdEntity {

		/**
		  *SupplierChange.java2016年10月12日
		 */
	private static final long serialVersionUID = 1L;
	private String formNo;//
	private String applicant;//申请公司
	private Date applyingDate;//申请日期
	private String reason;//申请事由
	private String businessUnit;//事业部
	private String materialName;//材料名
	private String materialCode;//物料编码
	private String applyingProject;//适用机型
	private String projectChange;//变更项目
	private String changeLevel;//变更等级
	private String contentBefore;//变更前内容
	private String beforeFile;
	private String contentAfter;//变更后内容
	private String afterFile;
	private String assessmentReport;//评估报告
	private String reportFile;
	
	//办理人
	private String procurementProcesser;//采购办理人
	private String procurementProcesserLog;//采购登录名
	private String projectProcesser;//工程办理人
	private String projectProcesserLog;//工程登录名
	private String developProcesser;//研发办理人
	private String developProcessLog;//研发登录名
	private String sqeProcesser;//sqe办理人
	private String sqeProcesserLog;//sqe登录名
	private String qeProcesser;//qe办理人
	private String qeProcesserLog;//qe登录名
	private String qualityProcesser;//品质办理人
	private String qualityProcesserLog;//品质登录名
	private String qaProcesser;//qa办理人
	private String qaProcesserLog;//qa登录名
	
	//核准
	private String qualityChecker;//采购部审核人
	private String qualityCheckerLog;
	private String developChecker;//研发部审核人
	private String developCheckLog;
	private String qeChecker;//qe审核
	private String qeCheckerLog;//
	private String projectChecker;//工程核准
	private String projectCheckerLog;//
	private String sqeChecker;//SQE核准
	private String sqeCheckerLog;//SQE核准登录名
	
	//意见
	private String procurementOpinion;//采购部意见
	private String projectOpinion;//工程意见
	private String developOpinion;//研发部意见
	private String qualityOpinion;//品质部意见
	private String sqeOpinion;//SQE意见
	private String qeOpinion;//QE意见
	private String qaOpinion;//qa意见
	
	//意见
	private String procurementOpinion1;//采购部意见
	private String projectOpinion1;//工程意见
	private String developOpinion1;//研发部意见
	private String qualityOpinion1;//品质部意见
	private String sqeOpinion1;//SQE意见
	private String qeOpinion1;//QE意见
	private String qaOpinion1;//qa意见

	//附件
	private String procurementFile;//采购部附件
	private String projectFile;//工程附件
	private String developFile;//研发附件
	private String sqeFile;//SQE附件
	private String qualityFile;//品质附件
	private String qeFile;//QE附件
	private String qaFile;//qa附件
	
	private String isMailCustomer;//是否需要通知客户
	private String checkDeptMansLog;//会签人员登录名
	
	/*
	 * 承认状态
	 */
	private String procurementStatus;//承认状态采购
	private String projectStatus;//承认状态工程
	private String rdStatus;//承认状态研发
	private String sqeStatus;//承认状态SQE
	private String qeStatus;//承认状态QE
	private String qaStatus;//承认状态QA
	private String npiStatus;//承认状态NPI
	private String dqeStatus;//承认状态DQE
	private String finalStatus;//最终状态
	
	//用于集成判断
	private  Long sourceId;//集成过来原ID
	private String sourceUnit;//集成的事业部

	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	
	public Date getApplyingDate() {
		return applyingDate;
	}
	public void setApplyingDate(Date applyingDate) {
		this.applyingDate = applyingDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getApplyingProject() {
		return applyingProject;
	}
	public void setApplyingProject(String applyingProject) {
		this.applyingProject = applyingProject;
	}
	public String getProjectChange() {
		return projectChange;
	}
	public void setProjectChange(String projectChange) {
		this.projectChange = projectChange;
	}
	public String getChangeLevel() {
		return changeLevel;
	}
	public void setChangeLevel(String changeLevel) {
		this.changeLevel = changeLevel;
	}
	public String getContentBefore() {
		return contentBefore;
	}
	public void setContentBefore(String contentBefore) {
		this.contentBefore = contentBefore;
	}
	public String getBeforeFile() {
		return beforeFile;
	}
	public void setBeforeFile(String beforeFile) {
		this.beforeFile = beforeFile;
	}
	public String getContentAfter() {
		return contentAfter;
	}
	public void setContentAfter(String contentAfter) {
		this.contentAfter = contentAfter;
	}
	public String getAfterFile() {
		return afterFile;
	}
	public void setAfterFile(String afterFile) {
		this.afterFile = afterFile;
	}
	public String getProcurementOpinion() {
		return procurementOpinion;
	}
	public void setProcurementOpinion(String procurementOpinion) {
		this.procurementOpinion = procurementOpinion;
	}
	public String getProcurementProcesser() {
		return procurementProcesser;
	}
	public void setProcurementProcesser(String procurementProcesser) {
		this.procurementProcesser = procurementProcesser;
	}
	public String getProcurementProcesserLog() {
		return procurementProcesserLog;
	}
	public void setProcurementProcesserLog(String procurementProcesserLog) {
		this.procurementProcesserLog = procurementProcesserLog;
	}
	public String getQualityOpinion() {
		return qualityOpinion;
	}
	public void setQualityOpinion(String qualityOpinion) {
		this.qualityOpinion = qualityOpinion;
	}
	public String getQualityFile() {
		return qualityFile;
	}
	public void setQualityFile(String qualityFile) {
		this.qualityFile = qualityFile;
	}
	public String getQualityProcesser() {
		return qualityProcesser;
	}
	public void setQualityProcesser(String qualityProcesser) {
		this.qualityProcesser = qualityProcesser;
	}
	public String getQualityProcesserLog() {
		return qualityProcesserLog;
	}
	public void setQualityProcesserLog(String qualityProcesserLog) {
		this.qualityProcesserLog = qualityProcesserLog;
	}
	public String getQualityChecker() {
		return qualityChecker;
	}
	public void setQualityChecker(String qualityChecker) {
		this.qualityChecker = qualityChecker;
	}
	public String getQualityCheckerLog() {
		return qualityCheckerLog;
	}
	public void setQualityCheckerLog(String qualityCheckerLog) {
		this.qualityCheckerLog = qualityCheckerLog;
	}
	public String getDevelopOpinion() {
		return developOpinion;
	}
	public void setDevelopOpinion(String developOpinion) {
		this.developOpinion = developOpinion;
	}
	public String getDevelopProcesser() {
		return developProcesser;
	}
	public void setDevelopProcesser(String developProcesser) {
		this.developProcesser = developProcesser;
	}
	public String getDevelopProcessLog() {
		return developProcessLog;
	}
	public void setDevelopProcessLog(String developProcessLog) {
		this.developProcessLog = developProcessLog;
	}
	public String getDevelopFile() {
		return developFile;
	}
	public void setDevelopFile(String developFile) {
		this.developFile = developFile;
	}
	public String getDevelopChecker() {
		return developChecker;
	}
	public void setDevelopChecker(String developChecker) {
		this.developChecker = developChecker;
	}
	public String getDevelopCheckLog() {
		return developCheckLog;
	}
	public void setDevelopCheckLog(String developCheckLog) {
		this.developCheckLog = developCheckLog;
	}
	public String getAssessmentReport() {
		return assessmentReport;
	}
	public void setAssessmentReport(String assessmentReport) {
		this.assessmentReport = assessmentReport;
	}
	public String getReportFile() {
		return reportFile;
	}
	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	public String getProjectChecker() {
		return projectChecker;
	}
	public void setProjectChecker(String projectChecker) {
		this.projectChecker = projectChecker;
	}
	public String getProjectCheckerLog() {
		return projectCheckerLog;
	}
	public void setProjectCheckerLog(String projectCheckerLog) {
		this.projectCheckerLog = projectCheckerLog;
	}
	public String getQeChecker() {
		return qeChecker;
	}
	public void setQeChecker(String qeChecker) {
		this.qeChecker = qeChecker;
	}
	public String getQeCheckerLog() {
		return qeCheckerLog;
	}
	public void setQeCheckerLog(String qeCheckerLog) {
		this.qeCheckerLog = qeCheckerLog;
	}
	public String getSqeChecker() {
		return sqeChecker;
	}
	public void setSqeChecker(String sqeChecker) {
		this.sqeChecker = sqeChecker;
	}
	public String getSqeCheckerLog() {
		return sqeCheckerLog;
	}
	public void setSqeCheckerLog(String sqeCheckerLog) {
		this.sqeCheckerLog = sqeCheckerLog;
	}
	public String getProjectProcesser() {
		return projectProcesser;
	}
	public void setProjectProcesser(String projectProcesser) {
		this.projectProcesser = projectProcesser;
	}
	public String getProjectProcesserLog() {
		return projectProcesserLog;
	}
	public void setProjectProcesserLog(String projectProcesserLog) {
		this.projectProcesserLog = projectProcesserLog;
	}
	public String getQeOpinion() {
		return qeOpinion;
	}
	public void setQeOpinion(String qeOpinion) {
		this.qeOpinion = qeOpinion;
	}
	public String getProjectOpinion() {
		return projectOpinion;
	}
	public void setProjectOpinion(String projectOpinion) {
		this.projectOpinion = projectOpinion;
	}
	public String getProcurementFile() {
		return procurementFile;
	}
	public void setProcurementFile(String procurementFile) {
		this.procurementFile = procurementFile;
	}
	public String getSqeProcesser() {
		return sqeProcesser;
	}
	public void setSqeProcesser(String sqeProcesser) {
		this.sqeProcesser = sqeProcesser;
	}
	public String getSqeProcesserLog() {
		return sqeProcesserLog;
	}
	public void setSqeProcesserLog(String sqeProcesserLog) {
		this.sqeProcesserLog = sqeProcesserLog;
	}
	public String getSqeFile() {
		return sqeFile;
	}
	public void setSqeFile(String sqeFile) {
		this.sqeFile = sqeFile;
	}
	public String getSqeOpinion() {
		return sqeOpinion;
	}
	public void setSqeOpinion(String sqeOpinion) {
		this.sqeOpinion = sqeOpinion;
	}
	public String getQeProcesser() {
		return qeProcesser;
	}
	public void setQeProcesser(String qeProcesser) {
		this.qeProcesser = qeProcesser;
	}
	public String getQeProcesserLog() {
		return qeProcesserLog;
	}
	public void setQeProcesserLog(String qeProcesserLog) {
		this.qeProcesserLog = qeProcesserLog;
	}
	public String getQeFile() {
		return qeFile;
	}
	public void setQeFile(String qeFile) {
		this.qeFile = qeFile;
	}
	public String getQaProcesser() {
		return qaProcesser;
	}
	public void setQaProcesser(String qaProcesser) {
		this.qaProcesser = qaProcesser;
	}
	public String getQaProcesserLog() {
		return qaProcesserLog;
	}
	public void setQaProcesserLog(String qaProcesserLog) {
		this.qaProcesserLog = qaProcesserLog;
	}
	public String getQaOpinion() {
		return qaOpinion;
	}
	public void setQaOpinion(String qaOpinion) {
		this.qaOpinion = qaOpinion;
	}
	public String getProcurementStatus() {
		return procurementStatus;
	}
	public void setProcurementStatus(String procurementStatus) {
		this.procurementStatus = procurementStatus;
	}
	public String getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	public String getRdStatus() {
		return rdStatus;
	}
	public void setRdStatus(String rdStatus) {
		this.rdStatus = rdStatus;
	}
	public String getSqeStatus() {
		return sqeStatus;
	}
	public void setSqeStatus(String sqeStatus) {
		this.sqeStatus = sqeStatus;
	}
	public String getQeStatus() {
		return qeStatus;
	}
	public void setQeStatus(String qeStatus) {
		this.qeStatus = qeStatus;
	}
	public String getQaStatus() {
		return qaStatus;
	}
	public void setQaStatus(String qaStatus) {
		this.qaStatus = qaStatus;
	}
	public String getNpiStatus() {
		return npiStatus;
	}
	public void setNpiStatus(String npiStatus) {
		this.npiStatus = npiStatus;
	}
	public String getDqeStatus() {
		return dqeStatus;
	}
	public void setDqeStatus(String dqeStatus) {
		this.dqeStatus = dqeStatus;
	}
	public String getFinalStatus() {
		return finalStatus;
	}
	public void setFinalStatus(String finalStatus) {
		this.finalStatus = finalStatus;
	}
	public String getQaFile() {
		return qaFile;
	}
	public void setQaFile(String qaFile) {
		this.qaFile = qaFile;
	}
	public String getProjectFile() {
		return projectFile;
	}
	public void setProjectFile(String projectFile) {
		this.projectFile = projectFile;
	}
	public String getProcurementOpinion1() {
		return procurementOpinion1;
	}
	public void setProcurementOpinion1(String procurementOpinion1) {
		this.procurementOpinion1 = procurementOpinion1;
	}
	public String getProjectOpinion1() {
		return projectOpinion1;
	}
	public void setProjectOpinion1(String projectOpinion1) {
		this.projectOpinion1 = projectOpinion1;
	}
	public String getDevelopOpinion1() {
		return developOpinion1;
	}
	public void setDevelopOpinion1(String developOpinion1) {
		this.developOpinion1 = developOpinion1;
	}
	public String getQualityOpinion1() {
		return qualityOpinion1;
	}
	public void setQualityOpinion1(String qualityOpinion1) {
		this.qualityOpinion1 = qualityOpinion1;
	}
	public String getSqeOpinion1() {
		return sqeOpinion1;
	}
	public void setSqeOpinion1(String sqeOpinion1) {
		this.sqeOpinion1 = sqeOpinion1;
	}
	public String getQeOpinion1() {
		return qeOpinion1;
	}
	public void setQeOpinion1(String qeOpinion1) {
		this.qeOpinion1 = qeOpinion1;
	}
	public String getQaOpinion1() {
		return qaOpinion1;
	}
	public void setQaOpinion1(String qaOpinion1) {
		this.qaOpinion1 = qaOpinion1;
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
	public String getIsMailCustomer() {
		return isMailCustomer;
	}
	public void setIsMailCustomer(String isMailCustomer) {
		this.isMailCustomer = isMailCustomer;
	}
	public String getCheckDeptMansLog() {
		return checkDeptMansLog;
	}
	public void setCheckDeptMansLog(String checkDeptMansLog) {
		this.checkDeptMansLog = checkDeptMansLog;
	}
	
	
	
}
