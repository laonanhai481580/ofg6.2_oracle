package com.ambition.aftersales.vlrr.web;

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

import com.ambition.aftersales.baseinfo.service.CustomerListManager;
import com.ambition.aftersales.baseinfo.service.CustomerPlaceManager;
import com.ambition.aftersales.baseinfo.service.DefectionClassManager;
import com.ambition.aftersales.baseinfo.service.DefectionItemManager;
import com.ambition.aftersales.baseinfo.service.VlrrWarmingManager;
import com.ambition.aftersales.entity.CustomerList;
import com.ambition.aftersales.entity.CustomerPlace;
import com.ambition.aftersales.entity.DefectionClass;
import com.ambition.aftersales.entity.DefectionItem;
import com.ambition.aftersales.entity.VlrrData;
import com.ambition.aftersales.entity.VlrrDefectiveItem;
import com.ambition.aftersales.entity.VlrrWarming;
import com.ambition.aftersales.vlrr.service.VlrrDataManager;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.supplier.develop.services.SupplierDevelopManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CheckMobileUtil;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exportexcel.ExcelListDynamicColumnValue;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.entity.organization.User;
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
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * 类名:OQC检验action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年8月31日 发布
 */
@Namespace("/aftersales/vlrr")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/aftersales/vlrr", type = "redirectAction") ,
		   @Result(name = "mobile-input", location = "input.jsp",type="dispatcher")})
public class VlrrDataAction extends BaseAction<VlrrData>{
	public static final String MOBILEINPUT = "mobile-input";
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;//删除ID
	private Page<VlrrData> page;
	private Page<VlrrData> dynamicPage;
	private VlrrData vlrrData;
	private String businessUnit;//所属事业部
	private String colCode;//列代号
	private File myFile;
	private List<DynamicColumnDefinition> dynamicColumn=new ArrayList<DynamicColumnDefinition>();
	@Autowired
	private SupplierDevelopManager supplierDevelopManager;
	@Autowired
	private VlrrDataManager vlrrDataManager;
	@Autowired
	private VlrrWarmingManager vlrrWarmingManager;
	@Autowired
	private DefectionClassManager defectionClassManager;
	@Autowired
	private DefectionItemManager defectionItemManager;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
    private CustomerListManager customerListManager;
	@Autowired
	private CustomerPlaceManager customerPlaceManager;
	private Logger log=Logger.getLogger(this.getClass());
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
	public Page<VlrrData> getPage() {
		return page;
	}
	public void setPage(Page<VlrrData> page) {
		this.page = page;
	}
	public Page<VlrrData> getDynamicPage() {
		return dynamicPage;
	}
	public void setDynamicPage(Page<VlrrData> dynamicPage) {
		this.dynamicPage = dynamicPage;
	}
	public List<DynamicColumnDefinition> getDynamicColumn() {
		return dynamicColumn;
	}
	public void setDynamicColumn(List<DynamicColumnDefinition> dynamicColumn) {
		this.dynamicColumn = dynamicColumn;
	}
	
