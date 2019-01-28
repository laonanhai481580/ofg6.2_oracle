package com.ambition.supplier.materialadmit.list.web;

import java.io.File;
import java.io.InputStream;
import java.util.Calendar;
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

import com.ambition.supplier.entity.MaterialRecognitionList;
import com.ambition.supplier.materialadmit.list.services.MaterialRecognitionListManager;
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


@Namespace("/supplier/material-admit/list")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location ="/supplier/material-admit/list", type = "redirectAction") })
public class MaterialRecognitionListAction extends CrudActionSupport<MaterialRecognitionList>{
	
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	private Long id;
	private String ids;
	private String deleteIds;
	private MaterialRecognitionList materialRecognitionList;
	private Page<MaterialRecognitionList> page;
	private File myFile;
	@Autowired
	private MaterialRecognitionListManager materialRecognitionListManager;
	@Autowired
	private LogUtilDao logUtilDao;
	
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

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public MaterialRecognitionList getMaterialRecognitionList() {
		return materialRecognitionList;
	}

	public void setMaterialRecognitionList(MaterialRecognitionList materialRecognitionList) {
		this.materialRecognitionList = materialRecognitionList;
	}

	public Page<MaterialRecognitionList> getPage() {
		return page;
	}

	public void setPage(Page<MaterialRecognitionList> page) {
		this.page = page;
	}
		
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

		public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

		@Override
		public MaterialRecognitionList getModel() {
			// TODO Auto-generated method stub
			return materialRecognitionList;
		}
		
		@Action("delete")
		@LogInfo(optType="删除",message="删除材料承认清单数据")
		@Override
		public String delete() throws Exception {
			try {
				materialRecognitionListManager.deleteMaterialRecognitionList(deleteIds);
				Struts2Utils.getRequest().setAttribute(LogInfo.MESSAGE_ATTRIBUTE, "删除数据，编号："+deleteIds);
			} catch (Exception e) {
				// TODO: handle exception
				renderText("删除失败:" + e.getMessage());
				log.error("删除数据信息失败",e);
			}
			return null;
		}
		@Override
		public String input() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Action("list")
		@Override
		public String list() throws Exception {
			return SUCCESS;
		}
		
		@Action("list-datas")
		@LogInfo(optType="查询",message="查询数据")
		public String listDates(){
			try {
				page = materialRecognitionListManager.search(page);
				renderText(PageUtils.pageToJson(page));
			} catch (Exception e) {
				// TODO: handle exception
				log.error("台账获取例表失败", e);
			}
			return null;
		}
		
		@Override
		protected void prepareModel() throws Exception {
			// TODO Auto-generated method stub
			if(id == null){
				materialRecognitionList = new MaterialRecognitionList();
				materialRecognitionList.setCompanyId(ContextUtils.getCompanyId());
				materialRecognitionList.setCreatedTime(new Date());
				materialRecognitionList.setCreator(ContextUtils.getUserName());
				materialRecognitionList.setModifiedTime(new Date());
				materialRecognitionList.setModifier(ContextUtils.getUserName());
				materialRecognitionList.setBusinessUnitName(ContextUtils.getSubCompanyName());
				materialRecognitionList.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
			}else {
				materialRecognitionList = materialRecognitionListManager.getMaterialRecognitionList(id);
			}
		}

		
		@Action("save")
		@LogInfo(optType="保存",message="保存材料承认清单数据")
		@Override
		public String save() throws Exception {
			try {
				materialRecognitionListManager.saveMaterialRecognitionList(materialRecognitionList);
				renderText(JsonParser.getRowValue(materialRecognitionList));
				logUtilDao.debugLog("保存",materialRecognitionList.toString());
			} catch (Exception e) {
				// TODO: handle exception
				createErrorMessage("保存失败："+e.getMessage());
			}
			return null;
		}
		@Action("export")
		@LogInfo(optType="导出",message="材料承认清单台帐")
		public String export() throws Exception {
			Page<MaterialRecognitionList> page = new Page<MaterialRecognitionList>(100000);
			page = materialRecognitionListManager.list(page);
			renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,"MATERIAL_RECOGNITION_LIST"),"材料承认清单台帐"));
			logUtilDao.debugLog("导出", "材料承认清单台帐");
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
					renderHtml(materialRecognitionListManager.importDatas(myFile));
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
		@LogInfo(optType="下载",message="下载材料承认清单模版")
		public String downloadTemplate() throws Exception {
			InputStream inputStream = null;
			try {
				inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/gp-exemption.xls");
				Workbook book = WorkbookFactory.create(inputStream);
				String fileName = "材料承认清单模板.xls";
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
		//创建返回消息
		public void createErrorMessage(String message){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("error",true);
			map.put("message",message);
			renderText(JSONObject.fromObject(map).toString());
		}
		public Date getTestReportDate(Date testReportDate){
			Calendar c = Calendar.getInstance();
			c.setTime(testReportDate);
			c.add(Calendar.DAY_OF_MONTH, 365);
			Date tomorrow = c.getTime();
			return tomorrow;
		}
}
