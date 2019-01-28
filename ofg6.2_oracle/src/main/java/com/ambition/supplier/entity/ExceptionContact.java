package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**    
 * 物料异常联络单
 * @authorBy LPF
 *
 */
@Entity
@Table(name = "SUPPLIER_EXCEPTION_CONTACT")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class ExceptionContact extends  WorkflowIdEntity{
	private static final long serialVersionUID = 1L;
	private String formNo;
	private String inspectionFormNo;
	private String inspectionId;
	private String happenSpace;//发生地点
	private String productStage;//产品阶段
	private String billingArea;//开单区域
	private String supplierName;//供应商
	private String supplierCode;//
	private String exceptionStage;//异常阶段
	private String exceptionType;//异常类型
	private String exceptionDegree;//异常程度
	private String bomName;//品名
	private String bomCode;//料号
	private String materialType;//物料类别
	private Date inspectionDate;//检验日期
	private Double incomingAmount;//进料数
	
	@Column(length=40)
	private String units;//单位
	private Double checkAmount;//抽检数
	private String surfaceBad;//外观不良
	private Double surfaceBadRate;//外观不良率
	private String surfaceBadState;//外观状态
	private String functionBad;//功能不良
	private Double functionBadRate;//功能不良率
	private String functionBadState;//功能状态
	private String sizeBad;//尺寸不良
	private Double sizeBadRate;//尺寸不良率
	private String sizeBadState;//	
	private String featuresBad;//特性不良
	private Double featuresBadRate;//特性不良率
	private String featuresBadState;//特性状态
	private String badDesc;//异常描述
	private String descFile;//附件
	private String inspector;//检验员，发起人
	private String inspectorLog;
	private Date sponsorDate;//发起日期
	//主管审核问题描述
	@Column(length=1000)
	private String reportCheckOpinion;//意见	
	private String reportChecker;//审核人
	private String reportCheckerLog;
	private Date reportCheckDate;//
	
	//MQE确认异常
	@Column(length=1000)
	private String mqeCheckOpinion;//意见	
	private String mqeChecker;//审核人
	private String mqeCheckerLog;
	private Date mqeCheckDate;//
	private String isImprove;//是否需要发起sqe改进
	
	//PMC意见
	@Column(length=1000)
	private String pmcOpinion;//pmc意见
	private Date demandDeliveryPeriod;//需求交期
	private String mrbApply;//mrb申请
	private String mrbReportNo;//mrb单号
	private String pmcChecker;//pmc审核人
	private String pmcCheckerLog;
	
	//MQE提供处理意见
	@Column(length=1000)
	private String qualityOpinion;//品质中心意见
	private String sqeProcessOpinion;//处理意见
	private String returnReportNo;//退货通知单单号
	private String sqeMrbReportNo;//mrb单号

	//MQE主管审核处理意见
	@Column(length=1000)
	private String mqeSupervisorOpinion;//MQE主管意见
	private String mqeSupervisor;//审核人
	private String mqeSupervisorLog;
	private Date mqeSupervisorDate;//
	
	//供应商回复内容
	@Column(length=2000)
	private String tempCountermeasures;//暂定对策实施
	@Column(length=2000)
	private String trueReasonCheck;//真因定义及验证
	@Column(length=2000)
	private String countermeasures;//永久对策实施
	@Column(length=2000)
	private String preventHappen;//预防再发生
	@Column(length=2000)
	private String supplierFile;//
	private Date requestDate;//8d回复日期
	
	//MQE确认改善报告
	
	private String mqeComfirm;
	private String mqeComfirmLog;
	@Column(length=1000)
	private String mqeComfirmOpinion;//MQE审核意见
	private Date mqeComfirmDate;//
	
	//MQE主管审核
	@Column(length=1000)
	private String mqeApprovalerOpinion;//MQE主管意见
	private Date mqeApprovalerDate;//
	private String mqeApprovaler;
	private String mqeApprovalerLog;
	
	//MQE追踪改善效果	
	private String checkResult;//效果确认		
	private Date sqeFinishDate;//sqe追踪完成时间
	private Date checkResultDate;//确认日期
	
	private String supplierEmail;//供应商邮箱地址
	private String isClosed="否";//是否结案
	private String isClosedAlaysis="否";//是否结案(用于统计)
	private String isSupplier="否";//是否供应商回复
	private String currentMan;//当前办理人
	private String currentManLog;//当前办理人登录名
	private String exceptionImproveId;//对应的异常矫正单id
	private String exceptionImprovetNo;//对应的异常矫正单编号
	
	//用于集成判断
	private  Long sourceId;//集成过来原ID
	private String sourceUnit;//集成的事业部
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getHappenSpace() {
		return happenSpace;
	}
	public void setHappenSpace(String happenSpace) {
		this.happenSpace = happenSpace;
	}
	public String getProductStage() {
		return productStage;
	}
	public void setProductStage(String productStage) {
		this.productStage = productStage;
	}
	public String getBillingArea() {
		return billingArea;
	}
	public void setBillingArea(String billingArea) {
		this.billingArea = billingArea;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getBomName() {
		return bomName;
	}
	public void setBomName(String bomName) {
		this.bomName = bomName;
	}
	public String getBomCode() {
		return bomCode;
	}
	public void setBomCode(String bomCode) {
		this.bomCode = bomCode;
	}
	public Date getInspectionDate() {
		return inspectionDate;
	}
	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public Double getIncomingAmount() {
		return incomingAmount;
	}
	public void setIncomingAmount(Double incomingAmount) {
		this.incomingAmount = incomingAmount;
	}
	public Double getCheckAmount() {
		return checkAmount;
	}
	public void setCheckAmount(Double checkAmount) {
		this.checkAmount = checkAmount;
	}
	public String getSurfaceBad() {
		return surfaceBad;
	}
	public void setSurfaceBad(String surfaceBad) {
		this.surfaceBad = surfaceBad;
	}
	public Double getSurfaceBadRate() {
		return surfaceBadRate;
	}
	public void setSurfaceBadRate(Double surfaceBadRate) {
		this.surfaceBadRate = surfaceBadRate;
	}
	public String getFunctionBad() {
		return functionBad;
	}
	public void setFunctionBad(String functionBad) {
		this.functionBad = functionBad;
	}
	public Double getFunctionBadRate() {
		return functionBadRate;
	}
	public void setFunctionBadRate(Double functionBadRate) {
		this.functionBadRate = functionBadRate;
	}
	public String getSizeBad() {
		return sizeBad;
	}
	public void setSizeBad(String sizeBad) {
		this.sizeBad = sizeBad;
	}
	public Double getSizeBadRate() {
		return sizeBadRate;
	}
	public void setSizeBadRate(Double sizeBadRate) {
		this.sizeBadRate = sizeBadRate;
	}
	public String getFeaturesBad() {
		return featuresBad;
	}
	public void setFeaturesBad(String featuresBad) {
		this.featuresBad = featuresBad;
	}
	public Double getFeaturesBadRate() {
		return featuresBadRate;
	}
	public void setFeaturesBadRate(Double featuresBadRate) {
		this.featuresBadRate = featuresBadRate;
	}
	public String getBadDesc() {
		return badDesc;
	}
	public void setBadDesc(String badDesc) {
		this.badDesc = badDesc;
	}
	public String getDescFile() {
		return descFile;
	}
	public void setDescFile(String descFile) {
		this.descFile = descFile;
	}
	public String getInspector() {
		return inspector;
	}
	public void setInspector(String inspector) {
		this.inspector = inspector;
	}
	public String getInspectorLog() {
		return inspectorLog;
	}
	public void setInspectorLog(String inspectorLog) {
		this.inspectorLog = inspectorLog;
	}
	public String getReportChecker() {
		return reportChecker;
	}
	public void setReportChecker(String reportChecker) {
		this.reportChecker = reportChecker;
	}
	public String getReportCheckerLog() {
		return reportCheckerLog;
	}
	public void setReportCheckerLog(String reportCheckerLog) {
		this.reportCheckerLog = reportCheckerLog;
	}
	public String getPmcOpinion() {
		return pmcOpinion;
	}
	public void setPmcOpinion(String pmcOpinion) {
		this.pmcOpinion = pmcOpinion;
	}

	public Date getDemandDeliveryPeriod() {
		return demandDeliveryPeriod;
	}
	public void setDemandDeliveryPeriod(Date demandDeliveryPeriod) {
		this.demandDeliveryPeriod = demandDeliveryPeriod;
	}
	public String getMrbApply() {
		return mrbApply;
	}
	public void setMrbApply(String mrbApply) {
		this.mrbApply = mrbApply;
	}
	public String getMrbReportNo() {
		return mrbReportNo;
	}
	public void setMrbReportNo(String mrbReportNo) {
		this.mrbReportNo = mrbReportNo;
	}
	public String getPmcChecker() {
		return pmcChecker;
	}
	public void setPmcChecker(String pmcChecker) {
		this.pmcChecker = pmcChecker;
	}
	public String getPmcCheckerLog() {
		return pmcCheckerLog;
	}
	public void setPmcCheckerLog(String pmcCheckerLog) {
		this.pmcCheckerLog = pmcCheckerLog;
	}
	public String getQualityOpinion() {
		return qualityOpinion;
	}
	public void setQualityOpinion(String qualityOpinion) {
		this.qualityOpinion = qualityOpinion;
	}
	public String getSqeProcessOpinion() {
		return sqeProcessOpinion;
	}
	public void setSqeProcessOpinion(String sqeProcessOpinion) {
		this.sqeProcessOpinion = sqeProcessOpinion;
	}
	public String getReturnReportNo() {
		return returnReportNo;
	}
	public void setReturnReportNo(String returnReportNo) {
		this.returnReportNo = returnReportNo;
	}
	public String getSqeMrbReportNo() {
		return sqeMrbReportNo;
	}
	public void setSqeMrbReportNo(String sqeMrbReportNo) {
		this.sqeMrbReportNo = sqeMrbReportNo;
	}
	public String getTempCountermeasures() {
		return tempCountermeasures;
	}
	public void setTempCountermeasures(String tempCountermeasures) {
		this.tempCountermeasures = tempCountermeasures;
	}
	public String getTrueReasonCheck() {
		return trueReasonCheck;
	}
	public void setTrueReasonCheck(String trueReasonCheck) {
		this.trueReasonCheck = trueReasonCheck;
	}
	public String getCountermeasures() {
		return countermeasures;
	}
	public void setCountermeasures(String countermeasures) {
		this.countermeasures = countermeasures;
	}
	public String getPreventHappen() {
		return preventHappen;
	}
	public void setPreventHappen(String preventHappen) {
		this.preventHappen = preventHappen;
	}
	public String getSupplierFile() {
		return supplierFile;
	}
	public void setSupplierFile(String supplierFile) {
		this.supplierFile = supplierFile;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public String getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	public Date getSqeFinishDate() {
		return sqeFinishDate;
	}
	public void setSqeFinishDate(Date sqeFinishDate) {
		this.sqeFinishDate = sqeFinishDate;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierEmail() {
		return supplierEmail;
	}
	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}
	public String getSurfaceBadState() {
		return surfaceBadState;
	}
	public void setSurfaceBadState(String surfaceBadState) {
		this.surfaceBadState = surfaceBadState;
	}
	public String getFunctionBadState() {
		return functionBadState;
	}
	public void setFunctionBadState(String functionBadState) {
		this.functionBadState = functionBadState;
	}
	public String getSizeBadState() {
		return sizeBadState;
	}
	public void setSizeBadState(String sizeBadState) {
		this.sizeBadState = sizeBadState;
	}
	public String getFeaturesBadState() {
		return featuresBadState;
	}
	public void setFeaturesBadState(String featuresBadState) {
		this.featuresBadState = featuresBadState;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getInspectionFormNo() {
		return inspectionFormNo;
	}
	public void setInspectionFormNo(String inspectionFormNo) {
		this.inspectionFormNo = inspectionFormNo;
	}
	public String getInspectionId() {
		return inspectionId;
	}
	public void setInspectionId(String inspectionId) {
		this.inspectionId = inspectionId;
	}
	public String getIsClosed() {
		return isClosed;
	}
	public void setIsClosed(String isClosed) {
		this.isClosed = isClosed;
	}
	public String getIsClosedAlaysis() {
		return isClosedAlaysis;
	}
	public void setIsClosedAlaysis(String isClosedAlaysis) {
		this.isClosedAlaysis = isClosedAlaysis;
	}
	public String getIsSupplier() {
		return isSupplier;
	}
	public void setIsSupplier(String isSupplier) {
		this.isSupplier = isSupplier;
	}
	public String getCurrentMan() {
		return currentMan;
	}
	public void setCurrentMan(String currentMan) {
		this.currentMan = currentMan;
	}
	public String getCurrentManLog() {
		return currentManLog;
	}
	public void setCurrentManLog(String currentManLog) {
		this.currentManLog = currentManLog;
	}
	public String getExceptionType() {
		return exceptionType;
	}
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}
	public Date getSponsorDate() {
		return sponsorDate;
	}
	public void setSponsorDate(Date sponsorDate) {
		this.sponsorDate = sponsorDate;
	}
	public String getReportCheckOpinion() {
		return reportCheckOpinion;
	}
	public void setReportCheckOpinion(String reportCheckOpinion) {
		this.reportCheckOpinion = reportCheckOpinion;
	}
	public Date getReportCheckDate() {
		return reportCheckDate;
	}
	public void setReportCheckDate(Date reportCheckDate) {
		this.reportCheckDate = reportCheckDate;
	}
	public String getMqeCheckOpinion() {
		return mqeCheckOpinion;
	}
	public void setMqeCheckOpinion(String mqeCheckOpinion) {
		this.mqeCheckOpinion = mqeCheckOpinion;
	}
	public String getMqeChecker() {
		return mqeChecker;
	}
	public void setMqeChecker(String mqeChecker) {
		this.mqeChecker = mqeChecker;
	}
	public String getMqeCheckerLog() {
		return mqeCheckerLog;
	}
	public void setMqeCheckerLog(String mqeCheckerLog) {
		this.mqeCheckerLog = mqeCheckerLog;
	}
	public Date getMqeCheckDate() {
		return mqeCheckDate;
	}
	public void setMqeCheckDate(Date mqeCheckDate) {
		this.mqeCheckDate = mqeCheckDate;
	}
	public String getMqeSupervisorOpinion() {
		return mqeSupervisorOpinion;
	}
	public void setMqeSupervisorOpinion(String mqeSupervisorOpinion) {
		this.mqeSupervisorOpinion = mqeSupervisorOpinion;
	}
	public String getMqeSupervisor() {
		return mqeSupervisor;
	}
	public void setMqeSupervisor(String mqeSupervisor) {
		this.mqeSupervisor = mqeSupervisor;
	}
	public String getMqeSupervisorLog() {
		return mqeSupervisorLog;
	}
	public void setMqeSupervisorLog(String mqeSupervisorLog) {
		this.mqeSupervisorLog = mqeSupervisorLog;
	}
	public Date getMqeSupervisorDate() {
		return mqeSupervisorDate;
	}
	public void setMqeSupervisorDate(Date mqeSupervisorDate) {
		this.mqeSupervisorDate = mqeSupervisorDate;
	}
	public Date getCheckResultDate() {
		return checkResultDate;
	}
	public void setCheckResultDate(Date checkResultDate) {
		this.checkResultDate = checkResultDate;
	}
	public String getExceptionImproveId() {
		return exceptionImproveId;
	}
	public void setExceptionImproveId(String exceptionImproveId) {
		this.exceptionImproveId = exceptionImproveId;
	}
	public String getExceptionImprovetNo() {
		return exceptionImprovetNo;
	}
	public void setExceptionImprovetNo(String exceptionImprovetNo) {
		this.exceptionImprovetNo = exceptionImprovetNo;
	}
	public String getExceptionStage() {
		return exceptionStage;
	}
	public void setExceptionStage(String exceptionStage) {
		this.exceptionStage = exceptionStage;
	}
	public String getExceptionDegree() {
		return exceptionDegree;
	}
	public void setExceptionDegree(String exceptionDegree) {
		this.exceptionDegree = exceptionDegree;
	}
	public String getIsImprove() {
		return isImprove;
	}
	public void setIsImprove(String isImprove) {
		this.isImprove = isImprove;
	}
	public String getMqeComfirmOpinion() {
		return mqeComfirmOpinion;
	}
	public void setMqeComfirmOpinion(String mqeComfirmOpinion) {
		this.mqeComfirmOpinion = mqeComfirmOpinion;
	}
	public Date getMqeComfirmDate() {
		return mqeComfirmDate;
	}
	public void setMqeComfirmDate(Date mqeComfirmDate) {
		this.mqeComfirmDate = mqeComfirmDate;
	}
	public String getMqeApprovalerOpinion() {
		return mqeApprovalerOpinion;
	}
	public void setMqeApprovalerOpinion(String mqeApprovalerOpinion) {
		this.mqeApprovalerOpinion = mqeApprovalerOpinion;
	}
	public Date getMqeApprovalerDate() {
		return mqeApprovalerDate;
	}
	public void setMqeApprovalerDate(Date mqeApprovalerDate) {
		this.mqeApprovalerDate = mqeApprovalerDate;
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
	public String getMqeComfirm() {
		return mqeComfirm;
	}
	public void setMqeComfirm(String mqeComfirm) {
		this.mqeComfirm = mqeComfirm;
	}
	public String getMqeComfirmLog() {
		return mqeComfirmLog;
	}
	public void setMqeComfirmLog(String mqeComfirmLog) {
		this.mqeComfirmLog = mqeComfirmLog;
	}
	public String getMqeApprovaler() {
		return mqeApprovaler;
	}
	public void setMqeApprovaler(String mqeApprovaler) {
		this.mqeApprovaler = mqeApprovaler;
	}
	public String getMqeApprovalerLog() {
		return mqeApprovalerLog;
	}
	public void setMqeApprovalerLog(String mqeApprovalerLog) {
		this.mqeApprovalerLog = mqeApprovalerLog;
	}
	
}
