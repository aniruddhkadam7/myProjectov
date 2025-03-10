package com.raybiztech.appraisals.dto;

import java.io.Serializable;

public class RatingDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long employeeId;
	private Long cycleId;
	private Long kraId;
	private Long kpiId;
	private KPIRatingDTO kpiRatingDTO;

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getCycleId() {
		return cycleId;
	}

	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}

	public Long getKraId() {
		return kraId;
	}

	public void setKraId(Long kraId) {
		this.kraId = kraId;
	}

	public Long getKpiId() {
		return kpiId;
	}

	public void setKpiId(Long kpiId) {
		this.kpiId = kpiId;
	}

	public KPIRatingDTO getKpiRatingDTO() {
		return kpiRatingDTO;
	}

	public void setKpiRatingDTO(KPIRatingDTO kpiRatingDTO) {
		this.kpiRatingDTO = kpiRatingDTO;
	}

}
