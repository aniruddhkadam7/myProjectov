package com.raybiztech.handbook.business;

import java.io.Serializable;

import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;

public class HandbookCountry implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public int id;
	public HandbookItem handbook;
	public CountryLookUp country;
	
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public HandbookItem getHandbook() {
		return handbook;
	}
	public void setHandbook(HandbookItem handbook) {
		this.handbook = handbook;
	}
	public CountryLookUp getCountry() {
		return country;
	}
	public void setCountry(CountryLookUp country) {
		this.country = country;
	}
	
	

}
