package com.raybiztech.leavemanagement.dto;

import java.io.Serializable;

public class LeaveCategorySummaryDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1725419394901590176L;
	private Long id;
	private LeaveCategoryDTO leaveCategoryDTO;
	private Double daysPending = 0.0;
	private Double daysTaken = 0.0;
	private Double daysScheduled = 0.0;
	private Double daysCancelAfterApprovalPending = 0.0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getDaysPending() {
		return daysPending;
	}

	public void setDaysPending(Double daysPending) {
		this.daysPending = daysPending;
	}

	public Double getDaysTaken() {
		return daysTaken;
	}

	public void setDaysTaken(Double daysTaken) {
		this.daysTaken = daysTaken;
	}

	public Double getDaysScheduled() {
		return daysScheduled;
	}

	public void setDaysScheduled(Double daysScheduled) {
		this.daysScheduled = daysScheduled;
	}

	public LeaveCategoryDTO getLeaveCategoryDTO() {
		return leaveCategoryDTO;
	}

	public void setLeaveCategoryDTO(LeaveCategoryDTO leaveCategoryDTO) {
		this.leaveCategoryDTO = leaveCategoryDTO;
	}

	public Double getDaysCancelAfterApprovalPending() {
		return daysCancelAfterApprovalPending;
	}

	public void setDaysCancelAfterApprovalPending(
			Double daysCancelAfterApprovalPending) {
		this.daysCancelAfterApprovalPending = daysCancelAfterApprovalPending;
	}

	@Override
	public String toString() {
		return "LeaveCategorySummary is [id=" + id + ", leaveCategory="
				+ leaveCategoryDTO.getName() + ", daysPending=" + daysPending
				+ ", daysTaken=" + daysTaken + "]";
	}

}
