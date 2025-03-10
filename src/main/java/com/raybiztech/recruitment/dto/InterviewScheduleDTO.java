package com.raybiztech.recruitment.dto;

import java.io.Serializable;

import com.raybiztech.appraisals.dto.EmployeeDTO;

public class InterviewScheduleDTO extends ScheduleDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9143327237052953619L;
	private EmployeeDTO interviewer;
	private CandidateDTO candidate;
	private String round;
	private String result;
	private String comments;

	public EmployeeDTO getInterviewer() {
		return interviewer;
	}

	public void setInterviewer(EmployeeDTO interviewer) {
		this.interviewer = interviewer;
	}

	public CandidateDTO getCandidate() {
		return candidate;
	}

	public void setCandidate(CandidateDTO candidate) {
		this.candidate = candidate;
	}

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
