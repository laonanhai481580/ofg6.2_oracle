package com.ambition.iqc.taskmonitor.dao;

import org.springframework.stereotype.Repository;

import com.ambition.iqc.entity.InspectionTaskEmail;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;

@Repository
public class InspectionTaskEmailDao extends HibernateDao< InspectionTaskEmail,Long> {
	
	public Page<InspectionTaskEmail> getPage(Page<InspectionTaskEmail> page){
		String hql="from InspectionTaskEmail task ";
		return this.searchPageByHql(page, hql);
	}
}
