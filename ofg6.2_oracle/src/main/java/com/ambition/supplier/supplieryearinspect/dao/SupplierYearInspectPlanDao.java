package com.ambition.supplier.supplieryearinspect.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierYearInspectPlan;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class SupplierYearInspectPlanDao extends HibernateDao<SupplierYearInspectPlan,Long>{
	public Page<SupplierYearInspectPlan> list(Page<SupplierYearInspectPlan> page){
		return findPage(page,"from SupplierYearInspectPlan t");
	}
	public List<SupplierYearInspectPlan> getSupplierYearInspectPlan(){
		return find("from SupplierYearInspectPlan t where t.companyId=?", ContextUtils.getCompanyId());
	}
	public Page<SupplierYearInspectPlan> search(Page<SupplierYearInspectPlan> page){
		return searchPageByHql(page,"from SupplierYearInspectPlan t");
	}
	public SupplierYearInspectPlan listPlan(String pId){
		String hql = "from SupplierYearInspectPlan t where t.planId=?";
		@SuppressWarnings("unchecked")
		List<SupplierYearInspectPlan> list = createQuery(hql,pId).list();
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
