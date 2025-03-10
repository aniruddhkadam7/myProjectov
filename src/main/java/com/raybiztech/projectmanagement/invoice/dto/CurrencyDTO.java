package com.raybiztech.projectmanagement.invoice.dto;

import java.io.Serializable; 

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class CurrencyDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -753611906358665737L;

	private Long id;
	private String type;

	public CurrencyDTO() {

	}

	public CurrencyDTO(Long id, String type) {
		this.setId(id);
		this.setType(type);
	}

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

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id)

		.toHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof CurrencyDTO) {
			final CurrencyDTO currencydto = (CurrencyDTO) obj;
			return new EqualsBuilder().append(this.id, currencydto.getId())
					.isEquals();
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "CurrencyDTO [id=" + id + "]";
	}

}
