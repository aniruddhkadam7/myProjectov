package com.raybiztech.projectmanagement.invoice.dto;

import java.io.Serializable;

public class PaymentTermLookupDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5841929287810337728L;
	private Integer id;
	private String name;

	private Integer value;

	public PaymentTermLookupDTO() {

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


}
