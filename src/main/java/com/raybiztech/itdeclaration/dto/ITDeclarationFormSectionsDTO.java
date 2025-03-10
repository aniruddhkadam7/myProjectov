package com.raybiztech.itdeclaration.dto;

import java.util.Set;

public class ITDeclarationFormSectionsDTO {
	private Long itSectionsId;
	private Long sectionId;
	private String sectionName;
	private Boolean isOld;
	private Long maxLimit;
	private Set<ITDeclarationFormInvestmentsDTO> formInvestmentDTO;

	public Long getItSectionsId() {
		return itSectionsId;
	}

	public void setItSectionsId(Long itSectionsId) {
		this.itSectionsId = itSectionsId;
	}

	public Boolean getIsOld() {
		return isOld;
	}

	public void setIsOld(Boolean isOld) {
		this.isOld = isOld;
	}

	public Set<ITDeclarationFormInvestmentsDTO> getFormInvestmentDTO() {
		return formInvestmentDTO;
	}

	public void setFormInvestmentDTO(
			Set<ITDeclarationFormInvestmentsDTO> formInvestmentDTO) {
		this.formInvestmentDTO = formInvestmentDTO;
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

	public Long getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(Long maxLimit) {
		this.maxLimit = maxLimit;
	}

	
}
