package com.ambition.aftersales.oba.service;

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
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.baseinfo.dao.DefectionClassDao;
import com.ambition.aftersales.baseinfo.service.DefectionClassManager;
import com.ambition.aftersales.baseinfo.service.DefectionItemManager;
import com.ambition.aftersales.entity.DefectionClass;
import com.ambition.aftersales.entity.DefectionItem;
import com.ambition.aftersales.entity.ObaData;
import com.ambition.aftersales.entity.ObaDefectiveItem;
import com.ambition.aftersales.oba.dao.ObaDataDao;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.common.DateUtil;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 
 * 类名:OBA数据Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Service
@Transactional
public class ObaDataManager {
	@Autowired
	private ObaDataDao obaDataDao;
	@Autowired
	private DefectionClassManager defectionClassManager;
	@Autowired
	private DefectionItemManager defectionItemManager;
	@Autowired
	private DefectionClassDao defectionClassDao;	
	 private static final Map<String,String> fieldMap = new HashMap<String, String>();
	    public ObaDataManager(){
	        fieldMap.put("obaDate", "oba_date");
	        fieldMap.put("place", "place");
	        fieldMap.put("customerName", "customer_name");
	        fieldMap.put("produceDate", "produce_date");
	    }	
	public ObaData getObaData(Long id){
		return obaDataDao.get(id);
	}
	public ObaDataDao getObaDataDao(){
		return obaDataDao;
	}	
	public void deleteObaData(ObaData obaData){
		obaDataDao.delete(obaData);
	}

	public Page<ObaData> search(Page<ObaData>page){
		return obaDataDao.search(page);
	}

	public List<ObaData> listAll(){
		return obaDataDao.getAllObaData();
	}
		
