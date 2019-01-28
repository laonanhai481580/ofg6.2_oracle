package com.ambition.aftersales.lar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.entity.LarData;
import com.ambition.aftersales.entity.LarDefectiveItem;
import com.ambition.aftersales.lar.dao.LarDataDao;
import com.ambition.aftersales.lar.dao.LarDefectiveItemDao;
import com.norteksoft.product.orm.Page;

/**
 * 
 * 类名:LAR批次合格率Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Service
@Transactional
public class LarDataManager {
	@Autowired
	private LarDataDao larDataDao;	
	@Autowired
	private LarDefectiveItemDao larDefectiveItemDao;
	public LarData getLarData(Long id){
		return larDataDao.get(id);
	}
	
	public void deleteLarData(LarData larData){
		larDataDao.delete(larData);
	}

	public Page<LarDefectiveItem> searchItem(Page<LarDefectiveItem>page,LarData larData){
		return larDefectiveItemDao.searchItem(page,larData);
	}
	public Page<LarData> search(Page<LarData>page){
		return larDataDao.search(page);
	}
	
	public Page<LarData> searchHide(Page<LarData>page){
		return larDataDao.searchHide(page);
	}
	
	public List<LarData> listAll(){
		return larDataDao.getAllLarData();
	}
		
	public void deleteLarData(Long id){
		larDataDao.delete(id);
	}
	public String deleteLarData(String ids) {
		StringBuilder sb = new StringBuilder("");
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			LarData  larData = larDataDao.get(Long.valueOf(id));
			if(larData.getId() != null){
				larDataDao.delete(larData);
				sb.append(larDataDao.get(Long.valueOf(id)).getCustomer() + ",");
			}
		}
		return sb.toString();
	}
	public void saveLarData(LarData larData){
		larDataDao.save(larData);
	}
	
	public String hiddenState(String hideId,String type){
		StringBuilder sb = new StringBuilder("");
		String[] ids = hideId.split(",");
		for(String id : ids){
			LarData larData = larDataDao.get(Long.valueOf(id));
			if("Y".equals(type)){
				larData.setHiddenState("N");
			}else{
				larData.setHiddenState("Y");
			}
			larDataDao.save(larData);
			sb.append(larData.getYearInt()+"-"+larData.getMonthInt()+"-"+larData.getCustomer()+ ",");
		}
		return sb.toString();
	}	
	
}
