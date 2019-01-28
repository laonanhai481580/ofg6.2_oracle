package com.ambition.aftersales.lar.web;

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

import com.ambition.aftersales.entity.LarData;
import com.ambition.aftersales.entity.LarDefectiveItem;
import com.ambition.aftersales.lar.service.LarDataManager;
import com.ambition.aftersales.lar.service.LarDefectiveItemManager;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PageUtils;
/**
 * 
 * 类名:LAR批次合格率action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Namespace("/aftersales/lar")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/aftersales/lar", type = "redirectAction") })
public class LarDefectiveItemAction extends BaseAction<LarDefectiveItem>{

	private static final long serialVersionUID = 1L;
	private LarData larData;
	private LarDefectiveItem larDefectiveItem;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private Long parentId;
	private String deleteIds;
	private Page<LarDefectiveItem> itemPage;
	@Autowired
	private LarDataManager larDataManager;
	private JSONObject params;//查询参数
	@Autowired
	private LarDefectiveItemManager larDefectiveItemManager;
	@Override
	public LarDefectiveItem getModel() {
		return larDefectiveItem;
	}
	@Action("delete-item")
	@LogInfo(optType="删除",message="删除LAR数据")
	public String delete() throws Exception {
		try {
			larDefectiveItemManager.deleteLarDefectiveItem(deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
		}
		return null;
	}

	@Action("input")
	@Override
	public String input() throws Exception {
		return SUCCESS;
	}	
	@Action("bad-item-datas")
	public String itemDatas() throws Exception {
		try {
			larData = larDataManager.getLarData(parentId);
			itemPage = larDefectiveItemManager.searchItem(itemPage,larData);
			renderText(PageUtils.pageToJson(itemPage));
			logUtilDao.debugLog("查询", "LAR数据信息");
		} catch (Exception e) {
			log.error("查询LAR数据信息失败  ",e);
		}		
		return null;
	}
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			if(parentId != null && parentId != 0){
				larData = larDataManager.getLarData(parentId);
			}else{
				larData = null;
			}
			larDefectiveItem=new LarDefectiveItem();
			larDefectiveItem.setCompanyId(ContextUtils.getCompanyId());
			larDefectiveItem.setCreatedTime(new Date());
			//larDefectiveItem.setLarData(larData);
			larDefectiveItem.setCreator(ContextUtils.getUserName());
			larDefectiveItem.setModifiedTime(new Date());
			larDefectiveItem.setModifier(ContextUtils.getLoginName());
			larDefectiveItem.setModifierName(ContextUtils.getUserName());
			larDefectiveItem.setBusinessUnitName(ContextUtils.getSubCompanyName());
			larDefectiveItem.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			larDefectiveItem=larDefectiveItemManager.getLarDefectiveItem(id);
		}		
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

	public Page<LarDefectiveItem> getItemPage() {
		return itemPage;
	}
	public void setItemPage(Page<LarDefectiveItem> itemPage) {
		this.itemPage = itemPage;
	}
	public JSONObject getParams() {
		return params;
	}
	public void setParams(JSONObject params) {
		this.params = params;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
}
