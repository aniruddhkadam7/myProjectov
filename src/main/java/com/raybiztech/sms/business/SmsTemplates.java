package com.raybiztech.sms.business;

import java.io.Serializable;

public class SmsTemplates implements Serializable {

	/**
	 * shashank
	 */
	private static final long serialVersionUID = 1051118915653992070L;

	private Long id;
	private String type;
	private String template;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}
