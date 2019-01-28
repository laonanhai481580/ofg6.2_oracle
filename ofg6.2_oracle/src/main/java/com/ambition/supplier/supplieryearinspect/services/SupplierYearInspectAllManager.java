package com.ambition.supplier.supplieryearinspect.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.entity.SupplierYearInspectAll;
import com.ambition.supplier.supplieryearinspect.dao.SupplierYearInspectAllDao;
import com.norteksoft.product.orm.hibernate.HibernateDao;

/**
 * 
 * 类名:供应商稽核计划Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  
 * @version 1.00 
 */
@Service
@Transactional
public class SupplierYearInspectAllManager extends AmbWorkflowManagerBase<SupplierYearInspectAll>{
	@Autowired
	private SupplierYearInspectAllDao yearInspectAllDao;	

	@Override
	public HibernateDao<SupplierYearInspectAll, Long> getHibernateDao() {
		return yearInspectAllDao;
	}

	@Override
	public String getEntityListCode() {
		return SupplierYearInspectAll.ENTITY_LIST_CODE;
	}

	@Override
	public Class<SupplierYearInspectAll> getEntityInstanceClass() {
		return SupplierYearInspectAll.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "supplier_yearinspect_all";
	}

	@Override
	public String getWorkflowDefinitionName() {
		return "供应商稽核报告2.0汇总";
	}
	
}
