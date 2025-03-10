package com.raybiztech.mailtemplates.business;

import java.io.Serializable;

public class MailTemplateType implements Serializable {

	/**
	 * 
	 * @author shashank
	 * 
	 */
	private static final long serialVersionUID = -802144885058144846L;

	private Long id;
	private String name;

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

}
