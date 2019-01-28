package com.ambition.supplier.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 类名:上线异常数据汇总数据
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  lpf
 * @version 1.00 2018年9月11日 发布
 */
@Entity
@Table(name = "SUPPLIER_ABNORMAL_ALL")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class SupplierAbnormalAll  extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String productType;//物料类别
	private Date enterDate;//录入时间
	private String period;//周期
	private String classGroup;//班别
	private String procedure;//工序
	private String productStage;//产品阶段
	private String supplierCode;//供应商编码
	private String supplierName;//供应商名称
	private String modelType;//型号
	private String factory;//工厂
	private String models;//机种
	private String customerName;//客户名称
	private String materialState;//物料状态
	private String filtrate;//筛选方式
	private String remorseAbsorption;//自责吸收
	private Integer inputAmount;//投入数
	private Integer unqualifiedAmount;//不良数
	private String unqualifiedRate;//不良率	
	
	private String productionChecker;//生产确认人
	private String productionCheckerLogin;//生产确认人
	private String supplierChecker;//供应商确认人
	private String supplierCheckerLogin;//供应商确认人
	private String mqeChecker;//MQE确认人
	private String mqeCheckerLogin;//MQE确认人
	private String processResult;//处理结果
	private String reportFile;//附件
	
	//用于集成判断
	private  Long sourceId;//集成过来原ID
	private String sourceUnit;//集成的事业部
	
	@OneToMany(mappedBy = "supplierAbnormalAll",cascade={javax.persistence.CascadeType.ALL})
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	List<SupplierAbnormalDetail> SupplierAbnormalItems;//不良明细
	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Date getEnterDate() {
		return enterDate;
	}

	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getClassGroup() {
		return classGroup;
	}

	public void setClassGroup(String classGroup) {
		this.classGroup = classGroup;
	}

	public String getProcedure() {
		return procedure;
	}

	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	public String getProductStage() {
		return productStage;
	}

	public void setProductStage(String productStage) {
		this.productStage = productStage;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getModels() {
		return models;
	}

	public void setModels(String models) {
		this.models = models;
	}

	public String getFiltrate() {
		return filtrate;
	}

	public void setFiltrate(String filtrate) {
		this.filtrate = filtrate;
	}

	public Integer getInputAmount() {
		return inputAmount;
	}

	public void setInputAmount(Integer inputAmount) {
		this.inputAmount = inputAmount;
	}

	public Integer getUnqualifiedAmount() {
		return unqualifiedAmount;
	}

	public void setUnqualifiedAmount(Integer unqualifiedAmount) {
		this.unqualifiedAmount = unqualifiedAmount;
	}

	public String getUnqualifiedRate() {
		return unqualifiedRate;
	}

	public void setUnqualifiedRate(String unqualifiedRate) {
		this.unqualifiedRate = unqualifiedRate;
	}

	public String getProductionChecker() {
		return productionChecker;
	}

	public void setProductionChecker(String productionChecker) {
		this.productionChecker = productionChecker;
	}

	public String getProductionCheckerLogin() {
		return productionCheckerLogin;
	}

	public void setProductionCheckerLogin(String productionCheckerLogin) {
		this.productionCheckerLogin = productionCheckerLogin;
	}

	public String getSupplierChecker() {
		return supplierChecker;
	}

	public void setSupplierChecker(String supplierChecker) {
		this.supplierChecker = supplierChecker;
	}

	public String getSupplierCheckerLogin() {
		return supplierCheckerLogin;
	}

	public void setSupplierCheckerLogin(String supplierCheckerLogin) {
		this.supplierCheckerLogin = supplierCheckerLogin;
	}

	public String getMqeChecker() {
		return mqeChecker;
	}

	public void setMqeChecker(String mqeChecker) {
		this.mqeChecker = mqeChecker;
	}

	public String getMqeCheckerLogin() {
		return mqeCheckerLogin;
	}

	public void setMqeCheckerLogin(String mqeCheckerLogin) {
		this.mqeCheckerLogin = mqeCheckerLogin;
	}

	public String getProcessResult() {
		return processResult;
	}

	public void setProcessResult(String processResult) {
		this.processResult = processResult;
	}

	public String getReportFile() {
		return reportFile;
	}

	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}

	public String getRemorseAbsorption() {
		return remorseAbsorption;
	}

	public void setRemorseAbsorption(String remorseAbsorption) {
		this.remorseAbsorption = remorseAbsorption;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getMaterialState() {
		return materialState;
	}

	public void setMaterialState(String materialState) {
		this.materialState = materialState;
	}

	public List<SupplierAbnormalDetail> getSupplierAbnormalItems() {
		return SupplierAbnormalItems;
	}

	public void setSupplierAbnormalItems(
			List<SupplierAbnormalDetail> supplierAbnormalItems) {
		SupplierAbnormalItems = supplierAbnormalItems;
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
