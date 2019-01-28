package com.ambition.gsm.inspectionplan.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.ambition.gsm.entity.GsmEquipment;
import com.ambition.gsm.entity.InspectionPlan;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 校准计划(DAO)
 * @author 张顺治
 *
 */
@Repository
public class InspectionPlanDao extends HibernateDao<InspectionPlan, Long> {
	public Page<InspectionPlan> list(Page<InspectionPlan> page){
		return searchPageByHql(page, "select distinct o from InspectionPlan o left outer join o.inspectionRecords inspectionRecords where o.companyId=?",ContextUtils.getCompanyId());
	}
	/**
	 * 分页对象
	 * @param page
	 * @param inspectionState
	 * @param operator
	 * @return
	 */
	public Page<InspectionPlan> getPage(Page<InspectionPlan> page,String inspectionState,String operator, String str){
		StringBuilder sbHql = new StringBuilder("from InspectionPlan i where i.companyId = ? and gsmState='N' ");
		List<Object> searchParames = new ArrayList<Object>();
		searchParames.add(ContextUtils.getCompanyId());
		if(str!=null){
			sbHql.append(" and i.factoryClassify =?");
			searchParames.add(str);
		}
		if(StringUtils.isNotEmpty(inspectionState) && StringUtils.isNotEmpty(operator)){
			if("IN".equals(operator.toUpperCase())){
				sbHql.append(" and i.inspectionState in('"+(inspectionState.contains(",")?inspectionState.replaceAll(",", "','"):inspectionState)+"') ");
			}else if("NOTIN".equals(operator.toUpperCase())){
				sbHql.append(" and i.inspectionState not in('"+(inspectionState.contains(",")?inspectionState.replaceAll(",", "','"):inspectionState)+"') ");
			}
		}
		return searchPageByHql(page, sbHql.toString(), searchParames.toArray());
	}
	
	public List<InspectionPlan> getImportInspectionPlan(Long id){
		return find("from InspectionPlan t where t.equipmentId = ? and t.companyId = ?", id,ContextUtils.getCompanyId());
	}
	public List<InspectionPlan> getInspectionPlanByGsm(GsmEquipment gsmEquipment){
		String a = InspectionPlan.STATE_CHECk_IN;
		String b = InspectionPlan.STATE_CHECk_WAIT;
		return find("from InspectionPlan t where t.managerAssets=? and (t.inspectionState=? or t.inspectionState=?) and t.companyId = ?",gsmEquipment.getManagerAssets(),a,b,ContextUtils.getCompanyId());
	}
	public List<InspectionPlan> getInspectionPlanByMan(String equipmentName){
		return find("from InspectionPlan t where t.equipmentName = ? and t.companyId = ?", equipmentName,ContextUtils.getCompanyId());
	}
	public List<InspectionPlan> getInspectionPlanByMeasurementNo(String measurementNo){
		return find("from InspectionPlan t where t.companyId=? and t.gsmEquipment.measurementNo=?", ContextUtils.getCompanyId(),measurementNo);
	}
}
