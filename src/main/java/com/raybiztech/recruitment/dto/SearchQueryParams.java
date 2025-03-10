/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.dto;

import java.io.Serializable;

/**
 *
 * @author naresh
 */
public class SearchQueryParams implements Serializable {

	/**
     *
     */
	private static final long serialVersionUID = -2887983957133334420L;
	private String selectionStatus;
	private String fromDate;
	private String toDate;
	private Integer startIndex;
	private Integer endIndex;
	private String searchByCandidateName;
	private String searchByRecruiterName;
	private String searchBySourceName;
	private String searchByMultipleFlag;
	private String candidateStatus;
	private String selectionTechnology;
	private String searchByExperience;
	private Integer selectionCountry;
	
	
	
	
	public Integer getSelectionCountry() {
		return selectionCountry;
	}

	public void setSelectionCountry(Integer selectionCountry) {
		this.selectionCountry = selectionCountry;
	}

	public String getSearchByMultipleFlag() {
		return searchByMultipleFlag;
	}

	public void setSearchByMultipleFlag(String searchByMultipleFlag) {
		this.searchByMultipleFlag = searchByMultipleFlag;
	}

	public String getSearchByCandidateName() {
		return searchByCandidateName;
	}

	public void setSearchByCandidateName(String searchByCandidateName) {
		this.searchByCandidateName = searchByCandidateName;
	}

	public String getSearchByRecruiterName() {
		return searchByRecruiterName;
	}

	public void setSearchByRecruiterName(String searchByRecruiterName) {
		this.searchByRecruiterName = searchByRecruiterName;
	}

	public String getCandidateStatus() {
		return candidateStatus;
	}

	public void setCandidateStatus(String candidateStatus) {
		this.candidateStatus = candidateStatus;
	}

	public String getSelectionStatus() {
		return selectionStatus;
	}

	public void setSelectionStatus(String selectionStatus) {
		this.selectionStatus = selectionStatus;
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

	

    public String getSelectionTechnology() {
        return selectionTechnology;
    }

    public void setSelectionTechnology(String selectionTechnology) {
        this.selectionTechnology = selectionTechnology;
    }

	public String getSearchBySourceName() {
		return searchBySourceName;
	}

	public void setSearchBySourceName(String searchBySourceName) {
		this.searchBySourceName = searchBySourceName;
	}

	public String getSearchByExperience() {
		return searchByExperience;
	}

	public void setSearchByExperience(String searchByExperience) {
		this.searchByExperience = searchByExperience;
	}
	

}
