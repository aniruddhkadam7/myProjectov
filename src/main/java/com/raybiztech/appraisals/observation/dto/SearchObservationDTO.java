/*
 * To change this license header; choose License Headers in Project Properties.
 * To change this template file; choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.observation.dto;

import java.io.Serializable;

import org.springframework.web.bind.annotation.RequestParam;

import com.raybiztech.date.Date;

/**
 *
 * @author hari
 */
public class SearchObservationDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// private Boolean checkEmpId;
	private Boolean checkEmpName;
	private Boolean checkAddedByName;
	private String searchString;
	private String selectionDates;
	private String fromDate;
	private String toDate;
	private Integer rating;
	private String selecteedMonthandYear;
	private Boolean notRatedFlag;
	// private Date observationMonth;
	private Integer startIndex;
	private Integer endIndex;
	private String monthStatus;
	private Integer fromMonthVal;
	private Integer fromYearVal;
	private Integer toMonthVal;
	private Integer toYearVal;
	private String employeeStatus;

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getSelectionDates() {
		return selectionDates;
	}

	public void setSelectionDates(String selectionDates) {
		this.selectionDates = selectionDates;
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

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public boolean isCheckEmpName() {
		return checkEmpName;
	}

	public void setCheckEmpName(boolean checkEmpName) {
		this.checkEmpName = checkEmpName;
	}

	public boolean isCheckAddedByName() {
		return checkAddedByName;
	}

	public void setCheckAddedByName(boolean checkAddedByName) {
		this.checkAddedByName = checkAddedByName;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getSelecteedMonthandYear() {
		return selecteedMonthandYear;
	}

	public void setSelecteedMonthandYear(String selecteedMonthandYear) {
		this.selecteedMonthandYear = selecteedMonthandYear;
	}

	public Boolean getNotRatedFlag() {
		return notRatedFlag;
	}

	public void setNotRatedFlag(Boolean notRatedFlag) {
		this.notRatedFlag = notRatedFlag;
	}

	/*
	 * public Date getObservationMonth() { return observationMonth; }
	 * 
	 * public void setObservationMonth(Date observationMonth) {
	 * this.observationMonth = observationMonth; }
	 */

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

	public String getMonthStatus() {
		return monthStatus;
	}

	public void setMonthStatus(String monthStatus) {
		this.monthStatus = monthStatus;
	}

	public Integer getFromMonthVal() {
		return fromMonthVal;
	}

	public void setFromMonthVal(Integer fromMonthVal) {
		this.fromMonthVal = fromMonthVal;
	}

	public Integer getFromYearVal() {
		return fromYearVal;
	}

	public void setFromYearVal(Integer fromYearVal) {
		this.fromYearVal = fromYearVal;
	}

	public Integer getToMonthVal() {
		return toMonthVal;
	}

	public void setToMonthVal(Integer toMonthVal) {
		this.toMonthVal = toMonthVal;
	}

	public Integer getToYearVal() {
		return toYearVal;
	}

	public void setToYearVal(Integer toYearVal) {
		this.toYearVal = toYearVal;
	}

	public String getEmployeeStatus() {
		return employeeStatus;
	}

	public void setEmployeeStatus(String employeeStatus) {
		this.employeeStatus = employeeStatus;
	}

}
