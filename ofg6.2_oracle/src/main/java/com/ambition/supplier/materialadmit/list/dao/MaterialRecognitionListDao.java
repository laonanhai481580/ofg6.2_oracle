package com.ambition.supplier.materialadmit.list.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.MaterialRecognitionList;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class MaterialRecognitionListDao extends HibernateDao<MaterialRecognitionList,Long>{
	public Page<MaterialRecognitionList> list(Page<MaterialRecognitionList> page){
		return findPage(page,"from MaterialRecognitionList m where m.companyId=?",ContextUtils.getCompanyId());
	}
	public List<MaterialRecognitionList> getMaterialRecognitionList(){
		return find("from MaterialRecognitionList m where m.companyId=?", ContextUtils.getCompanyId());
	}
	public Page<MaterialRecognitionList> search(Page<MaterialRecognitionList> page){
		return searchPageByHql(page,"from MaterialRecognitionList m where m.companyId= ? ",ContextUtils.getCompanyId());
		
	}
}
