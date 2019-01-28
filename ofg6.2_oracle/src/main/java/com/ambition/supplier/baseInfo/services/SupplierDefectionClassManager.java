package com.ambition.supplier.baseInfo.services;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.baseInfo.dao.SupplierDefectionClassDao;
import com.ambition.supplier.entity.SupplierDefectionClass;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class SupplierDefectionClassManager {
	@Autowired
	private SupplierDefectionClassDao defectionTypeDao;	
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	
	//获取记录
	public SupplierDefectionClass getSupplierDefectionClass(Long id){
		return defectionTypeDao.get(id);
	}
	//验证并保存记录
	public boolean isExistSupplierDefectionClass(Long id, String name,String businessUnit){
		String hql = "select count(*) from SupplierDefectionClass d where d.companyId =? and d.businessUnitName=? and d.productType = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(businessUnit);
		params.add(name);
		if(id != null){
			hql += " and d.id <> ?";
			params.add(id);
		}
		Query query = defectionTypeDao.getSession().createQuery(hql);
		for(int i = 0;i < params.size(); i++){
			query.setParameter(i, params.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if(Integer.valueOf(list.get(0).toString()) > 0){
			return true;
		}else{
			return false;
		}		
	}
	public void saveSupplierDefectionClass(SupplierDefectionClass defectionType){
		if(StringUtils.isEmpty(defectionType.getProductType())){
			throw new RuntimeException("不良类别不能为空!");
		}
		if(isExistSupplierDefectionClass(defectionType.getId(), defectionType.getSupplierDefectionClass(),defectionType.getBusinessUnitName())){
			throw new RuntimeException("该厂区已存在相同物料类别!");
		}
		defectionTypeDao.save(defectionType);
	}
	public void saveExcelSupplierDefectionClass(SupplierDefectionClass defectionType){
		defectionTypeDao.save(defectionType);
	}
	
	//删除记录
	public String deleteSupplierDefectionClass(String ids){
		StringBuilder sb = new StringBuilder("");
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(defectionTypeDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", defectionTypeDao.get(Long.valueOf(id)).toString());
			}
			defectionTypeDao.delete(Long.valueOf(id));
			sb.append(defectionTypeDao.get(Long.valueOf(id)).getProductType() + ",");
		}
		return sb.toString();
	}
	
	//删除对象
	public void deleteSupplierDefectionClass(SupplierDefectionClass defectionType){
		logUtilDao.debugLog("删除", defectionType.toString());
		defectionTypeDao.delete(defectionType);
	}
	
	//返回页面对象列表
	public Page<SupplierDefectionClass> list(Page<SupplierDefectionClass> page,String businessUnit){
		return defectionTypeDao.list(page,businessUnit);
	}
	
	//返回所有对象列表
	public List<SupplierDefectionClass> listAll(){
		return defectionTypeDao.getAllSupplierDefectionClass();
	}
	public SupplierDefectionClass getSupplierDefectionClassByCode(String code){
		return defectionTypeDao.getSupplierDefectionClassByCode(code);
	}
	public List<SupplierDefectionClass> getSupplierDefectionClassByBusinessUnit(String businessUnit){
		return defectionTypeDao.getSupplierDefectionClassByBusinessUnit(businessUnit);
	}
	public List<SupplierDefectionClass> getSupplierDefectionClassByBusinessUnit(String businessUnit,String productType){
		return defectionTypeDao.getSupplierDefectionClassByBusinessUnit(businessUnit,productType);
	}
	/**
	 * 获取所有的不良类别
	 * @return
	 */
	public List<Option> listAllForOptions(){
		List<SupplierDefectionClass> defectionTypes = listAll();
		List<Option> options = new ArrayList<Option>();
		for(SupplierDefectionClass defectionType : defectionTypes){
			Option option = new Option();
			option.setName(defectionType.getSupplierDefectionClass());
			option.setValue(defectionType.getSupplierDefectionClass());
			options.add(option);
		}
		return options;
	}
}
