package com.ambition.iqc.fctorycode.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.bom.dao.ProductBomDao;
import com.ambition.iqc.entity.IqcFactoryCode;
import com.ambition.iqc.fctorycode.dao.IqcFactoryCodeDao;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

@Service
@Transactional
public class IqcFactoryCodeManager {
	@Autowired
	private IqcFactoryCodeDao iqcFactoryCodeDao;	
	@Autowired
	private ProductBomDao productBomDao;
	public IqcFactoryCode getIqcFactoryCode(Long id){
		return iqcFactoryCodeDao.get(id);
	}
	
	public void deleteIqcFactoryCode(IqcFactoryCode iqcFactoryCode){
		iqcFactoryCodeDao.delete(iqcFactoryCode);
	}

	public Page<IqcFactoryCode> search(Page<IqcFactoryCode>page){
		return iqcFactoryCodeDao.search(page);
	}

	public List<IqcFactoryCode> listAll(){
		return iqcFactoryCodeDao.getAllIqcFactoryCode();
	}
		
	public void deleteIqcFactoryCode(Long id){
		iqcFactoryCodeDao.delete(id);
	}
	public void deleteIqcFactoryCode(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			IqcFactoryCode  iqcFactoryCode = iqcFactoryCodeDao.get(Long.valueOf(id));
			if(iqcFactoryCode.getId() != null){
				iqcFactoryCodeDao.delete(iqcFactoryCode);
			}
		}
	}
	//验证并保存记录
	public boolean isExistIqcFactoryCode(Long id, String code){
		String hql = "select count(*) from IqcFactoryCode d where d.companyId =? and d.organizationCode=?   ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());		
		params.add(code);
		if(id != null){
			hql += " and d.id <> ?";
			params.add(id);
		}
		Query query = iqcFactoryCodeDao.getSession().createQuery(hql);
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
	public void saveIqcFactoryCode(IqcFactoryCode iqcFactoryCode){
		if(isExistIqcFactoryCode(iqcFactoryCode.getId(), iqcFactoryCode.getOrganizationCode())){
			throw new RuntimeException("组织代码"+ iqcFactoryCode.getOrganizationCode()+"已存在!");
		}
		iqcFactoryCodeDao.save(iqcFactoryCode);
	}
	public IqcFactoryCode getIqcFactoryCode(String code){
		return iqcFactoryCodeDao.getIqcFactoryCode(code);
	}
	/**
	  * 方法名:获取字段映射 
	  * <p>功能说明：</p>
	  * @return
	 */
	public Map<String,String> getFieldMap(String listCode){
		Map<String,String> fieldMap = new HashMap<String, String>();
		ListView columns = ApiFactory.getMmsService().getListViewByCode(listCode);
		for(ListColumn column: columns.getColumns()){
			if(column.getVisible()){
				fieldMap.put(column.getHeaderName(), column.getTableColumn().getName());
			}
		}
		return fieldMap;
	}	
	
}
