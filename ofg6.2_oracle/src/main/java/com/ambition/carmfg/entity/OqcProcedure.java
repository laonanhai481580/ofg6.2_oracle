package com.ambition.carmfg.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * 类名:OQC工序维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月8日 发布
 */
@Entity
@Table(name = "MFG_OQC_PROCEDURE")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class OqcProcedure extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String procedure;//工序
	@ManyToOne
	@JoinColumn(name = "MFG_OQC_FACTORY_NO")
	@JsonIgnore
	private OqcFactory oqcFactory;//工厂
	public String getProcedure() {
		return procedure;
	}
	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}
	public OqcFactory getOqcFactory() {
		return oqcFactory;
	}
	public void setOqcFactory(OqcFactory oqcFactory) {
		this.oqcFactory = oqcFactory;
	}

	
	
}
