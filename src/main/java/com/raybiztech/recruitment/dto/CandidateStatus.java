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
public class CandidateStatus implements Serializable{

    private Long candidateId;
    private String status;
    private String statusComments;
    private String holdSubStatus;
    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusComments() {
        return statusComments;
    }

    public void setStatusComments(String statusComments) {
        this.statusComments = statusComments;
    }

	public String getHoldSubStatus() {
		return holdSubStatus;
	}

	public void setHoldSubStatus(String holdSubStatus) {
		this.holdSubStatus = holdSubStatus;
	}
    
    
}
