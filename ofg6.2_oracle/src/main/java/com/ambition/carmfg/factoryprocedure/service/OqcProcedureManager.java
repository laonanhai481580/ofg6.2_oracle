package com.ambition.carmfg.factoryprocedure.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.entity.CustomerPlace;
import com.ambition.carmfg.entity.OqcFactory;
import com.ambition.carmfg.entity.OqcProcedure;
import com.ambition.carmfg.factoryprocedure.dao.OqcProcedureDao;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class OqcProcedureManager {
	@Autowired
	private OqcProcedureDao oqcProcedureDao;
	@Autowired
	private OqcFactoryManager oqcFactoryManager;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	//获取记录
	public OqcProcedure getOqcProcedure(Long id){
		return oqcProcedureDao.get(id);
	}
	
	public OqcProcedure getOqcProcedure(String procedure){
		return oqcProcedureDao.getOqcProcedure(procedure);
	}
	
	//验证并保存记录
	private boolean isExistOqcProcedure(Long id, String procedure, OqcFactory oqcFactory) {
		String hql = "select count(*) from OqcProcedure d where d.companyId = ? and d.oqcFactory = ? and d.procedure =?  ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(oqcFactory);
		params.add(procedure);
		if(id != null){
			hql += "and d.id <> ?";
			params.add(id);
		}
		Query query = oqcProcedureDao.getSession().createQuery(hql);
		for(int i = 0;i < params.size();i++){
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
	public void saveOqcProcedure(OqcProcedure oqcProcedure){
		if(StringUtils.isEmpty(oqcProcedure.getProcedure())){
			throw new RuntimeException("工序不能为空!");
		}
		if(isExistOqcProcedure(oqcProcedure.getId(),oqcProcedure.getProcedure(),oqcProcedure.getOqcFactory())){
			throw new RuntimeException("已存在相同的不良代码或名称!");
		}
		oqcProcedureDao.save(oqcProcedure);
	}	
	public void saveExcelOqcProcedure(OqcProcedure oqcProcedure){
		oqcProcedureDao.save(oqcProcedure);
	}	
	//删除记录
	public String deleteOqcProcedure(String ids){
		String[] deleteIds = ids.split(",");
		StringBuilder sb = new StringBuilder("");
		for(String id : deleteIds){
			if(oqcProcedureDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", oqcProcedureDao.get(Long.valueOf(id)).toString());
			}
			sb.append(oqcProcedureDao.get(Long.valueOf(id)).getProcedure()+ ",");
			oqcProcedureDao.delete(Long.valueOf(id));
		}
		return sb.toString();
	}
	//删除对象
	public void deleteOqcProcedure(OqcProcedure oqcProcedure){
		logUtilDao.debugLog("删除", oqcProcedure.toString());
		oqcProcedureDao.delete(oqcProcedure);
	}
	
	//返回页面对象列表
	public Page<OqcProcedure> list(Page<OqcProcedure> page,OqcFactory oqcFactory){
		return oqcProcedureDao.list(page, oqcFactory);
	}	
	
	public Page<OqcProcedure> list(Page<OqcProcedure> page,String code){
		return oqcProcedureDao.list(page, code);
	}
	//返回所有对象列表
	public List<OqcProcedure> getAllOqcProcedure(){
		return oqcProcedureDao.getAllOqcProcedure();
	}	
	//返回所有对象列表
	public List<OqcProcedure> listAll(OqcFactory oqcFactory){
		return oqcProcedureDao.getAllOqcProcedures(oqcFactory);
	}	
	public List<OqcProcedure> listProcedure(String factory) {
		String hql = "from OqcProcedure c where  c.companyId = ?  ";
		List<OqcProcedure> customerPlaces=null;
		if(factory!=null&&!"".equals(factory)){
			hql+=" and c.oqcFactory.factory=? ";
			customerPlaces= oqcProcedureDao.find(hql,ContextUtils.getCompanyId(),factory);
		}else{
			customerPlaces= oqcProcedureDao.find(hql,ContextUtils.getCompanyId());
		}		
		return customerPlaces;
	}	
    /**
     * 方法名: 
     * <p>功能说明：</p>
     * @param calendar
     * @return
    */
	public List<Option> converOqcProcedureToList(List<OqcProcedure> list){
	   List<Option> options = new ArrayList<Option>();
	   for(OqcProcedure oqcProcedure : list){
	       Option option = new Option();
	       String name = oqcProcedure.getProcedure().toString();
	       String value = oqcProcedure.getProcedure().toString();
	       option.setName(name==null?"":name.replaceAll("\n","").replaceAll(",","，"));
	       option.setValue(value==null?"":value.replaceAll("\n","").replaceAll(",","，"));
	       options.add(option);
	   }
	   return options;
	}  
}
