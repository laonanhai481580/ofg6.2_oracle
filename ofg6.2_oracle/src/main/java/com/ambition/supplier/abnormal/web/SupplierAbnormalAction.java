package com.ambition.supplier.abnormal.web;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.abnormal.services.SupplierAbnormalManager;
import com.ambition.supplier.baseInfo.services.SupplierDefectionClassManager;
import com.ambition.supplier.baseInfo.services.SupplierDefectionItemManager;
import com.ambition.supplier.baseInfo.services.SupplierMaterialTypeGoalManager;
import com.ambition.supplier.entity.SupplierAbnormal;
import com.ambition.supplier.entity.SupplierAbnormalItem;
import com.ambition.supplier.entity.SupplierDefectionClass;
import com.ambition.supplier.entity.SupplierDefectionItem;
import com.ambition.supplier.entity.SupplierMaterialTypeGoal;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exportexcel.ExcelListDynamicColumnValue;
import com.norteksoft.mms.base.DynamicColumnValues;
import com.norteksoft.mms.base.utils.view.DynamicColumnDefinition;
import com.norteksoft.mms.form.enumeration.DataType;
import com.norteksoft.mms.form.enumeration.EditControlType;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 类名:上线异常数据
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  linshaowei
 * @version 1.00 2016年11月5日 发布
 */
@Namespace("/supplier/abnormal")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/abnormal", type = "redirectAction") })
public class SupplierAbnormalAction extends CrudActionSupport<SupplierAbnormal>{

