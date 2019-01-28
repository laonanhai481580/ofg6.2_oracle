package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 
 * 类名:材料承认清单
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  
 * @version 1.00 2018年5月23日 发布
 */
@Entity
@Table(name="MATERIAL_RECOGNITION_LIST")
public class MaterialRecognitionList extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String supplierName;//供应商名称
	private String supplierCode;//供应商编码
	private String materialName;//物料名称
	private String materialCode;//物料编码
	private String materialSort;//物料类别
	private String productVersion;//规格型号版本号
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
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getMaterialSort() {
		return materialSort;
	}
	public void setMaterialSort(String materialSort) {
		this.materialSort = materialSort;
	}
	public String getProductVersion() {
		return productVersion;
	}
	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}
	
}
