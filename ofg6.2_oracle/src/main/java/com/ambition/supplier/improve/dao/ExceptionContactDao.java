package com.ambition.supplier.improve.dao;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.ExceptionContact;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PropUtils;
/**    
 * 物料异常联络单
 * @authorBy LPF
 *
 */
@Repository
public class ExceptionContactDao extends HibernateDao<ExceptionContact,Long>{

	public Page<ExceptionContact> searchSingle(Page<ExceptionContact> page) {
		  String hql = "from ExceptionContact o where (o.mqeCheckerLog=? or o.creator=?) and o.hiddenState='N' and (o.sourceUnit=? or o.sourceUnit is null) ";
		  String companyName= PropUtils.getProp("companyName");
		  String login=ContextUtils.getLoginName();
		  return searchPageByHql(page, hql,login,login,companyName);
	}
	public Page<ExceptionContact> searchUnSingle(Page<ExceptionContact> page) {
		  String hql = "from ExceptionContact o where (o.mqeCheckerLog=? or o.creator=?) and o.hiddenState='N' and o.isClosedAlaysis='否' and o.sourceUnit=? ";
		  String login=ContextUtils.getLoginName();
		  String companyName= PropUtils.getProp("companyName");
		  return searchPageByHql(page, hql,login,login,companyName);
	}
	public Page<ExceptionContact> searchHide(Page<ExceptionContact> page) {
		  String hql = "from ExceptionContact o where o.hiddenState='Y' ";
		  return searchPageByHql(page, hql);
	}
	public Page<ExceptionContact> searchSupplierSingle(Page<ExceptionContact> page) {
		  String hql = "from ExceptionContact o where o.supplierCode=? and o.hiddenState='N' and (o.sourceUnit=? or o.sourceUnit is null) ";
		  String login=ContextUtils.getLoginName();
		  String companyName= PropUtils.getProp("companyName");
		  return searchPageByHql(page, hql,login,companyName);
	}
	public Page<ExceptionContact> searchUnSupplierSingle(Page<ExceptionContact> page) {
		  String hql = "from ExceptionContact o where o.supplierCode=? and o.hiddenState='N' and o.isClosedAlaysis='否' and o.sourceUnit=? ";
		  String login=ContextUtils.getLoginName();
		  String companyName= PropUtils.getProp("companyName");
		  return searchPageByHql(page, hql,login,companyName);
	}	
	public Page<ExceptionContact> search(Page<ExceptionContact> page) {
		String hql = "from ExceptionContact o where o.hiddenState='N' and (o.sourceUnit=? or o.sourceUnit is null) ";
		String companyName= PropUtils.getProp("companyName");
		 return searchPageByHql(page, hql,companyName);
	}
	
	public Page<ExceptionContact> searchAll(Page<ExceptionContact> page) {
		String hql = "from ExceptionContact o where o.hiddenState='N' ";
		  return searchPageByHql(page, hql);
	}	
	public Page<ExceptionContact> searchUn(Page<ExceptionContact> page) {
		String hql = "from ExceptionContact o where o.hiddenState='N' and o.isClosedAlaysis='否' and o.sourceUnit=? ";
		String companyName= PropUtils.getProp("companyName");
		  return searchPageByHql(page, hql,companyName);
	}
}
