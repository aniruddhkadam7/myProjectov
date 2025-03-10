package com.raybiztech.recruitment.dto;

import java.io.Serializable;



public class PersonDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7840600698724079491L;

	Long personId;

	String firstName;
	String lastName;
	String middleName;
	String fullName;
	String mobile;
	String phone;
	String email;
	String qualification;
	String dob;
	String skypeId;
	String currentEmployer;
    String currentLocation;
    private String createdDate;

	

	public String getSkypeId() {
		return skypeId;
	}

	public void setSkypeId(String skypeId) {
		this.skypeId = skypeId;
	}

	public String getCurrentEmployer() {
		return currentEmployer;
	}

	public void setCurrentEmployer(String currentEmployer) {
		this.currentEmployer = currentEmployer;
	}

	public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}

	public String getDob() {
		return dob;
	}

	public AddressDTO getAddressDto() {
		return addressDto;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public void setAddressDto(AddressDTO addressDto) {
		this.addressDto = addressDto;
	}

	AddressDTO addressDto;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	

}
