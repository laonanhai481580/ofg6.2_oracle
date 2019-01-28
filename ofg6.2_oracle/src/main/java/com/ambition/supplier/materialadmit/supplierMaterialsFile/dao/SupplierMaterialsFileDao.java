package com.ambition.supplier.materialadmit.supplierMaterialsFile.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierMaterialsFile;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class SupplierMaterialsFileDao extends HibernateDao<SupplierMaterialsFile,Long>{
	public Page<SupplierMaterialsFile> list(Page<SupplierMaterialsFile> page){
		return findPage(page,"from SupplierMaterialsFile m where m.companyId=?",ContextUtils.getCompanyId());
	}
	public List<SupplierMaterialsFile> getSupplierMaterialsFile(){
		return find("from SupplierMaterialsFile m where m.companyId=?", ContextUtils.getCompanyId());
	}
	public Page<SupplierMaterialsFile> search(Page<SupplierMaterialsFile> page){
		return searchPageByHql(page,"from SupplierMaterialsFile m where m.companyId=?", ContextUtils.getCompanyId());
	}
} 
