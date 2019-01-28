package com.ambition.aftersales.customer.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.aftersales.entity.CustomerIssue;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:客户issue台账DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2018年4月12日 发布
 */
@Repository
public class CustomerIssueDao extends HibernateDao<CustomerIssue,Long>{
	public Page<CustomerIssue> list(Page<CustomerIssue> page){
		return findPage(page, "from CustomerIssue d where d.companyId=?", ContextUtils.getCompanyId());
	}
	
	public List<CustomerIssue> getAllCustomerIssue(){
		return find("from CustomerIssue d where d.companyId=?", ContextUtils.getCompanyId());
	}

    public Page<CustomerIssue> search(Page<CustomerIssue> page) {
        return searchPageByHql(page, "from CustomerIssue d where d.companyId=?  ",ContextUtils.getCompanyId());
    }
}
