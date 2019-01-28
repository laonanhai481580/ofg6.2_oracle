package com.ambition.carmfg.patrol.service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.carmfg.entity.InspectionPointTypeEnum;
import com.ambition.carmfg.entity.MfgInspectingItem;
import com.ambition.carmfg.entity.MfgItemIndicator;
import com.ambition.carmfg.entity.MfgPatrolDetail;
import com.ambition.carmfg.entity.MfgPatrolDetailData;
import com.ambition.carmfg.entity.MfgPatrolReport;
import com.ambition.carmfg.patrol.dao.MfgPatrolReportDao;
import com.ambition.carmfg.patrol.web.MfgPatrolReportAction;
import com.ambition.iqc.entity.MfgToMes;
import com.ambition.spc.abnormal.service.AbnormalInfoManager;
import com.ambition.spc.abnormal.util.BaseUtil;
import com.ambition.spc.dataacquisition.dao.SpcSgSampleDao;
import com.ambition.spc.dataacquisition.dao.SpcSubGroupDao;
import com.ambition.spc.dataacquisition.service.SpcSubGroupManager;
import com.ambition.spc.entity.LayerType;
import com.ambition.spc.entity.ProcessPoint;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.entity.SpcSgSample;
import com.ambition.spc.entity.SpcSgTag;
import com.ambition.spc.entity.SpcSubGroup;
import com.ambition.spc.layertype.dao.LayerTypeDao;
import com.ambition.spc.processdefine.dao.QualityFeatureDao;
import com.ambition.supplier.utils.DateUtil;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.exportexcel.ExportExcelFormatter;
import com.ambition.webservice.QisToBackService;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.product.web.wf.WorkflowManagerSupport;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.engine.client.EndInstanceInterface;
import com.norteksoft.wf.engine.client.FormFlowableDeleteInterface;
import com.norteksoft.wf.engine.client.RetrieveTaskInterface;

