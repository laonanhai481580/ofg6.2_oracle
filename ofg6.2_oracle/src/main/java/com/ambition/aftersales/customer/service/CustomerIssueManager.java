package com.ambition.aftersales.customer.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.customer.dao.CustomerIssueDao;
import com.ambition.aftersales.entity.CustomerIssue;
import com.ambition.util.common.CommonUtil;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**
 * 
 * 类名:客户issue台账Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2018年4月12日 发布
 */
@Service
@Transactional
public class CustomerIssueManager {
	@Autowired
	private CustomerIssueDao customerIssueDao;	
	public CustomerIssue getCustomerIssue(Long id){
		return customerIssueDao.get(id);
	}
	
	public void deleteCustomerIssue(CustomerIssue customerIssue){
		customerIssueDao.delete(customerIssue);
	}

	public Page<CustomerIssue> search(Page<CustomerIssue>page){
		return customerIssueDao.search(page);
	}

	public List<CustomerIssue> listAll(){
		return customerIssueDao.getAllCustomerIssue();
	}
		
	public void deleteCustomerIssue(Long id){
		customerIssueDao.delete(id);
	}
	public void deleteCustomerIssue(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			if(id!=null){
				CustomerIssue  customerIssue = customerIssueDao.get(Long.valueOf(id));
				customerIssueDao.delete(customerIssue);
			}			
		}
	}
	public void saveCustomerIssue(CustomerIssue customerIssue){
		customerIssueDao.save(customerIssue);
	}

	public String importDatas(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		//表单字段
		Map<String,String> fieldMap = this.getFieldMap("AFS_CUSTOMER_ISSUE");
		Workbook book = WorkbookFactory.create(new FileInputStream(file));
		
		Sheet sheet = book.getSheetAt(0);
		Row row = sheet.getRow(0);
		if(row == null){
			throw new RuntimeException("第一行不能为空!");
		}
		
		Map<String,Integer> columnMap = new HashMap<String,Integer>();
		for(int i=0;;i++){
			Cell cell = row.getCell(i);
			if(cell==null){
				break;
			}
			String value = cell.getStringCellValue();
			if(fieldMap.containsKey(value)){
				columnMap.put(value,i);
			}
		}
		/*if(columnMap.keySet().size() != fieldMap.keySet().size()){
			throw new AmbFrameException("Excel格式不正确!请重新导出台账数据模板!");
		}*/
		
		DecimalFormat df = new DecimalFormat("#.##");
		Iterator<Row> rows = sheet.rowIterator();
		rows.next();//标题行
		int i = 0;
		while(rows.hasNext()){
			row = rows.next();
			try {
				Map<String,Object> objMap = new HashMap<String, Object>();
				for(String columnName : columnMap.keySet()){
					Cell cell = row.getCell(columnMap.get(columnName));
					if(cell != null){
						Object value = null;
						if(Cell.CELL_TYPE_STRING == cell.getCellType()){
							value = cell.getStringCellValue();
						}else if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								value = cell.getDateCellValue();
							} else {
								value = df.format(cell.getNumericCellValue());
							}
						}else if(Cell.CELL_TYPE_FORMULA == cell.getCellType()){
							value = cell.getCellFormula();
						}
						objMap.put(fieldMap.get(columnName),value);
					}
				}
				CustomerIssue customerIssue = new CustomerIssue();
				customerIssue.setCompanyId(ContextUtils.getCompanyId());
				customerIssue.setCreatedTime(new Date());
				customerIssue.setCreator(ContextUtils.getLoginName());
				customerIssue.setModifiedTime(new Date());
				customerIssue.setModifier(ContextUtils.getLoginName());
				customerIssue.setModifierName(ContextUtils.getUserName());
				for(String key : objMap.keySet()){
					CommonUtil.setProperty(customerIssue,key, objMap.get(key));
				}
			   customerIssueDao.save(customerIssue);
			   sb.append("第" + (i+1) + "行导入成功!<br/>");
			} catch (Exception e) {
				e.printStackTrace();
				sb.append("第" + (i+1) + "行导入失败:<font color=red>" + e.getMessage() + "</font><br/>");
			}
			i++;
		}
		file.delete();
		return sb.toString();
	}
	
	/**
	  * 方法名:获取字段映射 
	  * <p>功能说明：</p>
	  * @return
	 */
	public Map<String,String> getFieldMap(String listCode){
		Map<String,String> fieldMap = new HashMap<String, String>();
		ListView columns = ApiFactory.getMmsService().getListViewByCode(listCode);
		for(ListColumn column: columns.getColumns()){
			if(column.getVisible()){
				fieldMap.put(column.getHeaderName(), column.getTableColumn().getName());
			}
		}
		return fieldMap;
	}
}
