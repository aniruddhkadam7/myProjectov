package com.raybiztech.recruitment.dto;

import java.io.Serializable;

public class JoinCandidateScheduleDTO extends ScheduleDTO implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2051354103069759657L;
	private CandidateDTO candidateDto;

	private String role;

	public CandidateDTO getCandidateDto() {
		return candidateDto;
	}

	public void setCandidateDto(CandidateDTO candidateDto) {
		this.candidateDto = candidateDto;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
