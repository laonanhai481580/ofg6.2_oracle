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

import com.ambition.aftersales.customer.service.CustomerReturnManager;
import com.ambition.aftersales.entity.CustomerReturn;
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
 * 类名:客退率台账action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00  2018年4月12日 发布
 */
@Namespace("/aftersales/customer-return")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/aftersales/customer-return", type = "redirectAction") })
public class CustomerReturnAction extends BaseAction<CustomerReturn>{

	private static final long serialVersionUID = 1L;
	private CustomerReturn customerReturn;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private Page<CustomerReturn> page;
	private File myFile;
	private String businessUnit;//所属事业部
	@Autowired
	private CustomerReturnManager customerReturnManager;
	@Override
	public CustomerReturn getModel() {
		return customerReturn;
	}
	@Action("delete")
	@LogInfo(optType="删除",message="售后客退率")
	public String delete() throws Exception {
		try {
			customerReturnManager.deleteCustomerReturn(deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除售后客退率失败",e);
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
			customerReturn=new CustomerReturn();
			customerReturn.setCompanyId(ContextUtils.getCompanyId());
			customerReturn.setCreatedTime(new Date());
			customerReturn.setCreator(ContextUtils.getLoginName());
			customerReturn.setModifiedTime(new Date());
			customerReturn.setModifier(ContextUtils.getLoginName());
			customerReturn.setModifierName(ContextUtils.getUserName());
		}else{
			customerReturn=customerReturnManager.getCustomerReturn(id);
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存售后客退率")
	public String save() throws Exception {
		if(id != null && id != 0){
			customerReturn.setModifiedTime(new Date());
			customerReturn.setModifier(ContextUtils.getLoginName());
			customerReturn.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", customerReturn.toString());
		}else{
			logUtilDao.debugLog("保存", customerReturn.toString());
		}
		try {
			customerReturnManager.saveCustomerReturn(customerReturn);
			this.renderText(JsonParser.object2Json(customerReturn));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存售后客退率失败  ",e);
		}		
		return null;
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = customerReturnManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "售后客退率");
		} catch (Exception e) {
			log.error("查询售后客退率失败  ",e);
		}		
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出", message="售后客退率")
	public String export() throws Exception {
		try {
			Page<CustomerReturn> page = new Page<CustomerReturn>(65535);
			page = customerReturnManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"售后客退率台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出售后客退率失败",e);
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
				renderHtml(customerReturnManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	
	/**
	  * 方法名: 下载售后客退率模板
	  * <p>功能说明：下载售后客退率模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download")
	@LogInfo(optType="下载",message="下载售后客退率模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/afs-customer-return.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "售后客退率台账导入模板.xls";
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
	public Page<CustomerReturn> getPage() {
		return page;
	}
	public void setPage(Page<CustomerReturn> page) {
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
