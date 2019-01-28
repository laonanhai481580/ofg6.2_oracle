package com.ambition.iqc.taskmonitor.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.iqc.entity.InspectionFromOverdueEmail;
import com.ambition.iqc.taskmonitor.dao.InspectionFromOverdueEmailDao;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2016-10-21 发布
 */
@Transactional
@Service
public class InspectionFromOverdueEmailManager {
	@Autowired
	private InspectionFromOverdueEmailDao inspectionFromOverdueEmailDao;
	
	public InspectionFromOverdueEmail getInspectionFromOverdueEmail(Long id){
		return inspectionFromOverdueEmailDao.get(id);
	}
	
	public void deleteInspectionFromOverdueEmail(String deleteIds){
		String [] ids=deleteIds.split(",");
		for(String id:ids){
			inspectionFromOverdueEmailDao.delete(Long.valueOf(id));
		}
	}
	
	
	public Page<InspectionFromOverdueEmail> getInspectionFromOverdueEmailPage(Page<InspectionFromOverdueEmail> page){
		return inspectionFromOverdueEmailDao.getPage(page);
	}
	
	public void saveInspectionFromOverdueEmail(InspectionFromOverdueEmail inspectionFromOverdueEmail){
		if(isExistOverdue(inspectionFromOverdueEmail.getId(),inspectionFromOverdueEmail.getMaterielCode(),inspectionFromOverdueEmail.getBusinessUnitName())){
			throw new AmbFrameException("该物料已经存在预警信息!");
		}	
		inspectionFromOverdueEmailDao.save(inspectionFromOverdueEmail);
	}
	
	/**
	 * 检查是否存在相同的预警信息
	 * @param id
	 * @param name
	 * @return
	 */
	public Boolean isExistOverdue(Long id,String materielCode,String businessUnitName){
		String hql = "select count(i.id) from InspectionFromOverdueEmail i where i.companyId = ? and i.materielCode = ?  ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(materielCode);
		if(businessUnitName != null&&"欧菲-TP".equals(ContextUtils.getCompanyName())){
			hql += " and i.businessUnitName= ? ";
			searchParams.add(businessUnitName);
		}
		if(id != null){
			hql += " and i.id <> ?";
			searchParams.add(id);
		}
		List<?> list = inspectionFromOverdueEmailDao.find(hql,searchParams.toArray());
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}	
	
}
