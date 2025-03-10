package com.raybiztech.appraisals.dto;


public class SourceLookUpDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long SourceLookUpId;
	private String name;
	int displayOrder;

	public Long getSourceLookUpId() {
		return SourceLookUpId;
	}

	public void setSourceLookUpId(Long sourceLookUpId) {
		SourceLookUpId = sourceLookUpId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
}
