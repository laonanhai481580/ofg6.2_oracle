package com.ambition.carmfg.factoryprocedure.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.entity.OqcProcedure;
import com.norteksoft.mms.base.utils.view.ComboxValues;
import com.norteksoft.product.api.entity.Option;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2017-11-9 发布
 */
@Service
@Transactional
public class ProcedureSelectManager implements ComboxValues{
    @Autowired
    private OqcProcedureManager oqcProcedureManager;

    @Override
    public Map<String, String> getValues(Object entity) {
        StringBuilder result=new StringBuilder();
        Map<String,String> map=new HashMap<String, String>();
        List<OqcProcedure> list = oqcProcedureManager.getAllOqcProcedure();
        List<Option> options = converExceptionLevelToList(list);
        result.append("'':''");
        for(Option option:options){
            if(result.length() > 0){
                result.append(",");
            }
            result.append("'"+option.getValue()+"':'"+option.getName()+"'");
        }
        map.put("procedure", result.toString());
        map.put("station", result.toString());
        return map;
    }
    
    public List<Option> converExceptionLevelToList(List<OqcProcedure> list){
    	   List<Option> options = new ArrayList<Option>();
    	   for(OqcProcedure procedure : list){
    	       Option option = new Option();
    	       String name = procedure.getProcedure().toString();
    	       String value = procedure.getProcedure().toString();
    	       option.setName(name==null?"":name.replaceAll("\n","").replaceAll(",","，"));
    	       option.setValue(value==null?"":value.replaceAll("\n","").replaceAll(",","，"));
    	       options.add(option);
    	   }
    	   return options;
    	}  
}
