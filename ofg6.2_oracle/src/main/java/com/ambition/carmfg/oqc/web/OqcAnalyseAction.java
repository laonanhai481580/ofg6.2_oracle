package com.ambition.carmfg.oqc.web;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.entity.OqcInspection;
import com.ambition.carmfg.entity.OqcProcedure;
import com.ambition.carmfg.factoryprocedure.service.OqcFactoryManager;
import com.ambition.carmfg.factoryprocedure.service.OqcProcedureManager;
import com.ambition.carmfg.oqc.service.OqcAnalyseManager;
import com.ambition.product.BaseAction;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.common.DateUtil;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * 类名:POQ层别统计表
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  lxh
 * @version 1.00 2017年11月21日 发布
 */
@Namespace("/carmfg/data-acquisition/analyse")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/carmfg/data-acquisition/analyse", type = "redirectAction") })
public class OqcAnalyseAction  extends BaseAction<OqcInspection>{
	
	private static final long serialVersionUID = 1L;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private JSONObject params;//过滤条件
	private JSONObject dateParams;//日期范围
	private String deleteIds;
	private OqcInspection oqcInspection;
	private Page<OqcInspection> page;
	@Autowired
	private OqcAnalyseManager oqcAnalyseManager;
	@Autowired
	private OqcFactoryManager oqcFactoryManager;
	@Autowired
	private OqcProcedureManager oqcProcedureManager;
	/**
	  * 方法名: LRR层别图
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("oqc-item-trend")
	public String allTrend() throws Exception {
		initTrend();
		return SUCCESS;
	}
	//初始化表头
	private void initTrend() {
		//厂区
		ActionContext.getContext().put("businessUnits",ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits"));
		// 产品类别
		ActionContext.getContext().put("productTypes",ApiFactory.getSettingService().getOptionsByGroupCode("mfg_oqc_product_type"));
		// 班别
		ActionContext.getContext().put("classGroups",ApiFactory.getSettingService().getOptionsByGroupCode("mfg_class_group"));
		//工厂
		List<Option> factorys = oqcFactoryManager.listAllForOptions();
		ActionContext.getContext().put("factorys",factorys);
		//工序
		List<OqcProcedure> procedures = oqcProcedureManager.getAllOqcProcedure();
        List<Option> proceduresOptions = oqcProcedureManager.converOqcProcedureToList(procedures);
        ActionContext.getContext().put("procedures",proceduresOptions);
        //设置默认值
		setDefaultValues();
	}
	private void setDefaultValues(){
		//设置默认值
		Calendar calendar = Calendar.getInstance();
		//默认天
		ActionContext.getContext().put("defaultDate_end",DateUtil.formateDateStr(calendar));
		calendar.set(Calendar.DATE,1);
		ActionContext.getContext().put("defaultDate_start",DateUtil.formateDateStr(calendar));
	}
	//缓存报表LRR层别图数据
	private static final String 缓存报表数据 = "oqc_item_data";
	/**
 	  * LRR层别图数据
	  * 方法名: 
	  * <p>功能说明：</p>
	  * @return
	 */
	@Action("analysis-list-datas")
	public String listDatas() throws Exception{
		try {
			JSONObject result = oqcAnalyseManager.queryTopReport(CommonUtil.convertJsonObject(params));
			//缓存数据
			Struts2Utils.getSession().setAttribute(缓存报表数据,result.get("cacheDataMap"));
			result.remove("cacheDataMap");
			renderText(result.toString());
		} catch (Exception e) {
			log.error("统计失败!",e);
			JSONObject result = new JSONObject();
			result.put("error",true);
			result.put("message","统计失败"+  e.getMessage());
			renderText(result.toString());
		}
		return null;
	}
	/**
	  * LRR层别图表格
	  * 方法名: 
	  * <p>功能说明：</p>
	  * @return
	 */
	@SuppressWarnings("unchecked")
	@Action("oqc-trend-table")
	public String trendTable() throws Exception{
		try {
			//获取缓存数据
			Map<String,Object> cacheDataMap = (Map<String,Object>)Struts2Utils.getSession().getAttribute(缓存报表数据);
			Struts2Utils.getRequest().setAttribute("cacheDataMap",cacheDataMap);
		} catch (Exception e) {
			log.error("表格生成失败!",e);
		}
		return SUCCESS;
	}
	
	/**
	 * 导出趋势图到Exel
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Action("export-charts")
	public String exportCharts() throws Exception {
		JSONObject result = new JSONObject();
		try {
			Struts2Utils.getRequest().setCharacterEncoding("utf-8");
			String svg = Struts2Utils.getRequest().getParameter("svg");
			String width = Struts2Utils.getParameter("width");
			String height = Struts2Utils.getParameter("height");
			
			//获取缓存数据
			Map<String,Object> cacheDataMap = (Map<String,Object>)Struts2Utils.getSession().getAttribute(缓存报表数据);
			String flagStrs = oqcAnalyseManager.exportChartAndGrid(svg,"出货不良层别统计图",width,height,cacheDataMap);
			result.put("flagStrs",flagStrs);
		} catch (Exception e) {
			log.error("导出失败!",e);
			result.put("error",true);
			result.put("message","导出失败,"+  e.getMessage());
		}
		renderText(result.toString());
		return null;
	}
	
	public String save() throws Exception {
		return "input";
	}
	public Logger getLog() {
		return log;
	}
	public void setLog(Logger log) {
		this.log = log;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public JSONObject getParams() {
		return params;
	}
	public void setParams(JSONObject params) {
		this.params = params;
	}
	public JSONObject getDateParams() {
		return dateParams;
	}
	public void setDateParams(JSONObject dateParams) {
		this.dateParams = dateParams;
	}
	public String getDeleteIds() {
		return deleteIds;
	}
	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}
	public OqcInspection getOqcInspection() {
		return oqcInspection;
	}
	public void setOqcInspection(OqcInspection oqcInspection) {
		this.oqcInspection = oqcInspection;
	}
	public Page<OqcInspection> getPage() {
		return page;
	}
	public void setPage(Page<OqcInspection> page) {
		this.page = page;
	}
	
}