	public void deleteObaData(Long id){
		obaDataDao.delete(id);
	}
	public void deleteObaData(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			ObaData  obaData = obaDataDao.get(Long.valueOf(id));
			if(obaData.getId() != null){
				obaDataDao.delete(obaData);
			}
		}
	}
	public void saveObaData(ObaData obaData){
		obaDataDao.save(obaData);
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
				+" order by c.id";
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
	/**
	  * 方法名: 计算对应的不良值
	  * <p>功能说明：</p>
	  * @param ids
	  * @param map
	 */
	public void setDefectiveValuesForExport(Map<String,String> map){
		String searchParameters = Struts2Utils.getParameter("searchParameters");
		String sql = "select  t.FK_OBA_DATA_ID,t.defection_Item_No,t.defection_Item_Value from AFS_OBA_DEFECTIVE_ITEMS t where t.FK_OBA_DATA_ID in "
				+ "(select s.id from AFS_OBA_DATA s where  1=1 and s.business_Unit_Name=?";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(Struts2Utils.getParameter("businessUnit"));
		if (StringUtils.isNotEmpty(searchParameters)) {
			JSONArray array = JSONArray.fromObject(searchParameters);
			for (int i = 0; i < array.size(); i++) {
				JSONObject json = array.getJSONObject(i);
				String propName = json.getString("propName");
				if (!fieldMap.containsKey(propName)) {
					throw new AmbFrameException("不包含查询条件[" + propName + "]");
				}
				Object propValue = json.getString("propValue");
				String optSign = json.getString("optSign");
				String dataType = json.getString("dataType");
				if ("DATE".equals(dataType)) {
					propValue = DateUtil.parseDate(propValue + "",
							"yyyy-MM-dd HH:mm:ss");
				}
				if ("like".equals(optSign)) {
					sql += " and s." + fieldMap.get(propName) + " like ?";
					searchParams.add("%" + propValue + "%");
				} else if (">=".equals(optSign)) {
					sql += " and s." + fieldMap.get(propName) + " >= ?";
					searchParams.add(propValue);
				} else if ("<=".equals(optSign)) {
					sql += " and s." + fieldMap.get(propName) + " <= ?";
					searchParams.add(propValue);
				} else {
					sql += " and s." + fieldMap.get(propName) + " = ?";
					searchParams.add(propValue);
				}
			}
		}
		sql += ") and t.defection_Item_Value is not null ";
		SQLQuery query = obaDataDao.getSession().createSQLQuery(sql);
		for (int i = 0; i < searchParams.size(); i++) {
			query.setParameter(i, searchParams.get(i));
		}
		List<?> items = query.list();
		//Date af = new Date();
		for (Object obj : items) {
			Object[] objs = (Object[]) obj;
			String key = objs[0] + "";
			String codeValue = "a" + objs[1] + ":" + objs[2];
			if (!map.containsKey(key)) {
				map.put(key, codeValue);
			} else {
				map.put(key, map.get(key) + "," + codeValue);
			}
		}
	}	
	public Page<ObaData> getListByBusinessUnit(Page<ObaData> page,String businessUnit){
			return obaDataDao.searchByBusinessUnit(page, businessUnit);
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
				Cell itemItitleCell = cellMap.get("客户名称");
				if(itemItitleCell==null){
					throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为客户的单元格!&nbsp;&nbsp;</br>");
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
					 String customerName=null,ofilmModel = null,customerModel = null,productStructure=null,place=null;
					Date obaDate=null,sendTime=null;
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
				    //生产场地
					tempCell = cellMap.get("生产场地");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【生产场地】的单元格!&nbsp;&nbsp;</br>");
					}					
					cell = row.getCell(tempCell.getColumnIndex());
					if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
						place=(ExcelUtil.getCellValue(cell).toString()).trim();
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
				    //送检时间
					tempCell = cellMap.get("送检时间");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());					
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							sendTime=(Date)ExcelUtil.getCellValue(cell);	
						}	
					}					
					Integer sendBoxCount=0,sendCount=0,inspectionCount=0;
					//送检箱数
					tempCell = cellMap.get("送检箱数");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(cell != null){
							String value = (ExcelUtil.getCellValue(cell)+"").trim();
							if(StringUtils.isNotEmpty(value)){
								if(!CommonUtil.isDouble(value) && !value.substring(0,1).equals("-")){
									throw new AmbFrameException("SHEET"+k+"客户【"+customerName+"】中送检箱数【"+value+"】不是有效数字!");
								}
								sendBoxCount = Integer.valueOf(value);
							}
						}
					}	
					//送检数
					tempCell = cellMap.get("送检数");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(cell != null){
							String value = (ExcelUtil.getCellValue(cell)+"").trim();
							if(StringUtils.isNotEmpty(value)){
								if(!CommonUtil.isDouble(value) && !value.substring(0,1).equals("-")){
									throw new AmbFrameException("SHEET"+k+"客户【"+customerName+"】中送检数【"+value+"】不是有效数字!");
								}
								sendCount = Integer.valueOf(value);
							}
						}
					}
					//检验数
					tempCell = cellMap.get("检验数");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(cell != null){
							String value = (ExcelUtil.getCellValue(cell)+"").trim();
							if(StringUtils.isNotEmpty(value)){
								if(!CommonUtil.isDouble(value) && !value.substring(0,1).equals("-")){
									throw new AmbFrameException("SHEET"+k+"客户【"+customerName+"】中检验数【"+value+"】不是有效数字!");
								}
								inspectionCount = Integer.valueOf(value);
							}
						}
					}
					//描述
					String remark=null;
					tempCell = cellMap.get("描述");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							remark=(ExcelUtil.getCellValue(cell).toString()).trim();
						}	
					}					
					//项目
					String itemName=null;
					tempCell = cellMap.get("项目");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							itemName=(ExcelUtil.getCellValue(cell).toString()).trim();
						}	
					}
					//检验方式
					String inspectionMethod=null;
					tempCell = cellMap.get("检验方式");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							inspectionMethod=(ExcelUtil.getCellValue(cell).toString()).trim();
						}	
					}
					//检验结论
					String inspectionConclusion=null;
					tempCell = cellMap.get("检验结论");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							inspectionConclusion=(ExcelUtil.getCellValue(cell).toString()).trim();
						}	
					}					
					ObaData obaData = new ObaData();
					obaData.setCreatedTime(new Date());
					obaData.setCompanyId(ContextUtils.getCompanyId());
					obaData.setCreator(ContextUtils.getUserName());
					obaData.setLastModifiedTime(new Date());
					obaData.setLastModifier(ContextUtils.getUserName());
					obaData.setCustomerName(customerName);
					obaData.setObaDate(obaDate);
					obaData.setCustomerModel(customerModel);
					obaData.setOfilmModel(ofilmModel);
					obaData.setProductStructure(productStructure);
					obaData.setSendBoxCount(sendBoxCount);
					obaData.setSendCount(sendCount);
					obaData.setInspectionCount(inspectionCount);
					obaData.setInspectionMethod(inspectionMethod);
					obaData.setInspectionConclusion(inspectionConclusion);
					obaData.setRemark(remark);
					obaData.setItemName(itemName);
					obaData.setPlace(place);
					obaData.setSendTime(sendTime);
					obaData.setBusinessUnitName(businessUnit);
					List<ObaDefectiveItem> obaDefectiveItems=new ArrayList<ObaDefectiveItem>();															
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
									ObaDefectiveItem defectionItem = new ObaDefectiveItem();
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
									defectionItem.setObaData(obaData);
									obaDefectiveItems.add(defectionItem);
								}		
							}												
						}						
					}
					obaData.setObaDefectiveItems(obaDefectiveItems);					
					obaData.setUnqualifiedCount(badValue);
					DecimalFormat   decimalFormat   =new   DecimalFormat("0.00");
					String 	unqualifiedRate=null;
					if(inspectionCount==0||badValue==0){
						unqualifiedRate="0.00";
					}else{
						unqualifiedRate=decimalFormat.format((float)(badValue*100)/(float)inspectionCount);					
					}
					obaData.setUnqualifiedRate(unqualifiedRate+"%");
					this.saveObaData(obaData);
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
	
	public void saveNew(String part, String item) {
		JSONArray partJson = JSONArray.fromObject(part);
		JSONArray itemJson = JSONArray.fromObject(item);
		for (int i = 0; i < itemJson.size(); i++) {				
			ObaData oba=new ObaData();
			oba.setCompanyId(ContextUtils.getCompanyId());
			oba.setCreatedTime(new Date());
			oba.setCreator(ContextUtils.getLoginName());
			oba.setCreatorName(ContextUtils.getUserName());
			oba.setModifiedTime(new Date());
			oba.setModifier(ContextUtils.getLoginName());
			oba.setModifierName(ContextUtils.getUserName());
			JSONObject partJs = partJson.getJSONObject(0);
			for(Object key : partJs.keySet()){
				if(key != null && partJs.get(key) != null){
					try{
						CommonUtil.setProperty(oba, key.toString(), partJs.get(key));
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if(oba.getObaDefectiveItems()==null){
				oba.setObaDefectiveItems(new ArrayList<ObaDefectiveItem>());
			}
			JSONObject itemJs = itemJson.getJSONObject(i);
			for(Object key : itemJs.keySet()){
				ObaDefectiveItem obaItem=new ObaDefectiveItem();
				obaItem.setCompanyId(ContextUtils.getCompanyId());
				obaItem.setCreatedTime(new Date());
				obaItem.setCreator(ContextUtils.getLoginName());
				obaItem.setCreatorName(ContextUtils.getUserName());
				obaItem.setModifiedTime(new Date());
				obaItem.setModifier(ContextUtils.getLoginName());
				obaItem.setModifierName(ContextUtils.getUserName());
				if(key.toString().equals("sendBoxCount")){
					oba.setSendBoxCount(Integer.valueOf(itemJs.get(key).toString()));
				}else if(key.toString().equals("sendCount")){
					oba.setSendCount(Integer.valueOf(itemJs.get(key).toString()));						
				}else if(key.toString().equals("inspectionCount")){
					oba.setInspectionCount(Integer.valueOf(itemJs.get(key).toString()));
				}else if(key.toString().equals("inspectionConclusion")){
					oba.setInspectionConclusion(itemJs.get(key).toString());
				}else if(key.toString().equals("unqualifiedCount")){
					oba.setUnqualifiedCount(Integer.valueOf(itemJs.get(key).toString()));
				}else if(key.toString().equals("unqualifiedRate")){
					oba.setUnqualifiedRate(itemJs.get(key).toString());
				}else{
					if(itemJs.get(key)!=null&&!"".equals(itemJs.get(key))){
						List<DefectionItem> list= defectionItemManager.listDefectionItem(key.toString());
						DefectionItem defectionItem=list.get(0);
						obaItem.setDefectionItemNo(key.toString());
						obaItem.setDefectionItemName(defectionItem.getDefectionItemName());
						obaItem.setDefectionItemValue(Integer.valueOf(itemJs.get(key).toString()));
						obaItem.setDefectionClass(defectionItem.getDefectionClass().getDefectionClass());
						obaItem.setObaData(oba);
						oba.getObaDefectiveItems().add(obaItem);
					}
				}				
			}
			this.saveObaData(oba);
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
