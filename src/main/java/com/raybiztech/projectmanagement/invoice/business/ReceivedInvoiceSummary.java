package com.raybiztech.projectmanagement.invoice.business;

import java.io.Serializable;

public class ReceivedInvoiceSummary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6107713434861299039L;
	private Long id;
	private Long receivedInvoiceId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReceivedInvoiceId() {
		return receivedInvoiceId;
	}

	public void setReceivedInvoiceId(Long receivedInvoiceId) {
		this.receivedInvoiceId = receivedInvoiceId;
	}

}
