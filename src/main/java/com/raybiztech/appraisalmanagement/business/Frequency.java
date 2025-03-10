package com.raybiztech.appraisalmanagement.business;

import java.io.Serializable;

public class Frequency implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String frequencyname;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFrequencyname() {
		return frequencyname;
	}

	public void setFrequencyname(String frequencyname) {
		this.frequencyname = frequencyname;
	}

}
