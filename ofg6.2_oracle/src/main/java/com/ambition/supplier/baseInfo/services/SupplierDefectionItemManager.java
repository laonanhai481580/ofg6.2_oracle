package com.ambition.supplier.baseInfo.services;

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

import org.apache.commons.beanutils.PropertyUtils;
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

import com.ambition.supplier.baseInfo.dao.SupplierDefectionItemDao;
import com.ambition.supplier.entity.SupplierDefectionClass;
import com.ambition.supplier.entity.SupplierDefectionItem;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

import flex.messaging.util.StringUtils;

@Service
@Transactional
public class SupplierDefectionItemManager {
	@Autowired
	private SupplierDefectionItemDao defectionItemDao;
	@Autowired
	private SupplierDefectionClassManager defectionClassManager;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
	//获取记录
	public SupplierDefectionItem getSupplierDefectionItem(Long id){
		return defectionItemDao.get(id);
	}
	
	public SupplierDefectionItem getSupplierDefectionByCode(String code){
		return defectionItemDao.getSupplierDefectionByCode(code);
	}
	
	//验证并保存记录
	private boolean isExistSupplierDefectionItem(Long id, String no, String name, SupplierDefectionClass defectionClass) {
		String hql = "select count(*) from SupplierDefectionItem d where d.companyId = ?  and (d.supplierDefectionItemNo =? or d.supplierDefectionItemName =?)  and d.supplierDefectionClass.businessUnitName=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(ContextUtils.getCompanyId());		
		params.add(no);
		params.add(name);
		params.add(defectionClass.getBusinessUnitName());
		if(id != null){
			hql += "and d.id <> ?";
			params.add(id);
		}
		Query query = defectionItemDao.getSession().createQuery(hql);
		for(int i = 0;i < params.size();i++){
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
	public void saveSupplierDefectionItem(SupplierDefectionItem defectionItem){
		if(StringUtils.isEmpty(defectionItem.getSupplierDefectionItemNo())){
			throw new RuntimeException("不良代码不为空!");
		}
		if(StringUtils.isEmpty(defectionItem.getSupplierDefectionItemName())){
			throw new RuntimeException("不良名称不为空!");
		}
		if(isExistSupplierDefectionItem(defectionItem.getId(),defectionItem.getSupplierDefectionItemNo(),defectionItem.getSupplierDefectionItemName(),defectionItem.getSupplierDefectionClass())){
			throw new RuntimeException(defectionItem.getSupplierDefectionClass().getBusinessUnitName()+"已存在相同的不良代码或名称!");
		}
		defectionItemDao.save(defectionItem);
	}	
	public void saveExcelSupplierDefectionItem(SupplierDefectionItem defectionItem){
		defectionItemDao.save(defectionItem);
	}	
	//删除记录
	public void deleteSupplierDefectionItem(String ids){
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(defectionItemDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", defectionItemDao.get(Long.valueOf(id)).toString());
			}
			defectionItemDao.delete(Long.valueOf(id));
		}
	}
	//删除对象
	public void deleteSupplierDefectionItem(SupplierDefectionItem defectionItem){
		logUtilDao.debugLog("删除", defectionItem.toString());
		defectionItemDao.delete(defectionItem);
	}
	
	//返回页面对象列表
	public Page<SupplierDefectionItem> list(Page<SupplierDefectionItem> page,SupplierDefectionClass defectionClass){
		return defectionItemDao.list(page, defectionClass);
	}
	
	public Page<SupplierDefectionItem> listByParent(Page<SupplierDefectionItem> page,Long parentId){
		return defectionItemDao.listByParent(page, parentId);
	}
	
	public Page<SupplierDefectionItem> getCodeByParams(Page<SupplierDefectionItem> page, JSONObject params){
		return defectionItemDao.getCodeByParams(page, params);
	}
	
	public Page<SupplierDefectionItem> list(Page<SupplierDefectionItem> page,String code){
		return defectionItemDao.list(page, code);
	}
	//返回所有对象列表
	public List<SupplierDefectionItem> listAll(SupplierDefectionClass defectionClass){
		return defectionItemDao.getAllSupplierDefectionItems(defectionClass);
	}	
	public List<SupplierDefectionItem> listSupplierDefectionItem(String code){
		return defectionItemDao.getSupplierDefectionItem(code);
	}
	@SuppressWarnings("static-access")
	public String importFile(File file) throws Exception {
		StringBuffer sb = new StringBuffer("");
		Map<String, String> fieldMap = new HashMap<String, String>();
		Map<String, String> fieldMap1 = new HashMap<String, String>();
		fieldMap1.put("不良编码", "supplierDefectionItemNo");
		fieldMap1.put("不良编码名称", "supplierDefectionItemName");
		Workbook book = WorkbookFactory.create(new FileInputStream(file));
		Sheet defectionClassSheet = book.getSheetAt(0);
		Row defectionClassRow = defectionClassSheet.getRow(0);
		if (defectionClassRow == null) {
			throw new RuntimeException("不良类别表格第一行不能为空!");
		}

		Map<String, Integer> columnMap = new HashMap<String, Integer>();
		for (int i = 0;; i++) {
			Cell cell = defectionClassRow.getCell(i);
			if (cell == null) {
				break;
			}
			String value = cell.getStringCellValue();
			if (fieldMap.containsKey(value)) {
				columnMap.put(value, i);
			}
		}
		if (columnMap.keySet().size() != fieldMap.keySet().size()) {
			throw new RuntimeException("不良类别表格模板格式不正确!");
		}
		Iterator<Row> rows = defectionClassSheet.rowIterator();
		DecimalFormat df = new DecimalFormat("#.##############");
		rows.next();// 标题行
		int i = 1;
		while (rows.hasNext()) {
			defectionClassRow = rows.next();
			//判断不良类别编码是否存在
			Cell defectionClasscell = defectionClassRow.getCell(columnMap.get("不良类型编码"));
			String defectionClassCode="";
			if(defectionClasscell!=null){
			if(defectionClasscell.CELL_TYPE_STRING == defectionClasscell.getCellType()){
				defectionClassCode=defectionClasscell.getStringCellValue();
			}else if(defectionClasscell.CELL_TYPE_NUMERIC == defectionClasscell.getCellType()){
				defectionClassCode=df.format(defectionClasscell.getNumericCellValue());
			}
			}
			SupplierDefectionClass defectionClass=defectionClassManager.getSupplierDefectionClassByCode(defectionClassCode);
			if(defectionClass==null){
				defectionClass = new SupplierDefectionClass();
				defectionClass.setCreatedTime(new Date());
				defectionClass.setCompanyId(ContextUtils.getCompanyId());
				defectionClass.setCreator(ContextUtils.getUserName());
				defectionClass.setLastModifiedTime(new Date());
				defectionClass.setLastModifier(ContextUtils.getUserName());
			}
			for (String columnName : columnMap.keySet()) {
				Cell cell = defectionClassRow.getCell(columnMap.get(columnName));
				if (cell != null) {
					if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
						if(cell.getStringCellValue().equals("False")){
							setProperty(defectionClass,fieldMap.get(columnName),false);
						}else if(cell.getStringCellValue().equals("True")){
							setProperty(defectionClass,fieldMap.get(columnName),true);
						}
						else{
							setProperty(defectionClass,fieldMap.get(columnName),cell.getStringCellValue());
						}
						
					} else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							Date date = cell.getDateCellValue();
							setProperty(defectionClass,fieldMap.get(columnName), date);
						} else {
							setProperty(defectionClass,fieldMap.get(columnName),df.format(cell.getNumericCellValue()));
						}
					} else if (Cell.CELL_TYPE_FORMULA == cell.getCellType()) {
						setProperty(defectionClass,fieldMap.get(columnName), cell.getCellFormula());
					}
				}
			}
			try {
				defectionClassManager.saveExcelSupplierDefectionClass(defectionClass);
				sb.append("不良类别第" + (i + 1) + "行保存成功!<br/>");
			} catch (Exception e) {
				sb.append("不良类别第" + (i + 1) + "行保存失败:<font color=red>"
						+ e.getMessage() + "</font><br/>");
			}
			i++;
		}
		
		Sheet defectionSheet = book.getSheetAt(1);
		Row defectionRow = defectionSheet.getRow(0);
		if (defectionRow == null) {
			throw new RuntimeException("不良第一行不能为空!");
		}

		Map<String, Integer> defectionColumnMap = new HashMap<String, Integer>();
		for (int j = 0;; j++) {
			Cell cell = defectionRow.getCell(j);
			if (cell == null) {
				break;
			}
			String value = cell.getStringCellValue();
			if (fieldMap1.containsKey(value)) {
				defectionColumnMap.put(value, j);
			}
		}
		if (defectionColumnMap.keySet().size() != fieldMap1.keySet().size()) {
			throw new RuntimeException("不良表格模板格式不正确!");
		}
		Iterator<Row> defectionRows = defectionSheet.rowIterator();
		defectionRows.next();// 标题行
		int k = 1;
		while (defectionRows.hasNext()) {
			defectionRow = defectionRows.next();
			//判断不良是否存在
			Cell defectionItemcell = defectionRow.getCell(defectionColumnMap.get("不良编码"));
			String defectionItem="";
			if(defectionItemcell!=null){
			if(defectionItemcell.CELL_TYPE_STRING == defectionItemcell.getCellType()){
				defectionItem=defectionItemcell.getStringCellValue();
			}else if(defectionItemcell.CELL_TYPE_NUMERIC == defectionItemcell.getCellType()){
				defectionItem=df.format(defectionItemcell.getNumericCellValue());
			}
			}
			SupplierDefectionItem d=getSupplierDefectionByCode(defectionItem);
			if(d==null){
				d = new SupplierDefectionItem();
				d.setCreatedTime(new Date());
				d.setCompanyId(ContextUtils.getCompanyId());
				d.setCreator(ContextUtils.getUserName());
				d.setLastModifiedTime(new Date());
				d.setLastModifier(ContextUtils.getUserName());
			}
			for (String columnName : defectionColumnMap.keySet()) {
				Cell cell = defectionRow.getCell(defectionColumnMap.get(columnName));
				if(!columnName.equals("不良类型编码")){
				if (cell != null) {
					if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
						if(cell.getStringCellValue().equals("False")){
							setProperty(d,fieldMap1.get(columnName),false);
						}else if(cell.getStringCellValue().equals("True")){
							setProperty(d,fieldMap1.get(columnName),true);
						}
						else{
							setProperty(d,fieldMap1.get(columnName),cell.getStringCellValue());
						}
						
					} else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							Date date = cell.getDateCellValue();
							setProperty(d,fieldMap1.get(columnName), date);
						} else {
							setProperty(d,fieldMap1.get(columnName),df.format(cell.getNumericCellValue()));
						}
					} else if (Cell.CELL_TYPE_FORMULA == cell.getCellType()) {
						setProperty(d,fieldMap1.get(columnName), cell.getCellFormula());
					}
					
				}
			}else{
				String defectionClassNo="";
				if(Cell.CELL_TYPE_STRING == cell.getCellType()){
					defectionClassNo=cell.getStringCellValue();
				}else if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
					defectionClassNo=df.format(cell.getNumericCellValue());
				}
				SupplierDefectionClass defectionClass=defectionClassManager.getSupplierDefectionClassByCode(defectionClassNo);
				d.setSupplierDefectionClass(defectionClass);
			}
		}
			try {
				saveExcelSupplierDefectionItem(d);
				sb.append("不良第" + (k + 1) + "行保存成功!<br/>");
			} catch (Exception e) {
				sb.append("不良第" + (k + 1) + "行保存失败:<font color=red>"
						+ e.getMessage() + "</font><br/>");
			}
			k++;
		}
		
		file.delete();
		return sb.toString();
	}
	
	private void setProperty(Object obj, String property, Object value) throws Exception {
		Class<?> type = PropertyUtils.getPropertyType(obj, property);
		if (type != null) {
			if (value == null || StringUtils.isEmpty(value.toString())) {
				PropertyUtils.setProperty(obj, property, null);
			} else {
				if (String.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property, value.toString());
				} else if (Integer.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property,Integer.valueOf(value.toString()));
				} else if (Double.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property,Double.valueOf(value.toString()));
				} else if (Float.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property,Float.valueOf(value.toString()));
				} else if (Boolean.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, property,Boolean.valueOf(value.toString()));
				} else {
					PropertyUtils.setProperty(obj, property, value);
				}
			}
		}
	}
}
