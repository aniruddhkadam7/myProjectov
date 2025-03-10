package com.raybiztech.projectmanagement.invoice.lookup;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class RemittanceLocation implements Serializable {

	/**
	 * shashank
	 */
	private static final long serialVersionUID = -3616044912603204328L;

	private Long id;
	private String name;

	public RemittanceLocation() {
	}

	public RemittanceLocation(Long id, String name) {
		this.id = id;
		this.name = name;
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

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(this.getId()).toHashCode();

	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof RemittanceLocation) {
			final RemittanceLocation Location = (RemittanceLocation) obj;
			return new EqualsBuilder().append(this.getId(), Location.getId())
					.isEquals();
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", name=" + name + "]";
	}

}
