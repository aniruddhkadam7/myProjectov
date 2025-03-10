package com.raybiztech.projectmanagement.invoice.lookup;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class PaymentTermLookup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2068298229810964831L;

	private Integer id;
	private String name;

	private Integer value;

	public PaymentTermLookup() {

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

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.getId())

		.toHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof PaymentTermLookup) {
			final PaymentTermLookup paymentTermLookup = (PaymentTermLookup) obj;
			return new EqualsBuilder().append(this.getId(),
					paymentTermLookup.getId()).isEquals();
		}
		return false;

	}

	@Override
	public String toString() {
		return "PaymentTermLookup [id=" + id + ", name=" + name + ", value="
				+ value + "]";
	}

}
