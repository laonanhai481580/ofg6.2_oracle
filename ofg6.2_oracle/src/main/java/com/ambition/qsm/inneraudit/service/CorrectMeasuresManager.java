package com.ambition.qsm.inneraudit.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.qsm.entity.CorrectMeasures;
import com.ambition.qsm.inneraudit.dao.CorrectMeasuresDao;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 类名:不符合与纠正措施报告Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年10月27日 发布
 */
@Service
@Transactional
public class CorrectMeasuresManager extends AmbWorkflowManagerBase<CorrectMeasures>{
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private CorrectMeasuresDao CorrectMeasuresDao;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Override
	public Class<CorrectMeasures> getEntityInstanceClass() {
		return CorrectMeasures.class;
	}

	@Override
	public String getEntityListCode() {
		return CorrectMeasures.ENTITY_LIST_CODE;
	}

	@Override
	public HibernateDao<CorrectMeasures, Long> getHibernateDao() {
		return CorrectMeasuresDao;
	}

	@Override
	public String getWorkflowDefinitionCode() {
		return "correct-measures";
	}

	@Override
	public String getWorkflowDefinitionName() {
		return "不符合与纠正措施报告";
	}
	@Override
	public void exportReport(Long entityId) throws IOException{
		exportReport(entityId, "correct-measures.xlsx", CorrectMeasures.ENTITY_LIST_NAME);
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
			CorrectMeasures report=getEntity(Long.valueOf(id));
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
	@Override
	public void deleteEntity(CorrectMeasures entity){
		if(entity.getWorkflowInfo()!=null){
			String workflowId =  entity.getWorkflowInfo().getWorkflowId();
			//先删除子表
			Long reportId = entity.getId();
			String sql31 = "delete from QSM_CORRECT_MEASURES_ITEM where QSM_CORRECT_MEASURES_ID = ?";	
			String sql21 = "delete from QSM_SIGN_REASON_ITEM where QSM_CORRECT_MEASURES_ID = ?";
			getHibernateDao().getSession().createSQLQuery(sql31)
			.setParameter(0,reportId)
			.executeUpdate();
			getHibernateDao().getSession().createSQLQuery(sql21)
			.setParameter(0,reportId)
			.executeUpdate();
			String sql41 = "delete from QSM_SIGN_MEASURE_ITEM where QSM_CORRECT_MEASURES_ID = ?";
			getHibernateDao().getSession().createSQLQuery(sql41)
			.setParameter(0,reportId)
			.executeUpdate();
			String sql1 = "delete from QSM_SIGN_COMPLETE_ITEM where QSM_CORRECT_MEASURES_ID = ?";
			getHibernateDao().getSession().createSQLQuery(sql1)
			.setParameter(0,reportId)
			.executeUpdate();
			String sql = "delete from product_task_all_his where execution_id = ?";
			getHibernateDao().getSession().createSQLQuery(sql)
			.setParameter(0,workflowId)
			.executeUpdate();
			getHibernateDao().delete(entity);
			ApiFactory.getInstanceService().deleteInstance(entity);
		}else{
			getHibernateDao().delete(entity);
		}
	}
	/**
	 * 查询所有部门
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Department> queryAllDepartments(){
		String hql = "from Department d where d.deleted = 0 order by d.parent desc,weight";
		List<Department> list = CorrectMeasuresDao.createQuery(hql).list();
		return list;
	}
	
	/**
	 * 查询所有用户
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> queryAllUsers(){
		String hql = "from User d where d.deleted = 0 order by d.weight";
		List<User> list = CorrectMeasuresDao.createQuery(hql).list();
		return list;
	}
	public String importDatas(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		//表单字段
		Map<String,String> fieldMap = this.getFieldMap("QSM_CORRECT_MEASURES");
		Workbook book = WorkbookFactory.create(new FileInputStream(file));
		
		Sheet sheet = book.getSheetAt(0);
		Row row = sheet.getRow(0);
		if(row == null){
			throw new RuntimeException("第一行不能为空!");
		}
		
		Map<String,Integer> columnMap = new HashMap<String,Integer>();
		for(int i=0;;i++){
			Cell cell = row.getCell(i);
			if(cell==null){
				break;
			}
			String value = cell.getStringCellValue();
			if(fieldMap.containsKey(value)){
				columnMap.put(value,i);
			}
		}
		/*if(columnMap.keySet().size() != fieldMap.keySet().size()){
			throw new AmbFrameException("Excel格式不正确!请重新导出台账数据模板!");
		}*/
		
		DecimalFormat df = new DecimalFormat("#.##");
		Iterator<Row> rows = sheet.rowIterator();
		rows.next();//标题行
		int i = 0;
		while(rows.hasNext()){
			row = rows.next();
			try {
				Map<String,Object> objMap = new HashMap<String, Object>();
				for(String columnName : columnMap.keySet()){
					Cell cell = row.getCell(columnMap.get(columnName));
					if(cell != null){
						Object value = null;
						if(Cell.CELL_TYPE_STRING == cell.getCellType()){
							value = cell.getStringCellValue();
						}else if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								value = cell.getDateCellValue();
							} else {
								value = df.format(cell.getNumericCellValue());
							}
						}else if(Cell.CELL_TYPE_FORMULA == cell.getCellType()){
							value = cell.getCellFormula();
						}
						objMap.put(fieldMap.get(columnName),value);
					}
				}
				CorrectMeasures correctMeasures = new CorrectMeasures();
				correctMeasures.setCompanyId(ContextUtils.getCompanyId());
				correctMeasures.setCreatedTime(new Date());
				correctMeasures.setCreator(ContextUtils.getUserName());
				correctMeasures.setModifiedTime(new Date());
				correctMeasures.setModifier(ContextUtils.getUserName());
				correctMeasures.setFormNo(formCodeGenerated.generateCorrectMeasuresNo());
				correctMeasures.setHiddenState("N");
				com.norteksoft.product.api.entity.User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
				String subName=user.getSubCompanyName();
				correctMeasures.setFactoryClassify(subName);
				for(String key : objMap.keySet()){
					CommonUtil.setProperty(correctMeasures,key, objMap.get(key));
				}
				CorrectMeasuresDao.save(correctMeasures);
			   sb.append("第" + (i+1) + "行导入成功!<br/>");
			} catch (Exception e) {
				e.printStackTrace();
				sb.append("第" + (i+1) + "行导入失败:<font color=red>" + e.getMessage() + "</font><br/>");
			}
			i++;
		}
		file.delete();
		return sb.toString();
	}
	
	/**
	  * 方法名:获取字段映射 
	  * <p>功能说明：</p>
	  * @return
	 */
	public Map<String,String> getFieldMap(String listCode){
		Map<String,String> fieldMap = new HashMap<String, String>();
		ListView columns = ApiFactory.getMmsService().getListViewByCode(listCode);
		for(ListColumn column: columns.getColumns()){
			if(column.getVisible()){
				fieldMap.put(column.getHeaderName(), column.getTableColumn().getName());
			}
		}
		return fieldMap;
	}
	public Page<CorrectMeasures> listState(Page<CorrectMeasures> page,String state ,String str){
		String hql = " from CorrectMeasures e where e.hiddenState=? and e.companyId=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(state);
		searchParams.add(ContextUtils.getCompanyId());
		if(str!=null ){
			hql=hql+" and e.factoryClassify=?";
			searchParams.add(str);
		}
		return CorrectMeasuresDao.searchPageByHql(page, hql, searchParams.toArray());
	}
}
