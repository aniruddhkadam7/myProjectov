package com.raybiztech.projectmanagement.invoice.business;

import java.io.Serializable;

import com.raybiztech.date.Second;

public class InvoiceTracker implements Serializable {

	/**
	 * shashank
	 */
	private static final long serialVersionUID = -6228202129162032878L;

	private Long id;
	private Long invoiceId;
	private String totalInvoiceContent;
	private String onlybodyContent;
	private Long version;
	private Long createdBy;
	private Second createdTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Second getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Second createdTime) {
		this.createdTime = createdTime;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public String getTotalInvoiceContent() {
		return totalInvoiceContent;
	}

	public void setTotalInvoiceContent(String totalInvoiceContent) {
		this.totalInvoiceContent = totalInvoiceContent;
	}

	public String getOnlybodyContent() {
		return onlybodyContent;
	}

	public void setOnlybodyContent(String onlybodyContent) {
		this.onlybodyContent = onlybodyContent;
	}

}
