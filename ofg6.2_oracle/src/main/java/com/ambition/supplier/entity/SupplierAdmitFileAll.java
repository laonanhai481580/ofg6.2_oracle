package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="SUPPLIER_ADMIT_FILE_ALL")
public class SupplierAdmitFileAll extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String materialSort;//材料类别
	private String auditDepartment;//审核部门
	private String materialName;//资料名称
	private String materialFile;//文件
	private String remark;//备注
	private String fileBT;//附件按钮
	
	//用于集成判断
	private  Long sourceId;//集成过来原ID
	private String sourceUnit;//集成的事业部	
	@ManyToOne
	@JoinColumn(name="SUPPLIER_SUBLIST_ID")
    @JsonIgnore()
	private SupplierAdmitAll supplierAdmitAll;
	
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public SupplierAdmitAll getSupplierAdmitAll() {
		return supplierAdmitAll;
	}
	public void setSupplierAdmitAll(SupplierAdmitAll supplierAdmitAll) {
		this.supplierAdmitAll = supplierAdmitAll;
	}
	public String getMaterialSort() {
		return materialSort;
	}
	public void setMaterialSort(String materialSort) {
		this.materialSort = materialSort;
	}
	public String getAuditDepartment() {
		return auditDepartment;
	}
	public void setAuditDepartment(String auditDepartment) {
		this.auditDepartment = auditDepartment;
	}
	public String getMaterialFile() {
		return materialFile;
	}
	public void setMaterialFile(String materialFile) {
		this.materialFile = materialFile;
	}
	public String getFileBT() {
		return fileBT;
	}
	public void setFileBT(String fileBT) {
		this.fileBT = fileBT;
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
