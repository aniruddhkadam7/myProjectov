package com.raybiztech.achievementNomination.dto;

import java.io.Serializable;
import java.util.Set;

public class NominationCycleDto implements Serializable {

	private static final long serialVersionUID = 2934946381851470818L;

	private Long id;
	private String cycleName;
	private String fromMonth;
	private String toMonth;
	private Boolean activateFlag;
	private String startDate;
	private String endDate;
	private Set<NominationQuestionDto> questionMappingDtos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCycleName() {
		return cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	public String getFromMonth() {
		return fromMonth;
	}

	public void setFromMonth(String fromMonth) {
		this.fromMonth = fromMonth;
	}

	public String getToMonth() {
		return toMonth;
	}

	public void setToMonth(String toMonth) {
		this.toMonth = toMonth;
	}

	public Boolean getActivateFlag() {
		return activateFlag;
	}

	public void setActivateFlag(Boolean activateFlag) {
		this.activateFlag = activateFlag;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Set<NominationQuestionDto> getQuestionMappingDtos() {
		return questionMappingDtos;
	}

	public void setQuestionMappingDtos(
			Set<NominationQuestionDto> questionMappingDtos) {
		this.questionMappingDtos = questionMappingDtos;
	}

}
