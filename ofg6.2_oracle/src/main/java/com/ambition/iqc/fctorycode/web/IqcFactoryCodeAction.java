package com.ambition.iqc.fctorycode.web;

import java.io.File;
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

import com.ambition.iqc.entity.IqcFactoryCode;
import com.ambition.iqc.fctorycode.service.IqcFactoryCodeManager;
import com.ambition.product.BaseAction;
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


@Namespace("/iqc/inspection-base/factory-code")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "iqc/inspection-base/factory-code", type = "redirectAction") })
public class IqcFactoryCodeAction extends BaseAction<IqcFactoryCode> {

	private static final long serialVersionUID = 1L;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;//删除的BOM编号
	private Page<IqcFactoryCode> page;			
	private File myFile;
	
	private IqcFactoryCode iqcFactoryCode;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
 	@Autowired
	private IqcFactoryCodeManager iqcFactoryCodeManager;
 	
 	
	public Page<IqcFactoryCode> getPage() {
		return page;
	}

	public void setPage(Page<IqcFactoryCode> page) {
		this.page = page;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
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
	public IqcFactoryCode getIqcFactoryCode() {
		return iqcFactoryCode;
	}
	public void setIqcFactoryCode(IqcFactoryCode iqcFactoryCode) {
		this.iqcFactoryCode = iqcFactoryCode;
	}
	public IqcFactoryCode getModel() {
		return iqcFactoryCode;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			iqcFactoryCode = new IqcFactoryCode();
			iqcFactoryCode.setCreatedTime(new Date());
			iqcFactoryCode.setCompanyId(ContextUtils.getCompanyId());
			iqcFactoryCode.setCreator(ContextUtils.getUserName());
			iqcFactoryCode.setLastModifiedTime(new Date());
			iqcFactoryCode.setLastModifier(ContextUtils.getUserName());
			iqcFactoryCode.setBusinessUnitName(ContextUtils.getSubCompanyName());
			iqcFactoryCode.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			iqcFactoryCode = iqcFactoryCodeManager.getIqcFactoryCode(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return null;
	}
	@Action("save")
	@LogInfo(optType="保存",message="厂区-组织代码维护")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			iqcFactoryCode.setModifiedTime(new Date());
			iqcFactoryCode.setModifier(ContextUtils.getLoginName());
			iqcFactoryCode.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", iqcFactoryCode.toString());
		}else{
			logUtilDao.debugLog("保存", iqcFactoryCode.toString());
		}
		try {
			iqcFactoryCodeManager.saveIqcFactoryCode(iqcFactoryCode);
			this.renderText(JsonParser.object2Json(iqcFactoryCode));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存厂区-组织代码维护信息失败  ",e);
		}		
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="厂区-组织代码维护")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				iqcFactoryCodeManager.deleteIqcFactoryCode(deleteIds);				
			} catch (Exception e) {
				createErrorMessage("删除失败:" + e.getMessage());
			}
		}
		return null;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		renderMenu();
		return SUCCESS;
	}		
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = iqcFactoryCodeManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "厂区-组织代码维护信息");
		} catch (Exception e) {
			log.error("查询厂区-组织代码维护信息失败  ",e);
		}		
		return null;
	}

	@Action("export")
	@LogInfo(optType="导出",message="厂区-组织代码维护")
	public String exports() throws Exception {
		try {
			Page<IqcFactoryCode> page = new Page<IqcFactoryCode>(65535);
			page = iqcFactoryCodeManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"厂区-组织代码维护台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出厂区-组织代码维护信息失败",e);
		}
		return null;
	}	
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	private void createMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
}
