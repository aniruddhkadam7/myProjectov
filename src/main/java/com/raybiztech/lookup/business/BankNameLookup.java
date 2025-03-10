package com.raybiztech.lookup.business;

import java.io.Serializable;

public class BankNameLookup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1901856719403580139L;

	private Long bankId;
	private String bankName;

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bankName == null) ? 0 : bankName.hashCode());
		result = prime * result + ((bankId == null) ? 0 : bankId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BankNameLookup other = (BankNameLookup) obj;
		if (bankName == null) {
			if (other.bankName != null)
				return false;
		} else if (!bankName.equals(other.bankName))
			return false;
		if (bankId == null) {
			if (other.bankId != null)
				return false;
		} else if (!bankId.equals(other.bankId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BankNameLookup [bankId=" + bankId + ", BankName=" + bankName
				+ "]";
	}

}