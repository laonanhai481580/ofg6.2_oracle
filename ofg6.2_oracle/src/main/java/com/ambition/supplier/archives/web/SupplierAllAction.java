package com.ambition.supplier.archives.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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

import com.ambition.supplier.archives.service.SupplierAllManager;
import com.ambition.supplier.entity.SupplierAll;
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
import com.opensymphony.xwork2.conversion.annotations.Conversion;

@Namespace("/supplier/archives/all")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/archives/all", type = "redirectAction") })
@Conversion
public class SupplierAllAction extends CrudActionSupport<SupplierAll> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private Page<SupplierAll> page;
	private SupplierAll supplier;
	@Autowired
	private LogUtilDao logUtilDao;

	private File myFile;
		
	private JSONObject params;//查询参数
		
 	@Autowired
	private SupplierAllManager supplierAllManager;


	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}


	public Page<SupplierAll> getPage() {
		return page;
	}

	public void setPage(Page<SupplierAll> page) {
		this.page = page;
	}

	public SupplierAll getSupplierAll() {
		return supplier;
	}

	public void setSupplierAll(SupplierAll supplier) {
		this.supplier = supplier;
	}

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
	
	public SupplierAll getModel() {
		return supplier;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			supplier = new SupplierAll();
			supplier.setCreatedTime(new Date());
			supplier.setCompanyId(ContextUtils.getCompanyId());
			supplier.setCreatorName(ContextUtils.getUserName());
			supplier.setCreator(ContextUtils.getLoginName());
			supplier.setLastModifiedTime(new Date());
			supplier.setLastModifier(ContextUtils.getUserName());
			supplier.setBusinessUnitName(ContextUtils.getSubCompanyName());
			supplier.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			supplier = supplierAllManager.getSupplierAll(id);
			Struts2Utils.getRequest().setAttribute("hisEstimateModelId",supplier.getEstimateModelId());
		}
	}

	
	
	
	
	@Action("save")
	@Override
	public String save() throws Exception {
		try {
			if(id == null){
				if(supplier.getSupplierEmail()!=null){
					supplier.setSupplierEmail(supplierAllManager.emailFormat(supplier.getSupplierEmail()));
					if(supplierAllManager.emailFormats(supplier.getSupplierEmail())){
						supplierAllManager.saveSupplierAll(supplier,"U");
						logUtilDao.debugLog("保存", supplier.toString());
					}else{
						throw new RuntimeException("供应商邮箱格式错误!");
					}
				}else{
					throw new RuntimeException("供应商邮箱不能为空!");
				}
			}else{
				if(supplier != null){
					if(supplier.getSupplierEmail()!=null&&!"".equals(supplier.getSupplierEmail())){
						supplier.setSupplierEmail(supplierAllManager.emailFormat(supplier.getSupplierEmail()));
						if(supplierAllManager.emailFormats(supplier.getSupplierEmail())){
							supplier.setLastModifiedTime(new Date());
							supplier.setLastModifier(ContextUtils.getUserName());
							supplierAllManager.saveSupplierAll(supplier,"S");
							logUtilDao.debugLog("修改", supplier.toString());
						}else{
							throw new RuntimeException("供应商邮箱格式错误!");
						}
					}else{
						throw new RuntimeException("供应商邮箱不能为空!");
					}
				}else{
					throw new RuntimeException("供应商为空!");
				}
			}
			this.renderText(JsonParser.getRowValue(supplier));
		} catch (Exception e) {
			log.error("保存失败",e);
			createErrorMessage("保存失败：" + e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			renderText("删除的对象不存在!");
		}else{
			try {
				supplierAllManager.deleteSupplierAll(deleteIds);
			} catch (Exception e) {
				log.error(e);
				renderText("删除失败:" + e.getMessage());
			}
		}
		return null;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getSuppliers() throws Exception {
		try{
			page = supplierAllManager.searchByPage(page);
			renderText(PageUtils.pageToJson(page));
		}catch (Exception e) {
			log.error(e);
		}
		return null;
	}

	/**
	 * 导出
	 * @return
	 * @throws Exception 
	 */
	@Action("exports")
	public String exports() throws Exception {
		page = supplierAllManager.searchByPage(new Page<SupplierAll>(Integer.MAX_VALUE));
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page, "SUPPLIER_SUPPLIER_ALL"),"供应商汇总台帐"));
		return null;
	}	

	@Action("import-form")
	public String importSupplierForm() throws Exception {
		return SUCCESS;
	}
	
	/**
	  * 方法名:导出导入模板 
	  * <p>功能说明：</p>
	  * @throws IOException
	 */
	@Action("download-excel")
	@LogInfo(optType="导出",message="导出导入模板")
	public void exportReport() throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/supplierAll.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "供应商资料模板.xlsx";
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
			log.error("导出失败!",e);
		} finally{
			if(inputStream != null){
				inputStream.close();
			}
		}
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

	@Override
	public String input() throws Exception {
		return null;
	}	
	
}
