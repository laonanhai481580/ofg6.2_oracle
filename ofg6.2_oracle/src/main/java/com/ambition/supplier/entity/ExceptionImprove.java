package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.WorkflowIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**    
 * 物料异常矫正单
 * @authorBy YUKE
 *
 */
@Entity
@Table(name = "SUPPLIER_EXCEPTION_IMPROVE")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class ExceptionImprove extends  WorkflowIdEntity{
	private static final long serialVersionUID = 1L;
	private String formNo;
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
	private String sqeProcessOpinion;//处理意见
	private String returnReportNo;//退货通知单单号
	private String sqeMrbReportNo;//mrb单号
	private String mqeChecker;//MQE
	private String mqeCheckerLog;
	//SQE确认异常
	@Column(length=1000)
	private String sqeCheckOpinion;//意见	
	private String sqeChecker;//审核人
	private String sqeCheckerLog;
	private Date sqeCheckDate;//	
	
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
	
	//SQE确认改善报告
	private String sqeComfirmOpinion;//效果确认
	private Date sqeComfirmDate;//效果确认日期
	
	
	//SQE主管改善报告
	private String sqeApprovalerOpinion;//审核意见
	private Date sqeApprovalerDate;//审核日期
	private String sqeApprovaler;//sqe主管
	private String sqeApprovalerLog;
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
	private String exceptionContactId;//对应的异常联络单id
	private String exceptionContactNo;//对应的异常联络单编号
	private String inspectionId;
	private String inspectionFormNo;
	
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
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getExceptionStage() {
		return exceptionStage;
	}
	public void setExceptionStage(String exceptionStage) {
		this.exceptionStage = exceptionStage;
	}
	public String getExceptionType() {
		return exceptionType;
	}
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}
	public String getExceptionDegree() {
		return exceptionDegree;
	}
	public void setExceptionDegree(String exceptionDegree) {
		this.exceptionDegree = exceptionDegree;
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
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
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
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
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
	public String getSurfaceBadState() {
		return surfaceBadState;
	}
	public void setSurfaceBadState(String surfaceBadState) {
		this.surfaceBadState = surfaceBadState;
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
	public String getFunctionBadState() {
		return functionBadState;
	}
	public void setFunctionBadState(String functionBadState) {
		this.functionBadState = functionBadState;
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
	public String getSizeBadState() {
		return sizeBadState;
	}
	public void setSizeBadState(String sizeBadState) {
		this.sizeBadState = sizeBadState;
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
	public String getFeaturesBadState() {
		return featuresBadState;
	}
	public void setFeaturesBadState(String featuresBadState) {
		this.featuresBadState = featuresBadState;
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
	public Date getSponsorDate() {
		return sponsorDate;
	}
	public void setSponsorDate(Date sponsorDate) {
		this.sponsorDate = sponsorDate;
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
	public String getSqeCheckOpinion() {
		return sqeCheckOpinion;
	}
	public void setSqeCheckOpinion(String sqeCheckOpinion) {
		this.sqeCheckOpinion = sqeCheckOpinion;
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
	public Date getSqeCheckDate() {
		return sqeCheckDate;
	}
	public void setSqeCheckDate(Date sqeCheckDate) {
		this.sqeCheckDate = sqeCheckDate;
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
	public String getSqeComfirmOpinion() {
		return sqeComfirmOpinion;
	}
	public void setSqeComfirmOpinion(String sqeComfirmOpinion) {
		this.sqeComfirmOpinion = sqeComfirmOpinion;
	}
	public Date getSqeComfirmDate() {
		return sqeComfirmDate;
	}
	public void setSqeComfirmDate(Date sqeComfirmDate) {
		this.sqeComfirmDate = sqeComfirmDate;
	}
	public String getSqeApprovalerOpinion() {
		return sqeApprovalerOpinion;
	}
	public void setSqeApprovalerOpinion(String sqeApprovalerOpinion) {
		this.sqeApprovalerOpinion = sqeApprovalerOpinion;
	}
	public Date getSqeApprovalerDate() {
		return sqeApprovalerDate;
	}
	public void setSqeApprovalerDate(Date sqeApprovalerDate) {
		this.sqeApprovalerDate = sqeApprovalerDate;
	}
	public String getSqeApprovaler() {
		return sqeApprovaler;
	}
	public void setSqeApprovaler(String sqeApprovaler) {
		this.sqeApprovaler = sqeApprovaler;
	}
	public String getSqeApprovalerLog() {
		return sqeApprovalerLog;
	}
	public void setSqeApprovalerLog(String sqeApprovalerLog) {
		this.sqeApprovalerLog = sqeApprovalerLog;
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
	public Date getCheckResultDate() {
		return checkResultDate;
	}
	public void setCheckResultDate(Date checkResultDate) {
		this.checkResultDate = checkResultDate;
	}
	public String getSupplierEmail() {
		return supplierEmail;
	}
	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
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
	public String getExceptionContactId() {
		return exceptionContactId;
	}
	public void setExceptionContactId(String exceptionContactId) {
		this.exceptionContactId = exceptionContactId;
	}
	public String getExceptionContactNo() {
		return exceptionContactNo;
	}
	public void setExceptionContactNo(String exceptionContactNo) {
		this.exceptionContactNo = exceptionContactNo;
	}
	public String getInspectionId() {
		return inspectionId;
	}
	public void setInspectionId(String inspectionId) {
		this.inspectionId = inspectionId;
	}
	public String getInspectionFormNo() {
		return inspectionFormNo;
	}
	public void setInspectionFormNo(String inspectionFormNo) {
		this.inspectionFormNo = inspectionFormNo;
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
