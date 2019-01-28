package com.ambition.carmfg.updatetime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.UpdateTimestamp;
import com.ambition.util.erp.dao.UpdateTimestampDao;
import com.norteksoft.product.orm.Page;

@Service
@Transactional
public class UpdateTimestampManager{

	  @Autowired
	  private UpdateTimestampDao updateTimestampDao;
	
	  public UpdateTimestamp getUpdateTimestamp(Long id)
	  {
	    return updateTimestampDao.get(id);
	  }
	
	  public void deleteUpdateTimestamp(UpdateTimestamp updateTimestamp) {
	    updateTimestampDao.delete(updateTimestamp);
	  }
	
	  public Page<UpdateTimestamp> search(Page<UpdateTimestamp> page) {
	    return updateTimestampDao.search(page);
	  }

	  public void saveUpdateTimestamp(UpdateTimestamp updateTimestamp) {
		    if (updateTimestamp.getUpdateTime()==null) {
			      throw new RuntimeException("最后更新时间不能为空!");
			    }
		    this.updateTimestampDao.save(updateTimestamp);
		  } 
	}