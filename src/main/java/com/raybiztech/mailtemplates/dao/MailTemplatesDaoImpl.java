package com.raybiztech.mailtemplates.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.assetmanagement.business.AssetType;
import com.raybiztech.mailtemplates.business.MailTemplate;

@Repository("mailTemplatesDaoImpl")
public class MailTemplatesDaoImpl extends DAOImpl implements MailTemplatesDao {

	/**
	 * 
	 * @author shashank
	 * 
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<MailTemplate> getMailTemplates(Long type, String searchText) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				MailTemplate.class);
		if (type != null)
			criteria.add(Restrictions.eq("templateType.id", type));
		if (searchText != null)
			criteria.add(Restrictions.ilike("templateName", searchText,
					MatchMode.ANYWHERE));
		criteria.addOrder(Order.desc("id"));

		return criteria.list();
	}

	@Override
	public List<MailTemplate> getMailTemplatesForExport(Long type,
			String searchText) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				MailTemplate.class);
		if (type != null)
			criteria.add(Restrictions.eq("templateType.id", type));
		if (searchText != null)
			criteria.add(Restrictions.ilike("templateName", searchText,
					MatchMode.ANYWHERE));

		criteria.addOrder(Order.desc("templateType"));
		return criteria.list();
	}

	@Override
	public String getMailContent(String mailTemplateName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				MailTemplate.class);
		criteria.add(Restrictions.eq("templateName", mailTemplateName));
		criteria.setProjection(Projections.property("template"));
		return (String) criteria.uniqueResult();
	}

	@Override
	public MailTemplate getmailTemplateOfAssetType(AssetType assetType) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				MailTemplate.class);
		criteria.add(Restrictions.eq("assetType", assetType));

		return (MailTemplate) criteria.uniqueResult();

	}

}
