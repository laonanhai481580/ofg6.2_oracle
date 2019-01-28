package com.ambition.carmfg.oqc.web;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.entity.OqcException;
import com.ambition.carmfg.oqc.service.OqcExceptionManager;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
/**
 * 
 * 类名:客退率台账action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00  2018年4月12日 发布
 */
@Namespace("/carmfg/oqc/exception")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/carmfg/oqc/exception", type = "redirectAction") })
public class OqcExceptionAction extends BaseAction<OqcException>{

	private static final long serialVersionUID = 1L;
	private OqcException oqcException;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private Page<OqcException> page;
	private File myFile;
	private String businessUnit;//所属事业部
	@Autowired
	private OqcExceptionManager oqcExceptionManager;
	@Override
	public OqcException getModel() {
		return oqcException;
	}
	@Action("delete")
	@LogInfo(optType="删除",message="OQC异常管理")
	public String delete() throws Exception {
		try {
			oqcExceptionManager.deleteOqcException(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除OQC异常管理数据"+deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除OQC异常管理失败",e);
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("list")
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			oqcException=new OqcException();
			oqcException.setCompanyId(ContextUtils.getCompanyId());
			oqcException.setCreatedTime(new Date());
			oqcException.setCreator(ContextUtils.getLoginName());
			oqcException.setModifiedTime(new Date());
			oqcException.setModifier(ContextUtils.getLoginName());
			oqcException.setModifierName(ContextUtils.getUserName());
		}else{
			oqcException=oqcExceptionManager.getOqcException(id);
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存OQC异常管理")
	public String save() throws Exception {
		if(id != null && id != 0){
			oqcException.setModifiedTime(new Date());
			oqcException.setModifier(ContextUtils.getLoginName());
			oqcException.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", oqcException.toString());
		}else{
			logUtilDao.debugLog("保存", oqcException.toString());
		}
		try {
			oqcExceptionManager.saveOqcException(oqcException);
			this.renderText(JsonParser.object2Json(oqcException));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存OQC异常管理失败  ",e);
		}		
		return null;
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = oqcExceptionManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "OQC异常管理");
		} catch (Exception e) {
			log.error("查询OQC异常管理失败  ",e);
		}		
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出", message="OQC异常管理")
	public String export() throws Exception {
		try {
			Page<OqcException> page = new Page<OqcException>(65535);
			page = oqcExceptionManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"OQC异常管理台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出OQC异常管理失败",e);
		}
		return null;
	}
	@Action("import")
	public String imports() throws Exception {
		return SUCCESS;
	}
	
	@Action("import-datas")
	@LogInfo(optType="导人", message="OQC异常管理")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(oqcExceptionManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	
	/**
	  * 方法名: 下载OQC异常管理模板
	  * <p>功能说明：下载OQC异常管理模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download")
	@LogInfo(optType="下载",message="下载OQC异常管理模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/mfg-oqc-exception.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "OQC异常管理台账导入模板.xls";
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
		return null;
	}	
	
	
	public void prepareEditSave() throws Exception{
		prepareModel();
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
	public Page<OqcException> getPage() {
		return page;
	}
	public void setPage(Page<OqcException> page) {
		this.page = page;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}		
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	
}
