package com.raybiztech.projectmanagement.dto;

import java.io.Serializable;

public class ClientDTO implements Serializable {

	/**
     *
     */
	private static final long serialVersionUID = -1765030640445049529L;
	private Long id;
	private String clientCode;
	
	private String name;
	private String address;
	private String personName;
	private String email;
	private String country;
	private String phone;
	private String description;
	private String organization;
	private int totalFixedBids;
	private int totalRetainers;
	private Boolean clientStatus;
	
	public Boolean getClientStatus() {
		return clientStatus;
	}

	public void setClientStatus(Boolean clientStatus) {
		this.clientStatus = clientStatus;
	}

	//As per the new recquirement we need GST Code for Clients
	private String gstCode;
	
	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}


	public int getTotalFixedBids() {
		return totalFixedBids;
	}

	public void setTotalFixedBids(int totalFixedBids) {
		this.totalFixedBids = totalFixedBids;
	}

	public int getTotalRetainers() {
		return totalRetainers;
	}

	public void setTotalRetainers(int totalRetainers) {
		this.totalRetainers = totalRetainers;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getGstCode() {
		return gstCode;
	}

	public void setGstCode(String gstCode) {
		this.gstCode = gstCode;
	}

}
