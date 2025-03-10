package com.raybiztech.projectmanagement.invoice.lookup;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class CountryLookUp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -443323229170533549L;
	private Integer id;
	private String name;
	private String mobileCode;
	private String countryCode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.getId())

				.toHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof CountryLookUp) {
			final CountryLookUp countryLookUp = (CountryLookUp) obj;
			return new EqualsBuilder().append(this.getId(), countryLookUp.getId()).isEquals();
		}
		return false;

	}

	@Override
	public String toString() {
		return "DurationLookup [id=" + id + ", name=" + name + "]";
	}

	
	

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getMobileCode() {
		return mobileCode;
	}

	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}

}
