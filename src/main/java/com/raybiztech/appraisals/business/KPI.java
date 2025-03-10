package com.raybiztech.appraisals.business;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class KPI implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long kpiId;

	private KRA kra;

	private String kpiName;

	private String description;

	private Set<KPIRating> kpiRatings = new HashSet<KPIRating>();

	public KPI() {
	}

	public KRA getKra() {
		return kra;
	}

	public void setKra(KRA kra) {
		this.kra = kra;
	}

	public KPI(String kpiName, String description) {
		this.kpiName = kpiName;
		this.description = description;
	}

	public Long KPIRating() {
		return kpiId;
	}

	public Long getKpiId() {
        return kpiId;
    }

    public void setKpiId(Long kpiId) {
        this.kpiId = kpiId;
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

	public Set<KPIRating> getKpiRatings() {
		return kpiRatings;
	}

	public void setKpiRatings(Set<KPIRating> kpiRatings) {
		this.kpiRatings = kpiRatings;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(1989, 55).append(kpiId).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final KPI other = (KPI) obj;
		return new EqualsBuilder().append(kpiId, other.kpiId).isEquals();
	}

}
