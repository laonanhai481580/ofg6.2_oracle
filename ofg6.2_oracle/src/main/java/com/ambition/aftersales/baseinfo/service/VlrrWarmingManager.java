package com.ambition.aftersales.baseinfo.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.baseinfo.dao.VlrrWarmingDao;
import com.ambition.aftersales.entity.VlrrData;
import com.ambition.aftersales.entity.VlrrWarming;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.AsyncMailUtils;

/**
 * 
 * 类名:VLRR机种+目标值维护Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月21日 发布
 */
@Service
@Transactional
public class VlrrWarmingManager {
	@Autowired
	private VlrrWarmingDao vlrrWarmingDao;	
	public VlrrWarming getVlrrWarming(Long id){
		return vlrrWarmingDao.get(id);
	}
	
	public void deleteVlrrWarming(VlrrWarming vlrrWarming){
		vlrrWarmingDao.delete(vlrrWarming);
	}

	public Page<VlrrWarming> search(Page<VlrrWarming>page){
		return vlrrWarmingDao.search(page);
	}

	public List<VlrrWarming> listAll(){
		return vlrrWarmingDao.getAllVlrrWarming();
	}
		
	public void deleteVlrrWarming(Long id){
		vlrrWarmingDao.delete(id);
	}
	public String deleteVlrrWarming(String ids) {
		String[] deleteIds = ids.split(",");
		StringBuilder sb = new StringBuilder("");
		for (String id : deleteIds) {
			VlrrWarming  vlrrWarming = vlrrWarmingDao.get(Long.valueOf(id));
			if(vlrrWarming.getId() != null){
				vlrrWarmingDao.delete(vlrrWarming);
				sb.append(vlrrWarmingDao.get(Long.valueOf(id)).getModel() + ",");
			}
		}
		return sb.toString();
	}
	public void saveVlrrWarming(VlrrWarming vlrrWarming){
		vlrrWarmingDao.save(vlrrWarming);
	}

	public VlrrWarming getByModel(String ofilmModel) {
		return vlrrWarmingDao.getByModel(ofilmModel);
	}
	public String saveMailSettings(VlrrData vlrrData,String warnManLogin){
		String message = "";
		Date myDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		String dateStr = sdf.format(myDate);			
		if(StringUtils.isNotEmpty(warnManLogin)){
			//发送邮件
			message = "*"+dateStr+"最新预警信息:"+vlrrData.getBusinessUnitName()+"事业部"+sdf.format(vlrrData.getVlrrDate())+","+vlrrData.getOfilmModel()+"机种VLRR数据不良率超过了目标值!";
			String email = ApiFactory.getAcsService().getUserByLoginName(warnManLogin).getEmail();
			if(StringUtils.isNotEmpty(email)){
				AsyncMailUtils.sendMail(email,"售后VLRR数据异常预警",message);
			}
		}
		return null;
	}
}