package com.raybiztech.projectmanagement.invoice.business;


public enum InvoiceStatus {
	RAISED("Raised"), SENT("Sent"), OVERDUE("Overdue"),RECEIVED("Received"),NOTSENT("Notsent");
	private	String  invoiceStatus;
	private InvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getTaxType() {
		return invoiceStatus;
	}

}
