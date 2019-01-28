package com.ambition.supplier.abnormal.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierAbnormal;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年11月5日 发布
 */
@Repository
public class SupplierAbnormalDao extends HibernateDao<SupplierAbnormal, Long>{
	public Page<SupplierAbnormal> search(Page<SupplierAbnormal> page) {
        return searchPageByHql(page, "from SupplierAbnormal d ");
    }
	public Page<SupplierAbnormal> searchByBusinessUnit(Page<SupplierAbnormal> page,String businessUnit){
		return searchPageByHql(page,"from SupplierAbnormal o where o.companyId=? and o.businessUnitName=? ",ContextUtils.getCompanyId(),businessUnit);
	}
	public Page<SupplierAbnormal> searchByBusinessUnit(Page<SupplierAbnormal> page,String businessUnit,String productType){
		return searchPageByHql(page,"from SupplierAbnormal o where o.companyId=? and o.businessUnitName=? and o.productType=? ",ContextUtils.getCompanyId(),businessUnit,productType);
	}
}
