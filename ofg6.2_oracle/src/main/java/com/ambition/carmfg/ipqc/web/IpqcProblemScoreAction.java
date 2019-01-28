package com.ambition.carmfg.ipqc.web;

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

import com.ambition.carmfg.entity.IpqcProblemScore;
import com.ambition.carmfg.ipqc.service.IpqcProblemScoreManager;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.ambition.util.annotation.LogInfo;
import com.ambition.util.common.CommonUtil;
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
 * 类名:IPQC问题严重度分数维护action
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月20日 发布
 */
@Namespace("/carmfg/ipqc/problem-score")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/carmfg/ipqc/problem-score", type = "redirectAction") })
public class IpqcProblemScoreAction extends BaseAction<IpqcProblemScore>{

	private static final long serialVersionUID = 1L;
	private IpqcProblemScore ipqcProblemScore;
	@Autowired
	private LogUtilDao logUtilDao;
	private Logger log=Logger.getLogger(this.getClass());
	private Long id;
	private String deleteIds;
	private Page<IpqcProblemScore> page;
	@Autowired
	private IpqcProblemScoreManager ipqcProblemScoreManager;
	@Override
	public IpqcProblemScore getModel() {
		return ipqcProblemScore;
	}
	@Action("delete")
	@LogInfo(optType="删除",message="IPQC问题严重度分数维护信息")
	public String delete() throws Exception {
		try {
			ipqcProblemScoreManager.deleteIpqcProblemScore(deleteIds);
		} catch (Exception e) {
			renderText("删除失败:" + e.getMessage());
			log.error("删除IPQC问题严重度分数维护信息失败",e);
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
			ipqcProblemScore=new IpqcProblemScore();
			ipqcProblemScore.setCompanyId(ContextUtils.getCompanyId());
			ipqcProblemScore.setCreatedTime(new Date());
			ipqcProblemScore.setCreator(ContextUtils.getUserName());
			ipqcProblemScore.setModifiedTime(new Date());
			ipqcProblemScore.setModifier(ContextUtils.getLoginName());
			ipqcProblemScore.setModifierName(ContextUtils.getUserName());
			ipqcProblemScore.setBusinessUnitName(ContextUtils.getSubCompanyName());
			ipqcProblemScore.setBusinessUnitCode(CommonUtil.getBusinessUnitCode(ContextUtils.getCompanyName()));
		}else{
			ipqcProblemScore=ipqcProblemScoreManager.getIpqcProblemScore(id);
		}
		
	}
	
	@Action("save")
	@LogInfo(optType="保存",message="保存IPQC问题严重度分数维护信息")
	public String save() throws Exception {
		if(id != null && id != 0){
			ipqcProblemScore.setModifiedTime(new Date());
			ipqcProblemScore.setModifier(ContextUtils.getLoginName());
			ipqcProblemScore.setModifierName(ContextUtils.getUserName());
			
			logUtilDao.debugLog("修改", ipqcProblemScore.toString());
		}else{
			logUtilDao.debugLog("保存", ipqcProblemScore.toString());
		}
		try {
			ipqcProblemScoreManager.saveIpqcProblemScore(ipqcProblemScore);
			this.renderText(JsonParser.object2Json(ipqcProblemScore));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("保存IPQC问题严重度分数维护信息失败  ",e);
		}		
		return null;
	}
	@Action("list-datas")
	public String listDatas() throws Exception {
		try {
			page = ipqcProblemScoreManager.search(page);
			renderText(PageUtils.pageToJson(page));
			logUtilDao.debugLog("查询", "IPQC问题严重度分数维护信息");
		} catch (Exception e) {
			log.error("查询IPQC问题严重度分数维护信息失败  ",e);
		}		
		return null;
	}
	@Action("export")
	@LogInfo(optType="导出", message="IPQC问题严重度分数维护")
	public String export() throws Exception {
		try {
			Page<IpqcProblemScore> page = new Page<IpqcProblemScore>(65535);
			page = ipqcProblemScoreManager.search(page);
			this.renderText(ExcelExporter.export(ApiFactory.getMmsService().getExportData(page,Struts2Utils.getParameter("_list_code")),"IPQC问题严重度分数维护台账"));
		} catch (Exception e) {
			createErrorMessage(e.getMessage());
			log.error("导出IPQC稽核预警信息失败",e);
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
	public Page<IpqcProblemScore> getPage() {
		return page;
	}
	public void setPage(Page<IpqcProblemScore> page) {
		this.page = page;
	}
	
}
