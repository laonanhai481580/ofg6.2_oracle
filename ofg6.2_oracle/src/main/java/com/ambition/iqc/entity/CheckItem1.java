package com.ambition.iqc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**    
 * 检查项目项目
 * @authorBy wlongfeng
 *
 */
@Entity
@Table(name="IQC_CHECK_ITEM1")
public class CheckItem1 extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String inspectionLevel;//检验级别
	private String inspectionType;//检验类型,对应检验项目顶级
	private String checkItemName;//检查项目名称
	private String countType;//统计类型
	private String checkMethod;//检查方法
	private Integer inspectionAmount;//检验数量
	@Column(length=1000)
	private String specifications;//规格
	private Double maxlimit;//上限
	private Double minlimit;//下限
	private Integer aqlAc;//接受数
	private Integer aqlRe;//拒收数
	@Column(length=1000)
	private String results;//数据
	private Double standardValue;//规格值
	private String resultsTotal;//计量数据拼接
	private Double minResult;//Min
	private Double maxResult;//Max
	private Double avgResult;//Avg
	private Double result1;//结果1
	private Double result2;//结果2
	private Double result3;//结果3
	private Double result4;//结果4
	private Double result5;//结果5
	private Double result6;//结果6
	private Double result7;//结果7
	private Double result8;//结果8
	private Double result9;//结果9
	private Double result10;//结果10
	private Double result11;//结果11
	private Double result12;//结果12
	private Double result13;//结果13
	private Double result14;//结果14
	private Double result15;//结果15
	private Integer qualifiedAmount;//合格数
	private Integer unqualifiedAmount;//不良数
	private String conclusion;//结论
	@ManyToOne
	@JoinColumn(name="FK_IQC_IIAR")
	private IncomingInspectionActionsReport iiar;//检验报告
	public String getInspectionLevel() {
		return inspectionLevel;
	}
	public void setInspectionLevel(String inspectionLevel) {
		this.inspectionLevel = inspectionLevel;
	}
	public String getInspectionType() {
		return inspectionType;
	}
	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}
	public String getCheckItemName() {
		return checkItemName;
	}
	public void setCheckItemName(String checkItemName) {
		this.checkItemName = checkItemName;
	}
	public String getCountType() {
		return countType;
	}
	public void setCountType(String countType) {
		this.countType = countType;
	}
	public String getCheckMethod() {
		return checkMethod;
	}
	public void setCheckMethod(String checkMethod) {
		this.checkMethod = checkMethod;
	}
	public Integer getInspectionAmount() {
		return inspectionAmount;
	}
	public void setInspectionAmount(Integer inspectionAmount) {
		this.inspectionAmount = inspectionAmount;
	}
	public String getSpecifications() {
		return specifications;
	}
	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}
	public Double getMaxlimit() {
		return maxlimit;
	}
	public void setMaxlimit(Double maxlimit) {
		this.maxlimit = maxlimit;
	}
	public Double getMinlimit() {
		return minlimit;
	}
	public void setMinlimit(Double minlimit) {
		this.minlimit = minlimit;
	}
	public Integer getAqlAc() {
		return aqlAc;
	}
	public void setAqlAc(Integer aqlAc) {
		this.aqlAc = aqlAc;
	}
	public Integer getAqlRe() {
		return aqlRe;
	}
	public void setAqlRe(Integer aqlRe) {
		this.aqlRe = aqlRe;
	}
	public String getResults() {
		return results;
	}
	public void setResults(String results) {
		this.results = results;
	}
	public Double getStandardValue() {
		return standardValue;
	}
	public void setStandardValue(Double standardValue) {
		this.standardValue = standardValue;
	}
	public Double getMinResult() {
		return minResult;
	}
	public void setMinResult(Double minResult) {
		this.minResult = minResult;
	}
	public Double getMaxResult() {
		return maxResult;
	}
	public void setMaxResult(Double maxResult) {
		this.maxResult = maxResult;
	}
	public Double getAvgResult() {
		return avgResult;
	}
	public void setAvgResult(Double avgResult) {
		this.avgResult = avgResult;
	}
	public Double getResult1() {
		return result1;
	}
	public void setResult1(Double result1) {
		this.result1 = result1;
	}
	public Double getResult2() {
		return result2;
	}
	public void setResult2(Double result2) {
		this.result2 = result2;
	}
	public Double getResult3() {
		return result3;
	}
	public void setResult3(Double result3) {
		this.result3 = result3;
	}
	public Double getResult4() {
		return result4;
	}
	public void setResult4(Double result4) {
		this.result4 = result4;
	}
	public Double getResult5() {
		return result5;
	}
	public void setResult5(Double result5) {
		this.result5 = result5;
	}
	public Double getResult6() {
		return result6;
	}
	public void setResult6(Double result6) {
		this.result6 = result6;
	}
	public Double getResult7() {
		return result7;
	}
	public void setResult7(Double result7) {
		this.result7 = result7;
	}
	public Double getResult8() {
		return result8;
	}
	public void setResult8(Double result8) {
		this.result8 = result8;
	}
	public Double getResult9() {
		return result9;
	}
	public void setResult9(Double result9) {
		this.result9 = result9;
	}
	public Double getResult10() {
		return result10;
	}
	public void setResult10(Double result10) {
		this.result10 = result10;
	}
	public Integer getQualifiedAmount() {
		return qualifiedAmount;
	}
	public void setQualifiedAmount(Integer qualifiedAmount) {
		this.qualifiedAmount = qualifiedAmount;
	}
	public Integer getUnqualifiedAmount() {
		return unqualifiedAmount;
	}
	public void setUnqualifiedAmount(Integer unqualifiedAmount) {
		this.unqualifiedAmount = unqualifiedAmount;
	}
	public String getConclusion() {
		return conclusion;
	}
	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}
	public IncomingInspectionActionsReport getIiar() {
		return iiar;
	}
	public void setIiar(IncomingInspectionActionsReport iiar) {
		this.iiar = iiar;
	}
	public String getResultsTotal() {
		return resultsTotal;
	}
	public void setResultsTotal(String resultsTotal) {
		this.resultsTotal = resultsTotal;
	}
	public Double getResult11() {
		return result11;
	}
	public void setResult11(Double result11) {
		this.result11 = result11;
	}
	public Double getResult12() {
		return result12;
	}
	public void setResult12(Double result12) {
		this.result12 = result12;
	}
	public Double getResult13() {
		return result13;
	}
	public void setResult13(Double result13) {
		this.result13 = result13;
	}
	public Double getResult14() {
		return result14;
	}
	public void setResult14(Double result14) {
		this.result14 = result14;
	}
	public Double getResult15() {
		return result15;
	}
	public void setResult15(Double result15) {
		this.result15 = result15;
	}
	
	

	
}