@Service
@Transactional
public class MfgPatrolReportManager extends WorkflowManagerSupport<MfgPatrolReport> implements FormFlowableDeleteInterface,RetrieveTaskInterface,
EndInstanceInterface{
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private MfgPatrolReportDao mfgPatrolReportDao;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Autowired
	private QualityFeatureDao qualityFeatureDao;
	@Autowired
	private SpcSgSampleDao spcSgSampleDao;
	@Autowired
	private SpcSubGroupDao spcSubGroupDao;	
	@Autowired
	private LayerTypeDao layerTypeDao;
	@Autowired
	private AbnormalInfoManager abnormalInfoManager;
	@Autowired
	private SpcSubGroupManager spcSubGroupManager;
	public static final String MFG_CODE="mfg-patrol-workflow-id";
	
	public MfgPatrolReport getMfgPatrolReport(Long id){
		return mfgPatrolReportDao.get(id);
	}
	
	public void saveMfgPatrolReport(MfgPatrolReport mfgPatrolReport){
		mfgPatrolReportDao.save(mfgPatrolReport);
	}
	
	@Override
	public void endInstanceExecute(Long arg0) {
		
	}

	@Override
	public void retrieveTaskExecute(Long arg0, Long arg1) {
		
	}

	@Override
	public void deleteFormFlowable(Long id) {
		mfgPatrolReportDao.delete(id);
	}

	@Override
	protected MfgPatrolReport getEntity(Long id) {
		return mfgPatrolReportDao.get(id);
	}

	@Override
	protected void saveEntity(MfgPatrolReport mfgPatrolReport) {
		mfgPatrolReportDao.save(mfgPatrolReport);
	}
	public Page<MfgPatrolReport> waitAuditList(Page<MfgPatrolReport> page){
		return mfgPatrolReportDao.waitAuditList(page);
	}
	public Page<MfgPatrolReport> list(Page<MfgPatrolReport> page){
		return mfgPatrolReportDao.list(page);
	}	
	
	public Page<MfgPatrolReport> recheckList(Page<MfgPatrolReport> page){
		return mfgPatrolReportDao.recheckList(page);
	}
	public Page<MfgPatrolReport> checkingList(Page<MfgPatrolReport> page){
		return mfgPatrolReportDao.checkingList(page);
	}
	 public String deleteMfgPatrolReport(String deleteIds){
	        String [] deleteId=deleteIds.split(",");
	        String message = "";
	        Integer deleteNum = 0;
	        Integer dontDeleteNum = 0;
	        StringBuilder sb = new StringBuilder("");
	        for(String id:deleteId){
	        	MfgPatrolReport apply = mfgPatrolReportDao.get(Long.valueOf(id));
	        	if(ContextUtils.getUserName().equals(apply.getCreator())){
	        		deleteNum++;
	        		String spcSampleIds = apply.getSpcSampleIds();
		        	if(spcSampleIds!=null){
		        		String[] spcArr = spcSampleIds.split(",");
		        		Long spcSubGroupId = null;
		        		for(String spcSampId:spcArr){
		        			if(StringUtils.isNotEmpty(spcSampId)&&!" ".equals(spcSampId)){
		        				SpcSgSample spcs = spcSgSampleDao.get(Long.valueOf(spcSampId));
		        				if(spcs!=null){
		        					spcSubGroupId = spcs.getSpcSubGroup().getId();
		        					mfgPatrolReportDao.getSession().delete(spcs);
		        				}
		        			}
		        		}
		                if(spcSubGroupId!=null){
		                	spcSubGroupDao.delete(spcSubGroupId);
		                }
		        	}
		        	if(apply.getWorkflowInfo() != null){
		        		for (MfgPatrolDetail item : apply.getCheckItems()) {
							Long itemId = item.getId();
							String sql1 = "delete from MFG_PATROL_DATAIL_DATA where FK_MFG_PATROL_DATAIL_ID = ?";	
							
							mfgPatrolReportDao.getSession().createSQLQuery(sql1)
							.setParameter(0,itemId)
							.executeUpdate();
						}
		        		Long reportId = apply.getId();
						String sql31 = "delete from MFG_PATROL_DATAIL where FK_MFG_PATROL_REPORT_ID = ?";	
						
						mfgPatrolReportDao.getSession().createSQLQuery(sql31)
						.setParameter(0,reportId)
						.executeUpdate();
		        		ApiFactory.getInstanceService().deleteInstance(apply);
		        	}else{
		        		for (MfgPatrolDetail item : apply.getCheckItems()) {
							Long itemId = item.getId();
							String sql1 = "delete from MFG_PATROL_DATAIL_DATA where FK_MFG_PATROL_DATAIL_ID = ?";	
							
							mfgPatrolReportDao.getSession().createSQLQuery(sql1)
							.setParameter(0,itemId)
							.executeUpdate();
						}
		        		Long reportId = apply.getId();
						String sql31 = "delete from MFG_PATROL_DATAIL where FK_MFG_PATROL_REPORT_ID = ?";	
						
						mfgPatrolReportDao.getSession().createSQLQuery(sql31)
						.setParameter(0,reportId)
						.executeUpdate();
		        		mfgPatrolReportDao.delete(apply);
		        		sb.append(apply.getInspectionNo() + ",");
		        	}
	        	}else{
	        		dontDeleteNum++;
	        	}
	        }
	        message = sb.toString()+"~"+deleteNum+"条已删除，"+dontDeleteNum+"条无权限删除";
	        return message;
	    }
		/**
		 * 删除实体，流程相关文件都删除
		 * @param entity 删除的对象
		 */
		public void deleteEntity(MfgPatrolReport entity){
			if(entity.getWorkflowInfo()!=null){
				String workflowId =  entity.getWorkflowInfo().getWorkflowId();
				for (MfgPatrolDetail item : entity.getCheckItems()) {
					Long id = item.getId();
					String sql1 = "delete from MFG_PATROL_DATAIL_DATA where FK_MFG_PATROL_DATAIL_ID = ?";	
					
					mfgPatrolReportDao.getSession().createSQLQuery(sql1)
					.setParameter(0,id)
					.executeUpdate();
				}
				//先删除子表
				Long reportId = entity.getId();
				String sql31 = "delete from MFG_PATROL_DATAIL where FK_MFG_PATROL_REPORT_ID = ?";	
				
				mfgPatrolReportDao.getSession().createSQLQuery(sql31)
				.setParameter(0,reportId)
				.executeUpdate();

				String sql = "delete from product_task_all_his where execution_id = ?";
				mfgPatrolReportDao.getSession().createSQLQuery(sql)
				.setParameter(0,workflowId)
				.executeUpdate();
				mfgPatrolReportDao.delete(entity);
				ApiFactory.getInstanceService().deleteInstance(entity);
			}else{
				mfgPatrolReportDao.delete(entity);
			}
		}	 
	/**
	  * 方法名: 提交发起流程
	  * <p>功能说明：</p>
	  * @return
	 */
	public boolean submitProcess(MfgPatrolReport report,List<JSONObject> checkItemArray,InspectionPointTypeEnum typeEnum
			) throws Exception{
			saveMadeInspection(report,checkItemArray,typeEnum);
			Long processId = ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(MFG_CODE).get(0).getId();
			ApiFactory.getInstanceService().submitInstance(processId,report);
			report.setReportState(MfgPatrolReport.STATE_AUDIT);
			this.saveEntity(report);
			return true;		
	}
	/**
	  * 方法名: 提交发起流程
	  * <p>功能说明：</p>
	  * @return
	 */
	public boolean submitProcess(MfgPatrolReport report) throws Exception{
			Long processId = ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(MFG_CODE).get(0).getId();
			ApiFactory.getInstanceService().submitInstance(processId,report);
			report.setReportState(MfgPatrolReport.STATE_AUDIT);
			this.saveEntity(report);
			return true;
	}

	/**
	  * 方法名:流程办理
	  * <p>功能说明：</p>
	  * @return
	 * @throws Exception 
	 */
	public void completeTaskCode(MfgPatrolReport report,Long taskId,TaskProcessingResult taskTransact) throws Exception{
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<JSONObject> checkItemArray =MfgPatrolReportAction.getRequestCheckItems();
		if(checkItemArray != null){
			report.getCheckItems().clear();
			for(JSONObject json : checkItemArray){
				MfgPatrolDetail checkItem = new MfgPatrolDetail();
				checkItem.setCompanyId(ContextUtils.getCompanyId());
				checkItem.setCreatedTime(new Date());
				checkItem.setCreator(ContextUtils.getUserName());
				checkItem.setLastModifiedTime(new Date());
				checkItem.setLastModifier(ContextUtils.getUserName());
				Map<String, MfgPatrolDetailData> map=new HashMap<String, MfgPatrolDetailData>();
				for(Object key : json.keySet()){
					String value = json.getString(key.toString());					
					if(key.toString().indexOf("result")>=0){
						String str=key.toString().split("_")[1];
						MfgPatrolDetailData data=null;
						if(!map.containsKey(str)){
							data=new MfgPatrolDetailData();
							data.setInspectionDate(sdf.parse(str));
							data.setMfgPatrolDetail(checkItem);
							map.put(str, data);
							setProperty(data, key.toString(),value);
						}else{
							data=map.get(str);
							setProperty(data, key.toString(),value);
						}
					}else{
						setProperty(checkItem, key.toString(),value);
					}				
				}
				List<MfgPatrolDetailData> list=new ArrayList<MfgPatrolDetailData>();
				for(String str: map.keySet()){
					list.add(map.get(str));
				}
				checkItem.setMfgPatrolDetailDatas(list);
				checkItem.setCheckBomCode(report.getCheckBomCode());
				checkItem.setCheckBomName(report.getCheckBomName());
				checkItem.setWorkProcedure(report.getWorkProcedure());
				checkItem.setInspectionDate(report.getInspectionDate());
				checkItem.setMfgPatrolReport(report);
				report.getCheckItems().add(checkItem);
			}
		}
		mfgPatrolReportDao.save(report);
		ApiFactory.getTaskService().completeWorkflowTask(taskId,taskTransact);
		String processResult = "";
        if("REFUSE".equals(taskTransact.name())){
        	processResult = "不同意";
        	report.setReportState(MfgPatrolReport.STATE_DEFAULT);
        }else  if("SUBMIT".equals(taskTransact.name())){
        	processResult = "提交";
        	report.setReportState(MfgPatrolReport.STATE_AUDIT);
        }else if("APPROVE".equals(taskTransact.name())){
        	report.setReportState(MfgPatrolReport.STATE_COMPLETE);
        	processResult = "同意";
        	/*String allHisPatrolSpcSampleIds = report.getSpcSampleIds()==null?"":report.getSpcSampleIds();
    		String allPatrolSpcSampleIds = ",";
    		Map<String,QualityFeature> featureMap = new HashMap<String, QualityFeature>();
    		String inspectionPointType=Struts2Utils.getParameter("inspectionPointType");
    		if(report.getCheckItems().size()>0){
    			for (MfgPatrolDetail checkItem : report.getCheckItems()) {
    				//以下用于SPC推送数据
    				String featureId = null,countType = null;
    				String values = "";
    				for (MfgPatrolDetailData data : checkItem.getMfgPatrolDetailDatas()) {
    					featureId=checkItem.getFeatureId();
    					countType=checkItem.getCountType();
    					for(int i=0;i<80;i++){
    						String value=null;
							if(PropertyUtils.getProperty(data, "result"+(i+1)+"")!=null){
								value=PropertyUtils.getProperty(data, "result"+(i+1)+"").toString();
							}    								
    						if(value != null && !value.equals("")){
    							values += value + ",";
    						}
    					}
        				//处理spc数据
        				String spcSampleIds = "";   				
        				if("计量".equals(countType) && StringUtils.isNotEmpty(featureId)){
    						if(StringUtils.isNotEmpty(values)){
    							if(!featureMap.containsKey(checkItem.getFeatureId())){
    								List<QualityFeature> features = qualityFeatureDao.find("from QualityFeature q where q.id = ?",Long.valueOf(checkItem.getFeatureId()));
    				    			if(features.isEmpty()){
    				    				featureMap.put(checkItem.getFeatureId(),null);
    				    			}else{
    				    				featureMap.put(checkItem.getFeatureId(),features.get(0));
    				    			}
    							}
    							QualityFeature feature = featureMap.get(checkItem.getFeatureId());
    							if(feature != null){
    								String hisSampleIds = checkItem.getSpcSampleIds();
    								if(StringUtils.isNotEmpty(hisSampleIds)){
    									allHisPatrolSpcSampleIds = allHisPatrolSpcSampleIds.replaceAll("," + hisSampleIds,",");
    								}
    								spcSampleIds =insertSpcByResults(feature,report.getInspectionDate(),hisSampleIds,values,report.getInspectionPointType(),report.getWorkGroupType(),report,checkItem.getEquipmentNo());
    							}
    							allPatrolSpcSampleIds += spcSampleIds;
    						}
    					
    					}
        				checkItem.setSpcSampleIds(spcSampleIds);
					}
				}
    		}*/
        }else{
        	processResult = "";
        }
		String opinion = Struts2Utils.getParameter("opinion");
		if(StringUtils.isNotEmpty(opinion)){
			WorkflowTask task = ApiFactory.getTaskService().getTask(taskId);
			//保存记录
			Opinion opinionParameter = new Opinion();
			opinionParameter.setCustomField(processResult);
	        opinionParameter.setOpinion(opinion);
	        opinionParameter.setTransactor(ContextUtils.getLoginName());
	        opinionParameter.setTransactorName(ContextUtils.getUserName());
	        opinionParameter.setTaskName(task.getName());
	        opinionParameter.setTaskId(taskId);
	        opinionParameter.setAddOpinionDate(new Date());
	        ApiFactory.getOpinionService().saveOpinion(opinionParameter);
		}
	}
	public String insertSpcByResults(QualityFeature feature,Date inspectionDate,String hisSampleIds,String results,InspectionPointTypeEnum inspectionPointType, String workGroupType, MfgPatrolReport mfgPatrolReport,String equipmentNo) throws NumberFormatException, Exception{
		List<SpcSgSample> samples = new ArrayList<SpcSgSample>();
		if(StringUtils.isNotEmpty(hisSampleIds)){
			String[] sampleIds = hisSampleIds.split(",");
			for(String id : sampleIds){
				if(StringUtils.isNotEmpty(id)){
					List<SpcSgSample> list = spcSgSampleDao.find("from SpcSgSample s where s.id = ?",Long.valueOf(id));
					if(!list.isEmpty()){
						SpcSgSample sample = list.get(0);
						//如果历史的特性与现在的特性不一致,直接跳过
						if(!sample.getSpcSubGroup().getQualityFeature().getId().equals(feature.getId())){
							break;
						}
						samples.add(sample);
					}
				}
			}
		}
		String[] vals = results.split(",");
		int count=0;
		String spcSampleIds = "";
		for(String val : vals){
			//判断是否数字
			if(CommonUtil.isNumber(val)){
				SpcSgSample sample = null;
				if(count<samples.size()){
					sample = samples.get(count);
					if(val.indexOf(".")==-1){
						val += ".0";
					}
					if(!val.equals(sample.getSamValue())){
						sample.setSamValue(Double.valueOf(val));
						spcSgSampleDao.save(sample);
						//更新值
						updateSpcSgGroup(sample.getSpcSubGroup(),ContextUtils.getCompanyId(),ContextUtils.getUserName(),mfgPatrolReport);
					}
				}else{
					sample = insertSpc(feature,inspectionDate,Double.valueOf(val),ContextUtils.getCompanyId(),ContextUtils.getUserName(),inspectionPointType,workGroupType,mfgPatrolReport,equipmentNo);
				}
				spcSampleIds += sample.getId() + ",";
				count++;
			}
		}
		if(count<samples.size()){
			for(;count<samples.size();count++){
				spcSubGroupDao.delete(samples.get(count).getSpcSubGroup());
			}
		}
		return spcSampleIds;
	}	
	
	private Integer getMaxGroupOrderNum(QualityFeature feature,Long companyId){
		List<Object> objs = qualityFeatureDao.find("select max(s.subGroupOrderNum) from SpcSubGroup s where s.qualityFeature = ? and s.companyId = ?",feature,companyId);
		if(objs.get(0)==null){
			return 1;
		}else{
			return Integer.valueOf(objs.get(0).toString())+1;
		}
	}	
	
	/**
	  * 方法名:添加数据时插入监控 
	  * @param feature
	  * @param detectDate
	  * @param paramValue
	  * @param companyid
	  * @param userName
	 * @param workGroupType 
	 * @param mfgCheckInspectionReport 
	  * @return
	 * @throws Exception 
	 */
	public SpcSgSample insertSpc(QualityFeature feature,Date detectDate,Double paramValue,Long companyid,String userName,InspectionPointTypeEnum inspectionPointType, String workGroupType, MfgPatrolReport mfgPatrolReport,String equipmentNo) throws Exception{
		String inspectionType = "巡检";
		SpcSubGroup spcSubGroup = spcSubGroupManager.findLastSubGroup(feature.getId(),feature.getSampleCapacity(),companyid,userName);
		int sampleSize = 0;
		ProcessPoint point = feature.getProcessPoint();
		String pointParentName = "";
		if(point!=null){
			if(point.getParent()!=null){
				if( point.getParent().getName().contains("CCM")){
					pointParentName=point.getName();
				}else{
					pointParentName = point.getParent().getName();
				}
			}else{
				pointParentName = point.getName();
			}
		}
		if(spcSubGroup == null){
		    spcSubGroup = new SpcSubGroup();
			spcSubGroup.setProsessPointName(pointParentName);
			spcSubGroup.setCompanyId(companyid);
			spcSubGroup.setCreatedTime(detectDate);
			spcSubGroup.setCreator(mfgPatrolReport.getInspector());
			spcSubGroup.setLastModifiedTime(new Date());
			spcSubGroup.setLastModifier(userName);
			spcSubGroup.setActualSmapleNum(1);
			spcSubGroup.setQualityFeature(feature);
			spcSubGroup.setSubGroupOrderNum(getMaxGroupOrderNum(feature,companyid));
			spcSubGroup.setSubGroupSize(feature.getSampleCapacity());
			spcSubGroup.setRangeDiff(0.0);
			spcSubGroup.setSigma(paramValue);
			spcSubGroup.setMaxValue(paramValue);
			spcSubGroup.setMinValue(paramValue);
			spcSubGroup.setInspectionType(inspectionType);
			if("D".equals(workGroupType)||"N".equals(workGroupType)){
				String setColumn = null;
				String inpectionTypeSetColumn = null;
				String inspector = null;
				String machine = null;
				String hql = " from LayerType t where t.typeName=? and t.companyId=?";
				String hqlInspectionType = " from LayerType t where t.typeName=? and t.companyId=?";
				String hqlInspector = " from LayerType t where t.typeName=? and t.companyId=?";
				String hqlMachine = " from LayerType t where t.typeName=? and t.companyId=?";
				List<LayerType> types = layerTypeDao.find(hql,"班组",ContextUtils.getCompanyId());
				List<LayerType> inspectionTypesLayer = layerTypeDao.find(hqlInspectionType,"检验",ContextUtils.getCompanyId());
				List<LayerType> inspecorTypesLayer = layerTypeDao.find(hqlInspector,"检验员",ContextUtils.getCompanyId());
				List<LayerType> machineTypesLayer = layerTypeDao.find(hqlMachine,"机台",ContextUtils.getCompanyId());
               if(types.size()>0){
               	setColumn = types.get(0).getTypeCode();
               	inpectionTypeSetColumn = inspectionTypesLayer.get(0).getTypeCode();
               	inspector = inspecorTypesLayer.get(0).getTypeCode();
               	machine = machineTypesLayer.get(0).getTypeCode();
               	if(setColumn!=null){
                   	SpcSgTag spcSgTag = new SpcSgTag();
       				spcSgTag.setCreatedTime(spcSubGroup.getCreatedTime());
       				spcSgTag.setCompanyId(ContextUtils.getCompanyId());
       				spcSgTag.setCreator(mfgPatrolReport.getInspector());
       				spcSgTag.setModifiedTime(new Date());
       				spcSgTag.setModifier(ContextUtils.getUserName());
       				spcSgTag.setTagName(types.get(0).getTypeName());
       				spcSgTag.setTagCode(setColumn);
       				if("D".equals(workGroupType)){
       					spcSgTag.setTagValue("SHIFT_D");
       					setProperty(spcSubGroup, setColumn, "SHIFT_D");
       				}else if("N".equals(workGroupType)){
       					spcSgTag.setTagValue("SHIFT_N");
       					setProperty(spcSubGroup, setColumn, "SHIFT_N");
       				}
       				spcSgTag.setSpcSubGroup(spcSubGroup);
       				
       				SpcSgTag spcSgTagInspectionType = new SpcSgTag();
       				spcSgTagInspectionType.setCreatedTime(spcSubGroup.getCreatedTime());
       				spcSgTagInspectionType.setCompanyId(ContextUtils.getCompanyId());
       				spcSgTagInspectionType.setCreator(mfgPatrolReport.getInspector());
       				spcSgTagInspectionType.setModifiedTime(new Date());
       				spcSgTagInspectionType.setModifier(ContextUtils.getUserName());
       				spcSgTagInspectionType.setTagName(inspectionTypesLayer.get(0).getTypeName());
       				spcSgTagInspectionType.setTagCode(inpectionTypeSetColumn);
       				spcSgTagInspectionType.setTagValue(inspectionType);
   					setProperty(spcSubGroup, inpectionTypeSetColumn, inspectionType);
       				spcSgTagInspectionType.setSpcSubGroup(spcSubGroup);
       				
       				SpcSgTag spcSgTagInspector = new SpcSgTag();
       				spcSgTagInspector.setCreatedTime(spcSubGroup.getCreatedTime());
       				spcSgTagInspector.setCompanyId(ContextUtils.getCompanyId());
       				spcSgTagInspector.setCreator(mfgPatrolReport.getInspector());
       				spcSgTagInspector.setModifiedTime(new Date());
       				spcSgTagInspector.setModifier(ContextUtils.getUserName());
       				spcSgTagInspector.setTagName(inspecorTypesLayer.get(0).getTypeName());
       				spcSgTagInspector.setTagCode(inspector);
       				spcSgTagInspector.setTagValue(mfgPatrolReport.getInspector());
   					setProperty(spcSubGroup, inspector, mfgPatrolReport.getInspector());
   					spcSgTagInspector.setSpcSubGroup(spcSubGroup);
   					
   					SpcSgTag spcSgTagMachine = new SpcSgTag();
   					spcSgTagMachine.setCreatedTime(spcSubGroup.getCreatedTime());
   					spcSgTagMachine.setCompanyId(ContextUtils.getCompanyId());
   					spcSgTagMachine.setCreator(mfgPatrolReport.getInspector());
   					spcSgTagMachine.setModifiedTime(new Date());
       				spcSgTagInspector.setModifier(ContextUtils.getUserName());
       				spcSgTagMachine.setTagName(machineTypesLayer.get(0).getTypeName());
       				spcSgTagMachine.setTagCode(machine);
       				spcSgTagMachine.setTagValue(equipmentNo);
   					setProperty(spcSubGroup, machine, equipmentNo);
   					spcSgTagMachine.setSpcSubGroup(spcSubGroup);
   					
       				spcSubGroupDao.getSession().save(spcSgTag);
       				spcSubGroupDao.getSession().save(spcSgTagInspectionType);
       				spcSubGroupDao.getSession().save(spcSgTagInspector);
       				spcSubGroupDao.getSession().save(spcSgTagMachine);
   				}
				}
			}
			
		}else{
			spcSubGroup.setLastModifiedTime(new Date());
			spcSubGroup.setLastModifier(userName);
			String hql = "select max(s.samValue),min(s.samValue),sum(s.samValue),count(id) from SpcSgSample s where s.spcSubGroup = ? and s.companyId = ?";
			Object[] objs = (Object[])spcSubGroupDao.find(hql,spcSubGroup,companyid).get(0);
			Double max = paramValue,min=paramValue,sum=paramValue;
			if(objs[0] != null){
				Double maxVal = Double.valueOf(objs[0].toString());
				if(maxVal>max){
					max = maxVal;
				}
			}
			if(objs[1] != null){
				Double minVal = Double.valueOf(objs[1].toString());
				if(minVal < min){
					min = minVal;
				}
			}
			if(objs[2] != null){
				sum  += Double.valueOf(objs[2].toString());
			}
			if(objs[3] != null){
				sampleSize = Integer.valueOf(objs[3].toString());
			}
			Double sigma = sum/(sampleSize+1);
			Double randDiff = max - min;
			spcSubGroup.setMaxValue(max);
			spcSubGroup.setMinValue(min);
			spcSubGroup.setSigma(sigma);
			spcSubGroup.setRangeDiff(randDiff);
			spcSubGroup.setActualSmapleNum(sampleSize+1);
		}
		spcSubGroupDao.save(spcSubGroup);
		
		SpcSgSample spcSgSample = new SpcSgSample();
		spcSgSample.setCreatedTime(detectDate);
		spcSgSample.setCompanyId(companyid);
		spcSgSample.setCreator(mfgPatrolReport.getInspector());
		spcSgSample.setLastModifiedTime(new Date());
		spcSgSample.setLastModifier(mfgPatrolReport.getInspector());
		spcSgSample.setSampleNo("X" + (sampleSize + 1));
		spcSgSample.setSampleOrderNum(sampleSize + 1+"");
		spcSgSample.setSamValue(paramValue);
		spcSgSample.setSpcSubGroup(spcSubGroup);
		spcSgSampleDao.save(spcSgSample);
		//如果采集的数据够一组时,检查监控条件
		if(spcSubGroup.getSubGroupSize() != null && spcSubGroup.getSubGroupSize().equals(sampleSize+1)){
			//根据条件查询采集的数据
			String hql = "from SpcSubGroup s where s.companyId = ?  and s.qualityFeature = ?";
			List<Object> searchParams = new ArrayList<Object>();
			searchParams.add(spcSubGroup.getCompanyId());
			searchParams.add(feature);
			//10天以前的数据
			hql = hql + " and s.createdTime between ? and ?";
			Calendar calendar = Calendar.getInstance();
			searchParams.add(calendar.getTime());
			calendar.add(Calendar.DATE, -10);
			searchParams.add(searchParams.size()-1, calendar.getTime());
			List<SpcSubGroup> list = spcSubGroupDao.find(hql,searchParams.toArray());
			BaseUtil.setSpcSgSampleDao(spcSgSampleDao);
			//根据规则检测所查询的数据  异常报警
			try{
				abnormalInfoManager.lanchAbnormal(spcSubGroup.getSubGroupOrderNum()+"", feature, list,null);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			BaseUtil.setSpcSgSampleDao(null);
			
			spcSubGroup.setJudgeState(1);
			spcSubGroupDao.save(spcSubGroup);
		}				
		return spcSgSample;
	} 	
	public void updateSpcSgGroup(SpcSubGroup spcSubGroup,Long companyid,String userName, MfgPatrolReport mfgPatrolReport){
		spcSubGroup.setLastModifiedTime(new Date());
		spcSubGroup.setLastModifier(userName);
		String hql = "select max(s.samValue),min(s.samValue),sum(s.samValue),count(id) from SpcSgSample s where s.spcSubGroup = ? and s.companyId = ?";
		Object[] objs = (Object[])spcSubGroupDao.find(hql,spcSubGroup,companyid).get(0);
		Double max = null,min=null,sum=0.0;
		if(objs[0] != null){
			max = Double.valueOf(objs[0].toString());
		}
		if(objs[1] != null){
			min = Double.valueOf(objs[1].toString());
		}
		if(objs[2] != null){
			sum  += Double.valueOf(objs[2].toString());
		}
		if(max==null){
			max = 0.0;
		}
		if(min==null){
			min=0.0;
		}
		int sampleSize = spcSubGroup.getActualSmapleNum();
		if(objs[3] != null){
			sampleSize = Integer.valueOf(objs[3].toString());
		}
		Double sigma = sum/sampleSize;
		Double randDiff = max - min;
		spcSubGroup.setMaxValue(max);
		spcSubGroup.setMinValue(min);
		spcSubGroup.setSigma(sigma);
		spcSubGroup.setCreator(mfgPatrolReport.getInspector());
		spcSubGroup.setRangeDiff(randDiff);
		spcSubGroupDao.save(spcSubGroup);
	}	
	 /**
	  * 方法名: 保存检验记录
	  * <p>功能说明：</p>
	  * @return
	 */
	public void saveMadeInspection(MfgPatrolReport incomingInspectionActionsReport,List<JSONObject> checkItemArray,
			InspectionPointTypeEnum typeEnum) throws Exception{
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(!(MfgPatrolReport.STATE_DEFAULT.equals(incomingInspectionActionsReport.getReportState())
			||MfgPatrolReport.STATE_RECHECK.equals(incomingInspectionActionsReport.getReportState()))){
			throw new AmbFrameException("只能保存状态为【"+MfgPatrolReport.STATE_DEFAULT+"】" +
					"或【"+MfgPatrolReport.STATE_RECHECK+"】的检验报告!");
		}
		if(incomingInspectionActionsReport.getId() == null){
			incomingInspectionActionsReport.setCheckItems(new ArrayList<MfgPatrolDetail>());
			incomingInspectionActionsReport.setInspectionNo(formCodeGenerated.generatePatrolCode());
		}else{
			incomingInspectionActionsReport.setLastModifiedTime(new Date());
			incomingInspectionActionsReport.setLastModifier(ContextUtils.getUserName());
		}
		if(checkItemArray != null){
			incomingInspectionActionsReport.getCheckItems().clear();
			for(JSONObject json : checkItemArray){
				MfgPatrolDetail checkItem = new MfgPatrolDetail();
				checkItem.setCompanyId(ContextUtils.getCompanyId());
				checkItem.setCreatedTime(new Date());
				checkItem.setCreator(ContextUtils.getUserName());
				checkItem.setLastModifiedTime(new Date());
				checkItem.setLastModifier(ContextUtils.getUserName());
				Map<String, MfgPatrolDetailData> map=new HashMap<String, MfgPatrolDetailData>();
				for(Object key : json.keySet()){
					String value = json.getString(key.toString());					
					if(key.toString().indexOf("result")>=0){
						String str=key.toString().split("_")[1];
						MfgPatrolDetailData data=null;
						if(!map.containsKey(str)){
							data=new MfgPatrolDetailData();
							data.setInspectionDate(sdf.parse(str));
							data.setMfgPatrolDetail(checkItem);
							map.put(str, data);
							setProperty(data, key.toString(),value);
						}else{
							data=map.get(str);
							setProperty(data, key.toString(),value);
						}
					}else{
						setProperty(checkItem, key.toString(),value);
					}				
				}
				List<MfgPatrolDetailData> list=new ArrayList<MfgPatrolDetailData>();
				for(String str: map.keySet()){
					list.add(map.get(str));
				}
				checkItem.setMfgPatrolDetailDatas(list);
				checkItem.setCheckBomCode(incomingInspectionActionsReport.getCheckBomCode());
				checkItem.setCheckBomName(incomingInspectionActionsReport.getCheckBomName());
				checkItem.setWorkProcedure(incomingInspectionActionsReport.getWorkProcedure());
				checkItem.setInspectionDate(incomingInspectionActionsReport.getInspectionDate());
				checkItem.setMfgPatrolReport(incomingInspectionActionsReport);
				incomingInspectionActionsReport.getCheckItems().add(checkItem);
			}
		}
		mfgPatrolReportDao.save(incomingInspectionActionsReport);
	}
	
	private void setProperty(Object obj, String property, Object value) throws Exception {
		String fieldName = property,customType = null;
		if(property.indexOf("_")>0){
			String[] strs = property.split("_");
			fieldName = strs[0];
			customType = strs[1];
		}
		Class<?> type = PropertyUtils.getPropertyType(obj, fieldName);
		if (type != null) {
			if (value == null || StringUtils.isEmpty(value.toString())) {
				PropertyUtils.setProperty(obj, fieldName, null);
			} else {
				if("timestamp".equals(customType)){
					PropertyUtils.setProperty(obj, fieldName,DateUtil.parseDateTime(value.toString()));
				}else if (String.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName, value.toString());
				} else if (Integer.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Integer.valueOf(value.toString()));
				} else if (Double.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Double.valueOf(value.toString()));
				} else if (Float.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Float.valueOf(value.toString()));
				} else if (Boolean.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Boolean.valueOf(value.toString()));
				} else if (Date.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,DateUtil.parseDateTime(value+""));
				} else {
					PropertyUtils.setProperty(obj, fieldName, value);
				}
			}
		}
	}
	 
    /**
     * 得到所有意见集合
     */
    public List<Opinion> getOpinions(MfgPatrolReport mfgPatrolReport) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        return ApiFactory.getOpinionService().getOpinions(mfgPatrolReport);
    }
    /**
     * 流程任务ID获取类
     * @param taskId
     * @return
     */
    public MfgPatrolReport  getMfgPatrolReportByTaskId(Long taskId){
        if(taskId == null) return null;
        MfgPatrolReport mfgPatrolReport=null;
        Long id =ApiFactory.getFormService().getFormFlowableIdByTask(taskId);
        mfgPatrolReport=getEntity(id);
        return mfgPatrolReport;
    }

	public String mfgToBarch(QisToBackService qbs, MfgToMes mes) throws Exception {
		return qbs.mfgToBarch(mes);
	}
	 /**
     * 方法名:导出Excel文件 
     * <p>功能说明：</p>
     * @param incomingInspectionActionsReport
     * @throws IOException
    */
   public void exportReport(MfgPatrolReport s) throws IOException{
       InputStream inputStream = null;
       try {
    	   MfgPatrolReport report = s;                                                
           inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/mfg-inspection-report.xlsx");
           Map<String,ExportExcelFormatter> formatterMap = new HashMap<String, ExportExcelFormatter>();
           //单号格式化
           formatterMap.put("code",new ExportExcelFormatter() {
			@Override
			public String format(Object value, int rowNum, String fieldName,
					Cell cell) {
				 MfgPatrolReport inspectionReport = (MfgPatrolReport)value;
                 return "编号:" + inspectionReport.getInspectionNo();
			}
           });
        
           String exportFileName = "市场问题改进表单";
           ExcelUtil.exportToExcel(inputStream, exportFileName, report, formatterMap);
       }catch (Exception e) {
           log.error("导出失败!",e);
       } finally{
           if(inputStream != null){
               inputStream.close();
           }
       }
   }

	public String getReportIdBySpcSmapleId(String spcSampleId) {
		String reportId = "";
		String sql = "select FK_MFG_PATROL_REPORT_ID from MFG_PATROL_DATAIL t where t.spc_sample_ids like ? ";
		@SuppressWarnings("unchecked")
		List<Object> objs = mfgPatrolReportDao.getSession().createSQLQuery(sql).setParameter(0,"%"+spcSampleId+"%").list();
		if(objs.size()!=0){
			reportId = objs.get(0).toString();
		}
		return reportId;
		
	}
	//查询隐藏数据
		public Page<MfgPatrolReport> listHid(Page<MfgPatrolReport>page){
			return mfgPatrolReportDao.listHid(page);
		}
	public String hiddenState(String hideId,String type){
		String[] ids = hideId.split(",");
		StringBuilder sb = new StringBuilder("");
		for(String id : ids){
			MfgPatrolReport mfgPatrolReport = mfgPatrolReportDao.get(Long.valueOf(id));
			if("Y".equals(type)){
				mfgPatrolReport.setHiddenState("N");
			}else{
				mfgPatrolReport.setHiddenState("Y");
			}
			sb.append(mfgPatrolReport.getInspectionNo()+ ",");
			mfgPatrolReportDao.save(mfgPatrolReport);
		}
		return sb.toString();
	}
	/**
	 * 根据生产线查询不良
	 * @param productLine
	 * @return
	 */
	public List<String> getListDate(MfgPatrolDetail mfgPatrolDetail){
		String sql = "select to_char(t.inspection_date,'yyyy-MM-dd HH:mi') from  MFG_PATROL_DATAIL_DATA t  " 
				+" where t.FK_MFG_PATROL_DATAIL_ID=? order by t.inspection_date ";
		@SuppressWarnings("unchecked")
		List<String> list = mfgPatrolReportDao.getSession()
							.createSQLQuery(sql)
							.setParameter(0,mfgPatrolDetail)
							.list();
	/*	List<String> listDate=new ArrayList<String>();
		for(Object obj :  list){
			Object[] objs = (Object[])obj;						
			listDate.add(objs[0].toString());
		}*/
		return list;
	}	
	/**
	 * 根据供应商,物料编码,检验数查询检验项目
	 * @param supplierId
	 * @param checkBomCode
	 * @param stockAmount
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public Map<String,Object> getCheckItems(String workProcedure,String checkBomCode,Integer stockAmount,List<JSONObject> existCheckItems,String inspectionPointType) throws 
	IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Map<String,JSONObject> existItemMap = new HashMap<String, JSONObject>();
		if(existCheckItems != null){
			for(JSONObject json : existCheckItems){
				if(json.containsKey("checkItemName")){
					existItemMap.put(json.getString("checkItemName"),json);
				}
			}
		}
		List<MfgPatrolDetail> checkItems = new ArrayList<MfgPatrolDetail>();
		Pattern pattern = Pattern.compile("[0-9]*|[0-9]*\\.[0-9]*");
		//查询所有的检验项目
		String hql = "select i from MfgItemIndicator i where i.companyId = ? and i.mfgInspectingIndicator.workingProcedure = ? and i.mfgInspectingIndicator.model = ?  and i.inAmountPatrol is not null ";
		List<MfgItemIndicator> itemIndicators = null;
		if(ContextUtils.getSubCompanyName()!=null){
			hql += " and i.mfgInspectingIndicator.businessUnitName= ?";
			itemIndicators = mfgPatrolReportDao.find(hql,ContextUtils.getCompanyId(),workProcedure,checkBomCode,ContextUtils.getSubCompanyName());
		}else{
			itemIndicators = mfgPatrolReportDao.find(hql,ContextUtils.getCompanyId(),workProcedure,checkBomCode);
		}
		if(itemIndicators==null||itemIndicators.size()==0){
			 hql = "select i from MfgItemIndicator i where i.companyId = ? and i.mfgInspectingIndicator.workingProcedure = ? and i.mfgInspectingIndicator.model = ?  and i.inAmountPatrol is not null ";
			itemIndicators = mfgPatrolReportDao.find(hql,ContextUtils.getCompanyId(),workProcedure,checkBomCode);
		}		
		Collections.sort(itemIndicators,new Comparator<MfgItemIndicator>() {
			@Override
			public int compare(MfgItemIndicator o1, MfgItemIndicator o2) {
				Integer o1OrderNum = o1.getOrderNum();
				Integer o2OrderNum = o2.getOrderNum();
				if(o1OrderNum == null || o2OrderNum == null){
					o1OrderNum = o1.getId().intValue();
					o2OrderNum = o2.getId().intValue();
				}
				if(o1OrderNum<o2OrderNum){
					return -1;
				}else if(o1OrderNum>o2OrderNum){
					return 1;
				}else{
					return 0;
				}
			}
		});
		List<MfgInspectingItem> parentInspectingItems = new ArrayList<MfgInspectingItem>();
		Map<Long,List<MfgItemIndicator>> parentItemIndicatorMap = new HashMap<Long, List<MfgItemIndicator>>();
		for(MfgItemIndicator itemIndicator : itemIndicators){
			MfgInspectingItem inspectingItem = itemIndicator.getMfgInspectingItem();
			MfgInspectingItem parentInspectionItem = inspectingItem.getItemParent();
			if(parentInspectionItem==null){
				parentInspectionItem = inspectingItem;
			}
			if(parentItemIndicatorMap.containsKey(parentInspectionItem.getId())){
				parentItemIndicatorMap.get(parentInspectionItem.getId()).add(itemIndicator);
			}else{
				List<MfgItemIndicator> indicators = new ArrayList<MfgItemIndicator>();
				indicators.add(itemIndicator);
				parentItemIndicatorMap.put(parentInspectionItem.getId(),indicators);
				parentInspectingItems.add(parentInspectionItem);
			}
		}
		for(MfgInspectingItem parentInspectingItem : parentInspectingItems){
			List<MfgItemIndicator> indicators = parentItemIndicatorMap.get(parentInspectingItem.getId());
			boolean isRowSpan = false;
			for(MfgItemIndicator itemIndicator : indicators){
				if(itemIndicator.getIsInEquipment()!=null&&itemIndicator.getIsInEquipment().equals("是")){//如果集成则过滤 
					continue;
				}
				if(inspectionPointType.equals(InspectionPointTypeEnum.PATROLINSPECTION.toString())){
					if(itemIndicator.getIsJnUnit()!=null&&itemIndicator.getIsJnUnit().equals("否")){//过滤掉巡检不要的检验项目
						continue;
					}
					if(itemIndicator.getInAmountPatrol()==null){
						continue;
					}
				}
				MfgPatrolDetail checkItem = new MfgPatrolDetail();
				if(!isRowSpan){
					isRowSpan = true;
					checkItem.setParentItemName(parentInspectingItem.getItemName());
					checkItem.setParentRowSpan(indicators.size());
				}
				if(itemIndicator.getIsJnUnit()!=null){
					checkItem.setIsJnUnit(itemIndicator.getIsJnUnit());
				}else{
					checkItem.setIsJnUnit("否");
				}				
				checkItem.setCheckMethod(itemIndicator.getMethod());
				checkItem.setInspectionType(itemIndicator.getMfgInspectingItem().getTopParent().getItemName());
				checkItem.setSpecifications(itemIndicator.getSpecifications());
				String level = itemIndicator.getInspectionLevel();
				checkItem.setInspectionLevel(level);
				checkItem.setRemark(itemIndicator.getRemark());
				checkItem.setCheckItemName(itemIndicator.getMfgInspectingItem().getItemName());
				checkItem.setCountType(itemIndicator.getCountType());
				checkItem.setMaxlimit(itemIndicator.getLevela());
				checkItem.setMinlimit(itemIndicator.getLevelb());
				checkItem.setInspectionAmount(itemIndicator.getInspectionAmount());
				if(itemIndicator.getFeatureId()!=null && !itemIndicator.getFeatureId().equals("")){
					checkItem.setFeatureId(itemIndicator.getFeatureId());
				}else{
					checkItem.setFeatureId("");
				}				
				MfgPatrolDetailData data=new MfgPatrolDetailData();
				data.setResults("");
				checkItem.setUnit(itemIndicator.getUnit());
				checkItem.setInspectionAmount(itemIndicator.getInAmountPatrol());
				checkItem.setIsInEquipment(itemIndicator.getIsJnUnit());
				if(existItemMap.containsKey(checkItem.getCheckItemName())){
					JSONObject json = existItemMap.get(checkItem.getCheckItemName());
					if(MfgInspectingItem.COUNTTYPE_COUNT.equals(checkItem.getCountType())){
						if(json.containsKey("results")){
							data.setResults(json.getString("results"));
						}
					}else{
						for(int i=1;i<=30;i++){
							String fieldName = "result" + i;
							if(json.containsKey(fieldName)){
								String value = json.getString(fieldName);
								if(StringUtils.isNotEmpty(value)&&pattern.matcher(value).matches()){
									PropertyUtils.setProperty(data,fieldName,Double.valueOf(value));
								}
							}
						}
					}
				}
				List<MfgPatrolDetailData> mfgPatrolDetailDatas=new ArrayList<MfgPatrolDetailData>();
				mfgPatrolDetailDatas.add(data);
				checkItem.setMfgPatrolDetailDatas(mfgPatrolDetailDatas);
				data.setMfgPatrolDetail(checkItem);
				checkItems.add(checkItem);
			}
		}
		resultMap.put("checkItems",checkItems);
		return resultMap;
	}	
	
}
