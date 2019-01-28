package com.ambition.aftersales.customer.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.aftersales.entity.CustomerReturn;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:客退率台账DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2018年4月12日 发布
 */
@Repository
public class CustomerReturnDao extends HibernateDao<CustomerReturn,Long>{
	public Page<CustomerReturn> list(Page<CustomerReturn> page){
		return findPage(page, "from CustomerReturn d where d.companyId=?", ContextUtils.getCompanyId());
	}
	
	public List<CustomerReturn> getAllCustomerReturn(){
		return find("from CustomerReturn d where d.companyId=?", ContextUtils.getCompanyId());
	}

    public Page<CustomerReturn> search(Page<CustomerReturn> page) {
        return searchPageByHql(page, "from CustomerReturn d where d.companyId=?  ",ContextUtils.getCompanyId());
    }
}
