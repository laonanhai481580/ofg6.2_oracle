package com.ambition.gsm.equipment.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.gsm.entity.InternalAuditor;
import com.ambition.gsm.equipment.dao.InternalAuditorDao;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

/**
 * 
 * 类名:内审员Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2018年7月26日 发布
 */
@Service
@Transactional
public class InternalAuditorManager {
	@Autowired
	private InternalAuditorDao internalAuditorDao;	
	public InternalAuditor getInternalAuditor(Long id){
		return internalAuditorDao.get(id);
	}
	
	public void deleteInternalAuditor(InternalAuditor internalAuditor){
		internalAuditorDao.delete(internalAuditor);
	}

	public Page<InternalAuditor> search(Page<InternalAuditor>page){
		return internalAuditorDao.search(page);
	}

	public List<InternalAuditor> listAll(){
		return internalAuditorDao.getAllInternalAuditor();
	}
		
	public void deleteInternalAuditor(Long id){
		internalAuditorDao.delete(id);
	}
	public String deleteInternalAuditor(String ids) {
		String[] deleteIds = ids.split(",");
		StringBuilder sb = new StringBuilder("");
		for (String id : deleteIds) {
			InternalAuditor  internalAuditor = internalAuditorDao.get(Long.valueOf(id));
			if(internalAuditor.getId() != null){
				internalAuditorDao.delete(internalAuditor);
				sb.append(internalAuditor.getName() + ",");
			}
		}
		return sb.toString();
	}
	//验证并保存记录
	public boolean isExistInternalAuditor(String workNumber,Long id){
		String hql = "select count(*) from InternalAuditor d where d.companyId =?  and d.workNumber = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());
		params.add(workNumber);
		if(id != null){
			hql += " and d.id <> ?";
			params.add(id);
		}
		Query query = internalAuditorDao.getSession().createQuery(hql);
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
	public void saveInternalAuditor(InternalAuditor internalAuditor){
		if(StringUtils.isEmpty(internalAuditor.getWorkNumber())){
			throw new RuntimeException("工号不能为空!");
		}
		if(StringUtils.isEmpty(internalAuditor.getName())){
			throw new RuntimeException("姓名不能为空!");
		}
		if(isExistInternalAuditor(internalAuditor.getWorkNumber(),internalAuditor.getId())){
			throw new RuntimeException("内审员【"+internalAuditor.getWorkNumber()+"】已存在!");
		}
		internalAuditorDao.save(internalAuditor);
	}
	public String importDatas(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		//表单字段
		Map<String,String> fieldMap = this.getFieldMap("GSM_INTERNAL_AUDITOR");
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
				InternalAuditor internalAuditor = new InternalAuditor();
				internalAuditor.setCompanyId(ContextUtils.getCompanyId());
				internalAuditor.setCreatedTime(new Date());
				internalAuditor.setCreator(ContextUtils.getUserName());
				internalAuditor.setModifiedTime(new Date());
				internalAuditor.setModifier(ContextUtils.getUserName());
				internalAuditor.setHiddenState("N");
				for(String key : objMap.keySet()){
					CommonUtil.setProperty(internalAuditor,key, objMap.get(key));
				}
				if(!isExistInternalAuditor(internalAuditor.getWorkNumber(),null)){
					internalAuditorDao.save(internalAuditor);
					sb.append("第" + (i+1) + "行导入成功!<br/>");
				}else{
					sb.append("第" + (i+1) + "行已经存在!<br/>");
				}			
			   
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
}
