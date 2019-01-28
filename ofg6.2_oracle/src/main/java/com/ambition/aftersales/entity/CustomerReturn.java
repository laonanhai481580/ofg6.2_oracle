package com.ambition.aftersales.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * 类名:客退率台账
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2018年4月12日 发布
 */
@Entity
@Table(name = "AFS_CUSTOMER_RETURN")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class CustomerReturn extends IdEntity{

	private static final long serialVersionUID = 1L;
	private Integer yearInt;//年
	private Integer monthInt;//月
	private Date returnDate;//日期
	private String customer;//客户
	private String customerCode;//客户编码
	private String ofilmModel;//欧菲机种
	private Integer deliverCount;//出货数
	private Integer returnCount;//退货数
	private String returnRate;//客退率
	private String targetRate;//目标值
	private String isOver="否";//是否超标
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
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
	public String getOfilmModel() {
		return ofilmModel;
	}
	public void setOfilmModel(String ofilmModel) {
		this.ofilmModel = ofilmModel;
	}
	public Integer getReturnCount() {
		return returnCount;
	}
	public void setReturnCount(Integer returnCount) {
		this.returnCount = returnCount;
	}
	public String getReturnRate() {
		return returnRate;
	}
	public void setReturnRate(String returnRate) {
		this.returnRate = returnRate;
	}
	public Integer getDeliverCount() {
		return deliverCount;
	}
	public void setDeliverCount(Integer deliverCount) {
		this.deliverCount = deliverCount;
	}
	public Integer getYearInt() {
		return yearInt;
	}
	public void setYearInt(Integer yearInt) {
		this.yearInt = yearInt;
	}
	public Integer getMonthInt() {
		return monthInt;
	}
	public void setMonthInt(Integer monthInt) {
		this.monthInt = monthInt;
	}
	public String getTargetRate() {
		return targetRate;
	}
	public void setTargetRate(String targetRate) {
		this.targetRate = targetRate;
	}
	public String getIsOver() {
		return isOver;
	}
	public void setIsOver(String isOver) {
		this.isOver = isOver;
	}	
	
	
}
