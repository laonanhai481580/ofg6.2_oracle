package com.ambition.iqc.inspectionbase.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.bom.dao.ProductBomDao;
import com.ambition.iqc.entity.TestFrequency;
import com.ambition.iqc.inspectionbase.dao.TestFrequencyDao;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

@Service
@Transactional
public class TestFrequencyManager {
	@Autowired
	private TestFrequencyDao testFrequencyDao;	
	@Autowired
	private ProductBomDao productBomDao;
	public TestFrequency getTestFrequency(Long id){
		return testFrequencyDao.get(id);
	}
	
	public void deleteTestFrequency(TestFrequency testFrequency){
		testFrequencyDao.delete(testFrequency);
	}

	public Page<TestFrequency> search(Page<TestFrequency>page){
		return testFrequencyDao.search(page);
	}
	public Page<TestFrequency> searchMaterial(Page<TestFrequency>page){
		return testFrequencyDao.searchMaterial(page);
	}
	public List<TestFrequency> listAll(){
		return testFrequencyDao.getAllTestFrequency();
	}
		
	public void deleteTestFrequency(Long id){
		testFrequencyDao.delete(id);
	}
	public void deleteTestFrequency(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			TestFrequency  testFrequency = testFrequencyDao.get(Long.valueOf(id));
			if(testFrequency.getId() != null&&!testFrequency.getIsErp().equals("是")){
				testFrequencyDao.delete(testFrequency);
			}
		}
	}
	public void saveTestFrequency(TestFrequency testFrequency){
		if(testFrequency.getIsErp()!=null&&testFrequency.getIsErp().equals("是")){
			if(isExistTestFrequency(testFrequency.getId(),testFrequency.getSupplierCode(),testFrequency.getCheckBomCode(),testFrequency.getBusinessUnitName())){
				throw new AmbFrameException("已经存在相同的测试频率信息!");
			}
		}else{
			if(testFrequency.getMaterialType()==null||"".equals(testFrequency.getMaterialType())){
				throw new AmbFrameException("材质必填!");
			}
			if(isExistMaterialFrequency(testFrequency.getId(),testFrequency.getSupplierCode(),testFrequency.getMaterialType(),testFrequency.getBusinessUnitName())){
				throw new AmbFrameException("已经存在相同的测试频率信息!");
			}
			if(testFrequency.getIsHsf()==null||"".equals(testFrequency.getIsHsf())){
				testFrequency.setIsHsf("否");
			}
			if(testFrequency.getIsOrt()==null||"".equals(testFrequency.getIsOrt())){
				testFrequency.setIsOrt("否");
			}
			testFrequency.setIsMaterial("是");
		}		
		testFrequencyDao.save(testFrequency);
	}
	/**
	 * 检查是否存在相同的测试频率（材质）
	 * @param id
	 * @param name
	 * @return
	 */
	public Boolean isExistMaterialFrequency(Long id,String supplierCode,String materialType,String businessUnitName){
		String hql = "select count(i.id) from TestFrequency i where i.companyId = ? and i.supplierCode = ? and i.checkBomCode=?  ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(supplierCode);
		searchParams.add(materialType);
		if(businessUnitName != null&&"欧菲-TP".equals(ContextUtils.getCompanyName())){
			hql += " and i.businessUnitName= ? ";
			searchParams.add(businessUnitName);
		}
		if(id != null){
			hql += " and i.id <> ?";
			searchParams.add(id);
		}
		List<?> list = testFrequencyDao.find(hql,searchParams.toArray());
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}	
	/**
	 * 检查是否存在相同的测试频率
	 * @param id
	 * @param name
	 * @return
	 */
	public Boolean isExistTestFrequency(Long id,String supplierCode,String checkBomCode,String businessUnitName){
		String hql = "select count(i.id) from TestFrequency i where i.companyId = ? and i.supplierCode = ? and i.checkBomCode=?  ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(supplierCode);
		searchParams.add(checkBomCode);
		if(businessUnitName != null&&"欧菲-TP".equals(ContextUtils.getCompanyName())){
			hql += " and i.businessUnitName= ? ";
			searchParams.add(businessUnitName);
		}
		if(id != null){
			hql += " and i.id <> ?";
			searchParams.add(id);
		}
		List<?> list = testFrequencyDao.find(hql,searchParams.toArray());
		if(Integer.valueOf(list.get(0).toString())>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 查询测试频率
	 * @param id
	 * @param name
	 * @return
	 */
	public TestFrequency searchFrequency(String supplierCode,String checkBomCode){
		String hql = "select i from TestFrequency i where i.companyId = ? and i.supplierCode = ? and i.checkBomCode=?   ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(supplierCode);
		searchParams.add(checkBomCode);
		List<TestFrequency> list = testFrequencyDao.find(hql,searchParams.toArray());
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 查询测试频率
	 * @param id
	 * @param name
	 * @return
	 */
	public TestFrequency searchFrequencyTP(String supplierCode,String checkBomCode,String businessUnitName){
		String hql = "select i from TestFrequency i where i.companyId = ? and i.supplierCode = ? and i.checkBomCode=?   ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(supplierCode);
		searchParams.add(checkBomCode);
		if(businessUnitName != null){
			hql += " and i.businessUnitName= ? ";
			searchParams.add(businessUnitName);
		}
		hql +=" and i.isMaterial='否' ";
		List<TestFrequency> list = testFrequencyDao.find(hql,searchParams.toArray());
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 查询测试频率(材质)
	 * @param id
	 * @param name
	 * @return
	 */
	public TestFrequency searchFrequencyMaterial(String supplierCode,String materialType,String businessUnitName){
		String hql = "select i from TestFrequency i where i.companyId = ? and i.supplierCode = ? and i.materialType=?   ";
		List<Object> searchParams = new ArrayList<Object>();
		searchParams.add(ContextUtils.getCompanyId());
		searchParams.add(supplierCode);
		searchParams.add(materialType);
		if(businessUnitName != null){
			hql += " and i.businessUnitName= ? ";
			searchParams.add(businessUnitName);
		}
		hql +=" and i.isMaterial='是' ";
		List<TestFrequency> list = testFrequencyDao.find(hql,searchParams.toArray());
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}	
}
