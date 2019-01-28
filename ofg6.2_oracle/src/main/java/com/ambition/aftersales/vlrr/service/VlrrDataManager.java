package com.ambition.aftersales.vlrr.service;

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
import com.ambition.aftersales.entity.VlrrData;
import com.ambition.aftersales.entity.VlrrDefectiveItem;
import com.ambition.aftersales.vlrr.dao.VlrrDataDao;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**
 * 
 * 类名:VLRR数据Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Service
@Transactional
public class VlrrDataManager {
	@Autowired
	private VlrrDataDao vlrrDataDao;
	@Autowired
	private DefectionClassDao defectionClassDao;	
	@Autowired
	private DefectionClassManager defectionClassManager;
	@Autowired
	private DefectionItemManager defectionItemManager;
	public VlrrData getVlrrData(Long id){
		return vlrrDataDao.get(id);
	}
	
	public void deleteVlrrData(VlrrData vlrrData){
		vlrrDataDao.delete(vlrrData);
	}

	public Page<VlrrData> search(Page<VlrrData>page){
		return vlrrDataDao.search(page);
	}
	public Page<VlrrData> searchBusinessUnit(Page<VlrrData>page,String businessUnit){
		return vlrrDataDao.searchBusinessUnit(page, businessUnit);
	}
	public Page<VlrrData> searchBusinessUnitHide(Page<VlrrData>page,String businessUnit){
		return vlrrDataDao.searchBusinessUnitHide(page, businessUnit);
	}
	public List<VlrrData> listAll(){
		return vlrrDataDao.getAllVlrrData();
	}
		
	public void deleteVlrrData(Long id){
		vlrrDataDao.delete(id);
	}
	public String deleteVlrrData(String ids) {
		String[] deleteIds = ids.split(",");
		StringBuilder sb = new StringBuilder("");
		for (String id : deleteIds) {
			VlrrData  vlrrData = vlrrDataDao.get(Long.valueOf(id));
			if(vlrrData.getId() != null){
				vlrrDataDao.delete(vlrrData);
				sb.append(vlrrDataDao.get(Long.valueOf(id)).getCustomerName() + ",");
			}
		}
		return sb.toString();
	}
	public void saveVlrrData(VlrrData vlrrData){
		vlrrDataDao.save(vlrrData);
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
	
	public Page<VlrrData> getListByBusinessUnit(Page<VlrrData> page,String businessUnit){
			return vlrrDataDao.searchByBusinessUnit(page, businessUnit);
	}

	public Page<VlrrData> getListByBusinessUnitHide(Page<VlrrData> page,String businessUnit){
		return vlrrDataDao.searchByBusinessUnitHide(page, businessUnit);
	}
	
	public VlrrDataDao getVlrrDataDao() {
		return vlrrDataDao;
	}

	public String hiddenState(String hideId,String type){
		StringBuilder sb = new StringBuilder("");
		String[] ids = hideId.split(",");
		for(String id : ids){
			VlrrData vlrrData = vlrrDataDao.get(Long.valueOf(id));
			if("Y".equals(type)){
				vlrrData.setHiddenState("N");
			}else{
				vlrrData.setHiddenState("Y");
			}
			vlrrDataDao.save(vlrrData);
			sb.append(vlrrData.getVlrrDate()+"-"+vlrrData.getCustomerName()+ ",");
		}
		return sb.toString();
	}	
	
	public void saveNew(String part, String item) {
		JSONArray partJson = JSONArray.fromObject(part);
		JSONArray itemJson = JSONArray.fromObject(item);
		for (int i = 0; i < itemJson.size(); i++) {				
			VlrrData vlrr=new VlrrData();
			vlrr.setCompanyId(ContextUtils.getCompanyId());
			vlrr.setCreatedTime(new Date());
			vlrr.setCreator(ContextUtils.getLoginName());
			vlrr.setCreatorName(ContextUtils.getUserName());
			vlrr.setModifiedTime(new Date());
			vlrr.setModifier(ContextUtils.getLoginName());
			vlrr.setModifierName(ContextUtils.getUserName());
			JSONObject partJs = partJson.getJSONObject(0);
			for(Object key : partJs.keySet()){
				if(key != null && partJs.get(key) != null){
					try{
						CommonUtil.setProperty(vlrr, key.toString(), partJs.get(key));
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if(vlrr.getVlrrDefectiveItems()==null){
				vlrr.setVlrrDefectiveItems(new ArrayList<VlrrDefectiveItem>());
			}
			JSONObject itemJs = itemJson.getJSONObject(i);
			for(Object key : itemJs.keySet()){
				VlrrDefectiveItem vlrrItem=new VlrrDefectiveItem();
				vlrrItem.setCompanyId(ContextUtils.getCompanyId());
				vlrrItem.setCreatedTime(new Date());
				vlrrItem.setCreator(ContextUtils.getLoginName());
				vlrrItem.setCreatorName(ContextUtils.getUserName());
				vlrrItem.setModifiedTime(new Date());
				vlrrItem.setModifier(ContextUtils.getLoginName());
				vlrrItem.setModifierName(ContextUtils.getUserName());
				if(key.toString().equals("inputCount")){
					vlrr.setInputCount(Integer.valueOf(itemJs.get(key).toString()));
				}else if(key.toString().equals("unqualifiedCount")){
					vlrr.setUnqualifiedCount(Integer.valueOf(itemJs.get(key).toString()));						
				}else if(key.toString().equals("unqualifiedRate")){
					vlrr.setUnqualifiedRate(itemJs.get(key).toString());
				}else{
					if(itemJs.get(key)!=null&&!"".equals(itemJs.get(key))){
						List<DefectionItem> list= defectionItemManager.listDefectionItem(key.toString());
						DefectionItem defectionItem=list.get(0);
						vlrrItem.setDefectionItemNo(key.toString());
						vlrrItem.setDefectionItemName(defectionItem.getDefectionItemName());
						vlrrItem.setDefectionItemValue(Integer.valueOf(itemJs.get(key).toString()));
						vlrrItem.setDefectionClass(defectionItem.getDefectionClass().getDefectionClass());
						vlrrItem.setVlrrData(vlrr);
						vlrr.getVlrrDefectiveItems().add(vlrrItem);
					}
				}				
			}
			this.saveVlrrData(vlrr);
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
					 String customerName=null,customerFactory = null,ofilmModel = null,customerModel = null,productStructure=null,productionLine=null,workingProcedure=null;
					Date vlrrDate=null,produceDate=null;
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
						vlrrDate=(Date)ExcelUtil.getCellValue(cell);	
					}						
				    if(vlrrDate==null||"".equals(vlrrDate)){
				    	throw new  AmbFrameException("Sheet"+k+"的日期值不能为空");
				    }	
				    //客户场地
					tempCell = cellMap.get("客户场地");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【客户场地】的单元格!&nbsp;&nbsp;</br>");
					}					
					cell = row.getCell(tempCell.getColumnIndex());
					if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
						customerFactory=(ExcelUtil.getCellValue(cell).toString()).trim();
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
					//产品结构
					tempCell = cellMap.get("产品结构");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							productStructure=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}
				    //线别
					tempCell = cellMap.get("线别");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							productionLine=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}					
				    //工序
					tempCell = cellMap.get("工序");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							workingProcedure=(ExcelUtil.getCellValue(cell).toString()).trim();
						}						
					}						
				    //生产日期
					tempCell = cellMap.get("生产日期");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());					
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							produceDate=(Date)ExcelUtil.getCellValue(cell);	
						}	
					}					
					@SuppressWarnings("unused")
					Integer inputCount=0,unqualifiedCount=0;
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
					//不良数
