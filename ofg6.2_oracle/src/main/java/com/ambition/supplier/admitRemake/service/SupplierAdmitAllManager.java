package com.ambition.supplier.admitRemake.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gp.entity.GpMaterial;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.admitRemake.dao.SupplierAdmitAllDao;
import com.ambition.supplier.entity.SupplierAdmitAll;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 
 * 类名:材料承认汇总Services
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  lpf
 * @version 1.00 2018年9月13日 发布
 */
@Service
@Transactional
public class SupplierAdmitAllManager extends AmbWorkflowManagerBase<SupplierAdmitAll>{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SupplierAdmitAllDao supplierAdmitAllDao;
	@Override
	public Class<SupplierAdmitAll> getEntityInstanceClass() {
		return SupplierAdmitAll.class;
	}

	@Override
	public String getEntityListCode() {
		return SupplierAdmitAll.ENTITY_LIST_CODE;
	}

	@Override
	public HibernateDao<SupplierAdmitAll, Long> getHibernateDao() {
		return supplierAdmitAllDao;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "supplier_supplier_admit_all";
	}

	@Override
	public String getWorkflowDefinitionName() {
		return "材料承认管理流程汇总";
	}

	public Page<SupplierAdmitAll> listState(Page<SupplierAdmitAll> page,String code,String state){
		String hql = " from SupplierAdmitAll e where  e.companyId=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		if(code!=null ){
			hql=hql+" and e.supplierCode=?";
			searchParams.add(code);
		}
		if(state!=null ){
			hql=hql+" and e.adminState=?";
			searchParams.add(state);
		}
		return supplierAdmitAllDao.searchPageByHql(page, hql, searchParams.toArray());
	}
	public String getMaterialsFile(String name){
		String sql= "select materials_File from SUPPLIER_MATERIALS_FILE t where t.file_Name =?";
		Query query = supplierAdmitAllDao.getSession().createSQLQuery(sql).setParameter(0, name);
		List<?> list = query.list();
		if(!list.isEmpty() && list.get(0)!=null){
			String materialsFile = list.get(0).toString();
			String file = getStringFormat(materialsFile);
			return file;
		}
		return null;
	}
	public String getStringFormat(String str){
		int index = str.indexOf("|");
		index = str.indexOf("|", index+1);
		String date1=str.substring(index+1);
		return date1;
	}
	
	public SupplierAdmitAll getSupplierAdmitAll(String supplierCode,String checkBomCode){
		String hql = "from SupplierAdmitAll s where  s.materialCode = ? and s.supplierCode=? order by s.id desc ";
		List<SupplierAdmitAll> supplierAdmits = supplierAdmitAllDao.find(hql,checkBomCode,supplierCode);
		if(supplierAdmits.isEmpty()){
			return null;
		}else{
			return supplierAdmits.get(0);
		}
	}	

	public List<SupplierAdmitAll> SupplierAdmitAllMaterialCode(String materialCode ,String supplierCode,Long id){
		return supplierAdmitAllDao.getSupplierAdmitAllMaterialCode(materialCode,supplierCode,id);
	}


	public String findSupplierMaterial(GpMaterial gpMaterial){
		String hql="from SupplierAdmitAll s where s.companyId=? and s.supplier_Code = ? and s.material_Code = ? ";
		List<SupplierAdmitAll> supplierAdmits=supplierAdmitAllDao.find(hql, gpMaterial.getSupplierCode(),gpMaterial.getMaterialCode().trim());
		if(supplierAdmits.size()>0){
			SupplierAdmitAll report=supplierAdmits.get(0);
			report.setGpMaterialNo(gpMaterial.getFormNo());
			report.setGpMaterialId(gpMaterial.getId().toString());
			report.setGpMaterialState(gpMaterial.getTaskProgress());
			supplierAdmitAllDao.save(report);
		}
		return null;
	}
}
