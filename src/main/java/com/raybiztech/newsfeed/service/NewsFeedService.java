package com.raybiztech.newsfeed.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.newsfeed.dto.FeedPostCommentsDto;
import com.raybiztech.newsfeed.dto.FeedPostDto;

public interface NewsFeedService {

	List<FeedPostDto> getFeedPosts();

	Long addFeedPost(FeedPostDto feedPostDto);

	void addFeedPostComments(Long feedpostid,
			FeedPostCommentsDto feedPostCommentsDtoDto);
	void uploadImage(MultipartFile mpf, String parameter);

	void deleteFeedPost(Long feedPostId);

}
