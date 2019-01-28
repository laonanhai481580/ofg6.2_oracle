package com.ambition.supplier.baseInfo.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.supplier.entity.SupplierMaterialTypeGoal;
import com.norteksoft.mms.base.utils.view.ComboxValues;
import com.norteksoft.product.api.entity.Option;

/**
 * 类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2014-12-24 发布
 */
@Service
@Transactional
public class FaultMaterialTypeManager implements ComboxValues{
    @Autowired
    private SupplierMaterialTypeGoalManager supplierMaterialTypeGoalManager;
    @Override
    public Map<String, String> getValues(Object entity) {
        StringBuilder result=new StringBuilder();
        Map<String,String> map=new HashMap<String, String>();
        List<SupplierMaterialTypeGoal> list = supplierMaterialTypeGoalManager.getAllType();
        List<Option> options = supplierMaterialTypeGoalManager.converExceptionLevelToList(list);
        result.append("'':''");
        for(Option option:options){
            if(result.length() > 0){
                result.append(",");
            }
            result.append("'"+option.getValue()+"':'"+option.getName()+"'");
        }
        map.put("materialType", result.toString());
        map.put("productType", result.toString());
        map.put("supplierAdmitClass", result.toString());
        return map;
    }
}
