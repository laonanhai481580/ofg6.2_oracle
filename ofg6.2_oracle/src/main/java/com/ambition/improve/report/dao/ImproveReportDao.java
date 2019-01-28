package com.ambition.improve.report.dao;

import org.springframework.stereotype.Repository;

import com.ambition.improve.entity.ImproveReport;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
/**
 * 
 * 类名:8D改进报告Dao
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月29日 发布
 */
@Repository
public class ImproveReportDao extends HibernateDao<ImproveReport, Long>{
	public Page<ImproveReport> searchSharePage(
			Page<ImproveReport> page) {
		return searchPageByHql(page, "from ImproveReport d where d.isShare='是' ");
	}
	public Page<ImproveReport> searchPage(
			Page<ImproveReport> page) {
		return searchPageByHql(page, "from ImproveReport d where d.hiddenState='N' ");
	}
	public Page<ImproveReport> searchHide(Page<ImproveReport> page) {
		  String hql = "from ImproveReport o where o.hiddenState='Y' ";
		  return searchPageByHql(page, hql);
	}
}
