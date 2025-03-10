package com.raybiztech.projectmanagement.invoice.lookup;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class TaxTypeLookup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3811802003629268613L;
	private Long id;
	private String name;
	private String taxRate;
	private String country;

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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
		return new HashCodeBuilder(1979, 13).append(this.getId())

		.toHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof TaxTypeLookup) {
			final TaxTypeLookup taxTypeLookup = (TaxTypeLookup) obj;
			return new EqualsBuilder().append(this.getId(),
					taxTypeLookup.getId()).isEquals();
		}
		return false;

	}

	@Override
	public String toString() {
		return "TaxTypeLookup [id=" + id + ", name=" + name + "]";
	}

}
