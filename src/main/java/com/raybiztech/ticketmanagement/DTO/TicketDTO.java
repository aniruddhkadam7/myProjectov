package com.raybiztech.ticketmanagement.DTO;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.swing.text.StyledEditorKit.BoldAction;

import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.date.Date;

public class TicketDTO implements Serializable {

	/**
     *
     */
	private static final long serialVersionUID = 6137861600423330305L;

	private List<Long> employeeIds;
	private String raisedDate;
	private String ticketStatus;
	private Long ticketNumber;
	private Long employeeCount;
	private Long mealId;
	private String mealName;
	private String managerName;
	private Boolean genarateType;
	private List<String> employeeNames;
	private String employeeNamesList;
	private Long totalEmployees;
	private String isApproved;
	

	public String getEmployeeNamesList() {
		return employeeNamesList;
	}

	public void setEmployeeNamesList(String employeeNamesList) {
		this.employeeNamesList = employeeNamesList;
	}

	public List<String> getEmployeeNames() {
		return employeeNames;
	}

	public void setEmployeeNames(List<String> employeeNames) {
		this.employeeNames = employeeNames;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getMealName() {
		return mealName;
	}

	public void setMealName(String mealName) {
		this.mealName = mealName;
	}

	public String getRaisedDate() {
		return raisedDate;
	}

	public void setRaisedDate(String raisedDate) {
		this.raisedDate = raisedDate;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public Long getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(Long ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public Long getEmployeeCount() {
		return employeeCount;
	}

	public void setEmployeeCount(Long employeeCount) {
		this.employeeCount = employeeCount;
	}

	public Long getMealId() {
		return mealId;
	}

	public void setMealId(Long mealId) {
		this.mealId = mealId;
	}

	public List<Long> getEmployeeIds() {
		return employeeIds;
	}

	public void setEmployeeIds(List<Long> employeeIds) {
		this.employeeIds = employeeIds;
	}

	public Boolean getGenarateType() {
		return genarateType;
	}

	public void setGenarateType(Boolean genarateType) {
		this.genarateType = genarateType;
	}

	public Long getTotalEmployees() {
		return totalEmployees;
	}

	public void setTotalEmployees(Long totalEmployees) {
		this.totalEmployees = totalEmployees;
	}

	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}



}
