package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:供应商稽核报告2.0
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  xjn
 * @version 1.00 2018年2月28日 发布
 */
@Entity
@Table(name="SUPPLIER_YEAR_INSPECT")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class SupplierYearInspect extends WorkflowIdEntity{
	
		/**
		  *SupplierYearInspect.java2018年2月26日
		 */
	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "SUPPLIER_YEAR_INSPECT";//实体编码
	public static final String ENTITY_LIST_NAME = "供应商稽核报告2.0";//实体名称
	private String formNo;//表单编号
	private String supplierName;//供应商名称
	private String supplierCode;//供应商编码
	private String address;//企业地址 供应商地址
	private String shareGroup;//供应事业部
	private String supplyFactory;//供应厂区
	private String supplyMaterial;//供应物料
	private String materialCode;//物料编码
	private String materialName;//物料名称
	private String materialType;//材料类别
	private String auditorType;//审核类型
	private String auditSort;//审核类别
	private String supplierEmail;//供应商邮箱
	private String initiatorPerson;//发起人
	private String initiatorPersonLogin;//发起人登录名
	/*自评*/
	private String supplierLoginName;//供应商登录名
	private Date selfAssessmentDate;//自评日期
	private String selfAssessmentResult;//自评结果
	private String selfAssessmentFile;//自评附件
	private String supplierGrade;//供应商评分
	private String aeo;//优质企业
	/*首次*/
	private Date firstCheckPlanDate;//首次计划稽核日期
	private Date firstCheckDesignDate;//首次实际稽核日期
	private String firstCheckResult;//首次结果
	private String firstGrade;//首次供应商评分
	private String firstAeo;//首次优质企业
	private String firstCheckFile;//首次稽核附件
	private String firstPerson;//首次稽核人
	private String firstPersonLogin;//首次稽核人登录名
	private String againAudit;//再次审核
	private Date firstOperationDate;//首次办理时间
	/*二次*/
	private Date secondCheckPlanDate;//二次计划日期
	private Date secondCheckDesignDate;//二次实际日期
	private String secondCheckResult;//二次结果
	private String secondrade;//二次供应商评分
	private String secondAeo;//二次优质企业
	private String secondCheckFile;//二次稽核附件
	private String secondPerson;//二次稽核人
	private String secondPersonLogin;//二次稽核人登录名
	private Date secondOperationDate;//二次办理时间
	
	/*会签*/
	private String checkDeptMans;//会签人员
	private String checkDeptMansLog;//会签人登陆名
	private String checkLeadMans;//会签领导人员
	private String checkLeadMansLog;//会签领导登陆名
	private String projectChecker;//工程核准
	private String projectCheckerLog;//工程核准登录名
	private String rdChecker;//研发核准
	private String rdCheckerLog;//研发核准人登录名
	private String sqeChecker;//SQE核准
	private String sqeCheckerLogin;//SQE核准
	private String purchaseProcesser;//采购部
	private String purchaseProcesserLogin;//采购部
	private String rdValue="N";//rd
	private String sqeValue="N";//sqe
	private String proValue="N";//工程
	private String purValue="N";//采购
	
	private String projectChecker1;//工程核准主管
	private String projectCheckerLog1;//工程核准主管登录名
	private String rdChecker1;//研发核准主管
	private String rdCheckerLog1;//研发核准主管登录名
	private String sqeChecker1;//SQE核准主管
	private String sqeCheckerLogin1;//SQE核准主管登录名
	private String purchaseProcesser1;//采购部主管
	private String purchaseProcesserLogin1;//采购部主管登录名
	
	/*意见*/
	private String countersignRD;//会签意见研发
	private String countersignSQE;//会签意见SQE
	private String countersignProject;//会签意见工程
	private String countersignPurchase;//会签意见采购
	
	private String countersignRD1;//会签意见研发主管
	private String countersignSQE1;//会签意见SQE主管
	private String countersignProject1;//会签意见工程主管
	private String countersignPurchase1;//会签意见采购主管
	//供应商
	private String improveReportFile;//改善报告
	private Date ncrDate;//不符合项回复时间
	//改善确认
	private String closeState;//关闭状态
	private Date closeDate;//关闭日期
	private String improveRemark;//改善备注
	private String checkPerson;//稽核人
	private String checkPersonLogin;//稽核人登录名
	private String affirmPerson;//确认人
	private String affirmPersonLogin;//确认人登录名
	private Date affirmDate;//确认日期
	//审核
	private String auditorPerson;//审核人
	private String auditorPersonLogin;//审核人登录名
	private Date auditorDate;//审核日期
	private String finalCheckResult;//最终稽核结果
	private String auditorRemark;//审核备注
	private String inspectId;//计划ID
	private Integer creationYear;//创建年份
	
	private String checkDeptMans1;//会签人员确认
	private String checkDeptMansLog1;//会签人登陆名确认
	private String projectChecker2;//工程核准确认
	private String projectCheckerLog2;//工程核准登录名确认
	private String rdChecker2;//研发核准确认
	private String rdCheckerLog2;//研发核准人登录名确认
	private String sqeChecker2;//SQE核准确认
	private String sqeCheckerLogin2;//SQE核准登录名确认
	private String purchaseProcesser2;//采购部确认
	private String purchaseProcesserLogin2;//采购部登录名确认
	private String countersignRD2;//会签意见研发确认
	private String countersignSQE2;//会签意见SQE确认
	private String countersignProject2;//会签意见工程确认
	private String countersignPurchase2;//会签意见采购确认
	private String closeStateRD;//关闭状态RD
	private String closeStatePurchase;//关闭状态采购
	private String closeStateSQE;//关闭状态SQE
	private String closeStateProject;//关闭状态工程
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSupplyFactory() {
		return supplyFactory;
	}
	public void setSupplyFactory(String supplyFactory) {
		this.supplyFactory = supplyFactory;
	}
	public String getSupplyMaterial() {
		return supplyMaterial;
	}
	public void setSupplyMaterial(String supplyMaterial) {
		this.supplyMaterial = supplyMaterial;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public String getSqeChecker() {
		return sqeChecker;
	}
	public void setSqeChecker(String sqeChecker) {
		this.sqeChecker = sqeChecker;
	}
	public String getPurchaseProcesser() {
		return purchaseProcesser;
	}
	public void setPurchaseProcesser(String purchaseProcesser) {
		this.purchaseProcesser = purchaseProcesser;
	}
	public String getSqeCheckerLogin() {
		return sqeCheckerLogin;
	}
	public void setSqeCheckerLogin(String sqeCheckerLogin) {
		this.sqeCheckerLogin = sqeCheckerLogin;
	}
	public String getPurchaseProcesserLogin() {
		return purchaseProcesserLogin;
	}
	public void setPurchaseProcesserLogin(String purchaseProcesserLogin) {
		this.purchaseProcesserLogin = purchaseProcesserLogin;
	}
	public Date getFirstCheckDesignDate() {
		return firstCheckDesignDate;
	}
	public void setFirstCheckDesignDate(Date firstCheckDesignDate) {
		this.firstCheckDesignDate = firstCheckDesignDate;
	}
	public Date getSecondCheckDesignDate() {
		return secondCheckDesignDate;
	}
	public void setSecondCheckDesignDate(Date secondCheckDesignDate) {
		this.secondCheckDesignDate = secondCheckDesignDate;
	}
	public String getFinalCheckResult() {
		return finalCheckResult;
	}
	public void setFinalCheckResult(String finalCheckResult) {
		this.finalCheckResult = finalCheckResult;
	}
	public Date getNcrDate() {
		return ncrDate;
	}
	public void setNcrDate(Date ncrDate) {
		this.ncrDate = ncrDate;
	}
	public String getAuditorPerson() {
		return auditorPerson;
	}
	public void setAuditorPerson(String auditorPerson) {
		this.auditorPerson = auditorPerson;
	}
	public String getAffirmPerson() {
		return affirmPerson;
	}
	public void setAffirmPerson(String affirmPerson) {
		this.affirmPerson = affirmPerson;
	}
	public String getAuditorPersonLogin() {
		return auditorPersonLogin;
	}
	public void setAuditorPersonLogin(String auditorPersonLogin) {
		this.auditorPersonLogin = auditorPersonLogin;
	}
	public String getAffirmPersonLogin() {
		return affirmPersonLogin;
	}
	public void setAffirmPersonLogin(String affirmPersonLogin) {
		this.affirmPersonLogin = affirmPersonLogin;
	}
	public String getAuditorType() {
		return auditorType;
	}
	public void setAuditorType(String auditorType) {
		this.auditorType = auditorType;
	}
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getAuditSort() {
		return auditSort;
	}
	public void setAuditSort(String auditSort) {
		this.auditSort = auditSort;
	}
	public String getInitiatorPerson() {
		return initiatorPerson;
	}
	public void setInitiatorPerson(String initiatorPerson) {
		this.initiatorPerson = initiatorPerson;
	}
	public String getInitiatorPersonLogin() {
		return initiatorPersonLogin;
	}
	public void setInitiatorPersonLogin(String initiatorPersonLogin) {
		this.initiatorPersonLogin = initiatorPersonLogin;
	}
	public Date getSelfAssessmentDate() {
		return selfAssessmentDate;
	}
	public void setSelfAssessmentDate(Date selfAssessmentDate) {
		this.selfAssessmentDate = selfAssessmentDate;
	}
	public String getSelfAssessmentResult() {
		return selfAssessmentResult;
	}
	public void setSelfAssessmentResult(String selfAssessmentResult) {
		this.selfAssessmentResult = selfAssessmentResult;
	}
	public String getSelfAssessmentFile() {
		return selfAssessmentFile;
	}
	public void setSelfAssessmentFile(String selfAssessmentFile) {
		this.selfAssessmentFile = selfAssessmentFile;
	}
	public String getSupplierGrade() {
		return supplierGrade;
	}
	public void setSupplierGrade(String supplierGrade) {
		this.supplierGrade = supplierGrade;
	}
	public String getAeo() {
		return aeo;
	}
	public void setAeo(String aeo) {
		this.aeo = aeo;
	}
	public Date getFirstCheckPlanDate() {
		return firstCheckPlanDate;
	}
	public void setFirstCheckPlanDate(Date firstCheckPlanDate) {
		this.firstCheckPlanDate = firstCheckPlanDate;
	}
	public String getFirstCheckResult() {
		return firstCheckResult;
	}
	public void setFirstCheckResult(String firstCheckResult) {
		this.firstCheckResult = firstCheckResult;
	}
	public String getFirstGrade() {
		return firstGrade;
	}
	public void setFirstGrade(String firstGrade) {
		this.firstGrade = firstGrade;
	}
	public String getFirstAeo() {
		return firstAeo;
	}
	public void setFirstAeo(String firstAeo) {
		this.firstAeo = firstAeo;
	}
	public String getFirstCheckFile() {
		return firstCheckFile;
	}
	public void setFirstCheckFile(String firstCheckFile) {
		this.firstCheckFile = firstCheckFile;
	}
	public String getFirstPerson() {
		return firstPerson;
	}
	public void setFirstPerson(String firstPerson) {
		this.firstPerson = firstPerson;
	}
	public String getFirstPersonLogin() {
		return firstPersonLogin;
	}
	public void setFirstPersonLogin(String firstPersonLogin) {
		this.firstPersonLogin = firstPersonLogin;
	}
	public String getAgainAudit() {
		return againAudit;
	}
	public void setAgainAudit(String againAudit) {
		this.againAudit = againAudit;
	}
	public Date getFirstOperationDate() {
		return firstOperationDate;
	}
	public void setFirstOperationDate(Date firstOperationDate) {
		this.firstOperationDate = firstOperationDate;
	}
	public Date getSecondCheckPlanDate() {
		return secondCheckPlanDate;
	}
	public void setSecondCheckPlanDate(Date secondCheckPlanDate) {
		this.secondCheckPlanDate = secondCheckPlanDate;
	}
	public String getSecondCheckResult() {
		return secondCheckResult;
	}
	public void setSecondCheckResult(String secondCheckResult) {
		this.secondCheckResult = secondCheckResult;
	}
	public String getSecondrade() {
		return secondrade;
	}
	public void setSecondrade(String secondrade) {
		this.secondrade = secondrade;
	}
	public String getSecondAeo() {
		return secondAeo;
	}
	public void setSecondAeo(String secondAeo) {
		this.secondAeo = secondAeo;
	}
	public String getSecondCheckFile() {
		return secondCheckFile;
	}
	public void setSecondCheckFile(String secondCheckFile) {
		this.secondCheckFile = secondCheckFile;
	}
	public String getSecondPerson() {
		return secondPerson;
	}
	public void setSecondPerson(String secondPerson) {
		this.secondPerson = secondPerson;
	}
	public String getSecondPersonLogin() {
		return secondPersonLogin;
	}
	public void setSecondPersonLogin(String secondPersonLogin) {
		this.secondPersonLogin = secondPersonLogin;
	}
	public Date getSecondOperationDate() {
		return secondOperationDate;
	}
	public void setSecondOperationDate(Date secondOperationDate) {
		this.secondOperationDate = secondOperationDate;
	}
	public String getCheckDeptMans() {
		return checkDeptMans;
	}
	public void setCheckDeptMans(String checkDeptMans) {
		this.checkDeptMans = checkDeptMans;
	}
	public String getCheckDeptMansLog() {
		return checkDeptMansLog;
	}
	public void setCheckDeptMansLog(String checkDeptMansLog) {
		this.checkDeptMansLog = checkDeptMansLog;
	}
	public String getCheckLeadMans() {
		return checkLeadMans;
	}
	public void setCheckLeadMans(String checkLeadMans) {
		this.checkLeadMans = checkLeadMans;
	}
	public String getCheckLeadMansLog() {
		return checkLeadMansLog;
	}
	public void setCheckLeadMansLog(String checkLeadMansLog) {
		this.checkLeadMansLog = checkLeadMansLog;
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
	public String getRdChecker() {
		return rdChecker;
	}
	public void setRdChecker(String rdChecker) {
		this.rdChecker = rdChecker;
	}
	public String getRdCheckerLog() {
		return rdCheckerLog;
	}
	public void setRdCheckerLog(String rdCheckerLog) {
		this.rdCheckerLog = rdCheckerLog;
	}
	public String getImproveReportFile() {
		return improveReportFile;
	}
	public void setImproveReportFile(String improveReportFile) {
		this.improveReportFile = improveReportFile;
	}
	public Date getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	public String getImproveRemark() {
		return improveRemark;
	}
	public void setImproveRemark(String improveRemark) {
		this.improveRemark = improveRemark;
	}
	public String getAuditorRemark() {
		return auditorRemark;
	}
	public void setAuditorRemark(String auditorRemark) {
		this.auditorRemark = auditorRemark;
	}
	public String getCountersignRD() {
		return countersignRD;
	}
	public void setCountersignRD(String countersignRD) {
		this.countersignRD = countersignRD;
	}
	public String getCountersignSQE() {
		return countersignSQE;
	}
	public void setCountersignSQE(String countersignSQE) {
		this.countersignSQE = countersignSQE;
	}
	public String getCountersignProject() {
		return countersignProject;
	}
	public void setCountersignProject(String countersignProject) {
		this.countersignProject = countersignProject;
	}
	public String getCountersignPurchase() {
		return countersignPurchase;
	}
	public void setCountersignPurchase(String countersignPurchase) {
		this.countersignPurchase = countersignPurchase;
	}
	public String getProjectChecker1() {
		return projectChecker1;
	}
	public void setProjectChecker1(String projectChecker1) {
		this.projectChecker1 = projectChecker1;
	}
	public String getProjectCheckerLog1() {
		return projectCheckerLog1;
	}
	public void setProjectCheckerLog1(String projectCheckerLog1) {
		this.projectCheckerLog1 = projectCheckerLog1;
	}
	public String getRdChecker1() {
		return rdChecker1;
	}
	public void setRdChecker1(String rdChecker1) {
		this.rdChecker1 = rdChecker1;
	}
	public String getRdCheckerLog1() {
		return rdCheckerLog1;
	}
	public void setRdCheckerLog1(String rdCheckerLog1) {
		this.rdCheckerLog1 = rdCheckerLog1;
	}
	public String getSqeChecker1() {
		return sqeChecker1;
	}
	public void setSqeChecker1(String sqeChecker1) {
		this.sqeChecker1 = sqeChecker1;
	}
	public String getSqeCheckerLogin1() {
		return sqeCheckerLogin1;
	}
	public void setSqeCheckerLogin1(String sqeCheckerLogin1) {
		this.sqeCheckerLogin1 = sqeCheckerLogin1;
	}
	public String getPurchaseProcesser1() {
		return purchaseProcesser1;
	}
	public void setPurchaseProcesser1(String purchaseProcesser1) {
		this.purchaseProcesser1 = purchaseProcesser1;
	}
	public String getPurchaseProcesserLogin1() {
		return purchaseProcesserLogin1;
	}
	public void setPurchaseProcesserLogin1(String purchaseProcesserLogin1) {
		this.purchaseProcesserLogin1 = purchaseProcesserLogin1;
	}
	public String getCountersignRD1() {
		return countersignRD1;
	}
	public void setCountersignRD1(String countersignRD1) {
		this.countersignRD1 = countersignRD1;
	}
	public String getCountersignSQE1() {
		return countersignSQE1;
	}
	public void setCountersignSQE1(String countersignSQE1) {
		this.countersignSQE1 = countersignSQE1;
	}
	public String getCountersignProject1() {
		return countersignProject1;
	}
	public void setCountersignProject1(String countersignProject1) {
		this.countersignProject1 = countersignProject1;
	}
	public String getCountersignPurchase1() {
		return countersignPurchase1;
	}
	public void setCountersignPurchase1(String countersignPurchase1) {
		this.countersignPurchase1 = countersignPurchase1;
	}
	public String getInspectId() {
		return inspectId;
	}
	public void setInspectId(String inspectId) {
		this.inspectId = inspectId;
	}
	public String getShareGroup() {
		return shareGroup;
	}
	public void setShareGroup(String shareGroup) {
		this.shareGroup = shareGroup;
	}
	public String getCloseState() {
		return closeState;
	}
	public void setCloseState(String closeState) {
		this.closeState = closeState;
	}
	public String getCheckPerson() {
		return checkPerson;
	}
	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}
	public String getCheckPersonLogin() {
		return checkPersonLogin;
	}
	public void setCheckPersonLogin(String checkPersonLogin) {
		this.checkPersonLogin = checkPersonLogin;
	}
	public String getSupplierLoginName() {
		return supplierLoginName;
	}
	public void setSupplierLoginName(String supplierLoginName) {
		this.supplierLoginName = supplierLoginName;
	}
	public Integer getCreationYear() {
		return creationYear;
	}
	public void setCreationYear(Integer creationYear) {
		this.creationYear = creationYear;
	}
	public Date getAffirmDate() {
		return affirmDate;
	}
	public void setAffirmDate(Date affirmDate) {
		this.affirmDate = affirmDate;
	}
	public Date getAuditorDate() {
		return auditorDate;
	}
	public void setAuditorDate(Date auditorDate) {
		this.auditorDate = auditorDate;
	}
	public String getRdValue() {
		return rdValue;
	}
	public void setRdValue(String rdValue) {
		this.rdValue = rdValue;
	}
	public String getProValue() {
		return proValue;
	}
	public void setProValue(String proValue) {
		this.proValue = proValue;
	}
	public String getPurValue() {
		return purValue;
	}
	public void setPurValue(String purValue) {
		this.purValue = purValue;
	}
	public String getSqeValue() {
		return sqeValue;
	}
	public void setSqeValue(String sqeValue) {
		this.sqeValue = sqeValue;
	}
	public String getSupplierEmail() {
		return supplierEmail;
	}
	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getCheckDeptMans1() {
		return checkDeptMans1;
	}
	public void setCheckDeptMans1(String checkDeptMans1) {
		this.checkDeptMans1 = checkDeptMans1;
	}
	public String getCheckDeptMansLog1() {
		return checkDeptMansLog1;
	}
	public void setCheckDeptMansLog1(String checkDeptMansLog1) {
		this.checkDeptMansLog1 = checkDeptMansLog1;
	}
	public String getProjectChecker2() {
		return projectChecker2;
	}
	public void setProjectChecker2(String projectChecker2) {
		this.projectChecker2 = projectChecker2;
	}
	public String getProjectCheckerLog2() {
		return projectCheckerLog2;
	}
	public void setProjectCheckerLog2(String projectCheckerLog2) {
		this.projectCheckerLog2 = projectCheckerLog2;
	}
	public String getRdChecker2() {
		return rdChecker2;
	}
	public void setRdChecker2(String rdChecker2) {
		this.rdChecker2 = rdChecker2;
	}
	public String getRdCheckerLog2() {
		return rdCheckerLog2;
	}
	public void setRdCheckerLog2(String rdCheckerLog2) {
		this.rdCheckerLog2 = rdCheckerLog2;
	}
	public String getSqeChecker2() {
		return sqeChecker2;
	}
	public void setSqeChecker2(String sqeChecker2) {
		this.sqeChecker2 = sqeChecker2;
	}
	public String getSqeCheckerLogin2() {
		return sqeCheckerLogin2;
	}
	public void setSqeCheckerLogin2(String sqeCheckerLogin2) {
		this.sqeCheckerLogin2 = sqeCheckerLogin2;
	}
	public String getPurchaseProcesser2() {
		return purchaseProcesser2;
	}
	public void setPurchaseProcesser2(String purchaseProcesser2) {
		this.purchaseProcesser2 = purchaseProcesser2;
	}
	public String getPurchaseProcesserLogin2() {
		return purchaseProcesserLogin2;
	}
	public void setPurchaseProcesserLogin2(String purchaseProcesserLogin2) {
		this.purchaseProcesserLogin2 = purchaseProcesserLogin2;
	}
	public String getCountersignRD2() {
		return countersignRD2;
	}
	public void setCountersignRD2(String countersignRD2) {
		this.countersignRD2 = countersignRD2;
	}
	public String getCountersignSQE2() {
		return countersignSQE2;
	}
	public void setCountersignSQE2(String countersignSQE2) {
		this.countersignSQE2 = countersignSQE2;
	}
	public String getCountersignProject2() {
		return countersignProject2;
	}
	public void setCountersignProject2(String countersignProject2) {
		this.countersignProject2 = countersignProject2;
	}
	public String getCountersignPurchase2() {
		return countersignPurchase2;
	}
	public void setCountersignPurchase2(String countersignPurchase2) {
		this.countersignPurchase2 = countersignPurchase2;
	}
	public String getCloseStateRD() {
		return closeStateRD;
	}
	public void setCloseStateRD(String closeStateRD) {
		this.closeStateRD = closeStateRD;
	}
	public String getCloseStatePurchase() {
		return closeStatePurchase;
	}
	public void setCloseStatePurchase(String closeStatePurchase) {
		this.closeStatePurchase = closeStatePurchase;
	}
	public String getCloseStateSQE() {
		return closeStateSQE;
	}
	public void setCloseStateSQE(String closeStateSQE) {
		this.closeStateSQE = closeStateSQE;
	}
	public String getCloseStateProject() {
		return closeStateProject;
	}
	public void setCloseStateProject(String closeStateProject) {
		this.closeStateProject = closeStateProject;
	}
	
}
