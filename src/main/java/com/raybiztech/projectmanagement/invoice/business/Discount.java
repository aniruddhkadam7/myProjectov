package com.raybiztech.projectmanagement.invoice.business;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Discount implements Serializable {

	private static final long serialVersionUID = 8889592808004328505L;
	private Long id;
	private DiscountType discountType;
	private String discountRate;
	private String discount;
	private String saltKey;

	public Discount() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}

	public String getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getSaltKey() {
		return saltKey;
	}

	public void setSaltKey(String saltKey) {
		this.saltKey = saltKey;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(1967, 13).append(this.getId()).hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Discount) {
			final Discount discount = (Discount) obj;
			return new EqualsBuilder().append(this.getId(), discount.getId())
					.isEquals();
		}
		return false;
	}

	@Override
	public String toString() {
		return "Discount [id=" + id + ", discountType=" + discountType
				+ ", discountRate=" + discountRate + ", discount=" + discount
				+ "]";
	}

}
