package com.ambition.iqc.inspectionbase.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ambition.iqc.entity.InspectionBomType;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;

@Repository
public class InspectionBomTypeDao extends HibernateDao<InspectionBomType, Long> {
	public Page<InspectionBomType> list(Page<InspectionBomType> page){
		return findPage(page, "from InspectionBomType d where d.companyId=? ",ContextUtils.getCompanyId());
	}
	
	public List<InspectionBomType> getAllInspectionBomType(){
		return find("from InspectionBomType d where d.companyId=? ",ContextUtils.getCompanyId());
	}

    public Page<InspectionBomType> search(Page<InspectionBomType> page) {
        return searchPageByHql(page, "from InspectionBomType d  order by d.materialTypeCode ");
    }
    
	public  InspectionBomType getInspectionBomType(String type) {
		String hql = "from InspectionBomType d where d.materialType = ?";
		List<InspectionBomType> inspectionBomTypes = this.find(hql,type);
		if(inspectionBomTypes.size()>0){
			return inspectionBomTypes.get(0);
		}else{
			return null;
		}
	}   
}