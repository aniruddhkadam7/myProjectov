package com.raybiztech.leavemanagement.dto;

import java.io.Serializable;
import java.text.ParseException;

import com.raybiztech.date.DateRange;
import com.raybiztech.leavemanagement.business.LeaveStatus;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;

public class SearchCriteriaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6284485098647980357L;
	private String from;
	private String to;
	private Long member;
	private LeaveStatus status;
	private Long managerId;
	private String teamMember;

	public Long getMember() {
		return member;
	}

	public void setMember(Long member) {
		this.member = member;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public LeaveStatus getStatus() {
		return status;
	}

	public void setStatus(LeaveStatus status) {
		this.status = status;
	}

	public String getTeamMember() {
		return teamMember;
	}

	public void setTeamMember(String teamMember) {
		this.teamMember = teamMember;
	}

	public DateRange getPeriod() throws ParseException {
		return new DateRange(DateParser.toDate(this.from), DateParser.toDate(this.to));
	}
}
