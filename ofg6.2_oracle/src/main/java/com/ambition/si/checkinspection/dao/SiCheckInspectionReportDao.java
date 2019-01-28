package com.ambition.si.checkinspection.dao;

import org.springframework.stereotype.Repository;

import com.ambition.si.entity.SiCheckInspectionReport;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 检验报告DAO
 * @author lpf
 *
 */
@Repository
public class SiCheckInspectionReportDao extends HibernateDao<SiCheckInspectionReport, Long> {
	public Page<SiCheckInspectionReport> search(Page<SiCheckInspectionReport> page) {
		String hql = "from SiCheckInspectionReport i where i.companyId = ? and  i.hiddenState='N' ";
	    return searchPageByHql(page,hql,ContextUtils.getCompanyId());
	}
	
	public Page<SiCheckInspectionReport> listHide(Page<SiCheckInspectionReport> page) {
		String hql = "from SiCheckInspectionReport i where i.companyId = ?  and  i.hiddenState='Y' ";
	    return searchPageByHql(page,hql,ContextUtils.getCompanyId());
	}
	
}
	
