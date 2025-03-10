package com.raybiztech.projectnewsfeed.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.projectnewsfeed.dto.ProjectFeedPostDto;
import com.raybiztech.projectnewsfeed.dto.ProjectFeedpostCommentsDto;

public interface ProjectNewsService {

	List<ProjectFeedPostDto> getFeedPosts(Long projectid);

	Long addFeedPost(ProjectFeedPostDto feedPostDto);

	void addFeedPostComments(Long feedpostid,
			ProjectFeedpostCommentsDto feedPostCommentsDtoDto);

	void uploadImage(MultipartFile mpf, String parameter);

	List<ProjectFeedPostDto> getMilestoneNewsFeed(Long projectId, Long Milestone);

	List<ProjectFeedPostDto> getInvoiceNewsFeed(Long projectId, Long invoiceId);

}