/*					tempCell = cellMap.get("不良数");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(cell != null){
							String value = (ExcelUtil.getCellValue(cell)+"").trim();
							if(StringUtils.isNotEmpty(value)){
								if(!CommonUtil.isDouble(value) && !value.substring(0,1).equals("-")){
									throw new AmbFrameException("SHEET"+k+"客户【"+customerName+"】中不良数【"+value+"】不是有效数字!");
								}
								unqualifiedCount = Integer.valueOf(value);
							}
						}
					}	
					if(Integer.valueOf(unqualifiedCount)>Integer.valueOf(inputCount)){
						throw new  AmbFrameException("Sheet"+k+"的不良数不能大于投入数");				  
					}	*/
					DecimalFormat   decimalFormat   =new   DecimalFormat("0.00");
					String 	unqualifiedRate=null;
					/*if(inputCount==0){
						unqualifiedRate="0.00";
					}else{
						unqualifiedRate=decimalFormat.format((float)(unqualifiedCount*100)/(float)inputCount);					
					}	*/				
					//备注
					@SuppressWarnings("unused")
					String remark=null;
					tempCell = cellMap.get("备注");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							remark=(ExcelUtil.getCellValue(cell).toString()).trim();
						}	
					}					
									
					VlrrData vlrrData = new VlrrData();
					vlrrData.setCreatedTime(new Date());
					vlrrData.setCompanyId(ContextUtils.getCompanyId());
					vlrrData.setCreator(ContextUtils.getUserName());
					vlrrData.setLastModifiedTime(new Date());
					vlrrData.setLastModifier(ContextUtils.getUserName());
					vlrrData.setCustomerName(customerName);
					vlrrData.setVlrrDate(vlrrDate);
					vlrrData.setCustomerFactory(customerFactory);
					vlrrData.setCustomerModel(customerModel);
					vlrrData.setOfilmModel(ofilmModel);
					vlrrData.setProduceDate(produceDate);
					vlrrData.setProductStructure(productStructure);
					vlrrData.setProductionLine(productionLine);
					vlrrData.setWorkingProcedure(workingProcedure);
					//vlrrData.setUnqualifiedCount(unqualifiedCount);
					vlrrData.setInputCount(inputCount);
					//vlrrData.setUnqualifiedRate(unqualifiedRate);
					vlrrData.setBusinessUnitName(businessUnit);
					List<VlrrDefectiveItem> vlrrDefectiveItems=new ArrayList<VlrrDefectiveItem>();															
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
									VlrrDefectiveItem defectionItem = new VlrrDefectiveItem();
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
									defectionItem.setVlrrData(vlrrData);
									vlrrDefectiveItems.add(defectionItem);
								}		
							}												
						}						
					}
					vlrrData.setVlrrDefectiveItems(vlrrDefectiveItems);					
					vlrrData.setUnqualifiedCount(badValue);
					String unDppm=null;
					if(inputCount==0||badValue==0){
						unqualifiedRate="0.00";
						unDppm="0";
					}else{
						unqualifiedRate=decimalFormat.format((float)(badValue*100)/(float)inputCount);	
						unDppm=(badValue*1000000/inputCount)+"";
					}
					vlrrData.setUnqualifiedRate(unqualifiedRate+"%");
					vlrrData.setUnqualifiedDppm(unDppm);
					this.saveVlrrData(vlrrData);
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
