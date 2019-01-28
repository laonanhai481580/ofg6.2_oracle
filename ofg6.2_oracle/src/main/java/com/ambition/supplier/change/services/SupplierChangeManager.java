package com.ambition.supplier.change.services;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.change.dao.SupplierChangeDao;
import com.ambition.supplier.entity.SupplierChange;
import com.ambition.supplier.entity.SupplierDeleteMark;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.wf.base.enumeration.CompleteTaskTipType;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年10月12日 发布
 */
@Service
@Transactional
public class SupplierChangeManager extends AmbWorkflowManagerBase<SupplierChange>{
	@Autowired
	private SupplierChangeDao supplierChangeDao;
	@Resource(name="tpdbDataJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Override
	public HibernateDao<SupplierChange, Long> getHibernateDao() {
		// TODO Auto-generated method stub
		return supplierChangeDao;
	}

	@Override
	public String getEntityListCode() {
		// TODO Auto-generated method stub
		return "SUPPLIER_CHANGE";
	}

	@Override
	public Class<SupplierChange> getEntityInstanceClass() {
		// TODO Auto-generated method stub
		return SupplierChange.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		// TODO Auto-generated method stub
		return "supplier-change";
	}

	@Override
	public String getWorkflowDefinitionName() {
		// TODO Auto-generated method stub
		return "PCN流程";
	}
	 /**
     * 方法名: 导出报告
     * <p>功能说明：</p>
     * @return
    */
   public void exportReport(Long entityId) throws IOException {
       exportReport(entityId,"supplier-change.xlsx","PCN申请单");
   }
	public Page<SupplierChange> listPageByParams(Page<SupplierChange> page,
			String name) {
		String hql = " from SupplierChange s where s.creator = ? and s.companyId=? ";
		return supplierChangeDao.searchPageByHql(page, hql ,name,ContextUtils.getCompanyId());
	}
	public Page<SupplierChange> search(Page<SupplierChange>page){
		return supplierChangeDao.search(page);		
	}
	public Page<SupplierChange> searchAll(Page<SupplierChange>page){
		return supplierChangeDao.searchAll(page);		
	}
	
	/**
	 * 删除实体，流程相关文件都删除
	 * @param ids
	 */
	public String deleteEntity(String ids) {
		String[] deleteIds = ids.split(",");
		StringBuilder sb = new StringBuilder("");
	     String message = "";
		for (String id : deleteIds) {
			SupplierChange report=getEntity(Long.valueOf(id));
			deleteEntity(report);
			sb.append(report.getFormNo() + ",");
		}
		message =sb.toString();
		return   message;
	}

	/**
	 * 删除实体，流程相关文件都删除
	 * @param entity 删除的对象
	 */
	public void deleteEntity(SupplierChange entity){
		if(entity.getWorkflowInfo()!=null){
			ApiFactory.getInstanceService().deleteInstance(entity);
		}else{
			getHibernateDao().delete(entity);
		}
		//记录删除的信息到TP数据库中
		String companyName= PropUtils.getProp("companyName");
		if(!companyName.equals("TP")){
			String sql = "insert into SUPPLIER_DELETE_MARK (id,source_id,source_unit,source_table) values("+entity.getId()+","+entity.getId()+",'"+companyName+"','"+SupplierDeleteMark.SUPPLIER_CHANGE+"')";
			jdbcTemplate.execute(sql);
		}		
	}	
	
	
	
	public CompleteTaskTipType submitProcessSelf(SupplierChange supplierChange) {
		// TODO Auto-generated method stub
	   saveEntity(supplierChange);
       Long processId = ApiFactory.getDefinitionService()
               .getWorkflowDefinitionsByCode(getWorkflowDefinitionCode()).get(0).getId();
       return ApiFactory.getInstanceService().submitInstance(processId, supplierChange);
	}

}
