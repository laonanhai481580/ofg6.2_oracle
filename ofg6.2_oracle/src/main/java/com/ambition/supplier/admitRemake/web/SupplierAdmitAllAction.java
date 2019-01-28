package com.ambition.supplier.admitRemake.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.gp.gpmaterial.services.GpMaterialManager;
import com.ambition.product.workflow.AmbWorkflowActionBase;
import com.ambition.product.workflow.AmbWorkflowManagerBase;
import com.ambition.supplier.admitRemake.service.SupplierAdmitAllManager;
import com.ambition.supplier.entity.SupplierAdmitAll;
import com.ambition.supplier.entity.SupplierAdmitFileAll;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;
/**
 * 
 * 类名:材料承认汇总Action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  lpf
 * @version 1.00 2018年9月13日 发布
 */
@Namespace("/supplier/material-admit/admit-remake/all")
@ParentPackage("default")
@Results({@Result(name=CrudActionSupport.RELOAD, location = "/supplier/material-admit/admit-remake/all", type = "redirectAction")})
public class SupplierAdmitAllAction extends AmbWorkflowActionBase<SupplierAdmitAll>{

	private static final long serialVersionUID = 1L;
	public static final String MOBILEINPUT = "mobile-input";
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Autowired
	private AcsUtils acsUtils;
	private String ids;
	private SupplierAdmitAll supplierAdmitAll;
	private String currentActivityName;
	private String transactorName;
	private File myFile;
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private GpMaterialManager gpMaterialManager;
	@Autowired
	private SupplierAdmitAllManager supplierAdmitAllManager;
	
	public String getIds() {
		return ids;
	}


	public void setIds(String ids) {
		this.ids = ids;
	}

	public SupplierAdmitAll getSupplierAdmitAll() {
		return supplierAdmitAll;
	}


	public void setSupplierAdmitAll(SupplierAdmitAll supplierAdmitAll) {
		this.supplierAdmitAll = supplierAdmitAll;
	}


	public String getCurrentActivityName() {
		return currentActivityName;
	}


	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}

	@Override
	protected AmbWorkflowManagerBase<SupplierAdmitAll> getAmbWorkflowBaseManager() {
		return supplierAdmitAllManager;
	}
	
	public String getTransactorName() {
		return transactorName;
	}


	public void setTransactorName(String transactorName) {
		this.transactorName = transactorName;
	}


	public File getMyFile() {
		return myFile;
	}


	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}


	/**
	  * 方法名: 初始化的参数在这里写
	  * <p>功能说明：</p>
	 */
	public void initForm(){
		if (getId() == null) {
			report.setFormNo(formCodeGenerated.generateAdmitNo());
			report.setCreatedTime(new Date());
			report.setCreator(ContextUtils.getLoginName());
			report.setCreatorName(ContextUtils.getUserName());
			report.setBusinessUnitName(ContextUtils.getCompanyName());
			report.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		} 
		transactorName=ContextUtils.getLoginName();
		List<SupplierAdmitFileAll> supplierAdmitFiles = report.getSupplierAdmitFiles();
		if(supplierAdmitFiles==null){
			supplierAdmitFiles = new ArrayList<SupplierAdmitFileAll>();
			SupplierAdmitFileAll item = new SupplierAdmitFileAll();
			supplierAdmitFiles.add(item);
		}
		ActionContext.getContext().put("_supplierAdmitFiles", supplierAdmitFiles);
		ActionContext.getContext().put("materialSorts",ApiFactory.getSettingService().getOptionsByGroupCode("supplicr_admit_materialSort"));
		ActionContext.getContext().put("admitProjects",ApiFactory.getSettingService().getOptionsByGroupCode("supplier_admit_status"));
		ActionContext.getContext().put("countersigns",ApiFactory.getSettingService().getOptionsByGroupCode("supplier_admit_countersigns"));
	}
	
		/**
		 * 列表数据(流程台账)
		 */
		@Action("list-datas")
		@LogInfo(optType="查询",message="查询数据")
		public String getListDatas() throws Exception {
			try{
				String dept =acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName();
				String code = ContextUtils.getLoginName();
				if("供应商".equals(dept)){
					page = supplierAdmitAllManager.listState(page,code,"FDL");
					renderText(PageUtils.pageToJson(page));
				}else{
					page = supplierAdmitAllManager.listState(page,null,"FDL");
					renderText(PageUtils.pageToJson(page));
				}
			}catch(Exception e){
				getLogger().error("查询失败!",e);
			}
			return null;
		}
		/**
		 * 列表页面
		 */
		@Action("list")
		@Override
		public String list() throws Exception {
			return SUCCESS;
		}
		/**
		 * 列表页面
		 */
		@Action("list-state")
		public String listState() throws Exception {
			return SUCCESS;
		}
		/**
		 * 列表数据(导入的清单台账)
		 */
		@Action("list-states")
		@LogInfo(optType="查询",message="查询数据")
		public String getListStates() throws Exception {
			try{
				String dept =acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName();
				String code = ContextUtils.getLoginName();
				String adminState= Struts2Utils.getParameter("adminState");
				if("供应商".equals(dept)){
					page = supplierAdmitAllManager.listState(page,code,adminState);
					renderText(PageUtils.pageToJson(page));
				}else{
					page = supplierAdmitAllManager.listState(page,null,adminState);
					renderText(PageUtils.pageToJson(page));
				}
			}catch(Exception e){
				getLogger().error("查询失败!",e);
			}
			return null;
		}

		/**
		 * 导出台账
		 * @return
		 * @throws Exception
		 */
		@Action("export")
		@LogInfo(optType="导出",message="导出数据")
		public String export() throws Exception {
			String adminState= Struts2Utils.getParameter("adminState");
			String code = ContextUtils.getLoginName();
			String dept =acsUtils.getManDepartment(ContextUtils.getUserId(), ContextUtils.getCompanyId()).getName();
			Page<SupplierAdmitAll> page = new Page<SupplierAdmitAll>(100000);
			if("供应商".equals(dept)){
				page = supplierAdmitAllManager.listState(page,code,null);
			}else{
				page = supplierAdmitAllManager.listState(page,null,adminState);
			}
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,getAmbWorkflowBaseManager().getEntityListCode()),getAmbWorkflowBaseManager().getWorkflowDefinitionName()));
			return null;
		}
	    /**
	     * 创建返回消息
	     * @param error
	     * @param message
	     * @return
	     */
	    public void createMessage(String message){
	        Map<String,Object> map = new HashMap<String, Object>();
	        map.put("error",false);
	        map.put("message",message);
	        renderText(JSONObject.fromObject(map).toString());
	    }
}
