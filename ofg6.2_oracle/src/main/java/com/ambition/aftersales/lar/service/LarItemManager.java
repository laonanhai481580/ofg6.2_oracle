package com.ambition.aftersales.lar.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.baseinfo.dao.DefectionClassDao;
import com.ambition.aftersales.baseinfo.service.DefectionClassManager;
import com.ambition.aftersales.baseinfo.service.DefectionItemManager;
import com.ambition.aftersales.entity.DefectionClass;
import com.ambition.aftersales.entity.DefectionItem;
import com.ambition.aftersales.entity.LarData;
import com.ambition.aftersales.entity.LarDefectiveItem;
import com.ambition.aftersales.entity.LarItem;
import com.ambition.aftersales.lar.dao.LarItemDao;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.common.DateUtil;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**
 * 
 * 类名:LAR数据子表Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Service
@Transactional
public class LarItemManager {
	@Autowired
	private LarItemDao larItemDao;
	@Autowired
	private DefectionClassDao defectionClassDao;	
	@Autowired
	private DefectionClassManager defectionClassManager;
	@Autowired
	private DefectionItemManager defectionItemManager;
	public LarItem getLarItem(Long id){
		return larItemDao.get(id);
	}
	
	public void deleteLarItem(LarItem larItem){
		larItemDao.delete(larItem);
	}

	public Page<LarItem> search(Page<LarItem>page){
		return larItemDao.search(page);
	}

	public List<LarItem> listAll(){
		return larItemDao.getAllLarItem();
	}
		
	public void deleteLarItem(Long id){
		larItemDao.delete(id);
	}
	public String deleteLarItem(String ids) {
		String[] deleteIds = ids.split(",");
		StringBuilder sb = new StringBuilder("");
		for (String id : deleteIds) {
			LarItem  larItem = larItemDao.get(Long.valueOf(id));
			if(larItem.getId() != null){
				larItemDao.delete(larItem);
				sb.append(larItemDao.get(Long.valueOf(id)).getCustomer() + ",");
			}
		}
		return sb.toString();
	}
	public void saveLarItem(LarItem larItem){
		larItemDao.save(larItem);
	}
	
	/**
	 * 根据事业部查询不良项目
	 * @param productLine
	 * @return
	 */
	public List<Map<String,Object>> queryDefectionsByBusinessUnit(String businessUnit){
		String sql = "select t.defection_class,c.defection_item_no,c.defection_item_name from  AFS_DEFECTION_CLASS t  " 
				+" inner join AFS_DEFECTION_ITEM c "
				+" on c.FK_DEFECTION_TYPE_NO = t.id and t.business_unit_name=? "
				+" order by t.defection_class,c.defection_item_no ";
		List<?> list = defectionClassDao.getSession()
							.createSQLQuery(sql)
							.setParameter(0,businessUnit)
							.list();
		List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
		for(Object obj :  list){
			Object[] objs = (Object[])obj;
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("typeName",objs[0]);
			map.put("itemCode",objs[1]);
			map.put("itemName",objs[2]);
			results.add(map);
		}
		return results;
	}
	
	public Page<LarItem> getListByBusinessUnit(Page<LarItem> page,String businessUnit){
			return larItemDao.searchByBusinessUnit(page, businessUnit);
	}

	public Page<LarItem> getListByLarId(Page<LarItem> page, Long larId) {
		return larItemDao.searchByLarId(page, larId);
	}
	public LarItemDao getLarItemDao() {
		return larItemDao;
	}

