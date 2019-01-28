package com.ambition.supplier.archives.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.archives.dao.SupplierFileDao;
import com.ambition.supplier.entity.SupplierDeleteMark;
import com.ambition.supplier.entity.SupplierFile;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.useFile.service.UseFileManager;
import com.norteksoft.acs.base.utils.log.LogUtilDao;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.PropUtils;

/**
 * 类名:供应商汇报资料
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  lpf
 * @version 1.00 2018年9月23日 发布
 */
@Service
@Transactional
public class SupplierFileManager {
	@Autowired
	private SupplierFileDao supplierFileDao;
	@Autowired
	private UseFileManager useFileManager;
	@Autowired
	private LogUtilDao logUtilDao;
	@Resource(name="tpdbDataJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	public SupplierFile getSupplierFile(Long id) {
		return supplierFileDao.get(id);
	}
	public Page<SupplierFile> searchBySupplier(Page<SupplierFile>page){
		return supplierFileDao.searchBySupplier(page);		
	}
	public Page<SupplierFile> search(Page<SupplierFile>page){
		return supplierFileDao.search(page);		
	}
	public void saveSupplierFile(SupplierFile supplierFile) {
		String department=CommonUtil.getMainDepartMent();
		if(!"供应商".equals(department)){
			throw new RuntimeException("只有供应商才能上传");
		}
		supplierFile.setSourceUnit(PropUtils.getProp("companyName"));
		supplierFile.setSupplierCode(ContextUtils.getLoginName());
		supplierFile.setSupplierName(ContextUtils.getUserName());
		supplierFileDao.save(supplierFile);
	}
	public String deleteSupplierFile(String deleteIds) {
		String[] ids = deleteIds.split(",");
		int delCount=0;
		int errCount=0;
		for(String id : ids){
			SupplierFile supplierFile = supplierFileDao.get(Long.valueOf(id));
			if(supplierFile != null){
				String companyName= PropUtils.getProp("companyName");
				if(supplierFile.getSourceUnit()==null||supplierFile.getSourceUnit().equals(companyName)){
					supplierFileDao.delete(supplierFile);
					delCount++;
				}else{
					errCount++;
				}			
				//记录删除的信息到TP数据库中				
				if(!companyName.equals("TP")){
					String sql = "insert into SUPPLIER_DELETE_MARK (id,source_id,source_unit,source_table) values("+supplierFile.getId()+","+supplierFile.getId()+",'"+companyName+"','"+SupplierDeleteMark.SUPPLIER_FILE+"')";
					jdbcTemplate.execute(sql);
				}
			}
		}
		String message="";
		if(delCount>0){
			message+=delCount+"条数据删除成功!";
		}		
		if(errCount>0){
			message+=errCount+"条集成数据请到源数据删除!";
		}
		return message;
	}
}
