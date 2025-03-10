/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisalmanagement.dto;

import java.io.Serializable;
import java.util.List;

import com.raybiztech.date.Date;

/**
 *
 * @author anil
 */
public class SearchQueryParamsInAppraisalForm implements Serializable {

	private static final long serialVersionUID = -28879830710020L;

	private Long cycleId;
	private Long employeeID;
	private String departmentName;
	private String designationName;
	private String appraisalFormStatus;
	private String searchString;
	private String role;
	private Integer startIndex;
	private Integer endIndex;
	private String fromDate;
	private String toDate;
	private String empStatus;
	private List<Double> ratings;

	public Long getCycleId() {
		return cycleId;
	}

	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDesignationName() {
		return designationName;
	}

	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}

	public String getAppraisalFormStatus() {
		return appraisalFormStatus;
	}

	public void setAppraisalFormStatus(String appraisalFormStatus) {
		this.appraisalFormStatus = appraisalFormStatus;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}

	public Long getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(Long employeeID) {
		this.employeeID = employeeID;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public String getEmpStatus() {
		return empStatus;
	}

	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public List<Double> getRatings() {
		return ratings;
	}

	public void setRatings(List<Double> ratings) {
		this.ratings = ratings;
	}

}
