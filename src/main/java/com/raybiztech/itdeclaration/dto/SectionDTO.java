package com.raybiztech.itdeclaration.dto;

import java.util.List;

public class SectionDTO {
	private Long sectionId;
	private String sectionName;
	private Long sectionLimit;
	private List<InvestmentDTO> invests ;

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

	public Long getSectionLimit() {
		return sectionLimit;
	}

	public void setSectionLimit(Long sectionLimit) {
		this.sectionLimit = sectionLimit;
	}

	public List<InvestmentDTO> getInvests() {
		return invests;
	}

	public void setInvests(List<InvestmentDTO> invests) {
		this.invests = invests;
	}

	

	
}
