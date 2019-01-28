package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 
 * 类名:供应商材料文件
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  Janam
 * @version 1.00 2017年9月22日 发布
 */
@Entity
@Table(name="SUPPLIER_MATERIALS_FILE")
public class SupplierMaterialsFile extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String fileName;//文件名称
	private String materialsFile;//文件
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getMaterialsFile() {
		return materialsFile;
	}
	public void setMaterialsFile(String materialsFile) {
		this.materialsFile = materialsFile;
	}
	
}
