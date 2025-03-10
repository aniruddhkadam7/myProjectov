package com.raybiztech.projectnewsfeed.dao;

import java.util.List;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.projectnewsfeed.business.ProjectFeedPost;

public interface ProjecNewsFeedtDao extends DAO {

	List<ProjectFeedPost> getFeedPosts(Long projectid);

	List<ProjectFeedPost> getMilestoneNewFeed(Long projectId, Long milestoneId);

	List<ProjectFeedPost> getInvoiceNewsFeed(Long projectId, Long invoiceId);

}
