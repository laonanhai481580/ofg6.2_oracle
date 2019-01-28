package com.ambition.supplier.task.dao;

import org.springframework.stereotype.Repository;

import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.task.base.enumeration.TaskState;
import com.norteksoft.task.entity.Task;

@Repository
public class SupplierTaskDao extends HibernateDao<Task, Long>{
	public Page<Task> getAllTasksByUserType(Page<Task> page){
		String hql=null;
		hql="from Task t where t.companyId = ? and ((t.transactor = ? and t.transactorId is null) or (t.transactorId is not null and t.transactorId=?)) and t.visible = true and (t.active=? or t.active=? or t.active=?  or t.active=?)  and t.paused=? order by t.createdTime desc";
		return searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),ContextUtils.getUserId(),TaskState.DRAW_WAIT.getIndex(), TaskState.WAIT_TRANSACT.getIndex(), TaskState.WAIT_DESIGNATE_TRANSACTOR.getIndex(),TaskState.WAIT_CHOICE_TACHE.getIndex(),false);
	}
	
	
}