	public void saveNew(String part, String item,LarData larData) {
		JSONArray partJson = JSONArray.fromObject(part);
		JSONArray itemJson = JSONArray.fromObject(item);
		for (int i = 0; i < itemJson.size(); i++) {				
			LarItem larItem=new LarItem();
			larItem.setLarData(larData);
			larItem.setCompanyId(ContextUtils.getCompanyId());
			larItem.setCreatedTime(new Date());
			larItem.setCreator(ContextUtils.getLoginName());
			larItem.setCreatorName(ContextUtils.getUserName());
			larItem.setModifiedTime(new Date());
			larItem.setModifier(ContextUtils.getLoginName());
			larItem.setModifierName(ContextUtils.getUserName());
			JSONObject partJs = partJson.getJSONObject(0);
			for(Object key : partJs.keySet()){
				if(key != null && partJs.get(key) != null){
					try{
						CommonUtil.setProperty(larItem, key.toString(), partJs.get(key));
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if(larItem.getLarDefectiveItems()==null){
				larItem.setLarDefectiveItems(new ArrayList<LarDefectiveItem>());
			}
			JSONObject itemJs = itemJson.getJSONObject(i);
			for(Object key : itemJs.keySet()){
				LarDefectiveItem larDefectiveItem=new LarDefectiveItem();
				larDefectiveItem.setCompanyId(ContextUtils.getCompanyId());
				larDefectiveItem.setCreatedTime(new Date());
				larDefectiveItem.setCreator(ContextUtils.getLoginName());
				larDefectiveItem.setCreatorName(ContextUtils.getUserName());
				larDefectiveItem.setModifiedTime(new Date());
				larDefectiveItem.setModifier(ContextUtils.getLoginName());
				larDefectiveItem.setModifierName(ContextUtils.getUserName());
				if(key.toString().equals("larDate")||key.toString().equals("inputAmount")||key.toString().equals("badAmount")||key.toString().equals("qualifiedAmount")
						||key.toString().equals("qualifiedRate")||key.toString().equals("unQualifiedRate")||key.toString().equals("materialNo")||key.toString().equals("inspectionAmount")){
					try {
						CommonUtil.setProperty(larItem, key.toString(), itemJs.get(key));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					if(itemJs.get(key)!=null&&!"".equals(itemJs.get(key))){
						List<DefectionItem> list= defectionItemManager.listDefectionItem(key.toString());
						DefectionItem defectionItem=list.get(0);
						larDefectiveItem.setDefectionItemNo(key.toString());
						larDefectiveItem.setDefectionItemName(defectionItem.getDefectionItemName());
						larDefectiveItem.setDefectionItemValue(Integer.valueOf(itemJs.get(key).toString()));
						larDefectiveItem.setDefectionClass(defectionItem.getDefectionClass().getDefectionClass());
						larDefectiveItem.setLarItem(larItem);
						larItem.getLarDefectiveItems().add(larDefectiveItem);
					}
				}				
			}
			this.saveLarItem(larItem);
		}
	}	
	
	
	/**
	 * 导入台帐数据
	 * @param file
	 * @param parent
	 * @throws Exception
	 */
	public String importDatas(File file,String businessUnit) throws Exception{
		StringBuffer sb = new StringBuffer("");
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);			
			Workbook book = WorkbookFactory.create(inputStream);
			int totalSheets = book.getNumberOfSheets();
			Map<String,Cell> cellMap = new HashMap<String, Cell>();
			Map<String,org.apache.poi.ss.util.CellRangeAddress> cellRangeAddressMap = new HashMap<String, org.apache.poi.ss.util.CellRangeAddress>();
			for(int k=0;k<totalSheets;k++){
				Sheet sheet = book.getSheetAt(k);
				Row row = sheet.getRow(0);
				//隐藏的sheet不处理
				if(book.isSheetHidden(k)){
					continue;
				}
				if(row == null){
					continue;
				}
				//缓存每个单元格值的
				cellMap.clear();
				Iterator<Row> rowIterator = sheet.rowIterator();
				while(rowIterator.hasNext()){
					row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					while(cellIterator.hasNext()){
						Cell cell = cellIterator.next();
						Object value = ExcelUtil.getCellValue(cell);
						if(value != null){
							String key = value.toString().replaceAll("\\n","").replaceAll(" ","").replaceAll("　","");
							if(!cellMap.containsKey(key)){
								cellMap.put(key,cell);
							}
						}
					}
				}
				//缓存所有的合并单元格
				cellRangeAddressMap.clear();
				int sheetMergeCount = sheet.getNumMergedRegions();  
			    for(int i = 0 ; i < sheetMergeCount ; i++ ){  
			        CellRangeAddress ca = sheet.getMergedRegion(i);
			        int firstRow = ca.getFirstRow(),lastRow = ca.getLastRow();
			        int firstColumn = ca.getFirstColumn(),lastColumn = ca.getLastColumn();
			        for(int rowIndex = firstRow;rowIndex<=lastRow;rowIndex++){
			        	for(int columnIndex = firstColumn;columnIndex<=lastColumn;columnIndex++){
			        		String key = rowIndex + "_" + columnIndex;
			        		cellRangeAddressMap.put(key,ca);
			        	}
			        }
			    }
				Iterator<Row> rows = sheet.rowIterator();
				Cell itemItitleCell = cellMap.get("日期");
				if(itemItitleCell==null){
					throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为日期的单元格!&nbsp;&nbsp;</br>");
				}
				Integer itemTitleRowNum = itemItitleCell.getRow().getRowNum();
				int i = 0;
				while(rows.hasNext()){
					row = rows.next();
					//标题以后再执行
					if(row.getRowNum() <= itemTitleRowNum){
						continue;
					}
					Cell cell = row.getCell(itemItitleCell.getColumnIndex());
					if(cell == null){
						continue;
					}
					Cell tempCell=null;
					 String customerName=null,ofilmModel = null,customerModel = null;
					Date larDate=null;
					 //客户名称
					tempCell = cellMap.get("客户名称");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【客户名称】的单元格!&nbsp;&nbsp;</br>");
					}					
					cell = row.getCell(tempCell.getColumnIndex());
					if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
						customerName=(ExcelUtil.getCellValue(cell).toString()).trim();
					}											 
				    if(customerName==null||"".equals(customerName)){
				    	throw new  AmbFrameException("Sheet"+k+"的客户名称值不能为空");
				    }
				    //日期
					tempCell = cellMap.get("日期");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【日期】的单元格!&nbsp;&nbsp;</br>");
					}					
					cell = row.getCell(tempCell.getColumnIndex());					
					if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
						larDate=(Date)ExcelUtil.getCellValue(cell);	
					}						
				    if(larDate==null||"".equals(larDate)){
				    	throw new  AmbFrameException("Sheet"+k+"的日期值不能为空");
				    }		
				    //欧菲机型
					tempCell = cellMap.get("欧菲机型");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							ofilmModel=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}	
				    //客户机型
					tempCell = cellMap.get("客户机型");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							customerModel=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}					
					Integer inputCount=0;
					//投入数
					tempCell = cellMap.get("投入数");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(cell != null){
							String value = (ExcelUtil.getCellValue(cell)+"").trim();
							if(StringUtils.isNotEmpty(value)){
								if(!CommonUtil.isDouble(value) && !value.substring(0,1).equals("-")){
									throw new AmbFrameException("SHEET"+k+"客户【"+customerName+"】中投入数【"+value+"】不是有效数字!");
								}
								inputCount = Integer.valueOf(value);
							}
						}
					}	

					DecimalFormat   decimalFormat   =new   DecimalFormat("0.00");
					String 	unqualifiedRate=null;							
					LarItem larItem = new LarItem();
					larItem.setCreatedTime(new Date());
					larItem.setCompanyId(ContextUtils.getCompanyId());
					larItem.setCreator(ContextUtils.getUserName());
					larItem.setLastModifiedTime(new Date());
					larItem.setLastModifier(ContextUtils.getUserName());
					larItem.setCustomer(customerName);
					larItem.setLarDate(larDate);
					larItem.setCustomerModel(customerModel);
					larItem.setOfilmModel(ofilmModel);
					larItem.setBusinessUnitName(businessUnit);
					List<LarDefectiveItem> vlrrDefectiveItems=new ArrayList<LarDefectiveItem>();															
					Map<String, Map<String,String>> map=getFieldMaps(businessUnit);
					Integer badValue=0;
					for (String typeName : map.keySet()) {
						Map<String,String> codeMap=map.get(typeName);
						for (String codeName : codeMap.keySet()) {
							Integer itemValue=null;
							tempCell = cellMap.get(codeName);
							if(tempCell==null||"".equals(tempCell)){
								continue;
							}
							cell = row.getCell(tempCell.getColumnIndex());
							if(cell != null){
								String value = (ExcelUtil.getCellValue(cell)+"").trim();
								if(StringUtils.isNotEmpty(value)){
									if(!CommonUtil.isInteger(value) && !value.substring(0,1).equals("-")){
										throw new AmbFrameException("SHEET"+k+"客户【"+customerName+"】不良项目中"+codeName+"【"+value+"】不是有效数字!");
									}
									itemValue = Integer.valueOf(value);
									badValue+=itemValue;
									LarDefectiveItem defectionItem = new LarDefectiveItem();
									defectionItem.setCreatedTime(new Date());
									defectionItem.setCompanyId(ContextUtils.getCompanyId());
									defectionItem.setCreator(ContextUtils.getLoginName());
									defectionItem.setCreatorName(ContextUtils.getUserName());
									defectionItem.setLastModifiedTime(new Date());
									defectionItem.setLastModifier(ContextUtils.getUserName());
									defectionItem.setDefectionItemName(codeName);
									defectionItem.setDefectionItemNo(codeMap.get(codeName));
									defectionItem.setDefectionItemValue(itemValue);
									defectionItem.setDefectionClass(typeName);
									defectionItem.setLarItem(larItem);
									vlrrDefectiveItems.add(defectionItem);
								}		
							}												
						}						
					}
					larItem.setLarDefectiveItems(vlrrDefectiveItems);					
					larItem.setBadAmount(badValue);
					if(inputCount==0||badValue==0){
						unqualifiedRate="0.00";
					}else{
						unqualifiedRate=decimalFormat.format((float)(badValue*100)/(float)inputCount);					
					}
					larItem.setUnQualifiedRate(unqualifiedRate+"%");
					this.saveLarItem(larItem);
					sb.append("第" + (i+1) + "行导入成功!<br/>");
					i++;
				}
			}
			return sb.toString();
		}finally{
			if(inputStream != null){
				inputStream.close();
			}
			file.delete();
		}
	}		
	
	private Map<String, Map<String,String>> getFieldMaps(String businessUnit){				
		Map<String, Map<String,String>> map=new HashMap<String, Map<String,String>>();
		List<DefectionClass> lisType=defectionClassManager.getDefectionClassByBusinessUnit(businessUnit);
		for (DefectionClass defectionClass : lisType) {
			List<DefectionItem> listCode=defectionClass.getDefectionItems();
			Map<String,String> fieldMap = new HashMap<String, String>();
			for (DefectionItem defectionItem : listCode) {
				fieldMap.put(defectionItem.getDefectionItemName(), defectionItem.getDefectionItemNo());
			}
			map.put(defectionClass.getDefectionClass(), fieldMap);
		}
		return map;
	}

}
