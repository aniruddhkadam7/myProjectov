package com.raybiztech.projectmanagement.business;

import java.io.Serializable;
import java.util.Set;

public class Client implements Serializable {

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
	private Country country;
	private String phone;
	private String description;
	private String organization;
	private Set<Project> projects;
	private Boolean clientStatus;
	
	//As per the new recquirement we need GST Code for Clients
	private String gstCode;

	public Set<Project> getProjects() {
		return projects;
	}

	public Boolean getClientStatus() {
		return clientStatus;
	}

	public void setClientStatus(Boolean clientStatus) {
		this.clientStatus = clientStatus;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
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

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Client other = (Client) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Client{" + "id=" + id + ", name=" + name + ", address="
				+ address + ", personName=" + personName + ", email=" + email
				+ ", country=" + country + ", phone=" + phone
				+ ", description=" + description + ", organization="
				+ organization + '}';
	}

	public String getGstCode() {
		return gstCode;
	}

	public void setGstCode(String gstCode) {
		this.gstCode = gstCode;
	}
}
