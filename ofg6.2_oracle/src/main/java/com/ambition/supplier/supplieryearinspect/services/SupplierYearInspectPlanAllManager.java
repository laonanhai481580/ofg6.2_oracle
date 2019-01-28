package com.ambition.supplier.supplieryearinspect.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.archives.dao.SupplierDao;
import com.ambition.supplier.entity.SupplierYearInspectPlanAll;
import com.ambition.supplier.supplieryearinspect.dao.SupplierYearInspectPlanAllDao;
import com.norteksoft.product.orm.Page;

@Service
@Transactional
public class SupplierYearInspectPlanAllManager {
	@Autowired
	private SupplierYearInspectPlanAllDao yearInspectPlanAllDao;
	@Autowired
	private SupplierDao supplierDao;
	public SupplierYearInspectPlanAll getSupplierYearInspectPlanAll(Long id){
		return yearInspectPlanAllDao.get(id);
	}
	public void saveSupplierYearInspectPlanAll(SupplierYearInspectPlanAll yearInspectPlanAll){
		yearInspectPlanAllDao.save(yearInspectPlanAll);
	}
	
	public Page<SupplierYearInspectPlanAll> list(Page<SupplierYearInspectPlanAll> page){
		return yearInspectPlanAllDao.list(page);
	}
	
	public List<SupplierYearInspectPlanAll> listAll(){
		return yearInspectPlanAllDao.getSupplierYearInspectPlanAll();
	}
	
	
	public void deleteMaintain(String deleteIds){
		String[] ids = deleteIds.split(",");
 		for(String id : ids){
 			SupplierYearInspectPlanAll yearInspectPlanAll=yearInspectPlanAllDao.get(Long.valueOf(id));
			if(yearInspectPlanAll.getId() != null){
				yearInspectPlanAllDao.delete(yearInspectPlanAll);
			}
		}
	}
	
	public Page<SupplierYearInspectPlanAll> search(Page<SupplierYearInspectPlanAll> page){
		return yearInspectPlanAllDao.search(page);
	}
}
