package com.raybiztech.projectmanagement.invoice.business;

import java.io.Serializable;
import java.util.Set;

import com.raybiztech.projectmanagement.business.ChangeRequest;
import com.raybiztech.projectmanagement.business.Client;
import com.raybiztech.projectmanagement.business.Project;

public class InvoiceSummary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2102364156425893794L;

	private Long id;
	private Client client;
	private Project project;
	private ChangeRequest changeRequest;
	private String totalAmount;
	private String sentAmount;
	private Set<SentInvoiceSummary> sentInvoices;
	private String receivedAmount;
	private Set<ReceivedInvoiceSummary> receivedinvoices;
	private String pendingAmount;
	private String currency;
	private String saltKey;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public ChangeRequest getChangeRequest() {
		return changeRequest;
	}

	public void setChangeRequest(ChangeRequest changeRequest) {
		this.changeRequest = changeRequest;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getSentAmount() {
		return sentAmount;
	}

	public void setSentAmount(String sentAmount) {
		this.sentAmount = sentAmount;
	}

	public Set<SentInvoiceSummary> getSentInvoices() {
		return sentInvoices;
	}

	public void setSentInvoices(Set<SentInvoiceSummary> sentInvoices) {
		this.sentInvoices = sentInvoices;
	}

	public Set<ReceivedInvoiceSummary> getReceivedinvoices() {
		return receivedinvoices;
	}

	public void setReceivedinvoices(Set<ReceivedInvoiceSummary> receivedinvoices) {
		this.receivedinvoices = receivedinvoices;
	}

	public String getPendingAmount() {
		return pendingAmount;
	}

	public void setPendingAmount(String pendingAmount) {
		this.pendingAmount = pendingAmount;
	}

	public String getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(String receivedAmount) {
		this.receivedAmount = receivedAmount;
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

}
