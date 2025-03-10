package com.raybiztech.lookup.business;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SkillLookUp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3559009240369531519L;

	/**
	 * 
	 */

	private Long id;

	private String name;

	private Integer diplayOrder;

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

	public Integer getDiplayOrder() {
		return diplayOrder;
	}

	public void setDiplayOrder(Integer diplayOrder) {
		this.diplayOrder = diplayOrder;
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(177, 1889).append(id).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SkillLookUp other = (SkillLookUp) obj;
		return new EqualsBuilder().append(other.id, id).isEquals();
	}

}
