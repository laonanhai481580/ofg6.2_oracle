package com.ambition.iqc.inspectionbase.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.bom.dao.ProductBomDao;
import com.ambition.carmfg.entity.ProductBom;
import com.ambition.iqc.entity.InspectionBomType;
import com.ambition.iqc.inspectionbase.dao.InspectionBomTypeDao;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

@Service
@Transactional
public class InspectionBomTypeManager {
	@Autowired
	private InspectionBomTypeDao inspectionBomTypeDao;	
	@Autowired
	private ProductBomDao productBomDao;
	public InspectionBomType getInspectionBom(Long id){
		return inspectionBomTypeDao.get(id);
	}
	
	public void deleteInspectionBomType(InspectionBomType inspectionBomType){
		inspectionBomTypeDao.delete(inspectionBomType);
	}

	public Page<InspectionBomType> search(Page<InspectionBomType>page){
		return inspectionBomTypeDao.search(page);
	}

	public List<InspectionBomType> listAll(){
		return inspectionBomTypeDao.getAllInspectionBomType();
	}
		
	public void deleteInspectionBomType(Long id){
		inspectionBomTypeDao.delete(id);
	}
	public void deleteInspectionBomType(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			InspectionBomType  inspectionBomType = inspectionBomTypeDao.get(Long.valueOf(id));
			if(inspectionBomType.getId() != null){
				inspectionBomTypeDao.delete(inspectionBomType);
			}
		}
	}
	public void saveInspectionBomType(InspectionBomType inspectionBomType){
		if(isExistInspectionBomType(inspectionBomType.getId(),inspectionBomType.getMaterialTypeCode(),inspectionBomType.getBusinessUnitCode())){
			throw new AmbFrameException(inspectionBomType.getBusinessUnitCode()+"组织下已经存在相同的物料类别编码!");
		}
		inspectionBomTypeDao.save(inspectionBomType);
	}
	/**
	 * 检查是否存在相同名称的物料类别
	 * @param id
	 * @param name
	 * @return
	 */
	public Boolean isExistInspectionBomType(Long id,String materialTypeCode,String businessUnitCode){
		String hql = "select count(i.id) from InspectionBomType i where i.companyId = ? and i.materialTypeCode = ? and i.businessUnitCode=? ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(materialTypeCode);
		searchParams.add(businessUnitCode);
		if(id != null){
			hql += " and i.id <> ?";
			searchParams.add(id);
		}
		List<?> list = inspectionBomTypeDao.find(hql,searchParams.toArray());
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	public InspectionBomType getInspectionBomType(String type){
		return inspectionBomTypeDao.getInspectionBomType(type);
	}
	/**
	 * 添加物料
	 * @param inspectingIndicatorId
	 * @param inspectingItemIds
	 * @return
	 */
	public int addBom(String bomIds){
		int addCount = 0;
		for(String bomId : bomIds.split(",")){
			if(StringUtils.isNotEmpty(bomId)){
				ProductBom productBom =productBomDao.get(Long.valueOf(bomId));
					//检查是否已经添加
					String hql = "select count(i.id) from InspectionBomType i where i.materielName = ? and i.materielCode = ?";
					List<?> list = inspectionBomTypeDao.find(hql,productBom.getMaterielName(),productBom.getMaterielCode());
					if(Integer.valueOf(list.get(0).toString())==0){
						InspectionBomType bom = new InspectionBomType();
						bom.setMaterialType(productBom.getMaterialType());
						bom.setMaterialTypeCode(productBom.getMaterialTypeCode());
						bom.setRemark(productBom.getRemark());
						inspectionBomTypeDao.save(bom);
						addCount++;
				}
			}
		}
		return addCount;
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
	public String importDatas(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		//表单字段
		Map<String,String> fieldMap = this.getFieldMap("IQC_INSPECTION_BOM_TYPE");
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
				InspectionBomType inspectionBomType = new InspectionBomType();
				inspectionBomType.setCompanyId(ContextUtils.getCompanyId());
				inspectionBomType.setCreatedTime(new Date());
				inspectionBomType.setCreator(ContextUtils.getUserName());
				inspectionBomType.setModifiedTime(new Date());
				inspectionBomType.setModifier(ContextUtils.getUserName());
				for(String key : objMap.keySet()){
					CommonUtil.setProperty(inspectionBomType,key, objMap.get(key));
				}
				InspectionBomType bom=this.getInspectionBomType(inspectionBomType.getMaterialType());
				if(bom==null){
					inspectionBomTypeDao.save(inspectionBomType);
					sb.append("第" + (i+1) + "行保存成功!<br/>");
					i++;
				}else{
					bom.setLastModifiedTime(new Date());
					bom.setLastModifier(ContextUtils.getUserName());
					bom.setMaterialType(inspectionBomType.getMaterialType());
					sb.append("第" + (i+1) + "行更新成功!<br/>");
					i++;
				}				
			} catch (Exception e) {
				e.printStackTrace();
				sb.append("第" + (i+1) + "行导入失败:<font color=red>" + e.getMessage() + "</font><br/>");
				i++;
			}
		}
		file.delete();
		return sb.toString();
	}	
}
