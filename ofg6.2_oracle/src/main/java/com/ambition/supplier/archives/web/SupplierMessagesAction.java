package com.ambition.supplier.archives.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.archives.service.SupplierMessagesManager;
import com.ambition.supplier.entity.SupplierMessages;
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
@Namespace("/supplier/archives/supplier-messages")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/archives/supplier-messages", type = "redirectAction") })
public class SupplierMessagesAction extends CrudActionSupport<SupplierMessages>{
		/**
		  *SupplierMessagesAction.java2016年10月26日
		 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private JSONObject params;//查询参数
	private SupplierMessages supplierMessages;
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SupplierMessagesManager supplierMessagesManager;
	@Autowired
	private LogUtilDao logUtilDao;
	private Page<SupplierMessages> page;
	@Override
	public SupplierMessages getModel() {
		return supplierMessages;
	}

	public Page<SupplierMessages> getPage() {
		return page;
	}

	public void setPage(Page<SupplierMessages> page) {
		this.page = page;
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

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public SupplierMessages getSupplierMessages() {
		return supplierMessages;
	}

	public void setSupplierMessages(SupplierMessages supplierMessages) {
		this.supplierMessages = supplierMessages;
	}
	@Override
	@Action("delete")
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			renderText("删除的对象不存在!");
		}else{
			try {
				supplierMessagesManager.deleteSupplierMessages(deleteIds);
			} catch (Exception e) {
				log.error(e);
				renderText("删除失败:" + e.getMessage());
			}
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	@Action("list")
	public String list() throws Exception {
		return "SUCCESS";
	}
	@Action("list-datas")
	@LogInfo(optType="查询",message="供应商台帐查询")
	public String getSuppliers() throws Exception {
		try{
			page = supplierMessagesManager.searchByPage(page);
			renderText(PageUtils.pageToJson(page,"SUPPLIER_MESSAGES"));
		}catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			supplierMessages = new SupplierMessages();
			supplierMessages.setCreatedTime(new Date());
			supplierMessages.setCompanyId(ContextUtils.getCompanyId());
			supplierMessages.setCreatorName(ContextUtils.getUserName());
			supplierMessages.setCreator(ContextUtils.getLoginName());
			supplierMessages.setModifiedTime(new Date());
			supplierMessages.setModifier(ContextUtils.getUserName());
			supplierMessages.setBusinessUnitName(ContextUtils.getSubCompanyName());
			supplierMessages.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			supplierMessages = supplierMessagesManager.getSupplierMessages(id);
		}
	}
	/**
	 * 导出
	 * @return
	 * @throws Exception 
	 */
	@Action("exports")
	public String exports() throws Exception {
		Page<SupplierMessages> page = new Page<SupplierMessages>(65535);
		page = supplierMessagesManager.searchByPage(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page, "SUPPLIER_MESSAGE"),"通告台帐"));
		return null;
	}
	@Override
	@Action("save")
	public String save() throws Exception {
		try {
			if(id == null){
				supplierMessagesManager.saveSupplierMessages(supplierMessages);
				logUtilDao.debugLog("保存", supplierMessages.toString());
			}else{
				if(supplierMessages != null){
					supplierMessages.setModifiedTime(new Date());
					supplierMessages.setModifier(ContextUtils.getUserName());
					supplierMessagesManager.saveSupplierMessages(supplierMessages);
					logUtilDao.debugLog("修改", supplierMessages.toString());
				}else{
					throw new RuntimeException("供应商为空!");
				}
			}
			this.renderText(JsonParser.getRowValue(supplierMessages));
		} catch (Exception e) {
			log.error("保存失败",e);
			createErrorMessage("保存失败：" + e.getMessage());
		}
		return null;
	}
	/*
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	private void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
}
