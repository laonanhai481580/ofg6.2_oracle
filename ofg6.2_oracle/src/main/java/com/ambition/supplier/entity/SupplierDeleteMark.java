package com.ambition.supplier.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**    
 * 删除标记（用于记录供应商新需求其他事业群删除的单子）
 * @authorBy LPF
 *
 */
@Entity
@Table(name = "SUPPLIER_DELETE_MARK")
public class SupplierDeleteMark {
	public static final String SUPPLIER_EXCEPTION_CONTACT = "ExceptionContact";
	public static final String SUPPLIER_EXCEPTION_IMPROVE = "ExceptionImprove";
	public static final String SUPPLIER_EVALUATE_TOTAL_VIEW = "SupplierEvaluateTotalView";//供应商评价汇总表
	public static final String SUPPLIER_YEAR_INSPECT_ALL = "SupplierYearInspectAll";
	public static final String SUPPLIER_YEAR_INSPECT_PLAN_ALL = "SupplierYearInspectPlanAll";
	public static final String SUPPLIER_CHANGE = "SupplierChange";
	public static final String SUPPLIER_ABNORMAL_ALL = "SupplierAbnormalAll";
	public static final String SUPPLIER_SUPPLIER_ADMIT_ALL = "SupplierAdmitAll";
	public static final String SUPPLIER_FILE = "SupplierFile";
	public static final String SUPPLIER_ALL = "SupplierAll";
	@Id
	@GenericGenerator(name = "IdGenerator", strategy = "native")
	@GeneratedValue(generator = "IdGenerator")
	private Long id;
	private  Long sourceId;//集成过来原ID
	private String sourceUnit;//集成的事业部
	private String sourceTable;//集成的表
	public Long getSourceId() {
		return sourceId;
	}
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}
	public String getSourceUnit() {
		return sourceUnit;
	}
	public void setSourceUnit(String sourceUnit) {
		this.sourceUnit = sourceUnit;
	}
	public String getSourceTable() {
		return sourceTable;
	}
	public void setSourceTable(String sourceTable) {
		this.sourceTable = sourceTable;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
}
