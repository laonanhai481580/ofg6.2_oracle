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
import com.ambition.aftersales.lar.service.LarDataManager;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.mms.base.utils.view.GridColumnInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
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
public class LarDataAction extends BaseAction<LarData>{

	private static final long serialVersionUID = 1L;
	private LarData larData;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private Page<LarData> page;
	private JSONObject params;//查询参数
	@Autowired
	private LarDataManager larDataManager;
	private GridColumnInfo larItemGridColumnInfo;
	@Override
	public LarData getModel() {
		return larData;
	}
	@Action("delete")
	@LogInfo(optType="删除")
	public String delete() throws Exception {
		try {
			String deleteNos=larDataManager.deleteLarData(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除LAR数据，客户："+deleteNos);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除LAR数据信息失败",e);
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return SUCCESS;
	}	
	
	@Action("list")
	public String list() throws Exception {
		return SUCCESS;
	}

	@Action("hide-list")
	public String hideList() throws Exception {
		return SUCCESS;
	}	
	
	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			larData=new LarData();
			larData.setCompanyId(ContextUtils.getCompanyId());
			larData.setCreatedTime(new Date());
			larData.setCreator(ContextUtils.getUserName());
			larData.setModifiedTime(new Date());
			larData.setModifier(ContextUtils.getLoginName());
			larData.setModifierName(ContextUtils.getUserName());
			larData.setBusinessUnitName(ContextUtils.getSubCompanyName());
			larData.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			larData=larDataManager.getLarData(id);
		}		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存LAR数据信息")
	public String save() throws Exception {
		if(id != null && id != 0){
			larData.setModifiedTime(new Date());
			larData.setModifier(ContextUtils.getLoginName());
			larData.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", larData.toString());
		}else{
			logUtilDao.debugLog("保存", larData.toString());
		}
		try {
			larDataManager.saveLarData(larData);
			this.renderText(JsonParser.object2Json(larData));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存LAR数据信息失败  ",e);
		}		
		return null;
	}
	
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = larDataManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "LAR数据信息");
		} catch (Exception e) {
			log.error("查询LAR数据信息失败  ",e);
		}		
		return null;
	}
	
	@Action("hide-list-datas")
	public String hideListDatas() throws Exception {
		try {
			page = larDataManager.searchHide(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "LAR数据信息");
		} catch (Exception e) {
			log.error("查询LAR数据信息失败  ",e);
		}		
		return null;
	}
	
	@Action("export")
	@LogInfo(optType="导出", message="LAR数据")
	public String export() throws Exception {
		try {
			Page<LarData> page = new Page<LarData>(65535);
			page = larDataManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"LAR数据台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出LAR数据信息失败",e);
		}
		return null;
	}

	@Action("export-hide")
	@LogInfo(optType="导出", message="LAR数据")
	public String exportHide() throws Exception {
		try {
			Page<LarData> page = new Page<LarData>(65535);
			page = larDataManager.searchHide(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"LAR数据台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出LAR数据信息失败",e);
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
			formNo=larDataManager.hiddenState(eid,type);
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
			str="取消敏感数据标记";
		}else{
			str="标记数据为敏感数据";
		}
		Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE,str+"，日期和客户为:"+formNo);
		renderText(result.toString());
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
	public Page<LarData> getPage() {
		return page;
	}
	public void setPage(Page<LarData> page) {
		this.page = page;
	}
	public GridColumnInfo getLarItemGridColumnInfo() {
		return larItemGridColumnInfo;
	}
	public void setLarItemGridColumnInfo(GridColumnInfo larItemGridColumnInfo) {
		this.larItemGridColumnInfo = larItemGridColumnInfo;
	}
	public JSONObject getParams() {
		return params;
	}
	public void setParams(JSONObject params) {
		this.params = params;
	}
	
}
