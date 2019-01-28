package com.ambition.carmfg.factoryprocedure.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.OqcFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class OqcFactoryDao extends HibernateDao<OqcFactory,Long>{
	
	public Page<OqcFactory> list(Page<OqcFactory> page){
		return searchPageByHql(page,"from OqcFactory d where d.companyId=?  ",ContextUtils.getCompanyId());
	}
	public List<OqcFactory> getAllOqcFactory(){
		return find("from OqcFactory d where d.companyId=?",ContextUtils.getCompanyId());
	}
	public List<OqcFactory> getOqcFactoryByBusinessUnit(String businessUnit){
		return find("from OqcFactory d where d.companyId = ? and d.businessUnitName = ?  ", ContextUtils.getCompanyId(),businessUnit);
	}
	public OqcFactory getOqcFactoryByCode(String factory) {
		String hql = "from OqcFactory d where d.factory = ?";
		List<OqcFactory> list = this.find(hql,factory);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}		
	}
}
