package com.raybiztech.recruitment.business;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.Status;

public class Interview implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 421188953947961583L;

    private Long InterviewId;

    private Set<Employee> interviewers;

    private Set<Candidate> candidates;

    private String result;

    private String comments;

    private Status status;

    private String round;

    private InterviewType interviewType;

    public Interview() {
        interviewers = new HashSet<Employee>();
    }

    public Long getInterviewId() {
        return InterviewId;
    }

    public void setInterviewId(Long InterviewId) {
        this.InterviewId = InterviewId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public String getComments() {
        return comments;
    }

    public InterviewType getInterviewType() {
        return interviewType;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setInterviewType(InterviewType interviewType) {
        this.interviewType = interviewType;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public Set<Employee> getInterviewers() {
        return interviewers;
    }

    public Set<Candidate> getCandidates() {
        return candidates;
    }

    public void setInterviewers(Set<Employee> interviewers) {
        this.interviewers = interviewers;
    }

    public void setCandidates(Set<Candidate> candidates) {
        this.candidates = candidates;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 13).append(InterviewId).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj instanceof Interview) {
            final Interview interview = (Interview) obj;
            return new EqualsBuilder().append(InterviewId, interview.getInterviewId()).isEquals();
        }
        return false;
    }

		
    

}
