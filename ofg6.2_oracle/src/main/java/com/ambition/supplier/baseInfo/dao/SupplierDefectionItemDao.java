package com.ambition.supplier.baseInfo.dao;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.ambition.supplier.entity.SupplierDefectionClass;
import com.ambition.supplier.entity.SupplierDefectionItem;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Repository
public class SupplierDefectionItemDao extends HibernateDao<SupplierDefectionItem,Long>{
	
	public Page<SupplierDefectionItem> list(Page<SupplierDefectionItem> page,SupplierDefectionClass defectionClass){
		if(defectionClass != null){
			return searchPageByHql(page,"from SupplierDefectionItem d where d.companyId = ? and d.supplierDefectionClass = ?",new Object[]{ContextUtils.getCompanyId(),defectionClass});
		}else{
			return searchPageByHql(page,"from SupplierDefectionItem d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public Page<SupplierDefectionItem> listByParent(Page<SupplierDefectionItem> page,Long parentId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from SupplierDefectionItem d where d.companyId = ?");
		params.add(ContextUtils.getCompanyId());
		String customSearch = Struts2Utils.getParameter("customSearch");
		if(customSearch != null){
			hql.append(" and (d.supplierDefectionItemName like ? or d.supplierDefectionItemNo like ?)");
			params.add("%" + customSearch.trim() + "%");
			params.add("%" + customSearch.trim() + "%");
		}else if(parentId != null){
			hql.append(" and d.defectionClass.id = ?");
			params.add(parentId);
		}
		hql.append(" order by d.supplierDefectionItemNo desc");
		return searchPageByHql(page, hql.toString(), params.toArray());
	}
	public Page<SupplierDefectionItem> getCodeByParams(Page<SupplierDefectionItem> page,JSONObject params){
		StringBuffer hql = new StringBuffer("from SupplierDefectionItem d where d.companyId=?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(params != null){
			for(Object pro : params.keySet()){
				hql.append(" and d." + pro + " like ?");
				searchParams.add("%" + params.getString(pro.toString()) + "%");
			}
		}
		hql.append(" order by d.supplierDefectionItemNo");
		return searchPageByHql(page,hql.toString(),searchParams.toArray());
	}
	public Page<SupplierDefectionItem> list(Page<SupplierDefectionItem> page, String code){
		if(code != null){
			return searchPageByHql(page,"from SupplierDefectionItem d where d.companyId = ? and d.supplierDefectionItemNo like ?",new Object[]{ContextUtils.getCompanyId(),"%"+code+"%"});
		}else{
			return searchPageByHql(page,"from SupplierDefectionItem d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<SupplierDefectionItem> getAllSupplierDefectionItems(SupplierDefectionClass defectionClass){
		if(defectionClass != null){
			return find("from SupplierDefectionItem d where d.companyId = ? and d.supplierDefectionClass = ?",new Object[]{ContextUtils.getCompanyId(),defectionClass});
		}else{
			return find("from SupplierDefectionItem d where d.companyId = ?",new Object[]{ContextUtils.getCompanyId()});
		}
	}
	public List<SupplierDefectionItem> getSupplierDefectionItem(String code) {
		return find("from SupplierDefectionItem d where d.supplierDefectionItemNo = ?",new Object[]{code});
	}
	public String getSupplierDefectionItemNameByCode(String code){
		String hql = "from SupplierDefectionItem d where d.supplierDefectionItemNo = ?";
		List<SupplierDefectionItem> defectionItems = this.find(hql,code);
		if(defectionItems.isEmpty()){
			return null;
		}else{
			return defectionItems.get(0).getSupplierDefectionItemName();
		}
	}
	public  SupplierDefectionItem getSupplierDefectionByCode(String code) {
		String hql = "from SupplierDefectionItem d where d.supplierDefectionItemNo = ?";
		List<SupplierDefectionItem> defectionItems = this.find(hql,code);
		if(defectionItems.size()>0){
			return defectionItems.get(0);
		}else{
			return null;
		}
	}
}
