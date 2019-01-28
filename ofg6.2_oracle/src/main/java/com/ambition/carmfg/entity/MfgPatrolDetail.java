package com.ambition.carmfg.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 制造检查项目记录明细
 * @author lpf
 *
 */
@Entity
@Table(name="MFG_PATROL_DATAIL")
public class MfgPatrolDetail extends IdEntity{
	private static final long serialVersionUID = 1L;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inspectionDate;//检验日期
	private String checkBomCode;//物料编码
	private String checkBomName;//物料名称
	private String workProcedure;//工序
	private String inspectionLevel;//检验级别
	private String codeLetter;//字码
	private String aql;//aql标准
	private Integer aqlAc;//接受数
	private Integer aqlRe;//拒收数
	private String inspectionType;//检验类型,对应检验项目顶级
	private String parentItemName;//项目父类名称
	private Integer parentRowSpan=new Integer(1);//项目父类合并数量
	@Column(length=1000)
	private String checkItemName;//检查项目名称
	private String countType;//统计类型
	private String checkMethod;//检查方法
	private Integer inspectionAmount;//检验数量
	@Column(length=1000)
	private String specifications;//规格
	private String unit;//单位
	private Double maxlimit;//上限
	private Double minlimit;//下限
	private String equipmentNo;//设备编号 机台编号
	private String testEquipmentNo;//测量仪器编号
	private String featureId;//质量特性
	private String spcSampleIds;
	private String remark;//备注
	private String inspector;//检验员
	private String isJnUnit="否";//是否IPQC测试项
	private String isInEquipment="否";//是否集成设备
	private String itemStatus="未领取";//检验项目状态
	
	private Integer qualifiedAmount;//合格数
	private Integer unqualifiedAmount;//不良数
	private Float qualifiedRate=(float) 100.0; // 合格率
	private String attachmentFiles;//附件
	private String conclusion;//结论
	
	@ManyToOne
	@JoinColumn(name="FK_MFG_PATROL_REPORT_ID")
	private MfgPatrolReport mfgPatrolReport;//检验报告

	@OneToMany(mappedBy="mfgPatrolDetail",cascade=CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	private List<MfgPatrolDetailData> mfgPatrolDetailDatas;//检查项目	
	
	public Float getQualifiedRate() {
		return qualifiedRate;
	}

	public void setQualifiedRate(Float qualifiedRate) {
		this.qualifiedRate = qualifiedRate;
	}

	public String getParentItemName() {
		return parentItemName;
	}

	public void setParentItemName(String parentItemName) {
		this.parentItemName = parentItemName;
	}

	public Integer getParentRowSpan() {
		return parentRowSpan;
	}

	public void setParentRowSpan(Integer parentRowSpan) {
		this.parentRowSpan = parentRowSpan;
	}

	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public String getCheckBomCode() {
		return checkBomCode;
	}

	public void setCheckBomCode(String checkBomCode) {
		this.checkBomCode = checkBomCode;
	}

	public String getCheckBomName() {
		return checkBomName;
	}

	public void setCheckBomName(String checkBomName) {
		this.checkBomName = checkBomName;
	}

	public String getInspectionLevel() {
		return inspectionLevel;
	}

	public void setInspectionLevel(String inspectionLevel) {
		this.inspectionLevel = inspectionLevel;
	}

	public String getAql() {
		return aql;
	}

	public void setAql(String aql) {
		this.aql = aql;
	}

	public Integer getAqlAc() {
		return aqlAc;
	}

	public void setAqlAc(Integer aqlAc) {
		this.aqlAc = aqlAc;
	}

	public Integer getAqlRe() {
		return aqlRe;
	}

	public void setAqlRe(Integer aqlRe) {
		this.aqlRe = aqlRe;
	}

	public String getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}

	public String getCheckItemName() {
		return checkItemName;
	}

	public void setCheckItemName(String checkItemName) {
		this.checkItemName = checkItemName;
	}

	public String getCountType() {
		return countType;
	}

	public void setCountType(String countType) {
		this.countType = countType;
	}

	public String getCheckMethod() {
		return checkMethod;
	}

	public void setCheckMethod(String checkMethod) {
		this.checkMethod = checkMethod;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getMaxlimit() {
		return maxlimit;
	}

	public void setMaxlimit(Double maxlimit) {
		this.maxlimit = maxlimit;
	}

	public Double getMinlimit() {
		return minlimit;
	}

	public void setMinlimit(Double minlimit) {
		this.minlimit = minlimit;
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}


	public Integer getQualifiedAmount() {
		return qualifiedAmount;
	}

	public void setQualifiedAmount(Integer qualifiedAmount) {
		this.qualifiedAmount = qualifiedAmount;
	}

	public Integer getUnqualifiedAmount() {
		return unqualifiedAmount;
	}

	public void setUnqualifiedAmount(Integer unqualifiedAmount) {
		this.unqualifiedAmount = unqualifiedAmount;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public String getCodeLetter() {
		return codeLetter;
	}

	public void setCodeLetter(String codeLetter) {
		this.codeLetter = codeLetter;
	}

	public Integer getInspectionAmount() {
		return inspectionAmount;
	}

	public void setInspectionAmount(Integer inspectionAmount) {
		this.inspectionAmount = inspectionAmount;
	}

	public String getWorkProcedure() {
		return workProcedure;
	}

	public void setWorkProcedure(String workProcedure) {
		this.workProcedure = workProcedure;
	}

	public String getEquipmentNo() {
		return equipmentNo;
	}

	public void setEquipmentNo(String equipmentNo) {
		this.equipmentNo = equipmentNo;
	}

	public String getTestEquipmentNo() {
		return testEquipmentNo;
	}

	public void setTestEquipmentNo(String testEquipmentNo) {
		this.testEquipmentNo = testEquipmentNo;
	}

	public String getSpcSampleIds() {
		return spcSampleIds;
	}

	public void setSpcSampleIds(String spcSampleIds) {
		this.spcSampleIds = spcSampleIds;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getInspector() {
		return inspector;
	}

	public void setInspector(String inspector) {
		this.inspector = inspector;
	}

	public String getIsJnUnit() {
		return isJnUnit;
	}

	public void setIsJnUnit(String isJnUnit) {
		this.isJnUnit = isJnUnit;
	}

	public String getIsInEquipment() {
		return isInEquipment;
	}

	public void setIsInEquipment(String isInEquipment) {
		this.isInEquipment = isInEquipment;
	}

	public String getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}

	public String getAttachmentFiles() {
		return attachmentFiles;
	}

	public void setAttachmentFiles(String attachmentFiles) {
		this.attachmentFiles = attachmentFiles;
	}

	public MfgPatrolReport getMfgPatrolReport() {
		return mfgPatrolReport;
	}

	public void setMfgPatrolReport(MfgPatrolReport mfgPatrolReport) {
		this.mfgPatrolReport = mfgPatrolReport;
	}

	public List<MfgPatrolDetailData> getMfgPatrolDetailDatas() {
		return mfgPatrolDetailDatas;
	}

	public void setMfgPatrolDetailDatas(
			List<MfgPatrolDetailData> mfgPatrolDetailDatas) {
		this.mfgPatrolDetailDatas = mfgPatrolDetailDatas;
	}

	
	
}