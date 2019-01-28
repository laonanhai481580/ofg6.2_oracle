package com.ambition.carmfg.patrol.dao;

import org.springframework.stereotype.Repository;

import com.ambition.carmfg.entity.InspectionPointTypeEnum;
import com.ambition.carmfg.entity.MfgCheckInspectionReport;
import com.ambition.carmfg.entity.MfgPatrolReport;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  lpf
 * @version 1.00 2018-1-4 发布
 */
@Repository
public class MfgPatrolReportDao extends HibernateDao<MfgPatrolReport, Long> {
	public Page<MfgPatrolReport> listHid(Page<MfgPatrolReport> page){
		  String hql = "from MfgPatrolReport o where o.hiddenState = 'Y'";
		  return searchPageByHql(page, hql);
	}
	
	public Page<MfgPatrolReport> list(Page<MfgPatrolReport> page){
		String hql = "from MfgPatrolReport o where o.hiddenState = 'N' and o.companyId = ? ";
		return searchPageByHql(page, hql,ContextUtils.getCompanyId());
	}
	
	public Page<MfgPatrolReport> waitAuditList(Page<MfgPatrolReport> page){
		String hql = "from MfgPatrolReport o where o.companyId = ? and  o.reportState=?  ";
		return searchPageByHql(page, hql,ContextUtils.getCompanyId(),MfgCheckInspectionReport.STATE_AUDIT);
	}
	
	public Page<MfgPatrolReport> recheckList(Page<MfgPatrolReport> page){
		String hql = "from MfgPatrolReport o where o.companyId = ? and ( o.reportState=? or o.reportState=? ) and o.workflowInfo.workflowId is not null and o.workflowInfo.currentActivityName=?  ";
		return searchPageByHql(page, hql,ContextUtils.getCompanyId(),MfgCheckInspectionReport.STATE_DEFAULT,MfgCheckInspectionReport.STATE_AUDIT,"填写检验报告");
	}
	
	public Page<MfgPatrolReport> checkingList(Page<MfgPatrolReport> page){
		String hql = "from MfgPatrolReport o where o.companyId = ? and  o.reportState=?    ";
		return searchPageByHql(page, hql,ContextUtils.getCompanyId(),MfgCheckInspectionReport.STATE_DEFAULT);
	}
}
