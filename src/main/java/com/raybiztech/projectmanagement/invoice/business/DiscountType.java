package com.raybiztech.projectmanagement.invoice.business;

public enum DiscountType {
	NONE("None"), PERCENTAGE("Percentage"), AMOUNT("Amount");
	private String discountType;

	private DiscountType(String discountType) {
		this.discountType = discountType;
	}

	public String getDiscountType() {
		return discountType;
	}
}
