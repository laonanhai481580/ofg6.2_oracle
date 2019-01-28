package com.ambition.gsm.inspectionplan.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.ambition.gsm.entity.GsmForeignReport;
import com.ambition.gsm.inspectionplan.service.GsmForeignReportManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/gsm/inspectionplan/external")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location ="/gsm/inspectionplan/external", type = "redirectAction") })
public class GsmForeignReportAction extends CrudActionSupport<GsmForeignReport>{

		/**
		  *GsmForeignReportAction.java2017年5月13日
		 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private GsmForeignReportManager gsmForeignReportManager;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log = Logger.getLogger(this.getClass());
	private GsmForeignReport gsmForeignReport;
	private Long id;
	private String ids;
	private String deleteIds;
	private Page<GsmForeignReport> page;
	private String processSection;
	
	@Override
	public GsmForeignReport getModel() {
		// TODO Auto-generated method stub
		return gsmForeignReport;
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存外校报告数据")
	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		try {
//			String fj = Struts2Utils.getParameter("attachmentFiles");
//			gsmForeignReport.setForeignFile(fj);
			gsmForeignReportManager.saveGsmForeignReport(gsmForeignReport);
			renderText(JsonParser.getRowValue(gsmForeignReport));
			logUtilDao.debugLog("保存",gsmForeignReport.toString());
		} catch (Exception e) {
			// TODO: handle exception
			createErrorMessage("保存失败："+e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除")
	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		try {
			gsmForeignReportManager.deleteMaintain(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据 :外校报告");
		} catch (Exception e) {
			// TODO: handle exception
			renderText("删除失败:" + e.getMessage());
			log.error("删除数据信息失败",e);
		}
		return null;
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		processSection = Struts2Utils.getParameter("processSection");
		List<Option> processSections = ApiFactory.getSettingService().getOptionsByGroupCode("_businessDivision");
		if(StringUtils.isEmpty(processSection)){
			if(processSections.size()>0){
				processSection = processSections.get(0).getValue();
			}
		}
		ActionContext.getContext().put("processSections",processSections);
		return SUCCESS;
	}
	@Action("list-datas")
	@LogInfo(optType="查询",message="查询外校报告数据")
	public String listDates(){
		try {
			page = gsmForeignReportManager.search(page);
			renderText(PageUtils.pageToJson(page));
//			System.out.println(page);
//			System.out.println(renderText(PageUtils.pageToJson(page)));
		} catch (Exception e) {
			// TODO: handle exception
			log.error("台账获取例表失败", e);
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		if(id == null){
			gsmForeignReport = new GsmForeignReport();
			gsmForeignReport.setCompanyId(ContextUtils.getCompanyId());
			gsmForeignReport.setCreatedTime(new Date());
			gsmForeignReport.setCreator(ContextUtils.getUserName());
			gsmForeignReport.setModifiedTime(new Date());
			gsmForeignReport.setModifier(ContextUtils.getUserName());
			gsmForeignReport.setBusinessUnitName(ContextUtils.getSubCompanyName());
			gsmForeignReport.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			gsmForeignReport = gsmForeignReportManager.getGsmForeignReport(id);
		}
	}
	@Action("export")
	@LogInfo(optType="导出",message="外校报告")
	public String export() throws Exception {
		Page<GsmForeignReport> page = new Page<GsmForeignReport>(100000);
		page = gsmForeignReportManager.list(page);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"GSM_FOREIGN_REPORT"),"外校报告"));
		logUtilDao.debugLog("导出", "外校报告");
		return null;
	}
	@Action("sendreport")
	@LogInfo(optType="校验",message="完成校验报告")
	public String sendReport() throws Exception {
		try {
			String sid = Struts2Utils.getParameter("id");
			GsmForeignReport foreign =gsmForeignReportManager.getGsmForeignReport(Long.valueOf(sid));
			if("N".equals(foreign.getIsAchieve())){
				gsmForeignReportManager.sendGsmForeignReport(foreign);
				logUtilDao.debugLog("校验成功",foreign.toString());
				renderText("校验成功");
			}else {
				throw new RuntimeException("该报告已校验!");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error("保存失败",e);
			createErrorMessage("校验失败："+e.getMessage());
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
	 * 列表数据
	 */
	@Action("list-state")
	@LogInfo(optType="查询",message="查询数据")
	public String getListStates() throws Exception {
		String type = Struts2Utils.getParameter("type");
		String companyName =PropUtils.getProp("companyName");
//		String processSection = Struts2Utils.getParameter("processSection");
		User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
		String subName=user.getSubCompanyName();
		Integer weight =user.getWeight();
		try{
			if("TP".equals(companyName)){
				if(weight==3){
					page = gsmForeignReportManager.listState(page, type,null);
				}else{
					page = gsmForeignReportManager.listState(page, type,subName);
				}
			}else{
				page = gsmForeignReportManager.listState(page, type,null);
			}
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			log.error("查询失败!",e);
		}
		return null;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public GsmForeignReport getGsmForeignReport() {
		return gsmForeignReport;
	}

	public void setGsmForeignReport(GsmForeignReport gsmForeignReport) {
		this.gsmForeignReport = gsmForeignReport;
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

	public Page<GsmForeignReport> getPage() {
		return page;
	}

	public void setPage(Page<GsmForeignReport> page) {
		this.page = page;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getProcessSection() {
		return processSection;
	}

	public void setProcessSection(String processSection) {
		this.processSection = processSection;
	}
	
	
}
