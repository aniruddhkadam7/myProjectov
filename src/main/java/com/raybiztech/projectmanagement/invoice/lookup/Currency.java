package com.raybiztech.projectmanagement.invoice.lookup;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Currency implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6561128865587386066L;

	private Long id;
	private String type;

	public Currency() {

	}

	

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.getId())

		.toHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Currency) {
			final Currency currency = (Currency) obj;
			return new EqualsBuilder().append(this.getId(), currency.getId())
					.isEquals();
		}
		return false;

	}

	@Override
	public String toString() {
		return "Currency [id=" + id + ", type=" + type + "]";
	}

}
