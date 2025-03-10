package com.raybiztech.separation.business;

import java.io.Serializable;
import java.util.Set;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

public class Separation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8682186648310294352L;

	private Long separationId;
	private Date resignationDate;
	private Date relievingDate;
	private Employee employee;
	private Set<SeparationComments> comments;
	private PrimaryReason primaryReason;
	private String reasonComments;
	private String employeeComments;
	private SeparationStatus status;
	private Set<ClearanceCertificate> certificate;
	private Second createdDate;
	private Boolean isRevoked;
	private Boolean isprocessInitiated;
	//private Date abscondedDate;
	private Date initiatedDate;
	
	



	public Boolean getIsRevoked() {
		return isRevoked;
	}

	public void setIsRevoked(Boolean isRevoked) {
		this.isRevoked = isRevoked;
	}

	public Long getSeparationId() {
		return separationId;
	}

	public void setSeparationId(Long separationId) {
		this.separationId = separationId;
	}

	public Date getResignationDate() {
		return resignationDate;
	}

	public void setResignationDate(Date resignationDate) {
		this.resignationDate = resignationDate;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getReasonComments() {
		return reasonComments;
	}

	public void setReasonComments(String reasonComments) {
		this.reasonComments = reasonComments;
	}

	public Date getRelievingDate() {
		return relievingDate;
	}

	public void setRelievingDate(Date relievingDate) {
		this.relievingDate = relievingDate;
	}

	public Set<SeparationComments> getComments() {
		return comments;
	}

	public void setComments(Set<SeparationComments> comments) {
		this.comments = comments;
	}

	public PrimaryReason getPrimaryReason() {
		return primaryReason;
	}

	public void setPrimaryReason(PrimaryReason primaryReason) {
		this.primaryReason = primaryReason;
	}

	public SeparationStatus getStatus() {
		return status;
	}

	public void setStatus(SeparationStatus status) {
		this.status = status;
	}

	public Set<ClearanceCertificate> getCertificate() {
		return certificate;
	}

	public void setCertificate(Set<ClearanceCertificate> certificate) {
		this.certificate = certificate;
	}

	public Second getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}

	public String getEmployeeComments() {
		return employeeComments;
	}

	public void setEmployeeComments(String employeeComments) {
		this.employeeComments = employeeComments;
	}

	public Boolean getIsprocessInitiated() {
		return isprocessInitiated;
	}

	public void setIsprocessInitiated(Boolean isprocessInitiated) {
		this.isprocessInitiated = isprocessInitiated;
	}

	/*public Date getAbscondedDate() {
		return abscondedDate;
	}

	public void setAbscondedDate(Date abscondedDate) {
		this.abscondedDate = abscondedDate;
	}
*/
	public Date getInitiatedDate() {
		return initiatedDate;
	}

	public void setInitiatedDate(Date initiatedDate) {
		this.initiatedDate = initiatedDate;
	}
	
	


	

}
