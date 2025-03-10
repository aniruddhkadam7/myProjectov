package com.raybiztech.separation.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

public class SeparationComments implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -167970026272523045L;
	private Long commentId;
	private Employee employee;
	private String comments;
	private SeparationStatus status;
	private Date relievingDate;
	private Second createdDate;
	private String withdrawComments;

	public Date getRelievingDate() {
		return relievingDate;
	}

	public void setRelievingDate(Date relievingDate) {
		this.relievingDate = relievingDate;
	}

	

	public Second getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public SeparationStatus getStatus() {
		return status;
	}

	public void setStatus(SeparationStatus status) {
		this.status = status;
	}

	public String getWithdrawComments() {
		return withdrawComments;
	}

	public void setWithdrawComments(String withdrawComments) {
		this.withdrawComments = withdrawComments;
	}

}
