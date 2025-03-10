package com.raybiztech.projectmanagement.invoice.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class RemittanceLocationDTO {

	private Long id;
	private String name;

	public RemittanceLocationDTO() {
	}

	public RemittanceLocationDTO(Long id, String name) {
		this.setId(id);
		this.setName(name);
	}

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

	@Override
	public int hashCode() {
		
		return new HashCodeBuilder().append(id)

				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RemittanceLocationDTO) {
			final RemittanceLocationDTO locationDTO= (RemittanceLocationDTO) obj;
			return new EqualsBuilder().append(this.id, locationDTO.getId())
					.isEquals();
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "LocationDTO [id=" + id + ", name=" + name + "]";
	}

}
