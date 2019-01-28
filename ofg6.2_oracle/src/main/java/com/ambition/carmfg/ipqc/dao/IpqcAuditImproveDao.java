package com.ambition.carmfg.ipqc.dao;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.IpqcAuditImprove;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
/**
 * 
 * 类名:IPQC稽核问题点改善Dao
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月9日 发布
 */
@Repository
public class IpqcAuditImproveDao extends HibernateDao<IpqcAuditImprove, Long>{

	public Page<IpqcAuditImprove> searchHide(Page<IpqcAuditImprove> page) {
		  String hql = "from IpqcAuditImprove o where o.hiddenState='Y' ";
		  return searchPageByHql(page, hql);
	}

	public Page<IpqcAuditImprove> searchPage(Page<IpqcAuditImprove> page) {
		return searchPageByHql(page, "from IpqcAuditImprove d where d.hiddenState='N' ");
	}

}
