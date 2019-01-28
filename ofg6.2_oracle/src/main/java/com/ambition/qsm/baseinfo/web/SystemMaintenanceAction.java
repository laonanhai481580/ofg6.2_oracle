package com.ambition.qsm.baseinfo.web;

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

import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.qsm.baseinfo.service.SystemMaintenanceManager;
import com.ambition.qsm.entity.SystemMaintenance;
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
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
/**
 * 
 * 类名:体系维护action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月26日 发布
 */
@Namespace("/qsm/system-maintenance")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/qsm/system-maintenance", type = "redirectAction") })
public class SystemMaintenanceAction extends BaseAction<SystemMaintenance>{

	private static final long serialVersionUID = 1L;
	private SystemMaintenance systemMaintenance;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private String menutype;
	private Page<SystemMaintenance> page;
	@Autowired
	private SystemMaintenanceManager systemMaintenanceManager;
	@Override
	public SystemMaintenance getModel() {
		return systemMaintenance;
	}
	@Action("delete")
	@LogInfo(optType="删除",message="体系维护")
	public String delete() throws Exception {
		try {
			systemMaintenanceManager.deleteSystemMaintenance(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除体系维护，编号："+deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除体系维护失败",e);
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("list")
	@LogInfo(optType="新建",message="新建体系维护")
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			systemMaintenance=new SystemMaintenance();
			systemMaintenance.setCompanyId(ContextUtils.getCompanyId());
			systemMaintenance.setCreatedTime(new Date());
			systemMaintenance.setCreator(ContextUtils.getUserName());
			systemMaintenance.setModifiedTime(new Date());
			systemMaintenance.setModifier(ContextUtils.getLoginName());
			systemMaintenance.setModifierName(ContextUtils.getUserName());
			systemMaintenance.setBusinessUnitName(ContextUtils.getSubCompanyName());
			systemMaintenance.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
			User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
			String subName=user.getSubCompanyName();
			systemMaintenance.setFactoryClassify(subName);
		}else{
			systemMaintenance=systemMaintenanceManager.getSystemMaintenance(id);
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存体系维护")
	public String save() throws Exception {
		if(id != null && id != 0){
			systemMaintenance.setModifiedTime(new Date());
			systemMaintenance.setModifier(ContextUtils.getLoginName());
			systemMaintenance.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", systemMaintenance.toString());
		}else{
			logUtilDao.debugLog("保存", systemMaintenance.toString());
		}
		try {
			String zb1 = Struts2Utils.getParameter("attachmentFiles");
			systemMaintenance.setAttachment(zb1);
			String menutype1 = Struts2Utils.getParameter("menutype");
			if(menutype1!=null){
				systemMaintenance.setMenutype(menutype1);
			}
			systemMaintenanceManager.saveSystemMaintenance(systemMaintenance);
			this.renderText(JsonParser.object2Json(systemMaintenance));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存体系维护失败  ",e);
		}		
		return null;
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = systemMaintenanceManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "体系维护");
		} catch (Exception e) {
			log.error("查询体系维护失败  ",e);
		}		
		return null;
	}
	@Action("list-menutype")
	public String listElses() throws Exception {
		String menutype1 = Struts2Utils.getParameter("menutype");
		String type = Struts2Utils.getParameter("type");
		String companyName =PropUtils.getProp("companyName");
		try {
			User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
			String subName=user.getSubCompanyName();
			Integer weight =user.getWeight();
			if(type==null){
				type="N";
			}
			if("TP".equals(companyName)){
				if(weight==3){
					page = systemMaintenanceManager.listState(page, type,menutype1,null);
				}else{
					page = systemMaintenanceManager.listState(page, type,menutype1,subName);
				}
			}else{
				page = systemMaintenanceManager.listState(page, type,menutype1,null);
			}
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "客户审核履历");
		} catch (Exception e) {
			log.error("查询客户审核履历失败  ",e);
		}		
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出", message="体系维护")
	public String export() throws Exception {
		try {
			String menutype1 = Struts2Utils.getParameter("menutype");
			User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
			String subName=user.getSubCompanyName();
			Page<SystemMaintenance> page = new Page<SystemMaintenance>(65535);
			page = systemMaintenanceManager.listState(page, "N",menutype1,subName);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"体系维护台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出体系维护失败",e);
		}
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
	public Page<SystemMaintenance> getPage() {
		return page;
	}
	public void setPage(Page<SystemMaintenance> page) {
		this.page = page;
	}
	public String getMenutype() {
		return menutype;
	}
	public void setMenutype(String menutype) {
		this.menutype = menutype;
	}
	
}
