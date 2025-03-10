package com.raybiztech.projectmanagement.invoice.dto;

import java.io.Serializable;

public class InvoiceStatusLookupDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3952221132638219832L;
	private Integer id;
	private String name;

	public InvoiceStatusLookupDTO() {

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


}
