package com.ambition.carmfg.updatetime.web;

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

import com.ambition.carmfg.entity.UpdateTimestamp;
import com.ambition.carmfg.updatetime.service.UpdateTimestampManager;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
/**
 * 类名:erp更新时间戳
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2013-5-27 发布
 */
@Namespace("/integration")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/integration", type = "redirectAction") })
public class UpdateTimestampAction extends BaseAction<UpdateTimestamp>{

	private static final long serialVersionUID = 1L;
	private UpdateTimestamp updateTimestamp;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private Page<UpdateTimestamp> page;
	@Autowired
	private UpdateTimestampManager updateTimestampManager;
	@Override
	public UpdateTimestamp getModel() {
		return updateTimestamp;
	}
	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("update-time-list")
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			updateTimestamp=new UpdateTimestamp();
			updateTimestamp.setCompanyId(ContextUtils.getCompanyId());
			updateTimestamp.setCreatedTime(new Date());
			updateTimestamp.setCreator(ContextUtils.getUserName());
			updateTimestamp.setModifiedTime(new Date());
			updateTimestamp.setModifier(ContextUtils.getLoginName());
			updateTimestamp.setModifierName(ContextUtils.getUserName());
			updateTimestamp.setBusinessUnitName(ContextUtils.getSubCompanyName());
			updateTimestamp.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			updateTimestamp=updateTimestampManager.getUpdateTimestamp(id);
		}
		
	}
	
	@Action("update-time-save")
	@LogInfo(optType="保存",message="保存erp更新时间戳信息")
	public String save() throws Exception {
		if(id != null && id != 0){
			updateTimestamp.setModifiedTime(new Date());
			updateTimestamp.setModifier(ContextUtils.getLoginName());
			updateTimestamp.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", updateTimestamp.toString());
		}else{
			logUtilDao.debugLog("保存", updateTimestamp.toString());
		}
		try {
			updateTimestampManager.saveUpdateTimestamp(updateTimestamp);
			this.renderText(JsonParser.object2Json(updateTimestamp));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存erp更新时间戳信息失败  ",e);
		}		
		return null;
	}
	@Action("update-list-datas")
	public String listDatas() throws Exception {
		try {
			page = updateTimestampManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "erp更新时间戳信息");
		} catch (Exception e) {
			log.error("查询erp更新时间戳信息失败  ",e);
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
	public Page<UpdateTimestamp> getPage() {
		return page;
	}
	public void setPage(Page<UpdateTimestamp> page) {
		this.page = page;
	}
	
}
