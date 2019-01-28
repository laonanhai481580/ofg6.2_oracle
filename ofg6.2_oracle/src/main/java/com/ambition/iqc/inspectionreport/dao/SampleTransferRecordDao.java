package com.ambition.iqc.inspectionreport.dao;

import org.springframework.stereotype.Repository;

import com.ambition.iqc.entity.SampleTransferRecord;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

/**
 * 检验规则变更
 * @author 赵骏
 *
 */
@Repository
public class SampleTransferRecordDao extends HibernateDao<SampleTransferRecord, Long> {
	
	/**
	 * 查询规则转换记录
	 * @param page
	 * @return
	 */
	public Page<SampleTransferRecord> search(Page<SampleTransferRecord> page){
		 String businessUnitName = CommonUtil.getDataByLogin();
		  if(businessUnitName!=null){
			  return searchPageByHql(page, "from SampleTransferRecord s where s.companyId = ? and s.businessUnitName=? order by s.auditState desc,s.createdTime desc",ContextUtils.getCompanyId(),businessUnitName);
		  }else{
			  return searchPageByHql(page, "from SampleTransferRecord s where s.companyId = ? order by s.auditState desc,s.createdTime desc",ContextUtils.getCompanyId());
		  }
		  
	}
}
	
