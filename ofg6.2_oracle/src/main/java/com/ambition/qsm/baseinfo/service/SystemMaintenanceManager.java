package com.ambition.qsm.baseinfo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.qsm.baseinfo.dao.SystemMaintenanceDao;
import com.ambition.qsm.entity.SystemMaintenance;
import com.norteksoft.product.orm.Page;

/**
 * 
 * 类名:体系维护Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月26日 发布
 */
@Service
@Transactional
public class SystemMaintenanceManager {
	@Autowired
	private SystemMaintenanceDao systemMaintenanceDao;	
	public SystemMaintenance getSystemMaintenance(Long id){
		return systemMaintenanceDao.get(id);
	}
	
	public void deleteSystemMaintenance(SystemMaintenance systemMaintenance){
		systemMaintenanceDao.delete(systemMaintenance);
	}

	public Page<SystemMaintenance> search(Page<SystemMaintenance>page){
		return systemMaintenanceDao.search(page);
	}

	public List<SystemMaintenance> listAll(){
		return systemMaintenanceDao.getAllSystemMaintenance();
	}
		
	public void deleteSystemMaintenance(Long id){
		systemMaintenanceDao.delete(id);
	}
	public void deleteSystemMaintenance(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			SystemMaintenance  systemMaintenance = systemMaintenanceDao.get(Long.valueOf(id));
			if(systemMaintenance.getId() != null){
				systemMaintenanceDao.delete(systemMaintenance);
			}
		}
	}
	public void saveSystemMaintenance(SystemMaintenance systemMaintenance){
		systemMaintenanceDao.save(systemMaintenance);
	}
//	public Page<SystemMaintenance> listState(Page<SystemMaintenance> page,String menutype){
//		String hql = " from SystemMaintenance e where e.menutype=?";
//		return systemMaintenanceDao.searchPageByHql(page, hql, menutype);
//	}
	public Page<SystemMaintenance> listState(Page<SystemMaintenance> page,String state,String menutype,String str){
		String hql = " from SystemMaintenance e where e.hiddenState=? ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(state);
		if(str!=null){
			hql=hql+" and e.factoryClassify=?";
			searchParams.add(str);
		}
		if(menutype!=null){
			hql=hql+" and e.menutype=?";
			searchParams.add(menutype);
		}
		return systemMaintenanceDao.searchPageByHql(page, hql, searchParams.toArray());
	}
}
