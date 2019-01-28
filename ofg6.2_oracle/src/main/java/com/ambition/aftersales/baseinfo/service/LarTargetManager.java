package com.ambition.aftersales.baseinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.baseinfo.dao.LarTargetDao;
import com.ambition.aftersales.entity.LarTarget;
import com.norteksoft.product.orm.Page;

/**
 * 
 * 类名:Lar目标值维护Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月21日 发布
 */
@Service
@Transactional
public class LarTargetManager {
	@Autowired
	private LarTargetDao larTargetDao;	
	public LarTarget getLarTarget(Long id){
		return larTargetDao.get(id);
	}
	
	public void deleteLarTarget(LarTarget larTarget){
		larTargetDao.delete(larTarget);
	}

	public Page<LarTarget> search(Page<LarTarget>page){
		return larTargetDao.search(page);
	}

	public List<LarTarget> listAll(){
		return larTargetDao.getAllLarTarget();
	}
		
	public void deleteLarTarget(Long id){
		larTargetDao.delete(id);
	}
	public void deleteLarTarget(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			LarTarget  larTarget = larTargetDao.get(Long.valueOf(id));
			if(larTarget.getId() != null){
				larTargetDao.delete(larTarget);
			}
		}
	}
	public void saveLarTarget(LarTarget larTarget){
		larTargetDao.save(larTarget);
	}
	
}
