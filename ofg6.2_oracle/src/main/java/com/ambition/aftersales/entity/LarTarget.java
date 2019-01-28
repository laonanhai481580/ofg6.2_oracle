package com.ambition.aftersales.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * 类名:Lar目标值维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月21日 发布
 */
@Entity
@Table(name = "AFS_LAR_TARGET")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class LarTarget extends IdEntity{

	private static final long serialVersionUID = 1L;
	private Integer yearFen;//年份
	//private String businessUnit;//事业部
	private String customer;//客户
	private	Float target1;//一月目标值
	private	Float target2;//二月目标值
	private	Float target3;//三月目标值
	private	Float target4;//四月目标值
	private	Float target5;//五月目标值
	private	Float target6;//六月目标值
	private	Float target7;//七月目标值
	private	Float target8;//八月目标值
	private	Float target9;//九月目标值
	private	Float target10;//十月目标值
	private	Float target11;//十一月目标值
	private	Float target12;//十二月目标值
	public Integer getYearFen() {
		return yearFen;
	}
	public void setYearFen(Integer yearFen) {
		this.yearFen = yearFen;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public Float getTarget1() {
		return target1;
	}
	public void setTarget1(Float target1) {
		this.target1 = target1;
	}
	public Float getTarget2() {
		return target2;
	}
	public void setTarget2(Float target2) {
		this.target2 = target2;
	}
	public Float getTarget3() {
		return target3;
	}
	public void setTarget3(Float target3) {
		this.target3 = target3;
	}
	public Float getTarget4() {
		return target4;
	}
	public void setTarget4(Float target4) {
		this.target4 = target4;
	}
	public Float getTarget5() {
		return target5;
	}
	public void setTarget5(Float target5) {
		this.target5 = target5;
	}
	public Float getTarget6() {
		return target6;
	}
	public void setTarget6(Float target6) {
		this.target6 = target6;
	}
	public Float getTarget7() {
		return target7;
	}
	public void setTarget7(Float target7) {
		this.target7 = target7;
	}
	public Float getTarget8() {
		return target8;
	}
	public void setTarget8(Float target8) {
		this.target8 = target8;
	}
	public Float getTarget9() {
		return target9;
	}
	public void setTarget9(Float target9) {
		this.target9 = target9;
	}
	public Float getTarget10() {
		return target10;
	}
	public void setTarget10(Float target10) {
		this.target10 = target10;
	}
	public Float getTarget11() {
		return target11;
	}
	public void setTarget11(Float target11) {
		this.target11 = target11;
	}
	public Float getTarget12() {
		return target12;
	}
	public void setTarget12(Float target12) {
		this.target12 = target12;
	}
	

	
	
	
}
