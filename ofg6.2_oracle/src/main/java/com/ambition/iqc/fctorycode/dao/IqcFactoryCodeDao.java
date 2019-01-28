package com.ambition.iqc.fctorycode.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.iqc.entity.IqcFactoryCode;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class IqcFactoryCodeDao extends HibernateDao<IqcFactoryCode, Long> {
	public Page<IqcFactoryCode> list(Page<IqcFactoryCode> page){
		return findPage(page, "from IqcFactoryCode d where d.companyId=? ",ContextUtils.getCompanyId());
	}
	
	public List<IqcFactoryCode> getAllIqcFactoryCode(){
		return find("from IqcFactoryCode d where d.companyId=? ",ContextUtils.getCompanyId());
	}

    public Page<IqcFactoryCode> search(Page<IqcFactoryCode> page) {
        return searchPageByHql(page, "from IqcFactoryCode d where d.companyId=? ",ContextUtils.getCompanyId());
    }
    
	public  IqcFactoryCode getIqcFactoryCode(String code) {
		String hql = "from IqcFactoryCode d where d.organizationCode = ?";
		List<IqcFactoryCode> iqcFactoryCodes = this.find(hql,code);
		if(iqcFactoryCodes.size()>0){
			return iqcFactoryCodes.get(0);
		}else{
			return null;
		}
	}   
}