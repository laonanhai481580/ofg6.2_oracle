package com.ambition.aftersales.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * 类名:重要客户评价台账
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2018年4月12日 发布
 */
@Entity
@Table(name = "AFS_CUSTOMER_EVALUATION")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class CustomerEvaluation extends IdEntity{

	private static final long serialVersionUID = 1L;
	private Date noticeDate;//通报日期
	private Integer yearInt;//年
	private String customer;//客户
	private String customerCode;//客户编码
	private String checkItem;//考核项目
	private String weight;//权重
	private String qualityTarget;//质量目标
	private String unit;//单位
	private	Float auctualQ1;//一季度目标值
	private	Float scoreQ1;//一季度评分
	private	Float auctualQ2;//二季度目标值
	private	Float scoreQ2;//二季度评分
	private	Float auctualQ3;//三季度目标值
	private	Float scoreQ3;//三季度评分
	private	Float auctualQ4;//四季度目标值
	private	Float scoreQ4;//四季度评分
	public Date getNoticeDate() {
		return noticeDate;
	}
	public void setNoticeDate(Date noticeDate) {
		this.noticeDate = noticeDate;
	}
	public Integer getYearInt() {
		return yearInt;
	}
	public void setYearInt(Integer yearInt) {
		this.yearInt = yearInt;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getCheckItem() {
		return checkItem;
	}
	public void setCheckItem(String checkItem) {
		this.checkItem = checkItem;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getQualityTarget() {
		return qualityTarget;
	}
	public void setQualityTarget(String qualityTarget) {
		this.qualityTarget = qualityTarget;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Float getAuctualQ1() {
		return auctualQ1;
	}
	public void setAuctualQ1(Float auctualQ1) {
		this.auctualQ1 = auctualQ1;
	}

	public Float getAuctualQ2() {
		return auctualQ2;
	}
	public void setAuctualQ2(Float auctualQ2) {
		this.auctualQ2 = auctualQ2;
	}

	public Float getAuctualQ3() {
		return auctualQ3;
	}
	public void setAuctualQ3(Float auctualQ3) {
		this.auctualQ3 = auctualQ3;
	}

	public Float getAuctualQ4() {
		return auctualQ4;
	}
	public void setAuctualQ4(Float auctualQ4) {
		this.auctualQ4 = auctualQ4;
	}
	public Float getScoreQ1() {
		return scoreQ1;
	}
	public void setScoreQ1(Float scoreQ1) {
		this.scoreQ1 = scoreQ1;
	}
	public Float getScoreQ2() {
		return scoreQ2;
	}
	public void setScoreQ2(Float scoreQ2) {
		this.scoreQ2 = scoreQ2;
	}
	public Float getScoreQ3() {
		return scoreQ3;
	}
	public void setScoreQ3(Float scoreQ3) {
		this.scoreQ3 = scoreQ3;
	}
	public Float getScoreQ4() {
		return scoreQ4;
	}
	public void setScoreQ4(Float scoreQ4) {
		this.scoreQ4 = scoreQ4;
	}

	
	
}
