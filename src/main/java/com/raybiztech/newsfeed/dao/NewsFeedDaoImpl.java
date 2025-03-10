package com.raybiztech.newsfeed.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.newsfeed.business.FeedPost;

@Repository("newsFeedDao")
public class NewsFeedDaoImpl extends DAOImpl implements NewsFeedDao {

	@Override
	public List<FeedPost> getFeedPostsByOrder() {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(FeedPost.class).addOrder(
				Order.desc("postDate"));
		return criteria.list();
	}

}
