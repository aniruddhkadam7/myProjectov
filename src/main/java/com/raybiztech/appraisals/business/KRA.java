package com.raybiztech.appraisals.business;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class KRA implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long kraId;

	private String kraName;

	private String description;
	
	private KraWithWeightage kraWithWeightage;

	private Set<KPI> kpis = new HashSet<KPI>();

	public KRA() {
	}

	public KRA(String kraName) {
		this.kraName = kraName;
	}

	public KRA(String kraName, String description) {
		this.kraName = kraName;
		this.description = description;
	}

	public KRA(String kraName, Set<KPI> kpis) {
		this.kraName = kraName;
		this.kpis = kpis;
	}

	public KRA(String kraName, String description, Set<KPI> kpis) {
		this(kraName, description);
		this.kpis = kpis;
	}

	public Long getKraId() {
		return kraId;
	}

	public void setKraId(Long kraId) {
		this.kraId = kraId;
	}

	public String getKraName() {
		return kraName;
	}

	public void setKraName(String kraName) {
		this.kraName = kraName;
	}

	public Set<KPI> getKpis() {
		return kpis;
	}

	public void setKpis(Set<KPI> kpis) {
		this.kpis = kpis;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(1989, 55).append(kraId).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final KRA other = (KRA) obj;
		return new EqualsBuilder().append(kraId, other.kraId).isEquals();
	}

	public KraWithWeightage getKraWithWeightage() {
		return kraWithWeightage;
	}

	public void setKraWithWeightage(KraWithWeightage kraWithWeightage) {
		this.kraWithWeightage = kraWithWeightage;
	}
}
