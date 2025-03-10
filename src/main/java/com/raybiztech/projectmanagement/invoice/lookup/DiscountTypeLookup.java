package com.raybiztech.projectmanagement.invoice.lookup;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class DiscountTypeLookup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8853295514653995910L;
	private Integer id;
	private String name;

	public DiscountTypeLookup() {

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
		if (obj instanceof DiscountTypeLookup) {
			final DiscountTypeLookup discountTypeLookup = (DiscountTypeLookup) obj;
			return new EqualsBuilder().append(this.getId(),
					discountTypeLookup.getId()).isEquals();
		}
		return false;

	}

	@Override
	public String toString() {
		return "DiscountTypeLookup [id=" + id + ", name=" + name + "]";
	}

}
