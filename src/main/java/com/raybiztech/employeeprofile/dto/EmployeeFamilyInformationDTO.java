/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.employeeprofile.dto;

import java.io.Serializable;

/**
 *
 * @author naresh
 */
public class EmployeeFamilyInformationDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long familyId;
    public String personName;
    public String relationShip;
    public String contactNumber;
    public String dateOfBirth;
    private Long employeeId;
    private Integer countryCodeContact;

      
    public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
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

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Integer getCountryCodeContact() {
		return countryCodeContact;
	}

	public void setCountryCodeContact(Integer countryCodeContact) {
		this.countryCodeContact = countryCodeContact;
	}
    
}
