/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.business;

import java.io.Serializable;

import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

/**
 *
 * @author naresh
 */
public class EmployeeFamilyInformation implements Serializable,
		Comparable<EmployeeFamilyInformation>, Cloneable {

	private static final long serialVersionUID = 1L;
	private Long familyId;
	public String personName;
	public String relationShip;
	public String contactNumber;
	public Employee employee;
	public Date dateOfBirth;
	public Long createdBy;
	public Long updatedBy;
	public Second createddate;
	public Second updatedDate;
	private Integer countryCodeContact;
	
	

	public Integer getCountryCodeContact() {
		return countryCodeContact;
	}

	public void setCountryCodeContact(Integer countryCodeContact) {
		this.countryCodeContact = countryCodeContact;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getRelationShip() {
		return relationShip;
	}

	public void setRelationShip(String relationShip) {
		this.relationShip = relationShip;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Long getFamilyId() {
		return familyId;
	}

	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
	}

	@Override
	public int compareTo(EmployeeFamilyInformation familyInformation) {
		return this.familyId.compareTo(familyInformation.getFamilyId());
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
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

	public Second getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Second createddate) {
		this.createddate = createddate;
	}

	public Second getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Second updatedDate) {
		this.updatedDate = updatedDate;
	}

}
