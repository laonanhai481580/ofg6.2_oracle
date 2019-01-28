package com.ambition.supplier.improve.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.iqc.inspectionreport.service.IncomingInspectionActionsReportManager;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.entity.ExceptionContact;
import com.ambition.supplier.entity.ExceptionImprove;
import com.ambition.supplier.entity.SupplierDeleteMark;
import com.ambition.supplier.improve.dao.ExceptionImproveDao;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;

/**    
 * 物料异常矫正单
 * @authorBy LPF
 *
 */
@Service
@Transactional
public class ExceptionImproveManager extends AmbWorkflowManagerBase<ExceptionImprove>{
	@Autowired
	private ExceptionImproveDao exceptionImproveDao;
	@Autowired
    private FormCodeGenerated formCodeGenerated;
	@Autowired
	private IncomingInspectionActionsReportManager iiarManager;
	@Resource(name="tpdbDataJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private ExceptionContactManager exceptionContactManager;
	@Override
	public HibernateDao<ExceptionImprove, Long> getHibernateDao() {
		return exceptionImproveDao;
	}

	@Override
	public String getEntityListCode() {		
		return "SUPPLIER_EXCEPTION_IMPROVE";
	}

	@Override
	public Class<ExceptionImprove> getEntityInstanceClass() {
		return ExceptionImprove.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "supplier-exception-improve";
	}
	@Override
	public String getWorkflowDefinitionName() {
		return "物料异常矫正单流程";
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "supplier-exception-improve.xlsx", "物料异常矫正单");
	}

	public Department searchSupplierDept() {
		String hql = "from Department d where  d.deleted=? and d.name=?";
		@SuppressWarnings("unchecked")
		List<Department> depts = exceptionImproveDao.getSession()
				.createQuery(hql).setParameter(0, false).setParameter(1, "供应商").list();
		if(depts.size()==0){
			return null;
		}else{
			return depts.get(0);
		}
	}
	
