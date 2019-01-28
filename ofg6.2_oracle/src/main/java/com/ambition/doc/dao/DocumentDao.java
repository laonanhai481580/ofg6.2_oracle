package com.ambition.doc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.doc.entity.DocumentFiles;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  林少伟
 * @version 1.00 2014-6-6 发布
 */
@Repository
public class DocumentDao extends HibernateDao<DocumentFiles, Long> {
	public Page<DocumentFiles> list(Page<DocumentFiles> page){
		return findPage(page,"from DocumentFiles d where d.companyId=?",ContextUtils.getCompanyId());
	}
	public List<DocumentFiles> getGpAverageMaterial(){
		return find("from DocumentFiles d where d.companyId=?", ContextUtils.getCompanyId());
	}
	public Page<DocumentFiles> search(Page<DocumentFiles> page){
		return searchPageByHql(page,"from DocumentFiles d where d.companyId=?",ContextUtils.getCompanyId());
	}
}
