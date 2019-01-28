package com.ambition.supplier.approval.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.gp.entity.GpMaterial;
import com.ambition.gp.entity.GpMaterialSub;
import com.ambition.gp.gpmaterial.services.GpMaterialManager;
import com.ambition.supplier.approval.dao.SupplierApprovalDao;
import com.ambition.supplier.entity.SupplierApproval;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.engine.client.AfterTaskCompleted;

@Service
public class SupplierApprovalConsentManager implements AfterTaskCompleted {
	@Autowired
	private SupplierApprovalDao supplierApprovalDao;
	@Autowired
	private GpMaterialManager gpMaterialManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Override
	public void execute(Long arg0, TaskProcessingResult transact) {
		// TODO Auto-generated method stub
		SupplierApproval supplierApproval = supplierApprovalDao.get(arg0);
		String gpFormNo=gpMaterialManager.findGpMaterialAdmitApprove(supplierApproval.getId());
		GpMaterial gpMaterial=null;
//		GpMaterial gpMaterial=gpMaterialManager.searchByApproveId(supplierApproval.getId());
			if(gpFormNo!=null){
				gpMaterial =gpMaterialManager.findReportByFormNo(gpFormNo);
				gpMaterial.setApprovalId(supplierApproval.getId().toString());
				gpMaterial.setApprovalNo(supplierApproval.getFormNo());
				
				supplierApproval.setGpMaterialNo(gpFormNo);
				supplierApproval.setGpMaterialState(gpMaterial.getTaskProgress());
				supplierApprovalDao.getSession().save(gpMaterial);
				supplierApprovalDao.getSession().save(supplierApproval);
			}else{
				gpMaterial = new GpMaterial();
				gpMaterial.setCompanyId(ContextUtils.getCompanyId());//公司ID
				gpMaterial.setCompleteDate(new Date());
				gpMaterial.setCreator(ContextUtils.getLoginName());
				gpMaterial.setCreatorName(ContextUtils.getUserName());
				gpMaterial.setModifiedTime(new Date());
				gpMaterial.setModifier(ContextUtils.getUserName());
				gpMaterial.setFormNo(formCodeGenerated.generateGpMaterialNo());//表单单号
				
				gpMaterial.setSupplierName(supplierApproval.getSupplierName());
				gpMaterial.setSupplierCode(supplierApproval.getSupplierCode());
				gpMaterial.setSupplierLoginName(supplierApproval.getSupplierLoginName());
				gpMaterial.setSupplierEmail(supplierApproval.getSupplierEmail());
				gpMaterial.setMaterialName(supplierApproval.getMaterialName());
				gpMaterial.setMaterialCode(supplierApproval.getMaterialCode());
				gpMaterial.setApprovalId(supplierApproval.getId().toString());
				gpMaterial.setApprovalNo(supplierApproval.getFormNo());
				gpMaterial.setApprovalSource("SupplierApproval");
				gpMaterial.setInitiator(supplierApproval.getPurchaseProcesser());
				gpMaterial.setInitiatorLogin(supplierApproval.getPurchaseProcessLog());
				gpMaterial.setConfirmDept(supplierApproval.getQsChecker());
				gpMaterial.setConfirmDeptLoing(supplierApproval.getQsCheckerLog());
				gpMaterial.setSupplierDate(new Date());
				gpMaterial.setTaskProgress("待提交");
				List<GpMaterialSub> gpMaterialSubs= gpMaterial.getGpMaterialSubs();
				if(gpMaterialSubs == null){
					gpMaterialSubs = new ArrayList<GpMaterialSub>();
					GpMaterialSub item = new GpMaterialSub();
					gpMaterialSubs.add(item);
				}
//				gpMaterial.setSupplierDate(new Date());
//				String params = supplierApproval.getQsChecker();
//				try {
//					gpMaterialManager.releaseEmail(params,gpMaterial.getFormNo());
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				supplierApprovalDao.getSession().save(gpMaterial);
				supplierApproval.setGpMaterialNo(gpMaterial.getFormNo());
				supplierApproval.setGpMaterialId(gpMaterial.getId().toString());
				supplierApproval.setGpMaterialState("待提交");
				supplierApprovalDao.getSession().save(supplierApproval);
			}
	}
	

}
