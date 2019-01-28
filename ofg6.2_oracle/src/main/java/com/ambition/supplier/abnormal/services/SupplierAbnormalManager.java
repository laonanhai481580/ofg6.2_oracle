package com.ambition.supplier.abnormal.services;

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

import javax.annotation.Resource;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.abnormal.dao.SupplierAbnormalDao;
import com.ambition.supplier.archives.dao.SupplierDao;
import com.ambition.supplier.baseInfo.dao.SupplierDefectionClassDao;
import com.ambition.supplier.baseInfo.services.SupplierDefectionClassManager;
import com.ambition.supplier.baseInfo.services.SupplierDefectionItemManager;
import com.ambition.supplier.entity.Supplier;
import com.ambition.supplier.entity.SupplierAbnormal;
import com.ambition.supplier.entity.SupplierAbnormalItem;
import com.ambition.supplier.entity.SupplierDefectionClass;
import com.ambition.supplier.entity.SupplierDefectionItem;
import com.ambition.supplier.entity.SupplierDeleteMark;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PropUtils;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年11月5日 发布
 */
@Service
@Transactional
public class SupplierAbnormalManager {
	@Autowired
	private SupplierAbnormalDao supplierAbnormalDao;
	@Autowired
	private SupplierDao supplierDao;
	@Autowired
	private SupplierDefectionClassDao supplierdefectionClassDao;
	@Autowired
	private SupplierDefectionItemManager supplierdefectionItemManager;
	@Autowired
	private SupplierDefectionClassManager supplierDefectionClassManager;
	@Resource(name="tpdbDataJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private LogUtilDao logUtilDao;
	public Page<SupplierAbnormal> listDatas(Page<SupplierAbnormal> page) {
		return supplierAbnormalDao.searchPageByHql(page, "from SupplierAbnormal s where s.companyId=?", ContextUtils.getCompanyId());
	}
	public SupplierAbnormal getSupplierAbnormal(Long id) {
		return supplierAbnormalDao.get(id);
	}
	public void saveSupplierAbnormal(SupplierAbnormal supplierAbnormal) {
		supplierAbnormalDao.save(supplierAbnormal);
	}
	public Page<SupplierAbnormal> search(Page<SupplierAbnormal>page){
		return supplierAbnormalDao.search(page);
	}
	public SupplierAbnormalDao getSupplierAbnormalDao() {
		return supplierAbnormalDao;
	}
	public void delete(String ids) {
		String[] deleteIds = ids.split(",");
		for(String id : deleteIds){
			if(supplierAbnormalDao.get(Long.valueOf(id))!=null){
				logUtilDao.debugLog("删除", supplierAbnormalDao.get(Long.valueOf(id)).toString());
			}
			supplierAbnormalDao.delete(Long.valueOf(id));
			//记录删除的信息到TP数据库中
			String companyName= PropUtils.getProp("companyName");
			if(!companyName.equals("")&&companyName!=null){
				String sql = "insert into SUPPLIER_DELETE_MARK (id,source_id,source_unit,source_table) values("+Long.valueOf(id)+","+Long.valueOf(id)+",'"+companyName+"','"+SupplierDeleteMark.SUPPLIER_ABNORMAL_ALL+"')";
				jdbcTemplate.execute(sql);
			}
		}
	}
	public Page<SupplierAbnormal> getListByBusinessUnit(Page<SupplierAbnormal> page,String businessUnit){
		return supplierAbnormalDao.searchByBusinessUnit(page, businessUnit);
	}
	public Page<SupplierAbnormal> getListByBusinessUnit(Page<SupplierAbnormal> page,String businessUnit,String productType){
		return supplierAbnormalDao.searchByBusinessUnit(page, businessUnit,productType);
	}
	/**
	 * 根据事业部查询不良项目
	 * @param productLine
	 * @return
	 */
	public List<Map<String,Object>> queryDefectionsByBusinessUnit(String businessUnit){
		String sql = "select t.product_type,c.supplier_defection_item_no,c.supplier_defection_item_name from  SUPPLIER_DEFECTION_CLASS t  " 
				+" inner join SUPPLIER_DEFECTION_ITEM c "
				+" on c.FK_DEFECTION_TYPE_NO = t.id and t.business_unit_name=? "
				+" order by t.product_type,c.supplier_defection_item_no ";
		List<?> list = supplierdefectionClassDao.getSession()
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
	 * 根据事业部查询不良项目
	 * @param productLine
	 * @return
	 */
	public List<Map<String,Object>> queryDefectionsByBusinessUnit(String businessUnit,String productType){
		String sql = "select t.product_type,c.supplier_defection_item_no,c.supplier_defection_item_name from  SUPPLIER_DEFECTION_CLASS t  " 
				+" inner join SUPPLIER_DEFECTION_ITEM c "
				+" on c.FK_DEFECTION_TYPE_NO = t.id and t.business_unit_name=? and t.product_type=?"
				+" order by t.product_type,c.supplier_defection_item_no ";
		List<?> list = supplierdefectionClassDao.getSession()
							.createSQLQuery(sql)
							.setParameter(0,businessUnit)
							.setParameter(1,productType)
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
	public void saveNew(String part, String item) {
		JSONArray partJson = JSONArray.fromObject(part);
		JSONArray itemJson = JSONArray.fromObject(item);
		for (int i = 0; i < itemJson.size(); i++) {				
			SupplierAbnormal supplierAbnormal=new SupplierAbnormal();
			supplierAbnormal.setCompanyId(ContextUtils.getCompanyId());
			supplierAbnormal.setCreatedTime(new Date());
			supplierAbnormal.setCreator(ContextUtils.getLoginName());
			supplierAbnormal.setCreatorName(ContextUtils.getUserName());
			supplierAbnormal.setModifiedTime(new Date());
			supplierAbnormal.setModifier(ContextUtils.getLoginName());
			supplierAbnormal.setModifierName(ContextUtils.getUserName());
			JSONObject partJs = partJson.getJSONObject(0);
			for(Object key : partJs.keySet()){
				if(key != null && partJs.get(key) != null){
					try{
						CommonUtil.setProperty(supplierAbnormal, key.toString(), partJs.get(key));
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if(supplierAbnormal.getSupplierAbnormalItems()==null){
				supplierAbnormal.setSupplierAbnormalItems(new ArrayList<SupplierAbnormalItem>());
			}
			JSONObject itemJs = itemJson.getJSONObject(i);
			for(Object key : itemJs.keySet()){
				SupplierAbnormalItem supplierAbnormalItem=new SupplierAbnormalItem();
				supplierAbnormalItem.setCompanyId(ContextUtils.getCompanyId());
				supplierAbnormalItem.setCreatedTime(new Date());
				supplierAbnormalItem.setCreator(ContextUtils.getLoginName());
				supplierAbnormalItem.setCreatorName(ContextUtils.getUserName());
				supplierAbnormalItem.setModifiedTime(new Date());
				supplierAbnormalItem.setModifier(ContextUtils.getLoginName());
				supplierAbnormalItem.setModifierName(ContextUtils.getUserName());
				if(key.toString().equals("unqualifiedAmount")){
					supplierAbnormal.setUnqualifiedAmount(Integer.valueOf(itemJs.get(key).toString()));						
				}else if(key.toString().equals("judgeIchiban")){
					supplierAbnormal.setUnqualifiedAmount(Integer.valueOf(itemJs.get(key).toString()));						
				}else if(key.toString().equals("misjudgment")){
					supplierAbnormal.setUnqualifiedAmount(Integer.valueOf(itemJs.get(key).toString()));						
				}else if(key.toString().equals("againBadness")){
					supplierAbnormal.setUnqualifiedAmount(Integer.valueOf(itemJs.get(key).toString()));						
				}else{
					if(itemJs.get(key)!=null&&!"".equals(itemJs.get(key))){
						List<SupplierDefectionItem> list= supplierdefectionItemManager.listSupplierDefectionItem(key.toString());
						SupplierDefectionItem defectionItem=list.get(0);
						supplierAbnormalItem.setDefectionItemNo(key.toString());
						supplierAbnormalItem.setDefectionItemName(defectionItem.getSupplierDefectionItemName());
						supplierAbnormalItem.setDefectionItemValue(Integer.valueOf(itemJs.get(key).toString()));
						supplierAbnormalItem.setDefectionClass(defectionItem.getSupplierDefectionClass().getSupplierDefectionClass());
						supplierAbnormalItem.setSupplierAbnormal(supplierAbnormal);
						supplierAbnormal.getSupplierAbnormalItems().add(supplierAbnormalItem);
					}
				}				
			}
			this.saveSupplierAbnormal(supplierAbnormal);
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
					Date enterDate=null;//录入时间
					String procedure=null,productStage=null,supplierCode=null,supplierName=null,modelType=null;
					Integer inputAmount=null;
					String factory=null,models=null,unqualifiedRate=null,productionChecker=null,supplierChecker=null,mqeChecker=null;
					String classGroup=null,period=null,productType=null,remorseAbsorption=null,customerName=null,materialState=null;
					 //物料类别
					tempCell = cellMap.get("物料类别");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							productType=(ExcelUtil.getCellValue(cell).toString()).trim();
						}else{
							throw new AmbFrameException("物料类别不能为空！");
						}
					}
					//录入时间
					tempCell = cellMap.get("日期");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());					
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							enterDate=(Date)ExcelUtil.getCellValue(cell);	
						}else{
							throw new AmbFrameException("日期不能为空！");
						}	
					}
					 //周期
					tempCell = cellMap.get("周期");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							period=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}
					 //班别
					tempCell = cellMap.get("班别");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							classGroup=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}
					 //供应商
					tempCell = cellMap.get("供应商编码");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							supplierCode=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}
					if(supplierCode!=null&&!"".equals(supplierCode)){
						Supplier supplier=supplierDao.getSupplierByCode(supplierCode);
						if(supplier!=null){
							supplierName=supplier.getName();
						}else{
							throw new AmbFrameException("编号为【"+supplierCode+"】的供应商不存在！");
						}
					}else{
						throw new AmbFrameException("供应商编码不能为空！");
					}
					 //厂区
					tempCell = cellMap.get("工厂");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							factory=(ExcelUtil.getCellValue(cell).toString()).trim();
						}else{
							throw new AmbFrameException("工厂不能为空！");
						}
					}
					 //型号
					tempCell = cellMap.get("型号");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							modelType=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}
					 //工序
					tempCell = cellMap.get("工序");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							procedure=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}
					 //阶段
					tempCell = cellMap.get("阶段");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							productStage=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}
					 //客户名称
					tempCell = cellMap.get("客户名称");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							customerName=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}
					 //物料状态
					tempCell = cellMap.get("物料状态");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							materialState=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}
					//投入数
					tempCell = cellMap.get("投入数");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(cell != null){
							String value = (ExcelUtil.getCellValue(cell)+"").trim();
							if(StringUtils.isNotEmpty(value)){
								if(!CommonUtil.isDouble(value) && !value.substring(0,1).equals("-")){
									throw new AmbFrameException("SHEET"+k+"投入数【"+value+"】不是有效数字!");
								}
								inputAmount = Integer.valueOf(value);
							}else{
								throw new AmbFrameException("投入数不能为空！");
							}
						}
					}
					 //自责吸收
					tempCell = cellMap.get("自责吸收");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							remorseAbsorption=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}
					 //生产确认人
					tempCell = cellMap.get("生产确认人");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							productionChecker=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}
					
					//供应商确认人
					tempCell = cellMap.get("供应商确认人");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							supplierChecker=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}
					
					//MQE确认人
					tempCell = cellMap.get("MQE确认人");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							mqeChecker=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}

					SupplierAbnormal supplierAbnormal = new SupplierAbnormal();
					supplierAbnormal.setCreatedTime(new Date());
					supplierAbnormal.setCompanyId(ContextUtils.getCompanyId());
					supplierAbnormal.setCreator(ContextUtils.getLoginName());
					supplierAbnormal.setCreatorName(ContextUtils.getUserName());
					supplierAbnormal.setModifiedTime(new Date());
					supplierAbnormal.setModifier(ContextUtils.getUserName());
					supplierAbnormal.setEnterDate(enterDate);
					supplierAbnormal.setPeriod(period);
					supplierAbnormal.setModelType(modelType);
					supplierAbnormal.setModels(models);
					supplierAbnormal.setFactory(factory);
					supplierAbnormal.setClassGroup(classGroup);
					supplierAbnormal.setProcedure(procedure);
					supplierAbnormal.setProductStage(productStage);
					supplierAbnormal.setInputAmount(inputAmount);
					supplierAbnormal.setProductionChecker(productionChecker);
					supplierAbnormal.setSupplierChecker(supplierChecker);
					supplierAbnormal.setMqeChecker(mqeChecker);
					supplierAbnormal.setBusinessUnitName(businessUnit);
					supplierAbnormal.setProductType(productType);
					supplierAbnormal.setSupplierCode(supplierCode);
					supplierAbnormal.setSupplierName(supplierName);
					supplierAbnormal.setRemorseAbsorption(remorseAbsorption);
					supplierAbnormal.setCustomerName(customerName);
					supplierAbnormal.setMaterialState(materialState);
					List<SupplierAbnormalItem> supplierAbnormalItems=new ArrayList<SupplierAbnormalItem>();															
					Map<String, Map<String,String>> map=getFieldMaps(businessUnit,productType);
					Integer badValue=0;
					DecimalFormat   decimalFormat   =new   DecimalFormat("0.00");
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
										throw new AmbFrameException("SHEET"+k+1+"第" + (i+1) +"不良项目中"+codeName+"【"+value+"】不是有效数字!");
									}
									itemValue = Integer.valueOf(value);
									badValue+=itemValue;
									SupplierAbnormalItem supplierAbnormalItem = new SupplierAbnormalItem();
									supplierAbnormalItem.setCreatedTime(new Date());
									supplierAbnormalItem.setCompanyId(ContextUtils.getCompanyId());
									supplierAbnormalItem.setCreator(ContextUtils.getLoginName());
									supplierAbnormalItem.setCreatorName(ContextUtils.getUserName());
									supplierAbnormalItem.setLastModifiedTime(new Date());
									supplierAbnormalItem.setLastModifier(ContextUtils.getUserName());
									supplierAbnormalItem.setDefectionItemName(codeName);
									supplierAbnormalItem.setDefectionItemNo(codeMap.get(codeName));
									supplierAbnormalItem.setDefectionItemValue(itemValue);
									supplierAbnormalItem.setDefectionClass(typeName);
									supplierAbnormalItem.setSupplierAbnormal(supplierAbnormal);
									supplierAbnormalItems.add(supplierAbnormalItem);
								}		
							}												
						}	
					}
					supplierAbnormal.setSupplierAbnormalItems(supplierAbnormalItems);
					supplierAbnormal.setUnqualifiedAmount(badValue);//不良数
					if(inputAmount==0||badValue==0){
						unqualifiedRate="0.00";
					}else{
						unqualifiedRate=decimalFormat.format((float)(badValue*100)/(float)inputAmount);					
					}
					supplierAbnormal.setUnqualifiedRate(unqualifiedRate+"%");
					this.saveSupplierAbnormal(supplierAbnormal);
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
	private Map<String, Map<String,String>> getFieldMaps(String businessUnit,String productType){				
		Map<String, Map<String,String>> map=new HashMap<String, Map<String,String>>();
		List<SupplierDefectionClass> lisType=supplierDefectionClassManager.getSupplierDefectionClassByBusinessUnit(businessUnit,productType);
		for (SupplierDefectionClass supplierDefectionClass : lisType) {
			List<SupplierDefectionItem> listCode=supplierDefectionClass.getSupplierDefectionItems();
			Map<String,String> fieldMap = new HashMap<String, String>();
			for (SupplierDefectionItem defectionItem : listCode) {
				fieldMap.put(defectionItem.getSupplierDefectionItemName(), defectionItem.getSupplierDefectionItemNo());
			}
			map.put(supplierDefectionClass.getProductType(), fieldMap);
		}
		return map;
	}
}
