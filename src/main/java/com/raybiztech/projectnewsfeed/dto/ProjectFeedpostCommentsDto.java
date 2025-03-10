package com.raybiztech.projectnewsfeed.dto;

import com.raybiztech.appraisals.dto.EmployeeDTO;

public class ProjectFeedpostCommentsDto {

	private Long id;

	private EmployeeDTO employeeComment;

	private String comments;

	private String commentDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EmployeeDTO getEmployeeComment() {
		return employeeComment;
	}

	public void setEmployeeComment(EmployeeDTO employeeComment) {
		this.employeeComment = employeeComment;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}

}
