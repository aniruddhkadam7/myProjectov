package com.raybiztech.itdeclaration.dto;

public class InvestmentDTO {
	private Long investmentId;
	private String investmentName;
	private Long maxLimit;
	private String description;
	private String requiredDocs;
	private Long sectionId;
	private String sectionName;
	public Long getInvestmentId() {
		return investmentId;
	}

	public void setInvestmentId(Long investmentId) {
		this.investmentId = investmentId;
	}

	public String getInvestmentName() {
		return investmentName;
	}

	public void setInvestmentName(String investmentName) {
		this.investmentName = investmentName;
	}

	public Long getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(Long maxLimit) {
		this.maxLimit = maxLimit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRequiredDocs() {
		return requiredDocs;
	}

	public void setRequiredDocs(String requiredDocs) {
		this.requiredDocs = requiredDocs;
	}

	public Long getSectionId() {
		return sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
}
