package com.ambition.carmfg.oqc.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.baseinfo.service.CustomerListManager;
import com.ambition.aftersales.baseinfo.service.OfilmModelManager;
import com.ambition.carmfg.defectioncode.dao.DefectionTypeDao;
import com.ambition.carmfg.defectioncode.service.DefectionCodeManager;
import com.ambition.carmfg.defectioncode.service.DefectionTypeManager;
import com.ambition.carmfg.entity.DefectionCode;
import com.ambition.carmfg.entity.DefectionType;
import com.ambition.carmfg.entity.OqcDefectiveItem;
import com.ambition.carmfg.entity.OqcInspection;
import com.ambition.carmfg.oqc.dao.OqcInspectionDao;
import com.ambition.product.EscColumnToBean;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.common.DateUtil;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.AsyncMailUtils;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 
 * 类名:OQC检验Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年8月31日 发布
 */
@Service
@Transactional
public class OqcInspectionManager {
	@Autowired
	private OqcInspectionDao oqcInspectionDao;
	@Autowired
	private DefectionTypeDao defectionTypeDao;	
	@Autowired
	private DefectionCodeManager defectionCodeManager;	
	@Autowired
	private CustomerListManager customerListManager;
	@Autowired
	private OfilmModelManager ofilmModelManager;
	@Autowired
	private DefectionTypeManager defectionTypeManager;
	 private static final Map<String,String> fieldMap = new HashMap<String, String>();
	    public OqcInspectionManager(){
	        fieldMap.put("classGroup", "class_group");
	        fieldMap.put("inspectionBatchNo", "inspection_batch_no");
	        fieldMap.put("customer", "customer");
	        fieldMap.put("model", "model");
	        fieldMap.put("judgeResult", "judge_result");
	        fieldMap.put("inspectionDate", "inspection_date");
	        fieldMap.put("qualityType", "quality_type");
	        fieldMap.put("factory", "factory");
	        fieldMap.put("procedure", "procedure");
	        fieldMap.put("lineType", "lineType");
	        fieldMap.put("remark", "remark");
	    }
	    
	public OqcInspection getOqcInspection(Long id){
		return oqcInspectionDao.get(id);
	}
	
	public void deleteOqcInspection(OqcInspection oqcInspection){
		oqcInspectionDao.delete(oqcInspection);
	}

	public Page<OqcInspection> search(Page<OqcInspection>page){
		return oqcInspectionDao.search(page);
	}

	public List<OqcInspection> listAll(){
		return oqcInspectionDao.getAllOqcInspection();
	}
		
