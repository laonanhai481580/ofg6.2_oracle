package com.ambition.gsm.equipment.web;

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

import com.ambition.gsm.entity.InternalAuditor;
import com.ambition.gsm.equipment.service.InternalAuditorManager;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.acs.service.organization.UserManager;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.User;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
/**
 * 
 * 类名:内审员信息action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2018年7月26日 发布
 */
@Namespace("/gsm/equipment/internal-auditor")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/gsm/equipment/internal-auditor", type = "redirectAction") })
public class InternalAuditorAction extends BaseAction<InternalAuditor>{

	private static final long serialVersionUID = 1L;
	private InternalAuditor internalAuditor;
	@Autowired
	private LogUtilDao logUtilDao;
	@Autowired
	private UserManager userManager;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private File myFile;
	private Page<InternalAuditor> page;
	@Autowired
	private InternalAuditorManager internalAuditorManager;
	@Override
	public InternalAuditor getModel() {
		return internalAuditor;
	}
	@Action("delete")
	@LogInfo(optType="删除",message="内审员信息")
	public String delete() throws Exception {
		try {
			String deleteNos=internalAuditorManager.deleteInternalAuditor(deleteIds);
			Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除内审员信息，姓名："+deleteNos);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除内审员信息失败",e);
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Action("list")
	@LogInfo(optType="新建",message="新建内审员信息")
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			internalAuditor=new InternalAuditor();
			internalAuditor.setCompanyId(ContextUtils.getCompanyId());
			internalAuditor.setCreatedTime(new Date());
			internalAuditor.setCreator(ContextUtils.getUserName());
			internalAuditor.setModifiedTime(new Date());
			internalAuditor.setModifier(ContextUtils.getLoginName());
			internalAuditor.setModifierName(ContextUtils.getUserName());
			internalAuditor.setBusinessUnitName(ContextUtils.getSubCompanyName());
			internalAuditor.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			internalAuditor=internalAuditorManager.getInternalAuditor(id);
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存内审员信息")
	public String save() throws Exception {
		if(id != null && id != 0){
			internalAuditor.setModifiedTime(new Date());
			internalAuditor.setModifier(ContextUtils.getLoginName());
			internalAuditor.setModifierName(ContextUtils.getUserName());
			User user= ApiFactory.getAcsService().getUserByLoginName(ContextUtils.getLoginName());
			String subName=user.getSubCompanyName();
			internalAuditor.setFactoryClassify(subName);
			logUtilDao.debugLog("修改", internalAuditor.toString());
		}else{
			logUtilDao.debugLog("保存", internalAuditor.toString());
		}
		try {
			internalAuditorManager.saveInternalAuditor(internalAuditor);
			this.renderText(JsonParser.object2Json(internalAuditor));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存内审员信息失败  ",e);
		}		
		return null;
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = internalAuditorManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "内审员信息");
		} catch (Exception e) {
			log.error("查询内审员信息失败  ",e);
		}		
		return null;
	}

	@Action("export")
	@LogInfo(optType="导出", message="内审员信息")
	public String export() throws Exception {
		try {
			Page<InternalAuditor> page = new Page<InternalAuditor>(65535);
			page = internalAuditorManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"内审员信息台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出内审员信息失败",e);
		}
		return null;
	}
	@Action("import")
	public String imports() throws Exception {
		return SUCCESS;
	}
	
	@Action("import-datas")
	public String importDatas() throws Exception {
		try {
			if(myFile != null){
				renderHtml(internalAuditorManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	/**
	  * 方法名: 下载检设备参数模板
	  * <p>功能说明：下载检验标准的模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download-template")
	@LogInfo(optType="下载",message="下载内审员信息模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/gsm-internal-auditor.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "内审员信息模板.xls";
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
	public Page<InternalAuditor> getPage() {
		return page;
	}
	public void setPage(Page<InternalAuditor> page) {
		this.page = page;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	
}
