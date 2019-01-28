package com.ambition.portal.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.MfgCheckInspectionReport;
import com.ambition.carmfg.entity.MfgCheckItem;
import com.ambition.carmfg.madeinspection.dao.MadeInspectionDao;
import com.ambition.iqc.entity.CheckItem;
import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.iqc.inspectionreport.dao.IncomingInspectionActionsReportDao;
import com.ambition.util.common.DateUtil;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.task.base.enumeration.TaskState;
import com.norteksoft.task.dao.TaskDao;
/**
 * 
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  lidefeng
 * @version 1.00 2017年1月9日 发布
 */
@Service
@Transactional
public class PortalManager {
	@Autowired
	private  IncomingInspectionActionsReportDao incomingInspectionActionsReportDao;
	@Autowired
	private MadeInspectionDao madeInspectionDao;
	@Autowired
	private TaskDao taskDao;
	
	
	public List<Map<String , Object>> queryTaskMessage(Long companyId,String loginName,Long userId){
		String hql = "select t.title,t.transactorName,t.createdTime,t.id from Task t where companyId=?"
					+" and ((t.transactor = ? and t.transactorId is null) or (t.transactorId is not null and t.transactorId=?))"
				 	+" and (t.active=? or t.active=? or t.active=? or t.active=?) and t.paused=? and t.visible=?";
		hql += " order by t.createdTime desc";
		StringBuffer sb = new StringBuffer();
		sb.append(hql);
		Query query = taskDao.createQuery(sb.toString(),new Object[]{companyId,loginName,userId, TaskState.WAIT_TRANSACT.getIndex(), TaskState.WAIT_DESIGNATE_TRANSACTOR.getIndex(), TaskState.DRAW_WAIT.getIndex(), TaskState.WAIT_CHOICE_TACHE.getIndex(), Boolean.valueOf(false), Boolean.valueOf(true)});
		query.setMaxResults(10);
		List<Object> list = query.list();
		List<Map<String , Object>> datas = new ArrayList<Map<String,Object>>();
		for(Object object : list){
			Object[] objs = (Object[])object;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title",objs[0]+"");
			map.put("transactorName",objs[1]+"");
			map.put("createdTime",objs[2]==null?"":DateUtil.formateDateStr((Date)objs[2]));
			map.put("id",objs[3]+"");
			datas.add(map);
		}
		return datas;
	}


	public List<Map<String , Object>> queryIqcDatas(String rows) {
		String hql = "select s.id,s.inspectionNo,s.checkBomCode,s.checkBomName from IncomingInspectionActionsReport s";
		hql += " where (s.auditLoginMan=? and s.auditTime is null) or (s.lastStateLoginMan=? and s.lastStateTime is null) ";
		StringBuffer sb = new StringBuffer();
		sb.append(hql);
		Query query = incomingInspectionActionsReportDao.createQuery(sb.toString());
		query.setParameter(0, ContextUtils.getLoginName());
		query.setParameter(1, ContextUtils.getLoginName());
		query.setMaxResults(Integer.valueOf(rows));
		@SuppressWarnings("unchecked")
		List<Object> list = query.list();
		List<Map<String , Object>> datas = new ArrayList<Map<String,Object>>();
		for(Object object : list){
			Object[] objs = (Object[])object;
			Map<String, Object> map = new HashMap<String, Object>();
			IncomingInspectionActionsReport report = incomingInspectionActionsReportDao.get(Long.valueOf(objs[0].toString()));
			List<CheckItem> checkItems = report.getCheckItems();
			for(CheckItem item : checkItems){
				map.put("id",objs[0]+"");
				map.put("inspectionNo",objs[1]+"");
				map.put("checkBomCode",objs[2]+"");
				map.put("checkBomName",objs[3]+"");
			}
			datas.add(map);
		}
		return datas;
	}
	public List<Map<String , Object>> queryMfgDatas(String rows) {
		String hql = "select s.id,s.inspectionNo,s.processSection,s.workProcedure,s.machineNo,s.machineName from MfgCheckInspectionReport s";
		hql += " where s.inspectionConclusion=? and s.processingResult is null ";
		hql += " and s.inspectionDate>=? and s.inspectionDate<=?";
		Calendar   cal_1= Calendar.getInstance();//获取当前日期 
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        Calendar cale = Calendar.getInstance();   
        cale.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天 
		StringBuffer sb = new StringBuffer();
		sb.append(hql);
		Query query = madeInspectionDao.createQuery(sb.toString());
		query.setParameter(0, "NG");
		query.setParameter(1, cal_1.getTime());
		query.setParameter(2, cale.getTime());
		query.setMaxResults(Integer.valueOf(rows));
		@SuppressWarnings("unchecked")
		List<Object> list = query.list();
		DecimalFormat df = new DecimalFormat("0.00");
		List<Map<String , Object>> datas = new ArrayList<Map<String,Object>>();
		for(Object object : list){
			Object[] objs = (Object[])object;
			Map<String, Object> map = new HashMap<String, Object>();
			MfgCheckInspectionReport report = madeInspectionDao.get(Long.valueOf(objs[0].toString()));
			List<MfgCheckItem> checkItems = report.getCheckItems();
			for(MfgCheckItem item : checkItems){
				if("NG".equals(item.getConclusion())){
					map.put("id",objs[0]+"");
					map.put("inspectionNo",objs[1]+"");
					map.put("workProcedure",objs[3]+"");
					map.put("machineNo",objs[4]+"");
					map.put("checkItemName",item.getCheckItemName()+"");
					if(item.getInspectionAmount()!=null){
						if(item.getInspectionAmount()==0){
							map.put("checkItemRate","0");
						}else{
							map.put("checkItemRate",df.format(item.getUnqualifiedAmount()*1.0/item.getInspectionAmount()*1.0));
						}
					}else{
						map.put("checkItemRate","0");
					}
				}
			}
			datas.add(map);
		}
		return datas;
	}
	
	
}
