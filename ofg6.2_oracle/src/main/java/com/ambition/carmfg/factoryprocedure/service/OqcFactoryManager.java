package com.ambition.carmfg.factoryprocedure.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.OqcFactory;
import com.ambition.carmfg.factoryprocedure.dao.OqcFactoryDao;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class OqcFactoryManager {
	@Autowired
	private OqcFactoryDao oqcFactoryDao;	
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	
	//获取记录
	public OqcFactory getOqcFactory(Long id){
		return oqcFactoryDao.get(id);
	}
	//验证并保存记录
	public boolean isExistOqcFactory(Long id,  String factory,String businessUnit){
		String hql = "select count(*) from OqcFactory d where d.companyId =?  and  d.factory = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());		
		params.add(factory);
		if(id != null){
			hql += " and d.id <> ?";
			params.add(id);
		}
		Query query = oqcFactoryDao.getSession().createQuery(hql);
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
	public void saveOqcFactory(OqcFactory oqcFactory){
		if(StringUtils.isEmpty(oqcFactory.getFactory())){
			throw new RuntimeException("工厂不能为空!");
		}
		if(isExistOqcFactory(oqcFactory.getId(), oqcFactory.getFactory(),oqcFactory.getBusinessUnitName())){
			throw new RuntimeException("已存在相同的工厂!");
		}
		oqcFactoryDao.save(oqcFactory);
	}
	public void saveExcelOqcFactory(OqcFactory oqcFactory){
		oqcFactoryDao.save(oqcFactory);
	}
	
	//删除记录
	public String deleteOqcFactory(String ids){
		String[] deleteIds = ids.split(",");
		StringBuilder sb = new StringBuilder("");
		for(String id : deleteIds){
			if(oqcFactoryDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", oqcFactoryDao.get(Long.valueOf(id)).toString());
			}
			oqcFactoryDao.delete(Long.valueOf(id));
			sb.append(oqcFactoryDao.get(Long.valueOf(id)).getFactory()+ ",");
		}
		return sb.toString();
	}
	
	//删除对象
	public void deleteOqcFactory(OqcFactory oqcFactory){
		logUtilDao.debugLog("删除", oqcFactory.toString());
		oqcFactoryDao.delete(oqcFactory);
	}
	
	//返回页面对象列表
	public Page<OqcFactory> list(Page<OqcFactory> page){
		return oqcFactoryDao.list(page);
	}
	
	//返回所有对象列表
	public List<OqcFactory> listAll(){
		return oqcFactoryDao.getAllOqcFactory();
	}
	public OqcFactory getOqcFactoryByCode(String code){
		return oqcFactoryDao.getOqcFactoryByCode(code);
	}
	public List<OqcFactory> getOqcFactoryByBusinessUnit(String businessUnit){
		return oqcFactoryDao.getOqcFactoryByBusinessUnit(businessUnit);
	}		
	/**
	 * 获取所有的工厂
	 * @return
	 */
	public List<Option> listAllForOptions(){
		List<OqcFactory> oqcFactorys = listAll();
		List<Option> options = new ArrayList<Option>();
		for(OqcFactory oqcFactory : oqcFactorys){
			Option option = new Option();
			option.setName(oqcFactory.getFactory());
			option.setValue(oqcFactory.getFactory());
			options.add(option);
		}
		return options;
	}
}
