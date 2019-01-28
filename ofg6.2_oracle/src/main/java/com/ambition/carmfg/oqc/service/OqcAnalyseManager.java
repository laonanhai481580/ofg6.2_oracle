package com.ambition.carmfg.oqc.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.oqc.dao.OqcInspectionDao;
import com.ambition.util.common.DateUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.util.PropUtils;

@Service
@Transactional
public class OqcAnalyseManager {
	@Autowired
	private OqcInspectionDao oqcInspectionDao;
	/**
	  * 方法名:查询LRR层别图
	  * <p>功能说明：</p>
	  * @searchParams 过滤条件
	  * @return
	 * @throws IllegalAccessException 
	 */
	public JSONObject queryTopReport(JSONObject searchParams) throws IllegalAccessException{
		//数字格式化
		DecimalFormat df = new DecimalFormat("#");
		JSONObject result = new JSONObject();
		Map<String,JSONObject> dataMap = new HashMap<String, JSONObject>();
		List<String> badItems = new ArrayList<String>();
		JSONArray categories = new JSONArray();
		
		
		//查询数据
		queryDatas(df,searchParams,dataMap,categories,badItems);
		
		JSONArray series = new JSONArray();
		//PPM
		JSONObject totalSerie = new JSONObject();
		totalSerie.put("name","不良PPM");
		totalSerie.put("type","spline");
		JSONObject data = dataMap.get("totalRate");
		if(data==null){
			data = new JSONObject();
		}
		JSONArray totalDatas = new JSONArray();
		for(Object itemName : categories){
			Double value = 0.0d;
			if(data.containsKey(itemName.toString())){
				value = data.getDouble(itemName.toString());
			}
			JSONObject point = new JSONObject();
			point.put("y",value);
			totalDatas.add(point);
		}
		totalSerie.put("data",totalDatas);
		series.add(totalSerie);
		
		//不良分布
		for(String badItem : badItems){
			JSONObject serie = new JSONObject();
			serie.put("name",badItem);
			JSONArray datas = new JSONArray();
			JSONObject rateData = dataMap.get(badItem+"_rate");
			if(rateData==null){
				rateData = new JSONObject();
			}
			for(Object itemName : categories){
				Double value = 0.0d;
				if(rateData.containsKey(itemName.toString())){
					value = rateData.getDouble(itemName.toString());
				}
				JSONObject point = new JSONObject();
				point.put("y",value);
				datas.add(point);
			}
			serie.put("data",datas);
			series.add(serie);
		}
		//显示格式化
		Map<String,String> showNameMap = null;
		JSONArray showCategories = new JSONArray();
		for(Object obj : categories){
			String str = obj.toString();
			if(showNameMap!=null&&showNameMap.containsKey(str)){
				str = showNameMap.get(str);
			}
			showCategories.add(str);
		}
		
		result.put("title","OQC不良项目PPM值推移图");
		result.put("categories", showCategories);
		result.put("series",series);
		List<Map<String,Object>> tableDatas = convertToTableDatas(dataMap,badItems,categories);
		
		Map<String,Object> cacheDataMap = new HashMap<String, Object>();
		cacheDataMap.put("tableDatas", tableDatas);//数据
		cacheDataMap.put("categories",categories);//横坐标
		cacheDataMap.put("showCategories",showCategories);//显示横坐标
		result.put("cacheDataMap",cacheDataMap);
		return result;
	}
	
	
	public void setSearchInfo(JSONObject params, List<Object> searchParams, StringBuffer whereSql, StringBuffer groupField){
		whereSql.append(" r.inspection_date between ? and ? ");
		searchParams.add(DateUtil.parseDate(params.getString("totalDate_start")));
		searchParams.add(DateUtil.parseDate(params.getString("totalDate_end") + " 23:59:59","yyyy-MM-dd HH:mm:ss"));
		
		//厂区
		if(params.containsKey("businessUnitName")){
			whereSql.append(" and r.business_unit_name in ('"+params.getString("businessUnitName").replace(",","','")+"')");
		}
		//客户
		if(params.containsKey("customer")){
			whereSql.append(" and r.customer = ? ");
			searchParams.add(params.getString("customer"));
		}
		//工厂
		if(params.containsKey("factory")){
			whereSql.append(" and r.factory= ? ");
			searchParams.add( params.getString("factory"));
		}
		//工序
		if(params.containsKey("procedure")){
			whereSql.append(" and r.procedure = ? ");
			searchParams.add(params.getString("procedure"));
		}
		//机种
		if(params.containsKey("model")){
			whereSql.append(" and r.model = ?");
			searchParams.add(params.getString("model"));
		}
		//产品类别
		if(params.containsKey("productType")){
			whereSql.append(" and r.product_type = ?");
			searchParams.add(params.getString("productType"));
		}
		//班别
		if(params.containsKey("classGroup")){
			whereSql.append(" and r.class_group = ?");
			searchParams.add(params.getString("classGroup"));
		}
		//作业员
		if(params.containsKey("operator")){
			whereSql.append(" and r.operator like ?");
			searchParams.add("%"+params.getString("operator")+"%");
		}
		whereSql.append(" and r.hidden_state='N' ");
		if(params.containsKey("groupValue")){
			String groupValue = params.getString("groupValue");
			if("day".equals(groupValue)){//日
				groupField.setLength(0);
				groupField.append("to_char(r.inspection_date,'yyyyMMdd')");
			}else if("week".equals(groupValue)){//周
				groupField.setLength(0);
				groupField.append("to_char(r.inspection_date,'yyyy_iw')");
			}else if("month".equals(groupValue)){//月
				groupField.setLength(0);
				groupField.append("to_char(r.inspection_date,'yyyy-MM')");
			}else if("qurter".equals(groupValue)){//季
				groupField.setLength(0);
				groupField.append("to_char(r.inspection_date,'yyyy_iw')");
			}else if("year".equals(groupValue)){//年
				groupField.setLength(0);
				groupField.append("to_char(r.inspection_date,'yyyy')");
			}else if("model".equals(groupValue)){//机种
				groupField.setLength(0);
				groupField.append("nvl(r.model,'')");
			}else if("businessUnitName".equals(groupValue)){//厂区
				groupField.setLength(0);
				groupField.append("nvl(r.business_unit_name,'')");
			}else if("factory".equals(groupValue)){//工厂
				groupField.setLength(0);
				groupField.append("nvl(r.factory,'')");
			}else if("procedure".equals(groupValue)){//工序
				groupField.setLength(0);
				groupField.append("nvl(r.procedure,'')");
			}else if("customer".equals(groupValue)){//客户
				groupField.setLength(0);
				groupField.append("nvl(r.customer,'')");
			}else if("lineType".equals(groupValue)){//组/线别
				groupField.setLength(0);
				groupField.append("nvl(r.line_type,'')");
			}else if("operator".equals(groupValue)){//作业员
				groupField.setLength(0);
				groupField.append("nvl(r.operator,'')");
			}
		}
	}
	
