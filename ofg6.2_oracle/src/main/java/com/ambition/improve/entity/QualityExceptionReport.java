package com.ambition.improve.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:品质异常联络单
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2017年4月22日 发布
 */
@Entity
@Table(name = "IMP_QUALITY_EXCEPTION_REPORT")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class QualityExceptionReport extends WorkflowIdEntity{

	private static final long serialVersionUID = 1L;
	public static final String ENTITY_LIST_CODE = "IMP_QUALITY_EXCEPTION_REPORT";//实体_列表_编码
	public static final String ENTITY_LIST_NAME = "品质异常联络单";//实体_列表_名称
	private String formNo;//表单编号
	private String managerNo;//管理编号
	private String factory; //
	private String procedure;//工序
	private String classGroup;//班别
	private String customerName;//客户名称
	private String customerModel;//客户机型
	private String ofilmModel;//欧菲机型
	private String productStage;//产品阶段
	private String problemDegree;//问题严重度
	private String productModel;//产品型号
	private Date exceptionDate;//异常日期
	private String processCard;//流程卡号
	private String exceptionItem;//异常项目
	private String lotNum;//不良数/Lot数量
	private String supplierName;//供应商名称
	private String supplierCode;//供应商编码
	private String materialType;//物料类别
	private String problemBelong;//问题归属
	@Column(length=1000)
	private String exceptionDescrible;//异常描述
	private String exceptionDescribleFile;//异常描述附件
	private String presentDept;//提出单位
	private String presentMan;//提出人
	private String presentManLogin;//提出人登录名
	private String presentDeptMan;//提出单位确认人
	private String presentDeptManLogin;//提出单位确认人登录名
	@Column(length=1000)
	private String exceptionDescribleConfirm;//异常描述确认
	private String engineerMan;//工程确认人
	private String engineerManLogin;//工程确认人登录名
	private String qualityMan;//品保确认人
	private String qualityManLogin;//品保确认人登录名
	@Column(length=1000)
	private String emergencyMeasures;//临时对策
	private String emergencyFile;//临时对策附件
	private String dutyDept1;//措施责任单位
	private String dutyMan1;//措施责任人
	private String dutyMan1Login;//措施责任人登录名
	@Column(length=1000)
	private String reasonAnalysis;//原因分析
	private String reasonAnalysisFile;//原因分析附件
	private String dutyDept2;//原因责任单位
	private String dutyMan2;//原因责任人
	private String dutyMan2Login;//原因责任人登录名
	private String approvalMan2;//原因作成人
	private String approvalMan2Login;//原因作成人登录名
	@Column(length=1000)
	private String shortMeasures;//短期对策
	@Column(length=1000)
	private String longMeasures;//长期对策
	@Column(length=1000)
	private String improvementMeasures;//改善措施
	private String improvementMeasuresFile;//改善措施附件
	private String dutyMan3;//改善责任人
	private String dutyMan3Login;//改善责任人登录名
	private Date planDate;//计划完成日
	private String qcMan;//QC确认人
	private String qcManLogin;//QC确认人登录名	
	private Date actualDate;//实际完成日
	@Column(length=1000)
	private String effectConfirm;//效果确认
	private String dutyMan4;//效果确认人
	private String dutyMan4Login;//效果确认人登录名
	private String approvalMan4;//效果作成人
	private String approvalMan4Login;//效果作成人登录名
	private String isShare="否";//是否共享案例
	private String mfgReportNo;//制程报告编号
	private String mfgReportId;//制程报告ID
	private String shareText;//共享原因
	private String isClose="否";//是否结案
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getProblemDegree() {
		return problemDegree;
	}
	public void setProblemDegree(String problemDegree) {
		this.problemDegree = problemDegree;
	}
	public String getProductModel() {
		return productModel;
	}
	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}
	public Date getExceptionDate() {
		return exceptionDate;
	}
	public void setExceptionDate(Date exceptionDate) {
		this.exceptionDate = exceptionDate;
	}
	public String getProcessCard() {
		return processCard;
	}
	public void setProcessCard(String processCard) {
		this.processCard = processCard;
	}
	public String getExceptionItem() {
		return exceptionItem;
	}
	public void setExceptionItem(String exceptionItem) {
		this.exceptionItem = exceptionItem;
	}
	public String getLotNum() {
		return lotNum;
	}
	public void setLotNum(String lotNum) {
		this.lotNum = lotNum;
	}
	public String getExceptionDescrible() {
		return exceptionDescrible;
	}
	public void setExceptionDescrible(String exceptionDescrible) {
		this.exceptionDescrible = exceptionDescrible;
	}
	public String getPresentMan() {
		return presentMan;
	}
	public void setPresentMan(String presentMan) {
		this.presentMan = presentMan;
	}
	public String getPresentManLogin() {
		return presentManLogin;
	}
	public void setPresentManLogin(String presentManLogin) {
		this.presentManLogin = presentManLogin;
	}
	public String getPresentDeptMan() {
		return presentDeptMan;
	}
	public void setPresentDeptMan(String presentDeptMan) {
		this.presentDeptMan = presentDeptMan;
	}
	public String getPresentDeptManLogin() {
		return presentDeptManLogin;
	}
	public void setPresentDeptManLogin(String presentDeptManLogin) {
		this.presentDeptManLogin = presentDeptManLogin;
	}
	public String getExceptionDescribleConfirm() {
		return exceptionDescribleConfirm;
	}
	public void setExceptionDescribleConfirm(String exceptionDescribleConfirm) {
		this.exceptionDescribleConfirm = exceptionDescribleConfirm;
	}
	public String getEngineerMan() {
		return engineerMan;
	}
	public void setEngineerMan(String engineerMan) {
		this.engineerMan = engineerMan;
	}
	public String getEngineerManLogin() {
		return engineerManLogin;
	}
	public void setEngineerManLogin(String engineerManLogin) {
		this.engineerManLogin = engineerManLogin;
	}
	public String getQualityMan() {
		return qualityMan;
	}
	public void setQualityMan(String qualityMan) {
		this.qualityMan = qualityMan;
	}
	public String getQualityManLogin() {
		return qualityManLogin;
	}
	public void setQualityManLogin(String qualityManLogin) {
		this.qualityManLogin = qualityManLogin;
	}
	public String getEmergencyMeasures() {
		return emergencyMeasures;
	}
	public void setEmergencyMeasures(String emergencyMeasures) {
		this.emergencyMeasures = emergencyMeasures;
	}
	public String getDutyDept1() {
		return dutyDept1;
	}
	public void setDutyDept1(String dutyDept1) {
		this.dutyDept1 = dutyDept1;
	}
	public String getDutyMan1() {
		return dutyMan1;
	}
	public void setDutyMan1(String dutyMan1) {
		this.dutyMan1 = dutyMan1;
	}
	public String getDutyMan1Login() {
		return dutyMan1Login;
	}
	public void setDutyMan1Login(String dutyMan1Login) {
		this.dutyMan1Login = dutyMan1Login;
	}
	public String getReasonAnalysis() {
		return reasonAnalysis;
	}
	public void setReasonAnalysis(String reasonAnalysis) {
		this.reasonAnalysis = reasonAnalysis;
	}
	public String getDutyDept2() {
		return dutyDept2;
	}
	public void setDutyDept2(String dutyDept2) {
		this.dutyDept2 = dutyDept2;
	}
	public String getDutyMan2() {
		return dutyMan2;
	}
	public void setDutyMan2(String dutyMan2) {
		this.dutyMan2 = dutyMan2;
	}
	public String getDutyMan2Login() {
		return dutyMan2Login;
	}
	public void setDutyMan2Login(String dutyMan2Login) {
		this.dutyMan2Login = dutyMan2Login;
	}
	public String getApprovalMan2() {
		return approvalMan2;
	}
	public void setApprovalMan2(String approvalMan2) {
		this.approvalMan2 = approvalMan2;
	}
	public String getApprovalMan2Login() {
		return approvalMan2Login;
	}
	public void setApprovalMan2Login(String approvalMan2Login) {
		this.approvalMan2Login = approvalMan2Login;
	}
	public String getImprovementMeasures() {
		return improvementMeasures;
	}
	public void setImprovementMeasures(String improvementMeasures) {
		this.improvementMeasures = improvementMeasures;
	}
	public String getDutyMan3() {
		return dutyMan3;
	}
	public void setDutyMan3(String dutyMan3) {
		this.dutyMan3 = dutyMan3;
	}
	public String getDutyMan3Login() {
		return dutyMan3Login;
	}
	public void setDutyMan3Login(String dutyMan3Login) {
		this.dutyMan3Login = dutyMan3Login;
	}
	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}

	public String getQcMan() {
		return qcMan;
	}
	public void setQcMan(String qcMan) {
		this.qcMan = qcMan;
	}
	public String getQcManLogin() {
		return qcManLogin;
	}
	public void setQcManLogin(String qcManLogin) {
		this.qcManLogin = qcManLogin;
	}
	public Date getActualDate() {
		return actualDate;
	}
	public void setActualDate(Date actualDate) {
		this.actualDate = actualDate;
	}
	public String getEffectConfirm() {
		return effectConfirm;
	}
	public void setEffectConfirm(String effectConfirm) {
		this.effectConfirm = effectConfirm;
	}
	public String getDutyMan4() {
		return dutyMan4;
	}
	public void setDutyMan4(String dutyMan4) {
		this.dutyMan4 = dutyMan4;
	}
	public String getDutyMan4Login() {
		return dutyMan4Login;
	}
	public void setDutyMan4Login(String dutyMan4Login) {
		this.dutyMan4Login = dutyMan4Login;
	}
	public String getApprovalMan4() {
		return approvalMan4;
	}
	public void setApprovalMan4(String approvalMan4) {
		this.approvalMan4 = approvalMan4;
	}
	public String getApprovalMan4Login() {
		return approvalMan4Login;
	}
	public void setApprovalMan4Login(String approvalMan4Login) {
		this.approvalMan4Login = approvalMan4Login;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
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
	public String getProductStage() {
		return productStage;
	}
	public void setProductStage(String productStage) {
		this.productStage = productStage;
	}
	public String getCustomerModel() {
		return customerModel;
	}
	public void setCustomerModel(String customerModel) {
		this.customerModel = customerModel;
	}
	public String getPresentDept() {
		return presentDept;
	}
	public void setPresentDept(String presentDept) {
		this.presentDept = presentDept;
	}
	public String getExceptionDescribleFile() {
		return exceptionDescribleFile;
	}
	public void setExceptionDescribleFile(String exceptionDescribleFile) {
		this.exceptionDescribleFile = exceptionDescribleFile;
	}
	public String getReasonAnalysisFile() {
		return reasonAnalysisFile;
	}
	public void setReasonAnalysisFile(String reasonAnalysisFile) {
		this.reasonAnalysisFile = reasonAnalysisFile;
	}
	public String getImprovementMeasuresFile() {
		return improvementMeasuresFile;
	}
	public void setImprovementMeasuresFile(String improvementMeasuresFile) {
		this.improvementMeasuresFile = improvementMeasuresFile;
	}
	public String getIsShare() {
		return isShare;
	}
	public void setIsShare(String isShare) {
		this.isShare = isShare;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getClassGroup() {
		return classGroup;
	}
	public void setClassGroup(String classGroup) {
		this.classGroup = classGroup;
	}
	public String getMfgReportNo() {
		return mfgReportNo;
	}
	public void setMfgReportNo(String mfgReportNo) {
		this.mfgReportNo = mfgReportNo;
	}
	public String getMfgReportId() {
		return mfgReportId;
	}
	public void setMfgReportId(String mfgReportId) {
		this.mfgReportId = mfgReportId;
	}
	public String getShareText() {
		return shareText;
	}
	public void setShareText(String shareText) {
		this.shareText = shareText;
	}
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
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public String getProblemBelong() {
		return problemBelong;
	}
	public void setProblemBelong(String problemBelong) {
		this.problemBelong = problemBelong;
	}
	public String getEmergencyFile() {
		return emergencyFile;
	}
	public void setEmergencyFile(String emergencyFile) {
		this.emergencyFile = emergencyFile;
	}
	public String getShortMeasures() {
		return shortMeasures;
	}
	public void setShortMeasures(String shortMeasures) {
		this.shortMeasures = shortMeasures;
	}
	public String getLongMeasures() {
		return longMeasures;
	}
	public void setLongMeasures(String longMeasures) {
		this.longMeasures = longMeasures;
	}
	public String getIsClose() {
		return isClose;
	}
	public void setIsClose(String isClose) {
		this.isClose = isClose;
	}
	public String getManagerNo() {
		return managerNo;
	}
	public void setManagerNo(String managerNo) {
		this.managerNo = managerNo;
	}	
	
}
