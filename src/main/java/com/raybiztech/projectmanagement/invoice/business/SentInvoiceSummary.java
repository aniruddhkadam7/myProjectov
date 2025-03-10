package com.raybiztech.projectmanagement.invoice.business;

import java.io.Serializable;

public class SentInvoiceSummary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4310037781465439024L;

	private Long id;
	private Long sentInvoiceId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSentInvoiceId() {
		return sentInvoiceId;
	}

	public void setSentInvoiceId(Long sentInvoiceId) {
		this.sentInvoiceId = sentInvoiceId;
	}

}
