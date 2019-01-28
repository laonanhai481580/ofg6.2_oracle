package com.ambition.supplier.baseInfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierDefectionClass;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class SupplierDefectionClassDao extends HibernateDao<SupplierDefectionClass,Long>{
	
	public Page<SupplierDefectionClass> list(Page<SupplierDefectionClass> page,String businessUnit){
		return searchPageByHql(page,"from SupplierDefectionClass d where d.companyId=? and d.businessUnitName=? ",ContextUtils.getCompanyId(),businessUnit);
	}
	public List<SupplierDefectionClass> getAllSupplierDefectionClass(){
		return find("from SupplierDefectionClass d where d.companyId=?",ContextUtils.getCompanyId());
	}
	public List<SupplierDefectionClass> getSupplierDefectionClassByBusinessUnit(String businessUnit){
		return find("from SupplierDefectionClass d where d.companyId = ? and d.businessUnitName = ? ", ContextUtils.getCompanyId(),businessUnit);
	}
	public List<SupplierDefectionClass> getSupplierDefectionClassByBusinessUnit(String businessUnit,String productType){
		return find("from SupplierDefectionClass d where d.companyId = ? and d.businessUnitName = ? and d.productType=? ", ContextUtils.getCompanyId(),businessUnit,productType);
	}
	public String getSupplierDefectionClassNameById(Long id) {
		String hql = "from SupplierDefectionClass d where d.id = ?";
		List<SupplierDefectionClass> list = this.find(hql,id);
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0).getSupplierDefectionClass();
		}		
	}
	public SupplierDefectionClass getSupplierDefectionClassByCode(String code) {
		String hql = "from SupplierDefectionClass d where d.supplierDefectionTypeNo = ?";
		List<SupplierDefectionClass> list = this.find(hql,code);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}		
	}
}
