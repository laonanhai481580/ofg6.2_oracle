package com.ambition.qsm.customeraudit.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.qsm.entity.CustomerAudit;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:客户审核履历DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月26日 发布
 */
@Repository
public class CustomerAuditDao extends HibernateDao<CustomerAudit,Long>{
	public Page<CustomerAudit> list(Page<CustomerAudit> page){
		return findPage(page, "from CustomerAudit d where d.companyId=?",ContextUtils.getCompanyId());
	}
	
	public List<CustomerAudit> getAllCustomerAudit(){
		return find("from CustomerAudit d where d.companyId=?",ContextUtils.getCompanyId());
	}

    public Page<CustomerAudit> search(Page<CustomerAudit> page) {
        return searchPageByHql(page, "from CustomerAudit d where d.companyId=?",ContextUtils.getCompanyId());
    }
}
