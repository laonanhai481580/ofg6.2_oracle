package com.ambition.qsm.baseinfo.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.qsm.baseinfo.dao.DefectionClauseDao;
import com.ambition.qsm.entity.DefectionClause;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**
 * 
 * 类名:不符合条款Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月26日 发布
 */
@Service
@Transactional
public class DefectionClauseManager {
	@Autowired
	private DefectionClauseDao defectionClauseDao;	
	public DefectionClause getDefectionClause(Long id){
		return defectionClauseDao.get(id);
	}
	
	public void deleteDefectionClause(DefectionClause defectionClause){
		defectionClauseDao.delete(defectionClause);
	}

	public Page<DefectionClause> search(Page<DefectionClause>page){
		return defectionClauseDao.search(page);
	}

	public List<DefectionClause> listAll(){
		return defectionClauseDao.getAllDefectionClause();
	}
		
	public void deleteDefectionClause(Long id){
		defectionClauseDao.delete(id);
	}
	public void deleteDefectionClause(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			DefectionClause  defectionClause = defectionClauseDao.get(Long.valueOf(id));
			if(defectionClause.getId() != null){
				defectionClauseDao.delete(defectionClause);
			}
		}
	}
	public void saveDefectionClause(DefectionClause defectionClause){
		defectionClauseDao.save(defectionClause);
	}

	public List<?> searchClauseNnames(JSONObject params) {
		return defectionClauseDao.searchClauseNnames(params);
	}
	public Page<DefectionClause> listState(Page<DefectionClause> page,String state ,String str){
		String hql = " from DefectionClause e where e.hiddenState=? ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(state);
		if(str!=null ){
			hql=hql+" and e.factoryClassify=?";
			searchParams.add(str);
		}
		return defectionClauseDao.searchPageByHql(page, hql, searchParams.toArray());
	}
	public String importDatas(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		//表单字段
		Map<String,String> fieldMap = this.getFieldMap("QSM_DEFECTION_CLAUSE");
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
				DefectionClause defectionClause = new DefectionClause();
				defectionClause.setCompanyId(ContextUtils.getCompanyId());
				defectionClause.setCreatedTime(new Date());
				defectionClause.setCreator(ContextUtils.getUserName());
				defectionClause.setModifiedTime(new Date());
				defectionClause.setModifier(ContextUtils.getUserName());
				User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
				String subName=user.getSubCompanyName();
				defectionClause.setFactoryClassify(subName);
				for(String key : objMap.keySet()){
					CommonUtil.setProperty(defectionClause,key, objMap.get(key));
				}
				defectionClauseDao.save(defectionClause);
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
}