		/**
		  *SupplierAbnormalAction.java2016年11月5日
		 */
	private static final long serialVersionUID = 5364456594768902530L;
	private Long id;
	private String deleteIds;
	private Page<SupplierAbnormal> page;
	private Page<SupplierAbnormal> dynamicPage;
	private JSONObject params;//查询参数
	private SupplierAbnormal supplierAbnormal;
	private String businessUnit;//所属事业部
	private String productType;//产品类别
	private List<DynamicColumnDefinition> dynamicColumn=new ArrayList<DynamicColumnDefinition>();
	private String colCode;//列代号
	private File myFile;
	@Autowired
    private SupplierMaterialTypeGoalManager supplierMaterialTypeGoalManager;
	@Autowired
	private SupplierDefectionClassManager supplierDefectionClassManager;
	@Autowired
	private SupplierDefectionItemManager supplierDefectionItemManager;
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SupplierAbnormalManager supplierAbnormalManager;
	@Override
	public SupplierAbnormal getModel() {
		return supplierAbnormal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public Page<SupplierAbnormal> getPage() {
		return page;
	}

	public void setPage(Page<SupplierAbnormal> page) {
		this.page = page;
	}

	public SupplierAbnormal getSupplierAbnormal() {
		return supplierAbnormal;
	}

	public void setSupplierAbnormal(SupplierAbnormal supplierAbnormal) {
		this.supplierAbnormal = supplierAbnormal;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}
	
	public List<DynamicColumnDefinition> getDynamicColumn() {
		return dynamicColumn;
	}

	public void setDynamicColumn(List<DynamicColumnDefinition> dynamicColumn) {
		this.dynamicColumn = dynamicColumn;
	}

	public String getColCode() {
		return colCode;
	}

	public void setColCode(String colCode) {
		this.colCode = colCode;
	}
	public Page<SupplierAbnormal> getDynamicPage() {
		return dynamicPage;
	}

	public void setDynamicPage(Page<SupplierAbnormal> dynamicPage) {
		this.dynamicPage = dynamicPage;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	
	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	@Override
	@Action("list")
	public String list() throws Exception {
		businessUnit = Struts2Utils.getParameter("businessUnit");
		productType = Struts2Utils.getParameter("productType");
		List<Option> businessUnits = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits");
		List<SupplierMaterialTypeGoal> list = supplierMaterialTypeGoalManager.getAllType();
        List<Option> productTypes = supplierMaterialTypeGoalManager.converExceptionLevelToList(list);
		if(StringUtils.isEmpty(businessUnit)){
			if(businessUnits.size()>0){
				businessUnit = businessUnits.get(0).getValue();
			}
		}
		if(StringUtils.isEmpty(productType)){
			if(productTypes.size()>0){
				productType = productTypes.get(0).getValue();
			}
		}
		ActionContext.getContext().put("businessUnits",businessUnits);
		ActionContext.getContext().put("productTypes",productTypes);
		StringBuffer colCodeBydefection = new StringBuffer();//列代号
		List<Map<String,Object>> definitions=null;
		if(productType==null){
			definitions = supplierAbnormalManager.queryDefectionsByBusinessUnit(businessUnit);
		}else{
			definitions = supplierAbnormalManager.queryDefectionsByBusinessUnit(businessUnit,productType);
		}
		
		JSONArray groupHeaders = new JSONArray();
		JSONObject lastHeader = null;
		String lastTypeName = "";
		Integer typeCount = 0;
		for(Map<String,Object> map : definitions){
			DynamicColumnDefinition dynamicColumnDefinition =new DynamicColumnDefinition();
			String fieldName = "a" + map.get("itemCode").toString().trim();
			dynamicColumnDefinition.setColName((String)map.get("itemName"));
			dynamicColumnDefinition.setName(fieldName);
			dynamicColumnDefinition.setEditable(true);
			dynamicColumnDefinition.setVisible(true);
			dynamicColumnDefinition.setEdittype(EditControlType.TEXT);
			dynamicColumnDefinition.setType(DataType.INTEGER);
			dynamicColumn.add(dynamicColumnDefinition);
			dynamicColumnDefinition.setColWidth("55px");
			if(lastTypeName.equals(map.get("typeName"))){
				typeCount++;
			}else{
				lastTypeName = map.get("typeName").toString();
				if(lastHeader != null){
					lastHeader.put("numberOfColumns", typeCount);
					groupHeaders.add(lastHeader);
				}
				lastHeader = new JSONObject();
				lastHeader.put("startColumnName", fieldName);
				lastHeader.put("titleText",lastTypeName);
				typeCount=1;
			}
			//-------------拼装动态列代号（备用）-------------------
			colCodeBydefection.append(dynamicColumnDefinition.getName()+",");
		}
		if(lastHeader != null && typeCount > 0){
			lastHeader.put("numberOfColumns", typeCount);
			groupHeaders.add(lastHeader);
		}
		ActionContext.getContext().put("groupHeaders", groupHeaders);
		ActionContext.getContext().put("systemCodes", ApiFactory.getSettingService().getOptionsByGroupCode("supplier-system-code"));
		this.setColCode(colCodeBydefection.toString());
		return SUCCESS;
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		if(productType==null){
			dynamicPage = supplierAbnormalManager.getListByBusinessUnit(dynamicPage, businessUnit);
		}else{
			dynamicPage = supplierAbnormalManager.getListByBusinessUnit(dynamicPage, businessUnit,productType);
		}
		this.renderText(PageUtils.dynamicPageToJson(dynamicPage,new DynamicColumnValues(){
			public void addValuesTo(List<Map<String, Object>> result) {
				for(Map<String, Object> map:result){
					Long supplierDataId=Long.valueOf(map.get("id").toString());
					SupplierAbnormal supplierAbnormal = supplierAbnormalManager.getSupplierAbnormal(supplierDataId);
					for(SupplierAbnormalItem defectiveItem:supplierAbnormal.getSupplierAbnormalItems()){
						map.put("a" + defectiveItem.getDefectionItemNo().trim(),defectiveItem.getDefectionItemValue());
					}
				}
			}			
		}));
		return null;
	}
	/**
	 * 导出台账
	 * @return
	 * @throws Exception
	 */
	@Action("export")
	@LogInfo(optType="导出",message="导出上线异常台账数据")
	public String export() throws Exception {
		try {
			Page<SupplierAbnormal> page = new Page<SupplierAbnormal>(65535);
			page = supplierAbnormalManager.search(page);
			if(productType==null){
				page = supplierAbnormalManager.getListByBusinessUnit(page, businessUnit);
			}else{
				page = supplierAbnormalManager.getListByBusinessUnit(page, businessUnit,productType);
			}
			List<Map<String,Object>> definitions=null;
			if(productType==null){
				definitions = supplierAbnormalManager.queryDefectionsByBusinessUnit(businessUnit);
			}else{
				definitions = supplierAbnormalManager.queryDefectionsByBusinessUnit(businessUnit,productType);
			}
			List<DynamicColumnDefinition>  dynamicColumnDefinitions  = new ArrayList<DynamicColumnDefinition>();
			for(Map<String,Object> map : definitions){
				DynamicColumnDefinition dynamicColumnDefinition =new DynamicColumnDefinition();
				String fieldName = "a" + map.get("itemCode").toString().trim();
				dynamicColumnDefinition.setColName((String)map.get("itemName"));
				dynamicColumnDefinition.setName(fieldName);
				dynamicColumnDefinition.setEditable(true);
				dynamicColumnDefinition.setVisible(true);
				dynamicColumnDefinition.setEdittype(EditControlType.TEXT);
				dynamicColumnDefinition.setType(DataType.INTEGER);
				dynamicColumnDefinitions.add(dynamicColumnDefinition);
			}
		    String fileFlags = ExcelUtil.exportListToExcel(page.getResult(),"上线不良数据台账",Struts2Utils.getParameter("_list_code"), 
		    		dynamicColumnDefinitions, new ExcelListDynamicColumnValue(){
						@Override
						public Map<String, String> getDynamicColumnValue(
								Object obj, int rowNum,
								Map<String, String> map) {
							SupplierAbnormal supplierAbnormal = (SupplierAbnormal) obj;
							for(SupplierAbnormalItem SupplierAbnormalItem:supplierAbnormal.getSupplierAbnormalItems()){
								map.put("a" + SupplierAbnormalItem.getDefectionItemNo().trim(),SupplierAbnormalItem.getDefectionItemValue()==null?"":SupplierAbnormalItem.getDefectionItemValue().toString());
							}
							return map;
						}
		    	
		    }, supplierAbnormalManager.getSupplierAbnormalDao().getSession());
            this.renderText(fileFlags);
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出上线不良数据失败",e);
		}
		return null;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			supplierAbnormal = new SupplierAbnormal();
			supplierAbnormal.setCreatedTime(new Date());
			supplierAbnormal.setCompanyId(ContextUtils.getCompanyId());
			supplierAbnormal.setCreatorName(ContextUtils.getUserName());
			supplierAbnormal.setCreator(ContextUtils.getLoginName());
			supplierAbnormal.setModifiedTime(new Date());
			supplierAbnormal.setModifier(ContextUtils.getUserName());
			supplierAbnormal.setBusinessUnitName(ContextUtils.getSubCompanyName());
			supplierAbnormal.setSupplierAbnormalItems(new ArrayList<SupplierAbnormalItem>());
			supplierAbnormal.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			supplierAbnormal = supplierAbnormalManager.getSupplierAbnormal(id);
		}
	}

	@Override
	@Action("save")
	@LogInfo(optType="修改/保存",message="修改/保存上线异常数据")
	public String save() throws Exception {
		supplierAbnormal.getSupplierAbnormalItems().clear();
		List<SupplierDefectionClass> lists =null;
		if(productType==null){
			lists=supplierDefectionClassManager.getSupplierDefectionClassByBusinessUnit(businessUnit);
		}else{
			lists=supplierDefectionClassManager.getSupplierDefectionClassByBusinessUnit(businessUnit,productType);
		}
		HashMap<String,Object> hs = new HashMap<String,Object>();
		//保存不良项值
		for(SupplierDefectionClass defectionClass :lists){
			List<SupplierDefectionItem> defectionCodes=defectionClass.getSupplierDefectionItems();
			for (SupplierDefectionItem defectionCode : defectionCodes) {
				SupplierAbnormalItem defectiveItem = new SupplierAbnormalItem();
				String defectiveItemValue = Struts2Utils.getParameter("a" + defectionCode.getSupplierDefectionItemNo().trim());
				if(defectiveItemValue==null||"".equals(defectiveItemValue)){
					continue;
				}
				if(defectiveItemValue!=null&&!"".equals(defectiveItemValue)){
					defectiveItem.setDefectionItemValue(Integer.valueOf(defectiveItemValue));
				}
				defectiveItem.setDefectionItemNo(defectionCode.getSupplierDefectionItemNo());
				defectiveItem.setDefectionItemName(defectionCode.getSupplierDefectionItemName());
				defectiveItem.setCompanyId(ContextUtils.getCompanyId());
				defectiveItem.setSupplierAbnormal(supplierAbnormal);
				hs.put(defectiveItem.getDefectionItemNo(), defectiveItem.getDefectionItemName());
				supplierAbnormal.getSupplierAbnormalItems().add(defectiveItem);
			}		
		}
			supplierAbnormal.setModifiedTime(new Date());
			supplierAbnormal.setProductType(productType);
			supplierAbnormal.setBusinessUnitName(businessUnit);
			supplierAbnormalManager.saveSupplierAbnormal(supplierAbnormal);			
			renderText(convertToJson(supplierAbnormal, hs));
		return null;
	}
	@Action("save-new")
	@LogInfo(optType="保存",message="保存上线不良数据")
	public String saveNew() throws Exception {
		String part=Struts2Utils.getParameter("part");
		String item=Struts2Utils.getParameter("item");		
		JSONObject result = new JSONObject();
		try{
			supplierAbnormalManager.saveNew(part,item);
			result.put("message", "保存成功!");
			result.put("error", false);
		}catch(Exception e){
			result.put("error", true);
			result.put("message", e.getMessage());
			e.printStackTrace();
		}
		renderText(result.toString());
		return null;
	}
	@Override
	@Action("delete")
	@LogInfo(optType="删除",message="删除上线异常数据")
	public String delete() throws Exception {
		supplierAbnormalManager.delete(deleteIds);
		return null;
	}

	@Override
	@Action("input")
	public String input() throws Exception {
		ActionContext.getContext().put("businessUnit", ApiFactory.getSettingService().getOptionsByGroupCode("processSections"));//厂区
		ActionContext.getContext().put("squads", ApiFactory.getSettingService().getOptionsByGroupCode("supplier-abnormal-squad"));//班别
		ActionContext.getContext().put("filtrates", ApiFactory.getSettingService().getOptionsByGroupCode("supplier-abnormal-filtrate"));//筛选
		ActionContext.getContext().put("factorys", ApiFactory.getSettingService().getOptionsByGroupCode("supplier-abnormal-factory"));//工厂
		
		return null;
	}
	@Action("input-new")
	public String inputNew() throws Exception {
		businessUnit = Struts2Utils.getParameter("businessUnit");
		List<Option> businessUnits = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits");
		if(StringUtils.isEmpty(businessUnit)){
			if(businessUnits.size()>0){
				businessUnit = businessUnits.get(0).getValue();
			}
		}
		ActionContext.getContext().put("businessUnits",businessUnits);
		ActionContext.getContext().put("squads", ApiFactory.getSettingService().getOptionsByGroupCode("supplier-abnormal-squad"));//班别
		ActionContext.getContext().put("filtrates", ApiFactory.getSettingService().getOptionsByGroupCode("supplier-abnormal-filtrate"));//筛选
		ActionContext.getContext().put("factorys", ApiFactory.getSettingService().getOptionsByGroupCode("supplier-abnormal-factory"));//工厂
		List<Map<String,Object>> definitions = supplierAbnormalManager.queryDefectionsByBusinessUnit(businessUnit);
		Map<String, Map<String, String>> defectionMap=new LinkedHashMap<String, Map<String, String>>();
		List<Map<String,String>> defectionList= new ArrayList<Map<String,String>>();
		for(Map<String,Object> map : definitions){
			if(defectionMap.containsKey(map.get("typeName").toString())){
				defectionMap.get(map.get("typeName").toString()).put(map.get("itemCode").toString(), map.get("itemName").toString());
			}else{
				Map<String, String> map1=new LinkedHashMap<String, String>();
				map1.put(map.get("itemCode").toString(), map.get("itemName").toString());
				map1.put("typeName", map.get("typeName").toString());
				defectionMap.put(map.get("typeName").toString(), map1);
			}						
		}
		for (Map<String, String> map : defectionMap.values()) {
			defectionList.add(map);
		}
		ActionContext.getContext().put("defectionList",defectionList);
		return SUCCESS;		
	}
	//返回json串
	@SuppressWarnings("rawtypes")
	public String convertToJson(SupplierAbnormal supplierAbnormal,HashMap hs){
		StringBuffer sb = new StringBuffer();
		sb.append(JsonParser.object2Json(supplierAbnormal));
		sb.delete(sb.length()-1, sb.length());
		sb.append(",");
		sb.append(JsonParser.object2Json(hs).substring(1,JsonParser.object2Json(hs).length()));
		return sb.toString();
	}
	@Action("import")
	public String imports() throws Exception {
		return SUCCESS;
	}
	@Action("import-datas")
	@LogInfo(optType="导入",message="导入上线不良数据")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				String businessUnit = Struts2Utils.getParameter("businessUnit");
				renderHtml(supplierAbnormalManager.importDatas(myFile,businessUnit));				
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	/**
	  * 方法名: 下载检设备参数模板
	  * <p>功能说明：下载检验标准的模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download-template")
	@LogInfo(optType="下载",message="下载供应商上线不良导入模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/supplier-abnormal.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "供应商上线不良模板.xls";
			byte byname[] = fileName.getBytes("gbk");
			fileName = new String(byname, "8859_1");
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append(fileName).append("\"")
					.toString());
			book.write(response.getOutputStream());
		}catch (Exception e) {
			log.error("下载供应商上线不良模板失败!",e);
		} finally{
			if(inputStream != null){
				inputStream.close();
			}
		}
		return null;
	}
	/**
	 * 
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}

}
