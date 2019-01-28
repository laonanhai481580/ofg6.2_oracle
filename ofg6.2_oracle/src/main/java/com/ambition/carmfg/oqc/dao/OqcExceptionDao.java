package com.ambition.carmfg.oqc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.OqcException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:OQC异常管理台账DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2018年4月13日 发布
 */
@Repository
public class OqcExceptionDao extends HibernateDao<OqcException,Long>{
	public Page<OqcException> list(Page<OqcException> page){
		return findPage(page, "from OqcException d");
	}
	
	public List<OqcException> getAllOqcException(){
		return find("from OqcException d");
	}

    public Page<OqcException> search(Page<OqcException> page) {
        return searchPageByHql(page, "from OqcException d where d.companyId=?  ",ContextUtils.getCompanyId());
    }
}
