package com.raybiztech.projectmanagement.invoice.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class BankDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -523806189465734477L;

	public BankDTO() {

	}

	public BankDTO(Integer id, String name) {
		this.Id = id;
		this.name = name;

	}

	private Integer Id;

	private String name;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(Id)

		.toHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof BankDTO) {
			final BankDTO bankdto = (BankDTO) obj;
			return new EqualsBuilder().append(this.Id, bankdto.getId())
					.isEquals();
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "BankDTO [Id=" + Id + "]";
	}

}
