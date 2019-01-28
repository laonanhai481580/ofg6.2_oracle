package com.ambition.carmfg.factoryprocedure.web;

import java.io.File;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.entity.OqcFactory;
import com.ambition.carmfg.entity.OqcProcedure;
import com.ambition.carmfg.factoryprocedure.service.OqcFactoryManager;
import com.ambition.carmfg.factoryprocedure.service.OqcProcedureManager;
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
public class OqcProcedureAction extends BaseAction<OqcProcedure>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private OqcProcedure oqcProcedure;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private OqcProcedureManager oqcProcedureManager;
	private Page<OqcProcedure> page;
	private List<OqcProcedure> list;
	private File myFile;
 	//不良类别Id
	private Long oqcFactoryId;
	@Autowired
	private OqcFactoryManager oqcFactoryManager;
	private OqcFactory oqcFactory;
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

	public OqcProcedure getOqcProcedure() {
		return oqcProcedure;
	}

	public void setOqcProcedure(OqcProcedure oqcProcedure) {
		this.oqcProcedure = oqcProcedure;
	}

	public Page<OqcProcedure> getPage() {
		return page;
	}

	public void setPage(Page<OqcProcedure> page) {
		this.page = page;
	}

	public List<OqcProcedure> getList() {
		return list;
	}

	public void setList(List<OqcProcedure> list) {
		this.list = list;
	}

	public Long getOqcFactoryId() {
		return oqcFactoryId;
	}

	public void setOqcFactoryId(Long oqcFactoryId) {
		this.oqcFactoryId = oqcFactoryId;
	}

	public OqcFactory getOqcFactory() {
		return oqcFactory;
	}

	public void setOqcFactory(OqcFactory oqcFactory) {
		this.oqcFactory = oqcFactory;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	@Override
	public OqcProcedure getModel() {
		return oqcProcedure;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			oqcProcedure = new OqcProcedure();
			if(oqcFactoryId != null && oqcFactoryId != 0){
				oqcFactory = oqcFactoryManager.getOqcFactory(oqcFactoryId);
			}else{
				oqcFactory = null;
			}
			oqcProcedure.setCompanyId(ContextUtils.getCompanyId());
			oqcProcedure.setCreatedTime(new Date());
			oqcProcedure.setCreator(ContextUtils.getUserName());
			oqcProcedure.setOqcFactory(oqcFactory);
			oqcProcedure.setLastModifiedTime(new Date());
			oqcProcedure.setLastModifier(ContextUtils.getUserName());
			oqcProcedure.setBusinessUnitName(ContextUtils.getSubCompanyName());
			oqcProcedure.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			oqcProcedure = oqcProcedureManager.getOqcProcedure(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}
	@Action("save")
	@LogInfo(optType="保存",message="OQC工序维护")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			oqcProcedure.setLastModifiedTime(new Date());
			oqcProcedure.setLastModifier(ContextUtils.getUserName());
			logUtilDao.debugLog("修改", oqcProcedure.toString());
		}else{
			logUtilDao.debugLog("保存", oqcProcedure.toString());
		}
		try {
			oqcProcedureManager.saveOqcProcedure(oqcProcedure);
			renderText(JsonParser.object2Json(oqcProcedure));
		} catch (Exception e) {
			e.printStackTrace();
			createErrorMessage(e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="OQC工序维护")
	@Override
	public String delete() throws Exception {
		String str=oqcProcedureManager.deleteOqcProcedure(deleteIds);
		Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"删除OQC工序,工序为:"+str);
		return null;
	}

	@Action("delete-subs")
	@LogInfo(optType="删除级联")
	public String deleteSubs() throws Exception {
		if(oqcFactoryId != null && oqcFactoryId != 0){
			oqcFactory = oqcFactoryManager.getOqcFactory(oqcFactoryId);
		}else{
			oqcFactory = null;
		}
		list = oqcProcedureManager.listAll(oqcFactory);
		for(OqcProcedure oqcProcedure: list){
			oqcProcedureManager.deleteOqcProcedure(oqcProcedure);
		}
		Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,"删除工厂："+oqcProcedure.getOqcFactory().getFactory()+"下所有工序");
		return null;
	}
	
	@Action("search-subs")
	public String searchSubs() throws Exception {
		if(oqcFactoryId != null && oqcFactoryId != 0){
			oqcFactory = oqcFactoryManager.getOqcFactory(oqcFactoryId);
		}else{
			oqcFactory = null;
		}
		list = oqcProcedureManager.listAll(oqcFactory);
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
		if(oqcFactoryId != null && oqcFactoryId != 0){
			oqcFactory = oqcFactoryManager.getOqcFactory(oqcFactoryId);
		}else{
			oqcFactory = null;
		}
		return SUCCESS;
	}
	
	@Action("list-datas")
	public String getListDatas() throws Exception{
		String myId = Struts2Utils.getParameter("oqcFactoryId");
		if(myId != null && !myId.equals("")){
			oqcFactoryId = Long.valueOf(myId);
			oqcFactory = oqcFactoryManager.getOqcFactory(oqcFactoryId);
		}
		String code = null;
		try {
			code = Struts2Utils.getParameter("oqcProcedure");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(code != null && !code.equals("")){
			page = oqcProcedureManager.list(page, code);
		}else{
			page = oqcProcedureManager.list(page, oqcFactory);
		}		
		renderText(PageUtils.pageToJson(page));
		logUtilDao.debugLog("查询", "制造质量管理：基础设置-工序维护");
		return null;
	}
	@Action("exportCode2")
	@LogInfo(optType="导出",message="工序维护")
	public String export() throws Exception {
		Page<OqcProcedure> page = new Page<OqcProcedure>(100000);
		String myId = Struts2Utils.getParameter("oqcFactoryId");
		if(myId != null && !myId.equals("")){
			oqcFactoryId = Long.valueOf(myId);
			oqcFactory = oqcFactoryManager.getOqcFactory(oqcFactoryId);
		}
		page = oqcProcedureManager.list(page, oqcFactory);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MFG_OQC_PROCEDURE"),"oqcProcedure"));
		logUtilDao.debugLog("导出", "制造质量管理：基础设置-工序维护");
		return null;
	}
    @Action("procedure-select")
    public String selectByProcess() throws Exception {
    	JSONObject result = new JSONObject();
        try{
            String factory = Struts2Utils.getParameter("factory");
            List<OqcProcedure> procedures = oqcProcedureManager.listProcedure(factory);
            String places = "";
            for (OqcProcedure procedure : procedures) {
	            	if(places.length()==0){
	            		places = procedure.getProcedure();
					}else{
						places += "," +  procedure.getProcedure();
					}
            }
            result.put("procedures",  places);
            result.put("error", false);
        }catch(Exception e){
            result.put("error", true);
            result.put("message", "查找工序失败");
        }
        this.renderText(JsonParser.object2Json(result));
        return null;
    }	
  
    @Action("factory-select")
    public String factorySelect() throws Exception {
    	JSONObject result = new JSONObject();
        try{
            List<OqcFactory> oqcFactorys = oqcFactoryManager.listAll();
            String factorys = "";
            for (OqcFactory factory : oqcFactorys) {
	            	if(factorys.length()==0){
	            		factorys = factory.getFactory();
					}else{
						factorys += "," +  factory.getFactory();
					}
            }
            result.put("factorys",  factorys);
            result.put("error", false);
        }catch(Exception e){
            result.put("error", true);
            result.put("message", "查找工厂失败");
        }
        this.renderText(JsonParser.object2Json(result));
        return null;
    }	
}
