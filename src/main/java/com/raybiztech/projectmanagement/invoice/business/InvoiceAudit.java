package com.raybiztech.projectmanagement.invoice.business;

import java.io.Serializable;
import java.util.Set;

import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

public class InvoiceAudit implements Serializable {
	/**
	 * Shaswat
	 */
	private static final long serialVersionUID = 8627146280558650379L;

	private Long id;
	//below invoiceNumber is serial number
	private String invoiceNumber;
	private String clientname;
	private String projectName;
	private String country;
	private String bankName;
	private String currencyType;
	private String wireTransferInstructions;
	private String notes;
	private String discountType;
	private String discountRate;
	private String discountInAmount;
	private String taxType;
	private String taxRate;
	private String taxAmount;
	private String milstoneTitle;
	private Date invoiceDate;
	private Date dueDate;
	private Date invoiceAmountReceviedDate;
	private String invoiceStatus;
	private String subTotal;
	private String invoiceAmount;
	private String totalAmount;
	private String amountAfterDiscount;
	private Set<LineItemAudit> lineitems;
	private Set<TaxAudit> taxAudits;
	private Set<ReceivedInvoiceAmountAudit> amountAudits;
	private Long invoiceId;
	private Second modifiedDate;
	private String modifiedBy;
	private String persistType;
	private String saltKey;
	private Date invoiceSentDate;
	private String statusNotes;
	private Boolean adjustedInvoice;
	private String conversionRate;
	private Boolean invoiceNumberFlag;
	//below number is invoiceNumber
	private String number;
	private String tdsAmount;
	private String netAmount;
	private String modifiedMilestoneName;
	private Boolean proformaInvoiceFlag;
	private Invoice proformaReferenceNo;
	
	
	
	
	
	
	

	public Invoice getProformaReferenceNo() {
		return proformaReferenceNo;
	}

	public void setProformaReferenceNo(Invoice proformaReferenceNo) {
		this.proformaReferenceNo = proformaReferenceNo;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getClientname() {
		return clientname;
	}

	public void setClientname(String clientname) {
		this.clientname = clientname;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/*
	 * public String getInvoiceDuration() { return invoiceDuration; }
	 * 
	 * public void setInvoiceDuration(String invoiceDuration) {
	 * this.invoiceDuration = invoiceDuration; }
	 * 
	 * public String getInvoiceCount() { return invoiceCount; }
	 * 
	 * public void setInvoiceCount(String invoiceCount) { this.invoiceCount =
	 * invoiceCount; }
	 * 
	 * public String getInvoiceRate() { return invoiceRate; }
	 * 
	 * public void setInvoiceRate(String invoiceRate) { this.invoiceRate =
	 * invoiceRate; }
	 */

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public String getWireTransferInstructions() {
		return wireTransferInstructions;
	}

	public void setWireTransferInstructions(String wireTransferInstructions) {
		this.wireTransferInstructions = wireTransferInstructions;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public String getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
	}

	public String getDiscountInAmount() {
		return discountInAmount;
	}

	public void setDiscountInAmount(String discountInAmount) {
		this.discountInAmount = discountInAmount;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public String getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getMilstoneTitle() {
		return milstoneTitle;
	}

	public void setMilstoneTitle(String milstoneTitle) {
		this.milstoneTitle = milstoneTitle;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getInvoiceAmountReceviedDate() {
		return invoiceAmountReceviedDate;
	}

	public void setInvoiceAmountReceviedDate(Date invoiceAmountReceviedDate) {
		this.invoiceAmountReceviedDate = invoiceAmountReceviedDate;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getAmountAfterDiscount() {
		return amountAfterDiscount;
	}

	public void setAmountAfterDiscount(String amountAfterDiscount) {
		this.amountAfterDiscount = amountAfterDiscount;
	}

	/*
	 * public String getPaymentTerm() { return paymentTerm; }
	 * 
	 * public void setPaymentTerm(String paymentTerm) { this.paymentTerm =
	 * paymentTerm; }
	 */

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getPersistType() {
		return persistType;
	}

	public void setPersistType(String persistType) {
		this.persistType = persistType;
	}

	public Second getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Second modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSaltKey() {
		return saltKey;
	}

	public void setSaltKey(String saltKey) {
		this.saltKey = saltKey;
	}

	public Set<LineItemAudit> getLineitems() {
		return lineitems;
	}

	public void setLineitems(Set<LineItemAudit> lineitems) {
		this.lineitems = lineitems;
	}

	public Date getInvoiceSentDate() {
		return invoiceSentDate;
	}

	public void setInvoiceSentDate(Date invoiceSentDate) {
		this.invoiceSentDate = invoiceSentDate;
	}

	public String getStatusNotes() {
		return statusNotes;
	}

	public void setStatusNotes(String statusNotes) {
		this.statusNotes = statusNotes;
	}

	public Set<TaxAudit> getTaxAudits() {
		return taxAudits;
	}

	public void setTaxAudits(Set<TaxAudit> taxAudits) {
		this.taxAudits = taxAudits;
	}

	public Set<ReceivedInvoiceAmountAudit> getAmountAudits() {
		return amountAudits;
	}

	public void setAmountAudits(Set<ReceivedInvoiceAmountAudit> amountAudits) {
		this.amountAudits = amountAudits;
	}

	public Boolean getAdjustedInvoice() {
		return adjustedInvoice;
	}

	public void setAdjustedInvoice(Boolean adjustedInvoice) {
		this.adjustedInvoice = adjustedInvoice;
	}

	public String getConversionRate() {
		return conversionRate;
	}

	public void setConversionRate(String conversionRate) {
		this.conversionRate = conversionRate;
	}

	public Boolean getInvoiceNumberFlag() {
		return invoiceNumberFlag;
	}

	public void setInvoiceNumberFlag(Boolean invoiceNumberFlag) {
		this.invoiceNumberFlag = invoiceNumberFlag;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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

	public String getModifiedMilestoneName() {
		return modifiedMilestoneName;
	}

	public void setModifiedMilestoneName(String modifiedMilestoneName) {
		this.modifiedMilestoneName = modifiedMilestoneName;
	}

	public Boolean getProformaInvoiceFlag() {
		return proformaInvoiceFlag;
	}

	public void setProformaInvoiceFlag(Boolean proformaInvoiceFlag) {
		this.proformaInvoiceFlag = proformaInvoiceFlag;
	}

	
	
	

	
	

}
