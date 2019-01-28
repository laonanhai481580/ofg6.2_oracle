package com.ambition.carmfg.oqc.service;

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

import com.ambition.carmfg.entity.OqcException;
import com.ambition.carmfg.oqc.dao.OqcExceptionDao;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**
 * 
 * 类名:OQC异常管理台账Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2018年4月13日 发布
 */
@Service
@Transactional
public class OqcExceptionManager {
	@Autowired
	private OqcExceptionDao oqcExceptionDao;	
	public OqcException getOqcException(Long id){
		return oqcExceptionDao.get(id);
	}
	
	public void deleteOqcException(OqcException oqcException){
		oqcExceptionDao.delete(oqcException);
	}

	public Page<OqcException> search(Page<OqcException>page){
		return oqcExceptionDao.search(page);
	}

	public List<OqcException> listAll(){
		return oqcExceptionDao.getAllOqcException();
	}
		
	public void deleteOqcException(Long id){
		oqcExceptionDao.delete(id);
	}
	public void deleteOqcException(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			if(id!=null){
				OqcException  oqcException = oqcExceptionDao.get(Long.valueOf(id));
				oqcExceptionDao.delete(oqcException);
			}			
		}
	}
	public void saveOqcException(OqcException oqcException){
		oqcExceptionDao.save(oqcException);
	}

	public String importDatas(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		//表单字段
		Map<String,String> fieldMap = this.getFieldMap("MFG_OQC_EXCEPTION");
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
				OqcException oqcException = new OqcException();
				oqcException.setCompanyId(ContextUtils.getCompanyId());
				oqcException.setCreatedTime(new Date());
				oqcException.setCreator(ContextUtils.getLoginName());
				oqcException.setModifiedTime(new Date());
				oqcException.setModifier(ContextUtils.getLoginName());
				oqcException.setModifierName(ContextUtils.getUserName());
				for(String key : objMap.keySet()){
					CommonUtil.setProperty(oqcException,key, objMap.get(key));
				}
				Integer inputCount=oqcException.getInputCount();
				Integer unQualityCount=oqcException.getUnQualityCount();
				if(inputCount==null||"".equals(inputCount)||unQualityCount==null||"".equals(unQualityCount)){
					throw new AmbFrameException("投入数和不良数不能为空!");
				}
				if(unQualityCount>inputCount){
					throw new AmbFrameException("不良数不能大于投入数!");
				}
				String 	unQualityRate=null;
				DecimalFormat   decimalFormat   =new   DecimalFormat("0.00");
				if(unQualityCount==0){
					unQualityRate="0.00";
				}else{
					unQualityRate=decimalFormat.format((float)(unQualityCount*100)/(float)inputCount);					
				}
				oqcException.setUnQualityRate(unQualityRate+"%");
				
			   oqcExceptionDao.save(oqcException);
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
