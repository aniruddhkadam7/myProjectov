/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.projectmanagement.invoice.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class InvoiceQueryDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1627776206483106821L;

	private Long invoiceId;
	//invoice  serial number
	private String invoicNumber;
	//invoice number
	private String number;
	private String poNumber;
	private String clientName;
	private Long clientId;
	private String contactPersonName;
	private String Country;
	private String projectName;
	private Long projectId;
	private String projectManager;
	private String deliveryManager;
	private String milestoneName;
	private String milestoneNumber;
	private Long milestoneId;
	private String milestonePercentage;
	private String milestonePlannedEndDate;
	private String milestoneActualEndDate;
	private String milestoneComments;
	private Long crId;
	private String crName;
	private String crDuration;
	private String invoiceStatus;
	private String raisedDate;
	private String dueDate;
	private String invoiceAmountReceviedDate;
	private BigDecimal totalAmount;
	private String subtotal;
	private String Notes;
	private Boolean showNotesOnInvoice;
	private String organization;
	private String companyAddress;
	private String currencyType;
	private String bankName;
	private String location;
	private String wireTransferInstructions;
	private String projectType;
	private Boolean genarateType;
	private String invoiceFileName;
	private String numberInWords;
	private String plannedDate;
	private String actualDate;
	private String amount;
	private String amountAfterDiscount;
	private String percentage;
	private String invoiceAmountSentDate;
	private String discountRate;
	private String discountType;
	private String discount;
	private String projectOrCRNumbers;
	private String statusNotes;
	private String customTextOnPDF;
	private Boolean invoiceDeletable;
	private Set<LineItemDTO> lineitem;
	private Set<ResourceRoleDto> roleDtos;
	private Set<TaxDTO> taxDTO;
	private Integer reminderSize;
	private String totalReceivedAmount;
	private String countTypeToDisplay;
	private List<String> roleAndNameCount;
	private List<String> resourceNames;
	private Boolean showTaxDetailsOnInvoice;
	private String onlybodyContent;
	private Long finalTotalAmount;
	private Long rate;
	private Long pendingAmount;
	private String compAddress;
	private String companyName;
	private String conversionRate;
	private String gstCode;
	private String billingContactPerson;
	private String billingContactPersonEmail;
	private String tdsAmount;
	private String netAmount;
	private String modifiedMilestoneName;
	private Boolean milestoneTypeFlag;
	private Boolean proformaInvoiceFlag;
	private String clientCountry;
	
	public Boolean getProformaInvoiceFlag() {
		return proformaInvoiceFlag;
	}

	public void setProformaInvoiceFlag(Boolean proformaInvoiceFlag) {
		this.proformaInvoiceFlag = proformaInvoiceFlag;
	}

	public String getBillingContactPerson() {
		return billingContactPerson;
	}

	public void setBillingContactPerson(String billingContactPerson) {
		this.billingContactPerson = billingContactPerson;
	}

	public String getBillingContactPersonEmail() {
		return billingContactPersonEmail;
	}

	public void setBillingContactPersonEmail(String billingContactPersonEmail) {
		this.billingContactPersonEmail = billingContactPersonEmail;
	}

	public String getOnlybodyContent() {
		return onlybodyContent;
	}

	public void setOnlybodyContent(String onlybodyContent) {
		this.onlybodyContent = onlybodyContent;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getInvoicNumber() {
		return invoicNumber;
	}

	public void setInvoicNumber(String invoicNumber) {
		this.invoicNumber = invoicNumber;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}

	public String getDeliveryManager() {
		return deliveryManager;
	}

	public void setDeliveryManager(String deliveryManager) {
		this.deliveryManager = deliveryManager;
	}

	public String getMilestoneName() {
		return milestoneName;
	}

	public void setMilestoneName(String milestoneName) {
		this.milestoneName = milestoneName;
	}

	public String getMilestoneNumber() {
		return milestoneNumber;
	}

	public void setMilestoneNumber(String milestoneNumber) {
		this.milestoneNumber = milestoneNumber;
	}

	public Long getMilestoneId() {
		return milestoneId;
	}

	public void setMilestoneId(Long milestoneId) {
		this.milestoneId = milestoneId;
	}

	public String getMilestonePercentage() {
		return milestonePercentage;
	}

	public void setMilestonePercentage(String milestonePercentage) {
		this.milestonePercentage = milestonePercentage;
	}

	public String getMilestonePlannedEndDate() {
		return milestonePlannedEndDate;
	}

	public void setMilestonePlannedEndDate(String milestonePlannedEndDate) {
		this.milestonePlannedEndDate = milestonePlannedEndDate;
	}

	public String getMilestoneActualEndDate() {
		return milestoneActualEndDate;
	}

	public void setMilestoneActualEndDate(String milestoneActualEndDate) {
		this.milestoneActualEndDate = milestoneActualEndDate;
	}

	public String getMilestoneComments() {
		return milestoneComments;
	}

	public void setMilestoneComments(String milestoneComments) {
		this.milestoneComments = milestoneComments;
	}

	public Long getCrId() {
		return crId;
	}

	public void setCrId(Long crId) {
		this.crId = crId;
	}

	public String getCrName() {
		return crName;
	}

	public void setCrName(String crName) {
		this.crName = crName;
	}

	public String getCrDuration() {
		return crDuration;
	}

	public void setCrDuration(String crDuration) {
		this.crDuration = crDuration;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getRaisedDate() {
		return raisedDate;
	}

	public void setRaisedDate(String raisedDate) {
		this.raisedDate = raisedDate;
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

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}

	public String getNotes() {
		return Notes;
	}

	public void setNotes(String notes) {
		Notes = notes;
	}

	public Boolean getShowNotesOnInvoice() {
		return showNotesOnInvoice;
	}

	public void setShowNotesOnInvoice(Boolean showNotesOnInvoice) {
		this.showNotesOnInvoice = showNotesOnInvoice;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getWireTransferInstructions() {
		return wireTransferInstructions;
	}

	public void setWireTransferInstructions(String wireTransferInstructions) {
		this.wireTransferInstructions = wireTransferInstructions;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public Boolean getGenarateType() {
		return genarateType;
	}

	public void setGenarateType(Boolean genarateType) {
		this.genarateType = genarateType;
	}

	public String getInvoiceFileName() {
		return invoiceFileName;
	}

	public void setInvoiceFileName(String invoiceFileName) {
		this.invoiceFileName = invoiceFileName;
	}

	public String getNumberInWords() {
		return numberInWords;
	}

	public void setNumberInWords(String numberInWords) {
		this.numberInWords = numberInWords;
	}

	public String getPlannedDate() {
		return plannedDate;
	}

	public void setPlannedDate(String plannedDate) {
		this.plannedDate = plannedDate;
	}

	public String getActualDate() {
		return actualDate;
	}

	public void setActualDate(String actualDate) {
		this.actualDate = actualDate;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
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

	public String getInvoiceAmountSentDate() {
		return invoiceAmountSentDate;
	}

	public void setInvoiceAmountSentDate(String invoiceAmountSentDate) {
		this.invoiceAmountSentDate = invoiceAmountSentDate;
	}

	public String getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getProjectOrCRNumbers() {
		return projectOrCRNumbers;
	}

	public void setProjectOrCRNumbers(String projectOrCRNumbers) {
		this.projectOrCRNumbers = projectOrCRNumbers;
	}

	public String getStatusNotes() {
		return statusNotes;
	}

	public void setStatusNotes(String statusNotes) {
		this.statusNotes = statusNotes;
	}

	public String getCustomTextOnPDF() {
		return customTextOnPDF;
	}

	public void setCustomTextOnPDF(String customTextOnPDF) {
		this.customTextOnPDF = customTextOnPDF;
	}

	public Boolean getInvoiceDeletable() {
		return invoiceDeletable;
	}

	public void setInvoiceDeletable(Boolean invoiceDeletable) {
		this.invoiceDeletable = invoiceDeletable;
	}

	public Set<LineItemDTO> getLineitem() {
		return lineitem;
	}

	public void setLineitem(Set<LineItemDTO> lineitem) {
		this.lineitem = lineitem;
	}

	public Set<ResourceRoleDto> getRoleDtos() {
		return roleDtos;
	}

	public void setRoleDtos(Set<ResourceRoleDto> roleDtos) {
		this.roleDtos = roleDtos;
	}

	public Set<TaxDTO> getTaxDTO() {
		return taxDTO;
	}

	public void setTaxDTO(Set<TaxDTO> taxDTO) {
		this.taxDTO = taxDTO;
	}

	public Integer getReminderSize() {
		return reminderSize;
	}

	public void setReminderSize(Integer reminderSize) {
		this.reminderSize = reminderSize;
	}

	public String getTotalReceivedAmount() {
		return totalReceivedAmount;
	}

	public void setTotalReceivedAmount(String totalReceivedAmount) {
		this.totalReceivedAmount = totalReceivedAmount;
	}

	public String getCountTypeToDisplay() {
		return countTypeToDisplay;
	}

	public void setCountTypeToDisplay(String countTypeToDisplay) {
		this.countTypeToDisplay = countTypeToDisplay;
	}

	public List<String> getRoleAndNameCount() {
		return roleAndNameCount;
	}

	public void setRoleAndNameCount(List<String> roleAndNameCount) {
		this.roleAndNameCount = roleAndNameCount;
	}

	public List<String> getResourceNames() {
		return resourceNames;
	}

	public void setResourceNames(List<String> resourceNames) {
		this.resourceNames = resourceNames;
	}

	public Boolean getShowTaxDetailsOnInvoice() {
		return showTaxDetailsOnInvoice;
	}

	public void setShowTaxDetailsOnInvoice(Boolean showTaxDetailsOnInvoice) {
		this.showTaxDetailsOnInvoice = showTaxDetailsOnInvoice;
	}

	public Long getFinalTotalAmount() {
		return finalTotalAmount;
	}

	public void setFinalTotalAmount(Long finalTotalAmount) {
		this.finalTotalAmount = finalTotalAmount;
	}

	public Long getRate() {
		return rate;
	}

	public void setRate(Long rate) {
		this.rate = rate;
	}

	public Long getPendingAmount() {
		return pendingAmount;
	}

	public void setPendingAmount(Long pendingAmount) {
		this.pendingAmount = pendingAmount;
	}

	public String getCompAddress() {
		return compAddress;
	}

	public void setCompAddress(String compAddress) {
		this.compAddress = compAddress;
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

	public String getGstCode() {
		return gstCode;
	}

	public void setGstCode(String gstCode) {
		this.gstCode = gstCode;
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

	public Boolean getMilestoneTypeFlag() {
		return milestoneTypeFlag;
	}

	public void setMilestoneTypeFlag(Boolean milestoneTypeFlag) {
		this.milestoneTypeFlag = milestoneTypeFlag;
	}

	public String getClientCountry() {
		return clientCountry;
	}

	public void setClientCountry(String clientCountry) {
		this.clientCountry = clientCountry;
	}
	
	
	
	
	
	
	

	
}