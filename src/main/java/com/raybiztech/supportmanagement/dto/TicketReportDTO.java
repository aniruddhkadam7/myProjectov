package com.raybiztech.supportmanagement.dto;

public class TicketReportDTO {

	private String trackerName;
	private String categoryName;
	private String subCategoryName;
	private String noOfTickets;
	private String noOfClosedTickets;
	private String noOfPendingTickets;
	private Long categoryId;
	private Long subCategoryId;
	private Long trackerId;
	private String status;

	public String getTrackerName() {
		return trackerName;
	}

	public void setTrackerName(String trackerName) {
		this.trackerName = trackerName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	public String getNoOfTickets() {
		return noOfTickets;
	}

	public void setNoOfTickets(String noOfTickets) {
		this.noOfTickets = noOfTickets;
	}

	public String getNoOfClosedTickets() {
		return noOfClosedTickets;
	}

	public void setNoOfClosedTickets(String noOfClosedTickets) {
		this.noOfClosedTickets = noOfClosedTickets;
	}

	public String getNoOfPendingTickets() {
		return noOfPendingTickets;
	}

	public void setNoOfPendingTickets(String noOfPendingTickets) {
		this.noOfPendingTickets = noOfPendingTickets;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(Long subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public Long getTrackerId() {
		return trackerId;
	}

	public void setTrackerId(Long trackerId) {
		this.trackerId = trackerId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	

}