	public void deleteOqcInspection(Long id){
		oqcInspectionDao.delete(id);
	}
	public String deleteOqcInspection(String ids) {
		StringBuilder sb = new StringBuilder("");
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			OqcInspection  oqcInspection = oqcInspectionDao.get(Long.valueOf(id));
			if(oqcInspection.getId() != null){
				oqcInspectionDao.delete(oqcInspection);
				sb.append(oqcInspectionDao.get(Long.valueOf(id)).getModel() + ",");
			}
		}
		return sb.toString();
	}
	
	public void saveOqcInspection(OqcInspection oqcInspection){
		if(oqcInspection.getIsSendMail()!=null&&oqcInspection.getIsSendMail().equals("是")){
			if(oqcInspection.getDutyMan().equals("")&&oqcInspection.getQeMan().equals("")){
				throw new AmbFrameException("责任领班和QE确认人为空，无法发送邮件!");				
			}else{
				if(oqcInspection.getHasSendMail()==0){
					sendMail(oqcInspection);
					oqcInspection.setHasSendMail(1);
				}else{
					throw new AmbFrameException("该条信息已经发送过邮件通知!");	
				}
			}
		}
		oqcInspectionDao.save(oqcInspection);
	}
	
	public void sendMail(OqcInspection oqcInspection) {
		String message = "";
		Date myDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		String dateStr = sdf.format(myDate);	
		List<String> list=new ArrayList<String>();
		String dutyMan=oqcInspection.getDutyManLogin();
		if(!dutyMan.equals("")){
			String email = ApiFactory.getAcsService().getUserByLoginName(dutyMan).getEmail();
			list.add(email);
		}
		String qeMan=oqcInspection.getQeManLogin();
		if(!qeMan.equals("")){
			String email = ApiFactory.getAcsService().getUserByLoginName(qeMan).getEmail();
			list.add(email);
		}
		//发送邮件
		message = "*"+dateStr+"最新预警信息:"+oqcInspection.getBusinessUnitName()+"事业部，OQC检验"+sdf.format(oqcInspection.getInspectionDate())+" "+oqcInspection.getCustomer()+"客户"+oqcInspection.getModel()+"机种,检验批号为"+oqcInspection.getInspectionBatchNo()+"的检验信息已录入，检验判定："+oqcInspection.getJudgeResult()+",检验员:"+oqcInspection.getInspectionMan();
		for (int i = 0; i < list.size(); i++) {
			if(StringUtils.isNotEmpty(list.get(i))){
				AsyncMailUtils.sendMail(list.get(i),"OQC检验预警",message);
			}
		}
		
	}
	/**
	 * 根据生产线查询不良
	 * @param productLine
	 * @return
	 */
	public List<Map<String,Object>> queryDefectionsByBusinessUnit(String businessUnit){
		String sql = "select t.defection_Type_Name,c.defection_code_no,c.defection_code_name from  MFG_DEFECTION_TYPE t  " 
				+" inner join MFG_DEFECTION_CODE c "
				+" on c.fk_defection_type_no = t.id and t.business_unit_name=? "
				+" order by t.defection_Type_No";
		List<?> list = defectionTypeDao.getSession()
							.createSQLQuery(sql)
							.setParameter(0,businessUnit)
							.list();
		List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
		//Map<String,Boolean> existMap = new HashMap<String, Boolean>();
		for(Object obj :  list){
			Object[] objs = (Object[])obj;
/*			String itemCode = objs[1]+"";
			if(existMap.containsKey(itemCode)){
				continue;
			}
			existMap.put(itemCode,true);*/
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("typeName",objs[0]);
			map.put("itemCode",objs[1]);
			map.put("itemName",objs[2]);
			results.add(map);
		}
		return results;
	}
	/**
	 * 根据生产线查询不良
	 * @param productLine
	 * @return
	 */
	public List<Map<String,Object>> queryDefectionsByBusinessUnit(String businessUnit,String productType){
		String sql = "select t.defection_Type_Name,c.defection_code_no,c.defection_code_name from  MFG_DEFECTION_TYPE t  " 
				+" inner join MFG_DEFECTION_CODE c "
				+" on c.fk_defection_type_no = t.id and t.business_unit_name=? and t.product_type=? "
				+" order by t.defection_Type_No,c.defection_code_no";
		List<?> list = defectionTypeDao.getSession()
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
	public Page<OqcInspection> getListByBusinessUnit(Page<OqcInspection> page,String businessUnit){
			return oqcInspectionDao.searchByBusinessUnit(page, businessUnit);
	}
	public Page<OqcInspection> getListByBusinessUnitHide(Page<OqcInspection> page,String businessUnit){
		return oqcInspectionDao.searchByBusinessUnitHide(page, businessUnit);
	}
	public Page<OqcInspection> getListByBusinessUnit(Page<OqcInspection> page,String businessUnit,String productType){
		return oqcInspectionDao.searchByBusinessUnit(page, businessUnit,productType);
	}
	public Page<OqcInspection> getListByBusinessUnitHide(Page<OqcInspection> page,String businessUnit,String productType){
		return oqcInspectionDao.searchByBusinessUnitHide(page, businessUnit,productType);
	}
	
	public String hiddenState(String hideId,String type){
		StringBuilder sb = new StringBuilder("");
		String[] ids = hideId.split(",");
		for(String id : ids){
			OqcInspection oqcInspection = oqcInspectionDao.get(Long.valueOf(id));
			if("Y".equals(type)){
				oqcInspection.setHiddenState("N");
			}else{
				oqcInspection.setHiddenState("Y");
			}
			oqcInspectionDao.save(oqcInspection);
			sb.append(oqcInspection.getInspectionDate()+ ",");
		}
		return sb.toString();
	}	
	
	/**
	 * 导入台帐数据
	 * @param file
	 * @param parent
	 * @throws Exception
	 */
	public String importDatasSQ(File file,String businessUnit) throws Exception{
		StringBuffer sb = new StringBuffer("");
		//表单字段
		Map<String,String> fieldMap = this.getFieldMap("MFG_OQC_INSPECTION");
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
/*		if(columnMap.keySet().size() != fieldMap.keySet().size()){
			throw new AmbFrameException("Excel格式不正确!请重新导出台账数据模板!");
		}*/
		
		DecimalFormat df = new DecimalFormat("#.##");
		Iterator<Row> rows = sheet.rowIterator();
		rows.next();//标题行
		int i = 0;
		while(rows.hasNext()){
			row = rows.next();
			Cell myCell = row.getCell(columnMap.get("日期"));
			if(myCell == null){
				continue;
			}
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
				OqcInspection oqcInspection = new OqcInspection();
				oqcInspection.setCompanyId(ContextUtils.getCompanyId());
				oqcInspection.setCreatedTime(new Date());
				oqcInspection.setBusinessUnitName(businessUnit);
				oqcInspection.setCreator(ContextUtils.getUserName());
				oqcInspection.setModifiedTime(new Date());
				oqcInspection.setModifier(ContextUtils.getUserName());
				Integer  samplingCount=Integer.valueOf((String) objMap.get("samplingCount"));
				Integer  unQualityCount = 0;
				if(objMap.get("unQualityCount")!=null){
					 unQualityCount=Integer.valueOf((String)objMap.get("unQualityCount"));
				}
				if(unQualityCount>samplingCount){
					  sb.append("第" + (i+1) + "行导入失败，不良数不能大于抽检数!<br/>");
					  i++;
					  continue;					  
				}
				if(samplingCount==0||unQualityCount==0){
					objMap.put("unQualityRate",0);
					objMap.put("judgeResult", "合格");
				}else{
					DecimalFormat   decimalFormat   =new   DecimalFormat("0.00");				
					String 	unQualityRate=decimalFormat.format((float)(unQualityCount*100)/(float)samplingCount);
					objMap.put("unQualityRate", unQualityRate+"%");
					if((float)(unQualityCount*100)/(float)samplingCount>0){
						objMap.put("judgeResult", "不合格");
					}else{
						objMap.put("judgeResult", "合格");
					}
				}								
				for(String key : objMap.keySet()){
					CommonUtil.setProperty(oqcInspection,key, objMap.get(key));
				}
			   this.saveOqcInspection(oqcInspection);
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
	
	private Map<String,String> getFieldMapWaiGuan(String businessUnit){
		Map<String,String> fieldMap = new HashMap<String, String>();
		String badItems="加强板不良,连接器不良,焊接不良,模流,白点/毛丝,通光孔刮伤,油污,金手指不良,镜座烫伤/镜筒不良,溢胶/补强不良,辅材不良,短装/多装/混料,漏点胶/漏封淘气孔,标签不良,其它外观不良";
		String a[]=badItems.split(",");
		for (int i = 0; i < a.length; i++) {
			String badItem=a[i];
			String itemCode=defectionCodeManager.listDefectionCodeByName(badItem, "外观",businessUnit);
			if(itemCode!=null){
				fieldMap.put(badItem, itemCode);
			}
		}
		return fieldMap;
	}
	private Map<String,String> getFieldMapGongNeng(String businessUnit){
		Map<String,String> fieldMap = new HashMap<String, String>();
		String badItems="POG,POD,OTP烧录不良,一拖三测试不良,电性,暗角,AF,MTF,SFR不良,漏烧OTP,漏调焦,其它功能不良";
		String a[]=badItems.split(",");
		for (int i = 0; i < a.length; i++) {
			String badItem=a[i];
			String itemCode=defectionCodeManager.listDefectionCodeByName(badItem, "功能",businessUnit);
			if(itemCode!=null){
				fieldMap.put(badItem, itemCode);
			}
		}
		return fieldMap;
	}	
	private Map<String, Map<String,String>> getFieldMaps(String businessUnit,String productType){				
		Map<String, Map<String,String>> map=new HashMap<String, Map<String,String>>();
		List<DefectionType> lisType=defectionTypeManager.getDefectionTypeByBusinessUnit(businessUnit,productType);
		for (DefectionType defectionType : lisType) {
			List<DefectionCode> listCode=defectionType.getDefectionCodes();
			Map<String,String> fieldMap = new HashMap<String, String>();
			for (DefectionCode defectionCode : listCode) {
				fieldMap.put(defectionCode.getDefectionCodeName(), defectionCode.getDefectionCodeNo());
			}
			map.put(defectionType.getDefectionTypeName(), fieldMap);
		}
		return map;
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
					throw new AmbFrameException("SHEET"+k+1 +"资料格式不正确!没有值为日期的单元格!&nbsp;&nbsp;</br>");
				}
				Integer itemTitleRowNum = itemItitleCell.getRow().getRowNum();
				int i = 1;
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
					 String model=null,customer = null,count = null,samplingCount = null,unQualityCount=null,classGroup=null,inspectionBatchNo=null,judgeResult=null,inspectionMan=null;
					Date inspectionDate=null;
					 //机种
					tempCell = cellMap.get("机种");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+1+"资料格式不正确!没有值为【机种】的单元格!&nbsp;&nbsp;</br>");
					}					
					cell = row.getCell(tempCell.getColumnIndex());
					if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
						model=(ExcelUtil.getCellValue(cell).toString()).trim();
					}											 
				    if(model==null||"".equals(model)){
				    	throw new  AmbFrameException("Sheet"+k+1+"第" + (i+1) +"的机种值不能为空");
				    }
				    //日期
					tempCell = cellMap.get("日期");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+1 +"资料格式不正确!没有值为【日期】的单元格!&nbsp;&nbsp;</br>");
					}					
					cell = row.getCell(tempCell.getColumnIndex());					
					if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
						inspectionDate=(Date)ExcelUtil.getCellValue(cell);	
					}						
				    if(inspectionDate==null||"".equals(inspectionDate)){
				    	throw new  AmbFrameException("Sheet"+k+1+"第" + (i+1) +"的日期值不能为空");
				    }	
				    //班别
					tempCell = cellMap.get("班别");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+1+"资料格式不正确!没有值为【班别】的单元格!&nbsp;&nbsp;</br>");
					}					
					cell = row.getCell(tempCell.getColumnIndex());
					if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
						classGroup=(ExcelUtil.getCellValue(cell).toString()).trim();
					}	
				    if(classGroup==null||"".equals(classGroup)){
				    	throw new  AmbFrameException("Sheet"+k+1+"第" + (i+1) +"的班别值不能为空");
				    }
				    String factory=null,procedure=null,lineType=null;
				    //factory
					tempCell = cellMap.get("工厂");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							factory=(ExcelUtil.getCellValue(cell).toString()).trim();
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
					//工序
					tempCell = cellMap.get("组/线别");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							lineType=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}
				    //检验批号
					tempCell = cellMap.get("检验批号");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							inspectionBatchNo=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}					
				    //判定
					tempCell = cellMap.get("判定");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							judgeResult=(ExcelUtil.getCellValue(cell).toString()).trim();
						}						
					}						
				    //检验人员
					tempCell = cellMap.get("检验人员");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							inspectionMan=ExcelUtil.getCellValue(cell).toString().trim();
						}
					}	
					String productType=null;
					 //产品类别
					tempCell = cellMap.get("产品类别");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							productType=ExcelUtil.getCellValue(cell).toString().trim();
						}
					}
					if(productType==null){
						throw new AmbFrameException("SHEET"+k+1+"第" + (i+1) +"资料格式不正确!【产品类别】的值不能为空!&nbsp;&nbsp;</br>");
					}
					String operator=null;
				    //作业员
					tempCell = cellMap.get("作业员");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							operator=ExcelUtil.getCellValue(cell).toString().trim();
						}	
					}					
															
					//客户
					tempCell = cellMap.get("客户");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+1+"资料格式不正确!没有值为【检验类别】的单元格!&nbsp;&nbsp;</br>");
					}					
					cell = row.getCell(tempCell.getColumnIndex());
					if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
						customer=(ExcelUtil.getCellValue(cell).toString()).trim();
					}						
					if(customer==null||"".equals(customer)){
				    	throw new  AmbFrameException("Sheet"+k+1+"第" + (i+1) +"的客户值不能为空");
				    }
					//客户和机种值有效性验证
					/*if(!customerListManager.isExistCustomer(customer)){
						throw new  AmbFrameException("第" + (i+1) + "行客户值不符合规范！");
					}
					if(!ofilmModelManager.isExistCustomerModel(customer, model)){
						throw new  AmbFrameException("第" + (i+1) + "行机种值不符合规范！");
					}*/
					//数量
					tempCell = cellMap.get("投入数");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+1 +"资料格式不正确!没有值为【投入数】的单元格!&nbsp;&nbsp;</br>");
					}					
					cell = row.getCell(tempCell.getColumnIndex());
					if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
						count=(ExcelUtil.getCellValue(cell).toString()).trim();
					}					
					if(count==null||"".equals(count)){
				    	throw new  AmbFrameException("Sheet"+k+1+"第" + (i+1) +"的投入数值不能为空");
				    }
					Integer stockBatch=0,qualityBatch=0;
					//投入批
					tempCell = cellMap.get("投入批");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(cell != null){
							String value = (ExcelUtil.getCellValue(cell)+"").trim();
							if(StringUtils.isNotEmpty(value)){
								if(!CommonUtil.isDouble(value) && !value.substring(0,1).equals("-")){
									throw new AmbFrameException("SHEET"+k+1+"第" + (i+1) +"机种【"+model+"】中投入批【"+value+"】不是有效数字!");
								}
								stockBatch = Integer.valueOf(value);
							}
						}
					}	
					//合格批
					tempCell = cellMap.get("合格批");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(cell != null){
							String value = (ExcelUtil.getCellValue(cell)+"").trim();
							if(StringUtils.isNotEmpty(value)){
								if(!CommonUtil.isDouble(value) && !value.substring(0,1).equals("-")){
									throw new AmbFrameException("SHEET"+k+1+"第" + (i+1) +"机种【"+model+"】中合格批【"+value+"】不是有效数字!");
								}
								qualityBatch = Integer.valueOf(value);
							}
						}
					}	
					if(Integer.valueOf(qualityBatch)>Integer.valueOf(stockBatch)){
						throw new  AmbFrameException("Sheet"+k+1+"第" + (i+1) +"的合格批数不能大于投入批数");
					}	
					DecimalFormat   decimalFormat   =new   DecimalFormat("0.00");
					String 	qualityBatchRate=null;
					if(stockBatch==0){
						qualityBatchRate="0.00";
					}else{
						qualityBatchRate=decimalFormat.format((float)(qualityBatch*100)/(float)stockBatch);					
					}
					//抽检数量
					tempCell = cellMap.get("抽检数量");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+1+"资料格式不正确!没有值为【抽检数量】的单元格!&nbsp;&nbsp;</br>");
					}					
					cell = row.getCell(tempCell.getColumnIndex());
					if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
						samplingCount=(ExcelUtil.getCellValue(cell).toString()).trim();
					}						
					if(samplingCount==null||"".equals(samplingCount)){
				    	throw new  AmbFrameException("Sheet"+k+1+"第" + (i+1) +"的抽检数量值不能为空");
				    }	
					
					/*//不良数
					tempCell = cellMap.get("不良数");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+1+"资料格式不正确!没有值为【不良数】的单元格!&nbsp;&nbsp;</br>");
					}					
					cell = row.getCell(tempCell.getColumnIndex());
					if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
						unQualityCount=(ExcelUtil.getCellValue(cell).toString()).trim();
					}					
					if(unQualityCount==null||"".equals(unQualityCount)){
						throw new  AmbFrameException("Sheet"+k+1+"第" + (i+1) +"的不良数值不能为空");
				    }					
					
					if(Integer.valueOf(unQualityCount)>Integer.valueOf(samplingCount)){
						throw new  AmbFrameException("Sheet"+k+1+"第" + (i+1) +"的不良数不能大于抽检数量");				  
					}*/	
					
					if(Integer.valueOf(samplingCount)>Integer.valueOf(count)){
						throw new  AmbFrameException("Sheet"+k+1+"第" + (i+1) +"的抽检数量不能大于检验数量");
					}	
					String 	unQualityRate=null;
					/*if(unQualityCountInt==0){
						unQualityRate="0.00";
					}else{
						unQualityRate=decimalFormat.format((float)(unQualityCountInt*100)/(float)samplingCountInt);					
					}*/
					String dutyMan=null,dutyManLogin=null,qeMan=null,qeManLogin=null,dealWay=null,remark=null,qualityType=null;					
					//责任领班
					tempCell = cellMap.get("责任领班");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							dutyMan=(ExcelUtil.getCellValue(cell).toString()).trim();
						}	
					}					
										
					//责任领班登录名
					tempCell = cellMap.get("责任领班登录名");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							dutyManLogin=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}					
															
					//QE确认人
					tempCell = cellMap.get("QE确认人");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							qeMan=(ExcelUtil.getCellValue(cell).toString()).trim();
						}	
					}					
					
					//QE确认人登录名
					tempCell = cellMap.get("QE确认人登录名");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							qeManLogin=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}					
					
					//批处理方式
					tempCell = cellMap.get("批处理方式");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							dealWay=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}					
					
					//重工品
					tempCell = cellMap.get("是否重工品");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							qualityType=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}					
					if(qualityType==null){
						qualityType="正常品";
					}
					//备注
					tempCell = cellMap.get("备注");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							remark=(ExcelUtil.getCellValue(cell).toString()).trim();
						}	
					}					
									
					OqcInspection oqcInspection = new OqcInspection();
					oqcInspection.setCreatedTime(new Date());
					oqcInspection.setCompanyId(ContextUtils.getCompanyId());
					oqcInspection.setCreator(ContextUtils.getUserName());
					oqcInspection.setLastModifiedTime(new Date());
					oqcInspection.setLastModifier(ContextUtils.getUserName());
					oqcInspection.setModel(model);
					oqcInspection.setBusinessUnitName(businessUnit);
					oqcInspection.setCustomer(customer);
					oqcInspection.setCount(Integer.valueOf(count));
					oqcInspection.setInspectionDate(inspectionDate);
					oqcInspection.setClassGroup(classGroup);
					oqcInspection.setInspectionBatchNo(inspectionBatchNo);
					oqcInspection.setJudgeResult(judgeResult);
					oqcInspection.setInspectionMan(inspectionMan);
					oqcInspection.setDutyMan(dutyMan);
					oqcInspection.setDutyManLogin(dutyManLogin);
					oqcInspection.setQeMan(qeMan);
					oqcInspection.setQeManLogin(qeManLogin);
					oqcInspection.setDealWay(dealWay);
					oqcInspection.setRemark(remark);
					oqcInspection.setQualityType(qualityType);
					oqcInspection.setIsSendMail("否");
					oqcInspection.setFactory(factory);
					oqcInspection.setProcedure(procedure);
					oqcInspection.setLineType(lineType);
					oqcInspection.setStockBatch(stockBatch);
					oqcInspection.setQualityBatch(qualityBatch);
					oqcInspection.setQualityBatchRate(qualityBatchRate+"%");
					oqcInspection.setOperator(operator);
					oqcInspection.setProductType(productType);
					
					List<OqcDefectiveItem> oqcDefectiveItems=new ArrayList<OqcDefectiveItem>();															
					Map<String, Map<String,String>> map=getFieldMaps(businessUnit,productType);
					Integer badValue=0;
					for (String typeName : map.keySet()) {
						String typeNo=defectionTypeManager.listDefectionTypeNoByName(typeName,businessUnit,productType);
						Map<String,String> codeMap=map.get(typeName);
						for (String codeName : codeMap.keySet()) {
							Integer itemValue=null;
							tempCell = cellMap.get(codeName);
							if(tempCell==null){
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
									OqcDefectiveItem oqcDefectiveItem = new OqcDefectiveItem();
									oqcDefectiveItem.setCreatedTime(new Date());
									oqcDefectiveItem.setCompanyId(ContextUtils.getCompanyId());
									oqcDefectiveItem.setCreator(ContextUtils.getLoginName());
									oqcDefectiveItem.setCreatorName(ContextUtils.getUserName());
									oqcDefectiveItem.setLastModifiedTime(new Date());
									oqcDefectiveItem.setLastModifier(ContextUtils.getUserName());
									oqcDefectiveItem.setDefectionCodeName(codeName);
									oqcDefectiveItem.setDefectionCodeNo(codeMap.get(codeName));
									oqcDefectiveItem.setDefectionCodeValue(itemValue);
									oqcDefectiveItem.setDefectionTypeName(typeName);
									oqcDefectiveItem.setDefectionTypeNo(typeNo);
									oqcDefectiveItem.setOqcInspection(oqcInspection);
									oqcDefectiveItems.add(oqcDefectiveItem);
								}		
							}												
						}						
					}
