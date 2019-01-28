package com.ambition.supplier.archives.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.archives.dao.SupplierAllDao;
import com.ambition.supplier.entity.SupplierAll;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

@Service
@Transactional
public class SupplierAllManager {
	@Autowired
	private SupplierAllDao supplierAllDao;
	
	@Autowired
	private LogUtilDao logUtilDao;
		
	@Autowired
	private UseFileManager useFileManager;
	/**
	 * 根据供应商名称查询供应商
	 * @param supplierName
	 * @return
	 */
	public SupplierAll getSupplierByName(String supplierName){
		return supplierAllDao.getSupplierByName(supplierName);
	}
	

	/**
	 * 检查是否存在相同名称的供应商
	 * @param id
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Boolean isExistSupplierAll(Long id,String sourceUnit,String code){
		String hql = "select count(*) from SupplierAll s where s.companyId = ?  and s.getSourceUnit = ? and s.code=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(sourceUnit);
		params.add(code);
		if(id != null ){
			hql += " and s.id <> ?";
			params.add(id);
		}
		Query query = supplierAllDao.getSession().createQuery(hql);
		for(int i=0;i<params.size();i++){
			query.setParameter(i,params.get(i));
		}
		List list = query.list();
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}

	public String emailFormat(String email) {  
		if(email.indexOf("；")!=-1){
			email=email.replaceAll("；", ";");
		} 
		if(email.indexOf(",")!=-1){
			email=email.replaceAll(",", ";");
		}
		if(email.indexOf("，")!=-1){
			email=email.replaceAll("，", ";");
		}
	       return email;
	 } 	
	
	public boolean emailFormats(String email) {  
		if(email.indexOf(";")!=-1){
			String[] emails=email.split(";");
			if(isEmail(emails[0])){
				return true;
			}
		}else if(isEmail(email)){
			 return true;
		}
	       return false;
	 }
	
	public boolean isEmail(String email) {  
	     String regexEmail1="^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
	     if(Pattern.matches(regexEmail1, email)){
	    	 return Pattern.matches(regexEmail1, email); 
	     }
		 return false;
	 } 
	
	public SupplierAll getSupplierAll(Long id){
		return supplierAllDao.get(id);
	}
	
	public SupplierAll getSupplierAll(String supplierCode){
		String hql = "from SupplierAll s where  s.code = ?";
		List<SupplierAll> suppliers = supplierAllDao.find(hql,supplierCode);
		if(suppliers.isEmpty()){
			return null;
		}else{
			return suppliers.get(0);
		}
	}	
	/**
	 * 保存供应商
	 * @param supplier
	 * @throws Exception 
	 */
	public void saveSupplierAll(SupplierAll supplier,String type) throws Exception{
		if(StringUtils.isEmpty(supplier.getCode())){
			throw new RuntimeException("编码不能为空!");
		}
		if(StringUtils.isEmpty(supplier.getName())){
			throw new RuntimeException("名称不能为空!");
		}
		if("U".equals(type)){
			if(isExistSupplierAll(supplier.getId(),supplier.getSourceUnit(),supplier.getCode())){
				throw new RuntimeException("已存在相同的编码或名称!");
			}
		}
		supplier.setCode(supplier.getCode().trim());
		supplierAllDao.save(supplier);
	}
	
	/**
	 * 查询供应商
	 * @param page
	 * @return
	 */
	public Page<SupplierAll> searchByPage(Page<SupplierAll> page){
		StringBuffer sb = new StringBuffer("from SupplierAll s where s.companyId = ? and s.deleted=? ");
		com.norteksoft.product.api.entity.Department d = ApiFactory.getAcsService().getDepartmentById(ContextUtils.getDepartmentId());
		if("供应商".equals(d.getName())){
			sb.append(" and s.code=" + ContextUtils.getLoginName());
		}
		return supplierAllDao.searchPageByHql(page,sb.toString(),ContextUtils.getCompanyId(),0);
	}	
	
	/**
	 * 删除供应商
	 * @param deleteIds
	 */
	public void deleteSupplierAll(String deleteIds){
		String[] ids = deleteIds.split(",");
		for(String id : ids){
			SupplierAll supplier = supplierAllDao.get(Long.valueOf(id));
			if(supplier != null){
				logUtilDao.debugLog("删除", supplier.toString());
				supplierAllDao.delete(supplier);
			}
		}
	}
	
}
