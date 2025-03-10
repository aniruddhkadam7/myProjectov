package com.raybiztech.projectmanagement.invoice.business;

public enum TaxType {
	SERVICE("SERVICE"), GST("GST"), NONE("NONE");
	private String taxType;

	private TaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getTaxType() {
		return taxType;
	}
}
