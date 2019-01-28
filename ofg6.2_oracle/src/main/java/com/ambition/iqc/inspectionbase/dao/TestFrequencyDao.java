package com.ambition.iqc.inspectionbase.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.iqc.entity.TestFrequency;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class TestFrequencyDao extends HibernateDao<TestFrequency, Long> {
	public Page<TestFrequency> list(Page<TestFrequency> page){
		return findPage(page, "from TestFrequency d");
	}
	
	public List<TestFrequency> getAllTestFrequency(){
		return find("from TestFrequency d");
	}

    public Page<TestFrequency> search(Page<TestFrequency> page) {
    	String businessUnitName = CommonUtil.getDataByLogin();
    	String hql = "from TestFrequency d where d.isMaterial='否' ";
    	if(businessUnitName!=null&&"欧菲-TP".equals(ContextUtils.getCompanyName())){
    		hql += " and d.businessUnitName='"+businessUnitName+"' ";
    	}
		  return searchPageByHql(page, hql);
    }  
    
    public Page<TestFrequency> searchMaterial(Page<TestFrequency> page) {
    	String businessUnitName = CommonUtil.getDataByLogin();
    	String hql = "from TestFrequency d where d.isMaterial='是' ";
    	if(businessUnitName!=null&&"欧菲-TP".equals(ContextUtils.getCompanyName())){
    		hql += " and d.businessUnitName='"+businessUnitName+"' ";
    	}
		  return searchPageByHql(page, hql);
    }  
}