package com.ambition.supplier.baseInfo.web;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.product.BaseAction;
import com.ambition.supplier.baseInfo.services.SupplierDefectionClassManager;
import com.ambition.supplier.entity.SupplierDefectionClass;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/supplier/base-info/defection-item")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/supplier/base-info/defection-item", type = "redirectAction") })
public class SupplierDefectionClassAction extends BaseAction<SupplierDefectionClass>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private String businessUnit;//所属事业部
	private String productType;//产品类别
	private SupplierDefectionClass defectionClass;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private SupplierDefectionClassManager defectionClassManager;
	private Page<SupplierDefectionClass> page;
	
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

	public SupplierDefectionClass getSupplierDefectionClass() {
		return defectionClass;
	}
	public void setSupplierDefectionClass(SupplierDefectionClass defectionClass) {
		this.defectionClass = defectionClass;
	}
	public Page<SupplierDefectionClass> getPage() {
		return page;
	}
	public void setPage(Page<SupplierDefectionClass> page) {
		this.page = page;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	@Override
	public SupplierDefectionClass getModel() {
		return defectionClass;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			defectionClass = new SupplierDefectionClass();
			defectionClass.setCompanyId(ContextUtils.getCompanyId());
			defectionClass.setCreatedTime(new Date());
			defectionClass.setCreator(ContextUtils.getUserName());
			defectionClass.setLastModifiedTime(new Date());
			defectionClass.setLastModifier(ContextUtils.getUserName());
			defectionClass.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			defectionClass = defectionClassManager.getSupplierDefectionClass(id);
		}
	}
	@Action("defection-class-delete")
	@LogInfo(optType="删除")
	@Override
	public String delete() throws Exception {
		try{
			String deleteNos=defectionClassManager.deleteSupplierDefectionClass(deleteIds);
			//日志消息,方便跟踪删除记录
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"删除物料类别台帐,物料类别为【" + deleteNos + "】!");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	@Action("defection-class-input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("defection-class-list")
	@Override
	public String list() throws Exception {
		renderMenu();
		//所属事业部
		businessUnit = Struts2Utils.getParameter("businessUnit");
		productType = Struts2Utils.getParameter("productType");
		List<Option> businessUnits = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits");
		if(StringUtils.isEmpty(businessUnit)){
			if(businessUnits.size()>0){
				businessUnit = businessUnits.get(0).getValue();
			}
		}
		ActionContext.getContext().put("businessUnits",businessUnits);
		ActionContext.getContext().put("systemCodes", ApiFactory.getSettingService().getOptionsByGroupCode("supplier-system-code"));
		return SUCCESS;
	}
	@Action("defection-class-save")
	@LogInfo(optType="保存",message="不良类别维护")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			defectionClass.setLastModifiedTime(new Date());
			defectionClass.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", defectionClass.toString());
		}else{
			logUtilDao.debugLog("保存", defectionClass.toString());
		}
		try{
			defectionClass.setBusinessUnitName(businessUnit);
			defectionClassManager.saveSupplierDefectionClass(defectionClass);
			this.renderText(JsonParser.object2Json(defectionClass));
		}catch(Exception e){
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}	
	@Action("defection-class-list-datas")
	public String getListDatas() throws Exception {
		businessUnit = Struts2Utils.getParameter("businessUnit");
		List<Option> businessUnits = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits");
		if(StringUtils.isEmpty(businessUnit)){
			if(businessUnits.size()>0){
				businessUnit = businessUnits.get(0).getValue();
			}
		}
		try {
			page = defectionClassManager.list(page,businessUnit);
			this.renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "供应商管理：基础设置-不良类别维护");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Action("exportCode")
	@LogInfo(optType="导出",message="不良类别维护")
	public String export() throws Exception {
		Page<SupplierDefectionClass> page = new Page<SupplierDefectionClass>(100000);
		page = defectionClassManager.list(page,businessUnit);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"SUPPLIER_DEFECTION_CLASS"),"defectionClass"));
		logUtilDao.debugLog("导出", "供应商管理：基础设置-不良类别维护");
		return null;
	}
}
