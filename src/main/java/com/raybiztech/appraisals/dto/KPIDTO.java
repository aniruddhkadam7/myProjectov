package com.raybiztech.appraisals.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class KPIDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7841959542626696525L;

	private Long id;

	private KRADTO kradto;

	private String kpiName;

	private String description;
	
	private Set<KPIRatingDTO> kpiRatingDTOs = new HashSet<KPIRatingDTO>();

	public KPIDTO() {
	}

	public KRADTO getKradto() {
        return kradto;
    }

    public void setKradto(KRADTO kradto) {
        this.kradto = kradto;
    }

    public KPIDTO(String kpiName, String description) {
		this.kpiName = kpiName;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<KPIRatingDTO> getKpiRatingDTOs() {
		return kpiRatingDTOs;
	}

	public void setKpiRatingDTOs(Set<KPIRatingDTO> kpiRatingDTOs) {
		this.kpiRatingDTOs = kpiRatingDTOs;
	}

}
