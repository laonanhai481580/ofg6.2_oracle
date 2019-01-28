package com.ambition.supplier.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:不良分类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月21日 发布
 */
@Entity
@Table(name = "SUPPLIER_DEFECTION_CLASS")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class SupplierDefectionClass extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String supplierDefectionClass;//不良类型
	private String productType;//物料类别
	@OneToMany(mappedBy="supplierDefectionClass")
	@OrderBy("supplierDefectionItemNo")
	@JsonIgnore
	List<SupplierDefectionItem> supplierDefectionItems;//不良项目
	public String getSupplierDefectionClass() {
		return supplierDefectionClass;
	}
	public void setSupplierDefectionClass(String supplierDefectionClass) {
		this.supplierDefectionClass = supplierDefectionClass;
	}
	public List<SupplierDefectionItem> getSupplierDefectionItems() {
		return supplierDefectionItems;
	}
	public void setSupplierDefectionItems(
			List<SupplierDefectionItem> supplierDefectionItems) {
		this.supplierDefectionItems = supplierDefectionItems;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}

	
	
}
