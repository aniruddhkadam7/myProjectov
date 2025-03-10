package com.raybiztech.projectmanagement.invoice.lookup;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class InvoiceStatusLookup implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7278771579235296272L;
	private Integer id;
	private String name;

	public InvoiceStatusLookup() {

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
		if (obj instanceof InvoiceStatusLookup) {
			final InvoiceStatusLookup invoiceStatusLookup = (InvoiceStatusLookup) obj;
			return new EqualsBuilder().append(this.getId(),
					invoiceStatusLookup.getId()).isEquals();
		}
		return false;

	}

	@Override
	public String toString() {
		return "InvoiceStatusLookup [id=" + id + ", name=" + name + "]";
	}

}
