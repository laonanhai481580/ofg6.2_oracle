package com.ambition.carmfg.factoryprocedure.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.OqcFactory;
import com.ambition.carmfg.entity.OqcProcedure;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class OqcProcedureDao extends HibernateDao<OqcProcedure,Long>{
	
	public Page<OqcProcedure> list(Page<OqcProcedure> page,OqcFactory oqcFactory){
		if(oqcFactory != null){
			return searchPageByHql(page,"from OqcProcedure d where d.companyId = ? and d.oqcFactory = ?",new Object[]{ContextUtils.getCompanyId(),oqcFactory});
		}else{
			return searchPageByHql(page,"from OqcProcedure d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}

	public Page<OqcProcedure> list(Page<OqcProcedure> page, String code){
		if(code != null){
			return searchPageByHql(page,"from OqcProcedure d where d.companyId = ? and d.defectionCodeNo like ?",new Object[]{ContextUtils.getCompanyId(),"%"+code+"%"});
		}else{
			return searchPageByHql(page,"from OqcProcedure d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<OqcProcedure> getAllOqcProcedure(){
		return find("from OqcProcedure d where d.companyId=?",ContextUtils.getCompanyId());
	}	
	public List<OqcProcedure> getAllOqcProcedures(OqcFactory oqcFactory){
		if(oqcFactory != null){
			return find("from OqcProcedure d where d.companyId = ? and d.oqcFactory = ?",new Object[]{ContextUtils.getCompanyId(),oqcFactory});
		}else{
			return find("from OqcProcedure d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public  OqcProcedure getOqcProcedure(String procedure) {
		String hql = "from OqcProcedure d where d.procedure = ?";
		List<OqcProcedure> defectionCodes = this.find(hql,procedure);
		if(defectionCodes.size()>0){
			return defectionCodes.get(0);
		}else{
			return null;
		}
	}
}
