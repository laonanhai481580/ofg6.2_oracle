package com.ambition.supplier.task.web;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.supplier.task.service.SupplierTaskManager;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.task.entity.Task;
import com.opensymphony.xwork2.ActionContext;
@Namespace("/supplier/task")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "/supplier/task", type = "redirectAction") })
public class SupplierTaskAction extends CrudActionSupport<Task>{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deleteIds;
	private JSONObject params;//查询参数
	private Task task;
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private SupplierTaskManager taskManager;
	@Autowired
	private LogUtilDao logUtilDao;
	private Page<Task> page;
	@Override
	public Task getModel() {
		return task;
	}
	
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
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

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}
	
	public Page<Task> getPage() {
		return page;
	}

	public void setPage(Page<Task> page) {
		this.page = page;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	@Action("task")
	public String list() throws Exception {
		ActionContext.getContext().put("systemCodes", ApiFactory.getSettingService().getOptionsByGroupCode("supplier-system-code"));
		return "SUCCESS";
	}
	@Action("list-datas")
	public String getSuppliers() throws Exception {
		try{
			page = taskManager.getAllTasksByUserType(page);
			this.renderText(PageUtils.pageToJson(page));
		}catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	@Override
	protected void prepareModel() throws Exception {
	}
	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String save() throws Exception {
		return null;
	}
}
