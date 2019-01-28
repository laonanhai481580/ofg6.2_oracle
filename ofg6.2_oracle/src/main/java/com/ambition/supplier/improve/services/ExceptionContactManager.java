package com.ambition.supplier.improve.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.iqc.inspectionreport.service.IncomingInspectionActionsReportManager;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.entity.ExceptionContact;
import com.ambition.supplier.entity.ExceptionImprove;
import com.ambition.supplier.entity.SupplierDeleteMark;
import com.ambition.supplier.improve.dao.ExceptionContactDao;
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
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.Md5;
import com.norteksoft.product.util.PropUtils;

/**    
 * 物料异常联络单
 * @authorBy LPF
 *
 */
@Service
@Transactional
public class ExceptionContactManager extends AmbWorkflowManagerBase<ExceptionContact>{
	@Autowired
	private ExceptionContactDao exceptionContactDao;
	@Autowired
    private FormCodeGenerated formCodeGenerated;
	@Autowired
	private IncomingInspectionActionsReportManager iiarManager;
	@Autowired
	private ExceptionImproveManager exceptionImproveManager;
	@Resource(name="tpdbDataJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Override
	public HibernateDao<ExceptionContact, Long> getHibernateDao() {
		return exceptionContactDao;
	}

	@Override
	public String getEntityListCode() {		
		return "SUPPLIER_EXCEPTION_CONTACT";
	}

	@Override
	public Class<ExceptionContact> getEntityInstanceClass() {
		return ExceptionContact.class;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "supplier-exception-contact";
	}
	@Override
	public String getWorkflowDefinitionName() {
		return "物料异常联络单";
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "supplier-exception-contact.xlsx", "物料异常联络单");
	}

