package com.ambition.gsm.equipment.dao;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.GsmEquipment;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 量检具管理(DAO)
 * @author 张顺治
 *
 */
@Repository
public class GsmEquipmentDao extends HibernateDao<GsmEquipment, Long> {
	public Page<GsmEquipment> getPageByGsmEquipmentIds(Page<GsmEquipment> page, String ids){
		String hql=" from GsmEquipment ge where ge.companyId=? and ge.id in("+ids+")";
		return this.searchPageByHql(page, hql.toString(),new Object[]{ContextUtils.getCompanyId()});
	}
	public Page<GsmEquipment> getPageOfInventory(Page<GsmEquipment> page,String subName){
		String hql=" from GsmEquipment ge where ge.companyId=? ";
		if(subName!=null){
			hql += "and ge.factoryClassify=?";
			return this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(),subName);
		}else{
			return this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId());
		}
	}
	public Page<GsmEquipment> getStockActual(Page<GsmEquipment> page,String subName,String name){
		String hql=" from GsmEquipment ge where ge.companyId=? and ge.dutyLoginMan=?";
		if(subName!=null){
			hql += "and ge.factoryClassify=?";
			return this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(),name,subName);
		}else{
			return this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(),name);
		}
	}
	public Page<GsmEquipment> getPageOfTransferConfirm(Page<GsmEquipment> page){
		String hql=" from GsmEquipment ge where ge.companyId=? and ge.transferState=? and ge.goalDutyLoginMan=? ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(GsmEquipment.STATE_TRANSFER_CONFIRM);
		searchParams.add(ContextUtils.getLoginName());
		return this.searchPageByHql(page, hql.toString(),searchParams.toArray());
	}
	public Page<GsmEquipment> getPageInTransfer(Page<GsmEquipment> page,String login){
		String hql=" from GsmEquipment g where g.companyId=? ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(login!=null){
			hql += "and g.transferState=? and g.dutyLoginMan=?";
			searchParams.add(GsmEquipment.STATE_TRANSFER_WAIT);
			searchParams.add(login);
		}
		return this.searchPageByHql(page, hql.toString(),searchParams.toArray());
	}
	public Page<GsmEquipment> getPageOfTransferDatas(Page<GsmEquipment> page,String login){
		String hql=" from GsmEquipment g where g.companyId=?   ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(login!=null){
			hql += "and (g.dutyLoginMan=? or g.goalDutyLoginMan=?)";
			searchParams.add(ContextUtils.getLoginName());
			searchParams.add(ContextUtils.getLoginName());
		}
		return this.searchPageByHql(page, hql.toString(),searchParams.toArray());
	}
	public List<GsmEquipment> getAllEquipment(){
		return find("from GsmEquipment d where d.companyId=?",ContextUtils.getCompanyId());
	}
	public List<GsmEquipment> getFacEquipment(String fac){
		return find("from GsmEquipment d where d.companyId=? and d.factoryClassify=?",ContextUtils.getCompanyId(),fac);
	}
	/**
	 * 根据对象获取分页对象
	 * @param page
	 * @return
	 */
	public Page<GsmEquipment> getPageByGsmEquipment(Page<GsmEquipment> page, GsmEquipment gsmEquipment,String processSection){
		StringBuilder sbHql = new StringBuilder("from GsmEquipment g where g.companyId= ?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(gsmEquipment != null){
			if(processSection!=null){
				sbHql.append(" and g.factoryClassify = ? ");
				searchParams.add(processSection);
			}
//			if(StringUtils.isNotEmpty(gsmEquipment.getMeasurementNo())){
//				sbHql.append(" and g.measurementNo = ? ");
//				searchParams.add(gsmEquipment.getMeasurementNo());
//			}
//			if(StringUtils.isNotEmpty(gsmEquipment.getMeasurementSerialNo())){
//				sbHql.append(" and g.measurementSerialNo = ? ");
//				searchParams.add(gsmEquipment.getMeasurementSerialNo());
//			}
//			if(StringUtils.isNotEmpty(gsmEquipment.getUseDept())){
//				sbHql.append(" and g.useDept = ? ");
//				searchParams.add(gsmEquipment.getUseDept());
//			}
//			if(StringUtils.isNotEmpty(gsmEquipment.getMeasurementState())){
//				sbHql.append(" and g.measurementState = ? ");
//				searchParams.add(gsmEquipment.getMeasurementState());
//			}
//			if(StringUtils.isNotEmpty(gsmEquipment.getState())){
//				sbHql.append(" and g.state = ? ");
//				searchParams.add(gsmEquipment.getState());
//			}
//			if(StringUtils.isNotEmpty(gsmEquipment.getMeasurementType())){
//				sbHql.append(" and g.measurementType = ? ");
//				searchParams.add(gsmEquipment.getMeasurementType());
//			}
		}
		return this.searchPageByHql(page, sbHql.toString(), searchParams.toArray());
	}

	/**
	 * 根据对象获取分页对象
	 * @param page
	 * @return
	 */
	public Page<GsmEquipment> getPageByGsmEquipmentSelect(Page<GsmEquipment> page){
		StringBuilder sbHql = new StringBuilder("from GsmEquipment g where g.companyId= ? and g.measurementState not in ('报废','停用','遗失','维修','封存') ");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		return this.searchPageByHql(page, sbHql.toString(), searchParams.toArray());
	}	

	/**
	 * 根据对象获取分页对象
	 * @param page
	 * @return
	 */
	public Page<GsmEquipment> getPageByGsmEquipmentSelectAll(Page<GsmEquipment> page){
		StringBuilder sbHql = new StringBuilder("from GsmEquipment g where g.companyId= ?  ");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		return this.searchPageByHql(page, sbHql.toString(), searchParams.toArray());
	}
	
	/**
	 * 根据对象获取集合对象
	 * @param gsmEquipment
	 * @return
	 */
	public List<GsmEquipment> getListByGsmEquipment(GsmEquipment gsmEquipment){
		StringBuilder sbHql = new StringBuilder("from GsmEquipment g where g.companyId= ?");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(gsmEquipment != null){
//			if(StringUtils.isNotEmpty(gsmEquipment.getMeasurementNo())){
//				sbHql.append(" and g.measurementNo = ? ");
//				searchParams.add(gsmEquipment.getMeasurementNo());
//			}
//			if(StringUtils.isNotEmpty(gsmEquipment.getMeasurementSerialNo())){
//				sbHql.append(" and g.measurementSerialNo = ? ");
//				searchParams.add(gsmEquipment.getMeasurementSerialNo());
//			}
//			if(StringUtils.isNotEmpty(gsmEquipment.getUseDept())){
//				sbHql.append(" and g.useDept = ? ");
//				searchParams.add(gsmEquipment.getUseDept());
//			}
//			if(StringUtils.isNotEmpty(gsmEquipment.getMeasurementState())){
//				sbHql.append(" and g.measurementState = ? ");
//				searchParams.add(gsmEquipment.getMeasurementState());
//			}
//			if(StringUtils.isNotEmpty(gsmEquipment.getState())){
//				sbHql.append(" and g.state = ? ");
//				searchParams.add(gsmEquipment.getState());
//			}
		}
		return this.find(sbHql.toString(), searchParams.toArray());
	}
	
	/**
	 * 根据计量编号规则一级和二级检索最大的序列号
	 * @param measurementType
	 * @return
	 */
	public Integer getMaxSerialNoByGsmCodeRules(String gsmCodeRules){
		String hql = "select max(i.serialNo) from GsmEquipment i where i.companyId = ? and i.gsmCodeRules = ?";
		List<Integer> list = this.find(hql,ContextUtils.getCompanyId(),gsmCodeRules);
		return list != null && list.size() > 0 && list.get(0) != null?list.get(0):0;
	}
	
	/**
	 * 根据对象获取分页对象
	 * @param page
	 * @return
	 */
	public Page<GsmEquipment> getPageByUseDept(Page<GsmEquipment> page, String useDept, String str){
		/*StringBuilder sbHql = new StringBuilder("from GsmEquipment g where g.companyId= ? and (g.measurementState = ? or g.measurementState = ? or g.measurementState = ?)");*/
		StringBuilder sbHql = new StringBuilder("from GsmEquipment g where g.companyId= ? ");
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(str!=null){
			sbHql.append(" and g.factoryClassify = ?");
			searchParams.add(str);
		}
		
		/*searchParams.add(GsmEquipment.STATE_DEFAULT_INSTOCK);
		searchParams.add(GsmEquipment.STATE_INUSE);
		searchParams.add(GsmEquipment.STATE_BORROW);*/
		if(useDept != null&&useDept !=""){
			sbHql.append(" and g.devName = ? ");
			searchParams.add(useDept);
		}
		return this.searchPageByHql(page, sbHql.toString(), searchParams.toArray());
	}
	public List<GsmEquipment> getGsmEquipmentByCode(String managerAssets) {
		return find("select i from GsmEquipment i where i.managerAssets=? ",managerAssets);
	}
}
