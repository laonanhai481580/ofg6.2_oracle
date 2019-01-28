package com.ambition.carmfg.ipqc.web;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.ambition.carmfg.entity.IpqcAudit;
import com.ambition.carmfg.entity.OqcProcedure;
import com.ambition.carmfg.factoryprocedure.service.OqcFactoryManager;
import com.ambition.carmfg.factoryprocedure.service.OqcProcedureManager;
import com.ambition.carmfg.ipqc.service.IpqcAuditManager;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;
/**
 * 
 * 类名:IPQC稽核action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月8日 发布
 */
@Namespace("/carmfg/ipqc/ipqc-audit")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/carmfg/ipqc/ipqc-audit", type = "redirectAction") })
public class IpqcAuditAction extends BaseAction<IpqcAudit>{

	private static final long serialVersionUID = 1L;
	private IpqcAudit ipqcAudit;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private JSONObject params;
	private String	searchParams;
	private Page<IpqcAudit> page;
	private File myFile;
	private String businessUnit;//所属事业部
	private String factory;//工厂
	@Autowired
	private IpqcAuditManager ipqcAuditManager;
	@Autowired
	private OqcFactoryManager oqcFactoryManager;
	@Autowired
	private OqcProcedureManager oqcProcedureManager;
	@Override
	public IpqcAudit getModel() {
		return ipqcAudit;
	}
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		try {
			String deleteNos=ipqcAuditManager.deleteIpqcAudit(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除IPQC稽核信息，稽核日期："+deleteNos);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除IPQC稽核信息失败",e);
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
			ipqcAudit=new IpqcAudit();
			ipqcAudit.setCompanyId(ContextUtils.getCompanyId());
			ipqcAudit.setCreatedTime(new Date());
			ipqcAudit.setCreator(ContextUtils.getUserName());
			ipqcAudit.setModifiedTime(new Date());
			ipqcAudit.setModifier(ContextUtils.getLoginName());
			ipqcAudit.setModifierName(ContextUtils.getUserName());
			ipqcAudit.setBusinessUnitName(CommonUtil.getDataByLogin());
			ipqcAudit.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			ipqcAudit=ipqcAuditManager.getIpqcAudit(id);
		}
		
	}
	
	@Action("miss-save")
	public String missSave() throws Exception {
		prepareModel();
		if(id != null && id != 0){
			ipqcAudit.setModifiedTime(new Date());
			ipqcAudit.setModifier(ContextUtils.getLoginName());
			ipqcAudit.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", ipqcAudit.toString());
		}else{
			logUtilDao.debugLog("保存", ipqcAudit.toString());
		}
		try {
			ipqcAuditManager.saveIpqcAuditMiss(ipqcAudit);
			this.renderText(JsonParser.object2Json(ipqcAudit));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存IPQC稽核信息失败  ",e);
		}		
		return null;
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存IPQC稽核信息")
	public String save() throws Exception {
		if(id != null && id != 0){
			ipqcAudit.setModifiedTime(new Date());
			ipqcAudit.setModifier(ContextUtils.getLoginName());
			ipqcAudit.setModifierName(ContextUtils.getUserName());			
		}
		try {
			ipqcAuditManager.saveIpqcAudit(ipqcAudit);
			this.renderText(JsonParser.object2Json(ipqcAudit));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存IPQC稽核信息失败  ",e);
		}		
		return null;
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = ipqcAuditManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "IPQC稽核信息");
		} catch (Exception e) {
			log.error("查询IPQC稽核信息失败  ",e);
		}		
		return null;
	}
	
	@Action("miss-list")
	public String missList() throws Exception {		
		return SUCCESS;
	}	
	
	@Action("miss-list-datas")
	public String missListDatas() throws Exception {
		try {
			page = ipqcAuditManager.searchMiss(page);
			renderText(PageUtils.pageToJson(page));
		} catch (Exception e) {
			log.error("查询IPQC稽核信息失败  ",e);
		}		
		return null;
	}	
	@Action("export-miss")
	public String exportMiss() throws Exception {
		try {
			Page<IpqcAudit> page = new Page<IpqcAudit>(65535);
			page = ipqcAuditManager.searchMiss(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"IPQC稽核信息台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出IPQC稽核信息失败",e);
		}
		return null;
	}	
	@Action("export")
	@LogInfo(optType="导出", message="IPQC稽核信息")
	public String export() throws Exception {
		try {
			Page<IpqcAudit> page = new Page<IpqcAudit>(65535);
			page = ipqcAuditManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"IPQC稽核信息台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出IPQC稽核信息失败",e);
		}
		return null;
	}
	/**
	 * 方法名: mailSettings 
	 * <p>功能说明：IPQC稽核邮件通知</p>
	 * @return String
	 * @throws
	 */
	@Action("mail-settings")
	@LogInfo(optType="IPQC稽核邮件通知")
	public String mailSettings(){
		try {
			ipqcAuditManager.saveGsmMailSettings();
			createErrorMessage("设置成功!");
		} catch (Exception e) {
			createErrorMessage("邮件提醒出错:" + e.getMessage());
			log.error("邮件提醒出错!",e);
		}
		return null;
	}
	
	public void prepareEditSave() throws Exception{
		prepareModel();
	}
	@Action("import")
	public String imports() throws Exception {
		return SUCCESS;
	}
	
	@Action("import-datas")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(ipqcAuditManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	/**
	  * 方法名: 下载检IPQC稽核模板
	  * <p>功能说明：下载IPQC稽核模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download-ipqc-audit")
	@LogInfo(optType="下载",message="下载IPQC稽核模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/mfg-ipqc-audit.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "IPQC稽核导入模板.xls";
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
	/**
	 * 列表页面
	 */
	@Action("hide-list")
	public String hideList() throws Exception {
		return SUCCESS;
	}		
	/**
	 * 列表数据
	 */
	@Action("hide-list-datas")
	@LogInfo(optType="查询",message="查询IPQC稽核隐藏数据")
	public String getHideListDatas() throws Exception {
		try{
			page = ipqcAuditManager.searchHide(page);
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			log.error("查询失败!",e);
		}
		return null;
	}	
	@Action("hiddenState")
	@LogInfo(optType="隐藏")
	public String hiddenState(){
		JSONObject result = new JSONObject();
		String eid = Struts2Utils.getParameter("id");
		String type = Struts2Utils.getParameter("type");
		String formNo="";
		try {
			formNo=ipqcAuditManager.hiddenState(eid,type);
			result.put("message", "操作成功!");
			result.put("error", false);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("error", true);
			result.put("message", e.getMessage());
		}	
		formNo=formNo.substring(0, formNo.length()-1);
		String str="";
		if("Y".equals(type)){
			str="IPQC稽核取消敏感数据标记";
		}else{
			str="IPQC稽核标记数据为敏感数据";
		}
		Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,str+"，稽核时间为:"+formNo);
		renderText(result.toString());
		return null;
	}	


	/**
	 * IPQC稽核遵守度统计图页面
	 */
	@Action("observe-rate")
	public String sizeSpreadchart() throws Exception {
		List<Option> businessUnits = ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits");
		ActionContext.getContext().put("businessUnits",businessUnits);
		List<Option> factorys = oqcFactoryManager.listAllForOptions();
        ActionContext.getContext().put("factorys",factorys);
        List<OqcProcedure> procedures = oqcProcedureManager.getAllOqcProcedure();
        List<Option> proceduresOptions = oqcProcedureManager.converOqcProcedureToList(procedures);
        ActionContext.getContext().put("procedures",proceduresOptions);
        ActionContext.getContext().put("stations", ApiFactory.getSettingService().getOptionsByGroupCode("mfg_station"));
        ActionContext.getContext().put("classGroups", ApiFactory.getSettingService().getOptionsByGroupCode("mfg_class_group"));
        ActionContext.getContext().put("auditTypes", ApiFactory.getSettingService().getOptionsByGroupCode("mfg_audit_type"));
		return SUCCESS;
	}

	/**
	 * IPQC稽核遵守度统计图数据
	 */
	@Action("observe-rate-datas")
	@LogInfo(optType="数据",message="IPQC稽核遵守度统计图数据")
	public String sizeSpreadchartDatas() throws Exception {
		try {
			Map<String,Object> map=ipqcAuditManager.getObserveRateDatas(params);
			this.renderText(JSONObject.fromObject(map).toString());
		} catch (Exception e) {
			log.error("查询出错!",e);
		}
		return null;	
	}	

	/**
	 * 尺寸分布趋势图页面
	 */
	@Action("observe-rate-show-detail")
	@LogInfo(optType="页面",message="查看详情")
	public String showDetail() throws Exception {
		searchParams=Struts2Utils.getParameter("searchParams");
		return SUCCESS;
	}	
	
	/**
	 * IPQC稽核遵守度统计图数据详情
	 */
	@Action("observe-rate-detail-datas")
	@LogInfo(optType="页面",message="查看详情")
	public String showDetailDatas() throws Exception {
		try {		
			searchParams=Struts2Utils.getParameter("searchParams");
			page=ipqcAuditManager.listDetail(page,searchParams);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		this.renderText(PageUtils.pageToJson(page));
		return null;
	}	
	
	/**
	 * 导出台账
	 * @return
	 * @throws Exception
	 */
	@Action("export-hide")
	@LogInfo(optType="导出",message="导出敏感数据")
	public String exportHide() throws Exception {
		Page<IpqcAudit> page = new Page<IpqcAudit>(65535);
		page = ipqcAuditManager.searchHide(page);
		this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"IPQC稽核敏感数据台账"));
		return null;
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
	public Page<IpqcAudit> getPage() {
		return page;
	}
	public void setPage(Page<IpqcAudit> page) {
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
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public JSONObject getParams() {
		return params;
	}
	public void setParams(JSONObject params) {
		this.params = params;
	}
	public String getSearchParams() {
		return searchParams;
	}
	public void setSearchParams(String searchParams) {
		this.searchParams = searchParams;
	}
	
}
