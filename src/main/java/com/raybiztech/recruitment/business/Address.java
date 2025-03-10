package com.raybiztech.recruitment.business;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Address implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1066173451380340238L;
	private Long addressId;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String county;
	private String zip;

	public Address() {
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}


	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getAddress1() {
		return address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 13).append(addressId).toHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Address) {
			final Address address = (Address) obj;
			return new EqualsBuilder()
					.append(addressId, address.getAddressId()).isEquals();
		}
		else
		{
			return false;
		}
	}
}
