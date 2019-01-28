package com.ambition.supplier.materialadmit.supplierMaterialsFile.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.entity.SupplierMaterialsFile;
import com.ambition.supplier.materialadmit.supplierMaterialsFile.service.SupplierMaterialsFileManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;


@Namespace("/supplier/material-admit/supplier-materials-file")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location ="/supplier/material-admit/supplier-materials-file", type = "redirectAction") })
public class SupplierMaterialsFileAction extends CrudActionSupport<SupplierMaterialsFile>{
	
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	private Long id;
	private String ids;
	private String deleteIds;
	private SupplierMaterialsFile supplierMaterialsFile;
	private Page<SupplierMaterialsFile> page;
	@Autowired
	private SupplierMaterialsFileManager supplierMaterialsFileManager;
	@Autowired
	private LogUtilDao logUtilDao;
	
	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public SupplierMaterialsFile getSupplierMaterialsFile() {
		return supplierMaterialsFile;
	}

	public void setSupplierMaterialsFile(SupplierMaterialsFile supplierMaterialsFile) {
		this.supplierMaterialsFile = supplierMaterialsFile;
	}

	public Page<SupplierMaterialsFile> getPage() {
		return page;
	}

	public void setPage(Page<SupplierMaterialsFile> page) {
		this.page = page;
	}
		
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

		@Override
		public SupplierMaterialsFile getModel() {
			// TODO Auto-generated method stub
			return supplierMaterialsFile;
		}
		
		@Action("delete")
		@LogInfo(optType="删除",message="删除成本数据")
		@Override
		public String delete() throws Exception {
			try {
				supplierMaterialsFileManager.deleteSupplierMaterialsFile(deleteIds);
				Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据，编号："+deleteIds);
			} catch (Exception e) {
				// TODO: handle exception
				renderText("删除失败:" + e.getMessage());
				log.error("删除数据信息失败",e);
			}
			return null;
		}
		@Override
		public String input() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Action("list")
		@Override
		public String list() throws Exception {
			// TODO Auto-generated method stub
			return SUCCESS;
		}
		
		@Action("list-datas")
		@LogInfo(optType="数据",message="成本数据")
		public String listDates(){
			try {
				page = supplierMaterialsFileManager.search(page);
				renderText(PageUtils.pageToJson(page));
			} catch (Exception e) {
				// TODO: handle exception
				log.error("台账获取例表失败", e);
			}
			return null;
		}
		
		@Override
		protected void prepareModel() throws Exception {
			// TODO Auto-generated method stub
			if(id == null){
				supplierMaterialsFile = new SupplierMaterialsFile();
				supplierMaterialsFile.setCompanyId(ContextUtils.getCompanyId());
				supplierMaterialsFile.setCreatedTime(new Date());
				supplierMaterialsFile.setCreator(ContextUtils.getUserName());
				supplierMaterialsFile.setModifiedTime(new Date());
				supplierMaterialsFile.setModifier(ContextUtils.getUserName());
				supplierMaterialsFile.setBusinessUnitName(ContextUtils.getSubCompanyName());
				supplierMaterialsFile.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
			}else {
				supplierMaterialsFile = supplierMaterialsFileManager.getSupplierMaterialsFile(id);
			}
		}
		
		@Action("save")
		@LogInfo(optType="保存",message="保存成本数据")
		@Override
		public String save() throws Exception {
			try {
//				String fj = Struts2Utils.getParameter("attachmentFiles");
//				supplierMaterialsFile.setMaterialsFile(fj);
				supplierMaterialsFileManager.saveSupplierMaterialsFile(supplierMaterialsFile);
				renderText(JsonParser.getRowValue(supplierMaterialsFile));
				logUtilDao.debugLog("保存",supplierMaterialsFile.toString());
			} catch (Exception e) {
				// TODO: handle exception
				createErrorMessage("保存失败："+e.getMessage());
			}
			return null;
		}
		//创建返回消息
		public void createErrorMessage(String message){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("error",true);
			map.put("message",message);
			renderText(JSONObject.fromObject(map).toString());
		}
		/**
		 * 创建返回消息
		 * @param error
		 * @param message
		 * @return
		 */
		public void createMessage(String message){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("error",false);
			map.put("message",message);
			renderText(JSONObject.fromObject(map).toString());
		}
}
