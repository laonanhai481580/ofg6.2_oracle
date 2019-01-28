package com.ambition.supplier.supplieryearinspect.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.entity.SupplierDeleteMark;
import com.ambition.supplier.entity.SupplierYearInspect;
import com.ambition.supplier.entity.SupplierYearInspectPlan;
import com.ambition.supplier.supplieryearinspect.dao.SupplierYearInspectDao;
import com.norteksoft.acs.base.enumeration.MailboxDeploy;
import com.norteksoft.acs.entity.authorization.Role;
import com.norteksoft.acs.entity.authorization.RoleUser;
import com.norteksoft.acs.entity.organization.Company;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.entity.organization.DepartmentUser;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.acs.entity.organization.UserInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.AsyncMailUtils;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.Md5;
import com.norteksoft.product.util.PropUtils;

/**
 * 
 * 类名:供应商稽核计划Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  
 * @version 1.00 
 */
@Service
@Transactional
public class SupplierYearInspectManager extends AmbWorkflowManagerBase<SupplierYearInspect>{
	@Autowired
	private SupplierYearInspectDao supplierYearInspectDao;	
	@Autowired
	private SupplierYearInspectPlanManager supplierYearInspectPlanManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Resource(name="tpdbDataJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Override
	public HibernateDao<SupplierYearInspect, Long> getHibernateDao() {
		return supplierYearInspectDao;
	}

	@Override
	public String getEntityListCode() {
		return SupplierYearInspect.ENTITY_LIST_CODE;
	}

	@Override
	public Class<SupplierYearInspect> getEntityInstanceClass() {
		return SupplierYearInspect.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "supplier_yearinspect";
	}

	@Override
	public String getWorkflowDefinitionName() {
		return "供应商稽核报告2.0";
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
			SupplierYearInspect report=getEntity(Long.valueOf(id));
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
	public void deleteEntity(SupplierYearInspect entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			ApiFactory.getInstanceService().deleteInstance(entity);
		}else{
			getHibernateDao().delete(entity);
		}
		//删掉对应稽核计划中的编号
		if(entity.getInspectId()!=null&&!"".equals(entity.getInspectId())){
			SupplierYearInspectPlan report=supplierYearInspectPlanManager.getSupplierYearInspectPlan(Long.valueOf(entity.getInspectId()));
			if(report!=null){
				report.setInspectId("");
				report.setInspectNo("");
			}			
		}
		//记录删除的信息到TP数据库中
		String companyName= PropUtils.getProp("companyName");
		if(!companyName.equals("")&&companyName!=null){
			String sql = "insert into SUPPLIER_DELETE_MARK (id,source_id,source_unit,source_table) values("+entity.getId()+","+entity.getId()+",'"+companyName+"','"+SupplierDeleteMark.SUPPLIER_YEAR_INSPECT_ALL+"')";
			jdbcTemplate.execute(sql);
		}
		
	}	
	
	public Page<SupplierYearInspect> listState(Page<SupplierYearInspect> page,String state ,String str,String code){
		String hql = " from SupplierYearInspect e where e.hiddenState=? and e.companyId=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(state);
		searchParams.add(ContextUtils.getCompanyId());
		if(str!=null){
			hql=hql+" and e.factoryClassify=?";
			searchParams.add(str);
		}
		if(code!=null){
			hql=hql+" and e.supplierCode=?";
			searchParams.add(code);
		}
		return supplierYearInspectDao.searchPageByHql(page, hql, searchParams.toArray());
	}
	public void setSupplierYearInspect(SupplierYearInspect report){
		try {
			SupplierYearInspectPlan supplierYearInspectPlan=supplierYearInspectPlanManager.getSupplierYearInspectPlan(Long.valueOf(report.getInspectId()));
			supplierYearInspectPlan.setAuditorState("稽核中");
			supplierYearInspectPlan.setInspectId(report.getId().toString());
			supplierYearInspectPlan.setInspectNo(report.getFormNo());
			supplierYearInspectPlan.setModifiedTime(new Date());
			supplierYearInspectPlanManager.saveSupplierYearInspectPlan(supplierYearInspectPlan);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Boolean auditSuppliers(String id){
		String sql = "select count(*) from SUPPLIER_YEAR_INSPECT s where s.inspect_Id=? ";
		Query query = supplierYearInspectDao.getSession().createSQLQuery(sql).setParameter(0, id);
		List<?> list = query.list();
		Integer mount = Integer.valueOf(list.get(0).toString());
		if(mount==0){
			return false;
		}else{
			return true;
		}
	}
	@SuppressWarnings("static-access")
	public String releaseEmail(String params ,String url) throws InterruptedException{
		params=emailFormat(params);
		String[] emails = params.split(";");
		String emailContent = "您有一份任务报告待处理!"+url;
		for(String email:emails){
			if(StringUtils.isNotEmpty(email)){
				AsyncMailUtils.sendMail(email,"稽核报告！",emailContent.toString());
				Thread.currentThread().sleep(10000);//毫秒
			}
		}
		return null;
	}
	@SuppressWarnings("static-access")
	public String releaseEmail1(String params,String url) throws InterruptedException{
		String emailContent = "您有一份任务报告待查看"+url;
		Set<String> names=new HashSet<String>();
		String[] sendMails = params.split(",");
		for (int i = 0; i < sendMails.length; i++) {
			names.add(sendMails[i]);
		}
		for(Iterator<String> nameIte = names.iterator(); nameIte.hasNext();) { 
			String name= nameIte.next().toString();
			List<com.norteksoft.product.api.entity.User> user=ApiFactory.getAcsService().getUsersByName(name);
			String email = user.get(0).getEmail();
			if(StringUtils.isNotEmpty(email)){
				AsyncMailUtils.sendMail(email,"稽核报告！",emailContent.toString());
				Thread.currentThread().sleep(10000);//毫秒
			}
		}
		return null;
	}
	public boolean isEmail(String email) {
	     String regexEmail1="^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
	     if(Pattern.matches(regexEmail1, email)){
	    	 return Pattern.matches(regexEmail1, email); 
	     }
		 return false;
	 } 
	public String emailFormat(String email) {  
		if(email.indexOf("；")!=-1){
			email=email.replaceAll("；", ";");
		} 
		if(email.indexOf(",")!=-1){
			email=email.replaceAll(",", ";");
		}
		if(email.indexOf("，")!=-1){
			email=email.replaceAll("，", ";");
		}
	       return email;
	 }
	//
	public Department searchSupplierDept() {
		String hql = "from Department d where  d.deleted=? and d.name=?";
		@SuppressWarnings("unchecked")
		List<Department> depts = supplierYearInspectDao.getSession()
				.createQuery(hql).setParameter(0, false).setParameter(1, "供应商").list();
		if(depts.size()==0){
			return null;
		}else{
			return depts.get(0);
		}
	}
	@SuppressWarnings("unchecked")
	public void saveUser(SupplierYearInspect report, Department department) {
		Company company = null;
		// 取到第一个公司
		if (company == null) {
			String hql = "from Company c";
			List<?> list = supplierYearInspectDao.getSession().createQuery(hql).list();
			company = (Company) list.get(0);
		}
		List<Role> defaultRoles = new ArrayList<Role>();
		String rolehql = "from Role r where r.deleted = ? and r.name = ?";
		defaultRoles = supplierYearInspectDao.getSession().createQuery(rolehql).setParameter(0, false).setParameter(1, "普通用户").list();
		String hql = "select user from User user where  user.loginName=?";
		List<?> users = supplierYearInspectDao.getSession().createQuery(hql).setParameter(0, report.getSupplierCode())
				.list();
		User user = new User();
		boolean isNew = false;
		if (users.size() <= 0) {
			user = new User();
			user.setCompanyId(company.getId());
			isNew = true;
			user.setLoginName(report.getSupplierCode().trim());
			user.setName(report.getSupplierName());
			user.setPassword(Md5.toMessageDigest(report.getSupplierCode().trim()));
			if(StringUtils.isNotEmpty(report.getSupplierEmail())){
				user.setEmail(report.getSupplierEmail());
			}
			user.setMailSize(10.0f);
			user.setHonorificName("");
			user.setMainDepartmentId(department == null ? null : department.getId());
			user.setMainDepartmentName(department == null ? null : department
					.getName());
			user.setMailboxDeploy(MailboxDeploy.INSIDE);
			supplierYearInspectDao.getSession().save(user);
		}else {
			user = (User) users.get(0);
		}
		if (isNew) {
			// 判断用户角色是否存在,RoleUser的在user,role建立了索引，不能重复
			for(Role role : defaultRoles){
				hql = "from RoleUser r where r.user = ? and r.role = ? ";
				List<?> roleUserList = supplierYearInspectDao.getSession().createQuery(hql).setParameter(0, user).setParameter(1, role).list();
				if (roleUserList.isEmpty()) {
					RoleUser roleUser = new RoleUser();
					roleUser.setAllUser("所有用户");
					roleUser.setCompanyId(company.getId());
					roleUser.setDeleted(false);
					roleUser.setRole(role);
					roleUser.setUser(user);
					roleUser.setTs(new Date());
					supplierYearInspectDao.getSession().save(roleUser);
				}
			}
			// 添加兼职部门
			List<?> departmentUsers = new ArrayList<DepartmentUser>();
			if (department != null) {
				hql = "from DepartmentUser d where d.user = ? and d.department = ?";
				departmentUsers = supplierYearInspectDao.getSession().createQuery(hql).setParameter(0, user)
						.setParameter(1, department).list();
			} else {
				hql = "from DepartmentUser d where d.user = ? and d.department is null";
				departmentUsers = supplierYearInspectDao.getSession().createQuery(hql).setParameter(0, user)
						.list();
			}
			if (departmentUsers.size() <= 0) {
				DepartmentUser departmentUser = new DepartmentUser();
				departmentUser.setCompanyId(company.getId());
				departmentUser.setUser(user);
				departmentUser.setDepartment(department);
				departmentUser.setDeleted(false);
				supplierYearInspectDao.getSession().save(departmentUser);
			}
			// 保存用户基础信息
			hql = "from UserInfo u where u.user.id=?";
			List<UserInfo> usersInfos = supplierYearInspectDao.getSession().createQuery(hql)
					.setParameter(0, user.getId()).list();
			UserInfo userInfo = new UserInfo();
			if (usersInfos.size() > 0) {
				userInfo = usersInfos.get(0);
			} else {
				userInfo = new UserInfo();
			}
			// userInfo.setTelephone(telephone);
			userInfo.setUser(user);
			userInfo.setCompanyId(user.getCompanyId());
			userInfo.setBirthday("");
			userInfo.setBloodGroup("");
			userInfo.setCityArea("");
			userInfo.setTelephone("");
			userInfo.setDegree("");
			userInfo.setEducationGrade("");
			userInfo.setFatherName("");
			userInfo.setFirstForeignLanguage("");
			userInfo.setGraduatedSchool("");
			userInfo.setPasswordUpdatedTime(new Date());
			userInfo.setTreatment("");
			// userInfo.setWeight("");
			supplierYearInspectDao.getSession().save(userInfo);
		}
		
	}
	
}
