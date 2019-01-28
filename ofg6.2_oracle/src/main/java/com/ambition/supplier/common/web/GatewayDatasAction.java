package com.ambition.supplier.common.web;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import com.ambition.carmfg.entity.DefectionCode;
import com.ambition.product.BaseAction;
import com.ambition.util.useFile.service.UseFileManager;

import com.norteksoft.portal.service.UserCurrentLanguageManager;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionContext;
/**
 * 类名:门户加载信息
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  吴旭明
 * @version 1.00 2016年12月26日 发布
 */
@Namespace("/supplier/common/gateway")
@ParentPackage("default")
public class GatewayDatasAction extends BaseAction<DefectionCode>{
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	private String lanague;
	private String url;

	@Autowired
	private UseFileManager useFileManager;
	@Autowired
	private UserCurrentLanguageManager userCurrentLanguageManager;
	
	
	
	@Action("organization-change-lanague")
	public String changeLanague() throws Exception {
		if(StringUtils.isNotEmpty(url)){
			if(url.endsWith("#")) url = url.replace("#", "");
			if(!url.contains("_r=1")){
				if(url.contains("?")){
					//交换_r和第二个参数的位置，否则会报脚本错误，暂时没有找到原因
					if(url.contains("&")){
						String resultUrl = url.substring(0,url.indexOf("&"));
						resultUrl = resultUrl + "&_r=1";
						url = resultUrl+url.substring(url.indexOf("&")+1);
					}else{
						url = url + "&_r=1";
					}
				}else{
					url = url + "?_r=1";
				}
			}
		}
		
		//String callback=Struts2Utils.getParameter("callback");//跨域请求需用此参数
		Locale currentLocale = Locale.getDefault();
		// 1、根据页面请求，创建不同的Locale对象
		if(StringUtils.isNotEmpty(lanague)){
			if ("en_US".equals(lanague.trim())) {
				currentLocale = new Locale("en", "US");
			} else if ("zh_CN".equals(lanague.trim())) {
				currentLocale = new Locale("zh", "CN");
			}
			//保存/更新用户设置的语言
			setUserCurrentLanguage(currentLocale);
		}
		/*
		 * 2、设置Action中的Locale 前台页面的Locale和后台session中的Locale范围是不一样的
		 * a)只改页面Locale当前页面信息会改变但提交后Locale又会改回到默认的
		 * b)改变了后台Locale，当前线程中的页面Locale并不会改变，但会随下一次提交
		 * Action一同改变，所以可能要刷新页面两次，第一次只变后台Locale，第二次 前台和后台同时改变
		 * 为避免上述情况，需要前台和后台的Locale一起改变
		 */
		ActionContext.getContext().setLocale(currentLocale);
		ServletActionContext.getRequest().getSession().setAttribute("WW_TRANS_I18N_LOCALE", currentLocale);
		if(StringUtils.isNotEmpty(url)){
			Struts2Utils.getResponse().sendRedirect(url);
		}else{
			renderText("{}");
		}
		//this.renderText(callback+"({data:'"+lanague+"'})");
		return null;
	}
	/**
	 * 保存/更新用户设置的语言
	 * @param currentLocale
	 */
	private void setUserCurrentLanguage(Locale currentLocale) {
//		UserCurrentLanguage userLanguage = userCurrentLanguageManager
//				.getUserLanguageByUid(ContextUtils.getUserId(), ContextUtils.getCompanyId());
//		if (null == userLanguage) {
//			userLanguage = new UserCurrentLanguage();
//			userLanguage.setUserId(ContextUtils.getUserId());
//		}
//		userLanguage.setCurrentLanguage(currentLocale.toString());
//		userCurrentLanguageManager.save(userLanguage);
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLanague() {
		return lanague;
	}
	public void setLanague(String lanague) {
		this.lanague = lanague;
	}
}
