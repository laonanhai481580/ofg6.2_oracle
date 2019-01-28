package com.ambition.aftersales.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 类名:LAR数据子表
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月23日 发布
 */
@Entity
@Table(name = "AFS_LAR_ITEM")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class LarItem extends IdEntity {

	private static final long serialVersionUID = 1L;
	private Integer yearInt;//年
	private Integer monthInt;//月
	private Date larDate;//日期
	private String customer;//客户
	private String customerCode;//客户编码
	private String customerModel;//客户端机种
	private String ofilmModel;//欧菲机种
	private String materialNo;//料号
	private Integer inputAmount;//入料数
	private Integer inspectionAmount;//抽检数
	private Integer badAmount;//不良数
	private Integer qualifiedAmount;//合格数
	private String qualifiedRate;//合格率
	private String unQualifiedRate;//不良率
	@ManyToOne
	@JoinColumn(name = "FK_LAR_DATA_ID")
	@JsonIgnore()
	private LarData larData;
	@OneToMany(mappedBy = "larItem",cascade={CascadeType.ALL})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JsonIgnore
	List<LarDefectiveItem> larDefectiveItems;//不良明细
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
	public LarData getLarData() {
		return larData;
	}
	public void setLarData(LarData larData) {
		this.larData = larData;
	}
	public List<LarDefectiveItem> getLarDefectiveItems() {
		return larDefectiveItems;
	}
	public void setLarDefectiveItems(List<LarDefectiveItem> larDefectiveItems) {
		this.larDefectiveItems = larDefectiveItems;
	}

	public Integer getInputAmount() {
		return inputAmount;
	}
	public void setInputAmount(Integer inputAmount) {
		this.inputAmount = inputAmount;
	}
	public Integer getBadAmount() {
		return badAmount;
	}
	public void setBadAmount(Integer badAmount) {
		this.badAmount = badAmount;
	}
	public Integer getQualifiedAmount() {
		return qualifiedAmount;
	}
	public void setQualifiedAmount(Integer qualifiedAmount) {
		this.qualifiedAmount = qualifiedAmount;
	}
	public String getQualifiedRate() {
		return qualifiedRate;
	}
	public void setQualifiedRate(String qualifiedRate) {
		this.qualifiedRate = qualifiedRate;
	}
	public String getUnQualifiedRate() {
		return unQualifiedRate;
	}
	public void setUnQualifiedRate(String unQualifiedRate) {
		this.unQualifiedRate = unQualifiedRate;
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
	public String getMaterialNo() {
		return materialNo;
	}
	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}
	public Integer getInspectionAmount() {
		return inspectionAmount;
	}
	public void setInspectionAmount(Integer inspectionAmount) {
		this.inspectionAmount = inspectionAmount;
	}



	
	
	
	
}
