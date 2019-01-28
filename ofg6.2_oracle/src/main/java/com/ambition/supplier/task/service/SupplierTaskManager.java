package com.ambition.supplier.task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.task.dao.SupplierTaskDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.task.entity.Task;

@Service
@Transactional
public class SupplierTaskManager {
	@Autowired
	private SupplierTaskDao taskDao;
	/**
	 * 分页查询用户所有未完成任务
	 * @param page
	 */
	public Page<Task> getAllTasksByUserType(Page<Task> page){
		return taskDao.getAllTasksByUserType(page);
	}
}
