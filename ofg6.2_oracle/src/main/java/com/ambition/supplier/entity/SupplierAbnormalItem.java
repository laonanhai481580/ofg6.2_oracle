package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name="SUPPLIER_ABNORMAL_ITEM")
public class SupplierAbnormalItem extends IdEntity{
		/**
		  *SupplierAbnormalItem.java2018年3月14日
		 */
	private static final long serialVersionUID = 1L;
	private String defectionClass;//不良类型
	private String defectionItemNo;//不良项目编码
	private String defectionItemName;//不良项目名称
	private Integer defectionItemValue;//不良项目值
	@ManyToOne
	@JoinColumn(name = "FK_SUPPLIER_DATA_ID")
	@JsonIgnore()
	private SupplierAbnormal supplierAbnormal;
	public String getDefectionClass() {
		return defectionClass;
	}
	public void setDefectionClass(String defectionClass) {
		this.defectionClass = defectionClass;
	}
	public String getDefectionItemNo() {
		return defectionItemNo;
	}
	public void setDefectionItemNo(String defectionItemNo) {
		this.defectionItemNo = defectionItemNo;
	}
	public String getDefectionItemName() {
		return defectionItemName;
	}
	public void setDefectionItemName(String defectionItemName) {
		this.defectionItemName = defectionItemName;
	}
	public Integer getDefectionItemValue() {
		return defectionItemValue;
	}
	public void setDefectionItemValue(Integer defectionItemValue) {
		this.defectionItemValue = defectionItemValue;
	}
	public SupplierAbnormal getSupplierAbnormal() {
		return supplierAbnormal;
	}
	public void setSupplierAbnormal(SupplierAbnormal supplierAbnormal) {
		this.supplierAbnormal = supplierAbnormal;
	}
	
}
