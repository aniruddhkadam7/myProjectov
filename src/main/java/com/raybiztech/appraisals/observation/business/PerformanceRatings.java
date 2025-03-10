package com.raybiztech.appraisals.observation.business;

import java.io.Serializable;

public class PerformanceRatings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4007545064914416231L;

	private Long id;
	private Integer rating;
	private String label;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
