package com.ambition.gsm.newEquipment.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.gsm.entity.GsmEquipment;
import com.ambition.gsm.entity.NewEquipmentRegister;
import com.ambition.gsm.entity.NewEquipmentSublist;
import com.ambition.gsm.newEquipment.dao.NewEquipmentRegisterDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.engine.client.AfterTaskCompleted;
@Service
public class NewEquipmentRegisterConsentManager implements AfterTaskCompleted {
	@Autowired
	private NewEquipmentRegisterDao newEquipmentRegisterDao;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Override
	public void execute(Long arg0, TaskProcessingResult transact) {
		// TODO Auto-generated method stub
		NewEquipmentRegister report = newEquipmentRegisterDao.get(arg0);
		List<NewEquipmentSublist> newEquipmentSublists = report.getNewEquipmentSublists();
		for(int i=0;newEquipmentSublists.size()>i;i++){
			NewEquipmentSublist newEquipmentSublist = newEquipmentSublists.get(i);
			if(transact.toString().equals("submit")){
				GsmEquipment gsmEquipment = new GsmEquipment();
				gsmEquipment.setCompanyId(ContextUtils.getCompanyId());//公司ID
				gsmEquipment.setCreatedTime(new Date());
				gsmEquipment.setCreator(ContextUtils.getLoginName());
				gsmEquipment.setCreatorName(ContextUtils.getUserName());
				gsmEquipment.setModifiedTime(new Date());
				gsmEquipment.setModifier(ContextUtils.getUserName());
				
				gsmEquipment.setEquipmentName(newEquipmentSublist.getDeviceName());
				gsmEquipment.setEquipmentModel(newEquipmentSublist.getModelSpecification());
				gsmEquipment.setManufacturer(newEquipmentSublist.getManufacturer());
				gsmEquipment.setFactoryNumber(newEquipmentSublist.getFactoryNumber());
				gsmEquipment.setManagerAssets(newEquipmentSublist.getNstrumentNumber());
				
				newEquipmentRegisterDao.getSession().save(gsmEquipment);
			}
		}
	}
	
}
