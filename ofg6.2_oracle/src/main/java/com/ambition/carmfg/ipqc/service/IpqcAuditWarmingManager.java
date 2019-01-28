package com.ambition.carmfg.ipqc.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.IpqcAuditWarming;
import com.ambition.carmfg.ipqc.dao.IpqcAuditWarmingDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class IpqcAuditWarmingManager
{

	  @Autowired
	  private IpqcAuditWarmingDao ipqcAuditWarmingDao;
	
	  public IpqcAuditWarming getIpqcAuditWarming(Long id)
	  {
	    return (IpqcAuditWarming)this.ipqcAuditWarmingDao.get(id);
	  }
	
	  public void deleteIpqcAuditWarming(IpqcAuditWarming ipqcAuditWarming) {
	    this.ipqcAuditWarmingDao.delete(ipqcAuditWarming);
	  }
	
	  public Page<IpqcAuditWarming> search(Page<IpqcAuditWarming> page) {
	    return this.ipqcAuditWarmingDao.search(page);
	  }
	
	  public List<IpqcAuditWarming> listAll() {
	    return this.ipqcAuditWarmingDao.getAllIpqcAuditWarming();
	  }
	
	  public void deleteIpqcAuditWarming(Long id) {
	    this.ipqcAuditWarmingDao.delete(id);
	  }
	  public void deleteIpqcAuditWarming(String ids) {
	    String[] deleteIds = ids.split(",");
	    for (String id : deleteIds) {
	      IpqcAuditWarming ipqcAuditWarming = ipqcAuditWarmingDao.get(Long.valueOf(id));
	      if (ipqcAuditWarming.getId() != null)
	        this.ipqcAuditWarmingDao.delete(ipqcAuditWarming);
	    }
	  }
	
	  public void saveIpqcAuditWarming(IpqcAuditWarming ipqcAuditWarming) {
	    if (StringUtils.isEmpty(ipqcAuditWarming.getBusinessUnitName())) {
	      throw new RuntimeException("厂区不能为空!");
	    }
	    if (StringUtils.isEmpty(ipqcAuditWarming.getFactory())) {
	      throw new RuntimeException("工厂不能为空!");
	    }
	    if (ipqcAuditWarming.getRepeatCount()==null) {
		      throw new RuntimeException("重复性不能为空!");
		}
	    if (StringUtils.isEmpty(ipqcAuditWarming.getProblemDegree())) {
		      throw new RuntimeException("问题严重度不能为空!");
		}
	    if (StringUtils.isEmpty(ipqcAuditWarming.getWarmingCycle())) {
		      throw new RuntimeException("统计周期不能为空!");
		}
	    if (isExistAuditWarming(ipqcAuditWarming.getId(), ipqcAuditWarming.getBusinessUnitName(),ipqcAuditWarming.getFactory(), ipqcAuditWarming.getStation(), ipqcAuditWarming.getProblemDegree(), ipqcAuditWarming.getAuditType(),ipqcAuditWarming.getClassGroup())) {
	      throw new RuntimeException("已存在相同的预警信息!");
	    }
	    this.ipqcAuditWarmingDao.save(ipqcAuditWarming);
	  }
	
	  private boolean isExistAuditWarming(Long id, String businessUnitName,String factory, String station, String problemDegree, String auditType,String classGroup)
	  {
	    String hql = "select count(*) from IpqcAuditWarming d where d.companyId = ? and d.businessUnitName = ? and d.factory = ? ";
	    List<Object> params = new ArrayList<Object>();
	    params.add(ContextUtils.getCompanyId());
	    params.add(businessUnitName);
	    params.add(factory);
	    if (auditType != null) {
		      hql = hql + "and d.auditType = ? ";
		      params.add(auditType);
		   }
	    if (station != null) {
		      hql = hql + "and d.station = ? ";
		      params.add(station);
		   }
	    if (problemDegree != null) {
		      hql = hql + "and d.problemDegree = ? ";
		      params.add(problemDegree);
		   }
	    if (classGroup != null) {
		      hql = hql + "and d.classGroup = ? ";
		      params.add(classGroup);
		   }
	    if (id != null) {
	      hql = hql + "and d.id <> ?";
	      params.add(id);
	    }
	    Query query = this.ipqcAuditWarmingDao.getSession().createQuery(hql);
	    for (int i = 0; i < params.size(); i++) {
	      query.setParameter(i, params.get(i));
	    }
	
	    List<?> list = query.list();
	    if (Integer.valueOf(list.get(0).toString()).intValue() > 0) {
	      return true;
	    }
	    return false;
	  }
	}