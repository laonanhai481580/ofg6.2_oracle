package com.ambition.carmfg.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * 
 * 类名:OQC工厂维护
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 20167年11月9日 发布
 */
@Entity
@Table(name = "MFG_OQC_FACTORY")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class OqcFactory extends IdEntity{

	private static final long serialVersionUID = 1L;
	private String factory; // 

	@OneToMany(mappedBy="oqcFactory")
	@OrderBy("procedure")
	List<OqcProcedure> oqcProcedures;//不良代码

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public List<OqcProcedure> getOqcProcedures() {
		return oqcProcedures;
	}

	public void setOqcProcedures(List<OqcProcedure> oqcProcedures) {
		this.oqcProcedures = oqcProcedures;
	}
	
	
}
