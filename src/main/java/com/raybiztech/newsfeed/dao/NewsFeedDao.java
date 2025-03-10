package com.raybiztech.newsfeed.dao;

import java.util.List;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.newsfeed.business.FeedPost;

public interface NewsFeedDao extends DAO {

	List<FeedPost> getFeedPostsByOrder();

}
