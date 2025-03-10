/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.business;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;

/**
 *
 * @author naresh
 */
public class CandidateInterviewCycle implements Serializable,Cloneable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2515978745874943467L;
	private Long interviewCycleId;
    private Candidate candidate;
    private String interviewers;
    private Date interviewDate;
    private String interviewTime;
    private String interviewComments;
    private Long interviewRound;
    private String interviewStatus;
    private Set<Employee> employeeList;
    private String status;
    private String rating;
    private String description;
    private String interviewResultStatus;
    private String updatedBy;
    private String recruiter;
    private Date commentedDate;
    private String commentedTime;
    
    
    public Date getCommentedDate() {
		return commentedDate;
	}

	public void setCommentedDate(Date commentedDate) {
		this.commentedDate = commentedDate;
	}

	public String getCommentedTime() {
		return commentedTime;
	}

	public void setCommentedTime(String commentedTime) {
		this.commentedTime = commentedTime;
	}

	public String getInterviewers() {
        return interviewers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInterviewers(String interviewers) {
        this.interviewers = interviewers;
    }

    public Long getInterviewCycleId() {
        return interviewCycleId;
    }

    public void setInterviewCycleId(Long interviewCycleId) {
        this.interviewCycleId = interviewCycleId;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Date getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(Date interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(String interviewTime) {
        this.interviewTime = interviewTime;
    }

    public String getInterviewComments() {
        return interviewComments;
    }

    public void setInterviewComments(String interviewComments) {
        this.interviewComments = interviewComments;
    }

    public Long getInterviewRound() {
        return interviewRound;
    }

    public void setInterviewRound(Long interviewRound) {
        this.interviewRound = interviewRound;
    }

    public String getInterviewStatus() {
        return interviewStatus;
    }

    public void setInterviewStatus(String interviewStatus) {
        this.interviewStatus = interviewStatus;
    }

    public Set<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(Set<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1997, 13).append(interviewCycleId).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof CandidateInterviewCycle) {
            CandidateInterviewCycle interviewCycle = (CandidateInterviewCycle) obj;
            return new EqualsBuilder()
                    .append(interviewCycleId, interviewCycle.getInterviewCycleId()).isEquals();
        }
        return false;
    }
    @Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getInterviewResultStatus() {
		return interviewResultStatus;
	}

	public void setInterviewResultStatus(String interviewResultStatus) {
		this.interviewResultStatus = interviewResultStatus;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getRecruiter() {
		return recruiter;
	}

	public void setRecruiter(String recruiter) {
		this.recruiter = recruiter;
	}

	
}
