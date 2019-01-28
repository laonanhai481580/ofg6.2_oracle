package com.ambition.aftersales.customer.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.aftersales.entity.CustomerEvaluation;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:重要客户评价DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2018年4月12日 发布
 */
@Repository
public class CustomerEvaluationDao extends HibernateDao<CustomerEvaluation,Long>{
	public Page<CustomerEvaluation> list(Page<CustomerEvaluation> page){
		return findPage(page, "from CustomerEvaluation d where d.companyId=?", ContextUtils.getCompanyId());
	}
	
	public List<CustomerEvaluation> getAllCustomerEvaluation(){
		return find("from CustomerEvaluation d where d.companyId=?", ContextUtils.getCompanyId());
	}

    public Page<CustomerEvaluation> search(Page<CustomerEvaluation> page,String customer,Integer yearInt) {
        return searchPageByHql(page, "from CustomerEvaluation d where d.companyId=? and d.customer=? and yearInt=?  ",ContextUtils.getCompanyId(),customer,yearInt);
    }
}