	/**
	  * 方法名: 
	  * <p>功能说明：</p>
	  * @return
	 * @throws IllegalAccessException 
	 */
	private void queryDatas(DecimalFormat df,JSONObject params,Map<String,JSONObject> dataMap,
			JSONArray categories,List<String> badItems) throws IllegalAccessException{
		List<Object> searchParams = new ArrayList<Object>();

		StringBuffer whereSql = new StringBuffer("");
		StringBuffer groupField = new StringBuffer("");
		//设置查询参数及分组字段
		setSearchInfo(params, searchParams, whereSql, groupField);
		
		//计算检验数
		String totalSql = "select "+groupField+",";
		String totalField = "count(r.id) total_,sum(r.sampling_count)  sampling_count," +
				"sum(0) inspection_bad_,sum(r.count) count,sum(r.stock_batch) stock_batch,sum(r.quality_batch) quality_batch ";
		totalSql += totalField + " from MFG_OQC_INSPECTION r where ";
		totalSql += whereSql + " group by " + groupField +"order by "+ groupField +"asc";
		Query query = oqcInspectionDao.getSession().createSQLQuery(totalSql);
		int pageSize = params.getInt("pageSize");
		//最多1000条
		//query.setMaxResults(1000);
		for(int i=0;i<searchParams.size();i++){
			query.setParameter(i,searchParams.get(i));
		}
		List<?> list = query.list();
		Map<String,Object[]> objMap = new LinkedHashMap<String, Object[]>();
		for(Object obj : list){
			Object[] objs = (Object[])obj;
			objs[3] = 0;
			
			String name = objs[0]==null?"":objs[0].toString();
			objMap.put(name,objs);
		}		
		//计算不良数
		String detailSql = "select "+groupField+",item.defection_code_name as name,sum(item.defection_code_value) bad_"
				+ " from MFG_OQC_INSPECTION r left join MFG_OQC_DEFECTIVE_ITEMS item " +
				" on r.id = item.MFG_OQC_INSPECTION_ID where item.defection_code_value > 0 and r.hidden_state='N' and ";
		detailSql += whereSql + " group by " + groupField+",item.defection_code_name order by "+groupField+" asc";
		query = oqcInspectionDao.getSession().createSQLQuery(detailSql);
		for(int i=0;i<searchParams.size();i++){
			query.setParameter(i,searchParams.get(i));
		}
		list = query.list();
		for(Object obj : list){
			
			Object[] objs = (Object[])obj;
			if(objs[2]==null){
				continue;
			}
			String groupName = objs[0]==null?"":objs[0].toString();
			if(!objMap.containsKey(groupName)){
				continue;
			}
			Integer badNum = Integer.valueOf(objs[2].toString());
			Object[] values = objMap.get(groupName);
			values[3] = (Integer)values[3] + badNum;
		}
		//排序
		//final String order = params.getString("order");
		List<Object[]> results = new ArrayList<Object[]>();
		for(Object[] objs : objMap.values()){
			results.add(objs);
		}
		
/*		Collections.sort(results,new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				Object[] objs1 = (Object[])o1;
				Object[] objs2 = (Object[])o2;
				Object totalObj1=null,badObj1=null,totalObj2=null,badObj2=null;
				totalObj1 = objs1[2];
				badObj1 = objs1[3];
				totalObj2 = objs2[2];
				badObj2 = objs2[3];
				Integer total1 = totalObj1==null?0:Integer.valueOf(totalObj1.toString());
				Integer bad1 = badObj1==null?0:Integer.valueOf(badObj1.toString());
				
				Integer total2 = totalObj2==null?0:Integer.valueOf(totalObj2.toString());
				Integer bad2 = badObj2==null?0:Integer.valueOf(badObj2.toString());
				
				try {
					double val1 = bad1;
					if(total1 > 0){
						val1 = bad1*1.0/total1;
					}
					double val2 = bad2;
					if(total2 > 0){
						val2 = bad2*1.0/total2;
					}
					if("asc".equalsIgnoreCase(order)){
						if(val1<val2){
							return -1;
						}else if(val1>val2){
							return 1;
						}else {
							return 0;
						}
					}else{
						if(val1<val2){
							return 1;
						}else if(val1>val2){
							return -1;
						}else{
							return 0;
						}
					}
				} catch (Exception e) {
					throw new AmbFrameException("排序失败!",e);
				}
			}
		});*/
		//前几位		
		for(int i=0;i<results.size()&&i<pageSize;i++){
			Object[] objs = (Object[])results.get(i);
			//列标题
			String colName = objs[0]==null?"":objs[0].toString();
			categories.add(colName);
			
			Integer total = null,bad=null,count=null,stockBatch=null,qualityBatch=null;
			total = objs[2]==null?0:Integer.valueOf(objs[2].toString());
			bad = objs[3]==null?0:Integer.valueOf(objs[3].toString());
			count = objs[4]==null?0:Integer.valueOf(objs[4].toString());
			stockBatch = objs[5]==null?0:Integer.valueOf(objs[5].toString());
			qualityBatch = objs[6]==null?0:Integer.valueOf(objs[6].toString());
			//抽检数
			JSONObject data = dataMap.get("totalInput");
			if(data == null){
				data = new JSONObject();
				dataMap.put("totalInput",data);
			}
			data.put(colName,total);
			//投入数
			data = dataMap.get("totalCount");
			if(data == null){
				data = new JSONObject();
				dataMap.put("totalCount",data);
			}
			data.put(colName,count);
			//不良数
			data = dataMap.get("totalBad");
			if(data == null){
				data = new JSONObject();
				dataMap.put("totalBad",data);
			}
			data.put(colName,bad);
			//不良PPM
			Double totalRate = 0.0;
			if(total>0){
				totalRate = bad * 1000000.0 / total;
				totalRate = Double.valueOf(df.format(totalRate));
			}
			data = dataMap.get("totalRate");
			if(data == null){
				data = new JSONObject();
				dataMap.put("totalRate",data);
			}
			data.put(colName,totalRate);
			//投入批
			data = dataMap.get("stockBatch");
			if(data == null){
				data = new JSONObject();
				dataMap.put("stockBatch",data);
			}
			data.put(colName,stockBatch);
			//合格批
			data = dataMap.get("qualityBatch");
			if(data == null){
				data = new JSONObject();
				dataMap.put("qualityBatch",data);
			}
			data.put(colName,qualityBatch);
			//批合格率
			Double qualityBatchRate = 0.0;
			if(stockBatch>0){
				qualityBatchRate = qualityBatch * 100.0 / stockBatch;
				DecimalFormat numDFormat = new DecimalFormat("0.00");
				qualityBatchRate = Double.valueOf(numDFormat.format(qualityBatchRate));
			}
			data = dataMap.get("qualityBatchRate");
			if(data == null){
				data = new JSONObject();
				dataMap.put("qualityBatchRate",data);
			}
			data.put(colName,qualityBatchRate+"%");
			
		}
		//子项的不良数和不良率
		for(Object obj : list){
			Object[] objs = (Object[])obj;
			String colName = objs[0]==null?"":objs[0].toString();
			//不是分组范围的值不处理
			if(!categories.contains(colName)){
				continue;
			}
			String badItem = objs[1]==null?"":objs[1].toString();
			if(!badItems.contains(badItem)){
				badItems.add(badItem);
			}
			Integer badNum = objs[2]==null?0:Integer.valueOf(df.format(Double.valueOf(objs[2].toString())));
			//项目不良数
			String itemKey = badItem + "_bad";
			JSONObject data = dataMap.get(itemKey);
			if(data == null){
				data = new JSONObject();
				dataMap.put(itemKey,data);
			}
			if(data.containsKey(colName)){
				badNum += data.getInt(colName);
			}
			data.put(colName,badNum);
			//总计检验数
			Integer totalInspection = 0;
			Object[] totalValues = objMap.get(colName);
			if(totalValues!=null&&totalValues[2]!=null){
				totalInspection = Integer.valueOf(totalValues[2].toString());
			}
			//不良项目占比
			Double badRate = 0.0;
			if(totalInspection>0){
				badRate = badNum*1000000.0/totalInspection;
				badRate = Double.valueOf(df.format(badRate));
			}
			
			String rateKey = badItem + "_rate";
			data = dataMap.get(rateKey);
			if(data == null){
				data = new JSONObject();
				dataMap.put(rateKey,data);
			}
			data.put(colName,badRate);
		}
	}
	
