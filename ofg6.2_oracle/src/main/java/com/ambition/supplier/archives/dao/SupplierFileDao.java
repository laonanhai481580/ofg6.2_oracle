package com.ambition.supplier.archives.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierFile;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:供应商汇报资料
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  lpf
 * @version 1.00 2018年9月23日 发布
 */
@Repository
public class SupplierFileDao extends  HibernateDao<SupplierFile,Long>{

	public List<SupplierFile> searchByFileName(String fileName, Long id) {
		if(null!=id&&!"".equals(id)){
        	return find("from SupplierFile t where t.fileName=? and t.id != ? and t.companyId=? and t.isDelete=false ",fileName,id,ContextUtils.getCompanyId());
    	}
    	return find("from SupplierFile t where t.fileName=? and t.companyId=? and t.isDelete=false ",fileName,ContextUtils.getCompanyId());
	}
	public Page<SupplierFile> searchBySupplier(Page<SupplierFile> page) {
		  String hql = "from SupplierFile o where o.supplierCode=? and o.hiddenState='N'  ";
		  String login=ContextUtils.getLoginName();
		  return searchPageByHql(page, hql,login);
	}
	public Page<SupplierFile> search(Page<SupplierFile> page) {
		  String hql = "from SupplierFile o where  o.hiddenState='N'  ";
		  return searchPageByHql(page, hql);
	}
}
