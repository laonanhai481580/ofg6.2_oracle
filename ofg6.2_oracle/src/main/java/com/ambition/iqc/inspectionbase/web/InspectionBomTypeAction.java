package com.ambition.iqc.inspectionbase.web;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
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

import com.ambition.iqc.entity.InspectionBomType;
import com.ambition.iqc.inspectionbase.service.InspectionBomTypeManager;
import com.ambition.product.BaseAction;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.ExcelExporter;
import com.norteksoft.product.util.JsonParser;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;


@Namespace("/iqc/inspection-base/inspection-bom-type")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "iqc/inspection-base/inspection-bom-type", type = "redirectAction") })
public class InspectionBomTypeAction extends BaseAction<InspectionBomType> {

	private static final long serialVersionUID = 1L;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;//删除的BOM编号
	private Page<InspectionBomType> page;			
	private File myFile;
	
	private InspectionBomType inspectionBomType;
	
	@Autowired
	private LogUtilDao logUtilDao;
	
 	@Autowired
	private InspectionBomTypeManager inspectionBomTypeManager;
 	
 	
	public Page<InspectionBomType> getPage() {
		return page;
	}

	public void setPage(Page<InspectionBomType> page) {
		this.page = page;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public InspectionBomType getInspectionBomType() {
		return inspectionBomType;
	}
	public void setInspectionBomType(InspectionBomType inspectionBomType) {
		this.inspectionBomType = inspectionBomType;
	}
	public InspectionBomType getModel() {
		return inspectionBomType;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(id==null){
			inspectionBomType = new InspectionBomType();
			inspectionBomType.setCreatedTime(new Date());
			inspectionBomType.setCompanyId(ContextUtils.getCompanyId());
			inspectionBomType.setCreator(ContextUtils.getUserName());
			inspectionBomType.setLastModifiedTime(new Date());
			inspectionBomType.setLastModifier(ContextUtils.getUserName());
			inspectionBomType.setBusinessUnitName(ContextUtils.getSubCompanyName());
			inspectionBomType.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else {
			inspectionBomType = inspectionBomTypeManager.getInspectionBom(id);
		}
	}
	
	@Action("input")
	@Override
	public String input() throws Exception {
		return null;
	}
	@Action("save")
	@LogInfo(optType="保存",message="报检物料类别")
	@Override
	public String save() throws Exception {
		if(id != null && id != 0){
			inspectionBomType.setModifiedTime(new Date());
			inspectionBomType.setModifier(ContextUtils.getLoginName());
			inspectionBomType.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", inspectionBomType.toString());
		}else{
			logUtilDao.debugLog("保存", inspectionBomType.toString());
		}
		try {
			inspectionBomTypeManager.saveInspectionBomType(inspectionBomType);
			this.renderText(JsonParser.object2Json(inspectionBomType));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存报检物料信息失败  ",e);
		}		
		return null;
	}
	
	@Action("delete")
	@LogInfo(optType="删除",message="报检物料")
	@Override
	public String delete() throws Exception {
		if(StringUtils.isEmpty(deleteIds)){
			createErrorMessage("删除的对象不存在!");
		}else{
			try {
				inspectionBomTypeManager.deleteInspectionBomType(deleteIds);				
			} catch (Exception e) {
				createErrorMessage("删除失败:" + e.getMessage());
			}
		}
		return null;
	}

	@Action("list")
	@Override
	public String list() throws Exception {
		renderMenu();
		return SUCCESS;
	}		
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = inspectionBomTypeManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "报检物料信息");
		} catch (Exception e) {
			log.error("查询报检物料信息失败  ",e);
		}		
		return null;
	}

	@Action("export")
	@LogInfo(optType="导出",message="报检物料")
	public String exports() throws Exception {
		try {
			Page<InspectionBomType> page = new Page<InspectionBomType>(65535);
			page = inspectionBomTypeManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"报检物料类别台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出报检物料信息失败",e);
		}
		return null;
	}
	@Action("check-item-select")
	public String checkItemSelect() throws Exception {
		return SUCCESS;
	}	
	/**
	 * 添加检验项目
	 * @return
	 * @throws Exception
	 */
	@Action("add-bom")
	public String addItem() throws Exception {
		try{
			int addCount = inspectionBomTypeManager.addBom(deleteIds);
			createMessage("操作成功!共添加了"+addCount + "个物料!");
		}catch (Exception e) {
			log.error("添加物料失败!",e);
			createErrorMessage("操作失败:" + e.getMessage());
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
				renderHtml(inspectionBomTypeManager.importDatas(myFile));
			}
		} catch (Exception e) {
			renderHtml("导入失败:<font color=red>" + e.getMessage() + "</font>");
		}
		return null;
	}
	/**
	  * 方法名: 下载报检物料导入模板
	  * <p>功能说明：下载报检物料导入模板</p>
	  * @return
	  * @throws Exception
	 */
	@Action("download-template")
	@LogInfo(optType="下载",message="下载报检物料导入模板")
	public String downloadTemplate() throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/iqc-inspection-bom-type-template.xlsx");
			Workbook book = WorkbookFactory.create(inputStream);
			String fileName = "报检物料导入模板.xls";
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
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	private void createMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",false);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
}
