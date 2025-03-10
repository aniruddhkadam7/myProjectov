package com.raybiztech.projectnewsfeed.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.projectnewsfeed.builder.ProjectFeedPostBuider;
import com.raybiztech.projectnewsfeed.builder.ProjectFeedPostCommentsBuilder;
import com.raybiztech.projectnewsfeed.business.ProjectFeedPost;
import com.raybiztech.projectnewsfeed.business.ProjectFeedPostComments;
import com.raybiztech.projectnewsfeed.dao.ProjecNewsFeedtDao;
import com.raybiztech.projectnewsfeed.dto.ProjectFeedPostDto;
import com.raybiztech.projectnewsfeed.dto.ProjectFeedpostCommentsDto;
import com.raybiztech.recruitment.utils.FileUploaderUtility;

@Service("projectNewsService")
@Transactional
public class ProjectNewsFeedServiceImpl implements ProjectNewsService {
	@Autowired
	ProjecNewsFeedtDao projecNewsFeedDao;
	@Autowired
	ProjectFeedPostBuider projectFeedPostBuider;
	@Autowired
	ProjectFeedPostCommentsBuilder projectFeedPostCommentsBuilder;
	@Autowired
	PropBean propBean;

	@Override
	public List<ProjectFeedPostDto> getFeedPosts(Long projectid) {
		List<ProjectFeedPost> list = projecNewsFeedDao.getFeedPosts(projectid);
		return projectFeedPostBuider.toDtoList(list);
	}

	@Override
	public Long addFeedPost(ProjectFeedPostDto feedPostDto) {
		ProjectFeedPost projectFeedPost = projectFeedPostBuider
				.toEntity(feedPostDto);

		return (Long) projecNewsFeedDao.save(projectFeedPost);
	}

	@Override
	public void addFeedPostComments(Long feedpostid,
			ProjectFeedpostCommentsDto feedPostCommentsDto) {
		ProjectFeedPost projectFeedPost = projecNewsFeedDao.findBy(
				ProjectFeedPost.class, feedpostid);
		ProjectFeedPostComments projectFeedPostComments = projectFeedPostCommentsBuilder
				.toEntity(feedPostCommentsDto);
		projectFeedPostComments.setFeedPost(projectFeedPost);
		projecNewsFeedDao.save(projectFeedPostComments);
	}

	@Override
	public void uploadImage(MultipartFile mpf, String parameter) {
		Long postId = Long.parseLong(parameter);
		ProjectFeedPost feedPost = projecNewsFeedDao.findBy(
				ProjectFeedPost.class, postId);
		FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
		projecNewsFeedDao.update(feedPost);

		try {
			projecNewsFeedDao.update(fileUploaderUtility.uploadImage(mpf,
					feedPost, propBean));
		} catch (IOException ex) {
			ex.printStackTrace();
			projecNewsFeedDao.update(feedPost);
		}
	}

	@Override
	public List<ProjectFeedPostDto> getMilestoneNewsFeed(Long projectId,
			Long Milestone) {
		List<ProjectFeedPost> list = projecNewsFeedDao.getMilestoneNewFeed(
				projectId, Milestone);
		return projectFeedPostBuider.toDtoList(list);
	}

	@Override
	public List<ProjectFeedPostDto> getInvoiceNewsFeed(Long projectId,
			Long invoiceId) {
		List<ProjectFeedPost> list = projecNewsFeedDao.getInvoiceNewsFeed(
				projectId, invoiceId);
		return projectFeedPostBuider.toDtoList(list);
	}

}
