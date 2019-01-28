package com.ambition.iqc.inspectionreport.web;
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

import com.ambition.iqc.entity.CheckItem;
import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.iqc.inspectionbase.service.InspectingIndicatorManager;
import com.ambition.iqc.inspectionreport.service.IncomingInspectionActionsReportManager;
import com.ambition.util.annotation.LogInfo;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**    
 * 检验报告ACTION
 *
 */
@Namespace("/iqc/statistical-analysis")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "iqc/statistical-analysis", type = "redirectAction") })
public class StatisticalAnalysisAction extends CrudActionSupport<IncomingInspectionActionsReport> {
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private IncomingInspectionActionsReport incomingInspectionActionsReport;
	private JSONObject params;
	private Page<CheckItem> page;
	private Long id;
	@Autowired
	private IncomingInspectionActionsReportManager incomingInspectionActionsReportManager;
	@Autowired
	private InspectingIndicatorManager inspectingIndicatorManager;

	public IncomingInspectionActionsReport getIncomingInspectionActionsReport() {
		return incomingInspectionActionsReport;
	}

	public void setIncomingInspectionActionsReport(
			IncomingInspectionActionsReport incomingInspectionActionsReport) {
		this.incomingInspectionActionsReport = incomingInspectionActionsReport;
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	public Page<CheckItem> getPage() {
		return page;
	}

	public void setPage(Page<CheckItem> page) {
		this.page = page;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	public IncomingInspectionActionsReport getModel() {
		return null;
	}

	@Override
	public String save() throws Exception {
		return null;
	}

	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {		
	}
	
	/**
	 * 尺寸分布趋势图页面
	 */
	@Action("size-spread-rate")
	@LogInfo(optType="页面",message="尺寸分布趋势图页面")
	public String sizeSpreadchart() throws Exception {
		ActionContext.getContext().put("businessUnits",ApiFactory.getSettingService().getOptionsByGroupCode("businessUnits"));
		ActionContext.getContext().put("processSections",ApiFactory.getSettingService().getOptionsByGroupCode("processSections"));
		ActionContext.getContext().put("checkItems",new Option());
		return SUCCESS;
	}

	/**
	 * 尺寸分布趋势图数据
	 */
	@Action("size-spread-chart-datas")
	@LogInfo(optType="数据",message="尺寸分布趋势图数据")
	public String sizeSpreadchartDatas() throws Exception {
		try {
			Map<String,Object> map=incomingInspectionActionsReportManager.getSizeSpreadChartDatas(params);
			this.renderText(JSONObject.fromObject(map).toString());
		} catch (Exception e) {
			log.error("查询出错!",e);
		}
		return null;	
	}
	/**
	 * 尺寸分布趋势图页面
	 */
	@Action("show-detail")
	@LogInfo(optType="页面",message="查看详情")
	public String showDetail() throws Exception {
		
		
		return SUCCESS;
	}	
	/**
	 * 尺寸分布趋势图页面
	 */
	@Action("show-detail-datas")
	@LogInfo(optType="页面",message="查看详情")
	public String showDetailDatas() throws Exception {
		String id=Struts2Utils.getParameter("id");
		try {
			page=incomingInspectionActionsReportManager.listItems(page,Long.valueOf(id));
		} catch (Exception e) {
			e.printStackTrace();
		}		
		this.renderText(PageUtils.pageToJson(page));
		return null;
	}
	@Action("bom-code-select")
	public String productBomSelect() throws Exception {
		return SUCCESS;
	}
    @Action("size-select")
    public String selectByProcess() throws Exception {
    	JSONObject result = new JSONObject();
        try{
            String checkBomCode = Struts2Utils.getParameter("checkBomCode");
          //查询所有尺寸的检验项目
    		List<String> list=incomingInspectionActionsReportManager.serachCheckItems(checkBomCode);
            String checkItems = "";
            for (String str : list) {
	            	if(checkItems.length()==0){
	            		checkItems = str;
					}else{
						checkItems += "," +  str;
					}
            }
            result.put("checkItems",  checkItems);
            result.put("error", false);
        }catch(Exception e){
            result.put("error", true);
            result.put("message", "查找检验项目失败");
        }
        this.renderText(JsonParser.object2Json(result));
        return null;
    }
}
