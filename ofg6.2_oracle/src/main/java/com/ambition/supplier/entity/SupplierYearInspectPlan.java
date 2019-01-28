package com.ambition.supplier.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:供应商稽核计划2.0
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  xjn
 * @version 1.00 2018年2月26日 发布
 */
@Entity
@Table(name="SUPPLIER_YEAR_INSPECT_PLAN")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class SupplierYearInspectPlan extends IdEntity{

		/**
		  *SupplierYearInspectPlan.java2018年2月26日
		 */
	private static final long serialVersionUID = 1L;
	private String supplierName;//供应商名称
	private String supplierCode;//供应商编码
	private String address;//企业地址 供应商地址
	private String supplierEmail;//供应商邮箱
	private String supplyFactory;//供应厂区
	private String supplyMaterial;//供应物料
	private String materialName;//物料名称
	private String materialCode;//物料编码
	private String materialType;//材料类别
	private Integer creationYear;//创建年份
	private Integer creationMonth;//创建月份
	private String sqeChecker;//SQE核准
	private String sqeCheckerLogin;//SQE核准
	private String purchaseProcesser;//采购部
	private String purchaseProcesserLogin;//采购部
	private Date firstCheckDesignDate;//首次实际日期
	private Date secondCheckDesignDate;//二次实际日期
	private String firstPerson;//首次稽核人
	private String secondPerson;//二次稽核人
	private String finalCheckResult;//最终稽核结果
	private Date ncrDate;//不符合项回复时间
	private String auditorPerson;//审核人
	private String affirmPerson;//确认人
	private String auditorType;//审核类型
	private String auditSort;//审核类别
	private String auditorState;//稽核状态
	private String planState;//计划状态;
	private Date planDate;//计划时间
	private String inspectId;//稽核报告ID
	private String inspectNo;//稽核报告编号
	private String plan1;
	private String do1;
	private String plan2;
	private String do2;
	private String plan3;
	private String do3;
	private String plan4;
	private String do4;
	private String plan5;
	private String do5;
	private String plan6;
	private String do6;
	private String plan7;
	private String do7;
	private String plan8;
	private String do8;
	private String plan9;
	private String do9;
	private String plan10;
	private String do10;
	private String plan11;
	private String do11;
	private String plan12;
	private String do12;
	
	//用于集成判断
	private  Long sourceId;//集成过来原ID
	private String sourceUnit;//集成的事业部
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
	public String getAuditorType() {
		return auditorType;
	}
	public void setAuditorType(String auditorType) {
		this.auditorType = auditorType;
	}
	public String getPlan1() {
		return plan1;
	}
	public void setPlan1(String plan1) {
		this.plan1 = plan1;
	}
	public String getDo1() {
		return do1;
	}
	public void setDo1(String do1) {
		this.do1 = do1;
	}
	public String getPlan2() {
		return plan2;
	}
	public void setPlan2(String plan2) {
		this.plan2 = plan2;
	}
	public String getDo2() {
		return do2;
	}
	public void setDo2(String do2) {
		this.do2 = do2;
	}
	public String getPlan3() {
		return plan3;
	}
	public void setPlan3(String plan3) {
		this.plan3 = plan3;
	}
	public String getDo3() {
		return do3;
	}
	public void setDo3(String do3) {
		this.do3 = do3;
	}
	public String getPlan4() {
		return plan4;
	}
	public void setPlan4(String plan4) {
		this.plan4 = plan4;
	}
	public String getDo4() {
		return do4;
	}
	public void setDo4(String do4) {
		this.do4 = do4;
	}
	public String getPlan5() {
		return plan5;
	}
	public void setPlan5(String plan5) {
		this.plan5 = plan5;
	}
	public String getDo5() {
		return do5;
	}
	public void setDo5(String do5) {
		this.do5 = do5;
	}
	public String getPlan6() {
		return plan6;
	}
	public void setPlan6(String plan6) {
		this.plan6 = plan6;
	}
	public String getDo6() {
		return do6;
	}
	public void setDo6(String do6) {
		this.do6 = do6;
	}
	public String getPlan7() {
		return plan7;
	}
	public void setPlan7(String plan7) {
		this.plan7 = plan7;
	}
	public String getDo7() {
		return do7;
	}
	public void setDo7(String do7) {
		this.do7 = do7;
	}
	public String getPlan8() {
		return plan8;
	}
	public void setPlan8(String plan8) {
		this.plan8 = plan8;
	}
	public String getDo8() {
		return do8;
	}
	public void setDo8(String do8) {
		this.do8 = do8;
	}
	public String getPlan9() {
		return plan9;
	}
	public void setPlan9(String plan9) {
		this.plan9 = plan9;
	}
	public String getDo9() {
		return do9;
	}
	public void setDo9(String do9) {
		this.do9 = do9;
	}
	public String getPlan10() {
		return plan10;
	}
	public void setPlan10(String plan10) {
		this.plan10 = plan10;
	}
	public String getDo10() {
		return do10;
	}
	public void setDo10(String do10) {
		this.do10 = do10;
	}
	public String getPlan11() {
		return plan11;
	}
	public void setPlan11(String plan11) {
		this.plan11 = plan11;
	}
	public String getDo11() {
		return do11;
	}
	public void setDo11(String do11) {
		this.do11 = do11;
	}
	public String getPlan12() {
		return plan12;
	}
	public void setPlan12(String plan12) {
		this.plan12 = plan12;
	}
	public String getDo12() {
		return do12;
	}
	public void setDo12(String do12) {
		this.do12 = do12;
	}
	public Integer getCreationYear() {
		return creationYear;
	}
	public void setCreationYear(Integer creationYear) {
		this.creationYear = creationYear;
	}
	public String getPlanState() {
		return planState;
	}
	public void setPlanState(String planState) {
		this.planState = planState;
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
	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	public String getAuditorState() {
		return auditorState;
	}
	public void setAuditorState(String auditorState) {
		this.auditorState = auditorState;
	}
	public String getFirstPerson() {
		return firstPerson;
	}
	public void setFirstPerson(String firstPerson) {
		this.firstPerson = firstPerson;
	}
	public String getSecondPerson() {
		return secondPerson;
	}
	public void setSecondPerson(String secondPerson) {
		this.secondPerson = secondPerson;
	}
	public Integer getCreationMonth() {
		return creationMonth;
	}
	public void setCreationMonth(Integer creationMonth) {
		this.creationMonth = creationMonth;
	}
	public String getAuditSort() {
		return auditSort;
	}
	public void setAuditSort(String auditSort) {
		this.auditSort = auditSort;
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
	public String getInspectId() {
		return inspectId;
	}
	public void setInspectId(String inspectId) {
		this.inspectId = inspectId;
	}
	public String getInspectNo() {
		return inspectNo;
	}
	public void setInspectNo(String inspectNo) {
		this.inspectNo = inspectNo;
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
