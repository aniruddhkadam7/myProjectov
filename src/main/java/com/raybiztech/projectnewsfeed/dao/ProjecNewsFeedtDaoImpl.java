package com.raybiztech.projectnewsfeed.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.projectnewsfeed.business.ProjectFeedPost;

@Repository("projecNewsFeedDao")
public class ProjecNewsFeedtDaoImpl extends DAOImpl implements
		ProjecNewsFeedtDao {

	@Override
	public List<ProjectFeedPost> getFeedPosts(Long projectid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProjectFeedPost.class);
		criteria.createAlias("project", "project");
		criteria.add(Restrictions.eq("project.id", projectid));
		criteria.add(Restrictions.isNull("milestone"));
		criteria.add(Restrictions.isNull("invoice"));
		criteria.addOrder(Order.desc("postDate"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<ProjectFeedPost> getMilestoneNewFeed(Long projectId,
			Long milestoneId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProjectFeedPost.class);
		criteria.createAlias("project", "project");
		criteria.createAlias("milestone", "milestone");
		criteria.add(Restrictions.eq("project.id", projectId));
		criteria.add(Restrictions.eq("milestone.id", milestoneId));
		criteria.addOrder(Order.desc("postDate"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<ProjectFeedPost> getInvoiceNewsFeed(Long projectId,
			Long invoiceId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProjectFeedPost.class);
		criteria.createAlias("project", "project");
		criteria.createAlias("invoice", "invoice");
		criteria.add(Restrictions.eq("project.id", projectId));
		criteria.add(Restrictions.eq("invoice.id", invoiceId));
		criteria.addOrder(Order.desc("postDate"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

}
