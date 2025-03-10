package com.raybiztech.newsfeed.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Second;

public class FeedPostComments implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2786269996778823985L;

	private Long id;
	
	private Employee employeeComment;
	
	private String comments;
	
	private Second commentDate;
	
	private boolean numberOfLikes;
	
	private FeedPost feedPost;

	public Long getId() {
		return id;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isNumberOfLikes() {
		return numberOfLikes;
	}

	public void setNumberOfLikes(boolean numberOfLikes) {
		this.numberOfLikes = numberOfLikes;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Employee getEmployeeComment() {
		return employeeComment;
	}

	public void setEmployeeComment(Employee employeeComment) {
		this.employeeComment = employeeComment;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	


	public Second getCommentDate() {
		return commentDate;
	}


	public void setCommentDate(Second commentDate) {
		this.commentDate = commentDate;
	}


	public FeedPost getFeedPost() {
		return feedPost;
	}


	public void setFeedPost(FeedPost feedPost) {
		this.feedPost = feedPost;
	}

	
	

}
