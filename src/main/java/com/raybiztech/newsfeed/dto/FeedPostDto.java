package com.raybiztech.newsfeed.dto;

import java.util.Set;

import com.raybiztech.appraisals.dto.EmployeeDTO;


public class FeedPostDto {
	private Long id;

	private EmployeeDTO postedBy;

	private String post;

	private String postDate;

	private String postImageData;
	
	private Set<FeedPostCommentsDto>  feedPostCommentsList; 

	public Long getId() {
		return id;
	}

	public Set<FeedPostCommentsDto> getFeedPostCommentsList() {
		return feedPostCommentsList;
	}

	public void setFeedPostCommentsList(
			Set<FeedPostCommentsDto> feedPostCommentsList) {
		this.feedPostCommentsList = feedPostCommentsList;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EmployeeDTO getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(EmployeeDTO postedBy) {
		this.postedBy = postedBy;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getPostImageData() {
		return postImageData;
	}

	public void setPostImageData(String postImageData) {
		this.postImageData = postImageData;
	}
	
	

}
