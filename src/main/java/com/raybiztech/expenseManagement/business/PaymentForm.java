package com.raybiztech.expenseManagement.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

public class PaymentForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long Id;
	private ExpenseForm expenseId;
	private String modeOfPayment;
	private String paidAmount;
	private String chequeNumber;
	private Date chequeDate;
	private Date rtgsDate;
	private String status;
	private String saltKey;
	private Employee createdBy;
	private Second createdDate;
	
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public ExpenseForm getExpenseId() {
		return expenseId;
	}
	public void setExpenseId(ExpenseForm expenseId) {
		this.expenseId = expenseId;
	}
	public String getModeOfPayment() {
		return modeOfPayment;
	}
	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}
	public String getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getChequeNumber() {
		return chequeNumber;
	}
	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}
	public Date getChequeDate() {
		return chequeDate;
	}
	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}
	public Date getRtgsDate() {
		return rtgsDate;
	}
	public void setRtgsDate(Date rtgsDate) {
		this.rtgsDate = rtgsDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSaltKey() {
		return saltKey;
	}
	public void setSaltKey(String saltKey) {
		this.saltKey = saltKey;
	}
	public Employee getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Employee createdBy) {
		this.createdBy = createdBy;
	}
	public Second getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}
	@Override
	public String toString() {
		return "PaymentForm [Id=" + Id + ", expenseId=" + expenseId + ", modeOfPayment=" + modeOfPayment
				+ ", paidAmount=" + paidAmount + ", chequeNumber=" + chequeNumber + ", chequeDate=" + chequeDate
				+ ", rtgsDate=" + rtgsDate + ", status=" + status + ", saltKey=" + saltKey + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Id == null) ? 0 : Id.hashCode());
		result = prime * result + ((chequeDate == null) ? 0 : chequeDate.hashCode());
		result = prime * result + ((chequeNumber == null) ? 0 : chequeNumber.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((expenseId == null) ? 0 : expenseId.hashCode());
		result = prime * result + ((modeOfPayment == null) ? 0 : modeOfPayment.hashCode());
		result = prime * result + ((paidAmount == null) ? 0 : paidAmount.hashCode());
		result = prime * result + ((rtgsDate == null) ? 0 : rtgsDate.hashCode());
		result = prime * result + ((saltKey == null) ? 0 : saltKey.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		PaymentForm other = (PaymentForm) obj;
		if (Id == null) {
			if (other.Id != null)
				return false;
		} else if (!Id.equals(other.Id))
			return false;
		if (chequeDate == null) {
			if (other.chequeDate != null)
				return false;
		} else if (!chequeDate.equals(other.chequeDate))
			return false;
		if (chequeNumber == null) {
			if (other.chequeNumber != null)
				return false;
		} else if (!chequeNumber.equals(other.chequeNumber))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (expenseId == null) {
			if (other.expenseId != null)
				return false;
		} else if (!expenseId.equals(other.expenseId))
			return false;
		if (modeOfPayment == null) {
			if (other.modeOfPayment != null)
				return false;
		} else if (!modeOfPayment.equals(other.modeOfPayment))
			return false;
		if (paidAmount == null) {
			if (other.paidAmount != null)
				return false;
		} else if (!paidAmount.equals(other.paidAmount))
			return false;
		if (rtgsDate == null) {
			if (other.rtgsDate != null)
				return false;
		} else if (!rtgsDate.equals(other.rtgsDate))
			return false;
		if (saltKey == null) {
			if (other.saltKey != null)
				return false;
		} else if (!saltKey.equals(other.saltKey))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
	
	
}
