package com.raybiztech.projectmanagement.invoice.lookup;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class DurationLookup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5363940426941830329L;

	private Integer id;
	private String name;

	public DurationLookup() {

	}

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
		if (obj instanceof DurationLookup) {
			final DurationLookup durationLookup = (DurationLookup) obj;
			return new EqualsBuilder().append(this.getId(),
					durationLookup.getId()).isEquals();
		}
		return false;

	}

	@Override
	public String toString() {
		return "DurationLookup [id=" + id + ", name=" + name + "]";
	}

}
