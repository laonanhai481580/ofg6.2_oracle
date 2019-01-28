package com.ambition.ecm.dcrn.dao;


import org.springframework.stereotype.Repository;

import com.ambition.ecm.entity.DcrnReport;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Repository
public class DcrnReportDao extends HibernateDao<DcrnReport, Long>{
	
	public Page<DcrnReport> searchPage(Page<DcrnReport> page){
		String hql="from DcrnReport report";
		return this.searchPageByHql(page, hql);
	}
}