	public Department searchSupplierDept() {
		String hql = "from Department d where  d.deleted=? and d.name=?";
		@SuppressWarnings("unchecked")
		List<Department> depts = exceptionContactDao.getSession()
				.createQuery(hql).setParameter(0, false).setParameter(1, "供应商").list();
		if(depts.size()==0){
			return null;
		}else{
			return depts.get(0);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void saveUser(ExceptionContact report, Department department) {
			Company company = null;
			// 取到第一个公司
			if (company == null) {
				String hql = "from Company c";
				List<?> list = exceptionContactDao.getSession().createQuery(hql).list();
				company = (Company) list.get(0);
			}
			List<Role> defaultRoles = new ArrayList<Role>();
			String rolehql = "from Role r where r.deleted = ? and r.name = ?";
			defaultRoles = exceptionContactDao.getSession().createQuery(rolehql).setParameter(0, false).setParameter(1, "普通用户").list();
			String hql = "select user from User user where  user.loginName=? ";
			List<?> users = exceptionContactDao.getSession().createQuery(hql).setParameter(0, report.getSupplierCode().trim())
					.list();
			User user = new User();
			boolean isNew = false;
			if (users.size() <= 0) {
				user = new User();
				user.setCompanyId(company.getId());
				user.setSubCompanyName(company.getName());
				isNew = true;
				user.setLoginName(report.getSupplierCode());
				user.setName(report.getSupplierName());
				user.setPassword(Md5.toMessageDigest(report.getSupplierCode().trim()));
				if(StringUtils.isNotEmpty(report.getSupplierEmail())){
					user.setEmail(report.getSupplierEmail().split("/")[0]);
				}
				user.setMailSize(10.0f);
				user.setHonorificName("");
				user.setMainDepartmentId(department == null ? null : department.getId());
				user.setMainDepartmentName(department == null ? null : department
						.getName());
				user.setMailboxDeploy(MailboxDeploy.INSIDE);
				exceptionContactDao.getSession().save(user);
			}else {
				user = (User) users.get(0);
			}
			if (isNew) {
				// 判断用户角色是否存在,RoleUser的在user,role建立了索引，不能重复
				for(Role role : defaultRoles){
					hql = "from RoleUser r where r.user = ? and r.role = ? ";
					List<?> roleUserList = exceptionContactDao.getSession().createQuery(hql).setParameter(0, user).setParameter(1, role).list();
					if (roleUserList.isEmpty()) {
						RoleUser roleUser = new RoleUser();
						roleUser.setAllUser("所有用户");
						roleUser.setCompanyId(company.getId());
						roleUser.setDeleted(false);
						roleUser.setRole(role);
						roleUser.setUser(user);
						roleUser.setTs(new Date());
						exceptionContactDao.getSession().save(roleUser);
					}
				}
			}
			// 添加兼职部门
			List<?> departmentUsers = new ArrayList<DepartmentUser>();
			if (department != null) {
				hql = "from DepartmentUser d where d.user = ? and d.department = ?";
				departmentUsers = exceptionContactDao.getSession().createQuery(hql).setParameter(0, user)
						.setParameter(1, department).list();
			} else {
				hql = "from DepartmentUser d where d.user = ? and d.department is null";
				departmentUsers = exceptionContactDao.getSession().createQuery(hql).setParameter(0, user)
						.list();
			}
			if (departmentUsers.size() <= 0) {
				DepartmentUser departmentUser = new DepartmentUser();
				departmentUser.setCompanyId(company.getId());
				departmentUser.setUser(user);
				departmentUser.setDepartment(department);
				departmentUser.setDeleted(false);
				exceptionContactDao.getSession().save(departmentUser);
			}
			// 保存用户基础信息
			hql = "from UserInfo u where u.user.id=?";
			List<UserInfo> usersInfos = exceptionContactDao.getSession().createQuery(hql)
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
			exceptionContactDao.getSession().save(userInfo);
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
			ExceptionContact report=getEntity(Long.valueOf(id));
			deleteEntity(report);
			sb.append(report.getFormNo() + ",");
		}
		message =sb.toString();
		return   message;
	}	
	
	/**
	  * 方法名: 保存对象
	  * <p>功能说明：</p>
	  * @param report
	  * @param childMaps 子表对象
	  * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 */
	public void saveEntity(ExceptionContact report,Map<String,List<JSONObject>> childMaps){
		//数据处理
		if(report.getTempCountermeasures()!=null){
			report.setTempCountermeasures(report.getTempCountermeasures().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getTrueReasonCheck()!=null){
			report.setTrueReasonCheck(report.getTrueReasonCheck().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getCountermeasures()!=null){
			report.setCountermeasures(report.getCountermeasures().replaceAll("", "").replaceAll("", ""));
		}
		if(report.getPreventHappen()!=null){
			report.setPreventHappen(report.getPreventHappen().replaceAll("", "").replaceAll("", ""));
		}	
		//更新mrb单号和退货单号到异常纠正单
		if(report.getExceptionImproveId()!=null&&!"".equals(report.getExceptionImproveId())){
			ExceptionImprove exceptionImprove=exceptionImproveManager.getEntity(Long.valueOf(report.getExceptionImproveId()));
			if(exceptionImprove!=null&&(StringUtils.isNotEmpty(report.getReturnReportNo())||StringUtils.isNotEmpty(report.getSqeMrbReportNo()))){
				exceptionImprove.setSqeProcessOpinion(report.getSqeProcessOpinion());
				exceptionImprove.setReturnReportNo(report.getReturnReportNo());
				exceptionImprove.setSqeMrbReportNo(report.getSqeMrbReportNo());
			}
		}
		report.setModifiedTime(new Date());
		report.setModifier(ContextUtils.getLoginName());
		report.setModifierName(ContextUtils.getUserName());
		exceptionContactDao.save(report);	
		//设置子表的值
		setChildItems(report,childMaps);
		
	}
	//验证并保存记录
	public boolean isExistExceptionContact(Long id){
		String hql = "select count(*) from ExceptionContact d where d.companyId =? and d.inspectionId=?   ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());		
		params.add(id.toString());
		Query query = exceptionContactDao.getSession().createQuery(hql);
		for(int i = 0;i < params.size(); i++){
			query.setParameter(i, params.get(i));
		}
		@SuppressWarnings("rawtypes")
		List list = query.list();
		if(Integer.valueOf(list.get(0).toString()) > 0){
			return true;
		}else{
			return false;
		}		
	}
	public Page<ExceptionContact> searchSingle(Page<ExceptionContact>page){
		return exceptionContactDao.searchSingle(page);		
	}
	public Page<ExceptionContact> searchUnSingle(Page<ExceptionContact>page){
		return exceptionContactDao.searchUnSingle(page);		
	}
	public Page<ExceptionContact> search(Page<ExceptionContact>page){
		return exceptionContactDao.search(page);		
	}
	public Page<ExceptionContact> searchAll(Page<ExceptionContact>page){
		return exceptionContactDao.searchAll(page);		
	}
	public Page<ExceptionContact> searchUn(Page<ExceptionContact>page){
		return exceptionContactDao.search(page);		
	}
	public Page<ExceptionContact> searchHide(Page<ExceptionContact>page){
		return exceptionContactDao.searchHide(page);		
	}
	public Page<ExceptionContact> searchSupplierSingle(Page<ExceptionContact>page){
		return exceptionContactDao.searchSupplierSingle(page);		
	}
	public Page<ExceptionContact> searchUnSupplierSingle(Page<ExceptionContact>page){
		return exceptionContactDao.searchUnSupplierSingle(page);		
	}
	public String hiddenState(String hideId,String type){
		StringBuilder sb = new StringBuilder("");
		String[] ids = hideId.split(",");
		for(String id : ids){
			ExceptionContact exceptionContact = exceptionContactDao.get(Long.valueOf(id));
			if("Y".equals(type)){
				exceptionContact.setHiddenState("N");
			}else{
				exceptionContact.setHiddenState("Y");
			}
			exceptionContactDao.save(exceptionContact);
			sb.append(exceptionContact.getFormNo() + ",");
		}
		return sb.toString();
	}
	
	/**
	 * 删除实体，流程相关文件都删除
	 * @param entity 删除的对象
	 */
	public void deleteEntity(ExceptionContact entity){
		if(entity.getWorkflowInfo()!=null){
			//String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			ApiFactory.getInstanceService().deleteInstance(entity);
		}else{
			getHibernateDao().delete(entity);
		}
		//删掉对应IQC报告中的编号
		if(entity.getInspectionId()!=null&&!"".equals(entity.getInspectionId())){
			IncomingInspectionActionsReport report=iiarManager.getIncomingInspectionActionsReport(Long.valueOf(entity.getInspectionId()));
			if(report!=null){
				report.setExceptionId("");
				report.setExceptionNo("");
				report.setSqeReplyTime(null);
				report.setSqeCompleteTime(null);
			}			
		}
		//删掉对应矫正单中的编号
		if(entity.getExceptionImproveId()!=null&&!"".equals(entity.getExceptionImproveId())){
			ExceptionImprove report=exceptionImproveManager.getEntity(Long.valueOf(entity.getExceptionImproveId()));
			if(report!=null){
				report.setExceptionContactId("");
				report.setExceptionContactNo("");
			}			
		}
		//记录删除的信息到TP数据库中
		String companyName= PropUtils.getProp("companyName");
		if(!companyName.equals("TP")){
			String sql = "insert into SUPPLIER_DELETE_MARK (id,source_id,source_unit,source_table) values("+entity.getId()+","+entity.getId()+",'"+companyName+"','"+SupplierDeleteMark.SUPPLIER_EXCEPTION_CONTACT+"')";
			jdbcTemplate.execute(sql);
		}		
	}	
	
}
