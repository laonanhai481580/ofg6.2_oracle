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

import com.ambition.supplier.archives.service.SupplierFileManager;
import com.ambition.supplier.entity.SupplierFile;
import com.ambition.supplier.entity.SupplierMessages;
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
import com.opensymphony.xwork2.ActionContext;
@Namespace("/supplier/archives/supplier-file")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/archives/supplier-file", type = "redirectAction") })
public class SupplierFileAction extends CrudActionSupport<SupplierFile>{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private JSONObject params;//查询参数
	private SupplierFile supplierFile;
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SupplierFileManager supplierFileManager;
	@Autowired
	private LogUtilDao logUtilDao;
	private Page<SupplierFile> page;
	@Override
	public SupplierFile getModel() {
		return supplierFile;
	}

	public Page<SupplierFile> getPage() {
		return page;
	}

	public void setPage(Page<SupplierFile> page) {
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

	public SupplierFile getSupplierFile() {
		return supplierFile;
	}

	public void setSupplierFile(SupplierFile supplierFile) {
		this.supplierFile = supplierFile;
	}
	@Override
	@Action("delete")
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			renderText("删除的对象不存在!");
		}else{
			try {
				String message=	supplierFileManager.deleteSupplierFile(deleteIds);
				renderText(message);
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
		ActionContext.getContext().put("systemCodes", ApiFactory.getSettingService().getOptionsByGroupCode("supplier-system-code"));
		return "SUCCESS";
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		try{
			String department=CommonUtil.getMainDepartMent();
			if(department!=null&&"供应商".equals(department)){
				page = supplierFileManager.searchBySupplier(page);
			}else{
				page = supplierFileManager.search(page);
			}			
			renderText(PageUtils.pageToJson(page));
		}catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			supplierFile = new SupplierFile();
			supplierFile.setCreatedTime(new Date());
			supplierFile.setCompanyId(ContextUtils.getCompanyId());
			supplierFile.setCreatorName(ContextUtils.getUserName());
			supplierFile.setCreator(ContextUtils.getLoginName());
			supplierFile.setModifiedTime(new Date());
			supplierFile.setModifier(ContextUtils.getUserName());
			supplierFile.setBusinessUnitName(ContextUtils.getSubCompanyName());
			supplierFile.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			supplierFile = supplierFileManager.getSupplierFile(id);
		}
	}
	/**
	 * 导出
	 * @return
	 * @throws Exception 
	 */
	@Action("exports")
	public String exports() throws Exception {
		Page<SupplierFile> page = new Page<SupplierFile>(65535);
		String department=CommonUtil.getMainDepartMent();
		if(department!=null&&"供应商".equals(department)){
			page = supplierFileManager.searchBySupplier(page);
		}else{
			page = supplierFileManager.search(page);
		}	
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page, "SUPPLIER_FILE"),"供应商汇报资料"));
		return null;
	}
	@Override
	@Action("save")
	public String save() throws Exception {
		if(id != null && id != 0){
			supplierFile.setModifiedTime(new Date());
			supplierFile.setModifier(ContextUtils.getLoginName());
			supplierFile.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", supplierFile.toString());
		}else{
			logUtilDao.debugLog("保存", supplierFile.toString());
		}
		try {
			String zb1 = Struts2Utils.getParameter("attachmentFiles");
			supplierFile.setMaterialsFile(zb1);
			supplierFileManager.saveSupplierFile(supplierFile);
			this.renderText(JsonParser.object2Json(supplierFile));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存供应商汇报资料失败  ",e);
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
