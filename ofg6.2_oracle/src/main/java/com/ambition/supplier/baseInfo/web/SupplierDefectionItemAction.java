package com.ambition.supplier.baseInfo.web;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.product.BaseAction;
import com.ambition.supplier.baseInfo.services.SupplierDefectionClassManager;
import com.ambition.supplier.baseInfo.services.SupplierDefectionItemManager;
import com.ambition.supplier.entity.SupplierDefectionClass;
import com.ambition.supplier.entity.SupplierDefectionItem;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;

@Namespace("/supplier/base-info/defection-item")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/supplier/base-info/defection-item", type = "redirectAction") })
public class SupplierDefectionItemAction extends BaseAction<SupplierDefectionItem>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private SupplierDefectionItem defectionItem;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private SupplierDefectionItemManager defectionItemManager;
	private Page<SupplierDefectionItem> page;
	private List<SupplierDefectionItem> list;
	private File myFile;
 	//不良类别Id
	private Long defectionClassId;
	@Autowired
	private SupplierDefectionClassManager defectionClassManager;
	private SupplierDefectionClass defectionClass;
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

	public SupplierDefectionItem getSupplierDefectionItem() {
		return defectionItem;
	}

	public void setSupplierDefectionItem(SupplierDefectionItem defectionItem) {
		this.defectionItem = defectionItem;
	}

	public Page<SupplierDefectionItem> getPage() {
		return page;
	}

	public void setPage(Page<SupplierDefectionItem> page) {
		this.page = page;
	}

	public List<SupplierDefectionItem> getList() {
		return list;
	}

	public void setList(List<SupplierDefectionItem> list) {
		this.list = list;
	}

	public Long getSupplierDefectionClassId() {
		return defectionClassId;
	}

	public void setSupplierDefectionClassId(Long defectionClassId) {
		this.defectionClassId = defectionClassId;
	}

	public SupplierDefectionClass getSupplierDefectionClass() {
		return defectionClass;
	}

	public void setSupplierDefectionClass(SupplierDefectionClass defectionClass) {
		this.defectionClass = defectionClass;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	@Override
	public SupplierDefectionItem getModel() {
		return defectionItem;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			String myId = Struts2Utils.getParameter("defectionClassId");
			
			defectionItem = new SupplierDefectionItem();
			if(myId != null && !myId.equals("")){
				defectionClassId = Long.valueOf(myId);
				defectionClass = defectionClassManager.getSupplierDefectionClass(defectionClassId);
			}else{
				defectionClass = null;
			}
			defectionItem.setCompanyId(ContextUtils.getCompanyId());
			defectionItem.setCreatedTime(new Date());
			defectionItem.setCreator(ContextUtils.getUserName());
			defectionItem.setSupplierDefectionClass(defectionClass);
			defectionItem.setLastModifiedTime(new Date());
			defectionItem.setLastModifier(ContextUtils.getUserName());
			defectionItem.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			defectionItem = defectionItemManager.getSupplierDefectionItem(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("save")
	@LogInfo(optType="保存",message="保存数据")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			defectionItem.setLastModifiedTime(new Date());
			defectionItem.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", defectionItem.toString());
		}else{
			logUtilDao.debugLog("保存", defectionItem.toString());
		}
		try {
			defectionItemManager.saveSupplierDefectionItem(defectionItem);
			renderText(JsonParser.object2Json(defectionItem));
		} catch (Exception e) {
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="不良项目维护")
	@Override
	public String delete() throws Exception {
		defectionItemManager.deleteSupplierDefectionItem(deleteIds);
		return null;
	}

	@Action("delete-subs")
	@LogInfo(optType="删除")
	public String deleteSubs() throws Exception {
		if(defectionClassId != null && defectionClassId != 0){
			defectionClass = defectionClassManager.getSupplierDefectionClass(defectionClassId);
		}else{
			defectionClass = null;
		}
		list = defectionItemManager.listAll(defectionClass);
		for(SupplierDefectionItem defectionItem: list){
			defectionItemManager.deleteSupplierDefectionItem(defectionItem);
			//日志消息,方便跟踪删除记录
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"删除不良项目,【" + defectionItem.getSupplierDefectionItemName() + "】!");
		}
		return null;
	}
	
	@Action("search-subs")
	public String searchSubs() throws Exception {
		if(defectionClassId != null && defectionClassId != 0){
			defectionClass = defectionClassManager.getSupplierDefectionClass(defectionClassId);
		}else{
			defectionClass = null;
		}
		list = defectionItemManager.listAll(defectionClass);
		if(list.size() >= 1){
			renderText("have");
		}else{
			renderText("no");
		}
		return null;
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		renderMenu();
		if(defectionClassId != null && defectionClassId != 0){
			defectionClass = defectionClassManager.getSupplierDefectionClass(defectionClassId);
		}else{
			defectionClass = null;
		}
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception{
		String myId = Struts2Utils.getParameter("defectionClassId");
		if(myId != null && !myId.equals("")){
			defectionClassId = Long.valueOf(myId);
			defectionClass = defectionClassManager.getSupplierDefectionClass(defectionClassId);
		}
		String code = null;
		try {
			code = Struts2Utils.getParameter("defectionItem");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(code != null && !code.equals("")){
			page = defectionItemManager.list(page, code);
		}else{
			page = defectionItemManager.list(page, defectionClass);
		}		
		renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "供应商质量管理：基础设置-不良项目维护");
		return null;
	}
	@Action("exportCode2")
	@LogInfo(optType="导出",message="不良项目维护")
	public String export() throws Exception {
		Page<SupplierDefectionItem> page = new Page<SupplierDefectionItem>(100000);
		String myId = Struts2Utils.getParameter("defectionClassId");
		if(myId != null && !myId.equals("")){
			defectionClassId = Long.valueOf(myId);
			defectionClass = defectionClassManager.getSupplierDefectionClass(defectionClassId);
		}
		page = defectionItemManager.list(page, defectionClass);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"SUPPLIER_DEFECTION_ITEM"),"supplierDefectionItem"));
		logUtilDao.debugLog("导出", "供应商质量管理：基础设置-不良项目维护");
		return null;
	}
	@Action("imports")
	public String imports() throws Exception {
		return "imports";
	}
	
	@Action("import-excel-datas")
	@LogInfo(optType="导入数据",message="不良项目维护")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(defectionItemManager.importFile(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	
}