	/**
	  * 方法名:转换为表格数据 
	  * <p>功能说明：</p>
	  * @return
	 */
	private List<Map<String,Object>> convertToTableDatas(Map<String,JSONObject> dataMap,
			List<String> badItems,JSONArray categories){
		List<Map<String,Object>> tableDatas = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> ppmCountMap = new HashMap<String, Object>();
		ppmCountMap.put("name","投入数");
		ppmCountMap.put("type","汇总");
		tableDatas.add(ppmCountMap);		
		
		Map<String,Object> ppmInspectionMap = new HashMap<String, Object>();
		ppmInspectionMap.put("name","抽检数");
		ppmInspectionMap.put("type","汇总");
		tableDatas.add(ppmInspectionMap);
		
		Map<String,Object> ppmBadMap = new HashMap<String, Object>();
		ppmBadMap.put("name","不良数");
		ppmBadMap.put("type","汇总");
		tableDatas.add(ppmBadMap);	
		
		Map<String,Object> ppmMap = new HashMap<String, Object>();
		ppmMap.put("name","不良PPM");
		ppmMap.put("type","汇总");
		tableDatas.add(ppmMap);
		
		Map<String,Object> stockBatchMap = new HashMap<String, Object>();
		stockBatchMap.put("name","投入批");
		stockBatchMap.put("type","汇总");
		tableDatas.add(stockBatchMap);
		
		Map<String,Object> qualityBatchMap = new HashMap<String, Object>();
		qualityBatchMap.put("name","合格批");
		qualityBatchMap.put("type","汇总");
		tableDatas.add(qualityBatchMap);
		
		Map<String,Object> qualityBatchRateMap = new HashMap<String, Object>();
		qualityBatchRateMap.put("name","批合格率");
		qualityBatchRateMap.put("type","汇总");
		tableDatas.add(qualityBatchRateMap);
		
		JSONObject totalCountJson = dataMap.get("totalCount");
		JSONObject totalInputJson = dataMap.get("totalInput");
		JSONObject totalBadJson = dataMap.get("totalBad");
		JSONObject totalRateJson = dataMap.get("totalRate");
		JSONObject stockBatchJson = dataMap.get("stockBatch");
		JSONObject qualityBatchJson = dataMap.get("qualityBatch");
		JSONObject qualityBatchRateJson = dataMap.get("qualityBatchRate");
		for(Object categori : categories){
			if(totalInputJson!=null&&totalInputJson.containsKey(categori)){
				ppmInspectionMap.put(categori.toString(),totalInputJson.get(categori));
			}
			if(totalBadJson!=null&&totalBadJson.containsKey(categori)){
				ppmBadMap.put(categori.toString(),totalBadJson.get(categori));
			}
			if(totalRateJson!=null&&totalRateJson.containsKey(categori)){
				ppmMap.put(categori.toString(),totalRateJson.get(categori));
			}
			if(totalCountJson!=null&&totalCountJson.containsKey(categori)){
				ppmCountMap.put(categori.toString(),totalCountJson.get(categori));
			}
			if(stockBatchJson!=null&&stockBatchJson.containsKey(categori)){
				stockBatchMap.put(categori.toString(),stockBatchJson.get(categori));
			}
			if(qualityBatchJson!=null&&qualityBatchJson.containsKey(categori)){
				qualityBatchMap.put(categori.toString(),qualityBatchJson.get(categori));
			}
			if(qualityBatchRateJson!=null&&qualityBatchRateJson.containsKey(categori)){
				qualityBatchRateMap.put(categori.toString(),qualityBatchRateJson.get(categori));
			}
		}
		//明细数据
		List<Map<String,Object>> badNumMaps = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> badRateMaps = new ArrayList<Map<String,Object>>();
		for(String badItem : badItems){
			String showName = badItem;
			Map<String,Object> badNumMap = new HashMap<String, Object>();
			badNumMap.put("type","不良数");
			badNumMap.put("name",showName);		
			 
			JSONObject badNumJson = dataMap.get(badItem + "_bad");
			if(badNumJson == null){
				badNumJson = new JSONObject();
			}
			JSONObject badRateJson = dataMap.get(badItem + "_rate");
			if(badRateJson == null){
				badRateJson = new JSONObject();
			}
			for(Object categori : categories){
				if(badNumJson.containsKey(categori)){
					badNumMap.put(categori.toString(),badNumJson.get(categori));
				}
			}
			badNumMaps.add(badNumMap);
		}
		tableDatas.addAll(badNumMaps);
		tableDatas.addAll(badRateMaps);
		return tableDatas;
	}
	/**
	  * 方法名: 导出到Excel
	  * <p>功能说明：</p>
	  * @return
	 */
	public String exportChartAndGrid(String svg,String fileName,
									String chartWidth,String chartHeight,
									Map<String,Object> cacheDataMap) throws Exception{
		InputStream inputStream = null;
		OutputStream out = null;
		try {
			inputStream = getClass().getResourceAsStream("/template/report/chart.xls");
			Workbook workbook = WorkbookFactory.create(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			//导出数据到单元格
			exportGrid(sheet,cacheDataMap);
			//导出图片到Excel
			exportChart(sheet,svg,chartWidth,chartHeight);
			//标题
			String savePath = PropUtils.getProp("excel.export.file.path");
			File pathFile = new File(savePath);
			if(!pathFile.exists()){
				pathFile.mkdir();
			}
			String saveFileName = UUID.randomUUID().toString();
			File file = new File(savePath + saveFileName);
			out = new FileOutputStream(file);
			workbook.write(out);
			return fileName + ".xls_" + saveFileName;
		} finally{
			if(inputStream != null){
				inputStream.close();
			}
		}
	}
	/**
	  * 方法名:导出数据到表格
	  * <p>功能说明：</p>
	  * @return
	 */
	@SuppressWarnings("unchecked")
	private void exportGrid(Sheet sheet,Map<String,Object> cacheDataMap){
		//获取缓存数据
		List<Map<String,Object>> tableDatas = (List<Map<String,Object>>)cacheDataMap.get("tableDatas");
		JSONArray categories = (JSONArray)cacheDataMap.get("categories");
		JSONArray showCategories = (JSONArray)cacheDataMap.get("showCategories");
		
		//表头
		int rowIndex = 19;
		Row row = sheet.createRow(rowIndex++);
		row.setHeightInPoints(25);
		//表头样式
		CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		
		row.createCell(1).setCellStyle(headerStyle);
		sheet.setColumnWidth(1,16*256);
		row.createCell(2).setCellStyle(headerStyle);
		sheet.setColumnWidth(2,16*256);
		Map<String,Integer> nameIndexMap = new HashMap<String,Integer>();
		for(int i=0;i<showCategories.size();i++){
			String name = showCategories.get(i).toString();
			int colIndex = i + 3;
			nameIndexMap.put(categories.getString(i),colIndex);
			Cell cell = row.createCell(colIndex);
			cell.setCellStyle(headerStyle);
			cell.setCellValue(name);
			
			sheet.setColumnWidth(colIndex,10*256);
		}
		
		//表头样式
		CellStyle contentStyle = sheet.getWorkbook().createCellStyle();
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		
		//第一行内容主体的行号
		DecimalFormat numDFormat = new DecimalFormat("0");
		int firstContentRowIndex = rowIndex;
		for(Map<String,Object> data : tableDatas){
			row = sheet.createRow(rowIndex++);
			row.setHeightInPoints(18);
			
			Cell firstCell = row.createCell(1);
			firstCell.setCellStyle(contentStyle);
			firstCell.setCellValue(data.get("type")==null?"":data.get("type").toString());
			Cell secCell = row.createCell(2);
			secCell.setCellStyle(contentStyle);
			secCell.setCellValue(data.get("name")==null?"":data.get("name").toString());
			
			for(Object cate : categories){
				Integer colIndex = nameIndexMap.get(cate.toString());
				if(colIndex==null){
					continue;
				}
				Cell cell = row.createCell(colIndex);
				cell.setCellStyle(contentStyle);
				Object value = null;
				if(data.containsKey(cate)){
					value = data.get(cate.toString());
				}
				if(value != null){
					if(value instanceof Number){
						cell.setCellValue(numDFormat.format(value));
					}else{
						cell.setCellValue(value.toString());
					}
				}
			}
		}
		
		//处理第一行合并行
		String lastStr = sheet.getRow(firstContentRowIndex).getCell(1).getStringCellValue();
		int firstSpanRow = firstContentRowIndex;
		for(int i=firstContentRowIndex;i<rowIndex;i++){
			row = sheet.getRow(i+1);
			String nextStr = "";
			if(row != null){
				Cell cell = row.getCell(1);
				if(cell != null){
					nextStr = cell.getStringCellValue();
				}
			}
			if(!nextStr.equals(lastStr)){
				sheet.addMergedRegion(new CellRangeAddress(firstSpanRow,i,1,1));
				firstSpanRow = i+1;
				lastStr = nextStr;
			}
		}
	}
	
	private void exportChart(Sheet sheet,String svg,String chartWidth,String chartHeight) throws Exception{
		if(StringUtils.isEmpty(svg)){
			throw new AmbFrameException("图片内容不能为空!");
		}
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			svg = svg.replaceAll(":rect", "rect");
		    Transcoder t = new JPEGTranscoder();
		    byteArrayOutputStream = new ByteArrayOutputStream();
			TranscoderInput input = new TranscoderInput(new StringReader(svg));
			TranscoderOutput output = new TranscoderOutput(byteArrayOutputStream);
			t.transcode(input, output);
			Drawing d= sheet.createDrawingPatriarch();
			int begin=0;
			int end=0;
			if(!chartWidth.equals("")){
				//生成列宽
				int firstRow = 1;
				int maxWidth = Integer.valueOf(chartWidth);
				double currentWidth = 0;
				while(true){
					double width = sheet.getColumnWidth(firstRow++)/29.25;
					if(currentWidth + width >= maxWidth){
						begin = firstRow;
						break;
					}
					currentWidth += width;
				}
//				begin=Integer.parseInt(chartWidth)/70;
			}
			if(!chartHeight.equals("")){
				end=Integer.parseInt(chartHeight)/25;
			}
			HSSFClientAnchor anchor=null;
			if(begin!=0&&end!=0){
				anchor= new HSSFClientAnchor(0,0,512,255,(short) 1,1,(short)begin,end);
			}else{
				anchor= new HSSFClientAnchor(0,0,512,255,(short) 1,1,(short)15,20);
			}
			d.createPicture(anchor,sheet.getWorkbook().addPicture(byteArrayOutputStream.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
		} finally{
			if(byteArrayOutputStream != null){
				byteArrayOutputStream.close();
			}
		}
	}
	
}