	/**
	 * 删除实体，流程相关文件都删除
	 * @param ids
	 */
	public String deleteEntity(String ids) {
		String[] deleteIds = ids.split(",");
		StringBuilder sb = new StringBuilder("");
	     String message = "";
		for (String id : deleteIds) {
			ExceptionImprove report=getEntity(Long.valueOf(id));
			deleteEntity(report);
			sb.append(report.getFormNo() + ",");
		}
		message =sb.toString();
		return   message;
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
	public void saveEntity(ExceptionImprove report,Map<String,List<JSONObject>> childMaps){
		//数据处理
		if(report.getTempCountermeasures()!=null&&!"".equals(report.getTempCountermeasures())){
			report.setTempCountermeasures(report.getTempCountermeasures().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getTrueReasonCheck()!=null&&!"".equals(report.getTrueReasonCheck())){
			report.setTrueReasonCheck(report.getTrueReasonCheck().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getCountermeasures()!=null&&!"".equals(report.getCountermeasures())){
			report.setCountermeasures(report.getCountermeasures().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getPreventHappen()!=null&&!"".equals(report.getPreventHappen())){
			report.setPreventHappen(report.getPreventHappen().replaceAll("", "").replaceAll("", ""));
		}		
		report.setModifiedTime(new Date());
		report.setModifier(ContextUtils.getLoginName());
		report.setModifierName(ContextUtils.getUserName());
		exceptionImproveDao.save(report);
	    //回写结果到IQC报告
	    if(report.getInspectionId()!=null&&!"".equals(report.getInspectionId())){
	    	IncomingInspectionActionsReport iiar=iiarManager.getIncomingInspectionActionsReport(Long.valueOf(report.getInspectionId()));
	    	if(iiar!=null&&report.getRequestDate()!=null){
	    		iiar.setSqeReplyTime(report.getRequestDate());
	    	}
	    	if(iiar!=null&&report.getSqeFinishDate()!=null){
	    		iiar.setSqeCompleteTime(report.getSqeFinishDate());
	    	}
	    }	
	    //回写结果到联络单
	    if(report.getExceptionContactId()!=null&&!"".equals(report.getExceptionContactId())){
	    	ExceptionContact contact=exceptionContactManager.getEntity(Long.valueOf(report.getExceptionContactId()));
	    	if(contact!=null){
	    		contact.setExceptionImproveId(report.getId().toString());
	    		contact.setExceptionImprovetNo(report.getFormNo());
	    		contact.setModifiedTime(new Date());
			}
	    	if(contact!=null&&report.getRequestDate()!=null){
	    		contact.setTempCountermeasures(report.getTempCountermeasures());
	    		contact.setTrueReasonCheck(report.getTrueReasonCheck());
	    		contact.setCountermeasures(report.getCountermeasures());
	    		contact.setPreventHappen(report.getPreventHappen());
	    		contact.setRequestDate(report.getRequestDate());
	    		contact.setSupplierFile(report.getSupplierFile());
	    		contact.setIsSupplier(report.getIsSupplier());
	    	}
	    	if(contact!=null&&report.getSqeFinishDate()!=null){
	    		contact.setMqeApprovalerOpinion(report.getSqeApprovalerOpinion());
	    		contact.setMqeApprovalerDate(report.getSqeApprovalerDate());
	    		contact.setMqeComfirmDate(report.getSqeComfirmDate());
	    		contact.setMqeComfirmOpinion(report.getSqeComfirmOpinion());
	    		contact.setMqeComfirmDate(report.getSqeComfirmDate());
	    		contact.setCheckResult(report.getCheckResult());
	    		contact.setCheckResultDate(report.getCheckResultDate());
	    		contact.setSqeFinishDate(report.getSqeFinishDate());
	    		contact.setIsClosedAlaysis(report.getIsClosedAlaysis());
	    	}
	    }		
		//设置子表的值
		setChildItems(report,childMaps);
		
	}
	//验证并保存记录
	public boolean isExistExceptionContact(Long id){
		String hql = "select count(*) from ExceptionImprove d where d.companyId =? and d.exceptionContactId=?   ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());		
		params.add(id.toString());
		Query query = exceptionImproveDao.getSession().createQuery(hql);
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
	
	public Page<ExceptionImprove> searchAll(Page<ExceptionImprove>page){
		return exceptionImproveDao.searchAll(page);		
	}
	public Page<ExceptionImprove> searchSingle(Page<ExceptionImprove>page){
		return exceptionImproveDao.searchSingle(page);		
	}
	public Page<ExceptionImprove> searchUnSingle(Page<ExceptionImprove>page){
		return exceptionImproveDao.searchUnSingle(page);		
	}
	public Page<ExceptionImprove> search(Page<ExceptionImprove>page){
		return exceptionImproveDao.search(page);		
	}
	public Page<ExceptionImprove> searchUn(Page<ExceptionImprove>page){
		return exceptionImproveDao.search(page);		
	}
	public Page<ExceptionImprove> searchHide(Page<ExceptionImprove>page){
		return exceptionImproveDao.searchHide(page);		
	}
	public Page<ExceptionImprove> searchSupplierSingle(Page<ExceptionImprove>page){
		return exceptionImproveDao.searchSupplierSingle(page);		
	}
	public Page<ExceptionImprove> searchUnSupplierSingle(Page<ExceptionImprove>page){
		return exceptionImproveDao.searchUnSupplierSingle(page);		
	}
	public String hiddenState(String hideId,String type){
		StringBuilder sb = new StringBuilder("");
		String[] ids = hideId.split(",");
		for(String id : ids){
			ExceptionImprove exceptionImprove = exceptionImproveDao.get(Long.valueOf(id));
			if("Y".equals(type)){
				exceptionImprove.setHiddenState("N");
			}else{
				exceptionImprove.setHiddenState("Y");
			}
			exceptionImproveDao.save(exceptionImprove);
			sb.append(exceptionImprove.getFormNo() + ",");
		}
		return sb.toString();
	}
	
	/**
	 * 删除实体，流程相关文件都删除
	 * @param entity 删除的对象
	 */
	public void deleteEntity(ExceptionImprove entity){
		if(entity.getWorkflowInfo()!=null){
			//String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			ApiFactory.getInstanceService().deleteInstance(entity);
		}else{
			getHibernateDao().delete(entity);
		}
		//删掉对应联络单中的编号
		if(entity.getExceptionContactId()!=null&&!"".equals(entity.getExceptionContactId())){
			ExceptionContact report=exceptionContactManager.getEntity(Long.valueOf(entity.getExceptionContactId()));
			if(report!=null){
				report.setExceptionImproveId("");
				report.setExceptionImprovetNo("");
				report.setRequestDate(null);
				report.setSqeFinishDate(null);
			}			
		}
		//记录删除的信息到TP数据库中
		String companyName= PropUtils.getProp("companyName");
		if(!companyName.equals("TP")){
			String sql = "insert into SUPPLIER_DELETE_MARK (id,source_id,source_unit,source_table) values("+entity.getId()+","+entity.getId()+",'"+companyName+"','"+SupplierDeleteMark.SUPPLIER_EXCEPTION_IMPROVE+"')";
			jdbcTemplate.execute(sql);
		}
	}	
	
	public ExceptionImprove creatImprove(ExceptionContact exceptionContact){
			ExceptionImprove report=new ExceptionImprove();
			report.setCreatedTime(new Date());
			report.setModifiedTime(new Date());
	    	report.setFormNo(formCodeGenerated.getExceptionImproveCode());
	    	report.setCreator(ContextUtils.getLoginName());
	    	report.setCreatorName(ContextUtils.getUserName());
	    	report.setSourceUnit(PropUtils.getProp("companyName"));
			report.setBusinessUnitName(exceptionContact.getBusinessUnitName());
			report.setHappenSpace(exceptionContact.getHappenSpace());
			report.setBillingArea(exceptionContact.getBillingArea());
			report.setInspector((ContextUtils.getUserName()));
			report.setProductStage(exceptionContact.getProductStage());
			report.setSupplierName(exceptionContact.getSupplierName());
			report.setSupplierCode(exceptionContact.getSupplierCode());
			report.setExceptionContactId(exceptionContact.getId().toString());
			report.setExceptionContactNo(exceptionContact.getFormNo());
			report.setInspectionId(exceptionContact.getInspectionId());
			report.setInspectionFormNo(exceptionContact.getInspectionFormNo());
			report.setSupplierEmail(exceptionContact.getSupplierEmail());
			report.setBomName(exceptionContact.getBomName());
			report.setBomCode(exceptionContact.getBomCode());
			report.setInspectionDate(exceptionContact.getInspectionDate());
			report.setCheckAmount(exceptionContact.getCheckAmount());	
			report.setIncomingAmount(exceptionContact.getIncomingAmount());
			report.setMaterialType(exceptionContact.getMaterialType());
			report.setUnits(exceptionContact.getUnits());
			report.setExceptionStage(exceptionContact.getExceptionStage());
			report.setExceptionType(exceptionContact.getExceptionType());
			report.setExceptionDegree(exceptionContact.getExceptionDegree());
			report.setSurfaceBadRate(exceptionContact.getSurfaceBadRate());
			report.setSurfaceBad(exceptionContact.getSurfaceBad());
			report.setSurfaceBadState(exceptionContact.getSurfaceBadState());
			report.setSponsorDate(exceptionContact.getSponsorDate());
			report.setDescFile(exceptionContact.getDescFile());
			if(StringUtils.isNotEmpty(exceptionContact.getReturnReportNo())||StringUtils.isNotEmpty(exceptionContact.getSqeMrbReportNo())){
				report.setSqeProcessOpinion(exceptionContact.getSqeProcessOpinion());
				report.setReturnReportNo(exceptionContact.getReturnReportNo());
				report.setSqeMrbReportNo(exceptionContact.getSqeMrbReportNo());
			}
			report.setMqeChecker(exceptionContact.getMqeChecker());
			report.setMqeCheckerLog(exceptionContact.getMqeCheckerLog());			
			report.setSizeBadRate(exceptionContact.getSizeBadRate());
			report.setSizeBad(exceptionContact.getSizeBad());
			report.setSizeBadState(exceptionContact.getSizeBadState());			
			report.setFunctionBadRate(exceptionContact.getFunctionBadRate());
			report.setFunctionBad(exceptionContact.getFunctionBad());
			report.setFunctionBadState(exceptionContact.getFunctionBadState());			
			report.setFeaturesBadRate(exceptionContact.getFeaturesBadRate());
			report.setFeaturesBad(exceptionContact.getFeaturesBad());
			report.setFeaturesBadState(exceptionContact.getFeaturesBadState());
			report.setBadDesc(exceptionContact.getBadDesc());
			if(exceptionContact.getMqeComfirmLog()!=null&&!"".equals(exceptionContact.getMqeComfirmLog())){
				report.setSqeChecker(exceptionContact.getMqeComfirm());
				report.setSqeCheckerLog(exceptionContact.getMqeComfirmLog());
				CompleteTaskTipType completeTaskTipType =  submitProcess(report,null);
				getCompleteTaskTipType(completeTaskTipType,report);
			}else{
				exceptionImproveDao.save(report);
			}			
			return report;		
	}
	
}
