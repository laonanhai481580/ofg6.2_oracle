package com.ambition.supplier.supplieryearinspect.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;








import javax.annotation.Resource;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.archives.dao.SupplierDao;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.entity.SupplierDeleteMark;
import com.ambition.supplier.entity.SupplierYearInspectPlan;
import com.ambition.supplier.supplieryearinspect.dao.SupplierYearInspectPlanDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Service
@Transactional
public class SupplierYearInspectPlanManager {
	@Autowired
	private SupplierYearInspectPlanDao supplierYearInspectPlanDao;
	@Autowired
	private SupplierDao supplierDao;
	@Resource(name="tpdbDataJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	public SupplierYearInspectPlan getSupplierYearInspectPlan(Long id){
		return supplierYearInspectPlanDao.get(id);
	}
	public void saveSupplierYearInspectPlan(SupplierYearInspectPlan supplierYearInspectPlan){
		supplierYearInspectPlanDao.save(supplierYearInspectPlan);
	}
	
	public Page<SupplierYearInspectPlan> list(Page<SupplierYearInspectPlan> page){
		return supplierYearInspectPlanDao.list(page);
	}
	
	public List<SupplierYearInspectPlan> listAll(){
		return supplierYearInspectPlanDao.getSupplierYearInspectPlan();
	}
	
	
	public void deleteMaintain(String deleteIds){
		String[] ids = deleteIds.split(",");
 		for(String id : ids){
 			SupplierYearInspectPlan supplierYearInspectPlan=supplierYearInspectPlanDao.get(Long.valueOf(id));
			if(supplierYearInspectPlan.getId() != null){
				supplierYearInspectPlanDao.delete(supplierYearInspectPlan);
				//记录删除的信息到TP数据库中
				String companyName= PropUtils.getProp("companyName");
				if(!companyName.equals("")&&companyName!=null){
					String sql = "insert into SUPPLIER_DELETE_MARK (id,source_id,source_unit,source_table) values("+supplierYearInspectPlan.getId()+","+supplierYearInspectPlan.getId()+",'"+companyName+"','"+SupplierDeleteMark.SUPPLIER_YEAR_INSPECT_PLAN_ALL+"')";
					jdbcTemplate.execute(sql);
				}
			}
		}
	}
	
	public Page<SupplierYearInspectPlan> search(Page<SupplierYearInspectPlan> page){
		return supplierYearInspectPlanDao.search(page);
	}
	public Page<SupplierYearInspectPlan> listState(Page<SupplierYearInspectPlan> page,String state ,String str){
		String hql = " from SupplierYearInspectPlan e where e.gsmState=? ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(state);
		if(str!=null ){
			hql=hql+" and e.factoryClassify=?";
			searchParams.add(str);
		}
		return supplierYearInspectPlanDao.searchPageByHql(page, hql, searchParams.toArray());
	}
	public String sendAuditSuppliers(String id) {
		Supplier supplier=supplierDao.get(Long.valueOf(id));
		Calendar a=Calendar.getInstance();
		Boolean auds=auditSuppliers(a.get(Calendar.YEAR),supplier.getCode());
		String ex="";
			if(auds==true){
				SupplierYearInspectPlan supplierYearInspectPlan = new SupplierYearInspectPlan();
				supplierYearInspectPlan.setCreationYear(a.get(Calendar.YEAR));//年份
				supplierYearInspectPlan.setSupplierName(supplier.getName());//名称
				supplierYearInspectPlan.setSupplierCode(supplier.getCode());//编码
				supplierYearInspectPlan.setAddress(supplier.getAddress());//供应商地址
				supplierYearInspectPlan.setSupplierEmail(supplier.getSupplierEmail());//供应商邮箱地址
	//			supplierYearInspectPlan.setSupplyFactory("");//供应商厂区
				supplierYearInspectPlan.setSupplyMaterial(supplier.getSupplyMaterial());//供应物料
				supplierYearInspectPlan.setMaterialType(supplier.getMaterialType());//材料类别
				supplierYearInspectPlan.setAuditorState("待稽核");
				supplierYearInspectPlanDao.save(supplierYearInspectPlan);
				ex="["+supplier.getCode()+"] 生成成功!";
			}else{
				ex="["+supplier.getCode()+"]  "+a.get(Calendar.YEAR)+"年的计划已生成!";
			}
			return ex;
	}
	public Boolean auditSuppliers(Integer year,String code){
		String sql = "select count(*) from SUPPLIER_YEAR_INSPECT_PLAN s where s.creation_Year=? and s.supplier_Code=?";
		Query query = supplierYearInspectPlanDao.getSession().createSQLQuery(sql).setParameter(0, year).setParameter(1, code);
		List<?> list = query.list();
		Integer mount = Integer.valueOf(list.get(0).toString());
		if(mount>=1){
			return false;
		}else{
			return true;
		}
	}
	public Page<SupplierYearInspectPlan> getSupplierYearCheckViewPage(Page<SupplierYearInspectPlan> page) {
        String auditYear = Struts2Utils.getParameter("auditYear");
//        Integer auditYear1 = Integer.valueOf(auditYear);
        List<Object> searchParams = new ArrayList<Object>();
        String hql = " from SupplierYearInspectPlan s where s.companyId=? ";
        searchParams.add(ContextUtils.getCompanyId());
		if(auditYear!=null ){
			hql=hql+" and s.creationYear = ? ";
			searchParams.add(Integer.valueOf(auditYear));
		}
        return supplierYearInspectPlanDao.searchPageByHql(page, hql, searchParams.toArray());
	}
	public void addInspectId(String id){
		SupplierYearInspectPlan supplierYearInspectPlan=supplierYearInspectPlanDao.get(Long.valueOf(id));
		supplierYearInspectPlan.setPlanState("已发起");
		supplierYearInspectPlan.setModifiedTime(new Date());
		supplierYearInspectPlanDao.getSession().save(supplierYearInspectPlan);
	}
}
