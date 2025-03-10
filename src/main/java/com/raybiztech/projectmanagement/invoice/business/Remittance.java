package com.raybiztech.projectmanagement.invoice.business;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Remittance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7094062681536229647L;

	private Integer id;
	private String bankName;
	private String currencyType;
	private String location;
	private String wireTransferInstructions;
	private Long client;

	public Remittance() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getWireTransferInstructions() {
		return wireTransferInstructions;
	}

	public void setWireTransferInstructions(String wireTransferInstructions) {
		this.wireTransferInstructions = wireTransferInstructions;
	}

	public Long getClient() {
		return client;
	}

	public void setClient(Long client) {
		this.client = client;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.getId()).toHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Remittance) {
			final Remittance remittance = (Remittance) obj;
			return new EqualsBuilder().append(this.getId(), remittance.getId())
					.isEquals();
		}
		return false;

	}

}
