package com.raybiztech.projectmanagement.invoice.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class RemittanceDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2635549451265944784L;

	private Integer id;
	private String bankName;
	private String currencyType;
	private String location;
	private String wireTransferInstructions;
	private String clientName;
	private Long client;

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

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Long getClient() {
		return client;
	}

	public void setClient(Long client) {
		this.client = client;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id)

		.toHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof RemittanceDTO) {
			final RemittanceDTO remittancedto = (RemittanceDTO) obj;
			return new EqualsBuilder().append(this.id, remittancedto.getId())
					.isEquals();
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "RemittanceDTO [id=" + id + "]";
	}

}
