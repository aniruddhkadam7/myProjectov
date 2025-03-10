package com.raybiztech.appraisals.business;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Appraisal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long appraisalId;

	private Employee employee;
	private Cycle cycle;
	private String status;
	
	public Appraisal()
	{
		
	}

	

	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public Long getAppraisalId() {
		return appraisalId;
	}



	public void setAppraisalId(Long appraisalId) {
		this.appraisalId = appraisalId;
	}



	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Cycle getCycle() {
		return cycle;
	}

	public void setCycle(Cycle cycle) {
		this.cycle = cycle;
	}
	
    @Override
    public int hashCode() {
        return new HashCodeBuilder(1987, 57).append(appraisalId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Appraisal other = (Appraisal) obj;
        return new EqualsBuilder().append(appraisalId, other.appraisalId).isEquals();
    }
}
