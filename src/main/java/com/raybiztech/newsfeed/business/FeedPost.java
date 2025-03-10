package com.raybiztech.newsfeed.business;

import java.io.Serializable;
import java.util.Set;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Second;

public class FeedPost implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1872037895571379015L;

	private Long id;
	
	private Employee postedBy;
	
	private String post;
	
	private Second postDate;
	
	private String  postImageData;
	
	private Set<FeedPostComments> feedpostcomments;
	
	
	public Long getId() {
		return id;
	}

	public Set<FeedPostComments> getFeedpostcomments() {
		return feedpostcomments;
	}

	public void setFeedpostcomments(Set<FeedPostComments> feedpostcomments) {
		this.feedpostcomments = feedpostcomments;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(Employee postedBy) {
		this.postedBy = postedBy;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	

	public Second getPostDate() {
		return postDate;
	}

	public void setPostDate(Second postDate) {
		this.postDate = postDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPostImageData() {
		return postImageData;
	}

	public void setPostImageData(String postImageData) {
		this.postImageData = postImageData;
	}
	
}
