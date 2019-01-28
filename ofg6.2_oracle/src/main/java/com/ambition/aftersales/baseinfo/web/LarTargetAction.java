package com.ambition.aftersales.baseinfo.web;

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

import com.ambition.aftersales.baseinfo.service.LarTargetManager;
import com.ambition.aftersales.entity.LarTarget;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
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
 * 类名:Lar目标值维护action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月21日 发布
 */
@Namespace("/aftersales/base-info/lar-target")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/aftersales/base-info/lar-target", type = "redirectAction") })
public class LarTargetAction extends BaseAction<LarTarget>{

	private static final long serialVersionUID = 1L;
	private LarTarget larTarget;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private Page<LarTarget> page;
	@Autowired
	private LarTargetManager larTargetManager;
	@Override
	public LarTarget getModel() {
		return larTarget;
	}
	@Action("delete")
	@LogInfo(optType="删除",message="删除数据")
	public String delete() throws Exception {
		try {
			larTargetManager.deleteLarTarget(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除Lar目标值维护，编号："+deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除Lar目标值维护信息失败",e);
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("list")
	@LogInfo(optType="新建",message="新建Lar目标值维护信息")
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			larTarget=new LarTarget();
			larTarget.setCompanyId(ContextUtils.getCompanyId());
			larTarget.setCreatedTime(new Date());
			larTarget.setCreator(ContextUtils.getUserName());
			larTarget.setModifiedTime(new Date());
			larTarget.setModifier(ContextUtils.getLoginName());
			larTarget.setModifierName(ContextUtils.getUserName());
			larTarget.setBusinessUnitName(ContextUtils.getSubCompanyName());
			larTarget.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			larTarget=larTargetManager.getLarTarget(id);
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存Lar目标值维护信息")
	public String save() throws Exception {
		if(id != null && id != 0){
			larTarget.setModifiedTime(new Date());
			larTarget.setModifier(ContextUtils.getLoginName());
			larTarget.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", larTarget.toString());
		}else{
			logUtilDao.debugLog("保存", larTarget.toString());
		}
		try {
			larTargetManager.saveLarTarget(larTarget);
			this.renderText(JsonParser.object2Json(larTarget));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存Lar目标值维护信息失败  ",e);
		}		
		return null;
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = larTargetManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "Lar目标值维护信息");
		} catch (Exception e) {
			log.error("查询Lar目标值维护信息失败  ",e);
		}		
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出", message="Lar目标值维护")
	public String export() throws Exception {
		try {
			Page<LarTarget> page = new Page<LarTarget>(65535);
			page = larTargetManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"Lar目标值维护台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出Lar目标值维护信息失败",e);
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
	public Page<LarTarget> getPage() {
		return page;
	}
	public void setPage(Page<LarTarget> page) {
		this.page = page;
	}
	
}
