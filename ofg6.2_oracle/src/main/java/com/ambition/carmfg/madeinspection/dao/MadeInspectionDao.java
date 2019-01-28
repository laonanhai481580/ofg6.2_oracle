package com.ambition.carmfg.madeinspection.dao;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.MfgCheckInspectionReport;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  陈伟伟
 * @version 1.00 2016-9-3 发布
 */
@Repository
public class MadeInspectionDao extends HibernateDao<MfgCheckInspectionReport, Long> {
	public Page<MfgCheckInspectionReport> listHid(Page<MfgCheckInspectionReport> page){
		  String hql = "from MfgCheckInspectionReport o where o.hiddenState = 'Y'";
		  return searchPageByHql(page, hql);
	}
}
