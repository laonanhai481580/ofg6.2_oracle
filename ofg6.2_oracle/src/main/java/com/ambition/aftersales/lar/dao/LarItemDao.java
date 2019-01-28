package com.ambition.aftersales.lar.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.aftersales.entity.LarItem;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:LAR数据子表DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Repository
public class LarItemDao extends HibernateDao<LarItem,Long>{
	public Page<LarItem> list(Page<LarItem> page){
		return findPage(page, "from LarItem d");
	}
	
	public List<LarItem> getAllLarItem(){
		return find("from LarItem d");
	}

    public Page<LarItem> search(Page<LarItem> page) {
        return searchPageByHql(page, "from LarItem d ");
    }
    
	public Page<LarItem> searchByBusinessUnit(Page<LarItem> page,String businessUnit){
		return searchPageByHql(page,"from LarItem o where o.companyId=? and o.businessUnitName=? ",ContextUtils.getCompanyId(),businessUnit);
	}
	public Page<LarItem> searchByLarId(Page<LarItem> page,Long larId){
		return searchPageByHql(page,"from LarItem o where o.companyId=? and o.larData.id=? ",ContextUtils.getCompanyId(),larId);
	}
}
