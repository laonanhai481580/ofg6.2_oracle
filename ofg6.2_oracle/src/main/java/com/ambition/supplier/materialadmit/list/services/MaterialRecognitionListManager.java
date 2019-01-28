package com.ambition.supplier.materialadmit.list.services;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.entity.MaterialRecognitionList;
import com.ambition.supplier.materialadmit.list.dao.MaterialRecognitionListDao;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

@Service
@Transactional
public class MaterialRecognitionListManager {
	@Autowired
	private MaterialRecognitionListDao materialRecognitionListDao;
	
	public MaterialRecognitionList getMaterialRecognitionList(Long id){
		return materialRecognitionListDao.get(id);
	}
	public void saveMaterialRecognitionList(MaterialRecognitionList materialRecognitionList){
		materialRecognitionListDao.save(materialRecognitionList);
	}
	
	public Page<MaterialRecognitionList> list(Page<MaterialRecognitionList> page){
		return materialRecognitionListDao.list(page);
	}
	
	public List<MaterialRecognitionList> listAll(){
		return materialRecognitionListDao.getMaterialRecognitionList();
	}
	
	
	public void deleteMaterialRecognitionList(String deleteIds){
		String[] ids = deleteIds.split(",");
 		for(String id : ids){
			MaterialRecognitionList materialRecognitionList=materialRecognitionListDao.get(Long.valueOf(id));
			if(materialRecognitionList.getId() != null){
				materialRecognitionListDao.delete(materialRecognitionList);
			}
		}
	}
	
	public Page<MaterialRecognitionList> search(Page<MaterialRecognitionList> page){
		return materialRecognitionListDao.search(page);
	}
	public String importDatas(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		//表单字段
		Map<String,String> fieldMap = this.getFieldMap("MATERIAL_RECOGNITION_LIST");
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
				MaterialRecognitionList materialRecognitionList = new MaterialRecognitionList();
				materialRecognitionList.setCompanyId(ContextUtils.getCompanyId());
				materialRecognitionList.setCreatedTime(new Date());
				materialRecognitionList.setCreator(ContextUtils.getUserName());
				materialRecognitionList.setModifiedTime(new Date());
				materialRecognitionList.setModifier(ContextUtils.getUserName());
				User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
				String subName=user.getSubCompanyName();
				materialRecognitionList.setFactoryClassify(subName);
				for(String key : objMap.keySet()){
					CommonUtil.setProperty(materialRecognitionList,key, objMap.get(key));
				}
				materialRecognitionListDao.save(materialRecognitionList);
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
