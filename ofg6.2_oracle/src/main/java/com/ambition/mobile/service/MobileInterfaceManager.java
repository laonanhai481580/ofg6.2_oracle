package com.ambition.mobile.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.carmfg.bom.dao.ProductBomDao;
import com.ambition.mobile.entity.MobileAnnouncement;
import com.ambition.util.common.DateUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.wf.engine.client.WorkflowInfo;

/**
 * 类名:手机接口业务类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2016-10-13 发布
 */
@Service
public class MobileInterfaceManager{
	@Autowired
	private ProductBomDao productBomDao;
	
	/**
	 * 查询待办事宜
	 * @param page
	 * @param 登录名
	 * @param completeFlag 是否完成的标识 1,表示已完成,否则未完成
	 * @param paramStr 查询参数
	 * @return
	 */
	public JSONObject queryProductTasks(Page<Object> page,String loginName,String completeFlag,String paramStr){
		if(page==null){
			throw new AmbFrameException("分页条件不能为空!");
		}
		List<Object> searchParams = new ArrayList<Object>();
		String orderBySql = "";
		String sql = "from product_task t";
		if("1".equals(completeFlag)){
			sql += " where t.transact_date is not null";
			orderBySql = " order by t.transact_date desc";
		}else{
			sql += " where t.transact_date is null";
			orderBySql = " order by t.created_time desc";
		}
		sql += " and transactor = ? ";
		searchParams.add(loginName);
		if(StringUtils.isNotEmpty(paramStr)){
			sql += " and (t.title like ? or t.name like ? or t.group_name like ?)";
			searchParams.add("%" + paramStr.trim() + "%");
			searchParams.add("%" + paramStr.trim() + "%");
			searchParams.add("%" + paramStr.trim() + "%");
		}
		//计算总数
		Query query = productBomDao.getSession().createSQLQuery("select count(*) " + sql);
		for(int i=0;i<searchParams.size();i++){
			query.setParameter(i,searchParams.get(i));
		}
		long totalRecords = Long.valueOf(query.list().get(0).toString());
		JSONObject resultPage = new JSONObject();
		resultPage.put("totalRecords", totalRecords);
		resultPage.put("page",page.getPageNo());
		long totalPages = totalRecords / page.getPageSize();
		if(totalRecords%page.getPageSize()>0){
			totalPages++;
		}
		resultPage.put("totalPages",totalPages);
		
		//分页查询
		String selectSql = "select id,created_time,CREATOR,CREATOR_NAME,group_name,name," +
				"title,url,is_read,transact_date " + sql + orderBySql;
		query = productBomDao.getSession().createSQLQuery(selectSql);
		for(int i=0;i<searchParams.size();i++){
			query.setParameter(i,searchParams.get(i));
		}
		query.setFirstResult(page.getFirst()-1);
		query.setMaxResults(page.getPageSize());
		List<?> list = query.list();
		JSONArray rows = new JSONArray();
		for(Object obj : list){
			Object objs[] = (Object[])obj;
			JSONObject json = new JSONObject();
			json.put("ID",objs[0]);
			json.put("CREATED_TIME",DateUtil.formateDateStr((Date)objs[1],"yyyy-MM-dd HH:mm"));//创建时间
			json.put("CREATOR",objs[2]);//创建人登录名
			json.put("CREATOR_NAME",objs[3]);//创建人用户名
			json.put("GROUP_NAME",objs[4]);//分组名称
			json.put("NAME",objs[5]);//流程名称
			json.put("TITLE",objs[6]);//流程标题
			String url = PropUtils.getProp("host.app") + "/" + objs[7] + objs[0];
			json.put("URL",url);//办理地址
			json.put("VISIBLE",objs[8]);//是否已阅
			json.put("TRANSACT_DATE",DateUtil.formateDateStr((Date)objs[9],"yyyy-MM-dd HH:mm"));//办理日期
			rows.add(json);
		}
		resultPage.put("rows",rows);
		return resultPage;
	}
	
	/**
	 * 查询报告未办理的任务
	 * @param workflowInfo
	 * @return list<map>
	 * title 标题
	 * taksId 任务ID
	 * transactorName 办理用户名
	 * transactor 办理用户登录名
	 * telephone 电话
	 * 
	 */
	public List<Map<String,String>> queryActiveTasks(WorkflowInfo workflowInfo){
		String sql = "select t.name title,t.id,u.name,u.login_name,ui.telephone from product_task t";
		sql += " inner join workflow_task w on t.id = w.id";
		sql += " inner join acs_user u on t.transactor = u.login_name";
		sql +=" inner join acs_userinfo ui on u.id = ui.fk_user_id";
		sql += "  where t.transact_date is null and w.process_instance_id = ? ";
		List<?> list = productBomDao.getSession().createSQLQuery(sql).setParameter(0,workflowInfo.getWorkflowId()).list();
		List<Map<String,String>> results = new ArrayList<Map<String,String>>();
		for(Object obj : list){
			Object objs[] = (Object[])obj;
			Map<String,String> map = new HashMap<String, String>();
			map.put("title",objs[0]+"");
			map.put("taskId",objs[1]+"");
			map.put("transactorName",objs[2]+"");
			map.put("transactor",objs[3]+"");
			map.put("telephone",objs[4]==null?"":objs[4].toString());
			results.add(map);
		}
		return results;
	}
	/**
	 * 查询公告
	 * 
	 * 
	 */
	public Map<String, Object>  queryAnnouncement(){
		String sql = "from MobileAnnouncement t";
		Map<String, Object> valueMap = new HashMap<String, Object>();
		@SuppressWarnings("unchecked")
		List<MobileAnnouncement> list = productBomDao.getSession().createQuery(sql).list();
		for(MobileAnnouncement mobileAnnouncement : list){
			valueMap.put("territorial", mobileAnnouncement.getTerritorial());
			valueMap.put("contentClassification", mobileAnnouncement.getContentClassification());
			valueMap.put("title", mobileAnnouncement.getTitle());
			valueMap.put("content", mobileAnnouncement.getContent());
			valueMap.put("contentHtml", mobileAnnouncement.getContentHtml());
			valueMap.put("attachFile", mobileAnnouncement.getAttachFile());
			valueMap.put("publishOrganization", mobileAnnouncement.getPublishOrganization());
			valueMap.put("publisher", mobileAnnouncement.getPublisher());
			valueMap.put("releaseTime", mobileAnnouncement.getReleaseTime());
			valueMap.put("sendMail", mobileAnnouncement.getSendMail());
			valueMap.put("isRelease",mobileAnnouncement.getIsRelease());
			valueMap.put("topFlag", mobileAnnouncement.getTopFlag());
			valueMap.put("releaseStatus", mobileAnnouncement.getReleaseStatus());
		}
		return valueMap;
	}
}
