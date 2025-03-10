package com.raybiztech.itdeclaration.dto;

import java.io.Serializable;
import java.util.Set;

public class ITDeclarationFormDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long itDeclarationFormId;
	private Long employeeId;
	private String employeeName;
	private String panNumber;
	private String designation;
	//private FinanceCycleDTO cycleDTO;
	private Set<ITDeclarationFormSectionsDTO> formSectionsDTOs;
	private String organisationName;
	private String fromDate;
	private String toDate;
	private Boolean isAgree;
	private Long grandTotal;
	private String filePath;
	private Long cycleId;
	public Long getItDeclarationFormId() {
		return itDeclarationFormId;
	}
	public void setItDeclarationFormId(Long itDeclarationFormId) {
		this.itDeclarationFormId = itDeclarationFormId;
	}
	
	public String getOrganisationName() {
		return organisationName;
	}
	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}
	
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	/*public FinanceCycleDTO getCycleDTO() {
		return cycleDTO;
	}
	public void setCycleDTO(FinanceCycleDTO cycleDTO) {
		this.cycleDTO = cycleDTO;
	}*/
	public Boolean getIsAgree() {
		return isAgree;
	}
	public void setIsAgree(Boolean isAgree) {
		this.isAgree = isAgree;
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
	public Set<ITDeclarationFormSectionsDTO> getFormSectionsDTOs() {
		return formSectionsDTOs;
	}
	public void setFormSectionsDTOs(Set<ITDeclarationFormSectionsDTO> formSectionsDTOs) {
		this.formSectionsDTOs = formSectionsDTOs;
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
	public Long getCycleId() {
		return cycleId;
	}
	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}
}
