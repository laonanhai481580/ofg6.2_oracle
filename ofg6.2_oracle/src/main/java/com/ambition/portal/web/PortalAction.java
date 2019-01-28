package com.ambition.portal.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.ambition.iqc.inspectionreport.service.IncomingInspectionActionsReportManager;
import com.ambition.portal.service.PortalManager;
import com.ambition.product.BaseAction;
import com.ambition.product.base.CrudActionSupport;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.product.util.freemarker.PortalUtil;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  lidefeng
 * @version 1.00 2017年1月6日 发布
 */
@Namespace("/portal")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/portal", type = "redirectAction") })
public class PortalAction extends BaseAction<Object>{
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private Long companyId;
	private String loginName;
	private Long id;
	private Long userId;
	@Autowired
	private IncomingInspectionActionsReportManager incomingInspectionActionsReportManager;
	@Autowired
	private PortalManager portalManager;
	

	@Action("show-iqc-datas")
	public String showIqcDatas() throws Exception{
		List<?> datas = null;
		String rows = Struts2Utils.getParameter("rows");
		try {
			datas = portalManager.queryIqcDatas(rows);
		} catch (Exception e) {
			datas = new ArrayList<Object>();
			log.error("显示进货检验不合格数据失败",e);
		}
		ActionContext.getContext().put("datas",datas);
		ActionContext.getContext().put("hostApp",PropUtils.getProp("host.app"));
		return SUCCESS;
	}
	@Action("show-mfg-datas")
	public String showMfgDatas() throws Exception{
		List<?> datas = null;
		String rows = Struts2Utils.getParameter("rows");
		try {
			datas = portalManager.queryMfgDatas(rows);
		} catch (Exception e) {
			datas = new ArrayList<Object>();
			log.error("显示过程检验不合格数据失败",e);
		}
		ActionContext.getContext().put("datas",datas);
		ActionContext.getContext().put("hostApp",PropUtils.getProp("host.app"));
		return SUCCESS;
	}
	@Action("show-iqc-audit-datas")
	public String showIqcAuditDatas() throws Exception{
		List<?> datas = null;
		String rows = Struts2Utils.getParameter("rows");
		try {
			datas = portalManager.queryIqcDatas(rows);
		} catch (Exception e) {
			datas = new ArrayList<Object>();
			log.error("显示进货检验待审数据失败",e);
		}
		ActionContext.getContext().put("datas",datas);
		ActionContext.getContext().put("hostApp",PropUtils.getProp("host.app"));
		return SUCCESS;
	}
	private String disposeSpecialCharacter(String json)
    {
		json = json.replaceAll("\\\\","\\\\\\\\");
		return PageUtils.disposeSpecialCharacter(json);
    }
	/**
	 * 显示待办事宜
	 */
	@Action("show-task-message-datas")
	public String showTaskMessageDatas() throws Exception {
		List<?> taskDatas = null;
		try {
			taskDatas = portalManager.queryTaskMessage(companyId,loginName,userId);
		} catch (Exception e) {
			taskDatas = new ArrayList<Object>();
			log.error("显示待办事宜消息失败",e);
		}
		ActionContext.getContext().put("taskDatas",taskDatas);
		ActionContext.getContext().put("hostImatrix",PropUtils.getProp("host.imatrix"));
		ActionContext.getContext().put("hostResources",PropUtils.getProp("host.resources"));
		return SUCCESS;
	}
	/**
	 * 首页宣传
	 */
	@Action("protoal-mess")
	public String protoalMess() throws Exception {
		return SUCCESS;
	}
	public Logger getLog() {
		return log;
	}
	public void setLog(Logger log) {
		this.log = log;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
