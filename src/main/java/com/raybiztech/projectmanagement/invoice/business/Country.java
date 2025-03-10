package com.raybiztech.projectmanagement.invoice.business;

public enum Country {
	INDIA("INDIA"), AUS("AUS"), USA("USA");
	private String country;

	private Country(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

}
