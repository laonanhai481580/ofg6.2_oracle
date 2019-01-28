package com.ambition.aftersales.customer.web;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.aftersales.customer.service.CustomerIssueManager;
import com.ambition.aftersales.entity.CustomerIssue;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
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
 * 类名:客户issue台账action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00  2018年4月12日 发布
 */
@Namespace("/aftersales/customer-issue")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/aftersales/customer-issue", type = "redirectAction") })
public class CustomerIssueAction extends BaseAction<CustomerIssue>{

	private static final long serialVersionUID = 1L;
	private CustomerIssue customerIssue;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private Page<CustomerIssue> page;
	private File myFile;
	private String businessUnit;//所属事业部
	@Autowired
	private CustomerIssueManager customerIssueManager;
	@Override
	public CustomerIssue getModel() {
		return customerIssue;
	}
	@Action("delete")
	@LogInfo(optType="删除",message="售后客户issue")
	public String delete() throws Exception {
		try {
			customerIssueManager.deleteCustomerIssue(deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除售后客户issue失败",e);
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("list")
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			customerIssue=new CustomerIssue();
			customerIssue.setCompanyId(ContextUtils.getCompanyId());
			customerIssue.setCreatedTime(new Date());
			customerIssue.setCreator(ContextUtils.getLoginName());
			customerIssue.setModifiedTime(new Date());
			customerIssue.setModifier(ContextUtils.getLoginName());
			customerIssue.setModifierName(ContextUtils.getUserName());
		}else{
			customerIssue=customerIssueManager.getCustomerIssue(id);
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存售后客户issue")
	public String save() throws Exception {
		if(id != null && id != 0){
			customerIssue.setModifiedTime(new Date());
			customerIssue.setModifier(ContextUtils.getLoginName());
			customerIssue.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", customerIssue.toString());
		}else{
			logUtilDao.debugLog("保存", customerIssue.toString());
		}
		try {
			customerIssueManager.saveCustomerIssue(customerIssue);
			this.renderText(JsonParser.object2Json(customerIssue));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存售后客户issue失败  ",e);
		}		
		return null;
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = customerIssueManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "售后客户issue");
		} catch (Exception e) {
			log.error("查询售后客户issue失败  ",e);
		}		
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出", message="售后客户issue")
	public String export() throws Exception {
		try {
			Page<CustomerIssue> page = new Page<CustomerIssue>(65535);
			page = customerIssueManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"售后客户issue台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出售后客户issue失败",e);
		}
		return null;
	}
	@Action("import")
	public String imports() throws Exception {
		return SUCCESS;
	}
	
	@Action("import-datas")
	@LogInfo(optType="导入",message="导入售后客退率数据")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(customerIssueManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	
	/**
	  * 方法名: 下载售后客户issue模板
	  * <p>功能说明：下载售后客户issue模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download")
	@LogInfo(optType="下载",message="下载售后客户issue模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/afs-customer-issue.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "售后客户issue台账导入模板.xls";
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
	public Page<CustomerIssue> getPage() {
		return page;
	}
	public void setPage(Page<CustomerIssue> page) {
		this.page = page;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}		
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
}
