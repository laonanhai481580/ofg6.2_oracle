package com.ambition.supplier.supplieryearinspect.web;

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

import com.ambition.supplier.entity.SupplierYearInspectPlan;
import com.ambition.supplier.supplieryearinspect.services.SupplierYearInspectPlanManager;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * 类名:供应商稽核计划2.0
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  xjn
 * @version 1.00 2018年3月7日 发布
 */
@Namespace("/supplier/audit/year-inspect-plan")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location ="/supplier/audit/year-inspect-plan", type = "redirectAction") })
public class SupplierYearInspectPlanAction extends CrudActionSupport<SupplierYearInspectPlan>{

		/**
		  *SupplierYearInspectPlanAction.java2017年5月13日
		 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private SupplierYearInspectPlanManager supplierYearInspectPlanManager;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log = Logger.getLogger(this.getClass());
	private SupplierYearInspectPlan supplierYearInspectPlan;
	private Long id;
	private String ids;
	private String deleteIds;
	private Page<SupplierYearInspectPlan> page;
	private String processSection;
	
	@Override
	public SupplierYearInspectPlan getModel() {
		return supplierYearInspectPlan;
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="供应商稽核计划")
	@Override
	public String save() throws Exception {
		try {
//			String fj = Struts2Utils.getParameter("attachmentFiles");
//			supplierYearInspectPlan.setForeignFile(fj);
			
			int month=supplierYearInspectPlan.getCreationMonth();
			
			supplierYearInspectPlan.setPlan1("");
			supplierYearInspectPlan.setPlan2("");
			supplierYearInspectPlan.setPlan3("");
			supplierYearInspectPlan.setPlan4("");
			supplierYearInspectPlan.setPlan5("");
			supplierYearInspectPlan.setPlan6("");
			supplierYearInspectPlan.setPlan7("");
			supplierYearInspectPlan.setPlan8("");
			supplierYearInspectPlan.setPlan9("");
			supplierYearInspectPlan.setPlan10("");
			supplierYearInspectPlan.setPlan11("");
			supplierYearInspectPlan.setPlan12("");
			switch (month) {
			case 1:
				supplierYearInspectPlan.setPlan1("P");
				break;
			case 2:
				supplierYearInspectPlan.setPlan2("P");
				break;
			case 3:
				supplierYearInspectPlan.setPlan3("P");
				break;
			case 4:
				supplierYearInspectPlan.setPlan4("P");
				break;
			case 5:
				supplierYearInspectPlan.setPlan5("P");
				break;
			case 6:
				supplierYearInspectPlan.setPlan6("P");
				break;
			case 7:
				supplierYearInspectPlan.setPlan7("P");
				break;
			case 8:
				supplierYearInspectPlan.setPlan8("P");
				break;
			case 9:
				supplierYearInspectPlan.setPlan9("P");
				break;
			case 10:
				supplierYearInspectPlan.setPlan10("P");
				break;
			case 11:
				supplierYearInspectPlan.setPlan11("P");
				break;
			case 12:
				supplierYearInspectPlan.setPlan12("P");
				break;
			default:
				break;
			}
			supplierYearInspectPlan.setModifiedTime(new Date());
			supplierYearInspectPlanManager.saveSupplierYearInspectPlan(supplierYearInspectPlan);
			renderText(JsonParser.getRowValue(supplierYearInspectPlan));
			logUtilDao.debugLog("保存",supplierYearInspectPlan.toString());
		} catch (Exception e) {
			createErrorMessage("保存失败："+e.getMessage());
		}
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="删除供应商稽核计划")
	@Override
	public String delete() throws Exception {
		try {
			supplierYearInspectPlanManager.deleteMaintain(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据，编号："+deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除数据信息失败",e);
		}
		return null;
	}
	
	@Action("list")
	@Override
	public String list() throws Exception {
		ActionContext.getContext().put("systemCodes", ApiFactory.getSettingService().getOptionsByGroupCode("supplier-system-code"));
		ActionContext.getContext().put("auditYears",ApiFactory.getSettingService().getOptionsByGroupCode("supplier_audit_year"));
		ActionContext.getContext().put("supplierAuditorType",ApiFactory.getSettingService().getOptionsByGroupCode("supplier-auditor-type"));
		return SUCCESS;
	}
	@Action("list-datas")
	public String listDates(){
		try {
//			page = supplierYearInspectPlanManager.search(page);
			page = supplierYearInspectPlanManager.getSupplierYearCheckViewPage(page);
			renderText(PageUtils.pageToJson(page));
//			System.out.println(page);
//			System.out.println(renderText(PageUtils.pageToJson(page)));
		} catch (Exception e) {
			log.error("台账获取例表失败", e);
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id == null){
			supplierYearInspectPlan = new SupplierYearInspectPlan();
			supplierYearInspectPlan.setCompanyId(ContextUtils.getCompanyId());
			supplierYearInspectPlan.setCreatedTime(new Date());
			supplierYearInspectPlan.setCreator(ContextUtils.getUserName());
			supplierYearInspectPlan.setModifiedTime(new Date());
			supplierYearInspectPlan.setModifier(ContextUtils.getUserName());
			supplierYearInspectPlan.setBusinessUnitName(ContextUtils.getSubCompanyName());
			supplierYearInspectPlan.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			supplierYearInspectPlan = supplierYearInspectPlanManager.getSupplierYearInspectPlan(id);
		}
	}
	@Action("export")
	@LogInfo(optType="导出",message="稽核计划")
	public String export() throws Exception {
		Page<SupplierYearInspectPlan> page = new Page<SupplierYearInspectPlan>(100000);
		page = supplierYearInspectPlanManager.list(page);
		renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"SUPPLIER_YEAR_INSPECT_PLAN"),"稽核计划"));
		logUtilDao.debugLog("导出", "稽核计划");
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
		User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
		String subName=user.getSubCompanyName();
		Integer weight =user.getWeight();
		try{
			if(weight==3){
				page = supplierYearInspectPlanManager.listState(page, type,null);
			}else{
				page = supplierYearInspectPlanManager.listState(page, type,subName);
			}
			renderText(PageUtils.pageToJson(page));
		}catch(Exception e){
			log.error("查询失败!",e);
		}
		return null;
	}
	@Action("sendAuditSuppliers")
	public String sendAuditSuppliers(){
		String userId = Struts2Utils.getParameter("userId");
		String[] ids = userId.split(",");
		StringBuffer sb = new StringBuffer("");
		int i=0;
		for(String id: ids){
			try {
				String ex1=supplierYearInspectPlanManager.sendAuditSuppliers(id);
				sb.append("第" + (i+1) + "行 "+ex1+"! \\\n");
			} catch (Exception e) {
				e.printStackTrace();
				createErrorMessage("生成失败：" + e.getMessage());
				sb.append("第" + (i+1) + "行 生成失败:" + e.getMessage() + " ");
			}
			i++;
		}
		return renderHtml(sb.toString());
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public SupplierYearInspectPlan getSupplierYearInspectPlan() {
		return supplierYearInspectPlan;
	}

	public void setSupplierYearInspectPlan(SupplierYearInspectPlan supplierYearInspectPlan) {
		this.supplierYearInspectPlan = supplierYearInspectPlan;
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

	public Page<SupplierYearInspectPlan> getPage() {
		return page;
	}

	public void setPage(Page<SupplierYearInspectPlan> page) {
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
