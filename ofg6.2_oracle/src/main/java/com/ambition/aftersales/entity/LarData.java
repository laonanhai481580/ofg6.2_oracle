package com.ambition.aftersales.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * 类名:LAR批次合格率
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Entity
@Table(name = "AFS_LAR_DATA")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class LarData extends IdEntity{

	private static final long serialVersionUID = 1L;
	private Integer yearInt;//年
	private Integer monthInt;//月
	private Date larDate;//日期
	private String customer;//客户
	private String customerFactory;//客户工厂
	private String customerCode;//客户编码
	private String customerModel;//客户端机种
	private String ofilmModel;//欧菲机种
	private Integer inspectionCount;//检验批数
	private Integer qualifiedCount;//合格批数
	private Integer unQualifiedCount;//不合格批数
	private String qualifiedRate;//合格率
	private String unqualifiedRate;//不良率
	private String unqualifiedDppm;//不良率DPPM
	@OneToMany(mappedBy = "larData",cascade={CascadeType.ALL})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	List<LarItem> larItems;//LAR数据子表
	public Date getLarDate() {
		return larDate;
	}
	public void setLarDate(Date larDate) {
		this.larDate = larDate;
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
	public String getCustomerModel() {
		return customerModel;
	}
	public void setCustomerModel(String customerModel) {
		this.customerModel = customerModel;
	}
	public String getOfilmModel() {
		return ofilmModel;
	}
	public void setOfilmModel(String ofilmModel) {
		this.ofilmModel = ofilmModel;
	}
	public Integer getInspectionCount() {
		return inspectionCount;
	}
	public void setInspectionCount(Integer inspectionCount) {
		this.inspectionCount = inspectionCount;
	}
	public Integer getQualifiedCount() {
		return qualifiedCount;
	}
	public void setQualifiedCount(Integer qualifiedCount) {
		this.qualifiedCount = qualifiedCount;
	}
	public String getQualifiedRate() {
		return qualifiedRate;
	}
	public void setQualifiedRate(String qualifiedRate) {
		this.qualifiedRate = qualifiedRate;
	}
		
	public List<LarItem> getLarItems() {
		return larItems;
	}
	public void setLarItems(List<LarItem> larItems) {
		this.larItems = larItems;
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
	public Integer getUnQualifiedCount() {
		return unQualifiedCount;
	}
	public void setUnQualifiedCount(Integer unQualifiedCount) {
		this.unQualifiedCount = unQualifiedCount;
	}
	public String getUnqualifiedRate() {
		return unqualifiedRate;
	}
	public void setUnqualifiedRate(String unqualifiedRate) {
		this.unqualifiedRate = unqualifiedRate;
	}
	public String getUnqualifiedDppm() {
		return unqualifiedDppm;
	}
	public void setUnqualifiedDppm(String unqualifiedDppm) {
		this.unqualifiedDppm = unqualifiedDppm;
	}
	public String getCustomerFactory() {
		return customerFactory;
	}
	public void setCustomerFactory(String customerFactory) {
		this.customerFactory = customerFactory;
	}
	
	
}
