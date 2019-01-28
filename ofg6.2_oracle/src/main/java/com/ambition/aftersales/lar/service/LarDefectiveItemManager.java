package com.ambition.aftersales.lar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.entity.LarData;
import com.ambition.aftersales.entity.LarDefectiveItem;
import com.ambition.aftersales.lar.dao.LarDefectiveItemDao;
import com.norteksoft.product.orm.Page;

/**
 * 
 * 类名:LAR批次合格率不良信息Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Service
@Transactional
public class LarDefectiveItemManager {	
	@Autowired
	private LarDefectiveItemDao larDefectiveItemDao;
	public LarDefectiveItem getLarDefectiveItem(Long id){
		return larDefectiveItemDao.get(id);
	}
	
	public void deleteLarDefectiveItem(LarDefectiveItem larDefectiveItem){
		larDefectiveItemDao.delete(larDefectiveItem);
	}

	public Page<LarDefectiveItem> searchItem(Page<LarDefectiveItem> page,LarData larData){
		return larDefectiveItemDao.searchItem(page,larData);
	}		
	public void deleteLarDefectiveItem(Long id){
		larDefectiveItemDao.delete(id);
	}
	public String deleteLarDefectiveItem(String ids) {
		StringBuilder sb = new StringBuilder("");
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			LarDefectiveItem  larDefectiveItem = larDefectiveItemDao.get(Long.valueOf(id));
			if(larDefectiveItem.getId() != null){
				larDefectiveItemDao.delete(larDefectiveItem);
			}
		}
		return sb.toString();
	}
	public void saveLarDefectiveItem(LarDefectiveItem larDefectiveItem){
		larDefectiveItemDao.save(larDefectiveItem);
	}	
}
