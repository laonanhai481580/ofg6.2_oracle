package com.ambition.carmfg.factoryprocedure.web;

import java.util.Date;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.entity.OqcFactory;
import com.ambition.carmfg.factoryprocedure.service.OqcFactoryManager;
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

@Namespace("/carmfg/base-info/factory-procedure")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "carmfg/base-info/factory-procedure", type = "redirectAction") })
public class OqcFactoryAction extends BaseAction<OqcFactory>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private OqcFactory oqcFactory;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private OqcFactoryManager oqcFactoryManager;
	private Page<OqcFactory> page;
	
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
	public OqcFactory getOqcFactory() {
		return oqcFactory;
	}
	public void setOqcFactory(OqcFactory oqcFactory) {
		this.oqcFactory = oqcFactory;
	}
	public Page<OqcFactory> getPage() {
		return page;
	}
	public void setPage(Page<OqcFactory> page) {
		this.page = page;
	}
	@Override
	public OqcFactory getModel() {
		return oqcFactory;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			oqcFactory = new OqcFactory();
			oqcFactory.setCompanyId(ContextUtils.getCompanyId());
			oqcFactory.setCreatedTime(new Date());
			oqcFactory.setCreator(ContextUtils.getUserName());
			oqcFactory.setLastModifiedTime(new Date());
			oqcFactory.setLastModifier(ContextUtils.getUserName());
			oqcFactory.setBusinessUnitName(ContextUtils.getSubCompanyName());
			oqcFactory.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			oqcFactory = oqcFactoryManager.getOqcFactory(id);
		}
	}
	@Action("factory-delete")
	@LogInfo(optType="删除")
	@Override
	public String delete() throws Exception {
		try{
			String str=oqcFactoryManager.deleteOqcFactory(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"删除OQC工厂维护,工厂为:"+str);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	@Action("factory-input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("factory-list")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	@Action("factory-save")
	@LogInfo(optType="保存",message="工厂维护")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			oqcFactory.setLastModifiedTime(new Date());
			oqcFactory.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", oqcFactory.toString());
		}else{
			logUtilDao.debugLog("保存", oqcFactory.toString());
		}
		try{
			oqcFactoryManager.saveOqcFactory(oqcFactory);
			this.renderText(JsonParser.object2Json(oqcFactory));
		}catch(Exception e){
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}	
	@Action("factory-list-datas")
	public String getListDatas() throws Exception {
		page = oqcFactoryManager.list(page);
		this.renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "制造质量管理：基础设置-不良类别维护");
		return null;
	}
	@Action("exportCode")
	@LogInfo(optType="导出",message="OQC工厂维护")
	public String export() throws Exception {
		Page<OqcFactory> page = new Page<OqcFactory>(100000);
		page = oqcFactoryManager.list(page);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_OQC_FACTORY"),"oqcFactory"));
		logUtilDao.debugLog("导出", "制造质量管理：基础设置-OQC工厂维护");
		return null;
	}
}
