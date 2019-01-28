package com.ambition.aftersales.customer.web;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.aftersales.baseinfo.service.CustomerListManager;
import com.ambition.aftersales.customer.service.CustomerEvaluationManager;
import com.ambition.aftersales.entity.CustomerEvaluation;
import com.ambition.aftersales.entity.CustomerList;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
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
 * 类名:客户评价action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年8月31日 发布
 */
@Namespace("/aftersales/customer-evaluation")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/aftersales/customer-evaluation", type = "redirectAction") })
public class CustomerEvaluationAction extends BaseAction<CustomerEvaluation>{

	private static final long serialVersionUID = 1L;
	private CustomerEvaluation customerEvaluation;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
    private CustomerListManager customerListManager;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private Page<CustomerEvaluation> page;
	private File myFile;
	private String customer;//所属事业部
	private String yearInt;//年
	@Autowired
	private CustomerEvaluationManager customerEvaluationManager;
	@Override
	public CustomerEvaluation getModel() {
		return customerEvaluation;
	}
	@Action("delete")
	@LogInfo(optType="删除",message="删除客户评价")
	public String delete() throws Exception {
		try {
			customerEvaluationManager.deleteCustomerEvaluation(deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除客户评价失败",e);
		}
		return null;
	}
	
	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("list")
	public String list() throws Exception {
		customer = Struts2Utils.getParameter("customer");
		List<CustomerList> list = customerListManager.listAll();
	    List<Option> customers = customerListManager.converExceptionLevelToList(list);
		if(StringUtils.isEmpty(customer)){
			if(customers.size()>0){
				customer = customers.get(0).getValue();
			}
		}
		yearInt = Struts2Utils.getParameter("yearInt");
		List<Option> yearInts = ApiFactory.getSettingService().getOptionsByGroupCode("afs-lar-year");
		if(StringUtils.isEmpty(yearInt)){
			if(yearInts.size()>0){
				yearInt = yearInts.get(0).getValue();
			}
		}
		ActionContext.getContext().put("yearInts",yearInts);
		ActionContext.getContext().put("customers",customers);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			customerEvaluation=new CustomerEvaluation();
			customerEvaluation.setCompanyId(ContextUtils.getCompanyId());
			customerEvaluation.setCreatedTime(new Date());
			customerEvaluation.setCreator(ContextUtils.getLoginName());
			customerEvaluation.setModifiedTime(new Date());
			customerEvaluation.setModifier(ContextUtils.getLoginName());
			customerEvaluation.setModifierName(ContextUtils.getUserName());
		}else{
			customerEvaluation=customerEvaluationManager.getCustomerEvaluation(id);
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存客户评价")
	public String save() throws Exception {
		if(id != null && id != 0){
			customerEvaluation.setModifiedTime(new Date());
			customerEvaluation.setModifier(ContextUtils.getLoginName());
			customerEvaluation.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", customerEvaluation.toString());
		}else{
			logUtilDao.debugLog("保存", customerEvaluation.toString());
		}
		try {
			customerEvaluation.setCustomer(customer);
			customerEvaluation.setYearInt(Integer.valueOf(yearInt));
			customerEvaluationManager.saveCustomerEvaluation(customerEvaluation);
			this.renderText(JsonParser.object2Json(customerEvaluation));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存客户评价失败  ",e);
		}		
		return null;
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = customerEvaluationManager.search(page,customer,Integer.valueOf(yearInt));
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "客户评价");
		} catch (Exception e) {
			log.error("查询客户评价失败  ",e);
		}		
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出", message="客户评价")
	public String export() throws Exception {
		try {
			Page<CustomerEvaluation> page = new Page<CustomerEvaluation>(65535);
			page = customerEvaluationManager.search(page,customer,Integer.valueOf(yearInt));
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"客户评价台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出客户评价失败",e);
		}
		return null;
	}
	@Action("import")
	public String imports() throws Exception {
		return SUCCESS;
	}
	
	@Action("import-datas")
	@LogInfo(optType="导入",message="导入客户评价数据")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(customerEvaluationManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	
	/**
	  * 方法名: 下载客户评价模板
	  * <p>功能说明：下载客户评价模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download")
	@LogInfo(optType="下载",message="下载客户评价模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/afs-customer-evaluation.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "客户评价台账导入模板.xls";
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
	
	public void prepareEditSave() throws Exception{
		prepareModel();
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
	public Page<CustomerEvaluation> getPage() {
		return page;
	}
	public void setPage(Page<CustomerEvaluation> page) {
		this.page = page;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}		

	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getYearInt() {
		return yearInt;
	}
	public void setYearInt(String yearInt) {
		this.yearInt = yearInt;
	}

	
}
