package com.ambition.sys.web;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.product.base.CrudActionSupport;
import com.norteksoft.mms.module.entity.Menu;
import com.norteksoft.mms.module.service.MenuManager;
import com.opensymphony.xwork2.ActionContext;

/**
 * @ClassName: IndexAction 
 * @Description: TODO(作用) 
 * @Author WangShaolin 
 * @Date 2017年11月6日 上午11:31:49
 */
@Namespace("/sys")
@ParentPackage("default")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "/sys", type = "redirectAction") })
public class IndexAction extends CrudActionSupport<Menu> {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private MenuManager menuManager;
	
	@Action("index")
	public String index() throws Exception {
		// 获取有权限一级菜单
		List<Long> subsciberSystemIds = menuManager.getSubsciberSystemIds();
		List<Menu> menus = menuManager.getHasAuthFirstMenus(subsciberSystemIds);
		// 转换国际化一级菜单
		menus = menuManager.toi18nMenu(menus); 
		ActionContext.getContext().put("_menus", menus);
		return SUCCESS;
	}

}
