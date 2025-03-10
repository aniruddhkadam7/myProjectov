package com.raybiztech.projectnewsfeed.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Second;

public class ProjectFeedPostComments implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1130376158199478436L;

	private Long id;

	private Employee employeeComment;

	private String comments;

	private Second commentDate;

	private ProjectFeedPost feedPost;

	public Long getId() {
		return id;
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

	public ProjectFeedPost getFeedPost() {
		return feedPost;
	}

	public void setFeedPost(ProjectFeedPost feedPost) {
		this.feedPost = feedPost;
	}

}
