package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:不良项目
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月21日 发布
 */
@Entity
@Table(name = "SUPPLIER_DEFECTION_ITEM")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class SupplierDefectionItem extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String supplierDefectionItemNo;//不良项目编码
	private String supplierDefectionItemName;//不良项目名称	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "FK_DEFECTION_TYPE_NO")
	private SupplierDefectionClass supplierDefectionClass;//不良类型	
	public String getSupplierDefectionItemNo() {
		return supplierDefectionItemNo;
	}
	public void setSupplierDefectionItemNo(String supplierDefectionItemNo) {
		this.supplierDefectionItemNo = supplierDefectionItemNo;
	}
	public String getSupplierDefectionItemName() {
		return supplierDefectionItemName;
	}
	public void setSupplierDefectionItemName(String supplierDefectionItemName) {
		this.supplierDefectionItemName = supplierDefectionItemName;
	}
	public SupplierDefectionClass getSupplierDefectionClass() {
		return supplierDefectionClass;
	}
	public void setSupplierDefectionClass(
			SupplierDefectionClass supplierDefectionClass) {
		this.supplierDefectionClass = supplierDefectionClass;
	}

	
	
}
