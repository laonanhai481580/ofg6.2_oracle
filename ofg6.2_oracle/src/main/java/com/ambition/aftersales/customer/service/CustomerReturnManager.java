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

import com.ambition.aftersales.customer.dao.CustomerReturnDao;
import com.ambition.aftersales.entity.CustomerReturn;
import com.ambition.util.common.CommonUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.ListView;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;

/**
 * 
 * 类名:客退率台账Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2018年4月12日 发布
 */
@Service
@Transactional
public class CustomerReturnManager {
	@Autowired
	private CustomerReturnDao customerReturnDao;	
	public CustomerReturn getCustomerReturn(Long id){
		return customerReturnDao.get(id);
	}
	
	public void deleteCustomerReturn(CustomerReturn customerReturn){
		customerReturnDao.delete(customerReturn);
	}

	public Page<CustomerReturn> search(Page<CustomerReturn>page){
		return customerReturnDao.search(page);
	}

	public List<CustomerReturn> listAll(){
		return customerReturnDao.getAllCustomerReturn();
	}
		
	public void deleteCustomerReturn(Long id){
		customerReturnDao.delete(id);
	}
	public void deleteCustomerReturn(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			if(id!=null){
				CustomerReturn  customerReturn = customerReturnDao.get(Long.valueOf(id));
				customerReturnDao.delete(customerReturn);
			}			
		}
	}
	public void saveCustomerReturn(CustomerReturn customerReturn){
/*		String returnRate=customerReturn.getReturnRate();
		String targetRate=customerReturn.getTargetRate();
		if(returnRate!=null&&!"".equals(returnRate)){
			returnRate=returnRate.replaceAll("%", "");
		}
		if(targetRate!=null&&!"".equals(targetRate)){
			targetRate=targetRate.replaceAll("%", "");
		}
		if("NA".equals(returnRate)){
			customerReturn.setIsOver("是");
		}else{
			if(CommonUtil.isDouble(returnRate)&&CommonUtil.isDouble(targetRate)){
				if(Double.valueOf(returnRate)>Double.valueOf(targetRate)){
					customerReturn.setIsOver("是");
				}
			}
		}	*/	
		customerReturnDao.save(customerReturn);
	}

	public String importDatas(File file) throws Exception{
		StringBuffer sb = new StringBuffer("");
		//表单字段
		Map<String,String> fieldMap = this.getFieldMap("AFS_CUSTOMER_RETURN");
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
				CustomerReturn customerReturn = new CustomerReturn();
				customerReturn.setCompanyId(ContextUtils.getCompanyId());
				customerReturn.setCreatedTime(new Date());
				customerReturn.setCreator(ContextUtils.getLoginName());
				customerReturn.setModifiedTime(new Date());
				customerReturn.setModifier(ContextUtils.getLoginName());
				customerReturn.setModifierName(ContextUtils.getUserName());
				for(String key : objMap.keySet()){
					CommonUtil.setProperty(customerReturn,key, objMap.get(key));
				}
				Integer deliverCount=customerReturn.getDeliverCount();
				Integer returnCount=customerReturn.getReturnCount();
				if(deliverCount==null||"".equals(deliverCount)||returnCount==null||"".equals(returnCount)){
					throw new AmbFrameException("出货数和退货数不能为空!");
				}
				String 	returnRate=null;
				if(deliverCount==0){
					customerReturn.setIsOver("是");
					returnRate="NA";
				}else{
					if(returnCount>deliverCount){
						returnRate="NA";
						customerReturn.setIsOver("是");
					}else{
						DecimalFormat   decimalFormat   =new   DecimalFormat("0.00");
						if(returnCount==0){
							returnRate="0.00";
						}else{
							returnRate=decimalFormat.format((float)(returnCount*100)/(float)deliverCount);					
						}
					}	
				}														
				String targetRate=customerReturn.getTargetRate();	
				if(targetRate!=null&&!"".equals(targetRate)){
					targetRate=targetRate.replaceAll("%", "");
				}
				if(CommonUtil.isDouble(returnRate)&&CommonUtil.isDouble(targetRate)){					
					if(Double.valueOf(returnRate)>Double.valueOf(targetRate)){
						customerReturn.setIsOver("是");
					}
				}
				customerReturn.setReturnRate(returnRate);
			   customerReturnDao.save(customerReturn);
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
