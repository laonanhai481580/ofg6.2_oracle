package com.ambition.supplier.supplieryearinspect.services;


import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.supplier.entity.SupplierYearInspect;
import com.ambition.supplier.entity.SupplierYearInspectPlan;
import com.ambition.supplier.supplieryearinspect.dao.SupplierYearInspectDao;
import com.ambition.supplier.supplieryearinspect.dao.SupplierYearInspectPlanDao;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.engine.client.AfterTaskCompleted;
@Service
public class SupplierYearInspectZapisaManager implements AfterTaskCompleted {
	@Autowired
	private SupplierYearInspectDao supplierYearInspectDao;
	@Autowired
	private SupplierYearInspectPlanDao supplierYearInspectPlanDao;
	@Override
	public void execute(Long arg0, TaskProcessingResult transact) {
				SupplierYearInspect report = supplierYearInspectDao.get(arg0);
				if(report.getInspectId()==null||"".equals(report.getInspectId())){
					return;					
				}
				SupplierYearInspectPlan supplierYearInspectPlan = supplierYearInspectPlanDao.get(Long.valueOf(report.getInspectId()));				
				supplierYearInspectPlan.setFirstCheckDesignDate(report.getFirstCheckDesignDate());//首次实际日期
				supplierYearInspectPlan.setSecondCheckDesignDate(report.getSecondCheckDesignDate());//二次实际日期
				supplierYearInspectPlan.setFinalCheckResult(report.getFinalCheckResult());//最终稽核结果
				supplierYearInspectPlan.setNcrDate(report.getNcrDate());//不符合项回复时间
				supplierYearInspectPlan.setFirstPerson(report.getFirstPerson());//首次人
				supplierYearInspectPlan.setSecondPerson(report.getSecondPerson());//二次人
				supplierYearInspectPlan.setAuditorPerson(report.getAuditorPerson());//审核人
				supplierYearInspectPlan.setAuditorState("稽核完");
				int month =0;
				Calendar now = Calendar.getInstance();
				month=now.get(Calendar.MONTH)+1;
				switch (month) {
				case 1:
					supplierYearInspectPlan.setDo1("D");
					break;
				case 2:
					supplierYearInspectPlan.setDo2("D");
					break;
				case 3:
					supplierYearInspectPlan.setDo3("D");
					break;
				case 4:
					supplierYearInspectPlan.setDo4("D");
					break;
				case 5:
					supplierYearInspectPlan.setDo5("D");
					break;
				case 6:
					supplierYearInspectPlan.setDo6("D");
					break;
				case 7:
					supplierYearInspectPlan.setDo7("D");
					break;
				case 8:
					supplierYearInspectPlan.setDo8("D");
					break;
				case 9:
					supplierYearInspectPlan.setDo9("D");
					break;
				case 10:
					supplierYearInspectPlan.setDo10("D");
					break;
				case 11:
					supplierYearInspectPlan.setDo11("D");
					break;
				case 12:
					supplierYearInspectPlan.setDo12("D");
					break;
				default:
					
					break;
				}
				supplierYearInspectDao.getSession().save(supplierYearInspectPlan);
			}
	
}
