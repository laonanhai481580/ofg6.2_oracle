package com.ambition.supplier.materialadmit.supplierMaterialsFile.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.entity.SupplierMaterialsFile;
import com.ambition.supplier.materialadmit.supplierMaterialsFile.dao.SupplierMaterialsFileDao;
import com.norteksoft.product.orm.Page;

@Service
@Transactional
public class SupplierMaterialsFileManager {
	@Autowired
	private SupplierMaterialsFileDao supplierMaterialsFileDao;
	
	public SupplierMaterialsFile getSupplierMaterialsFile(Long id){
		return supplierMaterialsFileDao.get(id);
	}
	public void saveSupplierMaterialsFile(SupplierMaterialsFile supplierMaterialsFile){
		supplierMaterialsFileDao.save(supplierMaterialsFile);
	}
	
	public Page<SupplierMaterialsFile> list(Page<SupplierMaterialsFile> page){
		return supplierMaterialsFileDao.list(page);
	}
	
	public List<SupplierMaterialsFile> listAll(){
		return supplierMaterialsFileDao.getSupplierMaterialsFile();
	}
	
	
	public void deleteSupplierMaterialsFile(String deleteIds){
		String[] ids = deleteIds.split(",");
 		for(String id : ids){
			SupplierMaterialsFile supplierMaterialsFile=supplierMaterialsFileDao.get(Long.valueOf(id));
			if(supplierMaterialsFile.getId() != null){
				supplierMaterialsFileDao.delete(supplierMaterialsFile);
			}
		}
	}
	
	public Page<SupplierMaterialsFile> search(Page<SupplierMaterialsFile> page){
		return supplierMaterialsFileDao.search(page);
	}
}