	public VlrrData getVlrrData() {
		return vlrrData;
	}
	public void setVlrrData(VlrrData vlrrData) {
		this.vlrrData = vlrrData;
	}
	
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	
	public String getColCode() {
		return colCode;
	}
	public void setColCode(String colCode) {
		this.colCode = colCode;
	}
	
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	@Override
	public VlrrData getModel() {
		return vlrrData;
	}
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		String deleteNos=vlrrDataManager.deleteVlrrData(deleteIds);
		//日志消息,方便跟踪删除记录
		Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"删除VLRR数据,客户名称为【" + deleteNos + "】!");
		return null;
	}

	/**
	 * 新建页面
	 */
	@Override
	@Action("input")
	public String input() throws Exception {
		/*String editId = Struts2Utils.getParameter("editId");
		if(editId!=null&&!"".equals(editId)){
			vlrrData=vlrrDataManager.getVlrrData(Long.valueOf(editId));
		}*/
		businessUnit = Struts2Utils.getParameter("businessUnit");
		List<VlrrDefectiveItem> vlrrDefectiveItems=vlrrData.getVlrrDefectiveItems();
		Map<String, Map<String, String>> defectionMap=new HashMap<String, Map<String, String>>();
		Map<String,Object > valueMap=new HashMap<String, Object>();
		List<Map<String,String>> defectionList= new ArrayList<Map<String,String>>();
		for (VlrrDefectiveItem vlrrDefectiveItem : vlrrDefectiveItems) {
			valueMap.put(vlrrDefectiveItem.getDefectionItemNo(), vlrrDefectiveItem.getDefectionItemValue());
		}
		List<Map<String,Object>> definitions = vlrrDataManager.queryDefectionsByBusinessUnit(businessUnit);
		for(Map<String,Object> map : definitions){
			if(defectionMap.containsKey(map.get("typeName").toString())){
				defectionMap.get(map.get("typeName").toString()).put(map.get("itemCode").toString(), map.get("itemName").toString());
			}else{
				Map<String, String> map1=new HashMap<String, String>();
				map1.put(map.get("itemCode").toString(), map.get("itemName").toString());
				map1.put("typeName", map.get("typeName").toString());
				defectionMap.put(map.get("typeName").toString(), map1);
			}						
		}
		for (Map<String, String> map : defectionMap.values()) {
			defectionList.add(map);
		}
		ActionContext.getContext().put("businessUnit", businessUnit);
		ActionContext.getContext().put("defectionList", defectionList);
		ActionContext.getContext().put("valueMap", valueMap);
		List<CustomerList> customerList = customerListManager.listAll();
        List<Option> customerListOptions = customerListManager.converExceptionLevelToList(customerList);		
        ActionContext.getContext().put("customerNames",customerListOptions);
        List<CustomerPlace> customerPlaceList = customerPlaceManager.getPlaces();
        List<Option> customerPlaceOptions = customerPlaceManager.converThirdLevelToList(customerPlaceList);
        ActionContext.getContext().put("customerPlaces",customerPlaceOptions);
        ActionContext.getContext().put("productStructures", ApiFactory.getSettingService().getOptionsByGroupCode("afs-product-structure"));
		//判断是否手机端发起的请求
		if(CheckMobileUtil.isMobile(Struts2Utils.getRequest())){
			ActionContext.getContext().put("userTreeHtml",generateDepartmentUserTree());
			return MOBILEINPUT;
		}else{
			return null;
		}
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
		List<Option> customers=customerListManager.listAllForOptions();
	    ActionContext.getContext().put("customers",customers);
	    List<Option> customerFactorys=customerPlaceManager.listAllForOptions();
	    ActionContext.getContext().put("customerFactorys",customerFactorys);
	    ActionContext.getContext().put("productStructures",ApiFactory.getSettingService().getOptionsByGroupCode("afs-product-structure"));
	    //ActionContext.getContext().put("productionLines",ApiFactory.getSettingService().getOptionsByGroupCode("afs-product-structure"));
		List<Map<String,Object>> definitions = vlrrDataManager.queryDefectionsByBusinessUnit(businessUnit);
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
	@Action("save-new")
	@LogInfo(optType="保存",message="保存VLRR数据")
	public String saveNew() throws Exception {
		String part=Struts2Utils.getParameter("part");
		String item=Struts2Utils.getParameter("item");		
		JSONObject result = new JSONObject();
		try{
			vlrrDataManager.saveNew(part,item);
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
	/**
	 * 编辑保存
	 */
	@Action("edit-save")
	@LogInfo(optType="保存",message="保存VLRR数据")
	public String editSave() throws Exception {
		String vlrrItems = Struts2Utils.getParameter("vlrrItems");
		if(StringUtils.isNotEmpty(vlrrItems)){
			JSONArray childrenInfo = JSONArray.fromObject(vlrrItems);
			if(vlrrData.getVlrrDefectiveItems()==null){
				vlrrData.setVlrrDefectiveItems(new ArrayList<VlrrDefectiveItem>());
			}else{
				vlrrData.getVlrrDefectiveItems().clear();
			}
			JSONObject js = childrenInfo.getJSONObject(0);
			for(Object key : js.keySet()){
				VlrrDefectiveItem tr = new VlrrDefectiveItem();
				tr.setCompanyId(ContextUtils.getCompanyId());
				tr.setCreatedTime(new Date());
				tr.setCreator(ContextUtils.getLoginName());
				tr.setCreatorName(ContextUtils.getUserName());
				tr.setModifiedTime(new Date());
				tr.setModifier(ContextUtils.getLoginName());
				tr.setModifierName(ContextUtils.getUserName());
				tr.setVlrrData(vlrrData);
				String value=js.get(key).toString();
				tr.setDefectionItemNo(key.toString());
				if(value!=null&&!"".equals(value)){
					tr.setDefectionItemValue(Integer.valueOf(value));
				}else{
					continue;
				}
				List<DefectionItem> list=defectionItemManager.listDefectionItem(key.toString());
				DefectionItem item=list.get(0);
				tr.setDefectionItemName(item.getDefectionItemName());
				tr.setDefectionClass(item.getDefectionClass().getDefectionClass());
				vlrrData.getVlrrDefectiveItems().add(tr);
			}
			vlrrData.setBusinessUnitName(businessUnit);
			vlrrDataManager.saveVlrrData(vlrrData);
		}
		//判断是否手机端发起的请求
		if(CheckMobileUtil.isMobile(Struts2Utils.getRequest())){
			ActionContext.getContext().put("userTreeHtml",generateDepartmentUserTree());
			input();
			return MOBILEINPUT;
		}else{
			return null;
		}
	} 

	@Action("list")
	public String list() throws Exception {
		//所属事业部
		businessUnit = Struts2Utils.getParameter("businessUnit");
		List<Option> businessUnits = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits");
		if(StringUtils.isEmpty(businessUnit)){
			if(businessUnits.size()>0){
				businessUnit = businessUnits.get(0).getValue();
			}
		}
		ActionContext.getContext().put("businessUnits",businessUnits);
		StringBuffer colCodeBydefection = new StringBuffer();//列代号
		List<Map<String,Object>> definitions = vlrrDataManager.queryDefectionsByBusinessUnit(businessUnit);
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
		this.setColCode(colCodeBydefection.toString());
		return SUCCESS;
	}

	@Action("hide-list")
	public String hideList() throws Exception {
		//所属事业部
		businessUnit = Struts2Utils.getParameter("businessUnit");
		List<Option> businessUnits = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits");
		if(StringUtils.isEmpty(businessUnit)){
			if(businessUnits.size()>0){
				businessUnit = businessUnits.get(0).getValue();
			}
		}
		ActionContext.getContext().put("businessUnits",businessUnits);
		StringBuffer colCodeBydefection = new StringBuffer();//列代号
		List<Map<String,Object>> definitions = vlrrDataManager.queryDefectionsByBusinessUnit(businessUnit);
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
		this.setColCode(colCodeBydefection.toString());
		return SUCCESS;
	}	
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			vlrrData=new VlrrData();
			vlrrData.setCompanyId(ContextUtils.getCompanyId());
			vlrrData.setCreatedTime(new Date());
			vlrrData.setCreator(ContextUtils.getUserName());
			vlrrData.setModifiedTime(new Date());
			vlrrData.setModifier(ContextUtils.getLoginName());
			vlrrData.setModifierName(ContextUtils.getUserName());
			vlrrData.setVlrrDefectiveItems(new ArrayList<VlrrDefectiveItem>());
			vlrrData.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			vlrrData=vlrrDataManager.getVlrrData(id);			
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存VLRR数据")
	public String save() throws Exception {
		vlrrData.getVlrrDefectiveItems().clear();
		List<DefectionClass> lists = defectionClassManager.getDefectionClassByBusinessUnit(businessUnit);
		int totalDefectiveItemValue = 0;//不良项累加值
		HashMap<String,Object> hs = new HashMap<String,Object>();
		//保存不良项值
		for(DefectionClass defectionClass :lists){
			List<DefectionItem> defectionCodes=defectionClass.getDefectionItems();
			for (DefectionItem defectionCode : defectionCodes) {
				VlrrDefectiveItem defectiveItem = new VlrrDefectiveItem();
				String defectiveItemValue = Struts2Utils.getParameter("a" + defectionCode.getDefectionItemNo().trim());
				if(defectiveItemValue==null||"".equals(defectiveItemValue)){
					continue;
				}
				if(defectiveItemValue!=null&&!"".equals(defectiveItemValue)){
					defectiveItem.setDefectionItemValue(Integer.valueOf(defectiveItemValue));
					totalDefectiveItemValue = totalDefectiveItemValue+Integer.valueOf(defectiveItemValue);//累加计算不良项值
				}
				defectiveItem.setDefectionClass(defectionClass.getDefectionClass());
				defectiveItem.setDefectionItemNo(defectionCode.getDefectionItemNo());
				defectiveItem.setDefectionItemName(defectionCode.getDefectionItemName());
				defectiveItem.setCompanyId(ContextUtils.getCompanyId());
				defectiveItem.setVlrrData(vlrrData);;					
				hs.put(defectiveItem.getDefectionItemNo(), defectiveItem.getDefectionItemName());
				vlrrData.getVlrrDefectiveItems().add(defectiveItem);
			}		
		}
		if(vlrrData.getUnqualifiedCount()!=totalDefectiveItemValue){//判断不良数值是否与不良项累加值相等
			createErrorMessage("数据有误，不良数量与不良项值累加数量不相等！");
		}else{
			vlrrData.setBusinessUnitName(businessUnit);
			vlrrDataManager.saveVlrrData(vlrrData);
			//不良率超过目标值时邮件通知相关人员
			String unqualifiedRate=vlrrData.getUnqualifiedRate();
			VlrrWarming vlrrWarming=vlrrWarmingManager.getByModel(vlrrData.getOfilmModel());
			if(vlrrWarming!=null){
				String target=vlrrWarming.getTargetValue();
				String rate=unqualifiedRate.substring(0,unqualifiedRate.indexOf('%'));
				Float a=Float.parseFloat(rate);
				Float b=Float.parseFloat(target);
				if(a>b){
					vlrrWarmingManager.saveMailSettings(vlrrData,vlrrWarming.getWarmingManLogin());
				}
			}
			renderText(convertToJson(vlrrData, hs));
		}				
		return null;
	}
	
	//返回json串
	@SuppressWarnings("rawtypes")
	public String convertToJson(VlrrData vlrrData,HashMap hs){
		StringBuffer sb = new StringBuffer();
		sb.append(JsonParser.object2Json(vlrrData));
		sb.delete(sb.length()-1, sb.length());
		sb.append(",");
		sb.append(JsonParser.object2Json(hs).substring(1,JsonParser.object2Json(hs).length()));
		return sb.toString();
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		dynamicPage = vlrrDataManager.getListByBusinessUnit(dynamicPage, businessUnit);
		this.renderText(PageUtils.dynamicPageToJson(dynamicPage,new DynamicColumnValues(){
			public void addValuesTo(List<Map<String, Object>> result) {
				for(Map<String, Object> map:result){
					Long vlrrDataId=Long.valueOf(map.get("id").toString());
					VlrrData vlrrData = vlrrDataManager.getVlrrData(vlrrDataId);
					for(VlrrDefectiveItem oqcDefectiveItem:vlrrData.getVlrrDefectiveItems()){
						map.put("a" + oqcDefectiveItem.getDefectionItemNo().trim(),oqcDefectiveItem.getDefectionItemValue());
					}
				}
			}
			
		}));
		return null;
	}
	
	@Action("hide-list-datas")
	public String hideListDatas() throws Exception {
		dynamicPage = vlrrDataManager.getListByBusinessUnitHide(dynamicPage, businessUnit);
		this.renderText(PageUtils.dynamicPageToJson(dynamicPage,new DynamicColumnValues(){
			public void addValuesTo(List<Map<String, Object>> result) {
				for(Map<String, Object> map:result){
					Long vlrrDataId=Long.valueOf(map.get("id").toString());
					VlrrData vlrrData = vlrrDataManager.getVlrrData(vlrrDataId);
					for(VlrrDefectiveItem oqcDefectiveItem:vlrrData.getVlrrDefectiveItems()){
						map.put("a" + oqcDefectiveItem.getDefectionItemNo().trim(),oqcDefectiveItem.getDefectionItemValue());
					}
				}
			}
			
		}));
		return null;
	}	
	
	@Action("export")
	@LogInfo(optType="导出", message="VLRR数据")
	public String export() throws Exception {
		try {
			Page<VlrrData> page = new Page<VlrrData>(65535);
			@SuppressWarnings("unused")
			Map<String,String> fieldMap = new HashMap<String, String>();
			page = vlrrDataManager.searchBusinessUnit(page,businessUnit);
			List<Map<String,Object>> definitions = vlrrDataManager.queryDefectionsByBusinessUnit(businessUnit);
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
		    String fileFlags = ExcelUtil.exportListToExcel(page.getResult(),"VLRR数据台账",Struts2Utils.getParameter("_list_code"), 
		    		dynamicColumnDefinitions, new ExcelListDynamicColumnValue(){
						@Override
						public Map<String, String> getDynamicColumnValue(
								Object obj, int rowNum,
								Map<String, String> map) {
							VlrrData vlrrData = (VlrrData) obj;
							for(VlrrDefectiveItem oqcDefectiveItem:vlrrData.getVlrrDefectiveItems()){
								map.put("a" + oqcDefectiveItem.getDefectionItemNo().trim(),oqcDefectiveItem.getDefectionItemValue()==null?"":oqcDefectiveItem.getDefectionItemValue().toString());
							}
							return map;
						}
		    	
		    }, vlrrDataManager.getVlrrDataDao().getSession());
            this.renderText(fileFlags);
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出VLRR数据失败",e);
		}
		return null;
	}
	
	@Action("export-hide")
	@LogInfo(optType="导出", message="VLRR数据")
	public String exportHide() throws Exception {
		try {
			Page<VlrrData> page = new Page<VlrrData>(65535);
			@SuppressWarnings("unused")
			Map<String,String> fieldMap = new HashMap<String, String>();
			page = vlrrDataManager.searchBusinessUnitHide(page,businessUnit);
			List<Map<String,Object>> definitions = vlrrDataManager.queryDefectionsByBusinessUnit(businessUnit);
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
		    String fileFlags = ExcelUtil.exportListToExcel(page.getResult(),"VLRR数据台账",Struts2Utils.getParameter("_list_code"), 
		    		dynamicColumnDefinitions, new ExcelListDynamicColumnValue(){
						@Override
						public Map<String, String> getDynamicColumnValue(
								Object obj, int rowNum,
								Map<String, String> map) {
							VlrrData vlrrData = (VlrrData) obj;
							for(VlrrDefectiveItem oqcDefectiveItem:vlrrData.getVlrrDefectiveItems()){
								map.put("a" + oqcDefectiveItem.getDefectionItemNo().trim(),oqcDefectiveItem.getDefectionItemValue()==null?"":oqcDefectiveItem.getDefectionItemValue().toString());
							}
							return map;
						}
		    	
		    }, vlrrDataManager.getVlrrDataDao().getSession());
            this.renderText(fileFlags);
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出VLRR数据失败",e);
		}
		return null;
	}	
	
	public void prepareEditSave() throws Exception{
		prepareModel();
	}
	
	   
   /**
	 * 生成部门用户
	 * @return
	 */
	public StringBuffer generateDepartmentUserTree(){
		List<com.norteksoft.acs.entity.organization.Department> allDepartments = supplierDevelopManager.queryAllDepartments();
		List<User> allUsers = supplierDevelopManager.queryAllUsers();
		StringBuffer userHtml = new StringBuffer();
		List<com.norteksoft.acs.entity.organization.Department> parentDepts = queryChildrens(allDepartments,null);
		for(com.norteksoft.acs.entity.organization.Department dept : parentDepts){
			generateHtml(userHtml, dept, allDepartments, allUsers);
		}
		//无部门用户
//				generateHtml(userHtml,null, allDepartments, allUsers);
		return userHtml;
	}
	private List<com.norteksoft.acs.entity.organization.Department> queryChildrens(List<com.norteksoft.acs.entity.organization.Department> allDepartments,Long parentId){
		List<com.norteksoft.acs.entity.organization.Department> children = new ArrayList<com.norteksoft.acs.entity.organization.Department>();
		for(com.norteksoft.acs.entity.organization.Department d : allDepartments){
			if(parentId==null){
				if(d.getParent()==null){
					children.add(d);
				}
			}else{
				if(d.getParent()!=null&&d.getParent().getId().equals(parentId)){
					children.add(d);
				}
			}
		}
		return children;
	}
	private void generateHtml(StringBuffer html,com.norteksoft.acs.entity.organization.Department dept,
			List<com.norteksoft.acs.entity.organization.Department> allDepartments,
			List<User> allUsers){
		List<User> users = queryUsersByDeptId(allUsers,dept==null?null:dept.getId());
		List<com.norteksoft.acs.entity.organization.Department> children = queryChildrens(allDepartments,dept==null?null:dept.getId());
		if(users.isEmpty()&&children.isEmpty()){
			//html.append("<p>"+dept.getName()+"</p>");
		}else{
			html.append("<li style=\"margin-left:20px;\">");
			html.append("<label><a href=\"javascript:;\" onclick=\"showdiv('"+(dept==null?"noId":dept.getName())+"')\" >"+(dept==null?"无部门用户":dept.getName())+"</a></label>");
			html.append("<div style=\"display:none;\" id="+(dept==null?"noId":dept.getName())+"><ul class=\"two\" style=\"margin-left:30px;\">");
			for(User user : users){
				html.append("<li><label><input  type=\"checkbox\" name='"+user.getName()+"' deptName="+(dept==null?"noId":dept.getName())+"  value='"+user.getLoginName()+"'><a  href=\"javascript:;\" >"+user.getName()+"</a></label></li>");
				
			}
			for(com.norteksoft.acs.entity.organization.Department child : children){
				generateHtml(html,child,allDepartments,allUsers);
			}
			html.append("</ul></div>");
			html.append("</li>");
		}
	}
	private List<User> queryUsersByDeptId(List<User> allUsers,Long deptId){
		List<User> users = new ArrayList<User>();
		for(User u : allUsers){
			if(deptId==null){
				if(u.getMainDepartmentId()==null){
					users.add(u);
				}
			}else{
				if(u.getMainDepartmentId()!=null&&u.getMainDepartmentId().equals(deptId)){
					users.add(u);
				}
			}
		}
		return users;
	}
	
	@Action("import")
	public String imports() throws Exception {
		return SUCCESS;
	}
	
	@Action("import-datas")
	@LogInfo(optType="导入",message="导入VLRR数据")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				String businessUnit = Struts2Utils.getParameter("businessUnit");
				renderHtml(vlrrDataManager.importDatas(myFile,businessUnit));				
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
	@LogInfo(optType="下载",message="下载VLRR数据导入模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/afs-vlrr-template.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "VLRR数据导入模板.xls";
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
			log.error("下载VLRR数据导入模板失败!",e);
		} finally{
			if(inputStream != null){
				inputStream.close();
			}
		}
		return null;
	}
	
	@Action("hiddenState")
	@LogInfo(optType="隐藏")
	public String hiddenState(){
		JSONObject result = new JSONObject();
		String eid = Struts2Utils.getParameter("id");
		String type = Struts2Utils.getParameter("type");
		String formNo="";
		try {
			formNo=vlrrDataManager.hiddenState(eid,type);
			result.put("message", "操作成功!");
			result.put("error", false);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("error", true);
			result.put("message", e.getMessage());
		}	
		formNo=formNo.substring(0, formNo.length()-1);
		String str="";
		if("Y".equals(type)){
			str="取消敏感数据标记";
		}else{
			str="标记数据为敏感数据";
		}
		Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,str+"，日期和客户为:"+formNo);
		renderText(result.toString());
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
