package com.ambition.supplier.improve.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierImprove;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年11月4日 发布
 */
@Repository
public class SupplierImproveDao extends HibernateDao<SupplierImprove,Long>{

	public Page<SupplierImprove> searchSingle(Page<SupplierImprove> page) {
		  String hql = "from SupplierImprove o where (o.sqeCheckerLog=? or o.creator=?) and o.hiddenState='N'  ";
		  String login=ContextUtils.getLoginName();
		  return searchPageByHql(page, hql,login,login);
	}
	public Page<SupplierImprove> searchUnSingle(Page<SupplierImprove> page) {
		  String hql = "from SupplierImprove o where (o.sqeCheckerLog=? or o.creator=?) and o.hiddenState='N' and o.isClosedAlaysis='否' ";
		  String login=ContextUtils.getLoginName();
		  return searchPageByHql(page, hql,login,login);
	}
	public List<SupplierImprove> getAllReport(){
		 String hql = "from SupplierImprove o where o.companyId=? and o.sqeReplyTime is null and o.sqeApprovalerDate is not null ";
		return find(hql, ContextUtils.getCompanyId());
	}
	public Page<SupplierImprove> searchHide(Page<SupplierImprove> page) {
		  String hql = "from SupplierImprove o where o.hiddenState='Y' ";
		  return searchPageByHql(page, hql);
	}
	public Page<SupplierImprove> searchSupplierSingle(Page<SupplierImprove> page) {
		  String hql = "from SupplierImprove o where o.supplierCode=? and o.hiddenState='N'  ";
		  String login=ContextUtils.getLoginName();
		  return searchPageByHql(page, hql,login);
	}
	public Page<SupplierImprove> searchUnSupplierSingle(Page<SupplierImprove> page) {
		  String hql = "from SupplierImprove o where o.supplierCode=? and o.hiddenState='N' and o.isClosedAlaysis='否' ";
		  String login=ContextUtils.getLoginName();
		  return searchPageByHql(page, hql,login);
	}	
	public Page<SupplierImprove> search(Page<SupplierImprove> page) {
		String hql = "from SupplierImprove o where o.hiddenState='N'  ";
		  return searchPageByHql(page, hql);
	}
	public Page<SupplierImprove> searchUn(Page<SupplierImprove> page) {
		String hql = "from SupplierImprove o where o.hiddenState='N' and o.isClosedAlaysis='否' ";
		  return searchPageByHql(page, hql);
	}
}
