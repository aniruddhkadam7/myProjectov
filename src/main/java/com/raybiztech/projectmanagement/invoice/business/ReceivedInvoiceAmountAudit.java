package com.raybiztech.projectmanagement.invoice.business;

import java.io.Serializable;

import com.raybiztech.date.Date;

public class ReceivedInvoiceAmountAudit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8335566480404661537L;
	private Long id;
	private String receivedAmount;
	private Date receivedDate;
	private String saltkey;
	private String tdsAmount;
	private String netAmount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(String receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getSaltkey() {
		return saltkey;
	}

	public void setSaltkey(String saltkey) {
		this.saltkey = saltkey;
	}

	public String getTdsAmount() {
		return tdsAmount;
	}

	public void setTdsAmount(String tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

	public String getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(String netAmount) {
		this.netAmount = netAmount;
	}
	

}
