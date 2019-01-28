package com.ambition.aftersales.lar.dao;

import org.springframework.stereotype.Repository;

import com.ambition.aftersales.entity.LarData;
import com.ambition.aftersales.entity.LarDefectiveItem;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class LarDefectiveItemDao extends HibernateDao<LarDefectiveItem, Long> {
	
	 public Page<LarDefectiveItem> searchItem(Page<LarDefectiveItem> page,LarData larData) {
	        return searchPageByHql(page, "from LarDefectiveItem d  where d.companyId = ? and d.larData=? ",new Object[]{ContextUtils.getCompanyId(),larData});
	    }
	
}
