package com.ambition.supplier.supplieryearinspect.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierYearInspectPlanAll;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class SupplierYearInspectPlanAllDao extends HibernateDao<SupplierYearInspectPlanAll,Long>{
	public Page<SupplierYearInspectPlanAll> list(Page<SupplierYearInspectPlanAll> page){
		return findPage(page,"from SupplierYearInspectPlanAll t");
	}
	public List<SupplierYearInspectPlanAll> getSupplierYearInspectPlanAll(){
		return find("from SupplierYearInspectPlanAll t where t.companyId=?", ContextUtils.getCompanyId());
	}
	public Page<SupplierYearInspectPlanAll> search(Page<SupplierYearInspectPlanAll> page){
		return searchPageByHql(page,"from SupplierYearInspectPlanAll t");
	}
	public SupplierYearInspectPlanAll listPlan(String pId){
		String hql = "from SupplierYearInspectPlanAll t where t.planId=?";
		@SuppressWarnings("unchecked")
		List<SupplierYearInspectPlanAll> list = createQuery(hql,pId).list();
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
