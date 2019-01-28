package com.ambition.improve.exceptionreport.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.checkinspection.service.MfgCheckInspectionReportManager;
import com.ambition.carmfg.entity.MfgCheckInspectionReport;
import com.ambition.improve.entity.QualityExceptionReport;
import com.ambition.improve.exceptionreport.dao.QualityExceptionReportDao;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 类名:品质异常联络单Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2017年4月22日 发布
 */
@Service
@Transactional
public class QualityExceptionReportManager extends AmbWorkflowManagerBase<QualityExceptionReport>{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private QualityExceptionReportDao qualityExceptionReportDao;
	@Autowired
	private MfgCheckInspectionReportManager mfgCheckInspectionReportManager;
	@Override
	public Class<QualityExceptionReport> getEntityInstanceClass() {
		return QualityExceptionReport.class;
	}

	@Override
	public String getEntityListCode() {
		return QualityExceptionReport.ENTITY_LIST_CODE;
	}

	@Override
	public HibernateDao<QualityExceptionReport, Long> getHibernateDao() {
		return qualityExceptionReportDao;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "exception-report";
	}

	@Override
	public String getWorkflowDefinitionName() {
		return "品质异常联络单";
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "exception-report.xlsx", QualityExceptionReport.ENTITY_LIST_NAME);
	}
	
	/**
	 * 删除实体，流程相关文件都删除
	 * @param ids
	 */
	public String deleteEntity(String ids) {
		String[] deleteIds = ids.split(",");
		 Integer deleteNum = 0;
	     Integer dontDeleteNum = 0;
	     String message = "";
		StringBuilder sb = new StringBuilder("");
		for (String id : deleteIds) {
			QualityExceptionReport report = getEntity(Long.valueOf(id));
				deleteEntity(report);
				sb.append(report.getFormNo() + ",");
				deleteNum++;			
		}
		message =sb.toString()+"~"+ deleteNum+"条已删除，"+dontDeleteNum+"条无权限删除";
		return   message;
	}

	public Page<QualityExceptionReport> searchSharePage(
			Page<QualityExceptionReport> page) {
		return qualityExceptionReportDao.searchSharePage(page);
	}
	public Page<QualityExceptionReport> searchPage(
			Page<QualityExceptionReport> page) {
		return qualityExceptionReportDao.searchPage(page);
	}
	public String cancle(String deleteIds){
		String[] ids = deleteIds.split(",");
		StringBuilder sb = new StringBuilder("");
		for(String id: ids){
			QualityExceptionReport report = qualityExceptionReportDao.get(Long.valueOf(id));
			if(report != null){
				report.setIsShare("否");
			}
		}
		return sb.toString();
	}

	public Page<QualityExceptionReport> searchHide(Page<QualityExceptionReport>page){
		return qualityExceptionReportDao.searchHide(page);		
	}

	public String hiddenState(String hideId,String type){
		StringBuilder sb = new StringBuilder("");
		String[] ids = hideId.split(",");
		for(String id : ids){
			QualityExceptionReport qualityExceptionReport = qualityExceptionReportDao.get(Long.valueOf(id));
			if("Y".equals(type)){
				qualityExceptionReport.setHiddenState("N");
			}else{
				qualityExceptionReport.setHiddenState("Y");
			}
			qualityExceptionReportDao.save(qualityExceptionReport);
			sb.append(qualityExceptionReport.getFormNo() + ",");
		}
		return sb.toString();
	} 

	/**
	  * 方法名: 保存对象
	  * <p>功能说明：</p>
	  * @param report
	  * @param childMaps 子表对象
	  * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 */
	public void saveEntity(QualityExceptionReport report,Map<String,List<JSONObject>> childMaps){
		//数据处理
		if(report.getExceptionDescrible()!=null){
			report.setExceptionDescrible(report.getExceptionDescrible().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getExceptionDescribleConfirm()!=null){
			report.setExceptionDescribleConfirm(report.getExceptionDescribleConfirm().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getEmergencyMeasures()!=null){
			report.setEmergencyMeasures(report.getEmergencyMeasures().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getReasonAnalysis()!=null){
			report.setReasonAnalysis(report.getReasonAnalysis().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getImprovementMeasures()!=null){
			report.setImprovementMeasures(report.getImprovementMeasures().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getEffectConfirm()!=null){
			report.setEffectConfirm(report.getEffectConfirm().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getShortMeasures()!=null){
			report.setShortMeasures(report.getShortMeasures().replaceAll("", "").replaceAll("", ""));
		}
		report.setModifiedTime(new Date());
		report.setModifier(ContextUtils.getLoginName());
		report.setModifierName(ContextUtils.getUserName());
		getHibernateDao().save(report);
		//设置子表的值
		setChildItems(report,childMaps);
		
	}	
	
	
	/**
	 * 删除实体，流程相关文件都删除
	 * @param entity 删除的对象
	 */
	public void deleteEntity(QualityExceptionReport entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			ApiFactory.getInstanceService().deleteInstance(entity);
		}else{
			getHibernateDao().delete(entity);
		}
		//删掉对应IQC报告中的编号
		if(entity.getMfgReportId()!=null){
			MfgCheckInspectionReport report=mfgCheckInspectionReportManager.getMfgCheckInspectionReport(Long.valueOf(entity.getMfgReportId()));
			report.setExceptionId("");
			report.setExceptionNo("");
		}
		
	}

	public boolean managerNoSelect(String formId, String managerNo) {
		String hql = "select count(*) from QualityExceptionReport d where d.companyId =?  and d.managerNo = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(managerNo);
		if(formId!=null&&!"".equals(formId)){
			hql+=" and d.id<> ? ";
			params.add(Long.valueOf(formId));
		}
		Query query = qualityExceptionReportDao.getSession().createQuery(hql);
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
	
}
