package com.raybiztech.projectmanagement.invoice.dto;

import java.io.Serializable;

public class InvoiceSummaryDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2515478830021727499L;

	private Long id;
	private String clientName;
	private String projectName;
	private String projectType;
	private String crName;
	private String totalAmount;
	private String sentAmount;
	private Long sentInvoiceCount;
	private String receivedAmount;
	private Long receivedInvoiceCount;
	private String pendingAmount;
	private String currency;
	private String saltKey;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getCrName() {
		return crName;
	}

	public void setCrName(String crName) {
		this.crName = crName;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Long getSentInvoiceCount() {
		return sentInvoiceCount;
	}

	public void setSentInvoiceCount(Long sentInvoiceCount) {
		this.sentInvoiceCount = sentInvoiceCount;
	}

	public String getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(String receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public Long getReceivedInvoiceCount() {
		return receivedInvoiceCount;
	}

	public void setReceivedInvoiceCount(Long receivedInvoiceCount) {
		this.receivedInvoiceCount = receivedInvoiceCount;
	}

	public String getPendingAmount() {
		return pendingAmount;
	}

	public void setPendingAmount(String pendingAmount) {
		this.pendingAmount = pendingAmount;
	}

	public String getSentAmount() {
		return sentAmount;
	}

	public void setSentAmount(String sentAmount) {
		this.sentAmount = sentAmount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSaltKey() {
		return saltKey;
	}

	public void setSaltKey(String saltKey) {
		this.saltKey = saltKey;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

}
