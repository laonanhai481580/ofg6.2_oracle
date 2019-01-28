package com.ambition.emailtemplate.dao;


import org.springframework.stereotype.Repository;

import com.ambition.emailtemplate.entity.EmailTemplate;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
/**
 * 类名:邮件模板DAO
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2017-3-20 发布
 */
@Repository
public class EmailTemplateDao extends HibernateDao<EmailTemplate, Long> {
	public Page<EmailTemplate> search(Page<EmailTemplate> page) {
        return searchPageByHql(page, "from EmailTemplate p where p.companyId=?", ContextUtils.getCompanyId());
    }
}
