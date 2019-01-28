package com.ambition.iqc.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 厂区-组织代码维护
 * @author ambition-lpf
 *
 */
@Entity
@Table(name = "IQC_FACTORY_CODE")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class IqcFactoryCode  extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String factory; //厂区
    private String organizationCode; //组织代码
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getOrganizationCode() {
		return organizationCode;
	}
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}
   
	
}
