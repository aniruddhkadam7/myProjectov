package com.raybiztech.separation.dto;

import java.io.Serializable;

public class SeparationCommentsDTO implements Serializable {

	private static final long serialVersionUID = 2240634840758558529L;
	private Long commentId;
	private Long employeeId;
	private String employeeName;
	private String comments;
	private String relievingDate;
	private String createdDate;
	private String status;
	private String withdrawComments;

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getRelievingDate() {
		return relievingDate;
	}

	public void setRelievingDate(String relievingDate) {
		this.relievingDate = relievingDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWithdrawComments() {
		return withdrawComments;
	}

	public void setWithdrawComments(String withdrawComments) {
		this.withdrawComments = withdrawComments;
	}

}
