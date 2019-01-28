package com.ambition.aftersales.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * 类名:VLRR机种+目标值维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月21日 发布
 */
@Entity
@Table(name = "AFS_VLRR_WARMING")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class VlrrWarming extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String model;//机种
	private String targetValue;//目标值
	private String warmingMan;//通知人员
	private String warmingManLogin;//通知人员登录名
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getTargetValue() {
		return targetValue;
	}
	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}
	public String getWarmingMan() {
		return warmingMan;
	}
	public void setWarmingMan(String warmingMan) {
		this.warmingMan = warmingMan;
	}
	public String getWarmingManLogin() {
		return warmingManLogin;
	}
	public void setWarmingManLogin(String warmingManLogin) {
		this.warmingManLogin = warmingManLogin;
	}
	
	
}
