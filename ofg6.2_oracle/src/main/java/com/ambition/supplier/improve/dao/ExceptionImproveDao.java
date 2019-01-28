package com.ambition.supplier.improve.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.ExceptionImprove;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PropUtils;
/**    
 * 物料异常矫正单
 * @authorBy LPF
 *
 */
@Repository
public class ExceptionImproveDao extends HibernateDao<ExceptionImprove,Long>{

	public Page<ExceptionImprove> searchSingle(Page<ExceptionImprove> page) {
		  String hql = "from ExceptionImprove o where (o.sqeCheckerLog=? or o.creator=?) and o.hiddenState='N' and (o.sourceUnit=? or o.sourceUnit is null) ";
		  String companyName= PropUtils.getProp("companyName");
		  String login=ContextUtils.getLoginName();
		  return searchPageByHql(page, hql,login,login,companyName);
	}
	public Page<ExceptionImprove> searchUnSingle(Page<ExceptionImprove> page) {
		  String hql = "from ExceptionImprove o where (o.mqeCheckerLog=? or o.creator=?) and o.hiddenState='N' and o.isClosedAlaysis='否' and o.sourceUnit=? ";
		  String login=ContextUtils.getLoginName();
		  String companyName= PropUtils.getProp("companyName");
		  return searchPageByHql(page, hql,login,login,companyName);
	}
	public Page<ExceptionImprove> searchHide(Page<ExceptionImprove> page) {
		  String hql = "from ExceptionImprove o where o.hiddenState='Y' ";
		  return searchPageByHql(page, hql);
	}
	public Page<ExceptionImprove> searchSupplierSingle(Page<ExceptionImprove> page) {
		  String hql = "from ExceptionImprove o where o.supplierCode=? and o.hiddenState='N' and (o.sourceUnit=? or o.sourceUnit is null) ";
		  String login=ContextUtils.getLoginName();
		  String companyName= PropUtils.getProp("companyName");
		  return searchPageByHql(page, hql,login,companyName);
	}
	public Page<ExceptionImprove> searchUnSupplierSingle(Page<ExceptionImprove> page) {
		  String hql = "from ExceptionImprove o where o.supplierCode=? and o.hiddenState='N' and o.isClosedAlaysis='否' and o.sourceUnit=? ";
		  String login=ContextUtils.getLoginName();
		  String companyName= PropUtils.getProp("companyName");
		  return searchPageByHql(page, hql,login,companyName);
	}	
	public Page<ExceptionImprove> search(Page<ExceptionImprove> page) {
		String hql = "from ExceptionImprove o where o.hiddenState='N' and (o.sourceUnit=? or o.sourceUnit is null) ";
		String companyName= PropUtils.getProp("companyName");
		  return searchPageByHql(page, hql,companyName);
	}
	
	public Page<ExceptionImprove> searchAll(Page<ExceptionImprove> page) {
		String hql = "from ExceptionImprove o where o.hiddenState='N' ";
		  return searchPageByHql(page, hql);
	}
	public Page<ExceptionImprove> searchUn(Page<ExceptionImprove> page) {
		String hql = "from ExceptionImprove o where o.hiddenState='N' and o.isClosedAlaysis='否' and o.sourceUnit=? ";
		String companyName= PropUtils.getProp("companyName");
		  return searchPageByHql(page, hql,companyName);
	}
}
