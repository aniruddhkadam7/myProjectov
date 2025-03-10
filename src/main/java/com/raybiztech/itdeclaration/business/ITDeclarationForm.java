package com.raybiztech.itdeclaration.business;

import java.io.Serializable;
import java.util.Set;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

public class ITDeclarationForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long itDeclarationFormId;
	private Employee employee;
	private FinanceCycle financeCycle;
	private Set<ITDeclarationFormSections> itSections;
	private String organisationName;
	private Date fromDate;
	private Date toDate;
	private Boolean isAgree;
	private Long createdBy;
	private Second createdDate;
	private Long updatedBy; 
	private Second updatedDate;
	private Long grandTotal;
	private String filePath;
	
	
	public Long getItDeclarationFormId() {
		return itDeclarationFormId;
	}
	public void setItDeclarationFormId(Long itDeclarationFormId) {
		this.itDeclarationFormId = itDeclarationFormId;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public FinanceCycle getFinanceCycle() {
		return financeCycle;
	}
	public void setFinanceCycle(FinanceCycle financeCycle) {
		this.financeCycle = financeCycle;
	}
	public String getOrganisationName() {
		return organisationName;
	}
	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Boolean getIsAgree() {
		return isAgree;
	}
	public void setIsAgree(Boolean isAgree) {
		this.isAgree = isAgree;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	
	public Long getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Second getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Second updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Set<ITDeclarationFormSections> getItSections() {
		return itSections;
	}
	public void setItSections(Set<ITDeclarationFormSections> itSections) {
		this.itSections = itSections;
	}
	public Second getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result
				+ ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result
				+ ((employee == null) ? 0 : employee.hashCode());
		result = prime * result
				+ ((financeCycle == null) ? 0 : financeCycle.hashCode());
		result = prime * result
				+ ((fromDate == null) ? 0 : fromDate.hashCode());
		result = prime * result + ((isAgree == null) ? 0 : isAgree.hashCode());
		result = prime
				* result
				+ ((itDeclarationFormId == null) ? 0 : itDeclarationFormId
						.hashCode());
		result = prime * result
				+ ((itSections == null) ? 0 : itSections.hashCode());
		result = prime
				* result
				+ ((organisationName == null) ? 0 : organisationName.hashCode());
		result = prime * result + ((toDate == null) ? 0 : toDate.hashCode());
		result = prime * result
				+ ((updatedBy == null) ? 0 : updatedBy.hashCode());
		result = prime * result
				+ ((updatedDate == null) ? 0 : updatedDate.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ITDeclarationForm other = (ITDeclarationForm) obj;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (employee == null) {
			if (other.employee != null)
				return false;
		} else if (!employee.equals(other.employee))
			return false;
		if (financeCycle == null) {
			if (other.financeCycle != null)
				return false;
		} else if (!financeCycle.equals(other.financeCycle))
			return false;
		if (fromDate == null) {
			if (other.fromDate != null)
				return false;
		} else if (!fromDate.equals(other.fromDate))
			return false;
		if (isAgree == null) {
			if (other.isAgree != null)
				return false;
		} else if (!isAgree.equals(other.isAgree))
			return false;
		if (itDeclarationFormId == null) {
			if (other.itDeclarationFormId != null)
				return false;
		} else if (!itDeclarationFormId.equals(other.itDeclarationFormId))
			return false;
		if (itSections == null) {
			if (other.itSections != null)
				return false;
		} else if (!itSections.equals(other.itSections))
			return false;
		if (organisationName == null) {
			if (other.organisationName != null)
				return false;
		} else if (!organisationName.equals(other.organisationName))
			return false;
		if (toDate == null) {
			if (other.toDate != null)
				return false;
		} else if (!toDate.equals(other.toDate))
			return false;
		if (updatedBy == null) {
			if (other.updatedBy != null)
				return false;
		} else if (!updatedBy.equals(other.updatedBy))
			return false;
		if (updatedDate == null) {
			if (other.updatedDate != null)
				return false;
		} else if (!updatedDate.equals(other.updatedDate))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ITDeclarationForm [itDeclarationFormId=" + itDeclarationFormId
				+ ", employee=" + employee + ", financeCycle=" + financeCycle
				+ ", itSections=" + itSections + ", organisationName="
				+ organisationName + ", fromDate=" + fromDate + ", toDate="
				+ toDate + ", isAgree=" + isAgree + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", updatedBy=" + updatedBy
				+ ", updatedDate=" + updatedDate + "]";
	}
	public Long getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(Long grandTotal) {
		this.grandTotal = grandTotal;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	
	

	
}
