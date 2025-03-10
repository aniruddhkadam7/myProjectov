package com.raybiztech.appraisals.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class KRADTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8205744616391996787L;

	private String kraName;

	private Long id;

	private String description;

	private Set<KPIDTO> kpis = new HashSet<KPIDTO>();

	private KraWithWeightageDTO kraWithWeightageDTO;

	public KRADTO() {
	}

	public KRADTO(String kraName, String description) {
		this.kraName = kraName;
		this.description = description;
	}

	public KRADTO(String kraName, Set<KPIDTO> kpis, String description) {
		this.kraName = kraName;
		this.kpis = kpis;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKraName() {
		return kraName;
	}

	public void setKraName(String kraName) {
		this.kraName = kraName;
	}

	public Set<KPIDTO> getKpis() {
		return kpis;
	}

	public void setKpis(Set<KPIDTO> kpis) {
		this.kpis = kpis;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public KraWithWeightageDTO getKraWithWeightageDTO() {
		return kraWithWeightageDTO;
	}

	public void setKraWithWeightageDTO(KraWithWeightageDTO kraWithWeightageDTO) {
		this.kraWithWeightageDTO = kraWithWeightageDTO;
	}
}
