package com.raybiztech.projectmanagement.invoice.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.dto.MilestoneDTO;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.raybiztech.projectmanagement.invoice.business.LineItemAudit;

public class InvoiceAuditDto implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = -435315093185314140L;

	private Long id;

	private String number;

	private String clientname;
	private String projectName;
	private String invoiceDuration;
	private String invoiceCount;
	private String invoiceRate;
	private String country;
	private String bankName;
	private String currencyType;
	private String wireTransferInstructions;
	private String notes;
	private String discountType;
	private String discountRate;
	private String discount;
	private String taxType;
	private String taxRate;
	private String tax;
	private String paymentTerm;
	private String invoiceDate;
	private String dueDate;
	private String invoiceAmountReceviedDate;
	private String invoiceStatus;
	private String subTotal;
	private String amount;
	private String totalAmount;
	private String finalAmount;
	private String amountAfterDiscount;
	private Set<LineItemDTO> lineitems;
	private Set<TaxDTO> taxDTOs;
	private Set<ReceivedInvoiceAmountDTO> amountDTOs;
	private Long invoiceId;
	private String modifiedDate;
	private String modifiedBy;
	private String persistType;
	private String milestoneName;
	private String projectManager;
	private String projectType;
	private String projectStatus;
	private String invoiceSentDate;
	private String statusNotes;
	private Boolean adjustedInvoice;
	private Long rate;
	private String conversionRate;
	private String receivedAmount;
	private String balanceAmount;
	private Boolean invoiceNumberFlag;
	private String invoiceNumber;
	private String invoicePattern;
	private String invoiceReferenceNumber;
	private Double totalTaxAmount;
	private String tdsAmount;
	private String netAmount;
	private String clientCountry;
	private String modifiedMilestoneName;
	private Boolean proformaInvoiceFlag;
	private Invoice proformaReferenceNo;
	

	

	private String saltKey;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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

	public String getInvoiceDuration() {
		return invoiceDuration;
	}

	public void setInvoiceDuration(String invoiceDuration) {
		this.invoiceDuration = invoiceDuration;
	}

	public String getInvoiceCount() {
		return invoiceCount;
	}

	public void setInvoiceCount(String invoiceCount) {
		this.invoiceCount = invoiceCount;
	}

	public String getInvoiceRate() {
		return invoiceRate;
	}

	public void setInvoiceRate(String invoiceRate) {
		this.invoiceRate = invoiceRate;
	}

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

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
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

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getInvoiceAmountReceviedDate() {
		return invoiceAmountReceviedDate;
	}

	public void setInvoiceAmountReceviedDate(String invoiceAmountReceviedDate) {
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

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
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

	public Set<LineItemDTO> getLineitems() {
		return lineitems;
	}

	public void setLineitems(Set<LineItemDTO> lineitems) {
		this.lineitems = lineitems;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getPersistType() {
		return persistType;
	}

	public void setPersistType(String persistType) {
		this.persistType = persistType;
	}

	public String getSaltKey() {
		return saltKey;
	}

	public void setSaltKey(String saltKey) {
		this.saltKey = saltKey;
	}

	public String getMilestoneName() {
		return milestoneName;
	}

	public void setMilestoneName(String milestoneName) {
		this.milestoneName = milestoneName;
	}

	public String getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public String getInvoiceSentDate() {
		return invoiceSentDate;
	}

	public void setInvoiceSentDate(String invoiceSentDate) {
		this.invoiceSentDate = invoiceSentDate;
	}

	public String getStatusNotes() {
		return statusNotes;
	}

	public void setStatusNotes(String statusNotes) {
		this.statusNotes = statusNotes;
	}

	public Set<TaxDTO> getTaxDTOs() {
		return taxDTOs;
	}

	public void setTaxDTOs(Set<TaxDTO> taxDTOs) {
		this.taxDTOs = taxDTOs;
	}

	public Set<ReceivedInvoiceAmountDTO> getAmountDTOs() {
		return amountDTOs;
	}

	public void setAmountDTOs(Set<ReceivedInvoiceAmountDTO> amountDTOs) {
		this.amountDTOs = amountDTOs;
	}

	public Boolean getAdjustedInvoice() {
		return adjustedInvoice;
	}

	public void setAdjustedInvoice(Boolean adjustedInvoice) {
		this.adjustedInvoice = adjustedInvoice;
	}

	public String getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(String finalAmount) {
		this.finalAmount = finalAmount;
	}

	public Long getRate() {
		return rate;
	}

	public void setRate(Long rate) {
		this.rate = rate;
	}

	public String getConversionRate() {
		return conversionRate;
	}

	public void setConversionRate(String conversionRate) {
		this.conversionRate = conversionRate;
	}

	public String getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(String receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public String getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(String balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public Boolean getInvoiceNumberFlag() {
		return invoiceNumberFlag;
	}

	public void setInvoiceNumberFlag(Boolean invoiceNumberFlag) {
		this.invoiceNumberFlag = invoiceNumberFlag;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getInvoicePattern() {
		return invoicePattern;
	}

	public void setInvoicePattern(String invoicePattern) {
		this.invoicePattern = invoicePattern;
	}

	public String getInvoiceReferenceNumber() {
		return invoiceReferenceNumber;
	}

	public void setInvoiceReferenceNumber(String invoiceReferenceNumber) {
		this.invoiceReferenceNumber = invoiceReferenceNumber;
	}

	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
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

	public String getClientCountry() {
		return clientCountry;
	}

	public void setClientCountry(String clientCountry) {
		this.clientCountry = clientCountry;
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

	public Invoice getProformaReferenceNo() {
		return proformaReferenceNo;
	}

	public void setProformaReferenceNo(Invoice proformaReferenceNo) {
		this.proformaReferenceNo = proformaReferenceNo;
	}
	
	
	
	
	
	
	
	
	

	
	
	

}
