package com.ambition.gsm.equipment.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.InternalAuditor;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:内审员DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2018年7月26日 发布
 */
@Repository
public class InternalAuditorDao extends HibernateDao<InternalAuditor,Long>{
	public Page<InternalAuditor> list(Page<InternalAuditor> page){
		return findPage(page, "from InternalAuditor d where d.companyId=?",ContextUtils.getCompanyId());
	}
	
	public List<InternalAuditor> getAllInternalAuditor(){
		return find("from InternalAuditor d where d.companyId=?",ContextUtils.getCompanyId());
	}

    public Page<InternalAuditor> search(Page<InternalAuditor> page) {
        return searchPageByHql(page, "from InternalAuditor d where d.companyId=?",ContextUtils.getCompanyId());
    }
}
