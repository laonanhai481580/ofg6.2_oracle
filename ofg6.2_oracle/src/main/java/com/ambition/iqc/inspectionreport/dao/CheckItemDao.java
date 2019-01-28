package com.ambition.iqc.inspectionreport.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.iqc.entity.CheckItem;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class CheckItemDao extends HibernateDao<CheckItem, Long>{
	public Page<CheckItem> listItems(Page<CheckItem> page,Long id ){
		  String hql = "from CheckItem o where id=? ";
		  return searchPageByHql(page, hql,id);
	}
	
	public List<CheckItem> getAllCheckItems(){
		 String hql = "from CheckItem o where o.companyId=? and o.countType='计量' and o.resultsTotal is null ";
		return find(hql, ContextUtils.getCompanyId());
	}
}
