package com.ambition.gsm.inspectionplan.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.gsm.entity.GsmInnerCheckReport;
import com.ambition.gsm.equipment.dao.GsmEquipmentDao;
import com.ambition.gsm.inspectionplan.dao.GsmInnerCheckReportDao;
import com.norteksoft.product.util.AsyncMailUtils;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.engine.client.AfterTaskCompleted;
@Service
public class GsmReportMailManager implements AfterTaskCompleted{
	
	@Autowired
	private GsmInnerCheckReportDao gsmInnerCheckReportDao;
	@Autowired
	private GsmEquipmentDao gsmEquipmentDao; 
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Long dataId, TaskProcessingResult transact) {
		// TODO Auto-generated method stub
		GsmInnerCheckReport report= gsmInnerCheckReportDao.get(dataId);
		if(transact.toString().equals("approve")){
			String message = "admin:"+report.getManagementNo()+" 仪器名称:"+report.getMeasurementName()+" 校验合格，请放心使用，谢谢！"+PropUtils.getProp("gsmEmailDress2");
			String SQL="select email from acs_user where login_name = ? ";
			Query query = null;
			Session session = gsmEquipmentDao.getSessionFactory().openSession();
			
			String dutyManEmails = report.getDutyLoginMan();
			String copyManEmails = report.getCopyLoginMan();
			String theme = report.getManagementNo()+" "+report.getMeasurementName()+" 校验合格，请放心使用，谢谢！";
			try {
				if(dutyManEmails!=null&&!"".equals(dutyManEmails)){
					query = session.createSQLQuery(SQL).setParameter(0,dutyManEmails);
					List<Object> emails = query.list();
					String dutyManEmail = emails.get(0).toString();
					if(dutyManEmail!=null){
						AsyncMailUtils.sendMail(dutyManEmail,theme,message);
					}
				}
				if(copyManEmails!=null&&!"".equals(copyManEmails)){
					String a[]=copyManEmails.split(",");
					String copyManEmail=null;
					for (int i = 0; i < a.length; i++) {
						String userMan=a[i].toString();
						query = session.createSQLQuery(SQL).setParameter(0,userMan);
						List<Object> emails = query.list();
						copyManEmail=emails.get(0).toString();
						if(copyManEmail!=null){
							AsyncMailUtils.sendMail(copyManEmail,theme,message);
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
	}

}