/*					Map<String,String> fieldMap=getFieldMapWaiGuan(businessUnit);																									
					String typeNo=defectionTypeManager.listDefectionTypeNoByName("外观",businessUnit);
					for (String itemName : fieldMap.keySet()) {
						Integer itemValue=null;
						tempCell = cellMap.get(itemName);
						if(tempCell==null){
							continue;
						}
						cell = row.getCell(tempCell.getColumnIndex());
						if(cell != null){
							String value = (ExcelUtil.getCellValue(cell)+"").trim();
							if(StringUtils.isNotEmpty(value)){
								if(!CommonUtil.isInteger(value) && !value.substring(0,1).equals("-")){
									throw new AmbFrameException("SHEET"+k+"机种【"+model+"】不良项目中"+itemName+"【"+value+"】不是有效数字!");
								}
								itemValue = Integer.valueOf(value);
							}
						}
						OqcDefectiveItem oqcDefectiveItem = new OqcDefectiveItem();
						oqcDefectiveItem.setCreatedTime(new Date());
						oqcDefectiveItem.setCompanyId(ContextUtils.getCompanyId());
						oqcDefectiveItem.setCreator(ContextUtils.getUserName());
						oqcDefectiveItem.setLastModifiedTime(new Date());
						oqcDefectiveItem.setLastModifier(ContextUtils.getUserName());
						oqcDefectiveItem.setDefectionCodeName(itemName);
						oqcDefectiveItem.setDefectionCodeNo(fieldMap.get(itemName));
						oqcDefectiveItem.setDefectionCodeValue(itemValue);
						oqcDefectiveItem.setDefectionTypeName("外观");
						oqcDefectiveItem.setDefectionTypeNo(typeNo);
						oqcDefectiveItem.setOqcInspection(oqcInspection);
						oqcDefectiveItems.add(oqcDefectiveItem);
					}
					 fieldMap=getFieldMapGongNeng(businessUnit);
					 typeNo=defectionTypeManager.listDefectionTypeNoByName("功能",businessUnit);
					for (String itemName : fieldMap.keySet()) {
						Integer itemValue=null;
						tempCell = cellMap.get(itemName);
						if(tempCell==null){
							continue;
						}
						cell = row.getCell(tempCell.getColumnIndex());
						if(cell != null){
							String value = (ExcelUtil.getCellValue(cell)+"").trim();
							if(StringUtils.isNotEmpty(value)){
								if(!CommonUtil.isInteger(value) && !value.substring(0,1).equals("-")){
									throw new AmbFrameException("SHEET"+k+"机种【"+model+"】不良项目中"+itemName+"【"+value+"】不是有效数字!");
								}
								itemValue = Integer.valueOf(value);
							}
						}
						OqcDefectiveItem oqcDefectiveItem = new OqcDefectiveItem();
						oqcDefectiveItem.setCreatedTime(new Date());
						oqcDefectiveItem.setCompanyId(ContextUtils.getCompanyId());
						oqcDefectiveItem.setCreator(ContextUtils.getUserName());
						oqcDefectiveItem.setLastModifiedTime(new Date());
						oqcDefectiveItem.setLastModifier(ContextUtils.getUserName());
						oqcDefectiveItem.setDefectionCodeName(itemName);
						oqcDefectiveItem.setDefectionCodeNo(fieldMap.get(itemName));
						oqcDefectiveItem.setDefectionCodeValue(itemValue);
						oqcDefectiveItem.setDefectionTypeName("功能");
						oqcDefectiveItem.setOqcInspection(oqcInspection);
						oqcDefectiveItem.setDefectionTypeNo(typeNo);
						oqcDefectiveItems.add(oqcDefectiveItem);
					}*/
					if(Integer.valueOf(samplingCount)==0||badValue==0){
						unQualityRate="0.00";
					}else{
						unQualityRate=decimalFormat.format((float)(badValue*100)/(float)Integer.valueOf(samplingCount));					
					}
					oqcInspection.setSamplingCount(Integer.valueOf(samplingCount));
					oqcInspection.setUnQualityCount(badValue);
					oqcInspection.setUnQualityRate(unQualityRate+"%");
					oqcInspection.setOqcDefectiveItems(oqcDefectiveItems);
					this.saveOqcInspection(oqcInspection);
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
	public OqcInspectionDao getOqcInspectionDao(){
		return oqcInspectionDao;
	}

	public Page<OqcInspection> searchByPage(Page<OqcInspection> page, String businessUnit) {
		// TODO Auto-generated method stub
		 String searchParameters = Struts2Utils.getParameter("searchParameters");
		String sql = "select model,rownum as id from ( select distinct(model) as model from MFG_OQC_INSPECTION o where  o.business_unit_name=? ";
		Session session = oqcInspectionDao.getSession();
		List<Object> searchParamsFault = new ArrayList<Object>();
		searchParamsFault.add(businessUnit);
		String appendSql = "";
		 if(StringUtils.isNotEmpty(searchParameters)){
	            JSONArray array = JSONArray.fromObject(searchParameters);
	            for(int i=0;i<array.size();i++){
	                JSONObject json = array.getJSONObject(i);
	                String propName = json.getString("propName");
	                Object propValue = json.getString("propValue");
	                String optSign = json.getString("optSign");
	                String dataType = json.getString("dataType");
	                if("like".equals(optSign)){
	                	appendSql += " and o.model like ?";
	                    searchParamsFault.add("%" + propValue + "%");
	                }
	            }
	        }
		SQLQuery query = session.createSQLQuery("select count(distinct(model)) from MFG_OQC_INSPECTION o where  o.business_unit_name=?"+appendSql);
        for(int i=0;i<searchParamsFault.size();i++){
            query.setParameter(i,searchParamsFault.get(i));
        }
        int totalCount = Integer.valueOf(query.list().get(0).toString());
        page.setTotalCount(totalCount);
        query = session.createSQLQuery(sql+ ""+appendSql+")");
        for(int i=0;i<searchParamsFault.size();i++){
            query.setParameter(i,searchParamsFault.get(i));
        }
//        System.out.println(sqlFault);
        query.setFirstResult((page.getPageNo()-1)*page.getPageSize());
        query.setMaxResults(page.getPageSize());
        //获取字段映射
		Field fs[] = OqcInspection.class.getDeclaredFields();
		Map<String,String> sqlFieldMap = new HashMap<String, String>();
		StringBuilder str = new StringBuilder();
		for(Field f : fs){
			str.delete(0,str.length());
			String name = f.getName();
			for(int i=0;i<name.length();i++){
				String s = name.substring(i,i+1);
				if(StringUtils.isAllUpperCase(s)){
					str.append("_" + s);
				}else{
					str.append(s);
				}
			}
			sqlFieldMap.put(str.toString().toUpperCase(),name);
		}
		sqlFieldMap.put("ID","id");
        query.setResultTransformer(new EscColumnToBean(OqcInspection.class,sqlFieldMap));
        @SuppressWarnings("unchecked")
        List<OqcInspection> results = query.list();
        page.setResult(results);
		return page;
	}
	/**
	  * 方法名: 计算对应的不良值
	  * <p>功能说明：</p>
	  * @param ids
	  * @param map
	 */
	public void setDefectiveValues(String ids,Map<String,String> map){
		String hql = "select i.oqcInspection.id,i.defectionCodeNo,i.defectionCodeValue from OqcDefectiveItem i " +
				"where i.oqcInspection.id in (" + ids + ") ";
		List<?> items = oqcInspectionDao.createQuery(hql).list();
		for(Object obj : items){
			Object[] objs = (Object[])obj;
			String key = objs[0]+"";
			String codeValue = "a" + objs[1] + ":" + objs[2];
			if(!map.containsKey(key)){
				map.put(key,codeValue);
			}else{
				map.put(key,map.get(key)+"," + codeValue);
			}
		}
	}
	/**
	  * 方法名: 计算对应的不良值
	  * <p>功能说明：</p>
	  * @param ids
	  * @param map
	 */
	public void setDefectiveValuesForExport(Map<String,String> map){
		String searchParameters = Struts2Utils.getParameter("searchParameters");
		String sql = "select  t.MFG_OQC_INSPECTION_ID,t.defection_Code_No,t.defection_Code_Value from MFG_OQC_DEFECTIVE_ITEMS t where t.mfg_oqc_inspection_id in "
				+ "(select s.id from MFG_OQC_INSPECTION s where  1=1 and s.business_Unit_Name=?";
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
		sql += ") and t.defection_Code_Value is not null ";
		SQLQuery query = oqcInspectionDao.getSession().createSQLQuery(sql);
		for (int i = 0; i < searchParams.size(); i++) {
			query.setParameter(i, searchParams.get(i));
		}
		List<?> items = query.list();
		Date af = new Date();
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
	public void saveOqcInspectionByParams(OqcInspection oqcInspection) throws Exception {
		// TODO Auto-generated method stub
		 String defectiveInfo = Struts2Utils.getParameter("defectiveInfo");
		 JSONArray itemStrArray1 = null;
		 if (!defectiveInfo.isEmpty()) {
	            itemStrArray1 = JSONArray.fromObject(defectiveInfo);
        }
		if (!itemStrArray1.isEmpty()) {
			 if(oqcInspection.getOqcDefectiveItems()==null){
				 oqcInspection.setOqcDefectiveItems(new ArrayList<OqcDefectiveItem>());
             }else{
            	 oqcInspection.getOqcDefectiveItems().clear();
             }
            for (int i = 0; i < itemStrArray1.size(); i++) {
                JSONObject jso = itemStrArray1.getJSONObject(i);
                OqcDefectiveItem item = new OqcDefectiveItem();
                item.setCreator(ContextUtils.getLoginName());
                item.setCreatedTime(new Date());
                item.setCreatorName(ContextUtils.getUserName());
                for (Object key : jso.keySet()) {
                    String value = jso.getString(key.toString());
                    setProperty(item, key.toString(), value);
                }
                item.setOqcInspection(oqcInspection);
                oqcInspection.getOqcDefectiveItems().add(item);
            }
            saveOqcInspection(oqcInspection);
        }
	}
	 private void setProperty(Object obj,String property,Object value) throws Exception{
         Class<?> type = PropertyUtils.getPropertyType(obj,property);
         if(type != null){
             if(value==null||StringUtils.isEmpty(value.toString())){
                 PropertyUtils.setProperty(obj,property,null);
             }else{
                  if(String.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,value.toString());
                 }else if(Integer.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Integer.valueOf(value.toString()));
                 }else if(Double.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Double.valueOf(value.toString()));
                 }else if(Float.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Float.valueOf(value.toString()));
                 }else if(Boolean.class.getName().equals(type.getName())){
                     PropertyUtils.setProperty(obj,property,Boolean.valueOf(value.toString()));
                 }else if(Date.class.getName().equals(type.getName())){
                     if(Date.class.getName().equals(value.getClass().getName())){
                         PropertyUtils.setProperty(obj,property,value);
                     }else if(String.class.getName().equals(value.getClass().getName())&&value.toString().length()==10){
                         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                         PropertyUtils.setProperty(obj,property,sdf.parse(value.toString()));
                     }
                 }else{
                     PropertyUtils.setProperty(obj,property,value);
                 }
             }
         }
     }

	public  List<OqcDefectiveItem> getOqcDefecitveItemsByValue(
			OqcInspection oqcInspection) {
		// TODO Auto-generated method stub
		String hql = "from OqcDefectiveItem o where o.oqcInspection.id=? and o.defectionCodeValue is not null";
		@SuppressWarnings("unchecked")
		List<OqcDefectiveItem> items = oqcInspectionDao.createQuery(hql,oqcInspection.getId()).list();
		return items;
	}
	
	public void saveNew(String part, String item) {
		JSONArray partJson = JSONArray.fromObject(part);
		JSONArray itemJson = JSONArray.fromObject(item);
		for (int i = 0; i < itemJson.size(); i++) {				
			OqcInspection oqcInspection=new OqcInspection();
			oqcInspection.setCompanyId(ContextUtils.getCompanyId());
			oqcInspection.setCreatedTime(new Date());
			oqcInspection.setCreator(ContextUtils.getLoginName());
			oqcInspection.setCreatorName(ContextUtils.getUserName());
			oqcInspection.setModifiedTime(new Date());
			oqcInspection.setModifier(ContextUtils.getLoginName());
			oqcInspection.setModifierName(ContextUtils.getUserName());
			JSONObject partJs = partJson.getJSONObject(0);
			for(Object key : partJs.keySet()){
				if(key != null && partJs.get(key) != null){
					try{
						if(key.toString().equals("businessUnit")){
							oqcInspection.setBusinessUnitName(partJs.get(key).toString());
						}else{
							CommonUtil.setProperty(oqcInspection, key.toString(), partJs.get(key));
						}						
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if(oqcInspection.getOqcDefectiveItems()==null){
				oqcInspection.setOqcDefectiveItems(new ArrayList<OqcDefectiveItem>());
			}
			JSONObject itemJs = itemJson.getJSONObject(i);
			for(Object key : itemJs.keySet()){
				OqcDefectiveItem oqcItem=new OqcDefectiveItem();
				oqcItem.setCompanyId(ContextUtils.getCompanyId());
				oqcItem.setCreatedTime(new Date());
				oqcItem.setCreator(ContextUtils.getLoginName());
				oqcItem.setCreatorName(ContextUtils.getUserName());
				oqcItem.setModifiedTime(new Date());
				oqcItem.setModifier(ContextUtils.getLoginName());
				oqcItem.setModifierName(ContextUtils.getUserName());
				if(key.toString().equals("samplingCount")){
					if(itemJs.get(key)!=null&&!"".equals(itemJs.get(key))){
						oqcInspection.setSamplingCount(Integer.valueOf(itemJs.get(key).toString()));
					}
				}else if(key.toString().equals("count")&&itemJs.get(key)!=null&&!"".equals(itemJs.get(key))){
					if(itemJs.get(key)!=null&&!"".equals(itemJs.get(key))){
						oqcInspection.setCount(Integer.valueOf(itemJs.get(key).toString()));
					}
				}else if(key.toString().equals("stockBatch")){
					if(itemJs.get(key)!=null&&!"".equals(itemJs.get(key))){
						oqcInspection.setStockBatch(Integer.valueOf(itemJs.get(key).toString()));
					}					
				}else if(key.toString().equals("qualityBatch")){
					if(itemJs.get(key)!=null&&!"".equals(itemJs.get(key))){
						oqcInspection.setQualityBatch(Integer.valueOf(itemJs.get(key).toString()));
					}
				}else if(key.toString().equals("unQualityCount")){
					if(itemJs.get(key)!=null&&!"".equals(itemJs.get(key))){
						oqcInspection.setUnQualityCount(Integer.valueOf(itemJs.get(key).toString()));
					}
				}else if(key.toString().equals("unQualityRate")){
					oqcInspection.setUnQualityRate(itemJs.get(key).toString());
				}else if(key.toString().equals("qualityBatchRate")){
					oqcInspection.setQualityBatchRate(itemJs.get(key).toString());
				}else if(key.toString().equals("judgeResult")){
					oqcInspection.setJudgeResult(itemJs.get(key).toString());
				}else if(key.toString().equals("operator")){
					oqcInspection.setOperator(itemJs.get(key).toString());
				}else if(key.toString().equals("inspectionMan")){
					oqcInspection.setInspectionMan(itemJs.get(key).toString());
				}else{
					if(itemJs.get(key)!=null&&!"".equals(itemJs.get(key))){
						List<DefectionCode> list= defectionCodeManager.listDefectionCode(key.toString());
						DefectionCode defectionCode=list.get(0);
						oqcItem.setDefectionCodeNo(key.toString());
						oqcItem.setDefectionCodeName(defectionCode.getDefectionCodeName());
						oqcItem.setDefectionCodeValue(Integer.valueOf(itemJs.get(key).toString()));
						oqcItem.setDefectionTypeName(defectionCode.getDefectionType().getDefectionTypeName());
						oqcItem.setOqcInspection(oqcInspection);
						oqcInspection.getOqcDefectiveItems().add(oqcItem);
					}
				}				
			}
			this.saveOqcInspection(oqcInspection);
		}
	}		
	
}
