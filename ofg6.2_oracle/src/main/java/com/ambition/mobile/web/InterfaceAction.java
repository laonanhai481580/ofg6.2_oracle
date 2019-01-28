package com.ambition.mobile.web; 
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

import com.ambition.mobile.service.MobileInterfaceManager;
import com.ambition.util.common.DateUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 类名:临时提供给手机APP的接口
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2016-10-13 发布
 */
@Namespace("/mobile")
@ParentPackage("default")
@Results( { 
		@Result(name = CrudActionSupport.RELOAD, location = "/mobile", type = "redirectAction")
	})
public class InterfaceAction extends com.ambition.product.base.CrudActionSupport<Object> {
	public static final String MOBILEINPUT = "mobile-input";
	private Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	@Autowired
	private MobileInterfaceManager mobileInterfaceManager;
	private Page<Object> page;
    
	/**
	 * 根据登录名和查询参数查询待办事务
	 * @return
	 * @throws Exception
	 */
	@Action("query-product-tasks")
	public String queryProductTasks() throws Exception {
		JSONObject result = null;
		try {
			String loginName = Struts2Utils.getParameter("loginName");
			String completeFlag = Struts2Utils.getParameter("completeFlag");
			String searchStr = Struts2Utils.getParameter("searchStr");
			result = mobileInterfaceManager.queryProductTasks(page, loginName, completeFlag, searchStr);
		} catch (Exception e) {
			log.error("根据登录名和查询参数查询待办事项失败!",e);
			result = new JSONObject();
			result.put("error",true);
			result.put("message","查询失败!");
			if(e instanceof AmbFrameException){
				result.put("message","查询失败!" + e.getMessage());
			}
		}
		renderText(result.toString());
		return null;
	}
	@Action("query-mobile-announcement")
	public String queryAnnouncement() throws Exception {
		Map<String, Object> valueMap = new HashMap<String, Object>();
		try {
			valueMap = mobileInterfaceManager.queryAnnouncement();
			valueMap.put("territorial", "欧菲光");
			valueMap.put("contentClassification", "内部");
			valueMap.put("title", "通知");
			valueMap.put("content", "上班时间调整");
			valueMap.put("contentHtml", "上班时间调整");
			valueMap.put("attachFile", "附件");
			valueMap.put("publishOrganization", "欧菲光");
			valueMap.put("publisher", "system");
			valueMap.put("releaseTime", new Date());
			valueMap.put("sendMail", "system");
			valueMap.put("isRelease", "0");
			valueMap.put("topFlag", 0L);
			valueMap.put("releaseStatus", "发布");
			renderText(JSONObject.fromObject(valueMap).toString());
		} catch (Exception e) {
			log.error("公告查询失败!",e);
			createErrorMessage("公告查询失败:" + e.getMessage());
		}
		return null;
	}
	public Page<Object> getPage() {
		return page;
	}
	public void setPage(Page<Object> page) {
		this.page = page;
	}
}
