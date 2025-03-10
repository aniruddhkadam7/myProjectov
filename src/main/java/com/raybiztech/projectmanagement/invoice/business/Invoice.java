/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.projectmanagement.invoice.business;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.raybiztech.date.Date;
import com.raybiztech.projectmanagement.business.Milestone;

/**
 *
 * @author naresh
 */
public class Invoice implements Serializable, Cloneable {

	private static final long serialVersionUID = -4188595605057303402L;
	private Long id;
	// below number is serial  invoice number
	private String number;
	private String referenceNumber;
	private Milestone milsestone;
	private String country;
	private Remittance remittance;
	private String notes;
	private Boolean showNotesOnInvoice;
	private Set<LineItem> lineItems;
	private String subTotal;
	private String amount;
	private String discountType;
	private String discountRate;
	private String discount;
	private String amountAfterDiscount;
	private Set<Tax> tax;
	private String totalAmount;
	private String paymentTerm;
	private Date invoiceDate;
	private Date dueDate;
	private Date invoiceAmountReceviedDate;
	private String invoiceStatus;
	private String invoiceFileName;
	private String countTypeToDisplay;
	private String percentage;
	private String saltKey;
	private Date invoiceAmountSentDate;
	private String poNumber;
	private String statusNotes;
	private String writeoffAmount;
	private String receivedAmount;
	private Set<ReceivedInvoiceAmount> receivedAmountList;
	private Boolean isAdjusted;
	private Boolean canBeAdjusted;
	private Boolean showTaxDetailsOnInvoice;
	private String companyAddress;
	private String companyName;
	private String conversionRate;
	private Boolean invoiceNumberFlag;
	private String invoiceNumber;
	private String invoiceReferenceNumber;
	private String tdsAmount;
	private String netAmount;
	private String modifiedMilestoneName;
	private Boolean proformaInvoiceFlag = false;
	private Invoice proformaReferenceNo;
	//private Boolean invoiceReopenFlag;
	

	public Invoice getProformaReferenceNo() {
		return proformaReferenceNo;
	}

	public void setProformaReferenceNo(Invoice proformaReferenceNo) {
		this.proformaReferenceNo = proformaReferenceNo;
	}

	public Boolean getProformaInvoiceFlag() {
		return proformaInvoiceFlag;
	}

	public void setProformaInvoiceFlag(Boolean proformaInvoiceFlag) {
		this.proformaInvoiceFlag = proformaInvoiceFlag;
	}

	public Invoice() {
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

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

	public Milestone getMilsestone() {
		return milsestone;
	}

	public void setMilsestone(Milestone milsestone) {
		this.milsestone = milsestone;
	}

	public Remittance getRemittance() {
		return remittance;
	}

	public void setRemittance(Remittance remittance) {
		this.remittance = remittance;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Set<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(Set<LineItem> lineItems) {
		this.lineItems = lineItems;
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

	public Set<Tax> getTax() {
		return tax;
	}

	public void setTax(Set<Tax> tax) {
		this.tax = tax;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
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

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getInvoiceFileName() {
		return invoiceFileName;
	}

	public void setInvoiceFileName(String invoiceFileName) {
		this.invoiceFileName = invoiceFileName;
	}

	public Date getInvoiceAmountReceviedDate() {
		return invoiceAmountReceviedDate;
	}

	public void setInvoiceAmountReceviedDate(Date invoiceAmountReceviedDate) {
		this.invoiceAmountReceviedDate = invoiceAmountReceviedDate;
	}

	public String getCountTypeToDisplay() {
		return countTypeToDisplay;
	}

	public void setCountTypeToDisplay(String countTypeToDisplay) {
		this.countTypeToDisplay = countTypeToDisplay;
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

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getSaltKey() {
		return saltKey;
	}

	public void setSaltKey(String saltKey) {
		this.saltKey = saltKey;
	}

	public Date getInvoiceAmountSentDate() {
		return invoiceAmountSentDate;
	}

	public void setInvoiceAmountSentDate(Date invoiceAmountSentDate) {
		this.invoiceAmountSentDate = invoiceAmountSentDate;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getStatusNotes() {
		return statusNotes;
	}

	public void setStatusNotes(String statusNotes) {
		this.statusNotes = statusNotes;
	}

	public String getWriteoffAmount() {
		return writeoffAmount;
	}

	public void setWriteoffAmount(String writeoffAmount) {
		this.writeoffAmount = writeoffAmount;
	}

	public Set<ReceivedInvoiceAmount> getReceivedAmountList() {
		return receivedAmountList;
	}

	public void setReceivedAmountList(
			Set<ReceivedInvoiceAmount> receivedAmountList) {
		this.receivedAmountList = receivedAmountList;
	}

	public String getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(String receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public Boolean getShowNotesOnInvoice() {
		return showNotesOnInvoice;
	}

	public void setShowNotesOnInvoice(Boolean showNotesOnInvoice) {
		this.showNotesOnInvoice = showNotesOnInvoice;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public Boolean getIsAdjusted() {
		return isAdjusted;
	}

	public void setIsAdjusted(Boolean isAdjusted) {
		this.isAdjusted = isAdjusted;
	}

	public Boolean getCanBeAdjusted() {
		return canBeAdjusted;
	}

	public void setCanBeAdjusted(Boolean canBeAdjusted) {
		this.canBeAdjusted = canBeAdjusted;
	}

	public Boolean getShowTaxDetailsOnInvoice() {
		return showTaxDetailsOnInvoice;
	}

	public void setShowTaxDetailsOnInvoice(Boolean showTaxDetailsOnInvoice) {
		this.showTaxDetailsOnInvoice = showTaxDetailsOnInvoice;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
	
	

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	

	public String getInvoiceReferenceNumber() {
		return invoiceReferenceNumber;
	}

	public void setInvoiceReferenceNumber(String invoiceReferenceNumber) {
		this.invoiceReferenceNumber = invoiceReferenceNumber;
	}

	/*public Boolean getInvoiceReopenFlag() {
		return invoiceReopenFlag;
	}

	public void setInvoiceReopenFlag(Boolean invoiceReopenFlag) {
		this.invoiceReopenFlag = invoiceReopenFlag;
	}*/

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

	@Override
	public int hashCode() {
		return new HashCodeBuilder(1967, 13).append(this.getId()).hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Invoice) {
			final Invoice invoice = (Invoice) obj;
			return new EqualsBuilder().append(this.getId(), invoice.getId())
					.isEquals();
		}
		return false;

	}
	

	@Override
	public String toString() {
		return "Invoice [id=" + id + ", number=" + number + ", milsestone="
				+ milsestone + ", remittance=" + remittance + ", notes="
				+ notes + ", lineItems=" + lineItems + ", subTotal=" + subTotal
				+ ", amount=" + amount + ", tax=" + tax + ", totalAmount="
				+ totalAmount + ", paymentTerm=" + paymentTerm
				+ ", invoiceDate=" + invoiceDate + ", dueDate=" + dueDate
				+ ", invoiceStatus=" + invoiceStatus + "]";
	}

}
